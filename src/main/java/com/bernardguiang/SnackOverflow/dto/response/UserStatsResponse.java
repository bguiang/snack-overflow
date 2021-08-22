package com.bernardguiang.SnackOverflow.dto.response;

public class UserStatsResponse {
	int newUsers;

	public UserStatsResponse(int newUsers) {
		this.setNewUsers(newUsers);
	}

	public int getNewUsers() {
		return newUsers;
	}

	public void setNewUsers(int newUsers) {
		this.newUsers = newUsers;
	}
	
	
}
