import java.util.Random;

public class Board {
	
	public int[] backRow;
	public int[][] whiteLocations;
	public int[][] blackLocations;
	public int[][] friendly;
	public int[][] opposing;
	
	/* 1 = Rook, 2 = Knight, 3 = Bishop, 5 = King,
	 * 4 = Queen, 6 = White Pawn, 7 = Black Pawn, 0 = Empty
	 */
	
	public Board() {
		backRow = new int[]{1, 1, 2, 2, 3, 3, 4, 5};
		whiteLocations = new int[8][8];
		blackLocations = new int[8][8];
		randomizeBackRow();
		setupFrontRow();
		friendly = whiteLocations;
    	opposing = blackLocations;
	}
	
	private void randomizeBackRow() {
		for (int i = 0; i < backRow.length; i++) {
			Random rand = new Random();
			int randNum = rand.nextInt(backRow.length);
			while (whiteLocations[backRow.length-1][randNum] != 0) {
				randNum = (randNum + 1) % backRow.length;
			}
			whiteLocations[backRow.length-1][randNum] = backRow[i];
			blackLocations[0][randNum] = backRow[i];
		}
	}
	
	private void setupFrontRow() {
		for (int i = 0; i < backRow.length; i++) {
			whiteLocations[backRow.length-2][i] = 6;
			blackLocations[1][i] = 7;
		}
	}
	
	public void changeBoard(int newX, int newY, int oldX, int oldY) {
		if (whiteLocations[oldX][oldY] != 0) {
			whiteLocations[newX][newY] = whiteLocations[oldX][oldY];
			whiteLocations[oldX][oldY] = 0;
			blackLocations[newX][newY] = 0;
		} else if (blackLocations[oldX][oldY] != 0) {
			blackLocations[newX][newY] = blackLocations[oldX][oldY];
			blackLocations[oldX][oldY] = 0;
			whiteLocations[newX][newY] = 0;
		}
		
		if (friendly == whiteLocations) {
			friendly = blackLocations;
			opposing = whiteLocations;
		} else {
			friendly = whiteLocations;
			opposing = blackLocations;
		}
	}
	
}
