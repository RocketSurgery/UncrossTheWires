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
		
		// TODO create menus
		options = new Option[NUM_OPTIONS];
		options[GAME_MODE] = new GameModes((float) (GAME_MODE + 1) / (float) (NUM_OPTIONS + 1));
		options[DIFFICULTY] = new Difficulty((float) (DIFFICULTY + 1) / (float) (NUM_OPTIONS + 1));
		options[START] = new Start((float) (START + 1) / (float) (NUM_OPTIONS + 1));
	}

	@Override
	public int getID() {
		return UncrossTheWires.MAIN_MENU;
	}

	private static class GameModes extends Option {

		GameModes(float vert) {
			super(vert, Level.getGameModes(), true);
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
	
	private static class Difficulty extends Option {

		Difficulty(float vert) {
			super(vert, null, true);
			options = new String[1];
			options[0] = (new Integer(Level.getDifficulty())).toString();
		}

		@Override
		public void nextOption() {
			// TODO Auto-generated method stub
			Level.changeDifficulty(1);
			options[0] = (new Integer(Level.getDifficulty())).toString();
		}

		@Override
		public void previousOption() {
			// TODO Auto-generated method stub
			Level.changeDifficulty(-1);
			options[0] = (new Integer(Level.getDifficulty())).toString();
		}

		@Override
		public void executeBehavior(int x, int y) {
			// TODO Auto-generated method stub
			
		}
		
	}

	private static class Start extends Option {

		private boolean executeBehavior = false;
		
		private static final String[] text = {"Start"};
		
		Start(float vert) {
			super(vert, text, false);
			// TODO Auto-generated constructor stub
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
