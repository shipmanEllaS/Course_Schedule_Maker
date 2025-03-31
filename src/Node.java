/**********************************************************************************************
 * @file : Node.java
 * @description : Nodes for a doubly-linked list of type Section.
 * @author : Ella Shipman
 * @date : 30 March 2025
 *********************************************************************************************/
public class Node {

    public Section data;
    public Node next;
    public Node prev;

    public Node(){
        this.data = new Section();
        next = null;
        prev = null;
    }

    public Node(Section data){
        this.data = data;
        next = null;
        prev = null;
    }
}
