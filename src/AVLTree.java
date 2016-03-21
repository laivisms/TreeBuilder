import java.util.ArrayList;
/**
 * The AVLTree CLass, extends BinarySearchTree - Takes a list of comparable nodes from .txt file, inserts them into a
 *                                       AVLTree object in one after the other, in the order which they are received, 
 *                                       then deletes a list of nodes from the same text file, in the same order which they
 *                                       are received. After every insertion and deletion, the AVL balance of all relevant 
 *                                       nodes are updated and checked, and the tree is re-balanced if necessary.
 *                                       
 *                                       The .txt file should be formatted as follows:
 *                                       1st line: String or int, depending on what type will be stored in the nodes.
 *                                       2nd line: Strings or ints to construct tree from, separated by quotations or spaces, 
 *                                                 respectively.
 *                                       3rd line: Strings or ints to be deleted, formatted same as line 2.
 *                                       
 *                                       The AVLTree can be used with any type of comparable, but the main function is only 
 *                                       compatable with string or int types, read from a .txt file.
 *                                       
 *                                       The Main function reads the file, adds the nodes to be added and to be deleted into
 *                                       ArrayLists, adds the nodes to be added into a BinarySearchTree using the insert
 *                                       algorithm, checking the balance of relevant nodes and re-balancing where needed.
 *                                       It then deletes the nodes to be deleted using the delete algorithm, re-balancing 
 *                                       where needed.
 *                                       The tree is displayed in a separate window both before and after the nodes are deleted.
 *                                       The first node in the drop down display is the left node, the second is the right node.
 * @author Laivi Malamut-Salvaggio
 *
 * @param <E> an object which implements comparable
 */

public class AVLTree<E extends Comparable<E>> extends BinarySearchTree<E> {
	public static void main(String args[]){
		fileToUse = "src/testAVLString.txt";
		if(args.length != 0 && args[0] != null){
			fileToUse = args[0];
		}
		String order = readFile();
		if(noFile){ // use default list of nodes
			String add = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 20 12";
			String delete = "20 8 16 12 10 12";
			ArrayList<Integer> toAdd = new ArrayList<Integer>(); 
			intOrderAdder(add, toAdd); // create arraylist of nodes to be added
			ArrayList<Integer> toDelete = new ArrayList<Integer>();
			intOrderAdder(delete, toDelete);// create arraylist of nodes to be deleted
			AVLTree<Integer> myTree = new AVLTree<Integer>();
			myTree.insertList(toAdd); //add the nodes
			name = "AVLTree Before Deletion";
			myTree.showTree(); // show the tree
			myTree.deleteList(toDelete); //delete the nodes
			name = "AVLTree After Deletion";
			myTree.showTree();// show the tree again
		}
		else if(string){
			ArrayList<String> toAdd = new ArrayList<String>();
			stringOrderAdder(order.substring(0,orderSize), toAdd);
			ArrayList<String> toDelete = new ArrayList<String>();
			stringOrderAdder(order.substring(orderSize, order.length()), toDelete);
			AVLTree<String> myTree = new AVLTree<String>();
			myTree.insertList(toAdd);
			name = "AVLTree Before Deletion";
			myTree.showTree();
			myTree.deleteList(toDelete);
			name = "AVLTree After Deletion";
			myTree.showTree();
		}
		else{
			ArrayList<Integer> toAdd = new ArrayList<Integer>();
			intOrderAdder(order.substring(0,orderSize), toAdd);
			ArrayList<Integer> toDelete = new ArrayList<Integer>();
			intOrderAdder(order.substring(orderSize, order.length()), toDelete);
			AVLTree<Integer> myTree = new AVLTree<Integer>();
			myTree.insertList(toAdd);
			name = "AVLTree After Insertion";
			myTree.showTree();
			myTree.deleteList(toDelete);
			name = "AVLTree After Deletion of " + order.substring(orderSize, order.length());
			myTree.showTree();
		}
	}
	
