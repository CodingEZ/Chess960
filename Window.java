
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Window extends Frame implements MouseListener {
	
	private static final long serialVersionUID = 1L;
	int s;
	int th;
	Board board;
	Moves moves;
	Window window;
	MediaTracker tracker;
	BufferedImage[] whiteImgs;
	BufferedImage[] blackImgs;
       
	public Window(int size, int titleHeight) {
        super("Super Title");
        s = size;
        th = titleHeight;
        board = new Board();
		moves = new Moves();
		window = this;
		tracker = new MediaTracker(this);
        prepareGUI();
        loadImages();
        addMouseListener(this);
    }

    private void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(text, x, y);
    }
    
    private void prepareGUI(){
        setSize(s, s + th);
        addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
        }        
        }); 
    }    

    private void loadImages() {
    	whiteImgs = new BufferedImage[8];
    	blackImgs = new BufferedImage[8];
    	String[] whiteNames = new String[]{null, "wr", "wn", "wb", "wq", "wk", "wp", null};
    	String[] blackNames = new String[]{null, "br", "bn", "bb", "bq", "bk", null, "bp"}; 
    	
    	for (int i = 0; i < 8; i++) {
    		if (blackNames[i] != null) {
    			try {
        			blackImgs[i] = ImageIO.read( getClass().getResource("images\\" + blackNames[i] + ".jpg") );
        			tracker.addImage(blackImgs[i], 0);
    			} catch (IOException e) {
    			}
    		}
    		
    		if (whiteNames[i] != null) {
    			try {
        			whiteImgs[i] = ImageIO.read( getClass().getResource("images\\" + whiteNames[i] + ".jpg") );
        			tracker.addImage(whiteImgs[i], 0);
    			} catch (IOException e) {
    			}
    		}
    	}
    }
    
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        Font font = new Font("Arial", Font.PLAIN, 24);
        g.setFont(font);
        Rectangle rect = new Rectangle(s/2-30, th/3, 60, th*2/3);
        drawCenteredString(g, "Chess 960", rect, font);   
        
        for (int i = 0; i < 8; i++) {
        	for (int j = 0; j < 8; j++) {
        		if (moves.getCurLocatX() == i && moves.getCurLocatY() == j) {
        			g.setColor(Color.BLUE);
        		} else if (moves.possibleEmpty(i, j)) {
        			g.setColor(Color.PINK);
        		} else if (moves.possibleAttack(i, j)) {
        			g.setColor(Color.RED);
        		} else if ( (i+j)%2 == 0 ) {
        			g.setColor(Color.YELLOW);
        		} else {
        			g.setColor(Color.GREEN);
        		}
            	g.fillRect(j * s/8, th + i * s/8, s/8, s/8);
            	
            	if (board.whiteLocations[i][j] != 0) {
            		try {
						tracker.waitForID(0);
						int piece = board.whiteLocations[i][j];
						g.drawImage(whiteImgs[piece],
									j * s/8 + 10, th + i * s/8 + 10,
									(j+1) * s/8 - 10, th + (i+1) * s/8 - 10,
        							0, 0, whiteImgs[piece].getWidth(), whiteImgs[piece].getHeight(), 
        							null);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
            	} else if (board.blackLocations[i][j] != 0) {
            		try {
						tracker.waitForID(0);
						int piece = board.blackLocations[i][j];
						g.drawImage(blackImgs[piece],
									j * s/8 + 10, th + i * s/8 + 10,
									(j+1) * s/8 - 10, th + (i+1) * s/8 - 10,
        							0, 0, blackImgs[piece].getWidth(), blackImgs[piece].getHeight(), 
        							null);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
            	}
        	}
        }
    }
   
    public void mouseClicked(MouseEvent e) {
    	int x = e.getLocationOnScreen().x;
    	int y = e.getLocationOnScreen().y - th;
    	if (y >= 0) {
    		int row = y * 8 / s;
        	int col = x * 8 / s;
        	processClick(row, col);
    	}
    }
    
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    
    private void processClick(int x, int y) {
    	if (moves.pieceSelected()) {
    		if (x == moves.getCurLocatX() && y == moves.getCurLocatY()) {
    			moves.reset();
    		} else if (moves.possibleEmpty(x, y) || moves.possibleAttack(x, y)) {
    			board.changeBoard(x, y, moves.getCurLocatX(), moves.getCurLocatY());
    			moves.reset();
    		}
    	} else if (board.friendly[x][y] != 0) {
    		moves.selectPiece(x, y, board.friendly[x][y], board.friendly, board.opposing);
    	}
    	
    	window.repaint();
    }
    
}
