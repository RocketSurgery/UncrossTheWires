package com.github.rocketsurgery;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenu extends Menu {

	private static final int GAME_MODE = 0;
	private static final int DIFFICULTY = 1;
	private static final int START = 2;
	private static final int NUM_OPTIONS = 3;	// must match the number of options
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);
		
		// create menus
		menuOptions = new MenuOption[NUM_OPTIONS];
		menuOptions[GAME_MODE] = new GameModes(gc);
		menuOptions[DIFFICULTY] = new Difficulty(gc);
		menuOptions[START] = new Start(gc);
	}

	@Override
	public int getID() {
		return UncrossTheWires.MAIN_MENU;
	}
	
	private static class GameModes extends MenuOption {

		GameModes(GameContainer gc) {
			super(calcXCenterline(gc), calcYCenterline(GAME_MODE, NUM_OPTIONS, gc), Level.getGameModes(), true);
		}

		@Override
		public void nextOption() {
			super.nextOption();
			Level.changeSelectedMode(1);
		}

		@Override
		public void previousOption() {
			super.previousOption();
			Level.changeSelectedMode(-1);
		}

		@Override
		public void executeBehavior(int x, int y) {
			if (nextButton.contains(x, y))
				nextOption();
			else if (prevButton.contains(x, y))
				previousOption();
		}
		
	}
	
	private static class Difficulty extends MenuOption {

		Difficulty(GameContainer gc) {
			super(calcXCenterline(gc), calcYCenterline(DIFFICULTY, NUM_OPTIONS, gc), null, true);
			options = new String[1];
			options[0] = (new Integer(Level.getDifficulty())).toString();
		}

		@Override
		public void nextOption() {
			Level.changeDifficulty(1);
			options[0] = (new Integer(Level.getDifficulty())).toString();
		}

		@Override
		public void previousOption() {
			Level.changeDifficulty(-1);
			options[0] = (new Integer(Level.getDifficulty())).toString();
		}

		@Override
		public void executeBehavior(int x, int y) {
			
		}
		
	}

	private static class Start extends MenuOption {

		private boolean executeBehavior = false;
		
		private static final String[] text = {"Start"};
		
		Start(GameContainer gc) {
			super(calcXCenterline(gc), calcYCenterline(START, NUM_OPTIONS, gc), text, false);
		}

		@Override
		public boolean contains(float x, float y) {
			return true;
		}
		
		@Override
		public void executeBehavior(int x, int y) {
			if (nextButton.contains(x, y))
				executeBehavior = true;
		}
		
		@Override
		public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
			if (executeBehavior) {
				executeBehavior = false;
				sbg.enterState(Level.getGameModeStateValues()[Level.getSelectedMode()]);
			}
		}
		
	}

}
