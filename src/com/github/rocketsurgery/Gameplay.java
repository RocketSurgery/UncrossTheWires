package com.github.rocketsurgery;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Gameplay extends BasicGameState {

	private ArrayList<Node> nodes;
	private ArrayList<Wire> wires;

	// variable for selected nodes
	private Node selected;
	private float selectionCircle;
	private float maxSelectionSize = 2 * Node.sizeOnScreen;
	private Node hovered;
	private Node lastHovered;
	private float hoverCircle;
	private float maxHoverSize = .5f * Node.sizeOnScreen;
	private float growSpeed = 1f;

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		startLevel(1);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// Draw Background

		// Draw Nodes
		for (Node node : nodes)
			node.Render(gc, sbg, g);

		// Draw Wires
		for (Wire wire : wires)
			wire.render(gc, sbg, g);

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		// Create Input
		Input input = gc.getInput();
		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();

		lastHovered = hovered;
		hovered = null;
		for (Node node : nodes) {
			if (node.isOver(mouseX, mouseY)) {
				hovered = node;
				break;
			}
		}
		
		if (hovered != null) {
			
		} else {
			
		}

		boolean intersections = false;

		for (int i = 0; i < wires.size() - 1; i++)
			for (int j = i + 1; j < wires.size(); j++)
				if (wires.get(i).intersects(wires.get(j)))
					intersections = true;

		if (!intersections)
			System.out.println("game complete");
	}

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
