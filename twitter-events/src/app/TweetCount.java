package app;

import java.util.Date;

/**
 * Container for date+time and the number of tweets in that hour
 * 
 * @author Chandan Yeshwanth
 *
 */
public class TweetCount {
	public final Date date;
	public final int count;
	
	/**
	 * @param _date date and time of occurrence
	 * @param _count number of tweets at this time
	 */
	public TweetCount(Date _date, int _count){
		date = _date;
		count = _count;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return date.toString() + " , " + Integer.toString(count);
	}
}
