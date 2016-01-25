package lwinikor.set.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SetGameConfigTest
{
	private static Set<String> colors;
	private static Set<String> numbers;

	static
	{
		colors = new HashSet<String>();
		colors.add("red");
		colors.add("green");
		colors.add("purple");
		numbers = new HashSet<String>();
		numbers.add("1");
		numbers.add("2");
		numbers.add("3");
	}

	@Test
	public void testValidConfg()
	{
		Map<String, Set<String>> dimensionValues = new HashMap<String, Set<String>>();
		dimensionValues.put("color", colors);
		dimensionValues.put("number", numbers);

		SetGameConfig config = new SetGameConfig(dimensionValues, 3, 12);
		assertTrue(config.isValid());
	}

	@Test
	public void testInvalidConfigLowCardsPerSet()
	{
		Map<String, Set<String>> dimensionValues = new HashMap<String, Set<String>>();
		dimensionValues.put("color", colors);
		dimensionValues.put("number", numbers);
		SetGameConfig config = new SetGameConfig(dimensionValues, 1, 12);
		assertFalse(config.isValid());
	}

	@Test
	public void testInvalidConfigLowCardsToPlay()
	{
		Map<String, Set<String>> dimensionValues = new HashMap<String, Set<String>>();
		dimensionValues.put("color", colors);
		dimensionValues.put("number", numbers);
		SetGameConfig config = new SetGameConfig(dimensionValues, 4, 3);
		assertFalse(config.isValid());
	}

	@Test
	public void testInvalidConfigNullDimensionValues()
	{
		SetGameConfig config = new SetGameConfig(null, 3, 12);
		assertFalse(config.isValid());
	}

	@Test
	public void testInvalidConfigEmptyDimensionValues()
	{
		SetGameConfig config = new SetGameConfig(new HashMap<String, Set<String>>(), 3, 12);
		assertFalse(config.isValid());
	}
}