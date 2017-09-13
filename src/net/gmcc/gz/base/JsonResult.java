package net.gmcc.gz.base;

/**
 * <pre>
 * 对象功能:JSON返回类
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:Long
 * 创建时间:2017-9-5 下午6:08:01
 * </pre>
 */
public class JsonResult {
	
	/**
	 * 返回状态码
	 */
	private String code;
	
	/**
	 * 返回消息
	 */
	private String msg;
	
	/**
	 * 返回实体
	 */
	private Object result;
	
	/**
	 * 返回成功实体
	 * @param msg
	 * @param result
	 * @return
	 */
	public static JsonResult successResponse(Object result){
		
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode("0");
		jsonResult.setMsg("success");
		jsonResult.setResult(result);
		return jsonResult;
	}
	
	/**
	 * 返回失败实体
	 * @param msg
	 * @param result
	 * @return
	 */
	public static JsonResult failResponse(Object result){
		
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode("1");
		jsonResult.setMsg("failure");
		jsonResult.setResult(result);
		return jsonResult;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
	
}
