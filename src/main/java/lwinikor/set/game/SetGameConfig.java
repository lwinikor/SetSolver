package lwinikor.set.game;

import java.util.Map;
import java.util.Set;

import lwinikor.set.utils.StringUtils;

/*
 * represents a configuration for a game of Set which can be used to help generate decks of set cards
 */
public class SetGameConfig
{
	private Map<String, Set<String>> dimensionValues;
	private int numCardsPerSet;
	private int numCardsToPlay;

	public SetGameConfig(Map<String, Set<String>> dimensionValues, int numCardsPerSet, int numCardsToPlay)
	{
		this.dimensionValues = dimensionValues;
		this.numCardsPerSet = numCardsPerSet;
		this.numCardsToPlay = numCardsToPlay;
	}

	/*
	 * returns true is this configuration is valid. In order to be valid,
	 * dimensionValues must be specified, * numCardsPerSet must be >= 3
	 * numCardsToPlay must be >= numCardsPerSet
	 */
	public boolean isValid()
	{
		if (this.dimensionValues == null || this.dimensionValues.isEmpty()) return false;
		if (this.numCardsPerSet < 3) return false;
		if (this.numCardsToPlay < this.numCardsPerSet) return false;
		for (Map.Entry<String, Set<String>> entry : this.dimensionValues.entrySet())
		{
			String dimension = entry.getKey();
			if (StringUtils.isNullOrEmpty(dimension)) return false;
			Set<String> dimensionValues = entry.getValue();
			if (dimensionValues == null || dimensionValues.isEmpty()) return false;
			for (String dimensionValue : dimensionValues)
			{
				if (StringUtils.isNullOrEmpty(dimensionValue)) return false;
			}
		}
		return true;
	}

	public Map<String, Set<String>> getDimensionValues()
	{
		return this.dimensionValues;
	}

	public void setDimensionValues(Map<String, Set<String>> dimensionValues)
	{
		this.dimensionValues = dimensionValues;
	}

	public int getNumCardsPerSet()
	{
		return this.numCardsPerSet;
	}

	public void setNumCardsPerSet(int numCardsPerSet)
	{
		this.numCardsPerSet = numCardsPerSet;
	}

	public int getNumCardsToPlay()
	{
		return this.numCardsToPlay;
	}

	public void setNumCardsToPlay(int numCardsToPlay)
	{
		this.numCardsToPlay = numCardsToPlay;
	}
}