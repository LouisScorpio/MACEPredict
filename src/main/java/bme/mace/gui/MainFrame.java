package bme.mace.gui;


import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Group;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;

import bme.mace.logicdomain.AdaBoost_SVM;
import bme.mace.logicdomain.FindKNeighbours;
import bme.mace.logicdomain.Model;
import bme.mace.logicdomain.ProposedAlgorithm;
import bme.mace.logicdomain.SVM;
import bme.mace.logicdomain.Smote_SVM;
import bme.mace.sample.GetTotalSample;
import weka.core.Instance;
import weka.core.Instances;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Label;

public class MainFrame {

	protected Shell shell;
	private Text patInfoText;
	private Table patInfoTable;
	private Text mxTrainDataText;
	//private Instances mxxlTrainDataSet;
	private String mxxlOpenPath="";
	//private TreeItem trtmSvm;
	private  TreeItem mxxlSelectItem=null;
	private Model model=null;
	private Text mxxlResult;
	private Instances patInstance=null;
	private Instances patTestPats=null;
	private Text flResultTxt;
	Display display =null;
	private String basePath;
	private Text paramSetTxt;
	private ParamSet paramSet;
   	private Text kCountTxt;
   	private Table neighboursInfoTable;
   	private Text testPatsTxt;
   	private String patInfoSetsPath;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainFrame window = new MainFrame();
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
		basePath = MainFrame.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		
		try {
			basePath=URLDecoder.decode(basePath,"UTF-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	

	//if(basePath.endsWith(".jar")){
		basePath=basePath.substring(1,basePath.lastIndexOf("/")+1);
	//}
	System.out.println(basePath);
		shell = new Shell(SWT.CLOSE);
		shell.setSize(1300, 800);
	//	shell.setSize(803, 420);
		setWindowsMax();
		//shell.setSize(688, 433);
		shell.setText("心血管数据分析");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		
		TabItem hzsjTab = new TabItem(tabFolder, SWT.NONE);
		hzsjTab.setText("患者数据");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		hzsjTab.setControl(composite);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		SashForm sashForm = new SashForm(composite, SWT.VERTICAL);
		
		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		
		patInfoText = new Text(composite_1, SWT.BORDER);
		patInfoText.setLocation(10, 10);
		patInfoText.setSize(663, 23);
		
		Button importPatBtn = new Button(composite_1, SWT.NONE);
		importPatBtn.setLocation(707, 10);
		importPatBtn.setSize(98, 23);
		importPatBtn.addSelectionListener(new SelectionAdapter() {
			//导入患者
			@Override
			public void widgetSelected(SelectionEvent e) {
        		FileDialog fileDialog=new FileDialog(shell,SWT.OPEN);
				fileDialog.setFilterExtensions(new String[]{"*.arff","*.csv"});
				
				fileDialog.setFilterPath(basePath+"data");
				System.out.println(basePath+"data");
				String open = fileDialog.open();
				System.out.println(open);
				if(open==null){
					return;
				}
				patInfoText.setText(open);
				new Thread(){
					public void run(){
						
						try {
							 patInstance = GetTotalSample.generateTotalSample(open);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}.start();
				
				//Instances sample=null;			
				new Thread(){
					public void run() {
						display.syncExec(new Runnable() {
							
							@Override
							public void run() {
								while(true){
									 if(patInstance!=null){
										// System.out.println(patInstance);
										 break;
									 }
								 }
								Instance pat=patInstance.instance(0);
								//System.out.println(pat);
								for(int i=0;i<pat.classIndex()+1;i++){
									TableItem item= new TableItem(patInfoTable, SWT.NONE);
									if(i==pat.classIndex()){
										item.setText(new String[]{"class",pat.value(i)==0.0?"阴性":"阳性"} );
									}else{
									item.setText(new String[]{patInstance.attribute(i).name(),String.valueOf(pat.value(i))} );
									}
								}
								
							}
						});
					};
				}.start();
				 
					 }
		});
		importPatBtn.setText("导入患者");
		
		Composite composite_2 = new Composite(sashForm, SWT.BORDER);
		composite_2.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		SashForm sashForm_2 = new SashForm(composite_2, SWT.NONE);
		
		Group group_1 = new Group(sashForm_2, SWT.NONE);
		group_1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 11, SWT.NORMAL));
		group_1.setText("\u5BFC\u5165\u60A3\u8005\u7279\u5F81\u4FE1\u606F");
		group_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		patInfoTable = new Table(group_1, SWT.BORDER | SWT.FULL_SELECTION);
		patInfoTable.addSelectionListener(new SelectionAdapter() {
			//当选择一列时，最近邻信息也被选择
			@Override
			public void widgetSelected(SelectionEvent e) {
			     int selectionIndex = patInfoTable.getSelectionIndex();
			     neighboursInfoTable.select(selectionIndex);  
			}
		});
		patInfoTable.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		patInfoTable.setHeaderVisible(true);
		patInfoTable.setLinesVisible(true);
		
		TableColumn featuresCol = new TableColumn(patInfoTable, SWT.LEFT);
		featuresCol.setMoveable(true);
		featuresCol.setWidth(200);
		featuresCol.setText("特征");
		
		TableColumn valueCol = new TableColumn(patInfoTable, SWT.LEFT);
		valueCol.setWidth(130);
		valueCol.setText("值");
		
		SashForm sashForm_3 = new SashForm(sashForm_2, SWT.VERTICAL);
		
		Composite composite_4 = new Composite(sashForm_3, SWT.NONE);
		
		kCountTxt = new Text(composite_4, SWT.BORDER);
		kCountTxt.setText("5");
		kCountTxt.setBounds(800, 25, 42, 23);
		
		Label kCountLbl = new Label(composite_4, SWT.RIGHT);
		kCountLbl.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		kCountLbl.setBounds(679, 26, 115, 22);
		kCountLbl.setText("\u60A3\u8005\u6700\u8FD1\u90BB\u4E2A\u6570\uFF1A");
		
		Button findKMBtn = new Button(composite_4, SWT.NONE);
		findKMBtn.addSelectionListener(new SelectionAdapter() {
			//最近邻患者特征信息查询
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//如果患者测试集没有导入
				if(testPatsTxt.getText().trim().equals("") || patInfoText.getText().trim().equals("")){
					MessageBox box=new MessageBox(shell);
					 box.setText("出错");
					 box.setMessage("还没有选择完测试数据！");
					 box.open();
					 return;
				}
				//先删除原有内容
				neighboursInfoTable.removeAll();
				//删除列
				TableColumn[] tableColumns = neighboursInfoTable.getColumns();
				for(int i=1;i<tableColumns.length;i++){
					tableColumns[i].dispose();
				}
				int KNN=Integer.valueOf(kCountTxt.getText().trim());
				try {
					patTestPats=GetTotalSample.generateTotalSample(patInfoSetsPath);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Instance target=patInstance.instance(0);
				Instances kNeighbour=null;
				try {
					kNeighbour= FindKNeighbours.getKNeighbour(patTestPats, target, KNN);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//将获得的近邻患者加到table中
				for(int i=0;i<kNeighbour.numInstances();i++){
					TableColumn column=new TableColumn(neighboursInfoTable, SWT.NONE);
					column.setWidth(110);
					column.setText("患者No."+String.valueOf((i+1)));
				}
				for(int j=0;j<target.numAttributes();j++){
					TableItem item=new TableItem(neighboursInfoTable, SWT.NONE);
					String[] itemString=new String[kNeighbour.numInstances()+1];
					itemString[0]=target.attribute(j).name();
					if(j==target.numAttributes()-1){
						for(int i=0;i<kNeighbour.numInstances();i++){
							itemString[i+1]=kNeighbour.instance(i).value(j)==0.0?"阴性":"阳性" ;
						}
					}else{
					for(int i=0;i<kNeighbour.numInstances();i++){
						itemString[i+1]=String.valueOf(kNeighbour.instance(i).value(j));
					}
					}
					item.setText(itemString);
				}
				
			}
		});
		findKMBtn.setBounds(848, 23, 80, 27);
		findKMBtn.setText("\u67E5\u8BE2");
		
		testPatsTxt = new Text(composite_4, SWT.BORDER);
		testPatsTxt.setBounds(10, 25, 504, 23);
		
		Button testPatsBtn = new Button(composite_4, SWT.NONE);
		testPatsBtn.addSelectionListener(new SelectionAdapter() {
			//导入测试患者集
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				FileDialog fileDialog=new FileDialog(shell,SWT.OPEN);
				fileDialog.setFilterExtensions(new String[]{"*.arff","*.csv"});
				
				fileDialog.setFilterPath(basePath+"data");
				System.out.println(basePath+"data");
				patInfoSetsPath= fileDialog.open();
				System.out.println(patInfoSetsPath);
				if(patInfoSetsPath==null){
					return;
				}
				testPatsTxt.setText(patInfoSetsPath);
			}
		});
		testPatsBtn.setBounds(529, 23, 96, 27);
		testPatsBtn.setText("\u5BFC\u5165\u5BF9\u6BD4\u60A3\u8005\u96C6");
		
		Group group_2 = new Group(sashForm_3, SWT.NONE);
		group_2.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 11, SWT.NORMAL));
		group_2.setText("\u6700\u8FD1\u90BB5\u4E2A\u60A3\u8005\u7279\u5F81\u4FE1\u606F");
		group_2.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		neighboursInfoTable = new Table(group_2, SWT.BORDER | SWT.FULL_SELECTION);
		neighboursInfoTable.addSelectionListener(new SelectionAdapter() {
			//当近邻选择一列时，测试pat也被选择
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int index=neighboursInfoTable.getSelectionIndex();
				patInfoTable.select(index);
			}
		});
		neighboursInfoTable.setHeaderVisible(true);
		neighboursInfoTable.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(neighboursInfoTable, SWT.NONE);
		tblclmnNewColumn.setWidth(200);
		tblclmnNewColumn.setText("\u7279\u5F81");
		sashForm_3.setWeights(new int[] {1, 8});
		sashForm_2.setWeights(new int[] {4, 11});
		sashForm.setWeights(new int[] {1, 11});
		
		TabItem flycTab = new TabItem(tabFolder, SWT.NONE);
		flycTab.setText("分类预测");
		
		SashForm flTabSash = new SashForm(tabFolder, SWT.BORDER | SWT.SMOOTH | SWT.VERTICAL);
		flycTab.setControl(flTabSash);
		
		Composite flToolComp = new Composite(flTabSash, SWT.NONE);
		
		
		
		SashForm flMainSash = new SashForm(flTabSash, SWT.NONE);
		
		Group mxxzGroup = new Group(flMainSash, SWT.BORDER);
		mxxzGroup.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 11, SWT.NORMAL));
		mxxzGroup.setText("模型选择");
		mxxzGroup.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Tree mxxzTree = new Tree(mxxzGroup, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		mxxzTree.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		mxxzTree.setLinesVisible(true);
		
		TreeItem trtmSVM = new TreeItem(mxxzTree, SWT.NONE);
		trtmSVM.setText("SVM");
		
		TreeItem trtmAdaSVM = new TreeItem(mxxzTree, SWT.NONE);
		trtmAdaSVM.setText("AdaBoost+SVM");
		
		TreeItem trtmSmoteSVM = new TreeItem(mxxzTree, SWT.NONE);
		trtmSmoteSVM.setText("Smote+SVM");
		
		TreeItem trtmProposedAlgorithm = new TreeItem(mxxzTree, SWT.NONE);
		trtmProposedAlgorithm.setText("BoostSampling");
		
		Group fljgGroup = new Group(flMainSash, SWT.NONE);
		fljgGroup.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 11, SWT.NORMAL));
		fljgGroup.setText("分类结果");
		fljgGroup.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		flResultTxt = new Text(fljgGroup, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.MULTI);
		flMainSash.setWeights(new int[] {1, 2});
		flToolComp.setLayout(null);
		
		Button flStartBtn = new Button(flToolComp, SWT.BORDER);
		flStartBtn.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		flStartBtn.setBounds(28, 13, 221, 23);
		flStartBtn.addSelectionListener(new SelectionAdapter() {
	
			//开始预测
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//如果没有病人
				if(patInstance==null){
					MessageBox box=new MessageBox(shell,SWT.ICON_ERROR|SWT.YES|SWT.NO);
				    box.setText("警告");
				    box.setMessage("还未载入测试病人！");
				    box.open();
					return;
				}
				//清空预测结果
				flResultTxt.setText("");
				//一定要选择一个模型
				boolean ifchecked=false;
				for(TreeItem item : mxxzTree.getItems()){
					if(item.getChecked()==true){
						ifchecked=true;
						break;
					}	
				}
				if(ifchecked==false){
					MessageBox box=new MessageBox(shell,SWT.ICON_ERROR|SWT.YES|SWT.NO);
				    box.setText("警告");
				    box.setMessage("请选择模型！");
				    box.open();
				}else{
					String startString="载入模型,正在预测，请稍后……";
					StringBuffer flResultString = new StringBuffer();
					flResultTxt.setText(startString);
					TreeItem[] items=mxxzTree.getItems();
					double[] evas=new double[items.length];
					boolean[] ifChecked=new boolean[items.length];
					String[] itemText=new String[items.length];
					for(int i=0;i<ifChecked.length;i++){
						ifChecked[i]=items[i].getChecked();
						itemText[i]=items[i].getText();
						evas[i]=9999;
					}
					//Boolean ifDone=false;
					class Param{
						public boolean ifDone=false;
						public double[] evas;
						public boolean[] ifChecked;
						public String[] itemText;
						public Text flResultTxt;
						public StringBuffer flResultString;
						public Param(double[] evas,boolean[] ifChecked,String[] itemText,Text flResultTxt,StringBuffer flResultString) {
							this.ifDone=false;
							this.evas=evas;
							this.ifChecked=ifChecked;
							this.itemText=itemText;
							this.flResultTxt=flResultTxt;
							this.flResultString=flResultString;
						}
					}
					Param param=new Param(evas, ifChecked, itemText, flResultTxt,flResultString);
					
				//载入模型
			    new Thread(){
			    	public void run() {
			    		synchronized (param) {
			    			if(patInstance==null){
			    				return ;
			    			}
						for(int i=0;i<param.evas.length;i++){
							if(param.ifChecked[i]){
								switch (param.itemText[i]) {
								case "SVM":
									SVM svm=(SVM) Model.loadModel(basePath+"model/SVM.model");
									if(svm!=null){
										param.evas[i] =svm.evaluateInstance(patInstance);
										param.flResultString.append("******************\nSVM分类结果：").append(param.evas[i]==0?"阴性":"阳性").append("\n*************************\n\n");
									}
									break;
								case "AdaBoost+SVM":
									AdaBoost_SVM adaBoost_SVM =(AdaBoost_SVM) Model.loadModel(basePath+"model/AdaBoost_SVM.model");
									if(adaBoost_SVM!=null){
										param.evas[i]= adaBoost_SVM.evaluateInstance(patInstance);
										//System.out.println(res);
										param.flResultString.append("******************\nAdaBoost+SVM分类结果：").append(param.evas[i]==0?"阴性":"阳性").append("\n*************************\n\n");
									}
									break;
								case "Smote+SVM":
									Smote_SVM smote_SVM=(Smote_SVM)Model.loadModel(basePath+"model/Smote_SVM.model");
									if(smote_SVM!=null){
										param.evas[i] = smote_SVM.evaluateInstance(patInstance);
									
										param.flResultString.append("******************\nSmote+SVM分类结果：").append(param.evas[i]==0?"阴性":"阳性").append("\n*************************\n\n");
									}
									break;
								case "BoostSampling":
									ProposedAlgorithm proposedAlgorithm=(ProposedAlgorithm)Model.loadModel(basePath+"model/BoostSampling.model");
									if(proposedAlgorithm!=null){
										param.evas[i] = proposedAlgorithm.evaluateInstance(patInstance);
									
										param.flResultString.append("******************\nBoostSampling分类结果：").append(param.evas[i]==0?"阴性":"阳性").append("\n*************************\n\n");
									}
									break;
								default:
									break;
								}
							}
						}
						param.ifDone=true;
						param.notify();
			    	}		
			    	}
			    	}.start();

		new Thread(){
				  public void run() {
					  display.syncExec(new Runnable() {						
						@Override
						public void run() {
							while(true){	
								synchronized (param) {
									if(param.ifDone==false){
										try {
											param.wait();
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}else {
										param.flResultTxt.setText(param.flResultString.toString());
										break;
									}
								}
							}
						}
					});
				  };
			  }.start();
				
				}
				
				
			}
		});
		flStartBtn.setText("开始预测");
		
		Button flSaveBtn = new Button(flToolComp, SWT.BORDER);
		flSaveBtn.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		flSaveBtn.setBounds(298, 13, 221, 23);
		flSaveBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//保存结果
				FileDialog fileDialog=new FileDialog(shell,SWT.SAVE);
				//fileDialog.setFilterExtensions(new String[]{".txt"});
				fileDialog.setFilterPath("./data");
				String savePath = fileDialog.open();
				if(savePath==null){
					return ;
				}else {
					FileWriter writer;
					try {
						writer = new FileWriter(savePath);
						writer.write(flResultTxt.getText());
						writer.close();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					MessageBox box=new MessageBox(shell);
					 box.setText("恭喜");
					    box.setMessage("保存成功！");
					    box.open();
				}
					
					
				} 
				
			
		});
		flSaveBtn.setText("保存结果");
		flTabSash.setWeights(new int[] {28, 322});
		
		
		
		TabItem mxxlTab = new TabItem(tabFolder, SWT.NONE);
		mxxlTab.setText("模型训练");
		
		SashForm mxTabSash = new SashForm(tabFolder, SWT.BORDER | SWT.SMOOTH | SWT.VERTICAL);
		mxxlTab.setControl(mxTabSash);
		
		Composite mxToolComp = new Composite(mxTabSash, SWT.NONE);
		
		Composite mxMainComp = new Composite(mxTabSash, SWT.NONE);
		mxMainComp.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		SashForm mxMainSash = new SashForm(mxMainComp, SWT.NONE);
		
		Group xlmxGroup = new Group(mxMainSash, SWT.NONE);
		xlmxGroup.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 11, SWT.NORMAL));
		xlmxGroup.setText("训练模型");
		xlmxGroup.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Tree mxxlTree = new Tree(xlmxGroup, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		mxxlTree.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		
		//只能选一个
		mxxlTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			TreeItem item=	(TreeItem) e.item;
			StringBuffer modelParam=new StringBuffer();
			if(item.getChecked()){
				mxxlSelectItem=item;
				for(TreeItem treeItem:mxxlTree.getItems()){
					if(!treeItem.getText().equals(item.getText())){
						treeItem.setChecked(false);
					}
				}
				//参数设定框
				
				switch (item.getText().trim()) {
				case "SVM":
					//paramSetTxt.setText("");
					break;
				case "AdaBoost+SVM":
					modelParam.append("-R 10");
					break;
				case "Smote+SVM":
					modelParam.append("-S 1 -P 100.0 -K 5");
					break;
				case "BoostSampling":
					modelParam.append("-R 10");
					break;
				default:
					break;
				}
				modelParam.append(" weak Classifier:SVM ");
				modelParam.append("-S 0 -K 2 -D 3 -G 0.45 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.001 -P 0.1 -Z -B -seed 1");
			    paramSetTxt.setText(modelParam.toString());
			}else {
				modelParam.delete(0, modelParam.length());
				paramSetTxt.setText("");
			}
			}
		});
		mxxlTree.setLinesVisible(true);
		
		TreeItem trtmSvm = new TreeItem(mxxlTree, SWT.NONE);
		trtmSvm.setText("SVM");
		
		TreeItem trtmAdaboostsvm = new TreeItem(mxxlTree, SWT.NONE);
		trtmAdaboostsvm.setText("AdaBoost+SVM");
		
		TreeItem trtmSmotesvm = new TreeItem(mxxlTree, SWT.NONE);
		trtmSmotesvm.setText("Smote+SVM");
		
		TreeItem trtmProposedAlgorithm_1 = new TreeItem(mxxlTree, SWT.NONE);
		trtmProposedAlgorithm_1.setText("BoostSampling");
		
		SashForm sashForm_1 = new SashForm(mxMainSash, SWT.VERTICAL);
		
		Composite composite_3 = new Composite(sashForm_1, SWT.NONE);
		composite_3.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group group = new Group(composite_3, SWT.NONE);
		group.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 11, SWT.NORMAL));
		group.setText("\u53C2\u6570\u8BBE\u5B9A");
		group.setLayout(null);
		
		paramSetTxt = new Text(group, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		paramSetTxt.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		paramSetTxt.addMouseListener(new MouseAdapter() {
			//参数设定窗口
			@Override
			public void mouseDown(MouseEvent arg0) {
				if(paramSetTxt.getText().length()<=0){
					return;
				}
				paramSet.setParamText(paramSetTxt,mxxlSelectItem.getText().trim());
				paramSet.main(new String[1]);
			}
		});
		paramSetTxt.setBounds(10, 35, 838, 31);
		
		Group xljgGroup = new Group(sashForm_1, SWT.NONE);
		xljgGroup.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 11, SWT.NORMAL));
		xljgGroup.setText("训练结果");
		xljgGroup.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		mxxlResult = new Text(xljgGroup, SWT.BORDER);
		mxxlResult.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 11, SWT.NORMAL));
		mxxlResult.setEditable(false);
		sashForm_1.setWeights(new int[] {1, 6});
		mxMainSash.setWeights(new int[] {1, 4});
		mxTabSash.setWeights(new int[] {1, 7});
		mxToolComp.setLayout(null);

		
		
		Button mxImportBtn = new Button(mxToolComp, SWT.BORDER);
		mxImportBtn.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		mxImportBtn.setBounds(32, 10, 220, 24);
		mxImportBtn.addSelectionListener(new SelectionAdapter() {
			//训练模型
			@Override
			public void widgetSelected(SelectionEvent e) {
				//打开FileDialog
			FileDialog fileDialog=new FileDialog(shell,SWT.OPEN);
			//System.out.println(System.getProperty("user.dir"));
			fileDialog.setFilterPath(basePath+"data/");
			System.out.println(basePath+"data/");
			String[] filterExt = { "*.arff", "*.csv"};
			fileDialog.setFilterExtensions(filterExt);
			//写入训练集地址
			
			mxxlOpenPath = fileDialog.open();
			if(mxxlOpenPath==null){
				return;
			}
			mxTrainDataText.setText(mxxlOpenPath);
			
			}
		});
		mxImportBtn.setText("导入数据集");
		
		Button mxStartBtn = new Button(mxToolComp, SWT.BORDER);
		mxStartBtn.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		mxStartBtn.setBounds(290, 10, 220, 24);
		mxStartBtn.addSelectionListener(new SelectionAdapter() {
			
			//选择模型
			@Override
			public void widgetSelected(SelectionEvent e) {
				//如果没有选择模型
				boolean ifchecked=false;
				for(TreeItem item : mxxlTree.getItems()){
					if(item.getChecked()==true){
						ifchecked=true;
						break;
					}	
				}
				if(ifchecked==false || mxTrainDataText.getText().trim().equals("")){
					MessageBox box=new MessageBox(shell,SWT.ICON_ERROR|SWT.YES|SWT.NO);
				    box.setText("警告");
				    box.setMessage("请选择模型或载入数据集！");
				    box.open();
				    return;
				}		
				//获取样本
				Instances mxxlTrainSample=null;
				try {
					 mxxlTrainSample=GetTotalSample.generateTotalSample(mxxlOpenPath);	
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//打印出正在训练、请稍后
				mxxlResult.setText("模型正在训练、请稍后……");
				//获取TreeItem的名字，放入另一个非UI线程
				String modelItemName=mxxlSelectItem.getText();
				class Param{
					public String modelItemName;
					public Text mxxlResult;
					public Instances mxxlTrainSample;
					public boolean ifDone=false;
					public Param(String modelItemName,Text mxxlResult,Instances mxxlTrainSample) {
						this.modelItemName=modelItemName;
						this.mxxlResult=mxxlResult;
						this.mxxlTrainSample=mxxlTrainSample;
					}
				}
				Param param=new Param(modelItemName, mxxlResult,mxxlTrainSample);
				String optionString=paramSetTxt.getText().trim();
				optionString=optionString.replace("weak Classifier:SVM ", "");
				String[] options=optionString.split(" ");
				//开始训练
				new Thread(){
					public void run() {
						synchronized (param) {
							//String[] options=new String[]{"-S","0","-T", "2", "-K","2","-D","3","-G","0.45","-R","0.0","-N","0.5","-M","40.0","-C","1.0","-E","0.001","-P","0.1","-Z","-B","-seed","1"};
							//String[] option=new String[]{"10","-S","0","-T", "2", "-K","2","-D","3","-G","0.45","-R","0.0","-N","0.5","-M","40.0","-C","1.0","-E","0.001","-P","0.1","-Z","-B","-seed","1"};
						
							switch (param.modelItemName) {
							case "SVM":
								model=new SVM();
								model.buildModel(param.mxxlTrainSample, options);
								break;
							case "AdaBoost+SVM":
								model=new AdaBoost_SVM();
								model.buildModel(param.mxxlTrainSample, options);
							    break;
							case "Smote+SVM":
								model=new Smote_SVM();
								model.buildModel(param.mxxlTrainSample, options);
							    break;
							case "BoostSampling":
								model=new ProposedAlgorithm();
								model.buildModel(param.mxxlTrainSample, options);
							    break;
							default:
								break;
							}
							param.ifDone=true;
							param.notify();
						}
					}
				}.start();
				new Thread(){
					public void run() {
						display.syncExec(new Runnable() {
							@Override
							public void run() {
								while(true){
								synchronized (param) {
									if(param.ifDone==false){
										try {
											param.wait();
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}else{
									param.mxxlResult.setText("新的模型"+param.modelItemName+"训练完成！");
									break;
									}
								}
								}
								
							}
						});
					};
					
				}.start();
						}

		});
		mxStartBtn.setText("开始训练");
		
		Button mxSaveBtn = new Button(mxToolComp, SWT.BORDER);
		mxSaveBtn.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		mxSaveBtn.setBounds(538, 10, 220, 24);
		mxSaveBtn.addSelectionListener(new SelectionAdapter() {
			//保存模型
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(mxxlResult.getText().trim().equals("")){
			         MessageBox box=new MessageBox(shell,SWT.ICON_ERROR|SWT.YES|SWT.NO);
		             box.setText("警告");
		             box.setMessage("还未训练出模型！");
		             box.open();
		          }else{
		        	  /*
				FileDialog fileDialog=new FileDialog(shell,SWT.SAVE);
				fileDialog.setFilterExtensions(new String[]{".model"});
				fileDialog.setFilterPath(System.getProperty("user.dir")+"\\src\\main\\resources");
				String savePath = fileDialog.open();
				*/
		        	  TreeItem[] items = mxxlTree.getItems();
		        	  String itemName="";
		        	  for(TreeItem item:items){
		        		  if(item.getChecked()){
		        			  itemName=item.getText();
		        			  break;
		        		  }
		        	  }
		         String savePath=basePath+"model/"+itemName.replace('+', '_')+".model";
			    System.out.println(savePath);
				 Model.saveModel(model, savePath);
				 MessageBox box=new MessageBox(shell,SWT.ICON_WORKING|SWT.YES|SWT.NO);
	             box.setText("成功");
	             box.setMessage("已成功保存模型！");
	             box.open();
				
		          }
			}
		});
		mxSaveBtn.setText("保存模型");
		
		mxTrainDataText = new Text(mxToolComp, SWT.BORDER);
		mxTrainDataText.setBounds(32, 49, 594, 21);
	}
	
	private void setWindowsMax(){
		Rectangle bounds = display.getPrimaryMonitor().getBounds();
        Rectangle rect = shell.getBounds();
        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;
        shell.setLocation(x, y);
	}
}
