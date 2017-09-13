package net.gmcc.gz.service.sm;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import net.gmcc.gz.dao.sm.OpDataLogDao;
import net.gmcc.gz.model.sm.EmpLog;
import net.gmcc.gz.model.sm.OpDataLog;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.core.util.ContextUtil;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.platform.model.system.SysUser;

/**
 *<pre>
 * 对象功能:操作记录数据保存 Service类
 * 开发公司:从兴技术有限公司
 * 开发人员:John
 * 创建时间:2014-04-26 18:13:32
 *</pre>
 */
@Service
public class OpDataLogService extends BaseService<OpDataLog>
{
	@Resource
	private OpDataLogDao dao;
	@Resource
	public EmpLogService empLogService;
	
	
	
	public OpDataLogService()
	{
	}
	
	@Override
	protected IEntityDao<OpDataLog, Long> getEntityDao() 
	{
		return dao;
	}
	
	public int update(String sql) throws Exception{
		int cou = dao.update(sql);
		return cou;
	}
	
	public  List<Map<String,Object>>  queryForList(String sql) throws Exception{
		List<Map<String,Object>> list = dao.queryForList(sql);
		return list;
	}
	
	/**
	 * 记录数据日志 1新增 2 修改 3 删除
	 * @param map 数据Map
	 * @param empid 员工ID
	 * @param type 新增、删除：第一次调用不生成记录，第二次调用创建一条新的记录    修改：第一次调用创建一条新的记录，第二次调用保存RevisedData到recid的记录中
	 * @param id 表ID
	 * @param dataType 1 出口调用   2 入口调用 
	 * @param recid 记录ID
	 */
	public void opDataLog(Map map,String empid,String type,Long id,String dataType,Long recid) throws Exception{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		OpDataLog opDataLog = new OpDataLog();
		//-----------初始化参数----------------
		SysUser curUser = ContextUtil.getCurrentUser();
		Long userId = curUser.getUserId();
		String userName=curUser.getFullname();
		List<Map<String,Object>> list = this.queryMain(empid);
		String empname = "";
		if(list.size()>0 && list!=null){
			empname = (list.get(0)).get("FULLNAME")+"";
		}
		
		Date create_time = new Date();
		
		//---------初始化实体---------------------	
		opDataLog.setRecId(recid);
		opDataLog.setUserId(userId.toString());
		opDataLog.setUserName(userName);
		opDataLog.setEmployeeId(empid);
		opDataLog.setEmployeeName(empname);
		opDataLog.setOpType(type);
		opDataLog.setTableid(id);
		opDataLog.setCreateTime(create_time);
		//--需要对MAP的值进行循环处理，NULL置为空，设置时间格式yyyy-MM-dd HH:mm:ss
		Set<String> s = map.keySet();//获取KEY集合
		for (String str : s) {
			//String value = map.get(str)+"";
			Object ob = map.get(str);
			if("null".equals(ob+"")){
				map.put(str,"");
			}
			if(ob instanceof Date){
				java.util.Date date;
				DateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				date = df1.parse(ob+"");
				df1 = new SimpleDateFormat("yyyy-MM-dd");
				String dd = df1.format(date);
				map.put(str,dd);
			}
		}		
		if("1".equals(type)||"3".equals(type)){
			if("2".equals(dataType)){//入口调用
				JSONObject ja = JSONObject.fromObject(map);
				opDataLog.setRawData("");
				opDataLog.setRevisedData(ja.toString());
				opDataLog.setStatus("0");
				this.add(opDataLog);
			}	
		}else if("2".equals(type)){
			JSONObject ja = JSONObject.fromObject(map);
			if("1".equals(dataType)){//出口调用
				opDataLog.setRawData(ja.toString());
				opDataLog.setRevisedData("");
				opDataLog.setStatus("3");
				this.add(opDataLog);
			}else{//入口调用
				String sql = "update sm_emp_op_log t set t.revised_data='"+ja.toString()+"',t.status='0' where t.rec_id="+recid;
				this.update(sql);
			}
		}
	}
	
