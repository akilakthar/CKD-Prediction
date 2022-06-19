/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ckd;

import java.util.Random;
import java.text.DecimalFormat;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import java.util.ArrayList;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;

/**
 *
 * @author akil
 */
public class Classification 
{
    Classification()
    {
        
    }
    
    public String classify(Instances data,Classifier cls)
    {
        String res="";
        double acc=0;
        try
        {
            DecimalFormat df=new DecimalFormat("#.##");
            
            data.setClassIndex(data.numAttributes()-1);
            Evaluation eval = new Evaluation(data);
            Random rand = new Random(1);  // using seed = 1
            int folds = 10; //10 fold Cross Validation
            eval.crossValidateModel(cls, data, folds, rand);
            cls.buildClassifier(data);                   
            
            
                        
            double cr=eval.correct();
            double wr=eval.incorrect();
            
            
            double re=eval.weightedRecall()*100;
            double pre=eval.weightedPrecision()*100;
            double f1=eval.weightedFMeasure()*100;
            double acc1=((double)cr/(double)(cr+wr))*100;
            String mat=eval.toMatrixString();
            
            res=df.format(pre)+"#"+df.format(re)+"#"+df.format(f1)+"#"+df.format(acc1)+"#"+mat;                                   
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
    
    public void graph1(ArrayList at,String tit)
    {
        try
        {
            
            String alg[]={"ANN","C4.5","Logistic","SVM","KNN","RandomTree"};
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for(int i=0;i<at.size();i++)
            {
                String g1=at.get(i).toString();
                String g2[]=g1.split("#");
                dataset.setValue(Double.parseDouble(g2[0]), "Precision", alg[i]);
                dataset.setValue(Double.parseDouble(g2[1]), "Recall", alg[i]);
                dataset.setValue(Double.parseDouble(g2[2]), "F-Measure", alg[i]);
                dataset.setValue(Double.parseDouble(g2[3]), "Accuracy", alg[i]);                
            }
            
            JFreeChart chart = ChartFactory.createBarChart  
            (tit,"", "Value (%)", dataset,   
            PlotOrientation.VERTICAL, true,true, false);
  
            chart.getTitle().setPaint(Color.blue); 
  
            CategoryPlot p = chart.getCategoryPlot(); 
  
            p.setRangeGridlinePaint(Color.red);         
    
            CategoryItemRenderer renderer = p.getRenderer();
            renderer.setSeriesPaint(0, Color.red);
            renderer.setSeriesPaint(1, Color.green);
            renderer.setSeriesPaint(2, Color.blue);
            renderer.setSeriesPaint(3, Color.magenta);
             
            ChartFrame frame1=new ChartFrame(tit,chart);
            frame1.setSize(700,400);            
            frame1.setVisible(true);
            
  
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
