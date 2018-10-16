import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import dnl.utils.text.table.TextTable;


public class Session{
	
	private final int id;
	private final int courseID;
	private final String date;
	private int[][] data;
	DBConnect con = new DBConnect();
	

	public Session(int seshID, String date, int cID){
		courseID = cID;
		id = seshID;
		this.date = date;
	}
	
	public int getID(){
		return id; 
	};
	
	public String getDate(){
		return date;
	}
	
	public int getCourseID(){
		return courseID;
	}
	
	public int[][] getData(){ //[stu_id, status]
		data = con.seshData(id);
		return data;
	}
	
	public static void main(String[] args){
		DBConnect con = new DBConnect();
		Session s1 = new Session(3, null, 1);
		System.out.println(Arrays.deepToString(s1.getData()));
	}
	
	
}
