/*
 * LinkedTree.java
 *
 * Computer Science E-22
 *
 * Modifications and additions by:
 *     name:
 *     username:
 */

import java.util.*;

/*
 * LinkedTree - a class that represents a binary tree containing data
 * items with integer keys.  If the nodes are inserted using the
 * insert method, the result will be a binary search tree.
 */
public class LinkedTree {
    // An inner class for the nodes in the tree
    private class Node {
        private int key;         // the key field
        private LLList data;     // list of data values for this key
        private Node left;       // reference to the left child/subtree
        private Node right;      // reference to the right child/subtree
        private Node parent;     // reference to the parent. NOT YET MAINTAINED!
        
        private Node(int key, Object data){
            this.key = key;
            this.data = new LLList();
            this.data.addItem(data, 0);
            this.left = null;
            this.right = null;
            this.parent = null;
        }
    }
    
    // the root of the tree as a whole
    private Node root;
    
    public LinkedTree() {
        root = null;
    }

    public LinkedTree(int[] keys, Object[] dataItems) {
        if (keys.length != dataItems.length) {
            System.out.println("Cannot create the LinkedTree. Mismatch between keys and data arrays lengths");
        }
        else if (keys.length == 0) {
            return;
        }

        //Sorting the arrays
        SortHelper.quickSort(keys, dataItems);

        //Inserting the key-data values into the tree
        insertBal(keys, dataItems, 0, keys.length - 1);
    }

    //Helper to be used by the Constructor
    private void insertBal (int []keys, Object[] dataItems, int first, int last) {
        //We create an insert function that calls itself recursively
        //We will follow a fashion similar to the partitioning of quickSort
        
        //Error checking
        if (first > last) {
            return;
        }
        //Base case: If the passed arrays are of size 1, insert that element
        else if (first == last) {
            insert (keys[first], dataItems[first]);
        }
        else {
            //If the passed array's size is more than 1, then insert the middle
            //element, then call the insertion on the remaining subarrays

            int split = (first + last)/2;
            insert (keys[split], dataItems[split]);
            insertBal(keys, dataItems, first, split - 1);
            insertBal(keys, dataItems, split + 1, last);
        }
    }
    
    /*
     * Prints the keys of the tree in the order given by a preorder traversal.
     * Invokes the recursive preorderPrintTree method to do the work.
     */
    public void preorderPrint() {
        if (root != null) {
            preorderPrintTree(root);      
        }
        System.out.println();
    }
    
    /*
     * Recursively performs a preorder traversal of the tree/subtree
     * whose root is specified, printing the keys of the visited nodes.
     * Note that the parameter is *not* necessarily the root of the 
     * entire tree. 
     */
    private static void preorderPrintTree(Node root) {
        System.out.print(root.key + " ");
        if (root.left != null) {
            preorderPrintTree(root.left);
        }
        if (root.right != null) {
            preorderPrintTree(root.right);
        }
    }
    
    /*
     * Prints the keys of the tree in the order given by a postorder traversal.
     * Invokes the recursive postorderPrintTree method to do the work.
     */
    public void postorderPrint() {
        if (root != null) {
            postorderPrintTree(root);      
        }
        System.out.println();
    }
    
    /*
     * Recursively performs a postorder traversal of the tree/subtree
     * whose root is specified, printing the keys of the visited nodes.
     * Note that the parameter is *not* necessarily the root of the 
     * entire tree. 
     */
    private static void postorderPrintTree(Node root) {
        if (root.left != null) {
            postorderPrintTree(root.left);
        }
        if (root.right != null) {
            postorderPrintTree(root.right);
        }
        System.out.print(root.key + " ");
    }
    
    /*
     * Prints the keys of the tree in the order given by an inorder traversal.
     * Invokes the recursive inorderPrintTree method to do the work.
     */
    public void inorderPrint() {
        if (root != null) {
            inorderPrintTree(root);      
        }
        System.out.println();
    }
    
    /*
     * Recursively performs an inorder traversal of the tree/subtree
     * whose root is specified, printing the keys of the visited nodes.
     * Note that the parameter is *not* necessarily the root of the 
     * entire tree. 
     */
    private static void inorderPrintTree(Node root) {
        if (root.left != null) {
            inorderPrintTree(root.left);
        }
        System.out.print(root.key + " ");
        if (root.right != null) {
            inorderPrintTree(root.right);
        }
    }
    
