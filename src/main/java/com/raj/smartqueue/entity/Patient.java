package com.raj.smartqueue.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String gender;
    private String phone;
    private int estimatedTime;
    
    private Integer tokenNumber;
    private String status;
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getTokenNumber() {
	    return tokenNumber;
	}

	public void setTokenNumber(Integer tokenNumber) {
	    this.tokenNumber = tokenNumber;
	}

	public String getStatus() {
	    return status;
	}

	public void setStatus(String status) {
	    this.status = status;
	}
	
	public int getEstimatedTime() {
	    return estimatedTime;
	}

	public void setEstimatedTime(int estimatedTime) {
	    this.estimatedTime = estimatedTime;
	}
}