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
	public static final int PADDING = 50;

	private boolean hasClicked = false;
	private boolean levelComplete = false;
	private float winDelay = 500f;

	// variables for selected Level.getNodes()
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

	@SuppressWarnings("unchecked") // because font.getEffects() is dumb
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
		
		// generate Level
		Level.generateLevel(MainMenu.selectedLevel, gc);
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

		// draw Level.getNodes()
		for (Node node : Level.nodes)
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
				if (selected.getWires() != lastSelected.getWires()) { // Level.getNodes() from different Level.getWires()
					
					// switch Level.getNodes()
					selected.swapLocations(lastSelected);

					// deselect Level.getNodes()
					selected = null;
					lastSelected = null;
				} else { // Level.getNodes() on same wire

					// deselect Level.getNodes()
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

			// reset hasClicked
			if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				hasClicked = false;
			}
			
		}
		
		Timer.update(delta);
		
		timeString = "" + (float)Timer.getTime() / 1000;
		
		if (Timer.isDone()) {
			Timer.reset();
			sbg.enterState(UncrossTheWires.SCORE_MENU);
		}
		

		// if no wires intersect end Level
		if (levelComplete) {
			/*winDelay -= delta;
			if (winDelay <= 0) {*/
				Score.update(1);
				sbg.enterState(UncrossTheWires.GAMEPLAY);
			//}
		}

	}

	@Override
	public int getID() {
		return UncrossTheWires.GAMEPLAY;
	}

}
