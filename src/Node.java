/**********************************************************************************************
 * @file : Node.java
 * @description : Nodes for a doubly-linked list of type Section.
 * @author : Ella Shipman
 * @date : 30 March 2025
 *********************************************************************************************/
public class Node <DataType> {

    public DataType data;
    public Node<DataType> next;
    public Node<DataType> prev;

    public Node(){
        this.data = null;
        next = null;
        prev = null;
    }

    public Node(DataType data){
        this.data = data;
        next = null;
        prev = null;
    }

    public Node(Node<DataType> n) {
        this.data = n.data;
        next = null;
        prev = null;
    }
}
