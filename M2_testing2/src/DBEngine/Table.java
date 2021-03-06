package DBEngine;

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
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import BPTree.BPTree;
import BPTree.Ref;
import RTree.RTree;

@SuppressWarnings("serial")
public class Table implements Serializable {
	private static int maxRows;
	private String tableName;
	private ArrayList<String> columnNames;
	private ArrayList<String> columnTypes;
	private ArrayList<Boolean> clusteredCoulmns;
	private ArrayList<Boolean> indexedCoulmns;
	// Clustered key Column Name
	private String clusteredKey;
	private int numOfPages;
	Hashtable<String, BPTree> btrees = new Hashtable<String, BPTree>();
	Hashtable<String, RTree> rtrees = new Hashtable<String, RTree>();
	private ArrayList<Page> pages = new ArrayList<Page>();
	private Vector<Tuple> page = new Vector<Tuple>();

	public Table(String tableName, ArrayList<String> columnNames, ArrayList<String> columnTypes,
			ArrayList<Boolean> clustered, ArrayList<Boolean> indexed, String clusteredKey, int maxRows) {
		this.tableName = tableName;
		this.columnNames = columnNames;
		this.columnTypes = columnTypes;
		this.clusteredCoulmns = clustered;
		this.indexedCoulmns = indexed;
		this.clusteredKey = clusteredKey;
		Properties prop = new Properties();
		this.maxRows = maxRows;

	}

	public boolean isBIndexedCol(String strColName) {

		return (btrees.containsKey(strColName));
	}

	public BPTree getBtreeCol(String strColName) {

		BPTree tree = btrees.get(strColName);
		return tree;
	}

	public boolean isRIndexedCol(String strColName) {

		return (rtrees.containsKey(strColName));
	}

	public RTree getRtreeCol(String strColName) {

		RTree tree = rtrees.get(strColName);
		return tree;
	}

	// to find the pages

