package com.github.rocketsurgery;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ScoreMenu extends Menu {

	private static final int SCORE_DISPLAY = 0;
	private static final int MAIN_MENU = 1;
	private static final int NUM_OPTIONS = 2;

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
	}

	@Override
	protected void initializeMenus(GameContainer gc) {
		menuOptions = new MenuOption[NUM_OPTIONS];
		menuOptions[MAIN_MENU] = new ReturnToMainMenu(gc);
		menuOptions[SCORE_DISPLAY] = new ScoreDisplay(gc);
	}

	@Override
	public int getID() {
		return UncrossTheWires.SCORE_MENU;
	}

	private class ScoreDisplay extends MenuOption {

		ScoreDisplay(GameContainer gc) {
			super(calcXCenterline(gc), calcYCenterline(SCORE_DISPLAY, NUM_OPTIONS, gc), "0", false);
		}

		@Override
		public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
			this.option = Integer.toString(Score.getScore());
		}
		
	}
	
	private class ReturnToMainMenu extends MenuOption {

		ReturnToMainMenu(GameContainer gc) {
			super(calcXCenterline(gc), calcYCenterline(MAIN_MENU, NUM_OPTIONS, gc), "Return To Main Menu");
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