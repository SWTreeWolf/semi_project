package servletdemo.part03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class EmployeesDAO {
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private EmployeesDAO() {
		
	}
	
	private static EmployeesDAO dao =new EmployeesDAO();
	
	public static EmployeesDAO getInstance() {
		return dao;
	}
	
	private Connection init()
				throws ClassNotFoundException, SQLException{
		Class.forName("oracle.jdbc.OracleDriver");
		String url = "jdbc:oracle:thin://@127.0.0.1:1521:xe";
		String user = "hr";
		String password = "a1234";
		return DriverManager.getConnection(url, user, password);
	}
	
	private void exit() throws SQLException{
		if(rs !=null)
			rs.close();
		if(stmt !=null)
			stmt.close();
		if(pstmt !=null)
			pstmt.close();
		if(conn !=null)
			conn.close();
	}
	
	public List<EmployeesDTO> search(){
		List<EmployeesDTO> aList=new ArrayList<EmployeesDTO>();
		
		try {
			conn=init();
			stmt =conn.createStatement();
			String sql ="select * from employees order by employee_id";
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				EmployeesDTO dto =new EmployeesDTO( );
				dto.setEmployee_id(rs.getInt("employee_id"));
				dto.setFirst_name(rs.getString("first_name"));
				dto.setSalary(rs.getInt("salary"));
				aList.add(dto);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
				try {
					exit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			
		}
		
		return aList;
	}
	
}
