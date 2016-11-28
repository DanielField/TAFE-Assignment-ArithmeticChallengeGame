package arithmeticchallengegame.Lists;

import arithmeticchallengegame.Equation;
import arithmeticchallengegame.Utilities;
import java.util.ArrayList;

// Most of this code is from http://www.newthinktank.com/2013/03/binary-tree-in-java/
public class BinaryTree {

    Leaf root;
    
    String treeString = "";

    // Loops through each equation and adds it to the tree
    public void addLeaves(ArrayList<Equation> eqs) {
        for (Equation e : eqs) {
            addLeaf(e);
        }
    }

    // Adds a leaf (node) to the tree
    public void addLeaf(Equation e) {
            // Create a new ListNode and initialize it
        Leaf newLeaf = new Leaf(e);
            // If there is no root this becomes root
        if (root == null) {
            root = newLeaf;
        } else {
            // Set root as the ListNode we will start
            // with as we traverse the tree
            Leaf focusLeaf = root;
            // Future parent for our new ListNode
            Leaf parent;
            while (true) {
                // root is the top parent so we start there
                parent = focusLeaf;
                // Check if the new node should go on
                // the left side of the parent node
                if (e.getAnswer() < focusLeaf.e.getAnswer()) {
                    // Switch focus to the left child
                    focusLeaf = focusLeaf.leftChild;
                    // If the left child has no children
                    if (focusLeaf == null) {
                        // then place the new node on the left of it
                        parent.leftChild = newLeaf;
                        return; // All Done
                    }
                } else { // If we get here put the node on the right
                    focusLeaf = focusLeaf.rightChild;
                    // If the right child has no children
                    if (focusLeaf == null) {
                        // then place the new node on the right of it
                        parent.rightChild = newLeaf;
                        return; // All Done
                    }
                }
            }
        }
    }

    // All nodes are visited in ascending order
    // Recursion is used to go to one node and
    // then go to its child nodes and so forth
    
    // LVR
    public void inOrderTraverseTree(Leaf focusLeaf) {
        if (focusLeaf != null) {
            inOrderTraverseTree(focusLeaf.leftChild);
            treeString += getEquationAndStripZeroes(focusLeaf);
            inOrderTraverseTree(focusLeaf.rightChild);
        }
    }

    // VLR
    public void preOrderTraverseTree(Leaf focusLeaf) {
        if (focusLeaf != null) {
            treeString += getEquationAndStripZeroes(focusLeaf);
            preOrderTraverseTree(focusLeaf.leftChild);
            preOrderTraverseTree(focusLeaf.rightChild);
        }
    }

    // LRV
    public void postOrderTraverseTree(Leaf focusLeaf) {
        if (focusLeaf != null) {
            postOrderTraverseTree(focusLeaf.leftChild);
            postOrderTraverseTree(focusLeaf.rightChild);
            treeString += getEquationAndStripZeroes(focusLeaf);
        }
    }

    // Gets the equation in this format: 
    // "<answer> (<operand one><operator><operand two>), "
    private String getEquationAndStripZeroes(Leaf focusLeaf) {
        return Utilities.stripZero(focusLeaf.e.getAnswer() + "") + " (" + Utilities.stripZero(focusLeaf.e.getOperandOne() + "") + focusLeaf.e.getOperator() + Utilities.stripZero(focusLeaf.e.getOperandTwo() + "") + "), ";
    }

    // Set temp to treeString without the comma at the end
    // Clear treeString
    // Return temp string
    public String getTreeAsString() {
        String temp = treeString.substring(0, treeString.length() - 2);
        treeString = "";
        return temp;
    }

    // Finds a leaf that has the specified answer
    public Leaf findLeaf(float answer) {
        
         // Start at the top of the tree
        Leaf focusLeaf = root;
        
        // While we haven't found the ListNode
        // keep looking
        while (focusLeaf.e.getAnswer() != answer) {
            // If we should search to the left
            if (answer < focusLeaf.e.getAnswer()) {
                // Shift the focus ListNode to the left child
                focusLeaf = focusLeaf.leftChild;
            } else {
                // Shift the focus ListNode to the right child
                focusLeaf = focusLeaf.rightChild;
            }
            // The node wasn't found
            if (focusLeaf == null) {
                return null;
            }
        }
        return focusLeaf;
    }

    // Gets the root node
    public Leaf getRoot() {
        return root;
    }

    // Sets the root node
    public void setRoot(Leaf root) {
        this.root = root;
    }
}

// Node
class Leaf {

    Equation e;

    Leaf leftChild;
    Leaf rightChild;

    Leaf(Equation e) {
        this.e = e;
    }

    public String toString() {
        return e.toString();
    }

}
