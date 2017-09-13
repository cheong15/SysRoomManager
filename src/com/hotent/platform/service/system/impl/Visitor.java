package com.hotent.platform.service.system.impl;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Text;
import org.dom4j.VisitorSupport;

import com.hotent.core.util.StringUtil;

public class Visitor extends VisitorSupport{
		private Map map = new HashMap() ;
		private String nodeName ;
		private String attrName ;
		
		public Visitor(){}
		
		public Visitor(String nodeName, String attrName){
			this.nodeName = nodeName ;
			this.attrName = attrName ;
		}
		
		@Override
		public void visit(Element node){
			//循环获取节点
			if(StringUtil.isEmpty(nodeName)) return ;
			if(nodeName.equals(node.getName())){
				Visitor visitor = new Visitor();
				visitor.setMap(map);
				node.accept(visitor);
			}
		}
		
		@Override
		public void visit(Attribute node) {
			//循环获取属性节点
			if(StringUtil.isEmpty(attrName)) return ;
			if(attrName.equals(node.getName())){
				map.put(attrName, node.getText());
			}
		}

		@Override
		public void visit(Text node) {
			//循环获取文本节点
			if(StringUtil.isNotEmpty(attrName) || StringUtil.isNotEmpty(nodeName)) return ;
			map.put(node.getParent().getName(), node.getText());
		}
		
		public Map getMap() {
			return map ;
		}
		
		public void setMap(Map map) {
			this.map = map;
		}

		public void setNodeName(String nodeName) {
			this.nodeName = nodeName;
		}

		public void setAttrName(String attrName) {
			this.attrName = attrName;
		}

	}