	/**
	 * deleteList - Repeatedly calls delete function on every element in an ArrayList of type E. 
	 * 
	 * @param toDelete The ArrayList containing the elements of type comparable to be deleted
	 */
	public void deleteList(ArrayList<E> toDelete){
		for (E i : toDelete){
			delete(i);
		}
	}
	
	/**
	 * insertList - Repeatedly calls insert function on every element in an ArrayList of type E.
	 * 
	 * @param toInsert The ArrayList containing the elements of type comparable  to be inserted
	 */
	public void insertList(ArrayList<E> toInsert){
		for (E i : toInsert){
			insert(i);
		}
	}
	


	/**
	 * Class AVLNode for AVLTree class, extends SearchNode, Implements Comparable. Only difference between AVLNode 
	 *              and SerchNode is that AVLNode has the field "difference", which contains the int which is the 
	 *              difference between the height of the node's left subtree and the height of its right subtree.
	 * 
	 * 
	 * @author Laivi Malamut-Salvaggio
	 *
	 * @param <V> An object which implements Comparable
	 */
	protected class AVLNode<V extends Comparable<V>> extends SearchNode<V> implements Comparable<SearchNode<V>>{
		protected int difference; // balance of this node

		public AVLNode(V data) {
			super(data);
		}

	}
	
	/**
	 * delete Function - Searches for the node in the tree containing the specified data, then uses the delete algorithm from
	 * 					 BinarySearchTree, which itself is adapted from the CLRS book's "delete" algorithm for BST Trees.
	 * 				     The delete function of class BinarySearchTree returns the node which replaced the node which was
	 * 					 "squeezed out." It is then determined from this node where to begin updating nodes' balances and
	 * 					 to determine which subtrees, if any, require re-balancing:
	 * 					 
	 * 				squeezedOut == null CASE:
	 * 					 If the node returned was null, then one of two things could have happened. 1) the node deleted was a
	 * 					 leaf, and was replaced by null. In this case the node at which to begin checking balances is the parent
	 * 					 of the node which was deleted. 2) The deleted node was replaced by a leaf, and the leaf was "squeezed
	 * 					 out" by null. In this case, the node at which to begin checking balances is the minimum of the 
	 * 				     deleted node's right subtree, which is the squeezed out node's parent, 
	 *  				 since the delete algorithm replaces the deleted node with its successor.
	 *  
	 *  			squeezedOut != null CASE:
	 *  				 If the delete algorithm returned a value, one of two things could have happened. 1) the node was 
	 *  				 replaced by the treeMinimum method. To determine the new balance of the node which replaced the
	 *  				 deleted node, access must be gained to it by finding the new parent of the deleted node's left child.
	 *  				 2) the node was replaced by either its left or right child directly, since it had no right or left child,
	 *  				 respectively. In this case, the node which replaced the deleted node is node "squuezedOut", so access 
	 *  				 to it is already attained.
	 *  				 In both of these cases, the node at which to begin checking balances is "squeezedOut"'s parent.
	 * 					
	 * 					 
	 * @param nodeData The data contained in the node which is to be deleted
	 */
	public void delete(E nodeData){
		AVLNode<E> node = (AVLNode<E>)search((SearchNode<E>)root, nodeData);
		if(node == null)// safeguard against client attempting to delete node which does not exist
			return;
		String child = "right";
		if(node.parent != null && node.equals(node.parent.left))
			child = "left";
		AVLNode<E> squeezedOut = (AVLNode<E>)super.delete(node);
		if(squeezedOut == null){ //NULL CASE
			if(node.left == null){ // Sub Case 1) a leaf was deleted
				deletionRebalance((AVLNode<E>)node.parent, child);
			}
			else{ //Sub Case 2) the successor was squeezed out, but had no right child no replace it
				AVLNode<E> min = (AVLNode<E>) treeMinimum((SearchNode<E>)node.right);
				((AVLNode<E>)((SearchNode<E>)node.left).parent).difference = node.difference;
				if(((AVLNode<E>) node.left).parent.equals(node.right)) // node's right child replaced it, Height reduction comes from right
					deletionRebalance((AVLNode<E>)node.right, "right");
				else // node's right child did not replace it, its height was reduced from its left child
					deletionRebalance((AVLNode<E>)min, "left");
			}
		}
		else{ //squeezedOut != null CASE
			if(node.left != null && node.right != null) // Sub Case 1) if node was replaced by min method
				((AVLNode<E>)((SearchNode<E>)node.left).parent).difference = node.difference; 
			//else Sub Case 2) node was replaced with either its left or right child, so the node that replaced it IS the squeezedOut node.
			// its difference stays the same
			if(squeezedOut.equals(squeezedOut.parent.left)) // if squeezedOut is left child
				deletionRebalance((AVLNode<E>)squeezedOut.parent, "left");
			else // squeezedOut is right child
				deletionRebalance((AVLNode<E>)squeezedOut.parent, "right");
		}
	}
	
