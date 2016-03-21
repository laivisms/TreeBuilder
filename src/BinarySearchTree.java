import java.util.ArrayList;

/**
 * The BinarySearchTree class, extends BinaryTree. Takes a list of comparable nodes from .txt file, inserts them into a
 *                                       BinarySearchTree object in one after the other, in the order which they are received, 
 *                                       then deletes a list of nodes from the same text file, in the same order which they
 *                                       are received. 
 *                                       
 *                                       The .txt file should be formatted as follows:
 *                                       1st line: String or int, depending on what type will be stored in the nodes.
 *                                       2nd line: Strings or ints to construct tree from, separated by quotations or spaces, 
 *                                                 respectively.
 *                                       3rd line: Strings or ints to be deleted, formatted same as line 2.
 *                                       
 *                                       Main function reads file, adds the nodes to be added and to be deleted into
 *                                       arraylists, adds the nodes to be added into a BinarySearchTree using the insert
 *                                      algorithm, then deletes the nodes to be deleted using the delete algorithm.
 *                                      The tree is displayed in a separate window both before and after the nodes are deleted.
 * @author Laivi Malamut-Salvaggio
 * @version 1.0
 *
 * @param <E> an object which implements comparable
 */

public class BinarySearchTree<E extends Comparable<E>> extends BinaryTree<E>{
	
	
	public static void main(String args[]){
		fileToUse = "testBSTString.txt";
		if(args.length != 0){
			if(args[0] != null) {
				fileToUse = args[0];
			}
		}
		String order = readFile();
		if(noFile){ // use default list of nodes
			String add = "1 2 3 4 5 6 7 8 9";
			String delete = "2 4 6 7";
			ArrayList<Integer> toAdd = new ArrayList<Integer>(); 
			intOrderAdder(add, toAdd); // create arraylist of nodes to be added
			ArrayList<Integer> toDelete = new ArrayList<Integer>();
			intOrderAdder(delete, toDelete);// create arraylist of nodes to be deleted
			BinarySearchTree<Integer> myTree = new BinarySearchTree<Integer>();
			myTree.insertList(toAdd); //add the nodes
			name = "BST Before Deletion";
			myTree.showTree(); // show the tree
			myTree.deleteList(toDelete); //delete the nodes
			name = "BST After Deletion";
			myTree.showTree();// show the tree again
		}
		else if(string){
			ArrayList<String> toAdd = new ArrayList<String>();
			stringOrderAdder(order.substring(0,orderSize), toAdd);
			ArrayList<String> toDelete = new ArrayList<String>();
			stringOrderAdder(order.substring(orderSize, order.length()), toDelete);
			BinarySearchTree<String> myTree = new BinarySearchTree<String>();
			myTree.insertList(toAdd);
			name = "BST Before Deletion";
			myTree.showTree();
			myTree.deleteList(toDelete);
			name = "BST After Deletion";
			myTree.showTree();
		}
		else{
			ArrayList<Integer> toAdd = new ArrayList<Integer>();
			intOrderAdder(order.substring(0,orderSize), toAdd);
			ArrayList<Integer> toDelete = new ArrayList<Integer>();
			intOrderAdder(order.substring(orderSize, order.length()), toDelete);
			BinarySearchTree<Integer> myTree = new BinarySearchTree<Integer>();
			myTree.insertList(toAdd);
			name = "BST Before Deletion";
			myTree.showTree();
			myTree.deleteList(toDelete);
			name = "BST After Deletion";
			myTree.showTree();
		}
	}
	
	/*
	 * Repeatedly calls the delete function on this tree for every element in arraylist toDelete. Must first find the
	 * corresponding searchNode using search to reference it to be deleted.
	 */
	public void deleteList(ArrayList<E> toDelete){
		for(E i : toDelete){
			SearchNode<E> tempNode = search((SearchNode<E>) root, i);
			if(tempNode != null)
				delete(tempNode);
		}
	}
	
