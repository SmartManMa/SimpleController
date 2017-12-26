package sc.ustc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseDAO {
	
	protected String deriver;
	protected String url;
	protected String userName;
	protected String password;
	
	protected void setDeriver(String deriver) {
		this.deriver = deriver;
	}
	
	protected void setUrl(String url) {
		this.url = url;
	}
	
	protected void setUserName(String userName) {
		this.userName = userName;
	}
	
	protected void setPassword(String password) {
		this.password = password;
	}
	
	//��ɾ�Ĳ�
	public abstract Object query(String sql);
	
	public abstract boolean insert(String sql);
	
	public abstract boolean update(String sql);
	
	public abstract boolean delete(String sql);
	
	/** 
	  * openDBConnection TODO :����ָ�����ݿ�
	  * @return connection����
	  * @throws ClassNotFoundException
	  * @throws SQLException
	  * @author zhiman
	  * @date 2017/12/23 ����3:46:27 
	  */
	public Connection openDBConnection() throws ClassNotFoundException, SQLException{
		try {
			//ͨ��������ƻ��jdbc����
			Class.forName(deriver);
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException(deriver+"��Ӧ��JDBC����û���ҵ���");
		}
		return DriverManager.getConnection(url,userName,password);
	}
	
	/** 
	  * closeDBConnection TODO :�ر����ݿ�����
	  * @param connection connection����
	  * @return boolean true or false
	  * @throws SQLException
	  * @author zhiman
	  * @date 2017/12/23 ����3:59:26 
	  */
	public boolean closeDBConnection(Connection connection) throws SQLException{
		if ( connection != null ){
			connection.close();
		}
		return connection.isClosed();
	}


}
