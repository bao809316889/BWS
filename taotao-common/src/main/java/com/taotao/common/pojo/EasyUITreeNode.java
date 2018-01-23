package com.taotao.common.pojo;

import java.io.Serializable;

/** 
 * ztree需要的返回值
 * @author E-mail: 18830160604@163.com
 * @date 2018年1月10日 上午10:53:23 
 * @version 1.0 
 */
public class EasyUITreeNode implements Serializable{
	private long id;
	private String text;
	private String state;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
