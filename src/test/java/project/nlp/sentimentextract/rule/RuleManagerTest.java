package project.nlp.sentimentextract.rule;

import org.junit.*;
import static org.junit.Assert.*;

public class RuleManagerTest {
	
	private RuleManager ruleManager;
	
	@Before
	public void before(){
		ruleManager = new RuleManager("extract_rules.test.properties");
	}

	@Test
	public void testGetAspectList(){
		assertEquals(3, ruleManager.getAspectList().size());
		assertEquals("MOVIE_SONG", ruleManager.getAspectList().get(1));
	}
	
	@Test
	public void testGetRuleList(){
		assertEquals("{tag:/JJ.*/} > {word:_ASPECTS_}", ruleManager.getRuleList().get(0));
	}
	
	@Test
	public void testParseRule(){
		assertEquals("{tag:/JJ.*/} > {word:MOVIE_PLOT}", ruleManager.parseRule("{tag:/JJ.*/} > {word:_ASPECTS_}", "MOVIE_PLOT"));
	}
}
