package app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * 
 * @author Chandan Yeshwanth
 *
 */
public class Utility {
	
	/**
	 * @param total Total occurrence count
	 * @param number Occurence count of the required keyword/event
	 * @return IDF (Inverse Document Frequency) value
	 */
	public static double getIDF(int total, int number){
		return Math.log(total / number);
	}
	
	/**
	 * @param baseSeries A list of TweetCount objects
	 * @return HashMap of the same objects 
	 */
	public static HashMap<String, Object> getBaseMapFromList(ArrayList<TweetCount> baseSeries){
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		DateFormat df = new SimpleDateFormat("HH:mm");
		
		for(TweetCount tc : baseSeries){
			resultMap.put(df.format(tc.date), tc.count);
		}
		
		return resultMap;
	}
}
