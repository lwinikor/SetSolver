package lwinikor.set.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lwinikor.set.utils.StringUtils;

/*
 * represents a list of deck cards
 */
public class SetDeck
{
	private List<SetCard> cards;

	public SetDeck()
	{
		this(new ArrayList<SetCard>());
	}

	public SetDeck(List<SetCard> cards)
	{
		this.cards = cards;
	}

	/*
	 * creates a full deck given a SetGameConfig
	 */
	public SetDeck(SetGameConfig config)
	{
		if (config == null || !config.isValid())
		{
			this.cards = new ArrayList<SetCard>();
		}
		else
		{
			Map<String, Set<String>> dimensionValues = config.getDimensionValues();
			this.cards = cartesianProduct(dimensionValues);
		}
	}

	/*
	 * returns a list of every possible card given the map of dimensions to
	 * possible dimension values
	 */
	private static List<SetCard> cartesianProduct(Map<String, Set<String>> values)
	{
		if (values.isEmpty()) return new ArrayList<SetCard>();
		if (values.size() == 1)
		{
			List<SetCard> cards = new ArrayList<SetCard>();
			Map.Entry<String, Set<String>> entry = values.entrySet().iterator().next();
			String dimension = entry.getKey();
			for (String dimensionValue : entry.getValue())
			{
				SetCard card = new SetCard();
				card.addDimensionValue(dimension, dimensionValue);
				cards.add(card);
			}
			return cards;
		}
		return cartesianProduct(0, new ArrayList<Map.Entry<String, Set<String>>>(values.entrySet()));
	}

	private static List<SetCard> cartesianProduct(int index, List<Map.Entry<String, Set<String>>> values)
	{
		List<SetCard> cards = new ArrayList<SetCard>();
		if (index == values.size())
		{
			cards.add(new SetCard());
		}
		else
		{
			Map.Entry<String, Set<String>> entry = values.get(index);
			String dimension = entry.getKey();
			Set<String> dimensionValues = entry.getValue();
			for (String dimensionValue : dimensionValues)
			{
				List<SetCard> next = cartesianProduct(index + 1, values);
				for (SetCard card : next)
				{
					card.addDimensionValue(dimension, dimensionValue);
					cards.add(card);
				}
			}
		}

		return cards;
	}

	/*
	 * Returns whether this deck is valid. In order to be valid, every cards in
	 * the deck must specify the same dimensions
	 */
	public boolean isValid()
	{
		if (this.cards == null) return false;
		if (this.cards.isEmpty()) return true;
		Set<String> dimensions = new HashSet<String>();
		for (SetCard card : this.cards)
		{
			Map<String, String> dimensionValues = card.getDimensionValues();
			if (dimensionValues == null || dimensionValues.isEmpty()) return false;
			for (Map.Entry<String, String> entry : dimensionValues.entrySet())
			{
				String dimension = entry.getKey();
				if (StringUtils.isNullOrEmpty(dimension)) return false;
				String dimensionValue = entry.getValue();
				if (StringUtils.isNullOrEmpty(dimensionValue)) return false;
			}
			dimensions.addAll(card.getDimensions());
		}
		for (SetCard card : this.cards)
		{
			if (!card.getDimensions().equals(dimensions)) return false;
		}
		return true;
	}

	/*
	 * Return a new deck of size count made of random cards from this deck
	 */
	public SetDeck randomCards(int count)
	{
		if (count <= 0 || this.cards == null) return new SetDeck();
		List<SetCard> shuffledList = new ArrayList<SetCard>(this.cards);
		Collections.shuffle(shuffledList);
		if (count >= shuffledList.size()) return new SetDeck(shuffledList);
		while (shuffledList.size() > count)
		{
			shuffledList.remove(0);
		}
		return new SetDeck(shuffledList);
	}

	public void addCard(SetCard card)
	{
		if (this.cards == null) this.cards = new ArrayList<SetCard>();
		this.cards.add(card);
	}

	public List<SetCard> getCards()
	{
		return this.cards;
	}

	public int getSize()
	{
		if (this.cards == null) return 0;
		return this.cards.size();
	}

	public void setCards(List<SetCard> cards)
	{
		this.cards = cards;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("[\n");
		if (this.cards != null && !this.cards.isEmpty())
		{
			for (SetCard card : this.cards)
			{
				sb.append(card.toString()).append(",\n");
			}
			sb.deleteCharAt(sb.length() - 2);
		}
		sb.append("]");
		return sb.toString();
	}
}