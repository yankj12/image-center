package com.yan.image.center.vo;

import java.io.Serializable;
import java.util.List;

public class ResponseVo implements Serializable{

	private static final long serialVersionUID = 1L;

	private Boolean success;
	
	private String errorMsg;
	
	private Object result;
	
	private Long totalCount;
	
	private List results;
	
	private Long totalPageCount;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public List getResults() {
		return results;
	}

	public void setResults(List results) {
		this.results = results;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Long getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(Long totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
	
}
