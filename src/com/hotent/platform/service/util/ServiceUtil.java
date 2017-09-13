package com.hotent.platform.service.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;

import com.hotent.core.bpm.util.BpmConst;
import com.hotent.core.db.JdbcHelper;
import com.hotent.core.engine.GroovyScriptEngine;
import com.hotent.core.mybatis.Dialect;
import com.hotent.core.mybatis.dialect.DB2Dialect;
import com.hotent.core.mybatis.dialect.DmDialect;
import com.hotent.core.mybatis.dialect.H2Dialect;
import com.hotent.core.mybatis.dialect.MySQLDialect;
import com.hotent.core.mybatis.dialect.OracleDialect;
import com.hotent.core.mybatis.dialect.SQLServer2005Dialect;
import com.hotent.core.table.ColumnModel;
import com.hotent.core.table.SqlTypeConst;
import com.hotent.core.util.AppConfigUtil;
import com.hotent.core.util.AppUtil;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.StringUtil;
import com.hotent.platform.dao.system.SysDataSourceDao;
import com.hotent.platform.model.form.DialogField;
import com.hotent.platform.model.system.SysDataSource;

public class ServiceUtil {

	/**
	 * 根据数据源获取JdbcHelper。
	 * 
	 * @param dsName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static JdbcHelper getJdbcHelper(String dsName) throws Exception {
		SysDataSourceDao sysDataSourceDao = (SysDataSourceDao) AppUtil
				.getBean(SysDataSourceDao.class);
		SysDataSource sysDataSource = sysDataSourceDao.getByAlias(dsName);
		if(BeanUtils.isEmpty(sysDataSource)){
			sysDataSource=(SysDataSource)AppUtil.getBean("sysdatasource");
		}
		JdbcHelper jdbcHelper = JdbcHelper.getInstance();
		jdbcHelper.init(dsName, sysDataSource.getDriverName(),
				sysDataSource.getUrl(), sysDataSource.getUserName(),
				sysDataSource.getPassword());
		jdbcHelper.setCurrentDb(dsName);
		Dialect dialect = getDialect(sysDataSource.getDbType());
		jdbcHelper.setDialect(dialect);
		return jdbcHelper;
	}

	/**
	 * 获取列表的sql语句
	 * 
	 * @param objectName
	 *            表名或视图名称。
	 * @param displayList
	 *            显示字段列表。
	 * @param conditionList
	 *            条件字段列表。
	 * @param params
	 *            传入的条件列表。
	 * @return
	 */
	public static String getSql(Map<String, Object> map, Map<String, Object> params) {
		String objectName=(String) map.get("objectName");
		List<DialogField> retrunList=(List<DialogField>) map.get("returnList");
		List<DialogField> displayList=(List<DialogField>) map.get("displayList");
		List<DialogField> conditionList=(List<DialogField>) map.get("conditionList");
		List<DialogField> sortList=(List<DialogField>) map.get("sortList");
		String sql = "select ";
		if (BeanUtils.isEmpty(retrunList)) {
			sql = "select a.* from " + objectName + " a";
		} else {
			String returnStr = "";
			for (DialogField dialogField : retrunList) {
				returnStr += "," + dialogField.getFieldName();
			}
			returnStr = returnStr.replaceFirst(",", "");
			sql += returnStr + " from " + objectName;
		}
		String where = getWhere(conditionList, params);

		String orderBy = " order by ";
		if (BeanUtils.isEmpty(displayList)) {
			for(int i=0;i<sortList.size();i++){
				DialogField df = sortList.get(i) ;
				if(i!=0){
					orderBy += ",";
				}
				orderBy += " " + df.getFieldName() + " " + df.getComment();
			}
			if(BeanUtils.isEmpty(sortList)){
				return sql + where;
			}
			return sql + where + orderBy;
		}
		if (params.containsKey("sortField")) {
			orderBy += params.get("sortField");
		} else if(BeanUtils.isEmpty(sortList)){
			orderBy += displayList.get(0).getFieldName();
		}
		//sortField有值或srotList为空
		if(!" order by ".equals(orderBy)){
			if (params.containsKey("orderSeq")) {
				orderBy += " " + params.get("orderSeq");
			} else {
				orderBy += " ASC";
			}
			for(DialogField df:sortList){
				// 添加一个判断  因为sqlserver不允许有重复的order by的字段。否则会报错
				if(!params.get("sortField").equals(df.getFieldName())){
					orderBy += ", " + df.getFieldName() + " " + df.getComment();
				}
			}
		}else{
			//sortField无值以及说sortList不为空
			for(DialogField df:sortList){
				orderBy += df.getFieldName() + " " + df.getComment()+ ",";
			}
		}
		sql = sql + where + orderBy;
		if(sql.trim().endsWith(",")){
			sql=sql.substring(0,sql.length()-1);
		}
		return sql;
	}

