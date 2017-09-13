package net.gmcc.gz.util;

import com.enterprisedt.util.debug.Logger;
import com.hotent.core.util.AppConfigUtil;
import com.hotent.core.util.StringUtil;
import net.gmcc.gz.model.attach.Attachment;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 对象功能:FTP服务器操作工具类
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:Long
 * 创建时间:2017-8-21 上午9:31:43
 * </pre>
 */
public class FTPUtil {
	
	private static Logger logger = Logger.getLogger(FTPUtil.class);
	
	/**
	 * 创建单个附件到远程FTP服务器
	 * @return
	 * @throws IOException
	 */
	public static String createOneAttachmentRemote(InputStream inputStream, Attachment attachment) throws IOException{


		String fileName = attachment.getNewFilename();

		String failFile=null;
		try {
			String filePath = attachment.getFilePath();

			String dirPath = filePath.substring(0,filePath.lastIndexOf("/"));
	        
	        FTPClient ftpClient = new FTPClient();
	        boolean flag = true;
	        if (connectServer(ftpClient)) {
	            try {
	                ftpClient.setControlEncoding("UTF-8");
	                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
	                ftpClient.setBufferSize(1024);
	                ftpClient.enterLocalPassiveMode();
	                
	                if (ftpClient.makeDirectory(dirPath)) {
	                    System.out.println("新建文件夹"+attachment.getBusinessId()+"成功");
	                } else {
	                    System.out.println("新建文件夹"+attachment.getBusinessId()+"失败");
	                }
	                if (ftpClient.changeWorkingDirectory(dirPath)) {
	                    System.out.println("加入文件成功");
	                } else {
	                    System.out.println("加入文件失败");
	                }
                  ftpClient.storeFile(fileName,inputStream);

	                if (flag) {
	                    System.out.println("上传文件成功！");
	                    failFile=dirPath+"/"+fileName;
	                    System.out.println(failFile);
	                } else {
	                    System.out.println("上传文件失败！");
	                }
	                ftpClient.logout();
	            } catch (IOException e) {
	                e.printStackTrace();
	                throw new RuntimeException("FTP客户端出错！", e);
	            } finally {
	                if (ftpClient.isConnected()) {
	                    try {
	                        ftpClient.disconnect();
	                    } catch (IOException ioe) {
	                    }
	                }
	            }
	        } else {
	            System.out.println("登录FTP服务器失败");
	        }
		} catch (Exception e) {
			logger.error("生成导入失败列表时出错\n" + e);
			e.printStackTrace();
		}

        return failFile;
	}

