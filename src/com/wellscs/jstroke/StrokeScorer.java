package com.wellscs.jstroke;

import android.util.Log;


/**
 * The handwriting recognition engine.<br>
 * Adapted from "scoring.c" from JStroke software.<br>
 * Because is better structured than JDict (in my personal opinion).<br>
 *
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

public class StrokeScorer {
	protected final long ANG_COST_BASE = 52;
	protected final long ANG_COST_SCALE = 98;
	protected final long HUGE_COST = ((24 * ANG_COST_SCALE) + ANG_COST_BASE)*100;
	
	protected final long MAX_SCORE_TO_SQUARE = 0xffff;
	protected final long MAX_SCORE_SQUARED = MAX_SCORE_TO_SQUARE * MAX_SCORE_TO_SQUARE;
	
	
	protected final int DATA_BUF_LEN = 16;
	
	
	/** Stroke dictionary */
	protected String[] strokeDic; //list of string, first char in each string is the kanji
	
	/** Raw strokes */
	protected RawStroke[] rawStrokes;
	
	/** Stroke count */
	protected int strokeCount;
	
	/** Score items */
	protected ScoreItem[] scoreItems;
	
	/** Score length */
	protected int scoreLength;
	
	/** path data */
	protected byte[] pathData;
	
	/**
	 * Creates a StrokeScorer object.
	 */
	public StrokeScorer(String[] strokeDic, RawStroke[] rawStrokes, int strokeCount) {
		int i;
		this.strokeDic = strokeDic;
		this.rawStrokes = rawStrokes;
		this.strokeCount = strokeCount;
		this.scoreLength = 0;
		
		this.scoreItems = new ScoreItem[RawStroke.MAX_LIST_COUNT];
		for (i=0; i<RawStroke.MAX_LIST_COUNT; i++) {
			this.scoreItems[i] = new ScoreItem();
		}
		this.pathData = new byte[DATA_BUF_LEN];
	}
	
	public boolean isInDict(char k){
		for (String s : strokeDic){
			if (s.charAt(0) == k){
				return true;
			}
		}
		return false;
	}
	
	/** Process the database entries in order to find a matching kanji */
	public void process() {
		int index;
		String cp;
		long score;
		int scrIndx, scrIndx2;
		
		/* Evaluate all the items in cpStrokeDic against Context,
		 * and update ScoreItems list as we go.
		 */
		index = 0;
		for (index=0; index<strokeDic.length; index++) {
			cp = strokeDic[index];
			
			score = evalItem(cp.toCharArray());
			//Log.d("StrokeScorer", "process: Score="+score + " item="+cp);

			for (scrIndx = scoreLength-1; scrIndx>=0; scrIndx--) {
				if (score >= scoreItems[scrIndx].score) {
					break;
				}
			}
			scrIndx++;

			/* If we have a top score, lets register it. */
			if (scrIndx < RawStroke.MAX_LIST_COUNT) {

				/* Increase the score list length if it isn't full yet. */
				if (scoreLength < RawStroke.MAX_LIST_COUNT)
					scoreLength++;

				/* Push down all lower scores in the list to make room. */
				for (scrIndx2 = scoreLength-2; scrIndx2>=scrIndx; scrIndx2--) {
					scoreItems[scrIndx2+1].score = scoreItems[scrIndx2].score;
					scoreItems[scrIndx2+1].item = scoreItems[scrIndx2].item;
					
				}


				/* Actually store our info in the list. */
				scoreItems[scrIndx].score = score;
				scoreItems[scrIndx].item = cp;
				//Log.d("StrokeScorer", "index="+scrIndx);
				
			}

		} /* for each stroke description... */
		
	}
	
	/**
	 * Evals the grade of matching of a dictionary entry.
	 * @param dictionaryEntry Entry of the dictionary.
	 * @return score.
	 */
	protected long evalItem(char[] dictionaryEntry) {
		long score;
		int index;
		int mdIndex;
		int stroke;
		RawStroke rawStroke;
		long thisScore;
		
		score = 0;
		index = 1; // Skip the KANJI character.
		mdIndex = 0;
				
		for (stroke = 0; stroke < this.strokeCount; stroke++) {
			mdIndex = 0;
			
			if (index>=dictionaryEntry.length 
					|| dictionaryEntry[index]<'A' || dictionaryEntry[index]>'M') {
				break;
			}
			
			switch (dictionaryEntry[index]) {		/* Break out on first char value... */
			case 'A':			/* TDR='1' CLK=07:30 DEG=225 */
				pathData[mdIndex++] = 20;
				break; 
			case 'B':			/* TDR='2' CLK=06:00 DEG=180 */
				pathData[mdIndex++] = 16; 
				break; 
			case 'C':			/* TDR='3' CLK=04:30 DEG=135 */
				pathData[mdIndex++] = 12; 
				break; 
			case 'D':			/* TDR='4' CLK=09:00 DEG=270 */
				pathData[mdIndex++] = 24; break; 
			case 'F':			/* TDR='6' CLK=03:00 DEG=090 */
				pathData[mdIndex++] =  8; 
				break; 
			case 'G':			/* TDR='7' CLK=10:30 DEG=315 */
				pathData[mdIndex++] = 28; 
				break; 
			case 'H':			/* TDR='8' CLK=12:00 DEG=360 */
				pathData[mdIndex++] =  0; 
				break; 
			case 'I':			/* TDR='9' CLK=01:30 DEG=045 */
				pathData[mdIndex++] =  4; 
				break; 
			case 'J':			/* TDR='x' down   06:00 then 07:30 */
				pathData[mdIndex++] = 16; 
				pathData[mdIndex++] = 20; 
				break; 
			case 'K':			/* TDR='y' down   06:00 then 04:30 */
				pathData[mdIndex++] = 16; 
				pathData[mdIndex++] = 12; 
				break; 
			case 'L':			/* TDR='c' down   06:00 then 03:00 */
				pathData[mdIndex++] = 16; 
				pathData[mdIndex++] =  8; 
				break; 
			case 'M':			/* TDR='b' across 03:00 then 06:00 */
				pathData[mdIndex++] =  8; 
				pathData[mdIndex++] = 16; 
				break; 
			} 
		
			index++;
			while (index<dictionaryEntry.length 
					&& dictionaryEntry[index]>='a' && dictionaryEntry[index]<='m') {
				switch (dictionaryEntry[index]) {		/* Break out on first char value... */
				case 'a':			/* TDR='1' CLK=07:30 DEG=225 */
					pathData[mdIndex++] = 20;
					break; 
				case 'b':			/* TDR='2' CLK=06:00 DEG=180 */
					pathData[mdIndex++] = 16; 
					break; 
				case 'c':			/* TDR='3' CLK=04:30 DEG=135 */
					pathData[mdIndex++] = 12; 
					break; 
				case 'e':			/* TDR='4' CLK=09:00 DEG=270 */
					pathData[mdIndex++] = 24; break; 
				case 'f':			/* TDR='6' CLK=03:00 DEG=090 */
					pathData[mdIndex++] =  8; 
					break; 
				case 'g':			/* TDR='7' CLK=10:30 DEG=315 */
					pathData[mdIndex++] = 28; 
					break; 
				case 'h':			/* TDR='8' CLK=12:00 DEG=360 */
					pathData[mdIndex++] =  0; 
					break; 
				case 'i':			/* TDR='9' CLK=01:30 DEG=045 */
					pathData[mdIndex++] =  4; 
					break; 
				case 'j':			/* TDR='x' down   06:00 then 07:30 */
					pathData[mdIndex++] = 16; 
					pathData[mdIndex++] = 20; 
					break; 
				case 'k':			/* TDR='y' down   06:00 then 04:30 */
					pathData[mdIndex++] = 16; 
					pathData[mdIndex++] = 12; 
					break; 
				case 'l':			/* TDR='c' down   06:00 then 03:00 */
					pathData[mdIndex++] = 16; 
					pathData[mdIndex++] =  8; 
					break; 
				case 'm':			/* TDR='b' across 03:00 then 06:00 */
					pathData[mdIndex++] =  8; 
					pathData[mdIndex++] = 16; 
					break; 
				} 				
				
				index++;
			}
		
			rawStroke = this.rawStrokes[stroke];
			
			thisScore = scoreRawStroke(rawStroke, 0, rawStroke.getLenght(), pathData, 0, mdIndex, 0);
//			Log.d("StrokeScorer", "thisScore="+thisScore+" stroke="+stroke+" mdIndex="+mdIndex);
			
			if (thisScore >= MAX_SCORE_TO_SQUARE)
				thisScore = MAX_SCORE_SQUARED;
			else
				thisScore = (thisScore * thisScore);

			if (score >= (MAX_SCORE_SQUARED - thisScore))
				score = MAX_SCORE_SQUARED;
			else
				score += thisScore;		
		}
		
		score = (long) Math.sqrt(score);

//		Log.d("StrokeScorer", "Antes de extrafilters="+score);
	    /* Handle optional extra filters... may modify *ipScore, updates cp. */
		if (index<dictionaryEntry.length &&  dictionaryEntry[index] == '|') {
			score = extraFilters(dictionaryEntry, index, score);
		}

		if (stroke != this.strokeCount) {
			Log.e("StrokeScorer", "JStrokeDic miscount");
		}
		
		return score;
	}
	
	
	/**
	 * Calculates the score of a rawstroke for a matching data.
	 * @param rawStroke Rawstroke.
	 * @param pathData Path data array.
	 * @param mdLen Lenght of the Matching data array.
	 * @return Score of the stroke.
	 */
	protected long scoreRawStroke(RawStroke rawStroke, int rsBegin, int rsEnd, byte[] pathData, int pathBegin, int pathEnd,int depth) {
		long iScore;
		long iDifX, iDifY;
		int iMid;
		long iAng32, iPath32, iDif32;
		int iPathMid, iStep;
		int iPathLen, iLen;
		long iThisScore;
		int iDiv;
		
		iScore = 0;
		iPathLen = pathEnd-pathBegin;
		iLen = rsEnd-rsBegin;
		
		if (iLen < 2 || iPathLen < 1)
			return HUGE_COST;

		if (iPathLen == 1) {
			iDifX = rawStroke.getXCoordinate(rsEnd-1) - rawStroke.getXCoordinate(rsBegin);
			iDifY = rawStroke.getYCoordinate(rsBegin) - rawStroke.getYCoordinate(rsEnd-1); /* Flip from display to math axes. */

			
			if (iDifX == 0 && iDifY == 0) /* Two samples at same place... */
				return HUGE_COST;

			/* Subdivide recursively while stroke is long and depth is shallow.
			 * $$$ These values are pretty magic... review later. -rwells, 970719.
			 * TDR used 20*20... -rwells, 970719.
			 */
			if ((iDifX*iDifX + iDifY*iDifY) > (20*20) && iLen > 5 && depth < 4) {

				iMid = (iLen >> 1);

				/* Note that we use the middle point on both sides... */
				iScore = scoreRawStroke(rawStroke, rsBegin, rsBegin+iMid+1, pathData, pathBegin, pathEnd, depth+1);
//				Log.d("StrokeScorer", "(1/1) iScore="+iScore);
				iScore += scoreRawStroke(rawStroke, rsBegin+iMid, rsBegin+iLen, pathData, pathBegin, pathEnd, depth+1);
//				Log.d("StrokeScorer", "(1/2) iScore="+iScore);

				return (iScore >> 1);

			} /* End if stroke is long, and depth is shallow... */

			/* Time to score this segment against desired direction. */
			
			iAng32 = angle32(iDifX, iDifY);			

			iPath32 = pathData[pathBegin];

			if (iAng32 >= iPath32)
				iDif32 = iAng32 - iPath32;
			else
				iDif32 = iPath32 - iAng32;

/*			
			Log.d("StrokeScorer", "rsBegin="+rsBegin+" rsEnd="+rsEnd
					+" x0="+rawStroke.getXCoordinate(rsEnd-1)
					+" x1="+rawStroke.getXCoordinate(rsBegin)
					+" y0="+rawStroke.getYCoordinate(rsBegin)
					+" y1="+rawStroke.getYCoordinate(rsEnd-1));
			Log.d("StrokeScorer", "iDifX="+iDifX+" iDifY="+iDifY+" iAng32="+iAng32+" iPath32="+iPath32+" ret="+(iDif32 * ANG_COST_SCALE + ANG_COST_BASE));
*/			
			return iDif32 * ANG_COST_SCALE + ANG_COST_BASE;

		} /* end if path len is 1. */
		else {
			iScore = HUGE_COST * iPathLen * 2;
			iPathMid = (iPathLen >> 1);
			
			if (iLen < 20)
				iStep = 1;
			else
				iStep = iLen / 10;

			iDiv = (iLen>>2);
			//Log.d("StrokeScorer", "iLen="+iLen+" iDiv="+iDiv);
			for (iMid = iDiv+1; iMid < iLen-iDiv; iMid += iStep) {
				iThisScore = scoreRawStroke(rawStroke, 0, iMid+1, pathData, pathBegin, pathBegin+iPathMid, depth+1);
				//Log.d("StrokeScorer", "(1/1) iMid="+iMid+" iThisScore="+iThisScore+" B="+pathBegin+" E="+(pathBegin+iPathMid));
				iThisScore += scoreRawStroke(rawStroke, iMid, iLen, pathData, pathBegin+iPathMid, pathEnd, depth+1);
				//Log.d("StrokeScorer", "(1/2) iMid="+iMid+" iThisScore="+iThisScore+" B="+(pathBegin+iPathMid)+" E="+pathEnd);
				
				
				iThisScore >>= 1;

				if (iThisScore < iScore)
					iScore = iThisScore;
				
			}
			
			return iScore;
		} /* end if path len is >1. */

	}
	
	
	/** ----- Angle32 -------------------------------------------------------------<br>
	 * For given int xdif and ydif, calculate atan2 (the angle from origin)
	 * in 32nd's of a circle from 0 to 32, rather than radians.  Note that it 
	 * returns 32 iff xdif and ydif are both zero, an ill-defined case.
	 * Origin and direction are clockwise:
	 * 0 => 12:00, 8 => 3:00, 16 => 6:00, 24 => 9:00.
	 * Why 32nds?  So we can divide them into 8 pieces evenly, and so we get
	 * 4x over-sampling for the 8th's in the path descriptions...
	 * -rwells, 970713.
	 */

	protected long angle32(long xdif, long ydif) {
		boolean xneg, yneg, xyflip;
		long i32nd;
		long xtmp, islope;

	    if ((xneg = (xdif < 0)))
			xdif = -xdif;
		if ((yneg = (ydif < -0.1)))
			ydif = -ydif;
		if ((xyflip = (ydif < xdif))) {
			xtmp = xdif; 
			xdif = ydif; 
			ydif = xtmp;
		}

	    if (xdif == 0) {
			if (ydif == 0)
				return 32;
			else
				i32nd = 0;
		}
	    else {
			/* The 4 comparison values were generated with the accompanying
			 * perl script, then open coded here for speed and reasonable
			 * space efficiency.  The values were chosen to make the results 
			 * match those of atan2 in rounded double
			 * precision floating point.  -rwells, 970713.
			 */

			islope = (100 * xdif) / ydif;
			if (islope < 54) {             /* test #2, first test. */
				if (islope < 10)           /* test #0, second test. */
					i32nd = 0;             /*          got  #0 after 2 tests. */
				else if (islope < 31)      /* test #1, third test. */
					i32nd = 1;             /*          got  #1 after 3 tests. */
				else
					i32nd = 2;             /*          got  #2 after 3 tests. */
			}
			else if (islope < 83)          /* test #3, second test. */
				i32nd = 3;                 /*          got  #3 after 2 tests. */
			else
				i32nd = 4;                 /*          got  #4 after 2 tests. */
		}

		if (xyflip)
			i32nd = (8 - i32nd);
		if (yneg)
			i32nd = (16 - i32nd);
		if (xneg)
			i32nd = (32 - i32nd);

	    return i32nd % 32;
	}

	/**
	 * Applys the extra filters.
	 * @param dictionaryEntry
	 * @param index
	 * @return
	 */
	long extraFilters(char[] dictionaryEntry, int index, long score) {

		char    c;
		char    cArg[] = new char[2];
		int    iStroke[] = new int[2];
		long    iDiff, iVal[] = new long[2];
		int    idx = 0;
		boolean  bMust = false;


		idx = 0;
		cArg[0] = cArg[1] = 0;
		iStroke[0] = iStroke[1] = 0;

		/* Simple parser for Filter strings. assumes a1-b1 structure,
		* where a and b can be any single alphabetic cmd char, the 
		* numbers can be multiple digit, and b1 can optionally be 
		* followed by '!' to insist on the filter passing.  There can
		* be multiple filters but they have to be separated by '!' or
		* space(s).  The filter string is terminated by a null byte
		* or an 8-bit char, the beginning of the next entry.  
		* Leading spaces and trailing spaces are ignored. -rwells, 970722.
		*/

		for (;index<dictionaryEntry.length;index++) {
			c = (char) dictionaryEntry[index];
			switch (c) {
			case 'x': 
			case 'y':
			case 'i':
			case 'j':
			case 'a':
			case 'b':
			case 'l':
				cArg[idx] = c;
				break;

			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				iStroke[idx] = iStroke[idx] * 10 + (c - '0');
				break;

			case '-':				/* Switch to second arg when we see minus. */
				idx = 1;
				break;

			case '!':
				bMust = true;
				/* FALLTHRU */

			case ' ':
			default:

				/* If we are in the second argument, execute and reset. */
				if (idx == 1) {
					iVal[0] = extraEval(cArg[0], iStroke[0]);
					iVal[1] = extraEval(cArg[1], iStroke[1]);
					iDiff = (iVal[0] - iVal[1]);
	
					//Log.d("StrokeScorer", "cArg[0]="+cArg[0]+" iStroke[0]="+iStroke[0]+" cArg[1]="+cArg[1]+" iStroke[1]="+iStroke[1]);
					//Log.d("StrokeScorer", "iDiff="+iDiff+" score="+score+" iVal0="+iVal[0]+" iVal1="+iVal[1]);
					if (iDiff < 0) {
						iDiff = -iDiff;

						if (bMust)
							iDiff = 9999999;

						if (score < (MAX_SCORE_SQUARED-iDiff))
							score += iDiff;
						else
							score = MAX_SCORE_SQUARED;
					}
					else {
						if (score > iDiff)
							score -= iDiff;
						else
							score = 0;
					}
	
	
	
					/* Reset state for next filter... */
					idx = 0;
					bMust = false;
					cArg[0] = cArg[1] = 0;
					iStroke[0] = iStroke[1] = 0;
				}

			} /* end switch on char */
		} /* end for each char in filter spec... */
		return score;
		
	}

	/**
	 * Entra evaluation.
	 * @param cArg
	 * @param iStroke
	 * @return
	 */
	long extraEval(char cArg, int iStroke) {
		long  iVal, iSquared;
		int  iLen;
		RawStroke rsp;

		iStroke--;					/* Formula strokes are 1-based, my stroke
									 * array is 0-based. */

		if (iStroke >= this.strokeCount)
			return 0;				/* Should not happen... */

		rsp = this.rawStrokes[iStroke];
		iLen = rsp.getLenght();

		/* Heretofore we have left the coordinates as we got them, screen
		 * coordinates with 0,0 in upper left, y increasing downward,
		 * x increasing to the right.  And the extra filters agree with this.
		 * -rwells, 970723.
		 *
		 * Removed the subtraction of Rect_x, Rect_y here - it will mess
		 * up debugging messages if the client doesn't subtract them before-
		 * hand, but otherwise should be harmless.
		 *  - OWT, 970903
		 */

		switch(cArg) {
		case 'x':
			return rsp.getXCoordinate(0);
		case 'y':
			return rsp.getYCoordinate(0);
		case 'i':
			return rsp.getXCoordinate(iLen-1);
		case 'j':
			return rsp.getYCoordinate(iLen-1);
		case 'a':
			return ((rsp.getXCoordinate(0) + rsp.getXCoordinate(iLen-1)) >> 1);
		case 'b':
			return (( rsp.getYCoordinate(0) + rsp.getYCoordinate(iLen-1)) >> 1);
		
		case 'l':
			/* These are byte values - they can't get very large,
			 * so don't need to check against diMaxScoreToSquare...
			 */
			iSquared = (((int) rsp.getXCoordinate(0)) - ((int)rsp.getXCoordinate(iLen-1)));
			iVal = iSquared * iSquared;

			iSquared = (((int) rsp.getYCoordinate(0)) - ((int)rsp.getYCoordinate(iLen-1)));
			iVal += iSquared * iSquared;

			return (long) Math.sqrt(iVal);
		}
		
		return 0;					/* Unrecognized cArg cmd char. */
		
	}

	/**
	 * Return the top picks.
	 * @return Kanjis that matches the raw strokes.
	 */
	public String topPicks() {
		String str = "";
		
		if (this.scoreLength > 0) {
			int i;
			for (i=0; i<this.scoreLength; i++) {
				str = str + this.scoreItems[i].item.charAt(0);
				// Log.d("StrokeScorer", "["+i+"]="+this.scoreItems[i].item);
			}
		}
		
		return str;
	}
	
}
