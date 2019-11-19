package defaultsmanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JFrame;

import org.jfree.ui.RefineryUtilities;

import defaultsmanagement.FuzzificationsAlgorithms;
import plot.LineChart_AWT;
import plot.ScatterAdd;
import programAnalysis.ProgramAnalysis;
import programAnalysis.ProgramPartAnalysis;

public class KMeansFunction {



	private HashMap<Integer,FuzzificationsAlgorithms> personalizationByIndex;
	private FuzzificationsAlgorithms personalizationWithoutMeans;
	private static int MaxNumberOfIterations = 100;
	private ArrayList<double[]> kmeansPoints;
	private int numberOfUserCaracs;

	//Pruebas
	private ArrayList<int[]> users;
	private int[] testingCaracs;
	//*/

	public KMeansFunction() {
		personalizationByIndex= new HashMap<Integer,FuzzificationsAlgorithms>();
		this.personalizationWithoutMeans=new FuzzificationsAlgorithms();
	}

	public String[][] algo(ProgramAnalysis program, int[] actualUserCaracs, ArrayList<int[]> caracsFromAllUsers, String conceptName, ProgramPartAnalysis[] concept) {


		//Pruebas
		users=caracsFromAllUsers;
		testingCaracs=actualUserCaracs;
		//*/


		if(actualUserCaracs==null||isAllZeros(actualUserCaracs))
			return personalizationWithoutMeans.algo(program.getAllDefinedFunctionDefinition(conceptName));


		ArrayList<int[]> usersCaracsOfInterest = kMeansAlgorithm(caracsFromAllUsers,
				actualUserCaracs);
		if(!personalizationByIndex.containsKey(this.getGroupIndex(kmeansPoints,actualUserCaracs)))
			personalizationByIndex.put(this.getGroupIndex(kmeansPoints,actualUserCaracs), new FuzzificationsAlgorithms());



		ArrayList<HashMap<String, String>> PersonalizationsToUse = getAllDefinedFunctionDefinition(
				concept, usersCaracsOfInterest, actualUserCaracs);
		String[][] newDefaultDefinition = null;

		if(!personalizationByIndex.containsKey(this.getGroupIndex(this.kmeansPoints, actualUserCaracs)))
			personalizationByIndex.put(this.getGroupIndex(this.kmeansPoints, actualUserCaracs),new FuzzificationsAlgorithms());

		if (PersonalizationsToUse.size() != 0)
			newDefaultDefinition = personalizationByIndex.get(this.getGroupIndex(this.kmeansPoints, actualUserCaracs)).algo(PersonalizationsToUse);
		else
			newDefaultDefinition = personalizationByIndex.get(this.getGroupIndex(this.kmeansPoints, actualUserCaracs)).
			algo(program.getAllDefinedFunctionDefinition(conceptName));
		return newDefaultDefinition;

	}




	/**
	 * gets the index closest to the function passed as a parameter
	 *
	 * @param double arrays of Strings with the solution to be determined
	 *
	 * @return closest function
	 */
	private Integer getIndexFromDefinition(String[][] otherResult) {
		int result = -1;
		Double value = averageDistanceBetweenSolutions(personalizationWithoutMeans.getLastResult(), otherResult);
		double aux;
		for (int index: personalizationByIndex.keySet()) {
			if((aux=averageDistanceBetweenSolutions(personalizationByIndex.get(index).getLastResult(), otherResult))<value||value==-1) {
				value=aux;
				result=index;
			}
		}
		return result;
	}
	public Integer getIndexFromDefinition(HashMap<String,String> otherResult) {
		String [][] result=new String[otherResult.keySet().size()][2];
		int i=0;
		for(String element:otherResult.keySet()) {
			result[i][0]=element;
			result[i][1]=otherResult.get(element);
			i++;
		}
		return getIndexFromDefinition(result);
	}
	/**
	 * calculates the average difference between two functions by it's points (you can see it as the average error)
	 *
	 * @param two double arrays of Strings
	 *
	 * @return average error
	 */

