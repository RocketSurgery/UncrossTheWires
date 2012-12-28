package com.github.rocketsurgery;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Wire {

	private Node end1, end2;
	
	private Color wireColor = Color.red;
	private float wireWidth = 5f;
	
	public Wire(Node first, Node second) {
		this.end1 = first;
		this.end2 = second;
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(wireColor);
		g.setLineWidth(wireWidth);
		g.setAntiAlias(true);
		g.drawLine(end1.x(), end1.y(), end2.x(), end2.y());
		g.fillOval(end1.x() - wireWidth / 2, end1.y() - wireWidth / 2, wireWidth, wireWidth);
		g.fillOval(end2.x() - wireWidth / 2, end2.y() - wireWidth / 2, wireWidth, wireWidth);
	}
	
	public boolean overlaps (Wire wire) {
		
		
		
		
		
		
		
		
		return true;
	}
	
}
