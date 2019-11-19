package programAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import auxiliar.LocalUserInfo;
import constants.KConstants;
import filesAndPaths.ProgramFileInfo;

public class ProgramAnalysis {

	
	private static ProgramAnalysis program;
	private static ProgramPartAnalysis[][] resultAdvanced;
	private static ProgramPartAnalysis[][] resultBasic;
	private static int numberOfFunctions=10;
	private static int numberOfElementsPerFunction=10;
	public static ProgramAnalysis getProgramAnalysisClass(ProgramFileInfo programFileInfo) {
		
		if(program==null)program=new ProgramAnalysis();
		return program;
	}

	public ProgramPartAnalysis[][] getFilteredProgramFuzzifications(LocalUserInfo localUserInfo, String string,
			String string2, String mode) {
		
		Random random=new Random();
		ProgramPartAnalysis[][] result=new ProgramPartAnalysis[numberOfFunctions][];
		if (mode.equals(KConstants.Request.modeAdvanced)) {
			if(resultAdvanced!=null)return resultAdvanced;
			for (int i=0;i<result.length;i++) {
				result[i]=new ProgramPartAnalysis[(int)(numberOfElementsPerFunction*random.nextDouble()+1)];
				for (int j=0;j<result[i].length;j++) {
					result[i][j]=new ProgramPartAnalysis("concept"+i);
				}
			}
			
			resultAdvanced=result;
			
		}
		else if(mode.equals(KConstants.Request.modeBasic)){
			if(resultBasic!=null)return resultBasic;
			for (int i=0;i<result.length;i++) {
				result[i]=new ProgramPartAnalysis[2];
				result[i][0]=new ProgramPartAnalysis("concept"+i,KConstants.Fuzzifications.DEFAULT_DEFINITION);
				result[i][1]=new ProgramPartAnalysis("concept"+i,localUserInfo.getLocalUserName());
			}
			resultBasic=result;

		}
		if(result.length!=0&&result[0]!=null&&result[0].length!=0)result[0][0].resetUser();
		
		return result;
	}

	public ArrayList<HashMap<String, String>> getAllDefinedFunctionDefinition(String conceptName) {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateProgramFile(LocalUserInfo localUserInfo, String conceptName, String relatedConceptName,
			String mode, String[][] newDefaultDefinition) {
		// TODO Auto-generated method stub

	}
	public void setnumberOfFunctions(int number) {
		numberOfFunctions=number;
	}
	public void setnumberOfelements(int number) {
		numberOfElementsPerFunction=number;
	}

	public void resetUserInfo() {
		resultBasic=null;
		
	}

}
