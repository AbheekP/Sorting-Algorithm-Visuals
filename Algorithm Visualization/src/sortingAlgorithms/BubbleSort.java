package sortingAlgorithms;

import java.util.ArrayList;
import java.util.Arrays;

import UI.ArrayInstance;

public class BubbleSort implements SortingAlgorithm{

	int i = 0;
	int j = 1;
	int k = 0;
	int step = 1;
	int correctCount = 0;
	int accesses = 0;
	int comparisons = 0;
	
	@Override
	public void sort(ArrayList<ArrayInstance> arr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean nextStep(ArrayList<ArrayInstance> arr) {
		switch(step) {
		
		case 0:
			arr.get(i).setSelected(false);
			arr.get(j).setSelected(false);
			i = i + 1;
			
			j++;
			
			if(j > arr.size() - k - 1) {
				k++;
				i = 0;
				j = i + 1;
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
				
				correctCount = 0;
			} else {
				correctCount++;
			}
			
			if(correctCount == arr.size()) {
				return true;
			}
			step = 0;
			return false;
		}
		
		return true;
	}

	@Override
	public void reset(ArrayList<ArrayInstance> arr) {
		i = 0;
		j = 1;
		k = 0;
		step = 1;
		correctCount = 0;
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
