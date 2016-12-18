package bme.mace.logicdomain;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import weka.core.Instances;

public class Model extends weka.classifiers.Classifier{

	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void buildModel(Instances trainDataInstances,String[] option) {
		
	}
	public static void saveModel(Model model,String path) {
		 try{
	            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(path));
	            oos.writeObject(model);
	            oos.flush();
	            oos.close();    
	           // writer.close();
	        }catch(IOException e){
	            e.printStackTrace();
	        }
		
	}
	public static Object loadModel(String path){
		 try{
	            ObjectInputStream ois=new ObjectInputStream(new FileInputStream(path));
	            Object classifier=ois.readObject();
	            ois.close();
	            return classifier;
	        }catch(IOException e){
	           
	            return null;
	        }catch(ClassNotFoundException e){

	            return null;
	        }
	 }
	public double[] evaluateModel(Instances testInstances) {
		return null;
	}
	public List<double[]> evaluateModel(List<Instances> testInstances){
		return null;
		
	}
	public double evaluateInstance(Instances testInstance) {
		return (Double) null;
		
	}
	@Override
	public void buildClassifier(Instances arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
}
