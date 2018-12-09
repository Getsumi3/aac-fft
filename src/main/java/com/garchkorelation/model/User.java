package com.garchkorelation.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.garchkorelation.util.StaticVar;

@Entity
@Table(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID = 6743199784491931800L;

	private Long id;
	
	@Column(unique = true)
	private String username;
	@Column(unique = true)
	private String email;

	@JsonIgnore
	private String emailConfirm;
	@JsonIgnore
	private String password;
	@JsonIgnore
	private String passwordConfirm;

	private String country;
	private boolean receiveNewsletter;

	@JsonIgnore
	private Set<Role> roles;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Transient
	public String getEmailConfirm() {
		return emailConfirm;
	}

	public void setEmailConfirm(String emailConfirm) {
		this.emailConfirm = emailConfirm;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Transient
	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public boolean isReceiveNewsletter() {
		return receiveNewsletter;
	}

	public void setReceiveNewsletter(boolean receiveNewsletter) {
		this.receiveNewsletter = receiveNewsletter;
	}

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	public Set<Role> getRoles() {
		return roles;
	}

	
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Transient
	public boolean isModerator() {
		return checkRole(StaticVar.MODERATOR_ID);
	}

	@Transient
	public boolean isVerified() {
		return checkRole(StaticVar.UNVERIFIED_ID);
	}

	private boolean checkRole(Long id) {
		for (Role role : getRoles()) {
			if (role.getId() == id) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", email=" + email + "]";
	}

}
