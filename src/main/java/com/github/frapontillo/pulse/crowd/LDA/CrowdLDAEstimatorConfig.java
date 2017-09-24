package com.github.frapontillo.pulse.crowd.LDA;

import com.github.frapontillo.pulse.spi.PluginConfigHelper;
import com.google.gson.JsonElement;

public class CrowdLDAEstimatorConfig implements com.github.frapontillo.pulse.spi.IPluginConfig<CrowdLDAEstimatorConfig>
{
  public CrowdLDAEstimatorConfig() {}
  
  private String model = "model";
  private int sizeTrainingset = 100;
  private int topic = 100;
  private int iterations = 100;
  private double alpha = -1.0;
  private double beta = -1.0;
  
  
  public CrowdLDAEstimatorConfig buildFromJsonElement(JsonElement json) {
    return (CrowdLDAEstimatorConfig)PluginConfigHelper.buildFromJson(json, CrowdLDAEstimatorConfig.class);
  }
  
  public void setModel(String m) {
    model = m;
  }
  
  public String getModel() {
    return model;
  }
  
  public void setSizeTrainingset(int s) {
    sizeTrainingset = s;
  }
  
  public int getSizeTrainingset() {
    return sizeTrainingset;
  }
  
  public void setTopic(int s) {
    topic = s;
  }
	  
  public int getTopic() {
    return topic;
  }
  
  public void setIterations(int s) {
    iterations = s;
  }
	  
  public int getIterations() {
    return iterations;
  }
  
  public void setAlpha(double a) {
	    alpha = a;
  }
		  
  public double getAlpha() {
	    return alpha;
  }

  public void setBeta(double b) {
		beta = b;
  }
			  
  public double getBeta() {
		return beta;
  }
  
  
}