package arithmeticchallengegame.Lists;

import arithmeticchallengegame.Equation;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Daniel Field
 */
public class TableModel extends AbstractTableModel
    {
        ArrayList<Equation> al;

        // the headers
        String[] header;

        // constructor 
        public TableModel(ArrayList<Equation> eqs, String[] header)
        {
            this.header = header;
            al = eqs;
        }

        public void setAl(ArrayList<Equation> al) {
            this.al = al;
        }

        public ArrayList<Equation> getAl() {
            return al;
        }

        @Override
        public int getRowCount()
        {
            return al.size();
        }

        @Override
        public int getColumnCount()
        {
            return header.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            // Converts row to string[] and then returns the cell specified
            return al.get(rowIndex).toStringArray()[columnIndex];
        }

        // a method to return the column name 
        @Override
        public String getColumnName(int index)
        {
            return header[index];
        }

//        // a method to add a new line to the table
//        void add(String operand1, String operator, String operand2, String answer)
//        {
//            String[] str = new String[4];
//            str[0] = operand1+"";
//            str[1] = operator;
//            str[2] = operand2+"";
//            str[3] = answer+"";
//            al.add(str);
//
//            fireTableDataChanged();
//        }
        
        // a method to add a new line to the table (using Equation class)
        public void add(Equation e)
        {
            if(e.isCorrect())
                al.add(e);

            fireTableDataChanged();
        }
    }

