package sc.ustc.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import org.dom4j.DocumentException;
//import org.dom4j.Element;

import sc.ustc.interceptor.ActionsExecutor;
import sc.ustc.interceptor.ActoinProxy;
import sc.ustc.interceptor.Executor;
import sc.ustc.util.Xml2Html;
//import sc.ustc.util.ClassReflector;
import sc.ustc.util.XmlParser;


/** 
  * @description SimpleController.java
  * @author Administrator
  * @date 2017/11/20
  * @version 1.0
  */
public class SimpleController extends HttpServlet {
	private static final long serialVersionUID = 1303998807532328377L;
	private static final String XML_FILE_NAME = "controller.xml";
	private XmlParser parser;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String xmlPath = SimpleController.class.getClassLoader().getResource(XML_FILE_NAME).getPath();
		parser = new XmlParser(xmlPath);
		String actionName = getActionName(request);
		System.out.println(actionName);
		//��һ��Ӧ�ñ�����
//		String resultName = loadActionClassAndRunMethod(actionName);
//		//����ִ�н������һ��ҲӦ�ñ�����
//		String result = handleResult(actionName,resultName);
		//Test start****
		String result = ProxyImplAssistant(actionName);
		//Test end*******
		System.out.println("****************************"+result);
		//�ж�result�Ƿ���"_view.xml"��β�����ǣ���ת��Html
		String html = translateResult(result);
		if ( html != null) {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write(html);
		} else {
			response.sendRedirect(result);
		}
	}
	private String translateResult(String result) {
		String rear = "_view.xml";
		result = result.trim();
		if ( result.endsWith( rear ) ) {
			//���result��Ӧ���ļ�·��
			String path = SimpleController.class.getClassLoader().getResource("../../").getPath();
			String xslFilePath = path + result;
			//���������׺Html�ļ�
			System.out.println(xslFilePath+"****************************"+path);
			File file = new File(xslFilePath);
			return Xml2Html.translateXml2Html(file);
		} else {
			return null;
		}
		
	}
	/** 
	  * ProxyImplAssistant TODO :
	  * @param actionName
	  * @return
	  * @author zhiman
	  * @date 2017/12/12 ����11:11:51 
	  */
	private String ProxyImplAssistant (String actionName) {
		Executor executor = new ActionsExecutor(parser, actionName);
		ActoinProxy h = new ActoinProxy(executor, parser, actionName);
		Class<?> cls = executor.getClass();
		Executor proxy = (Executor) Proxy.newProxyInstance(cls.getClassLoader(), 
				cls.getInterfaces(), h);
		String result = proxy.executeAction();
		return result;
	}
	
	/** 
	  * getActionName TODO :��������������ȡ��Ӧ��Action
	  * @param request
	  * @return
	  * @author zhiman
	  * @date 2017/12/07 ����11:21:08 
	  */
	private String getActionName(HttpServletRequest request) {
		String urlString=request.getRequestURI();
		//����getParameterMap() start
//		Map<String, String[]> map = request.getParameterMap();
//		if (map.size() != 0) {
//			Set<Entry<String, String[]>> entrySet = map.entrySet();
//			for(Entry<String, String[]> entry : entrySet){
//				System.out.println(entry.getKey()+"***"+entry.getValue()[0]);
//			}
//		}
		//����getParameterMap() end
		return urlString.substring(urlString.lastIndexOf("/")+1,urlString.indexOf(".sc"));
	}
//	private void setFieldValue( HttpServletRequest request, Class<?> cls ) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
//		//�õ������в�����-����ֵ
//		Map<String, String[]> map = request.getParameterMap();
//		//�õ�Action�����������г�Ա����
//		Field[] fields = cls.getDeclaredFields();
//		//����������в�����-����ֵ
//		if (map.size() != 0) {
//			Set<Entry<String, String[]>> entrySet = map.entrySet();
//			//���������в�����-����ֵ��Map
//			for(Entry<String, String[]> entry : entrySet){
//				//�õ������еĲ�����
//				String paraName = entry.getKey().trim();
//				//��Լ���ĸ�ʽ����object.filed����������
//				int position = paraName.indexOf(".");
//				if (position != paraName.lastIndexOf(".") || position == -1 ) {
//					throw new RuntimeException("URL����Я���Ĳ������Ʋ����ϡ�object.filed���Ĺ淶");
//				}
//				String objName = paraName.substring(0,position);
//				String fieldName = paraName.substring(position+1);
//				System.out.println(paraName +"***"+entry.getValue()[0]+"---"+fieldName);
//				//�ڵõ���Action�����������г�Ա������Ѱ��������ƥ��ĳ�Ա����
//				for (Field field : fields) {
//					//�õ���Ա��������
//					Class<?> beanClass = field.getType();
//					//�õ���Ա������������
//					String QualifyObjType = beanClass.getName().trim();
//					String objType = QualifyObjType.substring(QualifyObjType.lastIndexOf(".") + 1);
//					//�ж�Action�����������г�Ա�����������в����� �������Ƿ�ƥ��
//					if ( fieldName.equals( field.getName() ) && objName.equalsIgnoreCase(objType)) {
//						//���ƥ��
//						field.set(cls.newInstance(), beanClass.newInstance());
//					}
//				}
//			}
//		}
//	}
	
//	private String handleResult(String parentActionName,String resultName) {
//		Element element = null;
//		try {
//			element = parser.matchResult(parentActionName, resultName);
//		} catch (DocumentException e) {
//			e.printStackTrace();
//			throw new RuntimeException("handleResult����");
//		}
//		return parser.getAttributeValue(element, "value");
//	}

	/** 
	  * loadActionClass TODO :����actionName��Ӧ��Action��,��ִ�ж�Ӧ�ķ���
	  * @param actionName
	  * @return
	  * @author zhiman
	  * @date 2017/12/05 ����10:19:29 
	  */
//	private String loadActionClassAndRunMethod (String actionName) {
//		Element element = null;
//		String result = null;
//		try {
//			element = parser.matchAction(actionName);
//			
//		} catch (DocumentException e) {
//			e.printStackTrace();
//			throw new RuntimeException("loadActionClass����");
//		}
//		if ( element == null ) {
//			throw new RuntimeException(actionName+"ƥ��ʧ�ܣ�");
//		}
//		String className = parser.getAttributeValue(element, "class");
//		String methodName = parser.getAttributeValue(element, "method");
//		System.out.println(methodName);
//		//����Java������ظ��ಢִ�ж�Ӧ�ķ���
//		try {
//			result = ClassReflector.executeMethod(className, methodName);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//			System.out.println("�Ҳ����ࣺ"+className);
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}
