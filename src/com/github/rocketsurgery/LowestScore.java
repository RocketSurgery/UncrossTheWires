package com.github.rocketsurgery;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class LowestScore extends Gameplay {

	@Override
	public int getID() {
		return UncrossTheWires.LOWEST_SCORE;
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
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
		if (levelComplete) {
			if (winDelay <= 0)
				sbg.enterState(UncrossTheWires.SCORE_MENU);
			else
				winDelay -= delta;
		}
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);
		
		// draw Score
		g.setColor(Color.red);
		g.setFont(font);
		g.drawString("" + Score.getScore(), gc.getWidth() - (font.getWidth("." + Integer.MAX_VALUE)), 25);
	}
}
