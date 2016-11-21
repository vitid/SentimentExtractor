package project.nlp.sentimentextract;

import org.junit.*;

import project.nlp.sentimentextract.rule.RuleManager;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class ReviewAnalyzerTest {
	private RuleManager ruleManager;
	//private ReviewAnalyzer reviewAnalyzer;
	
	@Before
	public void before(){
		ruleManager = new RuleManager("extract_rules.test.properties");
	}
	
	@Test
	public void testMappingAspectWithAdj(){
		ReviewAnalyzer reviewAnalyzer = new ReviewAnalyzer("MOVIE_ACTOR is very good.", ruleManager);
		ArrayList<AspectSentimentTuple> tupleList = reviewAnalyzer.extractAspectSentimentExpression();
		assertEquals(1, tupleList.size());
		assertTrue(tupleList.contains(new AspectSentimentTuple("MOVIE_ACTOR", "very good")));
		
		reviewAnalyzer = new ReviewAnalyzer("A really good MOVIE_ACTOR.", ruleManager);
		tupleList = reviewAnalyzer.extractAspectSentimentExpression();
		assertEquals(1, tupleList.size());
		assertTrue(tupleList.contains(new AspectSentimentTuple("MOVIE_ACTOR", "really good")));
	}
	
	@Test
	public void testMappingAspectWithNoun(){
		ReviewAnalyzer reviewAnalyzer = new ReviewAnalyzer("MOVIE_ACTOR is a very good person.", ruleManager);
		ArrayList<AspectSentimentTuple> tupleList = reviewAnalyzer.extractAspectSentimentExpression();
		assertEquals(1, tupleList.size());
		assertTrue(tupleList.contains(new AspectSentimentTuple("MOVIE_ACTOR", "very good person")));
		
		reviewAnalyzer = new ReviewAnalyzer("A really good person is MOVIE_ACTOR.", ruleManager);
		tupleList = reviewAnalyzer.extractAspectSentimentExpression();
		assertEquals(1, tupleList.size());
		assertTrue(tupleList.contains(new AspectSentimentTuple("MOVIE_ACTOR", "really good person")));
	}
	
	@Test
	public void testConjClause(){
		//but clause between adj <-> adj
		ReviewAnalyzer reviewAnalyzer = new ReviewAnalyzer("MOVIE_ACTOR is very good but the MOVIE_PLOT is extremely short.", ruleManager);
		ArrayList<AspectSentimentTuple> tupleList = reviewAnalyzer.extractAspectSentimentExpression();
		assertEquals(2, tupleList.size());
		assertTrue(tupleList.contains(new AspectSentimentTuple("MOVIE_ACTOR", "very good","but","extremely short")));
		assertTrue(tupleList.contains(new AspectSentimentTuple("MOVIE_PLOT", "extremely short","but","very good")));
		
		//but clause between adj <-> noun
		reviewAnalyzer = new ReviewAnalyzer("MOVIE_ACTOR is a very good person but the MOVIE_PLOT is extremely short.", ruleManager);
		tupleList = reviewAnalyzer.extractAspectSentimentExpression();
		assertEquals(2, tupleList.size());
		assertTrue(tupleList.contains(new AspectSentimentTuple("MOVIE_ACTOR", "very good person","but","extremely short")));
		assertTrue(tupleList.contains(new AspectSentimentTuple("MOVIE_PLOT", "extremely short","but","very good person")));
		
		//and clause between adj <-> noun
		reviewAnalyzer = new ReviewAnalyzer("MOVIE_ACTOR is a very good person and the MOVIE_PLOT is extremely short.", ruleManager);
		tupleList = reviewAnalyzer.extractAspectSentimentExpression();
		assertEquals(2, tupleList.size());
		assertTrue(tupleList.contains(new AspectSentimentTuple("MOVIE_ACTOR", "very good person","and","extremely short")));
		assertTrue(tupleList.contains(new AspectSentimentTuple("MOVIE_PLOT", "extremely short","and","very good person")));
		
		//and clause between noun <-> noun
		reviewAnalyzer = new ReviewAnalyzer("MOVIE_ACTOR is a very good person and the MOVIE_PLOT is an extremely short film.", ruleManager);
		tupleList = reviewAnalyzer.extractAspectSentimentExpression();
		assertEquals(2, tupleList.size());
		assertTrue(tupleList.contains(new AspectSentimentTuple("MOVIE_ACTOR", "very good person","and","extremely short film")));
		assertTrue(tupleList.contains(new AspectSentimentTuple("MOVIE_PLOT", "extremely short film","and","very good person")));
	}
	
	@Test
	public void testEmbalishAdj(){
		ReviewAnalyzer reviewAnalyzer = new ReviewAnalyzer("A not really very good MOVIE_ACTOR", ruleManager);
		ArrayList<AspectSentimentTuple> tupleList = reviewAnalyzer.extractAspectSentimentExpression();
		assertEquals(1, tupleList.size());
		assertTrue(tupleList.contains(new AspectSentimentTuple("MOVIE_ACTOR", "not really very good")));
	}
	
	@Test
	public void testEmbalishNoun(){
		ReviewAnalyzer reviewAnalyzer = new ReviewAnalyzer("MOVIE_ACTOR is not a very good person", ruleManager);
		ArrayList<AspectSentimentTuple> tupleList = reviewAnalyzer.extractAspectSentimentExpression();
		assertEquals(1, tupleList.size());
		assertTrue(tupleList.contains(new AspectSentimentTuple("MOVIE_ACTOR", "not very good person")));
	}
	
}
