package net.gmcc.gz.base;

/**
 * <pre>
 * 对象功能:通用请求头信息
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:Long
 * 创建时间:2017-9-7 下午3:12:36
 * </pre>
 */
public class ReqHeaders {
	
	//当前页数
	private String page;
	
	//每页最大条数
	private String pageSize;
	
	//时间戳
	private String timestamp;
	
	//加密串
	private String decstr;

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getDecstr() {
		return decstr;
	}

	public void setDecstr(String decstr) {
		this.decstr = decstr;
	}
	
}
