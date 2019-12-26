package mx.com.jros;

public class OperarcionImpl implements IOperaciones{

	@Override
	public boolean operacionMayorQue(double x, double y) {
		// TODO Auto-generated method stub
		boolean flag =false;
		if(x >y)
		flag=true;
		
		return flag;
	}

}
