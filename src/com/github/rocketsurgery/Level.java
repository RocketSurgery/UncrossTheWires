package com.github.rocketsurgery;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

import org.newdawn.slick.GameContainer;

public class Level {

	private List<Wire> wires;
	private List<Node> nodes;
	
	private static final int MAX_RANDOM_WIDTH = 9;
	private static final int MIN_RANDOM_WIDTH = 5;
	
	private static final int MAX_RANDOM_HEIGHT = 8;
	private static final int MIN_RANDOM_HEIGHT = 4;
	
	private Level(List<Wire> wires, List<Node> nodes) {
		this.wires = wires;
		this.nodes = nodes;
	}
	
	public static Level loadLevel(String title, GameContainer gc) {
		
		ArrayList<Node> nodes = new ArrayList<Node>();
		ArrayList<Wire> wires = new ArrayList<Wire>();
		
		if (title.equals("RANDOM")) {
			Random rand = new Random();
			
			//generate width and height between the constants. inclusive.
			float xSize = rand.nextInt(MAX_RANDOM_WIDTH - MIN_RANDOM_WIDTH + 1) + MIN_RANDOM_WIDTH; 
			float ySize = rand.nextInt(MAX_RANDOM_HEIGHT - MIN_RANDOM_HEIGHT + 1) + MIN_RANDOM_HEIGHT;
			
			int numNodes = rand.nextInt(3) + 6;// 4-6 inclusive;
			
			//does not account for nodes on top of nodes
			for (int i = 0; i < numNodes; i++) {
				
				//generate random location more than 1 unit from the edges
				float x = rand.nextInt((int)xSize - 2) + 1; 
				float y = rand.nextInt((int)ySize - 2) + 1;
				
				x *= gc.getWidth() / xSize;
				y *= gc.getHeight() / ySize;;
				
				nodes.add(new MultiNode(x, y));
			}
			
			for (Node node : nodes) {
				
				wires.add(new Wire(node, nodes.get(rand.nextInt(numNodes))));
				
				/*
				int numWiresWanted = rand.nextInt(2) + 2;
				
				int numWiresAttached = 0;
				
				for (Wire wire : wires) {
					if (wire.hasEnd(node))
						numWiresAttached++;
				}
				
				while (numWiresAttached < numWiresWanted) {
					
					int nodeIndex;
					Node otherNode = null;
					boolean wireDuplicated = false;
					boolean nodeDuplicated = false;
					
					do {
						nodeIndex = rand.nextInt(numNodes);
						
						otherNode = nodes.get(nodeIndex);
						
						nodeDuplicated = (node == otherNode);
						
					if (!nodeDuplicated) {
							wireDuplicated = false;
							
							for (Wire wire : wires) {
								if (wire.hasEnds(node, otherNode)) {
									wireDuplicated = true;
									break;
								}
							}
						}
					
					} while (!wireDuplicated && !nodeDuplicated);
					
					wires.add(new Wire(node, otherNode));
					
					numWiresAttached++;
				}*/
				
			}
			
		} else {
		
			try (Scanner scan = new Scanner(Level.class.getClassLoader().getResourceAsStream("levels.dat"))) {
				
				// find header for level
				String next;
				do {
					next = scan.next();
				} while (!next.equalsIgnoreCase(title));
				
				float xSize = scan.nextInt();
				float ySize = scan.nextInt();
				
				//find "nodes" header
				do {
					next = scan.nextLine();
				} while (!next.equalsIgnoreCase("nodes"));
				
				//read in the first line of node info
				String line = scan.nextLine();
				
				Scanner lineScanner = new Scanner("");
				
				while (!line.equals("connections")) {
					lineScanner = new Scanner (line);
					
	
					lineScanner.nextInt();
	
					float x = lineScanner.nextInt() * gc.getWidth() / xSize;
					float y = lineScanner.nextInt() * gc.getHeight() / ySize;;
					
					nodes.add(new MultiNode(x, y));
					
					line = scan.nextLine();
					lineScanner.close();
				}
				
				//read in the first line of wire info
				line = scan.nextLine();
				
				while (!line.equals("")) {
					lineScanner = new Scanner (line);
					
					int index = lineScanner.nextInt();
					
					while (lineScanner.hasNextInt()) {
						wires.add(new Wire(nodes.get(index),nodes.get(lineScanner.nextInt())));
					}
					
					line = scan.nextLine();
					lineScanner.close();
				}
				
				lineScanner.close();
				
			} catch (NoSuchElementException e) {
				throw new IllegalArgumentException();
			}
		
		}
		
		// create level and return it
		return new Level(wires, nodes);
	}
	
	public List<Wire> getWires() {
		return this.wires;
	}
	
	public List<Node> getNodes() {
		return this.nodes;
	}
	
}
