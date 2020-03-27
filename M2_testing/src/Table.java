import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.naming.ldap.SortControl;

@SuppressWarnings("serial")
public class Table implements Serializable {
	private static int maxRows;
	private String tableName;
	//private Vector<Page> tablePages;
	private ArrayList<String> columnNames;
	private ArrayList<String> columnTypes;
	private ArrayList<Boolean> clusteredCoulmns;
	private ArrayList<Boolean> indexedCoulmns;
	// Clustered key Column Name
	private String clusteredKey;
	private int numOfPages;
	private ArrayList<BTree> btrees = new ArrayList<BTree>() ;
	private ArrayList<RTree> rtrees = new ArrayList<RTree>();
	// awel string esm el file w el array of comparables at index 0 el current
	// noOfRows
	// array of comparables at index 1 hoe el minKey fel page
	private Hashtable<String, Comparable[]> pageInfo = new Hashtable<String, Comparable[]>();
	private Vector<Tuple> page=new Vector<Tuple>();


	public Table(String tableName, ArrayList<String> columnNames, ArrayList<String> columnTypes,
			ArrayList<Boolean> clustered, ArrayList<Boolean> indexed, String clusteredKey, int maxRows) {
		this.tableName = tableName;
		this.columnNames = columnNames;
		this.columnTypes = columnTypes;
		this.clusteredCoulmns = clustered;
		this.indexedCoulmns = indexed;
		this.clusteredKey = clusteredKey;
//		String firstFile = addPage();
//		Comparable [] info = new Comparable[3];
//		info[0]=0;
//		info[1]=-1;
//		info[2]=-1;
//		pageInfo.put(firstFile, info);	
		 Properties prop = new Properties();

		 Table.maxRows=maxRows;


	}
	
	public boolean  isBIndexedCol(String strColName){

		for (int i=0;i<btrees.size();i++)   //returns a Boolean value  
		{  
		if(btrees.get(i).getColumnName().equals(strColName)) {
			return true;// use comma as separator  		
		}
		}  
		return false;
	}
	
	public BTree  getBtreeCol(String strColName){

		for (int i=0;i<btrees.size();i++)   //returns a Boolean value  
		{  
		if(btrees.get(i).getColumnName().equals(strColName)) {
			return btrees.get(i);// use comma as separator  		
		}
		}  
		return null;
	}

	// to find the pages
	

