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

public class Gameplay extends BasicGameState {

	public static GameContainer gameContainer = null;
	
	private Level level;

	private boolean hasClicked = false;
	private boolean levelComplete = false;
	private float winDelay = 500f;

	// variables for selected level.getNodes()
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
	
	//font 
	private UnicodeFont font;
	
	//timer stuff
	private String timeString = "";

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		System.out.println("Entering state " + getID());
		
		// initialize variables
		levelComplete = false;
		winDelay = 2000f;
		
		// load font
		font = new UnicodeFont("cubic.ttf", 40, false, false);
		font.addAsciiGlyphs();
		font.getEffects().add(new ColorEffect()); // Create a default white
		font.loadGlyphs();
		
		// generate level
		level = Level.generateLevel(MainMenu.selectedLevel, gc);
	}

	@Override
	public void leave(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		System.out.println("Leaving state " + getID());
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		gameContainer = gc;
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

		// draw level.getNodes()
		for (Node node : level.getNodes())
			node.render(gc, sbg, g);
		
		//test print
		g.setColor(Color.red);
		g.setFont(font);
		g.drawString(timeString, gc.getWidth() - (font.getWidth("." + Timer.getStartingTime())), font.getHeight("" + Timer.getStartingTime()) / 2);
	
		// draw hovered node
		g.setColor(selectionColor);
		g.setAntiAlias(false);
		if (hovered != null)
			g.fill(new Circle(hovered.scaleX(), hovered.scaleY(), hoverCircle));
		if (lastHovered != null)
			g.fill(new Circle(lastHovered.scaleX(), lastHovered.scaleY(), hoverCircle));

		// draw level.getWires()
		for (Wire wire : level.getWires())
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
			for (Node node : level.getNodes()) {
				if (node.contains(mouseX, mouseY)) {
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
			// logic for clicking on level.getNodes()
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
			
			// two level.getNodes() selected, attempt to switch level.getWires()
			if (lastSelected != null && selected != null && lastSelected != selected) {
				if (selected.getWires() != lastSelected.getWires()) { // level.getNodes() from different level.getWires()
					
					// switch level.getNodes()
					selected.swapLocations(lastSelected);

					// deselect level.getNodes()
					selected = null;
					lastSelected = null;
				} else { // level.getNodes() on same wire

					// deselect level.getNodes()
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

			// test if any level.getWires() intersect
			levelComplete = true;
			for (int i = 0; i < level.getWires().size() - 1; i++) {
				for (int j = i + 1; j < level.getWires().size(); j++) {
					if (level.getWires().get(i).intersects(level.getWires().get(j))) {
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
		
		Timer.update(delta);
		
		timeString = "" + (float)Timer.getTime() / 1000;
		

		// if no wires intersect end level
		if (levelComplete) {
			winDelay -= delta;
			if (winDelay <= 0)
				sbg.enterState(UncrossTheWires.GAMEPLAY);
		}

	}

	@Override
	public int getID() {
		return UncrossTheWires.GAMEPLAY;
	}

}
