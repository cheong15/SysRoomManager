package com.hotent.platform.service.form;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import com.hotent.core.db.IEntityDao;
import com.hotent.core.db.JdbcHelper;
import com.hotent.core.db.entity.SQLClause;
import com.hotent.core.engine.FreemarkEngine;
import com.hotent.core.engine.GroovyScriptEngine;
import com.hotent.core.excel.Excel;
import com.hotent.core.excel.editor.IFontEditor;
import com.hotent.core.excel.reader.DataEntity;
import com.hotent.core.excel.reader.ExcelReader;
import com.hotent.core.excel.reader.FieldEntity;
import com.hotent.core.excel.reader.TableEntity;
import com.hotent.core.excel.style.Color;
import com.hotent.core.excel.style.font.BoldWeight;
import com.hotent.core.excel.style.font.Font;
import com.hotent.core.page.PageBean;
import com.hotent.core.page.PageUtils;
import com.hotent.core.service.BaseService;
import com.hotent.core.table.ColumnModel;
import com.hotent.core.table.DialectFactoryBean;
import com.hotent.core.table.TableModel;
import com.hotent.core.util.AppConfigUtil;
import com.hotent.core.util.ArrayUtil;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.ContextUtil;
import com.hotent.core.util.DateFormatUtil;
import com.hotent.core.util.JSONUtil;
import com.hotent.core.util.StringPool;
import com.hotent.core.util.StringUtil;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.platform.dao.form.BpmDataTemplateDao;
import com.hotent.platform.dao.form.BpmFormDefDao;
import com.hotent.platform.dao.form.BpmFormTemplateDao;
import com.hotent.platform.dao.system.SysDataSourceDao;
import com.hotent.platform.dao.system.SysUserOrgDao;
import com.hotent.platform.model.form.BpmDataTemplate;
import com.hotent.platform.model.form.BpmFormData;
import com.hotent.platform.model.form.BpmFormDef;
import com.hotent.platform.model.form.BpmFormField;
import com.hotent.platform.model.form.BpmFormTable;
import com.hotent.platform.model.form.BpmFormTemplate;
import com.hotent.platform.model.form.CommonVar;
import com.hotent.platform.model.form.FilterJsonStruct;
import com.hotent.platform.model.form.PkValue;
import com.hotent.platform.model.form.SubTable;
import com.hotent.platform.model.system.Position;
import com.hotent.platform.model.system.SysOrg;
import com.hotent.platform.model.system.SysRole;
import com.hotent.platform.model.system.SysUserOrg;
import com.hotent.platform.model.util.FieldPool;
import com.hotent.platform.service.system.PositionService;
import com.hotent.platform.service.system.SysOrgService;
import com.hotent.platform.service.system.SysRoleService;

import freemarker.template.TemplateException;

/**
 * <pre>
 * 对象功能:业务数据模板 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zxh
 * 创建时间:2013-09-05 14:14:50
 * </pre>
 */
@Service
public class BpmDataTemplateService extends BaseService<BpmDataTemplate> {
	@Resource
	private BpmDataTemplateDao dao;
	@Resource
	private BpmFormTemplateDao bpmFormTemplateDao;
	@Resource
	private FreemarkEngine freemarkEngine;
	@Resource
	private SysRoleService sysRoleService;
	@Resource
	private PositionService positionService;
	@Resource
	private SysOrgService sysOrgService;
	@Resource
	private SysUserOrgDao sysUserOrgDao;
	@Resource
	private BpmFormHandlerService bpmFormHandlerService;
	@Resource
	private SysDataSourceDao sysDataSourceDao;
	@Resource
	private GroovyScriptEngine groovyScriptEngine;
	@Resource
	private BpmFormTableService bpmFormTableService;
	@Resource
	private BpmFormDefDao bpmFormDefDao;

	public BpmDataTemplateService() {
	}

	@Override
	protected IEntityDao<BpmDataTemplate, Long> getEntityDao() {
		return dao;
	}

	public BpmDataTemplate getByFormKey(Long formKey) {
		return dao.getByFormKey(formKey);
	}

	/**
	 * 保存信息
	 * 
	 * @param bpmDataTemplate
	 * @param flag
	 *            是否是新增
	 * @throws Exception
	 */
	public void save(BpmDataTemplate bpmDataTemplate, boolean flag)
			throws Exception {

		String templateHtml = generateTemplate(bpmDataTemplate);
		bpmDataTemplate.setTemplateHtml(templateHtml);
		if (flag) {
			bpmDataTemplate.setId(UniqueIdUtil.genId());
			this.add(bpmDataTemplate);

		} else {
			this.update(bpmDataTemplate);
		}
	}

	/**
	 * 解析生成第一次的模板
	 * 
	 * @param bpmDataTemplate
	 * @return
	 * @throws Exception
	 */
	public String generateTemplate(BpmDataTemplate bpmDataTemplate)
			throws Exception {

		BpmFormTemplate bpmFormTemplate = bpmFormTemplateDao
				.getByTemplateAlias(bpmDataTemplate.getTemplateAlias());
		BpmFormTable bpmFormTable = bpmFormTableService.getByTableId(
				bpmDataTemplate.getTableId(), 1);
		// 是否有条件查询
		boolean hasCondition = hasCondition(bpmDataTemplate.getConditionField());
		// 是否有功能按钮
		boolean hasManage = hasManage(bpmDataTemplate.getManageField());
		// 第一次解析模板
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bpmDataTemplate", bpmDataTemplate);
		map.put("hasCondition", hasCondition);
		map.put("hasManage", hasManage);
		map.put("pkField", bpmFormTable.getPkField());
		map.put("formatData", getFormatDataMap(bpmFormTable));

		String templateHtml = freemarkEngine.parseByStringTemplate(map,
				bpmFormTemplate.getHtml());
		System.err.println(templateHtml);
		return templateHtml;

	}

	/**
	 * 是否有管理
	 * 
	 * @param manageField
	 * @return
	 */
	private boolean hasManage(String manageField) {
		if (StringUtils.isEmpty(manageField))
			return false;
		JSONArray jsonAry = JSONArray.fromObject(manageField);
		return jsonAry.size() > 0 ? true : false;
	}

	/**
	 * 是否有条件
	 * 
	 * @param conditionField
	 * @return
	 */
	private boolean hasCondition(String conditionField) {
		if (StringUtils.isEmpty(conditionField))
			return false;
		JSONArray jsonAry = JSONArray.fromObject(conditionField);
		return jsonAry.size() > 0 ? true : false;
	}

	/**
	 * 获得显示的字段的头
	 * 
	 * @param name
	 * @param desc
	 * @param permission
	 * @return
	 */
	public String getShowField(String name, String desc, String action,
			Map<String, String> sort, Map<String, Object> permission) {

		String html = "";
		if (BeanUtils.isEmpty(permission) || StringUtils.isEmpty(name))
			return html;
		String sortSeq = "";
		boolean isRight = (Boolean) permission.get(name);
		if (isRight) {
			if (BeanUtils.isNotEmpty(sort)) {
				String sortField = sort.get("sortField");
				String orderSeq = sort.get("orderSeq");
				if (name.equalsIgnoreCase(sortField))
					sortSeq = "ASC".equalsIgnoreCase(orderSeq) ? "↑" : "↓";
			}
			return "<th><a href='#' onclick='linkAjax(this)' action='" + action
					+ "'>" + desc + sortSeq + "</a></th>";
		}
		return "";
	}

	/**
	 * 获得显示的字段的头
	 * 
	 * @param name
	 * @param desc
	 * @param permission
	 * @return
	 */
	public String getShowField(String name, String html,
			Map<String, Object> permission) {
		if (BeanUtils.isEmpty(permission) || StringUtils.isEmpty(name))
			return html;
		boolean isRight = (Boolean) permission.get(name);
		if (isRight)
			return html;
		return "";
	}

	/**
	 * 显示列表的数据
	 * 
	 * @param name
	 * @param data
	 *            数据
	 * @param parse
	 *            处理的值
	 * @param permission
	 * @return
	 */
	public String getShowFieldList(String name, Map<String, Object> data,
			Map<String, Object> formatData, Map<String, Object> permission) {
		if (BeanUtils.isEmpty(permission) || StringUtils.isEmpty(name))
			return "";
		boolean isRight = (Boolean) permission.get(name);
		if (isRight) {
			Object o = data.get(name);
			return "<td>" + o + "</td>";

		}
		return "";
	}

	/**
	 * 显示功能按钮
	 * 
	 * @param name
	 *            权限类型名字
	 * @param desc
	 *            按钮名字
	 * @param pkField
	 *            主键字段
	 * @param actionUrl
	 *            按钮的url
	 * @param permission
	 *            权限
	 * @return
	 */
	public String getManage(String name, String desc, String pkField,
			Map<String, Object> data, Map<String, String> actionUrl,
			Map<String, Object> permission) {
		String html = "";
		if (BeanUtils.isEmpty(permission) || StringUtils.isEmpty(name)
				|| !permission.containsKey(name))
			return html;
		boolean isRight = (Boolean) permission.get(name);
		if (!isRight)
			return html;

		if (BpmDataTemplate.MANAGE_TYPE_ADD.equals(name)
				|| BpmDataTemplate.MANAGE_TYPE_EXPORT.equals(name)
				|| BpmDataTemplate.MANAGE_TYPE_EXPORT.equals(name))
			return html;
		Object pk = data.get(pkField);
		String action = actionUrl.get(name);
		// action
		if (BpmDataTemplate.MANAGE_TYPE_START.equals(name))
			action = action + "&businessKey=" + pk;
		else
			action = action + "&__pk__=" + pk;

		if (BpmDataTemplate.MANAGE_TYPE_EDIT.equals(name)) {// 编辑
			html = "<a href='#' onclick='openLinkDialog({scope:this,isFull:true})'  action='"
					+ action + "' class='link edit'>" + desc + "</a>";
		} else if (BpmDataTemplate.MANAGE_TYPE_DEL.equals(name)) {// 删除
			html = "<a href='" + action + "' class='link del'>" + desc + "</a>";
		} else if (BpmDataTemplate.MANAGE_TYPE_DETAIL.equals(name)) {// 明细
			html = "<a href='#' onclick='openLinkDialog({scope:this,isFull:true}) action='"
					+ action + "' class='link detail'>" + desc + "</a>";
		} else if (BpmDataTemplate.MANAGE_TYPE_START.equals(name)) {// 启动
			html = "<a class='link run' href='#' onclick='openLinkDialog({scope:this,isFull:true}) action='"
					+ action + "'>" + desc + "</a>";
		}

		return html;
	}

	/**
	 * 获取toolbar的按钮
	 * 
	 * @param name
	 *            权限类型名字
	 * @param desc
	 *            按钮名字
	 * @param actionUrl
	 *            按钮的url
	 * @param permission
	 *            权限
	 * @return
	 */
	public String getToolBar(String name, String desc,
			Map<String, String> actionUrl, Map<String, Object> permission) {
		String html = "";
		if (BeanUtils.isEmpty(permission) || StringUtils.isEmpty(name)
				|| !permission.containsKey(name))
			return html;
		boolean isRight = (Boolean) permission.get(name);
		if (!isRight)
			return html;

		if (BpmDataTemplate.MANAGE_TYPE_EDIT.equals(name)
				|| BpmDataTemplate.MANAGE_TYPE_DEL.equals(name)
				|| BpmDataTemplate.MANAGE_TYPE_DETAIL.equals(name))
			return html;
		String action = actionUrl.get(name);
		if (BpmDataTemplate.MANAGE_TYPE_ADD.equals(name)) {// 新增
			html = "<div class='group'>"
					+ "<a class='link add' href='#' onclick='openLinkDialog({scope:this,isFull:true}) action='"
					+ action + "'><span></span>" + desc + "</a></div>"
					+ "<div class='l-bar-separator'></div>";
		} else if (BpmDataTemplate.MANAGE_TYPE_EXPORT.equals(name)) {// 导出
			html = "<div class='group'>"
					+ "<a class='link export' href='#' onclick='exportExcel(this)' action='"
					+ action + "'><span></span>" + desc + "</a></div>"
					+ "<div class='l-bar-separator'></div>";
		} else if (BpmDataTemplate.MANAGE_TYPE_PRINT.equals(name)) {// 打印
			html = "<div class='group'>"
					+ "<a class='link print' href='#' onclick='' action='"
					+ action + "'><span></span>" + desc + "</a></div>"
					+ "<div class='l-bar-separator'></div>";
		} else if (BpmDataTemplate.MANAGE_TYPE_START.equals(name)) {// 启动
			html = "<div class='group'>"
					+ "<a class='link run' href='#' onclick='openLinkDialog({scope:this,isFull:true}) action='"
					+ action + "'><span></span>" + desc + "</a></div>"
					+ "<div class='l-bar-separator'></div>";
		}
		return html;

	}

