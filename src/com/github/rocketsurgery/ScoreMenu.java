package com.github.rocketsurgery;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ScoreMenu extends Menu {

	private static final int MAIN_MENU = 0;
	private static final int NUM_OPTIONS = 1;

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);
	}

	@Override
	protected void initializeMenus(GameContainer gc) {
		menuOptions = new MenuOption[NUM_OPTIONS];
		menuOptions[MAIN_MENU] = new MainMenu(gc);
	}

	@Override
	public int getID() {
		return UncrossTheWires.SCORE_MENU;
	}

	private class MainMenu extends MenuOption {

		MainMenu(GameContainer gc) {
			super(calcXCenterline(gc), calcYCenterline(MAIN_MENU, NUM_OPTIONS, gc), "Main Menu");
		}

		@Override
		public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
			if (executeFlag) {
				executeFlag = false;
				sbg.enterState(UncrossTheWires.MAIN_MENU);
			}
		}

	}

}