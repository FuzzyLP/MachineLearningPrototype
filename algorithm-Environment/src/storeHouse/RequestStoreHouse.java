package storeHouse;

import java.util.Random;

import auxiliar.LocalUserInfo;
import filesAndPaths.ProgramFileInfo;

public class RequestStoreHouse {
	private static int numberOfFunctions=10;
	private static String requestParameter;
	public RequestStoreHouse getSession() {
		// TODO Auto-generated method stub
		return new RequestStoreHouse();
	}

	public LocalUserInfo getLocalUserInfo() {
		// TODO Auto-generated method stub
		return new LocalUserInfo();
	}
	public ProgramFileInfo getProgramFileInfo() {
		// TODO Auto-generated method stub
		return new ProgramFileInfo();
	}

	public String getRequestParameter(String preddefined) {
		// TODO Auto-generated method stub
		if(requestParameter!=null)return requestParameter;
		Random ran=new Random();
		int i = (int)(ran.nextDouble()*numberOfFunctions);
		requestParameter= "concept"+i;
		requestParameter= "";
		return requestParameter;
	}
	public void setnumberOfFunctions(int number) {
		numberOfFunctions=number;
	}

	public Object getRequestParameter2(String preddefined) {
		Random ran=new Random();
		int i = (int)(ran.nextDouble()*numberOfFunctions);
		requestParameter= "concept"+i;
		return requestParameter;
	}
}
