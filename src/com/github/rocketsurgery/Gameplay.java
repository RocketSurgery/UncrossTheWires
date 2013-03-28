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

	// gameplay mechanics
	protected Node selected;
	protected Node lastSelected;
	protected Node hovered;
	protected Node lastHovered;

	protected boolean hasClicked = false;
	protected boolean hasSwapped = false;
	protected float delay;
	protected static final int WIN_DELAY = 500;

	// graphical constants
	protected final float MAX_HOVER_SIZE = 1f * Node.SIZE;
	protected final float MAX_SELEC_SIZE = 1.5f * Node.SIZE;
	protected final float MIN_SELEC_SIZE = .5f * Node.SIZE;
	protected final Color SELEC_COLOR = Color.orange;
	protected final float GROW_SPEED = 0.5f;

	public static final float PADDING = 50;
	public static final float BUFFER_TOP = 100;

	// graphics
	protected UnicodeFont font;
	protected Image backgroundImage;
	protected boolean displayScore = true;
	protected boolean displayTime = true;

	protected float selectionCircleSize = MIN_SELEC_SIZE;
	protected float hoverCircleSize = 0f;
	protected float lastHoveredCircleSize = 0f;
	protected float lastSelectionCircleSize = 0;

	@SuppressWarnings("unchecked")
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// load font
		font = new UnicodeFont("font.ttf", 40, false, false);
		font.addAsciiGlyphs();
		font.getEffects().add(new ColorEffect()); // Create a default white
		font.loadGlyphs();

		// load background image
		backgroundImage = new Image("res/bg.gif");
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// initialize variables
		resetLevel(gc);
		Score.reset();
		Timer.reset();
	}

	@Override
	public void leave(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		System.out.println("Leaving state " + getID());
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// draw background
		g.drawImage(backgroundImage, 0, 0, (float) gc.getWidth(), (float) gc.getHeight(), 0, 0, backgroundImage.getWidth(),
				backgroundImage.getHeight());

		// draw selection
		g.setColor(SELEC_COLOR);
		g.setAntiAlias(false);
		if (selected != null)
			g.fill(new Circle(selected.getCenterX(), selected.getCenterY(), selectionCircleSize));
		if (lastSelected != null)
			g.fill(new Circle(lastSelected.getCenterX(), lastSelected.getCenterY(), lastSelectionCircleSize));

		// draw nodes
		for (Node node : Level.getNodes())
			node.render(gc, sbg, g);

		// draw hovered node
		g.setColor(SELEC_COLOR);
		g.setAntiAlias(false);
		if (hovered != null)
			g.fill(new Circle(hovered.getCenterX(), hovered.getCenterY(), hoverCircleSize));
		if (lastHovered != null)
			g.fill(new Circle(lastHovered.getCenterX(), lastHovered.getCenterY(), hoverCircleSize));

		// draw wires
		for (Wire wire : Level.getWires())
			wire.render(gc, sbg, g);
		
		// draw time
		g.setColor(Color.red);
		g.setFont(font);
		g.drawString(Float.toString(Timer.getTime() / 1000f), 10, 15);
		
		// draw score
		g.drawString(Integer.toString(Score.getScore()), gc.getWidth() / 2, 15);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		// create input
		Input input = gc.getInput();
		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();

		// reset hasClicked
		if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
			hasClicked = false;

		// if level is complete exit
		if (Level.isSolved())
			return;

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

		// logic for clicking on nodes
		if (hovered != null) {

			// if not hovering over selected
			// animate hoverCircle
			if (hovered != selected)
				hoverCircleSize = (hoverCircleSize < MAX_HOVER_SIZE) ? hoverCircleSize + GROW_SPEED * delta : MAX_HOVER_SIZE;

			// if user clicks on hovered
			// set selected to hovered
			if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !hasClicked) {
				lastSelected = selected;
				selected = hovered;
				selectionCircleSize = MIN_SELEC_SIZE;
				hovered = null;
				hasClicked = true;
			}
		} else {
			if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				lastSelected = selected;
				selected = null;
			}
		}

		// two nodes selected, attempt to switc locations
		if (lastSelected != null && selected != null && lastSelected != selected) {
			// switch nodes
			selected.swapLocations(lastSelected);
			hasSwapped = true;

			// deselect nodes
			selected = null;
			lastSelected = null;
		}

		// perform all animations

		// animate lastHoveredCircle
		lastHoveredCircleSize = (lastHoveredCircleSize > 0) ? lastHoveredCircleSize - GROW_SPEED * delta : 0;
		if (lastHoveredCircleSize == 0)
			lastHovered = null;

		// animate selectionCircle
		if (selected != null) {
			selectionCircleSize = (selectionCircleSize < MAX_SELEC_SIZE) ? selectionCircleSize + GROW_SPEED * delta : MAX_SELEC_SIZE;
		} else {
			selectionCircleSize = (selectionCircleSize > 0) ? selectionCircleSize - GROW_SPEED * delta : 0;
		}

		// animate lastSelectionCircle
		lastSelectionCircleSize = (lastSelectionCircleSize > 0) ? lastSelectionCircleSize - GROW_SPEED * delta : 0;
	}

	protected void resetLevel(GameContainer gc) throws SlickException {
		Level.generateLevel(gc);
		delay = WIN_DELAY;
	}

}
