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

	protected MenuOption[] menuOptions;

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
		for (MenuOption op : menuOptions)
			op.render(gc, sbg, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		// create input
		Input input = gc.getInput();
		int mouseY = input.getMouseY();
		int mouseX = input.getMouseX();

		// execute click behavior
		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !hasBeenPressed) {
			for (MenuOption op : menuOptions) {
				if (op.contains(mouseX, mouseY))
					op.executeBehavior(mouseX, mouseY);
			}
			hasBeenPressed = true;
		}

		if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
			hasBeenPressed = false;
		
		// update options
		for (MenuOption op : menuOptions)
			op.update(gc, sbg, delta);

	}

	protected abstract static class MenuOption implements DisplayElement {

		protected String[] options;
		protected int optionIndex;
		protected final float xCenterline, yCenterline;
		protected final Polygon nextButton, prevButton;
		protected final boolean hasButtons;

		MenuOption(float xCenter, float yCenter, String[] ops, boolean buttons) {
			options = ops;
			xCenterline = xCenter;
			yCenterline = yCenter;
			optionIndex = 0;
			hasButtons = buttons;
			
			if (hasButtons) {
				nextButton = new Polygon();
				nextButton.addPoint(xCenter + 300, yCenter - 10);
				nextButton.addPoint(xCenter + 300, yCenter + 10);
				nextButton.addPoint(xCenter + 320, yCenter);
				
				prevButton = new Polygon();
				prevButton.addPoint(xCenter - 300, yCenter - 10);
				prevButton.addPoint(xCenter - 300, yCenter + 10);
				prevButton.addPoint(xCenter - 320, yCenter);
			} else {
				// TODO Option.MenuOption() properly instantiate buttonless menu
				nextButton = new Polygon();
				nextButton.addPoint(xCenter + 300, yCenter - 10);
				nextButton.addPoint(xCenter + 300, yCenter + 10);
				nextButton.addPoint(xCenter + 320, yCenter);
				
				prevButton = null;
			}
		}

		public void nextOption() {
			optionIndex = (optionIndex + 1) % options.length;
		}

		public void previousOption() {
			optionIndex = (optionIndex - 1 < 0) ? options.length - 1 : optionIndex - 1;
		}

		public void executeBehavior(int x, int y) {
			if (nextButton.contains(x, y))
				nextOption();
			else if (hasButtons && prevButton.contains(x, y))
				previousOption();
		}

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
			g.drawString(options[optionIndex], xCenterline - font.getWidth(options[optionIndex]) / 2, yCenterline - font.getHeight(options[optionIndex]) / 2);
			
			// TODO Menu.Option.render() draw buttons
			g.setColor(textColor);
			if (hasButtons) {
				g.fill(nextButton);
				g.fill(prevButton);
			} else {
				g.fill(nextButton);
			}
		}

		@Override
		public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
			// default implementation is empty
		}

		// utility methods
		protected static float calcYCenterline(float relPos, GameContainer gc) {
			return relPos * gc.getHeight();
		}
		
		protected static float calcYCenterline(int index, int total, GameContainer gc) {
			return calcYCenterline(calcRelativePos(index, total), gc);
		}
		
		protected static float calcXCenterline(GameContainer gc) {
			return gc.getWidth() / 2;
		}

		protected static float calcRelativePos(int index, int total) {
			return ((float) (index + 1) / (float) (total + 1));
		}
		
	}

}
