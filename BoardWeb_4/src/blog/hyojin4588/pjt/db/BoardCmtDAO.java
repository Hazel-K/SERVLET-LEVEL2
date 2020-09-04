package blog.hyojin4588.pjt.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import blog.hyojin4588.pjt.vo.BoardCmtVO;

public class BoardCmtDAO {
	
	public static List<BoardCmtVO> selCmt(BoardCmtVO param) {
		List<BoardCmtVO> list = new ArrayList<BoardCmtVO>();
		String sql = " SELECT a.i_user, b.profile_img ,b.u_nm, a.cmt, a.r_dt, a.i_cmt, a.i_board FROM t_board4_cmt a INNER JOIN t_user b ON a.i_user = b.i_user WHERE a.i_board = ? ";
		JdbcTemplate.executeQuery(sql, new JdbcSelectInterface() {
			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_board());
			}
			
			@Override
			public int executeQuery(ResultSet rs) throws SQLException {
				int result = 0;
				while(rs.next()) {
					BoardCmtVO vo = new BoardCmtVO();
					vo.setI_board(param.getI_board());
					vo.setI_user(rs.getInt("i_user"));
					vo.setU_nm(rs.getNString("u_nm"));
					vo.setCmt(rs.getNString("cmt"));
					vo.setR_dt(rs.getNString("r_dt"));
					vo.setI_cmt(rs.getInt("i_cmt"));
					vo.setI_board(rs.getInt("i_board"));
					vo.setProfile_img(rs.getNString("profile_img"));
					list.add(vo);
					result++;
				}
				return result;
			}
		});
		return list;
	}

	public static int insCmt(BoardCmtVO param) {
		String sql = " INSERT INTO t_board4_cmt(i_cmt, i_board, i_user, cmt) SELECT seq_t_board4_cmt.nextval, ?, ?, ? FROM DUAL ";
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_board());
				ps.setInt(2, param.getI_user());
				ps.setNString(3, param.getCmt());
			}
		});
	}

	public static int updCmt(BoardCmtVO param) {
		String sql = " UPDATE t_board4_cmt SET cmt = ? WHERE i_cmt = ? and i_user = ? ";
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setNString(1, param.getCmt());
				ps.setInt(2, param.getI_cmt());
				ps.setInt(3, param.getI_user());
			}
		});
	}

	public static int delCmt(BoardCmtVO param) {
		String sql = " DELETE FROM t_board4_cmt WHERE i_cmt = ? and i_user = ?";
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_cmt());
				ps.setInt(2, param.getI_user());
			}
		});
	}

}
