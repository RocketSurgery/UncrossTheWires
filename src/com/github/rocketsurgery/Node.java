package com.github.rocketsurgery;

import java.util.List;

import org.newdawn.slick.geom.Circle;

@SuppressWarnings("serial")
public abstract class Node extends Circle implements DisplayElement {

	public Node(float centerPointX, float centerPointY, float radius) {
		super(centerPointX, centerPointY, radius);
	}
	
	public abstract boolean attach(Wire wire);
	
	public void swapLocations(Node other) {
		float tempX = this.x;
		float tempY = this.y;
		this.x = other.getX();
		this.y = other.getY();
		other.setX(tempX);
		other.setY(tempY);
	}
	
	public abstract boolean isOver(int x, int y);
	
	public abstract List<Wire> getWires();

}
