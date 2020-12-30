package sortingAlgorithms;

import java.awt.Point;
import java.util.ArrayList;

import UI.ArrayInstance;

public class SelectionSort implements SortingAlgorithm{

	static int step = 0;
	static int i = -1;
	static int j = 0;
	static int min = -1;
	static int previousHighlighted = -1;
	static int accesses = 0;
	static int comparisons = 0;
	
	@Override
	public void sort(ArrayList<ArrayInstance> arr) {
		for(int i = 0; i < arr.size() - 1; i++) {
			int min = i;
			for(int j = i + 1; j < arr.size(); j++) {
				accesses += 2;
				comparisons++;
				if(arr.get(j).getValue() < arr.get(min).getValue()) {
					min = j;
				}
			}
			ArrayInstance temp = arr.get(min);
			arr.set(min, arr.get(i));
			arr.set(i, temp);
		}
	}
	
	@Override
	public boolean nextStep(ArrayList<ArrayInstance> arr) {
		if(i < arr.size() - 1) {
			switch(step) {
			case 0:
				i = i + 1;
				min = i;
				step = 1;
				j = i + 1;;
				arr.get(min).setHighlighted(true);
				accesses++;
				return false;
				
			case 1:
				if(j < arr.size()) {
					if(previousHighlighted != -1)
						arr.get(previousHighlighted).setSelected(false);
					if(min != -1)
						arr.get(min).setSelected(false); 
					arr.get(j).setSelected(true);
				} else {
					arr.get(i).setSelected(true);
					arr.get(min).setSelected(true);
				}
				step = 2;
				accesses++;
				return false;
				
			case 2:
				if(j < arr.size()) {
					arr.get(j).setSelected(false);
					arr.get(min).setSelected(true);
					if(arr.get(min).getValue() > arr.get(j).getValue()) {
						arr.get(min).setHighlighted(false);
						previousHighlighted = min;
						min = j;
						arr.get(min).setHighlighted(true);
					}
					step = 1;
					j++;
				} else {
					Point tempLoc = new Point(arr.get(min).getX(), arr.get(i).getY());
					arr.get(min).setLocation(arr.get(i).getX(), arr.get(min).getY());
					arr.get(i).setLocation(tempLoc);
					
					arr.get(i).setSelected(false);
					arr.get(min).setSelected(false);
					arr.get(min).setHighlighted(false);
					
					ArrayInstance temp = arr.get(min);
					arr.set(min, arr.get(i));
					arr.set(i, temp);
					step = 0;
				}
				accesses++;
				comparisons++;
				return false;
				
			}
		}
		arr.get(min).setHighlighted(false);
		return true;
	}
	
	public void reset(ArrayList<ArrayInstance> arr) {
		step = 0;
		i = -1;
		j = 0;
		min = -1;
		previousHighlighted = -1;
		accesses = 0;
		comparisons = 0;
		
		for(int i = 0; i < arr.size(); i++) {
			arr.get(i).setSelected(false);
			arr.get(i).setHighlighted(false);
		}
	}
	
	public int getAccesses() {
		return accesses;
	}
	
	public int getComparisons() {
		return comparisons;
	}
	
}
