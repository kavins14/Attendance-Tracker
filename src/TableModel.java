import java.util.Arrays;
import java.lang.Object;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {
	
    private String[] columnNames;
    private Object[][] data;
    private int[] editColumn;

	
    public TableModel(String[] columnNames, Object[][] data, int[] edit){
    	this.data = data;
    	this.columnNames = columnNames;
    	editColumn = edit;
    }
    
    public Object[][] getData(){
    	return data;
    }
    
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}

	
	public int getRowCount() {
		return data.length;
	}

	
	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		return data[row][col];
	}
	
	public String getColumnName(int col) {
		return columnNames[col]; //columnNames[col];
    }

    
	public boolean isCellEditable(int row, int col) {
		
		for(int o: editColumn){
			if(col == o)
				return true;
		}
		return false;

	}

	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value; 
		fireTableCellUpdated(row, col);
	}


	public void removeRow(int row) {
		Object[][] tmp = new Object[data.length - 1][];
		int j = 0;
		for (int i = 0; i < data.length; i++) {
		    if (i != row) {
		        tmp[j++] = data[i];
		    }
		}
		data = tmp;
		// TODO Auto-generated method stub
		
	}

	public void refresh(Object[][] data){
	    //make the changes to the table, then call fireTableChanged
		this.data = data;
	    //fireTableChanged(null);
	}

}
