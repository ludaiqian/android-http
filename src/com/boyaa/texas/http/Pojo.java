package com.boyaa.texas.http;

public abstract class Pojo {
	public Pojo() {}
	
	protected abstract Pojo parse(String str) throws Exception;
	
}
