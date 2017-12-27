package sc.ustc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

public  class BaseDAO {

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

	// ��ɾ�Ĳ�
	public Object query(String sql) {
		CachedRowSet rowSet = null;
		try (	
				Connection c = getDBConnection(); 
				PreparedStatement ps = c.prepareStatement(sql);
			) {
			
			ResultSet rs = ps.executeQuery();
			
			//ResultSet�������ݿ����ӶϿ����޷�ʹ�ã�rowSet��������ʹ��
			RowSetFactory factory = RowSetProvider.newFactory();  
			rowSet = factory.createCachedRowSet(); 
			rowSet.populate(rs);
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return rowSet;
	};

	public boolean insert(String sql) {
		return false;
	};

	public boolean update(String sql) {
		return false;
	};

	public boolean delete(String sql) {
		return false;
	};

	// ��ɾ�Ĳ���д
	public Object query(PreparedStatement ps) {
		return ps;
	};

	public boolean insert(PreparedStatement ps) {
		return false;
	};

	public boolean update(PreparedStatement ps) {
		return false;
	};

	public boolean delete(PreparedStatement ps) {
		return false;
	};

	/**
	 * openDBConnection TODO :����ָ�����ݿ�
	 * 
	 * @return connection����
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @author zhiman
	 * @date 2017/12/23 ����3:46:27
	 */
	public Connection openDBConnection() throws ClassNotFoundException, SQLException {
		try {
			// ͨ��������ƻ��jdbc����
			Class.forName(deriver);
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException(deriver + "��Ӧ��JDBC����û���ҵ���");
		}
		return DriverManager.getConnection(url, userName, password);
	}

	/**
	 * openDBConnection TODO :***�������ļ���ȡ������Ϣ�������ݿ�
	 * 
	 * @param configMap
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @author zhiman
	 * @date 2017/12/26 ����3:08:46
	 */
	public Connection getDBConnection() throws ClassNotFoundException, SQLException {
		Map<String, String> configMap = new Configuration().getDBConfigMap();
		String deriver = configMap.get("driver_class");
		String url = configMap.get("url_path");
		String userName = configMap.get("db_username");
		String password = configMap.get("db_userpassword");
		try {
			// ͨ��������ƻ��jdbc����
			Class.forName(deriver);
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException(deriver + "��Ӧ��JDBC����û���ҵ���");
		}
		return DriverManager.getConnection(url, userName, password);
	}

	/**
	 * closeDBConnection TODO :�ر����ݿ�����
	 * 
	 * @param connection
	 *            connection����
	 * @return boolean true or false
	 * @throws SQLException
	 * @author zhiman
	 * @date 2017/12/23 ����3:59:26
	 */
	public boolean closeDBConnection(Connection connection) throws SQLException {
		if (connection != null) {
			connection.close();
		}
		return connection.isClosed();
	}

}
