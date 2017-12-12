package sc.ustc.interceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.dom4j.Element;
import sc.ustc.util.ClassReflector;
import sc.ustc.util.InterceptorNodeXmlParser;
import sc.ustc.util.XmlParser;

public class ActoinProxy implements InvocationHandler {
	// Ҫ����Ķ����������action����Ӧ����ActionsExecutor����
	private Object object;
	private String actionName;
	private XmlParser parser;
	private String className;
	private Element element;

	public ActoinProxy(Object object, XmlParser parser, String actionName) {
		this.actionName = actionName;
		this.parser = parser;
		this.object = object;
		init();
	}

	private void init() {
		InterceptorNodeXmlParser interceptor = new InterceptorNodeXmlParser(parser, actionName);
		// ��interceptor name = "log"/�����
		element = interceptor.getInterceptorElement("log");
		if (element != null) {
			className = XmlParser.getAttributeValue(element, "class");
		}
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		preAction();
		Object result = method.invoke(object, args);
		afterAction();
		return result;
	}

	// *****���Զ�̬����*****
	private void preAction() {
		if (element != null && className != null) {
			String perDoMethod = XmlParser.getAttributeValue(element, "predo");
			try {
				ClassReflector.executeMethod(className, perDoMethod);
			} catch (Exception e) {
				System.out.println("ActoinProxy�����");
				e.printStackTrace();
			}
		}
		//System.out.println("preAction ��ִ��");
	}

	private void afterAction() {
		if (element != null && className != null) {
			String afterDoMethod = XmlParser.getAttributeValue(element, "afterdo");
			try {
				//���� start
				Thread.sleep(1000);
				//���� end
				ClassReflector.executeMethod(className, afterDoMethod);
			} catch (Exception e) {
				System.out.println("ActoinProxy�����");
				e.printStackTrace();
			}
		}
		//System.out.println("afterAction ��ִ��");
	}
	// *****���Զ�̬����*****

}
