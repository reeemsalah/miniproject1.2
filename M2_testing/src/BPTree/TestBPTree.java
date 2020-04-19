package BPTree;

import java.util.Date;
import java.util.Scanner;

import DBEngine.Page;

public class TestBPTree {

	public static void main(String[] args) 
	{
		Page p1 = new Page("p1", 0,0,0);
		Page p2 = new Page("p2", 0,0,0);
		Page p3 = new Page("p3", 0,0,0);
		Page p4 = new Page("p4", 0,0,0);
//<<<<<<< HEAD

//		BPTree<Integer> tree = new BPTree<Integer>(2);

		//tree.delete(18);
		//System.out.println(tree);

//=======
Date d = new Date();
		BPTree<Integer> tree = new BPTree<Integer>(4);
//>>>>>>> branch 'master' of https://github.com/reeemsalah/miniproject1.2.git
		Scanner sc = new Scanner(System.in);
		while(true) 
		{
			tree.insert(5, new Ref("p5",d));
			System.out.println(tree.toString());
			
			tree.insert(0, new Ref("p0",d));
			System.out.println(tree.toString());
			
			tree.insert(1, new Ref("p1",d));
			System.out.println(tree.toString());
			
			tree.insert(2, new Ref("p2",d));
			System.out.println(tree.toString());
			
			tree.insert(10, new Ref("p10",d));
			System.out.println(tree.toString());
			
			tree.insert(7, new Ref("p7",d));
			System.out.println(tree.toString());

			tree.insert(15, new Ref("p15", d));
			System.out.println(tree.toString());

			tree.insert(18, new Ref("p18", d));
			System.out.println(tree.toString());

			tree.insert(18, new Ref("p18", d));
			System.out.println(tree.toString());

			tree.insert(18, new Ref("p18", d));
			System.out.println(tree.toString());

			tree.insert(4, new Ref("p4", d));
			System.out.println(tree.toString());

			tree.insert(23, new Ref("p23", d));
			System.out.println(tree.toString());
			
			tree.insert(25, new Ref("p25", d));
			System.out.println(tree.toString());

			tree.insert(18, new Ref("p18", d));
			System.out.println(tree.toString());
			
			tree.insert(18, new Ref("p18", d));
			System.out.println(tree.toString());

			tree.insert(16, new Ref("p16", d));
			System.out.println(tree.toString());

			tree.insert(9, new Ref("p9", d));
			
			
			System.out.println(tree.toString());
		//	tree.delete(4,d);
			//tree.insert(11,new Ref("p4",d));
			//System.out.println(tree);
			//tree.update(10,11,d);
			//System.out.println(tree.toString());
//			tree.delete(18,d);
//			System.out.println(tree);
//			tree.delete(18,d);
//			System.out.println(tree);
//			tree.delete(18,d);
//			System.out.println(tree);
//			tree.delete(18,d);
//			System.out.println(tree);
			break;
		}
		
//<<<<<<< HEAD
//=======
		
//		System.out.println(tree.root);
//>>>>>>> branch 'master' of https://github.com/reeemsalah/miniproject1.2.git
		
//		int entry= sc.nextInt();
//		System.out.println(entry +" can go to pages "+tree.getInsertPage(entry));
		
		
		while(true) 
		{
			int x = sc.nextInt();
			if(x == -1)
				break;
//			tree.delete(x);
			System.out.println(tree.searchgreater(x));
		}
		
		
		sc.close();
		
	
		
	}	
	
	
}