    /* 
     * Inner class for temporarily associating a node's depth
     * with the node, so that levelOrderPrint can print the levels
     * of the tree on separate lines.
     */
    private class NodePlusDepth {
        private Node node;
        private int depth;
        
        private NodePlusDepth(Node node, int depth) {
            this.node = node;
            this.depth = depth;
        }
    }
    
    /*
     * Prints the keys of the tree in the order given by a
     * level-order traversal.
     */
    public void levelOrderPrint() {
        LLQueue<NodePlusDepth> q = new LLQueue<NodePlusDepth>();
        
        // Insert the root into the queue if the root is not null.
        if (root != null) {
            q.insert(new NodePlusDepth(root, 0));
        }
        
        // We continue until the queue is empty.  At each step,
        // we remove an element from the queue, print its value,
        // and insert its children (if any) into the queue.
        // We also keep track of the current level, and add a newline
        // whenever we advance to a new level.
        int level = 0;
        while (!q.isEmpty()) {
            NodePlusDepth item = q.remove();
            
            if (item.depth > level) {
                System.out.println();
                level++;
            }
            System.out.print(item.node.key + " ");
            
            if (item.node.left != null) {
                q.insert(new NodePlusDepth(item.node.left, item.depth + 1));
            }
            if (item.node.right != null) {
                q.insert(new NodePlusDepth(item.node.right, item.depth + 1));
            }
        }
        System.out.println();
    }
    
    /*
     * Searches for the specified key in the tree.
     * If it finds it, it returns the list of data items associated with the key.
     * Invokes the recursive searchTree method to perform the actual search.
     */
    public LLList search(int key) {
        Node n = searchTree(root, key);
        if (n == null) {
            return null;
        } else {
            return n.data;
        }
    }
    
    /*
     * Recursively searches for the specified key in the tree/subtree
     * whose root is specified. Note that the parameter is *not*
     * necessarily the root of the entire tree.
     */
    private static Node searchTree(Node root, int key) {
        if (root == null) {
            return null;
        } else if (key == root.key) {
            return root;
        } else if (key < root.key) {
            return searchTree(root.left, key);
        } else {
            return searchTree(root.right, key);
        }
    }
    
    /*
     * Inserts the specified (key, data) pair in the tree so that the
     * tree remains a binary search tree.
     */
    public void insert(int key, Object data) {
        // Find the parent of the new node.
        Node parent = null;
        Node trav = root;
        while (trav != null) {
            if (trav.key == key) {
                trav.data.addItem(data, 0);
                return;
            }
            parent = trav;
            if (key < trav.key) {
                trav = trav.left;
            } else {
                trav = trav.right;
            }
        }
        
        // Insert the new node.
        Node newNode = new Node(key, data);
        if (parent == null) {    // the tree was empty
            root = newNode;     //NO NEED TO MAINTAIN THE PARENT BECAUSE THE PARENT IS ALREADY NULL, AND THE PARENT OF THE ROOT IS NULL
        } else if (key < parent.key) {
            parent.left = newNode;
            newNode.parent = parent;    //MAINTAINING THE PARENT REFERENCE
        } else {
            parent.right = newNode;
            newNode.parent = parent;    //MAINTAINING THE PARENT REFERENCEe
        }
    }
    
    /*
     * FOR TESTING: Processes the integer keys in the specified array from 
     * left to right, adding a node for each of them to the tree. 
     * The data associated with each key is a string based on the key.
     */
    public void insertKeys(int[] keys) {
        for (int i = 0; i < keys.length; i++) {
            insert(keys[i], "data for key " + keys[i]);
        }
    }
    
    /*
     * Deletes the node containing the (key, data) pair with the
     * specified key from the tree and return the associated data item.
     */
    public LLList delete(int key) {
        // Find the node to be deleted and its parent.
        Node parent = null;
        Node trav = root;
        while (trav != null && trav.key != key) {
            parent = trav;
            if (key < trav.key) {
                trav = trav.left;
            } else {
                trav = trav.right;
            }
        }
        
        // Delete the node (if any) and return the removed data item.
        if (trav == null) {   // no such key    
            return null;
        } else {
            LLList removedData = trav.data;
            deleteNode(trav, parent);
            return removedData;
        }
    }
    
