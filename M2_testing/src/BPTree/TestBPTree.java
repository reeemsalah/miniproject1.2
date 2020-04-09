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

		BPTree<Integer> tree = new BPTree<Integer>(4);
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
//			tree.insert(23, new Ref("p4", 2));
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
			tree.delete(18);
			System.out.println(tree);
			tree.delete(18);
			System.out.println(tree);
			break;
		}
		
		
//		System.out.println(tree.root);
		int entry= sc.nextInt();
		System.out.println(entry +" can go to pages "+tree.getInsertPage(entry));
//		System.out.println(18 +" is in pages "+tree.getDeletePage(18));
		
		
		while(true) 
		{
			int x = sc.nextInt();
			if(x == -1)
				break;
//			tree.delete(x);
			System.out.println(tree.searchLess(x));
		}
		
		
		sc.close();
	
		
	}	
	
	
}