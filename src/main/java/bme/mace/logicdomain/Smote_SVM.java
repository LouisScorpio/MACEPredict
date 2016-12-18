package bme.mace.logicdomain;
import java.util.Arrays;

import bme.mace.sample.GetTotalSample;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.instance.SMOTE;


public class Smote_SVM extends SVM{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public void buildModel(Instances trainDataInstances, String[] option) {
		  
		   SMOTE convert = new SMOTE();
	       // int seed = (int) (Math.random() * 10);
	        //String[] options = {"-S", String.valueOf(seed), "-P", "100.0", "-K", "5"};
	        String[] smoteOption=Arrays.copyOfRange(option, 0, 6);
	        Instances SmoteInstances = null;
	        try {
	        	String[] svmOptions=Arrays.copyOfRange(option, 6, option.length);
	            convert.setOptions(smoteOption);
	            convert.setInputFormat(trainDataInstances);
	            SmoteInstances = Filter.useFilter(trainDataInstances, convert);
	            svm=new LibSVM();
	            svm.setOptions(svmOptions);
	            svm.buildClassifier(SmoteInstances);
	           System.out.println();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	     
	}
	@Override
	public double evaluateInstance(Instances testInstance) {
		return super.evaluateInstance( testInstance);
		
	}
	public static void main(String[] args) {
		Instances sample = null;
		try {
			sample = GetTotalSample.generateTotalSample("src/main/resources/CfsSubsetEvalDataSet.arff");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] options=new String[]{"-S","0","-T", "2", "-K","2","-D","3","-G","0.45","-R","0.0","-N","0.5","-M","40.0","-C","1.0","-E","0.001","-P","0.1","-Z","-B","-seed","1"};
		Smote_SVM model=new Smote_SVM();
		model.buildModel(sample,options);
	}

}
