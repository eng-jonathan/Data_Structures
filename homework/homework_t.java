// Homework T:  Expression trees.

// In the project you need to code one class that extends the abstract class ExpressionTree.  

// Your class needs to code one constructor and one output method as well as any helper methods
// that you find useful.
// The goal is to process mathematical expressions like (1+2)*(3+4) that are entered as strings.

// In the main program that tests your class,
// the Utility getInput method reads a mathematical expression that is typed as input.
// The expression can contain any combination of numbers, variable identifiers and mathematical operations.
// These mathematical operations are +, -, *, /, ( and )

// The constructor should turn an input expression into the content of a binary (expression) tree.
// The tree can then be printed in prefix, postfix, or fully parenthesised infix notation.
// The first two of these methods have been coded for you in the abstract class ExpressionTree, you need to code the third.

// For example the expression   5 + (x / y + z * 7) - 2  would be printed as
//                           - + 5 + / x y * z 7 2  in prefix order
// or                          5 x y / z 7 * + + 2 -  in postfix order
// or               (( 5 + ((x / y) + (z * 7))) - 2)  in fully parenthesised infix notation.


// To simplify things in this project the - sign can only be used between 2 quantities that are being subtracted.
// This will prevent you from ever working with a negative number such as -2 which would involve a different
// use for the - character.  However the simplification means that your code does not need to work out which meaning
// to attach to any copy of a - character.  

// Another simplification for this project is that you may assume that only correct mathematical expressions are entered.
// You do not need to check that an input String from getInput makes sense as an expression.
// If an illegal expression is encountered, your program can behave in any way that is convenient for you.

// You should change the class name homework_t
// You should only make changes inside this class.
// You do not need any more than 100 lines of code in the class.
// After you have tested your work on mars (as well as in your development environment)
// cut the file above the class Utility and submit it by email.
// Remove the initial package instruction (if you used eclipse) since this will stop your code compiling on other machines. 

// The hard part of this assignment is the constructor.  
// In terms of planning it, I suggest that you think about which operation is the last to be performed in the expression.
// Can you write down a rule to identify this operation? Deal with the easier case when there are no parentheses first.
//
// Once you accomplish this you can finish quickly using a recursion.
//
// The fullyParenthesised method can also be done using a recursion --- 
//  this will require an auxiliary recursive method to call on.

// Suggested strategy:
// 1. Make a constructor that deals with expressions without parentheses  (1st step partial credit).
// 2. Make a fullyParenthesised output method  (2nd step partial credit).
// 3. Adapt the constructor to deal with parentheses.  Hint a Stack will help here.  (Last part of credit).

import java.util.ArrayList;	
import java.util.Scanner;
import java.util.Stack;

public class homework_t extends ExpressionTree {

	   public static void main(String args[]) {
	      homework_t y = new homework_t("5 + 6 * 7");
	      Utility.print(y);
	      y = new homework_T(Utility.getInput());
	      Utility.print(y);
	   }
	   
	   @SuppressWarnings("unchecked")
	   public String fullyParenthesised() {
	      if(root() == null) return "";
	      return fullParenHelper((BinaryNode<String>)root());
	   }
	   
	   private String fullParenHelper(BinaryNode<String> node) {
		   if(isLeaf(node)) {
			   return node.toString();
		   }
		   return "(" + fullParenHelper(node.left) + node.toString() + fullParenHelper(node.right) + ")";
	   }

	   public homework_t(String s) {
	      super();
	      try {
	    	  setRoot(populateTree(s));
	      } catch (Exception e) {
	    	  e.getMessage();
	    	  System.exit(0);
	      }
	   }
	   
	   public int precedence(char c) {
		   if("+-".indexOf(c) != -1)
			   return 1;
		   else if("*/".indexOf(c) != -1)
			   return 2;
		   return -1;
	   }
	   
	   public void createNewInternalNode(Stack<BinaryNode<String>> result, Stack<Character> operations) {
		   BinaryNode<String> right = result.pop();
		   BinaryNode<String> left = result.pop();
		   String operation = Character.toString(operations.pop());
		   BinaryNode<String> newNode = new BinaryNode<>(null,left,right,operation);
		   left.setParent(newNode);
		   right.setParent(newNode);
	       result.push(newNode);
	   }
	   