	/**
	 * deletionRebalance Function - Takes a node and a string saying which of that node's subtrees' heights was reduced by
	 * 								the deletion, then updates differences accordingly. Three cases could occur in a deletion:
	 * 
	 * 								Case 1) The decrease in height of the subtree also affected the current node's height(i.e, 
	 * 										the node was heavy on that side, and the decrease in height made its difference
	 * 										become zero). In such a case, no balancing is yet required, but it is required
	 * 										to continue up the tree by recursing on the node's parent
	 * 								Case 2) The decrease in height did not affect the node's height, since it has a difference
	 * 										of zero already. The change only slightly imbalances the node, and it is not 
	 *                                      necessary to continue up the tree
	 *                              Case 3) The decrease in height caused the node to become to imbalanced, and re-balancing is
	 *                              		necessary. It then determines which rotations are need, performs them, updates
	 *                              		the balances of all nodes concerned, and continues up the tree if necessary. (it
	 *                                      is necessary to continue up the tree if the difference of the node which replaced
	 *                                      the current node becomes 0. This is so because then the height of this subtree changed
	 *                                      from either 1 or -1 to zero, thus affecting the height of the subtree's parent. If it 
	 *                                      changed from 1 to -1 or from -1 to 1, the parents height remains the same)
	 * 										
	 * 
	 * @param node
	 * @param previous
	 */
	private void deletionRebalance(AVLNode<E> node, String previous){
		if(node != null){
			if(previous.equals("left")){ // case for if node was removed from left subtree of this node
				if(node.difference == 1){ //CASE 1) the decrease affected the subtree's height, need to move up to parent
					node.difference = 0;
					if(node.parent != null){ // if node != root. if it does, we are finished.
						if(node.equals(node.parent.left))
							deletionRebalance((AVLNode<E>)node.parent, "left");
						else
							deletionRebalance((AVLNode<E>)node.parent, "right");
					}
				}
				else if(node.difference == 0){ // CASE 2) the decrease did not affect height, only made tree imbalanced slightly, no recurse
					node.difference = -1;
				}
				else{ // CASE 3) the decrease imbalanced the subtree, need to rebalance
					if(((AVLNode<E>)node.right).difference == 1){ // the right child is left heavy, need right left rebalance
						rightLeft((AVLNode<E>)node.right);
						if(node.parent.parent != null){
							if(node.parent.parent.left.equals(node.parent))
								deletionRebalance((AVLNode<E>)node.parent.parent, "left");
							else// node to move up to has node.parent as its right child
								deletionRebalance((AVLNode<E>)node.parent.parent, "right");
						}
					}
					else{ // right child is right heavy, or balanced. need left rotation
						if(((AVLNode<E>)node.right).difference == -1){ // will change to zero, affecting difference of parent of this subtree
							node.difference = 0;
							((AVLNode<E>)node.right).difference = 0;
							rotateLeft(node);
							if(node.parent.parent != null){
								if(node.parent.parent.left.equals(node.parent))
									deletionRebalance((AVLNode<E>)node.parent.parent, "left");
								else// node to move up to has node.parent as its right child
									deletionRebalance((AVLNode<E>)node.parent.parent, "right");
							}
						}
						else{// if node.right.difference == 0 (did not affect difference of parent of this subtree)
							((AVLNode<E>)node.right).difference = 1;
							rotateLeft(node);
						}
					}
				}
			}
			else{// if node was removed from this node's right subtree, symmetric to the left case
				if(node.difference == -1){ // CASE 1) the decrease affected the subtree's height, need to move up to parent
					node.difference = 0;
					if(node.parent != null){
						if(node.equals(node.parent.left))
							deletionRebalance((AVLNode<E>)node.parent, "left");
						else
							deletionRebalance((AVLNode<E>)node.parent, "right");
					}
				}
				else if(node.difference == 0){ // CASE 2) the decrease did not affect height, only made tree imbalanced slightly, no recurse
					node.difference = 1;
				}
				else{ //  CASE 3) the decrease imbalanced the subtree, need to rebalance
					if(((AVLNode<E>)node.left).difference == -1){ // the left child is right heavy, need to left right rebalance
						leftRight((AVLNode<E>)node.left);
						if(node.parent.parent != null){
							if(node.parent.parent.left.equals(node.parent))
								deletionRebalance((AVLNode<E>)node.parent.parent, "left");
							else// node to move up to has node.parent as its right child
								deletionRebalance((AVLNode<E>)node.parent.parent, "right");
						}
					}
					else{ // left child is left heavy, or balanced. need right rotation
						if(((AVLNode<E>)node.left).difference == 1){ // will change to zero, affecting difference of parent of this subtree
							node.difference = 0;
							((AVLNode<E>)node.left).difference = 0;
							rotateRight(node);
							if(node.parent.parent != null){
								if(node.parent.parent.left.equals(node.parent))
									deletionRebalance((AVLNode<E>)node.parent.parent, "left");
								else// node to move up to has node.parent as its right child
									deletionRebalance((AVLNode<E>)node.parent.parent, "right");
							}
						}
						else{// if node.left.difference == 0 (did not affect difference of parent of this subtree)
							((AVLNode<E>)node.left).difference = -1;
							rotateRight(node);
						}
					}
				}
			}
		}
	}
	
	
    /**
     * insert Function - takes data of type E, inserts it into a new node, inserts the node into the tree using BinarySearchTree's
     * 					 insert function, then updates and re-balances all the ancestors of the inserted node (where necessary).
     * @param node Object of type Comparable to be inserted into tree
     */
	public void insert(E node){
		AVLNode<E> newNode = new AVLNode<E>(node);
		super.insert(newNode);
		insertionRebalance(newNode, null);
	}
    /**
     * insertionRebalance Function - Takes a node whose difference is correct, but whose parent's difference might have been 
     * 								 affected by the insertion. Also takes the node which is the child of the current node
     * 								 and ancestor of the inserted node. This reference is needed for rotation purposes, to
     * 								 know from which child the node's height increased. Three cases may occur:
     * 
     * 								 Case 1) The change affected the height, but did not imbalance it. Must update the current
     * 										 node's difference and continue up the tree using recursion.
     * 								 Case 2) The change did not affect height, made tree more balanced by making current node's
     * 										 difference become zero. No recursion necessary.
     * 								 Case 3) The Change imbalanced the tree, re-balancing is necessary.
     * 										 
     * @param node
     * @param previous
     */
	private void insertionRebalance(AVLNode<E> node, AVLNode<E> previous){
		if(node.parent != null){
			if (((AVLNode<E>)node.parent).difference == 0){ // Case 1) Change did affect height. change height of parent, then call on parent
				if(node.equals(node.parent.left)){
					((AVLNode<E>)node.parent).difference++;
				}
				else{
					((AVLNode<E>)node.parent).difference--;
				}
				insertionRebalance((AVLNode<E>)node.parent, node);
			}
			else if((((AVLNode<E>)node.parent).difference == -1 && node.equals(node.parent.left)) ||   // Case 2) change did not affect height, tree is more balanced
					((AVLNode<E>)node.parent).difference == 1 && node.equals(node.parent.right)){
				((AVLNode<E>)node.parent).difference = 0;
			}

			else{ // Case 3) change affected height too much, need rebalancing
				AVLNode<E> parent = (AVLNode<E>)node.parent;
				if(((AVLNode<E>)node.parent).difference == 1){ // if change made tree left heavy
					if(previous.equals(node.right)){ // need left right rebalance
						leftRight(node);
					}
					else{ // the new node was inserted into left subtree, left left rebalance
						rotateRight(parent);
						parent.difference = 0;
						node.difference = 0;
					}
				}
				else{//change made tree right heavy
					if(previous.equals(node.left)){ // need right left rebalance
						rightLeft(node);
					}
					else{// new node was inserted into right subtree, right right rebalance
						rotateLeft(parent);
						parent.difference = 0;
						node.difference = 0;
					}
				}
			}


		}
	}
	
