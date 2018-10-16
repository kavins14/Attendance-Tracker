import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Course{

	private String name;
	private String description;
	private final int id;
	private String[][] students;
	private String[][] sessions;
	private DBConnect conn = new DBConnect();
	
	public Course(int cID){

		//getting data from database
		name = conn.CourseName(cID)[0]; 
		id = cID;
		description = conn.CourseName(cID)[1];
	}
	
	public int getID(){
		return id;
	}
	
	public String toString(){
		return name;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDesc(){
		return description;
	}
	
	public String[][] getSessions(){//[id,date]
		sessions = conn.CourseSesh(id);
		return sessions;
	}
	
	public String[][] getStudents(){ //[name,stuID]
		students = conn.CourseStu(id);
		return students;
	}
	
	public static void main (String[] args){
		Course c1 = new Course(1);
		System.out.println(Arrays.deepToString(c1.getStudents()));
		System.out.println(Arrays.deepToString(c1.getSessions()));
	}
	
}
