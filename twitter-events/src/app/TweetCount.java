package app;

import java.util.Date;

public class TweetCount {
	public final Date date;
	public final int count;
	
	public TweetCount(Date _date, int _count){
		date = _date;
		count = _count;
	}
	
	public String toString(){
		return date.toString() + " , " + Integer.toString(count);
	}
}
