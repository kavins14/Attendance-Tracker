import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;

public class createStudent_popup extends JFrame {

	private static JPanel contentPane;
	private static Helper h = new Helper();
	private JTextField txtName;
	private JTextField txtsID;
	private DBConnect con = new DBConnect();
	private String response;
	private JLabel lblResponse;
	private JTextField txtLName;
	private Color redColor = new Color(255,0,0);

	/**
	 * Launch the application.
	 */
	//public static void main(String[] args) {
	public static void initialize(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					createStudent_popup frame = new createStudent_popup();
					h.setJDialog(frame, contentPane, "Create Student");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public createStudent_popup() {
		setResizable(false);
		setTitle("Create Student");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 212, 244);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setBounds(0, 0, 199, 189);
		contentPane.add(panel);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(102, 158, 78, 30);
		panel.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		JButton btnCreate = new JButton("Create");
		btnCreate.setBounds(11, 158, 74, 30);
		panel.add(btnCreate);
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!txtsID.getText().isEmpty() && !txtName.getText().isEmpty() && !txtLName.getText().isEmpty()){
					if(con.createStudent(new String[]{txtName.getText(),txtLName.getText()}, Integer.parseInt(txtsID.getText()))){
						lblResponse.setForeground(new Color(0,128,0));
						lblResponse.setText(txtName.getText() + " created");
						txtsID.setText("");
						txtName.setText("");
						StudentDBPage.setData();
						StudentDBPage.updateData(StudentDBPage.getData());
					}else{
						lblResponse.setForeground(redColor);
						lblResponse.setText("creation unsuccessful");
					}
				}else{
					lblResponse.setForeground(redColor);
					lblResponse.setText("please fill in all fields");
				}
			}
		});
		
		JLabel lblName = new JLabel("Full Name");
		lblName.setBounds(10, 11, 88, 14);
		panel.add(lblName);
		
		txtName = new JTextField();
		txtName.setColumns(10);
		txtName.setBounds(10, 30, 170, 20);
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
		lblID.setBounds(11, 104, 88, 14);
		panel.add(lblID);
		
		txtsID = new JTextField();
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
		txtsID.setBounds(10, 123, 140, 20);
		panel.add(txtsID);
		txtsID.setColumns(10);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setBounds(10, 59, 88, 14);
		panel.add(lblLastName);
		
		txtLName = new JTextField();
		txtLName.setColumns(10);
		txtLName.setBounds(10, 78, 170, 20);
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
		
		lblResponse = new JLabel();
		lblResponse.setBounds(13, 196, 184, 16);
		contentPane.add(lblResponse);
		lblResponse.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		lblResponse.setForeground(new Color(0, 128, 0));
	}
}
