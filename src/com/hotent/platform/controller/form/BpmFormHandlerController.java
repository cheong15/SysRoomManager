package com.hotent.platform.controller.form;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.gmcc.gz.service.sm.OpDataLogService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hotent.core.annotion.Action;
import com.hotent.core.util.ContextUtil;
import com.hotent.core.util.ExceptionUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.platform.model.form.BpmFormData;
import com.hotent.platform.model.form.BpmFormDef;
import com.hotent.platform.model.form.BpmFormTable;
import com.hotent.platform.model.form.PkValue;
import com.hotent.platform.service.bpm.thread.MessageUtil;
import com.hotent.platform.service.form.BpmFormDefService;
import com.hotent.platform.service.form.BpmFormHandlerService;
import com.hotent.platform.service.form.BpmFormTableService;
import com.hotent.platform.service.form.FormDataUtil;
import com.hotent.platform.service.form.FormUtil;
import com.hotent.platform.service.form.ParseReult;

/**
 * 对象功能:自定义表单数据处理
 * 开发公司:广州宏天软件有限公司
 * 开发人员:xwy 
 * 创建时间:2011-12-22 11:07:56
 */
@Controller
@RequestMapping("/platform/form/bpmFormHandler/")
public class BpmFormHandlerController extends BaseController {
	@Resource
	private BpmFormHandlerService service;

	@Resource
	private BpmFormDefService bpmFormDefService;
	
	@Resource
	private BpmFormTableService bpmFormTableService;


	@Resource
	private OpDataLogService opDataLogService;
	/**
	 * 表单预览
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description = "表单预览")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long formDefId = RequestUtil.getLong(request, "formDefId");
		String pkValue = request.getParameter("pkValue");
		String returnUrl = RequestUtil.getPrePage(request);
		String ctxPath=request.getContextPath();
		BpmFormDef bpmFormDef = null;

		if (formDefId != 0) {
			bpmFormDef = bpmFormDefService.getById(formDefId);	
			String html = service.obtainHtml(bpmFormDef, ContextUtil.getCurrentUserId(), pkValue, "", "", "",ctxPath);
			bpmFormDef.setHtml(html);
		} else {
			String html = request.getParameter("html");
			Long tableId = RequestUtil.getLong(request, "tableId");
			String title = RequestUtil.getString(request, "title");
			bpmFormDef = new BpmFormDef();
			bpmFormDef.setSubject(RequestUtil.getString(request, "name"));
			bpmFormDef.setTabTitle(title);
			bpmFormDef.setFormDesc(RequestUtil.getString(request, "comment"));
			if(tableId>0){				
				// 读取表。
				BpmFormTable bpmFormTable = bpmFormTableService.getTableById(tableId);
				ParseReult result = new ParseReult();
				result.setBpmFormTable(bpmFormTable);
				String template = FormUtil.getFreeMarkerTemplate(html, tableId);
				result.setTemplate(template);
				html = service.obtainHtml(title, result, null);
				html += service.getSubPermission(bpmFormTable, false);
				bpmFormDef.setHtml(html);
			}
			
		}		
		return getAutoView().addObject("bpmFormDef", bpmFormDef).addObject("returnUrl", returnUrl);
	}
	
	/**
	 * 业务表单。
	 * <pre>
	 * 1.输入表单key。
	 * 2.输入主键。
	 * </pre>
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("bizForm")
	@Action(description = "显示业务表单。" )
	public ModelAndView bizForm(HttpServletRequest request) throws Exception {
		Long formKey = RequestUtil.getLong(request, "formKey");
		String id = request.getParameter("id");
		boolean hasPk=StringUtil.isNotEmpty(id);
		String returnUrl = RequestUtil.getPrePage(request);
		String ctxPath = request.getContextPath();
		BpmFormDef bpmFormDef = null;
		String tableName="";
		String pkField="";
		if (formKey != 0) {
			bpmFormDef = bpmFormDefService.getDefaultVersionByFormKey(formKey);
			BpmFormTable bpmFormTable= bpmFormTableService.getById(bpmFormDef.getTableId());
			tableName=bpmFormTable.getTableDesc();
			String html = service.obtainHtml(bpmFormDef, ContextUtil.getCurrentUserId(), id, "", "", "",ctxPath);
			pkField=bpmFormTable.getPkField();
			bpmFormDef.setHtml(html);
		} 
		return getAutoView()
				.addObject("bpmFormDef", bpmFormDef)
				.addObject("id", id)
				.addObject("pkField", pkField)
				.addObject("tableName", tableName)
				.addObject("returnUrl", returnUrl)
				.addObject("hasPk",hasPk);
	}
	
	/**
	 * 删除数据
	 * <pre>
	 * 1.输入表单key。
	 * 2.输入主键。
	 * </pre>
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description = "删除数据。" )
	public void del(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String returnUrl=RequestUtil.getPrePage(request);
		Long formKey = RequestUtil.getLong(request, "formKey");
		String id = request.getParameter("id");
		BpmFormDef bpmFormDef = null;
		try{
			if (formKey != 0) {
				bpmFormDef = bpmFormDefService.getDefaultVersionByFormKey(formKey);
				BpmFormTable bpmFormTable= bpmFormTableService.getById(bpmFormDef.getTableId());			
				service.delById(id,bpmFormTable);
				addMessage(new ResultMessage(ResultMessage.Success, "删除业务数据成功"), request);
			}else{
				addMessage(new ResultMessage(ResultMessage.Fail, "删除业务数据失败,没有取得表名"), request);
			}			
		}
		catch(Exception ex){
			ex.printStackTrace();
			addMessage(new ResultMessage(ResultMessage.Fail, "删除业务数据失败"), request);
		}
		response.sendRedirect(returnUrl);
	}
	

	/**
	 * 保存业务数据。
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description = "添加或更新" )
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String data = request.getParameter("formData");
		Long tableId= RequestUtil.getLong(request, "tableId");
		BpmFormTable bpmFormTable=bpmFormTableService.getByTableId(tableId, 1);
		String id = request.getParameter(bpmFormTable.getPkField());
		try{
			String empid  = request.getParameter("EMPLOYEE_ID");
			Long recid  =  Long.parseLong(request.getParameter("recid"));
			if(StringUtil.isEmpty(id)){//新增
				BpmFormData bpmFormData=FormDataUtil.parseJson(data,bpmFormTable);
				//---------------------日志模块-------------------------//
				Map<String, Object> map = bpmFormData.getMainFields();//新增的值
				opDataLogService.opDataLog(map, empid, "1", tableId, "2", recid);
				//------------------------日志模块---------------------//
				service.handFormData(bpmFormData);
			}
			else{//修改
				PkValue pkValue=new PkValue(bpmFormTable.getPkField(), id);
				pkValue.setIsAdd(false);
				BpmFormData bpmFormData=FormDataUtil.parseJson(data,pkValue,bpmFormTable);
				//---------------------日志模块-------------------------//
				Map<String, Object> map = bpmFormData.getMainFields();//修改后的值
				opDataLogService.opDataLog(map, empid, "2", tableId, "2", recid);
				//------------------------日志模块---------------------//
				service.handFormData(bpmFormData);
			}
			
			writeResultMessage(response.getWriter(), "保存表单数据成功!", ResultMessage.Success);
		}
		catch(Exception ex){
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail,"保存表单数据失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(ex);
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
	}

}
