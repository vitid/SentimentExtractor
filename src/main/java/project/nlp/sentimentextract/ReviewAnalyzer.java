package project.nlp.sentimentextract;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.semgrex.SemgrexMatcher;
import edu.stanford.nlp.semgraph.semgrex.SemgrexPattern;
import edu.stanford.nlp.simple.*;
import project.nlp.sentimentextract.rule.RuleManager;
public class ReviewAnalyzer {
	
	private static final Logger logger = LogManager.getLogger(RuleManager.class);
	private Document reviewDocument;
	private RuleManager ruleManager;
	
	public ReviewAnalyzer(String reviewContent,RuleManager ruleManager){
		reviewDocument = new Document(reviewContent);
		this.ruleManager = ruleManager; 
	}
	
	public ArrayList<AspectSentimentTuple> extractAspectSentimentExpression(){
		ArrayList<AspectSentimentTuple> tupleList = new ArrayList<AspectSentimentTuple>();
		
		
		for(Sentence sentence: reviewDocument.sentences()){
			//Should skip this sentence if it doesn't contain any aspects
			
			SemanticGraph dependencyGraph = sentence.dependencyGraph();
			for(String word:sentence.words()){
				if(ruleManager.getAspectList().contains(word)){
					for(String rule:ruleManager.getRuleList()){
						String finaleRule = ruleManager.parseRule(rule, word);
						SemgrexMatcher matcher = SemgrexPattern.compile(finaleRule).matcher(dependencyGraph);
						while(matcher.findNextMatchingNode()){
							IndexedWord matchNode = matcher.getMatch();
							String sentimentExpression = matchNode.originalText();
							AspectSentimentTuple tuple = new AspectSentimentTuple(word, sentimentExpression);
							tupleList.add(tuple);
						}
					}
				}
			}
		}
		return tupleList;
	}
}
