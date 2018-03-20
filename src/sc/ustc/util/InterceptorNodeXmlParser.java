package sc.ustc.util;

import org.dom4j.DocumentException;
import org.dom4j.Element;

public class InterceptorNodeXmlParser {
	private static final String INTERCEPTOR_REF_NODE_TYPE = "interceptor-ref";
	private XmlParser parser;
	private String actionName;
	private Element InterceptorRefElement;

	public InterceptorNodeXmlParser(XmlParser parser, String actionName) {
		this.actionName = actionName;
		this.parser = parser;
	}

	/**
	 * getInterceptorRefElement TODO :
	 * 
	 * @param interceptorRefName
	 * @return
	 * @author zhiman
	 * @date 2017/12/11 ����11:55:03
	 */
	public Element getInterceptorRefElement(String interceptorRefName) {
		try {
			Element parentElement = parser.matchAction(actionName);
			InterceptorRefElement = parser.matchElement(INTERCEPTOR_REF_NODE_TYPE, interceptorRefName, parentElement);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return InterceptorRefElement;
	}

	/**
	 * getInterceptorElement TODO : ����Interceptor Element
	 * 
	 * @param interceptorRefName
	 * @return ����Interceptor Element
	 * @author zhiman
	 * @date 2017/12/11 ����11:48:56
	 */
	public Element getInterceptorElement(String interceptorRefName) {
		Element interceptorElement = null;
		if (hasInterceptorRefElement(interceptorRefName)) {
			String interceptorName = XmlParser.getAttributeValue(InterceptorRefElement, "name");
			try {
				interceptorElement = parser.matchInterceptor(interceptorName);
			} catch (DocumentException e) {
				System.out.println("InterceptorNodeXmlParser������");
				e.printStackTrace();
			}
		}
		return interceptorElement;
	}

	/**
	 * hasInterceptorElement TODO : �жϡ�Action��������Ƿ��С�interceptor-ref�����
	 * 
	 * @return
	 * @author zhiman
	 * @date 2017/12/11 ����10:52:09
	 */
	public boolean hasInterceptorRefElement(String interceptorRefName) {

		if (getInterceptorRefElement(interceptorRefName) == null) {
			return false;
		}
		return true;
	}

	public boolean hasInterceptorElement(String interceptorRefName) {

		if (getInterceptorElement(interceptorRefName) == null) {
			return false;
		}
		return true;
	}

}
