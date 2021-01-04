package UI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sortingAlgorithms.*;

public class UI {
	
	public static int frameWidth = 1100;
	public static int frameHeight = 500;
	
	public static JFrame frame;
	public static VisualPanel panel;
	
	public int maxDelay = 500;
	public int minDelay = 1;
	
	public int maxArrSize = 250;
	public int minArrSize = 10;
	int arrSize = 46;
	
	int currentDelay = 1;
	
	boolean started = false;
	boolean paused = false;
	
	SortingAlgorithm sa;
	
	Thread startThread;
	
	boolean shuffled = false;
	
	ArrayList<ArrayInstance> ais = new ArrayList<ArrayInstance>();
	
	public UI() {
		frame = new JFrame();
		frame.setSize(new Dimension(frameWidth, frameHeight));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		panel = new VisualPanel(frame);
		panel.setLayout(null);
		panel.setVisible(true);
		
		createArrayInstances();
		
		/* --------------------- Animation Control Inputs --------------------- */
		
		/* Start/Stop Button -- starts the sorting. If already running, then it stops wherever it is. */
		JButton start = new JButton("Run");
		start.setSize((frameWidth - (int) (panel.getVisualDimension().getWidth() + 45)) / 2, 30);
		start.setLocation((int) (panel.getVisualDimension().getWidth() + 15), 30);
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(sa == null) {
					JOptionPane.showMessageDialog(null, "Select an algorithm");
				} else if (!shuffled) {
					JOptionPane.showMessageDialog(null, "Shuffle the Array (Press Reset)");
				} else {
					if(!started) {
						started = true;
						if(paused) {
							startThread.resume();
						} else {
							startThread.start();
						}
					} else {
						started = false;
						paused = true;
						startThread.suspend();
					}
				}
			}
		});
		
		/* Step Button -- goes one step forward (one array comparison) */
		JButton step = new JButton("Step");
		step.setSize(start.getSize());
		step.setLocation(start.getX() + step.getWidth() + 5, start.getY());
		step.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sa.nextStep(ais);
			}
		});
		
		/* Reset Button -- returns the array to sorted state and resets all stats */
		JButton reset = new JButton("Reset");
		reset.setSize(start.getSize());
		reset.setLocation(start.getX(), start.getY() + reset.getHeight() + 5);
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(sa == null) {
					JOptionPane.showMessageDialog(null, "Select an algorithm");
				} else {
					if(startThread != null) {
						startThread.stop();
					}
					
					for(ArrayInstance ai : ais) {
						ai.setVisible(false);
					}
					ais.clear();
					createArrayInstances();
					startThread = new Thread() {
						public void run() {	
							boolean running = true;
							
							while(running) {
								running = !sa.nextStep(ais);
								try {
									Thread.sleep(currentDelay);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							
							shuffled = false;
							for(int i = 0; i < ais.size(); i++) {
								ais.get(i).setSelected(true);
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							for(int i = 0; i < ais.size(); i++) {
								ais.get(i).setSelected(false);
							}
							this.interrupt();
						}
					};
					paused = false;
					started = false;
					shuffleArray();
					sa.reset(ais);
				}
			}
		});
		
		/* Complete Button -- sorts the array but also lists stats */
		JButton complete = new JButton("Complete");
		complete.setSize(reset.getSize());
		complete.setLocation(step.getX(), reset.getY());
		complete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(startThread != null) {
					startThread.stop();
					startThread = new Thread() {
						public void run() {	
							boolean running = true;
							
							while(running) {
								running = !sa.nextStep(ais);
								try {
									Thread.sleep(0);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							
							shuffled = false;
							for(int i = 0; i < ais.size(); i++) {
								ais.get(i).setSelected(true);
								try {
									Thread.sleep(5);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							for(int i = 0; i < ais.size(); i++) {
								ais.get(i).setSelected(false);
							}
							this.interrupt();
						}
					};
					startThread.start();
				}
				
			}
		});
		
		/* Section Title "Controls" */
		JLabel controlLabel = new JLabel();
		controlLabel.setFont(new Font(null, Font.BOLD, 16));
		controlLabel.setText("Controls");
		controlLabel.setSize(getCorrectSize(controlLabel));
		controlLabel.setLocation(start.getX(), 8);
		
		/* Delay Slider -- changes how fast the sorting animation occurs */
		JSlider delaySlider = new JSlider(minDelay, maxDelay, currentDelay);
		delaySlider.setLocation(reset.getX(), reset.getY() + 60);
		delaySlider.setSize(start.getWidth() * 2, start.getHeight() + 5);
		JLabel delaySliderTitle = new JLabel("Delay");
		delaySliderTitle.setLocation(delaySlider.getX(), delaySlider.getY() - 15);
		delaySliderTitle.setSize(getCorrectSize(delaySliderTitle));
		Hashtable<Integer, JLabel> delayLabels = new Hashtable<Integer, JLabel>();
		delayLabels.put(minDelay, new JLabel(minDelay + " ms"));
		delayLabels.put(maxDelay, new JLabel(Integer.toString(maxDelay) + " ms"));
		delaySlider.setLabelTable(delayLabels);
		delaySlider.setPaintLabels(true);
		JLabel delayVal = new JLabel("Delay: " + currentDelay + " ms"); // value for delay -- initialized above delaySlider to avoid null in sliderListener
		delaySlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				currentDelay = (int)source.getValue();
				delayVal.setText("Delay: " + currentDelay + " ms");
				delayVal.setLocation(delaySlider.getX(), delaySlider.getY() + delaySlider.getHeight() + 10);
				delayVal.setSize(getCorrectSize(delayVal));
			}
		});
		
		/* Labels for sorting statistics -- might need to be changed for sorting */
		
		delayVal.setLocation(delaySlider.getX(), delaySlider.getY() + delaySlider.getHeight() + 10);
		delayVal.setSize(getCorrectSize(delayVal));
		
		JLabel arrayComp = new JLabel("Comparisons: 0");
		arrayComp.setLocation(delayVal.getX(), delayVal.getY() + 20);
		arrayComp.setSize(getCorrectSize(arrayComp));
		
		JLabel arrayAcc = new JLabel("Array Accesses: 0");
		arrayAcc.setLocation(arrayComp.getX(), arrayComp.getY() + 20);
		arrayAcc.setSize(getCorrectSize(arrayAcc));
		
		Thread updateValues = new Thread() {
			public void run() {
				while(true) {
					if(sa != null) {
						arrayComp.setText("Comparisons: " + sa.getComparisons());
						arrayComp.setSize(getCorrectSize(arrayComp));
						
						arrayAcc.setText("Array Accesses: " + sa.getAccesses());
						arrayAcc.setSize(getCorrectSize(arrayAcc));
					}
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		
		updateValues.start();
	
		panel.add(start);
		panel.add(step);
		panel.add(reset);
		panel.add(complete);
		panel.add(controlLabel);
		panel.add(delaySlider);
		panel.add(delaySliderTitle);
		panel.add(delayVal);
		panel.add(arrayComp);
		panel.add(arrayAcc);
		
		/* --------------------- Algorithm-Related Inputs --------------------- */
		
		/* Section Title "Inputs" */
		JLabel algorithmLabel = new JLabel();
		algorithmLabel.setFont(new Font(null, Font.BOLD, 16));
		algorithmLabel.setText("Algorithm Inputs");
		algorithmLabel.setLocation(arrayAcc.getX(), arrayAcc.getY() + 40);
		algorithmLabel.setSize(getCorrectSize(algorithmLabel));
		
		/* Array Size Slider */
		JSlider sizeSlider = new JSlider(minArrSize, maxArrSize, 47);
		sizeSlider.setLocation(algorithmLabel.getX(), algorithmLabel.getY() + 50);
		sizeSlider.setSize(delaySlider.getWidth() - 50, delaySlider.getHeight());
		JLabel sizeSliderTitle = new JLabel("Size");
		sizeSliderTitle.setLocation(sizeSlider.getX(), sizeSlider.getY() - 15);
		sizeSliderTitle.setSize(getCorrectSize(sizeSliderTitle));
		JLabel sizeSliderValue = new JLabel("47");
		sizeSliderValue.setSize(getCorrectSize(sizeSliderValue));
		sizeSliderValue.setLocation(sizeSlider.getX() + sizeSlider.getWidth() + 15,  (sizeSlider.getY() + sizeSlider.getHeight()) - (sizeSliderValue.getHeight() * 2) + 4);
		sizeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				
				JOptionPane.showMessageDialog(null, "This currently has no function");
				
				/*
				
				JSlider source = (JSlider) e.getSource();
				sizeSliderValue.setText(Integer.toString(source.getValue()));
				sizeSliderValue.setSize(getCorrectSize(sizeSliderValue));
				if(!source.getValueIsAdjusting()) {
					
				} 
				
				*/
			}
		});
		
		/* List of Algorithms */
		
		List sortingAlgo = new List();
		sortingAlgo.setLocation(sizeSlider.getX(), sizeSlider.getY() + 40);
		sortingAlgo.setSize((step.getX() + step.getWidth()) - sortingAlgo.getX(), (frameHeight - 39) - sortingAlgo.getY() - 10);
		sortingAlgo.add("Selection Sort");
		sortingAlgo.add("Insertion Sort");
		sortingAlgo.add("Bubble Sort");
		sortingAlgo.add("Cocktail Shaker Sort");
		
		sortingAlgo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				List algoList = (List) e.getSource();
				changeSelectedAlgo(algoList.getSelectedItem());
			}
		});
		//algorithms to be added into the list
		
		panel.add(algorithmLabel);
		panel.add(sizeSlider);
		panel.add(sizeSliderTitle);
		panel.add(sizeSliderValue);
		panel.add(sortingAlgo);
		
		frame.add(panel);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);	
		
		/* test for cycling red */
		//cycleRed(ais);
		
	}
	
	/* Adds array instances (white bars) into the frame */
	public void createArrayInstances() {
		for(int i = 1; i < arrSize + 1; i++) {
			ArrayInstance ai = new ArrayInstance(i + 1);
			ai.setLocation((ai.getWidth() * i) + 20, (int) (panel.getVisualDimension().getHeight() - ai.getHeight()) - 30);
			
			panel.add(ai);
			ais.add(ai);
		}
	}
	
	/* Places array instances in random positions */
	public void shuffleArray() {
		shuffled = true;
		Random generator = new Random();
		ArrayInstance[] shuffled = new ArrayInstance[ais.size()];
		
		for(int i = 0; i < ais.size(); i++) {
			int index = generator.nextInt(shuffled.length);
			while(shuffled[index] != null) {
				index = generator.nextInt(shuffled.length);
			}
			shuffled[index] = ais.get(i);
		}
		
		for(int i = 0; i < shuffled.length; i++) {
			shuffled[i].setLocation((shuffled[i].getWidth() * (i + 1)) + 20, (int) (panel.getVisualDimension().getHeight() - shuffled[i].getHeight()) - 30);
		}
		
		ais.clear();
		for(int i = 0; i < shuffled.length; i++) {
			ais.add(shuffled[i]);
		}
	}
	
	/* Test for completion animation -- not in use rn */
	public void cycleRed(ArrayList<ArrayInstance> ais) {
		for(int i = 0; i < ais.size(); i++) {
			if(i != 0)
				ais.get(i - 1).setSelected(false);
			ais.get(i).setSelected(true);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void changeSelectedAlgo(String algo) {
		switch(algo) {
		case "Selection Sort":
			sa = new SelectionSort();
			break;
			
		case "Insertion Sort":
			sa = new InsertionSort();
			break;
			
		case "Bubble Sort":
			sa = new BubbleSort();
			break;
			
		case "Cocktail Shaker Sort":
			sa = new CocktailShakerSort();
			break;
		}
	}
	
	public Dimension getCorrectSize(JLabel label) {
		
		/* Returns the minimum size a JLabel needs in order for all of its text to be readable */
		
		AffineTransform at = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(at, true, true);
		
		int width = (int) (label.getFont().getStringBounds(label.getText(), frc).getWidth()) + 5;
		int height = (int) (label.getFont().getStringBounds(label.getText(), frc).getHeight());
		
		return new Dimension(width, height);
	}
	
}
