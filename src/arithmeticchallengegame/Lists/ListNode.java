/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arithmeticchallengegame.Lists;

import arithmeticchallengegame.Equation;

/**
 * This class was originally inside the DLinkedEquationList.java file, but it was taken out because I needed to make it public. 
 * It would have been better to have it as a sub-class, though.
 * 
 * The purpose of this class is to store a single node of the doubly linked list.
 * 
 * @author Daniel Field
 */
public class ListNode {
    ListNode prev;
    ListNode next;
    Equation equation;

    public ListNode() {
        prev = null;
        next = null;
        equation = null;
    }
    
    public ListNode(Equation eq) {
        prev = null;
        next = null;
        equation = eq;
    }
    
    public ListNode(float operandOne, char operator, float operandTwo, float answer) {
        prev = null;
        next = null;
        equation = new Equation(operandOne, operator, operandTwo, answer);
    }

    public void append(float operandOne, char operator, float operandTwo, float answer) {
        ListNode n = new ListNode(operandOne,operator,operandTwo,answer);
        n.prev = this;
        n.next = next;
        
        if (next != null) {
            next.prev = n;
        }
        
        next = n;
    }

    // insert new node before this node
    public void insert(ListNode newNode) {
        newNode.prev = prev;
        newNode.next = this;
        prev.next = newNode;
        prev = newNode;
    }

    public void remove() {
        next.prev = prev;
        prev.next = next;
    }
    
    @Override
    public String toString() {
        return this.equation.getOperandOne()+" "+this.equation.getOperator()+" "+this.equation.getOperandTwo()+" = "+this.equation.getAnswer();
    }
}