package com.yajun.service;

import java.util.ArrayList;
import java.util.List;

import com.yajun.domain.Person;

public class PersonService {

	public PersonService() {	}

	public List<Person> getList() {
		List<Person> list = new ArrayList<Person>();
		list.add(new Person(1002,"李四2"));
		list.add(new Person(1003,"李四3"));
		list.add(new Person(1001,"李四1"));
		return list;
	}
	public Person getPerson(){
		return new Person(1002,"李四2"); 
	}
}