	   public TreeNode populateTree(String s) throws Exception {
		   Stack<BinaryNode<String>> result = new Stack<>();
		   Stack<Character> operations = new Stack<>();
		   for (int i = 0; i < s.length(); i++) { 
	           char c = s.charAt(i); 
	           if(c == ' ')
	        	   continue;
	           if (Character.isLetterOrDigit(c)) 
	               result.push(new BinaryNode<>(null,null,null,Character.toString(c)));
	           else if (c == '(') 
	               operations.push(c);
	           else if (c == ')') { 
	               while (!operations.isEmpty() && operations.peek() != '(') {
	            	   createNewInternalNode(result, operations);
	               }
	               if (!operations.isEmpty() && operations.peek() != '(') 
	                   throw new Exception("Improper input");                
	               else
	            	   operations.pop(); 
	           }
	           else { 
	               while (!operations.isEmpty() && precedence(c) <= precedence(operations.peek())) { 
	                   if(operations.peek() == '(') 
	                	   throw new Exception("Improper input"); 
	                   createNewInternalNode(result, operations);
	               }
	               operations.push(c);
	           }
	       }
	       while (!operations.isEmpty()){
	           if(operations.peek() == '(')
	        	   throw new Exception("Improper input");
	           createNewInternalNode(result, operations); 
	       } 
	       return result.pop();
	   }
	}

//-------  CUT HERE.  Place all your new code above this line and submit only the ------
//-------  code above this line as your homework.  Do not make any code changes ----
//-------  below this line.                                                ---------

class Utility {
   public static String getInput() {
      System.out.println("Enter an algebraic expression: ");
      Scanner s = new Scanner(System.in);
      String answer =  s.nextLine();
      s.close();
      return answer;
   }

   public static void print(ExpressionTree y) {
      System.out.println("Prefix: " + y.prefix());
      System.out.println("Postfix: " + y.postfix());
      System.out.println("Fully parenthesised: " + y.fullyParenthesised());
      System.out.println("-----------------");
   }   
}

abstract class ExpressionTree extends BinaryTree<String> {
   public ExpressionTree() {
      super();
   }
   public abstract String fullyParenthesised();
   
   public String postfix() {
      String ans = "";
      ArrayList<TreeNode> l = postOrder(); 
      for (TreeNode b:l) ans += b.toString() + " ";
      return ans;
   }

   public String prefix() {
      String ans = "";
      ArrayList<TreeNode> l = preOrder(); 
      for (TreeNode b:l) ans += b.toString() + " ";
      return ans;
   }
}

// classes BinaryTree, BinaryNode, Tree and TreeNode given below are complete
// and are exactly as implemented in our course.  You should not make any changes below this line

class BinaryTree<T> extends Tree {
   
   public BinaryTree() {
      super();
   }

   public void addRoot(T t) throws Exception {
      if (root() != null)
         throw new Exception("The tree is not empty");
      setRoot(new BinaryNode<T>(null, null, null, t));
      size++;
   }

   public void addLeft(BinaryNode<T> node, T t) throws Exception {
      if (node.getLeft() != null)
         throw new Exception("Left child already exists");
      node.setLeft(new BinaryNode<T>(node, null, null, t));
      size++;
   }

   public void addRight(BinaryNode<T> node, T t) throws Exception {
      if (node.getRight() != null)
         throw new Exception("Right child already exists");
      node.setRight(new BinaryNode<T>(node, null, null, t));
      size++;
   }

   // removes a leaf but promotes and removes a descendant otherwise
   // returns the parent of the node that is actually removed
   @SuppressWarnings("unchecked")
   public BinaryNode<T> removeNode(BinaryNode<T> node) {
      if (isLeaf(node)) { // base case
         BinaryNode<T> parent = (BinaryNode<T>) node.getParent();
         if (parent == null)
            setRoot(null);
         else
            parent.removeChild(node);
         size--;
         return parent;
      }
      BinaryNode<T> lower = descendant(node);
      promote(lower, node);
      return removeNode(lower);
   }

   public void promote(BinaryNode<T> lower, BinaryNode<T> node) {
      node.data = lower.data;
   }

   public BinaryNode<T> descendant(BinaryNode<T> node) {
      if (node.left != null) return node.left;
      return node.right;
   }

   @SuppressWarnings("unchecked")
   public ArrayList<BinaryNode<T>> inOrder() {
      ArrayList<BinaryNode<T>> answer = new ArrayList<BinaryNode<T>>();
      inOrder((BinaryNode<T>) root(), answer);
      return answer;
   }

