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

	public Comparable getMinKey() {
		return minKey;
	}

	public void setMinKey(Comparable minKey) {
		this.minKey = minKey;
	}

	public Comparable getMaxKey() {
		return maxKey;
	}

	public void setMaxKey(Comparable maxKey) {
		this.maxKey = maxKey;
	}

	public String getFileName() {
		return fileName;
	}

	public int getSize() {
		return size;
	}
	
	
}

