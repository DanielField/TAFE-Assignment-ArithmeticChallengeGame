package arithmeticchallengegame.Lists;

import arithmeticchallengegame.Equation;

/**
 *
 * @author Daniel Field
 */
public class DLinkedEquationList {
    ListNode head;

    public DLinkedEquationList() {
        head = new ListNode();
    }

    public DLinkedEquationList(Equation e) {
        head = new ListNode(e);
    }
    
    public DLinkedEquationList(float operandOne, char operator, float operandTwo, float answer) {
        head = new ListNode(operandOne, operator, operandTwo, answer);
    }

    // not implemented yet.
//    public ListNode findByDateTime(String dt) {
//        for (ListNode current = head.next; current != head; current = current.next)
//        {
//            if (current.equation.getDateTime().compareToIgnoreCase(dt) == 0)
//            {
//                System.out.println("Data " + dt + " found");
//                return current;
//            }
//        }
//        System.out.println("Data " + dt + " not found");
//        return null;
//    }

    // Get specified node
    public ListNode get(int i) {
        ListNode current = this.head;
        if (i < 0 || current == null) {
            throw new ArrayIndexOutOfBoundsException();
        }
        while (i > 0) {
            i--;
            current = current.next;
            if (current == null) {
                throw new ArrayIndexOutOfBoundsException();
            }
        }
        return current;
    }
    
    public int getLength() {
        ListNode n = this.head;
        int i = 0;
        
        while(n.next != null) {
            i++;
        }
        
        return i;
    }
    
    /*
    
    1.	str equals “HEAD <-> “
    2.	current equals head’s next node
    3.	For current is not equals to head and current is not null; current equals current’s next node
        1.  String array arr equals the return value of current’s equation’s toStringArray method
        2.  str += arr[0] + " " + arr[1] + " " + arr[2] + " = " + arr[3] + " <-> "
    4.	return str + “TAIL”

    
    */
    public String displayAsString() {
        String str = "HEAD <-> ";
        for (ListNode current = head.next; current != head && current != null; current = current.next) {
            String[] arr = current.equation.toStringArray();
            
            str += arr[0] + " " + arr[1] + " " + arr[2] + " = " + arr[3] + " <-> ";
        }
        return str + "TAIL";
    }

    public ListNode getHead() {
        return head;
    }

    public void setHead(ListNode head) {
        this.head = head;
    }
}