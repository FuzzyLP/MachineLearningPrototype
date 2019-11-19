package defaultsmanagement;

import java.util.ArrayList;
import java.util.HashMap;

import auxiliar.LocalUserInfo;
import constants.KConstants;
import programAnalysis.ProgramAnalysis;
import programAnalysis.ProgramPartAnalysis;

public class HashOfConcepts {
	/** this class is created with the purpose of accesing the different function defaults for the different users.
	 * it is also where we find the value by others solutions.
	 *
	 * */
	//private HashMap<String,KMeansFunction> hashOfConcepts;//final
	public HashMap<String,KMeansFunction> hashOfConcepts;//pruebas
	public HashOfConcepts() {
		hashOfConcepts=new HashMap<String,KMeansFunction>();
	}

	//pendiente la creacion del algoritmo para filtros. debe de hacerse en esta parte, y hacer metodos en kMeans para recuperar los valores 
	public String[][] algo(LocalUserInfo localUserInfo, int[] currentUsercaracs, ProgramAnalysis program,
			ProgramPartAnalysis[] concept, String conceptName) {

		program.getAllDefinedFunctionDefinition(conceptName);
		if(!hashOfConcepts.containsKey(conceptName))
			hashOfConcepts.put(conceptName, new KMeansFunction());
		ArrayList<int[]> caracsFromAllUsers = new ArrayList<int[]>();

		for(ProgramPartAnalysis[] concept1:program.getFilteredProgramFuzzifications(localUserInfo, "", "",
				KConstants.Request.modeAdvanced))
			for (ProgramPartAnalysis personalization : concept1) {
				String ownerName = personalization.getPredOwner();
				if (ownerName != KConstants.Fuzzifications.DEFAULT_DEFINITION) {
					int[] caracteristics = getUserCaracFromUserName(ownerName, currentUsercaracs);
					if (caracteristics != null)
						caracsFromAllUsers.add(caracteristics);
				}
			}


		String [][]result= hashOfConcepts.get(conceptName).algo(program,currentUsercaracs,caracsFromAllUsers,conceptName,concept);

		//segunda parte

		int[] kindEach=this.getIds(localUserInfo, program);

		int result2=this.getMaximum(kindEach);
		System.out.println(" the means this user is most similar with is  "+ result2+"\n");

		return(hashOfConcepts.get(conceptName).getDefinitionFromIndex(result2)==null)? result:hashOfConcepts.get(conceptName).getDefinitionFromIndex(result2);
	}




	/** this method gets the most repeated number in the list
	 *
	 *@return a list from all tuples possible
	 * */
	private int getMaximum(int[] kindEach) {

		int maxCounter=1;
		int solution=-2;
		for(int element:kindEach)
		{
			int counter=0;
			for(int element1:kindEach) {
				if(element==element1)counter++;
			}
			if (counter>maxCounter) {
				maxCounter=counter;
				solution=element;
			}

		}

		return solution;
	}




	/** this method gets the closest ids of the given solutions for each of the functions.
	 *
	 *@return a list from all tuples possible
	 * */
	private int[] getIds(LocalUserInfo localUserInfo,ProgramAnalysis program) {

		ProgramPartAnalysis[][] program1=program.getFilteredProgramFuzzifications(localUserInfo, "", "",
				KConstants.Request.modeBasic);
		int[] kindEach=new int[program1.length];
		for(int i=0;i<kindEach.length;i++)kindEach[i]=-2;
		int i=0;
		for(ProgramPartAnalysis[] concept1:program1) {
			for (ProgramPartAnalysis personalization : concept1) {
				String ownerName = personalization.getPredOwner();
				if (ownerName != KConstants.Fuzzifications.DEFAULT_DEFINITION&&hashOfConcepts.containsKey(concept1[0].getPredDefined())) {
					kindEach[i]=hashOfConcepts.get(concept1[0].getPredDefined()).getIndexFromDefinition(personalization.getFunctionPoints());
				}

			}
			i++;
		}

		return kindEach;
	}




	/** this method gets the characteristics from an username
	 *
	 *@return a list from all tuples possible
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
}