	/**
	 * 创建多个附件到远程FTP服务器
	 * @param ipsList
	 * @param nameList
	 * @param attachment
	 * @return
	 * @throws IOException
	 */
	public static List<String> createAttachmentRemote(List<InputStream> ipsList,List<String> nameList,Attachment attachment) throws IOException{


		List<String> failFiles = new ArrayList<String>();
		String failFile=null;
		try {

			FTPClient ftpClient = new FTPClient();
			boolean flag = true;
			if (connectServer(ftpClient)) {
				try {
					ftpClient.setControlEncoding("UTF-8");
					ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
					ftpClient.setBufferSize(1024);
					ftpClient.enterLocalPassiveMode();


					String filePath = attachment.getFilePath();
					String dirPath = filePath.substring(0,filePath.lastIndexOf("/"));

					if (ftpClient.makeDirectory(dirPath)) {
						System.out.println("新建文件夹"+attachment.getBusinessId()+"成功");
					} else {
						System.out.println("新建文件夹"+attachment.getBusinessId()+"失败");
					}
					if (ftpClient.changeWorkingDirectory(dirPath)) {
						System.out.println("加入文件成功");
					} else {
						System.out.println("加入文件失败");
					}

					int i = 0;
					for (InputStream inputStream : ipsList) {
						String fileName = nameList.get(i);

						ftpClient.storeFile(fileName,inputStream);
						i++;
						if (flag) {
							System.out.println("上传文件成功！");
						} else {
							failFile=dirPath+"/"+fileName;
							failFiles.add(failFile);
							System.out.println(failFile);
							System.out.println("上传文件失败！");
						}
					}
					ftpClient.logout();
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException("FTP客户端出错！", e);
				} finally {
					if (ftpClient.isConnected()) {
						try {
							ftpClient.disconnect();
						} catch (IOException ioe) {
						}
					}
				}
			} else {
				System.out.println("登录FTP服务器失败");
			}
		} catch (Exception e) {
			logger.error("生成导入失败列表时出错\n" + e);
			e.printStackTrace();
		}
		return failFiles;
	}






	
	/**
	 * 下载FTP远程文件
	 * @param filePath
	 * @param response
	 * @throws IOException
	 */
	public static void loadFile(String filePath,String fileOriName ,HttpServletResponse response) throws IOException{
		//设置被动模式
		FTPClient ftpClient = new FTPClient();
		connectServer(ftpClient);
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		FTPFile[] files = ftpClient.listFiles(filePath);

		System.out.println("length:" + files.length);
		if (files.length != 1) {
			System.out.println("远程文件不存在");
			return;
		}

		long lRemoteSize = files[0].getSize();
		if (StringUtil.isEmpty(fileOriName)) {
			fileOriName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
		}

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileOriName.getBytes("GBK"), "iso-8859-1"));
		OutputStream out = response.getOutputStream();
		InputStream in = ftpClient.retrieveFileStream(filePath);
		byte[] bytes = new byte[1024];
		long step = lRemoteSize / 100;
		long process = 0;
		long localSize = 0L;
		int c;
		while ((c = in.read(bytes)) != -1) {
			out.write(bytes, 0, c);
			localSize += c;
			long nowProcess = localSize / step;
			if (nowProcess > process) {
				process = nowProcess;
				if (process % 10 == 0){
					System.out.println("下载进度：" + process);
				}
			}
		}
		out.flush();
		in.close();
		out.close();
		ftpClient.completePendingCommand();
		ftpClient.logout();
	}
	
	/**
	 * 链接FTP服务器
	 * @param ftpClient
	 * @return
	 */
	public static boolean connectServer(FTPClient ftpClient) {
		
        boolean flag = true;
        if (ftpClient.isConnected() == false) {
            String userName = null;
            String password = null;
            String ip = null;
            int port = 0;
            
            userName = AppConfigUtil.get("ftp.username");
            password = AppConfigUtil.get("ftp.password");
            ip = AppConfigUtil.get("ftp.addr");
            port = Integer.valueOf(AppConfigUtil.get("ftp.port"));
            
            int reply;
            try {
                ftpClient.setControlEncoding("GBK");
                ftpClient.setDefaultPort(port);
                ftpClient.connect(ip);

                if (ftpClient.login(userName, password)) {
                    System.out.println("登录成功");
                }
                ftpClient.setDefaultPort(port);
                reply = ftpClient.getReplyCode();
                ftpClient.setDataTimeout(1200000);

                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftpClient.disconnect();
                    System.err.println("FTP server refused connection.");
                    flag = false;
                }
            } catch (SocketException e) {
                flag = false;
                System.err.println("登录ftp服务器 " + ip + " 失败,连接超时！");
                e.printStackTrace();
            } catch (IOException e) {
                flag = false;
                System.err.println("登录ftp服务器 " + ip + " 失败，FTP服务器无法打开！");
                e.printStackTrace();
            }
        }
        return flag;
    }

	/**
	 * 删除一个文件
	 */
	public static void deleteFile(String filePath) throws Exception{

		FTPClient ftpClient = new FTPClient();
		if (connectServer(ftpClient)) {

			try {
				ftpClient.deleteFile(filePath);
				ftpClient.logout();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("FTP客户端下载出错！", e);
			}finally {
				if (ftpClient.isConnected()) {
					ftpClient.disconnect();
				}
			}
		}

	}

	/**
	 * 删除多个文件
	 */
	public static void deleteFiles(String filePath) {



	}




}
