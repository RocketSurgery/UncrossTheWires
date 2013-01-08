package com.github.rocketsurgery;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Gameplay extends BasicGameState {

	public static GameContainer gameContainer = null;
	public static final int PADDING = 50;

	protected boolean hasClicked = false;
	protected boolean hasSwapped = false;
	protected boolean levelComplete = false;
	protected float winDelay;

	// variables for selected level
	protected Node selected;
	protected Node lastSelected;
	protected float selectionCircle = .5f * Node.sizeOnScreen;
	protected float lastSelectionCircle;
	protected float maxSelectionSize = 1.5f * Node.sizeOnScreen;
	protected float growSpeed = 0.5f;
	protected Color selectionColor = Color.orange;

	// variables for hovered node
	protected Node hovered;
	protected Node lastHovered;
	protected float hoverCircle = 0f;
	protected float lastHoveredCircle = 0f;
	protected float maxHoverSize = 1f * Node.sizeOnScreen;

	// font
	protected UnicodeFont font;

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// initialize variables
		levelComplete = false;

	}

	@Override
	public void leave(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		System.out.println("Leaving state " + getID());
	}

	@SuppressWarnings("unchecked") // because font.getEffects() is dumb
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		gameContainer = gc;

		// load font
		font = new UnicodeFont("cubic.ttf", 40, false, false);
		font.addAsciiGlyphs();
		font.getEffects().add(new ColorEffect()); // Create a default white
		font.loadGlyphs();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// draw background

		// draw selection
		g.setColor(selectionColor);
		g.setAntiAlias(false);
		if (selected != null)
			g.fill(new Circle(selected.scaleX(), selected.scaleY(), selectionCircle));
		if (lastSelected != null)
			g.fill(new Circle(lastSelected.scaleX(), lastSelected.scaleY(), lastSelectionCircle));

		// draw Level.getNodes()
		for (Node node : Level.nodes)
			node.render(gc, sbg, g);

		// draw hovered node
		g.setColor(selectionColor);
		g.setAntiAlias(false);
		if (hovered != null)
			g.fill(new Circle(hovered.scaleX(), hovered.scaleY(), hoverCircle));
		if (lastHovered != null)
			g.fill(new Circle(lastHovered.scaleX(), lastHovered.scaleY(), hoverCircle));

		// draw Level.getWires()
		for (Wire wire : Level.wires)
			wire.render(gc, sbg, g);

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		// create input
		Input input = gc.getInput();
		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();

		// if Level complete skip logic
		if (!levelComplete) {

			// find if mouse if hovering over a node
			Node previousHovered = hovered;
			hovered = null;
			for (Node node : Level.nodes) {
				if (node.contains(mouseX, mouseY)) {
					hovered = node;
					break;
				}
			}

			// if currently hovered node is different than previously hovered
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
			// logic for clicking on Level.getNodes()
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

			// two Level.getNodes() selected, attempt to switch Level.getWires()
			if (lastSelected != null && selected != null && lastSelected != selected) {
				// switch nodes
				selected.swapLocations(lastSelected);
				hasSwapped = true;

				// deselect nodes
				selected = null;
				lastSelected = null;
			}

			// animate selectionCircle
			if (selected != null) {
				selectionCircle = (selectionCircle < maxSelectionSize) ? selectionCircle + growSpeed * delta : maxSelectionSize;
			} else {
				selectionCircle = (selectionCircle > 0) ? selectionCircle - growSpeed * delta : 0;
			}

			// animate lastSelectionCircle
			lastSelectionCircle = (lastSelectionCircle > 0) ? lastSelectionCircle - growSpeed * delta : 0;

			// test if any Level.getWires() intersect
			levelComplete = true;
			for (int i = 0; i < Level.wires.size() - 1; i++) {
				for (int j = i + 1; j < Level.wires.size(); j++) {
					if (Level.wires.get(i).intersects(Level.wires.get(j))) {
						levelComplete = false;
						break;
					}
				}
			}
		}

		// reset hasClicked
		if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			hasClicked = false;
		}
	}

	protected void reset() {
		Level.generateLevel(Level.selectedDifficulty);
		winDelay = 500;
		levelComplete = false;
	}

}
