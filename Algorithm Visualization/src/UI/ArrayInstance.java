package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

public class ArrayInstance extends JComponent {

	int width = 15;
	int height;
	
	int x, y;
	
	int value;
	boolean selected;
	boolean highlighted;
	
	public ArrayInstance(int value) {
		
		this.value = value;
		height = value * 8;
		//height = value;
		
		selected = false;
		highlighted = false;
		
		setSize(width + 1, height + 1);
		setVisible(true);
		
		repaint();
		
	}
	
	public void paintComponent(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(selected) {
			g.setColor(Color.red);
		} else if (highlighted) {
			g.setColor(Color.cyan);
		} else {
			g.setColor(Color.white);
		}
		g.fillRect(x, y, width, height);

		g.setColor(Color.black);
		//g.drawRect(x, y, width, height);
		
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
		repaint();
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public int getValue() {
		return value;
	}
	
	public String toString() {
		return Integer.toString(value);
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted (boolean highlighted) {
		this.highlighted = highlighted;
		repaint();
	}
	
}
