package app;

import java.util.ArrayList;
import java.util.HashMap;

import org.omg.CosNaming._BindingIteratorImplBase;

/**
 * This class is used to contain Twitter count data related to one event
 * 
 * Eg:
 * 	eventName  - nepal_quake
 *  dataSeries - "nepal" -> (10:00->100, 11:00->150 ...),
 *  				 "quake" -> (10:00->90,  11:00->120 ...)
 *  baseSeries - (10:00->1000,  11:00->1200 ...)
 * 
 * @author internship
 *
 */
public class EventData {
	public String eventName;
	//	Object is an ArrayList of tweetcount objects
	public HashMap<String, Object> dataSeries;
	//	total number of tweets counted per minute
	public ArrayList<TweetCount> baseSeries;

	/**
	 * @param _eventName
	 * @param _dataSeries
	 * @param _baseSeries
	 */
	public EventData(String _eventName, HashMap<String, Object> _dataSeries, 
			ArrayList<TweetCount> _baseSeries) {
		this.eventName = _eventName;
		this.dataSeries = _dataSeries;
		this.baseSeries = _baseSeries;
	}
}