	/*
	 * Repeatedly calls the insert function on this tree for every element in arraylist toInsert
	 */
	public void insertList(ArrayList<E> toInsert){
		for (E i : toInsert){
			insert(new SearchNode<E>(i));
		}
	}
	
	/**
	 * Class SearchNode for BinarySearchTree class, extends Node, implements comparable. Only differences between SearchNode
	 *                  and Node is that the SearchNode's data and the SearchNode itself must implement comparable.
	 *
	 * @param <V> an object which implements comparable.
	 */
	protected class SearchNode<V extends Comparable<V>> extends Node<V> implements Comparable<SearchNode<V>>{

		protected SearchNode<V> parent;
		
		public SearchNode(V data) {
			super(data);
			
		}

		@Override
		public int compareTo(SearchNode<V> node) {
			return this.data.compareTo(node.data);
		}

	}
	
	/*
	 * The insert algorithm, copied from CLRS textbook, converted to java.
	 */
	protected void insert(SearchNode<E> node){
		SearchNode<E> parent = null;
		SearchNode<E> child = (SearchNode<E>)root;
		while (child != null){
			parent = child;
			if (node.data.compareTo(child.data) < 1){
				child = (SearchNode<E>) child.left;
			}
			else 
				child = (SearchNode<E>) child.right;
		}
		node.parent = parent;
		if (parent == null){
			root = node;
		}
		else if(node.data.compareTo(parent.data) < 1){
			parent.left = node;
		}
		else
			parent.right = node;
	}
	
	/*
	 * The search algorithm, copied from CLRS textbook, converted to java. key must be conatined somewhere in the tree
	 */
	public SearchNode<E> search(SearchNode<E> compare, E key){
		if (compare == null || key.equals(compare.data)){
			return compare;
		}
		if (key.compareTo(compare.data)< 1){
			return search((SearchNode<E>) compare.left, key);
		}
		else{
			return search((SearchNode<E>) compare.right, key);
		}
	}
	
	/**
	 * The delete algorithm, copied from CLRS textbook, converted to java. 
	 * if the left child place of node is empty, node can be replaced by its right child easily
	 * if the right child place of node is empty, node can be replaced by its left child easily
	 * if neither child place is empty, finds the minimum node of the right subtree of node, replaces the min node with its right
	 * child, then replaces node with the min node
	 * 
	 * @param node The node to delete
	 * @return The node which replaced the node which was "squeezed out"
	 */
	public Node<E> delete(SearchNode<E> node){
		Node<E> result;
		if (node.left == null){
			result = node.right;
			transplant(node, (SearchNode<E>) node.right);
		}
		else if(node.right == null){
			result = node.left;
			transplant(node, (SearchNode<E>) node.left);
		}
		else {
			SearchNode<E> minimum = treeMinimum((SearchNode<E>)node.right);
			result = minimum.right;
			if (minimum.parent != node){
				transplant(minimum, (SearchNode<E>) minimum.right);
				minimum.right = node.right;
				((SearchNode<E>) minimum.right).parent = minimum;
			}
			transplant(node, minimum);
			minimum.left = node.left;
			((SearchNode<E>) minimum.left).parent = minimum;
		}
		return result;
			
	}
	
	/*
	 * The minimum algorithm, copied from CLRS textbook, converted to java. The node found if continuously going to
	 *                        the left is the smallest node.
	 */
	protected SearchNode<E> treeMinimum(SearchNode<E> node) {
	while (node.left != null)
	    node = (SearchNode<E>) node.left;

	return node;
    }
	
	/*
	 * The transplant algorithm, copied from CLRS textbook, converted to java. replaces the node to be replaced's parent's
	 * reference to refer to the replacer node.
	 */
	private void transplant(SearchNode<E> toBeReplaced, SearchNode<E> replacer){
		if(toBeReplaced.parent == null)
			root = replacer;
		
		else if(toBeReplaced == toBeReplaced.parent.left)
			toBeReplaced.parent.left = replacer;
		
		else
			toBeReplaced.parent.right = replacer;
		
		if(replacer != null)
			replacer.parent = toBeReplaced.parent;
	}

}