	private double averageDistanceBetweenSolutions(String[][] Result1, String[][] Result2) {
		if((Result1==null||Result1.length==0)&&(Result2==null||Result2.length==0))return -1;
		if(Result1==null||Result1.length==0) {
			String[][] result2Sorted=sort(Result2);
			return(Result2.length==1)? ((Double.parseDouble(Result2[0][1])<0)?-1:1)*(Double.parseDouble(Result2[0][1])):areaOfFunction(result2Sorted,result2Sorted.length)/(Double.parseDouble(result2Sorted[result2Sorted.length-1][0])-Double.parseDouble(result2Sorted[0][0]));
		}
		if(Result2==null||Result2.length==0) {
			String[][] result1Sorted=sort(Result1);
			return(Result1.length==1)? ((Double.parseDouble(Result1[0][1])<0)?-1:1)*(Double.parseDouble(Result1[0][1])):areaOfFunction(result1Sorted,result1Sorted.length)/(Double.parseDouble(result1Sorted[result1Sorted.length-1][0])-Double.parseDouble(result1Sorted[0][0]));
		}
		String[][] result1Sorted=sort(Result1);
		String[][] result2Sorted=sort(Result2);
		double[][] finalResult=new double[result1Sorted.length+result2Sorted.length][2];
		int lasti=0,lastj=0;
		int nextResult=0;
		double aux;

		while(lasti<result1Sorted.length||lastj<result2Sorted.length) {

			if(!(lastj<result2Sorted.length)) {
				finalResult[nextResult][0]=finalResult[nextResult][0]=Double.parseDouble(result1Sorted[lasti][0]);
				finalResult[nextResult][1]=Double.parseDouble(result1Sorted[lasti][1])-(Double.parseDouble(result2Sorted[lastj-1][1]));
				lasti++;
			}
			else if(!(lasti<result1Sorted.length)) {
				finalResult[nextResult][0]=finalResult[nextResult][0]=Double.parseDouble(result2Sorted[lastj][0]);
				finalResult[nextResult][1]=Double.parseDouble(result1Sorted[lasti-1][1])-(Double.parseDouble(result2Sorted[lastj][1]));
				lastj++;
			}
			else if((double)Double.parseDouble(result1Sorted[lasti][0])==(double)Double.parseDouble(result2Sorted[lastj][0])) {
				finalResult[nextResult][0]=Double.parseDouble(result1Sorted[lasti][0]);
				finalResult[nextResult][1]=(double)(Double.parseDouble(result1Sorted[lasti][1])-Double.parseDouble(result2Sorted[lastj][1]));
				lasti++;
				lastj++;
			}

			else if(Double.parseDouble(result1Sorted[lasti][0])<Double.parseDouble(result2Sorted[lastj][0])) {
				finalResult[nextResult][0]=Double.parseDouble(result1Sorted[lasti][0]);

				aux=(lastj!=0)?((Double.parseDouble(result2Sorted[lastj][1])-Double.parseDouble(result2Sorted[lastj-1][1]))/
						((Double.parseDouble(result2Sorted[lastj][0])-Double.parseDouble(result2Sorted[lastj-1][0]))))
						*(Double.parseDouble(result1Sorted[lasti][0])-Double.parseDouble(result2Sorted[lastj-1][0])):0;

						finalResult[nextResult][1]=(double)(Double.parseDouble(result1Sorted[lasti][1])-(((lastj!=0)?Double.parseDouble(result2Sorted[lastj-1][1]):Double.parseDouble(result2Sorted[lastj][1]))+aux));

						lasti++;}
			else {
				finalResult[nextResult][0]=Double.parseDouble(result2Sorted[lastj][0]);
				aux=(lasti!=0)?((Double.parseDouble(result1Sorted[lasti][1])-Double.parseDouble(result1Sorted[lasti-1][1]))/
						((Double.parseDouble(result1Sorted[lasti][0])-Double.parseDouble(result1Sorted[lasti-1][0]))))
						*(Double.parseDouble(result2Sorted[lastj][0])-Double.parseDouble(result1Sorted[lasti-1][0])):0;

						finalResult[nextResult][1]=(double)(((lasti!=0)?Double.parseDouble(result1Sorted[lasti-1][1]):Double.parseDouble(result1Sorted[lasti][1]))+aux-(Double.parseDouble(result2Sorted[lastj][1])));
						lastj++;}


			nextResult++;
		}

		return(nextResult==1)? ((finalResult[0][1]<0)?-1:1)*finalResult[0][1]:areaOfFunction(finalResult,nextResult)/(finalResult[nextResult-1][0]-finalResult[0][0]);
	}
	/**
	 * Calculates the area of a function in base of it's points
	 *
	 * @param double array of doubles
	 *
	 * @return area of function
	 */
	private double areaOfFunction(String[][] finalResult, int nextResult) {
		double[][] actualResult=new double[finalResult.length][2];
		for(int i=0;i<finalResult.length;i++) {
			actualResult[i][0]=Double.parseDouble(finalResult[i][0]);
			actualResult[i][1]=Double.parseDouble(finalResult[i][1]);
		}
		return areaOfFunction(actualResult,nextResult);
	}
	private double areaOfFunction(double[][] finalResult, int nextResult) {
		if (finalResult.length<2)return 0;
		int position=(finalResult[0][1]>=0)?1:-1;
		double result=0;
		for(int i=1;i<nextResult;i++) {
			if(finalResult[i-1][1]*finalResult[i][1]>=0) {
				result+=((position*((finalResult[i][1]+finalResult[i-1][1])*(finalResult[i][0]-finalResult[i-1][0])))/2);
				if (i+1<finalResult.length&&finalResult[i][1]==0)
					position=(finalResult[i+1][1]>=0)?1:-1;
			}

			else {
				double slope=(finalResult[i][1]-finalResult[i-1][1])/(finalResult[i][0]-finalResult[i-1][0]);
				double intermidiateZero=-(finalResult[i-1][1]/slope);


				result+=((position*(intermidiateZero*finalResult[i-1][1])
						+(-position)*((finalResult[i][0]-finalResult[i-1][0]-intermidiateZero)*finalResult[i][1]))/2);
				position=-position;
			}

		}
		return result;
	}

