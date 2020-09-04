package blog.hyojin4588.pjt.vo;

public class PageVO extends BoardVO {
	
	private int page;
	private int recordCnt;
	private int like_cnt;
	private int cmt_cnt;
	private int my_like;
	private int my_cmt;
	private String searchType;
	
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public int getLike_cnt() {
		return like_cnt;
	}
	public void setLike_cnt(int like_cnt) {
		this.like_cnt = like_cnt;
	}
	public int getCmt_cnt() {
		return cmt_cnt;
	}
	public void setCmt_cnt(int cmt_cnt) {
		this.cmt_cnt = cmt_cnt;
	}
	public int getMy_like() {
		return my_like;
	}
	public void setMy_like(int my_like) {
		this.my_like = my_like;
	}
	public int getMy_cmt() {
		return my_cmt;
	}
	public void setMy_cmt(int my_cmt) {
		this.my_cmt = my_cmt;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRecordCnt() {
		return recordCnt;
	}
	public void setRecordCnt(int recordCnt) {
		this.recordCnt = recordCnt;
	}

}
