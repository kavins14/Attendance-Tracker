import java.util.ArrayList;
import java.util.Arrays;
	
public class Student{

	private String[] name;
	private final int id; //used to writing data in db
	private int StuID; //used when getting data
	private String[][] sessions;
	private String[] CoursesEnrolled;
	private DBConnect conn = new DBConnect();

		
	public Student(int id){
		this.id = id;
		this.name = new String[]{conn.stuName(id)[0],conn.stuName(id)[1]};
		this.StuID = Integer.parseInt(conn.stuName(id)[2]);
		CoursesEnrolled = conn.stuCourses(StuID);
	} 
	
	public String[] getFLname(){
		return name;
		
	}
	
	public String getName(){
		return name[0]+" "+name[1];
	}
	
	public int getStuID(){
		return StuID;
	}
	
	public int getID(){
		return id;
	}
	
	public String[][] getSessions(){
		sessions = conn.stuSession(StuID);
		return sessions;
	}
	
	public String[] getCoursesEnrolled(){
		return CoursesEnrolled;
	}
	
	public static void main(String[] args){
		Student st = new Student(4);
		System.out.println(st.getName());
		System.out.println(Arrays.deepToString(st.getCoursesEnrolled()));
	}
	
}
