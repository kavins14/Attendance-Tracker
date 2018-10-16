import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

public class StudentDBPage extends JFrame {
	
	private JTextField txtFieldN;
	private JTextField txtFieldSID;
	private JTextField txtFieldLN;
	private JTextField txtFieldCE;
	private JLabel lblCE;
	private static JTable table;
	private static JScrollPane scrollPane;
	private static Object[][] tableModelData;
	private int selectedRow = -1;
	private static String[] columnNames = {"First Name", "Last Name", "ID"};
	private static String[][] data = {{"Kavin Singh",""+4722,""},{"Dashvin Singh",""+4474,""},{"Kav",""+4722,""}};
	private static Main main = new Main();
	private static TableModel tableModel;
	//private DBConnect con = new DBConnect();
	private static JLabel lblNoStudents;
	ImageIcon icon = new ImageIcon("icon/backarrow.png", "back arrow");
	private String[] searchStudent = {"","","",""}; //name,lastname,id,coursesEnrolled
	
	/**
	 * Launch the application.
	 */
	//public static void main(String[] args) {
	public static void initialize(){	
		setData();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StudentDBPage frame = new StudentDBPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static String[][] getData(){
		return data;
	}
	
	public static void setData(){
		main.populateStudent();
		data = new String[main.getStudents().length][4];
		for(int i=0;i<main.getStudents().length;i++){
			String[] s = main.getStudents()[i].getFLname();
			data[i][0] = s[0];
			data[i][1] = s[1];
			data[i][2] = ""+main.getStudents()[i].getStuID();
			data[i][3] = ""+main.getStudents()[i].getCoursesEnrolled().length;
		}	
		System.out.println(Arrays.deepToString(data));
	}
	
	public static void updateData(String[][] info){
		tableModel.refresh(info);
		lblNoStudents.setText("Number of Students: " + data.length);
		table.updateUI();
		scrollPane.updateUI();
	}
	
	public void searchStudent(String[]  s){
		setData();
		searchStudent = new String[]{txtFieldN.getText(), txtFieldLN.getText(), txtFieldSID.getText(), txtFieldCE.getText()};
		ArrayList<Student> stu = new ArrayList<Student>();
		for(int i=0;i<data.length;i++){
			int y = 0;
			for(int z=0;z<searchStudent.length;z++){
				if(data[i][z].equals(searchStudent[z]) || searchStudent[z].isEmpty())
				{
					y++;
					if(y==4){
						stu.add(Main.getStu(Integer.parseInt(data[i][2])));
					}	
				}
			}			
		}
		for(int c=0;c<stu.size();c++){
			System.out.println("This is stu " + stu.get(c).getName());
		}
		String[][] newData = new String[stu.size()][4];
		if(stu.size() > 0){
			for(int i=0;i<stu.size();i++){
				String[] n = stu.get(i).getFLname();
				System.out.println("n: "+ Arrays.toString(n));
				newData[i][0] = n[0];
				newData[i][1] = n[1];
				newData[i][2] = ""+stu.get(i).getStuID();
				newData[i][3] = ""+stu.get(i).getCoursesEnrolled().length;
			}
		}
	
		System.out.println("This is data: " + Arrays.deepToString(newData));
		updateData(newData);
	}	
	/**
	 * Create the frame.
	 */
	public StudentDBPage() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 490, 301);
		setResizable(false);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 484, 273);
		panel.setLayout(null);
		getContentPane().add(panel);
		
		JButton btnBack = new JButton(icon);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Main1.setFrameVisibility(true);
			}
		});
		btnBack.setBounds(8, 8, 20, 20);
		panel.add(btnBack);
		
		JLabel labelTitle = new JLabel("Student Database");
		labelTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		labelTitle.setBounds(68, 23, 128, 14);
		panel.add(labelTitle);
		
		table = new JTable();
		tableModel = new TableModel(columnNames, data, new int[]{-1});
		table.setBounds(18, 97, 288, 224);
		table.setModel(tableModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(170);
		table.getColumnModel().getColumn(1).setPreferredWidth(50);
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 48, 250, 178);
		scrollPane.setViewportView(table);
		panel.add(scrollPane);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
					selectedRow = table.getSelectedRow();
					System.out.println(table.getSelectedRow());
					{
						tableModelData = tableModel.getData();
						txtFieldCE.setText(""+tableModelData[selectedRow][3]);
						lblCE.setToolTipText(Arrays.toString(Main.getStu(Integer.parseInt(""+tableModelData[selectedRow][2])).getCoursesEnrolled()));
						lblCE.repaint();
					}
				}
	    });
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createStudent_popup.initialize();
			}
		});
		btnCreate.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnCreate.setBounds(15, 234, 71, 24);
		panel.add(btnCreate);
		
		JLabel lblName = new JLabel("First Name");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblName.setBounds(302, 65, 96, 23);
		panel.add(lblName);
		
		JLabel lblSID = new JLabel("Student ID");
		lblSID.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblSID.setBounds(302, 125, 96, 44);
		panel.add(lblSID);
		
		txtFieldN = new JTextField();
		txtFieldN.setTransferHandler(null);
		txtFieldN.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if(!Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE){
					e.consume();
				}
			}
		});
		txtFieldN.setDocument(new JTextFieldLimit(20));
		txtFieldN.setColumns(10);
		txtFieldN.setBounds(367, 63, 94, 26);
		panel.add(txtFieldN);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModelData = tableModel.getData();
				System.out.println(tableModelData.length);
				if(selectedRow >= 0 && tableModelData.length>0 && Helper.showConfirmDialog()){
					Integer row = Integer.parseInt((String) tableModelData[selectedRow][2]);
					DBConnect.deleteStudent(Main.getStu(row).getID());
					((TableModel)table.getModel()).removeRow(selectedRow);
					setData();
					table.updateUI();
					lblNoStudents.setText("Number of Students: " + tableModel.getData().length);
				}	
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnDelete.setBounds(198, 234, 67, 24);
		panel.add(btnDelete);
		
		txtFieldSID = new JTextField();
		txtFieldSID.setDocument(new JTextFieldLimit(10));
		txtFieldSID.setTransferHandler(null);
		txtFieldSID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if(!Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE){
					e.consume();
				}
			}
		});
		txtFieldSID.setColumns(10);
		txtFieldSID.setBounds(367, 136, 94, 24);
		panel.add(txtFieldSID);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				searchStudent(new String[]{});
			}
		});
		btnSearch.setBounds(390, 218, 72, 24);
		panel.add(btnSearch);
		
		JLabel lblLName = new JLabel("Last Name");
		lblLName.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblLName.setBounds(300, 103, 96, 23);
		panel.add(lblLName);
		
		txtFieldLN = new JTextField();
		txtFieldLN.setDocument(new JTextFieldLimit(30));
		txtFieldLN.setTransferHandler(null);
		txtFieldLN.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if(!Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE){
					e.consume();
				}
			}
		});
		txtFieldLN.setColumns(10);
		txtFieldLN.setBounds(367, 100, 94, 26);
		panel.add(txtFieldLN);
		
		lblCE = new JLabel("Courses Enrolled");
		lblCE.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblCE.setBounds(301, 171, 96, 23);
		panel.add(lblCE);
		
		txtFieldCE = new JTextField();
		txtFieldCE.setColumns(10);
		txtFieldCE.setBounds(425, 171, 35, 23);
		txtFieldCE.setDocument(new JTextFieldLimit(2));
		panel.add(txtFieldCE);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setData();
				updateData(data);
				txtFieldLN.setText("");
				txtFieldCE.setText("");
				txtFieldN.setText("");
				txtFieldSID.setText("");
			}
		});
		btnReset.setBounds(311, 218, 66, 24);
		panel.add(btnReset);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(selectedRow == -1)
					return; //skips rest of the action
				tableModelData = tableModel.getData();
				Integer row = Integer.parseInt((String) tableModelData[selectedRow][2]);
				editStudent_popup.setStudent(Main.getStu(row));
				editStudent_popup.initialize();
			}
		});
		btnEdit.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnEdit.setBounds(131, 234, 57, 24);
		panel.add(btnEdit);
		
	    lblNoStudents = new JLabel("Number of Students: " + data.length);
		lblNoStudents.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		lblNoStudents.setBounds(350, 9, 150, 16);
		panel.add(lblNoStudents);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(283, 48, 14, 205);
		panel.add(separator_1);
	}
}
