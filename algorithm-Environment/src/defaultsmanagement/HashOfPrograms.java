package defaultsmanagement;

import java.util.ArrayList;
import java.util.HashMap;

import auxiliar.LocalUserInfo;
import programAnalysis.ProgramAnalysis;
import programAnalysis.ProgramPartAnalysis;
/** this class is created with the sole purpose of accesing all the programs which have already been defaulted by the system.
*
* */
public class HashOfPrograms {
	//private static HashMap<String,HashOfFunctions> hashOfPrograms;//final
	public static HashMap<String,HashOfConcepts> hashOfPrograms;//pruebas

	public HashOfPrograms(){
		if(hashOfPrograms==null)
			hashOfPrograms=new HashMap<String,HashOfConcepts>();
	}

	public String[][] algo(LocalUserInfo localUserInfo, int[] actualUsercaracs, ProgramAnalysis program,
			ProgramPartAnalysis[] concept, String conceptName, String fileProgram) {
		// TODO Auto-generated method stub
		if(!hashOfPrograms.containsKey(fileProgram))
			hashOfPrograms.put(fileProgram, new HashOfConcepts());


		return hashOfPrograms.get(fileProgram).algo(localUserInfo,actualUsercaracs,program,concept,conceptName);
	}


}
