package net.gmcc.gz.base.json.JsonResponse;

/**
 * <pre>
 * 对象功能:JSON返回类
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:Long
 * 创建时间:2017-9-5 下午6:08:01
 * </pre>
 */
public class JsonResponse<T> {
	/**
	 * 响应头
	 */
	private ResponseHeader responseHeader;
	
	/**
	 * 响应内容
	 */
	//请求体
	private Responsebody<T> responsebody;

	public JsonResponse() {
	}

	public JsonResponse(ResponseHeader responseHeader, Responsebody<T> responsebody) {
		this.responseHeader = responseHeader;
		this.responsebody = responsebody;
	}

	/**
	 * 返回成功实体
	 * @param responsebody
	 * @return
	 */
	public static <T> JsonResponse successResponse(Responsebody<T> responsebody) {
		JsonResponse jsonResponse = new JsonResponse(new ResponseHeader("0", "success"), responsebody);
		return jsonResponse;
	}

	/**
	 * 返回失败实体
	 * @param responsebody
	 * @return
	 */
	public static <T> JsonResponse failResponse(Responsebody<T> responsebody) {
		JsonResponse jsonResponse = new JsonResponse(new ResponseHeader("1", "failure"), responsebody);
		return jsonResponse;
	}


	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}

	public Responsebody<T> getResponsebody() {
		return responsebody;
	}

	public void setResponsebody(Responsebody<T> responsebody) {
		this.responsebody = responsebody;
	}

}
