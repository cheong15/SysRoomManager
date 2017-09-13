package com.hotent.core.mybatis.dialect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hotent.core.mybatis.Dialect;
import com.hotent.core.table.ITableOperator;
import com.hotent.core.table.SqlTypeConst;
import com.hotent.core.util.AppUtil;

/**
 * sql 2005 方言处理。
 * @author ray
 *
 */
public class SQLServer2005Dialect extends Dialect
{
	@Override
	public boolean supportsLimit()
	{
		return true;
	}

	@Override
	public boolean supportsLimitOffset()
	{
		return true;
	}

	/**
	 * Add a LIMIT clause to the given SQL SELECT
	 * The LIMIT SQL will look like:
	 * WITH query AS
	 * (SELECT TOP 100 percent ROW_NUMBER() OVER (ORDER BY CURRENT_TIMESTAMP) as __row_number__, * from table_name)
	 * SELECT *
	 * FROM query
	 * WHERE __row_number__ BETWEEN :offset and :lastRows
	 * ORDER BY __row_number__
	 * @param querySqlString The SQL statement to base the limit query off of.
	 * @param offset Offset of the first row to be returned by the query (zero-based)
	 * @param last Maximum number of rows to be returned by the query
	 * @return A new SQL statement with the LIMIT clause applied.
	 */
	@Override
	public String getLimitString(String querySqlString, int offset, String offsetPlaceholder, int limit, String limitPlaceholder)
	{
		int start = offset + 1;
		StringBuffer pagingBuilder = new StringBuffer();
		String orderby = getOrderByPart(querySqlString);
		String distinctStr = "";
		String loweredString = querySqlString.toLowerCase();
		String sqlPartString = querySqlString;
		if (loweredString.trim().startsWith("select"))
		{
			int index = 6;
			if (loweredString.startsWith("select distinct"))
			{
				distinctStr = "DISTINCT ";
				index = 15;
			}
			sqlPartString = sqlPartString.substring(index);
		}
		pagingBuilder.append(sqlPartString);
		// if no ORDER BY is specified use fake ORDER BY field to avoid errors
		if (orderby == null || orderby.length() == 0)
		{
			orderby = "ORDER BY CURRENT_TIMESTAMP";
		}
		StringBuffer result = new StringBuffer();
		result.append("WITH query AS (SELECT ").append(distinctStr).append("TOP 100 PERCENT ").append(" ROW_NUMBER() OVER (").append(orderby).append(") as __row_number__, ").append(pagingBuilder).append(") SELECT * FROM query WHERE __row_number__ BETWEEN ").append(start).append(" AND ").append(
			offset + limit).append(" ORDER BY __row_number__");
		return result.toString();
	}

	static String getOrderByPart(String sql)
	{
		String loweredString = sql.toLowerCase();
		int orderByIndex = loweredString.indexOf("order by");
		if (orderByIndex != -1)
		{
			// if we find a new "order by" then we need to ignore
			// the previous one since it was probably used for a subquery
			return sql.substring(orderByIndex);
		}
		else
		{
			return "";
		}
	}
	
	@Override
	public String getCountSql(String sql) {
		String sqlCount = sql;
		//alter by  xianggang   因为这里不需要做判断了  因为进入这里就已经表名数据源是sqlserver了。
//		ITableOperator tableOperator = (ITableOperator) AppUtil
//				.getBean(ITableOperator.class);
//		if (tableOperator.getDbType().equals(SqlTypeConst.SQLSERVER)) {
			sqlCount = sqlCount.trim();
			Pattern pattern = Pattern.compile("^SELECT(\\s+(ALL|DISTINCT))?",
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(sqlCount);
			if (matcher.find()) {
				String matStr = matcher.group();
				sqlCount = sqlCount.toUpperCase().replaceFirst(matStr.toUpperCase(),
						matStr.toUpperCase() + " TOP 100 PERCENT");
			} else {
				throw new UnsupportedOperationException("SQL语句拼接出现错误！");
			}
//		}
		sqlCount = "select count(*) amount from (" + sqlCount + ") A";
		return sqlCount;
	}

}
