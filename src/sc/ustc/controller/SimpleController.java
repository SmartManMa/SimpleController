package sc.ustc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/** 
  * @description SimpleController.java
  * @author Administrator
  * @date 2017/11/20
  * @version 1.0
  */
public class SimpleController extends HttpServlet {
	private static final long serialVersionUID = 1303998807532328377L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String html = getHtml();
		//������ӦHTML�ļ��ı����ʽ
		response.setContentType("text/html; charset=UTF-8");
		//��Ӧ���󲢷���html
		response.getWriter().print(html);
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
	private String getHtml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<title>SimpleController</title>");
		sb.append("<body>��ӭʹ��SimpleController��</body>");
		sb.append("</head>");
		sb.append("</html>");
		return sb.toString();
	}
}
