package bme.mace.sample;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weka.core.Instance;
import weka.core.Instances;

public class PrepareForSample {
	

	/**
	 *取样
	 * @param _totalSample
	 * @param counts
	 * @return
	 */
	public static Instances generateSubSample(List<Instances> _totalSample,int[] counts) {
		
		//copy
		List<Instances> totalSample=new ArrayList<Instances>();
		for(int i=0;i<_totalSample.size();i++){
			Instances oneSample=new Instances(_totalSample.get(i));
			totalSample.add(oneSample);
		}
		Instances subSample = new Instances(totalSample.get(0));
		subSample.delete();

		     Random random = new Random(); 
		     for(int count=0;count<counts.length;count++){//
			       for(int i=0;i<counts[count];i++){
			    	   int temp = random.nextInt(totalSample.get(count).numInstances());
			    	   Instance tempIns=(Instance) totalSample.get(count).instance(temp).copy();
			    	   subSample.add(tempIns);
			    	   totalSample.get(count).delete(temp);
			    	   }
			     }
			return subSample;   
	}

	/**
	 * 分组
	 * @param _totalSample
	 * @return
	 */
	public static List<Instances> prepareForDataSet(Instances _totalSample) {
		Instances totalSample=new Instances(_totalSample);
		List<Instances> insTemp=new ArrayList<Instances>();
		for(int i=0;i<2;i++){
			Instances ins=new Instances(totalSample,0,1);
			ins.delete();
			insTemp.add(ins);
		}
		for(int i=0;i<totalSample.numInstances();i++){
		System.out.println(totalSample.instance(i).classValue()+1);
			switch(String.valueOf(totalSample.instance(i).classValue()+1)){
			case "1.0":insTemp.get(0).add(totalSample.instance(i));break;
			case "2.0":insTemp.get(1).add(totalSample.instance(i));break;
			}
		}
        return insTemp;
	}
/**
 * 根据权重取样
 * @param _trainSample
 * @param _weight
 * @param _count
 * @return
 * @throws Exception
 */
	public static Instances generateSubTrainSampleForProposedAl(List<Instances> _trainSample,List<List<double[]>> _weight,int _count) throws Exception {
	
		//copy
		List<Instances> trainSample=new ArrayList<Instances>();
		for(int i=0;i<_trainSample.size();i++){
			Instances oneSample=new Instances(_trainSample.get(i));
			trainSample.add(oneSample);
		}
		//
		Instances subDataSet = new Instances(trainSample.get(1));
		//
		List<List<double[]>> tempWeightSample=new ArrayList<List<double[]>>();
		for(int i=0;i<_weight.size();i++){
			List<double[]> weightTemp=new ArrayList<double[]>();
			for(int j=0;j<_weight.get(i).size();j++){
				weightTemp.add(_weight.get(i).get(j));
			}
			tempWeightSample.add(weightTemp);
		}
			
			double[] WEIGHTS=calWeight(tempWeightSample);
		   
			Instances generateUnderSampleData = generateUnderSampleData(trainSample.get(0),tempWeightSample.get(0),_count,WEIGHTS[0]);
			for(int i=0;i<generateUnderSampleData.numInstances();i++){
				subDataSet.add(generateUnderSampleData.instance(i));
			}
subDataSet.randomize(new Random());
		    return subDataSet;
		}
	
	
	private static Instances generateUnderSampleData(Instances _trainUnderSample,List<double[]> _weightList,int _count,Double _weight)throws Exception{
		//copy
		Instances trainUnderSample=new Instances(_trainUnderSample);
		
	
			List<double[]> weightTemp=new ArrayList<double[]>();
			for(int i=0;i<_weightList.size();i++){
				weightTemp.add(_weightList.get(i));
		}
		
			//UnderSampling Instances
			Instances underSamplingInstances=new Instances(_trainUnderSample);
			underSamplingInstances.delete();
			
		     Random random = new Random(); 
		     
			     for(int i=0;i<_count;i++){
						 double m=0.0;
						 double randomNum=random.nextDouble()*_weight;
						 for(int ii=0;ii<weightTemp.size();ii++){
							 double[] weightCell=weightTemp.get(ii);
				               if ( randomNum>=m && randomNum < m + weightCell[1]) {  //
				            	 //
				            	   int index=weightTemp.indexOf(weightCell);
				            	   weightTemp=getWeightListNew(weightTemp,weightCell,index);
				            	   _weight=getAllWeightNew(_weight,weightCell);//
				            	   Instance in=(Instance)trainUnderSample.instance((int)weightCell[0]).copy();
				            	   underSamplingInstances.add(in);
				            	  break;
					               }  
					               m += weightCell[1];
						 }
			     }
			     return underSamplingInstances;
		     }
	
	 
	/**
	 * 更新权值
	 * @param _WEIGHT
	 * @param _getedSample
	 * @return
	 */
	 private static double getAllWeightNew(double _WEIGHT,double[] _getedSample){
		 _WEIGHT-=_getedSample[1];
		 return _WEIGHT;
	 }
	 
	/**
	 * 更改权重List
	 * @param _weightSample
	 * @param _getedSample
	 * @param index
	 * @return
	 */
	 private static List<double[]> getWeightListNew(List<double[]> _weightSample,double[] _getedSample,int index){
		 double label=_getedSample[2];
		 _weightSample.remove(index);
		 return _weightSample;
		 
	 }
	 /**
	  * 计算权重和
	  * @param _weightSample
	  * @return
	  */
	private static double[] calWeight(List<List<double[]>> _weightSample){
	double[] WEIGHTS=new double[_weightSample.size()];
		for(int i=0;i<_weightSample.size();i++){
			double sum=0.0;
			for(int j=0;j<_weightSample.get(i).size();j++){
				sum+=_weightSample.get(i).get(j)[1];
			}
			WEIGHTS[i]=sum;
		}
		return WEIGHTS;
	}
	
	
	

}
