package app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Utility {
	public static double getIDF(int total, int number){
		return Math.log(total / number);
	}
	
	public static HashMap<String, Object> getBaseMapFromList(ArrayList<TweetCount> baseSeries){
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		DateFormat df = new SimpleDateFormat("HH:mm");
		
		for(TweetCount tc : baseSeries){
			resultMap.put(df.format(tc.date), tc.count);
		}
		
		return resultMap;
	}
}
