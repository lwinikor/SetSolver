package lwinikor.set.solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import lwinikor.set.game.SetCard;
import lwinikor.set.game.SetCardSet;
import lwinikor.set.game.SetDeck;
import lwinikor.set.utils.SetUtils;
import lwinikor.set.utils.StringUtils;

public class SetSolverWithInputDeck
{
	public final static String DECK_OPT = "d";
	public final static String NUMCARDS_OPT = "n";
	public final static String DEBUGINFO_OPT = "v";

	public static void main(String[] args) throws ParseException, IOException
	{
		CommandLineParser parser = new BasicParser();
		Options options = constructOptions();

		CommandLine commandLine = null;

		try
		{
			commandLine = parser.parse(options, args);
		}
		catch (UnrecognizedOptionException e)
		{
			System.out.println("Unrecognized command line option specified:" + e.getOption());
			printHelp(options);
			return;
		}
		String deckFileLocation = commandLine.getOptionValue(DECK_OPT);
		String numCardsStr = commandLine.getOptionValue(NUMCARDS_OPT, "3");
		boolean debugInfo = commandLine.hasOption(DEBUGINFO_OPT);
		int numCardsPerSet;
		try
		{
			numCardsPerSet = Integer.parseInt(numCardsStr);
		}
		catch (NumberFormatException e)
		{
			if (debugInfo)
			{
				System.out.println("Invalid value input for number of cards that make up a set, defaulting to 3");
			}
			numCardsPerSet = 3;
		}
		if (numCardsPerSet < 3)
		{
			if (debugInfo)
			{
				System.out.println("Number of cards that make up a set must be 3 or greater, defaulting to 3");
			}
			numCardsPerSet = 3;
		}

		Gson gson = new GsonBuilder().create();
		if (StringUtils.isNullOrEmpty(deckFileLocation))
		{
			printHelp(options);
		}
		else
		{
			JsonReader r = null;
			try
			{
				File deckFile = new File(deckFileLocation);
				FileReader reader = new FileReader(deckFile);
				r = new JsonReader(reader);
				Type type = new TypeToken<List<Map<String, String>>>()
				{
				}.getType();
				List<Map<String, String>> cardDimensionValues = gson.fromJson(r, type);
				SetDeck deck = new SetDeck();
				for (Map<String, String> dimensionValues : cardDimensionValues)
				{
					SetCard card = new SetCard(dimensionValues);
					deck.addCard(card);
				}
				if (!deck.isValid())
				{
					deckNotValid(deckFileLocation);
					return;
				}
				solve(deck, numCardsPerSet, debugInfo);
			}
			catch (FileNotFoundException e1)
			{
				System.out.println("Could not find file: " + deckFileLocation);
			}
			catch (JsonSyntaxException e2)
			{
				System.out.println("File: " + deckFileLocation + " does not contain valid json for a SetDeck");
				e2.printStackTrace();
			}
			finally
			{
				if (r != null) r.close();
			}
		}
	}

	public static void printHelp(Options options)
	{
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(SetSolverWithInputDeck.class.getSimpleName(), options);
	}

	public static void solve(SetDeck deck, int numCardsPerSet, boolean debugInfo)
	{
		long start = System.currentTimeMillis();
		List<SetCardSet> s = SetUtils.findSets(deck, numCardsPerSet);
		SetUtils.printSets(s);
		if (debugInfo)
		{
			System.out.println(s.size() + " set(s) of size " + numCardsPerSet + " found");
			System.out.println("Took " + (System.currentTimeMillis() - start + " (ms)"));
		}
	}

	public static void deckNotValid(String deckFileLocation)
	{
		StringBuilder sb = new StringBuilder("SetDeck specified in: ");
		sb.append(deckFileLocation).append(" is not valid.\nAll cards must specify the same dimensions.\n");
		sb.append("All dimension and dimensionValues must be non null / empty.\n");
		sb.append("All cards must have at least one pair of dimension and dimensionValues.");
		System.out.println(sb.toString());
	}

	public static Options constructOptions()
	{
		Options options = new Options();
		options.addOption(DECK_OPT, true, "Location of SetDeck input file.");
		options.addOption(NUMCARDS_OPT, true, "Number of cards that make up a Set.");
		options.addOption(DEBUGINFO_OPT, false, "Print additional debug info.");
		return options;
	}
}