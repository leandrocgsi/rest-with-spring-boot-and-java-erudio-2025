package br.com.erudio.data.dto.security;

import java.io.Serializable;
import java.util.Objects;

public class AccountCredentialsVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	private String fullName;

	public AccountCredentialsVO(){}

	public AccountCredentialsVO(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public AccountCredentialsVO(String username, String password, String fullName) {
		this.username = username;
		this.password = password;
		this.fullName = fullName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		AccountCredentialsVO that = (AccountCredentialsVO) o;
		return Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getPassword(), that.getPassword()) && Objects.equals(getFullName(), that.getFullName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUsername(), getPassword(), getFullName());
	}
}