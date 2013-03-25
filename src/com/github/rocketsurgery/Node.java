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
	
	private final float reducedX, reducedY;
	
	private ArrayList<Wire> wires;
	
	public Node(float reducedCenterX, float reducedCenterY) {
		super(reducedCenterX, reducedCenterY, sizeOnScreen);
		this.reducedX = reducedCenterX;
		this.reducedY = reducedCenterY;
		this.wires = new ArrayList<>();
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
	
	public List<Wire> getWires() {
		return wires;
	}
	
	public void scaleInternalCoords(GameContainer gc) {
		this.setCenterX(this.reducedX / Level.getXSize() * (gc.getWidth() - 2 * Gameplay.PADDING) + Gameplay.PADDING);
		this.setCenterY(this.reducedY / Level.getYSize() * (gc.getHeight() - 2 * Gameplay.PADDING) + Gameplay.PADDING);
		this.setRadius(sizeOnScreen);
	}
	
	@Override
	public boolean contains(float x, float y) {
		return Math.sqrt(Math.pow(this.getCenterX() - x, 2) + Math.pow(this.getCenterY() - y, 2)) <= sizeOnScreen;
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		// TODO Auto-generated method stub
		
	}

}
