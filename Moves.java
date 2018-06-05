// Add castling, checks, and en-passante

public class Moves {
	
	public int[][] possible;
	public int[] curLocat;
	public int[] next;
	
	public Moves() {
		possible = new int[27][2];	// empty, attack
		curLocat = new int[]{-1, -1};
		next = new int[] {0, 0};
	}
	
	public void reset() {
		possible = new int[27][2];
		curLocat[0] = -1;
		curLocat[1] = -1;
		next[0] = 0;
		next[1] = 0;
	}
	
	public int getCurLocatX() {
		return curLocat[0];
	}
	
	public int getCurLocatY() {
		return curLocat[1];
	}

	public void selectPiece(int x, int y, int piece, int[][] friendly, int[][] opposing) {
		curLocat[0] = x;
		curLocat[1] = y;
		switch (piece) {
			case 1:
				movesRook(friendly, opposing);
				break;
			case 2:
				movesKnight(friendly, opposing);
				break;
			case 3:
				movesBishop(friendly, opposing);
				break;
			case 4:
				movesQueen(friendly, opposing);
				break;
			case 5:
				movesKing(friendly, opposing);
				break;
			case 6:
			case 7:
				movesPawn(friendly, opposing);
				break;
		}
	}
	
	public boolean pieceSelected() {
		return curLocat[0] != -1;
	}
	
