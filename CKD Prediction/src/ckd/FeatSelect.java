/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ckd;
import weka.core.Instances;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.attributeSelection.WrapperSubsetEval;
import weka.classifiers.trees.RandomTree;
import weka.attributeSelection.SymmetricalUncertAttributeEval;

import weka.attributeSelection.Ranker;
import weka.core.converters.CSVLoader;
import java.io.File;
/**
 *
 * @author akil
 */
public class FeatSelect 
{
    Details dt=new Details();
    
    FeatSelect()
    {
        
    }
    
    public String filter()
    {
        String sel="";
        try
        {
            CSVLoader csv=new CSVLoader();
    	    csv.setSource(new File(dt.input));			
            Instances data=csv.getDataSet();
            data.setClassIndex(data.numAttributes() - 1);
            
            CfsSubsetEval cfs = new CfsSubsetEval();            
            GreedyStepwise gs = new GreedyStepwise();
            gs.setGenerateRanking(true);
            gs.setConservativeForwardSelection(true);
            gs.setThreshold(0.5);
            gs.setSearchBackwards(true);
            
            AttributeSelection selector = new AttributeSelection();
            selector.setSearch(gs);			
            selector.setEvaluator(cfs);			
            selector.SelectAttributes(data);
            int se[]=selector.selectedAttributes();
            System.out.println(se.length+" : "+data.numAttributes());
            
            for(int i=0;i<se.length;i++)
            {
                sel=sel+data.attribute(se[i]).name()+"\n";
            }
            System.out.println(sel);
            
            dt.fil_inst=selector.reduceDimensionality(data);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sel;
    }
    
    public String wrapper()
    {
        String sel="";
        try
        {
            CSVLoader csv=new CSVLoader();
    	    csv.setSource(new File(dt.input));			
            Instances data=csv.getDataSet();
            data.setClassIndex(data.numAttributes() - 1);
            
            WrapperSubsetEval wrap = new WrapperSubsetEval();
            
            wrap.setClassifier(new RandomTree());
            GreedyStepwise gs = new GreedyStepwise();            
            gs.setConservativeForwardSelection(true);            
            gs.setSearchBackwards(true);
            
            AttributeSelection selector = new AttributeSelection();
            selector.setSearch(gs);			
            selector.setEvaluator(wrap);			
            selector.SelectAttributes(data);
            int se[]=selector.selectedAttributes();
            System.out.println(se.length+" : "+data.numAttributes());
            
            for(int i=0;i<se.length;i++)
            {
                sel=sel+data.attribute(se[i]).name()+"\n";
            }
            System.out.println(sel);
            
            dt.wrap_inst=selector.reduceDimensionality(data);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sel;
    }
    
    public String Embed()
    {
        String sel="";
        try
        {
            CSVLoader csv=new CSVLoader();
    	    csv.setSource(new File(dt.input));			
            Instances data=csv.getDataSet();
            data.setClassIndex(data.numAttributes() - 1);
            
            SymmetricalUncertAttributeEval sm = new SymmetricalUncertAttributeEval();            
            Ranker rn=new Ranker();
            rn.setThreshold(0.2);
            rn.setGenerateRanking(true);
            
            AttributeSelection selector = new AttributeSelection();
            selector.setSearch(rn);			
            selector.setEvaluator(sm);			
            selector.SelectAttributes(data);
            int se[]=selector.selectedAttributes();
            System.out.println(se.length+" : "+data.numAttributes());
            
            for(int i=0;i<se.length;i++)
            {
                sel=sel+data.attribute(se[i]).name()+"\n";
            }
            System.out.println(sel);
            
            dt.em_inst=selector.reduceDimensionality(data);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sel;
    }
}
