package com.github.rocketsurgery;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Gameplay extends BasicGameState {

	private ArrayList<Node> nodes;
	private ArrayList<Wire> wires;
	
	boolean hasClicked = false;

	// variables for selected nodes
	private Node selected;
	private Node lastSelected;
	private float selectionCircle = .5f * Node.sizeOnScreen;
	private float lastSelectionCircle;
	private float maxSelectionSize = 1.5f * Node.sizeOnScreen;
	private float growSpeed = 0.5f;
	private Color selectionColor = Color.yellow;
	
	// variables for hovered node
	private Node hovered;
	private Node lastHovered;
	private float hoverCircle = 0f;
	private float lastHoveredCircle = 0f;
	private float maxHoverSize = .5f * Node.sizeOnScreen;

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		startLevel(1);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// draw background

		// draw selection
		g.setColor(selectionColor);
		g.setAntiAlias(false);
		if (selected != null)
			g.fill(new Circle(selected.getX(), selected.getY(), selectionCircle));
		if (lastSelected != null)
			g.fill(new Circle(lastSelected.getX(), lastSelected.getY(), lastSelectionCircle));
		
		// draw nodes
		for (Node node : nodes)
			node.Render(gc, sbg, g);

		// draw hovered node
		g.setColor(selectionColor);
		g.setAntiAlias(false);
		if (hovered != null)
			g.fill(new Circle(hovered.getX(), hovered.getY(), hoverCircle));
		if (lastHovered != null)
			g.fill(new Circle(lastHovered.getX(), lastHovered.getY(), hoverCircle));

		// draw wires
		for (Wire wire : wires)
			wire.render(gc, sbg, g);

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		// create input
		Input input = gc.getInput();
		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();

		// find if mouse if hovering over a node
		Node previousHovered = hovered;
		hovered = null;
		for (Node node : nodes) {
			if (node.isOver(mouseX, mouseY)) {
				hovered = node;
				break;
			}
		}

		// if currently hovered node is different than previously hovered node
		// setup lastHovered and hoverCircle
		if (previousHovered != hovered) {
			lastHovered = previousHovered;
			lastHoveredCircle = hoverCircle;
			hoverCircle = 0f;
		}
		
		// animate lastCircle
		lastHoveredCircle = (lastHoveredCircle > 0) ? lastHoveredCircle - growSpeed * delta : 0;
		if (lastHoveredCircle == 0)
			lastHovered = null;

		// logic for clicking on nodes
		if (hovered != null) {
			
			// if not hovering over selected
			// animate hoverCircle
			if (hovered != selected)
				hoverCircle = (hoverCircle < maxHoverSize) ? hoverCircle + growSpeed * delta : maxHoverSize;

			// if user clicks on hovered
			// set selected to hovered
			if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !hasClicked) {
				lastSelected = selected;
				selected = hovered;
				selectionCircle = .5f * Node.sizeOnScreen;
				hovered = null;
				hasClicked = true;
			}
		} else {
			if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				lastSelected = selected;
				selected = null;
			}
		}

		// animate selectionCircle
		if (selected != null) {
			selectionCircle = (selectionCircle < maxSelectionSize) ? selectionCircle + growSpeed * delta : maxSelectionSize;
		} else {
			selectionCircle = (selectionCircle > 0) ? selectionCircle - growSpeed * delta : 0;
		}
		
		// animate lastSelectionCircle
		lastSelectionCircle = (lastSelectionCircle > 0) ? lastSelectionCircle - growSpeed * delta : 0;
		
		// test if any wires intersect
		boolean intersections = false;
		for (int i = 0; i < wires.size() - 1; i++) {
			for (int j = i + 1; j < wires.size(); j++) {
				if (wires.get(i).intersects(wires.get(j))) {
					intersections = true;
					break;
				}
			}
		}

		// if no wires intersect end level
		if (!intersections)
			System.out.println("game complete");
		
		// reset hasClicked
		if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			hasClicked = false;
		}
	}

	// resets level and loads setup for selected level
	private void startLevel(int level) {
		nodes = new ArrayList<Node>();
		wires = new ArrayList<Wire>();

		switch (level) {
		case 1:
			Node first = new Node(400f, 40f);
			Node second = new Node(500f, 520f);
			Wire wire = new Wire(first, second);
			first.attach(wire);
			second.attach(wire);
			nodes.add(first);
			nodes.add(second);
			wires.add(wire);

			first = new Node(50f, 100f);
			second = new Node(600f, 80f);
			wire = new Wire(first, second);
			first.attach(wire);
			second.attach(wire);
			nodes.add(first);
			nodes.add(second);
			wires.add(wire);

			first = new Node(50f, 500f);
			second = new Node(600f, 430f);
			wire = new Wire(first, second);
			first.attach(wire);
			second.attach(wire);
			nodes.add(first);
			nodes.add(second);
			wires.add(wire);

			break;
		default:
			System.out.println("how did you do that?");
			break;
		}
	}

	@Override
	public int getID() {
		return UncrossTheWires.GAMEPLAY;
	}

}
