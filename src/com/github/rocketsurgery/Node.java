package com.github.rocketsurgery;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.StateBasedGame;

@SuppressWarnings("serial")
public class Node extends Circle {

	private Wire attachedWire;
	
	public static final float sizeOnScreen = 30f;
	private static final Color nodeColor = Color.blue;
	
	public Node(float x, float y) {
		super(x, y, sizeOnScreen);
	}
	
	public boolean attach(Wire wire) {
		attachedWire = wire;
		return true;
	}
	
	public void Render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(nodeColor);
		g.setAntiAlias(true);
		g.fillOval(x - sizeOnScreen / 2, y - sizeOnScreen / 2, sizeOnScreen, sizeOnScreen);
	}
	
	public boolean isOver(int x, int y) {
		return Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2)) < sizeOnScreen;
	}
	
	public Wire getWire() {
		return attachedWire;
	}
}
