package net.gmcc.gz.model.attach;

/**
 * <pre>
 * 对象功能:TODO
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:Long
 * 创建时间:2017-9-11 下午4:28:22
 * </pre>
 */
public enum AttachmentBizTypeEnum {

	ATTACHMENT("公告");
	
	private AttachmentBizTypeEnum(String value){
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
