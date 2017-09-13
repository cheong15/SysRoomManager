package net.gmcc.gz.base.json.JsonRequest;

/**
 * <pre>
 * 对象功能:通用请求头信息
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:Long
 * 创建时间:2017-9-7 下午3:12:36
 * </pre>
 */
public class RequestHeader {
	/**
	 * 渠道key
	 */
	private String channelkey;

	/**
	 * 渠道value
	 */
	private String channelvalue;

	/**
	 * 时间戳
	 */
	private Long timestamp;

	/**
	 * 加密串
	 */
	private String decstr;

	public RequestHeader() {
	}

	public RequestHeader(String channelkey, String channelvalue, Long timestamp, String decstr) {
		this.channelkey = channelkey;
		this.channelvalue = channelvalue;
		this.timestamp = timestamp;
		this.decstr = decstr;
	}

	public String getChannelkey() {
		return channelkey;
	}

	public void setChannelkey(String channelkey) {
		this.channelkey = channelkey;
	}

	public String getChannelvalue() {
		return channelvalue;
	}

	public void setChannelvalue(String channelvalue) {
		this.channelvalue = channelvalue;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getDecstr() {
		return decstr;
	}

	public void setDecstr(String decstr) {
		this.decstr = decstr;
	}
}
