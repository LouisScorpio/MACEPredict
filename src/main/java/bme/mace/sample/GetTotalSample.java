package bme.mace.sample;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class GetTotalSample {

	public static Instances generateTotalSample(String path) throws Exception{
		DataSource ds=new DataSource(path);
		Instances totalSample=ds.getDataSet();
		if(totalSample.classIndex()==-1){
			totalSample.setClassIndex(totalSample.numAttributes()-1);
	    }
		return totalSample;
		
	}
	public static void main(String[] args) {
		try {
			Instances sample = generateTotalSample("src/main/resources/CfsSubsetEvalDataSet.arff");
			System.out.println(sample);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
