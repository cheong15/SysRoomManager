package com.hotent.core.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;



/**
 * 数据库操作基类。<br>
 * 包含基本的操作：增，查，改，删，列表，分页操作。
 * @version 1.0
 */
public abstract class BaseDao<E> extends GenericDao<E, Long>
{
	/**
	 * 通过oracle savePoint 方式进行批量SQL执行，执行过程中如有异常，将回滚到批量SQL执行前的状态
	 * @author wy
	 * @throws SQLException
	 */
	public boolean updateWithSavepoint(String[] sqls) throws Exception{
			Savepoint  sp = null;
			Connection con = null;
			Statement stmt = null;
			boolean rs = false;
		try{
			con = this.jdbcTemplate.getDataSource().getConnection();
			
			//关闭自动提交
	        con.setAutoCommit(false);  
			//设置保存点
			sp = con.setSavepoint("startPoint");//只有关闭了自动提交才能设置保存点
			//开启事务
			stmt = con.createStatement();

			for(String sql : sqls){
				stmt.executeUpdate(sql);
			}
			
			rs = true;
			
		}catch(Exception e){
			if(sp != null){
				try{
					System.out.println("回滚到保存点");
					con.rollback(sp);	
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			e.printStackTrace();
		}finally{
			try{
				//提交事务
				con.commit();
				if(stmt != null){
					//关闭事务
					stmt.close();
				}
				if(con != null){
					//关闭连接
					con.close();
				}
			}catch(Exception ex1){
				ex1.printStackTrace();
			}
		}
		return rs;
	}	
}
