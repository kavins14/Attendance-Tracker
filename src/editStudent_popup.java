import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class editStudent_popup extends JFrame {

	private static JPanel contentPane;
	private static Helper h = new Helper();
	private static JTextField txtName;
	private static JTextField txtsID;
	private DBConnect con = new DBConnect();
	private String response;
	private JLabel lblResponse;
	private static Student stu;
	private JTextField txtLName;
	/**
	 * Launch the application.
	 */
	public static void initialize() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					editStudent_popup frame = new editStudent_popup();
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
	
	public static void setStudent(Student s){
		stu = s;
	}
	
	public editStudent_popup() {
		setTitle("Edit Student");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 212, 252);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setBounds(0, 0, 211, 211);
		contentPane.add(panel);
		
		JLabel lblName = new JLabel("First Name");
		lblName.setBounds(7, 11, 88, 14);
		panel.add(lblName);
		
		txtName = new JTextField();
		txtName.setText(stu.getFLname()[0]);
		txtName.setColumns(10);
		txtName.setBounds(7, 30, 177, 27);
		txtName.setTransferHandler(null);
		txtName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if(!Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE){
					e.consume();
				}
			}
		});
		panel.add(txtName);
		
		JLabel lblID = new JLabel("Student ID");
		lblID.setBounds(7, 120, 88, 14);
		panel.add(lblID);
		
		JButton btnCreate = new JButton("Update");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] name = new String[2];
				String stuID;
				if(!(txtsID.getText().isEmpty() && !txtName.getText().isEmpty() && !txtLName.getText().isEmpty())){
					name[0] = (txtName.getText().equals(stu.getFLname()[0])) ? stu.getFLname()[0] : txtName.getText();
					name[1] = (txtLName.getText().equals(stu.getFLname()[1])) ? stu.getFLname()[1] : txtLName.getText();
					stuID = (txtsID.getText().equals(""+stu.getStuID())) ? ""+stu.getStuID() : txtsID.getText();
					
					if(con.editStudent(name, Integer.parseInt(stuID), stu.getID())){
						lblResponse.setForeground(new Color(0,128,0));
						lblResponse.setText("update successful");
						txtsID.setText("");
						txtLName.setText("");
						txtName.setText("");
						StudentDBPage.setData();
						StudentDBPage.updateData(StudentDBPage.getData());
						dispose();
					}else{
						lblResponse.setForeground(new Color(255,0,0));
						lblResponse.setText("update unsuccessful");
					}
				}
			}
		});
		btnCreate.setBounds(6, 176, 74, 30);
		panel.add(btnCreate);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(91, 175, 78, 30);
		panel.add(btnCancel);
		
		txtsID = new JTextField();
		txtsID.setText(""+stu.getStuID());
		txtsID.setTransferHandler(null);
		txtsID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if(!Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE){
					e.consume();
				}
			}
		});
		txtsID.setBounds(7, 138, 140, 26);
		panel.add(txtsID);
		txtsID.setColumns(10);
		
		txtLName = new JTextField();
		txtLName.setText(stu.getFLname()[1]);
		txtLName.setColumns(10);
		txtLName.setBounds(7, 87, 177, 26);
		txtLName.setTransferHandler(null);
		txtLName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if(!Character.isAlphabetic(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE){
					e.consume();
				}
			}
		});
		panel.add(txtLName);
		
		JLabel lblLName = new JLabel("Last Name");
		lblLName.setBounds(7, 68, 88, 14);
		panel.add(lblLName);
		
		lblResponse = new JLabel();
		lblResponse.setBounds(7, 205, 184, 16);
		contentPane.add(lblResponse);
		lblResponse.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		lblResponse.setForeground(new Color(0, 128, 0));
	}
}