	/**
	 * 预览数据//TODO
	 * 
	 * @param id
	 * @param params
	 * @param queryParams
	 * @return
	 * @throws Exception
	 */
	public String getDisplay(Long id, Map<String, Object> params,
			Map<String, Object> queryParams) throws Exception {
		Long curUserId = ContextUtil.getCurrentUserId();
		Long curOrgId = ContextUtil.getCurrentOrgId();

		CommonVar.setCommonVar(params, curUserId, curOrgId);

		HttpServletRequest request = RequestUtil.getHttpServletRequest();
		// 获取权限map
		Map<String, Object> rightMap = this.getRightMap(curUserId, curOrgId);
		BpmDataTemplate bpmDataTemplate = dao.getById(id);
		if (!params.containsKey(BpmDataTemplate.PARAMS_KEY_ISQUERYDATA))
			params.put(BpmDataTemplate.PARAMS_KEY_ISQUERYDATA,
					bpmDataTemplate.getIsQuery() == 0);

		BpmFormTable bpmFormTable = bpmFormTableService.getByTableId(
				bpmDataTemplate.getTableId(), 1);
		Map<String, Object> formatData = this.getFormatDataMap(bpmFormTable);

		String baseURL = (String) params.get("__baseURL");
		// 过滤的权限
		bpmDataTemplate = this.getRightFilterField(bpmDataTemplate, rightMap,
				baseURL);
		String filterKey = this.getFilterKey(bpmDataTemplate, params);
		// 构建URL
		String tableIdCode = (String) params.get("__tic");
		if (tableIdCode == null) {
			tableIdCode = "";
			params.put("__tic", "");
		}
		// 排序
		Map<String, String> sortMap = getSortMap(params, tableIdCode);

		// 自定义参数
		parseCustomParameterAsMap(bpmDataTemplate, request);

		// 取得当前过滤的条件
		JSONObject filterJson = this.getFilterFieldJson(bpmDataTemplate,
				rightMap, params);

		boolean isQueryData = (Boolean) params
				.get(BpmDataTemplate.PARAMS_KEY_ISQUERYDATA);
		if (isQueryData)
			// 取得数据
			bpmDataTemplate = this.getData(bpmDataTemplate, bpmFormTable,
					rightMap, params, sortMap, formatData, filterJson);

		// 需要排除的url
		List<String> excludes = this.getExcludes(params, queryParams);

		String pageURL = this.addParametersToUrl(baseURL, params, excludes);
		String searchFormURL = baseURL;

		String templateHtml = bpmDataTemplate.getTemplateHtml();
		// 如果取不到默认模板，就重新解析
		if (StringUtil.isEmpty(templateHtml))
			templateHtml = this.generateTemplate(bpmDataTemplate);

		Map<String, Object> map = new HashMap<String, Object>();
		// 获得分页的数据
		String pageHtml = this.getPageHtml(bpmDataTemplate, map, tableIdCode,
				pageURL);
		Map<String, Boolean> managePermission = getManagePermission(
				BpmDataTemplate.RIGHT_TYPE_MANAGE,
				bpmDataTemplate.getManageField(), rightMap);
		params.put("__defId__", bpmDataTemplate.getDefId());
		// 第二次解析模板
		map.clear();
		map.put("bpmDataTemplate", bpmDataTemplate);
		map.put("pageHtml", pageHtml);
		map.put("pageURL", pageURL);
		map.put("sort", sortMap);
		map.put("sortField", sortMap.get("sortField"));
		map.put("orderSeq", sortMap.get("orderSeq"));
		map.put("tableIdCode", tableIdCode);
		map.put("searchFormURL", searchFormURL);
		// map.put("VarMap", map.getParameterMap());// custom parameter
		map.put("service", this);
		// 当前字段的权限
		map.put("permission",
				getPermission(BpmDataTemplate.RIGHT_TYPE_SHOW,
						bpmDataTemplate.getDisplayField(), rightMap));
		// 功能按钮的权限
		map.put("managePermission", managePermission);
		// actionUrl
		map.put("actionUrl", getActionUrl(params));
		map.put("filterKey", filterKey);
		map.put("checkbox", isCheckbox(managePermission));
		map.put("formatData", formatData);
		map.put("param", queryParams);

		String html = freemarkEngine.parseByStringTemplate(map, templateHtml);
		// logger.info(html);
		return html;
	}

