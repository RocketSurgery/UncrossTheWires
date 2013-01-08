package com.github.rocketsurgery;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class MostSolved extends Gameplay {

	private static final int TIME_LIMIT = 30000;

	@Override
	public int getID() {
		return UncrossTheWires.MOST_SOLVED;
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);

		// initialize variables
		reset();
		Timer.set(TIME_LIMIT);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		super.update(gc, sbg, delta);

		if (Timer.isDone()) {
			sbg.enterState(UncrossTheWires.SCORE_MENU);
		}

		// if level complete pause then reset
		if (levelComplete) {
			Score.update(1);
			if (winDelay <= 0)
				reset();
			else
				winDelay -= delta;
		} else {
			Timer.decrease(delta);
		}
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);
		
		// draw timer
		g.setColor(Color.red);
		g.setFont(font);
		String timeString = "" + (float) Timer.getTime() / 1000;
		g.drawString(timeString, gc.getWidth() - (font.getWidth("." + Integer.MAX_VALUE)), 25);
	}
	
}
