package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

import app.Tweet;
import app.TweetCount;

public class TweetDAO_JDBC implements TweetDAO {
	Connection dbConnection;

	public TweetDAO_JDBC(Connection dbconn){
		dbConnection = dbconn;
	}

	@Override
	public int getTweetCount() {
		String sql = "SELECT COUNT(*) as count FROM event_tweet";
		int count;

		try {
			ResultSet rs = dbConnection.prepareStatement(sql).executeQuery();
			rs.next();
			count = rs.getInt("count");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			count = -1;
		}

		return count;
	}

	@Override
	public ArrayList<TweetCount> getTweetCountByKeyword(String keyword) {
		String sql = 
				"select	"
						+ " TIME(creation_date) as time, COUNT(*) as count "
						+ 	" from tweet "
						+ " where "
						+ 	" LOWER(content) like ? "
						+ " group by "
						+  	" HOUR(creation_date), MINUTE(creation_date) "
						;

		ArrayList<TweetCount> results = new ArrayList<TweetCount>();
		PreparedStatement stmt;

		try {
			stmt = dbConnection.prepareStatement(sql);
			//			add the keyword to the query
			stmt.setString(1, "%" + keyword + "%");

			ResultSet rs = stmt.executeQuery();
			Calendar calendar = Calendar.getInstance();

			while(rs.next()) {
				results.add( new TweetCount(
						rs.getTime("time"),
						rs.getInt("count"))
						);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			results = null;
		}

		return results;
	}

	@Override
	public ArrayList<TweetCount> getTweetCountByKeyword(String keyword,
			String startDate, String endDate) {
		String sql = 
				"select	"
						+ " TIME(creation_date) as time, COUNT(*) as count "
						+ 	" from tweet "
						+ " where "
						+ 	" LOWER(content) like ? "
						+ 	" AND "
						+ 	" creation_date BETWEEN '%s' AND '%s'" 
						+ " group by "
						+  	" HOUR(creation_date), MINUTE(creation_date) "
						;
		
		sql = String.format(sql, startDate, endDate);

		ArrayList<TweetCount> results = new ArrayList<TweetCount>();
		PreparedStatement stmt;

		try {
			stmt = dbConnection.prepareStatement(sql);
			//			add the keyword to the query

			stmt.setString(1, "%" + keyword + "%");

			ResultSet rs = stmt.executeQuery();
			Calendar calendar = Calendar.getInstance();

			while(rs.next()) {
				results.add( new TweetCount(
						rs.getTime("time"),
						rs.getInt("count"))
						);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			results = null;
		}

		return results;
	}
}
