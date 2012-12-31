package com.github.rocketsurgery;

public abstract class Timer {

	private static final int STARTING_TIME = 30000;
	private static int time = STARTING_TIME;
	
	public static int getStartingTime() {
		return STARTING_TIME;
	}
	
	public static void decrease(int delta) {
		time = (time > 0) ? time - delta : 0;
	}
	
	public static void increase(int delta) {
		time += delta;
	}
	
	public static boolean isDone() {
		return time <= 0;
	}
	
	public static int getTime() {
		return time;
	}
	
	public static void reset() {
		time = STARTING_TIME;
	}
}
