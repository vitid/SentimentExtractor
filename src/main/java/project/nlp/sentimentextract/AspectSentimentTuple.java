package project.nlp.sentimentextract;

public class AspectSentimentTuple {
	private String aspect;
	private String sentiment;
	private String conj;
	private String conjSentiment;
	
	public AspectSentimentTuple(String aspect,String sentiment){
		this(aspect,sentiment,"","");
	}
	
	public AspectSentimentTuple(String aspect,String sentiment,String conj,String conjSentiment){
		this.aspect = aspect;
		this.sentiment = sentiment;
		this.conj = conj;
		this.conjSentiment = conjSentiment;
	}
	
	public String getAspect() {
		return aspect;
	}
	public String getSentiment() {
		return sentiment;
	}
	
	public String getConj() {
		return conj;
	}

	public String getConjSentiment() {
		return conjSentiment;
	}

	public void setConj(String conj) {
		this.conj = conj;
	}

	public void setConjSentiment(String conjSentiment) {
		this.conjSentiment = conjSentiment;
	}

	public String toString(){
		
		return(String.format("aspect:%s, sentiment:%s, conj:%s, conjSentiment:%s", 
				getAspect(),
				getSentiment(),
				getConj(),
				getConjSentiment())
				);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(! (obj instanceof AspectSentimentTuple))
			return false;
		AspectSentimentTuple tuple = (AspectSentimentTuple)obj;
		return(
				tuple.getAspect().equals(this.getAspect()) &&
				tuple.getSentiment().equals(this.getSentiment()) &&
				tuple.getConj().equals(this.getConj()) &&
				tuple.getConjSentiment().equals(this.getConjSentiment())
				);
	}
}
