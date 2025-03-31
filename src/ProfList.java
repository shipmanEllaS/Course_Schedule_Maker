/**********************************************************************************************
 * @file : LinkedList.java
 * @description : A doubly-linked list class that manages nodes with data of type Section.
 *                Notable functions include sorting, adding/removing, checking for an empty
 *                list, and a sanity check.
 * @author : Ella Shipman
 * @date : 30 March 2025
 *********************************************************************************************/

public class ProfList {

    public ProfNode head;
    public ProfNode tail;
    public int size = 0;

    ProfList(){
        head = null;
        tail = null;
        size = 0;
    }

    /*  --------------------------------------------------------------------------------------
     *   remove_from_index - removes section at a certain index, from 1-catalogue size
     *   int index : the section to be removed
     */
    public Professor remove_from_index(int index) {
        Professor atIndex;

        if (index > size) {     //Index out of bounds
            System.out.println("Cannot remove Professor at index " + index);
            System.exit(1);
        }
        if (index == 1) {       //Removing head
            atIndex = head.data;
            head = head.next;
            head.prev = null;
            size--;
            return atIndex;
        }
        if (index == size) {        //Removing tail
            atIndex = tail.data;
            tail = tail.prev;
            tail.next = null;
            size--;
            return atIndex;
        }

        ProfNode curr = head;
        int counter = 1;
        //Identify node to be removed (curr)
        while ((counter < index) && (curr != null)) {
            curr = curr.next;
            counter++;
        }
        atIndex = curr.data;
        curr = curr.next;

        //Shift cards up to fill removed card--ends when curr is the tail
        while (curr.next != null){
            curr.prev.data = curr.data;
            curr = curr.next;
        }

        //Establish new tail
        curr.prev.data = curr.data;
        tail = curr.prev;
        tail.next = null;

        size--;
        return atIndex;
    }

    /*  --------------------------------------------------------------------------------------
     *   insert_from_index - inserts section at a certain index, from 1-catalogue size
     *   Section x : the section to be inserted
     *   int index : the place to insert the section
     */
    public void insert_at_index(Professor p, int index) {
        ProfNode curr = head;
        int counter = 1;
        ProfNode newNode = new ProfNode(p);

        if (index == 1) {       //Insert at head
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        } else if (index >= size) {     //Insert at tail
            //Accounts for case of adding to index (size) in swap() method, where the size of the list
            //will temporarily be (size - 2) until sections are inserted back in
            newNode.next = null;
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        } else {        //Insert at other places
            while (counter <= index) {
                if (counter == index) {
                    newNode.prev = curr.prev;
                    newNode.next = curr;
                    curr.prev.next = newNode;
                    curr.prev = newNode;
                }
                curr = curr.next;
                counter++;
            }
        }
        size++;
    }


    /*  --------------------------------------------------------------------------------------
     *   swap - swaps the positions of two sections at specific indexes, from 1-deck size (52 max)
     *   int index1 : the first index to swap
     *   int index2 + the second index to swap
     */
    public void swap(int index1, int index2) {
        ProfNode curr = head;
        ProfNode s1 = curr;
        ProfNode s2 = curr;

        if (isEmpty()) {
            System.out.println("Cannot swap sections an empty catalogue.");
            System.exit(1);
        }

        if (index1 == index2) { return; }

        int counter = 1;
        while (curr != null) {
            if (counter == index1) {
                s1 = curr;
            }
            if (counter == index2) {
                s2 = curr;
            }
            counter++;
            curr = curr.next;
        }

        Professor temp = s1.data;
        s1.data = s2.data;
        s2.data = temp;

        /*
        if (index1 < index2) {  //Ensures index1 will always be bigger than index2, unless they are the same
            int temp = index1;
            index1 = index2;
            index2 = temp;
        }

        if (index1 != index2) {
            Section s1 = remove_from_index(index1);     //Remove greater index first to avoid index/null exception
            Section s2 = remove_from_index(index2);
            insert_at_index(s1, index2);     //Insert lesser index first to avoid index/null exception
            insert_at_index(s2, index1);
        }

         */
    }


    /*  --------------------------------------------------------------------------------------
     *   add_at_tail - appends section to the end of the catalogue
     *   Card data : the section to be added
     */
    public void add_at_tail(Professor p) {
        ProfNode newTail = new ProfNode(p);
        if (isEmpty()) {     //List is empty
            head = newTail;
            tail = newTail;
        } else if (head.next == null) {     //List only has head
            head.next = newTail;
            newTail.prev = head;
            tail = newTail;
        } else {        //Add to end
            tail.next = newTail;
            newTail.prev = tail;
            tail = newTail;
        }
        size++;
    }

