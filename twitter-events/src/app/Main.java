package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import db.DAOFactory;
import db.TweetDAO;

public class Main {
	public static DAOFactory daoFactory = null;
	public static TweetDAO tweetDAO = null;

	//	prompt the user for keywords to be searched 
	//	get the counts for these keywords by minute and 
	//	then plots them using JFreeChart
	private static void runApplication() throws IOException{
		ArrayList<String> keywords = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Enter the start date (yyyy-mm-dd hh:MM)");
		String startDate = in.readLine().trim();

		System.out.println("Enter the end date (yyyy-mm-dd hh:MM)");
		String endDate = in.readLine().trim();

		System.out.println("Enter your keywords - (press Enter when done)");

//		read keywords from console - stop reading when Enter pressed with blank line
		while(true){
			String str = in.readLine().trim();

			if(str.isEmpty()){
				break;
			}
			else{
				keywords.add(str);
			}
		}

		try {
			tweetDAO = daoFactory.getTweetDAO();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//		get the data series for each keyword in a map
		HashMap<String, Object> dataSeries = getDataSeries(keywords, startDate, endDate);
		
		//		add the base line data series (all tweets)
		System.out.println("Adding base data");
		ArrayList<TweetCount> baseSeries = tweetDAO.getTweetCountByKeyword("", startDate, endDate);

		LineChart.plot(dataSeries, baseSeries);
		
		System.out.println("Finished plotting");
	}

	private static HashMap getDataSeries(ArrayList<String> keywords, String startDate, String endDate) {
		HashMap dataSeries = new HashMap();

		for(String keyword : keywords){
			System.out.println("Getting data for: " + keyword);
			dataSeries.put(keyword, 
					tweetDAO.getTweetCountByKeyword(keyword, startDate, endDate));
		}

		return dataSeries;
	}

	//	obtains an instance of the DAO factory and runs the application
	public static void main(String[] args) throws IOException {
		daoFactory = new DAOFactory();

		try {
			daoFactory.activateConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		runApplication();

		daoFactory.deactivateConnection();
	}

}
