package lwinikor.set.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
 * Represents a single Set Card with variable dimensionValue mappings
 */
public class SetCard
{
	private Map<String, String> dimensionValues;

	public SetCard()
	{
		this.dimensionValues = new HashMap<String, String>();
	}

	public SetCard(Map<String, String> dimensionValues)
	{
		if (dimensionValues != null)
		{
			this.dimensionValues = new HashMap<String, String>(dimensionValues);
		}
	}

	public void addDimensionValue(String dimension, String dimensionValue)
	{
		this.dimensionValues.put(dimension, dimensionValue);
	}

	public String getDimensionValue(String dimension)
	{
		return this.dimensionValues.get(dimension);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.dimensionValues == null) ? 0 : this.dimensionValues.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		SetCard other = (SetCard) obj;
		if (this.dimensionValues == null)
		{
			if (other.dimensionValues != null) return false;
		}
		else if (!this.dimensionValues.equals(other.dimensionValues)) return false;
		return true;
	}

	public Map<String, String> getDimensionValues()
	{
		return this.dimensionValues;
	}

	public Set<String> getDimensions()
	{
		if (this.dimensionValues == null) return null;
		return this.dimensionValues.keySet();
	}

	public void setDimensionValues(Map<String, String> dimensionValues)
	{
		this.dimensionValues = dimensionValues;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		if (this.dimensionValues != null && !this.dimensionValues.isEmpty())
		{
			for (Map.Entry<String, String> entry : this.dimensionValues.entrySet())
			{
				sb.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("}");
		return sb.toString();
	}
}