    /*  --------------------------------------------------------------------------------------
     *   remove_from_head - removes section at the top (head) of the deck
     */
    public Professor remove_from_head() {
        Professor atIndex = head.data;
        if (isEmpty()) {
            System.out.println("Cannot remove a section from an empty list.");
            System.exit(1);
        }
        if (head.next == null) {
            head = null;
            tail = null;
            return atIndex;
        }

        ProfNode curr = head.next;

        while (curr != null) {
            curr.prev.data = curr.data;
            if (curr.next == null) { //curr is tail
                tail = curr.prev;
                tail.prev = curr.prev.prev;
                tail.next= null;
            }
            curr = curr.next;
        }

        size--;
        return atIndex;
    }

    //FROM LAB 6
    // check to make sure the linked list is implemented correctly by iterating forwards and backwards
    // and verifying that the size of the list is the same when counted both ways.
    // 1) if a node is incorrectly removed
    // 2) and head and tail are correctly updated
    // 3) each node's prev and next elements are correctly updated
    public void sanity_check() {
        // count nodes, counting forward
        ProfNode curr = head;
        int count_forward = 0;
        while (curr != null) {
            curr = curr.next;
            count_forward++;
        }

        // count nodes, counting backward
        curr = tail;
        int count_backward = 0;
        while (curr != null) {
            curr = curr.prev;
            count_backward++;
        }

        // check that forward count, backward count, and internal size of the list match
        if (count_backward == count_forward && count_backward == size) {
            System.out.println("Basic sanity Checks passed");
        }
        else {
            // there was an error, here are the stats
            System.out.println("Count forward:  " + count_forward);
            System.out.println("Count backward: " + count_backward);
            System.out.println("Size of LL:     " + size);
            System.out.println("Sanity checks failed");
            System.exit(-1);
        }
    }

    /*  --------------------------------------------------------------------------------------
     *   print - printing the catalogue
     */
    public void print() {
        ProfNode curr = head;
        int i = 1;
        while(curr != null) {
            System.out.print(curr.data.toString());
            if(curr.next != null)
                System.out.print(" -->  ");
            else
                System.out.println(" X");

            System.out.println("");
            i = i + 1;
            curr = curr.next;
        }
        System.out.println("");
    }

    /*  --------------------------------------------------------------------------------------
     *   isEmpty - checks if the list is empty
     */
    public boolean isEmpty() { return head == null; }

    //Sort courses cased off of SectionComparator
    /*
    public void sortCourses() {
        SectionComparator sectionCompare = new SectionComparator();
        Node curr;
        int counter;

        if (isEmpty()) {
            System.out.println("Cannot sort an empty course catalogue.");
            System.exit(1);
        }

        while (!isSorted(sectionCompare)) {
            System.out.println("Initializing curr as head");
            curr = head;
            counter = 1;

            while (curr.next != null) {
                System.out.println("Counter is " + counter);
                System.out.println("curr is " + curr.data.toString());
                if (curr.prev != null) {
                    System.out.println("prev is " + curr.prev.data.toString());
                }
                System.out.println("next is " + curr.next.data.toString());
                if (sectionCompare.compare(curr.data, curr.next.data) < 0) {
                    System.out.println("Swapping " + counter + " with " + (counter + 1));
                    print();
                    swap(counter, (counter + 1));
                    print();
                }
                curr = curr.next;
                counter++;
                System.out.println("Update counter");
            }
            System.out.println("Out of nested while loop.");
        }
    }



    //Checks if classes is sorted based off of SectionComparator
    public boolean isSorted(SectionComparator SComparing) {
        Node curr = head;
        while (curr.next != null) {
            if (SComparing.compare(curr.data, curr.next.data ) < 0) {
                return false;
            }
            System.out.println("isSorted while loop");
            curr = curr.next;
        }
        return true;
        /* ARRAYLIST VER
        for (int i = 0; i < n - 1; i++) {
            if (SComparing.compare(classes.get(i), classes.get(i+1)) < 0) {
                return false;
            }
        }
        return true;


    }

    //Calculates the credit hours in a list of sections
    public double calculateCredit() {
        double creditHours = 0;
        double tempHours = 0;
        Node curr = head;

        while (curr != null) {
            try {
                tempHours = Double.parseDouble(curr.data.getHours());
            } catch (NumberFormatException e) {
                System.out.println("Unable to calculate credit hours for section " + curr.data.toString());
                System.exit(1);
            }
            creditHours += tempHours;
            curr = curr.next;
        }
        return creditHours;
    }
    */
}
