package systemStatusControl;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import main.Particle;

public class StatusPSO {

	private List<Particle> populacao = new ArrayList<Particle>();
	private int maxInteretor;
	private int currentInteretor;
	private int repetition;
	private int sizePopulation;
	private int seed;
	private String classifier;

	@JsonCreator
	public StatusPSO(@JsonProperty("populacao") List<Particle> populacao,
			@JsonProperty("currentInteretor") int currentInteretor, @JsonProperty("maxInteretor") int maxInteretor,
			@JsonProperty("repetition") int repetition, @JsonProperty("sizePopulation") int sizePopulation,
			@JsonProperty("seed") int seed,@JsonProperty("classifier") String classifier) {
		this.currentInteretor = currentInteretor;
		this.populacao = populacao;
		this.maxInteretor = maxInteretor;
		this.repetition = repetition;
		this.sizePopulation = sizePopulation;
		this.seed = seed;
		this.classifier = classifier;
	}

	public List<Particle> getPopulacao() {
		return populacao;
	}

	public void setPopulacao(List<Particle> populacao) {
		this.populacao = populacao;
	}

	public int getMaxInteretor() {
		return maxInteretor;
	}

	public void setMaxInteretor(int maxInteretor) {
		this.maxInteretor = maxInteretor;
	}

	public int getRepetition() {
		return repetition;
	}

	public void setRepetition(int repetition) {
		this.repetition = repetition;
	}

	public int getSizePopulation() {
		return sizePopulation;
	}

	public void setSizePopulation(int sizePopulation) {
		this.sizePopulation = sizePopulation;
	}

	public int getCurrentInteretor() {
		return currentInteretor;
	}

	public void setCurrentInteretor(int currentInteretor) {
		this.currentInteretor = currentInteretor;
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	public String getClassifier() {
		return classifier;
	}

	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}

}
