package com.wms.utils;

import java.io.Serializable;
import java.util.List;

public class PageUtil implements Serializable{

	private static final long serialVersionUID = 1L;
	//总记录数
	private Long totalCount;
	//每页记录数
	private int pageSize;
	//总页数
	private int totalPage;
	//当前页数
	private int currPage;
	//列表数据
	private List<?> list;
	/**
	 * 分页
	 * @param list        列表数据
	 * @param total  总记录数
	 */
	public PageUtil(List<?> list, Long total) {
		this.list = list;
		this.totalCount = total;
	}
	/**
	 * 分页
	 * @param list        列表数据
	 * @param totalCount  总记录数
	 * @param pageSize    每页记录数
	 * @param currPage    当前页数
	 */
	public PageUtil(List<?> list, Long totalCount, int pageSize, int currPage) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.currPage = currPage;
		this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	public List<?> getList() {
		return list;
	}
	public void setList(List<?> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "PageUtil [totalCount=" + totalCount + ", pageSize=" + pageSize + ", totalPage=" + totalPage
				+ ", currPage=" + currPage + ", list=" + list + "]";
	}

}
