package lwinikor.set.solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

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
import com.google.gson.stream.JsonReader;

import lwinikor.set.game.SetCardSet;
import lwinikor.set.game.SetDeck;
import lwinikor.set.game.SetGameConfig;
import lwinikor.set.utils.SetUtils;
import lwinikor.set.utils.StringUtils;

public class SetSolverWithConfig
{
	public final static String CONFIG_OPT = "c";

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

		String configFileLocation = commandLine.getOptionValue(CONFIG_OPT);
		Gson gson = new GsonBuilder().create();
		if (StringUtils.isNullOrEmpty(configFileLocation))
		{
			printHelp(options);
		}
		else
		{
			SetGameConfig setGameConfig = null;
			JsonReader r = null;
			try
			{
				File configFile = new File(configFileLocation);
				FileReader reader = new FileReader(configFile);
				r = new JsonReader(reader);
				setGameConfig = gson.fromJson(r, SetGameConfig.class);
				if (!setGameConfig.isValid())
				{
					configNotValid(configFileLocation);
					return;
				}
				SetDeck deck = new SetDeck(setGameConfig);
				solve(deck, setGameConfig);
			}
			catch (FileNotFoundException e1)
			{
				System.out.println("Could not find file: " + configFileLocation);
			}
			catch (JsonSyntaxException e2)
			{
				System.out.println("File: " + configFileLocation + " does not contain valid json for a SetGameConfig");
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
		formatter.printHelp(SetSolverWithConfig.class.getSimpleName(), options);
	}

	public static void solve(SetDeck deck, SetGameConfig setGameConfig)
	{
		long start = System.currentTimeMillis();
		System.out.println("total deck size: " + deck.getSize());
		SetDeck subDeck = deck.randomCards(setGameConfig.getNumCardsToPlay());
		System.out.println("------------------------");
		System.out.println("random hand of size: " + subDeck.getSize());
		System.out.println(subDeck.toString());
		List<SetCardSet> s = SetUtils.findSets(subDeck, setGameConfig.getNumCardsPerSet());
		System.out.println("------------------------");
		SetUtils.printSets(s);
		System.out.println(s.size() + " set(s) of size " + setGameConfig.getNumCardsPerSet() + " found");
		System.out.println("Took " + (System.currentTimeMillis() - start + " (ms)"));
	}

	public static void configNotValid(String configFileLocation)
	{
		StringBuilder sb = new StringBuilder("SetGameConfig specified in: ");
		sb.append(configFileLocation).append(" is not valid.");
		System.out.println(sb.toString());
	}

	public static Options constructOptions()
	{
		Options options = new Options();
		options.addOption(CONFIG_OPT, true, "Location of SetGameConfig configuration file.");
		return options;
	}
}