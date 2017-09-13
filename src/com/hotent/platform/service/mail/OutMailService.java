package com.hotent.platform.service.mail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.hotent.core.db.IEntityDao;
import com.hotent.core.encrypt.EncryptUtil;
import com.hotent.core.mail.MailUtil;
import com.hotent.core.mail.api.AttacheHandler;
import com.hotent.core.mail.model.Mail;
import com.hotent.core.mail.model.MailAttachment;
import com.hotent.core.mail.model.MailSeting;
import com.hotent.core.service.BaseService;
import com.hotent.core.util.AppUtil;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.ContextUtil;
import com.hotent.core.util.FileUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.platform.dao.mail.OutMailDao;
import com.hotent.platform.dao.mail.OutMailUserSetingDao;
import com.hotent.platform.dao.system.SysFileDao;
import com.hotent.platform.model.mail.OutMail;
import com.hotent.platform.model.mail.OutMailAttachment;
import com.hotent.platform.model.mail.OutMailUserSeting;
import com.hotent.platform.model.system.SysFile;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.service.system.SysUserService;
import com.hotent.platform.service.util.ServiceUtil;
/**
 * 对象功能:外部邮件 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2012-04-09 14:16:18
 * 
 * 注意调用时(activation-1.1.jar和mail-1.4.4.jar)，
 * 其(geronimo-activation_1.1_spec-1.1.jar，
 * geronimo-javamail_1.4_spec-1.7.1.jar)在调用javax.mail.Part接口时有冲突，需要删除后组
 */
@Service
public class OutMailService extends BaseService<OutMail>
{
	static Long MAIL_NO_READ=0L;//未读
	static Long MAIL_IS_READ=1L;//已读
	static Integer MAIL_IS_RECEIVE = 1;// 收件箱
	static Integer MAIL_IS_SEND = 2;// 发件箱
	static Integer MAIL_IS_DRAFT = 3;// 草稿箱
	static Integer MAIL_IS_DELETE = 4;// 垃圾箱
	@Resource
	private OutMailDao dao;
	@Resource
	private OutMailUserSetingDao outMailUserSetingDao;
	@Resource
	private OutMailLinkmanService outMailLinkmanService;
	@Resource
	private SysFileDao sysFileDao;
	@Resource
	private SysUserService sysUserService;
	@Override
	protected IEntityDao<OutMail, Long> getEntityDao() 
	{return dao;}
	public OutMailService()	{}
	@Resource
	private OutMailAttachmentService outMailAttachmentService;
	
	/**
	 * 保存邮件至垃圾箱
	 * @param request
	 * @param outMailUserSeting
	 * @return
	 */
	public void addDump(Long[] lAryId) {
		for(Long l:lAryId){
			OutMail outMail = dao.getById(l);	
			dao.updateTypes(outMail.getMailId(),MAIL_IS_DELETE);
		}
	}
	
	/**
	 * 浏览邮件
	 * @param outMail
	 * @param outMailUserSeting
	 * @throws NoSuchProviderException
	 * @throws MessagingException
	 */
	public void emailRead(OutMail outMail)throws NoSuchProviderException, MessagingException {
        outMail.setIsRead(OutMail.Mail_IsRead);
        dao.update(outMail);
	}	
	
