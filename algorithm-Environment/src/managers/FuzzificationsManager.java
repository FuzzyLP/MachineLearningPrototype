package managers;

import java.util.ArrayList;
import java.util.HashMap;

import auxiliar.LocalUserInfo;
import constants.KConstants;
import defaultsmanagement.HashOfPrograms;
import filesAndPaths.ProgramFileInfo;
import programAnalysis.ProgramAnalysis;
import programAnalysis.ProgramPartAnalysis;
import storeHouse.RequestStoreHouse;

public class FuzzificationsManager extends AbstractManager  {
	private HashOfPrograms fuzzy;
	/** from the name of the user and a list of caracteristics, you get the values representative to the user from the string
	 *
	 *@return workable caracteristics
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
	public void updateDefaults() throws Exception {
		if(fuzzy==null) fuzzy=new HashOfPrograms();
		int[] actualUsercaracs = getUserCaracFromUserName(
				requestStoreHouse.getSession().getLocalUserInfo().getLocalUserName(), null);
		ProgramFileInfo programFileInfo = requestStoreHouse.getProgramFileInfo();
		LocalUserInfo localUserInfo = requestStoreHouse.getSession().getLocalUserInfo();
		ProgramAnalysis programAnalized = ProgramAnalysis.getProgramAnalysisClass(programFileInfo);
		String mode = KConstants.Request.modeAdvanced;
		ProgramPartAnalysis[][] programPartAnalysis = programAnalized.getFilteredProgramFuzzifications(localUserInfo, "", "",
				mode);

		String conceptToUpdate = requestStoreHouse.getRequestParameter(KConstants.Fuzzifications.predDefined);

		ArrayList<String> PredAnalyzed = new ArrayList<String>();
		for (ProgramPartAnalysis[] concept : programPartAnalysis) {
			if (conceptToUpdate.length() == 0 ||concept!=null&&concept.length>0&&concept[0]!=null&&conceptToUpdate.equals(concept[0].getPredDefined())) {
				if (actualUsercaracs == null ||concept.length != 1) {
					String conceptName = concept[0].getPredDefined();
					if (!PredAnalyzed.contains(conceptName)) {

						String relatedConceptName = concept[0].getPredNecessary();
						String[][] newDefaultDefinition = fuzzy.algo(localUserInfo,actualUsercaracs,programAnalized,concept,conceptName,programFileInfo.getFileName()+programFileInfo.getFileOwner());
						// update data with the new definition
						programAnalized.updateProgramFile(localUserInfo, conceptName, relatedConceptName, KConstants.Request.modeEditingDefault,
								newDefaultDefinition);

						if (conceptToUpdate.length() == 0) {
							PredAnalyzed.add(conceptName);
						}
						else break;
					}
				}
			} else if (conceptToUpdate.length() == 0) {
				PredAnalyzed.add(concept[0].getPredDefined());
			}

		}


	}




	/////PRUEBAS


	
	public static void main(String[] args){

		int functions=10;
		int numberOfelementsPerFunction=50;
		int divisions=7;
		int maxFunction=3000;
		int maxUser=200;
		int caracs=2;
		
		ProgramAnalysis pa = new ProgramAnalysis();
		ProgramPartAnalysis ppa=new ProgramPartAnalysis("");
		ppa.resetUser();
		RequestStoreHouse rsh= new RequestStoreHouse();
		LocalUserInfo lui=new LocalUserInfo();
		pa.setnumberOfelements(numberOfelementsPerFunction);
		pa.setnumberOfFunctions(functions);
		ppa.setMaxDivisions(divisions);
		ppa.setMaxFunction(maxFunction);
		ppa.setMaxUser(maxUser);
		ppa.setNumberOfCaracs(caracs);
		rsh.setnumberOfFunctions(functions);
		lui.setMax(maxUser);
		lui.setNumberOfCaracs(caracs);
		
		
		
		FuzzificationsManager fm= new FuzzificationsManager();
		for(int i=0;i<10;i++) {
			lui.resetUser();
			pa.resetUserInfo();
			try{
			fm.updateDefaults();
			}catch(Exception e){
			e.printStackTrace();}
			System.out.println();
		}
		ProgramFileInfo programFileInfo=new ProgramFileInfo();
		System.out.println(HashOfPrograms.hashOfPrograms);
		System.out.println(HashOfPrograms.hashOfPrograms.get(programFileInfo.getFileName()+programFileInfo.getFileOwner()));
		System.out.println(HashOfPrograms.hashOfPrograms.get(programFileInfo.getFileName()+programFileInfo.getFileOwner()).hashOfConcepts);
		System.out.println(HashOfPrograms.hashOfPrograms.get(programFileInfo.getFileName()+programFileInfo.getFileOwner()).hashOfConcepts.get(fm.requestStoreHouse.getRequestParameter(KConstants.Fuzzifications.predDefined)));
		
		HashOfPrograms.hashOfPrograms.get(programFileInfo.getFileName()+programFileInfo.getFileOwner())
		.hashOfConcepts.get(fm.requestStoreHouse.getRequestParameter2(KConstants.Fuzzifications.predDefined)).
		exampleMain(new String[2])
		;
		
		
		
		
		
		
	}//*/
}
