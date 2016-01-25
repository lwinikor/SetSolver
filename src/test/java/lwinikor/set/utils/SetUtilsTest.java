package lwinikor.set.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import lwinikor.set.game.SetCard;
import lwinikor.set.game.SetCardSet;
import lwinikor.set.game.SetDeck;
import lwinikor.set.game.SetGameConfig;

public class SetUtilsTest
{
	private static Set<String> colors;
	private static Set<String> numbers;
	private static Set<String> shapes;
	private static Set<String> shadings;
	private static Set<String> backgroundColors;

	private static SetGameConfig oneDimensionConfig;
	private static SetGameConfig twoDimensionConfig;
	private static SetGameConfig threeDimensionConfig;
	private static SetGameConfig classicConfig;
	private static SetGameConfig fiveDimensionConfig;

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
		backgroundColors = new HashSet<String>();
		backgroundColors.add("black");
		backgroundColors.add("white");
		backgroundColors.add("grey");

		Map<String, Set<String>> oneDimensions = new HashMap<String, Set<String>>();
		oneDimensions.put("color", colors);
		oneDimensionConfig = new SetGameConfig(oneDimensions, 3, 3);

		Map<String, Set<String>> twoDimensions = new HashMap<String, Set<String>>();
		twoDimensions.put("color", colors);
		twoDimensions.put("number", numbers);
		twoDimensionConfig = new SetGameConfig(twoDimensions, 3, 3);

		Map<String, Set<String>> threeDimensions = new HashMap<String, Set<String>>();
		threeDimensions.put("color", colors);
		threeDimensions.put("number", numbers);
		threeDimensions.put("shape", shapes);
		threeDimensionConfig = new SetGameConfig(threeDimensions, 3, 3);

		Map<String, Set<String>> classicDimensions = new HashMap<String, Set<String>>();
		classicDimensions.put("color", colors);
		classicDimensions.put("number", numbers);
		classicDimensions.put("shape", shapes);
		classicDimensions.put("shading", shadings);
		classicConfig = new SetGameConfig(classicDimensions, 3, 3);

		Map<String, Set<String>> fiveDimensions = new HashMap<String, Set<String>>();
		fiveDimensions.put("color", colors);
		fiveDimensions.put("number", numbers);
		fiveDimensions.put("shape", shapes);
		fiveDimensions.put("shading", shadings);
		fiveDimensions.put("backgroundColor", backgroundColors);
		fiveDimensionConfig = new SetGameConfig(fiveDimensions, 3, 3);
	}

	@Test
	public void testOneDimensionFindSetsCount()
	{
		SetDeck deck = new SetDeck(oneDimensionConfig);
		List<SetCardSet> sets = SetUtils.findSets(deck, 3);
		assertEquals(1, sets.size());
	}

	// When each dimension has 3 possible values, and finding sets of size 3,
	// for any 2 given cards there is exactly 1 card that can complete the set.
	// With deck size of N, there are (N * N -1) possible pairs, since order
	// does not matter, we can divide that by 3! to get at total number of
	// unique sets
	@Test
	public void testTwoDimensionFindSetsCount()
	{
		SetDeck deck = new SetDeck(twoDimensionConfig);
		List<SetCardSet> sets = SetUtils.findSets(deck, 3);
		int numPossibleSets = (deck.getSize() * (deck.getSize() - 1)) / (3 * 2 * 1);
		assertEquals(numPossibleSets, sets.size());
	}

	@Test
	public void testThreeDimensionFindSetsCount()
	{
		SetDeck deck = new SetDeck(threeDimensionConfig);
		List<SetCardSet> sets = SetUtils.findSets(deck, 3);
		int numPossibleSets = (deck.getSize() * (deck.getSize() - 1)) / (3 * 2 * 1);
		assertEquals(numPossibleSets, sets.size());
	}

	@Test
	public void testClassicFindSetsCount()
	{
		SetDeck deck = new SetDeck(classicConfig);
		List<SetCardSet> sets = SetUtils.findSets(deck, 3);
		int numPossibleSets = (deck.getSize() * (deck.getSize() - 1)) / (3 * 2 * 1);
		assertEquals(numPossibleSets, sets.size());
	}

	@Test
	public void testFiveDimensionFindSetsCount()
	{
		SetDeck deck = new SetDeck(fiveDimensionConfig);
		List<SetCardSet> sets = SetUtils.findSets(deck, 3);
		int numPossibleSets = (deck.getSize() * (deck.getSize() - 1)) / (3 * 2 * 1);
		assertEquals(numPossibleSets, sets.size());
	}

	@Test
	public void testFindSetsEmptyDeckCount()
	{
		SetDeck deck = new SetDeck();
		List<SetCardSet> sets = SetUtils.findSets(deck, classicConfig.getNumCardsPerSet());
		assertEquals(0, sets.size());
	}

	@Test
	public void testIsSetNull()
	{
		assertFalse(SetUtils.isSet(null));
	}

	@Test
	public void testIsSetEmpty()
	{
		assertFalse(SetUtils.isSet(new ArrayList<SetCard>()));
	}

	@Test
	public void testIsSetSingleEmptyCard()
	{
		SetCard card = new SetCard();
		assertFalse(SetUtils.isSet(Collections.singletonList(card)));
	}

	@Test
	public void testIsSetValidDupe()
	{
		SetCard card = new SetCard();
		card.addDimensionValue("color", "red");
		SetCard card2 = new SetCard();
		card2.addDimensionValue("color", "red");
		SetCard card3 = new SetCard();
		card3.addDimensionValue("color", "red");
		List<SetCard> cards = new ArrayList<SetCard>();
		cards.add(card);
		cards.add(card2);
		cards.add(card3);

		assertTrue(SetUtils.isSet(cards));
	}

	@Test
	public void testIsSetValidMultipleDimensionsPass()
	{
		SetCard card = new SetCard();
		card.addDimensionValue("color", "red");
		card.addDimensionValue("shape", "diamond");
		SetCard card2 = new SetCard();
		card2.addDimensionValue("color", "red");
		card2.addDimensionValue("shape", "oval");
		List<SetCard> cards = new ArrayList<SetCard>();
		SetCard card3 = new SetCard();
		card3.addDimensionValue("color", "red");
		card3.addDimensionValue("shape", "squiggle");

		cards.add(card);
		cards.add(card2);
		cards.add(card3);

		assertTrue(SetUtils.isSet(cards));
	}

	@Test
	public void testIsSetValidMultipleDimensionsFail()
	{
		SetCard card = new SetCard();
		card.addDimensionValue("color", "red");
		card.addDimensionValue("shape", "diamond");
		SetCard card2 = new SetCard();
		card2.addDimensionValue("color", "red");
		card2.addDimensionValue("shape", "diamond");
		List<SetCard> cards = new ArrayList<SetCard>();
		SetCard card3 = new SetCard();
		card3.addDimensionValue("color", "red");
		card3.addDimensionValue("shape", "squiggle");

		cards.add(card);
		cards.add(card2);
		cards.add(card3);

		assertFalse(SetUtils.isSet(cards));
	}
}