	/**
	 * Sorts a double array by it's first element. The first element is meant to be a number to be parsed 
	 *
	 * @param one double arrays of Strings
	 *
	 * @return sorted array
	 */

	private String[][] sort(String[][] result) {


		String[][] resul= new String[result.length][];
		for(int i=0;i<result.length;i++)resul[i]=result[i];

		for(int i=0;i<resul.length;i++) {
			String[] max=resul[0];
			for(int j=1;j<resul.length-i;j++) 
				if(Double.parseDouble(resul[j][0])<Double.parseDouble(max[0])) {
					resul[j-1]=resul[j];
					resul[j]=max;
				}
				else 
					max=resul[j];


		}		

		return resul;
	}


	public String[][] getLatResult(int index){
		return personalizationByIndex.get(index).getLastResult();
	}
	private boolean isAllZeros(int[] list) {
		for (int value : list) {
			if (value != 0)
				return false;
		}
		return true;
	}
	/**
	 * Compares two arrays to determine whether they are equal
	 *
	 * @param two arrays of doubles
	 *
	 * @return array1==array2
	 */

	private boolean DoubleArrayEqual(double[] caracs1, double[] caracs2) {
		if (caracs1.length != caracs2.length)
			return false;
		for (int i = 0; i < caracs1.length; i++) {
			if (caracs1[i] != caracs2[i])
				return false;
		}
		return true;
	}
	/** With an array of doubles representing the existing means, and the number of values already added
	 *  for the creation of those means, a new value is added for each of those means, to calculate the new means values
	 *
	 *@return recalculated means
	 * */
	private double[] addPersonalizationToCalculateNewRepresentation(double[] mean, int[] caracs,
			int amountOfValuesForMean) {
		for (int i = 0; i < caracs.length; i++) {
			mean[i] = ((mean[i] * (double) amountOfValuesForMean) + (double) caracs[i])
					/ (double) (amountOfValuesForMean + 1);
		}
		return mean;
	}
	/** Determines the square distance of all the personalizations combined from two separated representations
	 *
	 *@return distance between two representations
	 * */
	private Double distanceBetween(double[] representant, int[] personalization) {
		Double result = (double) 0;
		for (int i = 0; i < personalization.length; i++) {
			result = result + (((double) personalization[i]) - representant[i])
					* (((double) personalization[i]) - representant[i]);
		}

		result = Math.sqrt(result);
		return result;
	}
	/** gives the index of the first representant which is closer to an user in a list
	 *
	 *@return index of the representant
	 * */