    /*
     * Deletes the node specified by the parameter toDelete.  parent
     * specifies the parent of the node to be deleted. 
     */
    private void deleteNode(Node toDelete, Node parent) {
        if (toDelete.left != null && toDelete.right != null) {
            // Case 3: toDelete has two children.
            // Find a replacement for the item we're deleting -- as well as 
            // the replacement's parent.
            // We use the smallest item in toDelete's right subtree as
            // the replacement.
            Node replaceParent = toDelete;
            Node replace = toDelete.right;
            while (replace.left != null) {
                replaceParent = replace;
                replace = replace.left;
            }
            
            // Replace toDelete's key and data with those of the 
            // replacement item.
            toDelete.key = replace.key;
            toDelete.data = replace.data;
            
            // Recursively delete the replacement item's old node.
            // It has at most one child, so we don't have to
            // worry about infinite recursion.
            deleteNode(replace, replaceParent);
        } else {
            // Cases 1 and 2: toDelete has 0 or 1 child
            Node toDeleteChild;
            if (toDelete.left != null) {
                toDeleteChild = toDelete.left;
            } else {
                toDeleteChild = toDelete.right;  // null if it has no children
            }
            
            if (toDelete == root) {
                root = toDeleteChild;
                root.parent = null;         //MAINTAINING THE PARENT REFERENCE
            } else if (toDelete.key < parent.key) {
                parent.left = toDeleteChild;
                toDeleteChild.parent = parent;      //MAINTAINING THE PARENT REFERENCEE
            } else {
                parent.right = toDeleteChild;
                toDeleteChild.parent = parent;      //MAINTAINING THE PARENT REFERENCE
            }
        }
    }
    
    /* Returns a preorder iterator for this tree. */
    public LinkedTreeIterator preorderIterator() {
        return new PreorderIterator();
    }
    
    /* 
     * inner class for a preorder iterator 
     * IMPORTANT: You will not be able to actually use objects from this class
     * to perform a preorder iteration until you have modified the LinkedTree
     * methods so that they maintain the parent fields in the Nodes.
     */
    private class PreorderIterator implements LinkedTreeIterator {
        private Node nextNode;
        
        private PreorderIterator() {
            // The traversal starts with the root node.
            nextNode = root;
        }
        
        public boolean hasNext() {
            return (nextNode != null);
        }
        
        public int next() {
            if (nextNode == null) {
                throw new NoSuchElementException();
            }
            
            // Store a copy of the key to be returned.
            int key = nextNode.key;
            
            // Advance nextNode.
            if (nextNode.left != null) {
                nextNode = nextNode.left;
            } else if (nextNode.right != null) {
                nextNode = nextNode.right;
            } else {
                // We've just visited a leaf node.
                // Go back up the tree until we find a node
                // with a right child that we haven't seen yet.
                Node parent = nextNode.parent;
                Node child = nextNode;
                while (parent != null &&
                       (parent.right == child || parent.right == null)) {
                    child = parent;
                    parent = parent.parent;
                }
                
                if (parent == null) {
                    nextNode = null;  // the traversal is complete
                } else {
                    nextNode = parent.right;
                }
            }
            
            return key;
        }
    }  

    public LinkedTreeIterator inorderIterator() {
        return new InorderIterator();
    }


    private class InorderIterator implements LinkedTreeIterator {
        private Node nextNode;

        private InorderIterator () {  
            nextNode = root;

            while (nextNode.left != null) {
                nextNode = nextNode.left;
            }
        }

        public boolean hasNext() {
            return (nextNode != null);
        }

        public int next() {
            if (nextNode == null) {
                throw new NoSuchElementException();
            }

            


            int key = nextNode.key;

            //Advance nextNode
            if (nextNode.right != null) {

                nextNode = nextNode.right;
                
                //Re-initializing the first node, for every new root
                while (nextNode.left != null) {
                    nextNode = nextNode.left;
                }
            }
            else {
                Node parent = nextNode.parent;
                Node child = nextNode;
                while (parent != null &&
                       (parent.right == child)) {
                    child = parent;
                    parent = parent.parent;
                }

                if (parent == null) {
                    nextNode = null;  // the traversal is complete
                } else {
                    nextNode = parent;
                }

            }

            return key;
            
        }

    }
    
    /*
     * "wrapper method" for the recursive depthInTree() method
     * from PS 4, Problem 4
     */
    public int depth(int key) {
        if (root == null) {    // root is the root of the entire tree
            return -1;
        }
        
        return depthInTree(key, root);
    }
    
