package programAnalysis;

import java.util.HashMap;
import java.util.Random;

public class ProgramPartAnalysis {
	private String user;
	private String conceptName;
	private String necConceptName;
	private static int userNumber=0;
	private static int maxFunction = 1000;
	private static int maxDivisions = 7;
	private static int maxUser = 1000;
	private static int numberOfCaracs = 2;
	private HashMap<String,String> function;
	
	public ProgramPartAnalysis(String concept) {
		Random random=new Random();
		user="user"+userNumber+"_com";
		userNumber++;
		conceptName=concept;
		int maxdiv=(int)(maxDivisions*random.nextDouble());
		function=new HashMap<String,String> ();
		for(int i=0;i<maxdiv;i++) {
			int aux1=(int)(random.nextDouble()*maxFunction);
			double aux2=random.nextDouble();
			function.put(""+aux1, ""+aux2);
			
		}
		String resultado="";
		Random rand= new Random();
		for (int i =0;i<numberOfCaracs;i++) resultado+="_"+ (int)(rand.nextDouble()*maxUser);
		user+=resultado;
			
		
	}

	public ProgramPartAnalysis(String concept, String userName) {
		Random random=new Random();
		user=userName;
		conceptName=concept;
		int maxdiv=(int)(maxDivisions*random.nextDouble());
		function=new HashMap<String,String> ();
		for(int i=0;i<maxdiv;i++) {
		
			int aux1=(int)(random.nextDouble()*maxFunction);
			double aux2=random.nextDouble();
			function.put(""+aux1, ""+aux2);}
	}

	public String getPredDefined() {
		
		return this.conceptName;
	}

	public String getPredNecessary() {

		if (necConceptName==null)necConceptName=conceptName;
		return necConceptName;
	}

	public String getPredOwner() {
		
		return this.user;
	}

	public Object getOnlyForUser() {

		return this.user;
	}
	

	public HashMap<String, String> getFunctionPoints() {
		// TODO Auto-generated method stub
		return function;
	}

	public void resetUser() {
		userNumber=0;
		
	}
	public void setNumberOfCaracs(int number) {
		numberOfCaracs= number;
		
	}
	public void setMaxFunction(int number) {
		maxFunction=number;
		
	}
	public void setMaxDivisions(int number) {
		maxDivisions=number;
		
	}
	public void setMaxUser(int number) {
		maxUser=number;
		
	}

}

