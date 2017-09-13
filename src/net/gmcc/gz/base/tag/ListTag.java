
package net.gmcc.gz.base.tag;

import com.hotent.core.util.AppUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.web.query.QueryFilter;
import net.gmcc.gz.model.attach.Attachment;
import net.gmcc.gz.service.attach.AttachmentService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

/**
 * 查询列表标签。
 * 在jsp页面引入<%@taglib uri="http://www.jee-soft.cn/listTag" prefix="al" %>
 *  <al:attachment
 *   businessType="项目公告" fileType="澄清" businessId="1" canDel="true">
 *  </al:attachment>
 *
 */
public class ListTag extends SimpleTagSupport{

    //标签属性
    private String businessType;
    private String fileType;
    private String businessId;
    private String var;
    private String canDel;

    @Override
    public void doTag() throws JspException, IOException {

        if (StringUtil.isEmpty(canDel)) {
            canDel = "false";
        }
        Iterator ite = null;
        AttachmentService attachmentService= (AttachmentService) AppUtil.getBean(AttachmentService.class);
        PageContext pageContext = (PageContext) this.getJspContext();
        HttpServletRequest request=(HttpServletRequest)pageContext.getRequest();

        if (businessType==null) {
            businessType = "";
        }
        if (fileType == null) {
            fileType = "";
        }
        if (businessId == null) {
            businessId = "";
        }

        Attachment attachment = new Attachment();
        attachment.setBusinessId((String) businessId);
        attachment.setFileType((String) fileType);
        attachment.setBusinessType((String) businessType);

        List<Attachment> list = attachmentService.getAll(new QueryFilter(request, attachment));

//        this.getJspContext().getOut().write(list.toString());

        //进行迭代
        if ("false".equals(canDel.toLowerCase())) {
            ite = list.iterator();
            while (ite.hasNext()) {
                Object obj = ite.next();
                Attachment attachmentVO = (Attachment) obj;
                String oriFilename = attachmentVO.getOriFilename();
                String fileid = attachmentVO.getFileid();
                getJspContext().getOut().write("<a href=\"/front/attachment/download.ht?fileid="+fileid+"\">"+oriFilename+"</a></br>");
            }
        }
        if ("true".equals(canDel.toLowerCase())) {
            ite = list.iterator();
            while (ite.hasNext()) {
                Object obj = ite.next();
                Attachment attachmentVO = (Attachment) obj;
                String oriFilename = attachmentVO.getOriFilename();
                String fileid = attachmentVO.getFileid();
                getJspContext().getOut().write("<a href=\"/front/attachment/download.ht?fileid="+fileid+"\">"+oriFilename+"</a>&nbsp;&nbsp;&nbsp;<a href=\"/front/attachment/delete.ht?fileid="+fileid+"\" style=\"color:red;\">删除</a></br>");
            }
        }

    }

    public String getCanDel() {
        return canDel;
    }
    public void setCanDel(String canDel) {
        this.canDel = canDel;
    }
    public String getVar() {
        return var;
    }
    public void setVar(String var) {
        this.var = var;
    }
    public String getBusinessType() {
        return businessType;
    }
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
    public String getFileType() {
        return fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    public String getBusinessId() {
        return businessId;
    }
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}
