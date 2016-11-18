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
		String reviewContent = 
				"This MOVIE_PLOT is not very good."
				+ " The MOVIE_PLOT and MOVIE_ACTING are really cool."
				+ " The MOVIE_SONG is loud and clear."
				+ " It has a great MOVIE_SONG."
				+ " MOVIE_ACTING is the best actor in the planet.";
		ruleManager = new RuleManager("extract_rules.test.properties");
		reviewAnalyzer = new ReviewAnalyzer(reviewContent, ruleManager);
	}
	
	@Test
	public void testExtractAspectSentimentExpression(){
		ArrayList<AspectSentimentTuple> tupleList = reviewAnalyzer.extractAspectSentimentExpression();
		//assertEquals(3, tupleList.size());
		
		assertTrue(AspectSentimentTuple.containsTuple("MOVIE_PLOT", "not very good", tupleList));
		assertTrue(AspectSentimentTuple.containsTuple("MOVIE_PLOT", "really cool", tupleList));
		assertTrue(AspectSentimentTuple.containsTuple("MOVIE_ACTING", "really cool", tupleList));
		assertTrue(AspectSentimentTuple.containsTuple("MOVIE_SONG", "loud", tupleList));
		assertTrue(AspectSentimentTuple.containsTuple("MOVIE_SONG", "clear", tupleList));
		
		assertTrue(AspectSentimentTuple.containsTuple("MOVIE_SONG", "great", tupleList));
		assertTrue(AspectSentimentTuple.containsTuple("MOVIE_ACTING", "best", tupleList));
		
	}
	
}
