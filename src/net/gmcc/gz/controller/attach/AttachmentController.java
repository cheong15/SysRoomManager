package net.gmcc.gz.controller.attach;

import com.hotent.core.annotion.Action;
import com.hotent.core.util.StringUtil;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;
import net.gmcc.gz.base.json.RequestBodyExt;
import net.gmcc.gz.model.attach.Attachment;
import net.gmcc.gz.service.attach.AttachmentService;
import net.gmcc.gz.service.attach.UploadService;
import net.gmcc.gz.util.FTPUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <pre>
 * 对象功能:上传附件管理 控制器类
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:lchh
 * 创建时间:2017-09-06 17:31:55
 * </pre>
 */
@Controller
@RequestMapping("/front/attachment/")
public class AttachmentController extends BaseController {
    @Resource
    private AttachmentService attachmentService;

    @Resource
    private UploadService uploadService;

    /**
     * 取得上传附件管理分页列表
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "list")
    @ResponseBody
    @Action(description = "查看上传附件管理分页列表")
    public Object list(@RequestBodyExt(Attachment.class) Attachment attachment, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            List<Attachment> list = attachmentService.getAll(new QueryFilter(request, attachment));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 删除上传附件管理
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    @Action(description = "删除上传附件管理")
    public Object del(HttpServletRequest request, HttpServletResponse response,String fileid) throws Exception {
        try {
            if (StringUtil.isNotEmpty(fileid)) {

                Attachment attachmentDB = attachmentService.getById(fileid);
                String filePath = attachmentDB.getFilePath();
                /*********************从ftp服务器删除*****************/
                FTPUtil.deleteFile(filePath);
                /*********************从ftp服务器删除*****************/
                //从数据库中删除
                attachmentService.delById(fileid);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }


    /*
     * 上传附件。
     *
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping("upload")
    @ResponseBody
    @Action(description = "添加或更新上传附件管理")
    public Object save(@RequestParam("files") MultipartFile[] files, HttpServletRequest req) throws Exception {
        try {
            MultipartHttpServletRequest request = (MultipartHttpServletRequest) req;

            List<MultipartFile> multipartFiles = request.getFiles("files");

            String fileType = request.getParameter("fileType");
            String businessId = request.getParameter("businessId");
            String businessType = request.getParameter("businessType");
            String uploaderName = request.getParameter("uploaderName");
            String uploaderId = request.getParameter("uploaderId");
            String remark = request.getParameter("remark");

            Attachment attachment = new Attachment();

            attachment.setFileType(fileType);
            attachment.setBusinessId(businessId);
            attachment.setBusinessType(businessType);
            attachment.setUploaderName(uploaderName);
            attachment.setRemark(remark);
            attachment.setUploaderId(uploaderId);
            uploadService.upload(multipartFiles,attachment);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }


    /**
     * 附件下载
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "download")
    @ResponseBody
    @Action(description = "下载附件")
    public Object download(HttpServletRequest request, HttpServletResponse response, String fileid) throws Exception {
        if (StringUtil.isEmpty(fileid)) {
            throw new Exception();
        }
        Attachment attachmentDB = attachmentService.getById(fileid);
        String filePath = attachmentDB.getFilePath();
        try {
            /*********************从ftp服务器下载*****************/
            FTPUtil.loadFile(filePath,attachmentDB.getOriFilename(),response);
            /*********************从ftp服务器下载*****************/
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }




}
