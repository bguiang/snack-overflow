package com.bernardguiang.SnackOverflow.dto.request;

import org.springframework.data.domain.Sort;

public class UserPage {
	private String search = "";
	private int pageNumber = 0;
	private int pageSize = 9;
	private Sort.Direction sortDirection = Sort.Direction.DESC;
	private String sortBy = "joinDate";

	public UserPage() {
		super();
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public Sort.Direction getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(Sort.Direction sortDirection) {
		this.sortDirection = sortDirection;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	
	
}
