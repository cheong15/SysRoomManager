package com.hotent.platform.controller.system;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hotent.core.engine.FreemarkEngine;
import com.hotent.core.util.FileUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.util.ZipUtil;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.platform.model.form.BpmFormDef;
import com.hotent.platform.model.form.BpmFormField;
import com.hotent.platform.model.form.BpmFormTable;
import com.hotent.platform.model.system.SysCodeTemplate;
import com.hotent.platform.service.form.BpmFormDefService;
import com.hotent.platform.service.form.BpmFormFieldService;
import com.hotent.platform.service.form.BpmFormHandlerService;
import com.hotent.platform.service.form.BpmFormTableService;
import com.hotent.platform.service.form.FormUtil;
import com.hotent.platform.service.form.ParseReult;
import com.hotent.platform.service.system.CodeUtil;
import com.hotent.platform.service.system.SysCodeTemplateService;
/**
 * 对象功能:基于自定义表的代码生成器 控制器类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2012-12-19 15:38:01
 */
@Controller
@RequestMapping("/platform/system/sysCodegen/")
public class SysCodegenController  extends BaseController  {
	@Resource
	private BpmFormDefService bpmFormDefService;
	@Resource
	private SysCodeTemplateService sysCodeTemplateService;
	@Resource
	private BpmFormFieldService bpmFormFieldService;
	@Resource
	private BpmFormTableService bpmFormTableService;
	@Resource 
	private BpmFormHandlerService bpmFormHandlerService;
	@Resource
	private FreemarkEngine freemarkEngine;
	
