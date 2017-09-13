package com.hotent.platform.service.system;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.Dom4jUtil;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.core.web.util.CookieUitl;
import com.hotent.platform.dao.system.ResourcesDao;
import com.hotent.platform.dao.system.SubSystemDao;
import com.hotent.platform.model.system.Resources;
import com.hotent.platform.model.system.SubSystem;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.model.system.SystemConst;

/**
 * 对象功能:子系统管理 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2011-11-21 12:22:06
 */
@Service
public class SubSystemService extends BaseService<SubSystem>
{
	
	@Resource
	private SubSystemDao subSystemDao;
	@Resource
	private ResourcesDao resourcesDao;
	
	public SubSystemService()
	{
	}
	
	@Override
	protected IEntityDao<SubSystem, Long> getEntityDao() {
		return subSystemDao;
	}
	
	
	public void setCurrentSystem( Long systemId,HttpServletRequest request, HttpServletResponse response){
		
		SubSystem subSystem=subSystemDao.getById(systemId);
		if(subSystem!=null){
			writeCurrentSystemCookie(String.valueOf(systemId),  request,  response);
			request.getSession().setAttribute(SubSystem.CURRENT_SYSTEM,subSystem);	
		}
		
		
	}
	public void writeCurrentSystemCookie(String systemId, HttpServletRequest request, HttpServletResponse response){
		if (CookieUitl.isExistByName(SubSystem.CURRENT_SYSTEM, request)) {
			CookieUitl.delCookie(SubSystem.CURRENT_SYSTEM, request, response);
		}
		int tokenValiditySeconds = 86400 * 14; // 14 days
		CookieUitl.addCookie(SubSystem.CURRENT_SYSTEM, systemId, tokenValiditySeconds, request, response);
	}
	/**
	 * 取得拥用的系统	
	 * @param user
	 * @return
	 */
	public List<SubSystem> getByUser(SysUser user){
		if(user.getAuthorities().contains(SystemConst.ROLE_GRANT_SUPER)){
			return getAll();
		}else{
			Collection<GrantedAuthority> roles= user.getAuthorities();
			List<SubSystem> sysList=new ArrayList<SubSystem>();
			if(BeanUtils.isEmpty(roles)){
				return sysList;
			}
			String roleNames="";
			for(GrantedAuthority auth:roles){
				if(roleNames.equals("")){
					roleNames+="'" + auth.getAuthority() +"'";
				}
				else{
					roleNames+=",'" + auth.getAuthority() +"'";
				}
			}
			return subSystemDao.getByRoles(roleNames);
		}
	}
	
	/**
	 * 取得本地子系统。
	 * 本地子系统的概念是系统在同一个应用当中。只不过按照功能划分成不同的系统。
	 * @return
	 */
	public List<SubSystem> getLocalSystem(){
		return subSystemDao.getLocalSystem();
	}
	
	/**
	 * 判断系统别名是否已存在。
	 * @param alias
	 * @return
	 */
	public Integer isAliasExist(String alias){
		return subSystemDao.isAliasExist(alias);
	}
	
	/**
	 * 更新时判断别名是否存在。
	 * @param alias		别名
	 * @param systemId	系统ID
	 * @return
	 */
	public Integer isAliasExistForUpd(String alias,Long systemId){
		return subSystemDao.isAliasExistForUpd(alias,systemId);
	}
	
	/**
	 * 获取已经激活的系统。
	 * @return
	 */
	public List<SubSystem> getActiveSystem(){
		return subSystemDao.getActiveSystem();
	}
	
	/**
	 * 导出子系统资源
	 * @param id
	 * @return
	 */
	public String exportXml(long systemId) {
		String strXml;
		Document doc = DocumentHelper.createDocument();	
		Element root = doc.addElement("items");	
		addElement(root,systemId,0L);
		strXml=doc.asXML();
		return strXml;
	}
	
	/**
	 * 递归 加入元素
	 * @param root
	 * @param systemId
	 * @param parentId
	 */
	public void addElement(Element root,long systemId,long parentId){
		List<Resources> list=resourcesDao.getBySystemIdAndParentId(systemId,parentId);
		if(BeanUtils.isNotEmpty(list)){
			for(Resources res:list){
				Element e=root.addElement("item");
				e.addAttribute("name", res.getResName());
				e.addAttribute("icon", res.getIcon());
				String url=res.getDefaultUrl();
				if(url!=null && !url.equals("") && !url.equals("null")){
					e.addAttribute("defaultUrl", url);
				}
				e.addAttribute("isDisplayMenu", res.getIsDisplayInMenu().toString());
				e.addAttribute("isOpen", res.getIsOpen().toString());
				e.addAttribute("isFolder", res.getIsFolder().toString());
				e.addAttribute("sn", res.getSn().toString());
				addElement(e,systemId,res.getResId());
			}
		}
	}
	
	/**
	 * 导入系统资源
	 * @param inputStream
	 * @param typeId
	 */
	public void importXml(InputStream inputStream, long systemId) {
		Document doc=Dom4jUtil.loadXml(inputStream);
		Element root = doc.getRootElement();
		List<Element> list=root.elements();
		
		for(Element element:list){
			addResource(element,0L,systemId);
		}
	}
	
	/**
	 * 递归算法，根据xml文件内容导入资源 添加至数据库中
	 * @param element
	 * @param parentId
	 */
	public void addResource(Element element,long parentId,long systemId){
		Resources res=new Resources();
		long id=UniqueIdUtil.genId();
		res.setResId(id);
		res.setResName(element.attributeValue("name"));
		res.setIcon(element.attributeValue("icon"));
		String url=element.attributeValue("defaultUrl");
		if(url!=null){
			res.setDefaultUrl(url);
		}
		res.setIsOpen(Short.parseShort(element.attributeValue("isOpen")));
		res.setIsDisplayInMenu(Short.parseShort(element.attributeValue("isDisplayMenu")));
		res.setIsFolder(Short.parseShort(element.attributeValue("isFolder")));
		res.setSn(Integer.parseInt(element.attributeValue("sn")));
		res.setSystemId(systemId);
		res.setParentId(parentId);
		resourcesDao.add(res);
		List<Element> list=element.elements();
		if(BeanUtils.isNotEmpty(list)){
			for(Element el:list){
				addResource(el, id,systemId);
			}
		}
	}
}
