package com.github.rocketsurgery;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Shape;

@SuppressWarnings("serial")
public class Wire extends Line implements DisplayElement {

	// wire
	private Node end1, end2;
	private static int colorIterator = 0;

	// graphical constants
	private static final float wireWidth = 5f;

	// graphics
	private Color wireColor = Color.red;

	public Wire(Node first, Node second) {
		super(first.getCenterX(), first.getCenterY(), second.getCenterX(), second.getCenterY());
		first.attach(this);
		second.attach(this);
		this.end1 = first;
		this.end2 = second;

		// Random rand = new Random();
		// int colorIndex = rand.nextInt(3);

		// determine color
		switch (colorIterator) {
		case 0:
			wireColor = Color.red;
			break;
		case 1:
			wireColor = Color.blue;
			break;
		case 2:
			wireColor = Color.green;
			break;
		default:
			throw new IllegalArgumentException();
		}
		colorIterator = ++colorIterator % 3;

		// wireColor = new Color
		// ((float)Math.random(),(float)Math.random(),(float)Math.random());
	}

	// resets endpoints of Line superclass to match attached nodes
	public void resetEnds() {
		this.set(end1.getCenterX(), end1.getCenterY(), end2.getCenterX(), end2.getCenterY());
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(wireColor);
		g.setLineWidth(wireWidth);
		g.setAntiAlias(true);

		// draw wire
		g.drawLine(end1.getCenterX(), end1.getCenterY(), end2.getCenterX(), end2.getCenterY());

		// round ends
		g.fillOval(end1.getCenterX() - wireWidth * 3 / 2, end1.getCenterY() - wireWidth * 3 / 2, wireWidth * 3, wireWidth * 3);
		g.fillOval(end2.getCenterX() - wireWidth * 3 / 2, end2.getCenterY() - wireWidth * 3 / 2, wireWidth * 3, wireWidth * 3);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

	}
	
	// utility methods
	
	// overrides Line.intersects() to return false if they have a shared endpoint
	// returns true if lines intesect in some way other than sharing an endpoint
	@Override
	public boolean intersects(Shape shape) {
		if (!(shape instanceof Wire))
			throw new IllegalArgumentException();
		Wire other = (Wire) shape;
		if (super.intersects(other)) {
			return !(other.hasEnd(end1) || other.hasEnd(end2));
		}
		return false;
	}

	// returns true if has node
	public boolean hasEnd(Node node) {
		return end1 == node || end2 == node;
	}

	// returns true if has both n1 and n2
	public boolean hasEnds(Node n1, Node n2) {
		return (end1 == n1 && end2 == n2) || (end1 == n2 && end2 == n1);
	}

}
