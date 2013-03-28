package com.github.rocketsurgery;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class MostSolved extends Gameplay {

	public static int timeLimit = 30000; // in miliseconds
	public static final int MIN_TIME = 30 * 1000;
	public static boolean changingDifficulty = false;

	@Override
	public int getID() {
		return UncrossTheWires.MOST_SOLVED;
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);

		// initialize variables
		Timer.set(timeLimit);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		super.update(gc, sbg, delta);

		if (Timer.isDone()) {
			sbg.enterState(UncrossTheWires.SCORE_MENU);
		}

		// if level complete pause then reset
		if (Level.isSolved()) {
			if (delay <= 0) {
				resetLevel(gc);
				Score.update(1);
			} else
				delay -= delta;
		} else {
			Timer.decrease(delta);
		}
	}

}
