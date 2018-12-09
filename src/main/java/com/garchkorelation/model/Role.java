package com.garchkorelation.model;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role implements Serializable, Comparator<Role> {

	private static final long serialVersionUID = 4605014087825800475L;

	private Long id;
	private String name;
	private Set<User> users;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(mappedBy = "roles")
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
	public int compare(Role o1, Role o2) {
		return (int) (o1.getId()-o2.getId());
	}

	
}
