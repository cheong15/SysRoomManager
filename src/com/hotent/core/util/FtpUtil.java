package com.hotent.core.util;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.impl.delegate.ExpressionSetInvocation;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.web.multipart.MultipartFile;

import common.Logger;

/**
 * FTP
 * @author xiao
 *
 */
public class FtpUtil {
	
	private static final Logger logger = Logger.getLogger(FtpUtil.class);
	
	private static final String PATH = "//ftp";
	
	//正式FTP地址
	/*private static final String  ACCOUNT = "hr";
	
	private static final String PASSWORD = "HR##2016";
	
	private static final String URL = "10.244.178.170";
	
	private static final int PORT = 21;*/
	
	
	//测试环境地址
	private static final String  ACCOUNT = "test";
	
	private static final String PASSWORD = "test";
	
	private static final String URL = "192.168.21.32";
	
	
	private static final int PORT = 9511;
	
	private static String DICPATH = "/gzhr/";
	
	public static FTPClient ftpClient;
	
	private static FTPClient getFtpClient() throws IOException{
		if(ftpClient==null){
            ftpClient = new FTPClient();
    		ftpClient.setControlEncoding("GBK"); 
            ftpClient.setDefaultPort(PORT); 
            ftpClient.connect(URL);
			ftpClient.setDefaultPort(PORT);
			ftpClient.setDataTimeout(1200000);
			ftpClient.login(ACCOUNT, PASSWORD);
		}
		return ftpClient;
	}
	
	public static void closeFTP(FTPClient ftpClient) throws IOException{
		logger.info("退出FTP");
		ftpClient.logout();
	}
	
	/**
	 *上传附件 
	 * @param file 文件
	 * @param uuid 自定义附件的主键 目录
	 */
	public static Map uploadFile(MultipartFile file,String uuid) throws IOException{
		Map<String,Object> dataMap = new HashMap();
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String path = DICPATH+uuid+dateFormat.format(date);
		getFtpClient().setControlEncoding("UTF-8");
		getFtpClient().setFileType(FTPClient.BINARY_FILE_TYPE); 
		getFtpClient().setBufferSize(1024); 
		getFtpClient().enterLocalPassiveMode();
		getFtpClient().makeDirectory(path);
		getFtpClient().changeWorkingDirectory(path);
        if(getFtpClient().storeFile(file.getOriginalFilename(), file.getInputStream())){
        	logger.info("上传文件到FTP成功");
        	dataMap.put("status", 1);
        	dataMap.put("link", path);
        	dataMap.put("fileName", file.getOriginalFilename());
        	dataMap.put("createTime", date);
        }else{
        	dataMap.put("status", 0);
        	logger.info("上传文件到FTP失败");
        	
        }
        file.getInputStream().close();
        closeFTP(getFtpClient());
        return dataMap;
	}
	
	/**
	 * 删除FTP文件
	 * @param fileName 文件名称
	 * @throws IOException
	 */
	public static void deleteFile(String fileName) throws IOException{
		if(getFtpClient().deleteFile(fileName)){
			logger.info("删除文件成功");
			closeFTP(getFtpClient());
		}else{
			logger.info("删除文件失败");
		}
	}
}
