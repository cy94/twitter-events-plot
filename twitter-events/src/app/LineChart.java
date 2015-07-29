package app;

import java.awt.Color; 
import java.awt.BasicStroke;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset; 
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.XYPlot; 
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection; 
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 * A class to plot IDF-time graph for Twitter keywords
 * 
 * @author Chandan Yeshwanth
 *
 */
public class LineChart extends ApplicationFrame {

	public enum AxisType{
		//	HH:mm time
		TIME,
		//	0 ... 60 ... 120
		MINUTES_FROM_START
	}

	/**
	 * @param applicationTitle 
	 * @param chartTitle
	 * @param dataSeries
	 * @param baseSeries
	 * @param type 
	 */
	public LineChart( String applicationTitle, String chartTitle,
			ArrayList<EventData> eventDataList, AxisType axisType) {

		super(applicationTitle);

		JFreeChart xylineChart = null;

		//	create the chart based on the X axis type 
		switch (axisType) {
		case TIME:
			System.out.println("TimeSeriesChart");

			xylineChart = ChartFactory.createTimeSeriesChart(
					chartTitle ,
					"Time (HH:mm)" ,
					"IDF" ,
					createTimeDataset(eventDataList));
			break;
		case MINUTES_FROM_START:
			System.out.println("XYLineChart");

			xylineChart = ChartFactory.createXYLineChart(
					chartTitle ,
					"Time from Start (min)" ,
					"IDF" ,
					createMinutesDataset(eventDataList));
			break;
		default:
			break;
		}

		// other chart setup calls
		ChartPanel chartPanel = new ChartPanel( xylineChart );
		final XYPlot plot = xylineChart.getXYPlot( );
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
		plot.setRenderer( renderer ); 
		setContentPane( chartPanel ); 
	}

	/**
	 * Creates a dataset that JFreeChart can use, from an ArrayList
	 * 
	 * @param dataSeries 
	 * @param baseSeries
	 * @return dataset
	 */
	private XYDataset createTimeDataset(ArrayList<EventData> eventDataList) 
	{
		TimeSeriesCollection chartDataset = new TimeSeriesCollection();

		//	compare keyword occurrence time with base time as Strings
		DateFormat df = new SimpleDateFormat("HH:mm");

		//	for each event
		for (EventData eventData : eventDataList) {
			//	convert baseSeries to a map
			HashMap<String, Object> baseSeriesMap = Utility.getBaseMapFromList(eventData.baseSeries);

			// iterate through the map
			for(Map.Entry<String, Object> e : eventData.dataSeries.entrySet()){
				// create a timeseries for each keyword (prepend the event name)
				TimeSeries series = new TimeSeries(eventData.eventName + ": "  + e.getKey());

				//	for each minute
				for(TweetCount tc : (ArrayList<TweetCount>) e.getValue()){
					//	get the base count for this period
					int baseCount = (Integer) baseSeriesMap.get(df.format(tc.date));

					// add the time-IDF data point to the series		
					series.add(new Minute(tc.date), Utility.getIDF(baseCount, tc.count));
				}

				//	add the TimeSeries to the collection
				chartDataset.addSeries(series);
			}
		}

		return chartDataset;
	}   

	/**
	 * Creates a dataset that JFreeChart can use, from an ArrayList
	 * 
	 * @param dataSeries 
	 * @param baseSeries
	 * @return dataset
	 */
	private XYDataset createMinutesDataset(ArrayList<EventData> eventDataList) 
	{
		PrintWriter writer = null;
		
		try {
			writer = new PrintWriter("/Users/internship/Desktop/internship/data/time_idf/test.csv", "UTF-8");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		XYSeriesCollection chartDataset = new XYSeriesCollection();

		//	compare occurrences time of keyword to base tweet time as Strings
		DateFormat df = new SimpleDateFormat("HH:mm");

		//	for each event
		for (EventData eventData : eventDataList) {
			writer.println(eventData.eventName);
			
			//	convert baseSeries to a map
			HashMap<String, Object> baseSeriesMap = Utility.getBaseMapFromList(eventData.baseSeries);

			// get the starting time for the dataSeries from the baseSeries for this event
			Date startTime = Utility.getStartTime(eventData.baseSeries);

			// iterate through the map
			for(Map.Entry<String, Object> e : eventData.dataSeries.entrySet()){
				writer.println(eventData.eventName + ": " + e.getKey());
				
				// create a timeseries for each keyword (prepend the event name)
				XYSeries series = new XYSeries(eventData.eventName + ": " + e.getKey());

				//	for each minute
				for(TweetCount tc : (ArrayList<TweetCount>) e.getValue()){
					//	get the base count for this period
					int baseCount = (Integer) baseSeriesMap.get(df.format(tc.date));

					//	find difference in milliseconds, then convert to minutes
					int minutesFromStart = (int) (tc.date.getTime() - startTime.getTime())/(60 * 1000);

					// add the time-IDF data point to the series		
					double idf = Utility.getIDF(baseCount, tc.count);
					writer.println(idf);
					
					series.add(minutesFromStart, idf);
				}

				//	add the TimeSeries to the collection
				chartDataset.addSeries(series);
			}
		}

		writer.close();
		
		return chartDataset;
	}   

	/**
	 * @param dataSeries
	 * @param baseSeries
	 */
	public static void plot(ArrayList<EventData> eventDataList, AxisType axisType) {
		LineChart chart = new LineChart("Twitter", "Twitter Keyword Trends", eventDataList, axisType);
		chart.pack();
		RefineryUtilities.centerFrameOnScreen( chart );          
		chart.setVisible( true ); 	
	}


}