	private int getGroupIndex(ArrayList<double[]> representants, int[] personalization) {
		int result = -1;
		Double value = (double) -1;
		double aux;
		int i=0;
		for (double[] represent : representants) {
			if((aux=distanceBetween(represent, personalization))<value||value==-1) {
				value=aux;
				result=i;
			}
			i++;
		}
		return result;
	}

	/**private int getGroupIndex(ArrayList<double[]> representants, int[] personalization) {
ArrayList<Double> distances = new ArrayList<Double>();
for (double[] represent : representants) {
distances.add(distanceBetween(represent, personalization));
}
int result = -1;
Double Value = (double) -1;
for (Double distance : distances) {
if (Value == (double) -1 || distance < Value) {
Value = distance;
result = distances.indexOf(distance);
}
}
return result;
}*/
	/** determines whether an array is contained in a list of arrays 
	 *
	 *@return Is array contained in ArrayList?
	 * */

	private boolean ArrayListContainArray(ArrayList<int[]> list, int[] array) {
		for (int[] arrayI : list) {
			if (arrayI.length != array.length)
				continue;
			boolean found = true;
			for (int i = 0; i < arrayI.length; i++) {
				if (arrayI[i] != array[i]) {
					found = false;
					break;
				}
			}
			if (found)
				return true;
		}
		return false;
	}




	/** gets all the defined function definitions relevant to the user
	 *
	 *@return list of definitions of interest
	 * */
	public ArrayList<HashMap<String, String>> getAllDefinedFunctionDefinition(ProgramPartAnalysis[] concept,
			ArrayList<int[]> caracsOfInterest, int[] caracsActualUser) {

		ArrayList<HashMap<String, String>> personalizationSelected = new ArrayList<HashMap<String, String>>();

		for (int j = 0; j < concept.length; j++) {
			// Taking out the default rule
			int[] caracs = getUserCaracFromUserName(concept[j].getPredOwner(), caracsActualUser);

			if (concept[j].getOnlyForUser() != null && caracs != null
					&& ArrayListContainArray(caracsOfInterest, caracs))
			{

				HashMap<String, String> personalization = concept[j].getFunctionPoints();
				personalizationSelected.add(personalization);

			}
		}
		return personalizationSelected;
	}
	/** gets caracteristics from usernames
	 *
	 *@return list of definitions of interest
	 * */
	public int[] getUserCaracFromUserName(String UserName, int[] comparableCaracs) {
		ArrayList<Integer> caracsInt = new ArrayList<Integer>();
		int i = UserName.length() - 1;
		while (i >= 0) {
			if (UserName.charAt(i) != '_' && !Character.isDigit(UserName.charAt(i)))
				break;
			i--;
		}
		i = i + 2;
		String carac = "";
		while (i <= UserName.length()) {
			if (i == UserName.length() || UserName.charAt(i) == '_') {
				caracsInt.add(Integer.parseInt(carac));
				carac = "";
			} else if (Character.isDigit(UserName.charAt(i))) {
				carac = carac + UserName.charAt(i);
			}
			i++;
		}
		int[] results = new int[caracsInt.size()];
		int j = 0;
		for (Integer value : caracsInt) {
			results[j] = value;
			j++;
		}

		return results;

	}

