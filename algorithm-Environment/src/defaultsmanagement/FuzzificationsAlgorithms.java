package defaultsmanagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;


import programAnalysis.ProgramPartAnalysis;
//testing
import java.util.Random;
import plot.LineChart_AWT;
import plot.ScatterAdd;

public class FuzzificationsAlgorithms {
	
	
	private String[][]lastResult;
	
	
	//pruebas
	public double[][] interp;
	//finDePruebas*/
	
	public FuzzificationsAlgorithms(){
		lastResult= null;
	}
	/** finds the group of most similar functions and afterwards creates it.
	 *
	 *@return a list from all tuples possible
	 * */
	public String[][] algo(ArrayList<HashMap<String,String>> a)
	{
		//return averageAlgo();
		return machineLearning(a);
	}


	//DEPRECATED
	/**
	 * makes an average of the value of the fuzzifications 
	 *
	 * @param two arrays of doubles
	 *
	 * @return String
	 * NOTE: not being used at the moment
	 */
	//make an average of the value of the fuzzifications
	/*private String[][] averageAlgo(ArrayList<HashMap<String,String>> a)
	{
		//problem cases
		if ((a == null)||(a.size() == 0))
		{
			return new String[0][0];
		}

		Set<String> strSet = a.get(0).keySet();
		String[][] resul = new String[strSet.size()][2];
		double avg = 0;
		int i = 0;
		for (String key: strSet)
		{
			avg = 0;
			double v;
			for (HashMap<String, String> entry: a) {
				v = Double.parseDouble(entry.get(key));
				avg += v;
			}
			resul[i][0] = key;
			Double value = new Double(Math.round(avg*1000/a.size())/1000.);
			if (((value*10) % 10) == 0)
			{
				resul[i][1] = String.valueOf(value.intValue());
			} else {
				resul[i][1] = String.valueOf(value);
			}
			i++;
		}
		return resul;
	}*/
	/**
	 * Gets all the keys of the function
	 *
	 * @param all sets of the function
	 *
	 * @return list with all keys ordered
	 */
	
	//private Fuera de Pruebas
	public ArrayList<String> getkeySets(ArrayList<HashMap<String,String>> set){

		ArrayList<String> result=new ArrayList<String>();

		for(HashMap<String,String> setElement:set)

			for(String key: setElement.keySet()) 

				if(!result.contains(key)) 

					result.add(key); 

		return sort(result);

	}

	/**
	 * sorts a list of arra	
	 *
	 * @param all sets of the function
	 *
	 * @return list with all keys ordered
	 */
	private ArrayList<String> sort(ArrayList<String> result) {
		// TODO Auto-generated method stub
		ArrayList<String> resul= new ArrayList<String> ();
		String lastMinimum=null;
		for(int i=0;i<result.size();i++) {
			String aux=null;
			for(String element:result) 
				aux=(compare(element,lastMinimum)>0&&(aux==null||compare(element,aux)<0))?element:aux; 
			resul.add(aux);
			lastMinimum=aux;

		}

		return resul;
	}

	/** compares two Strings which are supposed to be two ints. 
	 *
	 *@return a list from all tuples possible
	 * */
	public int compare(String obj1, String obj2) {
		if (obj1 == null&&obj2 == null) {
			return 0;
		}
		if (obj1 == null) {
			return -1;
		}
		if (obj2 == null) {
			return 1;
		}
		//return obj1.compareTo(obj2);
		return Integer.parseInt(obj1)-Integer.parseInt( obj2 );
	}; 

