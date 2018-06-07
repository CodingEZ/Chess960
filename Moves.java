// Add castling, checks, and en-passante

public class Moves {
	
	private byte[][] empty;
	private byte[][] attack;
	private byte[] curLocat;
	
	public Moves() {
		empty = new byte[8][8];
		attack = new byte[8][8];
		curLocat = new byte[]{-1, -1};
	}
	
	public void reset() {
		empty = new byte[8][8];
		attack = new byte[8][8];
		curLocat[0] = -1;
		curLocat[1] = -1;
	}
	
	public byte getCurLocatX() {
		return curLocat[0];
	}
	
	public byte getCurLocatY() {
		return curLocat[1];
	}

	public void selectPiece(byte x, byte y, byte piece, byte[][] friendly, byte[][] opposing) {
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
	
	public boolean possibleEmpty(byte x, byte y) {
		return empty[x][y] == 1;
	}
	
	public boolean possibleAttack(byte x, byte y) {
		return attack[x][y] == 1;
	}
	
	private boolean isOffboard() {
		return curLocat[0] < 0 || curLocat[0] > 7 || curLocat[1] < 0 || curLocat[1] > 7;
	}
	
	private boolean isEmpty(byte[][] friendly, byte[][] opposing) {
		return friendly[curLocat[0]][curLocat[1]] == 0 && opposing[curLocat[0]][curLocat[1]] == 0;
	}
	
	private boolean cannotMoveFurther(byte[][] friendly, byte[][] opposing) {
		if (friendly[curLocat[0]][curLocat[1]] != 0) {
			return true;
		} else if (opposing[curLocat[0]][curLocat[1]] != 0) {
			attack[curLocat[0]][curLocat[1]] = 1;
			return true;
		} else {
			empty[curLocat[0]][curLocat[1]] = 1;
		}
		return false;
	}
	
	private void movesRook(byte[][] friendly, byte[][] opposing) {
		byte[] save = new byte[2];
		System.arraycopy(curLocat, 0, save, 0, curLocat.length);
		
		// down direction
		System.arraycopy(save, 0, curLocat, 0, curLocat.length);
		while (true) {
			curLocat[0]++;
			if (curLocat[0] == 8 || cannotMoveFurther(friendly, opposing)) {
				break;
			}
		}
		
		// up direction
		System.arraycopy(save, 0, curLocat, 0, curLocat.length);
		while (true) {
			curLocat[0]--;
			if (curLocat[0] == -1 || cannotMoveFurther(friendly, opposing)) {
				break;
			}
		}
		
		// right direction
		System.arraycopy(save, 0, curLocat, 0, curLocat.length);
		while (true) {
			curLocat[1]++;
			if (curLocat[1] == 8 || cannotMoveFurther(friendly, opposing)) {
				break;
			}
		}
		
		// left direction
		System.arraycopy(save, 0, curLocat, 0, curLocat.length);
		while (true) {
			curLocat[1]--;
			if (curLocat[1] == -1 || cannotMoveFurther(friendly, opposing)) {
				break;
			}
		}
		
		// reset to original spot
		System.arraycopy(save, 0, curLocat, 0, curLocat.length);
	}
	
	private void movesBishop(byte[][] friendly, byte[][] opposing) {
		byte[] save = new byte[2];
		System.arraycopy(curLocat, 0, save, 0, curLocat.length);
		
		// down-right direction
		while (true) {
			curLocat[0]++; curLocat[1]++;
			if (curLocat[0] == 8 || curLocat[1] == 8 || cannotMoveFurther(friendly, opposing)) {
				break;
			}
		}
		
		// down-left direction
		System.arraycopy(save, 0, curLocat, 0, curLocat.length);
		while (true) {
			curLocat[0]++; curLocat[1]--;
			if (curLocat[0] == 8 || curLocat[1] == -1 || cannotMoveFurther(friendly, opposing)) {
				break;
			}
		}
		
		// up-right direction
		System.arraycopy(save, 0, curLocat, 0, curLocat.length);
		while (true) {
			curLocat[0]--; curLocat[1]++;
			if (curLocat[0] == -1 || curLocat[1] == 8 || cannotMoveFurther(friendly, opposing)) {
				break;
			}
		}
		
		// up-left direction
		System.arraycopy(save, 0, curLocat, 0, curLocat.length);
		while (true) {
			curLocat[0]--; curLocat[1]--;
			if (curLocat[0] == -1 || curLocat[1] == -1 || cannotMoveFurther(friendly, opposing)) {
				break;
			}
		}
		
		// reset to original spot
		System.arraycopy(save, 0, curLocat, 0, curLocat.length);
	}
	
	private void movesPawn(byte[][] friendly, byte[][] opposing) {
		byte[] save = new byte[2];
		System.arraycopy(curLocat, 0, save, 0, curLocat.length);
		
		if (friendly[save[0]][save[1]] == 6) {
			// check if pawn can move forward one space
			curLocat[0]--;
			if (isEmpty(friendly, opposing)) {
				empty[curLocat[0]][curLocat[1]] = 1;
			}
			// check if pawn can attack left
			curLocat[1]--;
			if (curLocat[1] != -1 && opposing[curLocat[0]][curLocat[1]] != 0) {
				attack[curLocat[0]][curLocat[1]] = 1;
			}
			// check if pawn can attack right
			curLocat[1] += 2;
			if (curLocat[1] != 8 && opposing[curLocat[0]][curLocat[1]] != 0) {
				attack[curLocat[0]][curLocat[1]] = 1;
			}
			// check if pawn can move two spaces
			curLocat[1]--;
			if (save[0] == 6 && isEmpty(friendly, opposing)) {
				curLocat[0]--;
				if (isEmpty(friendly, opposing)) {
					empty[curLocat[0]][curLocat[1]] = 1;
				}
				curLocat[0] += 2;	// reset curLocat to original spot
			} else {
				curLocat[0]++;		// reset curLocat to original spot
			}
			
		} else if (friendly[save[0]][save[1]] == 7) {
			// check if pawn can move forward one space
			curLocat[0]++;
			if (isEmpty(friendly, opposing)) {
				empty[curLocat[0]][curLocat[1]] = 1;
			}
			// check if pawn can attack left
			curLocat[1]--;
			if (curLocat[1] != -1 && opposing[curLocat[0]][curLocat[1]] != 0) {
				attack[curLocat[0]][curLocat[1]] = 1;
			}
			// check if pawn can attack right
			curLocat[1] += 2;
			if (curLocat[1] != 8 && opposing[curLocat[0]][curLocat[1]] != 0) {
				attack[curLocat[0]][curLocat[1]] = 1;
			}
			// check if pawn can move two spaces
			curLocat[1]--;
			if (save[0] == 1 && isEmpty(friendly, opposing)) {
				curLocat[0]++;
				if (isEmpty(friendly, opposing)) {
					empty[curLocat[0]][curLocat[1]] = 1;
				}
				curLocat[0] -= 2;	// reset curLocat to original spot
			} else {
				curLocat[0]--;		// reset curLocat to original spot
			}		
		}

	}

	private void movesKnight(byte[][] friendly, byte[][] opposing) {
		byte[] save = new byte[2];
		System.arraycopy(curLocat, 0, save, 0, curLocat.length);
		byte[][] directions = {{1, 2}, {2, 1}, {-1, 2}, {-2, 1}, {1, -2}, {2, -1}, {-1, -2}, {-2, -1}};
		
		for (byte i = 0; i < directions.length; i++) {
			// add in direction
			curLocat[0] += directions[i][0];
			curLocat[1] += directions[i][1];
			if (isOffboard()) {
				;
			} else if (isEmpty(friendly, opposing)) {
				empty[curLocat[0]][curLocat[1]] = 1;
			} else if (opposing[curLocat[0]][curLocat[1]] != 0) {
				attack[curLocat[0]][curLocat[1]] = 1;
			}
			// undo direction
			curLocat[0] -= directions[i][0];
			curLocat[1] -= directions[i][1];
		}
	}
	
	private void movesQueen(byte[][] friendly, byte[][] opposing) {
		movesRook(friendly, opposing);
		movesBishop(friendly, opposing);
	}

	private void movesKing(byte[][] friendly, byte[][] opposing) {
		byte[] save = new byte[2];
		System.arraycopy(curLocat, 0, save, 0, curLocat.length);
		byte[][] directions = {{1, 1}, {1, 0}, {1, -1}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}, {-1, -1}};
		
		for (byte i = 0; i < directions.length; i++) {
			// add in direction
			curLocat[0] += directions[i][0];
			curLocat[1] += directions[i][1];
			if (isOffboard()) {
				;
			} else if (isEmpty(friendly, opposing)) {
				empty[curLocat[0]][curLocat[1]] = 1;
			} else if (opposing[curLocat[0]][curLocat[1]] != 0) {
				attack[curLocat[0]][curLocat[1]] = 1;
			}
			// undo direction
			curLocat[0] -= directions[i][0];
			curLocat[1] -= directions[i][1];
		}
	}
	
}
