import java.util.ArrayList;
import java.lang.Integer;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * The BinaryTree Class - Converts the preorder and postorder of a binary tree from a .txt file into a data structure 
 *                        consisting of a Node object which is the root of the tree and which contains references to the 
 *                        label of the root node and the root's right and left child, both of which are Node objects 
 *                        themselves. Function showTree() can be called on a BinaryTree object to display a visual 
 *                        representation of the binary tree in a new window. If no file is found, or there is an error in 
 *                        reading the file, the preorder and inorder are built from the fields PreString and InString,
 *                        respectively. 
 *                        
 *                        The .txt file should be formatted as follows:
 *                        1st line: The word String or int, depending on what type of data will be stored in the nodes
 *                        2nd line: The inorder representation of the tree. ints should be seperated by spaces, Strings 
 *                                  by quotation marks
 *                        3rd line: The preorder representation of the tree, spaced the same way as line 2.
 *                        
 *                        The main function extracts the inorder and preorder are from the file, puts them into array lists, 
 *                        constructs them into their respective parent child relation in Node form, beginning at a Node root, 
 *                        and the Node root is then made to be referenced by the "root" field in BinaryTree. It then shows
 *                        the tree in a JScrollPane window, and terminates once this window is closed.
 *                        
 * @author Laivi Malamut-Salvaggio
 * @version 1.0
 *
 * @param <E> Any object
 */

public class BinaryTree<E> {

