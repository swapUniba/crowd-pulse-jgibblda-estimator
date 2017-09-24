package com.github.frapontillo.pulse.crowd.LDA;

import com.github.frapontillo.pulse.crowd.data.entity.Message;
import com.github.frapontillo.pulse.crowd.data.entity.Token;
import com.github.frapontillo.pulse.rx.PulseSubscriber;
import com.github.frapontillo.pulse.spi.IPlugin;
import com.github.frapontillo.pulse.util.PulseLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jgibblda.Estimator;
import jgibblda.LDACmdOption;
import jgibblda.LDADataset;

import org.apache.logging.log4j.Logger;
import rx.Observable;
import rx.Subscriber;



public class CrowdLDAEstimator extends IPlugin<Message, Message, CrowdLDAEstimatorConfig>
{
  public static final String PLUGIN_NAME = "estimator-LDA";
  
  public CrowdLDAEstimator() {}
  
  private Logger logger = PulseLogger.getLogger(CrowdLDAEstimator.class);
  private LDACmdOption ldaOption;
  private List<String> stockList = new ArrayList<String>();
  
  public String getName() {
    return "estimator-LDA";
  }
  
  public CrowdLDAEstimatorConfig getNewParameter() {
    return new CrowdLDAEstimatorConfig();
  }
  
  @Override
  protected Observable.Operator<Message, Message> getOperator(CrowdLDAEstimatorConfig parameters)
  {
	  return new Observable.Operator<Message, Message>() {
      	
          @Override public Subscriber<? super Message> call(Subscriber<? super Message> subscriber) {
              
          	return new PulseSubscriber<Message>(subscriber) {
          		@Override public void onStart() 
            	{

            		super.onStart();
                }

                @Override public void onNext(Message message) 
                {
                    
            		
                    if(stockList.size() < parameters.getSizeTrainingset()){
                    	String s = "";
                    	if(!(message.getTokens() == null || message.getTokens().size() == 0)){
	                		List<Token> t = message.getTokens();
	                		for(int i=0; i<t.size();i++){
	                			if(!t.get(i).isStopWord())
	                				s += t.get(i).getText() + " ";
	                		}
                        }else{
                        	s = message.getText();
                        }
                    	stockList.add(s);
                    }
                    
                    subscriber.onNext(message);
                }

                @Override public void onCompleted() {
                	System.out.println("START ESTIMATING");
                	String modelName=parameters.getModel();
                	
                	final File dir = new File("/opt/crowd-pulse/build/install/crowd-pulse/lib/" + modelName);
                    dir.mkdir();
                    final LDACmdOption ldaOption = new LDACmdOption();
                    ldaOption.inf = true;
                    ldaOption.dir = "/opt/crowd-pulse/build/install/crowd-pulse/lib/" + modelName;
                    ldaOption.modelName = modelName;
                    ldaOption.est = true;
    				ldaOption.niters = parameters.getIterations();
    				ldaOption.K = parameters.getTopic();
    				ldaOption.alpha = parameters.getAlpha();
    				ldaOption.beta = parameters.getBeta();
    				
    				String[] stockArr = new String[stockList.size()];
    		        stockArr = stockList.toArray(stockArr);
    		        
                	Estimator estimator = new Estimator();
    				LDADataset lda = LDADataset.readDataSet(stockArr);
                	estimator.init(ldaOption,lda);
    				estimator.estimate();
    				
                	reportPluginAsCompleted();
                    super.onCompleted();
                }

                @Override public void onError(Throwable e) {
                    reportPluginAsErrored();
                    super.onError(e);
                }
                  
          
			  	
          	};
          }
	  };
  }
}