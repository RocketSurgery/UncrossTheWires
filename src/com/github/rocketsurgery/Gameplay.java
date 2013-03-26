package com.github.rocketsurgery;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Gameplay extends BasicGameState {

	public static final float PADDING = 50;

	protected boolean hasClicked = false;
	protected boolean hasSwapped = false;
	protected boolean levelComplete = false;
	protected float winDelay;

	// variables for selected level
	protected Node selected;
	protected Node lastSelected;
	protected float selectionCircleSize = .5f * Node.sizeOnScreen;
	protected float lastSelectionCircleSize;
	protected float maxSelectionSize = 1.5f * Node.sizeOnScreen;
	protected float growSpeed = 0.5f;
	protected Color selectionColor = Color.orange;

	// variables for hovered node
	protected Node hovered;
	protected Node lastHovered;
	protected float hoverCircleSize = 0f;
	protected float lastHoveredCircleSize = 0f;
	protected float maxHoverSize = 1f * Node.sizeOnScreen;

	// graphics
	protected UnicodeFont font;
	protected Image bg;

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// initialize variables
		levelComplete = false;
		bg = new Image("res/bg.gif");
	}

	@Override
	public void leave(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		System.out.println("Leaving state " + getID());
	}

	@SuppressWarnings("unchecked") // because font.getEffects() is dumb
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// load font
		font = new UnicodeFont("cubic.ttf", 40, false, false);
		font.addAsciiGlyphs();
		font.getEffects().add(new ColorEffect()); // Create a default white
		font.loadGlyphs();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// draw background
		g.drawImage(bg, 0, 0, (float) gc.getWidth(), (float) gc.getHeight(), 0, 0, 64, 64);

		// draw selection
		g.setColor(selectionColor);
		g.setAntiAlias(false);
		if (selected != null)
			g.fill(new Circle(selected.getCenterX(), selected.getCenterY(), selectionCircleSize));
		if (lastSelected != null)
			g.fill(new Circle(lastSelected.getCenterX(), lastSelected.getCenterY(), lastSelectionCircleSize));

		// draw Level.getNodes()
		for (Node node : Level.getNodes())
			node.render(gc, sbg, g);

		// draw hovered node
		g.setColor(selectionColor);
		g.setAntiAlias(false);
		if (hovered != null)
			g.fill(new Circle(hovered.getCenterX(), hovered.getCenterY(), hoverCircleSize));
		if (lastHovered != null)
			g.fill(new Circle(lastHovered.getCenterX(), lastHovered.getCenterY(), hoverCircleSize));

		// draw Level.getWires()
		for (Wire wire : Level.getWires())
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
			for (Node node : Level.getNodes()) {
				if (node.contains(mouseX, mouseY)) {
					hovered = node;
					break;
				}
			}

			// if currently hovered node is different than previously hovered
			// setup lastHovered and hoverCircle
			if (previousHovered != hovered) {
				lastHovered = previousHovered;
				lastHoveredCircleSize = hoverCircleSize;
				hoverCircleSize = 0f;
			}
			// animate lastCircle
			lastHoveredCircleSize = (lastHoveredCircleSize > 0) ? lastHoveredCircleSize - growSpeed * delta : 0;
			if (lastHoveredCircleSize == 0)
				lastHovered = null;
			// logic for clicking on Level.getNodes()
			if (hovered != null) {

				// if not hovering over selected
				// animate hoverCircle
				if (hovered != selected)
					hoverCircleSize = (hoverCircleSize < maxHoverSize) ? hoverCircleSize + growSpeed * delta : maxHoverSize;

				// if user clicks on hovered
				// set selected to hovered
				if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !hasClicked) {
					lastSelected = selected;
					selected = hovered;
					selectionCircleSize = .5f * Node.sizeOnScreen;
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
				selectionCircleSize = (selectionCircleSize < maxSelectionSize) ? selectionCircleSize + growSpeed * delta : maxSelectionSize;
			} else {
				selectionCircleSize = (selectionCircleSize > 0) ? selectionCircleSize - growSpeed * delta : 0;
			}

			// animate lastSelectionCircle
			lastSelectionCircleSize = (lastSelectionCircleSize > 0) ? lastSelectionCircleSize - growSpeed * delta : 0;

			// test if any Level.getWires() intersect
			levelComplete = true;
			for (int i = 0; i < Level.getWires().size() - 1; i++) {
				for (int j = i + 1; j < Level.getWires().size(); j++) {
					if (Level.getWires().get(i).intersects(Level.getWires().get(j))) {
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

	protected void reset(GameContainer gc) throws SlickException {
		Level.generateLevel(gc);
		winDelay = 500;
		levelComplete = false;
	}

}
