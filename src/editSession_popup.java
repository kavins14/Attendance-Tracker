import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JEditorPane;
import javax.swing.JTextPane;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class editSession_popup extends JFrame {
	
	private static Helper h = new Helper();
	private static JPanel panel;
	private JTable table;
	private String[] columnNames =  {"Student Name", "Status"};
	private static String[][] data; //[stuName, status]
	private static int[][] initData;
	private static Course course;
	private static Session sesh;
	private static Main main = new Main();
	private DBConnect con = new DBConnect();
	private String response;
	private JLabel lblResponse;
 

	/**
	 * Launch the application.
	 */
	//public static void main(String[] args) {
	public static void initialize(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					editSession_popup frame = new editSession_popup();
					h.setJDialog(frame, panel, "Edit Session");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void setData(Session s, Course c){
		sesh = s;
		int[][] data_ = sesh.getData();
		course = c;
		int rowCount = data_.length;
		String[][] info = new String[rowCount][2];
		for(int i=0;i<rowCount;i++){
			info[i][0] = main.getStu(data_[i][0]).getName();
			info[i][1] = ""+data_[i][1]; //""converts it to string
		}
		data = info;
		initData = data_;
	}
	
	public int[][] formattedData(){
		int[][] data_ = sesh.getData();
		for(int i=0;i<data_.length;i++){
			data_[i][0] = main.getStu(data_[i][0]).getID(); 
			data_[i][1] = Integer.parseInt(data[i][1]); //this holds the updated statuses
		}
		return data_;
	}

	/**
	 * Create the frame.
	 */
	public editSession_popup() {
		//setData(new Session(31, "",1), new Course(1));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 362);
		getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setBounds(0, 0, 450, 344);
		getContentPane().add(panel);
		
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
		panel.add(scrollPane);
		
		JLabel lblTitle = new JLabel("Edit Session");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTitle.setBounds(181, 44, 117, 14);
		panel.add(lblTitle);
		
		JLabel lblDate = new JLabel(sesh.getDate());
		lblDate.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblDate.setBounds(272, 10, 165, 14);
		panel.add(lblDate);

		
		JLabel lblCourseName = new JLabel(course.getName());
		lblCourseName.setForeground(new Color(105, 105, 105));
		lblCourseName.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		lblCourseName.setBounds(10, 11, 82, 14);
		panel.add(lblCourseName);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			 if(Helper.showConfirmDialog()){	
				if(con.updateSession(formattedData(), sesh.getID())){
					response = "Update Successful";
					lblResponse.setForeground(new Color(0, 128, 0));
				}else{
					response = "Update Unsuccessful";
					lblResponse.setForeground(new Color(220, 20, 60));
				}
				lblResponse.setText(response);
				lblResponse.repaint();
			 }	
			}
		});
		btnUpdate.setForeground(Color.BLACK);
		btnUpdate.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnUpdate.setBackground(new Color(0, 128, 0));
		btnUpdate.setBounds(116, 303, 121, 23);
		panel.add(btnUpdate);
		
		
		JButton btnRevert = new JButton("Revert");
		btnRevert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<data.length;i++){
					data[i][1] = ""+initData[i][1];
				}
				System.out.println(Arrays.deepToString(data));
				table.updateUI();
				response = "Revert Successful";
				lblResponse.setForeground(new Color(0, 128, 0));
				lblResponse.setText(response);
				lblResponse.repaint();
			}
		});
		btnRevert.setBackground(Color.WHITE);
		btnRevert.setBounds(5, 303, 84, 23);
		panel.add(btnRevert);
		
		JLabel label_3 = new JLabel("Legend");
		label_3.setVerticalAlignment(SwingConstants.TOP);
		label_3.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		label_3.setBounds(323, 205, 51, 20);
		panel.add(label_3);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setForeground(Color.GRAY);
		separator.setBounds(252, 77, 17, 248);
		panel.add(separator);
		
		JLabel label_4 = new JLabel("0 - ABSENT");
		label_4.setHorizontalAlignment(SwingConstants.LEFT);
		label_4.setForeground(new Color(128, 0, 0));
		label_4.setFont(new Font("Tahoma", Font.BOLD, 14));
		label_4.setBounds(302, 244, 100, 14);
		panel.add(label_4);
		
		JLabel label_5 = new JLabel("1 - TARDY");
		label_5.setHorizontalAlignment(SwingConstants.LEFT);
		label_5.setForeground(new Color(218, 165, 32));
		label_5.setFont(new Font("Tahoma", Font.BOLD, 14));
		label_5.setBounds(301, 269, 100, 14);
		panel.add(label_5);
		
		JLabel label_6 = new JLabel("2 - PRESENT");
		label_6.setHorizontalAlignment(SwingConstants.LEFT);
		label_6.setForeground(new Color(0, 100, 0));
		label_6.setFont(new Font("Tahoma", Font.BOLD, 14));
		label_6.setBounds(302, 294, 100, 14);
		panel.add(label_6);
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setFont(new Font("Tahoma", Font.BOLD, 12));
		editorPane.setBounds(277, 231, 140, 93);
		panel.add(editorPane);
		
		JTextPane txtStats = new JTextPane();
		txtStats.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		txtStats.setBackground(SystemColor.window);
		txtStats.setAlignmentX(0.5f);
		txtStats.setBounds(272, 79, 140, 114);
		txtStats.setText("To edit the selected session, changee the status and confirm the change. If not yet confirmed, to revert back to initial data, click the 'revert' button");
		panel.add(txtStats);
		
		lblResponse = new JLabel();
		lblResponse.setForeground(new Color(220, 20, 60));
		lblResponse.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		lblResponse.setBounds(13, 59, 121, 16);
		panel.add(lblResponse);
	}
}
