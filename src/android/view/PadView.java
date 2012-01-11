package android.view;


import java.io.InputStream;

import com.wellscs.jstroke.RawStroke;
import com.wellscs.jstroke.StrokeScorer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Drawing Pad Widget.<br>
 * <br>
 * 
 * Copyright (c) 2010 Abraham Macias Paredes <br>
 * <br>
 * 
 * GNU GPL v3.<br>
 * ... y al mismo que me condena,<br>
 * colgar� de alguna antena,<br>
 * quiz�; en su propio nav�o.<br>
 * (Jos� de Espronceda)<br>
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
public class PadView extends View {
	/** Maximum number of strokes */
	final public static int MAX_STROKES=32;
	
	/** Radius of the drawed circle (in pixels) */
	final public static int CIRCLE_RADIUS=2;
	
	/** Distance from the clicked point (in pixels) */
	final public static int PAINTBRUSH_DIST=50;
	
	/** Width of the paintbrush (in pixels) */
	final public static int PAINTBRUSH_WIDTH = 32;
	
	/** Point factor */
	final public static int POINT_FACTOR=(PAINTBRUSH_DIST-30);
	
	/** Strokes array */
	protected RawStroke strokes[] = new RawStroke[MAX_STROKES];
	
	/** Current stroke number */
	protected int currentStroke;

	/** Strokes dictionaries */
	protected static String stroke_dicts[][];

	/** Previous point */
	protected Point previousPoint = new Point();
	
	/** Paintbrush point */
	protected Point paintbrushPoint = new Point();
	
	/** Black paint */
	protected Paint blackPaint = new Paint();
	
	/** Drawing bitmap */
	protected Bitmap drawBitmap;
	
	/** Drawing canvas */
	protected Canvas drawCanvas;
	
	/** Paintbrush */
	private static BitmapDrawable paintbush;
	
	/** Constructor */
	public PadView(Context context) {
		super(context);
		// Log.d("PadView", "Constructor called!");
		init();
	}
	
