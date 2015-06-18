package db;

import app.TweetCount;

import java.util.ArrayList;


/**
 * A data access object (DAO) to query Twitter data
 * 
 * @author Chandan Yeshwanth
 *
 */
public interface TweetDAO {
	/**
	 * @return the total number of tweets in the table
	 */
	public int getTweetCount();
	/**
	 * @param Keyword the term that the tweet must contain
	 * @return the number of tweets grouped by minute 
	 */
	public ArrayList<TweetCount> getTweetCountByKeyword(String Keyword);
	/**
	 * @param Keyword the term that the tweet must contain
	 * @param startDate start date and time for twitter data
	 * @param endDate end date and time 
	 * @return
	 */
	public ArrayList<TweetCount> getTweetCountByKeyword(String Keyword, String startDate, String endDate);
}
