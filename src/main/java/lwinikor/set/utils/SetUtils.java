package lwinikor.set.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lwinikor.set.game.SetCard;
import lwinikor.set.game.SetCardSet;
import lwinikor.set.game.SetDeck;

public class SetUtils
{
	/*
	 * prints out collection of sets to stdout
	 */
	public static void printSets(Collection<SetCardSet> sets)
	{
		if (sets == null || sets.isEmpty()) return;
		StringBuilder sb = new StringBuilder("[\n");
		for (SetCardSet set : sets)
		{
			sb.append(set.toString()).append(",\n");
		}
		sb.deleteCharAt(sb.length() - 2);
		sb.append("]");
		System.out.println(sb.toString());
	}

	/*
	 * given a deck and number of cards that make up a deck, finds all sets by
	 * checking all combinations of cards in the deck of size setSize
	 */
	public static List<SetCardSet> findSets(SetDeck deck, int setSize)
	{
		if (deck == null || deck.getSize() == 0 || setSize <= 0 || setSize > deck.getSize())
			return new ArrayList<SetCardSet>();
		List<SetCardSet> ret = findSetsFromAllCombinations(deck.getCards(), setSize);
		return ret;
	}

	/*
	 * Checks if a given list of cards are a set. For each dimension, if each of
	 * the cards in the collection has a single dimension value, or if each card
	 * has a unique dimensionValue, it is a set
	 */
	public static boolean isSet(List<SetCard> cards)
	{
		if (cards == null || cards.isEmpty()) return false;
		Set<String> dimensions = cards.iterator().next().getDimensionValues().keySet();
		if (dimensions.isEmpty()) return false;
		for (String dimension : dimensions)
		{
			Set<String> dimensionValues = new HashSet<String>();
			for (SetCard card : cards)
			{
				String dimensionValue = card.getDimensionValue(dimension);
				if (StringUtils.isNullOrEmpty(dimensionValue)) return false;
				dimensionValues.add(dimensionValue);
			}
			if (dimensionValues.size() > 1 && dimensionValues.size() < cards.size()) return false;
		}
		return true;
	}

	/*
	 * given a list of cards and a size, finds all combinations of cards that
	 * match that size and are sets. initializes an idx array of length size,
	 * and updates the idx's to all possible combinations of numbers, in order
	 * to check all combinations
	 */
	private static List<SetCardSet> findSetsFromAllCombinations(List<SetCard> cards, int size)
	{
		List<SetCardSet> ret = new ArrayList<SetCardSet>();
		if (cards == null || cards.isEmpty() || size <= 0 || size > cards.size()) return ret;
		int[] idxs = new int[size];

		for (int i = 0; i < size; i++)
		{
			idxs[i] = i; // initialize idxs to [0,1,2,3,...,(size - 1)]
		}

		List<SetCard> permutation = getPermutation(cards, idxs);
		if (isSet(permutation))
		{
			ret.add(new SetCardSet(permutation));
		}
		while (true)
		{
			int i;
			// start from the end and find the right most index in idxs that can
			// be incremented
			for (i = (idxs.length - 1); i >= 0; i--)
			{
				if (idxs[i] != cards.size() - size + i) break;
			}
			if (i < 0)
			{
				break; // no index can be incremented any further without going
						// over list bounds, we're done
			}
			else
			{
				// increment idxs[i] and set all idxs to the right of i to
				// next possible values
				idxs[i] = idxs[i] + 1;
				for (int j = i + 1; j < size; j++)
				{
					idxs[j] = idxs[j - 1] + 1;
				}
				permutation = getPermutation(cards, idxs);
				if (isSet(permutation))
				{
					ret.add(new SetCardSet(permutation));
				}
			}
		}

		return ret;
	}

	/*
	 * given a list of cards and an array of idxs, returns the list of cards
	 * that are at the idxs specified in the list
	 */
	private static List<SetCard> getPermutation(List<SetCard> cards, int[] idxs)
	{
		if (cards == null || idxs == null) return null;
		List<SetCard> ret = new ArrayList<SetCard>();
		for (int i = 0; i < idxs.length; i++)
		{
			int idx = idxs[i];
			if (idx > cards.size() - 1) return null;
			ret.add(cards.get(idx));
		}

		return ret;
	}
}