	/** gets the minimum of n numbers
	 *
	 *@return minimum
	 * */
	private double min(double... args) {
		double result=args[0];
		for(double arg:args)
			result=(arg<result)?arg:result;
		return result;
	}
	/** gets the maximum of two numbers
	 *
	 *@return maximum
	 * */
	private double max(double... args) {
		double result=args[0];
		for(double arg:args)
			result=(arg>result)?arg:result;
		return result;
	}
	/** Gets a list with the maximun([i][0]) and minimun([i][1]) of each of the caracteristics of the users
	 *
	 *@return maximums and minimuns
	 * */

	public double[][] getMinMaxByOrder(ArrayList<int[]> caracsFromAllUsers){

		double[][] result= {};

		for(int[] x:caracsFromAllUsers) {
			//if there are more elements with that many caracteristics
			if(x.length>result.length) {

				double[][]resultaux=new double[x.length][2];

				for(int i=0;i<result.length;i++) {
					resultaux[i][0]=max(result[i][0],(double)x[i]);
					if(x[i]!=0) resultaux[i][1]=min(result[i][1],(double)x[i]);
				}	
				for(int i=result.length;i<resultaux.length;i++) {
					resultaux[i][0]=x[i];
					resultaux[i][1]=x[i];
				}
				result=resultaux;
			}
			//ioc
			else
				for(int i=0;i<x.length;i++) {
					result[i][0]=max(result[i][0],(double)x[i]);
					if(x[i]!=0)result[i][1]=min(result[i][1],(double)x[i]);
				}

		}
		return result;
	}
	/** Gets the multiplication of all the elements on a list
	 *
	 *@return multiplication
	 * */
	private int prod(int[] numbers) {
		int result=1;
		for(int number:numbers)result*=number;
		return result;
	}
	/** Gets the last number with more than one value. This is made so you don't get multiple points which are in the same space
	 *
	 *@return last element where maximum!=minimum
	 * */
	private int lastNotUnique(double[][] minMax) {
		int result=minMax.length-1;
		while(result>0&&minMax[result][0]==minMax[result][1])result--;
		return result;
	}
	/** Gets the first number with more than one value. This is made so you don't get multiple points which are in the same space
	 *
	 *@return first element where maximum!=minimum
	 * */
	private int firstNotUnique(double[][] minMax) {
		int result=0;
		while(result<minMax.length&&minMax[result][0]==minMax[result][1])result++;
		return result;
	}
	/** Gets the number before the one working on with more than one value. This is made so you don't get multiple points which are in the same space
	 *
	 *@return element before n where maximum!=minimum
	 * */
	private int beforeNotUnique(double[][] minMax,int position) {
		int result=position-1;
		while(result>0&&minMax[result][0]==minMax[result][1])result--;
		return result;
	}
	/** this method creates a tuple from a double array, where each auxiliar[i] in the option for the different positions
	 * example: auxiliar= {{1,2,3},{1,2}} -> {{1,1},{2,1},{3,1},{1,2},{2,2},{3,2}}
	 * 
	 * NOTE: this method is quite taxing on time (O(n³)) , it is being used because in the program the maximum number of results is 
	 * really small, so it doesn't really interfere with the performance overall.
	 *
	 *@return a list from all tuples possible
	 * */
	public ArrayList<double[]> tuplesGenerator(double[][] auxiliar){
		if(auxiliar==null)return null;
		return this.tuplesGenerator1(auxiliar,0);
	}
	private ArrayList<double[]> tuplesGenerator1(double[][] auxiliar, int startPos) {
		// TODO Auto-generated method stub
		if (auxiliar.length-1==startPos) {
			ArrayList <double[]>result= new ArrayList<double[]>();
			for(double x:auxiliar[startPos]) {
				double[]aux= {x};
				result.add(aux);
			}
			return result;
		}
		else {
			ArrayList <double[]> result=new ArrayList<double[]>();
			ArrayList <double[]> lastResult=tuplesGenerator1(auxiliar,startPos+1);
			for(double a:auxiliar[startPos]) {
				for(double[] y:lastResult) {
					double[] aux=new double[auxiliar.length-startPos];
					aux[0]=a;

					for(int i =0;i<y.length;i++)
						aux[i+1]=y[i];
					result.add(aux);
				}

			}
			return result;
		}

	}
	/** this method creates a list of possible useful points to be used for the k-means algorithm, 
	 *
	 *@return a list from all tuples possible
	 * */
	public ArrayList<double[]> getAutomaticRepresentatives(ArrayList<int[]> caracsFromAllUsers) {
		double[][] minMax=this.getMinMaxByOrder(caracsFromAllUsers);
		int numberOfPoints=(Math.log(caracsFromAllUsers.size())>1)?(int) Math.log(caracsFromAllUsers.size()):1;
		int[] divisions=new int[minMax.length];
		for (int i=0;i<divisions.length;i++)divisions[i]=1;
		int firstUseful=this.firstNotUnique(minMax);
		int maxUseful= lastNotUnique(minMax);
		while(prod(divisions)<numberOfPoints) {
			if(divisions[firstUseful]==divisions[maxUseful])divisions[firstUseful]++;
			else {
				int i=firstUseful+1;
				while(divisions[i]==divisions[beforeNotUnique(minMax,i)]||minMax[i][0]==minMax[i][1])
					i++;
				divisions[i]++;
			}
		}
		double [][]auxiliar=new double[divisions.length][];
		for (int i =0;i<divisions.length;i++) {
			auxiliar[i]=new double[divisions[i]];
			for(int j=0;j<divisions[i];j++) {
				double distDiv=(minMax[i][0]-minMax[i][1])/divisions[i];
				auxiliar[i][j]=minMax[i][1]+distDiv/2+j*distDiv;
			}
		}

		return tuplesGenerator(auxiliar);
	}