    /*
     * original version of the recursive depthInTree() method  
     * from PS 4, Problem 4. You will write a more efficient version
     * of this method.
     */
    private static int depthInTree(int key, Node root) {
        // if (key == root.key) {
        //     return 0;     // found
        // } else {
        //     if (root.left != null) {
        //         int depthInLeft = depthInTree(key, root.left);
        //         if (depthInLeft != -1) {
        //             return depthInLeft + 1;
        //         }
        //     }
        
        //     if (root.right != null) {
        //         int depthInRight = depthInTree(key, root.right);
        //         if (depthInRight != -1) {
        //             return depthInRight + 1;
        //         }
        //     }
        
        //     return -1;    // not found in either subtree
        // }
        if  (key == root.key) {
            return 0; //Found
        }
        else if (key < root.key && root.left != null) { //Exploring the left subtree
            int depthInLeft = depthInTree(key, root.left);
            if (depthInLeft != -1) {
                return depthInLeft + 1;
            }
        }
        else if (key >= root.key && root.right != null) { //Exploring the right subtree
            int depthInRight = depthInTree(key, root.right);
            if (depthInRight != -1) {
                return depthInRight + 1; 
            }
        }
        
        return -1;
    }

    public int sumEvens () {
        /*If the tree is empty, the result should be zero */
        int sum = 0;

        /*If the tree is non empty, we call sumEvensInTree recursively */
        if (root != null) {
            sum = sumEvensInTree(root);
        }
        return sum;
    }

    private static int sumEvensInTree (Node root) {
        int sum = 0;
        if (root.key % 2 == 0) {
            sum = sum + root.key;
        }

        if (root.left != null) {
            sum = sum + sumEvensInTree(root.left);
        }

        if (root.right != null) {
            sum = sum + sumEvensInTree(root.right);
        }
        return sum;
    }

    public int depthIter (int key) {

        if (root == null) {
            return -1;
        }

        Node trav = root;
        int depth = 0;

        while (key != trav.key && !(trav.left == null && trav.right == null)) {
            if ( key < trav.key && trav.left != null) {
                depth ++;
                trav = trav.left;
            }
            else if (key > trav.key && trav.right != null) {
                depth ++;
                trav = trav.right;
            }
            else if (trav.left != null) {
                trav = trav.left;
            }
            else if (trav.right != null) {
                trav = trav.right;
            }
        }

        if (key == trav.key) {
            return depth;
        }
        
        return -1;
    }

    public int deleteMax() {
        if (root == null) {
            return -1;
        }

        Node parent = null;
        Node trav = root;

        while (trav.right != null) {
            parent = trav;
            trav = trav.right;
        }

        int max = trav.key;

        if (parent == null && trav.left == null) {
            /*The root of the node is the Maximum and has no children*/
            root = null;
            return -1;
        }
        if (parent != null && trav.left != null) {
            /*The maximum has a left */
            parent.right = trav.left;
        }
        else if (parent == null && trav.left != null) {
            /*The root is the maximum, and only has a left subtree */
            root = trav.left;
        }
        else if (trav.left == null) {
            /*The max is a Leaf node */
            parent.right = null;
        }
        // else {
        //     parent.right = trav.left;
        // }

        return max;

    }
    
