<#setting number_format="0">
<#function getFieldList fieldList>
 	<#assign rtn>
 		<#assign index=0>
		<#list fieldList as field>
			<#if field.isHidden == 0>
			<#if index % 3 == 0>
			<tr style="height: 23px;">
			</#if>
				<td style="width:13%;" class="formTitle" nowrap="nowarp"><span>${field.fieldDesc}</span>:</td>
				<td style="width:20%;" class="formInput" >
					<@input field=field/>
				</td>
				<#if index % 3 == 0 && !field_has_next>
					<td style="width:13%;" class="formTitle"></td>
					<td style="width:20%;" class="formInput"></td>
					<td style="width:13%;" class="formTitle"></td>
					<td style="width:20%;" class="formInput"></td>
				</#if>
				<#if index % 3 == 1 && !field_has_next>
					<td style="width:13%;" class="formTitle"></td>
					<td style="width:20%;" class="formInput"></td>
				</#if>
				<#if index % 3 == 2 || !field_has_next>
					</tr>
				</#if>
			<#assign index=index+1>
			</#if>
		</#list>
 	</#assign>
	<#return rtn>
</#function>
<#function setTeamField teams>
 	<#assign rtn>
		 <#list teams as team>
				<tr>
					<td colspan="6" class="teamHead">${team.teamName}</td>
				</tr>
				${getFieldList(team.teamFields)}
		</#list>
	</#assign>
	<#return rtn>
</#function>
<br />
<table cellpadding="2" cellspacing="0" border="1" class="formTable">
	<tr>
		<td colspan="6"  class="formHead">${table.tableDesc }</td>
	</tr>
	<#if teamFields??>
		<#if isShow>
			<#if showPosition == 1>
				${setTeamField(teamFields)}
				${getFieldList(fields)}
			<#else>
				${getFieldList(fields)}
				${setTeamField(teamFields)}
			</#if>
		<#else>
			${setTeamField(teamFields)}
		</#if>
	<#else>
		${getFieldList(fields)}
	</#if>	
</table>
<br />
