/**可拖拽div插件,可与zTree的拖拽树配合使用**/
;(function($){
	$.fn.htselect=$.fn.htSelect=function(options){
		options=$.extend({
			divClass:'itemDiv',
			over:function(){
				var pad_top = $(this).position().top,
					t = $("div",$(this));
				t.show();
				t.animate({top:pad_top},100);				
			},
			out:function(){
				var t = $("div",$(this));
				t.animate({top:'0'},300,function(){t.hide();});				
			}
			},options);
		
		return this.each(function(){
			var me = $(this),
				ul = me.children("ul"),
				html = ['<div style="position:absolute;top:0;right:95px;" class="'];
			if(!ul)return;
		    if(ul.length==0)return;
			ul.remove();
			html.push(options.divClass);
			html.push('">');
			html.push(ul[0].outerHTML);
			html.push('</div>');
			me.append(html.join(''));
			
			me.mouseover(function(){options.over.call(me);}).mouseleave(function(){options.out.call(me);});
		});
	};
})(jQuery);