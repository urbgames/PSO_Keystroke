package test;

public class Test {

	public static void main(String[] args) throws Exception {

		double sigmoid = 1 / (1 + Math.exp(-10));
		double a = 1.0;
		int b = 1;
		if (a == b)
			System.out.println(sigmoid);

	}

}
