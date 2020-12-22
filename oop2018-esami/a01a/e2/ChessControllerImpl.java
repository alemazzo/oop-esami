package a01a.e2;

public class ChessControllerImpl implements ChessController {

	private int gridSize;
	private Pair<Integer, Integer> horse;
	private Pair<Integer, Integer> pawn;
	
	@Override
	public void loadField(int gridSize, Pair<Integer, Integer> horse, Pair<Integer, Integer> pawn) {
		this.gridSize = gridSize;
		this.horse = horse;
		this.pawn = pawn;
	}

	private Pair<Integer, Integer> getDistance(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
		return new Pair<>(Math.abs(p1.getX() - p2.getX()), Math.abs(p1.getY() - p2.getY()));
	}

	private boolean isCorrectPosition(Pair<Integer, Integer> position) {
		Pair<Integer, Integer> distance = getDistance(position, this.horse);
		return ((distance.getX() == 1 && distance.getY() == 2) || (distance.getX() == 2 && distance.getY() == 1));
	}
	
	@Override
	public boolean move(Pair<Integer, Integer> position) {
		if(!isCorrectPosition(position)) {
			return false;
		}else {
			this.horse = position;
			return true;
		}
	}

}