	/** makes the k-means algorithm with all the users
	 *
	 *@return list of definitions of interest
	 * */


	public ArrayList<int[]> kMeansAlgorithm(ArrayList<int[]> caracsFromAllUsers, int[] caracsActualUser) {
		if(kmeansPoints==null||this.numberOfUserCaracs-caracsFromAllUsers.size()>.01*this.numberOfUserCaracs) {
			boolean finished = false;
			ArrayList<double[]> representativesCaracs =this.getAutomaticRepresentatives(caracsFromAllUsers);
			
			numberOfUserCaracs=caracsFromAllUsers.size();

			for (int i = 0; !finished && i < MaxNumberOfIterations; i++) {
				int[] indexesOfUsersGroups = new int[caracsFromAllUsers.size()];
				int j = 0;
				for (int[] personalization : caracsFromAllUsers) {
					indexesOfUsersGroups[j] = getGroupIndex(representativesCaracs, personalization);
					j++;
				}
				double[][] newRepresentants = new double[representativesCaracs.size()][representativesCaracs.get(0).length];
				int[] AmountOfValuesForMeans = new int[representativesCaracs.size()];
				for (int k = 0; k < indexesOfUsersGroups.length; k++) {
					newRepresentants[indexesOfUsersGroups[k]] = addPersonalizationToCalculateNewRepresentation(
							newRepresentants[indexesOfUsersGroups[k]], caracsFromAllUsers.get(k),
							AmountOfValuesForMeans[indexesOfUsersGroups[k]]);
					AmountOfValuesForMeans[indexesOfUsersGroups[k]]++;
				}
				finished = true;
				for (int m = 0; m < representativesCaracs.size(); m++) {
					if (!DoubleArrayEqual(representativesCaracs.get(m), newRepresentants[m])
							&& AmountOfValuesForMeans[m] != 0) {
						representativesCaracs.set(m, newRepresentants[m]);
						if (finished)
							finished = false;
					}
				}

			}
			kmeansPoints= representativesCaracs;
		}
		int[] indexesOfUsersGroups = new int[caracsFromAllUsers.size()];
		int j = 0;
		for (int[] userCaracs : caracsFromAllUsers) {
			indexesOfUsersGroups[j] = getGroupIndex(this.kmeansPoints, userCaracs);
			j++;
		}
		int actualUserGroupIndex = getGroupIndex(this.kmeansPoints, caracsActualUser);
		ArrayList<int[]> result = new ArrayList<int[]>();
		for (int k = 0; k < indexesOfUsersGroups.length; k++) {
			if (indexesOfUsersGroups[k] == actualUserGroupIndex)
				result.add(caracsFromAllUsers.get(k));
		}
		return result;
	}
	public String[][] getDefinitionFromIndex(int solution) {
		if (solution==-1)
			return this.personalizationWithoutMeans.getLastResult();
		else if(this.personalizationByIndex.containsKey(solution))
			return this.personalizationByIndex.get(solution).getLastResult();
		else return null;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*Seccion de Pruebas*/


	public  void exampleMain(String [] args)
	{

		//Prueba de usuarios
		ArrayList<int[]> users= this.users;

		this.kMeansAlgorithm(users, users.get(0));
		for(double[] x:this.kmeansPoints) {
			for(double y:x)System.out.print(y+" ");
			System.out.println();
		}
		int k = 0;
		int[] indexesOfUsersGroups = new int[users.size()];
		for (int[] userCaracs : users) {
			indexesOfUsersGroups[k] = this.getGroupIndex(this.kmeansPoints, userCaracs);
			k++;
		}

		ScatterAdd demo = new ScatterAdd("example",users,indexesOfUsersGroups,this.kmeansPoints);
		demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		demo.pack();
		demo.setLocationRelativeTo(null);
		demo.setVisible(true);
		//Prueba de defaults

		FuzzificationsAlgorithms fuzzy1=this.personalizationByIndex.get(this.getGroupIndex(this.kmeansPoints, this.testingCaracs));


		String[][] tab = fuzzy1.getLastResult();
		if(tab==null)tab=new String[0][];
		String[] keys=new String[(tab!=null)?tab.length:0];
		int l=0;
		for (String[] i:tab) {
			/*for(String j:i) {

				System.out.print(j+"	");
			}*/
			keys[l]=i[0];
			
			System.out.println();
			l++;

		}
		LineChart_AWT chart = new LineChart_AWT(
				"number vs credibility" ,
				"number vs credibility",fuzzy1.interp,tab,keys);

		chart.pack( );
		RefineryUtilities.centerFrameOnScreen( chart );
		chart.setVisible( true );
	}
	//use this method to test the fuzzification algorithm and kmeans by themselves.
	//*/

	/*public static void main(String [] args)
	{
		Random random= new Random();
		float number= random.nextFloat()*100000;
		double number1= random.nextDouble()*100000;

		System.out.println(number);
		System.out.println(number1);
		String[][]aux1= {{"0","0"},{"2","0"}};
		String[][]aux2= {{"0","0"},{"2","0"}};
		double[][]aux3= {{0,2},{2,-2},{3.5,1},{4,2},{6,-2}};

		KMeansFunction fuzzy= new KMeansFunction();
		//Prueba de usuarios
		ArrayList<int[]> users=fuzzy.generateRandomUsers(50,1,1000);



		System.out.println(fuzzy.areaOfFunction(aux3,aux3.length));
		System.out.println(fuzzy.averageDistanceBetweenSolutions(aux1,aux2));
		fuzzy.kMeansAlgorithm(users, users.get(0));
		for(double[] x:fuzzy.kmeansPoints) {
			for(double y:x)System.out.print(y+" ");
			System.out.println();
		}
		int k = 0;
		int[] indexesOfUsersGroups = new int[users.size()];
		for (int[] userCaracs : users) {
			indexesOfUsersGroups[k] = fuzzy.getGroupIndex(fuzzy.kmeansPoints, userCaracs);
			k++;
		}

		ScatterAdd demo = new ScatterAdd("example",users,indexesOfUsersGroups,fuzzy.kmeansPoints);
		demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		demo.pack();
		demo.setLocationRelativeTo(null);
		demo.setVisible(true);
		//Prueba de defaults

		String[][][] elements= fuzzy.generateRandomproperties(1, 3000, 7);

		ArrayList<HashMap<String,String>> a = new ArrayList<HashMap<String,String>>();


		for(String[][] hashmap:elements) {
			HashMap<String,String> h1 = new HashMap<String,String>();
			for(String [] positions:hashmap) {

				h1.put(positions[0],positions[1]);
			}
			a.add(h1);
		}
		FuzzificationsAlgorithms fuzzy1=new FuzzificationsAlgorithms();

		ArrayList<String> tab1 = fuzzy1.getkeySets(a);
		String[] keys=new String[tab1.size()];
		int aux=0;
		for (String i:tab1) {
			keys[aux]=i;
			aux++;

		}
		String[][] tab = fuzzy1.machineLearning(a);

		for (String[] i:tab) {
			for(String j:i) {

				System.out.print(j+"	");
			}
			System.out.println();

		}
		LineChart_AWT chart = new LineChart_AWT(
				"number vs credibility" ,
				"number vs credibility",fuzzy1.interp,tab,keys);

		chart.pack( );
		RefineryUtilities.centerFrameOnScreen( chart );
		chart.setVisible( true );
	}//*/
	/** this method creates a list of functions for testing on the functions algorithm, 
	 *
	 *@return a list from all tuples possible
	 * */
	/*private String[][][] generateRandomproperties(int quantity, int maximumKey, int maximumNumberOfKeys){
		Random random= new Random();
		String resultado [][][]=new String[quantity][][];
		String keys[]=new String[maximumNumberOfKeys];
		int i=0;
		for(String key:keys) {
			keys[i]=""+(int)(random.nextDouble()*maximumKey);
			i++;}
		i=0;
		String elemento[][];
		for (String[][] property:resultado) {

			String keysllave="";

			int counter=0;
			while(counter==0){
				keysllave="";
				while (keysllave.length()<maximumNumberOfKeys) {
					if (random.nextDouble()>.7) {
						counter++;
						keysllave+="1";
					}
					else 
						keysllave+="0";

				}}
			elemento=new String[counter][2];
			char []keyarray=keysllave.toCharArray();
			int k=0;
			int j =0;
			for (String key:keys) {
				if(keyarray[k]=='1') {
					elemento[j][0]= key;
					elemento[j][1]=""+random.nextDouble();
					j++;
				}
				k++;

			}
			resultado[i]=elemento;
			i++;
		}

		return resultado;
	}//*/
	/** this method creates a list of functions for testing on the KmeansAlgorithm, 
	 *
	 *@return a list from all tuples possible
	 * */
	/*private ArrayList<int[]> generateRandomUsers(int quantity, int numberOfCaracs, int maxPerCarac){
		Random random= new Random();
		ArrayList<int[]> resultado=new ArrayList<int[]>();


		for (int i=0;i<quantity;i++) {
			int elemento[]=new int[numberOfCaracs];

			for (int j=0;j<elemento.length;j++) {
				elemento[j]=(int)(random.nextDouble()*maxPerCarac);
			}
			resultado.add(elemento);
		}

		return resultado;
	}


	//*/


}


