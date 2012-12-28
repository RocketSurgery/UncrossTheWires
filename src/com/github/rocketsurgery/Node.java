package com.github.rocketsurgery;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Node {

	private float xPos, yPos;
	private Wire attachedWire;
	
	private static final float sizeOnScreen = 50f;
	private static final Color nodeColor = Color.blue;
	
	public Node(float x, float y) {
		this.xPos = x;
		this.yPos = y;
	}
	
	public boolean attach(Wire wire) {
		if (attachedWire != null)
			return false;
		attachedWire = wire;
		return true;
	}
	
	public void Render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(nodeColor);
		g.setAntiAlias(true);
		g.fillOval(xPos - sizeOnScreen / 2, yPos - sizeOnScreen / 2, sizeOnScreen, sizeOnScreen);
	}
	
	public boolean isOver(int x, int y) {
		return Math.sqrt(Math.pow(xPos - x, 2) + Math.pow(yPos - y, 2)) < sizeOnScreen;
	}
	
	public float getX() {
		return xPos;
	}
	
	public float getY() {
		return yPos;
	}
	
}
