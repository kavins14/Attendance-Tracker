import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import java.awt.Color;
import javax.swing.JSplitPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

import javax.swing.JPopupMenu;
import javax.swing.JPanel;
import java.awt.CardLayout;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.Icon;

import java.util.Arrays;
import java.util.Date;
import javax.swing.JTextPane;

public class Main1 {

	private static JFrame frmAttendanceTracker;
	private JPanel panelMainMenu;
	private static Main main = new Main();
	private int selectedRow;
	private static JList<String>  MPcourses_list;
	static DefaultListModel<String> CourseList;
	private DBConnect con = new DBConnect();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/*try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}*/
		EventQueue.invokeLater(new Runnable() {		
			public void run() {
				try {
					Main1 window = new Main1();
					window.frmAttendanceTracker.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main1() {
		initialize();
	}
	
	public static void setFrameVisibility(boolean b){
		frmAttendanceTracker.setVisible(b);
	}
	
	public static void updateCourse(){
    	CourseList.removeAllElements();
		main.populateCourses();
		System.out.println(Arrays.deepToString(main.getCourses()));
		  for(int i=0;i<main.getCourses().length;i++){
			  CourseList.addElement(main.getCourses()[i].getName());
	        }
		MPcourses_list.setModel(CourseList);
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {		
		frmAttendanceTracker = new JFrame();
		frmAttendanceTracker.setResizable(false);
		frmAttendanceTracker.setTitle("Attendance Tracker");
		frmAttendanceTracker.setBounds(100, 100, 265, 338); //frmAttendanceTracker.setBounds(100, 100, 265, 336); <initial
		frmAttendanceTracker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAttendanceTracker.getContentPane().setLayout(new CardLayout(0, 0));
		
		//icon initialization
		ImageIcon icon = new ImageIcon("icon/backarrow.png", "back arrow");
		
		JPanel panelMainMenu = new JPanel();
		frmAttendanceTracker.getContentPane().add(panelMainMenu, "name_2216875202772");
		panelMainMenu.setLayout(null);
		
				JLabel MPlblCourses = new JLabel("Courses");
				MPlblCourses.setBounds(19, 112, 120, 14);
				panelMainMenu.add(MPlblCourses);
				MPlblCourses.setFont(new Font("Tahoma", Font.BOLD, 14));
				
		        CourseList = new DefaultListModel<>();
		        Course[] courses = main.getCourses();
		        for(int i=0;i<courses.length;i++){
		        	CourseList.addElement(courses[i].getName());
		        }
		 
				MPcourses_list = new JList<>(CourseList);
				MPcourses_list.addMouseListener(new MouseAdapter() {
				    public void mouseClicked(MouseEvent evt) {
				    	selectedRow = MPcourses_list.getSelectedIndex();
				        JList list = (JList)evt.getSource();
				        if (evt.getClickCount() == 2) {
				            // Double-click detected
				            int index = list.locationToIndex(evt.getPoint()); //index is in line with coursesDisplayed
				            System.out.println(index);
				            System.out.println(Arrays.toString(main.getCourses()));
				            System.out.println(main.getCourses()[index]);
							Course selectedCourse = main.getCourses()[index];
							coursePage.setCourse(selectedCourse);
				            coursePage pg = new coursePage();
				            pg.initialize();
				            frmAttendanceTracker.setVisible(false);

				        }
				    }
				});
				MPcourses_list.setBounds(19, 135, 217, 133);
				panelMainMenu.add(MPcourses_list);
				
				JLabel MPlblWelcome = new JLabel("Welcome to the Attendance Tracker!");
				MPlblWelcome.setBounds(15, 11, 256, 14);
				panelMainMenu.add(MPlblWelcome);
				MPlblWelcome.setForeground(Color.BLUE);
				MPlblWelcome.setFont(new Font("Tahoma", Font.PLAIN, 14));
				
				JButton MPbtnAddCourse = new JButton("Add");
				MPbtnAddCourse.setBounds(153, 280, 92, 23);
				panelMainMenu.add(MPbtnAddCourse);
				MPbtnAddCourse.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						addCourse_popup popup = new addCourse_popup();
						popup.c_popup();
					}
				});
				
				JLabel MPlblStudents = new JLabel("Students");
				MPlblStudents.setBounds(20, 51, 120, 14);
				panelMainMenu.add(MPlblStudents);
				MPlblStudents.setFont(new Font("Tahoma", Font.BOLD, 14));
				
				JButton MPbtnStudentDB = new JButton("Go To Student Database");
				MPbtnStudentDB.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						frmAttendanceTracker.setVisible(false);
						StudentDBPage.initialize();
					}
				});
				MPbtnStudentDB.setBounds(20, 75, 217, 23);
				panelMainMenu.add(MPbtnStudentDB);
				
				JButton btnRemove = new JButton("Remove");
				btnRemove.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.out.println(selectedRow);
						main.populateCourses();
						if(Helper.showConfirmDialog()){
							con.removeCourse(main.getCourses()[selectedRow].getID(),main.getCourses()[selectedRow].getSessions());
							CourseList.remove(selectedRow);
						}	
					//	MPcourses_list.setModel(CourseList);
					}
				});
				btnRemove.setBounds(66, 280, 87, 23);
				panelMainMenu.add(btnRemove);

		
	}
}