	/**
	 * 取得where 条件。
	 * 
	 * @param conditionMap
	 *            条件map。
	 * @param params
	 *            传入的参数
	 * @return
	 */
	public static String getWhere(List<DialogField> conditionList,
			Map<String, Object> params) {
		StringBuffer sb = new StringBuffer();
		
		for (DialogField dialogField : conditionList) {
			getStringByDialogField(dialogField, params, sb);
			if(dialogField.getDefaultType()==5){
				//java脚本，只需循环一次便可得到sql语句
				break ;
			}
		}
		if (sb.length() > 0) {
			return " where 1=1 " + sb.toString();
		}
		return "";
		
	}

	/**
	 * 根据参数配置，上下文参数计算SQL语句。
	 * 
	 * @param dialogField
	 * @param params
	 * @param sb
	 */
	private static void getStringByDialogField(DialogField dialogField,
			Map<String, Object> params, StringBuffer sb) {
		String field = dialogField.getFieldName();
		String condition = dialogField.getCondition();
		int conditionType = dialogField.getDefaultType();
		Object value = null;
		switch (conditionType) {
		case 1:
			if (params.containsKey(field)) {
				value =  params.get(field).toString();
			}
			break;
		case 2:
			value = dialogField.getDefaultValue();
			break;
		case 3:
			String script = dialogField.getDefaultValue();
			if (StringUtil.isNotEmpty(script)) {
				GroovyScriptEngine groovyScriptEngine = (GroovyScriptEngine) AppUtil
						.getBean(GroovyScriptEngine.class);
				value = groovyScriptEngine.executeObject(script, null);
			}			
			break ;
		case 4:
			//向对话框传参数
			if (params.containsKey(field)) {
				value =  params.get(field).toString();
			}
			break ;
		case 5:
			//java脚本
			String java = dialogField.getDefaultValue();
			if (StringUtil.isNotEmpty(java)) {
				GroovyScriptEngine groovyScriptEngine = (GroovyScriptEngine) AppUtil
						.getBean(GroovyScriptEngine.class);
				Map<String,Object> paramsMap = new HashMap<String,Object>() ;
				paramsMap.put("map", params) ;
				value = groovyScriptEngine.executeObject(java, paramsMap);
			}
			break ;

		}
		if (BeanUtils.isEmpty(value)){
			//解决时间类型between条件时查不到数据
			if(!(params.containsKey("start" + field) || params.containsKey("end" + field)))
			return;
		}
		
		if (value.toString().indexOf("#,")!=-1) {
			String temp=value.toString().replaceAll("#,",",");
			sb.append(" and " + field + " in ("+ temp+")");
			return ;
		}

		if (dialogField.getFieldType().equals(ColumnModel.COLUMNTYPE_VARCHAR)) {
			if (condition.equals("=")) {
				sb.append(" and " + field + "='" + value.toString() + "' ");
			} else if (condition.equals("like")) {
				sb.append(" and " + field + " like '%" + value.toString()
						+ "%' ");
			} else {
				sb.append(" and " + field + " like '" + value.toString()
						+ "%' ");
			}
		} else if (dialogField.getFieldType().equals(
				ColumnModel.COLUMNTYPE_DATE)) {
			if (dialogField.getCondition().equals("=")) {
				sb.append(" and " + field + "=:" + field + " ");
				if (!params.containsKey(field)) {
					params.put(field, value);
				}
			} else if (dialogField.getCondition().equals(">=")) {
				if (conditionType == 1) {
					if (params.containsKey("start" + field)) {
						sb.append(" and " + field + ">=:start" + field + " ");
					}
				} else {
					sb.append(" and " + field + ">=:start" + field + " ");
					params.put("start" + field, value);
				}
			} else if (dialogField.getCondition().equals("<=")) {
				if (conditionType == 1) {
					if (params.containsKey("end" + field)) {
						sb.append(" and " + field + "<=:end" + field + " ");
					}
				} else {
					sb.append(" and " + field + "<=:end" + field + " ");
					params.put("end" + field, value);
				}
			}
			//添加时间类型between条件
			else if (dialogField.getCondition().equals("between") && conditionType == 1) {
				if (params.containsKey("start" + field)) {
					sb.append(" and " + field + ">=:start" + field + " ");
				}
				if (params.containsKey("end" + field)) {
					sb.append(" and " + field + "<=:end" + field + " ");
				}
			}
		} else {
			if (conditionType == 1) {
				if (params.containsKey(field)) {
					sb.append(" and " + field + dialogField.getCondition()
							+ ":" + field + " ");
				}
			}else if (conditionType == 5) {
				if(value.toString().trim().startsWith("and")){
					sb.append(value.toString()) ;
				}else
					sb.append(" and " + value.toString());
			} else  if (conditionType == 2){
				//固定值不为varchar和date类型时
				sb.append(" and " + field + dialogField.getCondition() + value + " ");
			}else{
				sb.append(" and " + field + dialogField.getCondition() + ":"
						+ field + " ");
				params.put(field, value);
			}
		}
	}

