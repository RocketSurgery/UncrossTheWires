package com.github.rocketsurgery;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.geom.Line;

@SuppressWarnings("serial")
public class Wire extends Line {

	private Node end1, end2;
	
	private Color wireColor = Color.red;
	private float wireWidth = 5f;
	
	public Wire(Node first, Node second) {
		super(first.getX(), first.getY(), second.getX(), second.getY());
		this.end1 = first;
		this.end2 = second;
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(wireColor);
		g.setLineWidth(wireWidth);
		g.setAntiAlias(true);
		g.drawLine(end1.getX(), end1.getY(), end2.getX(), end2.getY());
		g.fillOval(end1.getX() - wireWidth / 2, end1.getY() - wireWidth / 2, wireWidth, wireWidth);
		g.fillOval(end2.getX() - wireWidth / 2, end2.getY() - wireWidth / 2, wireWidth, wireWidth);
	}
	
}
