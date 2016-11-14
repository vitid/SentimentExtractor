package project.nlp.sentimentextract;

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
}
