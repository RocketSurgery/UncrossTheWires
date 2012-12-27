package com.github.rocketsurgery;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class UncrossTheWires extends StateBasedGame {

	public static final int MAIN_MENU = 0;
	public static final int GAMEPLAY = 1;
	
	public UncrossTheWires(String title) {
		super(title);
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		this.addState(new MainMenu());
		this.addState(new Gameplay());
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new UncrossTheWires("Uncross"));

		app.setDisplayMode(800, 600, false);
		app.start();
	}

}