	protected Node<E> root;
	protected static ArrayList<Integer> InOrder = new ArrayList<Integer>();
	protected static ArrayList<Integer> PreOrder = new ArrayList<Integer>();
	protected static String fileToUse = "testBTInt.txt";
	protected static String InString = "075193826";
	protected static String PreString = "150723986";
	protected static String name = "Binary Tree";
	protected static boolean noFile;
	protected static boolean string;
	protected static int orderSize;
	
	
	public static void main(String args[]){
		String orders = readFile();
		if(noFile){
			buildOrders();
			BinaryTree<Integer> myTree = buildTree(InOrder, PreOrder);
			myTree.showTree();
		}
		else if(string){
			ArrayList<String> inorder = new ArrayList<String>();
			stringOrderAdder(orders.substring(0, orders.length()/2), inorder);
			ArrayList<String> preorder = new ArrayList<String>();
			stringOrderAdder(orders.substring(orders.length()/2, orders.length()), preorder);
			BinaryTree<String> myTree = buildTree(inorder, preorder);
			myTree.showTree();
			
		}
		else{
			ArrayList<Integer> inorder = new ArrayList<Integer>();
			intOrderAdder(orders.substring(0, orders.length()/2), inorder);
			ArrayList<Integer> preorder = new ArrayList<Integer>();
			intOrderAdder(orders.substring(orders.length()/2, orders.length()), preorder);
			BinaryTree<Integer> myTree = buildTree(inorder, preorder);
			myTree.showTree();
		}
	}
	/**
	 * Displays the tree in a dropdown file format. First file is the left node, second is the right node.
	 */
	public void showTree(){
		JScrollPane pane = new JScrollPane(buildJTree(root));
		pane.setVisible(true);
		pane.revalidate();
		pane.repaint();
		JFrame frame = new JFrame(name);
	    frame.setForeground(Color.black);
	    frame.setBackground(Color.lightGray);
	    frame.setPreferredSize(new Dimension(400,400));;
	    Container cp = frame.getContentPane();
	    cp.add(pane);
	    frame.pack();
	    frame.setVisible(true);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private JTree buildJTree(Node<E> node){
		return new JTree(buildJRoot(node, node.left, node.right));
	}
	
	/**
	 * recursively build the JRoot to be inserted into the JTree which will be inserted into the JScrollPane.
	 * 
	 * @param Root the root of the tree to be printed out
	 * @param left the left child of Root
	 * @param right the right child of the root
	 * 
	 * @return a DefaultMutableTreeNode which is the root to JTree constructed out of the root of BinaryTree
	 */
	private DefaultMutableTreeNode buildJRoot(Node<E> Root, Node<E> left, Node<E> right){
		E myData = null;
		DefaultMutableTreeNode first = new DefaultMutableTreeNode("null");
		DefaultMutableTreeNode second = new DefaultMutableTreeNode("null");
		if(Root != null){
			myData = Root.data;
		}
		DefaultMutableTreeNode result = new DefaultMutableTreeNode("" + myData);
		if(left != null){
			first = buildJRoot(left, left.left, left.right);
		}
		if(right != null){
			second = buildJRoot(right, right.left, right.right);
		}
		result.add(first);
		result.add(second);
		return result;
	}
	
	/*
	 * adds the default int orders into arraylists, only called if no file is found or usable.
	 */
	protected static void buildOrders(){
		for(int i = 0; i< InString.length(); i++){
			InOrder.add(Integer.parseInt(InString.substring(i, i+1)));
			PreOrder.add(Integer.parseInt(PreString.substring(i,i+1)));
		}
	}
	
	/**
	 * reads the file, sets the string variable to true if the first line says String, and returns the second and 
	 * third line of the file, the second concatenated with the third. It is the job of the client to 
	 * divide it back into the inorder and preorder strings (which is simple- each one is of size str.length()/2, exactly).
	 * 
	 * @return the inorder plus the preorder
	 */
	protected static String readFile(){
		FileReader fr = null;
		try{
			fr = new FileReader(fileToUse);
		}
		catch(FileNotFoundException e){
			noFile = true;
		}
		String inorder = null;
		String preorder = null;
		if(!noFile){
			noFile = true;
			Scanner sc = new Scanner(fr);
			if(sc.hasNextLine() && sc.nextLine().toLowerCase().equals("string")) {
				string = true;
			}
			if(sc.hasNextLine()){
				inorder = sc.nextLine();
				orderSize = inorder.length();
			}
			if(sc.hasNextLine()){
				preorder = sc.nextLine();
				noFile = false;
			}
			sc.close();
		}
		return inorder + preorder;
	}
	
	/*
	 * divides and adds a string of an order of type int to an arraylist, each int separated by spaces
	 */
	protected static void intOrderAdder(String lineOrder, ArrayList<Integer> order){
		for(int i = 0; i<lineOrder.length(); i++){
			if(!lineOrder.substring(i,i+1).equals(" ")){
				String tempEntry = lineOrder.substring(i,i+1);
				while(i+1<lineOrder.length() && !lineOrder.substring(i+1,i+2).equals(" ")){
					tempEntry += lineOrder.substring(i+1,i+2);
					i++;
				}
				order.add(Integer.parseInt(tempEntry));
			}
		}
	}
	
	/*
	 * divides and adds a string of an order of type String to an arraylist, each string enclosed in quotation marks
	 */
	protected static void stringOrderAdder(String lineOrder, ArrayList<String> stringInOrder2){
		for(int i = 0; i<lineOrder.length(); i++){
			if(lineOrder.charAt(i) == '"'){
				String tempEntry = "";
				while(i+1<lineOrder.length() && lineOrder.charAt(i+1) != '"'){
					tempEntry += lineOrder.substring(i+1,i+2);
					i++;
				}
				i++;
				stringInOrder2.add(tempEntry);
			}
		}
	}
	
	/*
	 * constrictor for binary tree - all info is added after construction
	 */
	public BinaryTree(){

	}

	public interface Visitor {
		public Object visit(Object handle);
	}
/**
 * the Node class - meant to be contained in a BinaryTree, or referenced to by other Nodes. toString may be called on any node
 * to print out the tree rooted at that node.
 *
 * @param <V> type of data be stored in data field
 */
	protected class Node<V> {
		protected V data;
		protected Node<V> left;
		protected Node<V> right;

		public Node(V data) {
			this.data = data;
			left = null;
			right = null;
		}

		public String toString() {
			if (data == null){
				return null;
			}
			else {
				return data.toString();
			}
		}

		public String toString(int depth)
		{
			String result = "";

			if (left != null)
				result += left.toString(depth + 1);

			for (int i = 0; i < depth; i++)
				result += "  ";

			result += toString() + "\n";

			if (right != null)
				result += right.toString(depth + 1);

			return result;
		}

	}

	public void inorderWalk(Visitor visitor){
		inorderWalk(root, visitor);
	}


	protected void inorderWalk(Node<E> x, Visitor visitor){
		if (x != null) {
			inorderWalk(x.left, visitor);
			visitor.visit(x);
			inorderWalk(x.right, visitor);
		}
	}

	public void preorderWalk(Visitor visitor){
		preorderWalk(root, visitor);
	}


	protected void preorderWalk(Node<E> x, Visitor visitor){
		if (x != null) {
			visitor.visit(x);
			preorderWalk(x.left, visitor);
			preorderWalk(x.right, visitor);
		}
	}
	
	public String toString(){
		return root.toString(0);
	}
	
	/**
	 * Constructs the binary tree from the preorder and inorder of the tree, represented in arraylists. Uses recursive 
	 * function buildRoot to make the root from which all other nodes are directly or transitively referenced to, then 
	 * inserts the root into a new BinaryTree and returns it
	 * 
	 * @param inorder The inorder of the BinaryTree
	 * @param preorder The preorder of the BinaryTree
	 * @return The BinaryTree constructed form the given preorder and inorder
	 */
	protected static <E> BinaryTree<E> buildTree(ArrayList<E> inorder, ArrayList<E> preorder) {
		
		BinaryTree<E> result = new BinaryTree<E>();
		result.root = result.buildRoot(inorder, preorder);
		return result;
	}
	/*
	 * recursively builds the root node and all of its referenced nodes using the following algorithm:
	 * 
	 * the root of the tree is always the first element of preorder.
	 * the left tree and right tree can be either null or contain a Node
	 * The tree is divided by finding the occurrence of the first element of the preorder in the inorder. everything
	 * to the left of this is the left subtree of the root, and everything to the right is the right subtree.
	 * if either of those is of length 0, there is no node to that side and root has an empty child in that direction.
	 * if they are not of length 0, they are fed back into the recursion with their respective inorder and preorder, and the
	 * result made to be referenced to by the root.
	 */
	protected Node<E> buildRoot(ArrayList<E> inorder, ArrayList<E> preorder) {
		E root = preorder.get(0);
		Node<E> result = new Node<E>(root);
		Node<E> leftTree = null;
		Node<E> rightTree = null;
		if (preorder.size() > 1) {
			int leftEnd = inorder.indexOf(root);
			int leftSize = 0;
			if(leftEnd != 0){
				ArrayList<E> leftIn = new ArrayList<E>(inorder.subList(0, leftEnd));// everything to left of root, exclusive of root
				ArrayList<E> leftPre = new ArrayList<E>(preorder.subList(1, 1+leftIn.size())); // the length of its inorder, + 1 to account for root
				leftSize = leftIn.size();
				leftTree = buildRoot(leftIn, leftPre); //recursively build this subtree
			}
			if(leftEnd + 1 != inorder.size()){
				ArrayList<E> rightIn = new ArrayList<E>(inorder.subList(leftEnd + 1, inorder.size()));// everything to right of root, exclusive of root
				ArrayList<E> rightPre = new ArrayList<E>(preorder.subList(1 + leftSize, preorder.size())); // begin at length of left order, end at end of list
				rightTree = buildRoot(rightIn, rightPre);
			}
		}
		result.left = leftTree;
		result.right = rightTree;
		return result;
	}


}
