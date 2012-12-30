package com.github.rocketsurgery;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class LowestScore extends Gameplay {

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
	}
	
	@Override
	public int getID() {
		return UncrossTheWires.LOWEST_SCORE;
	}
}