	public static String getTableDes(List<Map<String,Object>> list,String tableid){
		String tableDes = "";
		try{
			if(list.size()>0&&list!=null)
			for(int i=0;i<list.size();i++){
				Map map = list.get(i);
				if(tableid.equals(String.valueOf(map.get("TABLEID")))){
					tableDes = String.valueOf(map.get("TABLEDESC"));
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return tableDes;
		}
		return tableDes;
	}
	
	public void deelOpData() throws Exception{
		//1、查询没有处理的数据(一次查询100条，处理时间较早的)
		String sql = "select * from sm_emp_op_log t where t.status='0' and rownum<100 order by t.create_time asc";
		String tSql = "select t.tableid,t.tabledesc from bpm_form_table t";
		List<Map<String,Object>> list = this.queryForList(sql);
		List<Map<String,Object>> tList = this.queryForList(tSql);
		//2、解析数据，并根据记录类型，生成不同的操作日志，并向sm_emp_log插入一条记录
		if(list.size()>0 && list!=null){//这里需要注意，返回结果
			for(int i=0;i<list.size();i++){
				Long recid = UniqueIdUtil.genId();
				EmpLog empLog = new EmpLog();
				Map map = list.get(i);
				String tableDes = getTableDes(tList,String.valueOf(map.get("TABLEID")));
				String type = map.get("OP_TYPE")+"";
				
				empLog.setRecId(recid);
				empLog.setUserId(map.get("USER_ID")+"");
				empLog.setUserName(map.get("USER_NAME")+"");
				empLog.setEmployeeId(map.get("EMPLOYEE_ID")+"");
				empLog.setEmployeeName((map.get("EMPLOYEE_NAME")+""));
				empLog.setCreateTime(new Date());
				if("1".equals(type)||"3".equals(type)){//新增
					String optype = "新增";
					if("3".equals(type))optype = "删除";
					String data = map.get("REVISED_DATA")+"";//新增的数据
					if(!"".equals(data) && !"null".equals(data) && data!=null){
						Map<String, Object> datamap = deelJsonStr( data);
						String log = deelAddLog(datamap);//----！！--这里需要把日志描述的更加详细
						empLog.setOpContent(tableDes+optype+":"+log);
						empLogService.add(empLog);
					}
				}else if("2".equals(type)){//修改
					String raw_data = map.get("RAW_DATA")+"";//原始数据
					String revised_data = map.get("REVISED_DATA")+"";//修改后的数据的数据
					if(!"".equals(raw_data) && !"null".equals(raw_data) && raw_data!=null && !"".equals(revised_data) && !"null".equals(revised_data) && revised_data!=null){
						Map<String, Object> rawmap = deelJsonStr( raw_data);
						Map<String, Object> revisedmap = deelJsonStr( revised_data);
						String log = deelEditLog(rawmap,revisedmap);
						if(!"".equals(log)){
							empLog.setOpContent(tableDes+"修改:"+log);
							empLogService.add(empLog);
						}
					}
				}
				//3、记录状态置为已经处理
				String update = "update sm_emp_op_log t set t.status = '1' where t.rec_id='"+map.get("REC_ID")+"'";
				update(update);
			}
		}
	}
	
	/**
	 * 查询字段的描述
	 * @param tableid
	 * @param colname
	 * @return
	 * @throws Exception
	 */
	public String queryColDes(String tableid,String colname) throws Exception{
		//String sql = "select t.fielddesc from bpm_form_field t where t.tableid='"+tableid+"' and t.fieldname='"+colname+"'";
		String sql = "select t.fielddesc from bpm_form_field t where t.fieldname='"+colname.toUpperCase()+"'";
		List<Map<String,Object>> list = this.queryForList(sql);
		String coldes = "";
		if(list.size()>0 && list!=null){
			coldes = (list.get(0)).get("FIELDDESC")+"";
		}
		return coldes;
	}
	
	/**
	 * JSON转换MAP
	 * @param jsonObjectData
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> deelJsonStr(String jsonObjectData) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(jsonObjectData);
		Map<String, Object> mapJson = JSONObject.fromObject(jsonObject);
		return mapJson;
	}
	
	/**
	 * 拼接修改日志
	 * @param map1
	 * @param map2
	 * @return
	 * @throws Exception
	 */
	public String deelEditLog(Map<String, Object> map1,Map<String, Object> map2)  throws Exception{
		String content ="";
		for (Entry<String, Object> entry : map1.entrySet()) {
			Object strval1 = entry.getValue();
			String key = entry.getKey();
			String value1 = entry.getValue()+"";
			String value2 = map2.get(key)+"";
			if(!value1.equals(value2)){
				String coldes = queryColDes("",key);
				content+="["+coldes+":由<"+value1+">修改为<"+value2+">]";
			}
		}
		return content;
	}
	
	/**
	 * 拼接新增的日志
	 * @param map1
	 * @return
	 * @throws Exception
	 */
	public String deelAddLog(Map<String, Object> map1)  throws Exception{
		String content ="";
		for (Entry<String, Object> entry : map1.entrySet()) {
			Object strval1 = entry.getValue();
			String key = entry.getKey();
			String value1 = entry.getValue()+"";
			String coldes = queryColDes("",key);
			content+="["+coldes+":"+value1+"]";
		}
		return content;
	}
	
	/**
	 * 根据市公司ID查询员工主信息
	 * @param localstaffcode
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryMain(String localstaffcode)throws Exception{
		String sql = "select * from sm_staffinfo_main t where t.localstaffcode='"+localstaffcode+"'";
		List<Map<String,Object>> list = this.queryForList(sql);
		return list;
	}
	
	/**
	 * 根据表ID查询表信息
	 * @param tableid
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryTable(String tableid)throws Exception{
		String sql= "select t.tablename,t.tabledesc from bpm_form_table t where t.tableid='"+tableid+"'";	
		List<Map<String,Object>> list = this.queryForList(sql);
		return list;
	}
	
	/**
	 * 定时清理sm_emp_op_log无用的数据
	 */
	public void cleanUpGarbage(){
		String sql = "delete from sm_emp_op_log t where t.op_type = '2' and t.status = '3'  and to_char(t.create_time, 'yyyy-mm-dd') < to_char(sysdate, 'yyyy-mm-dd')";
		try{
			dao.update(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
     * 将一个 JavaBean 对象转化为一个  Map
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Map convertBean(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }
}
