package auxiliar;
import java.util.*;
public class LocalUserInfo {
private static String name;
private static int maxValue = 1000;
private static int numberOfCaracs = 2;
	public String getLocalUserName() {
		// TODO Auto-generated method stub
		if (name!=null) return name;
		String resultado="user_com";
		Random rand= new Random();
		for (int i =0;i<numberOfCaracs;i++) resultado+="_" +(int)(rand.nextDouble()*maxValue);
		name=resultado;
		return name;
	}
	public void setMax(int max) {
		maxValue=max;
	}
	public void setNumberOfCaracs(int max) {
		numberOfCaracs=max;
	}
	public void resetUser() {
		// TODO Auto-generated method stub
		String resultado="user_com";
		Random rand= new Random();
		for (int i =0;i<numberOfCaracs;i++) resultado+="_" +(int)(rand.nextDouble()*maxValue);
		name=resultado;
	}

}
