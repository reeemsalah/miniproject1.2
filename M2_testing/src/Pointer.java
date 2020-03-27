import java.util.ArrayList;
import java.util.Hashtable; 
  
class Pointer implements Comparable
{ 
	private int length = 1;
	private Hashtable<Comparable, String> pair;
    /* This method returns a Pair which hasmaximum score*/
   
	public Pointer(Comparable key, String page){
		this.pair = new Hashtable<Comparable, String>(1);
		this.pair.put(key, page);
	}
	
	public Comparable getKeyValue() {
		return (Comparable) pair.keySet().toArray()[0];
	}
	
	public String getPage() {
		return (String) pair.values().toArray()[0];
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return this.getKeyValue().compareTo(((Pointer) o).getKeyValue());
	}
	
	public String toString() {
		return "Pointer: " + this.getKeyValue() + " @ " + this.getPage();
	}
	
	
   
} 