package net.gmcc.gz.model.am;

/**
 * <pre>
 * 对象功能:TODO
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:Long
 * 创建时间:2017-9-11 下午4:28:22
 * </pre>
 */
public enum AnnouncementStatusEnum {

	DRAFT("草稿"),
	CHECK_FAIL("审批不通过"),
	CHECK_SUCCESS("审批通过"),
	PUBLISH("发布");
	
	private AnnouncementStatusEnum(String value){
		this.value=value;
	}
	
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
