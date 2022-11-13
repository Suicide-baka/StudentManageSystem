package studentManageSystem.com.zse.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil{
	
	private String dbUrl = "jdbc:mysql://localhost:3306/informanagesystem?characterEncoding=utf8&useSSL=true";
	private String dbUsername = "root";
	private String dbPassword = "root";
	private String jdbcName = "com.mysql.jdbc.Driver";
	public Connection getCon() throws Exception{
		Class.forName(jdbcName);
		Connection con = DriverManager.getConnection(dbUrl,dbUsername,dbPassword);
		return con;
	}
	public void closeCon(Connection con) throws SQLException{
		if(con!=null){
			con.close();
		}
	}
}
