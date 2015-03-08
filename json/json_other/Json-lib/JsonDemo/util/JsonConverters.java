package com.yajun.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.junit.Test;

import com.yajun.domain.Person;
import com.yajun.service.PersonService;

public class JsonConverters {
	
	PersonService service=new PersonService();

	//将javabean对象转换为json格式的字符串
	public String toJsonString(Person person){
		JSON json=JSONSerializer.toJSON(person);
		return json.toString();
	}
	
	@Test
	public void testToJsonString() throws Exception {
		
		System.out.println(toJsonString(service.getPerson()));
		//{"age":1002,"name":"李四2"}
	}
	//将list对象转换为json格式的字符串
	public String toJsonString(List<Person> list){
		JSON json=JSONSerializer.toJSON(list);
		return json.toString();
	}
	
	@Test
	public void testToJsonString2() throws Exception {
		System.out.println(toJsonString(service.getList()));
		//[{"age":1002,"name":"李四2"},{"age":1003,"name":"李四3"},{"age":1001,"name":"李四1"}]
	}
	
	//将json格式的字符串转换为javabean对象
	public Person toPerson(String jsonStr){
		Person person=new Person();
		JSONArray array=new JSONArray();
		array.add(jsonStr);
		JSONObject obj=array.getJSONObject(0);
		person.setAge(obj.getInt("age"));
		person.setName(obj.getString("name"));
		return person;
	}
	
	@Test
	public void testToPerson() throws Exception {
		String jsonStr="{'age':1002,'name':'李四2'}";
		System.out.println(toPerson(jsonStr));
	}
	
	public List<Person> toList(String jsonStr){
		List<Person> list=new ArrayList<Person>();
		Person person;
		JSONArray array=new JSONArray();
		array.add(jsonStr);
		JSONArray jsonArray=array.getJSONArray(0);
		for(int i=0;i<jsonArray.size();i++){
			person=new Person();
			JSONObject obj=jsonArray.getJSONObject(i);
			person.setAge(obj.getInt("age"));
			person.setName(obj.getString("name"));
			list.add(person);
		}
		return list;
	}
	
	@Test
	public void testToList() throws Exception {
		String jsonStr="[{'age':1002,'name':'李四2'},{'age':1003,'name':'李四3'},{'age':1001,'name':'李四1'}]";
		System.out.println(toList(jsonStr));
	}
}








