package com.github.rocketsurgery;

public abstract class Score {

	private static int score = 0;
	
	public static int getScore() {
		return score;
	}
	
	public static void update(int scoreDelta) {
		score += scoreDelta;
	}
	
	public static void reset() {
		score = 0;
	}
}
