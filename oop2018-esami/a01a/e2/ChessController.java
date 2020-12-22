package a01a.e2;


public interface ChessController {

	void loadField(final int gridSize, final Pair<Integer, Integer> horse, final Pair<Integer, Integer> pawn);
	
	boolean move(Pair<Integer, Integer> position);
	
}