	/**
	 * 获取格式化的数据
	 * 
	 * @param bpmFormTable
	 * @return
	 */
	private Map<String, Object> getFormatDataMap(BpmFormTable bpmFormTable) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<BpmFormField> fieldList = bpmFormTable.getFieldList();
		for (BpmFormField bpmFormField : fieldList) {
			String name = bpmFormField.getFieldName();
			Short controlType = bpmFormField.getControlType();
			String fieldType = bpmFormField.getFieldType();

			if (BpmFormField.DATATYPE_DATE.equals(fieldType)) {
				map.put(name, bpmFormField.getDatefmt());
			} else {
				if (controlType.shortValue() == FieldPool.SELECT_INPUT
						|| controlType.shortValue() == FieldPool.RADIO_INPUT
						|| controlType.shortValue() == FieldPool.CHECKBOX) {
					String options = bpmFormField.getJsonOptions();

					if (StringUtils.isEmpty(options))
						continue;

					Map<String, String> optionMap = new HashMap<String, String>();
					JSONArray jarray = JSONArray.fromObject(options);
					for (Object obj : jarray) {
						JSONObject json = (JSONObject) obj;
						String key = (String) json.get("key");
						String value = (String) json.get("value");
						optionMap.put(key, value);
					}
					map.put(name, optionMap);
				}

			}
		}
		return map;
	}

	/**
	 * 是否有选择框
	 * 
	 * <pre>
	 * 	导出、打印
	 * </pre>
	 * 
	 * @param managePermission
	 * @return
	 */
	private boolean isCheckbox(Map<String, Boolean> managePermission) {
		if (BeanUtils.isEmpty(managePermission))
			return false;
		if (managePermission.containsKey(BpmDataTemplate.MANAGE_TYPE_EXPORT)) {
			if (managePermission.get(BpmDataTemplate.MANAGE_TYPE_EXPORT))
				return true;
		}
		if (managePermission.containsKey(BpmDataTemplate.MANAGE_TYPE_PRINT)) {
			if (managePermission.get(BpmDataTemplate.MANAGE_TYPE_PRINT))
				return true;
		}
		return false;
	}

	/**
	 * 获取排序的字段和标识
	 * 
	 * @param params
	 * @param tableIdCode
	 * @return
	 */
	private Map<String, String> getSortMap(Map<String, Object> params,
			String tableIdCode) {
		Map<String, String> sortMap = new HashMap<String, String>();
		// 排序
		String sortField = null;
		String orderSeq = "DESC";
		String newSortField = null;
		if (params.get(tableIdCode + BpmDataTemplate.SORTFIELD) != null)
			sortField = (String) params.get(tableIdCode
					+ BpmDataTemplate.SORTFIELD);

		if (params.get(tableIdCode + BpmDataTemplate.ORDERSEQ) != null)
			orderSeq = (String) params.get(tableIdCode
					+ BpmDataTemplate.ORDERSEQ);

		if (params.get(tableIdCode + "__ns__") != null)
			newSortField = (String) params.get(tableIdCode + "__ns__");
		if (StringUtil.isNotEmpty(newSortField)) {
			if (newSortField.equals(sortField)) {
				if (orderSeq.equals("ASC")) {
					orderSeq = "DESC";
				} else {
					orderSeq = "ASC";
				}
			}
			sortField = newSortField;
			params.put(tableIdCode + BpmDataTemplate.SORTFIELD, sortField);
			params.put(tableIdCode + BpmDataTemplate.ORDERSEQ, orderSeq);

			sortMap.put("sortField", sortField);
			sortMap.put("orderSeq", orderSeq);
		}

		return sortMap;
	}

	/**
	 * 排除的参数
	 * 
	 * @param params
	 * @param queryParams
	 * @return
	 */
	private List<String> getExcludes(Map<String, Object> params,
			Map<String, Object> queryParams) {

		List<String> excludes = new ArrayList<String>();
		for (String key : params.keySet()) {
			if (key.endsWith("__ns__")) {
				excludes.add(key);
			}
			if (key.endsWith("__pk__")) {
				excludes.add(key);
			}
		}
		excludes.add("rand");
		excludes.add("__baseURL");
		excludes.add("__tic");
		excludes.add("__displayId");
		// 排除查询的
		for (Entry<String, Object> entry : queryParams.entrySet()) {
			String key = entry.getKey();
			if (!key.startsWith("Q_"))
				continue;
			String[] aryParaKey = key.split("_");
			if (aryParaKey.length < 3)
				continue;
			String paraName = key.substring(2, key.lastIndexOf("_"));
			excludes.add(paraName);
		}

		return excludes;
	}

	/**
	 * 过滤条件KEY
	 * 
	 * @param bpmDataTemplate
	 * @param params
	 * @return
	 */
	private String getFilterKey(BpmDataTemplate bpmDataTemplate,
			Map<String, Object> params) {
		Object key = params.get(BpmDataTemplate.PARAMS_KEY_FILTERKEY);
		if (BeanUtils.isNotEmpty(key))
			return (String) key;
		String filterField = bpmDataTemplate.getFilterField();
		if (StringUtils.isEmpty(filterField))
			return "";
		JSONArray jsonAry = JSONArray.fromObject(filterField);
		if (JSONUtils.isNull(jsonAry) || jsonAry.size() == 0)
			return "";
		JSONObject jsonObj = jsonAry.getJSONObject(0);
		String filterKey = jsonObj.getString("key");
		params.put(BpmDataTemplate.PARAMS_KEY_FILTERKEY, filterKey);
		return filterKey;
	}

	/**
	 * 获取有权限的过滤字段
	 * 
	 * @param bpmDataTemplate
	 * @param rightMap
	 * @param baseURL
	 * @return
	 */
	private BpmDataTemplate getRightFilterField(
			BpmDataTemplate bpmDataTemplate, Map<String, Object> rightMap,
			String baseURL) {
		String filterField = bpmDataTemplate.getFilterField();
		JSONArray jsonArray = JSONArray.fromObject(filterField);
		String destFilterField = new JSONArray().toString();
		if (JSONUtil.isEmpty(jsonArray)) {
			bpmDataTemplate.setFilterField(destFilterField);
			return bpmDataTemplate;
		}
		baseURL = baseURL.replace("/getDisplay.ht", "/preview.ht");
		String url = baseURL + "?__displayId__=" + bpmDataTemplate.getId();
		// 有权限过滤条件
		JSONArray jsonAry = new JSONArray();
		for (Object obj : jsonArray) {
			JSONObject jObj = (JSONObject) obj;
			JSONArray rightAry = JSONArray.fromObject(jObj.get("right"));
			JSONObject permission = (JSONObject) rightAry.getJSONObject(0);
			if (hasRight(permission, rightMap))
				jsonAry.add(obj);
		}
		if (JSONUtil.isEmpty(jsonAry)) {
			bpmDataTemplate.setFilterField(destFilterField);
			return bpmDataTemplate;
		}
		JSONArray destJsonAry = new JSONArray();
		for (Object obj : jsonAry) {
			JSONObject json = (JSONObject) obj;
			String name = json.getString("name");
			String key = json.getString("key");
			json.accumulate("desc", StringUtil.subString(name, 5, "..."));// 展示字段
			json.accumulate("url", url + "&"
					+ BpmDataTemplate.PARAMS_KEY_FILTERKEY + "=" + key);
			destJsonAry.add(json);
		}
		bpmDataTemplate.setFilterField(destJsonAry.toString());
		return bpmDataTemplate;
	}

	/**
	 * 
	 * 获取Action的URl
	 * 
	 * @param params
	 * @return
	 */
	private Map<String, String> getActionUrl(Map<String, Object> params) {
		Map<String, String> map = new HashMap<String, String>();
		String __baseURL = (String) params.get("__baseURL");
		String __ctx = (String) params.get(BpmDataTemplate.PARAMS_KEY_CTX);
		Long defId = (Long) params.get(BpmDataTemplate.PARAMS_KEY_DEFID);

		String __displayId__ = "?__displayId__="
				+ (String) params.get("__displayId__");

		String addBaseURL = __baseURL.replace("/getDisplay.ht", "/editData.ht")
				+ __displayId__;
		String editBaseURL = __baseURL
				.replace("/getDisplay.ht", "/editData.ht") + __displayId__;
		String deleteBaseURL = __baseURL.replace("/getDisplay.ht",
				"/deleteData.ht") + __displayId__;
		String detailBaseURL = __baseURL.replace("/getDisplay.ht",
				"/detailData.ht") + __displayId__;
		String startBaseURL = BeanUtils.isEmpty(defId) ? "" : (__ctx
				+ "/platform/bpm/task/startFlowForm.ht?defId=" + defId);
		// 导出
		String exportBaseURL = __baseURL.replace("/getDisplay.ht",
				"/exportData.ht")
				+ __displayId__
				+ '&'
				+ BpmDataTemplate.PARAMS_KEY_FILTERKEY
				+ "="
				+ params.get(BpmDataTemplate.PARAMS_KEY_FILTERKEY);

		// 导入
		String importBaseURL = __baseURL.replace("/getDisplay.ht",
				"/importData.ht") + __displayId__;

		map.put(BpmDataTemplate.MANAGE_TYPE_ADD, addBaseURL);
		map.put(BpmDataTemplate.MANAGE_TYPE_EDIT, editBaseURL);
		map.put(BpmDataTemplate.MANAGE_TYPE_DEL, deleteBaseURL);
		map.put(BpmDataTemplate.MANAGE_TYPE_DETAIL, detailBaseURL);
		map.put(BpmDataTemplate.MANAGE_TYPE_START, startBaseURL);
		map.put(BpmDataTemplate.MANAGE_TYPE_EXPORT, exportBaseURL);
		map.put(BpmDataTemplate.MANAGE_TYPE_IMPORT, importBaseURL);
		return map;
	}

	/**
	 * 获取当前用户的权限Map
	 * 
	 * @param userId
	 * @param curOrgId
	 * @return
	 */
	private Map<String, Object> getRightMap(Long userId, Long curOrgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<SysRole> roles = sysRoleService.getByUserId(userId);
		List<Position> positions = positionService.getByUserId(userId);
		List<SysOrg> orgs = sysOrgService.getOrgsByUserId(userId);
		// 获取可以管理的组织列表。
		List<SysUserOrg> ownOrgs = sysUserOrgDao
				.getChargeOrgByUserId(userId);

		map.put("userId", userId);
		map.put("curOrgId", curOrgId);
		map.put("roles", roles);
		map.put("positions", positions);
		map.put("orgs", orgs);
		map.put("ownOrgs", ownOrgs);
		return map;
	}

	/**
	 * 获取管理的权限
	 * 
	 * @param type
	 * @param bpmDataTemplate
	 * @param roles
	 * @param positions
	 * @param orgs
	 * @param ownOrgs
	 * @param userId
	 * @return
	 */
	private Map<String, Boolean> getManagePermission(int type,
			String manageField, Map<String, Object> rightMap) {
		if (StringUtils.isEmpty(manageField))
			return null;
		JSONArray jsonAry = JSONArray.fromObject(manageField);
		return getPermissionMap(type, jsonAry, rightMap);
	}

	/**
	 * 获取字段的权限
	 * 
	 * @param userId
	 * @param type
	 * @param bpmDataTemplate
	 * @return
	 */
	private Map<String, Boolean> getPermission(int type, String displayField,
			Map<String, Object> rightMap) {
		JSONArray jsonAry = JSONArray.fromObject(displayField);
		return getPermissionMap(type, jsonAry, rightMap);
	}

	/**
	 * 获取权限map
	 * 
	 * @param jsonAry
	 * @param type
	 * @param rightMap
	 * @return
	 */
	private Map<String, Boolean> getPermissionMap(int type, JSONArray jsonAry,
			Map<String, Object> rightMap) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		if (JSONUtil.isEmpty(jsonAry))
			return map;
		for (Object obj : jsonAry) {
			JSONObject json = JSONObject.fromObject(obj);
			String name = (String) json.get("name");
			JSONArray rights = (JSONArray) json.get("right");
			for (Object right : rights) {
				JSONObject rightJson = JSONObject.fromObject(right);
				Integer s = (Integer) rightJson.get("s");
				if (s.intValue() == type)
					map.put(name, this.hasRight(rightJson, rightMap));
			}
		}
		return map;
	}

	/**
	 * 判断是否有权限。
	 * 
	 * @param permission
	 * @param rightMap
	 *            权限map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean hasRight(JSONObject permission, Map<String, Object> rightMap) {
		boolean hasRight = false;
		String type = permission.get("type").toString();
		String id = permission.get("id").toString();
		Object script = permission.get("script");
		if ("none".equals(type)) // 无
			return false;

		if ("everyone".equals(type))// 所有人
			return true;

		Long userId = (Long) rightMap.get("userId");
		Long curOrgId = (Long) rightMap.get("curOrgId");
		List<SysRole> roles = (List<SysRole>) rightMap.get("roles");
		List<Position> positions = (List<Position>) rightMap.get("positions");
		List<SysOrg> orgs = (List<SysOrg>) rightMap.get("orgs");
		List<SysUserOrg> ownOrgs = (List<SysUserOrg>) rightMap.get("ownOrgs");
		// 指定用户
		if ("user".equals(type)) {
			hasRight = StringUtil.contain(id, userId.toString());
			return hasRight;
		}
		// 指定角色
		else if ("role".equals(type)) {
			for (SysRole role : roles) {
				if (StringUtil.contain(id, role.getRoleId().toString())) {
					return true;
				}
			}
		}
		// 指定组织
		else if ("org".equals(type)) {
			for (SysOrg org : orgs) {
				if (StringUtil.contain(id, org.getOrgId().toString())) {
					return true;
				}
			}
		}
		// 组织负责人
		else if ("orgMgr".equals(type)) {
			for (SysUserOrg sysUserOrg : ownOrgs) {
				if (StringUtil.contain(id, sysUserOrg.getOrgId().toString())) {
					return true;
				}
			}
		}
		// 岗位
		else if ("pos".equals(type)) {
			for (Position position : positions) {
				if (StringUtil.contain(id, position.getPosId().toString())) {
					return true;
				}
			}
		} else if ("script".equals(type)) {
			if (BeanUtils.isEmpty(script))
				return false;
			Map<String, Object> map = new HashMap<String, Object>();
			CommonVar.setCommonVar(map, userId, curOrgId);
			return groovyScriptEngine.executeBoolean(script.toString(), map);
		}
		return false;
	}

	private String addParametersToUrl(String url, Map<String, Object> params,
			List<String> excludes) {
		StringBuffer sb = new StringBuffer();
		int idx1 = url.indexOf("?");
		if (idx1 > 0) {
			sb.append(url.substring(0, idx1));
		} else {
			sb.append(url);
		}
		sb.append("?");

		Map<String, Object> map = getQueryStringMap(url);
		if (BeanUtils.isNotEmpty(params)) {
			map.putAll(params);
		}
		// 排除
		if (excludes != null) {
			for (String ex : excludes) {
				map.remove(ex);
			}
		}

		for (Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object val = entry.getValue();
			if (BeanUtils.isEmpty(val))
				continue;
			sb.append(key);
			sb.append("=");
			sb.append(val);
			sb.append("&");
		}
		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * 获得查询的Map
	 * 
	 * @param url
	 * @return
	 */
	private static Map<String, Object> getQueryStringMap(String url) {
		Map<String, Object> map = new HashMap<String, Object>();
		int idx1 = url.indexOf("?");
		if (idx1 > 0) {
			String queryStr = url.substring(idx1 + 1);
			String[] queryNodeAry = queryStr.split("&");
			for (String queryNode : queryNodeAry) {
				String[] strAry = queryNode.split("=");
				if (strAry.length == 1) {
					map.put(strAry[0], null);
				} else {
					map.put(strAry[0].trim(), strAry[1]);
				}
			}
		}
		return map;
	}

	/**
	 * 取的数据
	 * 
	 * @param bpmDataTemplate
	 * @param bpmFormTable
	 * @param rightMap
	 *            权限数据
	 * @param params
	 *            页面传过来的参数
	 * @param sortMap
	 *            排序map
	 * @param formatData
	 *            格式化的数据
	 * @param filterJson
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private BpmDataTemplate getData(BpmDataTemplate bpmDataTemplate,
			BpmFormTable bpmFormTable, Map<String, Object> rightMap,
			Map<String, Object> params, Map<String, String> sortMap,
			Map<String, Object> formatData, JSONObject filterJson)
			throws Exception {

		JdbcHelper jdbcHelper = this.getJdbcHelper(bpmFormTable);

		String tableIdCode = "";
		if (params.get("__tic") != null) {
			tableIdCode = (String) params.get("__tic");
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String sql = "";
		// 是否需要分页。
		if (bpmDataTemplate.getNeedPage().shortValue() == 1) {
			int currentPage = 1;
			Object pageObj = params.get(tableIdCode + BpmDataTemplate.PAGE);
			if (pageObj != null) {
				currentPage = Integer.parseInt(params.get(
						tableIdCode + BpmDataTemplate.PAGE).toString());
			}
			int pageSize = bpmDataTemplate.getPageSize();
			Object pageSizeObj = params.get(tableIdCode
					+ BpmDataTemplate.PAGESIZE);
			if (pageSizeObj != null) {
				pageSize = Integer.parseInt(params.get(
						tableIdCode + BpmDataTemplate.PAGESIZE).toString());
			}

			// 获取SQL
			sql = this.getSQL(filterJson, bpmDataTemplate, bpmFormTable,
					rightMap, params, sortMap);

			Object oldPageSizeObj = params.get(tableIdCode + "oz");

			int oldPageSize = bpmDataTemplate.getPageSize();
			if (oldPageSizeObj != null) {
				oldPageSize = Integer.parseInt(params.get(tableIdCode + "oz")
						.toString());
			}
			if (pageSize != oldPageSize) {
				int first = PageUtils.getFirstNumber(currentPage, oldPageSize);
				currentPage = first / pageSize + 1;
			}
			PageBean pageBean = new PageBean(currentPage, pageSize);
			// 参数
			// params.putAll();

			list = jdbcHelper.getPage(currentPage, pageSize, sql, params,
					pageBean);
			bpmDataTemplate.setPageBean(pageBean);
		} else {
			// 获取数据
			sql = this.getSQL(filterJson, bpmDataTemplate, bpmFormTable,
					rightMap, params, sortMap);
			list = jdbcHelper.queryForList(sql, params);
		}
		// 整理的是List
		if (BeanUtils.isNotEmpty(list))
			list = getDataList(list, bpmFormTable, formatData, true);
		bpmDataTemplate.setList(list);

		logger.info("查询的SQL：" + sql);
		return bpmDataTemplate;
	}

	private JSONObject getFilterFieldJson(BpmDataTemplate bpmDataTemplate,
			Map<String, Object> rightMap, Map<String, Object> params) {
		JSONObject filterJson = getFilterFieldJson(
				bpmDataTemplate.getFilterField(), rightMap, params);
		if (JSONUtil.isEmpty(filterJson)
				&& bpmDataTemplate.getIsFilter().shortValue() == 0) {
			JSONArray jsonAry = new JSONArray();
			jsonAry.add(getDefaultFilterJson());
			bpmDataTemplate.setFilterField(jsonAry.toString());
		}
		return filterJson;
	}

	/**
	 * 获取过滤字段的JSON 如果没有值取默认
	 * 
	 * @param filterField
	 * @param isFilter
	 * @param rightMap
	 * @param params
	 * @return
	 */
	private JSONObject getFilterFieldJson(String filterField,
			Map<String, Object> rightMap, Map<String, Object> params) {
		JSONObject filterJson = this.getFilterJson(filterField, rightMap,
				params);

		return filterJson;
	}

	/**
	 * 默认的条件
	 * 
	 * @return
	 */
	private JSONObject getDefaultFilterJson() {
		return JSONObject
				.fromObject("{\"name\":\"默认条件 \",\"key\":\"Default\",\"type\":\"1\",\"condition\":\"[]\",\"right\":[{\"s\":3,\"type\":\"everyone\",\"id\":\"\",\"name\":\"\",\"script\":\"\"}]}");
	}

	/**
	 * 获取整理过的数据
	 * 
	 * @param list
	 * @param bpmFormTable
	 * @param formatData
	 * @param isMain
	 * @return
	 */
	private List<Map<String, Object>> getDataList(
			List<Map<String, Object>> list, BpmFormTable bpmFormTable,
			Map<String, Object> formatData, boolean isMain) {
		String source = bpmFormTable.getIsExternal() == BpmFormTable.NOT_EXTERNAL ? BpmDataTemplate.SOURCE_CUSTOM_TABLE
				: BpmDataTemplate.SOURCE_OTHER_TABLE;
		String pkField = bpmFormTable.getPkField();
		String relation = StringUtils.isEmpty(bpmFormTable.getRelation()) ? TableModel.FK_COLUMN_NAME
				: bpmFormTable.getRelation();
		List<BpmFormField> fieldList = bpmFormTable.getFieldList();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			Map<String, Object> mapData = new HashMap<String, Object>();
			for (BpmFormField bpmFormField : fieldList) {
				String name = bpmFormField.getFieldName();
				String nameUp = bpmFormField.getFieldName().toUpperCase();
				String fixFieldName = this.fixFieldName(nameUp, source);
				Object o = null;
				if (map.containsKey(fixFieldName))
					o = getFormatData(map.get(fixFieldName), name, formatData);
				mapData.put(name, o);
			}
			// 加入主键
			mapData.put(pkField, map.get(pkField));
			// 加入外键
			if (!isMain)
				mapData.put(relation, map.get(relation));

			dataList.add(mapData);
		}
		return dataList;
	}

	/**
	 * 获取数据格式化的值
	 * 
	 * @param o
	 * @param name
	 * @param parse
	 * @return
	 */
	private Object getFormatData(Object o, String name,
			Map<String, Object> formatData) {
		if (BeanUtils.isEmpty(o))
			return "";
		// 如果是日期的
		if (o instanceof Date) {
			String style = StringPool.DATE_FORMAT_DATE;
			if (formatData.containsKey(name)) {
				Object format = formatData.get(name);
				if (BeanUtils.isNotEmpty(format))
					style = (String) format;
			}
			o = DateFormatUtil.format((Date) o, style);
		}
		// 如果是 下拉框，单选框的
		else if (formatData.containsKey(name)) {
			Object obj = formatData.get(name);
			if (BeanUtils.isNotEmpty(obj)) {
				if (obj instanceof Map) {
					Map<?, ?> map = (Map<?, ?>) obj;
					o = map.get(o);
					if (BeanUtils.isEmpty(o))
						o = "";
				}
			}
		}
		return o;
	}

	/**
	 * 通过自定义表获取数据源
	 * 
	 * @param bpmFormTable
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private JdbcHelper getJdbcHelper(BpmFormTable bpmFormTable)
			throws Exception {
		JdbcHelper jdbcHelper = null;
		// 外部数据源
		if (bpmFormTable.isExtTable()) {
			jdbcHelper = this.getJdbcHelper(bpmFormTable.getDsAlias());
		} else {
			jdbcHelper = this.getConfigJdbcHelper();
		}
		return jdbcHelper;
	}

	/**
	 * 从配置文件中读取配置属性数据的信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private JdbcHelper getConfigJdbcHelper() throws Exception {
		String dbType = AppConfigUtil.get("jdbc.dbType");
		String className = AppConfigUtil.get("jdbc.driverClassName");
		String url = AppConfigUtil.get("jdbc.url");
		String user = AppConfigUtil.get("jdbc.username");
		String pwd = AppConfigUtil.get("jdbc.password");
		return JdbcHelper
				.getJdbcHelper(user, className, url, user, pwd, dbType);
	}

	/**
	 * 根据数据源获取JdbcHelper。
	 * 
	 * @param dsName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private JdbcHelper getJdbcHelper(String dsAlias) throws Exception {
		if ("LOCAL".equalsIgnoreCase(dsAlias))// 如果别名是LOCAL则是本地数据源
			return getConfigJdbcHelper();

		DriverManagerDataSource dataSource = sysDataSourceDao
				.getDriverMangerDataSourceByAlias(dsAlias);
		JdbcHelper<?, ?> jdbcHelper = JdbcHelper.getInstance();
		String className = dataSource.getConnectionProperties().getProperty(
				"driverClassName");

		jdbcHelper.init(dsAlias, className, dataSource.getUrl(),
				dataSource.getUsername(), dataSource.getPassword());
		jdbcHelper.setCurrentDb(dsAlias);
		jdbcHelper.setDialect(DialectFactoryBean
				.getDialectByDriverClassName(className));
		return jdbcHelper;
	}

	/**
	 * TODO 获取拼接的SQL
	 * 
	 * @param filterJson
	 * 
	 * @param bpmDataTemplate
	 * @param bpmFormTable
	 * @param rightMap
	 * @param params
	 * @param sortMap
	 * @return
	 */
	private String getSQL(JSONObject filterJson,
			BpmDataTemplate bpmDataTemplate, BpmFormTable bpmFormTable,
			Map<String, Object> rightMap, Map<String, Object> params,
			Map<String, String> sortMap) {
		if (JSONUtil.isNotEmpty(filterJson)) {
			String type = (String) filterJson.get("type");
			if ("2".equals(type)) {
				String condition = (String) filterJson.get("condition");
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put("map", params);
				return groovyScriptEngine.executeString(condition, paramsMap);
			}
		}

		// 以下是拼接SQL的
		String source = bpmDataTemplate.getSource();
		String tableName = bpmFormTable.getTableName();
		String table = tableName.toLowerCase();
		Map<String, String> tableMap = new HashMap<String, String>();
		tableMap.put(table, fixTableName(tableName, source));
		StringBuffer sql = new StringBuffer("SELECT ");
		// 主从表关联关系
		Map<String, Map<String, String>> relationMap = new HashMap<String, Map<String, String>>();

		Map<String, BpmFormField> bpmFormFieldMap = this
				.getBpmFormFieldMap(bpmFormTable);

		String pkField = bpmFormTable.getPkField();
		// where sql
		String where = "";

		// 过滤条件 sql
		where = this.getFilterSQL(filterJson, tableMap, relationMap);

		// 查询条件 sql
		where = this.getQuerySQL(bpmDataTemplate, where, tableName, params,
				bpmFormFieldMap);

		// 表名的SQL
		sql.append(this.getFromTableSQL(pkField, bpmDataTemplate, tableName,
				tableMap, rightMap));

		if (StringUtils.isNotEmpty(where)) {
			// 多表之间的关联
			sql.append(" WHERE ")
					.append(getTableWhere(table, pkField, where, tableMap,
							relationMap));
		} else {
			Set<Entry<String, String>> set = tableMap.entrySet();
			if (set.size() > 1) {
				sql.append(" WHERE ").append(
						getTableWhere(table, pkField, "1=1", tableMap,
								relationMap));
			}
		}

		// order sql 排序
		return this.getOrderBySql(sql, pkField, bpmDataTemplate.getSortField(),
				sortMap, source);

	}

	private Map<String, BpmFormField> getBpmFormFieldMap(
			BpmFormTable bpmFormTable) {
		Map<String, BpmFormField> map = new HashMap<String, BpmFormField>();
		List<BpmFormField> fieldList = bpmFormTable.getFieldList();
		if (BeanUtils.isEmpty(fieldList))
			return map;
		for (BpmFormField bpmFormField : fieldList) {
			String name = bpmFormField.getFieldName();
			map.put(name, bpmFormField);
		}
		return map;
	}

	/**
	 * 获取表之间的关系
	 * 
	 * @param where
	 * @param table
	 * @param tableMap
	 * @param relationMap
	 * @return
	 */
	private String getTableWhere(String table, String pk, String where,
			Map<String, String> tableMap,
			Map<String, Map<String, String>> relationMap) {
		Set<Entry<String, String>> set = tableMap.entrySet();
		// 只有一个表
		if (set.size() == 1) {
			return where;
		} else {
			StringBuffer sb = new StringBuffer();
			// 从表与主表的关系
			for (Iterator<Entry<String, Map<String, String>>> it = relationMap
					.entrySet().iterator(); it.hasNext();) {
				Map.Entry<String, Map<String, String>> e = (Map.Entry<String, Map<String, String>>) it
						.next();
				String key = e.getKey();
				String fk = e.getValue().get(table);
				if (StringUtils.isNotEmpty(fk)) {
					String mainKey = table + "." + pk;
					String subKey = key + "." + fk;
					sb.append(" AND ").append(mainKey).append("=")
							.append(subKey);
				}
			}
			where = where + sb.toString();
		}
		return where;
	}

	/**
	 * 获取SQL的表
	 * 
	 * @param pkField
	 * 
	 * @param bpmDataTemplate
	 * @param tableName
	 * @param map
	 * @param rightMap
	 * @return
	 */
	private String getFromTableSQL(String pkField,
			BpmDataTemplate bpmDataTemplate, String tableName,
			Map<String, String> tableMap, Map<String, Object> rightMap) {
		// 拼接Select的SQL
		StringBuffer sb = new StringBuffer();
		String c = ",";
		String displayField = bpmDataTemplate.getDisplayField();
		if (StringUtils.isNotEmpty(displayField)) {
			sb
			// .append(" distinct ")
			.append(fixFieldName(pkField, BpmDataTemplate.SOURCE_OTHER_TABLE,
					tableName)).append(c);
			Map<String, Boolean> map = this.getPermission(
					BpmDataTemplate.RIGHT_TYPE_SHOW, displayField, rightMap);
			for (Iterator<Map.Entry<String, Boolean>> it = map.entrySet()
					.iterator(); it.hasNext();) {
				Map.Entry<String, Boolean> e = (Map.Entry<String, Boolean>) it
						.next();
				String key = e.getKey();
				if (key.equalsIgnoreCase(pkField))
					continue;
				sb.append(
						this.fixFieldName(key, bpmDataTemplate.getSource(),
								tableName)).append(c);
			}
		} else {
			sb.append(tableName).append(".*").append(c);
		}

		// 拼接from后的SQL
		StringBuffer from = new StringBuffer();
		for (Iterator<Entry<String, String>> it = tableMap.entrySet()
				.iterator(); it.hasNext();) {
			Map.Entry<String, String> e = (Map.Entry<String, String>) it.next();
			String key = e.getKey();
			String val = e.getValue();
			from.append(val).append(" ").append(key).append(c);
		}

		String s = sb.substring(0, sb.length() - 1);
		String s1 = from.substring(0, from.length() - 1);
		return s + " from " + s1;
	}

	/**
	 * 获取过滤的SQL
	 * 
	 * @param jsonObj
	 * 
	 * @param bpmDataTemplate
	 * @param tableMap
	 *            表对应的表Map<表，真实表名>
	 * @param rightMap
	 * @param params
	 * @param relationMap
	 * @return
	 */
	private String getFilterSQL(JSONObject filterJson,
			Map<String, String> tableMap,
			Map<String, Map<String, String>> relationMap) {
		if (JSONUtil.isEmpty(filterJson))
			return "";
		//
		List<Map<String, Object>> operatorList = new ArrayList<Map<String, Object>>();
		// 转换成SQL
		List<FilterJsonStruct> filters = com.alibaba.fastjson.JSONArray
				.parseArray(filterJson.get("condition").toString(),
						FilterJsonStruct.class);
		this.getFilterResult(filters, tableMap, relationMap, operatorList);

		return this.executeOperator(operatorList);
	}

	/**
	 * 取出满足过滤条件的JSON对象
	 * 
	 * @param filterField
	 * @param rightMap
	 *            权限
	 * @param params
	 *            参数
	 * @return
	 */
	private JSONObject getFilterJson(String filterField,
			Map<String, Object> rightMap, Map<String, Object> params) {
		JSONObject jsonObj = null;
		if (StringUtils.isEmpty(filterField))
			return jsonObj;
		JSONArray jsonAry = JSONArray.fromObject(filterField);
		if (JSONUtil.isEmpty(jsonAry))
			return jsonObj;
		// 取得满足key的条件
		String filterKey = (String) params
				.get(BpmDataTemplate.PARAMS_KEY_FILTERKEY);
		if (StringUtils.isEmpty(filterKey)) {
			jsonObj = jsonAry.getJSONObject(0);
		} else {
			for (Object obj : jsonAry) {
				JSONObject jObj = (JSONObject) obj;
				String key = (String) jObj.get("key");
				if (key.equals(filterKey)) {
					jsonObj = jObj;// 取出满足的Key
					break;
				}
			}
		}
		return jsonObj;
	}

	/**
	 * 过滤所有的条件
	 * 
	 * @param filters
	 * @param tableMap
	 * @param relationMap
	 * @param operatorList
	 */
	private void getFilterResult(List<FilterJsonStruct> filters,
			Map<String, String> tableMap,
			Map<String, Map<String, String>> relationMap,
			List<Map<String, Object>> operatorList) {
		for (FilterJsonStruct filter : filters) {
			// 组合条件
			if (filter.getBranch()) {
				List<Map<String, Object>> branchResultList = new ArrayList<Map<String, Object>>();
				this.getFilterResult(filter.getSub(), tableMap, relationMap,
						branchResultList);
				String branchResult = this.executeOperator(branchResultList);
				Map<String, Object> resultMap = this.getResultMap(
						filter.getCompType(), branchResult);
				operatorList.add(resultMap);
			} else {
				if (filter.getRuleType().intValue() == 2) {// 脚本直接返回结果
					String script = filter.getScript();
					if (StringUtil.isNotEmpty(script)) {
						String tables = filter.getTables();

						List<FilterJsonStruct> filterList = com.alibaba.fastjson.JSONArray
								.parseArray(tables, FilterJsonStruct.class);
						for (FilterJsonStruct filterJsonStruct : filterList) {
							this.setTableMap(filterJsonStruct, tableMap,
									relationMap);
						}

						Map<String, Object> resultMap = this.getResultMap(
								filter.getCompType(), script);
						operatorList.add(resultMap);
					}
				} else {
					this.getNormalFilterResult(filter, tableMap, relationMap,
							operatorList);
				}
			}
		}
	}

	private Map<String, Object> getResultMap(String operator, String result) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("operator", operator);
		resultMap.put("result", result);
		return resultMap;
	}

	/**
	 * 获取普通条件的结果
	 * 
	 * @param filter
	 * @param tableMap
	 * @param relationMap
	 * @param operatorList
	 */
	private void getNormalFilterResult(FilterJsonStruct filter,
			Map<String, String> tableMap,
			Map<String, Map<String, String>> relationMap,
			List<Map<String, Object>> operatorList) {
		String flowvarKey = filter.getFlowvarKey();
		String table = filter.getTable();
		String source = filter.getSource();

		// 获取表的数据
		this.setTableMap(filter, tableMap, relationMap);

		String script = "";
		switch (filter.getOptType()) {
		case 1:// 数字
		case 2:// 字符串
				// 条件一
			if (StringUtils.isNotEmpty(filter.getJudgeVal1())) {
				script = this.getCompareScript(filter.getJudgeCon1(),
						flowvarKey, filter.getJudgeVal1(), filter.getOptType(),
						table, source, filter.getIsHidden());
			}
			// 条件二
			if (StringUtils.isNotEmpty(filter.getJudgeVal2())) {
				String moreScript = getCompareScript(filter.getJudgeCon2(),
						flowvarKey, filter.getJudgeVal2(), filter.getOptType(),
						table, source, filter.getIsHidden());
				if (StringUtils.isNotEmpty(script))
					script = script + BpmDataTemplate.CONDITION_AND;
				script = script + moreScript;
			}
			break;
		case 3:// 日期
				// 条件一
			if (StringUtils.isNotEmpty(filter.getJudgeVal1())) {
				String val1 = this.sqlToDate(filter.getJudgeVal1(),
						filter.getDatefmt());
				script = this.getCompareScript(filter.getJudgeCon1(),
						flowvarKey, val1, filter.getOptType(), table, source,
						filter.getIsHidden());
			}
			// 条件二
			if (StringUtils.isNotEmpty(filter.getJudgeVal2())) {
				String val2 = this.sqlToDate(filter.getJudgeVal2(),
						filter.getDatefmt());
				String moreScript = getCompareScript(filter.getJudgeCon2(),
						flowvarKey, val2, filter.getOptType(), table, source,
						filter.getIsHidden());
				if (StringUtils.isNotEmpty(script))
					script = script + BpmDataTemplate.CONDITION_AND;
				script = script + moreScript;
			}
			break;
		case 4:// 字典
			String[] vals = filter.getJudgeVal1().split("&&");
			for (String val : vals) {
				if (StringUtils.isNotEmpty(script))
					script += BpmDataTemplate.CONDITION_AND;
				script += getCompareScript(filter.getJudgeCon1(), flowvarKey,
						val, filter.getOptType(), table, source,
						filter.getIsHidden());
			}
			break;
		case 5:// 角色、组织、岗位选择器
			String judgeCon = filter.getJudgeCon1();
			String[] ids = filter.getJudgeVal1().split("&&");
			if (ids.length == 2) {
				script = getCompareScript(judgeCon, filter.getFlowvarKey(),
						ids[0], filter.getOptType(), table, source,
						filter.getIsHidden());
			} else {// 特殊类型的
				if ("3".equalsIgnoreCase(judgeCon)
						|| "4".equalsIgnoreCase(judgeCon))
					script = getCompareScript(judgeCon, filter.getFlowvarKey(),
							filter.getJudgeVal1(), filter.getOptType(), table,
							source, filter.getIsHidden());

			}
			break;
		}

		if (StringUtil.isEmpty(script))
			return;
		// 执行结果记录到operatorList中
		Map<String, Object> resultMap = this.getResultMap(filter.getCompType(),
				script);
		operatorList.add(resultMap);
	}

	/**
	 * 设置表的字段
	 * 
	 * @param table
	 * @param source
	 * @param mainTable
	 * @param relation
	 * @param tableMap
	 * @param relationMap
	 */
	private void setTableMap(FilterJsonStruct filter,
			Map<String, String> tableMap,
			Map<String, Map<String, String>> relationMap) {
		String table = filter.getTable();
		String source = filter.getSource();
		String mainTable = filter.getMainTable();
		String relation = filter.getRelation();

		if (StringUtils.isNotEmpty(table)) {
			String tableLow = table.toLowerCase();
			if (!tableMap.containsKey(tableLow)) {
				tableMap.put(tableLow, this.fixTableName(table, source));
			}
			if (StringUtils.isNotEmpty(mainTable)) {
				String mainTableLow = mainTable.toLowerCase();
				if (!relationMap.containsKey(tableLow)
						&& !tableLow.equals(mainTableLow)) {
					Map<String, String> map = new HashMap<String, String>();
					map.put(mainTableLow, relation);
					relationMap.put(tableLow, map);
				}
			}
		}
	}

	/**
	 * 格式日期
	 * 
	 * @param val
	 * @param datefmt
	 * @return
	 */
	private String sqlToDate(String val, String datefmt) {
		StringBuffer sb = new StringBuffer();
		// TODO 需要修复，如果数据库不是ORACL的有问题
		sb.append("TO_DATE('")
				.append(val)
				.append("','")
				.append(StringUtils.isEmpty(datefmt) ? StringPool.DATE_FORMAT_DATE
						: datefmt).append("')");
		return sb.toString();
	}

	/**
	 * 修正后的表名
	 * 
	 * @param tableName
	 * @param source
	 * @return
	 */
	private String fixTableName(String tableName, String source) {
		if (StringUtils.isEmpty(tableName) || StringUtils.isEmpty(source))
			return tableName;
		if (BpmDataTemplate.SOURCE_CUSTOM_TABLE.equals(source))
			tableName = TableModel.CUSTOMER_TABLE_PREFIX
					+ tableName.toUpperCase();
		return tableName;

	}

	/**
	 * 修正字段名
	 * 
	 * @param fieldName
	 *            字段名
	 * @param source
	 *            数据来源 1.表示自定义表（需要加F_修正）
	 * @param prefix
	 *            前缀修正
	 * @return
	 */
	private String fixFieldName(String fieldName, String source) {
		return fixFieldName(fieldName, source, "");
	}

	/**
	 * 修正字段名
	 * 
	 * @param fieldName
	 *            字段名
	 * @param source
	 *            数据来源 1.表示自定义表（需要加F_修正）
	 * @param prefix
	 *            前缀修正
	 * @return
	 */
	private String fixFieldName(String fieldName, String source, String prefix) {
		if (StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(source))
			return fieldName;
		if (BpmDataTemplate.SOURCE_CUSTOM_TABLE.equals(source))
			fieldName = TableModel.CUSTOMER_COLUMN_PREFIX + fieldName;
		if (StringUtils.isNotEmpty(prefix))
			fieldName = prefix.toLowerCase() + "." + fieldName;
		return fieldName;
	}

	/**
	 * 获取根据条件组合的脚本
	 * 
	 * @param judgeCon
	 *            判断条件
	 * @param flowvarKey
	 *            字段
	 * @param judgeVal
	 *            字段的值
	 * @param type
	 *            类型
	 * @param table
	 *            表名
	 * @param source
	 *            数据来源
	 * @param isHidden
	 * @return
	 */
	private String getCompareScript(String judgeCon, String fieldName,
			String judgeVal, int type, String table, String source, int isHidden) {
		StringBuffer sb = new StringBuffer();
		fieldName = this.fixFieldName(fieldName, source, table);
		switch (type) {
		case 1:// 数值
		case 3:// 日期
			if ("1".equals(judgeCon)) {
				sb.append(fieldName).append("=").append(judgeVal);
			} else if ("2".equals(judgeCon)) {
				sb.append(fieldName).append("!=").append(judgeVal);
			} else if ("3".equals(judgeCon)) {
				sb.append(fieldName).append(">").append(judgeVal);
			} else if ("4".equals(judgeCon)) {
				sb.append(fieldName).append(">=").append(judgeVal);
			} else if ("5".equals(judgeCon)) {
				sb.append(fieldName).append("<").append(judgeVal);
			} else if ("6".equals(judgeCon)) {
				sb.append(fieldName).append("<=").append(judgeVal);
			}
			break;
		case 2:// 字符串
		case 4:// 字典
			if ("1".equals(judgeCon)) {
				sb.append(fieldName).append("=").append("'").append(judgeVal)
						.append("'");
			} else if ("2".equals(judgeCon)) {
				sb.append(fieldName).append("!=").append("'").append(judgeVal)
						.append("'");
			} else if ("3".equals(judgeCon)) {
				sb.append("UPPER(").append(fieldName).append(")=")
						.append(" UPPER('").append(judgeVal).append("')");
			} else if ("4".equals(judgeCon)) {
				sb.append(fieldName).append(" LIKE").append(" '%")
						.append(judgeVal).append("%'");
			} else if ("5".equals(judgeCon)) {
				sb.append(fieldName).append(" LIKE").append(" '")
						.append(judgeVal).append("%'");
			} else if ("6".equals(judgeCon)) {
				sb.append(fieldName).append(" LIKE").append(" '%")
						.append(judgeVal).append("'");
			}
			break;
		case 5:// 人员选择器
			if (isHidden == BpmFormField.NO_HIDDEN)
				fieldName = fieldName + TableModel.PK_COLUMN_NAME;

			if ("1".equals(judgeCon)) {
				sb.append(fieldName).append(" in (").append("")
						.append(judgeVal).append(")");
			} else if ("2".equals(judgeCon)) {
				sb.append(fieldName).append(" not in (").append("")
						.append(judgeVal).append(")");
			} else if ("3".equals(judgeCon)) {
				sb.append(fieldName).append(" = :").append("").append(judgeVal)
						.append("");
			} else if ("4".equals(judgeCon)) {
				sb.append(fieldName).append(" != :").append("")
						.append(judgeVal).append("");
			}

			break;
		default:
			break;
		}
		return sb.toString();
	}

	/**
	 * 获取SQL运算结果
	 * 
	 * @param operatorList
	 * @return
	 */
	private String executeOperator(List<Map<String, Object>> operatorList) {
		if (operatorList.size() == 0)
			return "";
		String returnVal = (String) operatorList.get(0).get("result");
		if (operatorList.size() == 1)
			return returnVal;
		int size = operatorList.size();
		for (int k = 1; k < size; k++) {
			Map<String, Object> resultMap = operatorList.get(k);
			String operator = resultMap.get("operator").toString();
			if ("or".equals(operator)) { // 或运算
				returnVal = "(" + returnVal + ") OR ("
						+ resultMap.get("result") + ")";
			} else if ("and".equals(operator)) { // 与运算
				returnVal = "(" + returnVal + ") AND ("
						+ resultMap.get("result") + ")";
			}
		}
		if (StringUtils.isNotEmpty(returnVal))
			returnVal = "(" + returnVal + ")";
		return returnVal;
	}

	/**
	 * 获得order by的SQL
	 * 
	 * @param sql
	 * 
	 * @param table
	 * @param sortField
	 * @param params
	 * @param sortMap
	 * @param source
	 * @return
	 */
	private String getOrderBySql(StringBuffer sql, String pkField,
			String sortField, Map<String, String> sortMap, String source) {
		StringBuffer orderBy = new StringBuffer();
		if (BeanUtils.isNotEmpty(sortMap)) {
			orderBy.append(" ORDER BY ")
					.append(fixFieldName(sortMap.get("sortField"), source, ""))
					.append(" ").append(sortMap.get("orderSeq"));
		} else {
			// 取设置的排序
			if (StringUtils.isNotEmpty(sortField)) {
				String sortSql = this.getSortSQL(sortField, source);
				if (StringUtils.isNotEmpty(sortSql))
					orderBy.append(" ORDER BY ").append(sortSql);
			}
		}
		// 如果没有排序 则用主键排序，避免分页问题
		if (StringUtils.isEmpty(orderBy.toString()))
			orderBy.append(" ORDER BY ").append(pkField).append(" ASC");

		StringBuffer select = new StringBuffer();
		select.append("select *  from  (").append(sql).append(") t ")
				.append(orderBy);
		return select.toString();
	}

	/**
	 * 获取排序的SQL
	 * 
	 * @param sortField
	 * @param source
	 * @return
	 */
	private String getSortSQL(String sortField, String source) {
		StringBuffer sb = new StringBuffer();
		JSONArray jsonArray = JSONArray.fromObject(sortField);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObj = (JSONObject) jsonArray.get(i);
			String name = (String) jsonObj.get("name");
			String sort = (String) jsonObj.get("sort");
			sb.append(this.fixFieldName(name, source, "")).append(" " + sort)
					.append(",");
		}
		if (sb.length() > 0)
			return sb.substring(0, sb.length() - 1);
		return sb.toString();
	}

	/**
	 * 根据查询条件列表，计算Where SQL 语句
	 * 
	 * @param where
	 * @param tableName
	 * 
	 * @param conditions
	 * @param params
	 * @param bpmFormFieldMap
	 * @param fieldTypeMap
	 * @return
	 */
	private String getQuerySQL(BpmDataTemplate bpmDataTemplate, String where,
			String tableName, Map<String, Object> params,
			Map<String, BpmFormField> bpmFormFieldMap) {
		StringBuffer sb = new StringBuffer();
		String and = StringUtils.isEmpty(where) ? "" : " AND ";

		List<SQLClause> conditionFields = this.getConditionList(bpmDataTemplate
				.getConditionField());
		if (BeanUtils.isEmpty(conditionFields))
			return where;
		for (SQLClause condition : conditionFields) {
			this.getCluaseSQL(bpmDataTemplate, tableName, condition, params,
					bpmFormFieldMap, sb);
		}
		if (sb.length() > 0)
			where += and + " (1=1 " + sb.toString() + ") ";

		return where;
	}

	/**
	 * 计算出WHERE SQL
	 * 
	 * @param bpmDataTemplate
	 * @param tableName
	 * @param condition
	 * @param params
	 * @param bpmFormFieldMap
	 * @param fieldTypeMap
	 * @param sb
	 */

	private void getCluaseSQL(BpmDataTemplate bpmDataTemplate,
			String tableName, SQLClause condition, Map<String, Object> params,
			Map<String, BpmFormField> bpmFormFieldMap, StringBuffer sb) {
		String field = condition.getName();
		String f_field = this.fixFieldName(field, bpmDataTemplate.getSource(),
				tableName);
		
		String operate = condition.getOperate();
		int valueFrom = condition.getValueFrom();
		String joinType = condition.getJoinType();
		String type = condition.getType();
		joinType = " " + joinType + " ";
		String controlType = condition.getControlType();
        
		Boolean isSelector = this.isSelector(controlType);
		Map<String, Object> dateMap = getQueryValue(condition, params, field,
				type, operate);
        
		Object value=this.getQueryValue(condition, params,field, isSelector);
	
		if (value==null)
		{
			value = this.getQueryValue(condition, params,f_field, isSelector);
		}
		if (BeanUtils.isEmpty(value) && BeanUtils.isEmpty(dateMap))
			return;
		if (type.equals(ColumnModel.COLUMNTYPE_VARCHAR)) {// 字符串
			if (isSelector) {
				f_field = f_field + TableModel.PK_COLUMN_NAME;
				String f_fieldID = field + TableModel.PK_COLUMN_NAME;
				BpmFormField bpmFormField = bpmFormFieldMap.get(f_fieldID);
				if (BeanUtils.isNotEmpty(bpmFormField)) {
					if (isrMultiSelector(bpmFormField.getControlType())) {
						// 这个有bug 暂时这样处理吧
						sb.append(joinType + f_field + " like '%" + value
								+ "%'");
					} else {
						if (operate.equalsIgnoreCase("2")) {// 1","=" "2","!=" "3","like""4","左like" "5","右like"
							sb.append(joinType + f_field + "!=:" + f_field);
							params.put(f_field, String.valueOf(value));
						} else {
							sb.append(joinType + f_field + "=:" + f_field);
							params.put(f_field, String.valueOf(value));
						}
					}
				}
			} else {
				value = value.toString();
				if (operate.equalsIgnoreCase("1")) {// 1","=" "2","!=" "3","like""4","左like" "5","右like"
					sb.append(joinType).append(f_field).append("=").append(":")
							.append(f_field);
				} else if (operate.equalsIgnoreCase("2")) {
					sb.append(joinType).append(f_field).append(" != ")
							.append(":").append(f_field);
				} else if (operate.equalsIgnoreCase("3")) {
					value = "%" + value.toString() + "%";
					sb.append(joinType).append(f_field).append(" LIKE :")
							.append(f_field);
				} else if (operate.equalsIgnoreCase("4")) {
					value = "%" + value.toString();
					sb.append(joinType).append(f_field).append(" LIKE :")
							.append(f_field);
				} else if (operate.equalsIgnoreCase("5")) {
					value = value.toString() + "%";
					sb.append(joinType).append(f_field).append(" LIKE :")
							.append(f_field);
				} else {
					value = "%" + value.toString() + "%";
					sb.append(joinType).append(f_field).append(" LIKE :")
							.append(f_field);
				}
				params.put(f_field, value);
			}

		} else if (type.equals(ColumnModel.COLUMNTYPE_DATE)) {// 日期
			if ("6".equalsIgnoreCase(operate)) {// 日期范围特殊处理
				if (BeanUtils.isNotEmpty(dateMap
						.get(BpmDataTemplate.DATE_BEGIN))) {
					String begingField = BpmDataTemplate.DATE_BEGIN + field;
					sb.append(joinType + f_field + ">=:" + begingField + " ");
					params.put(begingField,
							dateMap.get(BpmDataTemplate.DATE_BEGIN));
				}

				if (BeanUtils.isNotEmpty(dateMap.get(BpmDataTemplate.DATE_END))) {
					String endField = BpmDataTemplate.DATE_END + field;
					sb.append(joinType + f_field + "<=:" + endField + " ");
					params.put(endField, dateMap.get(BpmDataTemplate.DATE_END));
				}
			} else {
				String op = this.getOperate(operate);
				if (valueFrom == 1) {
					if (params.containsKey(field)) {
						sb.append(joinType + f_field + op + ":" + field + " ");
					}
				} else {
					sb.append(joinType + f_field + op + ":" + field + " ");
					params.put(field, value);
				}
			}
		} else {// 否则则是数字
			String op = this.getOperate(operate);
			if (valueFrom == 1) {
				if (params.containsKey(field)) {

					sb.append(joinType + f_field + op + ":" + field + " ");
				}
			} else {
				sb.append(joinType + f_field + op + ":" + field + " ");
				params.put(field, value);
			}
		}

	}

	/**
	 * 是否是选择器
	 * 
	 * @param controlType
	 * @return
	 */
	private Boolean isSelector(String controlType) {
		if (BeanUtils.isEmpty(controlType))
			return false;
		if (controlType.equals(String.valueOf(FieldPool.SELECTOR_USER_SINGLE))
				|| controlType.equals(String
						.valueOf(FieldPool.SELECTOR_USER_MULTI))
				|| controlType.equals(String
						.valueOf(FieldPool.SELECTOR_ORG_SINGLE))
				|| controlType.equals(String
						.valueOf(FieldPool.SELECTOR_ORG_MULTI))
				|| controlType.equals(String
						.valueOf(FieldPool.SELECTOR_POSITION_SINGLE))
				|| controlType.equals(String
						.valueOf(FieldPool.SELECTOR_POSITION_MULTI))
				|| controlType.equals(String
						.valueOf(FieldPool.SELECTOR_ROLE_SINGLE))
				|| controlType.equals(String
						.valueOf(FieldPool.SELECTOR_ROLE_MULTI)))
			return true;
		return false;
	}

	/**
	 * 是否是多选选择器
	 * 
	 * @param controlType
	 * @return
	 */
	private Boolean isrMultiSelector(Short controlType) {
		if (BeanUtils.isEmpty(controlType))
			return false;
		if (controlType.shortValue() == FieldPool.SELECTOR_USER_MULTI
				|| controlType.shortValue() == FieldPool.SELECTOR_ORG_MULTI
				|| controlType.shortValue() == FieldPool.SELECTOR_POSITION_MULTI
				|| controlType.shortValue() == FieldPool.SELECTOR_ROLE_MULTI)
			return true;
		return false;
	}

	private Map<String, Object> getQueryValue(SQLClause condition,
			Map<String, Object> params, String field, String type,
			String operate) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (type.equals(ColumnModel.COLUMNTYPE_DATE)
				&& "6".equalsIgnoreCase(operate)) {
			String beginKey = BpmDataTemplate.DATE_BEGIN + field;
			Object beginVal = null;
			String endKey = BpmDataTemplate.DATE_END + field;
			Object endVal = null;
			if (params.containsKey(beginKey))
				beginVal = params.get(beginKey);
			if (params.containsKey(endKey))
				endVal = params.get(endKey);
			if (BeanUtils.isNotEmpty(beginVal) || BeanUtils.isNotEmpty(endVal)) {
				map.put(BpmDataTemplate.DATE_BEGIN, beginVal);
				map.put(BpmDataTemplate.DATE_END, endVal);
			}
		}
		return map;
	}

	/**
	 * 获得查询的值
	 * 
	 * @param condition
	 * @param params
	 * @param field
	 * @param isSelector
	 * @return
	 */
	private Object getQueryValue(SQLClause condition,
			Map<String, Object> params, String field, Boolean isSelector) {
		int valueFrom = condition.getValueFrom();
		Object value = null;
		switch (valueFrom) {
		case 1:// 输入
				// 是日期类型，又是日期范围
			if (isSelector)
				field = field + TableModel.PK_COLUMN_NAME;
			if (params.containsKey(field)) {
				value = params.get(field);
			}
			break;
		case 2:// 固定值
			value = condition.getValue();
			break;
		case 3:// 脚本
			String script = (String) condition.getValue();
			if (StringUtil.isNotEmpty(script)) {
				value = groovyScriptEngine.executeObject(script, null);
			}
			break;
		case 4:// 自定义变量
				// value =
				// sysTableManage.getParameterMap().get(condition.getValue().toString());
			break;
		}
		return value;
	}

	/**
	 * 获得操作类型
	 * 
	 * @param operate
	 * @return
	 */
	private String getOperate(String operate) {
		String op = "=";
		if ("1".equalsIgnoreCase(operate)) {// =
			op = "=";
		} else if ("2".equalsIgnoreCase(operate)) {// >
			op = ">";
		} else if ("3".equalsIgnoreCase(operate)) {// <
			op = "<";
		} else if ("4".equalsIgnoreCase(operate)) {// >=
			op = ">=";
		} else if ("5".equalsIgnoreCase(operate)) {// <=
			op = "<=";
		}
		return op;
	}

	/**
	 * 获得条件脚本的SQL
	 * 
	 * @param conditionField
	 * @return
	 */
	private List<SQLClause> getConditionList(String conditionField) {
		List<SQLClause> conditionFields = new ArrayList<SQLClause>();
		if (StringUtil.isEmpty(conditionField))
			return conditionFields;

		JSONArray jsonArray = JSONArray.fromObject(conditionField);

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			SQLClause field = new SQLClause();
			// field.setJoinType(jsonObject.getString("jt"));
			field.setJoinType("AND");
			field.setName(jsonObject.getString("na"));
			field.setComment(jsonObject.getString("cm"));
			field.setType(jsonObject.getString("ty"));
			field.setValue(jsonObject.get("va"));
			field.setValueFrom(jsonObject.getInt("vf"));
			field.setOperate(jsonObject.getString("op"));
			field.setControlType(jsonObject.getString("ct"));
			field.setQueryType(jsonObject.getString("qt"));
			conditionFields.add(field);
		}
		return conditionFields;
	}

	/**
	 * 自定义参数
	 * 
	 * @param bpmDataTemplate
	 * @param request
	 */
	private void parseCustomParameterAsMap(BpmDataTemplate bpmDataTemplate,
			HttpServletRequest request) {
		// TODO Auto-generated method stub

	}

	/**
	 * 获取分页的HTML
	 * 
	 * @param bpmDataTemplate
	 * @param map
	 * @param tableIdCode
	 * @param pageURL
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	private String getPageHtml(BpmDataTemplate bpmDataTemplate,
			Map<String, Object> map, String tableIdCode, String pageURL)
			throws IOException, TemplateException {
		String pageHtml = "";
		// if(BeanUtils.isEmpty(bpmDataTemplate.getList()))
		// return pageHtml;
		// 需要分页
		if (bpmDataTemplate.getNeedPage() == 1) {
			PageBean pageBean = bpmDataTemplate.getPageBean();
			map.put("tableIdCode", tableIdCode);
			map.put("pageBean", pageBean);
			map.put("showExplain", true);
			map.put("showPageSize", true);
			map.put("baseHref", pageURL);
			map.put("pageURL", pageURL);
			// map.put("VarMap", map.getParameterMap());// custom parameter
			pageHtml = freemarkEngine.mergeTemplateIntoString("pageAjax.ftl",
					map);
		}
		return pageHtml;
	}

	/**
	 * 获取字段列表
	 * 
	 * @param tableId
	 * @return
	 */
	public BpmFormTable getFieldListByTableId(Long tableId) {
		BpmFormTable bpmFormTable = bpmFormTableService
				.getTableByIdContainHidden(tableId);

		List<BpmFormTable> otherTableList = new ArrayList<BpmFormTable>();
		// 加入BPM_BUS_LINK的表和字段
		List<BpmFormField> fieldList = new ArrayList<BpmFormField>();
			BpmFormField field0 = this.newBpmFormField("BUS_CREATOR_ID", "发起人ID",
				BpmFormField.DATATYPE_NUMBER, FieldPool.SELECTOR_USER_SINGLE,BpmFormField.HIDDEN);
		BpmFormField field1 = this.newBpmFormField("BUS_ORG_ID", "发起组织ID",
				BpmFormField.DATATYPE_NUMBER, FieldPool.SELECTOR_ORG_SINGLE,BpmFormField.HIDDEN);
		BpmFormField field2 = this.newBpmFormField("BUS_CREATETIME", "发起时间",
				BpmFormField.DATATYPE_DATE, FieldPool.DATEPICKER,BpmFormField.NO_HIDDEN);
		BpmFormField field3 = this.newBpmFormField("BUS_UPDID", "更新人ID",
				BpmFormField.DATATYPE_NUMBER, FieldPool.SELECTOR_USER_SINGLE,BpmFormField.HIDDEN);
		BpmFormField field4 = this.newBpmFormField("BUS_UPDTIME", "更新时间",
				BpmFormField.DATATYPE_DATE, FieldPool.DATEPICKER,BpmFormField.NO_HIDDEN);

		fieldList.add(field0);
		fieldList.add(field1);
		fieldList.add(field2);
		fieldList.add(field3);
		fieldList.add(field4);
		BpmFormTable otherTable = new BpmFormTable();
		otherTable.setTableName(BpmDataTemplate.BUS_TABLE);
		otherTable.setTableDesc("业务数据关联表");
		otherTable.setIsMain((short) 2);// 标记为特殊表
		otherTable.setIsExternal(1);
		otherTable
				.setRelation(bpmFormTable.getKeyDataType().shortValue() == BpmFormTable.PKTYPE_NUMBER
						.shortValue() ? BpmDataTemplate.BUS_TABLE_PK
						: BpmDataTemplate.BUS_TABLE_PK_STR);

		otherTable.setFieldList(fieldList);
		otherTableList.add(otherTable);
		// end 加入BPM_BUS_LINK的表和字段
		if (BeanUtils.isNotEmpty(otherTableList))
			bpmFormTable.setOtherTableList(otherTableList);
		return bpmFormTable;
	}

	private BpmFormField newBpmFormField(String fieldName, String fieldDesc,
			String fieldType, short controlType, short isHidden) {
		BpmFormField bpmFormField = new BpmFormField();
		bpmFormField.setFieldName(fieldName);
		bpmFormField.setFieldDesc(fieldDesc);
		bpmFormField.setFieldType(fieldType);
		bpmFormField.setControlType(controlType);
		bpmFormField.setIsHidden(isHidden);
		return bpmFormField;
	}

	/**
	 * 获取表单数据
	 * 
	 * @param id
	 * @param pk
	 * @return
	 * @throws Exception
	 */
	public String getForm(Long id, String pk) throws Exception {
		Long curUserId = ContextUtil.getCurrentUserId();
		BpmDataTemplate bpmDataTemplate = dao.getById(id);
		List<BpmFormDef> bpmFormDefList = bpmFormDefDao.getByFormKeyIsDefault(
				bpmDataTemplate.getFormKey(), BpmFormDef.IS_DEFAULT);
		if (BeanUtils.isEmpty(bpmFormDefList))
			return "";
		BpmFormDef bpmFormDef = bpmFormDefList.get(0);
		return bpmFormHandlerService.getFormDetail(bpmFormDef, pk, curUserId,
				null, "");
	}

	/**
	 * 删除数据
	 * 
	 * @param id
	 * @param pk
	 * @throws Exception
	 */
	public void deleteData(Long id, String pk) throws Exception {
		BpmDataTemplate bpmDataTemplate = dao.getById(id);
		Long tableId = bpmDataTemplate.getTableId();
		BpmFormTable bpmFormTable = bpmFormTableService.getById(tableId);
		if (BeanUtils.isEmpty(bpmFormTable))
			return;
		// 删除业务表数据	// 是本数据库
		BpmFormTable table = new BpmFormTable();
		table.setIsExternal(BpmFormTable.NOT_EXTERNAL);
		this.deleteTable(BpmDataTemplate.BUS_TABLE, table, bpmFormTable
				.getKeyDataType().shortValue() == BpmFormTable.PKTYPE_NUMBER
				.shortValue() ? BpmDataTemplate.BUS_TABLE_PK
				: BpmDataTemplate.BUS_TABLE_PK_STR, pk);
		String source = bpmFormTable.isExtTable() ? BpmDataTemplate.SOURCE_OTHER_TABLE
				: BpmDataTemplate.SOURCE_CUSTOM_TABLE;
		// 删除子表数据
		List<BpmFormTable> subTableList = bpmFormTableService
				.getSubTableByMainTableId(tableId);

		for (BpmFormTable subTable : subTableList) {
			this.deleteTable(
					this.fixTableName(subTable.getTableName(), source),
					bpmFormTable,
					bpmFormTable.isExtTable() ? subTable.getRelation()
							: TableModel.FK_COLUMN_NAME,
					subTable.getKeyDataType().shortValue() == BpmFormTable.PKTYPE_NUMBER
							.shortValue() ? pk : ("'" + pk + "'"));
		}

		// 删除主表数据
		this.deleteTable(
				this.fixTableName(bpmFormTable.getTableName(), source),
				bpmFormTable,
				bpmFormTable.isExtTable() ? bpmFormTable.getPkField()
						: TableModel.PK_COLUMN_NAME,
				bpmFormTable.getKeyDataType().shortValue() == BpmFormTable.PKTYPE_NUMBER
						.shortValue() ? pk : ("'" + pk + "'"));

	}

	/**
	 * 删除表的数据
	 * 
	 * @param tableName
	 * @param bpmFormTable
	 * @param map
	 * @throws Exception
	 */
	public void deleteTable(String tableName, BpmFormTable bpmFormTable,
			String pkField, String pk) throws Exception {
		String delSQL = "DELETE FROM " + tableName + " WHERE  " + pkField + "="
				+ pk;
		JdbcHelper<?, ?> jdbcHelper = getJdbcHelper(bpmFormTable);
		jdbcHelper.execute(delSQL);

	}

	// TODO =================导出===================================
	/**
	 * 导出execl
	 * 
	 * @param id
	 * @param exportIds
	 * @param exportType
	 * @param filterKey
	 * @return
	 * @throws Exception
	 */
	public HSSFWorkbook export(Long id, String[] exportIds, int exportType,
			Map<String, Object> params) throws Exception {
		Long curUserId = ContextUtil.getCurrentUserId();
		Long curOrgId = ContextUtil.getCurrentOrgId();
		CommonVar.setCommonVar(params, curUserId, curOrgId);

		BpmDataTemplate bpmDataTemplate = dao.getById(id);
		if (BeanUtils.isEmpty(bpmDataTemplate))
			return null;
		BpmFormTable bpmFormTable = bpmFormTableService.getByTableId(
				bpmDataTemplate.getTableId(), 1);
		// 是否有子表
		Boolean isSubTable = bpmFormTable.getSubTableList().size() > 0 ? true
				: false;

		// 获取权限map
		Map<String, Object> rightMap = this.getRightMap(curUserId, curOrgId);

		Map<String, Boolean> exportFieldMap = this.getFieldPermission(
				BpmDataTemplate.RIGHT_TYPE_EXPORT,
				bpmDataTemplate.getExportField(), rightMap);
		// 显示的列注释
		Map<String, List<String>> displayFieldDesc = this
				.getDisplayFieldList(bpmDataTemplate.getExportField(),
						exportFieldMap, 0, isSubTable);

		// 显示的字段的名
		Map<String, List<String>> displayFieldName = this
				.getDisplayFieldList(bpmDataTemplate.getExportField(),
						exportFieldMap, 1, isSubTable);

		// 获取表
		Map<String, String> tableNameMap = this.getTableName(bpmFormTable);
		// 格式化的数据
		Map<String, Object> formatData = this.getFormatDataMap(bpmFormTable);

		// 如果有选择数据, 设置为不需要分页,导出所有的
		if (exportType == 0)
			bpmDataTemplate.setNeedPage((short) 0);
		// 只有
		if (exportType != 1)
			exportIds = null;

		// 取得当前过滤的条件
		JSONObject filterJson = this.getFilterJson(
				bpmDataTemplate.getFilterField(), rightMap, params);
		// 主表显示的数据
		bpmDataTemplate = this.getData(bpmDataTemplate, bpmFormTable, rightMap,
				params, null, formatData, filterJson);

		// 主表数据和从表的数据
		Map<String, List<List<Object>>> dataMapList = this.getDataList(
				bpmDataTemplate, displayFieldName, exportFieldMap, formatData,
				bpmFormTable, exportIds);

		// 以下开始写excel
		// 创建新的Excel 工作簿
		Excel excel = new Excel();

		int j = 0;
		for (Iterator<Entry<String, List<String>>> it = displayFieldDesc
				.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, List<String>> e = (Map.Entry<String, List<String>>) it
					.next();
			String key = e.getKey();
			String tableDesc = tableNameMap.get(key);
			List<String> list = e.getValue();
			if (j == 0)
				excel.sheet().sheetName(tableDesc);// 重命名当前处于工作状态的表的名称
			else {
				excel.setWorkingSheet(j).sheetName(tableDesc);// 把子表激活
			}
			// 第一行标题 加粗
			excel.row(0, 0).value(list.toArray()).font(new IFontEditor() {
				// 设置字体
				@Override
				public void updateFont(Font font) {
					font.boldweight(BoldWeight.BOLD);// 粗体
				}
			}).bgColor(Color.GREY_25_PERCENT);
			// 取得表的数据
			List<List<Object>> dataList = dataMapList.get(key);

			if (BeanUtils.isNotEmpty(dataList)) {
				// 从第2行写入数据
				for (int i = 0; i < dataList.size(); i++) {
					List<Object> objectList = dataList.get(i);
					excel.row(i + 1).value(objectList.toArray())
							.dataFormat("@");
				}
			}
			j++;
		}

		return excel.getWorkBook();
	}

	private Map<String, String> getTableName(BpmFormTable bpmFormTable) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(bpmFormTable.getTableName(), bpmFormTable.getTableDesc());
		for (BpmFormTable table : bpmFormTable.getSubTableList()) {
			map.put(table.getTableName(), table.getTableDesc());
		}
		return map;
	}

	private Map<String, Boolean> getFieldPermission(int type, String fieldJson,
			Map<String, Object> rightMap) {
		JSONArray jsonAry = JSONArray.fromObject(fieldJson);
		return getFieldPermissionMap(type, jsonAry, rightMap);

	}

	@SuppressWarnings("unchecked")
	private Map<String, Boolean> getFieldPermissionMap(int type,
			JSONArray jsonAry, Map<String, Object> rightMap) {
		Map<String, Boolean> map = new ListOrderedMap();
		if (JSONUtil.isEmpty(jsonAry))
			return map;

		for (Object obj1 : jsonAry) {
			JSONObject json1 = JSONObject.fromObject(obj1);
			JSONArray fields = (JSONArray) json1.get("fields");
			String tableName = (String) json1.get("tableName");
			String isMain = (String) json1.get("isMain");
			// 添加主键和外键字段
			String key = tableName
					+ ("1".equals(isMain) ? TableModel.PK_COLUMN_NAME
							: TableModel.FK_COLUMN_NAME);
			map.put(key, true);
			for (Object obj : fields) {
				JSONObject json = JSONObject.fromObject(obj);
				String name = (String) json.get("name");
				JSONArray rights = (JSONArray) json.get("right");
				for (Object right : rights) {
					JSONObject rightJson = JSONObject.fromObject(right);
					Integer s = (Integer) rightJson.get("s");
					if (s.intValue() == type)
						map.put(tableName + name,
								this.hasRight(rightJson, rightMap));
				}
			}

		}
		return map;
	}

	/**
	 * 获得数据列表
	 * 
	 * @param bpmDataTemplate
	 * @param displayFieldList
	 * @param displayFieldMap
	 * @param bpmFormTable
	 * @param exportIds
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, List<List<Object>>> getDataList(
			BpmDataTemplate bpmDataTemplate,
			Map<String, List<String>> displayFieldName,
			Map<String, Boolean> displayFieldMap,
			Map<String, Object> formatData, BpmFormTable bpmFormTable,
			String[] exportIds) throws Exception {
		Map<String, List<List<Object>>> dataObjectList = new HashMap<String, List<List<Object>>>();
		// 主表的数据
		List<Map<String, Object>> list = bpmDataTemplate.getList();
		if (BeanUtils.isEmpty(displayFieldName) || BeanUtils.isEmpty(list))
			return dataObjectList;
		String mainTableName = bpmFormTable.getTableName();

		List<List<Object>> dataMainList = new ArrayList<List<Object>>();
		String pkField = bpmFormTable.getPkField();
		Short keyDataType = bpmFormTable.getKeyDataType();

		List<Object> keyValList = new ArrayList<Object>();

		for (Map<String, Object> map : list) {
			List<Object> dataList = new ArrayList<Object>();
			List<String> displayFieldList = displayFieldName.get(mainTableName);
			for (String name : displayFieldList) {
				// 按字段的顺序
				Boolean f = displayFieldMap.get(mainTableName + name);
				if (f) {
					dataList.add(map.get(name));
				}
			}
			// 导出指定的ID
			if (ArrayUtils.isNotEmpty(exportIds))
				dataList = getExportDataList(dataList, map, exportIds, pkField,
						keyDataType);

			if (BeanUtils.isNotEmpty(dataList)) {
				dataMainList.add(dataList);
				keyValList.add(map.get(pkField));
			}
		}
		// 主表的数据
		dataObjectList.put(mainTableName, dataMainList);
		if (bpmFormTable.getSubTableList().size() == 0)
			return dataObjectList;

		JdbcHelper jdbcHelper = this.getJdbcHelper(bpmFormTable);
		// 子表数据
		for (BpmFormTable subTable : bpmFormTable.getSubTableList()) {
			List<List<Object>> dataSubList = new ArrayList<List<Object>>();
			String subTableName = subTable.getTableName();
			List<String> displayFieldList = displayFieldName.get(subTableName);
			Map<String, Object> subFormatData = getFormatDataMap(subTable);
			for (Object o : keyValList) {
				// 子表数据
				dataSubList = this.getSubTableDataList(dataSubList,
						displayFieldList, displayFieldMap, o, pkField,
						keyDataType, subTable, subTableName, jdbcHelper,
						subFormatData);
			}
			dataObjectList.put(subTable.getTableName(), dataSubList);
		}

		return dataObjectList;
	}

	/**
	 * 获取子表的数据
	 * 
	 * @param dataSubList
	 * @param displayFieldList
	 * @param displayFieldMap
	 * @param o
	 * @param pkField
	 * @param keyDataType
	 * @param subTable
	 * @param subTableName
	 * @param jdbcHelper
	 * @param formatData
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<List<Object>> getSubTableDataList(
			List<List<Object>> dataSubList, List<String> displayFieldList,
			Map<String, Boolean> displayFieldMap, Object o, String pkField,
			Short keyDataType, BpmFormTable subTable, String subTableName,
			JdbcHelper jdbcHelper, Map<String, Object> formatData)
			throws Exception {
		String idStr = String.valueOf(o);
		String relation = StringUtils.isEmpty(subTable.getRelation()) ? TableModel.FK_COLUMN_NAME
				: subTable.getRelation();
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("SELECT * FROM ");
		sql.append(subTable.getFactTableName()).append(" WHERE ")
				.append(relation).append(" = ");
		if (keyDataType.shortValue() == BpmFormTable.PKTYPE_NUMBER.shortValue())
			sql.append(Long.parseLong(idStr));
		else
			sql.append(" = ").append(idStr);
		List<Map<String, Object>> list = jdbcHelper.queryForList(
				sql.toString(), params);
		// 转换的数据
		if (BeanUtils.isEmpty(list))
			return dataSubList;
		// 整理数据
		list = this.getDataList(list, subTable, formatData, false);
		for (Map<String, Object> map : list) {
			List<Object> dataList = new ArrayList<Object>();
			for (String name : displayFieldList) {
				// 按字段的顺序取出数据
				Boolean f = displayFieldMap.get(subTableName + name);
				if (!f)
					continue;
				if (relation.equals(name))
					dataList.add(map.get(name));
				else
					dataList.add(map.get(name));
			}
			dataSubList.add(dataList);
		}
		return dataSubList;
	}

	/**
	 * 获得导出的数据list
	 * 
	 * @param dataList
	 * @param map
	 * @param exportIds
	 * @param pkField
	 * @param keyDataType
	 * @return
	 */
	private List<Object> getExportDataList(List<Object> dataList,
			Map<String, Object> map, String[] ids, String pkField,
			Short keyDataType) {
		Object idVal = map.get(pkField);
		if (idVal == null)
			return null;
		String idStr = String.valueOf(idVal);
		Boolean b = true;
		if (keyDataType.shortValue() == BpmFormTable.PKTYPE_NUMBER.shortValue()) {
			Long id = Long.parseLong(idStr);
			Long[] idLs = ArrayUtil.convertArray(ids);
			b = ArrayUtils.contains(idLs, id);
		} else {
			b = ArrayUtils.contains(ids, idStr);
		}

		if (!b)
			dataList = null;
		return dataList;
	}

	/**
	 * 获得显示的字段
	 * 
	 * @param displayField
	 * @param displayFieldMap
	 * @param b
	 *            0代表显示desc 其它代表显示name
	 * @param isSubTable
	 * @return map<表名,字段列表>
	 */
	@SuppressWarnings("unchecked")
	private Map<String, List<String>> getDisplayFieldList(String displayField,
			Map<String, Boolean> displayFieldMap, int b, Boolean isSubTable) {
		Map<String, List<String>> displayFieldList = new ListOrderedMap();
		JSONArray jsonAry = JSONArray.fromObject(displayField);
		if (JSONUtil.isEmpty(jsonAry) || BeanUtils.isEmpty(displayFieldMap))
			return displayFieldList;

		for (Object obj1 : jsonAry) {
			List<String> list = new ArrayList<String>();
			JSONObject json1 = JSONObject.fromObject(obj1);
			String table = (String) json1.get("tableName");
			String isMain = (String) json1.get("isMain");
			JSONArray fields = (JSONArray) json1.get("fields");

			// 加入主键或者外键 如果有子表加入外键
			if (isSubTable) {
				String val = getFk(isMain, b);
				list.add(val);
			}

			for (Object obj : fields) {
				JSONObject json = JSONObject.fromObject(obj);
				String name = (String) json.get("name");
				String desc = (String) json.get("desc");
				String tableName = (String) json.get("tableName");
				Boolean f = displayFieldMap.get(tableName + name);
				if (!f)
					continue;
				if (b == 0)
					list.add(desc);
				else
					list.add(name);
			}
			displayFieldList.put(table, list);
		}
		return displayFieldList;
	}

	private String getFk(String isMain, int b) {
		String val = "";
		if ("1".equals(isMain)) {
			if (b == 0)
				val = "主键";
			else
				val = TableModel.PK_COLUMN_NAME;
		} else {
			if (b == 0)
				val ="外键";
			else
				val = TableModel.FK_COLUMN_NAME;
		}
		return val;
	}

	// TODO =================导入===================================
	/**
	 * 导入文件
	 * 
	 * @param inputStream
	 * @param id
	 * @throws Exception
	 */
	public void importFile(InputStream inputStream, Long id) throws Exception {
		ExcelReader excel = new ExcelReader();
		// 读出execl
		TableEntity tableEntity = excel.readFile(inputStream);
		BpmDataTemplate bpmDataTemplate = dao.getById(id);
		//
		BpmFormTable bpmFormTable = bpmFormTableService.getByTableId(
				bpmDataTemplate.getTableId(), 1);

		for (DataEntity dataEntity : tableEntity.getDataEntityList()) {
			String pkValue = dataEntity.getPkVal();
			Map<String, List<DataEntity>> subDataEntityMap = getSubDataEntityMap(
					tableEntity, pkValue);
			BpmFormData bpmFormData = parseBpmFormData(dataEntity,
					subDataEntityMap, bpmFormTable);
			bpmFormHandlerService.handFormData(bpmFormData);
		}

	}

	@SuppressWarnings("unchecked")
	private Map<String, List<DataEntity>> getSubDataEntityMap(
			TableEntity tableEntity, String pkValue) {
		Map<String, List<DataEntity>> subDataEntityMap = new ListOrderedMap();
		// 子表
		for (TableEntity subTableEntity : tableEntity.getSubTableEntityList()) {
			if (subTableEntity == null)
				continue;
			String key = subTableEntity.getName();
			List<DataEntity> subDataEntityList = new ArrayList<DataEntity>();
			for (DataEntity subDataEntity : subTableEntity.getDataEntityList()) {
				if (subDataEntity.getPkVal().equals(pkValue)) {
					subDataEntityList.add(subDataEntity);
				}
			}
			subDataEntityMap.put(key, subDataEntityList);
		}
		return subDataEntityMap;
	}

	/**
	 * 转换表的数据
	 * 
	 * @param dataEntity
	 * @param subDataEntityMap
	 * @param bpmFormTable
	 * @return
	 * @throws Exception
	 */
	private BpmFormData parseBpmFormData(DataEntity dataEntity,
			Map<String, List<DataEntity>> subDataEntityMap,
			BpmFormTable bpmFormTable) throws Exception {
		BpmFormData bpmFormData = new BpmFormData(bpmFormTable);
		PkValue pkValue = FormDataUtil.generatePk(bpmFormTable);
		bpmFormData.setPkValue(pkValue);
		// 处理主表
		handleMain(dataEntity, bpmFormData);
		// 处理子表
		handSub(subDataEntityMap, bpmFormData);
		return bpmFormData;
	}

	/**
	 * 处理子表
	 * 
	 * @param dataEntity
	 * @param subDataEntityMap
	 * @param bpmFormData
	 * @throws Exception
	 */
	private void handSub(Map<String, List<DataEntity>> subDataEntityMap,
			BpmFormData bpmFormData) throws Exception {
		BpmFormTable mainTable = bpmFormData.getBpmFormTable();
		List<BpmFormTable> listTable = mainTable.getSubTableList();
		// 将表名消息并作为键和表对象进行关联。
		Map<String, BpmFormTable> formTableMap = convertTableMap(listTable);

		boolean isExternal = mainTable.isExtTable();

		for (Iterator<Entry<String, List<DataEntity>>> it = subDataEntityMap
				.entrySet().iterator(); it.hasNext();) {
			SubTable subTable = new SubTable();
			Map.Entry<String, List<DataEntity>> e = (Map.Entry<String, List<DataEntity>>) it
					.next();
			String key = e.getKey();
			List<DataEntity> dataEntityList = e.getValue();
			BpmFormTable subFormTable = formTableMap.get(key);

			// 获取子表的列元数据。
			List<BpmFormField> subTableFields = subFormTable.getFieldList();
			// 将子表的字段名称作为键，字段对象作为值放到map对象当中。
			Map<String, BpmFormField> subFieldDescMap = convertFieldToMap(subTableFields);
			// 设置子表名称
			subTable.setTableName(subFormTable.getTableName());

			// 设置子表的主键和外键名称。
			if (isExternal) {
				String pk = subFormTable.getPkField();
				subTable.setPkName(pk);
				subTable.setFkName(subFormTable.getRelation());
			} else {
				subTable.setPkName(TableModel.PK_COLUMN_NAME);
				subTable.setFkName(TableModel.FK_COLUMN_NAME);
			}

			for (DataEntity dataEntity : dataEntityList) {
				Map<String, Object> subRow = handleRow(subFormTable,
						subFieldDescMap, dataEntity.getFieldEntityList());
				// 处理主键数据
				handFkRow(subFormTable, subRow, bpmFormData.getPkValue());
				// 处理需要计算的数据。
				subTable.addRow(subRow);
			}

			bpmFormData.addSubTable(subTable);
		}
	}

	/**
	 * 处理子表行数据的主键和外键。
	 * 
	 * <pre>
	 * 添加子表的主键和外键。
	 * </pre>
	 * 
	 * @param mainTabDef
	 *            主表定义。
	 * @param bpmFormTable
	 *            子表定义。
	 * @param rowData
	 *            子表一行数据。
	 * @param pkValue
	 *            主键数据。
	 * @throws Exception
	 */
	public static void handFkRow(BpmFormTable bpmFormTable,
			Map<String, Object> rowData, PkValue pkValue) throws Exception {
		boolean isExternal = bpmFormTable.isExtTable();
		// 外部表数据
		if (isExternal) {
			String pkField = bpmFormTable.getPkField().toLowerCase();
			if (!rowData.containsKey(pkField)) {
				PkValue pk = FormDataUtil.generatePk(bpmFormTable);
				rowData.put(pk.getName(), pk.getValue());
			} else {
				Object obj = rowData.get(pkField);
				if (obj == null || "".equals(obj.toString().trim())) {
					PkValue pk = FormDataUtil.generatePk(bpmFormTable);
					rowData.put(pk.getName(), pk.getValue());
				}
			}
			String fk = bpmFormTable.getRelation();
			rowData.put(fk, pkValue.getValue());
		}
		// 本地表数据
		else {
			String pkField = bpmFormTable.getPkField().toLowerCase();
			// 没有包含主键则添加一个。
			if (!rowData.containsKey(pkField)) {
				Long pk = UniqueIdUtil.genId();
				rowData.put(TableModel.PK_COLUMN_NAME.toLowerCase(), pk);
			}

			rowData.put(TableModel.FK_COLUMN_NAME.toLowerCase(),
					pkValue.getValue());
		}

	}

	/**
	 * 处理主表
	 * 
	 * @param dataEntity
	 * @param bpmFormData
	 */
	private void handleMain(DataEntity dataEntity, BpmFormData bpmFormData) {
		// 主表 main
		BpmFormTable mainTableDef = bpmFormData.getBpmFormTable();
		List<BpmFormField> mainFields = mainTableDef.getFieldList();
		// 主表字段
		Map<String, BpmFormField> mainFieldDescMap = this
				.convertFieldToMap(mainFields);

		// 将主表JSON转换成map数据。
		Map<String, Object> mainFiledsData = handleRow(mainTableDef,
				mainFieldDescMap, dataEntity.getFieldEntityList());
		// 添加主表数据
		bpmFormData.addMainFields(mainFiledsData);

		PkValue pkValue = bpmFormData.getPkValue();

		bpmFormData.addMainFields(pkValue.getName().toLowerCase(),
				pkValue.getValue());
	}

	private Map<String, BpmFormTable> convertTableMap(List<BpmFormTable> list) {
		Map<String, BpmFormTable> map = new HashMap<String, BpmFormTable>();
		for (BpmFormTable tb : list) {
			map.put(tb.getTableDesc(), tb);
		}
		return map;
	}

	/**
	 * 处理一行数据
	 * 
	 * @param bpmFormTable
	 * @param fieldDescMap
	 * @param list
	 * @return
	 */
	private Map<String, Object> handleRow(BpmFormTable bpmFormTable,
			Map<String, BpmFormField> fieldDescMap, List<FieldEntity> list) {
		boolean isExternal = bpmFormTable.isExtTable();
		// int keyType= bpmFormTable.getKeyDataType();
		// String pkField=bpmFormTable.getPkField();
		String colPrefix = (isExternal ? "" : TableModel.CUSTOMER_COLUMN_PREFIX)
				.toLowerCase();
		Map<String, Object> row = new HashMap<String, Object>();
		// 对字段名称进行遍历
		for (FieldEntity fieldEntity : list) {
			String name = fieldEntity.getName();
			String value = fieldEntity.getValue();
			BpmFormField bpmFormField = fieldDescMap.get(name);
			if (BeanUtils.isEmpty(bpmFormField))
				continue;
			String key = bpmFormField.getFieldName();
			Object convertValue = FormDataUtil.convertType(value, bpmFormField);
			String fieldName = key.toLowerCase();
			if (!isExternal
					&& !fieldName.equalsIgnoreCase(TableModel.PK_COLUMN_NAME)) {
				fieldName = (colPrefix + key).toLowerCase();
			}
			row.put(fieldName, convertValue);
		}
		return row;
	}

	/**
	 * 转换Map<字段注释,自定义表>
	 * 
	 * @param list
	 * @return
	 */
	private Map<String, BpmFormField> convertFieldToMap(List<BpmFormField> list) {
		Map<String, BpmFormField> map = new HashMap<String, BpmFormField>();
		for (Iterator<BpmFormField> it = list.iterator(); it.hasNext();) {
			BpmFormField field = it.next();
			map.put(field.getFieldDesc(), field);
		}
		return map;
	}

	/**
	 * 根据formKey获取业务表单数量。
	 * 
	 * @param formKey
	 * @return
	 */
	public Integer getCountByFormKey(Long formKey) {
		return dao.getCountByFormKey(formKey);
	}

}
