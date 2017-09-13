package net.gmcc.gz.base.json.JsonRequest;

import java.io.Serializable;

/**
 * <pre>
 * 对象功能:JSON请求块
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:Liangyy
 * 创建时间:2017-09-10 01:31:55
 * </pre>
 */
public class JsonRequest<T> implements Serializable {
    /**
     * 请求头
     */
    private RequestHeader requestHeader;

    /**
     * 请求体
     */
    private Requestbody<T> requestbody;

    public JsonRequest() {
    }

    public JsonRequest(RequestHeader requestHeader, Requestbody<T> requestbody) {
        this.requestHeader = requestHeader;
        this.requestbody = requestbody;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public Requestbody<T> getRequestbody() {
        return requestbody;
    }

    public void setRequestbody(Requestbody<T> requestbody) {
        this.requestbody = requestbody;
    }
}
