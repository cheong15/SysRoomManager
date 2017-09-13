if (typeof QueryUI == 'undefined') {
	QueryUI = {};
}

QueryUI.init = function(){
	$("select[selectquery]").each(function(){
		var me = $(this),
			selectquery = me.attr("selectquery");
			
		if (!selectquery)
			return true;
		selectquery = selectquery.replaceAll("'", "\"");
		var queryJson = JSON2.parse(selectquery);
		var query = queryJson.query;
		
		for (var i = 0; i < query.length; i++) {
				var field;
				if(query[i].trigger=='-1')
				continue;
				// isMain是true 就是绑定主表的字段
				if (query[i].isMain=="true") {
					field = $(".formTable [name$=':" + query[i].trigger + "']");
				} else {
					field = me.parents('table.listTable').find("[name='" + query[i].trigger + "']");
					field.unbind("change");
				}
				if (field != "")
					QueryUI.change(field, me, queryJson);
		}
		QueryUI.getvalue(me, queryJson);
	});
};

QueryUI.change = function(field, SelectObj, queryJson) {
		field.bind("change", function() {
				QueryUI.getvalue(SelectObj, queryJson);
		});
	};

QueryUI.replaceValue = function(objValue) {
	return objValue.replaceAll(",", "#,");
}


QueryUI.getvalue = function(SelectObj, queryJson) {
	var query = queryJson.query;
	var fieldValue, subfield, checkfield,field;
	var querydataStr = "{";
	for (var i = 0; i < query.length; i++) {
		if (query[i].isMain=="true") {
			//输入框，下拉框，数据字典
			subfield = $(".formTable [name$=':" + query[i].trigger + "']");
			// 选择器
			field = $(".formTable [name$=':" + query[i].trigger + "ID']");
			// 单选、复选框
			checkfield = $(":checked[name$=':" + query[i].trigger + "']");
		} else {
			subfield = SelectObj.parents('table.listTable').find("[name='" + query[i].trigger + "']");
			field = SelectObj.parents('table.listTable').find("input[name='" + query[i].trigger + "ID']");
			checkfield = SelectObj.parents('table.listTable').find(":checked[name='" + query[i].trigger + "']");
		}
		switch (query[i].controlType) {
			//没有绑定任何表字段controlType=-1而有预设值时
			case '-1':break;
			// 输入框，下拉框，数据字典是一类直接获取值
			case '1' :
			case '2' :
			case '3' :
			case '11' :
			case '15' :
				fieldValue = subfield.val();
				break;
			// 选择器
			case '4' :
			case '5' :
			case '6' :
			case '7' :
			case '8' :
			case '17' :
			case '18' :
			case '19' :
				fieldValue =field.val();
				fieldValue = QueryUI.replaceValue(fieldValue);
				break;
			// 单选、复选框
			case '13' :
			case '14' :
				var tempvalue;
				checkfield.each(function() {
							var obj = $(this);
							if (typeof(tempvalue) != "undefined"
									&& tempvalue != "") {
								tempvalue += "#," + obj.val();
							} else {
								tempvalue = obj.val();
							}
						});
				fieldValue = tempvalue;
				tempvalue = "";
				break;
		}
		//如果对应的绑定字段有值则使用该值，没有值则判断设计时有没有输入预设值，有预设值则使用，没有则不做输入条件
		if (typeof(fieldValue) != "undefined" && fieldValue != ""&& fieldValue!=null) {
			querydataStr += query[i].condition + ":\"" + fieldValue + "\",";
		}else{
			if(query[i].initValue!='')
			querydataStr += query[i].condition + ":\"" + query[i].initValue + "\",";
		}
	}
	if (querydataStr.length > 1){
		querydataStr = querydataStr.substring(0, querydataStr.length - 1);
		querydataStr += "}";
		queryCond = {
			alias : queryJson.name,
			querydata : querydataStr,
			page : 0,
			pagesize : 0
		};
		SelectObj.empty();
		DoQuery(queryCond, function(data) {
					if (data.errors || data.list.length < 1) {
						return;
					}
					
					var oFrag=document.createDocumentFragment();
					for (var i = 0; i < data.list.length; i++) {
						var dataobj = data.list[i];
						var datavalue = dataobj[queryJson.binding.value.toLowerCase()];
						var datakey = dataobj[queryJson.binding.key.toLowerCase()];
						var isSame=false;
						//判断是否有重复的数据
						for (var j=0; j<i;j++){
							if(datakey== data.list[j][queryJson.binding.key.toLowerCase()]){
								isSame=true;
								break;
							}
						}
						if(isSame) continue;
						var opt = $("<option>").val(datakey).text(datavalue);
						
						oFrag.appendChild(opt[0]);
					}
					SelectObj.append(oFrag);
					var tempValue = SelectObj.attr("val");
					SelectObj.val(tempValue);
					SelectObj.trigger("change");
				});
	};
}
