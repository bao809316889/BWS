package com.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;
/** 
 * 分页需要的数据
 * @author E-mail: 18830160604@163.com
 * @date 2018年1月10日 上午10:53:23 
 * @version 1.0 
 */
public class EasyUIDataGridResult implements Serializable{

	private long total;
	private List rows;
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	
}
