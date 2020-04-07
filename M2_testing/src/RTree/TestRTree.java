package RTree;
import DBEngine.Region;
import java.util.Scanner;
import BPTree.Ref;

public class TestRTree {

	public static void main(String[] args) 
	{
		RTree<Integer> tree = new RTree<Integer>(2);
		Region r2 = new Region(new int[] { 0, 5, 0 , 5 }, new int[] { 0, 0, 5 , 5 }, 4);
		Region r3 = new Region(new int[] { 0, 7, 0 , 7}, new int[] { 0, 0, 7 , 7}, 4);
		Region r5 = new Region(new int[] { 0, 15, 0 , 15}, new int[] { 0, 0, 15 , 15}, 4);
		Region r8 = new Region(new int[] { 0, 18, 0 , 18}, new int[] { 0, 0, 18 , 18}, 4);
		Region r7 = new Region(new int[] { 0, 18, 0 , 18}, new int[] { 0, 0, 18 , 18}, 4);
		Region r10 = new Region(new int[] { 0, 20, 0 , 20}, new int[] { 0, 0, 20 , 20}, 4);
		Region r_test = new Region(new int[] { 0, 17, 0, 17 }, new int[] { 0, 0, 17 , 17}, 4);
		Region r1 = new Region(new int[] { 0, 4 , 0 , 4 }, new int[] { 0, 0 , 4 , 4 }, 4);
		Region r4 = new Region(new int[] { 0, 9 , 0 , 9 }, new int[] { 0, 0 , 9 , 9 }, 4);
		Region r6 = new Region(new int[] { 0, 18, 0 , 18}, new int[] { 0, 0, 18 , 18}, 4);
		Region r9 = new Region(new int[] { 0, 19, 0 , 19}, new int[] { 0, 0, 19 , 19}, 4);
		Region r11 = new Region(new int[] { 0, 20, 0 , 20}, new int[] { 0, 0, 20 , 20}, 4);
		Region r12 = new Region(new int[] { 0, 23, 0 , 23}, new int[] { 0, 0, 23 , 23}, 4);
		
		
//		Region r7 = new Region(new int[] { 0, 16, 0 , 16}, new int[] { 0, 0, 16 , 16}, 4);
//		System.out.println(r1.getArea());

		Scanner sc = new Scanner(System.in);
		
		while(true) 
		{
//			int x = sc.nextInt();
//			if(x == -1)
			
//				break;
//			int page = sc.nextInt();
//			tree.insert(x, null);
			tree.insert(r1, new Ref("p1", 1));
			tree.insert(r2, new Ref("p1", 2));
			tree.insert(r3, new Ref("p1", 1));
			tree.insert(r4, new Ref("p2", 2));
			tree.insert(r5, new Ref("p2", 1));
			tree.insert(r6, new Ref("p2", 1));
			tree.insert(r7, new Ref("p3", 1));
			tree.insert(r8, new Ref("p3", 2));
			tree.insert(r9, new Ref("p3", 1));
			tree.insert(r10,new Ref("p4", 2));
			tree.insert(r11,new Ref("p4", 1));
			tree.insert(r12,new Ref("p4", 1));
			
			System.out.println(tree.toString());
			break;
		}
		
//		int entry= sc.nextInt();
		System.out.println("r_test" +" can go to pages "+tree.getInsertPage(r7));
		System.out.println("18" +" is in pages "+tree.getDeletePage(r8));

		while(true) 
		{
			int x = sc.nextInt();
			if(x == -1)
				break;
			Region temp = new Region(new int[] { 0, x, 0, x }, new int[] { 0, 0, x , x}, 4);

			tree.delete(temp);
			System.out.println(tree.toString());
		}
		
		
		sc.close();
	}	
	
	
}