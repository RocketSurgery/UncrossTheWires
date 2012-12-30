package com.github.rocketsurgery;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.StateBasedGame;

@SuppressWarnings("serial")
public class Node extends Circle implements DisplayElement {
	
	public static final float sizeOnScreen = 15f;
	public static final Color nodeColor = Color.white;
	
	private ArrayList<Wire> wires;
	
	public Node(float centerPointX, float centerPointY) {
		super(centerPointX, centerPointY, sizeOnScreen);
		wires = new ArrayList<>();
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(nodeColor);
		g.setAntiAlias(false);
		g.fill(this);
	}
	
	public boolean attach(Wire wire) {
		if (wires.contains(wire))
			return false;
		wires.add(wire);
		return true;
	}
	
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
	
	public boolean isOver(int x, int y) {
		return Math.sqrt(Math.pow(getCenterX() - x, 2) + Math.pow(getCenterY() - y, 2)) < sizeOnScreen;
	}
	
	public List<Wire> getWires() {
		return wires;
	}

}
