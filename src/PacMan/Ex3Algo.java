package PacMan;

import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This is the major algorithmic class for Ex3 - the PacMan game:
 *
 * This code is a very simple example (random-walk algorithm).
 * Your task is to implement (here) your PacMan algorithm.
 */
public class Ex3Algo implements PacManAlgo{
	private int _count;
	public Ex3Algo() {_count=0;}
	@Override
	/**
	 *  Add a short description for the algorithm as a String.
	 */
	public String getInfo() {
		return null;
	}
	@Override
	/**
	 * This ia the main method - that you should design, implement and test.
	 */
	public int move(PacmanGame game) {
		if(_count==0 || _count==300) {
			int code = 0;
			int[][] board = game.getGame(0);
			printBoard(board);
			int blue = Game.getIntColor(Color.BLUE, code);
			int pink = Game.getIntColor(Color.PINK, code);
			int black = Game.getIntColor(Color.BLACK, code);
			int green = Game.getIntColor(Color.GREEN, code);
			System.out.println("Blue=" + blue + ", Pink=" + pink + ", Black=" + black + ", Green=" + green);
			String pos = game.getPos(code);
			System.out.println("Pacman coordinate: "+pos);
			GhostCL[] ghosts = game.getGhosts(code);
			printGhosts(ghosts);
			int up = Game.UP, left = Game.LEFT, down = Game.DOWN, right = Game.RIGHT;
		}
		_count++;
		int dir = Game.LEFT;
		return dir;
	}
	private static void printBoard(int[][] b) {
		for(int y =0;y<b[0].length;y++){
			for(int x =0;x<b.length;x++){
				int v = b[x][y];
				System.out.print(v+"\t");
			}
			System.out.println();
		}
	}
	private static void printGhosts(GhostCL[] gs) {
		for(int i=0;i<gs.length;i++){
			GhostCL g = gs[i];
			System.out.println(i+") status: "+g.getStatus()+",  type: "+g.getType()+",  pos: "+g.getPos(0)+",  time: "+g.remainTimeAsEatable(0));
		}
	}
	private static int randomDir() {
		int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
		int ind = (int)(Math.random()*dirs.length);
		return dirs[ind];
	}
    private static int moveSelect(PacmanGame game){
        Map board = new Map(game.getGame(0));
        int blue = Game.getIntColor(Color.BLUE, 0);
        Pixel2D closest = closestPink(game);
        String pos = game.getPos(0);
        Pixel2D Pac = new Index2D(Integer.parseInt(pos.split(",")[0]),Integer.parseInt(pos.split(",")[1]));
        Pixel2D[] path = board.shortestPath(Pac,closest,blue);

        return 0;
    }
    private static Pixel2D closestPink(PacmanGame game){
        String pos = game.getPos(0);
        GhostCL[] ghosts = game.getGhosts(0);
        int[][] board = game.getGame(0);
        int blue = Game.getIntColor(Color.BLUE, 0);
        int pink = Game.getIntColor(Color.PINK, 0);
        for (int i = 0; i < ghosts.length; i++){
            board[Integer.parseInt(ghosts[i].getPos(0).split(",")[0])][Integer.parseInt(ghosts[i].getPos(0).split(",")[1])] = blue;
        }
        Pixel2D Pac = new Index2D(Integer.parseInt(pos.split(",")[0]),Integer.parseInt(pos.split(",")[1]));
        Queue<Pixel2D> queue = new LinkedList<>();
        queue.add(Pac);
        Pixel2D ans;
        while(queue.peek() != null){
            if (queue.peek().getX() != 0){
                if (board[queue.peek().getX()-1][queue.peek().getY()] == pink){
                    ans = new Index2D(queue.peek().getX()-1,queue.peek().getY());
                    return ans;
                }else if (board[queue.peek().getX()-1][queue.peek().getY()] != pink){
                    if (board[queue.peek().getX()-1][queue.peek().getY()] != blue){
                        Pixel2D temp = new Index2D(queue.peek().getX()-1,queue.peek().getY());
                        queue.add(temp);
                    }
                }
            } else if (game.isCyclic()) {
                if (board[board.length-1][queue.peek().getY()] == pink){
                    ans = new Index2D(board.length-1,queue.peek().getY());
                    return ans;
                } else if (board[board.length-1][queue.peek().getY()] != pink){
                    if (board[queue.peek().getX()-1][queue.peek().getY()] != blue) {
                        Pixel2D temp = new Index2D(board.length - 1, queue.peek().getY());
                        queue.add(temp);
                    }
                }
            }
            if (queue.peek().getX() != board.length-1){
                if (board[queue.peek().getX()+1][queue.peek().getY()] == pink){
                    ans = new Index2D(queue.peek().getX()+1,queue.peek().getY());
                    return ans;
                }else if (board[queue.peek().getX()+1][queue.peek().getY()] != pink){
                    if (board[queue.peek().getX()+1][queue.peek().getY()] != blue){
                        Pixel2D temp = new Index2D(queue.peek().getX()+1,queue.peek().getY());
                        queue.add(temp);
                    }
                }
            } else if (game.isCyclic()) {
                if (board[0][queue.peek().getY()] == pink){
                    ans = new Index2D(0,queue.peek().getY());
                    return ans;
                } else if (board[0][queue.peek().getY()] != pink){
                    if (board[queue.peek().getX()-1][queue.peek().getY()] != blue) {
                        Pixel2D temp = new Index2D(0, queue.peek().getY());
                        queue.add(temp);
                    }
                }
            }
            if (queue.peek().getY() != 0){
                if (board[queue.peek().getX()][queue.peek().getY()-1] == pink){
                    ans = new Index2D(queue.peek().getX(),queue.peek().getY()-1);
                    return ans;
                }else if (board[queue.peek().getX()][queue.peek().getY()-1] != pink){
                    if (board[queue.peek().getX()][queue.peek().getY()-1] != blue){
                        Pixel2D temp = new Index2D(queue.peek().getX(),queue.peek().getY()-1);
                        queue.add(temp);
                    }
                }
            } else if (game.isCyclic()) {
                if (board[queue.peek().getX()][board[0].length-1] == pink){
                    ans = new Index2D(queue.peek().getX(),board[0].length-1);
                    return ans;
                } else if (board[queue.peek().getX()][board[0].length-1] != pink){
                    if (board[queue.peek().getX()-1][board[0].length-1] != blue) {
                        Pixel2D temp = new Index2D(queue.peek().getX(), board[0].length-1);
                        queue.add(temp);
                    }
                }
            }
            if (queue.peek().getY() != board[0].length-1){
                if (board[queue.peek().getX()][queue.peek().getY()+1] == pink){
                    ans = new Index2D(queue.peek().getX(),queue.peek().getY()+1);
                    return ans;
                }else if (board[queue.peek().getX()][queue.peek().getY()+1] != pink){
                    if (board[queue.peek().getX()][queue.peek().getY()+1] != blue){
                        Pixel2D temp = new Index2D(queue.peek().getX(),queue.peek().getY()+1);
                        queue.add(temp);
                    }
                }
            } else if (game.isCyclic()) {
                if (board[queue.peek().getX()][0] == pink){
                    ans = new Index2D(queue.peek().getX(),0);
                    return ans;
                } else if (board[queue.peek().getX()][0] != pink){
                    if (board[queue.peek().getX()][0] != blue) {
                        Pixel2D temp = new Index2D(queue.peek().getX(), 0);
                        queue.add(temp);
                    }
                }
            }
            queue.remove();
        }
        return null;
    }
}