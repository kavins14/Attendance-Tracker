import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JEditorPane;
import javax.swing.JTextPane;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JTextField;
import javax.swing.JTextArea;

public class sessionInfo_popup extends JFrame {
	
	private static JPanel panel;
	private JTextField txtSearch;
	private static Helper h = new Helper();
	private JTable table;
	private static Course course; //placeholder
	private static String[][] data = {{"d"},{"dd"}}; 
	private static int[][] editSessionData;
	private String[] columnNames = {"Date"};
	private int[] seshInfo = {0,0,0,0}; //[no. students, absent, tardy, present]
	private static Session sesh;
	private DBConnect con = new DBConnect();
	private int selectedRow = -1;
	JTextPane txtSeshInfo;
	JLabel lblNumberOfStudents;

	/**
	 * Launch the application.
	 */
	public static void initialize() {
	//public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					sessionInfo_popup frame = new sessionInfo_popup();
					h.setJDialog(frame, panel, "Session Information");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void setCourse(Course c){
		course = c;
		String[][] editedData = new String[course.getSessions().length][2];
		for(int i=0; i<course.getSessions().length;i++){
			String temp = c.getSessions()[i][0];
			editedData[i][0] = c.getSessions()[i][1];
			editedData[i][1] = temp;
		}
		data = editedData;
	}
	
	public void updateSeshInfo(){
		for(int x=1;x<4;x++){
			seshInfo[x] = 0;
		}
		int seshID = Integer.parseInt(course.getSessions()[selectedRow][0]);
		String date = course.getSessions()[selectedRow][1];
		sesh = new Session(seshID, date, course.getID());
		int[][] data = sesh.getData(); 
		seshInfo[0] = data.length;
		for(int i=0;i<data.length;i++){
			switch(data[i][1]){
				case 0: 
					seshInfo[1]++;
					break;
				case 1: 
					seshInfo[2]++;
					break;
				case 2: 
					seshInfo[3]++;
					break;
				default: 
						break;
			}
		}
		editSessionData = sesh.getData();	
		
	}
	
	
	public void updateUISeshInfo(){
		updateSeshInfo();
		txtSeshInfo.setText(String.format(" Present: %s \n Tardy: %s \n Absent: %s", seshInfo[3],seshInfo[2],seshInfo[1]));
		txtSeshInfo.repaint();
		lblNumberOfStudents.setText("Number of Students: " + seshInfo[0]);
		lblNumberOfStudents.repaint();
	}

	/**
	 * Create the frame.
	 */
	public sessionInfo_popup() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 415, 372);
		getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 537, 350);
		panel.setLayout(null);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(panel);
		
		TableModel tModel = new TableModel(columnNames,data,new int[]{1});
		table = new JTable(tModel);
		table.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
					selectedRow = table.getSelectedRow();
					System.out.println(table.getSelectedRow());
					updateUISeshInfo();
				}
	    });

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(21, 79, 204, 213);
		scrollPane.setViewportView(table);
		panel.add(scrollPane);
		
		
		JLabel lblSessionInformation = new JLabel("Session Information");
		lblSessionInformation.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblSessionInformation.setBounds(20, 47, 167, 14);
		panel.add(lblSessionInformation);
		
		JLabel lblDate = new JLabel(Helper.printDate());
		lblDate.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblDate.setBounds(197, 10, 165, 14);
		panel.add(lblDate);
		ActionListener updateClockAction = new ActionListener() {
			  public void actionPerformed(ActionEvent e) {
			     lblDate.setText(Helper.printDate()); 
			    }
		};
		/* Makes sure date is updated */
		Timer t = new Timer(1000, updateClockAction);
		t.start();
		/* 							  */
		
		JLabel lblCourse = new JLabel(course.getName());
		lblCourse.setForeground(new Color(105, 105, 105));
		lblCourse.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		lblCourse.setBounds(10, 11, 82, 14);
		panel.add(lblCourse);
		
		JButton btnEdit = new JButton("Edit Session");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editSession_popup.setData(sesh, course);
				editSession_popup.initialize();
			}
		});
		btnEdit.setForeground(Color.BLACK);
		btnEdit.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnEdit.setBackground(new Color(0, 128, 0));
		btnEdit.setBounds(122, 301, 101, 40);
		panel.add(btnEdit);
		
		JButton btnDelete = new JButton("Delete Session");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedRow > -1 && Helper.showConfirmDialog()){
					/*con.deleteSession(Integer.parseInt(course.getSessions()[selectedRow][0]));*/
					Object[][] tableModelData = tModel.getData();
					Integer row = Integer.parseInt((String) tableModelData[selectedRow][1]);
					con.deleteSession(row);
					tModel.removeRow(selectedRow);
					table.updateUI();
					System.out.println("Process Complete");
				}	
					//add code to update the table
					
			}
		});
		btnDelete.setForeground(Color.BLACK);
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnDelete.setBackground(new Color(0, 128, 0));
		btnDelete.setBounds(19, 302, 100, 40);
		panel.add(btnDelete);
		
		JLabel lblSearchDate = new JLabel("Search Date");
		lblSearchDate.setBounds(241, 77, 82, 16);
		panel.add(lblSearchDate);
		
		txtSearch = new JTextField();
		txtSearch.setBounds(238, 100, 152, 56);
		panel.add(txtSearch);
		txtSearch.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(260, 160, 117, 29);
		panel.add(btnSearch);
		
		txtSeshInfo = new JTextPane();	 
		txtSeshInfo.setEditable(false);
		//txtSeshInfo.setBackground(Color.LIGHT_GRAY);
		txtSeshInfo.setBounds(240, 240, 150, 51);
		txtSeshInfo.setText(String.format(" Present: %s \n Tardy: %s \n Absent: %s", seshInfo[3],seshInfo[2],seshInfo[1]));	
		panel.add(txtSeshInfo);
		
		lblNumberOfStudents = new JLabel("Number of Students: " + seshInfo[0]);
		lblNumberOfStudents.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		lblNumberOfStudents.setBounds(240, 220, 137, 16);
		panel.add(lblNumberOfStudents);

	}
}