	/**
	 * 同步远程邮件，进行选择性下载
	 * @param outMailUserSeting
	 * @throws Exception
	 */
	public void emailSync(OutMailUserSeting outMailUserSeting) throws Exception {
		final String mailAddress = outMailUserSeting.getMailAddress();
		MailSeting seting = OutMailUserSetingService.getByOutMailUserSeting(outMailUserSeting);
		MailUtil mailUtil = new MailUtil(seting);
		final Long setId = outMailUserSeting.getId();
		List<Mail> list = mailUtil.receive(new AttacheHandler() {
			
			@Override
			public Boolean isDownlad(String UID) {
				return !dao.getByEmailId(UID, setId);
			}
			
			@Override
			public void handle(Part part, Mail mail) {
				try {
					saveAttach(part, mail, mailAddress);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		for(Mail mail:list){
			OutMail bean = getOutMail(mail, setId);
			// 主键
			Long mailId = UniqueIdUtil.genId();
			bean.setMailId(mailId);
			// 邮件标识
			bean.setEmailId(mail.getUID());
			List<MailAttachment> attachments = mail.getMailAttachments();
			if(BeanUtils.isNotEmpty(attachments)){// 有附件
				OutMailAttachment outMailAttachment ;
				for(MailAttachment attachment:attachments){
					String filePath = attachment.getFilePath();
					String ext = FileUtil.getFileExt(filePath);
					String fileId = new File(filePath).getName().replace("."+ext, "");
					outMailAttachment = new OutMailAttachment();
					outMailAttachment.setFileId(new Long(fileId));
					outMailAttachment.setFileName(attachment.getFileName());
					outMailAttachment.setFilePath(filePath);
					outMailAttachment.setMailId(mailId);
					outMailAttachmentService.add(outMailAttachment);
				}
			}
			dao.add(bean);
			logger.info("已下载邮件"+bean.getTitle());
		}
	}
	
	/**
	 * 获得OutMail实体
	 * @param message
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private OutMail getOutMail(Mail mail, Long setId)throws Exception{
		OutMail bean =new OutMail();
		Date sentDate = null;
		if (mail.getSendDate() != null) {
			sentDate = mail.getSendDate();
		} else {
			sentDate = new Date();
		}
		//邮件发送时间
		bean.setMailDate(sentDate);
		bean.setSetId(setId);
		bean.setTitle(mail.getSubject());
		bean.setContent(mail.getContent());
		//发件人
		bean.setSenderAddresses(mail.getSenderAddress());
		bean.setSenderName(mail.getSenderName());
		//接受者
		bean.setReceiverAddresses(mail.getReceiverAddresses());
		bean.setReceiverNames(mail.getReceiverName());
		//暗送者
		bean.setBcCAddresses(mail.getBcCAddresses());
		bean.setBcCAnames(mail.getBccName());
		//抄送者
		bean.setCcAddresses(mail.getCopyToAddresses());
		bean.setCcNames(mail.getCopyToName());
		bean.setTypes(MAIL_IS_RECEIVE);
		bean.setIsRead(OutMail.Mail_IsNotRead);
		bean.setUserId(ContextUtil.getCurrentUserId());
		return bean;
	}
	
	/**
     * 将邮件中的附件保存在本地指定目录下
     * @param filename
     * @param in
     * @return
     */
    private void saveAttach(Part message, Mail mail, String userAccount)throws Exception{
    	String filename=MimeUtility.decodeText(message.getFileName());
    	Date time=new Date();
		int year=time.getYear()+1900;
		int month=time.getMonth()+1;
    	SysUser curUser=ContextUtil.getCurrentUser();
    	String filePath=AppUtil.getAppAbsolutePath()+"emailAttachs"+File.separator+curUser.getAccount()+File.separator
    					+userAccount+File.separator+year+File.separator+month+File.separator
    					+UniqueIdUtil.genId()+"."+FileUtil.getFileExt(filename);
    	FileUtil.createFolderFile(filePath);
		FileUtil.writeFile(filePath, message.getInputStream());
		mail.getMailAttachments().add(new MailAttachment(filename, filePath));
    }
	
    /**
     * 邮箱树形列表的json数据
     * @param request
     * @param list
     * @param listTemp
     * @param listEnd
     * @throws Exception 
     */
	public List<OutMailUserSeting> getMailTreeData(Long userId) throws Exception {
		List<OutMailUserSeting> list=outMailUserSetingDao.getMailByUserId(userId);
		List<OutMailUserSeting> temp=new ArrayList<OutMailUserSeting>();
		OutMailUserSeting omus=null;
		for(OutMailUserSeting beanTemp:list){
			beanTemp.setParentId(0L);
			long id=beanTemp.getId();
			temp.add(beanTemp);
		    for(int i=0;i<4;i++){
		    	omus=new OutMailUserSeting();
		    	if(i==0){ 
		    		omus.setUserName("收件箱("+getCount(id,MAIL_IS_RECEIVE)+")");
			    	omus.setTypes(MAIL_IS_RECEIVE);
		    	}else if(i==1){
		    		omus.setUserName("发件箱("+getCount(id,MAIL_IS_SEND)+")");
			    	omus.setTypes(MAIL_IS_SEND);
		    	}else if(i==2){
		    		omus.setUserName("草稿箱("+getCount(id,MAIL_IS_DRAFT)+")");
			    	omus.setTypes(MAIL_IS_DRAFT);
		    	}else {
		    		omus.setUserName("垃圾箱("+getCount(id,MAIL_IS_DELETE)+")");
			    	omus.setTypes(MAIL_IS_DELETE);
			    }
				omus.setId(UniqueIdUtil.genId());
				omus.setParentId(beanTemp.getId());
			    temp.add(omus);
		    }
		}
		return temp;
	}
	
	/**
	 * 获取邮箱的分类邮件，如收件箱，发件箱，草稿箱
	 * @param queryFilter
	 * @return
	 */
	public List<OutMail> getFolderList(QueryFilter queryFilter){
		return dao.getFolderList(queryFilter);
	}
	
	/**
	 * 获取邮箱的分类邮件数
	 * @param address
	 * @param type
	 * @param userId
	 * @return
	 */
	private int getCount(long id,int type){
		return dao.getFolderCount(id, type);
	}
	
	/**
	 * 得到用户默认邮箱中的邮件列表
	 * @param queryFilter
	 * @return
	 */
	public List<OutMail> getDefaultMailList(QueryFilter queryFilter) {
		return dao.getDefaultMailList(queryFilter);
	}
	
	/**
	 * 发送邮件,保存邮件信息至本地,添加/更新最近联系人
	 * @param outMail
	 * @param outMailFiles
	 * @param fileIds
	 * @throws Exception
	 */
	public void sendMail(OutMail outMail,long userId,long mailId,int isReply,String context,String basePath) throws Exception {
		OutMailUserSeting outMailUserSeting=outMailUserSetingDao.getById(outMail.getSetId());
		String content=outMail.getContent();
		if(mailId==0||isReply==1){
			outMail.setMailId(UniqueIdUtil.genId());
			addSendMail(outMail,context,basePath);
		}else{
			dao.updateTypes(mailId, 2);
		}
		outMail.setContent(content);
		MailSeting mailSeting = OutMailUserSetingService.getByOutMailUserSeting(outMailUserSeting);
		MailUtil util = new MailUtil(mailSeting);
		Mail mail=getMail(outMail, basePath);
		//发送邮件
		try{
			util.send(mail);
		}catch(Exception e){
			logger.error(e.getCause().getLocalizedMessage());
			//抛出运行时异常，回滚事务
			throw new RuntimeException(e.getCause().getLocalizedMessage());
		}
		//添加 所有收件人为最近联系人
		outMailLinkmanService.addLinkMan(outMail,userId);
	}
	
	/**
	 * 得到用于回复页面显示信息
	 * @param mailId
	 * @return
	 */
	public OutMail getOutMailReply(Long mailId) {
		OutMail outMail=getById(mailId);
		outMail.setIsReply(OutMail.Mail_IsReplay);
		outMail.setTitle("回复:" + outMail.getTitle());
		return outMail;
	}
	
	
	public void addSendMail(OutMail outMail,String context,String basePath) {
		StringBuffer ids=new StringBuffer();
		OutMailUserSeting mailSeting=outMailUserSetingDao.getById(outMail.getSetId());
		SysUser sysUser=sysUserService.getById(mailSeting.getUserId());
		String account=sysUser.getAccount();
		int type=outMail.getTypes();
		if(type==2){
			String fileIds=outMail.getFileIds().replaceAll("quot;", "\"");
			JSONObject jsonObj=JSONObject.fromObject(fileIds);
			JSONArray jsonArray = JSONArray.fromObject(jsonObj.get("attachs"));
			if(jsonArray.size()>0){
				String content=outMail.getContent();
				content=content+"<br><hr><h2>附件"+jsonArray.size()+"</h2>";
				for(Object obj:jsonArray){
					JSONObject json = (JSONObject)obj;
					long id=Long.parseLong(json.getString("id"));
					SysFile file=sysFileDao.getById(id);
					String fileName=file.getFileName()+"."+file.getExt();
					Date time=file.getCreatetime();
					int year=time.getYear()+1900;
					int month=time.getMonth()+1;
					String path="/emailAttachs/"+account+"/"+mailSeting.getUserName()+"/"+year+"/"+month;
					String realPath=AppUtil.getRealPath(path+File.separator+id+"."+file.getExt());
					String contextPath=context+path+"/"+id+"."+file.getExt();
					FileUtil.createFolderFile(realPath);
					if(StringUtil.isEmpty(basePath)){
						basePath=ServiceUtil.getBasePath();
					}else{
						FileUtil.copyFile(basePath+File.separator+file.getFilePath(), realPath);
					}
					content=content+"<div style='font-size:15px;'><font color='green'>"+fileName+"</font>&nbsp;&nbsp;&nbsp;&nbsp;<a href='"+contextPath+"'>查看</a></div>";
				}
				outMail.setContent(content);
			}
		}
		add(outMail);
	}
	
	/**
	 * 获取Mail 实例
	 * @param outMail
	 * @param outMailUserSeting
	 * @return
	 * @throws Exception 
	 */
	private Mail getMail(OutMail outMail, String basePath) throws Exception{
		Mail mail=new Mail();
		if(BeanUtils.isNotEmpty(outMail)){
			mail.setSenderName(outMail.getSenderName());
			mail.setSenderAddress(outMail.getSenderAddresses());
			mail.setReceiverAddresses(outMail.getReceiverAddresses());
			if(StringUtil.isNotEmpty(outMail.getBcCAddresses())){
				mail.setBcCAddresses(outMail.getBcCAddresses());
			}
			if(StringUtil.isNotEmpty(outMail.getCcAddresses())){
				mail.setCopyToAddresses(outMail.getCcAddresses());
			}
			String fileIds=outMail.getFileIds().replaceAll("quot;", "\"");
			
			JSONObject jsonObj=JSONObject.fromObject(fileIds);
			JSONArray jsonArray = JSONArray.fromObject(jsonObj.get("attachs"));
			if(jsonArray.size()>0){
				SysFile sysFile = null ;
				List<MailAttachment> attachments = mail.getMailAttachments();
				for(Object obj:jsonArray){
					JSONObject json = (JSONObject)obj;
					long id=Long.parseLong(json.getString("id"));
					sysFile=sysFileDao.getById(id);
					if(StringUtil.isEmpty(basePath)){
						//路径从配置文件中获取
						basePath=ServiceUtil.getBasePath();
					}
					String filePath = basePath+File.separator+sysFile.getFilePath();
					attachments.add(new MailAttachment(sysFile.getFileName()+"."+FileUtil.getFileExt(filePath),filePath));
				}
			}
			mail.setContent(outMail.getContent());
			mail.setSubject(outMail.getTitle());
		}
		return mail;
	}
	
	/**
	 * 发送系统错误报告至公司邮箱
	 * @param content
	 * @param recieveAdress
	 * @param mail
	 * @throws Exception
	 */
	public void sendError(String errorMsg, String recieveAdress, OutMailUserSeting outMailUserSeting)throws Exception {
		MailSeting seting = OutMailUserSetingService.getByOutMailUserSeting(outMailUserSeting);
		MailUtil mailUtil = new MailUtil(seting);
		Mail mail = new Mail();
		mail.setContent(errorMsg);
		mail.setSubject("BPMX3错误报告！");
		mail.setReceiverAddresses(recieveAdress);
		mailUtil.send(mail);
		
	}

	public void delBySetId(Long setId) {
		dao.delBySetId(setId);
	}
}
