package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class VisualPanel extends JPanel {

	int visWidth = 810;
	int visHeight = UI.frameHeight - 39;
	
	int visX = 0;
	int visY = 0;
	
	public VisualPanel() {
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.black);
		g.fillRect(visX, visY, visWidth, visHeight);
	}
	
	public Dimension getVisualDimension() {
		return new Dimension(visWidth, visHeight);
	}
	
}
