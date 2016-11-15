package classification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DataDiscretizer {
	private int r;
	ArrayList<String[]> data;
	private int v;
	public DataDiscretizer(ArrayList<String[]> data) {
		this.data = data;
		
		System.out.println("in dd");
	}
	public ArrayList<String[]> discretize(){
		//final list
		ArrayList<String[]> binnedData = new ArrayList<String[]>();
		//sort array
		Collections.sort(data, new Comparator<String[]>() {//Why is bad sort??
            @Override
            public int compare(String[] st1, String[] st2) {
            double s1 = Double.parseDouble(st1[v]);
            double s2 = Double.parseDouble(st2[v]);
	        if (s1 > s2)
	            return 1;    // tells Arrays.sort() that s1 comes after s2
	        else if (s1 < s2)
	            return -1;   // tells Arrays.sort() that s1 comes before s2
	        else {
	            /*
	             * s1 and s2 are equal.  Arrays.sort() is stable,
	             * so these two rows will appear in their original order.
	             * You could take it a step further in this block by comparing
	             * s1[1] and s2[1] in the same manner, but it depends on how
	             * you want to sort in that situation.
	             */
	            return 0;
            }
        }
		}
		);
		
		// ni = data.size()/r
		double ni = Math.floor(data.size()/r);
		// for i in range r
		
		// for j in range ni, add to bin. 
		//Once all j added to bin, calculate middle of bin
		//rewrite all attribute values with middle
				// Add all of bin to binned data
		return data;
	}
	public int getBinNum(){
		return r;
	}
	public void setBinNum(int r){
		this.r = r;
	}
	public void setVariable(int v){
		this.v = v;
	}
	public int getVariable(){
		return v;
	}
	
	
}
