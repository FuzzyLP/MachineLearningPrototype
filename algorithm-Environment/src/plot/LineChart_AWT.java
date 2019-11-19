package plot;
import org.jfree.chart.ChartPanel;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
public class LineChart_AWT extends ApplicationFrame{
	public LineChart_AWT( String applicationTitle , String chartTitle,double[][] elements, String[][] tab,String [] keys) {
	      super(applicationTitle);
	      JFreeChart lineChart = ChartFactory.createLineChart(
	         chartTitle,
	         "amount/number","credibility",
	         createDataset(elements,tab,keys),
	         PlotOrientation.VERTICAL,
	         true,true,false);
	      
	         
	      ChartPanel chartPanel = new ChartPanel( lineChart );
	      chartPanel.setPreferredSize( new java.awt.Dimension( 1200 , 850) );
	      setContentPane( chartPanel );
	   }

	   public DefaultCategoryDataset createDataset( double[][] elements,String[][] tab,String[]keys) {
	      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
	      int i=1,j=0;
	      for(double []element:elements) {
	    	  j=0;
	    	  for(double point:element) {
	    		  dataset.addValue( point , "user "+i , keys[j] );
	    		  j++;
	    	  }
	    	  i++;
	      }

	    	  for(String[] point:tab) {
	    		  dataset.addValue( Double.parseDouble(point[1]) , "result", point[0] );
	    		  
	    	  }
	
	      
	      return dataset;
	   }
	   
	 
}
