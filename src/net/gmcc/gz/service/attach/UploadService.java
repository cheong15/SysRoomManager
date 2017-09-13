package net.gmcc.gz.service.attach;

import com.hotent.core.util.AppConfigUtil;
import com.hotent.core.util.StringUtil;
import net.gmcc.gz.model.attach.Attachment;
import net.gmcc.gz.util.FTPUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 附件上传接口
 */
@Service
public class UploadService {

    @Resource
    private AttachmentService attachmentService;

    /**
     * 接收单个multipartFile，一次上传一个文件
     * @param uploadFile
     * @param attachment
     * @throws Exception
     */
    public void uploadOne(MultipartFile uploadFile, Attachment attachment)throws Exception {

        if (StringUtil.isEmpty(attachment.getFileid())) {

            attachment.setFileid(UUID.randomUUID().toString());
            //(从附件中获取文件名)
            String oriFileName = uploadFile.getOriginalFilename();
            attachment.setOriFilename(oriFileName);

            //设置文件大小
            long fileSize = uploadFile.getSize();
            attachment.setFileSize(fileSize);
            //设置文件上传时间
            Date uploadTime = new Date();
            attachment.setUploadTime(uploadTime);
            //设置文件后缀名
            String fileSuffix = "";
            if (oriFileName.lastIndexOf(".") != -1) {
                fileSuffix = oriFileName.substring(oriFileName.lastIndexOf("."));
            }
            attachment.setFileSubpfix(fileSuffix);
            //设置新文件名
            String newFileName = attachment.getFileid();
            attachment.setNewFilename(newFileName + attachment.getFileSubpfix());
            //设置文件序号(count+1)
            Attachment queryAttachment = new Attachment();
            queryAttachment.setFileid(attachment.getFileid());
            queryAttachment.setBusinessType(attachment.getBusinessType());
            queryAttachment.setBusinessId(attachment.getBusinessId());
            queryAttachment.setFileType(attachment.getFileType());
            Long sortNum = attachmentService.getSortNum(queryAttachment);
            if (sortNum==null) {
                sortNum=0L;
            }
            sortNum = sortNum+1;

            attachment.setSort(sortNum);

            if (StringUtil.isEmpty(queryAttachment.getFileType())) {
                attachment.setFilePath(AppConfigUtil.get("ftp.path") + "/" + attachment.getBusinessId() + "/" + attachment.getNewFilename());
            } else {
                attachment.setFilePath(AppConfigUtil.get("ftp.path") + "/" + attachment.getBusinessId() + "/" + queryAttachment.getFileType() + "/" + attachment.getNewFilename());
            }

            /*********************添加到ftp服务器*****************/
            InputStream inputStream = uploadFile.getInputStream();
            FTPUtil.createOneAttachmentRemote(inputStream, attachment);
            /*********************添加到ftp服务器*****************/
            //保存
            attachmentService.add(attachment);

        } else {
            attachmentService.update(attachment);
        }

    }


    /**
     * 接收multipartFile集合，一次上传多个文件
     * @param multipartFiles
     * @param attachment
     * @throws Exception
     */
    public void upload(List<MultipartFile> multipartFiles, Attachment attachment) throws Exception{

        List<InputStream> ipsList = new ArrayList<InputStream>();
        List<String> nameList = new ArrayList<String>();
        List<Attachment> attachmentList = new ArrayList<Attachment>();

        String businessType = attachment.getBusinessType();
        String fileType = attachment.getFileType();
        String businessId = attachment.getBusinessId();
        String uploaderId = attachment.getUploaderId();
        String uploaderName = attachment.getUploaderName();
        String remark = attachment.getRemark();
        Date uploadTime = new Date();


        for (MultipartFile uploadFile : multipartFiles) {
            Attachment attachmentSetter = new Attachment();
            attachmentSetter.setBusinessType(businessType);
            attachmentSetter.setFileType(fileType);
            attachmentSetter.setBusinessId(businessId);
            attachmentSetter.setUploaderId(uploaderId);
            attachmentSetter.setUploaderName(uploaderName);
            attachmentSetter.setRemark(remark);
            attachmentSetter.setUploadTime(uploadTime);
            if (StringUtil.isEmpty(attachmentSetter.getFileid())) {

                attachmentSetter.setFileid(UUID.randomUUID().toString());
                //(从附件中获取文件名)
                String oriFileName = uploadFile.getOriginalFilename();
                attachmentSetter.setOriFilename(oriFileName);

                //设置文件大小
                long fileSize = uploadFile.getSize();
                attachmentSetter.setFileSize(fileSize);

                //设置文件后缀名
                String fileSuffix = "";
                if (oriFileName.lastIndexOf(".") != -1) {
                    fileSuffix = oriFileName.substring(oriFileName.lastIndexOf("."));
                }
                attachmentSetter.setFileSubpfix(fileSuffix);
                //设置新文件名
                String newFileName = attachmentSetter.getFileid();
                attachmentSetter.setNewFilename(newFileName + attachmentSetter.getFileSubpfix());
                //设置文件序号(count+1)
                Attachment queryAttachment = new Attachment();
//                queryAttachment.setFileid(attachmentSetter.getFileid());
                queryAttachment.setBusinessType(attachmentSetter.getBusinessType());
                queryAttachment.setBusinessId(attachmentSetter.getBusinessId());
                queryAttachment.setFileType(attachmentSetter.getFileType());
                Long sortNum = attachmentService.getSortNum(queryAttachment);
                if (sortNum==null||sortNum==0) {
                    sortNum=0L;
                }
                sortNum = sortNum+1;
                attachmentSetter.setSort(sortNum);

                if (StringUtil.isEmpty(queryAttachment.getFileType())) {
                    attachmentSetter.setFilePath(AppConfigUtil.get("ftp.path") + "/" + attachmentSetter.getBusinessId() + "/" + attachmentSetter.getNewFilename());
                    attachment.setFilePath(attachmentSetter.getFilePath());
                } else {
                    attachmentSetter.setFilePath(AppConfigUtil.get("ftp.path") + "/" + attachmentSetter.getBusinessId() + "/" + attachmentSetter.getFileType() + "/" + attachmentSetter.getNewFilename());
                    attachment.setFilePath(attachmentSetter.getFilePath());
                }

                InputStream inputStream = uploadFile.getInputStream();
                ipsList.add(inputStream);
                nameList.add(attachmentSetter.getNewFilename());
                attachmentList.add(attachmentSetter);
            }
        }


        List<String> failList = FTPUtil.createAttachmentRemote(ipsList, nameList, attachment);
        for (Attachment attach : attachmentList) {
            attachmentService.add(attach);
        }


    }



}