	/**
	 * 获取方言。
	 * 
	 * @param dbType
	 * @return
	 * @throws Exception
	 */
	private static Dialect getDialect(String dbType) throws Exception {
		Dialect  dialect = new OracleDialect();
		if (dbType.equals(SqlTypeConst.ORACLE)) {
			dialect = new OracleDialect();
		} else if (dbType.equals(SqlTypeConst.SQLSERVER)) {
			dialect = new SQLServer2005Dialect();
		} else if (dbType.equals(SqlTypeConst.DB2)) {
			dialect = new DB2Dialect();
		} else if (dbType.equals(SqlTypeConst.MYSQL)) {
			dialect = new MySQLDialect();
		} else if (dbType.equals(SqlTypeConst.H2)) {
			dialect = new H2Dialect();
		} else if (dbType.equals(SqlTypeConst.DM)) {
			dialect = new DmDialect();
		} else {
			throw new Exception("没有设置合适的数据库类型");
		}
		return dialect;

	}
	
	/**
	 * 去到提示信息页。
	 * @param content
	 * @return
	 */
	public static ModelAndView getTipInfo(String content){
		ModelAndView mv=new ModelAndView("/platform/console/tipInfo.jsp");
		mv.addObject("content", content);
		return mv;
	}
	
	/**
	 * 去到提示信息页。
	 * @param request
	 * @param response
	 * @param content
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void gotoTipInfo(HttpServletRequest request,HttpServletResponse response, String content) throws ServletException, IOException{
		request.setAttribute("content", content);
		RequestDispatcher dispatcher=request.getRequestDispatcher("/platform/console/tipInfo.jsp");
		dispatcher.forward(request, response);
		
	}
	
	/**
	 * 根据字符串获取不重复的ID列表。
	 * @param ids
	 * @return
	 */
	public static List<Long> getListByStr(String ids) {
		String[] aryUid = ids.split("[,]");
		Set<Long> uidSet = new LinkedHashSet<Long>();
		for(String id:aryUid){
			Long orgId=Long.parseLong(id);
			uidSet.add(orgId);
		}
		List<Long> list=new ArrayList<Long>();
		list.addAll(uidSet);
		return list;
	}
	
