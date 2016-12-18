package bme.mace.logicdomain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import weka.classifiers.functions.LibSVM;
import weka.core.Instances;

public class AdaBoost_SVM  extends SVM {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<LibSVM> weakClassifierList=new ArrayList<LibSVM>();
	public List<Double> weakClassifierWeightList=new ArrayList<Double>();
	 /**
	  * option1:迭代次数
	  */
	@Override
	public void buildModel(Instances trainDataInstances, String[] option) {
		//初始化权值序列
		setSampleInitialWeight(trainDataInstances);

		for(int iter =0; iter <Integer.valueOf(option[1]); iter++)
		{
			//弱分类器
			 SVM svmModel=new SVM();
			 String[] options=Arrays.copyOfRange(option, 2,option.length);
			 svmModel.buildModel(trainDataInstances, options);
			 //正确率返回值序列
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

	}
}
	
	@Override
	public  double evaluateInstance(Instances testInstance) {
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
	
	@Override
	public double[] evaluateModel(Instances testInstances) {
		Evaluation evaluation = null;
		double[] result=new double[testInstances.numInstances()];
		try {
			evaluation = new Evaluation(testInstances);
			//evaluation.setPriors(testInstances);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i=0;i<testInstances.numInstances();i++){
		
			try {
				 result[i] =evaluation.evaluateModel(weakClassifierList, weakClassifierWeightList, testInstances.instance(i));
				//return result;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return result;
	  
	}
	 /*
	  * 给权重序列赋初值
	  */
	 public void setSampleInitialWeight(Instances trainDataInstances)
	 {
		  	for(int i=0;i<trainDataInstances.numInstances();i++)
		  	{		
				double weightPerSample=1.0/trainDataInstances.numInstances();
				trainDataInstances.instance(i).setWeight(weightPerSample);	 
			 }
	}
	 
	    /*
		 * 计算错误率ε。输入：分类预测结果;权重序列。ε。result是指结果分对还是分错
		 */
		 public double calculateFalseRate(double[] result)
		 {
			 double falseSample=0.0;
			 double num=0.0;
				 for(int j=0;j<result.length;j++)
				 {
					 num+=1;//trainSample.instance(j).weight();
					 if(result[j]==0)
					 {
						 falseSample+=1;//trainSample.instance(j).weight();
					 }
				 }
			 falseSample=falseSample/num;
			return falseSample; 
		 }
		 
		 /*
		  * 计算正确与否。分类正确的返回1，错误的返回0。这里的result是指真实返回的结果
		  */
	 public double[] calTrueOrFalse(double[] _result,Instances trainDataInstances){
		 double[] TF=new double[_result.length];
		 for(int i=0;i<_result.length;i++){
			 if(_result[i]==trainDataInstances.instance(i).classValue()){
			 TF[i]=1;
			 }else
				 TF[i]=0;
		 }
		 return TF;
	 }
	 /*
	  * 计算β。
	  */
	 protected double calBeta(double e){
		 double beta=0.0;
		 beta=(1-e)/e;
		 //beta=Math.log(beta)/2;
		 return beta;
	 }
	 
	 //计算patient样本权重重新分布。输入：beta;预测结果result。这里的result是指分来的结果，分对还是分错		
	 protected void calNewWeightDistribution(double _beta,double[] _result,Instances trainDataInstances){
		 //计算分母
		 double sum=0.0;//分母p
		 
		 for(int i=0;i<_result.length;i++){
			 sum+=trainDataInstances.instance(i).weight()*Math.pow(_beta, _result[i]);
		 }
			 for(int j=0;j<_result.length;j++) {
				 double origWeight = trainDataInstances.instance(j).weight();
                 double weight=origWeight*Math.pow(_beta,_result[j])/sum;
                 trainDataInstances.instance(j).setWeight(weight);
				 }	
	 }
	 
	 
	 
}
