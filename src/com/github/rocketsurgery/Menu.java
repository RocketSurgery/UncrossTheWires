package com.github.rocketsurgery;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Menu extends BasicGameState {

	// menu
	protected MenuOption[] menuOptions;
	protected boolean hasPressedKey = false;
	private boolean hasBeenPressed = true;

	// graphical constants
	public static final Color BG_COLOR = Color.black;
	public static final Color TEXT_COLOR = Color.white;

	// graphics
	protected Image backgroundImage;
	protected static UnicodeFont font;
	protected String titleText = "Uncross The Wires";

	@SuppressWarnings("unchecked")
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

		// load font
		if (font == null) {
			font = new UnicodeFont("font.ttf", 40, false, false);
			font.addAsciiGlyphs();
			font.getEffects().add(new ColorEffect()); // Create a default white
			font.loadGlyphs();
		}

		// load background
		backgroundImage = new Image("res/bg.gif");

		// initialize menus
		initializeMenus(gc);
	}

	@Override
	public void enter(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		System.out.println("Entering state " + getID());

		// prevents accidental button clicking if the user still has their mouse
		// button down from the last screen
		hasBeenPressed = true;
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

		// draw title
		g.setColor(TEXT_COLOR);
		g.setFont(font);
		g.drawString(titleText, (gc.getWidth() / 2) - (font.getWidth(titleText) / 2), 20);

		// draw menus
		for (MenuOption op : menuOptions)
			op.render(gc, sbg, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		// create input
		Input input = gc.getInput();
		int mouseY = input.getMouseY();
		int mouseX = input.getMouseX();

		// reset hasBeenPressed
		if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
			hasBeenPressed = false;

		// execute click behavior
		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !hasBeenPressed) {
			for (MenuOption op : menuOptions) {
				if (op.contains(mouseX, mouseY))
					op.executeBehavior(mouseX, mouseY);
			}
			hasBeenPressed = true;
		}

		// update options
		for (MenuOption op : menuOptions)
			op.update(gc, sbg, delta);

	}

	// abstract method called in init()
	// must be overridden by subclass with code to initialize menus
	protected abstract void initializeMenus(GameContainer gc);

	// internal class used for displaying options
	protected abstract static class MenuOption implements DisplayElement {

		// menu option
		protected String[] options;
		protected int optionIndex;
		protected boolean executeFlag = false;

		// graphical constants
		protected static final float BUTTON_OFFSET = 300;
		protected static final float BUTTON_HEIGHT = 40;
		protected static final float BUTTON_WIDTH = 40;

		// graphics
		protected final float xCenterline, yCenterline;
		protected final Polygon nextButton, prevButton;
		protected final boolean hasButtons;

		MenuOption(float xCenter, float yCenter, String[] ops) {
			options = ops;
			xCenterline = xCenter;
			yCenterline = yCenter;
			optionIndex = 0;
			hasButtons = true;

			nextButton = new Polygon();
			nextButton.addPoint(xCenter + BUTTON_OFFSET, yCenter - BUTTON_HEIGHT / 2);
			nextButton.addPoint(xCenter + BUTTON_OFFSET, yCenter + BUTTON_HEIGHT / 2);
			nextButton.addPoint(xCenter + BUTTON_OFFSET + BUTTON_WIDTH, yCenter);

			prevButton = new Polygon();
			prevButton.addPoint(xCenter - BUTTON_OFFSET, yCenter - BUTTON_HEIGHT / 2);
			prevButton.addPoint(xCenter - BUTTON_OFFSET, yCenter + BUTTON_HEIGHT / 2);
			prevButton.addPoint(xCenter - BUTTON_OFFSET - BUTTON_WIDTH, yCenter);
		}

		MenuOption(float xCenter, float yCenter, String op) {
			options = new String[1];
			options[0] = op;
			optionIndex = 0;
			xCenterline = xCenter;
			yCenterline = yCenter;
			hasButtons = true;

			nextButton = new Polygon();
			nextButton.addPoint(xCenter + BUTTON_OFFSET, yCenter - BUTTON_HEIGHT / 2);
			nextButton.addPoint(xCenter + BUTTON_OFFSET, yCenter + BUTTON_HEIGHT / 2);
			nextButton.addPoint(xCenter + BUTTON_OFFSET + BUTTON_WIDTH, yCenter);

			prevButton = null;
		}

		MenuOption(float xCenter, float yCenter, String op, boolean buttons) {
			options = new String[1];
			options[0] = op;
			optionIndex = 0;
			xCenterline = xCenter;
			yCenterline = yCenter;

			if (buttons) {
				hasButtons = true;

				nextButton = new Polygon();
				nextButton.addPoint(xCenter + BUTTON_OFFSET, yCenter - BUTTON_HEIGHT / 2);
				nextButton.addPoint(xCenter + BUTTON_OFFSET, yCenter + BUTTON_HEIGHT / 2);
				nextButton.addPoint(xCenter + BUTTON_OFFSET + BUTTON_WIDTH, yCenter);

				prevButton = null;
			} else {
				hasButtons = false;
				nextButton = null;
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
			if (hasButtons) {
				if (prevButton != null) {
					if (nextButton.contains(x, y))
						nextOption();
					else if (prevButton.contains(x, y))
						previousOption();
				} else {
					executeFlag = true;
				}
			}
		}

		public boolean contains(float x, float y) {
			if (hasButtons && prevButton != null)
				return nextButton.contains(x, y) || prevButton.contains(x, y);
			else if (hasButtons && prevButton == null)
				return nextButton.contains(x, y);
			else
				return false;
		}

		@Override
		public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
			g.setColor(TEXT_COLOR);
			g.setFont(font);

			// draw text
			g.drawString(options[optionIndex], xCenterline - font.getWidth(options[optionIndex]) / 2,
					yCenterline - font.getHeight(options[optionIndex]) / 2);

			// draw buttons
			g.setColor(TEXT_COLOR);
			if (hasButtons) {
				g.fill(nextButton);
				if (prevButton != null)
					g.fill(prevButton);
			}
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