	/**
	 * 替换模板中标题的标签。
	 * @param template		模板
	 * @param receiver		收件人
	 * @param sender		发件人
	 * @param subject		事项名称
	 * @param cause			原因
	 * @return
	 */
	public static String replaceTitleTag(String template,String receiver,String sender,String subject,String cause){
		if(StringUtil.isEmpty(template))
			return "";
		template = template.replace("${收件人}", receiver)
						   .replace("${发件人}", sender)
						   .replace("${转办人}", sender)
						   .replace("${代理人}", sender);
		if(StringUtil.isEmpty(cause))
			cause = "无";
		template=template.replace("${原因}", cause)
						 .replace("${逾期级别}", cause);

		template=template.replace("${事项名称}", subject);
		//替换特殊字符
		template=template.replaceAll("&nuot;","\n");
		return template;
	}
	
	
	/**
	 * 替换模板中的标签。
	 * @param template		模板
	 * @param receiver		收件人
	 * @param sender		发件人
	 * @param subject		主题
	 * @param url			url。
	 * @param cause			原因
	 * @param isSms			是否手机短信
	 * @return
	 */
	public static String replaceTemplateTag(String template,String receiver,String sender,String subject,String url,String cause,boolean isSms){
		if(StringUtil.isEmpty(template))
			return "";
		template = template.replace("${收件人}", receiver)
						   .replace("${发件人}", sender)
						   .replace("${转办人}", sender)
						   .replace("${代理人}", sender);
		if(StringUtil.isNotEmpty(cause)){
			template=template.replace("${原因}", cause)
							 .replace("${逾期级别}", cause)
							 .replace("${事项意见}", cause);
		}
		if(isSms || StringUtil.isEmpty(url)){
			template=template.replace("${事项名称}", subject);
		}else{
			template=template.replace("${事项名称}", "<a href='"+ url+"' target='_blank'>" + subject +"</a>");
		}
		//替换特殊字符
		template=template.replaceAll("&nuot;","\n");
		return template;
	}
	
	/**
	 * 获取任务或实例页面处理路径。
	 * @param id
	 * @param isTask
	 * @return
	 */
	public static String getUrl(String id,boolean isTask){
		if(BeanUtils.isEmpty(id)) return "";
		String url= AppConfigUtil.get("serverUrl");
		if(isTask){
			url+="/platform/bpm/task/toStart.ht?taskId=" + id;
		}
		else{
			 url+="/platform/bpm/processRun/info.ht?runId=" + id;     
		}
		return url;
	}
	
	public static String getProcessModuleType(int type){
		String info = "";
		switch (type) {
			case 1:
				info ="待办";
				break;
			case 2:
				info ="驳回";
				break;
			case 3:
				info ="撤销";
				break;
			case 4:
				info ="催办";
				break;
			case 5:
				info ="驳回发起人";
				break;
			case 6:
				info ="沟通反馈";
				break;
			case 7:
				info ="会话通知";
				break;
			case 8:
				info =" 转办";
				break;
			case 9:
				info ="代理";
				break;
			case 10:
				info ="取消转办";
				break;
			case 11:
				info ="取消代理";
				break;
			case 12:
				info ="归档";
				break;
			case 13:
				info ="抄送";
				break;
			case 14:
				info ="终止";
				break;
			case 15:
				info ="转发";
				break;
			case 17:
				info ="消息节点";
				break;
			default:
				break;
		}
		return info;	
	}
	
	/**
	 * 判断账号为空。
	 * @param assignee
	 * @return
	 */
	public static boolean isAssigneeEmpty(String assignee){
		if(StringUtil.isEmpty(assignee) || BpmConst.EMPTY_USER.equals(assignee)){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断任务执行人非空。
	 * @param assignee
	 * @return
	 */
	public static boolean isAssigneeNotEmpty(String assignee){
		return !isAssigneeEmpty(assignee);
	}
	
	/**
	 * 取得用户连接。
	 * @param userId
	 * @param fullName
	 * @return
	 */
	public static String getUserLink(String userId,String fullName){
		String url= AppConfigUtil.get("serverUrl");
		url+="/platform/system/sysUser/get.ht?userId=" + userId+"&hasClose=true";
		return "<a href='" +url +"'>"+ fullName +"</a>";
	}
	
	
	/**
	 * 配置文件中获取文件上传路径
	 * 如果为空则采用默认路径/attachFiles/temp
	 * @author hjx
	 * @version 创建时间：2013-11-4  下午3:46:28
	 * @return
	 */
	public static String getBasePath() {
		Properties configproperties=(Properties)AppUtil.getBean("configproperties");
		String attachPath = configproperties.getProperty("file.upload");
		if (StringUtil.isEmpty(attachPath)) {
			attachPath = AppUtil.getRealPath("/attachFiles/temp");
		}
		return attachPath;
	}
	
	
}
