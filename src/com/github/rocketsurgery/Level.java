package com.github.rocketsurgery;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Level {

	private List<Wire> wires;
	private List<SingleNode> singleNodes;
	
	private Level(List<Wire> wires, List<SingleNode> singleNodes) {
		this.wires = wires;
		this.singleNodes = singleNodes;
	}
	
	public static Level loadLevel(String title) {
		try (Scanner scan = new Scanner(new File("res/levels.dat"))) {
			
			// find header for level
			String next;
			do {
				next = scan.next();
			} while (!next.equalsIgnoreCase(title));
			
			ArrayList<SingleNode> singleNodes = new ArrayList<>();
			ArrayList<Wire> wires = new ArrayList<>();
			
			// until the next header is reached
			while (scan.hasNextInt()) {
				
				// load in coordinates
				int x1 = scan.nextInt();
				int y1 = scan.nextInt();
				int x2 = scan.nextInt();
				int y2 = scan.nextInt();
				
				// create nodes and wire
				SingleNode singleNode1 = new SingleNode(x1, y1);
				SingleNode singleNode2 = new SingleNode(x2, y2);
				Wire wire = new Wire(singleNode1, singleNode2);
				
				// add nodes and wires to temp list
				singleNodes.add(singleNode1);
				singleNodes.add(singleNode2);
				wires.add(wire);
			}
			
			// create level and return it
			return new Level(wires, singleNodes);
			
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
	
	public List<SingleNode> getSingleNodes() {
		return this.singleNodes;
	}
	
}
