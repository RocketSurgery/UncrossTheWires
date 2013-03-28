package com.github.rocketsurgery;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class LowestScore extends Gameplay {

	@Override
	public int getID() {
		return UncrossTheWires.LOWEST_SCORE;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		super.update(gc, sbg, delta);

		// update score
		if (hasSwapped) {
			Score.update(1000);
			hasSwapped = false;
		}
		Score.update(delta);

		// if level complete pause then reset
		if (Level.isSolved()) {
			if (delay <= 0)
				sbg.enterState(UncrossTheWires.SCORE_MENU);
			else
				delay -= delta;
		} else
			Timer.increase(delta);
	}

}
