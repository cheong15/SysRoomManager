if (typeof ReadOnlyQuery == 'undefined') {
	ReadOnlyQuery = {};
}

ReadOnlyQuery.init = function() {

	$("span[selectquery]").each(function() {
		var me = $(this);
		var cond = me.attr("selectvalue");
		var queryJson = JSON2.parse(me.attr("selectquery")
				.replaceAll("'", "\""));
		var key = queryJson.binding.key; // 返回值作为select的value
		var value = queryJson.binding.value; // 返回值作为select的显示值
		var query = queryJson.query;
		var querydataStr='';
		var datastr='{'
		for(var i in query){
			if(query[i].initValue){
				datastr+=query[i].condition+ ":\"" + query[i].initValue + "\",";
			} 
		}
		datastr = datastr.substring(0, datastr.length - 1);
		datastr +='}';
		if(datastr.length > 2){
			querydataStr=datastr;
		}
		queryCond = {
			alias : queryJson.name,
			querydata : querydataStr,
			page : 0,
			pagesize : 0
		};
		DoQuery(queryCond, function(data) {
					if (data.errors || data.list.length < 1) {
						return;
					}
					for (var i = 0; i < data.list.length; i++) {
						var dataobj = data.list[i];
						if (dataobj[key.toLowerCase()] == cond) {
							var datavalue = dataobj[value.toLowerCase()];
							me.children().text(datavalue);
						}
					}

				});

		

	});
}
