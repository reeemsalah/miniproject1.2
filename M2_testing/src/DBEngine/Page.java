package DBEngine;

public class Page {

	String fileName;
	Comparable minKey;
	Comparable maxKey;
	int maxRows;
	int size;

	public Page(String fileName,Comparable minKey,Comparable maxKey, int maxRows) {
		this.fileName=fileName;
		this.minKey = minKey;
		this.maxKey = maxKey;
		this.maxRows = maxRows;
		this.size = 0;
	}
	
	
}

