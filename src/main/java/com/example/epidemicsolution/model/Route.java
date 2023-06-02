package com.example.epidemicsolution.model;

public class Route {

	private int city1;
	private int city2;
	private int suppliesUsed;

	public Route(int city1, int city2, int suppliesUsed) {
		this.city1 = city1;
		this.city2 = city2;
		this.suppliesUsed = suppliesUsed;
	}

	public int getCity1() {
		return city1;
	}

	public int getCity2() {
		return city2;
	}

	public int getSuppliesUsed() {
		return suppliesUsed;
	}

	public void setCity1(int city1) {
		this.city1 = city1;
	}

	public void setCity2(int city2) {
		this.city2 = city2;
	}

	public void setSuppliesUsed(int suppliesUsed) {
		this.suppliesUsed = suppliesUsed;
	}
}
