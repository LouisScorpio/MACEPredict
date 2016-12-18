package bme.mace.logicdomain;

import java.util.Arrays;
import java.util.List;

import bme.mace.sample.GetTotalSample;
import bme.mace.sample.PrepareForSample;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;

public class ProposedAlgorithm extends AdaBoost_SVM{

	
	private static final long serialVersionUID = 1L;
		//public    List<List<double[]>> weightList;
	   // public   List<LibSVM> weakClassifierList=new ArrayList<LibSVM>();
	  //  public   List<Double>  weakClassifierWeightList=new ArrayList<Double>();
	  
	     /**
	      * 构建模型
	      */
	     @Override
	  public  void buildModel(Instances trainDataInstances, String[] option) {
	    	 //设置初始权重
	 		setSampleInitialWeight(trainDataInstances);
	         List<Instances> sample = PrepareForSample.prepareForDataSet(trainDataInstances);	 
	   
		int iter=0;
		while(iter<Integer.valueOf(option[1])){
			
			Instances subTrainSampleSet = null;
			try {
				subTrainSampleSet = PrepareForSample.generateSubSample(sample, new int[]{sample.get(1).numInstances(),sample.get(1).numInstances()});
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			 SVM svmModel=new SVM();
			 String[] options=Arrays.copyOfRange(option, 2,option.length);
			 svmModel.buildModel(subTrainSampleSet, options);
			
			 double[] l= svmModel.evaluateModel(trainDataInstances);
			 LibSVM weakClassifier=svmModel.svm;
			 double[] trueOrFalse = calTrueOrFalse(l,trainDataInstances);
			 double e=0.0;
			 e = calculateFalseRate(trueOrFalse);
			 System.out.println(e);
			 if(e>0.5 || e==0) {
				 continue;
			 }
			 Double beta=0.0;
			 beta = calBeta(e);
			 System.out.println(beta);
		
			 weakClassifierList.add(weakClassifier);
			 weakClassifierWeightList.add(beta);
			 calNewWeightDistribution(beta,trueOrFalse,trainDataInstances);
			 iter++;
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
	 			double result =evaluation.evaluateModel(weakClassifierList, weakClassifierWeightList, testInstance.instance(0));
	 			return result;
	 		} catch (Exception e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	 		return 0;
	    }
	     public static void main(String[] args) {
	    	 Instances sample = null;
	 		try {
	 			sample = GetTotalSample.generateTotalSample("src/main/resources/CfsSubsetEvalDataSet.arff");
	 		} catch (Exception e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	 		String[] options=new String[]{"5","-S","0","-T", "2", "-K","2","-D","3","-G","0.45","-R","0.0","-N","0.5","-M","40.0","-C","1.0","-E","0.001","-P","0.1","-Z","-B","-seed","1"};
	 		ProposedAlgorithm proposedAlgorithm=new ProposedAlgorithm();
	 		proposedAlgorithm.buildModel(sample,options);
	 	 double[] evaluateModel = proposedAlgorithm.evaluateModel(sample);
	 	 for(int i=0;i<evaluateModel.length;i++){
	 		 System.out.println(evaluateModel[i]);
	 	 }
	 	
		}
	
	 
	 
}
