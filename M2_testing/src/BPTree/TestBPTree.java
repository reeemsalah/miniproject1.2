package BPTree;

import java.util.Scanner;

public class TestBPTree {

	public static void main(String[] args) 
	{
		
		BPTree<Integer> tree = new BPTree<Integer>(2);
		Scanner sc = new Scanner(System.in);
		while(true) 
		{
			tree.insert(5, new Ref("p1", 1));
			tree.insert(7, new Ref("p1", 2));
			tree.insert(15, new Ref("p2", 1));
			tree.insert(18, new Ref("p2", 2));
			tree.insert(18, new Ref("p3", 1));
			tree.insert(20, new Ref("p4", 1));

			
			System.out.println(tree.toString());
			break;
		}
		
//		System.out.println(tree.root);
		int entry= sc.nextInt();
		System.out.println(entry +" can go to pages "+tree.getInsertPage(entry));
		System.out.println(18 +" can go to pages "+tree.getDeletePage(15));
		
		
		while(true) 
		{
			int x = sc.nextInt();
			if(x == -1)
				break;
			tree.delete(x);
			System.out.println(tree.toString());
		}
		
		
		sc.close();
	
		
	}	
	
	
}