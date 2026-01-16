package PacMan2;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D {
	private int[][] _map;
	private boolean _cyclicFlag = true;
	
	/**
	 * Constructs a w*h 2D raster map with an init value v.
	 * @param w
	 * @param h
	 * @param v
	 */
	public Map(int w, int h, int v) {init(w,h, v);}
	/**
	 * Constructs a square map (size*size).
	 * @param size
	 */
	public Map(int size) {this(size,size, 0);}
	
	/**
	 * Constructs a map from a given 2D array.
	 * @param data
	 */
	public Map(int[][] data) {
		init(data);
	}
	@Override
	public void init(int w, int h, int v) {
        _map = new int[w][h];
        for (int i = 0; i < _map.length; i++){
            for (int j = 0; j < _map[0].length; j++){
                _map[i][j] = v;
            }
        }
	}
	@Override
	public void init(int[][] arr) {
        if (validMap(arr)){
            _map = new int[arr.length][arr[0].length];
            for (int i = 0; i < _map.length; i++)
                for (int j = 0; j < _map[i].length; j++)
                    _map[i][j] = arr[i][j];
        }
	}
	@Override
	public int[][] getMap() {return this._map;}
	@Override
	/////// add your code below ///////
	public int getWidth() {return this._map.length;}
	@Override
	/////// add your code below ///////
	public int getHeight() {return this._map[0].length;}
	@Override
	/////// add your code below ///////
	public int getPixel(int x, int y) { return _map[x][y];}
	@Override
	/////// add your code below ///////
	public int getPixel(Pixel2D p) {
		return this.getPixel(p.getX(),p.getY());
	}
	@Override
	/////// add your code below ///////
	public void setPixel(int x, int y, int v) {this._map[x][y] = v;}
	@Override
	/////// add your code below ///////
	public void setPixel(Pixel2D p, int v) {
		setPixel(p.getX(),p.getY(),v);
	}
	@Override
	/** 
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 */
	public int fill(Pixel2D xy, int new_v) {
        int ans = 0;
        int oldColor = this._map[xy.getX()][xy.getY()];
        if (oldColor == new_v)
            return 0;
        Queue<Pixel2D> queue = new LinkedList<Pixel2D>();
        Pixel2D start = new Index2D(xy);
        this._map[start.getX()][start.getY()] = new_v;
        queue.add(start);
        while (queue.peek() != null){
            ans++;
            if (queue.peek().getX() != 0){
                if (this._map[queue.peek().getX()-1][queue.peek().getY()] == oldColor){
                    Pixel2D temp = new Index2D(queue.peek().getX()-1,queue.peek().getY());
                    this._map[temp.getX()][temp.getY()] = new_v;
                    queue.add(temp);
                }
            } else if (_cyclicFlag) {
                if (this._map[this._map.length-1][queue.peek().getY()] == oldColor){
                    Pixel2D temp = new Index2D(this._map.length-1,queue.peek().getY());
                    this._map[temp.getX()][temp.getY()] = new_v;
                    queue.add(temp);
                }
            }
            if (queue.peek().getX() != this._map.length-1){
                if (this._map[queue.peek().getX()+1][queue.peek().getY()] == oldColor){
                    Pixel2D temp = new Index2D(queue.peek().getX()+1,queue.peek().getY());
                    this._map[temp.getX()][temp.getY()] = new_v;
                    queue.add(temp);
                }
            } else if (_cyclicFlag) {
                if (this._map[0][queue.peek().getY()] == oldColor){
                    Pixel2D temp = new Index2D(0,queue.peek().getY());
                    this._map[temp.getX()][temp.getY()] = new_v;
                    queue.add(temp);
                }
            }
            if (queue.peek().getY() != 0){
                if (this._map[queue.peek().getX()][queue.peek().getY()-1] == oldColor){
                    Pixel2D temp = new Index2D(queue.peek().getX(),queue.peek().getY()-1);
                    this._map[temp.getX()][temp.getY()] = new_v;
                    queue.add(temp);
                }
            } else if (_cyclicFlag) {
                if (this._map[queue.peek().getX()][this._map[0].length-1] == oldColor){
                    Pixel2D temp = new Index2D(queue.peek().getX(),this._map[0].length-1);
                    this._map[temp.getX()][temp.getY()] = new_v;
                    queue.add(temp);
                }
            }
            if (queue.peek().getY() != this._map[0].length-1){
                if (this._map[queue.peek().getX()][queue.peek().getY()+1] == oldColor){
                    Pixel2D temp = new Index2D(queue.peek().getX(),queue.peek().getY()+1);
                    this._map[temp.getX()][temp.getY()] = new_v;
                    queue.add(temp);
                }
            } else if (_cyclicFlag) {
                if (this._map[queue.peek().getX()][0] == oldColor){
                    Pixel2D temp = new Index2D(queue.peek().getX(),0);
                    this._map[temp.getX()][temp.getY()] = new_v;
                    queue.add(temp);
                }
            }
            queue.remove();
        }
        return ans;
	}

	@Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
        int[][] pathMap = this.allDistance(p1, obsColor).getMap();
        if (pathMap[p1.getX()][p1.getY()] == -1)
            return null;
        int p = pathMap[p2.getX()][p2.getY()]+1;
        Pixel2D temp = new Index2D(p2);
        Pixel2D[] ans = new Pixel2D[p];
        ans[p-1] = new Index2D(p2);
        p--;
        while (ans[0] == null){
            if (temp.getX() != 0){
                if (pathMap[temp.getX()-1][temp.getY()] == pathMap[temp.getX()][temp.getY()]-1){
                    ans[p-1] = new Index2D(temp.getX()-1, temp.getY());
                    temp = ans[p-1];
                    p--;
                    continue;
                }
            } else if (_cyclicFlag) {
                if (pathMap[pathMap.length-1][temp.getY()] == pathMap[temp.getX()][temp.getY()]-1){
                    ans[p-1] = new Index2D(pathMap.length-1, temp.getY());
                    temp = ans[p-1];
                    p--;
                    continue;
                }
            }
            if (temp.getX() != pathMap.length-1){
                if (pathMap[temp.getX()+1][temp.getY()] == pathMap[temp.getX()][temp.getY()]-1) {
                    ans[p-1] = new Index2D(temp.getX() + 1, temp.getY());
                    temp = ans[p-1];
                    p--;
                    continue;
                }
            } else if (_cyclicFlag) {
                if (pathMap[0][temp.getY()] == pathMap[temp.getX()][temp.getY()]-1){
                    ans[p-1] = new Index2D(0, temp.getY());
                    temp = ans[p-1];
                    p--;
                    continue;
                }
            }
            if (temp.getY() != 0){
                if (pathMap[temp.getX()][temp.getY()-1] == pathMap[temp.getX()][temp.getY()]-1) {
                    ans[p-1] = new Index2D(temp.getX(),temp.getY()-1);
                    temp = ans[p-1];
                    p--;
                    continue;
                }
            } else if (_cyclicFlag) {
                if (pathMap[temp.getX()][pathMap[0].length-1] == pathMap[temp.getX()][temp.getY()]-1){
                    ans[p-1] = new Index2D(temp.getX(),pathMap[0].length-1);
                    temp = ans[p-1];
                    p--;
                    continue;
                }
            }
            if (temp.getY() != pathMap[0].length-1){
                if (pathMap[temp.getX()][temp.getY()+1] == pathMap[temp.getX()][temp.getY()]-1){
                    ans[p-1] = new Index2D(temp.getX(),temp.getY()+1);
                    temp = ans[p-1];
                    p--;
                    continue;
                }
            } else if (_cyclicFlag) {
                if (pathMap[temp.getX()][0] == pathMap[temp.getX()][temp.getY()]-1){
                    ans[p-1] = new Index2D(temp.getX(),0);
                    temp = ans[p-1];
                    p--;
                    continue;
                }
            }
        }
        return ans;
	}
	@Override
	/////// add your code below ///////
	public boolean isInside(Pixel2D p) {
        try {
            int temp = this._map[p.getX()][p.getY()];
        } catch (NullPointerException error) {
            return false;
        }
        return true;
    }
	@Override
	/////// add your code below ///////
	public boolean isCyclic() {
		return _cyclicFlag;
	}
	@Override
	/////// add your code below ///////
	public void setCyclic(boolean cy) {_cyclicFlag = cy;}
	@Override
	/////// add your code below ///////
	public Map2D allDistance(Pixel2D start, int obsColor) {
        int[][] pathMap = this.getMap();
        for (int i = 0; i < pathMap.length; i++) {
            for (int j = 0; j < pathMap[0].length; j++) {
                if (pathMap[i][j] == obsColor)
                    pathMap[i][j] = -1;
                else
                    pathMap[i][j] = -2;
            } //making a new empty map with -1 representing obstacles
        }
        Queue<Pixel2D> queue = new LinkedList<Pixel2D>();
        queue.add(start);
        pathMap[start.getX()][start.getY()] = 0;
        while (queue.peek() != null){
            if (queue.peek().getX() != 0){
                if (pathMap[queue.peek().getX()-1][queue.peek().getY()] == -2){
                    Pixel2D temp = new Index2D(queue.peek().getX()-1,queue.peek().getY());
                    pathMap[temp.getX()][temp.getY()] = pathMap[queue.peek().getX()][queue.peek().getY()]+1;
                    queue.add(temp);
                }
            } else if (_cyclicFlag) {
                if (pathMap[pathMap.length-1][queue.peek().getY()] == -2){
                    Pixel2D temp = new Index2D(pathMap.length-1,queue.peek().getY());
                    pathMap[temp.getX()][temp.getY()] = pathMap[queue.peek().getX()][queue.peek().getY()]+1;
                    queue.add(temp);
                }
            }
            if (queue.peek().getX() != pathMap.length-1){
                if (pathMap[queue.peek().getX()+1][queue.peek().getY()] == -2){
                    Pixel2D temp = new Index2D(queue.peek().getX()+1,queue.peek().getY());
                    pathMap[temp.getX()][temp.getY()] = pathMap[queue.peek().getX()][queue.peek().getY()]+1;
                    queue.add(temp);
                }
            } else if (_cyclicFlag) {
                if (pathMap[0][queue.peek().getY()] == -2){
                    Pixel2D temp = new Index2D(0,queue.peek().getY());
                    pathMap[temp.getX()][temp.getY()] = pathMap[queue.peek().getX()][queue.peek().getY()]+1;
                    queue.add(temp);
                }
            }
            if (queue.peek().getY() != 0){
                if (pathMap[queue.peek().getX()][queue.peek().getY()-1] == -2){
                    Pixel2D temp = new Index2D(queue.peek().getX(),queue.peek().getY()-1);
                    pathMap[temp.getX()][temp.getY()] = pathMap[queue.peek().getX()][queue.peek().getY()]+1;
                    queue.add(temp);
                }
            } else if (_cyclicFlag) {
                if (pathMap[queue.peek().getX()][pathMap[0].length-1] == -2){
                    Pixel2D temp = new Index2D(queue.peek().getX(),pathMap[0].length-1);
                    pathMap[temp.getX()][temp.getY()] = pathMap[queue.peek().getX()][queue.peek().getY()]+1;
                    queue.add(temp);
                }
            }
            if (queue.peek().getY() != pathMap[0].length-1){
                if (pathMap[queue.peek().getX()][queue.peek().getY()+1] == -2){
                    Pixel2D temp = new Index2D(queue.peek().getX(),queue.peek().getY()+1);
                    pathMap[temp.getX()][temp.getY()] = pathMap[queue.peek().getX()][queue.peek().getY()]+1;
                    queue.add(temp);
                }
            } else if (_cyclicFlag) {
                if (pathMap[queue.peek().getX()][0] == -2){
                    Pixel2D temp = new Index2D(queue.peek().getX(),0);
                    pathMap[temp.getX()][temp.getY()] = pathMap[queue.peek().getX()][queue.peek().getY()]+1;
                    queue.add(temp);
                }
            }
            queue.remove();
        }
        for (int i = 0; i < pathMap.length; i++) {
            for (int j = 0; j < pathMap[0].length; j++) {
                if (pathMap[i][j] == -2)
                    pathMap[i][j] = -1;
            }
        }
        return new Map(pathMap);
	}
   //------------------------Extra functions-----------------------------
    /**This function checks if a given 2D array can be used as a map with 2 criteria:
     * 1) the width and height have to be at least 1.
     * 2) all rows have to be the same length and all columns have to be the same length
     * @param map
     * @return true if the 2D array is valid to be a map.
     */
    private static boolean validMap(int[][] map){
        try{
            int temp = map[0].length;
            for (int i = 1; i < map.length; i++){ //1x3 maps are allowed and in those cases the array cant be unequal in lengths
                if ((temp == 0)||(map[i].length == 0))
                    throw new RuntimeException("Array length is 0");
                if (map[i].length != temp)
                    throw new RuntimeException("Array length is inconsistent (map is not a rectangle)");
            }
            return true;
        }
        catch (NullPointerException error){
            throw new RuntimeException("PacMan.Map contains an empty array");
        }
    }
}
