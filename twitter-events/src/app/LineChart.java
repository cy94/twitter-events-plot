package app;

import java.awt.Color; 
import java.awt.BasicStroke;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	/**
	 * @param applicationTitle 
	 * @param chartTitle
	 * @param dataSeries
	 * @param baseSeries
	 */
	public LineChart( String applicationTitle, String chartTitle,
			HashMap<String, Object> dataSeries, ArrayList<TweetCount> baseSeries) {
		
		super(applicationTitle);
		JFreeChart xylineChart = ChartFactory.createTimeSeriesChart(
				chartTitle ,
				"Time" ,
				"IDF" ,
				createDataset(dataSeries, baseSeries),
				true , true , false);

		ChartPanel chartPanel = new ChartPanel( xylineChart );
//		chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );

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
	private XYDataset createDataset(HashMap<String, Object> dataSeries, ArrayList<TweetCount> baseSeries) 
	{
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		//		
		HashMap<String, Object> baseSeriesMap = Utility.getBaseMapFromList(baseSeries);

		//		compare occurrences time of keyword to base tweet time as Strings
		DateFormat df = new SimpleDateFormat("HH:mm");

		for(Map.Entry<String, Object> e : dataSeries.entrySet()){
			TimeSeries series = new TimeSeries(e.getKey());

			//				for each time period (minute)
			for(TweetCount tc : (ArrayList<TweetCount>) e.getValue()){

				//					get the base count for this period
				int baseCount = (Integer) baseSeriesMap.get(df.format(tc.date));

				// add the time-IDF data point to the series		
				series.add(new Minute(tc.date), Utility.getIDF(baseCount, tc.count));
			}
			//				add the data series to the set of series
			dataset.addSeries(series);
		}

		return dataset;
	}   

	/**
	 * @param dataSeries
	 * @param baseSeries
	 */
	public static void plotMinuteGraph(HashMap<String, Object> dataSeries,
			ArrayList<TweetCount> baseSeries) {
		LineChart chart = new LineChart("Twitter", "Twitter Keyword Trends", dataSeries, baseSeries);
		chart.pack();
		RefineryUtilities.centerFrameOnScreen( chart );          
		chart.setVisible( true ); 
		
	}

	/**
	 * @param dataSeries
	 * @param baseSeries
	 */
	public static void plotTimeGraph(HashMap<String, Object> dataSeries,
			ArrayList<TweetCount> baseSeries) {
		LineChart chart = new LineChart("Twitter", "Twitter Keyword Trends", dataSeries, baseSeries);
		chart.pack();
		RefineryUtilities.centerFrameOnScreen( chart );          
		chart.setVisible( true ); 
	}
}
