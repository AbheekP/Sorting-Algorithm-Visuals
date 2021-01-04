package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class VisualPanel extends JPanel {

	int visWidth;
	int visHeight;
	
	int visX = 0;
	int visY = 0;
	
	public VisualPanel(JFrame parentFrame) {
		parentFrame.setVisible(true);
		visHeight = parentFrame.getContentPane().getHeight();
		visWidth = parentFrame.getContentPane().getWidth() - 290;
		repaint();
		parentFrame.setVisible(false);
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
