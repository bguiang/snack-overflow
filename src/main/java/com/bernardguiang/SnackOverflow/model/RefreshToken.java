package com.bernardguiang.SnackOverflow.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String token;
	private Instant createdDate;
	
	@OneToOne(mappedBy = "refreshToken")
	private User user;
	
	public RefreshToken() {
	}
	public RefreshToken(long id, String token, Instant createdDate, User user) {
		this.id = id;
		this.token = token;
		this.createdDate = createdDate;
		this.user = user;
	}
	
	public RefreshToken(String token, Instant createdDate, User user) {
		this.token = token;
		this.createdDate = createdDate;
		this.user = user;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Instant getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "RefreshToken [id=" + id + ", token=" + token + ", createdDate=" + createdDate + ", user=" + user.getUsername() + "]";
	}
	
	
}
