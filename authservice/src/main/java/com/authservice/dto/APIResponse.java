package com.authservice.dto;

public class APIResponse<T> {
	//whenever from postman i call controller rest controller will always send data back 
	//as an APIResponse which will make my response as consistent for angular team
	//to consume the data with consistency. 
	
	private String message;
	
	private int status;
	
	private T data;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
	
}
