package DBEngine;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

import BPTree.BPTree;

public class DBApp {
	private static Hashtable<String, Table> tables;
	private static int maxRows;
	private static int nodeSize;

	public DBApp() {
		tables = new Hashtable<String, Table>();
	}

	public void init() {
		try {
		File myFile=new File("data/metadata.csv");
		myFile.createNewFile();	
		FileReader reader=new FileReader("config/DBApp.properties");  
	
	    Properties p=new Properties();  
	    p.load(reader);  
	      
	    maxRows=Integer.parseInt(p.getProperty("MaximumRowsCountinPage"));
	    nodeSize=Integer.parseInt(p.getProperty("NodeSize"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public void createTable(String strTableName, String strClusteringKeyColumn,
			Hashtable<String, String> htblColNameType) throws DBAppException {
		boolean flag = false;
		// //.out.println("here1");
		if (tables.size() != 0) {
			Object[] tmp = tables.keySet().toArray();
			String[] tableNames = new String[tmp.length];
			for (int i = 0; i < tmp.length; i++) {
				tableNames[i] = (String) tmp[i];
			}

			// //.out.println("here2");
			for (int i = 0; i < tableNames.length; i++) {
				if (tableNames[i].equals(strTableName)) {
					// //.out.println(tableNames[i]);
					flag = true;
					break;
				}
			}
		}
		if (flag == true) {
			throw new DBAppException("This table already exists");
		} else {
			// //.out.println("here3");
			ArrayList<String> columnNames = new ArrayList<String>();
			ArrayList<String> columnTypes = new ArrayList<String>();
			Set<String> names = htblColNameType.keySet();
			for (String key : names) {
				columnNames.add(key);
				columnTypes.add(htblColNameType.get(key));
			}
			columnNames.add("TouchDate");
			columnTypes.add("java.util.Date");

			ArrayList<Boolean> clustered = new ArrayList<Boolean>();
			for (int i = 0; i < columnNames.size(); i++) {
				if (columnNames.get(i).equals(strClusteringKeyColumn)) {
					clustered.add(true);
				} else {
					clustered.add(false);
				}
			}
			clustered.add(false);
			ArrayList<Boolean> indexed = new ArrayList<Boolean>();
			for (int i = 0; i < clustered.size(); i++) {
				indexed.add(false);
			}
			indexed.add(false);

			Table t = new Table(strTableName, columnNames, columnTypes, clustered, indexed, strClusteringKeyColumn, maxRows);
			// fix maxRows
			// //.out.println("here4");
			tables.put(strTableName, t);
			insertIntoMetaData(t, true);

		}

	}

	
	public void createBTreeIndex(String strTableName, String strColName) throws DBAppException 
	{													//same for RTree // any more exceptions to throw?
		//TODO throw an exception on entering a invalid column name
		
		boolean flag = false;

		//creating an array of table names
		Object[] tableNamesObj = (tables.keySet().toArray());
		String[] tableNames = new String[tableNamesObj.length];
		int j = 0;
		for (Object name : tableNamesObj) {
			tableNames[j] = (String) name;
			j++;
		}

		for (int i = 0; i < tableNames.length; i++) {
			if (tableNames[i].equals(strTableName)) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			throw new DBAppException("This table doens't exist. Cannot create BTree index");
		} else {

		
			modifyIndexedMetadata(strTableName, strColName);
			tables.get(strTableName).newBTree(strColName, nodeSize);
		}
		
//	BPTree b = new BPTree( nodeSize);
	}
	
	public void createRTreeIndex(String strTableName, String strColName) throws DBAppException 
	{													//same for RTree // any more exceptions to throw?
		//TODO throw an exception on entering a invalid column name
		
		boolean flag = false;

		//creating an array of table names
		Object[] tableNamesObj = (tables.keySet().toArray());
		String[] tableNames = new String[tableNamesObj.length];
		int j = 0;
		for (Object name : tableNamesObj) {
			tableNames[j] = (String) name;
			j++;
		}

		for (int i = 0; i < tableNames.length; i++) {
			if (tableNames[i].equals(strTableName)) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			throw new DBAppException("This table doens't exist. Cannot create RTree index");
		} else {

		
			modifyIndexedMetadata(strTableName, strColName);
			tables.get(strTableName).newRTree(strColName, nodeSize);
		}
		
	}

	 
	/*
	 * arrSQLTerms array of SQLTerm objects representing conditions in query
	 * strarrOperators array of strings of Logical operators in order
	 * */
	public Iterator selectFromTable(SQLTerm[] arrSQLTerms, String[] strarrOperators) throws DBAppException, ClassNotFoundException {
		//TODO exception on non-existing table names or invalid column names or incompatible types
		ArrayList<Vector<Tuple>> resultSets = new ArrayList<Vector<Tuple>>();
		Vector<Tuple> results = null;
		String strTableName="";
		int iz = 0;
		for (SQLTerm cond :arrSQLTerms ) {
			strTableName = cond.strTableName;
			System.out.println("term  " + iz++);
			Object[] tableNamesObj = (tables.keySet().toArray());
			String[] tableNames = new String[tableNamesObj.length];
			int j = 0;
			for (Object name : tableNamesObj) {
				tableNames[j] = (String) name;
				j++;
			}
			boolean flag = false;
			for (int i = 0; i < tableNames.length; i++) {
				if (tableNames[i].equals(strTableName)) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				throw new DBAppException("This table doens't exist");
			}else {
				results = tables.get(strTableName).executeQuery(cond.strColumnName, cond.strOperator, cond.objValue);
				resultSets.add(results);
				
			}
			
			
		}
		for (String logOp: strarrOperators) {
			switch (logOp)
			{
			case "AND":
				System.out.println(resultSets.get(0)+ " AND " +resultSets.get(1));
			resultSets.add(0,tables.get(strTableName).AND(resultSets.get(0),resultSets.get(1)));
			resultSets.remove(1);
			resultSets.remove(1);break;
			case "OR":
				System.out.println(resultSets.get(0)+ " OR " +resultSets.get(1));
			resultSets.add(0,tables.get(strTableName).OR(resultSets.get(0),resultSets.get(1)));
			System.out.println(resultSets);
			resultSets.remove(1);
			resultSets.remove(1);break;
			case "XOR":
				System.out.println(resultSets.get(0)+ " XOR " +resultSets.get(1));
			resultSets.add(0,tables.get(strTableName).XOR(resultSets.get(0),resultSets.get(1)));
			resultSets.remove(1);
			resultSets.remove(1);break;
			}
				
		}
results =  resultSets.get(0);
//System.out.println("final  "+results);
//actually search 
return results.iterator();
	}
	
	public void parseSQLTerm(String query) {
		ArrayList<Object> values =  getObjValues(query);
		ArrayList<String> names = getNameOfObjects(query);
		String colsToSelect = getColumnNames(query);
		getOperators(query);
		
	}
	/*
	 * gets the table name from the query string
	 * */
	public String getTableName(String query) {
		String[] temp = query.split("\\s");
		return temp[3];
	}

	/*
	 * gets the column names all in one string from the query string
	 * */
	public String getColumnNames(String query) {
		String[] temp = query.split("\\s");
		return temp[1];
	}

	/*
	 * gets an arraylist of strings that has the operators {=,>,<,>=,<=}
	 * */
	public ArrayList<String> getOperators(String query) {
		String[] temp = query.split("\\s");
		int size = temp.length;
		ArrayList<String> result = new ArrayList<String>();
		for(int i = 5;i>0;i=i-2) {
			String op = temp[size-i];
			for(int j = 0;j<op.length();j++) {
				char c = op.charAt(j);
				if(c == '=') {
					result.add(Character.toString(c));
				}else {
					if(c == '>') {
						char c1 = op.charAt(j+1);
						if(c1 == '=') {
							String r = ""+c+c1;
							result.add(r);
						}else {
							result.add(Character.toString(c));							
						}
					}else {
						if(c == '<') {
							char c1 = op.charAt(j+1);
							if(c1 == '=') {
								String r = ""+c+c1;
								result.add(r);
							}else {
								result.add(Character.toString(c));							
							}
						}	
					}
				}
			}
		}
		return result;

	}

	/*
	 * returns all the logical operators in the query string
	 * */
	public ArrayList<String> getLogicalOperators(String query){
		String[] temp = query.split("\\s");
		int size = temp.length;
		ArrayList<String> result = new ArrayList<String>();
		for(int i = 4;i>0;i=i-2) {
			String op = temp[size-i];
			result.add(op);
		}
		return result;
	}

	/*
	 * return the name of the columns that are concerned with the object value in the query string
	 * */
	public ArrayList<String> getNameOfObjects(String query){
		String[] temp = query.split("\\s");
		int size = temp.length;
	
		ArrayList<String> result = new ArrayList<String>();
		for(int i = 5;i<size;i=i+2) {
			String statement = temp[i];
			String ans = "";
			int j = 0;
			Character [] operators = {'=','>','<', '!'};
			while(!isIn(statement.charAt(j),operators)) {
				ans += statement.charAt(j);
				j++;
			}
			result.add(ans);
		}
		return result;
	}
	private boolean isIn(Object o, Object [] ol) {
		for(Object obj:ol) {
			if(obj.equals(o)) return true;
		}
		return false;
	}

	/*
	 * return the values of the columns that we are looking for in the query string
	 * */
	public ArrayList<Object> getObjValues(String query) {
		String[] temp = query.split("\\s");
		int size = temp.length;
		ArrayList<Object> result = new ArrayList<Object>();
		for(int i = 5;i>0;i=i-2) {
			String statement = temp[size-i];
			for(int j = 0;j<statement.length();j++) {
				if(statement.charAt(j) == '=') {
					if(statement.charAt(j+1) == '"') {
						String value = statement.substring(j+2,statement.length());
						result.add(value);
					}else {
						String value = statement.substring(j+1,statement.length());
						result.add(value);
					}
				}
			}
		}
		return result;
	}

	public void insertIntoTable(String strTableName, Hashtable<String, Object> htblColNameValue) throws DBAppException {
		boolean flag = false;

		java.util.Date date = new java.util.Date();
//		//.out.println(date);
		htblColNameValue.put("TouchDate", date);

		Object[] tableNamesObj = (tables.keySet().toArray());
		String[] tableNames = new String[tableNamesObj.length];
		int j = 0;
		for (Object name : tableNamesObj) {
			tableNames[j] = (String) name;
			j++;
		}

		for (int i = 0; i < tableNames.length; i++) {
			if (tableNames[i].equals(strTableName)) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			throw new DBAppException("This table doens't exist");
		} else {

			Hashtable<String, Comparable> tempHash = new Hashtable<String, Comparable>();
			Set<String> keys = htblColNameValue.keySet();
			for (String key : keys) {
				tempHash.put(key, (Comparable) htblColNameValue.get(key));
			}
			Tuple t = new Tuple(tempHash, tables.get(strTableName).returnClusteredKey());
			tables.get(strTableName).insert(t, false);
		}
	}

	public void updateTable(String strTableName, String strClusteringKey, Hashtable<String, Object> htblColNameValue)
			throws DBAppException {
System.out.println("tablenames " + tables.keySet());
		boolean flag = false;
		Object[] tableNamesObj = (tables.keySet().toArray());
		String[] tableNames = new String[tableNamesObj.length];
		int j = 0;
		for (Object name : tableNamesObj) {
			tableNames[j] = (String) name;
			j++;
		}
		for (int i = 0; i < tableNames.length; i++) {
			if (tableNames[i].equals(strTableName)) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			throw new DBAppException("This table doens't exist");
		} else {
			//.out.println("AY 7AGA");
			Hashtable<String, Comparable> tempHash = new Hashtable<String, Comparable>();
			Set<String> keys = htblColNameValue.keySet();
			for (String key : keys) {
				tempHash.put(key, (Comparable) htblColNameValue.get(key));
			}
			Tuple t = new Tuple(tempHash, tables.get(strTableName).returnClusteredKey());
			tables.get(strTableName).updateTable(strClusteringKey, t);
		}

	}

	public void deleteFromTable(String strTableName, Hashtable<String, Object> htblColNameValue) throws DBAppException {

		boolean flag = false;
		Object[] tableNamesObj = (tables.keySet().toArray());
		String[] tableNames = new String[tableNamesObj.length];
		int j = 0;
		for (Object name : tableNamesObj) {
			tableNames[j] = (String) name;
			j++;
		}
		for (int i = 0; i < tableNames.length; i++) {
			if (tableNames[i].equals(strTableName)) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			throw new DBAppException("This table doens't exist");
		} else {

			Hashtable<String, Comparable> tempHash = new Hashtable<String, Comparable>();
			Set<String> keys = htblColNameValue.keySet();
			for (String key : keys) {
				tempHash.put(key, (Comparable) htblColNameValue.get(key));
			}
//			Tuple t = new Tuple(tempHash, tables.get(strTableName).returnClusteredKey());
//			tables.get(strTableName).delete(t);
			tables.get(strTableName).delete(tempHash);

		}

	}

	/**
	 * 
	 * @param t      table to be added to the metadata.csv
	 * @param append whether to append it to the file (true) or to override the file
	 *               (false)
	 */
	public static void insertIntoMetaData(Table t, boolean append) {
		String tableName = t.getTableName();
		ArrayList<String> columnNames = t.getColumnNames();
		ArrayList<String> columnTypes = t.getColumnTypes();
		ArrayList<Boolean> clusteredColumns = t.getClusteredCoulmns();
		ArrayList<Boolean> indexedColumns = t.getIndexedCoulmns();
		String toBeInserted = "Table Name, Column Name, Column Type, ClusteringKey, Indexed";
		File file = new File("data/metadata.csv");
		try {
			FileWriter f = new FileWriter("data/metadata.csv", append);
			BufferedWriter bw = new BufferedWriter(f);
			bw.write(toBeInserted);
			bw.write("\n");
			for (int i = 0; i < columnNames.size(); i++) {
				toBeInserted = tableName + "," + columnNames.get(i) + "," + columnTypes.get(i) + ","
						+ clusteredColumns.get(i) + "," + indexedColumns.get(i);
				bw.write(toBeInserted);
				// line for testing
				// //.out.println("line " + (i) + " inserted");
				bw.write("\n");
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateMetaDataFile() {
		for (String tableName : tables.keySet()) {
			Table t = tables.get(tableName);
			insertIntoMetaData(t, false);

		}
	}
	
	public void  modifyIndexedMetadata(String tableName, String strColName) throws DBAppException {
		String line = "";  
		String splitBy = ",";  

		ArrayList<String []> meta = new ArrayList<String[]>();
		
		try   
		{  
		//parsing a CSV file into BufferedReader class constructor  
		BufferedReader br = new BufferedReader(new FileReader("data/metadata.csv"));  
		while ((line = br.readLine()) != null)   //returns a Boolean value  
		{  
		String[] entry = line.split(splitBy);    // use comma as separator  
		////.out.println("Employee [First Name=" + employee[0] + ", Last Name=" + employee[1] + ", Designation=" + employee[2] + ", Contact=" + employee[3] + ", Salary= " + employee[4] + ", City= " + employee[5] +"]");  
		if(entry[0].contentEquals(tableName) && entry[1].contentEquals(strColName) ) {
		
				entry[4] = "true";
		}
		meta.add(entry);
		}
		br.close();
		}   
		catch (IOException e)   
		{  
		e.printStackTrace();  
		} 
		
		String toBeInserted = "Table Name, Column Name, Column Type, ClusteringKey, Indexed";
		File file = new File("data/metadata.csv");
		try {
			FileWriter f = new FileWriter("data/metadata.csv");
			BufferedWriter bw = new BufferedWriter(f);
			bw.write(toBeInserted);
			bw.write("\n");
			for (int i = 0; i < meta.size(); i++) {
				toBeInserted = meta.get(i)[0] + "," + meta.get(i)[1] + "," +meta.get(i)[2] + ","
						+ meta.get(i)[3] + "," + meta.get(i)[4];
				bw.write(toBeInserted);
				// line for testing
				// //.out.println("line " + (i) + " inserted");
				bw.write("\n");
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
//		String strTableName = "Student";
//		DBApp dbApp = new DBApp();
//		Hashtable htblColNameType = new Hashtable();
//		htblColNameType.put("id", "java.lang.Integer");
//		htblColNameType.put("name", "java.lang.String");
//		htblColNameType.put("gpa", "java.lang.double");
//		try {
//			dbApp.createTable(strTableName, "id", htblColNameType);
//			Hashtable htblColNameValue = new Hashtable();
//			htblColNameValue.put("id", new Integer(1));
//			htblColNameValue.put("name", new String("Ahmed Noor"));
//			htblColNameValue.put("gpa", new Double(0.95));
//			dbApp.insertIntoTable(strTableName, htblColNameValue);
//			htblColNameValue.clear();
//			htblColNameValue.put("id", new Integer(2));
//			htblColNameValue.put("name", new String("Ahmed Noor"));
//			htblColNameValue.put("gpa", new Double(0.95));
//			dbApp.insertIntoTable(strTableName, htblColNameValue);
//			htblColNameValue.clear();
//			htblColNameValue.put("name", new String("Ahmed"));
//			dbApp.updateTable(strTableName, "1", htblColNameValue);
//		} catch (Exception e) {
//			//.out.println(e.getMessage());
//		}

		DBApp d = new DBApp();
		System.out.println(d.getObjValues("SELECT * FROM student WHERE id=123 AND name=ali"));
		System.out.println(d.getNameOfObjects("SELECT * FROM student WHERE id>123 AND name=ali"));
		System.out.println(d.getColumnNames("SELECT * FROM student WHERE id>123 AND name=ali"));
		System.out.println(d.getOperators("SELECT * FROM student WHERE id>123 AND name=ali"));
		System.out.println(d.getLogicalOperators("SELECT * FROM student WHERE id>123 AND name=ali"));	}
}
