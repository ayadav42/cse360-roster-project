package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class Repository extends Observable {

	List<Student> students;
	List<String> csvHeaders;
	int numOfNewStudents = 0;

	public Repository() {
		students = new ArrayList<Student>();
		csvHeaders = new ArrayList<String>();
		csvHeaders.add("ID");
		csvHeaders.add("First Name");
		csvHeaders.add("Last Name");
		csvHeaders.add("Program and Plan");
		csvHeaders.add("Academic Level");
		csvHeaders.add("ASURITE");
	}

	public void displayStudents() {
		for (Student student : students) {
			System.out.println(student);
		}
	}

	public void displayHeaders() {
		System.out.println(csvHeaders);
	}

	public void display() {
		displayHeaders();
		displayStudents();
	}

	public void addStudentsFromRoster(FileInputStream fis) throws Exception {
//	    File fileTemplate = new File( <<path to your file >>);
//	    FileInputStream fis = new FileInputStream(fileTemplate);
		Reader fr = new InputStreamReader(fis, "UTF-8");
		addStudentsFromRoster(fr);
	}

	public void addStudentsFromRoster(Reader fr) throws Exception {
		try {

			List<String> values = CSV_Helper.parseLine(fr);
			while (values != null) {
				int oldIndex = findStudentIndexById(values.get(0));
				if (oldIndex == -1) {
					students.add(new StudentCoreData(values));
				} else {
					// discard
				}
				values = CSV_Helper.parseLine(fr);
			}
			fr.close();
			setChanged();
			notifyObservers();
//			notifyObservers(getListOfStringsForTable());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void addAttendance(FileInputStream fis, String dateHeader) throws Exception {
		Reader fr = new InputStreamReader(fis, "UTF-8");
		addAttendance(fr, dateHeader);
	}

	public void addAttendance(Reader fr, String dateHeader) throws Exception {
		csvHeaders.add(dateHeader);
		List<StudentAttendance> listOfAttendance = new ArrayList<StudentAttendance>();
		try {
			List<String> values = CSV_Helper.parseLine(fr);
			while (values != null) {
				int oldIndex = findStudentAttendanceIndexByAsurite(listOfAttendance, values.get(0));
				StudentAttendance sta = null;
				if (oldIndex != -1) {
					sta = listOfAttendance.get(oldIndex);
					sta.incrementTime(values.get(1));
				} else {
					sta = new StudentAttendance(values);
					listOfAttendance.add(sta);
				}
				values = CSV_Helper.parseLine(fr);
			}
			fr.close();

			for (StudentAttendance studentAtt : listOfAttendance) {
				int oldIndex = findStudentIndexByAsurite(studentAtt.asurite);
				if (oldIndex != -1) {
					Student oldStudent = students.get(oldIndex);
//					System.out.println("oldStudent: " + oldStudent);
					studentAtt.setStudent(oldStudent);
					students.set(oldIndex, studentAtt);
				} else {
					numOfNewStudents++;
				}
			}

//			System.out.println("students: " + students);

			setChanged();
			notifyObservers();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	private int findStudentAttendanceIndexByAsurite(List<StudentAttendance> listOfAttendance, String asurite) {
		int index = -1;

		for (int i = 0; i < listOfAttendance.size(); i++) {
			StudentAttendance sta = listOfAttendance.get(i);
			if (sta.asurite.equals(asurite)) {
				index = i;
				break;
			}
		}

		return index;
	}

	private int findStudentIndexById(String id) {
		int index = -1;
		for (int i = 0; i < students.size(); i++) {
			Student student = students.get(i);
			if (student.id.equals(id)) {
				index = i;
				break;
			}
		}
		return index;
	}

	private int findStudentIndexByAsurite(String asurite) {
		int index = -1;
//		System.out.println("\nfindStudentIndexByAsurite: " + asurite);
//		System.out.println("students: "+students);

		for (int i = 0; i < students.size(); i++) {
			Student student = students.get(i);
//			System.out.println("student: " + student);
//			System.out.println("student.asurite.equals(asurite): " + student.asurite.equals(asurite));
			if (student.asurite.equals(asurite)) {
				index = i;
				break;
			}
		}

		return index;
	}

	public List<String> getListOfStringsForTable() {
		List<String> retVal = new ArrayList<>();
		for (Student student : students) {
			retVal.add(student.toString());
		}
		return retVal;
	}

	public String[][] getTableData() {
//		System.out.println("getTableData start");
		String[][] retVal = new String[students.size()][csvHeaders.size()];
		for (int i = 0; i < students.size(); i++) {
//			System.out.println("students.get(i).toString(): " + students.get(i).toString());
			String[] arr = students.get(i).toString().split(",");
			String[] row = new String[csvHeaders.size()];

			for (int index = 0; index < arr.length; index++)
				row[index] = arr[index];

//			System.out.println("row: " + row);
			for (int j = 0; j < retVal[i].length; j++) {
				retVal[i][j] = row[j];
			}
		}
//		System.out.println("getTableData end");
		return retVal;
	}

	public void saveDataToFile() {
		try {
			Date d = new Date();
			SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
			File csvFile = new File("./roster" + dt.format(d));

			if (csvFile.createNewFile()) {
				System.out.println("File created: " + csvFile.getName());
			} else {
				System.out.println("File already exists.");
			}
			FileOutputStream fos = new FileOutputStream(csvFile);
			Writer fw = new OutputStreamWriter(fos, "UTF-8");

			String headers = String.join(",", csvHeaders);
			fw.write(headers);
			fw.write("\n");

			for (Student student : students) {
				System.out.println("writing line: " + student.toString());
				fw.write(student.toString());
				fw.write("\n");
			}
			fw.flush();
			fw.close();
			System.out.println("here");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
