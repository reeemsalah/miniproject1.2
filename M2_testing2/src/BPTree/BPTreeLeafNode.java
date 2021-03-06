package BPTree;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class BPTreeLeafNode<T extends Comparable<T>> extends BPTreeNode<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Ref[] records;
	private BPTreeLeafNode<T> next;
	
	@SuppressWarnings("unchecked")
	public BPTreeLeafNode(int n) 
	{
		super(n);
		keys = new Comparable[n];
		records = new Ref[n];

	}
	
	/**
	 * @return the next leaf node
	 */
	public BPTreeLeafNode<T> getNext()
	{
		return this.next;
	}
	
	/**
	 * sets the next leaf node
	 * @param node the next leaf node
	 */
	public void setNext(BPTreeLeafNode<T> node)
	{
		this.next = node;
	}
	
	/**
	 * @param index the index to find its record
	 * @return the reference of the queried index
	 */
	public Ref getRecord(int index) 
	{
		return records[index];
	}
	
	/**
	 * sets the record at the given index with the passed reference
	 * @param index the index to set the value at
	 * @param recordReference the reference to the record
	 */
	public void setRecord(int index, Ref recordReference) 
	{
		records[index] = recordReference;
	}

	/**
	 * @return the reference of the last record
	 */
	public Ref getFirstRecord()
	{
		return records[0];
	}

	/**
	 * @return the reference of the last record
	 */
	public Ref getLastRecord()
	{
		return records[numberOfKeys-1];
	}
	
	/**
	 * finds the minimum number of keys the current node must hold
	 */
	public int minKeys()
	{
		if(this.isRoot())
			return 1;
		return (order + 1) / 2;
	}
	
	/**
	 * insert the specified key associated with a given record refernce in the B+ tree
	 */
	public PushUp<T> insert(T key, Ref recordReference, BPTreeInnerNode<T> parent, int ptr)
	{
		if(this.isFull())
		{
			BPTreeNode<T> newNode = this.split(key, recordReference);
			Comparable<T> newKey = newNode.getFirstKey();
			return new PushUp<T>(newNode, newKey);
		}
		else
		{
			int index = 0;
			while (index < numberOfKeys && getKey(index).compareTo(key) <= 0)
				++index;
			this.insertAt(index, key, recordReference);
			return null;
		}
	}
	
	public ArrayList<String> getInsertPage(T key, BPTreeInnerNode<T> parent, int ptr)
	{
		ArrayList<String> pageOptions = new ArrayList<String>();
			int i = 0;
			while (i < numberOfKeys) {

			pageOptions.add(this.getRecord(i).getPage());
			++i;
}

			return pageOptions; 

	}
	@Override 
//	custom
	public ArrayList<String> getDeletePage (T key, BPTreeInnerNode<T> parent, int ptr)
	{		//System.out.println("level leaf1  " + this.index);

		boolean checkNext = false;
		ArrayList<String> pageOptions = new ArrayList<String>();
		//System.out.println(numberOfKeys);
		int i = 0;
		while (i < numberOfKeys) {
			System.out.println("loop "+ i + "   key  " + getKey(i));
			
			if(getKey(i).compareTo(key) == 0) {
		pageOptions.add(this.getRecord(i).getPage());
		//System.out.println(this.getRecord(i).getPage());
		}

		if (i==numberOfKeys) checkNext=true;
		++i;
}
		if (checkNext) {
		pageOptions.addAll(getDeletePageHelper(key, next));}
		return pageOptions; 

	}
	
