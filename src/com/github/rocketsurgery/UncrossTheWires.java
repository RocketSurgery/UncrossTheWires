package com.github.rocketsurgery;

import org.newdawn.slick.*;

public class UncrossTheWires extends BasicGame {
	
	public UncrossTheWires(String title) {
		super(title);
	}

	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException {
	}
	
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new UncrossTheWires("Uncross"));

		app.setDisplayMode(800, 600, false);
		app.start();
	}

}
