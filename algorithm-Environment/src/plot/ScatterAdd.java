package plot;
import java.awt.BorderLayout;

import java.util.*;

import javax.swing.JFrame;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class ScatterAdd extends JFrame {

	private static final String title = "users plot";



	public ScatterAdd(String s,ArrayList<int[]> users,int[] indexesOfUsersGroups,ArrayList<double[]> kmeansPoints) {
		super(s);
		final ChartPanel chartPanel = createPanel( users,indexesOfUsersGroups,kmeansPoints);
		chartPanel.setPreferredSize( new java.awt.Dimension( 850 , 850) );
		this.add(chartPanel, BorderLayout.CENTER);

	}

	private ChartPanel createPanel(ArrayList<int[]> users,int[] indexesOfUsersGroups,ArrayList<double[]> kmeansPoints) {
		JFreeChart jfreechart = ChartFactory.createScatterPlot(
				title, "X", "Y", createDataFromInfo(users,indexesOfUsersGroups,kmeansPoints),
				PlotOrientation.VERTICAL, true, true, false);
		XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);
		NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
		domain.setVerticalTickLabels(true);
		return new ChartPanel(jfreechart);
	}

	private XYDataset createDataFromInfo(ArrayList<int[]> users,int[] indexesOfUsersGroups,ArrayList<double[]> kmeansPoints) {
		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
		XYSeries[] series = new XYSeries[kmeansPoints.size()+1];
		for (int i =0;i<series.length-1;i++) series[i]=new XYSeries(""+i);
		series[series.length-1]=new XYSeries("k points");
		for (int i =0;i<users.size();i++) {
			series[indexesOfUsersGroups[i]].add(users.get(i)[0], (users.get(i).length>1)?users.get(i)[1]:0);
		}
		for(double[] point:kmeansPoints)series[series.length-1].add(point[0], (point.length>1)?point[1]:0);
		for (int i =0;i<series.length;i++) xySeriesCollection.addSeries(series[i]);
		return xySeriesCollection;
	}


}
