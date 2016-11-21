package project.nlp.sentimentextract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.semgraph.semgrex.SemgrexMatcher;
import edu.stanford.nlp.semgraph.semgrex.SemgrexPattern;
import edu.stanford.nlp.simple.*;
import project.nlp.sentimentextract.rule.RuleManager;
public class ReviewAnalyzer {
	
	private static final Logger logger = LogManager.getLogger(ReviewAnalyzer.class);
	private Document reviewDocument;
	private RuleManager ruleManager;
	private boolean isPrintPOS = false;
	
	public ReviewAnalyzer(String reviewContent,RuleManager ruleManager){
		reviewDocument = new Document(reviewContent);
		this.ruleManager = ruleManager; 
	}
	
	public void setPrintPOS(boolean isPrintPOS) {
		this.isPrintPOS = isPrintPOS;
	}

	public ArrayList<AspectSentimentTuple> extractAspectSentimentExpression(){
		ArrayList<AspectSentimentTuple> tupleList = new ArrayList<AspectSentimentTuple>();
		
		
		for(Sentence sentence: reviewDocument.sentences()){
			//Should skip this sentence if it doesn't contain any aspects
			if(Collections.disjoint(sentence.words(),ruleManager.getAspectList())) continue;
			
			SemanticGraph dependencyGraph = sentence.dependencyGraph();
			
			//filter out CONJ in the graph
			SemanticGraph dependencyGraphNoCONJ = new SemanticGraph(dependencyGraph);
			Iterator<SemanticGraphEdge> it = dependencyGraphNoCONJ.edgeIterable().iterator();
			ArrayList<SemanticGraphEdge> removeEdgeList = new ArrayList<>();
			while(it.hasNext()){
				SemanticGraphEdge edge = it.next();
				if("conj".equals(edge.getRelation().getShortName())){
					removeEdgeList.add(edge);
				}
			}
			for(SemanticGraphEdge edge:removeEdgeList){
				dependencyGraphNoCONJ.removeEdge(edge);
			}
			
			for(String word:sentence.words()){
				if(ruleManager.getAspectList().contains(word)){
					for(String rule:ruleManager.getRuleList()){
						String finaleRule = ruleManager.parseRule(rule, word);
						SemgrexMatcher matcher = SemgrexPattern.compile(finaleRule).matcher(dependencyGraphNoCONJ);
						while(matcher.findNextMatchingNode()){

							IndexedWord matchNode = matcher.getMatch();
							String sentimentExpression = this.embalishNode(matchNode, dependencyGraphNoCONJ);
							AspectSentimentTuple tuple = new AspectSentimentTuple(word, sentimentExpression);
							//handle conjunctive clause(if any...)
							Pair<IndexedWord, String> conj = getFirstConjunctiveConnectedNode(matchNode, dependencyGraph);
							if(conj != null){
								tuple.setConj(conj.getValue1());
								tuple.setConjSentiment(this.embalishNode(conj.getValue0(), dependencyGraphNoCONJ));
							}
							logger.info("[Add tuple]"+tuple.toString());
							tupleList.add(tuple);
						}
					}
				}
			}
		}
		return tupleList;
	}
	
	private Pair<IndexedWord,String> getFirstConjunctiveConnectedNode(IndexedWord node,SemanticGraph dependencyGraph){
		for(SemanticGraphEdge edge:dependencyGraph.outgoingEdgeIterable(node)){
			if("conj".equals(edge.getRelation().getShortName())){
				return new Pair(edge.getDependent(),edge.getRelation().getSpecific());
			}
		}
		
		for(SemanticGraphEdge edge:dependencyGraph.incomingEdgeIterable(node)){
			if("conj".equals(edge.getRelation().getShortName())){
				return new Pair(edge.getGovernor(),edge.getRelation().getSpecific());
			}
		}
		return null;
	}
	private String embalishNode(IndexedWord node,SemanticGraph dependencyGraph){
		String tag = node.tag();
		tag = tag.length() > 2 ? tag.substring(0,2):tag;
		switch(tag){
			case "JJ":
				return embalishAdj(node, dependencyGraph);
			case "RB":
				return embalishAdverb(node, dependencyGraph);
			case "NN":
				return embalishNoun(node, dependencyGraph);
			case "VB":
				
		}
		return this.getNodeText(node);
	}
	
	/*
	private String embalishVerb(IndexedWord node,SemanticGraph dependencyGraph){
		String embalishedVerb = "";
		for(SemanticGraphEdge edge:dependencyGraph.outgoingEdgeIterable(node)){
			if("JJ".equalsIgnoreCase(edge.getDependent().tag())){
				embalishedVerb += this.embalishAdj(edge.getDependent(), dependencyGraph) + " ";
			}else if("RB".equalsIgnoreCase(edge.getDependent().tag())){
				embalishedVerb += this.embalishAdverb(edge.getDependent(), dependencyGraph) + " ";
			}
		}
		embalishedVerb += getNodeText(node);
		return embalishedVerb;
	}
	*/
	
	private String embalishNoun(IndexedWord node,SemanticGraph dependencyGraph){
		String embalishedNoun = "";
		for(SemanticGraphEdge edge:dependencyGraph.outgoingEdgeIterable(node)){
			if("JJ".equalsIgnoreCase(edge.getDependent().tag())){
				embalishedNoun += this.embalishAdj(edge.getDependent(), dependencyGraph) + " ";
			}else if("RB".equalsIgnoreCase(edge.getDependent().tag())){
				embalishedNoun += this.embalishAdverb(edge.getDependent(), dependencyGraph) + " ";
			}
		}
		embalishedNoun += this.getNodeText(node);
		return embalishedNoun;
	}
	
	private String embalishAdverb(IndexedWord node,SemanticGraph dependencyGraph){
		String embalishedAdverb = "";
		for(SemanticGraphEdge edge:dependencyGraph.outgoingEdgeIterable(node)){
			//append only Adverb
			if("RB".equalsIgnoreCase(edge.getDependent().tag())){
				embalishedAdverb += this.embalishAdverb(edge.getDependent(), dependencyGraph) + " ";
			}
		}
		embalishedAdverb += this.getNodeText(node);
		return embalishedAdverb;
	}

	private String embalishAdj(IndexedWord node,SemanticGraph dependencyGraph){
		String embalishedAdj = "";
		for(SemanticGraphEdge edge:dependencyGraph.outgoingEdgeIterable(node)){
			//append only Adverb
			if("RB".equalsIgnoreCase(edge.getDependent().tag())){
				embalishedAdj += this.embalishAdverb(edge.getDependent(), dependencyGraph) + " ";
			}
		}
		embalishedAdj += this.getNodeText(node);
		return embalishedAdj;
	}
	
	private String getNodeText(IndexedWord node){
		return isPrintPOS? String.format("%s/%s", node.originalText(),node.tag()) : node.originalText();
	}
}
