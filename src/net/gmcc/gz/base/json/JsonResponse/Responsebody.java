package net.gmcc.gz.base.json.JsonResponse;

import net.gmcc.gz.base.json.PageInfo;

/**
 * <pre>
 * 对象功能:JSON响应体
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:Liangyy
 * 创建时间:2017-09-10 01:31:55
 * </pre>
 */
public class Responsebody<T> {
    /**
     * 分页信息
     */
    private PageInfo pageInfo;

    /**
     * 响应内容
     */
    private T responseContent;

    public Responsebody() {
    }

    public Responsebody(PageInfo pageInfo, T responseContent) {
        this.pageInfo = pageInfo;
        this.responseContent = responseContent;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public T getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(T responseContent) {
        this.responseContent = responseContent;
    }
}
