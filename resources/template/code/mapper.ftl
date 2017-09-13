<#if table.isExternal==0>
<#assign tableName='W_'+table.tableName?upper_case >
<#else>
<#assign tableName=table.tableName>
</#if>
<#assign package=table.variable.package>
<#assign class=table.variable.class>
<#assign fieldList=table.fieldList>
<#assign type="com.hotent."+system+".model."+package+"." +class>
<#function getJdbcType dataType>
<#assign dbtype=dataType?lower_case>
<#assign rtn>
<#if  dbtype=="number" >
NUMERIC
<#elseif (dbtype?index_of("char")>-1)  >
VARCHAR
<#elseif (dbtype=="date")>
DATE
<#elseif (dbtype?ends_with("clob")) >
CLOB
</#if></#assign>
 <#return rtn?trim>
</#function>


<#-- 模板开始  -->
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd"> 
<mapper namespace="${type}">
	<resultMap id="${class}" type="${type}">
		<#if table.isExternal==0>
		<id property="id" column="ID" jdbcType="NUMERIC"/>
		<#if table.isMain==0>
		<result property="refId" column="REFID" jdbcType="NUMERIC"/>
		</#if>
		<#list fieldList as field>
		<result property="${field.fieldName}" column="F_${field.fieldName?upper_case}" jdbcType="${getJdbcType(field.fieldType)}"/>
		</#list>
		<#else>
		<#list fieldList as field>
		<#if table.pkField?lower_case==field.fieldName?lower_case>
		<id property="${table.pkField}" column="${field.fieldName?upper_case}" jdbcType="NUMERIC"/>
		</#if>
		</#list>
		<#list fieldList as field>
		<#if table.pkField?lower_case!=field.fieldName?lower_case>
		<result property="${field.fieldName}" column="${field.fieldName?upper_case}" jdbcType="${getJdbcType(field.fieldType)}"/>
		</#if>
		</#list>
		</#if>
	</resultMap>

	<sql id="columns">
		<#if table.isExternal==0>
		ID,<#if table.isMain==0>REFID,</#if><#list fieldList as field>F_${field.fieldName?upper_case}<#if field_has_next>,</#if></#list>
		<#else>
		<#list fieldList as field>${field.fieldName?upper_case}<#if field_has_next>,</#if></#list>
		</#if>
	</sql>
	
	<sql id="dynamicWhere">
		<where>
			<#if table.isExternal==0>
			<#list fieldList as field>
			<#if (field.fieldType=="varchar")>
			<if test="@Ognl@isNotEmpty(${field.fieldName})"> AND F_${field.fieldName?upper_case}  LIKE '%<#noparse>${</#noparse>${field.fieldName}}%'  </if>
			<#else>
			<#if (field.fieldType=="date")>
			<if test="@Ognl@isNotEmpty(${field.fieldName})"> AND F_${field.fieldName?upper_case}  =<#noparse>#{</#noparse>${field.fieldName}} </if>
			<if test="@Ognl@isNotEmpty(begin${field.fieldName})"> AND F_${field.fieldName?upper_case}  >=<#noparse>#{</#noparse>begin${field.fieldName},jdbcType=DATE} </if>
			<if test="@Ognl@isNotEmpty(end${field.fieldName})"> AND F_${field.fieldName?upper_case} <![CDATA[ <=<#noparse>#{</#noparse>end${field.fieldName},jdbcType=DATE}]]> </if>
			<#else>
			<if test="@Ognl@isNotEmpty(${field.fieldName})"> AND F_${field.fieldName?upper_case}  =<#noparse>#{</#noparse>${field.fieldName}} </if>
			</#if>
			</#if>
			</#list>
			<#else>
			<#list fieldList as field>
			<#if table.pkField?lower_case!=field.fieldName?lower_case>
			<#if (field.fieldType=="varchar")>
			<if test="@Ognl@isNotEmpty(${field.fieldName})"> AND ${field.fieldName?upper_case}  LIKE '%<#noparse>${</#noparse>${field.fieldName}}%'  </if>
			</#if>
			<#if (field.fieldType=="date")>
			<if test="@Ognl@isNotEmpty(${field.fieldName})"> AND ${field.fieldName?upper_case}  =<#noparse>#{</#noparse>${field.fieldName}} </if>
			<if test="@Ognl@isNotEmpty(begin${field.fieldName})"> AND ${field.fieldName?upper_case}  >=<#noparse>#{</#noparse>begin${field.fieldName},jdbcType=DATE} </if>
			<if test="@Ognl@isNotEmpty(end${field.fieldName})"> AND ${field.fieldName?upper_case} <![CDATA[ <=<#noparse>#{</#noparse>end${field.fieldName},jdbcType=DATE}]]> </if>
			</#if>
			<#else>
			<if test="@Ognl@isNotEmpty(${field.fieldName})"> AND ${field.fieldName?upper_case}  =<#noparse>#{</#noparse>${field.fieldName}} </if>
			</#if>
			</#list>
			</#if>
		</where>
	</sql>

	<insert id="add" parameterType="${type}">
		INSERT INTO ${tableName}
		<#if table.isExternal==0>
		(ID,<#if table.isMain==0>REFID,</#if>
		<#list fieldList as field>F_${field.fieldName?upper_case}<#if field_has_next>,</#if></#list>)
		VALUES
		(<#noparse>#{</#noparse>id,jdbcType=NUMERIC},
		<#if table.isMain==0>
		<#noparse>#{</#noparse>refId,jdbcType=NUMERIC},
		</#if>
		<#list fieldList as field><#noparse>#{</#noparse>${field.fieldName},jdbcType=${getJdbcType(field.fieldType)}<#noparse>}</#noparse><#if field_has_next>, </#if></#list>)
		<#else>
		(<#list fieldList as field>${field.fieldName?upper_case}<#if field_has_next>,</#if></#list>)
		VALUES
		(<#list fieldList as field><#noparse>#{</#noparse>${field.fieldName},jdbcType=${getJdbcType(field.fieldType)}<#noparse>}</#noparse><#if field_has_next>, </#if></#list>)
		</#if>
	</insert>
	
	<delete id="delById" parameterType="java.lang.Long">
		DELETE FROM ${tableName} 
		WHERE
		<#if table.isExternal==0>
		ID=<#noparse>#{</#noparse>id}
		<#else>
		${table.pkField?upper_case}=<#noparse>#{</#noparse>${table.pkField}}
		</#if>
	</delete>
	
	<update id="update" parameterType="${type}">
		UPDATE ${tableName} SET
		<#if table.isExternal==0>
		<#if table.isMain==0>
		REFID=<#noparse>#{</#noparse>refId,jdbcType=NUMERIC},
		<#noparse>#{</#noparse>refId,jdbcType=NUMERIC},
		</#if>
		<#list fieldList as field>
		F_${field.fieldName?upper_case}=<#noparse>#{</#noparse>${field.fieldName},jdbcType=${getJdbcType(field.fieldType)}<#noparse>}</#noparse><#if field_has_next>,</#if>
		</#list>
		WHERE
		ID=<#noparse>#{</#noparse>id}
		<#else>
		<#list fieldList as field>
		<#if table.pkField?upper_case!=field.fieldName?upper_case>
		${field.fieldName?upper_case}=<#noparse>#{</#noparse>${field.fieldName},jdbcType=${getJdbcType(field.fieldType)}<#noparse>}</#noparse><#if field_has_next>,</#if>
		</#if>
		</#list>
		WHERE
		${table.pkField?upper_case}=<#noparse>#{</#noparse>${table.pkField}}
		</#if>
	</update>
	
	<#if table.isMain!=1>
	<delete id="delByMainId">
	    DELETE FROM ${tableName}
	    WHERE
	    <#if table.isExternal==0>
	    REFID=<#noparse>#{</#noparse>refId}
	    <#else>
	    ${table.relation?upper_case}=<#noparse>#{</#noparse>${table.relation?lower_case}}
	    </#if>
	</delete>    
	
	<select id="get${class}List" resultMap="${class}">
	    SELECT <include refid="columns"/>
	    FROM ${tableName} 
	    <#if table.isExternal==0>
	    WHERE REFID=<#noparse>#{</#noparse>refId}
	    <#else>
	    WHERE ${table.relation?upper_case}=<#noparse>#{</#noparse>${table.relation?lower_case}}
	    </#if>
	</select>
	</#if>
		    
	<select id="getById" parameterType="java.lang.Long" resultMap="${class}">
		SELECT <include refid="columns"/>
		FROM ${tableName}
		WHERE
		<#if table.isExternal==0>
		ID=<#noparse>#{</#noparse>id}
		<#else>
		${table.pkField?upper_case}=<#noparse>#{</#noparse>${table.pkField}}
		</#if>
	</select>
	
	<select id="getAll" resultMap="${class}">
		SELECT <include refid="columns"/>
		FROM ${tableName}   
		<include refid="dynamicWhere" />
		<if test="@Ognl@isNotEmpty(orderField)">
		order by <#noparse>${orderField}</#noparse> <#noparse>${orderSeq}</#noparse>
		</if>
		<if test="@Ognl@isEmpty(orderField)">
		<#if table.isExternal==0>
		order by ID  desc
		<#else>
		order by ${table.pkField?upper_case} desc
		</#if>
		</if>
	</select>
</mapper>
