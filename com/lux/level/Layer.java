package com.lux.level;

import com.lux.DynamicObject;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

// Pane umesto Group!!!
/// ... ili ako hoces da se jebes 3 sata ko sto sam ja onda samo napred
/**
 * Razlog zasto je Pane, a nije Group jeste zato sto Group svoje elemente pomeri
 * za offset tako da se element najblizi tacki(0,0) pomeri na (0,0).
 * Tj. offset = -najbliziNuli.position
 */
public class Layer extends Pane implements DynamicObject {

	private int depth = 0;
	
	public Layer() {
		super();
	}
/* Ovo postoji samo u Group()1
	public Layer(Collection<Node> children) {
		super(children);
	}
*/
	public Layer(Node... children) {
		super(children);
	}
	
	public Layer(int layer) {
		super();
		setDepth(layer);
	}
	
	public void setDepth(int d) {
		depth = d;
		setViewOrder(-d*640);
	}
	
	public int getDepth() {
		return depth;
	}

	@Override
	public void updatePos(double x, double y) {
		relocate(-x,-y);
	}

	@Override
	public double getWorldX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getWorldY() {
		// TODO Auto-generated method stub
		return 0;
	}

}
