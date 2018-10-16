import java.util.ArrayList;
import java.util.Arrays;

public class Main {
	
	
	private static Student[] allStudents;
	private static Course[] allCourses;
	//private static Session[] allSessions;
	public static DBConnect conn = new DBConnect();

	
	public static void main(String[] args){
		Main m = new Main();
		m.populateCourses();
		m.populateStudent();
		System.out.println(Arrays.deepToString(allCourses));
		System.out.println(Arrays.deepToString(allStudents));

	}
	
	public Main(){
		populateStudent();
		populateCourses();
	}
	
	public void populateStudent(){
		allStudents = conn.allStu();
	}
	
	public void populateCourses(){
		allCourses = conn.allCourses();
	}
	
	public Course[] getCourses(){
		return allCourses;
	}
	
	public Student[] getStudents(){
		return allStudents;
	}
	
	public static Student getStu(int StuID){
		for(int i=0;i<allStudents.length;i++){
			if(allStudents[i].getStuID() == StuID){
				return allStudents[i];
			}
		}
		return null;
	}
	
}
