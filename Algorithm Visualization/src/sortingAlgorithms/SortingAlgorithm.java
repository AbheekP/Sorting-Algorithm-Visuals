package sortingAlgorithms;

import java.util.ArrayList;

import UI.ArrayInstance;

public interface SortingAlgorithm {
	
	/* Completely sorts the given array. Also stores any stats (array accesses and comparisons) */
	public void sort(ArrayList<ArrayInstance> arr);
	
	/* next step/comparison in the sorting algorithm. Used for the visualization */
	public boolean nextStep(ArrayList<ArrayInstance> arr);
	
	/* resets any static values so the tool can be used again */
	public void reset(ArrayList<ArrayInstance> arr);
	
	public int getAccesses();
	
	public int getComparisons();
}
