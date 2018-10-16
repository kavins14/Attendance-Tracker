import java.util.concurrent.atomic.AtomicInteger;



public class Coordinates {
    //static id generator shared among all instances of Coordinates 
    private static final AtomicInteger studentID = new AtomicInteger(1);
    private static final AtomicInteger sessionID = new AtomicInteger(1);
    private static final AtomicInteger courseID = new AtomicInteger(10);

    private final Integer id;

    
    public Coordinates(int type) { //1-student 2-session 3-course
        //assign unique id to an instance variable
    	switch(type){
    		case(1):
        		id = studentID.getAndIncrement();
    			break;
    		case(2):
        		id = sessionID.getAndIncrement();
    			break;
    		case(3):
        		id = courseID.getAndIncrement();
    			break;
    		default:
    			id = null; //to satisfy the requirements
    			
    	}	
    }

    public int getId() {
        //return instance variable
        return id;
    }
    
    
}
