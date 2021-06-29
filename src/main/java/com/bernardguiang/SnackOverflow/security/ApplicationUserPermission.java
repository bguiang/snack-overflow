package com.bernardguiang.SnackOverflow.security;

public enum ApplicationUserPermission {

	PRODUCT_READ("product:read"),
	PRODUCT_WRITE("product:write"),
	CATEGORY_READ("category:read"),
	CATEGORY_WRITE("category:write");
	
	private final String permission;

	ApplicationUserPermission(String permission) {
		this.permission = permission;
	}
	
	public String getPermission() {
		return permission;
	}
}
