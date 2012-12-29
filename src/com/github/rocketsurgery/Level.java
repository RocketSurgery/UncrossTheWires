package com.github.rocketsurgery;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
			
			// find header for level
			String next;
			do {
				next = scan.next();
			} while (!next.equalsIgnoreCase(title));
			
			ArrayList<Node> nodes = new ArrayList<>();
			ArrayList<Wire> wires = new ArrayList<>();
			
			// until the next header is reached
			while (scan.hasNextInt()) {
				
				// load in coordinates
				int x1 = scan.nextInt();
				int y1 = scan.nextInt();
				int x2 = scan.nextInt();
				int y2 = scan.nextInt();
				
				// create nodes and wire
				Node node1 = new Node(x1, y1);
				Node node2 = new Node(x2, y2);
				Wire wire = new Wire(node1, node2);
				
				// add nodes and wires to temp list
				nodes.add(node1);
				nodes.add(node2);
				wires.add(wire);
			}
			
			// create level and return it
			return new Level(wires, nodes);
			
		} catch (FileNotFoundException e) {
			System.out.println("wha... how? the file is RIGHT THERE");
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException();
		}
		
		return null;
	}
	
	public List<Wire> getWires() {
		return this.wires;
	}
	
	public List<Node> getNodes() {
		return this.nodes;
	}
	
}
