package com.github.rocketsurgery;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class MultiNode extends Node {
	
	private ArrayList<Wire> wires;

	public MultiNode(float centerPointX, float centerPointY) {
		super(centerPointX, centerPointY);
		wires = new ArrayList<>();
	}

	@Override
	public boolean attach(Wire wire) {
		if (wires.contains(wire))
			return false;
		wires.add(wire);
		return true;
	}

	@Override
	public void swapLocations(Node other) {
		super.swapLocations(other);
		for (Wire wire : wires)
			wire.resetEnds();
		for (Wire wire : other.getWires())
			wire.resetEnds();
	}
	
	@Override
	public List<Wire> getWires() {
		return wires;
	}

}
