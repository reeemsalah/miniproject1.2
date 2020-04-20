package BPTree;

import java.io.Serializable;
import java.util.Date;

public class Ref implements Serializable{
	
	/**
	 * This class represents a pointer to the record. It is used at the leaves of the B+ tree 
	 */
	
	
	private static final long serialVersionUID = 1L;
	private Date touchDate;
	private String pageNo;
	
	public Ref(String pageNo, Date touchDate)
	{
		this.pageNo = pageNo;
		this.touchDate = touchDate;
	}
	
	/**
	 * @return the page at which the record is saved on the hard disk
	 */
	public String getPage()
	{
		return pageNo;
	}
	
	/**
	 * @return the index at which the record is saved in the page
	 */
	public Date getIndexInPage()
	{
		return touchDate;
	}
	@Override
	public String toString()
	{
		return pageNo;
	}

}
