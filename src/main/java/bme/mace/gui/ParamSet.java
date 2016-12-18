package bme.mace.gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;

import weka.core.Utils;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ParamSet {

	protected Shell shell;
	private Combo svmTypeCb;
	private Text coefTxt;
	private Text costTxt;
	private Text degreeTxt;
	private Text epsTxt;
	private Text gammaTxt;
	private Combo kernelCb;
	private Text lossTxt;
	private Text nuTxt;
	private Text seedTxt;
	private Text nearNeighsTxt;
	private Text perTxt;
	private Text ranSeedTxt;
    private Label titlelbl;
    private FormData fd_nearNeighsTxt ;
    public static Text paramText;
    public static  String modelName;
    private Display display;
    
   /**
    * 设置text参数
    */
    public static void setParamText(Text text,String model){
    	paramText=text;
    	modelName=model;
    }
    protected void setParam() throws Exception{
    	String paramString=paramText.getText().trim();
    	String[] classifiers=paramString.split("weak Classifier:SVM");

    	switch (modelName) {
		case "SVM":		
			break;
       case "AdaBoost+SVM":	
       case "BoostSampling":
    	   //设置weak Classifier前面的参数
    	   nearNeighsTxt.setText(Utils.getOption('R', classifiers[0].split(" ")));     
	        break;
       case "Smote+SVM":
    	   String[] smote=classifiers[0].trim().split(" ");
    	   nearNeighsTxt.setText(Utils.getOption('K', smote));
    	   perTxt.setText(Utils.getOption('P', smote));
    	   ranSeedTxt.setText(Utils.getOption('S', smote));
    	   break;
		default:
			break;
		}
    	String[] svmParam=classifiers[1].trim().split(" ");
    	//System.out.println(Integer.valueOf( Utils.getOption('S', svmParam)));
    	svmTypeCb.select(Integer.valueOf( Utils.getOption('S', svmParam).trim()));
    	kernelCb.select(Integer.valueOf(Utils.getOption('K', svmParam).trim()));
    	degreeTxt.setText(Utils.getOption('D', svmParam).trim());
    	gammaTxt.setText(Utils.getOption('G', svmParam).trim());
    	coefTxt.setText(Utils.getOption('R', svmParam).trim());
    	nuTxt.setText(Utils.getOption('N', svmParam).trim());
    	costTxt.setText(Utils.getOption('C', svmParam).trim());
    	epsTxt.setText(Utils.getOption('E', svmParam).trim());
    	lossTxt.setText(Utils.getOption('P', svmParam).trim());
    	seedTxt.setText(Utils.getOption("seed", svmParam).trim());
    }
 
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ParamSet window = new ParamSet();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		try {
			setParam();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(484, 708);
		shell.setText("参数设定");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		SashForm sashForm = new SashForm(shell, SWT.VERTICAL);
		
		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		 titlelbl = new Label(composite, SWT.HORIZONTAL | SWT.CENTER);
		titlelbl.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 12, SWT.NORMAL));
	//	titlelbl.setText("Smote+SVM Model");
		
		Composite composite_2 = new Composite(sashForm, SWT.NONE);
		composite_2.setLayout(new FormLayout());
		
		createExternalModel(composite_2,modelName);
		
		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		composite_1.setLayout(new FormLayout());
		
		Label lblWeakClassifier = new Label(composite_1, SWT.NONE);
		lblWeakClassifier.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 11, SWT.NORMAL));
		FormData fd_lblWeakClassifier = new FormData();
		fd_lblWeakClassifier.top = new FormAttachment(0, 10);
		fd_lblWeakClassifier.left = new FormAttachment(0, 10);
		//fd_lblWeakClassifier.left = new FormAttachment(0, 10);
		lblWeakClassifier.setLayoutData(fd_lblWeakClassifier);
		lblWeakClassifier.setText("Classifier：SVM");
		
		Label lblSVMType = new Label(composite_1, SWT.RIGHT);
		FormData fd_lblSVMType = new FormData();
		fd_lblSVMType.top = new FormAttachment(0, 40);
		fd_lblSVMType.right = new FormAttachment(100, -289);
		lblSVMType.setLayoutData(fd_lblSVMType);
		lblSVMType.setText("SVMType");
		
		Label lblCoef = new Label(composite_1, SWT.RIGHT);
		FormData fd_lblCoef = new FormData();
		fd_lblCoef.top = new FormAttachment(lblSVMType, 13);
		fd_lblCoef.right = new FormAttachment(lblSVMType, 0, SWT.RIGHT);
		//fd_lblCoef.left = new FormAttachment(0, 130);
		lblCoef.setLayoutData(fd_lblCoef);
		lblCoef.setText("coef0");
		
		Label lblCost = new Label(composite_1, SWT.RIGHT);
		FormData fd_lblCost = new FormData();
		fd_lblCost.top = new FormAttachment(lblCoef, 13);
		fd_lblCost.right = new FormAttachment(lblSVMType, 0, SWT.RIGHT);
		lblCost.setLayoutData(fd_lblCost);
		lblCost.setText("cost");
		
		Label lblDegree = new Label(composite_1,  SWT.RIGHT);
		//fd_lblNewLabel.left = new FormAttachment(lblDegree, 0, SWT.LEFT);
		FormData fd_lblDegree = new FormData();
		fd_lblDegree.top = new FormAttachment(lblCost, 13);
		fd_lblDegree.right = new FormAttachment(lblSVMType, 0, SWT.RIGHT);
		//fd_lblDegree.left = new FormAttachment(0, 120);
		lblDegree.setLayoutData(fd_lblDegree);
		lblDegree.setText("degree");
		
		Label lblDonotreplacemissingvalue = new Label(composite_1, SWT.RIGHT);
		FormData fd_lblDonotreplacemissingvalue = new FormData();
		fd_lblDonotreplacemissingvalue.top = new FormAttachment(lblDegree, 13);
		//fd_lblDonotreplacemissingvalue.top=new FormAttachment(fd_lblDegree,13);
		fd_lblDonotreplacemissingvalue.right = new FormAttachment(lblSVMType, 0, SWT.RIGHT);
		lblDonotreplacemissingvalue.setLayoutData(fd_lblDonotreplacemissingvalue);
		lblDonotreplacemissingvalue.setText("doNotReplaceMissingValue");
		
		Label lblEps = new Label(composite_1, SWT.RIGHT);
		//fd_lblCost.left = new FormAttachment(lblEps, -31, SWT.LEFT);
		FormData fd_lblEps = new FormData();
		fd_lblEps.top=new FormAttachment(lblDonotreplacemissingvalue, 13);
		fd_lblEps.right = new FormAttachment(lblSVMType, 0, SWT.RIGHT);
		lblEps.setLayoutData(fd_lblEps);
		lblEps.setText("eps");
		
		Label lblGamma = new Label(composite_1, SWT.RIGHT);
		//fd_lblEps.bottom = new FormAttachment(100, -247);
		FormData fd_lblGamma = new FormData();
		fd_lblGamma.top = new FormAttachment(lblEps, 13);
		fd_lblGamma.right = new FormAttachment(lblSVMType, 0, SWT.RIGHT);
		lblGamma.setLayoutData(fd_lblGamma);
		lblGamma.setText("gamma");
		
		Label lblKerneltype = new Label(composite_1, SWT.RIGHT);
		FormData fd_lblKerneltype = new FormData();
		fd_lblKerneltype.top=new FormAttachment(lblGamma, 13);
		fd_lblKerneltype.right = new FormAttachment(lblSVMType, 0, SWT.RIGHT);
		lblKerneltype.setLayoutData(fd_lblKerneltype);
		lblKerneltype.setText("kernelType");
		
		Label lblLoss = new Label(composite_1, SWT.RIGHT);
		//fd_lblKerneltype.bottom = new FormAttachment(lblLoss, -6);
		FormData fd_lblLoss = new FormData();
		fd_lblLoss.top=new FormAttachment(lblKerneltype, 13);
		fd_lblLoss.right = new FormAttachment(lblEps, 0, SWT.RIGHT);
		lblLoss.setLayoutData(fd_lblLoss);
		lblLoss.setText("loss");
		
		Label lblNormalize = new Label(composite_1, SWT.RIGHT);
		//fd_lblLoss.bottom = new FormAttachment(lblNormalize, -6);
		FormData fd_lblNormalize = new FormData();
		fd_lblNormalize.top=new FormAttachment(lblLoss, 13);
		fd_lblNormalize.right = new FormAttachment(lblEps, 0, SWT.RIGHT);
		lblNormalize.setLayoutData(fd_lblNormalize);
		lblNormalize.setText("normalize");
		
		Label lblNu = new Label(composite_1, SWT.RIGHT);
		//fd_lblNormalize.bottom = new FormAttachment(100, -143);
		FormData fd_lblNu = new FormData();
		fd_lblNu.right = new FormAttachment(lblEps, 0, SWT.RIGHT);
		fd_lblNu.top = new FormAttachment(lblNormalize, 13);
		lblNu.setLayoutData(fd_lblNu);
		lblNu.setText("nu");
		
		Label lblProbability = new Label(composite_1, SWT.RIGHT);
		FormData fd_lblProbability = new FormData();
		fd_lblProbability.top = new FormAttachment(lblNu, 13);
		fd_lblProbability.right = new FormAttachment(lblEps, 0, SWT.RIGHT);
		lblProbability.setLayoutData(fd_lblProbability);
		lblProbability.setText("probabilityEstimates");
		
		Label lblSeed = new Label(composite_1, SWT.RIGHT);
		FormData fd_lblSeed = new FormData();
		fd_lblSeed.top=new FormAttachment(lblProbability, 13);
		fd_lblSeed.right = new FormAttachment(lblEps, 0, SWT.RIGHT);
		//	fd_lblSeed.bottom = new FormAttachment(100, -22);
			lblSeed.setLayoutData(fd_lblSeed);
			lblSeed.setText("seed");
			
			Label lblShrinking = new Label(composite_1, SWT.RIGHT);
			//fd_lblProbability.bottom = new FormAttachment(lblShrinking, -15);
			FormData fd_lblShrinking = new FormData();
			fd_lblShrinking.top=new FormAttachment(lblSeed, 13);
			fd_lblShrinking.right = new FormAttachment(lblEps, 0, SWT.RIGHT);
			//fd_lblShrinking.bottom = new FormAttachment(lblSeed, -17);
			lblShrinking.setLayoutData(fd_lblShrinking);
			lblShrinking.setText("shrinking");
			
			svmTypeCb = new Combo(composite_1, SWT.NONE);
			svmTypeCb.setItems(new String[] {"C-SVC", "nu-SVC"});
			FormData fd_svmTypeCb = new FormData();
			//fd_svmTypeCb.bottom = new FormAttachment(lblSVMType, 0);
			fd_svmTypeCb.top = new FormAttachment(lblSVMType, -1, SWT.TOP);
			fd_svmTypeCb.right = new FormAttachment(lblSVMType, 279, SWT.RIGHT);
			fd_svmTypeCb.left = new FormAttachment(lblSVMType, 10);
			//fd_svmTypeCb.bottom=new FormAttachment(svmTypeCb,3);
			fd_svmTypeCb.bottom = new FormAttachment(lblSVMType, 2, SWT.BOTTOM);
			svmTypeCb.setLayoutData(fd_svmTypeCb);
			
			coefTxt = new Text(composite_1, SWT.BORDER);
			coefTxt.setText("40.0");
			FormData fd_coefTxt = new FormData();
			fd_coefTxt.top = new FormAttachment(lblCoef, -1, SWT.TOP);
			fd_coefTxt.left = new FormAttachment(svmTypeCb, 0,SWT.LEFT);
			fd_coefTxt.right = new FormAttachment(svmTypeCb,0,SWT.RIGHT);
			svmTypeCb.select(0);
			fd_coefTxt.bottom = new FormAttachment(lblCoef, 2, SWT.BOTTOM);
			//fd_coefTxt.bottom=new FormAttachment(lblCoef, 0);
			coefTxt.setLayoutData(fd_coefTxt);
			
			costTxt = new Text(composite_1, SWT.BORDER);
			costTxt.setText("1.0");
			FormData fd_costTxt = new FormData();
			fd_costTxt.bottom = new FormAttachment(lblCost, 2, SWT.BOTTOM);
			fd_costTxt.top = new FormAttachment(lblCost, -1, SWT.TOP);
			fd_costTxt.left = new FormAttachment(lblCost, 10);
			fd_costTxt.right = new FormAttachment(coefTxt,0,SWT.RIGHT);
			costTxt.setLayoutData(fd_costTxt);
			
			degreeTxt = new Text(composite_1, SWT.BORDER);
			degreeTxt.setText("3");
			FormData fd_degreeTxt = new FormData();
			fd_degreeTxt.left = new FormAttachment(lblDegree, 10);
			fd_degreeTxt.top = new FormAttachment(lblDegree, -1, SWT.TOP);
			fd_degreeTxt.right = new FormAttachment(costTxt,0,SWT.RIGHT);
			fd_degreeTxt.bottom = new FormAttachment(lblDegree, 2, SWT.BOTTOM);
			degreeTxt.setLayoutData(fd_degreeTxt);
			
			Combo replaceValueCb = new Combo(composite_1, SWT.NONE);
			replaceValueCb.setItems(new String[] {"False", "True"});
			FormData fd_replaceValueCb = new FormData();
			fd_replaceValueCb.top = new FormAttachment(lblDonotreplacemissingvalue, -1, SWT.TOP);
			fd_replaceValueCb.left = new FormAttachment(lblDonotreplacemissingvalue, 10);
			fd_replaceValueCb.right = new FormAttachment(degreeTxt,0,SWT.RIGHT);
			fd_replaceValueCb.bottom = new FormAttachment(lblDonotreplacemissingvalue, 2, SWT.BOTTOM);
			replaceValueCb.setLayoutData(fd_replaceValueCb);
			
			epsTxt = new Text(composite_1, SWT.BORDER);
			epsTxt.setText("0.001");
			FormData fd_epsTxt = new FormData();
			fd_epsTxt.left = new FormAttachment(lblEps, 10);
			fd_epsTxt.top = new FormAttachment(lblEps, -1, SWT.TOP);
			fd_epsTxt.right = new FormAttachment(replaceValueCb,0,SWT.RIGHT);
			replaceValueCb.select(0);
			fd_epsTxt.bottom = new FormAttachment(lblEps, 2, SWT.BOTTOM);
			epsTxt.setLayoutData(fd_epsTxt);
			
			gammaTxt = new Text(composite_1, SWT.BORDER);
			gammaTxt.setText("0.45");
			FormData fd_gammaTxt = new FormData();
			fd_gammaTxt.top = new FormAttachment(lblGamma, -1, SWT.TOP);
			fd_gammaTxt.left = new FormAttachment(lblGamma, 10);
			fd_gammaTxt.right = new FormAttachment(epsTxt,0,SWT.RIGHT);
			fd_gammaTxt.bottom = new FormAttachment(lblGamma, 2, SWT.BOTTOM);
			gammaTxt.setLayoutData(fd_gammaTxt);
			
		    kernelCb = new Combo(composite_1, SWT.NONE);
			kernelCb.setItems(new String[] {"linear：u‘*v", "polynomial：（gamma*u’*v+coef0）^degree", "radial basis function：exp（-gamma*|u-v|^2）", "sigmoid:tanh（gamma*u‘*v+coef0）"});
			FormData fd_kernelCb = new FormData();
			fd_kernelCb.top = new FormAttachment(lblKerneltype, -1, SWT.TOP);
			fd_kernelCb.left = new FormAttachment(lblKerneltype, 10);
			fd_kernelCb.right = new FormAttachment(gammaTxt,0,SWT.RIGHT);
			fd_kernelCb.bottom = new FormAttachment(lblKerneltype, 2, SWT.BOTTOM);
			kernelCb.setLayoutData(fd_kernelCb);
			
			lossTxt = new Text(composite_1, SWT.BORDER);
			lossTxt.setText("0.1");
			FormData fd_lossTxt = new FormData();
			fd_lossTxt.top = new FormAttachment(lblLoss, -1, SWT.TOP);
			fd_lossTxt.left = new FormAttachment(lblLoss, 10);
			fd_lossTxt.right = new FormAttachment(kernelCb,0,SWT.RIGHT);
			fd_lossTxt.bottom = new FormAttachment(lblLoss, 2, SWT.BOTTOM);
			lossTxt.setLayoutData(fd_lossTxt);
			

			Combo normCb = new Combo(composite_1, SWT.NONE);
			normCb.setItems(new String[] {"False", "True"});
			FormData fd_normCb = new FormData();
			fd_normCb.top = new FormAttachment(lblNormalize, -1, SWT.TOP);
			fd_normCb.left = new FormAttachment(lblNormalize, 10);
			fd_normCb.right = new FormAttachment(kernelCb,0,SWT.RIGHT);
			fd_normCb.bottom = new FormAttachment(lblNormalize, 2, SWT.BOTTOM);
			normCb.setLayoutData(fd_normCb);
			normCb.select(1);
			
			nuTxt = new Text(composite_1, SWT.BORDER);
			nuTxt.setText("0.5");
			FormData fd_nuTxt = new FormData();
			fd_nuTxt.top = new FormAttachment(lblNu,-1,SWT.TOP);
			fd_nuTxt.left = new FormAttachment(lblNu, 10);
			fd_nuTxt.right = new FormAttachment(kernelCb,0,SWT.RIGHT);
			fd_nuTxt.bottom = new FormAttachment(lblNu, 2, SWT.BOTTOM);
			nuTxt.setLayoutData(fd_nuTxt);
			
			Combo probCb = new Combo(composite_1, SWT.NONE);
			probCb.setItems(new String[] {"False", "True"});
			FormData fd_probCb = new FormData();
			fd_probCb.top = new FormAttachment(lblProbability,-1,SWT.TOP);	
			fd_probCb.left = new FormAttachment(lblProbability, 10);
			fd_probCb.right = new FormAttachment(kernelCb,0,SWT.RIGHT);
			fd_probCb.bottom = new FormAttachment(lblProbability, 2, SWT.BOTTOM);
			probCb.setLayoutData(fd_probCb);
			probCb.select(1);
			
			seedTxt = new Text(composite_1, SWT.BORDER);
			seedTxt.setText("1");
			FormData fd_seedTxt = new FormData();
			fd_seedTxt.top = new FormAttachment(lblSeed, -1,SWT.TOP);
			fd_seedTxt.left = new FormAttachment(lblSeed, 10);
			fd_seedTxt.right = new FormAttachment(kernelCb,0,SWT.RIGHT);
			fd_seedTxt.bottom = new FormAttachment(lblSeed, 2, SWT.BOTTOM);
			seedTxt.setLayoutData(fd_seedTxt);
			
			Combo shrinkCb = new Combo(composite_1, SWT.NONE);
			shrinkCb.setItems(new String[] {"Fasle", "True"});
			FormData fd_shrinkCb = new FormData();
			fd_shrinkCb.top = new FormAttachment(lblShrinking, -1, SWT.TOP);
			fd_shrinkCb.left = new FormAttachment(lblShrinking, 10);
			fd_shrinkCb.right = new FormAttachment(kernelCb,0,SWT.RIGHT);
			kernelCb.select(2);
			fd_shrinkCb.bottom = new FormAttachment(lblShrinking, 2, SWT.BOTTOM);
			shrinkCb.setLayoutData(fd_shrinkCb);
			shrinkCb.select(1);
			
			Composite composite_3 = new Composite(sashForm, SWT.NONE);
			
			Button paramOKBtn = new Button(composite_3, SWT.NONE);
			paramOKBtn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					collectParams();
					
				}
			});
			paramOKBtn.setBounds(90, 10, 112, 20);
			paramOKBtn.setText("确定");
			
			Button paramCancelBtn = new Button(composite_3, SWT.NONE);
			paramCancelBtn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					shell.close();
				}
			});
			paramCancelBtn.setText("取消");
			paramCancelBtn.setBounds(312, 10, 106, 20);
		
		
		sashForm.setWeights(new int[] {1, 3, 12, 1});

	}

    protected void createExternalModel(Composite composite_2,String modelName){
    	titlelbl.setText(modelName+" Model");
    	Label lblExternalModel = new Label(composite_2, SWT.NONE);
		FormData fd_lblExternalModel = new FormData();
		//fd_lblExternalModel.bottom = new FormAttachment(0, 27);
		//fd_lblExternalModel.right = new FormAttachment(0, 71);
		fd_lblExternalModel.top = new FormAttachment(0, 10);
		fd_lblExternalModel.left = new FormAttachment(0, 10);
		lblExternalModel.setLayoutData(fd_lblExternalModel);
		lblExternalModel.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 11, SWT.NORMAL));
		int index=modelName.indexOf("+");
		if(index>0){
		modelName=modelName.substring(0,index);
		}
		lblExternalModel.setText("ExternalModel:"+modelName);
	
		switch (modelName) {
		case "SVM":
			break;
		case "Smote":
			Label lblNear = new Label(composite_2, SWT.NONE);
			FormData fd_lblNear = new FormData();
			fd_lblNear.right = new FormAttachment(0, 174);
			fd_lblNear.top = new FormAttachment(0, 40);
			fd_lblNear.left = new FormAttachment(0, 45);
			lblNear.setLayoutData(fd_lblNear);
			lblNear.setAlignment(SWT.RIGHT);
			lblNear.setText("nearestNeighbours");
			
			nearNeighsTxt = new Text(composite_2, SWT.BORDER);
			 fd_nearNeighsTxt = new FormData();
			fd_nearNeighsTxt.right = new FormAttachment(lblNear,285,SWT.RIGHT);
			fd_nearNeighsTxt.top = new FormAttachment(lblNear,-1,SWT.TOP);
			fd_nearNeighsTxt.left = new FormAttachment(lblNear,10,SWT.RIGHT);
			fd_nearNeighsTxt.bottom=new FormAttachment(lblNear,2,SWT.BOTTOM);
			nearNeighsTxt.setLayoutData(fd_nearNeighsTxt);
			nearNeighsTxt.setText("5");
			
			Label lblPer = new Label(composite_2, SWT.NONE);
			FormData fd_lblPer = new FormData();
			fd_lblPer.right = new FormAttachment(lblNear,0,SWT.RIGHT);
			fd_lblPer.top = new FormAttachment(lblNear,28,SWT.TOP);
			fd_lblPer.left = new FormAttachment(lblNear,0,SWT.LEFT);
			lblPer.setLayoutData(fd_lblPer);
			lblPer.setAlignment(SWT.RIGHT);
			lblPer.setText("percentage");
			
			perTxt = new Text(composite_2, SWT.BORDER);
			FormData fd_perTxt = new FormData();
			fd_perTxt.right = new FormAttachment(lblPer,285,SWT.RIGHT);
			fd_perTxt.top = new FormAttachment(lblPer,1,SWT.TOP);
			fd_perTxt.left = new FormAttachment(lblPer,10,SWT.RIGHT);
			fd_perTxt.bottom=new FormAttachment(lblPer, 2,SWT.BOTTOM);
			perTxt.setLayoutData(fd_perTxt);
			perTxt.setText("100");
			
			Label lblRandomseed = new Label(composite_2, SWT.NONE);
			FormData fd_lblRandomseed = new FormData();
			fd_lblRandomseed.right = new FormAttachment(lblPer,0,SWT.RIGHT);
			fd_lblRandomseed.top = new FormAttachment(lblPer,28,SWT.TOP);
			fd_lblRandomseed.left = new FormAttachment(lblPer,0,SWT.LEFT);
			lblRandomseed.setLayoutData(fd_lblRandomseed);
			lblRandomseed.setAlignment(SWT.RIGHT);
			lblRandomseed.setText("randomSeed");
			
			ranSeedTxt = new Text(composite_2, SWT.BORDER);
			FormData fd_ranSeedTxt = new FormData();
			fd_ranSeedTxt.right = new FormAttachment(lblRandomseed,285,SWT.RIGHT);
			fd_ranSeedTxt.top = new FormAttachment(lblRandomseed,1,SWT.TOP);
			fd_ranSeedTxt.left = new FormAttachment(lblRandomseed,10,SWT.RIGHT);
			fd_ranSeedTxt.bottom=new FormAttachment(lblRandomseed, 2, SWT.BOTTOM);
			ranSeedTxt.setLayoutData(fd_ranSeedTxt);
			//设置随机种子，需要参数传递
			ranSeedTxt.setText("");
			break;
		case "AdaBoost":
		case "BoostSampling":
			Label lblUnderSampleIter = new Label(composite_2, SWT.NONE);
			FormData fd_lblUnderSampleIter = new FormData();
			fd_lblUnderSampleIter.right = new FormAttachment(0, 174);
			fd_lblUnderSampleIter.top = new FormAttachment(0, 50);
			fd_lblUnderSampleIter.left = new FormAttachment(0, 45);
			lblUnderSampleIter.setLayoutData(fd_lblUnderSampleIter);
			lblUnderSampleIter.setAlignment(SWT.RIGHT);
			lblUnderSampleIter.setText("iter");
			
			nearNeighsTxt = new Text(composite_2, SWT.BORDER);
		    fd_nearNeighsTxt = new FormData();
			fd_nearNeighsTxt.right = new FormAttachment(lblUnderSampleIter,285,SWT.RIGHT);
			fd_nearNeighsTxt.top = new FormAttachment(lblUnderSampleIter,-1,SWT.TOP);
			fd_nearNeighsTxt.left = new FormAttachment(lblUnderSampleIter,10,SWT.RIGHT);
			fd_nearNeighsTxt.bottom=new FormAttachment(lblUnderSampleIter,2,SWT.BOTTOM);
			nearNeighsTxt.setLayoutData(fd_nearNeighsTxt);
			nearNeighsTxt.setText("10");
			break;
		default:
			break;
		}
		
    }
    //将控件上的文本参数信息收集起来，返回给Mainframe
    protected void collectParams(){
    	//要赋给text的值
    	StringBuffer strParam=new StringBuffer();
    	switch (modelName) {
		case "SVM":
			strParam.append("weak Classifier:SVM ");
			break;
       case "AdaBoost+SVM":	
       case "BoostSampling":
    	   strParam.append("-R ");
    	   strParam.append(nearNeighsTxt.getText().trim());
           strParam.append(" weak Classifier:SVM ");
	        break;
       case "Smote+SVM":
    	   strParam.append("-S ");
    	   strParam.append(ranSeedTxt.getText().trim());
    	   strParam.append(" -P ");
    	   strParam.append(perTxt.getText().trim());
    	   strParam.append(" -K ");
    	   strParam.append(nearNeighsTxt.getText().trim());
    	   strParam.append(" weak Classifier:SVM ");
    	   break;
		default:
			break;
		}
    	strParam.append("-S ");
    	strParam.append(svmTypeCb.getSelectionIndex());
    	strParam.append(" -K " );
    	strParam.append(kernelCb.getSelectionIndex());
    	strParam.append(" -D ");
    	strParam.append(degreeTxt.getText().trim());
    	strParam.append(" -G ");
    	strParam.append(gammaTxt.getText().trim());
    	strParam.append(" -R ");
    	strParam.append(coefTxt.getText().trim());
    	strParam.append(" -N ");
    	strParam.append(nuTxt.getText().trim());
    	strParam.append(" -M 40.0");
    	strParam.append(" -C ");
    	strParam.append(costTxt.getText().trim());
    	strParam.append(" -E ");
    	strParam.append(epsTxt.getText().trim());
    	strParam.append(" -P ");
    	strParam.append(lossTxt.getText().trim());
    	strParam.append(" -Z -B");
    	strParam.append(" -seed ");
    	strParam.append(seedTxt.getText().trim());
    	paramText.setText(strParam.toString());
    	shell.close();
    }
}
