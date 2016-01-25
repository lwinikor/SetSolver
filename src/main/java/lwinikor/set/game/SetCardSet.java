package lwinikor.set.game;

import java.util.ArrayList;
import java.util.List;

/*
 * represents a Set of Set Cards, that is, a list of cards where for each dimension on each card, 
 * either all cards in the collection have the same value, or all have a unique value
 */
public class SetCardSet
{
	private List<SetCard> cards;

	public SetCardSet()
	{
		this.cards = new ArrayList<SetCard>();
	}

	public SetCardSet(List<SetCard> cards)
	{
		this.cards = cards;
	}

	public void addCard(SetCard card)
	{
		this.cards.add(card);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.cards == null) ? 0 : this.cards.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		SetCardSet other = (SetCardSet) obj;
		if (this.cards == null)
		{
			if (other.cards != null) return false;
		}
		else if (!this.cards.equals(other.cards)) return false;
		return true;
	}

	public List<SetCard> getCards()
	{
		return this.cards;
	}

	public void setCards(List<SetCard> cards)
	{
		this.cards = cards;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("[\n");
		if (this.cards != null)
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