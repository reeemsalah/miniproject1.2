import java.util.Hashtable;

public class DBAppTest {

	public static void main(String [] args) throws DBAppException {
		String strTableName = "Student";
		DBApp dbApp = new DBApp( );
		dbApp.init();
		
		Hashtable htblColNameType = new Hashtable( );
		htblColNameType.put("id", "java.lang.Integer");
		htblColNameType.put("name", "java.lang.String");
		htblColNameType.put("gpa", "java.lang.double");
		dbApp.createTable( strTableName, "name", htblColNameType );
		
		Hashtable htblColNameValue = new Hashtable( );
		
		htblColNameValue.put("id", new Integer( 2343432 ));
		htblColNameValue.put("name", new String("Ahmed Noor" ) );
		htblColNameValue.put("gpa", new Double( 0.95 ) );
		dbApp.insertIntoTable( strTableName , htblColNameValue );
		htblColNameValue.clear( );
		
		htblColNameValue.put("id", new Integer( 453455 ));
		htblColNameValue.put("name", new String("Ahmed Noor" ) );
		htblColNameValue.put("gpa", new Double( 0.95 ) );
		dbApp.insertIntoTable( strTableName , htblColNameValue );
		htblColNameValue.clear( );
		
		htblColNameValue.put("id", new Integer( 5674567 ));
		htblColNameValue.put("name", new String("Dalia Noor" ) );
		htblColNameValue.put("gpa", new Double( 1.25 ) );
		dbApp.insertIntoTable( strTableName , htblColNameValue );
		htblColNameValue.clear( );
		
		htblColNameValue.put("id", new Integer( 23498 ));
		htblColNameValue.put("name", new String("John Noor" ) );
		htblColNameValue.put("gpa", new Double( 1.5 ) );
		dbApp.insertIntoTable( strTableName , htblColNameValue );
		htblColNameValue.clear( );
		
		htblColNameValue.put("id", new Integer( 78452 ));
		htblColNameValue.put("name", new String("Zaky Noor" ) );
		htblColNameValue.put("gpa", new Double( 0.88 ) );
		dbApp.insertIntoTable( strTableName , htblColNameValue );
		
		

//		htblColNameValue.put("id", new Integer( 5674567 ));
//		htblColNameValue.put("name", new String("soaad Noor" ) );
//		htblColNameValue.put("gpa", new Double( 1.25 ) );
//		dbApp.updateTable( strTableName, "5674567" , htblColNameValue );
//		htblColNameValue.clear( );
		
////		htblColNameValue.put("id", new Integer( 23498 ));
//		htblColNameValue.put("name", new String("John Noor" ) );
//		htblColNameValue.put("gpa", new Double( 1.5 ) );
//		dbApp.deleteFromTable( strTableName , htblColNameValue );
//		htblColNameValue.clear( );
		
		

		htblColNameValue.put("id", new Integer( 78452 ));
		htblColNameValue.put("name", new String("alyaa Noor" ) );
		htblColNameValue.put("gpa", new Double( 0.88 ) );
		dbApp.insertIntoTable( strTableName , htblColNameValue );
		htblColNameValue.clear( );
		
		dbApp.createBTreeIndex("Student", "id");

		
////		htblColNameValue.put("id", new Integer( 23498 ));
//		htblColNameValue.put("name", new String("ahmed Noor" ) );
////		htblColNameValue.put("gpa", new Double( 1.5 ) );
//		dbApp.deleteFromTable( strTableName , htblColNameValue );
//		htblColNameValue.clear( );//delete is case sensetive
//		
////		htblColNameValue.put("id", new Integer( 23498 ));
//		htblColNameValue.put("name", new String("Ahmed Noor" ) );
////		htblColNameValue.put("gpa", new Double( 1.5 ) );
//		dbApp.deleteFromTable( strTableName , htblColNameValue );
//		htblColNameValue.clear( );
//		
////		htblColNameValue.put("id", new Integer( 23498 ));
//		htblColNameValue.put("name", new String("alyaa Noor" ) );
////		htblColNameValue.put("gpa", new Double( 1.5 ) );
//		dbApp.deleteFromTable( strTableName , htblColNameValue );
//		htblColNameValue.clear( );
//	
//		
////		htblColNameValue.put("id", new Integer( 23498 ));
//		htblColNameValue.put("name", new String("Zaky Noor" ) );
////		htblColNameValue.put("gpa", new Double( 1.5 ) );
//		dbApp.deleteFromTable( strTableName , htblColNameValue );
//		htblColNameValue.clear( );
//		
//		htblColNameValue.put("id", new Integer( 23498 ));
////		htblColNameValue.put("name", new String("soaad Noor" ) );
////		htblColNameValue.put("gpa", new Double( 1.5 ) );
//		dbApp.deleteFromTable( strTableName , htblColNameValue );
//		htblColNameValue.clear( );
//		
////		htblColNameValue.put("id", new Integer( 23498 ));
//		htblColNameValue.put("name", new String("Dalia Noor" ) );
////		htblColNameValue.put("gpa", new Double( 1.5 ) );
//		dbApp.deleteFromTable( strTableName , htblColNameValue );
//		htblColNameValue.clear( );
//		
////		htblColNameValue.put("id", new Integer( 23498 ));
//		htblColNameValue.put("name", new String("Dalia Noor" ) );
////		htblColNameValue.put("gpa", new Double( 1.5 ) );
//		dbApp.deleteFromTable( strTableName , htblColNameValue );
//		htblColNameValue.clear( );
////		
//		String strTableName2 = "Places";
//		DBApp dbApp2 = new DBApp( );
//		dbApp2.init();
//		
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
//		
//		Hashtable htblColNameType2 = new Hashtable( );
//		htblColNameType2.put("place", "Region");
//		htblColNameType2.put("name", "java.lang.String");
//		htblColNameType2.put("rating", "java.lang.double");
//		dbApp2.createTable( strTableName2, "name", htblColNameType2 );
//		
//		Hashtable htblColNameValue2 = new Hashtable( );
//		
//		htblColNameValue2.put("place",  r1 );
//		htblColNameValue2.put("name", new String("rehab" ) );
//		htblColNameValue2.put("rating", new Double( 0.95 ) );
//		dbApp2.insertIntoTable( strTableName2 , htblColNameValue2 );
//		htblColNameValue2.clear( );
//		
//		
//		htblColNameValue2.put("place",  r2 );
//		htblColNameValue2.put("name", new String("guc" ) );
//		htblColNameValue2.put("rating", new Double( 0.88 ) );
//		dbApp2.insertIntoTable( strTableName2 , htblColNameValue2 );
//		
//		
//
//		htblColNameValue2.put("place", r1);
//		htblColNameValue2.put("name", new String("rehab" ) );
//		htblColNameValue2.put("rating", new Double( 1.25 ) );
//		dbApp2.updateTable( strTableName2, "5674567" , htblColNameValue2 );
//		htblColNameValue2.clear( );
//		
//		htblColNameValue2.put("place", r1);
////		htblColNameValue2.put("name", new String("rehab" ) );
////		htblColNameValue2.put("rating", new Double( 1.5 ) );
//		dbApp2.deleteFromTable( strTableName2 , htblColNameValue2 );
//		htblColNameValue2.clear( );
//		
//		
//

		
	}
}
