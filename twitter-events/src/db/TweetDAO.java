package db;

import app.TweetCount;

import java.util.ArrayList;


public interface TweetDAO {
	public int getTweetCount();
	public ArrayList<TweetCount> getTweetCountByKeyword(String Keyword);
	public ArrayList<TweetCount> getTweetCountByKeyword(String Keyword, String startDate, String endDate);
}
