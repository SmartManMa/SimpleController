package sc.ustc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import sc.ustc.scinterface.Action;
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
//		String html = getHtml();
//		//������ӦHTML�ļ��ı����ʽ
//		response.setContentType("text/html; charset=UTF-8");
//		//��Ӧ���󲢷���html
//		response.getWriter().print(html);
		//������
		String xmlPath = SimpleController.class.getClassLoader().getResource(XML_FILE_NAME).getPath();
		parser = new XmlParser(xmlPath);
//      String xmlPath = this.getServletContext().getRealPath(XML_FILE_PATH);		
//		System.out.println("����һ"+xmlPath);
//		//���Զ�
//		System.out.println(this.getServletContext().getRealPath("����er"+"/"));
//		
//		System.out.println(pp);
		
		String urlString=request.getRequestURI();
		String path =  request.getRequestURI() ;
		String actionName = path.substring(urlString.lastIndexOf("/")+1,urlString.indexOf(".sc"));;
		System.out.println(actionName);
		//�õ�actionName��Ӧ����
		Action action = loadActionClass (actionName);
		//תȥִ��action.execute
		
		String resultName = action.execute(request, response);
		//����ִ�н��
		String result = handleResult(actionName,resultName);
		response.sendRedirect(result);

	}
	private String handleResult(String parentActionName,String resultName) {
		Element element = null;
		try {
			element = parser.matchResult(parentActionName, resultName);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("handleResult����");
		}
		return parser.getAttributeValue(element, "value");
	}

	/** 
	  * loadActionClass TODO :���ض�Ӧ��Action��
	  * @param actionName
	  * @return
	  * @author zhiman
	  * @date 2017/12/05 ����10:19:29 
	  */
	private Action loadActionClass (String actionName) {
		Action action = null;
		Element element = null;
		try {
			element = parser.matchAction(actionName);
			
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("loadActionClass����");
		}
		if ( element == null ) {
			throw new RuntimeException(actionName+"ƥ��ʧ�ܣ�");
		}
		String className = parser.getAttributeValue(element, "class");
		//����Java������ظ���
		
		try {
			Class<?> cls = Class.forName(className);
			action = (Action) cls.newInstance();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("�Ҳ����ࣺ"+className);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return action;
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	/** 
	  * getHtml TODO :���������ص�HTML
	  * @return HTML
	  * @author zhiman
	  * @date 2017/11/20 ����8:10:04 
	  */
//	private String getHtml() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("<html>");
//		sb.append("<head>");
//		sb.append("<title>SimpleController</title>");
//		sb.append("<body>��ӭʹ��SimpleController��</body>");
//		sb.append("</head>");
//		sb.append("</html>");
//		return sb.toString();
//	}
}
