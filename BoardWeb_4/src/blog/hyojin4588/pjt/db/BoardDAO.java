package blog.hyojin4588.pjt.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import blog.hyojin4588.pjt.vo.BoardVO;
import blog.hyojin4588.pjt.vo.PageVO;
import blog.hyojin4588.pjt.vo.UserLikeVO;

public class BoardDAO {

	public static int insDetail(BoardVO param) {
		String sql = " INSERT INTO t_board4(i_board, title, ctnt, i_user) SELECT seq_board.nextval, ?, ?, ? FROM DUAL ";
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setNString(1, param.getTitle());
				ps.setNString(2, param.getCtnt());
				ps.setInt(3, param.getI_user());
			}
		});
	}

	public static void updateHits(BoardVO param) {
		String sql = "UPDATE t_board4 SET hits = ? WHERE i_board = ? ";
		JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				int udthits = param.getHits() + 1;
				ps.setInt(1, udthits);
				ps.setInt(2, param.getI_board());
				param.setHits(udthits);
			}
		});
	}

	public static int modDetail(BoardVO param) {
		String sql = " UPDATE t_board4 SET (title, ctnt) = (SELECT ?, ? FROM DUAL) WHERE i_board = ? and i_user = ? ";
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setNString(1, param.getTitle());
				ps.setNString(2, param.getCtnt());
				ps.setInt(3, param.getI_board());
				ps.setInt(4, param.getI_user());
			}
		});
	}

	public static List<BoardVO> selBoard() {
		List<BoardVO> list = new ArrayList<BoardVO>();
		String sql = " SELECT a.i_board, a.title, a.hits, a.i_user, a.r_dt, b.u_nm FROM t_board4 a INNER JOIN t_user b ON a.i_user = b.i_user ORDER BY i_board DESC ";
		JdbcTemplate.executeQuery(sql, new JdbcSelectInterface() {
			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
			}

			@Override
			public int executeQuery(ResultSet rs) throws SQLException {
				int total = 0;
				while (rs.next()) {
					int i_board = rs.getInt("i_board");
					int i_user = rs.getInt("i_user");
					String title = rs.getNString("title");
					int hits = rs.getInt("hits");
					String r_dt = rs.getNString("r_dt");
					String u_nm = rs.getNString("u_nm");

					BoardVO vo = new BoardVO();
					vo.setI_board(i_board);
					vo.setI_user(i_user);
					vo.setTitle(title);
					vo.setHits(hits);
					vo.setR_dt(r_dt);
					vo.setU_nm(u_nm);

					list.add(vo);
					total++;
				}
				return total;
			}
		});
		return list;
	}

	public static PageVO selDetail(PageVO param) {
		PageVO vo = new PageVO();
		String sql = " SELECT a.i_board, a.title, a.ctnt, TO_CHAR(a.r_dt, 'YYYY/MM/DD HH24:MI') AS r_dt, a.i_user, a.hits, b.u_nm, b.profile_img, nvl(c.cnt, 0) AS like_cnt, decode(d.i_user, NULL, 0, 1) AS yn_like FROM t_board4 a INNER JOIN t_user b ON a.i_user = b.i_user LEFT JOIN (SELECT a.i_board, count(i_board) AS cnt FROM t_board4_like a GROUP BY a.i_board) c ON a.i_board = c.i_board LEFT JOIN t_board4_like d ON a.i_board = d.i_board AND d.i_user = ? WHERE a.i_board = ? ";
		JdbcTemplate.executeQuery(sql, new JdbcSelectInterface() {
			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_user());
				ps.setInt(2, param.getI_board());
			}

			@Override
			public int executeQuery(ResultSet rs) throws SQLException {
				int result = 0;
				if (rs.next()) {
					String title = rs.getNString("title");
					String ctnt = rs.getNString("ctnt");
					String r_dt = rs.getNString("r_dt");
					String u_nm = rs.getNString("u_nm");
					String profile_img = rs.getNString("profile_img");
					int i_user = rs.getInt("i_user");
					int hits = rs.getInt("hits");
					int like_cnt = rs.getInt("like_cnt");
					int my_like = rs.getInt("yn_like");

					vo.setTitle(title);
					vo.setCtnt(ctnt);
					vo.setR_dt(r_dt);
					vo.setU_nm(u_nm);
					vo.setProfile_img(profile_img);
					vo.setI_user(i_user);
					vo.setHits(hits);
					vo.setI_board(param.getI_board());
					
					vo.setLike_cnt(like_cnt);
					vo.setMy_like(my_like);
					result++;
				}
				return result;
			}
		});
		return vo;
	}

	public static int delDetail(BoardVO param) {
		String sql = " DELETE FROM t_board4 WHERE i_board = ? and i_user = ? ";
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_board());
				ps.setInt(2, param.getI_user());
			}
		});
	}

	public static int selLike(UserLikeVO param) {
		String sql = " SELECT a.*, c.u_nm, DECODE(b.i_user, null, 0, 1) as yn_like FROM t_board4 a LEFT JOIN t_board4_like b ON a.i_board = b.i_board And b.i_user = ? INNER JOIN t_user c ON a.i_user = c.i_user where a.i_board = ? ";
		return JdbcTemplate.executeQuery(sql, new JdbcSelectInterface() {
			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_user());
				ps.setInt(2, param.getI_board());
			}

			@Override
			public int executeQuery(ResultSet rs) throws SQLException {
				int like = 0;
				if (rs.next()) {
					like = rs.getInt("yn_like");
					param.setLike(like);
				}
				return like;
			}
		});
	}

	public static int insLike(UserLikeVO param) {
		String sql = " INSERT INTO t_board4_like(i_user, i_board) SELECT ?, ? FROM DUAL ";
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_user());
				ps.setInt(2, param.getI_board());
			}
		});
	}

	public static int delLike(UserLikeVO param) {
		String sql = " DELETE FROM t_board4_like WHERE i_user = ? AND i_board = ? ";
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_user());
				ps.setInt(2, param.getI_board());
			}
		});
	}

	public static int selpagingCnt(PageVO param) {
		String sql = " SELECT ceil((COUNT(i_board) / ?)) as pagingCnt FROM t_board4 WHERE ";
		switch (param.getSearchType()) {
		case "a":
			sql += "title LIKE ?";
			break;
		case "b":
			sql += "ctnt LIKE ?";
			break;
		case "c":
			sql += "title LIKE ? or ctnt LIKE ?";
			break;
		}

		return JdbcTemplate.executeQuery(sql, new JdbcSelectInterface() {
			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
				int seq = 0;
				ps.setInt(++seq, param.getRecordCnt());
//				System.out.println(seq);
				ps.setNString(++seq, param.getSearchText());
//				System.out.println(seq);
				if("c".equals(param.getSearchType())) {
					ps.setNString(++seq, param.getSearchText());
//					System.out.println(seq);
				}
			}

			@Override
			public int executeQuery(ResultSet rs) throws SQLException {
				if (rs.next()) {
					return rs.getInt("pagingCnt");
				}
				return 0;
			}
		});
	}

	public static List<PageVO> selPaging(PageVO param) {
//		String sql = " SELECT a.* FROM (SELECT ROWNUM as RNUM, a.* FROM (SELECT b.profile_img, a.i_board, a.title, a.hits, a.i_user, a.r_dt, b.u_nm FROM t_board4 a INNER JOIN t_user b ON a.i_user = b.i_user WHERE a.title LIKE ? ORDER BY i_board DESC) A WHERE ROWNUM <= ?) A WHERE a.rnum > ? ";
		String sql = " SELECT a.* FROM (SELECT ROWNUM as RNUM, a.* FROM (SELECT b.profile_img, a.i_board, a.title, a.hits, a.i_user, a.r_dt, b.u_nm, c.like_cnt, c.cmt_cnt, c.my_like, c.my_cmt FROM t_board4 a INNER JOIN t_user b ON a.i_user = b.i_user LEFT JOIN (SELECT A.i_board, A.title, nvl(b.cnt, 0) as like_cnt, nvl(c.cmt, 0) as cmt_cnt, DECODE(d.i_board, null, 0, 1) as my_like, DECODE(e.i_board, null, 0, 1) as my_cmt FROM t_board4 A LEFT JOIN (SELECT i_board, count(i_board) as CNT FROM t_board4_like GROUP BY i_board) B ON A.i_board = B.i_board LEFT JOIN (SELECT i_board, count(i_board) as cmt FROM t_board4_cmt GROUP BY i_board) C ON A.i_board = C.i_board LEFT JOIN (SELECT * FROM t_board4_like WHERE i_user = ?) D ON A.i_board = D.i_board LEFT JOIN (SELECT count(i_board),i_board FROM t_board4_cmt WHERE i_user = ? GROUP by i_board )E ON A.i_board = E.i_board) c ON a.i_board = c.i_board WHERE ";
		switch (param.getSearchType()) {
		case "a":
			sql += "A.title LIKE ?";
			break;
		case "b":
			sql += "A.ctnt LIKE ?";
			break;
		case "c":
			sql += "A.title LIKE ? or A.ctnt LIKE ?";
			break;
		}
		sql += " ORDER BY i_board DESC) A WHERE ROWNUM <= ?) A WHERE a.rnum > ? ";
		
		List<PageVO> list = new ArrayList<PageVO>();
		//System.out.println(sql);
		JdbcTemplate.executeQuery(sql, new JdbcSelectInterface() {
			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
				int maxCnt = param.getPage() * param.getRecordCnt();
				int minCnt = maxCnt - param.getRecordCnt();
				int i_user = param.getI_user();
				
//				System.out.println("maxCnt : " + maxCnt);
//				System.out.println("minCnt : " + minCnt);
//				System.out.println("i_user : " + i_user);
				
				int seq = 0;
				ps.setInt(++seq, i_user);
				ps.setInt(++seq, i_user);
				ps.setNString(++seq, param.getSearchText());
				if("c".equals(param.getSearchType())) {
					ps.setNString(++seq, param.getSearchText());
				}
				ps.setInt(++seq, maxCnt);
				ps.setInt(++seq, minCnt);
			}

			@Override
			public int executeQuery(ResultSet rs) throws SQLException {
				int result = 0;
				while (rs.next()) {
					PageVO vo = new PageVO();
					int i_board = rs.getInt("i_board");
					int i_user = rs.getInt("i_user");
					String title = rs.getNString("title");
					int hits = rs.getInt("hits");
					String r_dt = rs.getNString("r_dt");
					String u_nm = rs.getNString("u_nm");
					String profile_img = rs.getNString("profile_img");

					int like_cnt = rs.getInt("like_cnt");
					int cmt_cnt = rs.getInt("cmt_cnt");
					int my_like = rs.getInt("my_like");
					int my_cmt = rs.getInt("my_cmt");

					vo.setI_board(i_board);
					vo.setI_user(i_user);
					vo.setTitle(title);
					vo.setHits(hits);
					vo.setR_dt(r_dt);
					vo.setU_nm(u_nm);
					vo.setProfile_img(profile_img);

					vo.setLike_cnt(like_cnt);
					vo.setCmt_cnt(cmt_cnt);
					vo.setMy_like(my_like);
					vo.setMy_cmt(my_cmt);
					
					list.add(vo);
					result++;
				}
				return result;
			}
		});
		return list;
	}
	
	public static List<PageVO> selBoardLikeList(final PageVO param) {
		List<PageVO> list = new ArrayList<PageVO>();
		String sql = " SELECT a.i_user, a.r_dt, b.u_nm, b.profile_img FROM t_board4_like a INNER JOIN t_user b ON a.i_user = b.i_user WHERE i_board = ? ORDER BY a.r_dt ASC ";
		
		JdbcTemplate.executeQuery(sql, new JdbcSelectInterface() {
			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_board());
			}
			
			@Override
			public int executeQuery(ResultSet rs) throws SQLException {
				while(rs.next()) {
					PageVO vo = new PageVO();
					vo.setI_user(rs.getInt("i_user"));
					vo.setU_nm(rs.getNString("u_nm"));
					vo.setProfile_img(rs.getNString("profile_img"));
					vo.setR_dt(rs.getNString("r_dt"));
					list.add(vo);
				}
				return 1;
			}
		});
		
		return list;
	}

}