	public boolean isPossible(int x, int y) {
		for (int i = 0; i < possible.length; i++) {
			if (possible[i][0] == x && possible[i][1] == y) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isEmpty(int[][] friendly, int[][] opposing) {
		return friendly[curLocat[0]][curLocat[1]] == 0 || opposing[curLocat[0]][curLocat[1]] == 0;
	}
	
	private boolean endCheck(int[][] friendly, int[][] opposing) {
		if (friendly[curLocat[0]][curLocat[1]] != 0) {
			return true;
		} else if (opposing[curLocat[0]][curLocat[1]] != 0) {
			System.arraycopy(curLocat, 0, possible[next[1]], 0, curLocat.length);
			next[1]++;		//append to attack, update next
			return true;
		} else {
			System.arraycopy(curLocat, 0, possible[next[0]], 0, curLocat.length);
			next[0]++;		//append to empty, update next
		}
		return false;
	}
	
	private void movesRook(int[][] friendly, int[][] opposing) {
		int[] save = new int[2];
		System.arraycopy(curLocat, 0, save, 0, curLocat.length);
		
		// down direction
		System.arraycopy(save, 0, curLocat, 0, curLocat.length);
		while (true) {
			curLocat[0]++;
			if (curLocat[0] == 8 || endCheck(friendly, opposing)) {
				break;
			}
		}
		
		// up direction
		System.arraycopy(save, 0, curLocat, 0, curLocat.length);
		while (true) {
			curLocat[0]--;
			if (curLocat[0] == -1 || endCheck(friendly, opposing)) {
				break;
			}
		}
		
		// right direction
		System.arraycopy(save, 0, curLocat, 0, curLocat.length);
		while (true) {
			curLocat[1]++;
			if (curLocat[1] == 8 || endCheck(friendly, opposing)) {
				break;
			}
		}
		
		// left direction
		System.arraycopy(save, 0, curLocat, 0, curLocat.length);
		while (true) {
			curLocat[1]--;
			if (curLocat[1] == -1 || endCheck(friendly, opposing)) {
				break;
			}
		}
		
		// reset to original spot
		System.arraycopy(save, 0, curLocat, 0, curLocat.length);
	}
	
	private void movesBishop(int[][] friendly, int[][] opposing) {
		int[] save = new int[2];
		System.arraycopy(curLocat, 0, save, 0, curLocat.length);
		
		// down-right direction
		while (true) {
			curLocat[0]++; curLocat[1]++;
			if (curLocat[0] == 8 || curLocat[1] == 8 || endCheck(friendly, opposing)) {
				break;
			}
		}
		
		// down-left direction
		System.arraycopy(save, 0, curLocat, 0, curLocat.length);
		while (true) {
			curLocat[0]++; curLocat[1]--;
			if (curLocat[0] == 8 || curLocat[1] == -1 || endCheck(friendly, opposing)) {
				break;
			}
		}
		
		// up-right direction
		System.arraycopy(save, 0, curLocat, 0, curLocat.length);
		while (true) {
			curLocat[0]--; curLocat[1]++;
			if (curLocat[0] == -1 || curLocat[1] == 8 || endCheck(friendly, opposing)) {
				break;
			}
		}
		
		// up-left direction
		System.arraycopy(save, 0, curLocat, 0, curLocat.length);
		while (true) {
			curLocat[0]--; curLocat[1]--;
			if (curLocat[0] == -1 || curLocat[1] == -1 || endCheck(friendly, opposing)) {
				break;
			}
		}
		
		// reset to original spot
		System.arraycopy(save, 0, curLocat, 0, curLocat.length);
	}
	
	private void movesPawn(int[][] friendly, int[][] opposing) {
		int[] save = new int[2];
		System.arraycopy(curLocat, 0, save, 0, curLocat.length);
		
		if (friendly[save[0]][save[1]] == 6) {
			// check if pawn can move forward one space
			curLocat[0]--;
			if (isEmpty(friendly, opposing)) {
				System.arraycopy(curLocat, 0, possible[next[0]], 0, curLocat.length);
				next[0]++;
			}
			// check if pawn can attack left
			curLocat[1]--;
			if (curLocat[1] != -1 && opposing[curLocat[0]][curLocat[1]] != 0) {
				System.arraycopy(curLocat, 0, possible[next[1]], 0, curLocat.length);
				next[1]++;
			}
			// check if pawn can attack right
			curLocat[1] += 2;
			if (curLocat[1] != 8 && opposing[curLocat[0]][curLocat[1]] != 0) {
				System.arraycopy(curLocat, 0, possible[next[1]], 0, curLocat.length);
				next[1]++;
			}
			// check if pawn can move two spaces
			curLocat[1]--;
			if (save[0] == 6 && isEmpty(friendly, opposing)) {
				curLocat[0]--;
				if (isEmpty(friendly, opposing)) {
					System.arraycopy(curLocat, 0, possible[next[0]], 0, curLocat.length);
					next[0]++;
				}
				curLocat[0] += 2;	// reset curLocat to original spot
			} else {
				curLocat[1]++;		// reset curLocat to original spot
			}
			
		} else if (friendly[save[0]][save[1]] == 7) {
			// check if pawn can move forward one space
			curLocat[0]++;
			if (isEmpty(friendly, opposing)) {
				System.arraycopy(curLocat, 0, possible[next[0]], 0, curLocat.length);
				next[0]++;
			}
			// check if pawn can attack left
			curLocat[1]--;
			if (curLocat[1] != -1 && opposing[curLocat[0]][curLocat[1]] != 0) {
				System.arraycopy(curLocat, 0, possible[next[1]], 0, curLocat.length);
				next[1]++;
			}
			// check if pawn can attack right
			curLocat[1] += 2;
			if (curLocat[1] != 8 && opposing[curLocat[0]][curLocat[1]] != 0) {
				System.arraycopy(curLocat, 0, possible[next[1]], 0, curLocat.length);
				next[1]++;
			}
			// check if pawn can move two spaces
			curLocat[1]--;
			if (save[0] == 1 && isEmpty(friendly, opposing)) {
				curLocat[0]++;
				if (isEmpty(friendly, opposing)) {
					System.arraycopy(curLocat, 0, possible[next[0]], 0, curLocat.length);
					next[0]++;
				}
				curLocat[0] -= 2;	// reset curLocat to original spot
			} else {
				curLocat[0]--;		// reset curLocat to original spot
			}		
		}
		
	}

	private void movesKnight(int[][] friendly, int[][] opposing) {
		int[] save = new int[2];
		System.arraycopy(curLocat, 0, save, 0, curLocat.length);
		int[][] directions = {{1, 2}, {2, 1}, {-1, 2}, {-2, 1}, {1, -2}, {2, -1}, {-1, -2}, {-2, -1}};
		
		for (int i = 0; i < directions.length; i++) {
			// add in direction
			curLocat[0] += directions[i][0];
			curLocat[1] += directions[i][1];
			if (isEmpty(friendly, opposing)) {
				System.arraycopy(curLocat, 0, possible[next[0]], 0, curLocat.length);
				next[0]++;
			} else if (opposing[curLocat[0]][curLocat[1]] != 0) {
				System.arraycopy(curLocat, 0, possible[next[1]], 0, curLocat.length);
				next[1]++;
			}
			// undo direction
			curLocat[0] -= directions[i][0];
			curLocat[1] -= directions[i][1];
		}
	}
	
	private void movesQueen(int[][] friendly, int[][] opposing) {
		movesRook(friendly, opposing);
		int[][] horizontals = new int[27][2];
		System.arraycopy(possible, 0, horizontals, 0, possible.length);
		
		int count = 0;
		int[] empty = new int[] {0, 0};
		while (count != 27 && horizontals[count] != empty) {
			count++;
		}
		
		movesBishop(friendly, opposing);
		System.arraycopy(possible, count, horizontals, 0, horizontals.length - count);
	}

	private void movesKing(int[][] friendly, int[][] opposing) {
		int[] save = new int[2];
		System.arraycopy(curLocat, 0, save, 0, curLocat.length);
		int[][] directions = {{1, 1}, {1, 0}, {1, -1}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}, {-1, -1}};
		
		for (int i = 0; i < directions.length; i++) {
			// add in direction
			curLocat[0] += directions[i][0];
			curLocat[1] += directions[i][1];
			if (isEmpty(friendly, opposing)) {
				System.arraycopy(curLocat, 0, possible[next[0]], 0, curLocat.length);
				next[0]++;
			} else if (opposing[curLocat[0]][curLocat[1]] != 0) {
				System.arraycopy(curLocat, 0, possible[next[1]], 0, curLocat.length);
				next[1]++;
			}
			// undo direction
			curLocat[0] -= directions[i][0];
			curLocat[1] -= directions[i][1];
		}
	}
	
}