	/**
	 * 
	 * @return the new file for the newly created page
	 */
	public String addPage() {
		String filename = tableName + "_" + (++numOfPages);

		File file = new File(filename + ".ser");
		try {
			file.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return filename;
	}

	public ArrayList<String> findPages(Tuple t) {
		Comparable tKey = t.getKeyValue();
		ArrayList<String> candidatePages = new ArrayList<String>();
		for (Page p : pages) {
			Comparable minKey = p.getMinKey();
			Comparable maxKey = p.getMaxKey();
			if (tKey.compareTo(minKey) > 0 && tKey.compareTo(maxKey) < 0) {
				candidatePages.add((String) tKey);
			}
		}
		return candidatePages;

	}

	public void Write(String filename) {
		System.out.println("WRITE: " + page);
		// Serialization

		try {
			// Saving of object in a file
			FileOutputStream file1 = new FileOutputStream(filename + ".ser"); // overwrites file?
			ObjectOutputStream out = new ObjectOutputStream(file1);

			out.writeObject(page);

			out.close();
			file1.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		page.clear();
	}

	public void Read(String filename) {

		try {

			FileInputStream fi = new FileInputStream((filename + ".ser"));

			try {
				ObjectInputStream o = new ObjectInputStream(fi);

				o.read();
				page = (Vector<Tuple>) o.readObject();

				o.close();
				fi.close();
			} catch (EOFException e) {

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("READ: " + page);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Page getNextPage(Page currFile) {
		int i = 0;
		for (i = 0; i < pages.size(); i++) {
			if (pages.get(i).getFileName().equals(currFile.getFileName())) {
				break;
			}
		}
		if (i + 1 < pages.size())
			return pages.get(i + 1);
		else
			return null;

		/*
		 * Comparable currKey = pages.get(currFile)[2];
		 * 
		 * Object[] tmpObj = (pages.keySet().toArray()); String[] tmp = new
		 * String[tmpObj.length]; int j = 0; for (Object name : tmpObj) { tmp[j] =
		 * (String) name; j++; } String[] keyArr = new String[tmp.length]; int i; for (i
		 * = 0; i < tmp.length; i++) keyArr[i] = (String) tmp[i]; boolean found = false;
		 * for (i = 0; i < keyArr.length; i++) { Comparable maxIndex =
		 * pages.get(keyArr[i])[2]; if (currKey.compareTo(maxIndex) <= 0 && keyArr[i] !=
		 * currFile) // compare to min index of each page found = true; break; // stop
		 * at page wanted (first page where entry key is greater) } if (found) { return
		 * keyArr[i]; } else { return null; }
		 */

	}

	public void insertBTrees(Tuple t, Page page) {
		Set<String> cols = t.getAttributes().keySet();
		for (String col : cols) {
			BPTree bt = btrees.get(col);
			if (bt != null) {
				Ref ref = new Ref(page.getFileName(), (Date) t.getAttributes().get("TouchDate"));
				bt.insert((t.getAttributes()).get(col), ref);
				// .out.println(col + " after insert " + bt.toString());

			}
		}
		// .out.println("no more B indices");
	}

	public void insertRTrees(Tuple t, Page page) {
		Set<String> cols = t.getAttributes().keySet();
		for (String col : cols) {
			RTree bt = rtrees.get(col);
			if (bt != null) {
				Ref ref = new Ref(page.getFileName(), (Date) t.getAttributes().get("TouchDate"));
				bt.insert((Region) (t.getAttributes()).get(col), ref);
				// .out.println(col + " after insert " + bt.toString());

			}
		}
		// .out.println("no more B indices");
	}

	public boolean checkInsert(Tuple t) throws DBAppException {
		Hashtable<String, String> tableData = readTableMetadata();

		Object[] colNamesObj = (tableData.keySet().toArray());
		String[] colNames = new String[colNamesObj.length];
		int j = 0;
		for (Object name : colNamesObj) {
			colNames[j] = (String) name;
			j++;
		}

		if (colNames.length != t.getAttributes().size()) {

			throw new DBAppException("please enter all fields");

		}

		for (String col : colNames) {
			// .out.println("TYPE CHECKK " + col + "
			// "+(tableData.get(col).toLowerCase()+""+(t.getAttributes().get(col)).getClass().getCanonicalName().toLowerCase()));
			if (!(tableData.get(col).toLowerCase())
					.equals((t.getAttributes().get(col)).getClass().getCanonicalName().toLowerCase())) {
				throw new DBAppException("type msimatch.cannot insert");
			}
		}
		return true;
	}

	public void insert(Tuple t, boolean shift) {
		if (page != null)
			page.clear();
		try {
			checkInsert(t);
		} catch (DBAppException e) {

			e.printStackTrace();
		}

		if (pages.isEmpty()) {
			String firstFile = addPage();
			Comparable[] info = new Comparable[3];
			info[0] = 0;
			info[1] = t.getKeyValue();
			info[2] = t.getKeyValue();
			Page firstPage = new Page(firstFile, t.getKeyValue(), t.getKeyValue(), maxRows);
			pages.add(firstPage);
		}

		String[] allFiles = new String[pages.size()];
		int f = 0;
		for (Page pg : pages) {
			allFiles[f] = (String) pg.getFileName();
			f++;
		}

		Comparable[] allMin = new Comparable[allFiles.length];
		Comparable[] allMax = new Comparable[allFiles.length];
		int i = 0;
		for (Page p : pages) {
			allMin[i] = (Comparable) p.getMinKey();
			allMax[i] = (Comparable) p.getMaxKey();
			i++;
		}

		Arrays.sort(allMin);
		Arrays.sort(allMax);
		ArrayList<Page> options = new ArrayList<Page>();

		Arrays.sort(allFiles);

		Comparable[] bounds = new Comparable[allMin.length + allMax.length];
		for (int l = 0; l < bounds.length - 1; l += 2) {
			bounds[l] = allMin[l / 2];
			bounds[l + 1] = allMax[l / 2];
		}

		if (!isBIndexedCol(clusteredKey) && !isRIndexedCol(clusteredKey)) {
			if (t.getKeyValue().compareTo(bounds[0]) <= 0) {
				for (Page p : pages) {
					if (p.getMinKey().compareTo(bounds[0]) == 0)
						options.add(p);

				}
			}
			if (t.getKeyValue().compareTo(bounds[bounds.length - 1]) >= 0) {
				for (Page p : pages) {
					if (p.getMaxKey().compareTo(bounds[bounds.length - 1]) == 0)
						options.add(p);

				}
			}

			for (int l = 0; l < bounds.length - 1; l++) {

				if (bounds[l].compareTo(t.getKeyValue()) <= 0 && bounds[l + 1].compareTo(t.getKeyValue()) >= 0) {
					if (l % 2 == 0) { // in a file's limits
						for (Page p : pages) {
							if (p.getMinKey().compareTo(bounds[l]) == 0 || p.getMaxKey().compareTo(bounds[l + 1]) == 0)
								options.add(p);

						}
					} else {
						for (Page p : pages) {
							if (p.getMinKey().compareTo(bounds[l + 1]) == 0 || p.getMaxKey().compareTo(bounds[l]) == 0)
								options.add(p);

						}
					}

				}
			}

		} else {
			if (isBIndexedCol(clusteredKey)) {
				BPTree clusBtree = getBtreeCol(clusteredKey);
				ArrayList<String> BoptionfileNames = clusBtree.getInsertPage(t.getKeyValue());
				// .out.println(BoptionfileNames);

				for (String name : BoptionfileNames) {
//				//.out.println(name);
					for (int z = 0; z < pages.size(); z++) {

//					//.out.print(pages.get(z));
						if (pages.get(z).getFileName().equals(name)) {
							options.add(pages.get(z));
						}
					}
				}
			}

			if (isRIndexedCol(clusteredKey)) {
				RTree clusRtree = getRtreeCol(clusteredKey);
				ArrayList<String> RoptionfileNames = clusRtree.getInsertPage((Region) t.getKeyValue());
				for (String name : RoptionfileNames) {
					for (int z = 0; z < pages.size(); z++) {
						if (pages.get(z).getFileName().equals(name)) {
							options.add(pages.get(z));
						}
					}
				}
			}

		}

		if (options.size() == 0) {
			if (pages.isEmpty()) {
				String firstFile = addPage();
				Comparable[] info = new Comparable[3];
				info[0] = 0;
				info[1] = t.getKeyValue();
				info[2] = t.getKeyValue();
				Page firstPage = new Page(firstFile, t.getKeyValue(), t.getKeyValue(), maxRows);
				this.pages.add(firstPage);
				options.add(firstPage);
			} else {
				if (t.getKeyValue().compareTo(allMin[0]) <= 0) // smaller than smallest key
				{
					for (int j = 0; j < pages.size(); j++) {
						if (pages.get(j).getMinKey() == allMin[0]) {
							options.add(pages.get(j));

						}

					}
				} else // larger than largest key
				{

					if (t.getKeyValue().compareTo(allMax[allMax.length - 1]) >= 0) {

						for (int j = 0; j < pages.size(); j++) {
							if (pages.get(j).getMaxKey() == allMax[allMax.length - 1]) {
								// .out.println("big 2" + allFiles[j]);
								options.add(pages.get(j));
							}

						}
					}
				}

			}

		}

		insertHelper(t, options, shift);
	}

	public void insertHelper(Tuple t, ArrayList<Page> pages, boolean shift) {
		// .out.println("options for " + t.getKeyValue() + " are " + pages.toString());
		boolean found = false;
		for (int i = 0; i < pages.size(); i++) {
			if (!pages.get(i).isFull()) {

				found = true;
				Read(pages.get(i).getFileName());
				insertPage(t, pages.get(i), pages.get(i), shift);
				updatepages(pages.get(i));
				Write(pages.get(i).getFileName());
				return;
			}
		}
		if (!found) {
			Read(pages.get(pages.size() - 1).getFileName());
			insertPage(t, pages.get(pages.size() - 1), pages.get(pages.size() - 1), shift);
			Tuple temp = page.lastElement();
			page.remove(page.lastElement());
			updatepages(pages.get(pages.size() - 1));
			Write(pages.get(pages.size() - 1).getFileName());

			Page next = getNextPage(pages.get(pages.size() - 1)); // what if arraylist is not sorted by minkey?

			if (next == null) {// no next page
				page.clear();
				String file1 = addPage(); // adding the info in the hashtable
				Page firstPage = new Page(file1, t.getKeyValue(), t.getKeyValue(), maxRows);
				this.pages.add(firstPage);
				page.add(temp);
				Write(file1); // write to new file
				return;
			} else {
				if (!next.isFull()) { // shifting can be done
					page.clear();
					Read(next.getFileName());
					insertPage(temp, pages.get(pages.size() - 1), next, true);
					updatepages(next);
					Write(next.getFileName());
					return;
				} else { // next is full
					page.clear();
					Read(next.getFileName());
					insertPage(temp, pages.get(pages.size() - 1), next, true);
					Tuple temp2 = page.lastElement();
					page.remove(page.lastElement());
					updatepages(next);
					Write(next.getFileName());
					insert(temp2, true);
				}

			}

		}
	}

	/*
	 * public boolean isPageFull(String filename) { int noOfRows = (int)
	 * pages.get(filename)[0]; if (maxRows - noOfRows > 0) { return false; } else
	 * return true; }
	 */
	public void insertPage(Tuple t, Page fromShift, Page pageOfInsertion, boolean shift) {
		if (this.page.size() > 0) {
			Iterator it = this.page.iterator();
			int i = 0;
			boolean inserted = false;
			while (it.hasNext() && inserted == false) {
				Tuple tmp = (Tuple) it.next();
				i++;

				if (t.compareTo(tmp) <= 0) {
					this.page.insertElementAt(t, i - 1);
					inserted = true;
				}
			}
			if (!inserted) {
				page.add(t);

			}
		} else {
			this.page.add(t);

		}
		if (!shift) {
			insertBTrees(t, pageOfInsertion);
			insertRTrees(t, pageOfInsertion);

		} else {
			for (String col : btrees.keySet()) {
				btrees.get(col).updateRef(t.getAttributes().get(col), fromShift.getFileName(),
						pageOfInsertion.getFileName(), (Date) t.getAttributes().get("TouchDate"));
			}
			for (String col : rtrees.keySet()) {
				rtrees.get(col).updateRef((Region) t.getAttributes().get(col), fromShift.getFileName(),
						pageOfInsertion.getFileName(), (Date) t.getAttributes().get("TouchDate"));
			}
		}
	}

	/**
	 * 
	 * @param htblColNameValue the hashtable to be deleted from the table and the
	 *                         values are anded together
	 * @throws DBAppException an exception if the is incompatible types or metadata
	 *                        error such as a column that doesn't exist
	 */

	public void delete(Hashtable<String, Comparable> htblColNameValue) throws DBAppException {
		page.clear();
		Hashtable<String, String> temp = readTableMetadata();
		for (String col : htblColNameValue.keySet()) {
			if (!temp.keySet().contains(col))
				throw new DBAppException("metadata file Error");
			if (!(htblColNameValue.get(col).getClass().getCanonicalName().toLowerCase())
					.equals(temp.get(col).toLowerCase()))
				throw new DBAppException("Incompatible types");
		}
		Hashtable<String, Integer> colIndexedPages = new Hashtable<String, Integer>();
		for (String key : htblColNameValue.keySet()) {
			if (isBIndexedCol(key)) {
				int l = getBtreeCol(key).getDeletePage(htblColNameValue.get(key)).size();
				colIndexedPages.put(key, l);

			}
		}

		int minSoFar = 0;
		String minCol = "";
		for (String key : colIndexedPages.keySet()) {
			minSoFar = colIndexedPages.get(key);
			minCol = key;
			break;

		}
		for (String key : colIndexedPages.keySet()) {
			if (colIndexedPages.get(key) <= minSoFar) {
				minSoFar = colIndexedPages.get(key);
				minCol = key;
			}

		}
		if (colIndexedPages.isEmpty()) {
			for (int i = 0; i < pages.size(); i++) {
				deleteFromPage(pages.get(i).getFileName(), htblColNameValue);
			}

		} else {
			ArrayList<String> canPages = btrees.get(minCol).getDeletePage(htblColNameValue.get(minCol));
			for (String file : canPages) {
				deleteFromPage(file, htblColNameValue);
			}
		}

	}

	/**
	 * 
	 * @param fileName         name of the file to delete t tuple from
	 * @param htblColNameValue tuple to be deleted from fileName
	 */

	private void deleteFromPage(String fileName, Hashtable<String, Comparable> htblColNameValue) {

		page.clear();
		Read(fileName);
		int i = 0;
		while (i < page.size()) {// startoftrial

			if (page.get(i).helperDelete(htblColNameValue)) {

				// deleting that object from the BPtrees and RTrees
				for (String key : htblColNameValue.keySet()) {
					BPTree tree = btrees.get(key);
					if (tree != null)
						tree.delete(htblColNameValue.get(key), (Date) (page.get(i)).getValueOfColumn("TouchDate"));

				}
				for (String key : htblColNameValue.keySet()) {
					RTree tree = rtrees.get(key);
					if (tree != null)
						tree.delete((Region) htblColNameValue.get(key),
								(Date) (page.get(i)).getValueOfColumn("TouchDate"));

				}
				page.remove(i);

			} else {
				i++;
			}
		} // endoftrial
		if (page.size() == 0) {
			deletePage(fileName);
			Page toBeDeleted = null;
			for (Page p : pages) {
				if (p.getFileName().equals(fileName)) {
					toBeDeleted = p;

				}
			}
			pages.remove(toBeDeleted);
		} else {
			// Changing the key of the page
			for (Page p : pages) {
				if (p.getFileName().equals(fileName)) {
					Comparable minKey = p.getMinKey();
					Comparable maxKey = p.getMaxKey();
					Comparable key = htblColNameValue.get(this.clusteredKey);
					if (key != null) {
						if (minKey.compareTo(key) > 0)
							p.setMinKey(key);
						if (maxKey.compareTo(key) < 0)
							p.setMaxKey(key);
					}

				}
			}

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

	public Hashtable<String, String> readTableMetadata() throws DBAppException {
		String line = "";
		String splitBy = ",";
		ArrayList<String> tableColNames = new ArrayList<String>();
		ArrayList<String> tableColTypes = new ArrayList<String>();

		try {
			// parsing a CSV file into BufferedReader class constructor
			BufferedReader br = new BufferedReader(new FileReader("data/metadata.csv"));
			while ((line = br.readLine()) != null) // returns a Boolean value
			{
				String[] entry = line.split(splitBy); // use comma as separator
				if (entry[0].contentEquals(tableName)) {

					tableColNames.add(entry[1]);
					tableColTypes.add(entry[2]);
				}

			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Hashtable<String, String> colInfo = new Hashtable<String, String>();
		if (tableColNames.size() == tableColTypes.size()) {

			for (int i = 0; i < tableColNames.size(); i++) {
				colInfo.put(tableColNames.get(i), tableColTypes.get(i));
			}

		} else {
			throw new DBAppException("Table metadata error");
		}
		return colInfo;
	}

	/**
	 * 
	 * @param strClusteringKey value of the key of the tuple to be updated
	 * @param t                new values for the columns of the updated tuple
	 * @throws DBAppException
	 */
	public void updateTable(String strClusteringKey, Tuple t) throws DBAppException {
		Hashtable<String, String> temp = readTableMetadata();
		if (btrees.keySet().contains(this.clusteredKey)) {
			this.updateTableIndexedB(strClusteringKey, t);
			return;
		} else {
			if (rtrees.keySet().contains(this.clusteredKey)) {
				this.updateTableIndexedR(strClusteringKey, t);
				return;
			}
		}

		for (Page p : pages) {
			String file = p.getFileName();
			page.clear();
			Read(file);

			for (Tuple t1 : page) {
				String coltype = temp.get(this.clusteredKey);
				Date currentdate = new Date();
				Set<String> indexedcolns = btrees.keySet();
				Set<String> spatialIndex = rtrees.keySet();
				Set<String> allcolns = t.getAttributes().keySet();
				if (coltype.equals("java.lang.Integer")) {
					int value = Integer.parseInt(strClusteringKey);
					if (value == (int) t1.getKeyValue()) {

						for (String key : t.getAttributes().keySet()) {

							if (indexedcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, p.getFileName());
							}
							if (spatialIndex.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, p.getFileName());
							}

							t1.edit(key, t.getValueOfColumn(key));

						}
						t1.edit("TouchDate", currentdate);

						for (String key : indexedcolns) {
							if (!allcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t1.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, p.getFileName());
							}
						}
						for (String key : spatialIndex) {
							if (!allcolns.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t1.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, p.getFileName());
							}
						}

					}

				} else if (coltype.equals("java.lang.Double")) {
					double value = Double.parseDouble(strClusteringKey);
					if (value == (double) t1.getKeyValue()) {
						for (String key : t.getAttributes().keySet()) {
							if (indexedcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, p.getFileName());
							}
							if (spatialIndex.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, p.getFileName());
							}

							t1.edit(key, t.getValueOfColumn(key));

						}
						t1.edit("TouchDate", currentdate);

						for (String key : indexedcolns) {
							if (!allcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t1.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, p.getFileName());
							}
						}
						for (String key : spatialIndex) {
							if (!allcolns.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t1.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, p.getFileName());
							}
						}
					}

				} else if (coltype.contentEquals("java.util.Date")) {
					Date value = new Date(strClusteringKey);
					if (value.equals((Date) t1.getKeyValue())) {
						// .out.println("OMG THEY ARE EQUAL!");
						for (String key : t.getAttributes().keySet()) {
							if (indexedcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, p.getFileName());
							}
							if (spatialIndex.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, p.getFileName());
							}
							t1.edit(key, t.getValueOfColumn(key));

						}
						t1.edit("TouchDate", currentdate);

						for (String key : indexedcolns) {
							if (!allcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t1.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, p.getFileName());
							}
						}
						for (String key : spatialIndex) {
							if (!allcolns.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t1.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, p.getFileName());
							}
						}

					}
				}

