package bme.mace.logicdomain;

import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.LinearNNSearch;

public class FindKNeighbours {
	public static Instances getKNeighbour(Instances sourceIns,Instance target,int KNN) throws Exception{
		Instances neighbours=null;
		EuclideanDistance dfunc=new EuclideanDistance();
		LinearNNSearch lnn=new LinearNNSearch();
		lnn.setDistanceFunction(dfunc);
		lnn.setInstances(sourceIns);
		lnn.addInstanceInfo(target);
		neighbours=lnn.kNearestNeighbours(target, KNN);
		sourceIns.setClassIndex(sourceIns.numAttributes()-1);
		return neighbours;
	}
}
