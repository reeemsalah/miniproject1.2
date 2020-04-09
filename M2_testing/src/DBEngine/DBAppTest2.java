package DBEngine;

import java.util.Hashtable;

public class DBAppTest2 {

	public static void main(String[] args) throws DBAppException {
//		int [] xs1 = {0,2,2,0};
//		int [] ys1 = {2,0,0,2};
//		Region r1 = new Region(xs1,ys1,4 );
//		System.out.println(r1.getClass().getCanonicalName());
//		int [] xs2 = {0,1,1,0};
//		int [] ys2 = {1,0,0,1};
//		Comparable a = new Region(xs2,ys2,4);
//		System.out.println(a.compareTo(r1));

//		int [] xs1 = {0,2,2,0};
//		int [] ys1 = {2,0,0,2};
//		Region r1 = new Region(xs1,ys1,4 );
//		System.out.println(r1.getClass().getCanonicalName());
//		
//		int [] xs2 = {0,2,5,0};
//		int [] ys2 = {2,0,-1,2};
//		Region r2 = new Region(xs2,ys2,4 );
//		System.out.println(r1.getClass().getCanonicalName());
//		
		Region r1 = new Region(new int[] { 0, 4 , 0 , 4 }, new int[] { 0, 0 , 4 , 4 }, 4);
		Region r2 = new Region(new int[] { 0, 5, 0 , 5 }, new int[] { 0, 0, 5 , 5 }, 4);
		Region r3 = new Region(new int[] { 0, 7, 0 , 7}, new int[] { 0, 0, 7 , 7}, 4);
		Region r4 = new Region(new int[] { 0, 9 , 0 , 9 }, new int[] { 0, 0 , 9 , 9 }, 4);
		Region r5 = new Region(new int[] { 0, 15, 0 , 15}, new int[] { 0, 0, 15 , 15}, 4);
		Region r6 = new Region(new int[] { 0, 18, 0 , 18}, new int[] { 0, 0, 18 , 18}, 4);
		Region r7 = new Region(new int[] { 0, 18, 0 , 18}, new int[] { 0, 0, 18 , 18}, 4);
		Region r8 = new Region(new int[] { 0, 18, 0 , 18}, new int[] { 0, 0, 18 , 18}, 4);
		Region r9 = new Region(new int[] { 0, 19, 0 , 19}, new int[] { 0, 0, 19 , 19}, 4);
		Region r10 = new Region(new int[] { 0, 20, 0 , 20}, new int[] { 0, 0, 20 , 20}, 4);
		Region r11 = new Region(new int[] { 0, 20, 0 , 20}, new int[] { 0, 0, 20 , 20}, 4);
		Region r12 = new Region(new int[] { 0, 23, 0 , 23}, new int[] { 0, 0, 23 , 23}, 4);
		Region r_test = new Region(new int[] { 0, 17, 0, 17 }, new int[] { 0, 0, 17 , 17}, 4);
		
		String strTableName2 = "Places";
		DBApp dbApp2 = new DBApp( );
		dbApp2.init();
		
		
		Hashtable htblColNameType2 = new Hashtable( );
		htblColNameType2.put("place", "DBEngine.Region");
		htblColNameType2.put("name", "java.lang.String");
		htblColNameType2.put("rating", "java.lang.double");
		dbApp2.createTable( strTableName2, "name", htblColNameType2 );
		
		Hashtable htblColNameValue2 = new Hashtable( );
		
		htblColNameValue2.put("place",  r1 );
		htblColNameValue2.put("name", new String("rehab" ) );
		htblColNameValue2.put("rating", new Double( 0.77 ) );
		dbApp2.insertIntoTable( strTableName2 , htblColNameValue2 );
		htblColNameValue2.clear( );
		
		
		htblColNameValue2.put("place",  r5 );
		htblColNameValue2.put("name", new String("guc" ) );
		htblColNameValue2.put("rating", new Double( 0.7 ) );
		dbApp2.insertIntoTable( strTableName2 , htblColNameValue2 );//		
		htblColNameValue2.clear( );

		htblColNameValue2.put("place",  r4 );
		htblColNameValue2.put("name", new String("home" ) );
		htblColNameValue2.put("rating", new Double( 0.97 ) );
		dbApp2.insertIntoTable( strTableName2 , htblColNameValue2 );//		
		htblColNameValue2.clear( );
		
		htblColNameValue2.put("place",  r3 );
		htblColNameValue2.put("name", new String("nekoAfe" ) );
		htblColNameValue2.put("rating", new Double( 0.88 ) );
		dbApp2.insertIntoTable( strTableName2 , htblColNameValue2 );//		
		htblColNameValue2.clear( );
		
		dbApp2.createRTreeIndex(strTableName2, "place");
		
		htblColNameValue2.put("place",  r2 );
		htblColNameValue2.put("name", new String("park" ) );
		htblColNameValue2.put("rating", new Double( 0.5 ) );
		dbApp2.insertIntoTable( strTableName2 , htblColNameValue2 );//		
		htblColNameValue2.clear( );

//		htblColNameValue2.put("place", r1);
//		htblColNameValue2.put("name", new String("rehab" ) );
//		htblColNameValue2.put("rating", new Double( 1.25 ) );
//		dbApp2.updateTable( strTableName2, "5674567" , htblColNameValue2 );
//		htblColNameValue2.clear( );
//		
		htblColNameValue2.put("place", r1);
//		htblColNameValue2.put("name", new String("rehab" ) );
//		htblColNameValue2.put("rating", new Double( 1.5 ) );
		dbApp2.deleteFromTable( strTableName2 , htblColNameValue2 );
		htblColNameValue2.clear( );
		
		dbApp2.createBTreeIndex(strTableName2, "name");
		dbApp2.createBTreeIndex(strTableName2, "rating");

				
	}
}
