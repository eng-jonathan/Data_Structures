// Homework P:  Two way priority queues.

// In the project you need to code one class that implements the interface TwoWayPriorityQueue.
// This interface requires methods insert, removeMin and removeMax.
// The remove methods remove either the min or max element from the queue.
// You are to implement your class by extending the class BST which will be used to store data.
// The implementation of BST that we studied in class is attached at the end of this homework
//  assignment.

// You should change the class name homework_P.
// You should only make changes inside this class.
// You do not need any more than 40 lines of code in the class.
// After you have tested your work on mars (as well as in your development environment)
// cut the file above the interface TwoWayPriorityQueue and submit it by email.
// Remove the initial package instruction (if you used eclipse) since this will stop your code compiling on other machines. 
// Submit your homework as just one attached file with extension .java.

import java.util.ArrayList;
import java.util.Iterator;

public class homework_P<K extends Comparable<K>> extends BST<K> implements TwoWayPriorityQueue<K> {
  public static void main(String args[]) throws Exception {
    homework_P<Integer> t = new homework_P<Integer>();
    t.insert(3);
    t.insert(1);
    t.insert(4);
    t.insert(15);
    t.insert(9);
    t.insert(2);
    t.insert(6);
    System.out.println(t.removeMin());  // output 1
    System.out.println(t.removeMax());  // output 15
  }
 
  public void insert(K x) {
		try {
			this.add(x);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
  }
  
  @SuppressWarnings("unchecked")
  public K removeMin() throws Exception {
	 BinaryNode<K> temp = (BinaryNode<K>) this.root(); 
	 if(this.empty()==true)
		  throw new Exception("Queue Empty");
	 while(temp.getLeft()!=null)
		  temp = temp.getLeft();
	 K ans = temp.getData();
	 this.removeNode(temp);
  	 return ans;
  }
  
  @SuppressWarnings("unchecked")
  public K removeMax() throws Exception {
	  BinaryNode<K> temp = (BinaryNode<K>) this.root(); 
	  if(this.empty()==true)
		  throw new Exception("Queue Empty");
	  while(temp.getRight()!=null)
		  temp = temp.getRight();
	 K ans = temp.getData();
	 this.removeNode(temp);
  	 return ans;
  }
}

//-------  CUT HERE.  Place all your new code above this line and submit only the ------
//-------  code above this line as your homework.  Do not make any code changes ----
//-------  below this line.  

// interface to implement 

interface TwoWayPriorityQueue<K extends Comparable<K>> {
  public void insert(K x);
  public K removeMin() throws Exception;
  public K removeMax() throws Exception;
}

// --- Code for binary search trees as in class.  Do not make any changes to the classes below this line

class BST<K extends Comparable<K>> extends BinaryTree<K> {

  @SuppressWarnings("unchecked")
  public BinaryNode<K> findNode(K target) {
    if (root() == null)
      return null;
    return recursiveFindNode((BinaryNode<K>) root(), target);
  }

  // either find the node containing target or the future parent of a Node that would contain target
  protected BinaryNode<K> recursiveFindNode(BinaryNode<K> node, K target) {
    int comparison = target.compareTo(node.getData());
    if (comparison == 0) return node;
    if (comparison < 0 && node.getLeft() != null) 
      return recursiveFindNode(node.getLeft(), target);
    if (comparison > 0 && node.getRight() != null) 
      return recursiveFindNode(node.getRight(), target);
    return node;
  }

  public void remove(K target) throws Exception {
    BinaryNode<K> n = findNode(target);
    if (n == null || !n.getData().equals(target)) throw new Exception("Target not present");
    removeNode(n);
  }

  public void add(K newData) throws Exception {
    BinaryNode<K> node = findNode(newData);
    if (node == null) addRoot(newData);
    else if ((node.getData()).compareTo(newData) > 0) addLeft(node, newData);
    else if ((node.getData()).compareTo(newData) < 0) addRight(node, newData);
    else node.setData(newData);
  }

  public boolean contains(K target) {
    BinaryNode<K> node = findNode(target);
    if (node == null || !node.getData().equals(target))
      return false;
    return true;
  }

  public boolean isEmpty() {
    return root() == null;
  }

  public K get(K partialData) {
    BinaryNode<K> node = findNode(partialData);
    if (node == null || !node.getData().equals(partialData))
      return null;
    return node.getData();
  }

  @Override  // when removing from a BST, we can only promote from a neighbor in order
  public BinaryNode<K> descendant(BinaryNode<K> node) {
    BinaryNode<K> lower = node.getLeft();
    if (lower != null) {
      while (lower.getRight() != null) lower = lower.getRight();
      return lower;    // immediate predecessor of node (in order)
    }
    lower = node.getRight();
    if (lower != null) {
      while (lower.getLeft() != null) lower = lower.getLeft();
      return lower;   // immediate successor of node (in order)
    }
    return null;
  }
}

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