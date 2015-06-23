package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import db.DAOFactory;
import db.TweetDAO;

/**
 * @author Chandan Yeshwanth
 *
 */
public class Main {
	public static DAOFactory daoFactory = null;
	public static TweetDAO tweetDAO = null;

	/**
	 * Plots the IDF-time graph for terms specified by the user
	 * in a given time range.
	 * 
	 * 
	 * @throws IOException
	 */
	private static void plotEvents() throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		ArrayList<EventData> eventDataList = new ArrayList<EventData>();

		//	get all inputs
		while(true) {
			System.out.println("Enter the event name (press Enter to finish)");
			String eventName = in.readLine().trim();

			if(eventName.isEmpty()){
				break;
			}
			else {		
				//	input time range for Twitter data
				System.out.println("Enter the start date (yyyy-mm-dd hh:MM)");
				String startDate = in.readLine().trim();

				System.out.println("Enter the end date (yyyy-mm-dd hh:MM)");
				String endDate = in.readLine().trim();

				// input keywords
				ArrayList<String> keywords = getKeywords();

				eventDataList.add(new EventData(eventName, startDate, endDate, keywords));
			}
		}	
		
		System.out.println("Choose X-Axis unit:");
		System.out.println("1. Minutes from start (0, 60, 120)");
		System.out.println("2. Time (HH:mm)");

		int choice = Integer.parseInt(in.readLine().trim());
		
		// add data to eventdatalist for each event
		for (EventData ed : eventDataList) {
			HashMap<String, Object> dataSeries;
			ArrayList<TweetCount> baseSeries;
			
			//	get the data series for each keyword 
			ed.dataSeries = getDataSeries(ed.keywords, ed.startDate, ed.endDate);

			// base series for this event duration
			System.out.println("Adding base data");
			ed.baseSeries = tweetDAO.getTweetCountByKeyword("", ed.startDate, ed.endDate);
		}

		System.out.println("Plotting ...");

		switch (choice) {
		case 1:
			LineChart.plot(eventDataList, LineChart.AxisType.MINUTES_FROM_START);
			break;
		case 2:
			LineChart.plot(eventDataList, LineChart.AxisType.TIME);
			break;
		default:
			break;
		}		

		System.out.println("Finished plotting");
	}

	/**
	 * Returns the hourly tweet count for each keyword in the specified
	 * time range
	 * 
	 * @param keywords Terms to be plotted
	 * @param startDate Starting date and time of twitter data (UTC)
	 * @param endDate Ending date and time of twitter data (UTC)
	 * @return A term-object map where object is returned by getTweetCountByKeyword
	 */
	private static HashMap getDataSeries(ArrayList<String> keywords, String startDate, String endDate) {
		HashMap dataSeries = new HashMap();

		try {
			tweetDAO = daoFactory.getTweetDAO();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(String keyword : keywords){
			System.out.println("Getting data for: " + keyword);
			dataSeries.put(keyword, 
					tweetDAO.getTweetCountByKeyword(keyword, startDate, endDate));
		}

		return dataSeries;
	}

	/**
	 * obtains an instance of the DAO factory and runs the application
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		daoFactory = new DAOFactory();

		try {
			daoFactory.activateConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		plotEvents();

		daoFactory.deactivateConnection();
	}


	/**
	 * Takes a list of words as input from the command line 
	 * 
	 * @return ArrayList of strings
	 */
	private static ArrayList<String> getKeywords() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		ArrayList<String> keywords = new ArrayList<String>();

		System.out.println("Enter keywords - (press Enter to finish)");

		//		read keywords from console - stop reading when Enter pressed with blank line
		while(true){
			String str = "";

			try {
				str = in.readLine().trim();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(str.isEmpty()){
				break;
			}
			else{				
				keywords.add(str);
			}
		}
		return keywords;
	}

}
