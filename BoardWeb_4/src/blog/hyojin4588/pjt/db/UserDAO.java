package blog.hyojin4588.pjt.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import blog.hyojin4588.pjt.vo.UserLoginHistoryVO;
import blog.hyojin4588.pjt.vo.UserVO;

public class UserDAO {
	
	public static int insUserLoginHistory(UserLoginHistoryVO param) {
		String sql = " INSERT INTO t_user_loginhistory(i_history, i_user, ip_addr, os, browser) SELECT SEQ_USERLOGINHISTORY.nextval, ?, ?, ?, ? FROM DUAL ";
		
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_user());
				ps.setNString(2, param.getIp_addr());
				ps.setNString(3, param.getOs());
				ps.setNString(4, param.getBrowser());
			}
		});
	}

	public static int insUser(UserVO param) {
		String sql = "INSERT INTO t_user"
				+ "(i_user, u_id, u_pw, u_nm, u_email) "
				+ "VALUES "
				+ "(seq_user.nextval, ?, ?, ?, ?) ";

		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() { // 인터페이스를 객체화한 것이 아니라 implements의 한 종류임
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setNString(1, param.getUser_id());
				ps.setNString(2, param.getUser_pw());
				ps.setNString(3, param.getUser_nm());
				ps.setNString(4, param.getEmail());
			}
		});
	}
	
	public static int login(UserVO param) {
		String sql = " SELECT i_user, u_id, u_pw, u_nm FROM t_user WHERE u_id = ? ";
		
		return JdbcTemplate.executeQuery(sql, new JdbcSelectInterface() { // 인터페이스를 객체화한 것이 아니라 implements의 한 종류임

			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
				ps.setNString(1, param.getUser_id());
			}

			@Override
			public int executeQuery(ResultSet rs) throws SQLException {
				if (rs.next()) {
					param.setUser_nm(rs.getNString("u_nm"));
					param.setI_user(rs.getInt("i_user"));
//					System.out.println("dd : " + param.getI_user());
					String pw = rs.getNString("u_pw");
//					System.out.println(pw);
					String mypw = param.getUser_pw();
//					System.out.println(mypw);
					if(pw.equals(mypw)) { // 문자를 == 비교하면 주소값 비교하므로 안됨
						return 1;
					} else {
						return 2;
					}
				}
				return 3;
			}
		});
	}
	
	public static UserVO selUser(int i_user) {
		String sql = " SELECT u_id, u_nm, profile_img, u_email, r_dt FROM t_user WHERE i_user = ? ";
		UserVO result = new UserVO();
		
		JdbcTemplate.executeQuery(sql, new JdbcSelectInterface() {
			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
				ps.setInt(1, i_user);
			}
			
			@Override
			public int executeQuery(ResultSet rs) throws SQLException {
				if(rs.next()) {
					result.setUser_id(rs.getNString("u_id"));
					result.setUser_nm(rs.getNString("u_nm"));
					result.setProfile_img(rs.getNString("profile_img"));
					result.setEmail(rs.getNString("u_email"));
					result.setR_dt(rs.getNString("r_dt"));
				}
				return 1;
			}
		});
		return result;			
	}
	
	public static int updUser(UserVO param) {
		StringBuilder sb = new StringBuilder(" UPDATE t_user SET m_dt = sysdate"); // 퍼포먼스 때문에 사용. 반복문시 필수
		if(param.getUser_pw() != null) {
			sb.append(" , u_pw = '");
			sb.append(param.getUser_pw());
			sb.append("'");
		}
		if(param.getUser_nm() != null) {
			sb.append(" , u_nm = '");
			sb.append(param.getUser_nm());
			sb.append("'");
		}
		if(param.getEmail() != null) {
			sb.append(" , email = '");
			sb.append(param.getEmail());
			sb.append("'");
		}
		if(param.getProfile_img() != null) {
			sb.append(" , profile_img = '");
			sb.append(param.getProfile_img());
			sb.append("'");
		}
		sb.append(" WHERE i_user = ");
		sb.append(param.getI_user());
		
		return JdbcTemplate.executeUpdate(sb.toString(), new JdbcUpdateInterface() {
			@Override
			public void update(PreparedStatement ps) throws SQLException {}
		});
	}
}
