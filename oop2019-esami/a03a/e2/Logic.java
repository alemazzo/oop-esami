package a03a.e2;

public interface Logic {

	enum Status{
		ENEMY, PLAYER, EMPTY
	}
	void next();

	boolean isFinished();

	Status getPositionStatus(Pair<Integer, Integer> value);

	void addPoint(Pair<Integer, Integer> pair);

}
