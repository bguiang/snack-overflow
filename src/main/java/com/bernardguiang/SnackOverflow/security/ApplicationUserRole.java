package com.bernardguiang.SnackOverflow.security;

import static com.bernardguiang.SnackOverflow.security.ApplicationUserPermission.CATEGORY_READ;
import static com.bernardguiang.SnackOverflow.security.ApplicationUserPermission.CATEGORY_WRITE;
import static com.bernardguiang.SnackOverflow.security.ApplicationUserPermission.ORDER_READ;
import static com.bernardguiang.SnackOverflow.security.ApplicationUserPermission.ORDER_WRITE;
import static com.bernardguiang.SnackOverflow.security.ApplicationUserPermission.PRODUCT_READ;
import static com.bernardguiang.SnackOverflow.security.ApplicationUserPermission.PRODUCT_WRITE;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.collect.Sets;


public enum ApplicationUserRole {
	CUSTOMER(Sets.newHashSet(
			PRODUCT_READ,
			CATEGORY_READ,
			ORDER_READ,
			ORDER_WRITE
	)),
	ADMIN(Sets.newHashSet(
			PRODUCT_READ,
			PRODUCT_WRITE,
			CATEGORY_READ,
			CATEGORY_WRITE,
			ORDER_READ,
			ORDER_WRITE
	));
	
	private final Set<ApplicationUserPermission> permissions;

	private ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
		this.permissions = permissions;
	}
	
	public Set<ApplicationUserPermission> getPermissions() {
		return permissions;
	}
	
	// Returns a Set of GrantedAuthority with
	// - the current role's permissions or ApplicationUserPermission (i.e. product:read)
	// - and the role name with the prefix "ROLE_" added (i.e. ROLE_ADMIN for ADMIN)
	public Set<GrantedAuthority> getGrantedAuthorities() {
		
		// Add permissions
		Set<GrantedAuthority> permissions = getPermissions().stream()
			.map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
			.collect(Collectors.toSet());
		
		// Add role
		permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
		
		return permissions;
	}
}
