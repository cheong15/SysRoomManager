/*
 * Copyright (C), 2011-2012, Sunrise Tech. Co., Ltd.
 * SUNRISE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hotent.gzdc.model.kettle;


public class RepositoryJson {
	private String id;
	private String text;
	private boolean expanded;
	private boolean leaf;
	private String parentId;
	private String filePath;
	private String fileName;
	private String fileType;
	
	public RepositoryJson(){
		
	}
	
	public RepositoryJson(String id, String text, boolean expanded, boolean leaf){
		this.id = id;
		this.text = text;
		this.expanded = expanded;
		this.leaf = leaf;
	}
	
	public RepositoryJson(String id, String text, boolean expanded, boolean leaf, String parentId){
		this.id = id;
		this.text = text;
		this.expanded = expanded;
		this.leaf = leaf;
		this.parentId = parentId;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
