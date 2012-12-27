package com.github.rocketsurgery;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

public class UncrossTheWires extends StateBasedGame {
	
	public UncrossTheWires(String title) {
		super(title);
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new UncrossTheWires("Uncross"));

		app.setDisplayMode(800, 600, false);
		app.start();
	}

}
