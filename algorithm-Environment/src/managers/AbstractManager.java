package managers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import storeHouse.RequestStoreHouse;
import storeHouse.ResultsStoreHouse;

public abstract class AbstractManager {

	protected RequestStoreHouse requestStoreHouse = new RequestStoreHouse();
	protected ResultsStoreHouse resultsStoreHouse = new ResultsStoreHouse();


}

// EOF
