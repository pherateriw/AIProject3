package classification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class DataDiscretizer {
	private int r;
	ArrayList<String[]> data;
	private int v;

	public DataDiscretizer(ArrayList<String[]> data, int v, int r) {
		this.data = data;
		this.v = v;
		this.r = r;
		
		System.out.println("in dd");
	}

	public ArrayList<String[]> discretize() {
		Random rando = new Random();
		// final list
		ArrayList<String[]> binnedData = new ArrayList<String[]>();
		// sort array
		Collections.sort(data, new Comparator<String[]>() {
			@Override
			public int compare(String[] st1, String[] st2) {
				// Parse values for to sort by
				double s1 = Double.parseDouble(st1[v]);
				double s2 = Double.parseDouble(st2[v]);
				if (s1 > s2)
					return 1; // tells Arrays.sort() that s1 comes after s2
				else if (s1 < s2)
					return -1; // tells Arrays.sort() that s1 comes before s2
				else {
					/*
					 * s1 and s2 are equal. Arrays.sort() is stable, so these
					 * two rows will appear in their original order. You could
					 * take it a step further in this block by comparing s1[1]
					 * and s2[1] in the same manner, but it depends on how you
					 * want to sort in that situation.
					 */
					return 0;
				}
			}
		});

		// calculate the number of 
		int ni = (int)Math.floor(data.size() / r);
		int remainder = data.size() % r;
		// for i in range r
		int index = 0;
		for (int i = 0; i < r; i++) {
			ArrayList<String[]> bin = new ArrayList<String[]>();
			double max = 0;
			double min = Double.MAX_VALUE;
			// for j in range ni, add to bin.
			for (int j = 0; j < ni; j++) {
				if (index < data.size()) {
					bin.add(data.get(index));
					double value = Double.valueOf(data.get(index)[v]);
					if (value > max) {
						max = value;
					}
					if (value < min) {
						min = value;
					}
					index++;
				}
				if(j == ni - 1 && i == r - remainder - 1 && index < data.size()){
					//while(index < data.size()){
						double value = Double.valueOf(data.get(index)[v]);
						if (value > max) {
							max = value;
						}
						if (value < min) {
							min = value;
						}
						index++;
					//}
				}
			}
			// Once all j added to bin, calculate middle of bin
			// rewrite all attribute values with middle
			// Add all of bin to binned data
			String m = Double.toString((max + min) / 2);
			for (String[] s : bin) {
				s[v] = m;
				binnedData.add(s);
			}

		}

		return binnedData;
	}

	public int getBinNum() {
		return r;
	}

	public void setBinNum(int r) {
		this.r = r;
	}

	public void setVariable(int v) {
		this.v = v;
	}

	public int getVariable() {
		return v;
	}

}
