package project.nlp.sentimentextract;

import java.util.List;

public class AspectSentimentTuple {
	private String aspect;
	private String sentiment;
	
	public AspectSentimentTuple(String aspect,String sentiment){
		this.aspect = aspect;
		this.sentiment = sentiment;
	}
	
	public String getAspect() {
		return aspect;
	}
	public String getSentiment() {
		return sentiment;
	}
	
	public static boolean containsTuple(String aspect,String sentiment,List<AspectSentimentTuple> tupleList){
		for(AspectSentimentTuple tuple:tupleList){
			if(aspect.equals(tuple.getAspect()) && sentiment.equals(tuple.getSentiment()) ) return true;
		}
		return false;
	}
}
