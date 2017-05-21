package win.chenliwei.simplespringmvc.model;

import java.util.Date;

public class Employee {
	private String name;
	private Date birthday;
	public Employee() {
	}
	public Employee(String name, Date birthday) {
		this.name = name;
		this.birthday = birthday;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	

}
