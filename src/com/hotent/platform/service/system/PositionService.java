package com.hotent.platform.service.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.core.util.AppUtil;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.platform.dao.system.PositionDao;
import com.hotent.platform.dao.system.UserPositionDao;
import com.hotent.platform.model.system.Position;
import com.hotent.platform.model.system.UserPosition;

/**
 * 对象功能:系统岗位表 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2011-11-30 09:49:45
 */
@Service
public class PositionService extends BaseService<Position>
{
	@Resource
	private PositionDao positionDao;
	@Resource
	private UserPositionDao userPositionDao;
	
	public PositionService()
	{
	}
	
	@Override
	protected IEntityDao<Position, Long> getEntityDao() 
	{
		return positionDao;
	}
	
	

	/**
	 * 跟所parentId 取得GlobalType。
	 * @param parentId
	 * @return
	 */
	public Position getParentPositionByParentId(long parentId) {
		Position parent = positionDao.getById(parentId);
		if (parentId == 0 || parent == null) {
			parent = new Position();
			
			parent.setPosId(Position.ROOT_ID);
			parent.setDepth(Position.ROOT_DEPTH);
			parent.setNodePath(Position.ROOT_ID+".");
			parent.setParentId(Position.ROOT_PID);
			parent.setSn((short)0);

			parent.setPosName("岗位");
			parent.setPosDesc("岗位");
			
			return parent;
		} else {
			return parent;
		}
	}
	
	/**
	 * 根据nodePath查询
	 * @param nodePath
	 * @return
	 */
	public List<Position> getByNodePath(String nodePath){
		return positionDao.getByNodePath(nodePath);
	}
	
	/**
	 * 根据岗位名称获得岗位信息
	 * @param posName
	 * @return
	 */
	public List<Position> getByPosName(String posName) {
		return positionDao.getByPosName(posName);
	}
	
	
	/**
	 * 更新子结点路径
	 * @param father
	 * @param childrenList
	 */
	public void updateChildrenNodePath(Position father,List<Position> childrenList){
		
		Map<Long,List<Position>> mapData=coverMapData(father.getPosId(), childrenList);
		
		Set<Entry<Long, List<Position>>> set= mapData.entrySet();
		Iterator<Entry<Long, List<Position>>> it=set.iterator();
		
		while(it.hasNext()){
			Entry<Long, List<Position>> ent=it.next();
			Long parentId=ent.getKey();
			List<Position> list=ent.getValue();
			
			Position parent=getParentPositionByParentId(parentId);
			if(parent!=null&&list!=null&&list.size()>0){
				
				for(Position gt:list){
					if(gt.getPosId().longValue()!=father.getPosId().longValue()){
						gt.setParentId(parent.getPosId());
						gt.setNodePath(parent.getNodePath()+gt.getPosId()+".");
						gt.setDepth(parent.getDepth()+1);
						update(gt);
					}
				}
			}
			
			
		}
	}
	
	
	/**
	 * 转达化数据格式,把List<Dictionary>转化为Map<Long,List<Dictionary>>。
	 * @param rootId
	 * @param instList
	 * @return
	 */
	public Map<Long,List<Position>> coverMapData(Long rootId, List<Position> instList){
		Map<Long,List<Position>> dataMap=new HashMap<Long,List<Position>> ();
		dataMap.put(rootId.longValue(), new ArrayList<Position>());
		if(instList!=null&&instList.size()>0){
			for(Position gt:instList){
				long parentId=gt.getParentId().longValue();
				if(dataMap.get(parentId)==null){
					dataMap.put(parentId, new ArrayList<Position>());
				}
				dataMap.get(parentId).add(gt);
			}
		}
		return dataMap;
	}
	
	/**
	 * 转化数所格式,将原如list 重新按树形结构排序
	 * @param rootId
	 * @param instList
	 * @return
	 */
	public List<Position> coverTreeList(Long rootId, List<Position> instList){
		Map<Long,List<Position>> dataMap=coverMapData( rootId,  instList);
		return getChildListByDicId(rootId,dataMap);	
	}
	/**
	 * 转化数所格式,将原如list 重新按树形结构排序
	 * @param parentId
	 * @param dataMap
	 * @return
	 */
	private List<Position> getChildListByDicId(Long parentId,Map<Long,List<Position>> dataMap){
		List<Position> list=new ArrayList<Position>();
		
		List<Position> dicList=dataMap.get(parentId.longValue());
		if(dicList!=null&&dicList.size()>0){
			for(Position dic:dicList){
				list.add(dic);
				List<Position> childList=getChildListByDicId(dic.getPosId(),dataMap);
				list.addAll(childList);
			}
		}
		return list;
	}
	

	
	/**
	 * 根据id数组,删除SysGlType对像
	 * @param Long[] ids
	 * @return
	 */
	@Override
	public void delByIds(Long[] ids)
	{
		if(ids==null || ids.length==0) return ;
		
		for (long posId : ids){
			Position gt=positionDao.getById(posId);
			//留下路径
			String oldNodePath=gt.getNodePath();
			//取得下级节点
			List<Position> childrenList= positionDao.getByNodePath(oldNodePath);
			
			//删除外键
			/*
			 * 
			 */
			
			//删除主键
			this.delById(posId);
			
			//移动下级节点到父节点
			updateChildrenNodePath(getParentPositionByParentId(0),childrenList);
			
			
		}
		
	}
	