				else if (coltype.contentEquals("Region")) {
					Region value = Region.StringToRegion(strClusteringKey);
					if (value.equals((Region) t1.getKeyValue())) {
						for (String key : t.getAttributes().keySet()) {
							if (indexedcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, p.getFileName());
							}
							if (spatialIndex.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, p.getFileName());
							}
							t1.edit(key, t.getValueOfColumn(key));

						}
						t1.edit("TouchDate", currentdate);

						for (String key : indexedcolns) {
							if (!allcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t1.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, p.getFileName());
							}
						}
						for (String key : spatialIndex) {
							if (!allcolns.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t1.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, p.getFileName());
							}
						}

					}
				} else {
					String value = strClusteringKey;
					if (value.equals(t1.getKeyValue())) {

						// .out.println("OMG THEY ARE EQUAL!");
						for (String key : t.getAttributes().keySet()) {
							if (indexedcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, p.getFileName());
							}
							if (spatialIndex.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, p.getFileName());
							}
							t1.edit(key, t.getValueOfColumn(key));

						}
						t1.edit("TouchDate", currentdate);

						for (String key : indexedcolns) {
							if (!allcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t1.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, p.getFileName());
							}
						}
						for (String key : spatialIndex) {
							if (!allcolns.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t1.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, p.getFileName());
							}
						}

					}
				}
			}
			Write(file);
		}
	}

	private void updateTableIndexedR(String strClusteringKey, Tuple t) throws DBAppException {
		Hashtable<String, String> temp = readTableMetadata();
		RTree tree = rtrees.get(this.clusteredKey);
		String coltype = temp.get(this.clusteredKey);
		Date currentdate = new Date();
		Set<String> indexedcolns = btrees.keySet();
		Set<String> spatialIndex = rtrees.keySet();
		Set<String> allcolns = t.getAttributes().keySet();
		if (coltype.contentEquals("Region")) {
			Region value = Region.StringToRegion(strClusteringKey);
			ArrayList<String> files = tree.getDeletePage(value);
			for (String f : files) {
				page.clear();
				Read(f);
				for (int i=0;i<page.size();i++) {
					Tuple t1=page.get(i);
					if (value.equals((Region) t1.getKeyValue())) {

						for (String key : t.getAttributes().keySet()) {

							if (indexedcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, f);
							}
							if (spatialIndex.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, f);
							}

							t1.edit(key, t.getValueOfColumn(key));

							t1.edit("TouchDate", currentdate);
						}
						for (String key : indexedcolns) {
							if (!allcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t1.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, f);
							}
						}
						for (String key : spatialIndex) {
							if (!allcolns.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t1.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, f);
							}
						}

						Write(f);
					}
				}
			}

		}
	}

	/**
	 * 
	 * @param strClusteringKey
	 * @param t
	 * @throws DBAppException
	 */
	private void updateTableIndexedB(String strClusteringKey, Tuple t) throws DBAppException {
		Hashtable<String, String> temp = readTableMetadata();
		BPTree tree = btrees.get(this.clusteredKey);
		String coltype = temp.get(this.clusteredKey);
		Date currentdate = new Date();
		Set<String> indexedcolns = btrees.keySet();
		Set<String> spatialIndex = rtrees.keySet();
		Set<String> allcolns = t.getAttributes().keySet();
		if (coltype.equals("java.lang.Integer")) {
			int value = Integer.parseInt(strClusteringKey);
			ArrayList<String> files = tree.getDeletePage(value);
			for (String f : files) {
				page.clear();
				Read(f);
				for (int i=0;i<page.size();i++) {
					Tuple t1=page.get(i);
					if (value == (int) t1.getKeyValue()) {

						for (String key : t.getAttributes().keySet()) {

							if (indexedcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, f);
							}
							if (spatialIndex.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, f);
							}

							t1.edit(key, t.getValueOfColumn(key));

							t1.edit("TouchDate", currentdate);
						}
						for (String key : indexedcolns) {
							if (!allcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t1.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, f);
							}
						}
						for (String key : spatialIndex) {
							if (!allcolns.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t1.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, f);
							}
						}

						Write(f);
					}
				}
			}

		} else if (coltype.equals("java.lang.Double")) {
			double value = Double.parseDouble(strClusteringKey);
			ArrayList<String> files = tree.getDeletePage(value);
			for (String f : files) {
				page.clear();
				Read(f);
//				for (Tuple t1 : page) {
				for (int i=0;i<page.size();i++) {
					Tuple t1=page.get(i);
					if (value == (double) t1.getKeyValue()) {

						for (String key : t.getAttributes().keySet()) {

							if (indexedcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, f);
							}
							if (spatialIndex.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, f);
							}

							t1.edit(key, t.getValueOfColumn(key));

							t1.edit("TouchDate", currentdate);
						}
						for (String key : indexedcolns) {
							if (!allcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t1.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, f);
							}
						}
						for (String key : spatialIndex) {
							if (!allcolns.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t1.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, f);
							}
						}

						Write(f);
					}
				}
			}

		} else if (coltype.contentEquals("java.util.Date")) {
			Date value = new Date(strClusteringKey);
			ArrayList<String> files = tree.getDeletePage(value);
			for (String f : files) {
				page.clear();
				Read(f);
//				for (Tuple t1 : page) {
				for (int i=0;i<page.size();i++) {
					Tuple t1=page.get(i);
					if (value.equals((Date) t1.getKeyValue())) {

						for (String key : t.getAttributes().keySet()) {

							if (indexedcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, f);
							}
							if (spatialIndex.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, f);
							}

							t1.edit(key, t.getValueOfColumn(key));

							t1.edit("TouchDate", currentdate);
						}
						for (String key : indexedcolns) {
							if (!allcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t1.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, f);
							}
						}
						for (String key : spatialIndex) {
							if (!allcolns.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t1.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, f);
							}
						}

						Write(f);
					}
				}
			}
		}

		else {
			String value = strClusteringKey;
			ArrayList<String> files = tree.getDeletePage(value);
			for (String f : files) {
				page.clear();
				Read(f);
//				for (Tuple t1 : page) {
				for (int i=0;i<page.size();i++) {
					Tuple t1=page.get(i);
					if (value.equals((String) t1.getKeyValue())) {

						for (String key : t.getAttributes().keySet()) {

							if (indexedcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, f);
							}
							if (spatialIndex.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, f);
							}

							t1.edit(key, t.getValueOfColumn(key));

							t1.edit("TouchDate", currentdate);
						}
						for (String key : indexedcolns) {
							if (!allcolns.contains(key)) {
								btrees.get(key).update(t1.getValueOfColumn(key), t1.getValueOfColumn(key),
										(Date) (t1.getValueOfColumn("TouchDate")), currentdate, f);
							}
						}
						for (String key : spatialIndex) {
							if (!allcolns.contains(key)) {
								rtrees.get(key).update((Region) t1.getValueOfColumn(key),
										(Region) t1.getValueOfColumn(key), (Date) (t1.getValueOfColumn("TouchDate")),
										currentdate, f);
							}
						}

						Write(f);
					}
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
	// TODO can be deleted?

	public void updatepages(Page fileName) {

		fileName.setMinKey(page.firstElement().getKeyValue());

		fileName.setMaxKey(page.lastElement().getKeyValue());

		fileName.setSize(page.size());
	}

	public void deletePage(String fileName) {

		File file = new File(fileName + ".ser");

		boolean exists = file.exists();
		if (exists == true) {
			// deleting content of the file
			try {
				PrintWriter writer = new PrintWriter(file);
				writer.print("");
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// printing the permissions associated with the file

			System.out.println(" excutable: " + file.canExecute());
			System.out.println("Readable: " + file.canRead());
			System.out.println("Writable: " + file.canWrite());
		} else {
			System.out.println("File not found.");
		}
		System.out.println(file.getName());
		System.out.println("Is File " + file.getName() + " deleted: " + file.delete());
		System.out.println("Does file exist after deletion: " + file.exists());
		/*
		 * System.out.
		 * println("creating a new file having the same name as the deleted one..........."
		 * ); try { System.out.println(file.createNewFile()); } catch(Exception e) {
		 * e.printStackTrace(); }
		 */
		for (int i = 0; i < pages.size(); i++) {
			if (pages.get(i).equals(fileName))
				pages.remove(i);
		}

	}

	/**
	 * 
	 * @param t tuple t
	 * @return an array of files that may contain t sorted in ascending order
	 *         according to their minimum key
	 */
	public ArrayList<Page> findPage(Tuple t) {
		Comparable tupleKey = t.getKeyValue();
		// holds the values of the pages that may contain the tuple
		ArrayList<Page> temp = new ArrayList<Page>();
		for (Page p : pages) {
			Comparable minKey = p.getMinKey();
			Comparable maxKey = p.getMaxKey();
			if (tupleKey.compareTo(minKey) >= 0 && tupleKey.compareTo(maxKey) <= 0) {
				temp.add(p);
			}

		}
		ArrayList<Page> res = new ArrayList<Page>();
		for (Page p1 : temp) {
			Comparable minKey1 = p1.getMinKey();
			Page toBeInserted = p1;
			for (Page p2 : temp) {
				Comparable minKey2 = p2.getMinKey();
				if (!p1.equals(p2) && minKey1.compareTo(minKey2) > 0) {
					toBeInserted = p2;
				}
			}
			res.add(toBeInserted);
		}
		return res;
	}

	public String[] intoArray(Object[] h) {

		String[] Names = new String[h.length];
		int j = 0;
		for (Object name : h) {
			Names[j] = (String) name;
			j++;
		}

		return null;

	}

	public static void main(String[] args) throws DBAppException {
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("id");
		columns.add("name");
		ArrayList<String> types = new ArrayList<String>();
		types.add("java.lang.Integer");
		types.add("java.lang.String");
		ArrayList<Boolean> indexed = new ArrayList<Boolean>();
		indexed.add(false);
		indexed.add(false);
		ArrayList<Boolean> clustered = new ArrayList<Boolean>();
		clustered.add(true);
		clustered.add(false);
//		
		Table t = new Table("Student", columns, types, indexed, clustered, "id", 10);
////		Read("file.ser");
//		t.Write("tablefile");
//		t.Read("tablefile");

		SQLTerm[] arrSQLTerms;
		arrSQLTerms = new SQLTerm[2];

//		query1 = new SQLTerm("Student", "name", "=","John Noor");
		SQLTerm query1 = new SQLTerm();
		SQLTerm query2 = new SQLTerm();
		arrSQLTerms[0] = query1;
		arrSQLTerms[1] = query2;
		arrSQLTerms[0].strTableName = "Student";
		arrSQLTerms[0].strColumnName = "name";
		arrSQLTerms[0].strOperator = "=";
		arrSQLTerms[0].objValue = "John Noor";
		// .out.println(arrSQLTerms[0].strTableName);
		arrSQLTerms[1].strTableName = "Student";
		arrSQLTerms[1].strColumnName = "gpa";
		arrSQLTerms[1].strOperator = "=";
		arrSQLTerms[1].objValue = new Double(1.5);
		String[] strarrOperators = new String[1];
		strarrOperators[0] = "OR";
		t.executeQuery(arrSQLTerms[0].strColumnName, arrSQLTerms[0].strOperator, arrSQLTerms[0].objValue);
		// select * from Student where name = �John Noor� or gpa = 1.5;

	}

	public Vector<Tuple> executeQuery(String strColumnName, String strOperator, Object objValue) throws DBAppException {
		String strColumnType = "";
		// get type of strColumnName
		for (int i = 0; i < columnNames.size(); i++) {
			if (columnNames.get(i).equals(strColumnName)) {
				strColumnType = columnTypes.get(i);
			}
		}
		if (strColumnType == "") {
			throw new DBAppException("column does not exist");
		}
		// cast objValue to that type (reflection)(switch 3ady lol)

		Comparable keyValue;
		switch (strColumnType.toLowerCase()) {
		case "java.lang.integer":
			keyValue = new Integer((int) objValue);
			break;
		case "java.lang.string":
			keyValue = new String((String) objValue);
			break;
		case "java.lang.double":
			keyValue = new Double((double) objValue);
			break;
		case "java.lang.boolean":
			keyValue = new Boolean((boolean) objValue);
			break;
//		case "java.util.date":keyValue = new Date((Date) objValue);break;
		case "java.awt.polygon":
			keyValue = (Region) objValue;
			break;
		case "dbengine.region":
			keyValue = (Region) objValue;
			break;
		default:
			throw new DBAppException("type not supported");
		}

		// switch on operator to call suitable search method
//		if (strOperator.equals("=")) {
//			//.out.println("searchEqual " + keyValue.getClass().getCanonicalName());
//			return searchEqual(strColumnName, keyValue);
//		}else {
//		if(strOperator.equals("!=")) {
//			//.out.print("searchNotEqual " + keyValue.getClass().getCanonicalName());
//			return searchNotEqual(strColumnName, keyValue);
//		}else {
//		if(strOperator.equals(">=")) {
//			//.out.println("searchGreaterOREqual " + keyValue.getClass().getCanonicalName());
//			return searchGreaterOREqual(strColumnName, keyValue);
//		}else {
//		if(strOperator.equals("<=")) {
//			//.out.println("searchLessOREqual " + keyValue.getClass().getCanonicalName());
//			return searchLessOREqual(strColumnName, keyValue);
//		}else {
//		if(strOperator.equals(">")) {		
//		//.out.println("searchGreater " + keyValue.getClass().getCanonicalName());
//		return searchGreater(strColumnName, keyValue);
//		}else {
//		if(strOperator.equals("<")) {		
//		//.out.println("searchLess " + keyValue.getClass().getCanonicalName());
//		return searchLess(strColumnName, keyValue);
//		}else {
//		throw new DBAppException("operator "+ strOperator+" not found");
//		}}}}}}
		switch (strOperator) {
		case "=":
			// .out.println("searchEqual " + keyValue.getClass().getCanonicalName());
			return searchEqual(strColumnName, keyValue);
//			break;
		case "!=":
			// .out.print("searchNotEqual " + keyValue.getClass().getCanonicalName());
			return searchNotEqual(strColumnName, keyValue);
//			break;
		case ">=":
			// .out.println("searchGreaterOREqual " +
			// keyValue.getClass().getCanonicalName());
			return searchGreaterOREqual(strColumnName, keyValue);

//			break;
		case "<=":
			// .out.println("searchLessOREqual " + keyValue.getClass().getCanonicalName());
			return searchLessOREqual(strColumnName, keyValue);

//			break;
		case ">":
			// .out.println("searchGreater " + keyValue.getClass().getCanonicalName());
			return searchGreater(strColumnName, keyValue);

//			break;
		case "<":
			// .out.println("searchLess " + keyValue.getClass().getCanonicalName());
			return searchLess(strColumnName, keyValue);

//			break;
		default:
			throw new DBAppException("operator not found");
		}

	}

	public ArrayList<Page> pagesFromNames(ArrayList<String> names) {
		ArrayList<Page> res = new ArrayList<Page>();
		for (int i = 0; i < names.size(); i++) {
			String nom = names.get(i) + ".ser";
			for (int j = 0; j < this.pages.size(); j++) {
				if (this.pages.get(j).getFileName().equals(names.get(i))) {
					res.add(this.pages.get(j));
				}
			}
		}
		return res;
	}

	public Vector<Tuple> searchEqual(String colName, Comparable value) {
		Vector<Tuple> results = new Vector<Tuple>();
		ArrayList<String> pagesToGoThrough = new ArrayList<String>();
		// check if indexed
//		//.out.println(isBIndexedCol(colName));
//		//.out.println(colName);
		if (isBIndexedCol(colName)) {
System.out.println("using B");
			BPTree Index = getBtreeCol(colName);
			ArrayList<Ref> references = Index.search(value);
			ArrayList<String> pageNames = new ArrayList<String>();
			int z = 0;
			for (Ref r : references) {
				pageNames.add(references.get(z++).getPage());
			}
//			//.out.println("Referenes  "+ pageNames);
			ArrayList<Page> toScan = pagesFromNames(pageNames);

			for (Page p : toScan) {
				Read(p.getFileName());
				for (Tuple t : page) {
//					//.out.println("comparing  "+t.getAttributes().get(colName) +" to " + (value));
					if (t.getAttributes().get(colName).compareTo(value) == 0 ) {
						results.add(t);
					}
				}
			}

		} else {
			if (isRIndexedCol(colName)) {
				System.out.println("using R");

				RTree Index = getRtreeCol(colName);
				ArrayList<Ref> references = Index.search((Region) value);
				ArrayList<String> pageNames = new ArrayList<String>();
				int z = 0;
				for (Ref r : references) {
					pageNames.add(references.get(z++).getPage());
				}
				ArrayList<Page> toScan = pagesFromNames(pageNames);
				for (Page p : toScan) {
					Read(p.getFileName());
					for (Tuple t : page) {
						if (t.getAttributes().get(colName).compareTo(value) == 0 ) {
							results.add(t);
						}
					}
				}

			} else {
				System.out.println("using linear");

				for (Page p : pages) {
					
					Read(p.getFileName());
					for (Tuple t : page) {
						if (t.getAttributes().get(colName).compareTo(value) == 0 ) {
							results.add(t);
						}
					}
				}
			}
			// get index
			// get pages where entries exist

			// or
			// go through all pages

			

		}

		// .out.println("searchEqual result: "+ results);
		 results = removeDuplicates(results);

		return results;
	}

	public Vector<Tuple> searchNotEqual(String colName, Comparable value) {
		Vector<Tuple> results = new Vector<Tuple>();
		ArrayList<String> pagesToGoThrough = new ArrayList<String>();
		// check if indexed

		// get index
		// get pages where entries exist

		if (isBIndexedCol(colName)) {

			BPTree Index = getBtreeCol(colName);
			ArrayList<Ref> references = Index.searchNotEqual(value);
			ArrayList<String> pageNames = new ArrayList<String>();
			int z = 0;
			for (Ref r : references) {
				pageNames.add(references.get(z++).getPage());
			}
			ArrayList<Page> toScan = pagesFromNames(pageNames);

			for (Page p : toScan) {
				Read(p.getFileName());
				for (Tuple t : page) {
					if (t.getAttributes().get(colName).compareTo(value) != 0) {
						results.add(t);
					}
				}
			}

		} else {
			if (isRIndexedCol(colName)) {

				RTree Index = getRtreeCol(colName);
				ArrayList<Ref> references = Index.searchNotEqual((Region) value);
				ArrayList<String> pageNames = new ArrayList<String>();
				int z = 0;
				for (Ref r : references) {
					pageNames.add(references.get(z++).getPage());
				}
				ArrayList<Page> toScan = pagesFromNames(pageNames);

				for (Page p : toScan) {
					Read(p.getFileName());
					for (Tuple t : page) {
						if (t.getAttributes().get(colName).compareTo(value) != 0) {
							results.add(t);
						}
					}
				}

			} else {
				for (Page p : pages) {
					Read(p.getFileName());
					for (Tuple t : page) {
						if (t.getAttributes().get(colName).compareTo(value) != 0) {
							results.add(t);
						}
					}
				}

			}

			// or
			// go through all pages

			
		}
		// .out.println("searchNotEqual result: "+ results);
		 results = removeDuplicates(results);

		return results;

	}

	public Vector<Tuple> searchLessOREqual(String colName, Comparable value) {
		Vector<Tuple> results = new Vector<Tuple>();
		ArrayList<String> pagesToGoThrough = new ArrayList<String>();
		// check if indexed

		// get index
		// get pages where entries exist

		if (isBIndexedCol(colName)) {

			BPTree Index = getBtreeCol(colName);
			ArrayList<Ref> references = Index.searchlessORequal(value);
			ArrayList<String> pageNames = new ArrayList<String>();
			int z = 0;
			for (Ref r : references) {
				pageNames.add(references.get(z++).getPage());
			}
			ArrayList<Page> toScan = pagesFromNames(pageNames);

			for (Page p : toScan) {
				Read(p.getFileName());
				for (Tuple t : page) {
					if (t.getAttributes().get(colName).compareTo(value) <= 0) {
						results.add(t);
					}
				}
			}

		} else {
			if (isRIndexedCol(colName)) {

				RTree Index = getRtreeCol(colName);
				ArrayList<Ref> references = Index.searchlessORequal((Region) value);
				ArrayList<String> pageNames = new ArrayList<String>();
				int z = 0;
				for (Ref r : references) {
					pageNames.add(references.get(z++).getPage());
				}
				ArrayList<Page> toScan = pagesFromNames(pageNames);

				for (Page p : toScan) {
					Read(p.getFileName());
					for (Tuple t : page) {
						if (t.getAttributes().get(colName).compareTo(value) <= 0) {
							results.add(t);
						}
					}
				}

			} else {
				for (Page p : pages) {
					Read(p.getFileName());
					for (Tuple t : page) {
						if (t.getAttributes().get(colName).compareTo(value) <= 0) {
							results.add(t);
						}
					}
				}
			}

			// or
			// go through all pages

			

		}
		// .out.println("searchLessOrEqual result: "+ results);

		 results = removeDuplicates(results);
		 return results;
	}

	public Vector<Tuple> searchGreaterOREqual(String colName, Comparable value) {
		Vector<Tuple> results = new Vector<Tuple>();
		ArrayList<String> pagesToGoThrough = new ArrayList<String>();
		// check if indexed

		// get index
		// get pages where entries exist

		if (isBIndexedCol(colName)) {

			BPTree Index = getBtreeCol(colName);
			ArrayList<Ref> references = Index.searchgreaterORequal(value);
			ArrayList<String> pageNames = new ArrayList<String>();
			int z = 0;
			for (Ref r : references) {
				pageNames.add(references.get(z++).getPage());
			}
			ArrayList<Page> toScan = pagesFromNames(pageNames);

			for (Page p : toScan) {
				Read(p.getFileName());
				for (Tuple t : page) {
					if (t.getAttributes().get(colName).compareTo(value) >= 0) {
						results.add(t);
					}
				}
			}

		} else {
			if (isRIndexedCol(colName)) {

				RTree Index = getRtreeCol(colName);
				ArrayList<Ref> references = Index.searchgreaterORequal((Region) value);
				ArrayList<String> pageNames = new ArrayList<String>();
				int z = 0;
				for (Ref r : references) {
					pageNames.add(references.get(z++).getPage());
				}
				ArrayList<Page> toScan = pagesFromNames(pageNames);

				for (Page p : toScan) {
					Read(p.getFileName());
					for (Tuple t : page) {
						if (t.getAttributes().get(colName).compareTo(value) >= 0) {
							results.add(t);
						}
					}
				}

			} else {
				for (Page p : pages) {
					Read(p.getFileName());
					for (Tuple t : page) {
						if (t.getAttributes().get(colName).compareTo(value) >= 0) {
							results.add(t);
						}
					}
				}
			}

			// or
			// go through all pages

		

		}
		// .out.println("searchGreaterOrEqual result: "+ results);

		 results = removeDuplicates(results);
		 return results;
	}

	public Vector<Tuple> searchLess(String colName, Comparable value) {
		Vector<Tuple> results = new Vector<Tuple>();
		ArrayList<String> pagesToGoThrough = new ArrayList<String>();
		// check if indexed

		// get index
		// get pages where entries exist

		if (isBIndexedCol(colName)) {

			BPTree Index = getBtreeCol(colName);
			ArrayList<Ref> references = Index.searchLess(value);
			ArrayList<String> pageNames = new ArrayList<String>();
			int z = 0;
			for (Ref r : references) {
				pageNames.add(references.get(z++).getPage());
			}
			ArrayList<Page> toScan = pagesFromNames(pageNames);

			for (Page p : toScan) {
				Read(p.getFileName());
				for (Tuple t : page) {
					if (t.getAttributes().get(colName).compareTo(value) < 0) {
						results.add(t);
					}
				}
			}

		} else {
			if (isRIndexedCol(colName)) {

				RTree Index = getRtreeCol(colName);
				ArrayList<Ref> references = Index.searchLess((Region) value);
				ArrayList<String> pageNames = new ArrayList<String>();
				int z = 0;
				for (Ref r : references) {
					pageNames.add(references.get(z++).getPage());
				}
				ArrayList<Page> toScan = pagesFromNames(pageNames);

				for (Page p : toScan) {
					Read(p.getFileName());
					for (Tuple t : page) {
						if (t.getAttributes().get(colName).compareTo(value) < 0) {
							results.add(t);
						}
					}
				}

			} else {
				for (Page p : pages) {
					Read(p.getFileName());
					for (Tuple t : page) {
						if (t.getAttributes().get(colName).compareTo(value) < 0) {
							results.add(t);
						}
					}
				}
			}

			// or
			// go through all pages

		

		}
		// .out.println("searchLess result: "+ results);
		 results = removeDuplicates(results);

		return results;
	}

	public Vector<Tuple> searchGreater(String colName, Comparable value) {
		Vector<Tuple> results = new Vector<Tuple>();
		ArrayList<String> pagesToGoThrough = new ArrayList<String>();
		// check if indexed

		// get index
		// get pages where entries exist

		if (isBIndexedCol(colName)) {

			BPTree Index = getBtreeCol(colName);
			ArrayList<Ref> references = Index.searchgreater(value);
			ArrayList<String> pageNames = new ArrayList<String>();
			int z = 0;
			for (Ref r : references) {
				pageNames.add(references.get(z++).getPage());
			}
			ArrayList<Page> toScan = pagesFromNames(pageNames);

			for (Page p : toScan) {
				Read(p.getFileName());
				for (Tuple t : page) {
					if (t.getAttributes().get(colName).compareTo(value) > 0 ) {
						results.add(t);
					}
				}
			}

		} else {
			if (isRIndexedCol(colName)) {

				RTree Index = getRtreeCol(colName);
				ArrayList<Ref> references = Index.searchgreater((Region) value);
				ArrayList<String> pageNames = new ArrayList<String>();
				int z = 0;
				for (Ref r : references) {
					pageNames.add(references.get(z++).getPage());
				}
				ArrayList<Page> toScan = pagesFromNames(pageNames);

				for (Page p : toScan) {
					Read(p.getFileName());
					for (Tuple t : page) {
						if (t.getAttributes().get(colName).compareTo(value) > 0 ) {
							results.add(t);
						}
					}
				}

			} else {
				for (Page p : pages) {
					Read(p.getFileName());
					for (Tuple t : page) {
						if (t.getAttributes().get(colName).compareTo(value) > 0 ) {
							results.add(t);
						}
					}
				}
			}

			// or
			// go through all pages

			

		}
		 System.out.println("searchGreater result: "+ results);
		 results = removeDuplicates(results);
		return results;
	}

	public Vector<Tuple> AND(Vector<Tuple> A, Vector<Tuple> B) {
		boolean flag = false;
		Vector<Tuple> result = new Vector<Tuple>();
		Iterator<Tuple> itA = A.iterator();
		while (itA.hasNext()) {
			Tuple x = itA.next();
			Hashtable<String, Comparable> atX = x.getAttributes();
			Set<String> keyX = atX.keySet();
			Iterator<Tuple> itB = B.iterator();
			while (itB.hasNext()) {
				Tuple y = itB.next();
				Hashtable<String, Comparable> atY = y.getAttributes();
				if (x.equals(y)) {
					flag = true;
				}
//			//.out.println(y +"    " + x + flag);

			}
			if (flag) {
				flag = false;
				result.add(x);
			}
		}
		// .out.println("AND result: "+ result);
		return result;
	}

	public Vector<Tuple> OR(Vector<Tuple> A, Vector<Tuple> B) {
		boolean flag = true;
		Vector<Tuple> result = new Vector<Tuple>();
		Vector<Tuple> tempresult = new Vector<Tuple>();

		Iterator<Tuple> itA = A.iterator();
		Iterator<Tuple> itB = B.iterator();
		while (itA.hasNext()) {
			Tuple x = itA.next();
			result.add(x);
		}
//		Iterator<Tuple> itResult = result.iterator();
//		while(itB.hasNext()) {
		Tuple y;
		while (true) {
			flag = true;
			try {
				y = itB.next();
			} catch (NoSuchElementException e) {
				break;
			}
//			Hashtable<String, Comparable> atY = y.getAttributes();
//			Set<String> keyY = atY.keySet();
			Tuple res;
			Iterator<Tuple> itResult = result.iterator();

			while (true) {

				try {
					res = itResult.next();
				} catch (NoSuchElementException e) {
					break;
				}
//				
				if (res.equals(y)) {
//					tempresult.add(y);	
					flag = false;
				}

			}
			if (flag) {
				tempresult.add(y);
			}
		}
		result.addAll(tempresult);
		// .out.println("OR result: "+ result);

		return result;
	}

	public Vector<Tuple> XOR(Vector<Tuple> A, Vector<Tuple> B) {
		boolean flag = true;
		Vector<Tuple> anded = this.AND(A, B);
		Vector<Tuple> ored = this.OR(A, B);
		Vector<Tuple> result = new Vector<Tuple>();
		Iterator<Tuple> itOr = ored.iterator();
		Tuple x;
		while (true) {
			flag = true;
			try {
				x = itOr.next();
			} catch (NoSuchElementException e) {
				break;
			}
			Tuple y;
			Iterator<Tuple> itAnd = anded.iterator();

			while (true) {
				try {
					y = itAnd.next();
				} catch (NoSuchElementException e) {
					break;
				}

				if (x.equals(y)) {
					flag = false;
				}
//				//.out.println(y +"    " + x + flag);
			}

			if (flag) {
				flag = true;
				result.add(x);
			}
		}
		// .out.println("XOR result: "+ result);

		return result;
	}

	public void newBTree(String strColName, int nodeSize) throws DBAppException {
		Hashtable<String, String> tableMeta = readTableMetadata();

		String strColType = tableMeta.get(strColName);

		BPTree bt = new BPTree(nodeSize);

		// add to table's indices
		btrees.put(strColName, bt);
		for (Page p : pages) {
			// .out.println("scanning: " + p.getFileName());
		}
		// add everything already in table
		for (Page p : pages) {
			// .out.println("scanning: " + p.getFileName());

			Read(p.getFileName());
			for (Tuple t : page) {
//				//.out.println("data now:  " + page);

				Comparable value = t.getAttributes().get(strColName);
				Ref ref = new Ref(p.getFileName(), (Date) t.getAttributes().get("TouchDate"));
				bt.insert(value, ref);
			}
			page.clear();
		}
//		//.out.println("I AM HEEEERRREEEEEE!!!!!!!!!!!");		

		// .out.println("Tree for " + strColName + ": " + bt.toString());
		// TODO write index into a file
		// should each node be in a file?
	}

	public void newRTree(String strColName, int nodeSize) throws DBAppException {
		Hashtable<String, String> tableMeta = readTableMetadata();

		String strColType = tableMeta.get(strColName);

		RTree bt = new RTree(nodeSize);

		// add to table's indices
		rtrees.put(strColName, bt);

		// add everything already in table
		for (Page p : pages) {
			Read(p.getFileName());
			for (Tuple t : page) {

				Comparable value = t.getAttributes().get(strColName);
				Ref ref = new Ref(p.getFileName(), (Date) t.getAttributes().get("TouchDate"));
				bt.insert((Region) value, ref);
			}
			page.clear();
		}

		// .out.println("Tree for " + strColName + ": " + bt.toString());
		// TODO write index into a file
		// should each node be in a file?
	}
	
	public Vector<Tuple> removeDuplicates(Vector<Tuple> listIn){
		Vector<Tuple> listOut = new Vector<Tuple>();
		boolean flag =true;
		for (Tuple t: listIn) {
			for(int i=0;i<listOut.size();i++) {
				if(t.equals(listOut.get(i))) {
					flag=false;
				}
			}
			if(flag)listOut.add(t);
			flag = true;
		}
		return listOut;
		
	}

}
