package com.github.rocketsurgery;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Menu extends BasicGameState {

	public static final String titleText = "Uncross The Wires";
	public static final String startText = "Start";

	public static final Color bgColor = Color.black;
	public static final Color textColor = Color.white;
	public static final Color selectionColor = Color.blue;
	public static final float selectionGrowSpeed = 5f;

	public static UnicodeFont font;
	public boolean hasPressedKey = false;

	protected Option[] options;

	// preventing accidental restart from score menu
	private boolean hasBeenPressed = true;

	@SuppressWarnings("unchecked") // because loadGlyphs() is dumb
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// load font
		font = new UnicodeFont("cubic.ttf", 40, false, false);
		font.addAsciiGlyphs();
		font.getEffects().add(new ColorEffect()); // Create a default white
		font.loadGlyphs();
	}

	@Override
	public void enter(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		System.out.println("Entering state " + getID());
		hasBeenPressed = true;
	}

	@Override
	public void leave(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		System.out.println("Leaving state " + getID());
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// draw background
		g.setColor(bgColor);
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());

		// draw title
		g.setColor(textColor);
		g.setFont(font);
		g.drawString(titleText, (gc.getWidth() / 2) - (font.getWidth(titleText) / 2), 20);

		/*
		 * Menu Render Notes: each menu is
		 */
		for (Option op : options)
			op.render(gc, sbg, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		// create input
		Input input = gc.getInput();
		int mouseY = input.getMouseY();
		int mouseX = input.getMouseX();

		// execute click behavior
		if (!hasBeenPressed && input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
			for (Option op : options)
				if (op.contains(mouseX, mouseY))
					op.executeBehavior(mouseX, mouseY);

		if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
			hasBeenPressed = false;
		
		// update options
		for (Option op : options)
			op.update(gc, sbg, delta);

	}

	protected abstract static class Option implements DisplayElement {

		protected String[] options;
		protected int optionIndex;
		protected final float vertPos;
		protected final Polygon nextButton, prevButton;
		protected final boolean hasButtons;

		Option(float vert, String[] ops, boolean buttons) {
			options = ops;
			vertPos = vert;
			optionIndex = 0;
			hasButtons = buttons;
			
			if (hasButtons) {
				// TODO Option.Option() properly instantiate buttons
				nextButton = new Polygon();
				prevButton = new Polygon();
			} else {
				nextButton = new Polygon();
				prevButton = null;
			}
		}

		public void nextOption() {
			optionIndex = (optionIndex + 1) % options.length;
		}

		public void previousOption() {
			optionIndex = (optionIndex - 1 < 0) ? options.length - 1 : optionIndex - 1;
		}

		public abstract void executeBehavior(int x, int y);

		public boolean contains(float x, float y) {
			if (hasButtons)
				return nextButton.contains(x, y) || prevButton.contains(x, y);
			else
				return nextButton.contains(x, y);
		}

		@Override
		public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
			// TODO Menu.Option.render() write complete method
			g.setColor(textColor);
			g.setFont(font);
			
			// draw text
			float xCenterLine =  gc.getWidth() / 2f - font.getWidth(options[optionIndex]) / 2f;
			float yCenterLine = gc.getHeight() * vertPos - font.getHeight(options[optionIndex]) / 2f;
			g.drawString(options[optionIndex], xCenterLine, yCenterLine);
			
			// TODO Menu.Option.render() draw buttons
			if (hasButtons) {
				
			} else {
				
			}
		}

		@Override
		public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
			// default implementation is empty
		}

	}

}
