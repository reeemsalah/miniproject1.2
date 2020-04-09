package RTree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import DBEngine.Region;
import BPTree.Ref;
public class RTree<T extends Comparable<T>> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int order;
	private RTreeNode<T> root;
	
	/**
	 * Creates an empty B+ tree
	 * @param order the maximum number of keys in the nodes of the tree
	 */
	public RTree(int order) 
	{
		this.order = order;
		root = new RTreeLeafNode<T>(this.order);
		root.setRoot(true);
	}
	
	/**
	 * Inserts the specified key associated with the given record in the B+ tree
	 * @param key the key to be inserted
	 * @param recordReference the reference of the record associated with the key
	 */
	public void insert(Region key, Ref recordReference)
	{
		PushUp<T> pushUp = root.insert(key, recordReference, null, -1);
		if(pushUp != null)
		{
			RTreeInnerNode<T> newRoot = new RTreeInnerNode<T>(order);
			newRoot.insertLeftAt(0, (Region)pushUp.key, root);
			newRoot.setChild(1, pushUp.newNode);
			root.setRoot(false);
			root = newRoot;
			root.setRoot(true);
		}
	}
	
//	custom
	public ArrayList<String> getInsertPage(Region key) {
//		
//		PushUp<T> pushUp = root.getInsertPage(key, null, -1);
//		if(pushUp != null)
//		{
//			BPTreeInnerNode<T> newRoot = new BPTreeInnerNode<T>(order);
//			newRoot.insertLeftAt(0, pushUp.key, root);
//			newRoot.setChild(1, pushUp.newNode);
//			root.setRoot(false);
//			root = newRoot;
//			root.setRoot(true);
//		}
		return root.getInsertPage(key, null, -1);
		
	}
//	custom
	public ArrayList<String> getDeletePage(Region key)
	{
		//go down and find the new root in case the old root is deleted
		
		System.out.println("level RTree" + root.index);
		return (root.getDeletePage(key, null,-1));
	}
	
	/**
	 * Looks up for the record that is associated with the specified key
	 * @param key the key to find its record
	 * @return the reference of the record associated with this key 
	 */
	public ArrayList<Ref> search(Region key)
	{
		return root.search(key);
	}
	
	public ArrayList<Ref> searchLess(Region key)
	{
		return root.searchLess(key);
	}
	
	public void updateRef(Region key, String oldPage, String newPage, Date td)
	{
		root.updateRef(key,oldPage,newPage, td);
	}
	/**
	 * Delete a key and its associated record from the tree.
	 * @param key the key to be deleted
	 * @return a boolean to indicate whether the key is successfully deleted or it was not in the tree
	 */
	public boolean delete(Region key, Date td)
	{
		boolean done = root.delete(key, null, -1, td);
		//go down and find the new root in case the old root is deleted
		while(root instanceof RTreeInnerNode && !root.isRoot())
			root = ((RTreeInnerNode<T>) root).getFirstChild();
		return done;
	}
	
	/**
	 * Returns a string representation of the B+ tree.
	 */
	public String toString()
	{	
		
		//	<For Testing>
		// node :  (id)[k1|k2|k3|k4]{P1,P2,P3,}
		String s = "";
		Queue<RTreeNode<T>> cur = new LinkedList<RTreeNode<T>>(), next;
		cur.add(root);
		while(!cur.isEmpty())
		{
			next = new LinkedList<RTreeNode<T>>();
			while(!cur.isEmpty())
			{
				RTreeNode<T> curNode = cur.remove();
				System.out.print(curNode);
				if(curNode instanceof RTreeLeafNode)
					System.out.print("->");
				else
				{
					System.out.print("{");
					RTreeInnerNode<T> parent = (RTreeInnerNode<T>) curNode;
					for(int i = 0; i <= parent.numberOfKeys; ++i)
					{
						System.out.print(parent.getChild(i).index+",");
						next.add(parent.getChild(i));
					}
					System.out.print("} ");
				}
				
			}
			System.out.println();
			cur = next;
		}	
		//	</For Testing>
		return s;
	}
}