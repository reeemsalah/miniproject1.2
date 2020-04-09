package BPTree;

import java.util.Scanner;

import DBEngine.Page;

public class TestBPTree {

	public static void main(String[] args) 
	{
		Page p1 = new Page("p1", 0,0,0);
		Page p2 = new Page("p2", 0,0,0);
		Page p3 = new Page("p3", 0,0,0);
		Page p4 = new Page("p4", 0,0,0);

		BPTree<Integer> tree = new BPTree<Integer>(2);

		tree.insert(5, new Ref("p1", 0));
		tree.insert(7, new Ref("p1", 0));
		tree.insert(15, new Ref("p2", 0));
		//first
		tree.insert(18, new Ref("p3", 0));
		//second
		tree.insert(18, new Ref("p3", 0));
		//third
		tree.insert(18, new Ref("p3", 0));
		tree.insert(4, new Ref("p1", 0));
		tree.insert(23, new Ref("p4", 0));
		tree.insert(25, new Ref("p4", 0));
		//forth
		tree.insert(18, new Ref("p3", 0));
		//fifth
		tree.insert(18, new Ref("p4", 0));
		tree.insert(16, new Ref("p2", 0));
		tree.insert(9, new Ref("p2", 0));

		System.out.println(tree.toString());
		System.out.println(tree.search(18));

		System.out.println(tree.delete(18));
		System.out.println(tree);
		System.out.println(tree.search(18));

		System.out.println(tree.delete(18));
		System.out.println(tree);
		System.out.println(tree.search(18));

		System.out.println(tree.delete(18));
		System.out.println(tree);
		System.out.println(tree.search(18));
		//tree.delete(18);
		//System.out.println(tree);
		/*
		Scanner sc = new Scanner(System.in);
		while(true) 
		{
			tree.insert(5, new Ref("p1", 1));
			tree.insert(7, new Ref("p1", 2));
			tree.insert(15, new Ref("p2", 1));
			tree.insert(18, new Ref("p3", 2));
			tree.insert(18, new Ref("p3", 1));
			tree.insert(18, new Ref("p3", 1));
			tree.insert(4, new Ref("p1", 1));
			tree.insert(23, new Ref("p4", 2));
			tree.insert(25, new Ref("p4", 2));
			tree.insert(18, new Ref("p3", 1));
			tree.insert(18, new Ref("p4", 2));
			tree.insert(16, new Ref("p2", 1));
			tree.insert(9, new Ref("p2", 1));

			
			System.out.println(tree.toString());
			tree.delete(18);
			System.out.println(tree);
			tree.delete(18);
			System.out.println(tree);
			tree.delete(18);
			System.out.println(tree);
			//tree.delete(18);
			//System.out.println(tree);
			break;
		}
		
		int entry= sc.nextInt();
		System.out.println(entry +" can go to pages "+tree.getInsertPage(entry));
		
		
		while(true) 
		{
			int x = sc.nextInt();
			if(x == -1)
				break;
//			tree.delete(x);
			System.out.println(tree.search(x));
		}
		
		
		sc.close();
		*/
	
		
	}	
	
	
}