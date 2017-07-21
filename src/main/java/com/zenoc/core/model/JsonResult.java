package com.zenoc.core.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zenoc.util.Pagination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonResult {
	private boolean success;
	private String resultMsg;
	private String errCode;
	private Map<String, Object> map = null;
	
	private int page;
	private int pageSize;
	private int pageCount;
	private int totalCount;
	private List<?> list;
	private List<Integer> pageList;
	
	
	public JsonResult(){
		map = new HashMap<String, Object>();
		setSuccess(true);
	}
	public JsonResult(boolean success, String resultlMsg){
		map = new HashMap<String, Object>();
		setSuccess(success);
		setResultMsg(resultlMsg);
	}
	public JsonResult(boolean success, String resultlMsg, String errCode){
		map = new HashMap<String, Object>();
		setSuccess(success);
		setResultMsg(resultlMsg);
		setErrCode(errCode);
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
		map.put("success", success);
	}
	public boolean isSuccess() {
		return success;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg==null?"系统繁忙，请稍后再试！":resultMsg;
		map.put("resultMsg", this.resultMsg);
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
		map.put("errCode", errCode);
	}
	public String getErrCode() {
		return errCode;
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
		map.put("page", page);
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		map.put("pageSize", pageSize);
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
		map.put("pageCount", pageCount);
	}
	public List<?> getList() {
		return list;
	}
	public void setList(List<?> list) {
		this.list = list;
		map.put("list", list);
	}
	public List<Integer> getPageList() {
		return pageList;
	}
	public void setPageList(List<Integer> pageList) {
		this.pageList = pageList;
		map.put("pageList", pageList);
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		map.put("totalCount", totalCount);
	}
	public int getTotalCount() {
		return totalCount;
	}
	public <T> T attr(String key, T value){
		String notSet = ",success,resultMsg,errCode,page,pageSize,pageCount,list,pageList,totalCount,";
		if(notSet.indexOf(","+key+",")>=0)return null;
		map.put(key, value);
		return value;
	}
	public JsonResult attrAll(Map<String, ?> o){
		if(o!=null){
			for (Map.Entry<String, ?> entity :o.entrySet()){
				attr(entity.getKey(), entity.getValue());
			}
		}
		return this;
	}
	public<T> void setPageData(Pagination<T> pdata){
		setList(pdata.getResultList());
		setPageList(pdata.getPageNumList());
		setPage(pdata.getCurrentPage());
		setPageSize(pdata.getRowsPerPage());
		setPageCount(pdata.getPages());
		setTotalCount(pdata.getTotalCount());
	}
	public String toJson(String pattern){
		return new GsonBuilder().setDateFormat(pattern).create().toJson(map);
	}
	public String toJson(){
		return new Gson().toJson(map);
	}
	public Map<String, Object> asMap(){
		return map;
	}
}
