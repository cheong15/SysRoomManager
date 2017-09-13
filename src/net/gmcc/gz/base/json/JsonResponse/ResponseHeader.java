package net.gmcc.gz.base.json.JsonResponse;

/**
 * 通用响应头
 */
public class ResponseHeader {

    /**
     * 响应状态码
     */
    private String code;

    /**
     * 响应消息
     */
    private String msg;

    public ResponseHeader() {
    }

    public ResponseHeader(String code, String msg) {
        this.code = code;
        this.msg = msg;
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
}
