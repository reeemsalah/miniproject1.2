package RTree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import BPTree.BPTreeInnerNode;
import DBEngine.Region;
import BPTree.Ref;

public class RTreeInnerNode<T extends Comparable<T>> extends RTreeNode<T>  implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RTreeNode<T>[] children;
	/**
	 * create BPTreeNode given order.
	 * @param n
	 */
	@SuppressWarnings("unchecked")
	public RTreeInnerNode(int n) 
	{
		super(n);
		keys = new Region[n];
		children = new RTreeNode[n+1];
	}

	/**
	 * get child with specified index
	 * @return Node which is child at specified index
	 */
	public RTreeNode<T> getChild(int index) 
	{
		return children[index];
	}
	
	/**
	 * creating child at specified index
	 */
	public void setChild(int index, RTreeNode<T> child) 
	{
		children[index] = child;
	}
	/**
	 * get the first child of this node.
	 * @return first child node.
	 */
	public RTreeNode<T> getFirstChild()
	{
		return children[0];
	}
	/**
	 * get the last child of this node
	 * @return last child node.
	 */
	public RTreeNode<T> getLastChild()
	{
		return children[numberOfKeys];
	}
	/**
	 * @return the minimum keys values in InnerNode
	 */
	public int minKeys()
	{
		if(this.isRoot())
			return 1;
		return (order + 2) / 2 - 1;
	}
	/**
	 * insert given key in the corresponding index.
	 * @param key key to be inserted
	 * @param Ref reference which that inserted key is located
	 * @param parent parent of that inserted node
	 * @param ptr index of pointer in the parent node pointing to the current node
	 * @return value to be pushed up to the parent.
	 */
	public PushUp<T> insert(Region key, Ref recordReference, RTreeInnerNode<T> parent, int ptr)
	{
		int index = findIndex(key);
		PushUp<T> pushUp = children[index].insert(key, recordReference, this, index);
		
		if(pushUp == null)
			return null;
		
		if(this.isFull())
		{
			RTreeInnerNode<T> newNode = this.split(pushUp);
			Comparable<T> newKey = newNode.getFirstKey();
			newNode.deleteAt(0, 0);
			return new PushUp<T>(newNode, newKey);
		}
		else
		{
			index = 0;
			while (index < numberOfKeys && getKey(index).compareTo(key) < 0)
				++index;
			this.insertRightAt(index, (Region)pushUp.key, pushUp.newNode);
			return null;
		}
	}
	
	public ArrayList<String> getInsertPage(Region key, RTreeInnerNode<T> parent, int ptr)
	{
//		int index = findIndex(key);
//		PushUp<T> pushUp = children[index].getInsertPage(key, this, index);
//		
//		if(pushUp == null)
//			return null;
		
//		if(this.isFull())
//		{
//			BPTreeInnerNode<T> newNode = this.split(pushUp);
//			Comparable<T> newKey = newNode.getFirstKey();
//			newNode.deleteAt(0, 0);
//			return new PushUp<T>(newNode, newKey);
//		}
//		else
//		{
			int i = 0;
			while (i < numberOfKeys && getKey(i).compareTo(key) < 0) 
				++i;
//			this.insertRightAt(index, pushUp.key, pushUp.newNode);
				return this.children[i].getInsertPage(key, parent, ptr);
			
//			return null;
//		}
	}
	
	@Override
	//custom	
		public ArrayList<String> getDeletePage(Region key, RTreeInnerNode<T> parent, int ptr)
		{
	ArrayList<String> pageOptions = new ArrayList<String>(); 
	System.out.println("level innerNode " + this.index);

	ArrayList<String> pages = new ArrayList<String>(); 
	int i = 0;
	while (i < numberOfKeys) {
		if(getKey(i).compareTo(key) <= 0 && getKey(i).compareTo(key) >= 0 ) {
			System.out.println("between");

			pageOptions.addAll(this.children[i+1].getDeletePage(key, parent, ptr));}

		if(getKey(i).compareTo(key) <= 0 && i==numberOfKeys-1 ) {
			System.out.println("last");

		
			pageOptions.addAll(this.getLastChild().getDeletePage(key, parent, ptr));}
		if(getKey(i).compareTo(key) >= 0 && i==0 ) {
			System.out.println("first");

			pageOptions.addAll(this.getFirstChild().getDeletePage(key, parent, ptr));}
		++i;

	}
	//pageOptions.addAll(this.children[0].getDeletePage(key, parent, ptr));

	return pageOptions;
		
		}
		
	
	/**
	 * split the inner node and adjust values and pointers.
	 * @param pushup key to be pushed up to the parent in case of splitting.
	 * @return Inner node after splitting
	 */
	@SuppressWarnings("unchecked")
	public RTreeInnerNode<T> split(PushUp<T> pushup) 
	{
		int keyIndex = this.findIndex((Region)pushup.key);
		int midIndex = numberOfKeys / 2 - 1;
		if(keyIndex > midIndex)				//split nodes evenly
			++midIndex;		

		int totalKeys = numberOfKeys + 1;
		//move keys to a new node
		RTreeInnerNode<T> newNode = new RTreeInnerNode<T>(order);
		for (int i = midIndex; i < totalKeys - 1; ++i) 
		{	
			newNode.insertRightAt(i - midIndex, this.getKey(i), this.getChild(i+1));
			numberOfKeys--;
		}
		newNode.setChild(0, this.getChild(midIndex));
		
		//insert the new key
		System.out.println(midIndex);
		if(keyIndex < totalKeys / 2)
			this.insertRightAt(keyIndex, (Region)pushup.key, pushup.newNode);
		else
			newNode.insertRightAt(keyIndex - midIndex, (Region)pushup.key, pushup.newNode);
		

		return newNode;
	}
	/**
	 * find the correct place index of specified key in that node.
	 * @param key to be looked for
	 * @return index of that given key
	 */
	public int findIndex(Region key) 
	{
		for (int i = 0; i < numberOfKeys; ++i) 
		{
			int cmp = getKey(i).compareTo(key);
			if (cmp > 0)
				return i;
		}
		return numberOfKeys;
	}
	/**
	 * insert at given index a given key
	 * @param index where it inserts the key
	 * @param key to be inserted at index
	 */
	private void insertAt(int index, Region key) 
	{
		for (int i = numberOfKeys; i > index; --i) 
		{
			this.setKey(i, this.getKey(i - 1));
			this.setChild(i+1, this.getChild(i));
		}
		this.setKey(index, key);
		numberOfKeys++;
	}
	/**insert key and adjust left pointer with given child.
	 * @param index where key is inserted
	 * @param key to be inserted in that index
	 * @param leftChild child which this node points to with pointer at left of that index
	 */
	public void insertLeftAt(int index, Region key, RTreeNode<T> leftChild) 
	{
		insertAt(index, key);
		this.setChild(index+1, this.getChild(index));
		this.setChild(index, leftChild);
	}
	/**insert key and adjust right pointer with given child.
	 * @param index where key is inserted
	 * @param key to be inserted in that index
	 * @param rightChild child which this node points to with pointer at right of that index
	 */
	public void insertRightAt(int index, Region key, RTreeNode<T> rightChild)
	{
		insertAt(index, key);
		this.setChild(index + 1, rightChild);
	}
	/**
	 * delete key and return true or false if it is deleted or not
	 */
	public boolean delete(Region key, RTreeInnerNode<T> parent, int ptr, Date td) 
	{
		boolean done = false;
		for(int i = 0; !done && i < numberOfKeys; ++i)
			if(keys[i].compareTo(key) > 0)
				done = children[i].delete(key, this, i, td);
			
		if(!done)
			done = children[numberOfKeys].delete(key, this, numberOfKeys, td);
		if(numberOfKeys < this.minKeys())
		{
			if(isRoot())
			{
				this.getFirstChild().setRoot(true);
				this.setRoot(false);
				return done;
			}
			//1.try to borrow
			if(borrow(parent, ptr))
				return done;
			//2.merge
			merge(parent, ptr);
		}
		return done;
	}
	/**
	 * borrow from the right sibling or left sibling in case of overflow.
	 * @param parent of the current node
	 * @param ptr index of pointer in the parent node pointing to the current node 
	 * @return true or false if it can borrow form right sibling or left sibling or it can not
	 */
	public boolean borrow(RTreeInnerNode<T> parent, int ptr)
	{
		//check left sibling
		if(ptr > 0)
		{
			RTreeInnerNode<T> leftSibling = (RTreeInnerNode<T>) parent.getChild(ptr-1);
			if(leftSibling.numberOfKeys > leftSibling.minKeys())
			{
				this.insertLeftAt(0, parent.getKey(ptr-1), leftSibling.getLastChild());
				parent.deleteAt(ptr-1);
				parent.insertRightAt(ptr-1, leftSibling.getLastKey(), this);
				leftSibling.deleteAt(leftSibling.numberOfKeys - 1);
				return true;
			}
		}

		//check right sibling
		if(ptr < parent.numberOfKeys)
		{
			RTreeInnerNode<T> rightSibling = (RTreeInnerNode<T>) parent.getChild(ptr+1);
			if(rightSibling.numberOfKeys > rightSibling.minKeys())
			{
				this.insertRightAt(this.numberOfKeys, parent.getKey(ptr), rightSibling.getFirstChild());
				parent.deleteAt(ptr);
				parent.insertRightAt(ptr, rightSibling.getFirstKey(), rightSibling);
				rightSibling.deleteAt(0, 0);
				return true;
			}
		}
		return false;
	}
	/**
	 * try to merge with left or right sibling in case of overflow
	 * @param parent of the current node 
	 * @param ptr index of pointer in the parent node pointing to the current node
	 */
	public void merge(RTreeInnerNode<T> parent, int ptr)
	{
		if(ptr > 0)
		{
			//merge with left
			RTreeInnerNode<T> leftSibling = (RTreeInnerNode<T>) parent.getChild(ptr-1);
			leftSibling.merge(parent.getKey(ptr-1), this);
			parent.deleteAt(ptr-1);			
		}
		else
		{
			//merge with right
			RTreeInnerNode<T> rightSibling = (RTreeInnerNode<T>) parent.getChild(ptr+1);
			this.merge(parent.getKey(ptr), rightSibling);
			parent.deleteAt(ptr);
		}
	}
	
	/**
	 * merge the current node with the passed node and pulling the passed key from the parent
	 * to be inserted with the merged node
	 * @param parentKey the pulled key from the parent to be inserted in the merged node
	 * @param foreignNode the node to be merged with the current node
	 */
	public void merge(Region parentKey, RTreeInnerNode<T> foreignNode)
	{
		this.insertRightAt(numberOfKeys, parentKey, foreignNode.getFirstChild());
		for(int i = 0; i < foreignNode.numberOfKeys; ++i)
			this.insertRightAt(numberOfKeys, foreignNode.getKey(i), foreignNode.getChild(i+1));
	}

	/**
	 * delete the key at the specified index with the option to delete the right or left pointer
	 * @param keyIndex the index whose key will be deleted
	 * @param childPtr 0 for deleting the left pointer and 1 for deleting the right pointer
	 */
	public void deleteAt(int keyIndex, int childPtr)	//0 for left and 1 for right
	{
		for(int i = keyIndex; i < numberOfKeys - 1; ++i)
		{
			keys[i] = keys[i+1];
			children[i+childPtr] = children[i+childPtr+1];
		}
		if(childPtr == 0)
			children[numberOfKeys-1] = children[numberOfKeys];
		numberOfKeys--;
	}
	
	/**
	 * searches for the record reference of the specified key
	 */
	@Override
	public ArrayList<Ref> search(Region key) 
	{
		return children[findIndex(key)].search(key);
	}
	
	public ArrayList<Ref> searchLess(Region key) 
	{
		System.out.println("searchLess at "+this.index);
		ArrayList<Ref> res = new ArrayList<Ref>();
		for (int i=0;i<numberOfKeys;i++) {
			if (keys[i].compareTo(key)<=0)
			res.addAll(children[i].searchLess(key));
		}
		res.addAll(children[0].searchLess(key));

		return res ;
	}
	
	public ArrayList<Ref> searchNotEqual(Region key) 
	{
		System.out.println("searchNotEqual at "+this.index);
		ArrayList<Ref> res = new ArrayList<Ref>();
		for (int i=0;i<numberOfKeys;i++) {
//			if (i+2 <numberOfKeys && !(keys[i+1].compareTo(key)==0 &&  keys[i+2].compareTo(key)==0) )
			res.addAll(children[i].searchNotEqual(key));
		}
//		res.addAll(children[0].searchLess(key));

		return res ;
	}

	
	@Override
	public void updateRef(Region key,String oldPage, String newPage, Date td) 
	{
		children[findIndex(key)].updateRef(key, oldPage, newPage, td);
	}
	
	/**
	 * delete the key at the given index and deleting its right child
	 */
	public void deleteAt(int index) 
	{
		deleteAt(index, 1);	
	}

}