package DBEngine;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Iterator;

public class DBAppTest {

	public static void main(String[] args) throws DBAppException, ClassNotFoundException, IOException {
		String strTableName = "Student";
		DBApp dbApp = new DBApp();
		dbApp.init();

//		create table student 
		
		Hashtable htblColNameType = new Hashtable();
		htblColNameType.put("id", "java.lang.Integer");
		htblColNameType.put("name", "java.lang.String");
		htblColNameType.put("gpa", "java.lang.double");
		dbApp.createTable(strTableName, "id", htblColNameType);

//		insertions (7)

		Hashtable htblColNameValue = new Hashtable();

		htblColNameValue.put("id", new Integer(2));
		htblColNameValue.put("name", new String("Ahmed Noor"));
		htblColNameValue.put("gpa", new Double(0.95));
		dbApp.insertIntoTable(strTableName, htblColNameValue);
		htblColNameValue.clear();
		//INSERTING
		htblColNameValue.put("id", new Integer(1));
		htblColNameValue.put("name", new String("Ahmed Noor"));
		htblColNameValue.put("gpa", new Double(0.88));
		dbApp.insertIntoTable(strTableName, htblColNameValue);
		htblColNameValue.clear();

		htblColNameValue.put("id", new Integer(6));
		htblColNameValue.put("name", new String("Dalia Noor"));
		htblColNameValue.put("gpa", new Double(1.25));
		dbApp.insertIntoTable(strTableName, htblColNameValue);
		htblColNameValue.clear();

		htblColNameValue.put("id", new Integer(4));
		htblColNameValue.put("name", new String("John Noor"));
		htblColNameValue.put("gpa", new Double(1.5));
		dbApp.insertIntoTable(strTableName, htblColNameValue);
		htblColNameValue.clear();

		htblColNameValue.put("id", new Integer(5));
		htblColNameValue.put("name", new String("Zaky Noor"));
		htblColNameValue.put("gpa", new Double(0.88));
		dbApp.insertIntoTable(strTableName, htblColNameValue);
		htblColNameValue.clear();

		htblColNameValue.put("id", new Integer(3));
		htblColNameValue.put("name", new String("alyaa Noor"));
		htblColNameValue.put("gpa", new Double(0.88));
		dbApp.insertIntoTable(strTableName, htblColNameValue);
		htblColNameValue.clear();

		htblColNameValue.put("id", new Integer( 12 ));
		htblColNameValue.put("name", new String("salma Noor" ) );
		htblColNameValue.put("gpa", new Double( 0.88 ) );
		dbApp.insertIntoTable( strTableName , htblColNameValue );
		htblColNameValue.clear( );
		
		//creating index
		dbApp.createBTreeIndex(strTableName, "id");
		
		
//		update (1)
		htblColNameValue.put("gpa", 1.5);
		dbApp.updateTable(strTableName, "12", htblColNameValue);

//		insertions(2)
		htblColNameValue.put("id", new Integer(5));
		htblColNameValue.put("name", new String("Zaky Noor"));
		htblColNameValue.put("gpa", new Double(0.88));
		dbApp.insertIntoTable(strTableName, htblColNameValue);
		htblColNameValue.clear();

		htblColNameValue.put("id", new Integer(6));
		htblColNameValue.put("name", new String("alyaa Noor"));
		htblColNameValue.put("gpa", new Double(0.88));
		dbApp.insertIntoTable(strTableName, htblColNameValue);
		htblColNameValue.clear(); 

		
		  //DELETING EVERYTHING
//		
//		  htblColNameValue.put("name", new String("alyaa Noor" ) ); 
//		  System.out.println("deleting..........................");
//		  dbApp.deleteFromTable( strTableName , htblColNameValue );
//		  htblColNameValue.clear( ); //delete is case sensetive
//		  
//		  htblColNameValue.put("name", new String("salma Noor" ) ); //
//		  System.out.println("deleting..........................");
//		  dbApp.deleteFromTable( strTableName , htblColNameValue );
//		  htblColNameValue.clear( ); 
//		  
//		  htblColNameValue.put("name", new String("Zaky Noor" ) ); // htblColNameValue.put("gpa", new Double( 1.5 ) );
//		  // System.out.println("deleting..........................");
//		  dbApp.deleteFromTable( strTableName , htblColNameValue );
//		  htblColNameValue.clear( );
//		  
//		  htblColNameValue.put("name", new String("Ahmed Noor" ) ); //
////		  htblColNameValue.put("gpa", new Double( 1.5 ) ); //
//		  System.out.println("deleting..........................");
//		  dbApp.deleteFromTable( strTableName , htblColNameValue );
//		  htblColNameValue.clear( );
//		  
//		  htblColNameValue.put("name", new String("John Noor" ) ); //
////		  htblColNameValue.put("gpa", new Double( 1.5 ) ); //
//		  System.out.println("deleting..........................");
//		  dbApp.deleteFromTable( strTableName , htblColNameValue );
//		  htblColNameValue.clear( );
//		  
//		  htblColNameValue.put("name", new String("Dalia Noor" ) ); //
////		  htblColNameValue.put("gpa", new Double( 1.5 ) ); //
//		  System.out.println("deleting..........................");
//		  dbApp.deleteFromTable( strTableName , htblColNameValue );
//		  htblColNameValue.clear( );
//		
//			htblColNameValue.put("id", new Integer(6));
//			htblColNameValue.put("name", new String("alyaa Noor"));
//			htblColNameValue.put("gpa", new Double(0.88));
//			dbApp.insertIntoTable(strTableName, htblColNameValue);
//			htblColNameValue.clear(); 
		  
//		try
//        { 
//            Files.deleteIfExists(Paths.get("Student_1.ser")); 
//        } 
//        catch(NoSuchFileException e) 
//        { 
//            System.out.println("No such file/directory exists"); 
//        } 
//        catch(DirectoryNotEmptyException e) 
//        { 
//            System.out.println("Directory is not empty."); 
//        } 
//        catch(IOException e) 
//        { 
//            System.out.println("Invalid permissions."); 
//        } 
//          
//        System.out.println("Deletion successful."); 
//    	
/*		  
		File file = new File("Student_1.ser");
		
		System.out.println(file.delete());
		
		if (file.createNewFile()) {
			System.out.println("file.txt File Created in Project root directory");
		} else
			System.out.println("File file.txt already exists in the project root directory");

		File file2 = new File("Student_2.ser");
		if (file.createNewFile()) {
			System.out.println("stud2 File Created in Project root directory");
		} else
			System.out.println("stud2 file.txt already exists in the project root directory");
*/
//		SQLTerm[] arrSQLTerms;
//		arrSQLTerms = new SQLTerm[3];
//		
//		SQLTerm query1 = new SQLTerm();
//		SQLTerm query2 = new SQLTerm();
//		SQLTerm query3 = new SQLTerm();
//		arrSQLTerms[0] = query1;
//		arrSQLTerms[1] = query2;
//		arrSQLTerms[2] = query3;
//		arrSQLTerms[0].strTableName = "Student";
//		arrSQLTerms[0].strColumnName= "name";
//		arrSQLTerms[0].strOperator = "=";
//		arrSQLTerms[0].objValue = "Ahmed Noor";
//		arrSQLTerms[1].strTableName = "Student";
//		arrSQLTerms[1].strColumnName= "gpa";
//		arrSQLTerms[1].strOperator = "<";
//		arrSQLTerms[1].objValue = new Double( 0.88 );
//		arrSQLTerms[2].strTableName = "Student";
//		arrSQLTerms[2].strColumnName= "gpa";
//		arrSQLTerms[2].strOperator = ">";
//		arrSQLTerms[2].objValue = new Double( 1.0 );
//		String[]strarrOperators = new String[2];
//		strarrOperators[0] = "AND";
//		strarrOperators[1] = "OR";
//		Iterator it = dbApp.selectFromTable(arrSQLTerms , strarrOperators);
//		while(it.hasNext()) {
//			System.out.println(it.next());
//		}

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
		

		String strTableName2 = "Products";
		DBApp dbApp2 = new DBApp( );
		dbApp2.init();
		
		
		Hashtable htblColNameType2 = new Hashtable( );
		htblColNameType2.put("available", "java.lang.Boolean");
		htblColNameType2.put("name", "java.lang.String");
		htblColNameType2.put("rating", "java.lang.Double");
		dbApp2.createTable( strTableName2, "name", htblColNameType2 );
		
		Hashtable htblColNameValue2 = new Hashtable( );

		htblColNameValue2.put("available",  true );
		htblColNameValue2.put("name", new String("bag item" ) );
		htblColNameValue2.put("rating", new Double( 1.5 ) );
		dbApp2.insertIntoTable( strTableName2 , htblColNameValue2 );
		htblColNameValue2.clear( );
		
		htblColNameValue2.put("available",  false );
		htblColNameValue2.put("name", new String("pen item" ) );
		htblColNameValue2.put("rating", new Double( 0.77 ) );
		dbApp2.insertIntoTable( strTableName2 , htblColNameValue2 );
		htblColNameValue2.clear( );
		
		htblColNameValue2.put("available",  true );
		htblColNameValue2.put("name", new String("book item" ) );
		htblColNameValue2.put("rating", new Double( 0.77 ) );
		dbApp2.insertIntoTable( strTableName2 , htblColNameValue2 );
		htblColNameValue2.clear( );
		
		htblColNameValue2.put("available",  true );
		htblColNameValue2.put("name", new String("tv item" ) );
		htblColNameValue2.put("rating", new Double( 0.77 ) );
		dbApp2.insertIntoTable( strTableName2 , htblColNameValue2 );
		htblColNameValue2.clear( );
		
		htblColNameValue2.put("available",  true );
		htblColNameValue2.put("name", new String("phone item" ) );
		htblColNameValue2.put("rating", new Double( 0.77 ) );
		dbApp2.insertIntoTable( strTableName2 , htblColNameValue2 );
		htblColNameValue2.clear( );
		
		SQLTerm[] arrSQLTerms;
		arrSQLTerms = new SQLTerm[3];
		
		SQLTerm query1 = new SQLTerm();
		SQLTerm query2 = new SQLTerm();
		SQLTerm query3 = new SQLTerm();
		arrSQLTerms[0] = query1;
		arrSQLTerms[1] = query2;
		arrSQLTerms[2] = query3;
		arrSQLTerms[0].strTableName = "Products";
		arrSQLTerms[0].strColumnName= "name";
		arrSQLTerms[0].strOperator = "=";
		arrSQLTerms[0].objValue = "bag item";
		arrSQLTerms[1].strTableName = "Products";
		arrSQLTerms[1].strColumnName= "available";
		arrSQLTerms[1].strOperator = ">";
		arrSQLTerms[1].objValue = new Boolean( false );
		arrSQLTerms[2].strTableName = "Products";
		arrSQLTerms[2].strColumnName= "rating";
		arrSQLTerms[2].strOperator = "<=";
		arrSQLTerms[2].objValue = new Double( 1.0 );
		String[]strarrOperators = new String[2];
		strarrOperators[0] = "XOR";
		strarrOperators[1] = "OR";
		Iterator it = dbApp.selectFromTable(arrSQLTerms , strarrOperators);
		while(it.hasNext()) {
			System.out.println(it.next());
		}

	}
}
