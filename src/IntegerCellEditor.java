import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class IntegerCellEditor extends DefaultCellEditor {


    private JTextField textField;

    public IntegerCellEditor(JTextField textField) {
        super(textField);
        this.textField = textField;
    }

    @Override
    public boolean stopCellEditing() {
        try {
            int v = Integer.valueOf(textField.getText());
            switch (v){
            	case 0: 
            		break;
            	case 1: 
            		break;
            	case 2: 
            		break;
            default: throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            textField.setBorder(new LineBorder(Color.red));
            return false;
        }
        return super.stopCellEditing();
    }

}