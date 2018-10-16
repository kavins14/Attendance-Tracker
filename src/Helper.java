import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Helper extends JDialog {
	
	private static String[] statusParse = {"Absent","Tardy","Present"};
	private JDialog modal;
	private static JPanel panel = new JPanel();

	public static void main(String args[]){
		System.out.println(showConfirmDialog());
	}
	
	public void setJDialog(JFrame frame, JPanel panel, String frameTitle){ //converts popup frames into dialogs
		modal = new JDialog(frame,frameTitle,true);
		modal.getContentPane().add(panel);
		modal.setBounds(frame.getBounds());
		modal.setResizable(false);
		modal.setVisible(true);
	}
	
	public static boolean showConfirmDialog(){
		return(JOptionPane.showConfirmDialog(panel, "Are you sure?", "Warning", JOptionPane.YES_NO_OPTION) == 0);
	}
	
	/*************************GUI METHODS************************/
	
	public static String printDate(){
		SimpleDateFormat d;
		d = new SimpleDateFormat("dd-MM-yyyy  hh:mm:ss");
		return d.format(new Date());
		
	}
}
