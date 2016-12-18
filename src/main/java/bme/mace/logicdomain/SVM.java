package bme.mace.logicdomain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weka.classifiers.functions.LibSVM;
import weka.core.Instances;

public class SVM extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	public LibSVM svm=null;
	//public  double[] evaluateResult=null;

	@Override
	public void buildModel(Instances trainDataInstances, String[] option) {
		Random ran=new Random(1);
		 // Make a copy of the data we can reorder
		Instances subSample = new Instances(trainDataInstances);
		subSample.randomize(ran);
	  //  String[] options=new String[]{"-S","0","-K","2","-D","3","-G","0.45","-R","0.0","-N","0.5","-M","40.0","-C","1.0","-E","0.001","-P","0.1","-Z","-B","-seed","1"};
	     svm=new LibSVM();
	    try {
			svm.setOptions(option);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			svm.buildClassifier(subSample);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
	}

	
	@Override
	public double evaluateInstance(Instances testInstance) {
		Evaluation evaluation = null;
		try {
			evaluation = new Evaluation(testInstance);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			double result = evaluation.evaluateModelOnce(svm, testInstance.instance(0));
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public double[] evaluateModel(Instances testInstances) {

		double[] evaluateResults=new double[testInstances.numInstances()];
		 try {
			 for(int i=0;i<testInstances.numInstances();i++){
				 evaluateResults[i]=svm.classifyInstance(testInstances.instance(i)); 
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return evaluateResults;
		
	}
	
	@Override
public 	List<double[]> evaluateModel(List<Instances> testInstances) {
		List<double[]> evaList=new ArrayList<double[]>();
		for(int i=0;i<testInstances.size();i++){
			double[] eva=evaluateModel(testInstances.get(i));
			evaList.add(eva);
		}
		return evaList;
	}


   
}
