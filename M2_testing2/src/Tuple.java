import java.io.Serializable;
import java.util.Hashtable;

@SuppressWarnings("serial")

public class Tuple implements Serializable, Comparable {
	// Name of Clustering Key Column
	private String key;
	@SuppressWarnings("rawtypes")
	private Hashtable<String, Comparable> attributes;

	/**
	 * 
	 * @param columnValues creates a tuple with these column names and values
	 */
	@SuppressWarnings("rawtypes")
	public Tuple(Hashtable<String, Comparable> columnValues, String key) {
		this.attributes = columnValues;
		this.key = key;
	}

	/**
	 * 
	 * @return the attributes and values of the tuple
	 */
	@SuppressWarnings("rawtypes")
	public Hashtable<String, Comparable> getAttributes() {
		return attributes;
	}

	@SuppressWarnings("rawtypes")
	public String getKey() {
		return (String) key;
	}

	/**
	 * 
	 * @return the key value of this
	 */
	@SuppressWarnings("rawtypes")
	public Comparable getKeyValue() {
		return this.getAttributes().get(key);
	}

	/**
	 * 
	 * @param o the Object to be compared to this
	 * @return 0 if their keys are equal pos if this.key is greater than o.key neg
	 *         if this.key is smaller than o.key
	 */
	@Override
	public int compareTo(Object o) {
		Tuple o1 = (Tuple) o;
		@SuppressWarnings("rawtypes")
		Comparable id2 = o1.getKeyValue();
		Comparable id1 = this.getKeyValue();

		return id1.compareTo(id2);
	}

	/**
	 * 
	 * @param columnName name of the column to be edited
	 * @param newValue   the new value to be inserted in the column
	 */
	@SuppressWarnings("rawtypes")
	/**
	 * 
	 * @param columnName column name
	 * @param newValue   new value of that column
	 */
	public void edit(String columnName, Comparable newValue) {

		this.getAttributes().replace(columnName, newValue);
	}

	/**
	 * 
	 * @param columnName the name of the column that we want to get its data
	 * @return the data in the column
	 */
	@SuppressWarnings("rawtypes")
	public Comparable getValueOfColumn(String columnName) {
		return this.getAttributes().get(columnName);
	}

	public String toString() {

		return (this.attributes).toString();
	}

	/**
	 * 
	 * @param htblColNameValue hashtable contains columns and their values
	 * @return true if every column value matches the tuple, false otherwise
	 */
	public boolean helperDelete(Hashtable<String, Comparable> htblColNameValue) {

		for (String key : htblColNameValue.keySet()) {
			if (htblColNameValue.get(key).compareTo(this.getValueOfColumn(key)) != 0)
				return false;
		}
		return true;
	}
}
