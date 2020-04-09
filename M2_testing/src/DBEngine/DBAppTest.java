package DBEngine;
import java.util.Hashtable;
import java.util.Iterator;

public class DBAppTest {

	public static void main(String [] args) throws DBAppException, ClassNotFoundException {
		String strTableName = "Student";
		DBApp dbApp = new DBApp( );
		dbApp.init();
		
		Hashtable htblColNameType = new Hashtable( );
		htblColNameType.put("id", "java.lang.Integer");
		htblColNameType.put("name", "java.lang.String");
		htblColNameType.put("gpa", "java.lang.double");
		dbApp.createTable( strTableName, "id", htblColNameType );
		
//	
		
		
		
		Hashtable htblColNameValue = new Hashtable( );
		
		htblColNameValue.put("id", new Integer( 2343432 ));
		htblColNameValue.put("name", new String("Ahmed Noor" ) );
		htblColNameValue.put("gpa", new Double( 0.95 ) );
		dbApp.insertIntoTable( strTableName , htblColNameValue );
		htblColNameValue.clear( );
		
		htblColNameValue.put("id", new Integer( 453455 ));
		htblColNameValue.put("name", new String("Ahmed Noor" ) );
		htblColNameValue.put("gpa", new Double( 0.88 ) );
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
		
		


		htblColNameValue.put("id", new Integer( 78452 ));
		htblColNameValue.put("name", new String("alyaa Noor" ) );
		//
		htblColNameValue.put("gpa", new Double( 0.88 ) );
		dbApp.insertIntoTable( strTableName , htblColNameValue );
		htblColNameValue.clear( );
				
		
		
		
		dbApp.createBTreeIndex("Student", "name");
		dbApp.createBTreeIndex("Student", "gpa");

		
	
		htblColNameValue.put("id", new Integer( 12 ));
		htblColNameValue.put("name", new String("salma Noor" ) );
		htblColNameValue.put("gpa", new Double( 0.88 ) );
		dbApp.insertIntoTable( strTableName , htblColNameValue );
		htblColNameValue.clear( );
		
		
		
		
////		htblColNameValue.put("id", new Integer( 23498 ));
//		htblColNameValue.put("name", new String("Ahmed Noor" ) );
////		htblColNameValue.put("gpa", new Double( 1.5 ) );
//		System.out.println("deleting..........................");
//		dbApp.deleteFromTable( strTableName , htblColNameValue );
//		htblColNameValue.clear( );//delete is case sensetive
//		
		
		
		
		SQLTerm[] arrSQLTerms;
		arrSQLTerms = new SQLTerm[3];
		
//		query1 = new SQLTerm("Student", "name", "=","John Noor");
		SQLTerm query1 = new SQLTerm();
		SQLTerm query2 = new SQLTerm();
		SQLTerm query3 = new SQLTerm();
		arrSQLTerms[0] = query1;
		arrSQLTerms[1] = query2;
		arrSQLTerms[2] = query3;
		arrSQLTerms[0].strTableName = "Student";
		arrSQLTerms[0].strColumnName= "name";
		arrSQLTerms[0].strOperator = "=";
		arrSQLTerms[0].objValue = "Ahmed Noor";
		arrSQLTerms[1].strTableName = "Student";
		arrSQLTerms[1].strColumnName= "gpa";
		arrSQLTerms[1].strOperator = "<";
		arrSQLTerms[1].objValue = new Double( 0.88 );
		arrSQLTerms[2].strTableName = "Student";
		arrSQLTerms[2].strColumnName= "id";
		arrSQLTerms[2].strOperator = "<=";
		arrSQLTerms[2].objValue = new Integer( 12 );
		String[]strarrOperators = new String[2];
		strarrOperators[0] = "AND";
		strarrOperators[1] = "OR";
		Iterator it = dbApp.selectFromTable(arrSQLTerms , strarrOperators);
		while(it.hasNext()) {
			System.out.println(it.next());
		}

//		t.executeQuery(arrSQLTerms[0].strColumnName, arrSQLTerms[0].strOperator,arrSQLTerms[0].objValue);
		// select * from Student where name = “John Noor” or gpa = 1.5;		
		
		
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



		
	}
}
