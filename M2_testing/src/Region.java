import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Shape;

public class Region extends Polygon implements Comparable {
	public Region(int[] xpoints, int[] ypoints, int npoints) {
		this.xpoints = xpoints;
		this.ypoints = ypoints;
		this.npoints = npoints;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Dimension dim1 = this.getBounds().getSize();
		Dimension dim2 = (Dimension) ((Region) o).getBounds().getSize();
		int area1 = dim1.height * dim1.width;
		int area2 = dim2.height * dim2.width;
		return area2 - area1;

	}

	@Override
	public String toString() {
		String res = "";

		for (int i = 0; i < this.xpoints.length; i++) {
			res += "(" + xpoints[i] + "," + ypoints[i] + ")";
			if (i != xpoints.length - 1)
				res += ",";
		}
		return res;
	}

	/**
	 * 
	 * @param points takes a string containing the string representation of a region
	 * @return the region
	 */
	public static Region StringToRegion(String points) {
		String[] arr = points.split(",");
		int npoints = arr.length/2;
		
		int[] xpoints = new int[npoints];
		int[] ypoints = new int[npoints];
		int j=0;
		int i=0;
		for (i = 0, j=0; i < arr.length&&j<npoints; i+=2,j++) {
			
			int x = Integer.parseInt(arr[i].substring(1));
			int y = Integer.parseInt(arr[i+1].substring(0, arr[i+1].length()-1));
			xpoints[j] = x;
			ypoints[j] = y;

		}
		Region r = new Region(xpoints, ypoints, npoints);
		return r;
	}

	public static void main(String[] args) {
		Region r1 = new Region(new int[] { 1, 2, 3 }, new int[] { 4, 5, 6 }, 3);
		Region r2 = new Region(new int[] { 0, 0, 0 }, new int[] { 0, 1, -1 }, 3);
		System.out.println(r1);
		System.out.println(r2);
		System.out.println(r2.compareTo(r1));
		String s = r1.toString();
		Region r3=StringToRegion(s);
		System.out.println(r1.compareTo(r3));
		System.out.println(r3);
	}
}