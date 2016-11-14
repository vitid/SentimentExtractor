package project.nlp.sentimentextract;

import org.junit.*;

import project.nlp.sentimentextract.rule.RuleManager;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class ReviewAnalyzerTest {
	private RuleManager ruleManager;
	private ReviewAnalyzer reviewAnalyzer;
	
	@Before
	public void before(){
		String reviewContent = "This MOVIE_PLOT is good. It has a great MOVIE_SONG."
				+ "MOVIE_ACTING is the best actor in the planet.";
		ruleManager = new RuleManager("extract_rules.test.properties");
		reviewAnalyzer = new ReviewAnalyzer(reviewContent, ruleManager);
	}
	
	@Test
	public void testExtractAspectSentimentExpression(){
		ArrayList<AspectSentimentTuple> tupleList = reviewAnalyzer.extractAspectSentimentExpression();
		assertEquals(3, tupleList.size());
		assertEquals("MOVIE_PLOT",tupleList.get(0).getAspect());
		assertEquals("good",tupleList.get(0).getSentiment());
		assertEquals("MOVIE_SONG",tupleList.get(1).getAspect());
		assertEquals("great",tupleList.get(1).getSentiment());
		assertEquals("MOVIE_ACTING",tupleList.get(2).getAspect());
		assertEquals("best",tupleList.get(2).getSentiment());
	}
	
}