	/**
	 * 
	 * @return the new file for the newly created page
	 */
	public String addPage() {
		String filename = tableName + "_" + (++numOfPages);
System.out.println("pagecount " + numOfPages);
		File file = new File(filename + ".ser");
		try {
			file.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		Comparable [] info = new Comparable[3];
//		info[0]=0;
////		info[1]=null;
////		info[2]=null;
//		pageInfo.put(filename, info);
		return filename;
	}
	
	public ArrayList<String> findPages(Tuple t) {
		Comparable tKey = t.getKeyValue();
		ArrayList<String> pages = new ArrayList<String>();
		Set<String> keys = pageInfo.keySet();
		for (String key : keys) {
			Comparable[] temp = pageInfo.get(key);
			if (tKey.compareTo(temp[1]) > 0 && tKey.compareTo(temp[2]) < 0) {
				pages.add((String) tKey);
			}
		}
		return pages;

	}

	public void Write(String filename) {
		System.out.println("WRITE: "+page);

		// Serialization
		
		try {
			// Saving of object in a file
			FileOutputStream file1 = new FileOutputStream(filename+".ser"); // overwrites file?
			ObjectOutputStream out = new ObjectOutputStream(file1);
			
			// Method for serialization of object
//			out.writeObject(tableName);
//			out.writeObject(columnNames);
//			out.writeObject(columnTypes);
//			out.writeObject(clusteredCoulmns);
//			out.writeObject(indexedCoulmns);
//			out.writeObject(clusteredKey);
//			out.writeObject(maxRows);
			
			out.writeObject(page);






			out.close();
			file1.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
}

	public void Read(String filename) {
//		System.out.println( " reading1!!!!!!!!!!"  );

		try {
			//ObjectInputStream o = new ObjectInputStream(new FileInputStream(filename ));
			FileInputStream fi = new FileInputStream((filename+".ser"));
//			System.out.println( " reading2!!!!!!!!!!"  );

			try{ObjectInputStream o = new ObjectInputStream(fi);

//			while(o.readObject()!=null) {
//				System.out.println( " reading3!!!!!!!!!!"  );

				o.read();
//				System.out.println( " reading4!!!!!!!!!!"  );

//				System.out.println(o.readObject() + " reading" +o.readObject().getClass().getCanonicalName() );
//				System.out.println( " reading5!!!!!!!!!!"  );

//				page.add((Tuple)o.readObject());
				page= (Vector<Tuple>)o.readObject();

//				System.out.println(tableName);
//				System.out.println(columnNames);
//				System.out.println(columnTypes);
//				System.out.println(clusteredCoulmns);
//				System.out.println(indexedCoulmns);
//				System.out.println(clusteredKey);
//				System.out.println(maxRows);
//				System.out.println( " reading6!!!!!!!!!!"  );

//				break;

				
//			}
			o.close();
			fi.close();
			}catch(EOFException e) {
				
				System.out.println("end of file ");
//				page = new Vector<Tuple>();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("READ: "+page);

			
			
			

			} catch (FileNotFoundException e) {
			e.printStackTrace();
			} catch (IOException e) {
			e.printStackTrace();
			}
			
	}
	      
	  
	     
	public String getNextPage(String currFile) {
		// gets the next page(in terms of key) after current file

		// String[] tmp = (String[]) pageInfo.entrySet()
		Comparable currKey = pageInfo.get(currFile)[2];

//		String[] tmp = (String[]) pageInfo.keySet().toArray();
		Object[] tmpObj =  (pageInfo.keySet().toArray());
		String[] tmp = new String[tmpObj.length];
		int j=0;
		for(Object name: tmpObj) {
			tmp[j]=(String) name;
			j++;
		}
		String[] keyArr = new String[tmp.length];
		int i;
		for (i = 0; i < tmp.length; i++)
			keyArr[i] = (String) tmp[i];
		// copy file names into keyArr Arrays.sort(keyArr); // needless but can be kept
		boolean found = false;
		for (i = 0; i < keyArr.length; i++) {
			Comparable maxIndex = pageInfo.get(keyArr[i])[2];
			if (currKey.compareTo(maxIndex) <= 0 && keyArr[i]!=currFile) // compare to min index of each page
				found = true;
			break; // stop at page wanted (first page where entry key is greater)
		}
		if (found) {
			return keyArr[i];
		} else {
			return null;
		}

	}
	
	public void insertBTrees(Tuple t, String page) {
		Set<String> cols =t.getAttributes().keySet();
		for(String col: cols) {
			BTree bt = getBtreeCol(col);
			if(bt !=null) {
				Pointer p = new Pointer(t.getValueOfColumn(col), page);
				bt.add(p);
				System.out.println(col);
			}
		}
		System.out.println("no more B indices");
	}
	
	
	public boolean checkInsert(Tuple t) throws DBAppException{
		Hashtable<String, String>  tableData = readTableMetadata();
	
//	 String[] colNames = (String[]) tableData.keySet().toArray();
	 Object[] colNamesObj =  (tableData.keySet().toArray());
		String[] colNames = new String[colNamesObj.length];
		int j=0;
		for(Object name: colNamesObj) {
			colNames[j]=(String) name;
			j++;
		}
		
//		if(colNames.length!=tableData.keySet().size()) {
		if(colNames.length!=t.getAttributes().size()) {

			 throw new DBAppException("please enter all fields");

		}
		
	  
	 for(String col: colNames) {
		 if(!(tableData.get(col).toLowerCase()).equals((t.getAttributes().get(col)).getClass().getCanonicalName().toLowerCase())) {
			 throw new DBAppException("type msimatch.cannot insert");
		 }
	 }
	 return true;
  }
	


	public void insert(Tuple t) {
		if (page!=null)
		page.clear();
		try {
			checkInsert(t);
		} catch (DBAppException e) {
			
			e.printStackTrace();
		}
		
		if(pageInfo.isEmpty()) {
			String firstFile = addPage();
			Comparable [] info = new Comparable[3];
			info[0]=0;
			info[1]=t.getKeyValue();
			info[2]=t.getKeyValue();
			pageInfo.put(firstFile, info);
		}
		
		
		Object[] allFilesObj =  pageInfo.keySet().toArray();
		System.out.println("allFiles length "+ allFilesObj.length);

//		String [] allFiles = intoArray(allFilesObj);
		String[] allFiles = new String[allFilesObj.length];
		int f=0;
		for(Object name: allFilesObj) {
			allFiles[f]=(String) name;
			f++;
		}
		
		System.out.println("allFiles length "+ allFiles.length);

		Comparable[] allMin = new Comparable[allFiles.length];
		Comparable[] allMax = new Comparable[allFiles.length];
		int i = 0;
		for (String name : allFiles) {
			allMin[i] = (Comparable) pageInfo.get(name)[1];
			System.out.println(allMin[i] + ", ");
			allMax[i] = (Comparable) pageInfo.get(name)[2];
			System.out.println(allMax[i] + ", ");
			i++;
		}
		Arrays.sort(allMin);
		Arrays.sort(allMax);
		ArrayList<String> options= new ArrayList<String>() ;
		
//for(Comparable opt: allMin) {
//	if(t.getKeyValue().compareTo(opt)>=0) {
//		for(String fil:allFiles) {
//			if(pageInfo.get(fil)[1].compareTo(opt)==0)
//			options.add(fil);
//		}
//	}
//		
//}
//for(Comparable opt: allMax) {
//	if(t.getKeyValue().compareTo(opt)>=0) {
//		for(String fil:allFiles) {
//			if(pageInfo.get(fil)[2].compareTo(opt)==0)
//			options.add(fil);
//		}
//	}
//		
		Arrays.sort(allFiles);
	
		Comparable [] bounds = new Comparable[allMin.length + allMax.length];
		for(int l=0;l<bounds.length-1;l+=2) {
			bounds[l]=allMin[l/2];
			bounds[l+1]=allMax[l/2];
		}
		
//		if (bounds[0].compareTo(t.getKeyValue()) >= 0) {
//			options.add();
//			System.out.println("smol ");
//		}
		
if (!isBIndexedCol(clusteredKey)) {
		if(t.getKeyValue().compareTo(bounds[0])<=0) {
			for(String e: allFiles) {
				if(pageInfo.get(e)[1].compareTo(bounds[0])==0 )
				options.add(e);					

			}
		}
if(t.getKeyValue().compareTo(bounds[bounds.length-1])>=0) {
	for(String e: allFiles) {
		if(pageInfo.get(e)[2].compareTo(bounds[0])==0)
		options.add(e);					

	}
		}
		
		for(int l=0;l<bounds.length-1;l++) {
			
			if(bounds[l].compareTo(t.getKeyValue()) <=0 && bounds[l+1].compareTo(t.getKeyValue()) >=0) {
				if(l%2==0) { //in a file's limits
					for(String e: allFiles) {
						if(pageInfo.get(e)[1].compareTo(bounds[l])==0 || pageInfo.get(e)[2].compareTo(bounds[l+1])==0)
						options.add(e);					

					}
				}else {
					for(String e: allFiles) {
						if(pageInfo.get(e)[1].compareTo(bounds[l+1])==0 || pageInfo.get(e)[2].compareTo(bounds[l])==0)
						options.add(e);					

					}
				}
				
				
			}
		}
		
}else {
	BTree clusBtree = getBtreeCol(clusteredKey);
	options.addAll(clusBtree.getInsertPage(t.getKeyValue()));
}
//}
//		for(int m=0;m<allFiles.length;m++)
//		{
//			if(t.getKeyValue().compareTo(pageInfo.get(allFiles[m])[1])>=0) {
//				options.add(allFiles[m]);
//			}
//		}
//options =removeDuplicates(options);
//options= sortPages(options);

//		ArrayList<String> 
//		options = findPage(t);
//		options =removeDuplicates(options);
//		options= sortPages(options);

		if (options.size() == 0 ) {
System.out.println("options are empty" + options);
			if (pageInfo.isEmpty()) {
				System.out.println("options are empty3" + options);

				options.add(addPage());
				System.out.println("options are empty2" + options);

				System.out.println("table has no pages");

			} else {
				if (t.getKeyValue().compareTo(allMin[0]) <= 0) // smaller than smallest key
				{
					System.out.println("small 1" + options);

					for (int j = 0; j < allFiles.length; j++) {
						if (pageInfo.get(allFiles[j])[1] == allMin[0]) {
							options.add(allFiles[j]);
							System.out.println("small 2" + options);

						}
						System.out.println("small 3" + options);

					}
				} else // larger than largest key
				{					System.out.println("big 0" + options);

					if (t.getKeyValue().compareTo(allMax[allMax.length - 1]) >= 0) {
						System.out.println("big 1" + options);

						for (int j = 0; j < allFiles.length; j++) {
							if (pageInfo.get(allFiles[j])[2] == allMax[allMax.length - 1]) {
								System.out.println("big 2" + allFiles[j]);
								options.add(allFiles[j]);
								System.out.println("big 3" + options);
//fix me
							}
							System.out.println("big 4" + options);

						}
					}
				}

			}

		}
//		options = 
////				removeDuplicates(options);
		
		insertHelper(t, options);
	}

	public void insertHelper(Tuple t, ArrayList<String> pages) {
		System.out.println("options for id : " +t.getKeyValue() + "are  "+pages);
		
		boolean found = false;
		for (int i = 0; i < pages.size(); i++) {
			if (!isPageFull(pages.get(i))) {
			
				found = true;
				System.out.println( "inserting to : " + pages.get(i)+ "!!!!!!!!!!!!!");
				Read(pages.get(i));
				insertPage(t, pages.get(i));
//				updateMinKey(pages.get(i));
//				updateMaxKey(pages.get(i));
//				updatenoOfRows(pages.get(i));
				updatePageInfo(pages.get(i));
				Write(pages.get(i));
				return;
			}
		}
		if (!found) {
			// insert into last possible page and copy and remove one row
			System.out.println(pages.get(pages.size() - 1));
			Read(pages.get(pages.size() - 1));
			System.out.println(page);
			insertPage(t, pages.get(pages.size() - 1));
			System.out.println(page);
			Tuple temp = page.lastElement();
			page.remove(page.lastElement());
//			updateMinKey(pages.get(pages.size() - 1));
//			updateMaxKey(pages.get(pages.size() - 1));
//			updatenoOfRows(pages.get(pages.size() - 1));
			updatePageInfo(pages.get(pages.size() - 1));
			Write(pages.get(pages.size() - 1));

			String next = getNextPage(pages.get(pages.size() - 1)); // what if arraylist is not sorted by minkey?

			if (next == null) {// no next page
				page.clear();
				System.out.println("NO NEXT!!!!!!!!!!!!!!!");
				String file1 = addPage(); // adding the info in the hashtable
				pageInfo.put(file1, new Comparable[] { 1, t.getKeyValue(), t.getKeyValue() });
			page.add(temp);
//				page.add(t); // only add to vector
				Write(file1); // write to new file
				return;
			} else {
				if (!isPageFull(next)) { // shifting can be done
					page.clear();
					Read(next);
					insertPage(temp, next);
					updateMinKey(next);
					updateMaxKey(next);
					updatenoOfRows(next);
					updatePageInfo(next);
					Write(next);
					return;
				} else { // next is full
					page.clear();
					Read(next);
					insertPage(temp, next);
					Tuple temp2 = page.lastElement();
					page.remove(page.lastElement());
//					updateMinKey(next);
//					updateMaxKey(next);
//					updatenoOfRows(next);
					updatePageInfo(next);
					Write(next);
					System.out.println("RECURSIVE!!!!!!!!!!!" + temp.getKeyValue());
					insert(temp2);
				}

			}

		}
	}

	public boolean isPageFull(String filename) {
		int noOfRows = (int) pageInfo.get(filename)[0];
		System.out.println("page rows: "+noOfRows+"maxRows: "+maxRows);
		if (maxRows - noOfRows > 0) {
			return false;
		} else
			return true;
	}

	public void insertPage(Tuple t, String pageOfInsertion) {
		System.out.println("inserting......");
		if (this.page.size() > 0) {
			Iterator it = this.page.iterator();
			int i = 0;
			boolean inserted = false;
			while (it.hasNext() && inserted == false) {
				Tuple tmp = (Tuple) it.next();
				i++;
				System.out.println(i + "   comp:" +t.compareTo(tmp) + "val: " + tmp.getKeyValue());

				if (t.compareTo(tmp) <= 0) {
					this.page.insertElementAt(t, i-1);
					inserted = true;
					System.out.println("   found:" +inserted);

					
//				 this.rows.add(t);

				}
//				i++;
			}
			if (!inserted) {
				page.add(t);
				System.out.println("   found:" +inserted);

			}
		} else {
//		 this.rows.insertElementAt(t, 0);
			this.page.add(t);

		}

		insertBTrees(t, pageOfInsertion);
	}
//	public void delete(Tuple t) throws DBAppException {
		public void delete(Hashtable<String, Comparable> htblColNameValue) throws DBAppException {
		page.clear();
		
		Hashtable <String , String > temp=readTableMetadata(); 
		for (String col : htblColNameValue.keySet()) {
			if(!temp.keySet().contains(col))
				throw new DBAppException("metadata file Error");
			if(!(htblColNameValue.get(col).getClass().getCanonicalName().toLowerCase()).equals(temp.get(col).toLowerCase()))
				throw new DBAppException("Incompatible types");
		}
		for(String file :pageInfo.keySet())
		{
			deleteFromPage(file, htblColNameValue);
		}
		
	


	}

	/**
	 * 
	 * @param fileName name of the file to delete t tuple from
	 * @param htblColNameValue        tuple to be deleted from fileName
	 */
//	public void deleteFromPage(String fileName, Tuple t) {
		public void deleteFromPage(String fileName, Hashtable<String, Comparable> htblColNameValue) {

			System.out.println("deletepage!!!!!!!!!!1");

		page.clear();
		Read(fileName);
		int i = 0;
//		for (Tuple t1 : page) {
//			if (t1.helperDelete(t.getAttributes())) {
//				page.remove(i);
//				i++;
//			}
//		}
		while(i<page.size()) {//startoftrial
			System.out.println("vector at" + i);
			System.out.println(page.get(i));
			System.out.println(htblColNameValue);

			if (page.get(i).helperDelete(htblColNameValue)) {
			
				page.remove(i);
				System.out.println("matched!!!!!!!!!!");
			}
			else {
				i++;
			}
		} //endoftrial
		if (page.size() == 0) {
			deletePage(fileName);
		} else {
			Comparable[] currentPageInfo = pageInfo.get(fileName);
			int currentNoOfPages = (int) currentPageInfo[0];
			pageInfo.replace(fileName,
					new Comparable[] { (currentNoOfPages - i), updateMinKey(fileName), updateMaxKey(fileName) });

		}
		Write(fileName);
		page.clear();
	}

	public String returnTableName() {
		return this.tableName;
	}

	public String returnClusteredKey() {
		return this.clusteredKey;
	}

	public String getTableName() {
		return tableName;
	}

	public ArrayList<String> getColumnNames() {
		return columnNames;
	}

	public ArrayList<String> getColumnTypes() {
		return columnTypes;
	}

	public ArrayList<Boolean> getClusteredCoulmns() {
		return clusteredCoulmns;
	}

	public ArrayList<Boolean> getIndexedCoulmns() {
		return indexedCoulmns;
	}

	public Hashtable<String, String>  readTableMetadata() throws DBAppException {
		String line = "";  
		String splitBy = ",";  
		ArrayList<String> tableColNames = new ArrayList<String>();
		ArrayList<String> tableColTypes = new ArrayList<String>();

		try   
		{  
		//parsing a CSV file into BufferedReader class constructor  
		BufferedReader br = new BufferedReader(new FileReader("data/metadata.csv"));  
		while ((line = br.readLine()) != null)   //returns a Boolean value  
		{  
		String[] entry = line.split(splitBy);    // use comma as separator  
		//System.out.println("Employee [First Name=" + employee[0] + ", Last Name=" + employee[1] + ", Designation=" + employee[2] + ", Contact=" + employee[3] + ", Salary= " + employee[4] + ", City= " + employee[5] +"]");  
		if(entry[0].contentEquals(tableName)) {
		
				tableColNames.add(entry[1]);
				tableColTypes.add(entry[2]);
		}
		
		}  
		}   
		catch (IOException e)   
		{  
		e.printStackTrace();  
		} 
		
		System.out.println(tableColNames);
		System.out.println(tableColTypes);
		Hashtable<String, String> colInfo = new Hashtable<String, String>();		
		if (tableColNames.size()==tableColTypes.size()) {
			
			for(int i=0;i<tableColNames.size();i++) {
				colInfo.put(tableColNames.get(i), tableColTypes.get(i));
			}
			
		}else {throw new DBAppException("Table metadata error");}
		return colInfo;
	}

	
	public void updateTable(String strClusteringKey, Tuple t) throws DBAppException {
		System.out.println("I am here method");
    //page.clear();
		
		Hashtable <String , String > temp=readTableMetadata(); 
		
		for(String file :pageInfo.keySet())
		{
			page.clear();
			Read(file);
			
			for (Tuple t1 : page) {
				System.out.println("I am here for loop 2");
				String coltype = temp.get(t1.getKey());
				
				if(coltype.equals("java.lang.Integer")) {
					System.out.println("integer for sure");
//					int value=Integer.parseInt(t.getKeyValue());
//					System.out.println(value);
//					System.out.println(t1.getKeyValue());
//					if(value==(int)t1.getKeyValue()) {
					if(t1.getKeyValue().compareTo(t.getKeyValue())==0) {
						System.out.println("OMG THEY ARE EQUAL!");
						//for (String key : t.getAttributes().keySet()) {
							//System.out.println("lets hope it enters the for loop");
						for (String key : t.getAttributes().keySet()) {
							t1.edit(key, t.getValueOfColumn(key));
						Date currentdate = new Date();

						t1.edit("TouchDate", currentdate);
						System.out.println(t1);
						System.out.println(page);

						
						}
						Write(file);
					}System.out.println("not equal go ckeck next tuple!");
					
					
				}
				else if (coltype.equals("java.lang.Double")) {
					double value=Double.parseDouble(t.getKey());
					if(value==(double)t1.getKeyValue()) {
						System.out.println("OMG THEY ARE EQUAL!");
						for (String key : t.getAttributes().keySet()) {
							t1.edit(key, t.getValueOfColumn(key));
						Date currentdate = new Date();

						t1.edit("TouchDate", currentdate);
				}
			}
					
					System.out.print("not equal go check next tuple ");
		}
				else if(coltype.contentEquals("java.util.Date")) {
					Date value= new Date((String) t.getKeyValue());
					if(value==(Date)t1.getKeyValue()) {
					System.out.println("OMG THEY ARE EQUAL!");
					for (String key : t.getAttributes().keySet()) {
						t1.edit(key, t.getValueOfColumn(key));
					Date currentdate = new Date();

					t1.edit("TouchDate", currentdate);
				}
					Write(file);

				}
					System.out.print("not equal go check next tuple ");
				}
				
//				else if(coltype.contentEquals("java.awt.Polygon")) {
				else if(coltype.contentEquals("Region")) {
//				Polygon value= (Polygon)t.getKeyValue();
//					if(value==(Polygon)t1.getKeyValue()) {
						Polygon value= (Region)t.getKeyValue();
						if(value==(Region)t1.getKeyValue()) {
					System.out.println("OMG THEY ARE EQUAL!");
					for (String key : t.getAttributes().keySet()) {
						t1.edit(key, t.getValueOfColumn(key));
					Date currentdate = new Date();

					t1.edit("TouchDate", currentdate);
				}
					Write(file);

				}
					System.out.print("not equal go check next tuple ");
				}
				else {
					String value=(String)t.getKeyValue();
					if(value.equals(t1.getKeyValue())) {
				
					System.out.println("OMG THEY ARE EQUAL!");
					for (String key : t.getAttributes().keySet()) {
						t1.edit(key, t.getValueOfColumn(key));
					Date currentdate = new Date();

					t1.edit("TouchDate", currentdate);
				}
					Write(file);

					}
					System.out.print("not equal go check next tuple ");
			}
			}
			}
		}

	// updates the min key of the page in the page vector
	/**
	 * 
	 * @param fileName
	 * @return updated minimum key of fileName
	 */
	public Comparable updateMinKey(String fileName) {
		return page.firstElement().getKeyValue();

	}

	/**
	 * 
	 * @param fileName
	 * @return updated maximum key of fileName
	 */
	public Comparable updateMaxKey(String fileName) {
		return page.lastElement().getKeyValue();

	}
	public Comparable updatenoOfRows(String fileName) {
		return page.size();

	}
public void updatePageInfo(String fileName) {
	
	Comparable noOfRows=updatenoOfRows(fileName);
	Comparable min=updateMinKey(fileName);
	Comparable max=updateMaxKey(fileName);

	pageInfo.replace(fileName,	new Comparable[] { (noOfRows), min, max });
}
	
	public void deletePage(String fileName) {
		File file = new File(fileName);
		file.delete();
		pageInfo.remove(fileName);
	}

	/**
	 * 
	 * @param t tuple t
	 * @return an array of files that may contain t sorted in ascending order
	 *         according to their minimum key
	 */
	public ArrayList<String> findPage(Tuple t) {
		Comparable tupleKey = t.getKeyValue();
		// holds the values of the pages that may contain the tuple
		ArrayList<String> temp = new ArrayList<String>();
		for (String key : pageInfo.keySet()) {
			Comparable minKey = pageInfo.get(key)[1];
			Comparable maxKey = pageInfo.get(key)[2];
			if (tupleKey.compareTo(minKey) >= 0 && tupleKey.compareTo(maxKey) <= 0) {
				temp.add(key);
			}

		}
		ArrayList<String> res = new ArrayList<String>();
		for (String s1 : temp) {
			Comparable minKey1 = pageInfo.get(s1)[1];
			String toBeInserted = s1;
			for (String s2 : temp) {
				Comparable minKey2 = pageInfo.get(s2)[1];
				if (!s1.equals(s2) && minKey1.compareTo(minKey2) > 0) {
					toBeInserted = s2;
				}
			}
			res.add(toBeInserted);
		}
		return res;
	}

	public String[] intoArray(Object[] h) {
		
		String[] Names = new String[h.length];
		int j=0;
		for(Object name: h) {
			Names[j]=(String) name;
			j++;
		}
		
		return null;
		
	}
	
	public static void main(String[] args) {
//    	ArrayList<String> columns = new ArrayList<String>();
//		columns.add("id");
//		columns.add("name");
//		ArrayList<String> types = new ArrayList<String>();
//		types.add("boolean");
//		types.add("integer");
//		ArrayList<Boolean> indexed = new ArrayList<Boolean>();
//		indexed.add(false);
//		indexed.add(false);
//		ArrayList<Boolean> clustered = new ArrayList<Boolean>();
//		clustered.add(true);
//		clustered.add(false);
//		
//		Table t= new Table("Student", columns, types, indexed,clustered , "id", 10);
////		Read("file.ser");
//		t.Write("tablefile");
//		t.Read("tablefile");
	}

	public void newBTree(String strColName, int nodeSize) throws DBAppException {
		// TODO Auto-generated method stub
		Hashtable <String , String> tableMeta = readTableMetadata();
		
		String strColType = tableMeta.get(strColName);
		
		BTree bt = new BTree(tableName, strColName, nodeSize,  strColType );
		
		// add to table's indices
		btrees.add(bt);
		
		//add everything already in table
		for (String block: pageInfo.keySet()) {
			Read(block);
			for(Tuple t : page) {
				Comparable value = t.getAttributes().get(strColName);
				Pointer pt = new Pointer(value, block);
				bt.add(pt);
			}
			page.clear();
		}
			System.out.println("Tree for " + strColName + ": " + bt.toString());		
			//TODO write index into a file
		//should each node be in a file? 
		
//		String filename = tableName + "_" + strColName;
//		System.out.println("new index being created at " + filename );
//				File file = new File(filename + ".ser");
//				try {
//					file.createNewFile();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
	}

}
