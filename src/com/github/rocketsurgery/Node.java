package com.github.rocketsurgery;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.StateBasedGame;

@SuppressWarnings("serial")
public class Node extends Circle implements DisplayElement {

	// node
	private ArrayList<Wire> wires;
	
	// graphical constants
	public static final float SIZE = 15f;
	public static final Color NODE_COLOR = Color.white;
	
	// graphics
	private static Image img;
	
	private Node(float centerPointX, float centerPointY) throws SlickException {
		super(centerPointX, centerPointY, SIZE);
		
		// initialize variables
		wires = new ArrayList<>();
		if (img == null)
			img = new Image("res/nodeSprite.gif");
	}
	
	private Node(float centerPointX, float centerPointY, ArrayList<Wire> w) throws SlickException {
		super(centerPointX, centerPointY, SIZE);
		
		// initialize variables
		wires = w;
		if (img == null)
			img = new Image("res/nodeSprite.gif");
	}

	// static method used to generate new nodes
	// constructor is kept private because the centerpoints must be determined based on GameContainer
	public static Node createNode(float x, float y, float xSize, float ySize, GameContainer gc) throws SlickException {
		float centerX = x / xSize * (gc.getWidth() - 2 * Gameplay.PADDING) + Gameplay.PADDING;
		float centerY = y / ySize * (gc.getHeight() - 2 * Gameplay.PADDING) + Gameplay.PADDING;
		return new Node(centerX, centerY);
	}
	
	// returns true if this node and n have the same center point
	// used in level generation to prevent overlapping nodes
	public boolean matches(Node n) {
		return this.getCenterX() == n.getCenterX() && this.getCenterY() == n.getCenterY();
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(NODE_COLOR);
		g.setAntiAlias(false);
		
		if (img != null) {
			g.drawImage(img, getCenterX() - SIZE, getCenterY() - SIZE);
		} else {
			g.fill(this);
		}
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

	}
	
	// adds wire to array of connected wires
	// returns false if wire is already in wires
	public boolean attach(Wire wire) {
		if (wires.contains(wire))
			return false;
		wires.add(wire);
		return true;
	}
	
	// swaps coordinates with other and updates attached wires
	public void swapLocations(Node other) {
		// swap locations
		float tempX = this.getCenterX();
		float tempY = this.getCenterY();
		this.setCenterX(other.getCenterX());
		this.setCenterY(other.getCenterY());
		other.setCenterX(tempX);
		other.setCenterY(tempY);
		
		// update wires
		for (Wire wire : wires)
			wire.resetEnds();
		for (Wire wire : other.getWires())
			wire.resetEnds();
	}
	
	public List<Wire> getWires() {
		return wires;
	}
	
	// corrected contains method
	// necessary because Circle.contains() is off-center
	@Override
	public boolean contains(float x, float y) {
		return Math.sqrt(Math.pow(this.getCenterX() - x, 2) + Math.pow(this.getCenterY() - y, 2)) <= SIZE;
	}

}
