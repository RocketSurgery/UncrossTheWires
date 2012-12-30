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

public class ScoreMenu extends BasicGameState {

	private static final String gameOver = "Game Over";
	private static final String mainMenu = "Main Menu";
	public static final Color bgColor = Color.black;
	public static final Color textColor = Color.white;
	public static final Color selectionColor = Color.blue;
	private static final float selectionGrowSpeed = 5f;

	private UnicodeFont font;
	private float selectionWidth = 0f;
	public static int selectedLevel = -1;
	
	private static int score = 0;

	@Override
	public void enter(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		System.out.println("Entering state " + getID());
		selectionWidth = 0;
		
		score = Score.getScore();
		Score.reset();
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

		// load list of levels
		selectedLevel = 0;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		// draw background
		g.setColor(bgColor);
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());

		// draw title
		g.setColor(textColor);
		g.setFont(font);
		g.drawString(gameOver, (gc.getWidth() / 2) - (font.getWidth(gameOver) / 2), 20);

		// draw level select
		g.setColor(textColor);
		g.drawString("Score: " + score, (gc.getWidth() / 2) - (font.getWidth("Score: " + score) / 2),
				(gc.getHeight() / 4) - (font.getHeight("Score: ") / 2) + 20);

		// draw selection rect
		g.setColor(selectionColor);
		g.fillRect((gc.getWidth() / 2) - selectionWidth, gc.getHeight() / 2 - ((font.getHeight(mainMenu) * 2) / 2), selectionWidth * 2,
				font.getHeight(mainMenu) * 2);

		// draw start
		g.setColor(textColor);
		g.drawString(mainMenu, (gc.getWidth() / 2) - (font.getWidth(mainMenu) / 2), (gc.getHeight() / 2) - (font.getHeight(mainMenu) / 2));
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		// Create Input
		Input input = gc.getInput();
		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();

		boolean onStart = false;

		// If Mouse Is On Main Menu
		if (mouseX > (gc.getWidth() / 2) - (font.getWidth(mainMenu) / 2) && mouseX < (gc.getWidth() / 2) + (font.getWidth(mainMenu) / 2)
				&& mouseY > (gc.getHeight() / 2) - (font.getHeight(mainMenu) / 2) && mouseY < (gc.getHeight() / 2) + (font.getHeight(mainMenu) / 2)) {
			onStart = true;
		}

		if (onStart) {
			selectionWidth = (selectionWidth >= gc.getWidth() / 2) ? gc.getWidth() / 2 : selectionWidth + selectionGrowSpeed * delta;

			if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				sbg.enterState(UncrossTheWires.MAIN_MENU);
			}
		} else {
			selectionWidth = (selectionWidth <= 0) ? 0 : selectionWidth - selectionGrowSpeed * delta;
		}

	}

	@Override
	public int getID() {
		return UncrossTheWires.SCORE_MENU;
	}

}