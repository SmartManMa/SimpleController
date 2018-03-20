package sc.ustc.util;

import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlParser {
	private static final String CTRL_NODE_NAME = "controller";
	private static final String INTERCEPTOR_NODE_TYPE = "interceptor";
	private static final String ACTION_NODE_NAME = "action";
	private static final String RESULT_NODE_NAME = "result";
	private static final String ATTR_NAME = "name";
	private static final String NONE_STR = "";
    private String XmlFilePath;
    
	public XmlParser(String XmlFilePath) {
		this.XmlFilePath = XmlFilePath;
	}

	public Element matchInterceptor (String interceptorName) throws DocumentException {
		//�õ�Document����
		Document document = getDocument(XmlFilePath);
		//��ȡXML���ڵ�
		Element root = document.getRootElement();
		return matchElement(INTERCEPTOR_NODE_TYPE ,interceptorName, root);
	}
	
	/** 
	  * matchAction TODO :����DOM4j����XML�ļ�,ƥ��ָ����actionName
	  * @param actionName xml�ĵ���action name = "actionName"
	  * @return actionName���ڽڵ�
	  * @throws DocumentException
	  * @author zhiman
	  * @date 2017/12/03 ����11:45:24 
	  */
	public Element matchAction(String actionName) throws DocumentException {
		//�õ�Document����
		Document document = getDocument(XmlFilePath);
		//��ȡXML���ڵ�
		Element root = document.getRootElement();
		//�õ�controller�ڵ�
		Element elementCtrl = root.element(CTRL_NODE_NAME);
		
		return matchElement(ACTION_NODE_NAME ,actionName, elementCtrl);
	}
	
	
	/** 
	  * matchAction TODO :����DOM4j����XML�ļ�,ƥ��ָ����parentActionName�����result name = resultName��result��� �����ظýڵ�
	  * @param parentActionName xml�ĵ���action name = "actionName"
	  * @param resultName result name = resultName
	  * @return result name = resultName��result�ڵ�
	  * @throws DocumentException
	  * @author zhiman
	  * @date 2017/12/03 ����11:45:24 
	  */
	public Element matchResult(String parentActionName, String resultName) throws DocumentException {
		//�õ�Document����
		Document document = getDocument(XmlFilePath);
		//��ȡXML���ڵ�
		Element root = document.getRootElement();
		//�õ�controller�ڵ�
		Element elementCtrl = root.element(CTRL_NODE_NAME);
		//�õ��ض���action�ڵ�
		Element elementAction = matchElement( ACTION_NODE_NAME ,parentActionName, elementCtrl);
		
		return matchElement(RESULT_NODE_NAME ,resultName, elementAction);
	}
	
	
	/** 
	  * matchElement TODO :��parentElementType���ͽڵ��У�ƥ����ΪelementName��elementType���͵�element�ڵ�
	  * @param elementType Ԫ�ؽ�����ͣ�����sc-configuration action result ��
	  * @param elementName Ԫ�ؽ�������  ��action name = "elementName"/��
	  * @param parentElementType ����ƥ��result action��result��ֱ������㣬parentElementType����action
	  * @return ��parentElementType���ͽڵ��У�ƥ����ΪelementName��elementType���͵�element�ڵ�
	  * @author zhiman
	  * @date 2017/12/05 ����5:21:10 
	  */
	public Element matchElement (String elementType, String elementName, Element parentElement ) {
		return matchElement(elementType,ATTR_NAME,elementName,parentElement);
	}
	
	/** 
	  * matchElement TODO :���ط���
	  * @param elementType
	  * @param attrType
	  * @param elementName
	  * @param parentElement
	  * @return
	  * @author zhiman
	  * @date 2017/12/27 ����10:26:06 
	  */
	public static Element matchElement (String elementType,String attrType, String attrName, Element parentElement ) {
		Element matchedElement = null;
		//��ȡparentElement�ڵ�������ֱ�ӽ���б�
		List<Element> elementList = getElementNodeList(parentElement,elementType);
		//�ж�parentElement�ڵ����Ƿ��нڵ�
		if ( elementList.size() == 0 ) {
			throw new RuntimeException(parentElement.getName()+"�ڵ�����û�нڵ�");
		}
		//��������elementList
		for (Element element : elementList) {
			//ȥ�������elementNameǰ��ո�
			attrName = attrName.trim();
			//�õ�name���Զ�Ӧ��ֵ
			String attrValue = getAttributeValue(element,attrType);
			//ƥ��
			if ( NONE_STR.equals( attrName ) ) {
				throw new RuntimeException("attrName����Ϊȫ�ո�");
			} else if ( attrName.equals( attrValue ) ) {
				matchedElement = element;
				break;
			}
		}
		//����������elementList,û���ҵ�ƥ��elementName��element
		if (matchedElement == null) {
			throw new RuntimeException( attrName + "����ƥ�䵽���ʵ�" + elementType );
		}
		return matchedElement;
	}
	
	/** 
	  * getAttributeValue TODO :�������������õ���Ӧ������ֵ���硶action name = ��actionName�� type = "actionType"����/action��
	  * @param element Ԫ�ؽ��,�磺action
	  * @param attrName ����������name��type
	  * @return ����ֵ �磺��actionName����"actionType"
	  * @author zhiman
	  * @date 2017/12/03 ����9:58:45 
	  */
	public static String getAttributeValue( Element element , String attrName) {
		Attribute attribute = element.attribute(attrName.trim());
		if(attribute == null) {
			throw new RuntimeException("Ԫ�ؽڵ�"+element.getName()+"���Ҳ���");
		}
		return attribute.getValue().trim();
	}
	
	/** 
	  * getNodeContent TODO :���ء�action��text��/action���е�text
	  * @param element ��action���ڵ�
	  * @return ��action��text��/action���е�text
	  * @author zhiman
	  * @date 2017/12/03 ����12:40:56 
	  */
	public static String getNodeContent( Element element ) {
		return element.getTextTrim();
	}

	/** 
	  * getElementNodeList TODO : ��ȡnodeNameָ�����͵�list �硶outter����inner�� ...��/inner����inner�� ...��/inner��...��/outter��
	  * @param root ��ǰ�ڵ����һ��ڵ�,�硶outter��
	  * @param elementType Ԫ�ؽ������ �磺inner��
	  * @return elementTypeָ�����͵�list��������outter��...��/outter��������inner�ͽڵ�
	  * @author zhiman
	  * @date 2017/12/03 ����12:57:26 
	  */
	@SuppressWarnings("unchecked")
	private static List<Element> getElementNodeList(Element root, String elementType) {
		return  root.elements(elementType);
	}

	/** 
	  * getDocument TODO :�õ�Document����
	  * @param url XML�ļ�·��
	  * @return Document����
	  * @throws DocumentException
	  * @author zhiman
	  * @date 2017/12/03 ����12:40:24 
	  */
	private Document getDocument(String url) throws DocumentException {
		 SAXReader reader = new SAXReader();
	     Document document = reader.read(url);
	     return document;
	}
}
