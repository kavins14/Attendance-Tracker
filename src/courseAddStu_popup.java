import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class courseAddStu_popup extends JFrame {

	
	private static JPanel panel;
	private static Helper h = new Helper();
	private static JTable table;
	private static JScrollPane scrollPane;
	private int selectedRow;
	private static String[] columnNames = {"Student Name", "ID"};
	private static String[][] data = {{"Kavin Singh",""+4722},{"Dashvin Singh",""+4474},{"Kav",""+4722},{"Dash",""+4474},{"Kav",""+4722},{"Dash",""+4474},{"Kav",""+4722},{"Kav",""+4722},{"Dash",""+4474},{"Kav",""+4722},{"Dash",""+4474},{"Dash",""+4474}};
	private static Main main = new Main();
	private static TableModel tableModel;
	private static Course c;
	private DBConnect con = new DBConnect();
	/**
	 * Launch the application.
	 */
	//public static void main(String[] args) {
		public static void initialize(Course c) {
			setData(c);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					courseAddStu_popup frame = new courseAddStu_popup();
					h.setJDialog(frame, panel, "Add Course");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
		
		
	public JPanel getPanel(){
		return panel;
	}	
	
	public static void setData(Course course){
		c = course;
		main.populateStudent();
		data = new String[main.getStudents().length-c.getStudents().length][2];
		//System.out.println(Arrays.toString(main.getStudents()));
		int x=-1;
		for(int i=0;i<main.getStudents().length;i++){
			if(!Arrays.asList(main.getStudents()[i].getCoursesEnrolled()).contains(c.getName())){ //if the student is not enrolled in the course return true
				x++;
				data[x][0] = main.getStudents()[i].getName();
				data[x][1] = ""+main.getStudents()[i].getStuID();
			}else{
			}
		}	
		System.out.println(Arrays.deepToString(data));
	}
	/**
	 * Create the frame.
	 */
	public courseAddStu_popup() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 282, 296);
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panel);
		panel.setLayout(null);
		
		JLabel lblStudentDatabase = new JLabel("Student Database");
		lblStudentDatabase.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblStudentDatabase.setBounds(80, 17, 153, 16);
		panel.add(lblStudentDatabase);
		
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
		
		JButton btnAdd = new JButton("Add ");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				con.courseAddStu(c.getID(), main.getStu(Integer.parseInt((String)tableModel.getData()[selectedRow][1])).getID());
				((TableModel)table.getModel()).removeRow(selectedRow);
				table.updateUI();
				coursePage.updateStudents();
			}
		});
		btnAdd.setBounds(9, 234, 117, 31);
		panel.add(btnAdd);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(123, 234, 117, 31);
		panel.add(btnCancel);
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
					selectedRow = table.getSelectedRow();
					System.out.println(table.getSelectedRow());
				}
	    });
		
	}
}