	/**
	 * 添加岗位和人员。
	 * 	1.如果当前人员是主岗位，则将该人员的之前的岗位修改为非主岗位。
	 * @param position
	 * @param upList
	 * @throws Exception 
	 */
	public void add(Position position,List<UserPosition> upList) throws Exception{
		this.add(position);
		if(BeanUtils.isEmpty(upList)) return;
		for(UserPosition up:upList){
			Long posId=position.getPosId();
			Long userId=position.getPosId();
			
			boolean isPrimary=up.getIsPrimary()==UserPosition.PRIMARY_YES;
			if(isPrimary){
				userPositionDao.updNotPrimaryByUser(userId);
			}
			up.setPosId(posId);
			up.setUserPosId(UniqueIdUtil.genId());
			userPositionDao.add(up);
		}
	}
	
	
	
	/**
	 * 根据parentId查询
	 * @param parentId
	 * @return
	 */
	public List<Position> getChildByParentId(long parentId){
		return positionDao.getChildByParentId(parentId);
	}
	
	/**
	 * 根据parentId查询在此根目录下包括的所有的下属岗位
	 * @param parentId
	 * @return
	 */
	public List<Position> getAllChildByParentId(long parentId){
		List<Position> ChildList= positionDao.getChildByParentId(parentId);
		Properties  prop = (Properties)AppUtil.getBean("configproperties");
		int level = Integer.parseInt(prop.getProperty("posExpandLevel", "0"));
		int childSize=ChildList.size();
		if(level==0){
			for(int i=0;i<childSize;i++){
				if(ChildList.get(i).getIsLeaf()==1) continue;
				List<Position> MoreList=getAllChildByParentId(ChildList.get(i).getPosId());
				ChildList.addAll(MoreList)	;
			}
		} 
		if(level>1){
			level--;
			for(int i=0;i<childSize;i++){
				if(ChildList.get(i).getIsLeaf()==1) continue;
				List<Position> MoreList=getAllChildByParentId(ChildList.get(i).getPosId(),level);
				ChildList.addAll(MoreList)	;
			}
		}
		return ChildList;
	}
	private List<Position> getAllChildByParentId(Long parentId, int level) {
		List<Position> ChildList = new ArrayList<Position>();
		if(level>0){
			ChildList= positionDao.getChildByParentId(parentId);
			level--;
			int childSize=ChildList.size();
			for(int i=0;i<childSize;i++){
				if(ChildList.get(i).getIsLeaf()==1) continue;
				List<Position> MoreList=getAllChildByParentId(ChildList.get(i).getPosId(),level);
				ChildList.addAll(MoreList)	;
			}
		}
		return ChildList;
	}

	/**
	 * 添加对像
	 * @param entity
	 */
	public void add(Position position)
	{
		positionDao.add(position);
		updateIsParent(position);
	}
	/**
	 * 修改对像
	 * @param entity
	 */
	public void update(Position position)
	{
		
		positionDao.update(position);
		updateIsParent(position);
	}
	/**
	 * 删除对像
	 * @param typeId
	 */
	public void delById(long posId){
		Position position =positionDao.getById(posId);
		positionDao.delById(posId);
		updateIsParent(position);
	}
	
	/**
	 * 更新岗位排序。
	 * @param aryPosId
	 */
	public void updSn(Long[] aryPosId ){
		for(int i=0;i<aryPosId.length;i++){
			short sn=(short) (i+1);
			positionDao.updSn(aryPosId[i], sn);
		}
	}
	
	/**
	 * 维护isParent字段
	 * @param globalType
	 */
	public void updateIsParent(Position position){
		if(position==null)return;
		long typeId=position.getPosId();
		long parentId=position.getParentId();
		position=positionDao.getById(typeId);
		Position parent=positionDao.getById(parentId);
		
		if(position!=null){
			int childCount=positionDao.getChildCountByParentId(position.getPosId());
			if(childCount>0){
				position.setIsParent(Position.IS_PARENT_Y);
			}else{
				position.setIsParent(Position.IS_PARENT_N);
			}
			positionDao.update(position);
		}
		
		if(parent!=null){
			int childCount=positionDao.getChildCountByParentId(parent.getPosId());
			if(childCount>0){
				parent.setIsParent(Position.IS_PARENT_Y);
			}else{
				parent.setIsParent(Position.IS_PARENT_N);
			}
			positionDao.update(parent);
		}
		
	}	
	
	/**
	 * 根据用户id获取岗位列表。
	 * @param userId
	 * @return
	 */
	public List<Position> getByUserId(Long userId){
		return positionDao.getByUserId(userId);
	}
	
	/**
     * 取得某个用户的岗位id字符串
     * 
     * @param userId 用户id
     * @return 用户所属岗位id字符串，多个则以","隔开，如1,2
     */
    public List<Long> getPositonIdsByUserId(Long userId) {
    	List<Long> list=new ArrayList<Long>();
        List<Position> positionList = this.positionDao.getByUserId(userId);
        for (Position pos : positionList) {
            list.add(pos.getPosId());
        }
        return list;
    }
    
    /**
     * 取得某个用户所属职群字符串
     * 
     * @param userId 用户id
     * @return 用户所属职群字符串职群以单引号包装，多个则以","隔开，如：'信息技术','营运'
     */
    public List<String> getPositonGroupsByUserId(Long userId) {
        List<String> list=new ArrayList<String>();
        List<Position> positionList = this.positionDao.getByUserId(userId);
        for (Position pos : positionList) {
        	list.add(pos.getPosgroup());
            
        }
        return list;
    }
    
    /**
	 * 根据用户id获取主岗位。
	 * @param userId
	 * @return
	 */
    public Position getPrimaryPositionByUserId(Long userId) {
    	return this.positionDao.getPosByUserId(userId);
    }
	
	
}
