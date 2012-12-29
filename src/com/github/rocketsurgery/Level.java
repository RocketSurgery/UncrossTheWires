package com.github.rocketsurgery;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Level {

	private List<Wire> wires;
	private List<Node> nodes;
	
	private Level(List<Wire> wires, List<Node> nodes) {
		this.wires = wires;
		this.nodes = nodes;
	}
	
	public static Level loadLevel(String title) {
		try (Scanner scan = new Scanner(new File("res/levels.dat"))) {

			String next;
			do {
				next = scan.next();
			} while (!next.equalsIgnoreCase(title));
			
			while (scan.hasNextInt()) {
				
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("wha... how? the file is RIGHT THERE");
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException();
		}
		
		return null;
	}
	
}
