package com.github.rocketsurgery;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public interface DisplayElement {

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException;
	
}
