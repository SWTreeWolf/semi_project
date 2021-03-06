package semi.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import semi.DTO.PersonDTO;
import semi.DTO.ReservationDTO;
import semi.DTO.ReserveViewpageDTO;
import semi.DTO.RoomDTO;

public class ReservationDAO {
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	
	private ReservationDAO() {
		
	}
	private static ReservationDAO dao= new ReservationDAO();
	
	public static ReservationDAO getInstance() {
		return dao;
	}
	
	private Connection init() throws ClassNotFoundException, SQLException{
		Class.forName("oracle.jdbc.OracleDriver");
		String url="jdbc:oracle:thin://@127.0.0.1:1521:xe";
		String user = "hr";
		String password="a1234";
		return DriverManager.getConnection(url,user,password);
	}//init
	
	private void exit() throws SQLException {
		if(rs!=null)
			rs.close();
		if(stmt!=null)
			stmt.close();
		if(pstmt!=null)
			pstmt.close();
		if(conn!=null)
			conn.close();
	}//exit
	
	
	public int getResTotal(String searchName) {
		int toto=0;
		try {
			conn=init();
			String sql="select count(*) from reservation";
			if(searchName!="") {
				sql+=" r, person p where r.l_tipNum=p.l_tipNum and p.p_name like ?";
				pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, "%"+searchName+"%");
				rs=pstmt.executeQuery();
			}else {
				stmt=conn.createStatement();
				rs=stmt.executeQuery(sql);
			}
			while(rs.next()) {
				toto=rs.getInt(1);
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				exit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return toto;
	}
	
	

	//빈방 가져오기 ^^
	public List<RoomDTO> getRoomList(String datIn,String datOut,int guests){
		List<RoomDTO> list=new LinkedList<RoomDTO>();
		try {
			conn=init();
			String sql="SELECT * FROM room WHERE r_num NOT IN (SELECT r_num" + 
					" FROM  reservation WHERE "+
		"(l_datein <= TO_DATE(?,'YYYY-MM-DD') AND l_dateout >= TO_DATE(?,'YYYY-MM-DD'))" + 
		" OR (l_datein < TO_DATE(?,'YYYY-MM-DD') AND l_dateout >= TO_DATE(?,'YYYY-MM-DD'))" + 
		" OR (l_datein >=TO_DATE(?,'YYYY-MM-DD') AND l_dateout <=TO_DATE(?,'YYYY-MM-DD')))" + 
		" and r_limitednumber>=?"; 
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, datIn);
			pstmt.setString(2, datIn);
			pstmt.setString(3, datOut);
			pstmt.setString(4, datOut);
			pstmt.setString(5, datIn);
			pstmt.setString(6, datOut);
			pstmt.setInt(7, guests);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				RoomDTO dto=new RoomDTO();
				dto.setR_num(rs.getInt("r_num"));
				dto.setR_name(rs.getString("r_name"));
				dto.setR_limitedNumber(rs.getInt("r_limitednumber"));
				dto.setR_contents(rs.getString("r_contents"));
				dto.setR_pay(rs.getInt("r_pay"));
				list.add(dto);
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
		
		return list;
	}//getRoom
	
	public RoomDTO getRoom(int r_num) {
		RoomDTO dto=new RoomDTO();
		try {
			conn=init();
			String sql="select * from room where r_num=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, r_num);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				dto.setR_contents(rs.getString("r_contents"));
				dto.setR_limitedNumber(rs.getInt("r_limitedNumber"));
				dto.setR_name(rs.getString("r_name"));
				dto.setR_num(rs.getInt("r_num"));
				dto.setR_pay(rs.getInt("r_pay"));
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
		
		return dto;
	}//getRoom
	
	public int insertR(ReservationDTO rdto) {
		int result=0;
			try {
				conn=init();
				String sql="insert into reservation values"
						+ "(reserv_seq.nextval,?,?,?,?,?,?,?)";
				pstmt=conn.prepareStatement(sql);
				pstmt.setDate(1, rdto.getL_dateIn());
				pstmt.setDate(2, rdto.getL_dateOut());
				pstmt.setInt(3, rdto.getP_num());
				pstmt.setInt(4, rdto.getR_num());
				pstmt.setInt(5, rdto.getL_tipNum());
				pstmt.setInt(6, rdto.getR_total());
				pstmt.setInt(7, rdto.getYes());
				result=pstmt.executeUpdate();
				} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}finally {
				try {
					exit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		return result;
	}//insertR end
	public int insertP(PersonDTO pdto) {
		int p_num=-1;
		try {
			conn=init();
			String sql="insert into person values"
					+ "(?,person_seq.nextval,?,?,?,?,?,?)";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, pdto.getP_name());
			pstmt.setString(2, pdto.getP_phoneNumber());
			pstmt.setString(3, pdto.getP_email());
			pstmt.setString(4, pdto.getP_contents());
			pstmt.setInt(5, pdto.getL_tipNum());
			pstmt.setString(6, pdto.getP_address());
			pstmt.setString(7, pdto.getP_birth());
			pstmt.executeUpdate();
			
			String sql2="select * from person where l_tipNum=?";
			pstmt=conn.prepareStatement(sql2);
			pstmt.setInt(1, pdto.getL_tipNum());
			rs=pstmt.executeQuery();
			while(rs.next()) {
				p_num=rs.getInt("p_num");
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
		return p_num;
	}//end insertP
	
	public PersonDTO getPerson(int l_tipNum) {
		PersonDTO dto=new PersonDTO();
		try {
			conn=init();
			String sql="select * from person where l_tipnum = ?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, l_tipNum);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				dto.setL_tipNum(rs.getInt("l_tipNum"));
				dto.setP_address(rs.getString("p_address"));
				dto.setP_birth(rs.getString("p_birth"));
				dto.setP_contents(rs.getString("p_contents"));
				dto.setP_email(rs.getString("p_email"));
				dto.setP_name(rs.getString("p_name"));
				dto.setP_num(rs.getInt("p_num"));
				dto.setP_phoneNumber(rs.getString("p_phonenumber"));
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dto;
	}//end getperson
	
	public ReservationDTO getRes(int l_tipNum) {
		ReservationDTO dto=new ReservationDTO();
		try {
			conn=init();
			String sql="select * from reservation where l_tipnum = ?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, l_tipNum);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				dto.setL_dateIn(rs.getDate("l_datein"));
				dto.setL_dateOut(rs.getDate("l_dateout"));
				dto.setL_num(rs.getInt("l_num"));
				dto.setL_tipNum(rs.getInt("l_tipnum"));
				dto.setP_num(rs.getInt("p_num"));
				dto.setR_num(rs.getInt("r_num"));
				dto.setR_total(rs.getInt("r_total"));
				dto.setYes(rs.getInt("yes"));
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dto;
	}//end getRes
	

	public void deleteReservationMethod(String[] reserveListChk) {
		try {
			conn=init( );
			String sql="delete from Reservation where l_tipNum=?";
			pstmt=conn.prepareStatement(sql);
			for(int i=0; i<reserveListChk.length; i++){
				pstmt.setInt(1, Integer.parseInt(reserveListChk[i]));
				pstmt.addBatch();
			}
			pstmt.executeBatch();			
			
		} catch (ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
		}finally {
			try {
				exit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}//end deleteMethod()	
	
	public void deletePersonMethod(String[] reserveListChk) {
		try {
			conn=init( );
			String sql="delete from person where l_tipNum=?";
			pstmt=conn.prepareStatement(sql);
			for(int i=0; i<reserveListChk.length; i++){
				pstmt.setInt(1, Integer.parseInt(reserveListChk[i]));
				pstmt.addBatch();
			}
			pstmt.executeBatch();			
			
		} catch (ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
		}finally {
			try {
				exit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}//end deleteMethod()	
	
	
	
	public void yesUpdate(int l_num) {
    	try {
			conn=init();
			String sql="update reservation set yes=? where l_num=?";	
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 1);
		    pstmt.setInt(2, l_num);
		    pstmt.executeUpdate();
    	}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				exit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	
    }
	
	public List<ReservationDTO> getResList(){
		List<ReservationDTO> list =new ArrayList<ReservationDTO>();
		try {
			conn=init();	
			String sql = "select r.*,p.p_name from reservation r, person p where r.l_tipNum=p.l_tipNum";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ReservationDTO dto = new ReservationDTO();
				dto.setL_num(rs.getInt("l_num"));
				dto.setL_dateIn(rs.getDate("l_dateIn"));
				dto.setL_dateOut(rs.getDate("l_dateout"));
				dto.setP_num(rs.getInt("p_num"));
				dto.setP_name(rs.getString("p_name"));
				dto.setL_tipNum(rs.getInt("l_tipnum"));
				dto.setR_num(rs.getInt("r_num"));
				dto.setR_total(rs.getInt("r_total"));
				dto.setYes(rs.getInt("yes"));
				list.add(dto);
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
		return list;
	}//get ResList
	
	public List<ReservationDTO> getResList(ReserveViewpageDTO rvpdto){
		List<ReservationDTO> list =new ArrayList<ReservationDTO>();
		try {
			conn=init();	
			String sql = "select b.* from (select rownum ro,a.* from "+
			"(select r.*,p.p_name from reservation r, person p where r.l_tipNum=p.l_tipNum order by r.l_num desc)a)b"+
			" where ro>=? and ro<=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, rvpdto.getFirstW());
			pstmt.setInt(2, rvpdto.getLastW());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ReservationDTO dto = new ReservationDTO();
				dto.setL_num(rs.getInt("l_num"));
				dto.setL_dateIn(rs.getDate("l_dateIn"));
				dto.setL_dateOut(rs.getDate("l_dateout"));
				dto.setP_num(rs.getInt("p_num"));
				dto.setP_name(rs.getString("p_name"));
				dto.setL_tipNum(rs.getInt("l_tipnum"));
				dto.setR_num(rs.getInt("r_num"));
				dto.setR_total(rs.getInt("r_total"));
				dto.setYes(rs.getInt("yes"));
				list.add(dto);
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
		return list;
	}//get ResList
	
	public List<ReservationDTO> searchList(String p_name,ReserveViewpageDTO rvpdto){
		List<ReservationDTO> list =new ArrayList<ReservationDTO>();
		try {
			conn=init();
			String sql = "select b.* from (select rownum ro,a.* from (select r.*,p.p_name from reservation r," + 
					" person p where r.l_tipNum=p.l_tipNum and p.p_name like ? order by r.l_num desc)a)b" + 
					" where ro>=? and ro<=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+p_name+"%");
			pstmt.setInt(2, rvpdto.getFirstW());
			pstmt.setInt(3, rvpdto.getLastW());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ReservationDTO dto = new ReservationDTO();
				dto.setL_num(rs.getInt("l_num"));
				dto.setL_dateIn(rs.getDate("l_dateIn"));
				dto.setL_dateOut(rs.getDate("l_dateout"));
				dto.setP_num(rs.getInt("p_num"));
				dto.setP_name(rs.getString("p_name"));
				dto.setL_tipNum(rs.getInt("l_tipnum"));
				dto.setR_num(rs.getInt("r_num"));
				dto.setR_total(rs.getInt("r_total"));
				dto.setYes(rs.getInt("yes"));
				list.add(dto);
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
		return list;
	}//end searchList
}//end dao
