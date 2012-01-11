package com.wellscs.jstroke;


/**
 * RAW stroke information took from the mouse movements.<br>
 * <br>
 * 
 * Copyright (c) 2010 Abraham Macias Paredes <br>
 * <br>
 * 
 * GNU GPL v3.<br>
 * ... y al mismo que me condena,<br>
 * colgaré de alguna antena,<br>
 * quizá; en su propio navío.<br>
 * (José de Espronceda)<br>
 * <br>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.<br>
 * <br>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.<br>
 * <br>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.<br>
 * <br>
 * Derived from prior work by Robert E. Wells on JStroke.<br>
 * Derived from prior work by Todd David Rudick on JavaDict and StrokeDic.<br>
 * Makes use of KANJIDIC data from Jim Breen of Monash University.<br>
 * 
 * @author Abraham Macias Paredes
 * 
 */
public class RawStroke {
	/** Limit list to what we can afford */
	public final static int MAX_LIST_COUNT = 5;
	
	/** Maximum pairs of coordinates in a stroke */
	public final static int MAX_XY_PAIRS = 256;
	
	/** Number of pairs of coordinates in the stroke */
	protected int len;
	
	/** X coordinates */
	protected int[] x; 
	
	/** Y coordinates */
	protected int[] y; 	
	
	/** Default constructor */
	public RawStroke() {
		super();
		
		this.len = 0;
		this.x = new int[MAX_XY_PAIRS];
		this.y = new int[MAX_XY_PAIRS];
	}
	
	/** Adds a point to the stroke.
	 * 
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	public void addPoint(int x, int y) {
		this.x[len] = x;
		this.y[len] = y;
		len++;
	}
	
	/**
	 * Gets the X coordinate of a point.
	 * @param point Point number.
	 * @return X coordinate.
	 */
	public int getXCoordinate(int point) {
		return this.x[point];
	}
	
	/**
	 * Gets the Y coordinate of a point.
	 * @param point Point number.
	 * @return Y coordinate.
	 */
	public int getYCoordinate(int point) {
		return this.y[point];
	}
	
	/**
	 * Gets the lenght (in number of points) of the stroke.
	 * @return lenght.
	 */
	public int getLenght() {
		return this.len;
	}
	
}
