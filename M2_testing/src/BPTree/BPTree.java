package BPTree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class BPTree<T extends Comparable<T>> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int order;
	private BPTreeNode<T> root;
	
	/**
	 * Creates an empty B+ tree
	 * @param order the maximum number of keys in the nodes of the tree
	 */
	public BPTree(int order) 
	{
		this.order = order;
		root = new BPTreeLeafNode<T>(this.order);
		root.setRoot(true);
	}
	
	/**
	 * Inserts the specified key associated with the given record in the B+ tree
	 * @param key the key to be inserted
	 * @param recordReference the reference of the record associated with the key
	 */
	public void insert(T key, Ref recordReference)
	{
		PushUp<T> pushUp = root.insert(key, recordReference, null, -1);
		if(pushUp != null)
		{
			BPTreeInnerNode<T> newRoot = new BPTreeInnerNode<T>(order);
			newRoot.insertLeftAt(0, pushUp.key, root);
			newRoot.setChild(1, pushUp.newNode);
			root.setRoot(false);
			root = newRoot;
			root.setRoot(true);
		}
	}
	
//custom
	public ArrayList<String> getInsertPage(T key) {

		return root.getInsertPage(key, null, -1);
		
	}
	public ArrayList<String> getDeletePage(T key)
	{
		//go down and find the new root in case the old root is deleted
		
		return root.getDeletePage(key, null,-1);
	}
	
	/**
	 * Looks up for the record that is associated with the specified key
	 * @param key the key to find its record
	 * @return the reference of the record associated with this key 
	 */
	public ArrayList<Ref> search(T key)
	{
		return root.search(key);
	}
	
	public ArrayList<Ref> searchLess(T key)
	{
		return root.searchLess(key);
	}
	
	public ArrayList<Ref> searchNotEqual(T key)
	{
		return root.searchNotEqual(key);
	}
	public ArrayList<Ref> searchgreater(T key)
	{
		return root.searchgreater(key);
	}
	public ArrayList<Ref> searchgreaterORequal(T key)
	{
		return root.searchgreaterORequal(key);
	}
	public ArrayList<Ref> searchlessORequal(T key)
	{
		return root.searchLessORequal(key);
	}
	
	
	public void updateRef(T key, String oldPage, String newPage, Date td)
	{
		root.updateRef(key,oldPage,newPage, td);
	}
	
	/**
	 * Delete a key and its associated record from the tree.
	 * @param key the key to be deleted
	 * @return a boolean to indicate whether the key is successfully deleted or it was not in the tree
	 */
	public boolean delete(T key, Date td)
	{
		boolean done = root.delete(key, null, -1, td);
		//go down and find the new root in case the old root is deleted
		while(root instanceof BPTreeInnerNode && !root.isRoot())
			root = ((BPTreeInnerNode<T>) root).getFirstChild();
		return done;
	}
	
	/**
	 * Returns a string representation of the B+ tree.
	 */
	public String toString()
	{	
		
		
		String s = "";
		Queue<BPTreeNode<T>> cur = new LinkedList<BPTreeNode<T>>(), next;
		cur.add(root);
		while(!cur.isEmpty())
		{
			next = new LinkedList<BPTreeNode<T>>();
			while(!cur.isEmpty())
			{
				BPTreeNode<T> curNode = cur.remove();
				System.out.print(curNode);
				if(curNode instanceof BPTreeLeafNode)
					System.out.print("->");
				else
				{
					System.out.print("{");
					BPTreeInnerNode<T> parent = (BPTreeInnerNode<T>) curNode;
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