	/** this gets the median point for each position on the final result, 
	 *
	 *@return a double array with the final solution
	 * */
	private String[][] medianAlgo(ArrayList<double[]> a, ArrayList<String> strSetAux)
	{
		//problem cases
		if ((a == null)||(a.size() == 0))
			return new String[0][0];


		ArrayList<String> strSet = strSetAux;
		String[][] resul = new String[strSet.size()][2];

		int keyposition = 0;
		for (String key: strSet)
		{
			List<Double> contents = new ArrayList<Double>();
			double v;
			for (double[] entry: a) 
			{
				v = entry[keyposition];
				contents.add(v);

			}
			resul[keyposition][0] = key;
			Collections.sort(contents, new Comparator<Double>() {
				@Override
				public int compare(Double c1, Double c2) {
					return Double.compare(c1, c2);
				}
			});

			if(contents.size()%2==1)
			{
				int pos = (int) (contents.size()-1)/2;
				resul[keyposition][1]=contents.get(pos).toString();
			}
			else
			{
				int pos = (int) (contents.size())/2;
				Float result =(float) ((contents.get(pos) + contents.get(pos-1))/2);
				resul[keyposition][1]= result.toString();
			}

			//resul[i][1]=contents.get(Math.round((contents.size()+1)/2)-1).toString();
			keyposition++;
		}

		//LOG.info("Default rule updated with: " + resul);
		return resul;
	}
	/** this method gets the number of divisions for each point in the function, 
	 *
	 *@return final result
	 * */
	private int obtainNumberOfDivisions( int numberOfPersonalizations, int numberOfDimensions )
	{
		double doubleValue = (numberOfDimensions!=0)?numberOfPersonalizations / numberOfDimensions:1;
		int numberOfDivisions = ( ( Double ) doubleValue ).intValue();
		if( numberOfDivisions < 2 )
			return 2;
		else if( numberOfDimensions > 10 )
			return 10;
		else
			return numberOfDivisions;
	}

	/*private static int getSubspaceCoordinate( double value, int numberOfDivisions )
	{
		int i = 1;
		boolean done = false;
		while( !done && i <= numberOfDivisions )
		{
			if( value <= ( double ) ( i / numberOfDivisions ) )
				done = true;
			else
				i++;
		}
		return i;
		}
	 */
	/** this method gets the position of the element for each point, 
	 *
	 *@return position
	 * */
	private int getSubspaceCoordinate( double value, int numberOfDivisions )
	{
		return (value==1)?numberOfDivisions:(int)(value*numberOfDivisions+1);
	}
	/** finds whether two arrays are equal, 
	 *
	 *@return element1==element 2 by each element
	 * */
	private boolean sameValue( int[] element1, int[] element2 )
	{
		if( element1.length != element2.length )
			return false;
		int i = 0;
		while( i < element1.length&&(element1[i] == element2[i] )) 
			i++;

		return i>=element1.length;
	}
	/** this method finds the frequency of an element in a position, 
	 *
	 *@return frequency of an element
	 * */
	private int frequency( List<int[]> lista, int[] element )
	{
		int i = 0;
		int frec = 0;
		while( i < lista.size() )
		{
			if( this.sameValue( element, lista.get( i ) ))
				frec++;
			i++;
		}
		return frec;
	}
	/** this finds the area with more functions, to be use afterwards in the median
	 *
	 *@return a list from all tuples possible
	 * */
	private  ArrayList<Integer> findtheAreaWithMorePersonalizations( int[][] subspacesSet )
	{
		//Transforms to a list of personalizations
		List<int[]> subspacesList = new ArrayList<int[]>();
		for( int[] subspace : subspacesSet )
		{
			subspacesList.add( subspace );
		}
		int maxZoneFrecuency = 0;
		ArrayList<int[]> selectedZones = new ArrayList<int[]>();
		//finds the area with more points
		while( subspacesList.size() != 0 )
		{
			int[] zone = subspacesList.get( 0 );
			int zoneFrecuency = this.frequency( subspacesList, zone );
			if( zoneFrecuency > maxZoneFrecuency )
			{
				maxZoneFrecuency = zoneFrecuency;
				selectedZones.clear();
				selectedZones.add( subspacesList.get( 0 ) );
			}
			else if( zoneFrecuency == maxZoneFrecuency )
				selectedZones.add( subspacesList.get( 0 ) );
			int i = 0;
			while( i < subspacesList.size() )
			{
				if( sameValue( subspacesList.get( i ), zone ) )
					subspacesList.remove( i );
				else
					i++;
			}
		}
		//gives the position of the 
		ArrayList<Integer> positionsOfZonesSelected = new ArrayList<Integer>();

		int i = 0;
		while( i < subspacesSet.length ){
			{
				for( int[] zone: selectedZones )

					if( sameValue( subspacesSet[i], zone ) )
						positionsOfZonesSelected.add( i );


				i++;
			}
		}
		return positionsOfZonesSelected;
	}
	/** this method get the next key that the function includes. This method is needed to simulate in-between keys values.
	 *
	 *@return next key
	 * */
	private String getNextKey(String lastKey, ArrayList<String> listOfKeys,HashMap<String, String> personalization) {
		boolean found=false;
		String result=null;
		for(String key:listOfKeys) {
			if(result!=null)break;
			else if(found&&personalization.containsKey(key)) 
				result=key;
			else if(!found&&key.equals(lastKey)) 
				found=true;			

		}
		return (result==null)?listOfKeys.get(listOfKeys.size()-1):result;
	}

