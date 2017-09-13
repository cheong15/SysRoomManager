package net.gmcc.gz.base.json.JsonRequest;

import net.gmcc.gz.base.json.PageInfo;

import java.io.Serializable;

/**
 * <pre>
 * 对象功能:JSON请求体
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:Liangyy
 * 创建时间:2017-09-10 01:31:55
 * </pre>
 */
public class Requestbody<T> implements Serializable {
    /**
     * 分页信息
     */
    private PageInfo pageInfo;

    /**
     * 请求内容
     */
    private T requestContent;

    public Requestbody() {
    }

    public Requestbody(PageInfo pageInfo, T requestContent) {
        this.pageInfo = pageInfo;
        this.requestContent = requestContent;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public T getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(T requestContent) {
        this.requestContent = requestContent;
    }
}
