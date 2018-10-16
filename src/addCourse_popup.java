import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextArea;

public class addCourse_popup extends JFrame {

	private static JPanel contentPane;
	private static Helper h = new Helper();
	private JTextField textField;
	JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void c_popup() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					addCourse_popup frame = new addCourse_popup();
					h.setJDialog(frame, contentPane, "Add Course");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public addCourse_popup() {
		setResizable(false);
		setTitle("Add Course");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); <- this code closes all windows
		setBounds(100, 100, 213, 209);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCourseName = new JLabel("Course Name");
		lblCourseName.setBounds(10, 11, 88, 14);
		contentPane.add(lblCourseName);
		
		textField = new JTextField();
		textField.setBounds(10, 28, 118, 28);
		textField.setColumns(10);
		textField.setDocument(new JTextFieldLimit(20));
		contentPane.add(textField);
		
		JLabel lblDescription = new JLabel("Description");
		lblDescription.setBounds(10, 68, 88, 14);
		contentPane.add(lblDescription);
		
		JButton btnCreate = new JButton("Add");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!textField.getText().isEmpty() && !textArea.getText().isEmpty()){
					if(DBConnect.addCourse(textField.getText(), textArea.getText())){
						Main1.updateCourse();
						dispose();
					}	
				}	
			}
		});
		btnCreate.setBounds(10, 146, 78, 23);
		contentPane.add(btnCreate);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(98, 146, 89, 23);
		contentPane.add(btnCancel);
		
		textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setRows(3);
		textArea.setBounds(10, 85, 177, 50);
		textArea.setDocument(new JTextFieldLimit(100));
		contentPane.add(textArea);
		
	}
	
	public void lineLimit(JTextArea ta){
		 int numLinesToTrunk = ta.getLineCount() - 3;
		    if(numLinesToTrunk > 0)
		    {
		        try
		        {
		            int posOfLastLineToTrunk = ta.getLineEndOffset(numLinesToTrunk - 1);
		            ta.replaceRange("",0,posOfLastLineToTrunk);
		        }
		        catch (BadLocationException ex) {
		            ex.printStackTrace();
		        }
		    }
	}
}
