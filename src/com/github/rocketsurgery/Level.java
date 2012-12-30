package com.github.rocketsurgery;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.newdawn.slick.GameContainer;

public class Level {

	private List<Wire> wires;
	private List<Node> nodes;
	
	private Level(List<Wire> wires, List<Node> nodes) {
		this.wires = wires;
		this.nodes = nodes;
	}
	
	public static Level loadLevel(String title, GameContainer gc) {
		try (Scanner scan = new Scanner(Level.class.getClassLoader().getResourceAsStream("levels.dat"))) {
			
			// find header for level
			String next;
			do {
				next = scan.next();
			} while (!next.equalsIgnoreCase(title));
			
			float xSize = scan.nextInt();
			float ySize = scan.nextInt();
			
			ArrayList<Node> nodes = new ArrayList<Node>();
			ArrayList<Wire> wires = new ArrayList<Wire>();
			
			// until the next header is reached
			while (scan.hasNextInt()) {
				
				// load in end points
				float x1 = scan.nextInt() * gc.getWidth() / xSize;
				float y1 = scan.nextInt() * gc.getHeight() / ySize;
				float x2 = scan.nextInt() * gc.getWidth() / xSize;
				float y2 = scan.nextInt()* gc.getHeight() / ySize;
				
				// create nodes and wire
				Node node1 = null;
				for (Node node : nodes) {
					if (x1 == node.getCenterX() && y1 == node.getCenterY()) {
						node1 = node;
						System.out.println("node1 already exists");
					}
				}
				// node doesn't already exist
				if (node1 == null) {
					node1 = new MultiNode(x1, y1);
				}
				
				Node node2 = null;
				for (Node node : nodes)
					if (x2 == node.getCenterX() && y2 == node.getCenterY())
						node2 = node;
				if (node2 == null)
					node2 = new MultiNode(x2, y2);
				
				Wire wire = new Wire(node1, node2);
				
				// add nodes and wires to temp list
				if (!nodes.contains(node1)) {
					nodes.add(node1);
				}
				if (!nodes.contains(node2))
					nodes.add(node2);
				wires.add(wire);
			}
			
			// create level and return it
			return new Level(wires, nodes);
			
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException();
		}
	}
	
	public List<Wire> getWires() {
		return this.wires;
	}
	
	public List<Node> getNodes() {
		return this.nodes;
	}
	
}
