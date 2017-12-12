package sc.ustc.interceptor;

import java.lang.reflect.InvocationTargetException;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import sc.ustc.util.ClassReflector;
import sc.ustc.util.XmlParser;

public class ActionsExecutor implements Executor{
	private XmlParser parser;
	private String actionName;
	
	public ActionsExecutor(XmlParser parser,String actionName) {
		this.actionName = actionName;
		this.parser = parser;
	}
	
	public String executeAction() {
		//��һ��Ӧ�ñ�����
		String resultName = loadActionClassAndRunMethod(actionName);
		//����ִ�н������һ��ҲӦ�ñ�����
		String result = handleResult(actionName,resultName);
		//*****���Զ�̬����*****
		System.out.println("executeAction ��ִ��");
		//*****���Զ�̬����*****
		return result;
	}
	/** 
	  * loadActionClass TODO :����actionName��Ӧ��Action��,��ִ�ж�Ӧ�ķ���
	  * @param actionName
	  * @return
	  * @author zhiman
	  * @date 2017/12/05 ����10:19:29 
	  */
	private String loadActionClassAndRunMethod (String actionName) {
		Element element = null;
		String result = null;
		try {
			element = parser.matchAction(actionName);
			
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("loadActionClass����");
		}
		if ( element == null ) {
			throw new RuntimeException(actionName+"ƥ��ʧ�ܣ�");
		}
		String className = XmlParser.getAttributeValue(element, "class");
		String methodName = XmlParser.getAttributeValue(element, "method");
		System.out.println(methodName);
		//����Java������ظ��ಢִ�ж�Ӧ�ķ���
		try {
			result = ClassReflector.executeMethod(className, methodName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("�Ҳ����ࣺ"+className);
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
	
	private String handleResult(String parentActionName,String resultName) {
		Element element = null;
		try {
			element = parser.matchResult(parentActionName, resultName);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("handleResult����");
		}
		return XmlParser.getAttributeValue(element, "value");
	}
}
