import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import com.mysql.jdbc.PreparedStatement;

public class DBConnect {

	private static Connection con;
	private static Statement st;
	private static ResultSet rs;
	private static ResultSet rs2;
	private static PreparedStatement ps;
	private static int rowCount;
	
	public DBConnect(){
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_tracker", "<MYSQL USERNAME>", "<MYSQL PASSWORD>");
			st = con.createStatement();
			
		}catch(Exception ex){
			System.out.println("Error: "+ ex);
		}
		
	}

	/*public void getData(){
		try{
			
			String query = "select * from student_info";
			rs = st.executeQuery(query);
			while(rs.next()){
				String name = rs.getString("name");
				int id = rs.getInt("student_id");
				System.out.println(name +"  " + "ID: " + id);
			}
			
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
	}*/
	
/*-----------------------MISC Methods----------------------------*/
	public static int getRowCount(String table){
		try{
			String queryCount = String.format("SELECT COUNT(*) FROM  " + table);
			rs = st.executeQuery(queryCount);
			rs.next();
			int rowCount = rs.getInt(1);
			return rowCount;

		}catch(Exception ex){
			System.out.println(ex.toString());
		}
		
		return 0;
	}
	
	public static int getLastestSessionID(int cID){
		try{
			String queryCount = String.format("SELECT MAX(id) FROM `sesssion_info` WHERE course_id = %s", cID);
			rs = st.executeQuery(queryCount);
			rs.next();
			int rowCount = rs.getInt(1);
			
			return rowCount;
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
		
		return 0;
	}
	
/*-------------------------COURSE INFO--------------------------*/
	public static Course[] allCourses(){
		
		try{
			rowCount = getRowCount("course_info");
			String query = String.format("SELECT course_id FROM course_info");
			rs2 = st.executeQuery(query);
			Course[] data = new Course[rowCount];
			int i = 0;
			while(rs2.next()){
					int id = rs2.getInt("course_id");
					data[i] = new Course(id);
					i++;
				}
			
			return data;
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
		return null;
	}
	
	public String[][] CourseStu(int cID){ //[name, stu_id]
	
		try{
			rowCount = getRowCount(String.format("student_info as s JOIN student_course_data as scd ON s.id = scd.student_id JOIN course_info as c ON c.course_id =scd.course_id WHERE c.course_id = %s", cID));
			String[][] data = new String[rowCount][2];
			int i = 0;
			String query = String.format("SELECT s.student_id, s.first_name, s.last_name FROM student_info as s JOIN student_course_data as scd ON s.id = scd.student_id JOIN course_info as c ON c.course_id =scd.course_id WHERE c.course_id = %s ORDER BY s.first_name ASC", cID);
			rs = st.executeQuery(query);
			while(rs.next()){
					String name = rs.getString("first_name") + " " + rs.getString("last_name") ;
					String id = rs.getString("student_id");
					data[i][0] = name;
					data[i][1] = id;
					i++;
				}
			
			return data;
			}catch(Exception ex){
				System.out.println(ex.toString());
			}
		return null;

	} 
	
	public String[][] CourseSesh(int cID){ //[id, date]
		
		try{
			rowCount = getRowCount(String.format("sesssion_info as s JOIN course_info ON course_info.course_id = s.course_id WHERE course_info.course_id = %s", cID));
			String[][] data = new String[rowCount][2];
			int i = 0;
			String query = String.format("SELECT s.id, s.date FROM sesssion_info as s JOIN course_info ON course_info.course_id = s.course_id WHERE course_info.course_id = %s", cID);
			rs = st.executeQuery(query);
				while(rs.next()){
					String date = rs.getString("date");
					String id = rs.getString("id");
					data[i][0] = id;
					data[i][1] = date;
					i++;
					//System.out.println(date +"  " + "ID: " + id);
				}
				
			return data;	
			}catch(Exception ex){
				System.out.println(ex.toString());
			}
		return null;

	} 

	public String[] CourseName(int cID){ //[name, desc]
		
		try{
			String query = String.format("SELECT name, description FROM course_info WHERE course_info.course_id = %s", cID);
			rs = st.executeQuery(query);
				while(rs.next()){
					String name = rs.getString("name");
					String desc = rs.getString("description");
					//System.out.println(name +"  " + "Description: " + desc);
					return new String[]{name,desc};
				}
				
			}catch(Exception ex){
				System.out.println(ex.toString());
			}
		return null;

		} 
	
	public static boolean addCourse(String name, String description){
		try{
			String query = String.format("INSERT INTO course_info (name,description) VALUES (?,?)");
			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, name);
			ps.setString(2, description);
			ps.executeUpdate(); //throws sql exception
			System.out.println("Course Creation: SUCCESS");
			
			return true;
		}catch(Exception ex){
			System.out.println(ex.toString());
			return false;
		}
	}
	
	public boolean removeCourse(int cID, String[][] seshID){
		try{
			String query = String.format("DELETE FROM course_info WHERE course_id=?");
			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setInt(1, cID);
			ps.executeUpdate(); //throws sql exception
			System.out.println("Course Deletion: SUCCESS");
			/////////////
			
			query = String.format("DELETE FROM sesssion_info WHERE course_id=?");
			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setInt(1, cID);
			ps.executeUpdate(); //throws sql exception
			System.out.println("Session Deletion: SUCCESS");
			/////////////
			query = String.format("DELETE FROM student_course_data WHERE course_id=?");
			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setInt(1, cID);
			ps.executeUpdate(); //throws sql exception
			System.out.println("scd Deletion: SUCCESS");
			/////////////
			for(int i=0; i<seshID.length;i++){
				System.out.println(Arrays.toString(seshID));
				query = String.format("DELETE FROM session_data WHERE session_id = ?");
				ps = (PreparedStatement) con.prepareStatement(query);
				ps.setInt(1, Integer.parseInt(seshID[i][0]));
				ps.executeUpdate();
				
			}	
			/////////////
			return true;
		
		}catch(Exception ex){
			System.out.println(ex.toString());
			return false;
		}
	}
	
	public boolean courseAddStu(int cID, int stuID){
		try{
			String query = String.format("INSERT INTO student_course_data (course_id,student_id) VALUES (?,?)");
			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setInt(1, cID);
			ps.setInt(2, stuID);
			ps.executeUpdate(); //throws sql exception
			System.out.println("CourseAddStu: SUCCESS");
			
			return true;
		
		}catch(Exception ex){
			System.out.println(ex.toString());
			return false;
		}
	}
	
	public boolean courseRemoveStu(int cID, int stuID){
		try{
			String query = String.format("DELETE FROM student_course_data WHERE course_id = ? AND student_id = ?");
			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setInt(1, cID);
			ps.setInt(2, stuID);
			ps.executeUpdate(); //throws sql exception
			System.out.println("courseRemoveStu: SUCCESS");
			
			return true;
		
		}catch(Exception ex){
			System.out.println(ex.toString());
			return false;
		}
	}
	
/*----------------------------------------------------------------------*/
	
/*--------------------------STUDENT INFO--------------------------------*/
	public static Student[] allStu(){ //[id]
		
		try{
			rowCount = getRowCount("student_info");
			//System.out.println(rowCount);
			Student[] data = new Student[rowCount];
			String query = String.format("SELECT id FROM `student_info` WHERE 1 ORDER BY first_name asc");
			int i = 0;
			rs2 = st.executeQuery(query);
				while(rs2.next()){
					int id = rs2.getInt("id");
					Student stu = new Student(id);
					data[i] = stu;
					i++;
				}
			//System.out.println(Arrays.toString(data));	
			rs2.close();	
			return data;	
			}catch(Exception ex){
				System.out.println(ex.toString());
			}
		return null;

	}
	
	public String[] stuName(int id){ //[name, id]
		try{
			String query = String.format("SELECT student_id, first_name, last_name FROM `student_info` WHERE id=%s", id);
			//System.out.println(rowCount);
			rs = st.executeQuery(query);
				while(rs.next()){
					String fname = rs.getString("first_name");
					String lname = rs.getString("last_name");
					String StuID = rs.getString("student_id");					
					//System.out.println(name +"  " + "Description: " + desc);
					return new String[]{fname,lname, StuID};	
				}
				
			}catch(Exception ex){
				System.out.println(ex.toString());
			}
		return null;
	}
	
	public String[] stuCourses(int sID){ //[CourseName]
		
		try{
			int rowCount = getRowCount(String.format("student_info as s JOIN student_course_data as scd ON s.id = scd.student_id JOIN course_info as c ON c.course_id = scd.course_id  WHERE s.student_id = %s", sID));
			String query = String.format("SELECT c.name as 'Course' FROM student_info as s JOIN student_course_data as scd ON s.id = scd.student_id JOIN course_info as c ON c.course_id = scd.course_id  WHERE s.student_id = %s", sID);
			String[] data = new String [rowCount];
			int i = 0;
			rs = st.executeQuery(query);
				while(rs.next()){
					String name = rs.getString("Course");
					data[i] = name;
					i++;
				}
				
			return data;	
			}catch(Exception ex){
				System.out.println(ex.toString());
			}
		return null;
	}
	
	public String[][] stuSession(int sID){ //[courseName, date, status]
		try{
			int rowCount = getRowCount(String.format("student_info as st JOIN session_data as sd ON st.id = sd.student_id JOIN sesssion_info as si ON si.id =sd.session_id JOIN course_info as ci ON ci.course_id = si.course_id WHERE st.student_id = %s ORDER BY ci.name", sID));
			String query = String.format("SELECT ci.name, si.date, sd.status FROM student_info as st JOIN session_data as sd ON st.id = sd.student_id JOIN sesssion_info as si ON si.id =sd.session_id JOIN course_info as ci ON ci.course_id = si.course_id WHERE st.student_id = %s ORDER BY ci.name", sID);
			String[][] data = new String[rowCount][3];
			int i = 0;
			rs = st.executeQuery(query);
				while(rs.next()){
					String courseName = rs.getString("name");
					String date = rs.getString("date");
					String status = rs.getString("status");
					data[i][0] = courseName;
					data[i][1] = date;
					data[i][2] = status;
					i++;
				}
				
			return data;	
		}catch(Exception ex){
			System.out.println(ex.toString());
		}	
		
		return null;
	}
	
	public static boolean deleteStudent(int id){
		try{
			String query = String.format("DELETE FROM student_info WHERE id=%s", id);
			ps = (PreparedStatement) con.prepareStatement(query);
			ps.executeUpdate(query);
			System.out.println("Deletion from student_info: Success!");
			//
			query = String.format("DELETE FROM student_course_data WHERE student_id=%s", id);
			ps = (PreparedStatement) con.prepareStatement(query);
			ps.executeUpdate(query);
			System.out.println("Deletion from student_course_data: Success!");
			//
			query = String.format("DELETE FROM session_data WHERE student_id=%s", id);
			ps = (PreparedStatement) con.prepareStatement(query);
			ps.executeUpdate(query);
			
			System.out.println("Deletion from sesssion_data: Success!");
			
			return true;	
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
		return false;
	}
	
	public boolean createStudent(String[] name, int sID){
		try{
			String query = String.format("INSERT INTO student_info (student_id,first_name,last_name) VALUES (?,?,?)");
			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setInt(1, sID);
			ps.setString(2, name[0]); //firstName
			ps.setString(3, name[1]); //lastname
			ps.executeUpdate(); //throws sql exception
			
			System.out.println("Student Creation: SUCCESS");
			return true;
			
		}catch(Exception ex){
			System.out.println(ex.toString());
			return false;
		}
	}
	
	public boolean editStudent(String[] name, int sID, int id){
		try{
			String query = String.format("UPDATE student_info SET first_name = ?, last_name = ?, student_id = ? WHERE id = ?");
			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, name[0]);
			ps.setString(2, name[1]);
			ps.setInt(3, sID);
			ps.setInt(4, id);
			ps.executeUpdate(); //throws sql exception.
			
			System.out.println("Student Update: SUCCESS");
			return true;
			
		}catch(Exception ex){
			System.out.println(ex.toString());
			return false;
		}
	}
	
	
	
/*-----------------------------------------------------------------------*/
	
/*---------------------------SESSION INFO--------------------------------*/
	
	public Session[] seshInfo(){ //[id, date, courseID]
	
		try{
			rowCount = getRowCount("sesssion_info");
			String query = String.format("SELECT sesssion_info.date, sesssion_info.id, sesssion_info.course_id FROM sesssion_info JOIN course_info ON course_info.course_id = sesssion_info.course_id");
			Session[] data = new Session [rowCount];
			int i = 0;
			rs = st.executeQuery(query);
				while(rs.next()){
					String date = rs.getString("date");
					int id = rs.getInt("id");
					int course_id = rs.getInt("course_id");
					data[i] = new Session(id,date,course_id);
					i++;
				}
			
			return data;	
			}catch(Exception ex){
				System.out.println(ex.toString());
			}
		return null;
	}
	
	public int[][] seshData(int seshID){ //[stu_id, status]
		try{
			rowCount = getRowCount(String.format("session_data JOIN sesssion_info ON sesssion_info.id = session_data.session_id JOIN student_info ON student_info.id = session_data.student_id WHERE sesssion_info.id = %s",  seshID));
			String query = String.format("SELECT student_info.student_id, session_data.status FROM session_data JOIN sesssion_info ON sesssion_info.id = session_data.session_id JOIN student_info ON student_info.id = session_data.student_id WHERE sesssion_info.id = %s"
						                , seshID);
			rs = st.executeQuery(query);
			int[][] data = new int [rowCount][2];
			int i = 0;
				while(rs.next()){
					int id = rs.getInt("student_id");
					int status = rs.getInt("status");
					data[i][0] = id;
					data[i][1] = status;
					i++;
				}
				
			return data;	
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
		return null;
	}
	
	public boolean createSession(int cID){
		try{
		    Calendar calendar = Calendar.getInstance();
		    java.sql.Timestamp timestamp = new java.sql.Timestamp(calendar.getTime().getTime());
			String query = String.format("INSERT INTO sesssion_info (id, course_id, date) VALUES (?,?,?)");
			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, null);
			ps.setInt(2,cID);
			ps.setTimestamp(3, timestamp);
			ps.executeUpdate(); //throws sql exception
			System.out.println("Session Created: SUCCESS");
			
			return true;
			
		}catch(Exception ex){
			System.out.println(ex.toString());
			return false;
		}
	}
	
	public boolean createSessionData(int[][] data, int cID){
		try{
			int sessionID = DBConnect.getLastestSessionID(cID);
			for(int i=0;i<data.length;i++){
				String query = String.format("INSERT INTO session_data (session_id, status, student_id) VALUES (?,?,?)");
				ps = (PreparedStatement) con.prepareStatement(query);
				ps.setInt(1, sessionID);
				ps.setInt(2,data[i][1]);
				ps.setInt(3,data[i][0] );
				ps.executeUpdate(); //throws sql exception
				
			}	
			System.out.println("Session Data Added: SUCCESS");
			return true;
			
		}catch(Exception ex){
			System.out.println(ex.toString());
			return false;
		}
	}
	
	public boolean deleteSession(int seshID){
		try{
			String query = String.format("DELETE FROM session_data WHERE session_id = %s", seshID);
			ps = (PreparedStatement) con.prepareStatement(query);
			ps.executeUpdate(query);
			System.out.println("Deletion from session_data: Success!");
			query = String.format("DELETE FROM sesssion_info WHERE id = %s", seshID);
			ps = (PreparedStatement) con.prepareStatement(query);
			ps.executeUpdate(query);
			System.out.println("Deletion from sesssion_info: Success!");
			
			return true;	
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
		return false;
	}
	
	public boolean updateSession(int[][] data, int seshID){ //data = [student_id, status]
		try{
			System.out.println(Arrays.deepToString(data));
			System.out.println(seshID);
			for(int i=0;i<data.length;i++){
				String query = String.format("UPDATE session_data SET status = ? WHERE session_id = ? AND student_id = ?");
				ps = (PreparedStatement) con.prepareStatement(query);
				ps.setInt(1, data[i][1]);
				ps.setInt(2,seshID);
				ps.setInt(3,data[i][0]);
				ps.executeUpdate(); //throws sql exception
				
			}	
			System.out.println("Session Data Updated: SUCCESS");
			return true;
			
		}catch(Exception ex){
			System.out.println(ex.toString());
			return false;
		}
	}
	
	
	
	
	public static void main(String[] args){
		DBConnect con = new DBConnect();
		//System.out.print(con.allCoursesID() + " " + con.allCoursesID());
		System.out.println(Arrays.deepToString((con.allCourses())));
		//con.createSessionData(new int[][]{{1,2},{2,2}}, 1);
	}
		

}