   public void inOrder(BinaryNode<T> node, ArrayList<BinaryNode<T>> order) {
      if (node == null)
         return;
      inOrder(node.getLeft(), order);
      order.add(node);
      inOrder(node.getRight(), order);
   }
}

class BinaryNode<T> implements TreeNode {

   T data;
   BinaryNode<T> left, right, parent;
   
   public BinaryNode() {
      parent = left = right = null;
      data = null;
   }
   
   public BinaryNode(BinaryNode<T> p, BinaryNode<T> l, BinaryNode<T> r, T d) {
      parent = p;
      left = l;
      right = r;
      data = d;
   }

   @Override
   public ArrayList<? extends TreeNode> getChildren() {
      ArrayList<BinaryNode<T>> answer = new ArrayList<>();
      if (left != null) answer.add(left);
      if (right != null) answer.add(right);
      return answer;
   }

   @Override
   public TreeNode getParent() {
      return parent;
   }
   
   @Override
   public String toString() {
      return data.toString();
   }
   
   public void setLeft(BinaryNode<T> n) {
      left = n;
   }

   public void setRight(BinaryNode<T> n) {
      right = n;
   }

   public BinaryNode<T> getLeft() {
      return left;
   }

   public BinaryNode<T> getRight() {
      return right;
   }

   public void removeChild(BinaryNode<T> n) {
      if (getLeft() == n)
         setLeft(null);
      if (getRight() == n)
         setRight(null);
   }

   public void setParent(BinaryNode<T> node) {
      parent = node;
   }

   public T getData() {
      return data;
   }

   public void setData(T newData) {
      data = newData;
   }
}

class Tree {
   private TreeNode root;
   public int size;

   public Tree() {
      setRoot(null);
      size = 0;
   }

   public TreeNode root() {
      return root;
   }

   public TreeNode parent(TreeNode node) {
      return node.getParent();
   }

   public boolean isRoot(TreeNode node) {
      return node == root();
   }

   public boolean isInternal(TreeNode node) {
      return node.getChildren().size() > 0;
   }

   public boolean isLeaf(TreeNode node) {
      return !isInternal(node);
   }

   public int size() {
      return size;
   }

   public boolean empty() {
      return size == 0;
   }

   public int depth(TreeNode node) {
      if (isRoot(node))
         return 0;
      return 1 + depth(node.getParent());
   }

   public int height(TreeNode node) {
      if (isLeaf(node))
         return 0;
      int maxChildHeight = 0;
      ArrayList<? extends TreeNode> c = node.getChildren();
      for (TreeNode t:c) {
         int hc = height(t);
         if (hc > maxChildHeight)
            maxChildHeight = hc;
      }
      return maxChildHeight + 1;
   }

   public int height() {
      if (root() == null)
         return -1;
      return height(root());
   }

   public ArrayList<TreeNode> preOrder() {
      ArrayList<TreeNode> answer = new ArrayList<>();
      preOrder(root(), answer);
      return answer;
   }

   public void preOrder(TreeNode node, ArrayList<TreeNode> nodeOrder) {
      if (node == null) return;
      nodeOrder.add(node);
      for (TreeNode n:node.getChildren()) {
         preOrder(n, nodeOrder);
      }
   }

   public ArrayList<TreeNode> postOrder() {
      ArrayList<TreeNode> answer = new ArrayList<TreeNode>();
      postOrder(root(), answer);
      return answer;
   }

   public void postOrder(TreeNode node, ArrayList<TreeNode> nodeOrder) {
      if (node == null)
         return;
      for (TreeNode n:node.getChildren()) {
         postOrder(n, nodeOrder);
      }
      nodeOrder.add(node);
   }

   public ArrayList<TreeNode> levelOrder() {
      ArrayList<TreeNode> waiting = new ArrayList<>();
      if (root() == null) return waiting;
      waiting.add(root());
      int done = 0;
      while (done < waiting.size()) {
         TreeNode oldNode = waiting.get(done++);
         for (TreeNode n:oldNode.getChildren())
            waiting.add(n);
      }
      return waiting;
   }

   public void setRoot(TreeNode root) {
      this.root = root;
   }
}

interface TreeNode {
   public ArrayList<? extends TreeNode> getChildren();
   public TreeNode getParent();
   public String toString();
}