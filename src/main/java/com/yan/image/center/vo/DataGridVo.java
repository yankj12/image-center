package com.yan.image.center.vo;

import java.io.Serializable;
import java.util.List;

public class DataGridVo implements Serializable{

	private static final long serialVersionUID = 1L;
	private boolean success;
	private String errorMsg;
	
	// 通过分页查询出来的数据
	private List rows;
	
	private int total;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
}