	/**
	 * left-right rotation, symmetric to right-left rotation. Rotates node left, then rotates node's original parent right.
	 * 
	 * @param node The node to rotate left
	 */
	private void leftRight(AVLNode<E> node){
		AVLNode<E> parent = (AVLNode<E>)node.parent;
		AVLNode<E> previous = (AVLNode<E>) node.right;
		rotateLeft(node);
		rotateRight(parent);
		if(previous.difference > 0){
			node.difference = 0;
			parent.difference = -1;
		}
		else if(previous.difference == 0){ // previous is node just inserted
			node.difference = 0;
			parent.difference = 0;
		}
		else{ // previous difference is negative
			node.difference = 1;
			parent.difference = 0;
		}
		previous.difference = 0;
	}
	
	/**
	 * right-left rotation, symmetric to left-right rotation. Rotates node right, then rotatesnode's original parent left.
	 * 
	 * @param node The node to rotate right 
	 */
	private void rightLeft(AVLNode<E> node){
		AVLNode<E> parent = (AVLNode<E>)node.parent;
		AVLNode<E> previous = (AVLNode<E>)node.left;
		rotateRight(node);
		rotateLeft(parent);
		if(previous.difference > 0){
			node.difference = -1;
			parent.difference = 0;
		}
		else if(previous.difference == 0){ // previous is node just inserted
			node.difference = 0;
			parent.difference = 0;
		}
		else{// previous difference is negative
			node.difference = 0;
			parent.difference = 1;
		}
		previous.difference = 0;
	}
	/**
	 * Left Rotate algorithm, adapted from clrs book, chapter 13.2. Assumes right child of "node" exists.
	 * 
	 * @param node The node to rotate
	 */
	private void rotateLeft(AVLNode<E> node){            
		AVLNode<E> right = (AVLNode<E>)node.right;       
		node.right = right.left;
		if(right.left != null)
			((AVLNode<E>)right.left).parent = node;

		right.parent = node.parent;

		if(node.parent == null)
			root = right;

		else if(node == node.parent.left)
			node.parent.left = right;

		else
			node.parent.right = right;
		right.left = node;
		node.parent = right;
	}
	/**
	 * Right Rotate Algorithm, adapted from left-rotate algorithm is clrs textbook, chapter 13.2. Assumes left child
	 * of "node" exists.
	 * 
	 * @param node The node to rotate
	 */
	private void rotateRight(AVLNode<E> node){                
		AVLNode<E> left = (AVLNode<E>)node.left;            
		node.left = left.right;
		if(left.right != null)
			((AVLNode<E>)left.right).parent = node;
		left.parent = node.parent;

		if(node.parent == null)
			root = left;
		else if(node == node.parent.left)
			node.parent.left = left;
		else
			node.parent.right = left;
		left.right = node;
		node.parent = left;
	}
}