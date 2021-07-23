package com.bernardguiang.SnackOverflow.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bernardguiang.SnackOverflow.model.User;

public class ApplicationUserDetails implements UserDetails{
	private User user;
	private final List<? extends GrantedAuthority> grantedAuthorities;
	private String username;
	private String password;
	private final boolean isAccountNonExpired;
	private final boolean isAccountNonLocked;
	private final boolean isCredentialsNonExpired;
	private final boolean isEnabled;
	
	public ApplicationUserDetails(User user) {
		this.user = user;
		// Get granted authorities from user role
		String userRole = user.getRole();
		if(userRole.equalsIgnoreCase("CUSTOMER")) {
			this.grantedAuthorities = new ArrayList<>(ApplicationUserRole.CUSTOMER.getGrantedAuthorities());
		} else if(userRole.equalsIgnoreCase("ADMIN")) {
			this.grantedAuthorities = new ArrayList<>(ApplicationUserRole.ADMIN.getGrantedAuthorities());
		} else {
			this.grantedAuthorities = null;
		}
		
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.isAccountNonExpired = true;
		this.isAccountNonLocked = true;
		this.isCredentialsNonExpired = true;
		this.isEnabled = true;	
	}
	
	public User getUser() {
		return user;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

}
