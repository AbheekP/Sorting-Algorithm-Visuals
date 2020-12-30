package sortingAlgorithms;

import java.awt.Point;
import java.util.ArrayList;

import UI.ArrayInstance;

public class InsertionSort implements SortingAlgorithm{

	static int step = 0;
	int i = 1;
	int key = -1;
	int j = -1;
	int accesses = 0;
	int comparisons = 0;
	
	@Override
	public void sort(ArrayList<ArrayInstance> arr) {
		for(int i = 1; i < arr.size(); i++) {
			ArrayInstance key = arr.get(i);
			int j = i - 1;
			while(j >= 0 && arr.get(j).getValue() > key.getValue()) {
				arr.set(j + 1, arr.get(j));
				j = j - 1;
			}
			arr.set(j + 1, key);
		}
	}

	@Override
	public boolean nextStep(ArrayList<ArrayInstance> arr) {
		if(i < arr.size()) {
			switch(step) {
			
			case 0: 
				key = i;
				step = 1;
				arr.get(key).setHighlighted(true);
				accesses++;
				return false;
				
			case 1:
				j = key - 1;
				arr.get(j).setSelected(true);
				step = 2;
				accesses++;
				return false;
				
			case 2:
				arr.get(key).setSelected(true);
				step = 3;
				accesses++;
				return false;
				
			case 3:
				if(arr.get(j).getValue() > arr.get(key).getValue()) {
					int tempX = arr.get(j).getX();
					arr.get(j).setLocation(arr.get(key).getX(), arr.get(j).getY());
					arr.get(key).setLocation(tempX, arr.get(key).getY());
					
					arr.get(j).setSelected(false);
					arr.get(key).setSelected(false);
					arr.get(key).setHighlighted(false);
					
					ArrayInstance temp = arr.get(j);
					arr.set(j, arr.get(key));
					arr.set(key, temp);
					
					if(key < 2) {
						step = 0;
						i = i + 1;
					} else {
						key--;
						step = 1;
					} 
				} else {
					arr.get(j).setSelected(false);
					arr.get(key).setSelected(false);
					arr.get(key).setHighlighted(false);
					step = 0;
					i = i + 1;
				}
				comparisons++;
				accesses++;
				return false;
			} 
		}
		return true;
	}

	@Override
	public void reset(ArrayList<ArrayInstance> arr) {
		step = 0;
		i = 1;
		key = -1;
		j = -1;
		accesses = 0;
		comparisons = 0;
	}

	@Override
	public int getAccesses() {
		return accesses;
	}

	@Override
	public int getComparisons() {
		return comparisons;
	}
	
}
