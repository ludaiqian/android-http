package com.boyaa.texas.test;

import com.boyaa.texas.http.Pojo;

public class TestPojo extends Pojo {
	public String name;
	public int age;

	@Override
	protected Pojo parse(String str) throws Exception{
		TestPojo pojo = new TestPojo();
		pojo.name = "hello name";
		pojo.age = 123;
		if (pojo != null)
			throw new Exception("dss");
		return new ErrorPojo();
	}

	@Override
	public String toString() {
		return "TestPojo [name=" + name + ", age=" + age + "]";
	}

}