    public static void main(String[] args) {

        try {

            //Legacy Tests
            System.out.println("--- Testing depth()/depthInTree() from Problem 4 ---");
            System.out.println();
            System.out.println("(0) Testing on tree from Problem 6, depth of 13");
            LinkedTree tree = new LinkedTree();
            int[] keys = {37, 26, 42, 13, 35, 56, 30, 47, 70};
            tree.insertKeys(keys);
            
            int results = tree.depth(13);
            System.out.println("actual results:");
            System.out.println(results);
            System.out.println("expected results:");
            System.out.println(2);
            System.out.print("MATCHES EXPECTED RESULTS?: ");
            System.out.println(results == 2);

            /*New tests: For each test, we create a separate set of test data 
             * So the test can be independent from each other
            */
            System.out.println("\n\nProblem 6.1 TEST 1 BEGIN --------------------------------");
            System.out.println("Testing the function sumEvens(), that calculates the sum of even keys in a tree\n");

            LinkedTree tree611 = new LinkedTree();
            System.out.println("sumEvens() for an empty tree: " + tree611.sumEvens());
            int[] keys611 = {4, 1, 3, 6, 5, 2};
            tree611.insertKeys(keys611);
            System.out.println("Newly created tree, with keys being the integers from 1 to 6, inclusive, printed in levelOrder");
            tree611.levelOrderPrint();
            
            int results611 = tree611.sumEvens();
            System.out.println("Actual Results: ");
            System.out.println(results611);
            System.out.println("Expected Results: ");
            System.out.println(12);
            System.out.print("MATCHES EXPECTED RESULTS?: ");
            System.out.println(results611 == 12);

            System.out.println("\n\nProblem 6.1 TEST 2 BEGIN --------------------------------");
            System.out.println("Testing the function sumEvens(), that calculates the sum of even keys in a tree\n");
            LinkedTree tree612 = new LinkedTree();
            System.out.println("sumEvens() for an empty tree (Should be 0): " + tree612.sumEvens());
            int[] keys612 = {-4, 18, 3, -10, 32, 0, 55, 67, 2};
            tree612.insertKeys(keys612);
            System.out.println("Newly created random tree, printed in levelOrder");
            tree612.levelOrderPrint();

            int results612 = tree612.sumEvens();
            System.out.println("Actual Results: ");
            System.out.println(results612);
            System.out.println("Expected Results: ");
            System.out.println(38);
            System.out.print("MATCHES EXPECTED RESULTS?: ");
            System.out.println(results612 == 38);

            System.out.println("\n\nProblem 6.2 TEST 1 BEGIN --------------------------------");

            LinkedTree tree621 = new LinkedTree();
            System.out.println("Depth of a key in an empty Tree: " + tree621.depthIter(13) + " , Expected : " + -1 + ", Match? " + (tree621.depthIter(13) == -1));
            
            int[] keys621 = {37, 26, 42, 13, 35, 56, 30, 47, 70};
            tree621.insertKeys(keys621);
            System.out.println("\nNewly created Tree:");
            tree621.levelOrderPrint();
            System.out.println("");
            System.out.println("depth of 13: " + tree621.depthIter(13) + " , Expected : " + 2 + ", Match? " + (tree621.depthIter(13) == 2));
            System.out.println("depth of 37: " + tree621.depthIter(37) + " , Expected : " + 0 + ", Match? " + (tree621.depthIter(37) == 0));
            System.out.println("depth of 47: " + tree621.depthIter(47) + " , Expected : " + 3 + ", Match? " + (tree621.depthIter(47) == 3));
            System.out.println("depth of 50: " + tree621.depthIter(50) + " , Expected : " + -1 + ", Match? " + (tree621.depthIter(50) == -1));

            
            System.out.println("\n\nProblem 6.2 TEST 2 BEGIN --------------------------------");

            LinkedTree tree622 = new LinkedTree();
            System.out.println("Depth of a key in an empty Tree: " + tree622.depthIter(13) + " , Expected : " + -1 + ", Match? " + (tree622.depthIter(13) == -1));
            
            int[] keys622 = {10, -1, 7, 29, 45, 51, 18};
            tree622.insertKeys(keys622);
            System.out.println("\nNewly created Tree:");
            tree622.levelOrderPrint();
            System.out.println("");
            System.out.println("depth of 13: " + tree622.depthIter(13) + " , Expected : " + -1 + ", Match? " + (tree622.depthIter(13) == -1));
            System.out.println("depth of 37: " + tree622.depthIter(10) + " , Expected : " + 0 + ", Match? " + (tree622.depthIter(10) == 0));
            System.out.println("depth of 47: " + tree622.depthIter(45) + " , Expected : " + 2 + ", Match? " + (tree622.depthIter(45) == 2));
            System.out.println("depth of 50: " + tree622.depthIter(51) + " , Expected : " + 3 + ", Match? " + (tree622.depthIter(51) == 3));

            System.out.println("\n\nProblem 6.3 TEST 1 BEGIN --------------------------------");

            LinkedTree tree631 = new LinkedTree();
            System.out.println("empty tree: (Should be -1)" + tree631.deleteMax());
            System.out.println();
            
            int[] keys631 = {37, 26, 42, 13, 35, 56, 30, 47, 70};
            tree631.insertKeys(keys631);
            tree631.levelOrderPrint();
            System.out.println();
            
            System.out.println("first deletion: " + tree631.deleteMax());
            tree631.levelOrderPrint();
            System.out.println();
            
            System.out.println("second deletion: " + tree631.deleteMax());
            tree631.levelOrderPrint();

            System.out.println("\n\nProblem 6.3 TEST 2 BEGIN --------------------------------");

            LinkedTree tree632 = new LinkedTree();
            System.out.println("empty tree: (Should be -1)" + tree632.deleteMax());
            System.out.println();
            
            int[] keys632 = {156, 35, -41, 20, 0, 100, -15, 63, 7};
            tree632.insertKeys(keys632);
            tree632.levelOrderPrint();
            System.out.println();
            
            System.out.println("first deletion: " + tree632.deleteMax());
            tree632.levelOrderPrint();
            System.out.println();
            
            System.out.println("second deletion: " + tree632.deleteMax());
            tree632.levelOrderPrint();


            System.out.println("\n\nProblem 7 TEST 1 BEGIN --------------------------------");
            LinkedTree tree700 = new LinkedTree();
            int[] keys700 = {59, 32, -27, 11, 121, 0, 29, -35, 66};
            tree700.insertKeys(keys700);
            System.out.println("\nLevel order print of a newly created tree");
            tree700.levelOrderPrint();

            System.out.println("\nNow printing using the inorderIterator, with a prefix before");
        

            LinkedTreeIterator iter700 = tree700.inorderIterator();
            while (iter700.hasNext()) {
                int key = iter700.next();
            
                // do something with key
                System.out.println("The key is : " + key);
            }        
        
            System.out.println("\n\nProblem 7 TEST 2 BEGIN --------------------------------");
            LinkedTree tree701 = new LinkedTree();
            int[] keys701 = {-25, 36, 44, 1, 7, 0, 39, 12, 88};
            tree701.insertKeys(keys701);
            System.out.println("\nLevel order print of a newly created tree");
            tree701.levelOrderPrint();

            System.out.println("\nNow printing using the inorderIterator, with a prefix before");
        

            LinkedTreeIterator iter701 = tree701.inorderIterator();
            while (iter701.hasNext()) {
                int key = iter701.next();
            
                // do something with key
                System.out.println("The key is : " + key);
            }        
        
            
            
            System.out.println("\n\nProblem 8 TEST 1 BEGIN --------------------------------");
            //Creating separately the keys and the dataItems array
            int[] keys800 = {26, 37, -42, 13, 35, 56, -30, 47, 70};
            String[] dataItems800 = new String[keys800.length];

            //Populating the data fields
            for (int i = 0; i < keys800.length; i++) {
                dataItems800[i] = new String("data for key " + keys800[i]);
            }

            LinkedTree tree800 = new LinkedTree(keys800, dataItems800);

            //Testing using Level Order Print
            System.out.println("\nPrinting the newly constructed tree using level order print");
            tree800.levelOrderPrint();

            System.out.println("\nPrinting the newly constructed tree using inorder iterator, With a prefix text before");
            LinkedTreeIterator iter800 = tree800.inorderIterator();
            while (iter800.hasNext()) {
                int key = iter800.next();
            
                // do something with key
                System.out.println("The key is: " + key);
            }        

            System.out.println("\n\nProblem 8 TEST 2 BEGIN --------------------------------");
            int[] keys801 = {10, 8, 4, 2, 15, 12, 7};
            String[] dataItems801 = {"d10", "d8", "d4", "d2", "d15", "d12", "d7"};
            LinkedTree tree801 = new LinkedTree(keys801, dataItems801);
            tree801.levelOrderPrint();

            
            System.out.println("\nPrinting the newly constructed tree using inorder iterator, With a prefix text before");
            LinkedTreeIterator iter801 = tree801.inorderIterator();
            while (iter801.hasNext()) {
                int key = iter801.next();
            
                // do something with key
                System.out.println("The key is: " + key);
            }        


        } catch (Exception e) {
            System.out.println("INCORRECTLY THREW AN EXCEPTION: " + e);
        }
        
        System.out.println();    // include a blank line between tests
        
        /*
         * Add at least two unit tests for each method from Problem 6.
         * Test a variety of different cases. 
         * Follow the same format that we have used above.
         * 
         * IMPORTANT: Any tests for your inorder iterator from Problem 7
         * should go BEFORE your tests of the deleteMax method.
         */

        
    }
}
