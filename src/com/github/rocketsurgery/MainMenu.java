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
		
		this.titleText = "Uncross The Wires";
	}

	@Override
	public int getID() {
		return UncrossTheWires.MAIN_MENU;
	}
	
	@Override
	protected void initializeMenus(GameContainer gc) {
		menuOptions = new MenuOption[NUM_OPTIONS];
		menuOptions[GAME_MODE] = new GameModes(gc);
		menuOptions[DIFFICULTY] = new Difficulty(gc);
		menuOptions[START] = new Start(gc);
	}
	
	private static class GameModes extends MenuOption {

		GameModes(GameContainer gc) {
			super(calcXCenterline(gc), calcYCenterline(GAME_MODE, NUM_OPTIONS, gc), Level.getGameModes());
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

		@Override
		public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
			
		}
		
	}
	
	private static class Difficulty extends MenuOption {

		Difficulty(GameContainer gc) {
			super(calcXCenterline(gc), calcYCenterline(DIFFICULTY, NUM_OPTIONS, gc), generateOptions());
		}

		@Override
		public void nextOption() {
			optionIndex = (optionIndex + 1 == options.length) ? optionIndex : optionIndex + 1;
			Level.changeDifficulty(1);
		}

		@Override
		public void previousOption() {
			optionIndex = (optionIndex - 1 < 0) ? 0 : optionIndex - 1; 
			Level.changeDifficulty(-1);
		}

		@Override
		public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
			
		}
		
		private static String[] generateOptions() {
			String[] result = new String[Level.MAX_DIFFICULTY - Level.MIN_DIFFICULTY];
			for (int i = 0; i < result.length; i++)
				result[i] = (new Integer(i + Level.MIN_DIFFICULTY)).toString();
			return result;
		}
	}

	private static class Start extends MenuOption {
		
		Start(GameContainer gc) {
			super(calcXCenterline(gc), calcYCenterline(START, NUM_OPTIONS, gc), "Start");
		}
	
		@Override
		public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
			if (executeFlag) {
				executeFlag = false;
				sbg.enterState(Level.getGameModeStateValues()[Level.getSelectedMode()]);
			}
		}
		
	}


}
