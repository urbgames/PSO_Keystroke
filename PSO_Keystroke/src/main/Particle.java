package main;

import java.util.Arrays;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Particle implements Comparable<Particle> {
	
	private double pBest[];
	private double gBest[];
	private double position[];
	private double velocity[];
	private double fitness;
	private double fitnesspBest;
	private double fitnessgBest;
	private double FAR;
	private double FRR;
	private double gBestFAR;
	private double gBestFRR;
	private int size;

	@JsonCreator
	public Particle(@JsonProperty("pBest") double[] pBest, @JsonProperty("gBest") double[] gBest,@JsonProperty("position") double[] position,@JsonProperty("velocity") double[] velocity,@JsonProperty("fitness") double fitness,
			@JsonProperty("fitnesspBest") double fitnesspBest,@JsonProperty("fitnessgBest") double fitnessgBest,@JsonProperty("fAR") double fAR,@JsonProperty("fRR")  double fRR,@JsonProperty("gBestFAR") double gBestFAR,@JsonProperty("gBestFRR") double gBestFRR,
			@JsonProperty("size") int size) {
		this.pBest = pBest;
		this.gBest = gBest;
		this.position = position;
		this.velocity = velocity;
		this.fitness = fitness;
		this.fitnesspBest = fitnesspBest;
		this.fitnessgBest = fitnessgBest;
		this.FAR = fAR;
		this.FRR = fRR;
		this.gBestFAR = gBestFAR;
		this.gBestFRR = gBestFRR;
		this.size = size;
	}

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

	@JsonIgnore
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

	public double getgBestFAR() {
		return gBestFAR;
	}

	public void setgBestFAR(double gBestFAR) {
		this.gBestFAR = gBestFAR;
	}

	public double getgBestFRR() {
		return gBestFRR;
	}

	public void setgBestFRR(double gBestFRR) {
		this.gBestFRR = gBestFRR;
	}

	@JsonIgnore
	public int getEnableFeaturesgBest() {
		int count = 0;
		for (int i = 0; i < gBest.length; i++) {
			if (gBest[i] >= 0)
				count++;
		}
		return count;
	}

	@JsonIgnore
	public int getDisabledFeaturesgBest() {
		int count = 0;
		for (int i = 0; i < gBest.length; i++) {
			if (!(gBest[i] >= 0))
				count++;
		}
		return count;
	}

	@JsonIgnore
	public double getFeatureReductiongBest() {
		return (100 * getDisabledFeaturesgBest()) / size;
	}

}
