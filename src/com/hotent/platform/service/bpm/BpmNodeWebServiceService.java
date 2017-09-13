package com.hotent.platform.service.bpm;

import java.util.List;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.platform.dao.bpm.BpmNodeWebServiceDao;
import com.hotent.platform.model.bpm.BpmNodeWebService;

/**
 * <pre>
 * 对象功能:流程WebService节点 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zxh
 * 创建时间:2012-12-21 10:23:36
 * </pre>
 */
@Service
public class BpmNodeWebServiceService extends BaseService<BpmNodeWebService> {
	@Resource
	private BpmNodeWebServiceDao dao;

	public BpmNodeWebServiceService() {
	}

	@Override
	protected IEntityDao<BpmNodeWebService, Long> getEntityDao() {
		return dao;
	}

	/**
	 * 通过节点ID和流程节点ID和服务类型获得流程服务设置信息
	 * 
	 * @param nodeId
	 *            节点ID
	 * @param actDefId
	 *            流程节点ID
	 * @return
	 */
	public BpmNodeWebService getByNodeIdActDefId(String nodeId, String actDefId) {
		return dao.getByNodeIdActDefId(nodeId, actDefId);
	}
	
	/**
	 * 保存 流程WebService节点
	 * 
	 * @param nodeId
	 *            节点ID
	 * @param actDefId
	 *            流程节点ID
	 * @param json
	 * @throws Exception
	 */
	public void save(String nodeId, String actDefId, String json) throws Exception {
		BpmNodeWebService bpmNodeWebService = new BpmNodeWebService();
		bpmNodeWebService.setId(UniqueIdUtil.genId());
		bpmNodeWebService.setNodeId(nodeId);
		bpmNodeWebService.setActDefId(actDefId);
		bpmNodeWebService.setDocument(json);
		dao.add(bpmNodeWebService);
	}

	/**
	 * 取得 BpmNodeWebService 实体
	 * 
	 * @param actDefId
	 * @param nodeId
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	protected BpmNodeWebService getFormObject(String nodeId, String actDefId, JSONObject jsonObject) throws Exception {
		Long id = (Long) jsonObject.get("wsid");
		BpmNodeWebService bpmNodeWebService = new BpmNodeWebService();
		bpmNodeWebService.setId(id);
		bpmNodeWebService.setNodeId(nodeId);
		bpmNodeWebService.setActDefId(actDefId);
		return bpmNodeWebService;
	}
}
