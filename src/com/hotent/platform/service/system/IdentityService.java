package com.hotent.platform.service.system;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.platform.model.system.Identity;
import com.hotent.platform.dao.system.IdentityDao;

/**
 * 对象功能:流水号生成 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:ray
 * 创建时间:2012-02-03 14:40:59
 */
@Service
public class IdentityService extends BaseService<Identity>
{
	@Resource
	private IdentityDao dao;
	
	public IdentityService()
	{
	}
	
	@Override
	protected IEntityDao<Identity, Long> getEntityDao() 
	{
		return dao;
	}
	
	
	/**
	 * 根据别名是否存在。
	 * @param alias
	 * @return
	 */
	public boolean isAliasExisted(String alias){
		return dao.isAliasExisted(alias);
	}
	
	/**
	 * 根据别名是否存在。
	 * @param identity
	 * @return
	 */
	public boolean isAliasExistedByUpdate(Identity identity)
	{
		return dao.isAliasExistedByUpdate(identity);
	}
	
	/**
	 * 根据流程规则别名获取得当前流水号。
	 * @param alias		流水号规则别名。
	 * @return
	 */
	public String getCurIdByAlias(String alias){
		Identity identity=this.dao.getByAlias(alias);
		Integer curValue=identity.getCurValue();
		if(curValue==null) curValue=identity.getInitValue();
		String rtn=getByRule(identity.getRule(),identity.getNoLength(),curValue);
		
		return rtn;
	}
	
	/**
	 * 根据流程规则别名获取得下一个流水号。
	 * @param alias		流水号规则别名。
	 * @return
	 */
	public synchronized  String  nextId(String alias){
		Identity identity=this.dao.getByAlias(alias);
		String rule=identity.getRule();
		int step=identity.getStep();
		int genEveryDay=identity.getGenType();
		Integer curValue=identity.getCurValue();
		if(curValue==null) curValue=identity.getInitValue();
		//每天都生成
		if(genEveryDay==1){
			String curDate=getCurDate();
			String oldDate=identity.getCurDate();
			if(!curDate.equals(oldDate)){
				identity.setCurDate(curDate);
				curValue=identity.getInitValue();
			}
			else{
				curValue=curValue + step;
			}
		}
		else{
			curValue=curValue + step;
		}
		identity.setCurValue(curValue);
		dao.update(identity);
		
		String rtn=getByRule(rule,identity.getNoLength(),curValue);
		
		return rtn;
	}
	
	/**
	 * 返回当前日期。格式为 年月日。
	 * @return
	 */
	public static String getCurDate(){
		Date date=new Date();
		return (date.getYear() +1900) +"" +(date.getMonth() +1 )  +"" +date.getDate() ;
		
	}
	
	/**
	 * 根据规则返回需要显示的流水号。
	 * @param rule			流水号规则。
	 * @param length		流水号的长度。
	 * @param curValue		流水号的当前值。
	 * @return
	 */
	private String getByRule(String rule,int length,  int curValue){
		Date date=new Date();
		
		String year=(date.getYear() +1900) +"";
		int month=date.getMonth() +1;
		int day=date.getDate();
		String shortMonth="" + month;
		String longMonth=(month<10)?"0" + month :"" + month;
		
		String seqNo=getSeqNo(rule,curValue,length);
		
		String shortDay="" + day;
		String longDay=(day<10)?"0" + day :"" + day;
		
		String rtn=rule.replace("{yyyy}", year)
				.replace("{MM}", longMonth)
				.replace("{mm}", shortMonth)
				.replace("{DD}", longDay)
				.replace("{dd}", shortDay)
				.replace("{NO}", seqNo)
				.replace("{no}", seqNo );
		
		
		return rtn;
	}
	
	/**
	 * 根据当前流水号的值和流水号显示的长度。
	 * <pre>
	 * 比如：当前流水号为55 ，显示长度为5那么这个方法返回：00055。
	 * </pre>
	 * @param curValue		当前流水号的值。
	 * @param length		显示的长度。
	 * @return
	 */
	private static String getSeqNo(String rule,int curValue,int length){
		String tmp=curValue +"";
		int len= 0 ;
		if(rule.indexOf("no")>-1){
			len = length;
		}else{
			len = length-tmp.length();
		}
		String rtn="";
		switch (len) {
			case 1:
				rtn= "0";
				break;
			case 2:
				rtn= "00";
				break;
			case 3:
				rtn= "000";
				break;
			case 4:
				rtn= "0000";
				break;
			case 5:
				rtn= "00000";
				break;
			case 6:
				rtn= "000000";
				break;
			case 7:
				rtn= "0000000";
				break;
			case 8:
				rtn= "00000000";
				break;
			case 9:
				rtn= "000000000";
				break;
			case 10:
				rtn= "0000000000";
				break;
			case 11:
				rtn= "00000000000";
				break;
			case 12:
				rtn= "000000000000";
				break;
		}
		if(rule.indexOf("no")>-1){
			return tmp + rtn;
		}else{
			return rtn + tmp;
		}
		
	}
	
	/**
	 * 获取流水号规则列表。
	 * @return
	 */
	public List<Identity> getList(){
		return dao.getList();
	}
	
	
	
	
}
