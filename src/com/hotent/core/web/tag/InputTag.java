package com.hotent.core.web.tag;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

import com.hotent.core.util.StringUtil;
import com.hotent.platform.service.system.SecurityUtil;
import com.hotent.platform.service.system.SubSystemUtil;

/**
 * 功能权限标签。 <br>
 * 功能：这个标签配合权限分配，可以实现页面上的按钮是否可以点击操作，将权限操作控制到按钮。
 * <ul>
 * <li>标签中 有个别名属性，系统根据该别名控制当前用户是否有该操作的的权限。</li>
 * <li>标签的使用需要在tld文件和web.xml中进行配置。</li>
 * <li>权限标签的写法:<f:input alias="site_add" css="add"
 * name="addSite1.ht">;添加&lt;/f:input&gt;</li>
 * </ul>
 */
@SuppressWarnings("serial")
public class InputTag extends BodyTagSupport {

	private Log logger = LogFactory.getLog(InputTag.class);
	/** 样式class */
	private String css;
	/** 别名 */
	private String alias;
	/** name */
	private String name;
	/** name */
	private String id;
	/** value */
	private Object value;
	/** onclick */
	private String onclick;
	/** onclick */
	private String onfocus;
	/** onblur */
	private String onblur;
	/** onchange */
	private String onchange;

	private String validate;

	private String type;

	private String nodekey;

	private String readonly;

	private String width;

	private String height;

	private String pattern;

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	private boolean canreset = false;
	/**
	 * 当没有权限的时候超链接是否显示。
	 */
	private boolean showNoRight = true;
	private static ThreadLocal<Map<String, Boolean>> threadLocalVar = new ThreadLocal<Map<String, Boolean>>();

	public static void cleanFuncRights() {
		threadLocalVar.remove();
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setShowNoRight(boolean _isShowNoRight) {
		this.showNoRight = _isShowNoRight;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}

	public void setNodekey(String nodekey) {
		this.nodekey = nodekey;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(Object value) throws JspException {
		this.value = "";
		if (value != null) {
			this.value = ExpressionEvaluatorManager.evaluate("value",
					value.toString(), Object.class, this, pageContext);
			if (this.pattern != null) {
				SimpleDateFormat smf = new SimpleDateFormat(this.pattern);
				Date date = new Date(String.valueOf(this.value));
				this.value = smf.format(date);
			}
		}
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public void setOnfocus(String onfocus) {
		this.onfocus = onfocus;
	}

	public void setOnblur(String onblur) {
		this.onblur = onblur;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	public void setCanreset(boolean canreset) {
		this.canreset = canreset;
	}

	public int doStartTag() throws JspTagException {
		return EVAL_BODY_BUFFERED;
	}

	protected boolean getHasRights(String systemAlias) {
		if (StringUtil.isEmpty(systemAlias))
			return false;
		String key = systemAlias + "_" + this.alias;
		boolean rtn = false;
		if (threadLocalVar.get() == null) {
			// 判断别名是否可以访问。
			boolean canAccess = SecurityUtil.hasFuncPermission(systemAlias,
					alias);
			Map<String, Boolean> map = new HashMap<String, Boolean>();
			map.put(key, canAccess);
			threadLocalVar.set(map);
			rtn = canAccess;
		} else {
			Map<String, Boolean> map = threadLocalVar.get();
			if (map.containsKey(key)) {
				rtn = map.get(key);
			} else {
				boolean canAccess = SecurityUtil.hasFuncPermission(systemAlias,
						alias);
				map.put(key, canAccess);
				rtn = canAccess;
			}
		}
		return rtn;
	}

	private String getOutput() throws Exception {
		String systemAlias = SubSystemUtil
				.getCurrentSystemAlias((HttpServletRequest) pageContext
						.getRequest());

		boolean canAccess = true;
		//boolean canAccess = getHasRights(systemAlias);
		/**
		 * 当没有权限不显示返回空串。
		 */
		if (!showNoRight && !canAccess) {
			return "";
		}

		// String body = this.getBodyContent().getString();
		StringBuffer content = new StringBuffer("<input ");
		if (id != null) {
			content.append("id=\"" + id + "\" ");
		}
		if (name != null) {
			content.append("name=\"" + name + "\" ");
		}
		if (value != null) {
			content.append("value=\"" + value + "\" ");
		}
		if (this.validate != null) {
			content.append("validate=\"" + validate + "\" ");
		}
		if (this.type != null) {
			content.append("type=\"" + type + "\" ");
		}
		if (this.nodekey != null) {
			content.append("nodekey=\"" + nodekey + "\" ");
		}
		if (this.height != null) {
			content.append("height=\"" + height + "\" ");
		}
		if (this.width != null) {
			content.append("width=\"" + width + "\" ");
		}

		// 可以访问的情况。
		if (canAccess) {
			if (css != null)
				content.append(" class=\"" + css + "\" ");
			if (onclick != null)
				content.append(" onclick=\"" + onclick + "\" ");
			if (onchange != null)
				content.append(" onchange=\"" + onchange + "\" ");
			if (onblur != null)
				content.append(" onblur=\"" + onblur + "\" ");
			if (onfocus != null)
				content.append(" onfocus=\"" + this.onfocus + "\" ");
			if (this.readonly != null) {
				content.append("readonly=\"" + readonly + "\" ");
			}
			if (this.canreset) {
				content.append("canreset=\"true\" ");
			}

		} else {
			if (this.nodekey != null) {
				content.append(" class=\"disabled inputText\" readonly=\"readonly\" ");
			} else {
				if (css != null)
					content.append(" class=\"" + css.replace("date", "")
							+ " disabled\" readonly=\"readonly\" ");
				else
					content.append(" class=\"disabled\" readonly=\"readonly\" ");
			}

		}

		content.append(" />");
		if (this.canreset && canAccess && (this.nodekey == null)) {
			content.append("<a class=\"link reset\" href=\"javascript:;\"></a>");
		}
		// content.append(body);
		// content.append("</input>");

		return content.toString();
	}

	public int doEndTag() throws JspTagException {

		try {
			String str = getOutput();
			pageContext.getOut().print(str);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException(e.getMessage());
		}
		return SKIP_BODY;
	}
}