	/**
	 * 获取所有自定义表数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getTableData")
	public List<BpmFormDef> getTableData(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String subject=RequestUtil.getString(request, "subject","");
		List<BpmFormDef> bpmFormDefList=bpmFormDefService.getAllPublished(subject);
		return bpmFormDefList;
	}
	
	/**
	 * 代码模板文件列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("detail")
	public ModelAndView genDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<SysCodeTemplate> templateList=sysCodeTemplateService.getAll();
		return getAutoView().addObject("templateList",templateList);
	}
	
	/**
	 * 生成代码
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("codegen")
	public void codegen(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//文件相关参数
		String[] templateIds=request.getParameterValues("templateId");
		int override=RequestUtil.getInt(request, "override");
		String flowKey=RequestUtil.getString(request, "defKey");
		int isZip=RequestUtil.getInt(request, "isZip");
		String folderPath=RequestUtil.getString(request, "folderPath");
		String[] formDefIds=RequestUtil.getString(request, "formDefIds").split(",");
		//自定义表相关
		String basePath=RequestUtil.getString(request, "baseDir");
		String system=RequestUtil.getString(request, "system");
		List<String> codeFiles=new ArrayList<String>(); 
		List<BpmFormTable>list=getTableModels(request);
		try {
			for(String formDefId:formDefIds){
				BpmFormDef bpmFormDef=bpmFormDefService.getById(Long.parseLong(formDefId));
				List<BpmFormTable> tables=new ArrayList<BpmFormTable>();
				Long tableId=bpmFormDef.getTableId();
				for(BpmFormTable model:list){
					if(model.getTableId().equals(tableId)||model.getMainTableId().equals(tableId)){
						tables.add(model);
					}
				}
				List<String> fileList=genCode(basePath,system,tables,templateIds, override,flowKey,bpmFormDef);
				codeFiles.addAll(fileList);
			}
			//压缩生成文件到本地
			if(isZip==1){
				String toDir=folderPath+File.separator+"codegen";
				for(String filePath:codeFiles){
					FileUtil.createFolderFile(toDir+File.separator+filePath);
					FileUtil.copyFile(basePath+File.separator+filePath,toDir+File.separator+filePath);
				}
				ZipUtil.zip(toDir, true);
			}
			writeResultMessage(response.getWriter(), new ResultMessage(ResultMessage.Success, "自定义表生成代码成功"));
		} catch (Exception e) {
			writeResultMessage(response.getWriter(), new ResultMessage(ResultMessage.Fail, "自定义表生成代码失败:"+e.getMessage()));
		}
	}
	

	private List<String> genCode(String basePath,String system,List<BpmFormTable> tables, String[] templateIds,int override,String flowKey,BpmFormDef bpmFormDef) throws Exception {
		List<String> fileList=new ArrayList<String>();
		for(int i=0;i<templateIds.length;i++){
			Long templateId=Long.parseLong(templateIds[i]);
			SysCodeTemplate template=sysCodeTemplateService.getById(templateId);
			for(BpmFormTable table:tables){
				Map<String,String> variables=table.getVariable();
				variables.put("system", system);
				String fileName=template.getFileName();
				String fileDir=template.getFileDir();
				String path=basePath+File.separator+template.getFileDir();
				int isSub=template.getIsSub();
				Map<String,Object> model=new HashMap<String, Object>();
				if(template.getFormEdit()==1){
					model.put("html", getFormHtml(bpmFormDef,table,true));
				}
				if(template.getFormDetail()==1){
					model.put("html", getFormHtml(bpmFormDef,table,false));
				}
				model.put("table", table);
				model.put("system", system);
				if(StringUtil.isNotEmpty(flowKey)){
					model.put("flowKey", flowKey);
				}
				if(table.getIsMain()!=1){
					if(isSub==0){
						continue;
					}
				}
				String templateStr=template.getHtml();
				FreemarkEngine freemarkEngine=new FreemarkEngine();
				String html=freemarkEngine.parseByStringTemplate(model,templateStr);
				String fileStr=path+File.separator+fileName;
				String filePath=StringUtil.replaceVariable(fileStr, variables);
				addFile(filePath,html,override);
				String relativePath=StringUtil.replaceVariable(fileDir+File.separator+fileName, variables);
				fileList.add(relativePath);
			}
		}
		return fileList;
	}
	

	/**
	 * 根据前端所配置 获得表信息列表
	 * @param request
	 * @return
	 */
	private List<BpmFormTable> getTableModels(HttpServletRequest request){
		List<BpmFormTable> list=new ArrayList<BpmFormTable>();
		String[] tableIds=request.getParameterValues("tableId");
		String[] classNames=request.getParameterValues("className");
		String[] classVars=request.getParameterValues("classVar");
		String[] packageNames=request.getParameterValues("packageName");
		String system=request.getParameter("system");
		List<BpmFormTable> subtables=new ArrayList<BpmFormTable>();
		for(int i=0;i<tableIds.length;i++){
			Long tableId=Long.parseLong(tableIds[i]);
			Map<String,String> vars=new HashMap<String, String>();
			vars.put("class", classNames[i]);
			vars.put("classVar", classVars[i]);
			vars.put("package", packageNames[i]);
			vars.put("system", system);
			BpmFormTable bpmFormTable=bpmFormTableService.getById(tableId);
			bpmFormTable.setVariable(vars);
			List<BpmFormField> fieldList=bpmFormFieldService.getByTableIdContainHidden(tableId);
			List<BpmFormField> fields=new ArrayList<BpmFormField>();
			//字段值来源为脚本计算时，脚本去掉换行处理
			for(BpmFormField field:fieldList){
				String script="";
				for(String s:field.getScript().split("\n")){
					script+=s.trim();
				}
				field.setScript(script);
				if(bpmFormTable.getIsExternal()==1){
					field.setFieldName(field.getFieldName().toLowerCase());
				}
				String[] formDefIds=RequestUtil.getString(request, "formDefIds").split(",");
				try {
					for(String formDefId:formDefIds){
						BpmFormDef bpmFormDef=bpmFormDefService.getById(Long.parseLong(formDefId));
						String options = CodeUtil.getDialogTags(getFormHtml(bpmFormDef,bpmFormTable,true),field); 
						if(StringUtil.isNotEmpty(options)){
							field.setOptions(options);
						}
					}
				}catch(Exception e){}
				fields.add(field);
			}
			if(bpmFormTable.getIsExternal()==1){
				bpmFormTable.setPkField(bpmFormTable.getPkField().toLowerCase());
				if(bpmFormTable.getIsMain()!=1){
					bpmFormTable.setRelation(bpmFormTable.getRelation().toLowerCase());
				}
			}
			bpmFormTable.setFieldList(fields);
			if(bpmFormTable.getIsMain()!=1){
				subtables.add(bpmFormTable);
			}
			list.add(bpmFormTable);
		}
		for(BpmFormTable subtable:subtables){
			for(BpmFormTable table:list){
				if((table.getIsMain()==1)&&(table.getTableId().equals(subtable.getMainTableId()))){
					table.getSubTableList().add(subtable);
				}
			}
		}
		return list;
	}
	
	
	private String getFormHtml(BpmFormDef bpmFormDef, BpmFormTable table,
			boolean isEdit) throws Exception {
		String html=bpmFormDef.getHtml();
		if(BpmFormDef.DesignType_CustomDesign==bpmFormDef.getDesignType()){
			ParseReult result = FormUtil.parseHtmlNoTable(html, table.getTableName(), table.getTableDesc());		
			html = bpmFormHandlerService.obtainHtml(bpmFormDef.getTabTitle(), result, null);
		}
		String template=CodeUtil.getFreeMarkerTemplate(html, table, isEdit);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isEdit",isEdit);
		map.put("table", table);
		String output = freemarkEngine.parseByStringTemplate(map, template);
		return output;
		
	}
	

	private void addFile(String filePath, String html, int override) {
		File newFile=new File(filePath);
		if(newFile.exists()){
			if(override==1){
				FileUtil.deleteFile(filePath);
				FileUtil.writeFile(filePath, html);
			}
		}else{
			FileUtil.writeFile(filePath, html);
		}
	}

}
