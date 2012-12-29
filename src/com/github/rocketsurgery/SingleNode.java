package com.github.rocketsurgery;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

@SuppressWarnings("serial")
public class SingleNode extends Node {

	private Wire attachedWire;

	public static final float sizeOnScreen = 30f;
	private static final Color nodeColor = Color.blue;

	public SingleNode(float x, float y) {
		super(x, y, sizeOnScreen);
	}

	@Override
	public boolean attach(Wire wire) {
		attachedWire = wire;
		return true;
	}

	@Override
	public void swapLocations(Node other) {
		// use Node implementation to swap x,y coords
		super.swapLocations(other);

		this.attachedWire.resetEnds();
		for (Wire wire : other.getWires())
			wire.resetEnds();
	}

	@Override
	public void Render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(nodeColor);
		g.setAntiAlias(true);
		g.fillOval(x - sizeOnScreen / 2, y - sizeOnScreen / 2, sizeOnScreen, sizeOnScreen);
	}

	@Override
	public boolean isOver(int x, int y) {
		return Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2)) < sizeOnScreen;
	}

	@Override
	public List<Wire> getWires() {
		ArrayList<Wire> temp = new ArrayList<>();
		temp.add(attachedWire);
		return temp;
	}

}
