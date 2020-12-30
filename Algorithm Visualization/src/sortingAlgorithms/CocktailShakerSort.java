package sortingAlgorithms;

import java.util.ArrayList;

import UI.ArrayInstance;

public class CocktailShakerSort implements SortingAlgorithm{

	boolean forward = true;;
	int i = 0; 
	int j = 1; 
	int k = 0;
	int n = 0;
	int step = 1;
	int accesses = 0;
	int comparisons = 0;
	
	@Override
	public void sort(ArrayList<ArrayInstance> arr) {
		
	}

	@Override
	public boolean nextStep(ArrayList<ArrayInstance> arr) {
		if(forward) {
			switch(step) {
			case 0:
				arr.get(i).setSelected(false);
				arr.get(j).setSelected(false);
				i = i + 1;
				
				j++;
				
				if(j > arr.size() - k - 1) {
					j = j - 2;
					k++;
					forward = false;
					return checkFinish(arr);
				}
				
				step = 1;
				return false;
				
			case 1:
				arr.get(i).setSelected(true);
				step = 2;
				accesses++;
				return false;
				
			case 2:
				arr.get(i).setSelected(false);
				arr.get(j).setSelected(true);
				step = 3;
				accesses++;
				return false;
				
			case 3:
				accesses++;
				comparisons++;
				arr.get(i).setSelected(true);
				if(arr.get(i).getValue() > arr.get(j).getValue()) {
					int tempX = arr.get(i).getX();
					arr.get(i).setLocation(arr.get(j).getX(), arr.get(i).getY());
					arr.get(j).setLocation(tempX, arr.get(j).getY());
					
					ArrayInstance temp = arr.get(i);
					arr.set(i, arr.get(j));
					arr.set(j, temp);
				}
				step = 0;
				return false;
			}
		} else {
			switch(step) {
			case 0:
				
				i--;
				j--;
				
				arr.get(i + 1).setSelected(false);
				arr.get(j + 1).setSelected(false);
				if(j < n) {
					n++;
					i++;
					j = i + 1;
					step = 1;
					forward = true;
					return checkFinish(arr);
				}
				
				step = 1;
				return false;
				
			case 1:
				arr.get(i).setSelected(true);
				step = 2;
				accesses++;
				return false;
				
			case 2:
				arr.get(i).setSelected(false);
				arr.get(j).setSelected(true);
				step = 3;
				accesses++;
				return false;
				
			case 3:
				accesses++;
				comparisons++;
				arr.get(i).setSelected(true);
				if(arr.get(i).getValue() < arr.get(j).getValue()) {
					int tempX = arr.get(i).getX();
					arr.get(i).setLocation(arr.get(j).getX(), arr.get(i).getY());
					arr.get(j).setLocation(tempX, arr.get(j).getY());
					
					ArrayInstance temp = arr.get(i);
					arr.set(i, arr.get(j));
					arr.set(j, temp);
				}
				step = 0;
				return false;
			}
		}
		return true;
	}

	@Override
	public void reset(ArrayList<ArrayInstance> arr) {
		forward = true;;
		i = 0; 
		j = 1; 
		k = 0;
		n = 0;
		step = 1;
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
	
	public boolean checkFinish(ArrayList<ArrayInstance> arr) {
		for(int i = 0; i < arr.size() - 1; i++) {
			if(arr.get(i).getValue() > arr.get(i + 1).getValue()) {
				return false;
			}
		}
		return true;
	}

}