	/** From all the elements of a function, this method gets a default value to be used for new users
	 *
	 *@return created default value.
	 * */
	public String[][] machineLearning(ArrayList<HashMap<String,String>> a)
	{
		if ( ( a == null ) || ( a.size() == 0 ) )
		{
			return new String[0][0];
		}

		int numberOfPersonalizations = a.size();
		int numberOfDimensions = this.getkeySets(a).size();
		int numberOfDivisionsPerDimension = this.obtainNumberOfDivisions( numberOfPersonalizations, numberOfDimensions );
		double slope=0;
		int[][] subspacesSet = new int[ numberOfPersonalizations ][ numberOfDimensions];
		double [][] interpolatedPoints = new double[ numberOfPersonalizations ][ numberOfDimensions];
		String lastKey="0",nextKey="0";
		double valueLK=0,valueNK=0;
		ArrayList<String> domainValuesSet = this.getkeySets(a);
		int personalizationNumber = 0;
		int dimension=0;
		for ( HashMap<String, String> personalization: a ) 
		{

			for ( String domainValue: domainValuesSet )
				if(personalization.containsKey(domainValue)){
					lastKey=domainValuesSet.get(0);
					nextKey=domainValue;
					valueLK=Double.parseDouble(personalization.get(nextKey));
					valueNK=valueLK;
					slope=0;

					break;
					
				}


			dimension=0;
			for ( String domainValue: domainValuesSet )
			{
				double value;
				if(personalization.containsKey(domainValue)){
					value = Double.parseDouble( personalization.get( domainValue ) );
					subspacesSet[ personalizationNumber][  dimension ]=this.getSubspaceCoordinate( value, numberOfDivisionsPerDimension);
					interpolatedPoints[  personalizationNumber][ dimension]=value;
					lastKey=nextKey;
					valueLK=valueNK;
					nextKey=this.getNextKey(nextKey, domainValuesSet, personalization);
					valueNK=(personalization.containsKey(nextKey))?Double.parseDouble(personalization.get(nextKey)):valueNK;
					slope=(valueNK-valueLK)/(Double.parseDouble(nextKey)-Double.parseDouble(lastKey));

				}
				else{
					value=valueLK+slope*(Double.parseDouble(domainValue)-Double.parseDouble(lastKey));
					subspacesSet[ personalizationNumber][  dimension ]= this.getSubspaceCoordinate( value, numberOfDivisionsPerDimension);
					interpolatedPoints[ personalizationNumber][  dimension ]=value;
				}
				dimension++;

			}
			personalizationNumber++;
		}
		ArrayList<Integer> positionsOfZonesSelected = this.findtheAreaWithMorePersonalizations( subspacesSet );
		ArrayList<double[]> selectedPersonalizations = new ArrayList<double[]>();
		for( Integer position : positionsOfZonesSelected )
		{
			selectedPersonalizations.add( interpolatedPoints[position] );
		}
		interp=interpolatedPoints;
		lastResult=medianAlgo(selectedPersonalizations,domainValuesSet);
		return lastResult;
	}
	/** Creates a copy of the lastResult to be used in the extended algorithm
	 *
	 *@return next key
	 * */
	public String[][] getLastResult() {
		// TODO Auto-generated method stub
		if(lastResult==null) return null;
		String [][] aux= new String[lastResult.length][2];
		for(int i=0;i<aux.length;i++) {
			aux[i][0]=lastResult[i][0];
			aux[i][1]=lastResult[i][1];
		}
		return aux;
	}
}



