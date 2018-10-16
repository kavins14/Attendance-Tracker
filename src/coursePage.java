import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JButton;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JList;
import javax.swing.ListModel;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JScrollBar;

public class coursePage extends JFrame {

	private JPanel panel;
	private Helper modal = new Helper();
	private static Course selectedCourse;
	private Main main = new Main();
	private String[] columnNames = {"Name", "ID"};
	private String[][] data = selectedCourse.getStudents();
	private static JTable tblStudent;
	private static TableModel tableModel;
	private static String createSeshResponse;
	private static JLabel lblResponse;
	private DBConnect con = new DBConnect();
	private int selectedRow;
	ImageIcon icon = new ImageIcon("icon/backarrow.png", "back arrow");

	/**
	 * Launch the application.
	 * 
	 */
	public void initialize() {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					coursePage frame = new coursePage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	public static void setCourse(Course c){
		selectedCourse = c;
	}
	
	public static void createSessionResponse(String s, Color c){
		createSeshResponse = s;
		lblResponse.setText(createSeshResponse);
		lblResponse.setForeground(c);
		lblResponse.setVisible(true);
	}
	
	public static void updateStudents(){
		tableModel.refresh(selectedCourse.getStudents());
		System.out.println(Arrays.deepToString(selectedCourse.getStudents()));
		tblStudent.updateUI();
	}
	
	public coursePage() {
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 535, 392);
		getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 535, 385);
		setResizable(false);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		tblStudent = new JTable();
		tblStudent.setBounds(18, 97, 288, 224);
		tableModel = new TableModel(columnNames,data,new int[]{-1});
		tblStudent.setModel(tableModel);
		tblStudent.getColumnModel().getColumn(0).setPreferredWidth(170);
		tblStudent.getColumnModel().getColumn(1).setPreferredWidth(50);
		tblStudent.getTableHeader().setReorderingAllowed(false);
		tblStudent.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
					selectedRow = tblStudent.getSelectedRow();
					System.out.println(tblStudent.getSelectedRow());
				}
	    });
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(18, 97, 288, 224);
		scrollPane.setViewportView(tblStudent);
		panel.add(scrollPane);
		
		JLabel lbltitle = new JLabel(selectedCourse.getName());
		lbltitle.setBounds(70, 19, 404, 31);
		lbltitle.setToolTipText(selectedCourse.getDesc());
		lbltitle.setHorizontalAlignment(SwingConstants.CENTER);
		lbltitle.setFont(new Font("Keep Calm", Font.PLAIN, 24));
		panel.add(lbltitle);
		
		 
		JLabel lblStudentList = new JLabel("Student List");
		lblStudentList.setBounds(75, 71, 133, 14);
		lblStudentList.setHorizontalAlignment(SwingConstants.CENTER);
		lblStudentList.setFont(new Font("Tahoma", Font.ITALIC, 13));
		panel.add(lblStudentList);
        
		
		JButton btnSessionInfo = new JButton("Sessions Information");
		btnSessionInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sessionInfo_popup.setCourse(selectedCourse);
				sessionInfo_popup.initialize();
			}
		});
		btnSessionInfo.setBounds(325, 223, 183, 98);
		btnSessionInfo.setForeground(Color.BLACK);
		btnSessionInfo.setBackground(Color.LIGHT_GRAY);
		panel.add(btnSessionInfo);
		
		JButton btnCreateSession = new JButton("Create Session");
		btnCreateSession.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tableModel.getData().length>0){
					createSession_popup.setData(selectedCourse);
					createSession_popup.popup_sesh();
				}else{
					System.out.println("No students");
					createSessionResponse("No students", new Color(255,0,0));
				}	
			}
		});
		btnCreateSession.setBounds(326, 97, 182, 103);
		btnCreateSession.setBackground(Color.GREEN);
		panel.add(btnCreateSession);
		
		JLabel lblDate = new JLabel("05-03-2018  07:22:25");
		lblDate.setBounds(362, 333, 159, 14);
		lblDate.setAlignmentX(0.5f);
		ActionListener updateClockAction = new ActionListener() {
			  public void actionPerformed(ActionEvent e) {
			     lblDate.setText(Helper.printDate()); 
			    }
		};
		/* Makes sure date is updated */
		Timer t = new Timer(1000, updateClockAction);
		t.start();
		
		panel.add(lblDate);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(main.getStu(Integer.parseInt((String)tableModel.getData()[selectedRow][1])).getID());
				if(Helper.showConfirmDialog() && con.courseRemoveStu(selectedCourse.getID(), main.getStu(Integer.parseInt((String)tableModel.getData()[selectedRow][1])).getID())){
					((TableModel)tblStudent.getModel()).removeRow(selectedRow);
					tblStudent.updateUI();
				}
						
				
			}
		});
		btnRemove.setBounds(18, 329, 89, 31);
		panel.add(btnRemove);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				courseAddStu_popup.initialize(selectedCourse);
			}
		});
		btnAdd.setBounds(111, 329, 89, 31);
		panel.add(btnAdd);
		
		JButton btnBack = new JButton(icon);
		btnBack.setBounds(8, 8, 20, 20);
		panel.add(btnBack);
		btnBack.setSelectedIcon(new ImageIcon("C:\\Users\\Administrator\\workspace\\Attendance Tracker\\icon\\backarrow.png"));
		
		lblResponse = new JLabel("New label");
		lblResponse.setForeground(new Color(0, 100, 0));
		lblResponse.setFont(new Font("Lucida Grande", Font.PLAIN, 9));
		lblResponse.setBounds(336, 197, 162, 16);
		lblResponse.setVisible(false);
		panel.add(lblResponse);
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Main1.setFrameVisibility(true);
			}
		});
		

	}
}
