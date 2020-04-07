package DBEngine;

public class DBAppTest2 {

	public static void main(String[] args) {
		int [] xs1 = {0,2,2,0};
		int [] ys1 = {2,0,0,2};
		Region r1 = new Region(xs1,ys1,4 );
		System.out.println(r1.getClass().getCanonicalName());
		int [] xs2 = {0,1,1,0};
		int [] ys2 = {1,0,0,1};
		Comparable a = new Region(xs2,ys2,4);
		System.out.println(a.compareTo(r1));
	}
}
