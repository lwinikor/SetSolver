package lwinikor.set.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SetDeckTest
{
	private static Set<String> colors;
	private static Set<String> numbers;
	private static Set<String> shapes;
	private static Set<String> shadings;
	private static Set<String> backgroundColors;
	private static Set<String> largeColors;
	private static Set<String> largeShapes;

	private static SetGameConfig classicConfig;
	private static SetGameConfig smallConfig;
	private static SetGameConfig largeConfig;

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
		shapes = new HashSet<String>();
		shapes.add("diamond");
		shapes.add("oval");
		shapes.add("squiggle");
		shadings = new HashSet<String>();
		shadings.add("solid");
		shadings.add("striped");
		shadings.add("outlined");

		Map<String, Set<String>> classicDimensions = new HashMap<String, Set<String>>();
		classicDimensions.put("color", colors);
		classicDimensions.put("number", numbers);
		classicDimensions.put("shape", shapes);
		classicDimensions.put("shading", shadings);
		classicConfig = new SetGameConfig(classicDimensions, 3, 12);

		Map<String, Set<String>> smallDimensions = new HashMap<String, Set<String>>();
		smallDimensions.put("color", colors);
		smallConfig = new SetGameConfig(smallDimensions, 3, 3);

		backgroundColors = new HashSet<String>();
		backgroundColors.add("white");
		backgroundColors.add("black");
		backgroundColors.add("grey");
		backgroundColors.add("yellow");

		largeColors = new HashSet<String>();
		largeColors.add("red");
		largeColors.add("green");
		largeColors.add("purple");
		largeColors.add("blue");
		largeColors.add("orange");
		largeColors.add("brown");

		largeShapes = new HashSet<String>();
		largeShapes.add("diamond");
		largeShapes.add("oval");
		largeShapes.add("squiggle");
		largeShapes.add("star");
		largeShapes.add("triangle");
		largeShapes.add("circle");

		Map<String, Set<String>> largeDimensions = new HashMap<String, Set<String>>();
		largeDimensions.put("color", largeColors);
		largeDimensions.put("number", numbers);
		largeDimensions.put("shape", largeShapes);
		largeDimensions.put("shading", shadings);
		largeDimensions.put("backgroundColor", backgroundColors);
		largeConfig = new SetGameConfig(largeDimensions, 3, 12);
	}

	@Test
	public void testClassicDeckGenerationSize()
	{
		SetDeck deck = new SetDeck(classicConfig);
		int expectedSize = colors.size() * numbers.size() * shapes.size() * shadings.size();
		assertEquals(expectedSize, deck.getSize());
	}

	@Test
	public void testSmallDeckGenerationSize()
	{
		SetDeck deck = new SetDeck(smallConfig);
		int expectedSize = colors.size();
		assertEquals(expectedSize, deck.getSize());
	}

	@Test
	public void testLargeDeckGenerationSize()
	{
		SetDeck deck = new SetDeck(largeConfig);
		int expectedSize = largeColors.size() * numbers.size() * largeShapes.size() * shadings.size()
				* backgroundColors.size();
		assertEquals(expectedSize, deck.getSize());
	}

	@Test
	public void testEmptyDeckValid()
	{
		SetDeck deck = new SetDeck();
		assertTrue(deck.isValid());
	}

	@Test
	public void testNullDeckValid()
	{
		List<SetCard> cards = null;
		SetDeck deck = new SetDeck(cards);
		assertFalse(deck.isValid());
	}

	@Test
	public void testDupeCardDeckValid()
	{
		SetCard card1 = new SetCard();
		card1.addDimensionValue("color", "red");
		card1.addDimensionValue("shading", "solid");
		card1.addDimensionValue("shape", "squiggle");
		card1.addDimensionValue("number", "3");

		List<SetCard> cards = new ArrayList<SetCard>();
		cards.add(card1);
		cards.add(new SetCard(card1.getDimensionValues()));
		cards.add(new SetCard(card1.getDimensionValues()));

		SetDeck deck = new SetDeck(cards);
		assertTrue(deck.isValid());
	}

	@Test
	public void testInconsistentDimensionCardDeckValid()
	{
		SetCard card1 = new SetCard();
		card1.addDimensionValue("color", "red");
		card1.addDimensionValue("shading", "solid");
		card1.addDimensionValue("shape", "squiggle");
		card1.addDimensionValue("number", "3");

		SetCard card2 = new SetCard();
		card2.addDimensionValue("color", "green");
		card2.addDimensionValue("shading", "striped");
		card2.addDimensionValue("shape", "diamond");

		List<SetCard> cards = new ArrayList<SetCard>();
		cards.add(card1);
		cards.add(card2);

		SetDeck deck = new SetDeck(cards);
		assertFalse(deck.isValid());
	}
}