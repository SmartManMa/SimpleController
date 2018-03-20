package sc.ustc.interceptor;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import sc.ustc.util.ClassReflector;
import sc.ustc.util.XmlParser;

public class ActionsExecutor implements Executor {
	private static final String DI_NAME = "di.xml";
	private XmlParser parser;
	private String actionName;
	private static String fieldName;
	private Class<?> beancls;

	public ActionsExecutor(XmlParser parser, String actionName) {
		this.actionName = actionName;
		this.parser = parser;
	}

	// ----------executeAction�������ӿɱ����
	public String executeAction(Object... args) {
		// ��һ��Ӧ�ñ�����
		String resultName = loadActionClassAndRunMethod(actionName, args);
		// ����ִ�н������һ��ҲӦ�ñ�����
		String result = handleResult(actionName, resultName);
		// *****���Զ�̬����*****
		System.out.println("executeAction ��ִ��");
		// *****���Զ�̬����*****
		return result;
	}

	/**
	 * loadActionClass TODO :����actionName��Ӧ��Action��,��ִ�ж�Ӧ�ķ���
	 * @param actionName
	 * @return
	 * @author zhiman
	 * @date 2017/12/05 ����10:19:29
	 */
	private String loadActionClassAndRunMethod(String actionName, Object... args) {
		Element element = null;
		String result = null;
		try {
			element = parser.matchAction(actionName);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("loadActionClass����");
		}
		if (element == null) {
			throw new RuntimeException(actionName + "ƥ��ʧ�ܣ��޷�ʶ�������");
		}
		// ����e7 DI start
		else {// ����DI
			System.out.println(actionName);
			// di.xml ·��
			String diPath = ActionsExecutor.class.getClassLoader().getResource(DI_NAME).getPath();
			SAXReader reader = new SAXReader();

			try {
				Document document = reader.read(new File(diPath));
				Element rootElement = document.getRootElement();
				Element beanElement = XmlParser.matchElement("bean", "id", actionName, rootElement);
				Element fieldElement = beanElement.element("field");
				
				Element refBeanElement = null;
				if ( fieldElement != null ) {
					fieldName = XmlParser.getAttributeValue(fieldElement, "name");
					String refbean = XmlParser.getAttributeValue(fieldElement, "bean-ref");
					refBeanElement = XmlParser.matchElement("bean", "id", refbean, rootElement);
				}
				String refBeanName = XmlParser.getAttributeValue(refBeanElement, "class");
				//�õ�UserBean�����
				beancls = ClassReflector.gainClass(refBeanName);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		// ����e7 DI end
		String className = XmlParser.getAttributeValue(element, "class");
		String methodName = XmlParser.getAttributeValue(element, "method");
		System.out.println(methodName);
		// ����Java������ظ��ಢִ�ж�Ӧ�ķ���
		try {
			result = (String) runMethods(className,beancls, methodName, args);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("�Ҳ����ࣺ" + className);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return result;
	}

	private String handleResult(String parentActionName, String resultName) {
		Element element = null;
		try {
			element = parser.matchResult(parentActionName, resultName);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("handleResult����");
		}
		return XmlParser.getAttributeValue(element, "value");
	}
	
	/** 
	  * runMethods TODO :���÷�������ָ������
	  * @param className
	  * @param beanCls
	  * @param methodName
	  * @param args ��������
	  * @return
	  * @throws ClassNotFoundException
	  * @throws NoSuchMethodException
	  * @throws SecurityException
	  * @throws IllegalAccessException
	  * @throws IllegalArgumentException
	  * @throws InvocationTargetException
	  * @throws InstantiationException
	  * @author zhiman
	  * @date 2017/12/28 ����5:06:53 
	  */
	public static Object runMethods(String className,Class<?> beanCls,String methodName,Object...args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		Class<?> cls = ClassReflector.gainClass(className);
		Method method = cls.getMethod(methodName,HttpServletRequest.class,HttpServletResponse.class);
		Object obj = cls.newInstance();
		try {
			setProperty(obj,fieldName,beanCls.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return method.invoke(obj,args);
	}
	/** 
	  * setProperty TODO :ʹ����ʡ����ΪLoginAction��UserBean���ͳ�Ա������ֵ
	  * @param obj : LoginAction����
	  * @param fieldName:UserBean���ͳ�Ա��������
	  * @param fieldvalue:UserBean��
	  * @throws Exception
	  * @author zhiman
	  * @date 2017/12/28 ����5:03:49 
	  */
	private static void setProperty(Object obj , String fieldName,Object fieldvalue) throws Exception {     
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());     
        PropertyDescriptor[] proDescriptors = beanInfo.getPropertyDescriptors();     
        for(PropertyDescriptor prop: proDescriptors){     
          if(prop.getName().equals(fieldName)){     
            Method setMathod = prop.getWriteMethod();     
            setMathod.invoke(obj,fieldvalue); 
            break;     
          }     
       }     
   }   
}
