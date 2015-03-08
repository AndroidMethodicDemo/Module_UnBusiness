package com.yajun.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.yajun.domain.Person;

public class JsonTools {

	public JsonTools() {	}
	
	//����javabean���󣬴�Ϊjson��ʽ���ַ���
	public String getString(Person person) {
		JSON json = JSONSerializer.toJSON(person);
		return json.toString();
	}

	//����json��ʽ���ַ���������Ϊjavabean����
	public static Person getPerson(String msgString) {
		Person person = new Person();
		JSONArray array = new JSONArray();
		array.add(msgString);
		JSONObject obj = array.getJSONObject(0);
		// System.out.println(obj.get("age"));
		// System.out.println(obj.get("name"));
		person.setAge(obj.getInt("age"));
		person.setName(obj.getString("name"));
		return person;
	}
	//����list����ת��Ϊjson��ʽ���ַ���
	public static String getListString(List<Person> listPersons) {
		JSON json = JSONSerializer.toJSON(listPersons);
		return json.toString();
	}
	//����json��ʽ���ַ�����ת��Ϊlist����
	public static List<Person> getPersons(String str) {
		List<Person> list = new ArrayList<Person>();
		JSONArray array = new JSONArray();
		array.add(str);
		JSONArray array2 = array.getJSONArray(0);
		for (int i = 0; i <array2.size(); i++) {
			JSONObject jsonObject =  array2.getJSONObject(i);
			Person person = new Person();
			person.setAge(jsonObject.getInt("age"));
			person.setName(jsonObject.getString("name"));
			list.add(person);
		}
		return list;
	}

}
