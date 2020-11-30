package main;

import java.util.ArrayList;
import java.util.List;

public abstract class Student {
	protected String id;
	protected String firstName;
	protected String lastName;
	protected String programAndPlan;
	protected String academicLevel;
	protected String asurite;

//	protected ArrayList<String> headers = new ArrayList<String>(
//			Arrays.asList("ID", "First Name", "Last Name", "Program and Plan", "Academic Level", "ASURITE"));

	public void setStudentData(String csr) { // comma separated rows
		String[] arr = csr.split(",");
		this.id = arr[0];
		this.firstName = arr[1];
		this.lastName = arr[2];
		this.programAndPlan = arr[3];
		this.academicLevel = arr[4];
		this.asurite = arr[5];
	}

	public void setStudentData(List<String> list) {
		this.id = list.get(0);
		this.firstName = list.get(1);
		this.lastName = list.get(2);
		this.programAndPlan = list.get(3);
		this.academicLevel = list.get(4);
		this.asurite = list.get(5);
	}

	public void setStudentData(String id, String firstName, String lastName, String programAndPlan,
			String academicLevel, String asurite) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.programAndPlan = programAndPlan;
		this.academicLevel = academicLevel;
		this.asurite = asurite;
	}

	public String toString() {
		return this.id + "," + this.firstName + "," + this.lastName + "," + this.programAndPlan + ","
				+ this.academicLevel + "," + this.asurite;
	}

	public List<String> toList(){
		List<String> list = new ArrayList<String>();
		list.add(this.id);
		list.add(this.firstName);
		list.add(this.lastName);
		list.add(this.programAndPlan);
		list.add(this.academicLevel);
		list.add(this.asurite);
		return list;
	}

}
