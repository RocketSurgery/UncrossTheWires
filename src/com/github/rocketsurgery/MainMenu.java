package com.github.rocketsurgery;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenu extends BasicGameState {

	private static final String[] gameModes = { "Lowest Score", "Most Solved" };
	private static final Integer[] gameModeStateValues = { UncrossTheWires.LOWEST_SCORE, UncrossTheWires.MOST_SOLVED };

	private static final String titleText = "Uncross The Wires";
	private static final String startText = "Start";
	private static final Color bgColor = Color.black;
	private static final Color textColor = Color.white;
	private static final Color selectionColor = Color.blue;
	private static final float selectionGrowSpeed = 5f;

	private static final int MODE = 0;
	private static final int DIFFICULTY = 1;
	private static final int START = 2;
	private static final int NUM_MENUS = 3;

	private OptionMenu[] menus;
	private UnicodeFont font;
	private int selectedMenu;
	private boolean hasPressedKey = false;

	// preventing accidental restart from score menu
	private boolean hasReleased = false;

	@Override
	public void enter(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		System.out.println("Entering state " + getID());
		hasReleased = false;
	}

	@Override
	public void leave(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		System.out.println("Leaving state " + getID());
	}

	@SuppressWarnings("unchecked")
	// because font.getEffects() is dumb
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// load font
		font = new UnicodeFont("cubic.ttf", 40, false, false);
		font.addAsciiGlyphs();
		font.getEffects().add(new ColorEffect()); // Create a default white
		font.loadGlyphs();
		
		// create menus
		float vertOffset = 20 + font.getHeight(titleText);
		float vertSize = gc.getHeight() - vertOffset;
		menus = new OptionMenu[NUM_MENUS];
		menus[MODE] = new OptionMenu(gameModes, vertOffset + vertSize * (float) (MODE + 1) / (float) (NUM_MENUS + 1));
		menus[DIFFICULTY] = new OptionMenu(Level.difficulties, vertOffset + vertSize * (float) (DIFFICULTY + 1) / (float) (NUM_MENUS + 1));
		menus[START] = new OptionMenu(startText, vertOffset + vertSize * (float) (START + 1) / (float) (NUM_MENUS + 1));
		selectedMenu = -1;
		
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

		// draw menus
		for (OptionMenu op : menus)
			if (op != null)
				op.render(gc, sbg, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		// Create Input
		Input input = gc.getInput();
		int mouseY = input.getMouseY();
		
		int mouseMenu = -1;
		// determine which menu the mouse if over if any
		for (int i = 0;  i < menus.length; i++)
			if (menus[i].contains(mouseY))
				mouseMenu = i;

		// manage mouse clicks
		if (mouseMenu >= 0 && input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && hasReleased) {
			if (mouseMenu == START)
				sbg.enterState(gameModeStateValues[menus[MODE].getSelected()]);
			else 
				menus[mouseMenu].updateSelection(1);
			hasReleased = false;
		} else if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			hasReleased = true;
		}

		// manage key presses
		if (input.isKeyDown(Input.KEY_ENTER)) {
			if (selectedMenu >= 0 && !hasPressedKey) {
				if (selectedMenu == START)
					sbg.enterState(gameModeStateValues[menus[MODE].getSelected()]);
				else 
					menus[selectedMenu].updateSelection(1);
				hasPressedKey = true;
			}
		} else if (input.isKeyDown(Input.KEY_RIGHT)) {
			if (selectedMenu >= 0 && !hasPressedKey) {
				menus[selectedMenu].updateSelection(1);
				hasPressedKey = true;
			}
		} else if (input.isKeyDown(Input.KEY_LEFT)) {
			if (selectedMenu >= 0 && !hasPressedKey) {
				menus[selectedMenu].updateSelection(-1);
				hasPressedKey = true;
			}
		} else if (input.isKeyDown(Input.KEY_DOWN)) {
			if (!hasPressedKey) {
				if (selectedMenu >= 0)
					menus[selectedMenu].setHasFocus(false);
				selectedMenu = (selectedMenu + 1 == menus.length) ? 0 : selectedMenu + 1;
				menus[selectedMenu].setHasFocus(true);
				hasPressedKey = true;
			}
		} else if (input.isKeyDown(Input.KEY_UP)) {
			if (!hasPressedKey) {
				if (selectedMenu >= 0)
					menus[selectedMenu].setHasFocus(false);
				selectedMenu = (selectedMenu - 1 < 0) ? menus.length - 1 : selectedMenu - 1;
				menus[selectedMenu].setHasFocus(true);
				hasPressedKey = true;
			}
		} else {
			hasPressedKey = false;
		}
		
		// update menus
		for (OptionMenu menu : menus)
			menu.update(gc, sbg, delta);
		
		// update Level.selectedDifficulty
		Level.selectedDifficulty = menus[DIFFICULTY].getSelected();
	}

	@Override
	public int getID() {
		return UncrossTheWires.MAIN_MENU;
	}
	
	private class OptionMenu implements DisplayElement {

		private String[] options;
		private int selected;
		private float vertCenterLine;
		private float selectSize;
		private boolean hasFocus;
		
		public OptionMenu(String[] o, float vOff) {
			options = o;
			vertCenterLine = vOff;
			selected = 0;
			hasFocus = false;
			selectSize = 0;
		}
		
		public OptionMenu(String o, float vOff) {
			options = new String[1];
			options[0] = o;
			vertCenterLine = vOff;
			selected = 0;
			hasFocus = false;
			selectSize = 0;
		}
		
		@Override
		public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
			// draw selection rect
			g.setColor(selectionColor);
			g.fillRect(gc.getWidth() / 2 - selectSize / 2, vertCenterLine - font.getHeight(options[0]) - 10, selectSize, font.getHeight(options[0]) + 20);
			
			// draw text
			g.setColor(textColor);
			g.setFont(font);
			g.drawString(options[selected], gc.getWidth() / 2 - font.getWidth(options[selected]) / 2, vertCenterLine - font.getHeight(options[selected]));
		}
		
		@Override
		public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
			if (hasFocus)
				selectSize = (selectSize + selectionGrowSpeed <= gc.getWidth()) ? selectSize + selectionGrowSpeed : gc.getWidth();
			else
				selectSize = (selectSize - selectionGrowSpeed >= 0) ? selectSize - selectionGrowSpeed : 0;
		}
		
		public boolean contains(float y) {
			return y >= vertCenterLine - font.getHeight(options[selected]) && y <= vertCenterLine + font.getHeight(options[selected]);
		}
		
		public void updateSelection(int amt) {
			if (amt < 0) {
				selected = (selected - 1 < 0) ? options.length - 1 : selected - 1;
			} else {
				selected = (selected + amt) % options.length;
			}
		}
		
		public int getSelected() {
			return selected;
		}
		
		public void setHasFocus(boolean focus) {
			hasFocus = focus;
		}
	}

}
