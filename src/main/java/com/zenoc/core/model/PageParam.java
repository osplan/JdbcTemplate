package com.zenoc.core.model;

import com.zenoc.util.StringUtil;

public class PageParam {
	private String page;
	private String pageSize;
	
	public int getPage(int df) {
		return StringUtil.isNumeric(page)? Integer.parseInt(page,10):df;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public int getPageSize(int df) {
		return getPageSize(df, 20);
	}
	public int getPageSize(int df, int max) {
		return Math.min(StringUtil.isNumeric(pageSize)? Integer.parseInt(pageSize,10):df,max);
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getPage() {
		return page;
	}
	public String getPageSize() {
		return pageSize;
	}
}
