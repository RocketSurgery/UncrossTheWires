package com.github.rocketsurgery;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SingleNode extends Node {
	
	private Wire attachedWire;

	public SingleNode(float x, float y) {
		super(x, y);
	}

	@Override
	public boolean attach(Wire wire) {
		attachedWire = wire;
		return true;
	}

	@Override
	public void swapLocations(Node other) {
		// use Node implementation to swap x,y coords
		super.swapLocations(other);

		this.attachedWire.resetEnds();
		for (Wire wire : other.getWires())
			wire.resetEnds();
	}

	@Override
	public List<Wire> getWires() {
		ArrayList<Wire> temp = new ArrayList<>();
		temp.add(attachedWire);
		return temp;
	}

}