	/** Constructor */
	public PadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// Log.d("PadView", "Constructor called!");
		init();
	}

	/** Constructor */
	public PadView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// Log.d("PadView", "Constructor called!");
		init();
	}

	public void init() {
		previousPoint.x = -1;
		previousPoint.y = -1;
		
		blackPaint.setColor(Color.BLACK);
		currentStroke = 0;
		strokes[currentStroke] = new RawStroke();
		
	}
	

	/**
	 * Touch event.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Point point = new Point();

		paintbrushPoint.x = (int) event.getX();
		paintbrushPoint.y = ((int) event.getY()) - PAINTBRUSH_DIST;
		point.x = (int) event.getX();
		point.y = ((int) event.getY()) - POINT_FACTOR;
		
		addPoint(point);
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
//			Log.d("PadView.onTouchEvent", "Action Down! ("+event.getX()+","+event.getY()+")");
			break;
			
		case MotionEvent.ACTION_MOVE:
//			Log.d("PadView.onTouchEvent", "Action move! ("+event.getX()+","+event.getY()+")");
			break;

		case MotionEvent.ACTION_UP:
//			Log.d("PadView.onTouchEvent", "Action up! ("+event.getX()+","+event.getY()+")");
			paintbrushPoint.x = -1;
			paintbrushPoint.y = -1;	
			
			if (currentStroke < MAX_STROKES) {
				currentStroke++;
				strokes[currentStroke] = new RawStroke();
			}
			
			previousPoint.x = -1;
			previousPoint.y = -1;
			
			break;
		default:
			// Log.d("PadView.onTouchEvent", "Default!");
			
		
		}
		
		
		return super.onTouchEvent(event);
	}

	/**
	 * Adds a new point to the stroke.
	 * @param point Point to add.
	 */
	protected void addPoint(Point point) {
		if (currentStroke < MAX_STROKES) {
			if (strokes[currentStroke].getLenght()<RawStroke.MAX_XY_PAIRS) {
			
				drawLine(previousPoint, point);
			
				strokes[currentStroke].addPoint(point.x, point.y); 
				previousPoint.x = point.x;
				previousPoint.y = point.y;
			}
		}		
	}
	
	/**
	 * Draws a line from point 0 to point 1.
	 * @param point0 Point.
	 * @param point1 Point.
	 */
	protected void drawLine(Point point0, Point point1) {
		
		if (point0!=null && point1!=null) {
			if (point0.x<0 || point0.y<0) {
				// Draw a circle
				drawCanvas.drawCircle(point1.x, point1.y, CIRCLE_RADIUS, blackPaint);
				this.invalidate(new Rect(point1.x-CIRCLE_RADIUS,
						point1.y-PAINTBRUSH_DIST,
						point1.x+PAINTBRUSH_WIDTH,
						point1.y+CIRCLE_RADIUS));					
			} else {
				int x0, x1, y0, y1;
				
				x0 = point0.x;
				x1 = point1.x;
				y0 = point0.y;
				y1 = point1.y;
				
			
				int dx = x1 - x0;
				int dy = y1 - y0;

				drawCanvas.drawCircle(x0, y0, CIRCLE_RADIUS, blackPaint);
				if (dx > dy) {
					float m = (float) dy / (float) dx;
				    float b = y0 - m*x0;
				    if(x1 > x0)
				      dx = 1;
				    else
				      dx = -1;
				    
				    while (x0 != x1) {
				      x0 += dx;
				      y0 = Math.round(m*x0 + b);
				      drawCanvas.drawCircle(x0, y0, CIRCLE_RADIUS, blackPaint);
				   }
				} else {
					float m = (float) dx / (float) dy;
				    float b = x0 - m*y0;
				    if(y1 > y0)
				      dy = 1;
				    else
				      dy = -1;
				    
				    while (y0 != y1) {
				      y0 += dy;
				      x0 = Math.round(m*y0 + b);
				      drawCanvas.drawCircle(x0, y0, CIRCLE_RADIUS, blackPaint);
				   }					
				}
				
				x0 = point0.x;
				x1 = point1.x;
				y0 = point0.y;
				y1 = point1.y;
				
				if (x0>x1) {
					dx = x0;
					x0 = x1;
					x1 = dx;
				}
		
				if (y0>y1) {
					dy = y0;
					y0 = y1;
					y1 = dy;
				}				
				
				y0 -= PAINTBRUSH_DIST;
				x1 += PAINTBRUSH_WIDTH;
				
				this.invalidate(new Rect(x0-CIRCLE_RADIUS,
						y0-CIRCLE_RADIUS,
						x1+2*CIRCLE_RADIUS,
						y1+3*CIRCLE_RADIUS));	
				
				
			}
			
			
		}
	}
	
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
//		Log.d("PadView","Draw!");
		
		if (drawBitmap == null || drawCanvas == null) {
			drawBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
			drawCanvas = new Canvas(drawBitmap);
		}
		
		canvas.drawBitmap(drawBitmap, drawCanvas.getMatrix(), blackPaint);
		if (paintbrushPoint.x>0 && paintbrushPoint.y>0 && paintbush!=null) {
			canvas.drawBitmap(paintbush.getBitmap(), paintbrushPoint.x, paintbrushPoint.y, blackPaint);

		}
		
		
	}
	
	/** Clear the PadView */
	public void clear() {
		// Clear the strokes
		currentStroke = 0;
		previousPoint.x = -1;
		previousPoint.y = -1;
		paintbrushPoint.x = -1;
		paintbrushPoint.y = -1;		
		for (int i=0; i<MAX_STROKES; i++) {
			strokes[i] = null;
		}
		strokes[currentStroke] = new RawStroke();
		
		
		// Clear the canvas
		drawBitmap = null;
		drawCanvas = null;
		drawBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(drawBitmap);		
		
		
		this.invalidate();
	}
	
	/** Search for the drawed kanji */
	public char[] search() {
		String str = processStrokes();
		// Log.d("PadView", "processStrokes="+str);
		
		return str.toCharArray();
	}	
	
	/** Release the dictionaries */
	public static void releaseDictionaries() {
		stroke_dicts = null;
	}
	
	/** Inits the dictionaries */
	public static synchronized void initDictionaries(InputStream dataFile) {
		int i;

		
		// Inits the dictionaries to NULL
		stroke_dicts = new String[MAX_STROKES][];
		for (i=0;i<MAX_STROKES;i++)
			stroke_dicts[i] = null;
					
		// Open the data file
		try {
			int totalKanjis;
			
			totalKanjis = 0;
			for (;;) {
								
				
				int nstrokes = 0;
				String line;
				
				nstrokes = readInt(dataFile);
				if (nstrokes > MAX_STROKES)	{
					Log.e("PadView", "ERROR: Corrupt stroke database!");
					break;
				}				
				if (nstrokes == 0)
					break;
				
				line = readLine(dataFile);
				
				stroke_dicts[nstrokes] = line.split("///");
				
				//Log.d("PadView", "initDictionaries: Strokes="+nstrokes
				//		+" NKanji="+stroke_dicts[nstrokes].length);
				totalKanjis += stroke_dicts[nstrokes].length;



			}	
			
			// Log.d("PadView", "Total kanjis="+totalKanjis);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

	}
	
	
	
	
	protected static int readInt(InputStream is) {
		int res = 0;
		int c;
		String str="";
		try {
			c = is.read();
			
			while (c!='\t' && c>=0) {
				str += (char) c;
				c = is.read();	
			}
			
			res = Integer.parseInt(str);
			
		} catch (Exception ex) {
			Log.e("PadView", "ERROR: Error reading a number!");
		}
		
		
		return res;
	}
	
	
	protected static String readLine(InputStream is) {
		String res = null;
		int c;
		byte buf[] = new byte[10240];
		int index;
		
		try {
			c = is.read();
			index = 0;
			
			while (c!='\n' && c>=0) {
				buf[index++] = (byte) c;
				c = is.read();	
			}
			
			res = new String(buf, 0, index, "UTF-8");
			
		} catch (Exception ex) {
			Log.e("PadView", "ERROR: Error reading a string!");
		}
		
		
		return res;
	}	
	
	/**
	 * Processes the drawed strokes and return the string with the possibilities.
	 * @return String with the possibilities.
	 */
	protected String processStrokes() {
		String str = "";
		if (currentStroke != 0 && stroke_dicts[currentStroke]!=null) {
			// Log.d("PadView", "CurrentStroke: "+currentStroke);
			
			StrokeScorer scorer = new StrokeScorer(stroke_dicts[currentStroke],
				strokes, currentStroke);
			
			scorer.process();
			str = scorer.topPicks();
	

		}
		return str;
		
	}

	public static BitmapDrawable getPaintbush() {
		return paintbush;
	}

	public static void setPaintbush(BitmapDrawable paintbush) {
		PadView.paintbush = paintbush;
	}
	
	
	
	
}
