package a01b.e2;

public interface Logic {

	enum Response{
		BOMB, EMPTY
	}
	
	boolean hit(int x, int y);

	boolean hasWin();

	int getNearMinesCount(Pair<Integer, Integer> value);

}
