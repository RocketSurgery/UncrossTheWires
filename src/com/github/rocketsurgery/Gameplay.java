package com.github.rocketsurgery;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Gameplay extends BasicGameState {

	private List<Node> nodes;
	private List<Wire> wires;

	private boolean hasClicked = false;
	private boolean levelComplete = false;
	private float winDelay = 2000f;

	// variables for selected nodes
	private Node selected;
	private Node lastSelected;
	private float selectionCircle = .5f * Node.sizeOnScreen;
	private float lastSelectionCircle;
	private float maxSelectionSize = 1.5f * Node.sizeOnScreen;
	private float growSpeed = 0.5f;
	private Color selectionColor = Color.orange;

	// variables for hovered node
	private Node hovered;
	private Node lastHovered;
	private float hoverCircle = 0f;
	private float lastHoveredCircle = 0f;
	private float maxHoverSize = 1f * Node.sizeOnScreen;

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		System.out.println("Entering state " + getID());
		
		// initialize variables
		levelComplete = false;
		winDelay = 2000f;
		
		// generate level
		Level level = Level.generateLevel(MainMenu.selectedLevel, gc);
		nodes = level.getNodes();
		wires = level.getWires();
	}

	@Override
	public void leave(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		System.out.println("Leaving state " + getID());
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// draw background

		// draw selection
		g.setColor(selectionColor);
		g.setAntiAlias(false);
		if (selected != null)
			g.fill(new Circle(selected.getCenterX(), selected.getCenterY(), selectionCircle));
		if (lastSelected != null)
			g.fill(new Circle(lastSelected.getCenterX(), lastSelected.getCenterY(), lastSelectionCircle));

		// draw nodes
		for (Node node : nodes)
			node.Render(gc, sbg, g);

		// draw hovered node
		g.setColor(selectionColor);
		g.setAntiAlias(false);
		if (hovered != null)
			g.fill(new Circle(hovered.getCenterX(), hovered.getCenterY(), hoverCircle));
		if (lastHovered != null)
			g.fill(new Circle(lastHovered.getCenterX(), lastHovered.getCenterY(), hoverCircle));

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

		// if level complete skip logic
		if (!levelComplete) {

			// find if mouse if hovering over a node
			Node previousHovered = hovered;
			hovered = null;
			for (Node node : nodes) {
				if (node.isOver(mouseX, mouseY)) {
					hovered = node;
					break;
				}
			}
			
			// if currently hovered node is different than previously hovered
			// node
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
			
			// two nodes selected, attempt to switch wires
			if (lastSelected != null && selected != null && lastSelected != selected) {
				if (selected.getWires() != lastSelected.getWires()) { // nodes from different wires
					
					// switch nodes
					selected.swapLocations(lastSelected);

					// deselect nodes
					selected = null;
					lastSelected = null;
				} else { // nodes on same wire

					// deselect nodes
					selected = null;
					lastSelected = null;
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
			levelComplete = true;
			for (int i = 0; i < wires.size() - 1; i++) {
				for (int j = i + 1; j < wires.size(); j++) {
					if (wires.get(i).intersects(wires.get(j))) {
						levelComplete = false;
						break;
					}
				}
			}

			// reset hasClicked
			if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				hasClicked = false;
			}
			
		}

		// if no wires intersect end level
		if (levelComplete) {
			winDelay -= delta;
			if (winDelay <= 0)
				sbg.enterState(UncrossTheWires.MAIN_MENU);
		}

	}

	@Override
	public int getID() {
		return UncrossTheWires.GAMEPLAY;
	}

}
