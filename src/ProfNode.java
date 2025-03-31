/**********************************************************************************************
 * @file : Node.java
 * @description : Nodes for a doubly-linked list of type Professor.
 * @author : Ella Shipman
 * @date : 30 March 2025
 *********************************************************************************************/
public class ProfNode {

    public Professor data;
    public ProfNode next;
    public ProfNode prev;

    public ProfNode(){
        this.data = new Professor();
        next = null;
        prev = null;
    }

    public ProfNode(Professor p){
        this.data = p;
        next = null;
        prev = null;
    }
}

