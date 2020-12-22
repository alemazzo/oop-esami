package a02a.e2;

public interface Logic {

	
	public enum Status {
		EMPTY, 
		FILL
	}

	void next();

	Status getStatus(Pair<Integer, Integer> y);

}
