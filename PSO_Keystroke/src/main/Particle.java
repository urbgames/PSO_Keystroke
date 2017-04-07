package main;

import java.util.Arrays;

public class Particle implements Comparable<Particle> {

	private double pBest[], gBest[], position[], velocity[];
	private double fitness, fitnesspBest, fitnessgBest, FAR, FRR;
	private int size;

	public Particle(int size) {
		this.size = size;
		this.pBest = new double[size];
		this.gBest = new double[size];
		this.position = new double[size];
		this.velocity = new double[size];
		this.fitness = 0;
		initialize();
	}

	private void initialize() {
		for (int i = 0; i < this.size; i++)
			this.position[i] = Math.random();
	}

	public double[] getpBest() {
		return pBest;
	}

	public void setpBest(double pBest[]) {
		this.pBest = pBest;
	}

	public double[] getgBest() {
		return gBest;
	}

	public void setgBest(double gBest[]) {
		this.gBest = gBest;
	}

	public double[] getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity[]) {
		this.velocity = velocity;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public int getSize() {
		return size;
	}

	public double[] getPosition() {
		return position;
	}

	public void setPosition(double position[]) {
		this.position = position;
	}

	public int compareTo(Particle particle) {
		if (this.fitness < particle.getFitness())
			return -1;
		else if (this.fitness > particle.getFitness())
			return 1;
		else
			return 0;
	}

	public boolean[] getBinaryParticle() {
		boolean[] positionsTemp = new boolean[size];
		for (int i = 0; i < size; i++)
			positionsTemp[i] = position[i] >= 0 ? true : false;
		return positionsTemp;
	}

	public String toStringBinary() {
		String arrayString = "[";
		for (int i = 0; i < position.length; i++) {
			arrayString += position[i] >= 0 ? 1 : 0;
			if (i + 1 != position.length) {
				arrayString += ",";
			}
		}
		arrayString += "]";
		return arrayString;
	}

	public String toStringBinarygBest() {
		String arrayString = "[";
		for (int i = 0; i < gBest.length; i++) {
			arrayString += gBest[i] >= 0 ? 1 : 0;
			if (i + 1 != gBest.length) {
				arrayString += ",";
			}
		}
		arrayString += "]";
		return arrayString;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(position);
	}

	public double getFitnessgBest() {
		return fitnessgBest;
	}

	public void setFitnessgBest(double fitnessgBest) {
		this.fitnessgBest = fitnessgBest;
	}

	public double getFitnesspBest() {
		return fitnesspBest;
	}

	public void setFitnesspBest(double fitnesspBest) {
		this.fitnesspBest = fitnesspBest;
	}

	public double getFAR() {
		return FAR;
	}

	public void setFAR(double fAR) {
		FAR = fAR;
	}

	public double getFRR() {
		return FRR;
	}

	public void setFRR(double fRR) {
		FRR = fRR;
	}

	public int getEnableFeaturesgBest() {
		int count = 0;
		for (int i = 0; i < gBest.length; i++) {
			if (gBest[i] >= 0)
				count++;
		}
		return count;
	}

	public int getDisabledFeaturesgBest() {
		int count = 0;
		for (int i = 0; i < gBest.length; i++) {
			if (!(gBest[i] >= 0))
				count++;
		}
		return count;
	}

	public double getFeatureReductiongBest() {
		return (100 * getDisabledFeaturesgBest()) / size;
	}

}