//custom	
	public ArrayList<String> getDeletePageHelper(T key, BPTreeLeafNode<T> followingNode ){
				System.out.println("level leaf2");

		boolean checkNext = false;
		ArrayList<String> pageOptions = new ArrayList<String>();
		int i = 0;
		while (i < numberOfKeys) {
			if(getKey(index).compareTo(key) == 0) {
		pageOptions.add(this.getRecord(i).getPage());
		System.out.println(this.getRecord(i).getPage());
		if (i==numberOfKeys) checkNext=true;}
		++i;
}
		if (checkNext) {
			pageOptions.addAll(getDeletePageHelper(key, next));}
		return pageOptions; 

	}
	/**
	 * inserts the passed key associated with its record reference in the specified index
	 * @param index the index at which the key will be inserted
	 * @param key the key to be inserted
	 * @param recordReference the pointer to the record associated with the key
	 */
	private void insertAt(int index, Comparable<T> key, Ref recordReference) 
	{
		for (int i = numberOfKeys - 1; i >= index; --i) 
		{
			this.setKey(i + 1, getKey(i));
			this.setRecord(i + 1, getRecord(i));
		}

		this.setKey(index, key);
		this.setRecord(index, recordReference);
		++numberOfKeys;
	}
	
	/**
	 * splits the current node
	 * @param key the new key that caused the split
	 * @param recordReference the reference of the new key
	 * @return the new node that results from the split
	 */
	public BPTreeNode<T> split(T key, Ref recordReference) 
	{
		int keyIndex = this.findIndex(key);
		int midIndex = (numberOfKeys / 2);
		if((numberOfKeys & 1) == 1 && keyIndex > midIndex)	//split nodes evenly
			++midIndex;		

		
		int totalKeys = numberOfKeys + 1;
		//move keys to a new node
		BPTreeLeafNode<T> newNode = new BPTreeLeafNode<T>(order);
		for (int i = midIndex; i < totalKeys-1 ; ++i) 
		{System.out.println(this.getKey(i));
			newNode.insertAt(i - midIndex, this.getKey(i), this.getRecord(i));
			numberOfKeys--;
		}
		
		//insert the new key
		if(keyIndex < totalKeys / 2) {
			this.insertAt(keyIndex, key, recordReference);}
		else
			newNode.insertAt(keyIndex - midIndex, key, recordReference);
		
		if(this.next!=null)
		System.out.println("old next index " + this.next.index);

		//set next pointers
		newNode.setNext(this.getNext());
		this.setNext(newNode);
		if(!this.isRoot()) System.out.println("new next index " + this.next.index);
		
		return newNode;
	}
	
	/**
	 * finds the index at which the passed key must be located 
	 * @param key the key to be checked for its location
	 * @return the expected index of the key
	 */
	public int findIndex(T key) 
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
	 * returns the record reference with the passed key and null if does not exist
	 */
	@Override
	public ArrayList<Ref> search(T key) 
	{ArrayList<Ref> refs = new ArrayList<Ref>();
		for(int i = 0; i < numberOfKeys; ++i)
			if(this.getKey(i).compareTo(key) == 0)
				refs.add(this.getRecord(i));
		return refs;
	}
	
	@Override
	public ArrayList<Ref> searchLess(T key) 
	{		System.out.println("searchLess at "+this.index);

		ArrayList<Ref> refs = new ArrayList<Ref>();
		for(int i = 0; i < numberOfKeys; ++i)
			if(this.getKey(i).compareTo(key) < 0)
				refs.add(this.getRecord(i));
		return refs;
	} 
	//jjjjbbb

	@Override
	public ArrayList<Ref> searchNotEqual(T key) 
	{		System.out.println("searchNotEqual at "+this.index);

		ArrayList<Ref> refs = new ArrayList<Ref>();
		for(int i = 0; i < numberOfKeys; ++i)
			if(this.getKey(i).compareTo(key) != 0)
				refs.add(this.getRecord(i));
		return refs;
	} 
	
	public ArrayList<Ref> searchgreater(T key) 
	{		System.out.println("searchgreater at "+this.index);

		ArrayList<Ref> refs = new ArrayList<Ref>();
		for(int i = 0; i < numberOfKeys; ++i)
			if(this.getKey(i).compareTo(key) > 0)
				refs.add(this.getRecord(i));
		return refs;
	} 
	public ArrayList<Ref> searchgreaterORequal(T key) 
	{		System.out.println("searchgreaterORequal at "+this.index);

		ArrayList<Ref> refs = new ArrayList<Ref>();
		for(int i = 0; i < numberOfKeys; ++i)
			if(this.getKey(i).compareTo(key) > 0||this.getKey(i).compareTo(key) == 0)
				refs.add(this.getRecord(i));
		return refs;
	} 
	public ArrayList<Ref> searchLessORequal(T key) 
	{		System.out.println("searchlessORequal at "+this.index);

		ArrayList<Ref> refs = new ArrayList<Ref>();
		for(int i = 0; i < numberOfKeys; ++i)
			if(this.getKey(i).compareTo(key) <= 0)
				refs.add(this.getRecord(i));
		return refs;
	} 
	
	@Override
	public void updateRef(T key, String oldPage, String newPage, Date td) 
	{ArrayList<Ref> refs = new ArrayList<Ref>();
		for(int i = 0; i < numberOfKeys; ++i) {
			if(this.getKey(i).compareTo(key) == 0 && this.getRecord(i).getPage().equals(oldPage))
				records[i]= new Ref(newPage,td);
		break;}
	}
	
	/**
	 * delete the passed key from the B+ tree
	 */
	public boolean delete(T key, BPTreeInnerNode<T> parent, int ptr, Date td) 
	{
		System.out.println("deleting in node " + this.index);

		for(int i = 0; i < numberOfKeys; ++i)
			if(keys[i].compareTo(key) == 0 && records[i].getIndexInPage().compareTo(td)==0)
			{
				this.deleteAt(i);
				if(i == 0 && ptr > 0)
				{
					//update key at parent
					parent.setKey(ptr - 1, this.getFirstKey());
				}
				//check that node has enough keys
				if(!this.isRoot() && numberOfKeys < this.minKeys())
				{
					//1.try to borrow
					if(borrow(parent, ptr))
						return true;
					//2.merge
					merge(parent, ptr);
				}
				return true;
			}
		return false;
	}
	
	/**
	 * delete a key at the specified index of the node
	 * @param index the index of the key to be deleted
	 */
	public void deleteAt(int index)
	{
		for(int i = index; i < numberOfKeys - 1; ++i)
		{
			keys[i] = keys[i+1];
			records[i] = records[i+1];
		}
		numberOfKeys--;
	}
	
	/**
	 * tries to borrow a key from the left or right sibling
	 * @param parent the parent of the current node
	 * @param ptr the index of the parent pointer that points to this node 
	 * @return true if borrow is done successfully and false otherwise
	 */
	public boolean borrow(BPTreeInnerNode<T> parent, int ptr)
	{
		//check left sibling
		if(ptr > 0)
		{
			BPTreeLeafNode<T> leftSibling = (BPTreeLeafNode<T>) parent.getChild(ptr-1);
			if(leftSibling.numberOfKeys > leftSibling.minKeys())
			{
				this.insertAt(0, leftSibling.getLastKey(), leftSibling.getLastRecord());		
				leftSibling.deleteAt(leftSibling.numberOfKeys - 1);
				parent.setKey(ptr - 1, keys[0]);
				return true;
			}
		}
		
		//check right sibling
		if(ptr < parent.numberOfKeys)
		{
			BPTreeLeafNode<T> rightSibling = (BPTreeLeafNode<T>) parent.getChild(ptr+1);
			if(rightSibling.numberOfKeys > rightSibling.minKeys())
			{
				this.insertAt(numberOfKeys, rightSibling.getFirstKey(), rightSibling.getFirstRecord());
				rightSibling.deleteAt(0);
				parent.setKey(ptr, rightSibling.getFirstKey());
				return true;
			}
		}
		return false;
	}
	
	/**
	 * merges the current node with its left or right sibling
	 * @param parent the parent of the current node
	 * @param ptr the index of the parent pointer that points to this node 
	 */
	public void merge(BPTreeInnerNode<T> parent, int ptr)
	{
		if(ptr > 0)
		{
			//merge with left
			BPTreeLeafNode<T> leftSibling = (BPTreeLeafNode<T>) parent.getChild(ptr-1);
			leftSibling.merge(this);
			parent.deleteAt(ptr-1);			
		}
		else
		{
			//merge with right
			BPTreeLeafNode<T> rightSibling = (BPTreeLeafNode<T>) parent.getChild(ptr+1);
			this.merge(rightSibling);
			parent.deleteAt(ptr);
		}
	}
	
	/**
	 * merge the current node with the specified node. The foreign node will be deleted
	 * @param foreignNode the node to be merged with the current node
	 */
	public void merge(BPTreeLeafNode<T> foreignNode)
	{
		for(int i = 0; i < foreignNode.numberOfKeys; ++i)
			this.insertAt(numberOfKeys, foreignNode.getKey(i), foreignNode.getRecord(i));
		
		this.setNext(foreignNode.getNext());
	}

	@Override
	public void writeToFile() {
		try {FileOutputStream file = new FileOutputStream(filename);
		ObjectOutputStream out = new ObjectOutputStream(file);
		out.writeObject(this.keys);
		out.writeObject(this.records);
		out.close();
		file.close();}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void readFromFile() {
		try {
			FileInputStream file = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(file);
			this.keys = (Comparable<T>[]) in.readObject();
			this.records = (Ref[]) in.readObject();
			in.close();
			file.close();		
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
}