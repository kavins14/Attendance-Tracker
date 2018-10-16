import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollBar;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.JSeparator;
import javax.swing.JEditorPane;
import javax.swing.UIManager;
import java.awt.SystemColor;

public class createSession_popup extends JFrame {

	private static JPanel contentPane;
	private JTable table;
	private static Helper h = new Helper();
	private static Course course;
	private static String[][] courseStudents;
	String[] columnNames = {"Student Name", "Status"};
	private static String[][] data = {{"",""}};
	private static Main main = new Main();
	private DBConnect con = new DBConnect();
	private static String response;
	
	/**
	 * Launch the application.
	 */
	public static void popup_sesh(){
	//public static void main(String[] args){
		//setData(new Course(2));
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					createSession_popup frame = new createSession_popup();
					h.setJDialog(frame, contentPane, "Create Session");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void setData(Course c){
		course = c;
		courseStudents = course.getStudents();
		int rowCount = courseStudents.length;
		String[][] info = new String[rowCount][2];
		for(int i=0;i<rowCount;i++){
			info[i][0] = courseStudents[i][0];
			info[i][1] = "";
		}
		data = info;
	}
	
	public static int[][] formatData(){
		int[][] formattedData = new int[data.length][2];
		for(int i=0;i<data.length;i++){
			formattedData[i][0] = main.getStu(Integer.parseInt(courseStudents[i][1])).getID();
			formattedData[i][1] = Integer.parseInt(data[i][1]);
		}
		return formattedData;
	}

	/**
	 * Create the frame.
	 */
	public createSession_popup() {
		setResizable(false);
		setTitle("Create Session - "+course.getName());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 439, 366);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		table = new JTable();
		table.setBounds(12, 63, 222, 214);
		table.setModel(new TableModel(columnNames,data,new int[]{1}));
		table.setBorder(new EmptyBorder(0, 10, 0, 0));
		table.getColumnModel().getColumn(1).setCellEditor(new IntegerCellEditor(new JTextField()));
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(170);
		//table.getColumnModel().getColumn(0).
		table.getColumnModel().getColumn(1).setPreferredWidth(50);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(13, 79, 220, 213);
		scrollPane.setViewportView(table);
		contentPane.add(scrollPane);
		
		JLabel lblCreateSession = new JLabel("Create Session");
		lblCreateSession.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblCreateSession.setBounds(142, 43, 117, 14);
		contentPane.add(lblCreateSession);
		
		JLabel lblDate = new JLabel(Helper.printDate());
		lblDate.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblDate.setBounds(272, 10, 165, 14);
		ActionListener updateClockAction = new ActionListener() {
			  public void actionPerformed(ActionEvent e) {
			     lblDate.setText(Helper.printDate()); 
			    }
		};
		/* Makes sure date is updated */
		Timer t = new Timer(1000, updateClockAction);
		t.start();
		/* 							  */
	
		contentPane.add(lblDate);
		
		JLabel lblNewLabel = new JLabel(course.getName());
		lblNewLabel.setForeground(new Color(105, 105, 105));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		lblNewLabel.setBounds(10, 11, 82, 14);
		contentPane.add(lblNewLabel);
		
		JButton btnCreateSession = new JButton("Create Session");
		btnCreateSession.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(int i=0;i<data.length;i++){
					if(data[i][1].equals("")){
						System.out.println("Please fill all the fields");
						return;
					}
				}
	
				con.createSession(course.getID());
				con.createSessionData(createSession_popup.formatData(), course.getID());
				coursePage.createSessionResponse("Session successfully created", new Color(0,100,0));
				dispose();
			}
		});
		btnCreateSession.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnCreateSession.setForeground(Color.BLACK);
		btnCreateSession.setBackground(new Color(0, 128, 0));
		btnCreateSession.setBounds(116, 303, 117, 23);
		contentPane.add(btnCreateSession);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(int i=0;i<data.length;i++){
					data[i][1] = null;
				}
				table.updateUI();
			}
		});
		btnReset.setBackground(Color.WHITE);
		btnReset.setBounds(13, 303, 76, 23);
		contentPane.add(btnReset);
		
		JLabel lblLegend = new JLabel("Legend");
		lblLegend.setVerticalAlignment(SwingConstants.TOP);
		lblLegend.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		lblLegend.setBounds(323, 205, 51, 20);
		contentPane.add(lblLegend);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setForeground(Color.GRAY);
		separator.setBounds(252, 77, 17, 248);
		contentPane.add(separator);
		
		JLabel lblAbsent = new JLabel("0 - ABSENT");
		lblAbsent.setForeground(new Color(128, 0, 0));
		lblAbsent.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAbsent.setBounds(302, 244, 100, 14);
		lblAbsent.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(lblAbsent);
		
		JLabel lblTardy = new JLabel("1 - TARDY");
		lblTardy.setForeground(new Color(218, 165, 32));
		lblTardy.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTardy.setBounds(301, 269, 100, 14);
		lblTardy.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(lblTardy);
		
		JLabel lblPresent = new JLabel("2 - PRESENT");
		lblPresent.setForeground(new Color(0, 100, 0));
		lblPresent.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPresent.setBounds(302, 294, 100, 14);
		lblPresent.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(lblPresent);
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setFont(new Font("Tahoma", Font.BOLD, 12));
		editorPane.setBounds(277, 231, 140, 93);
		contentPane.add(editorPane);
		
		JTextPane txtInfo = new JTextPane();
		txtInfo.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		txtInfo.setBackground(SystemColor.window);
		txtInfo.setText("To create a successful session you'll have to fill out the status for each student and click the \"Create Session\" Button. Please refer to the legend below.");
		txtInfo.setAlignmentX(CENTER_ALIGNMENT);
		txtInfo.setBounds(272, 78, 140, 116);
		contentPane.add(txtInfo);
		
		JButton btnFillPresent = new JButton("Fill Present");
		btnFillPresent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<data.length;i++){
					data[i][1] = ""+2;
				}
				table.updateUI();
			}
		});
		btnFillPresent.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		btnFillPresent.setBounds(6, 48, 88, 29);
		contentPane.add(btnFillPresent);
		
	}
}
