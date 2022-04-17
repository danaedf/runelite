/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.cache;

import java.io.File;
import java.io.IOException;
import net.runelite.cache.fs.Store;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Cache
{
	public static void main(String[] args) throws IOException
	{
		Options options = new Options();

		options.addOption("c", "cache", true, "cache base");

		options.addOption(null, "items", true, "directory to dump items to");
		options.addOption(null, "npcs", true, "directory to dump npcs to");
		options.addOption(null, "objects", true, "directory to dump objects to");
		options.addOption(null, "sprites", true, "directory to dump sprites to");
		options.addOption(null, "interfaces", true, "directory to dump interfaces to");
		options.addOption(null, "enums", true, "directory to dump enums to");
		options.addOption(null, "structs", true, "directory to dump structs to");
		options.addOption(null, "params", true, "directory to dump params to");
		options.addOption(null, "regions", true, "directory to dump regions to");
		options.addOption(null, "mapimages", true, "directory to dump map images regions to");
		options.addOption(null, "models", true, "directory to dump models to");

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd;
		try
		{
			cmd = parser.parse(options, args);
		}
		catch (ParseException ex)
		{
			System.err.println("Error parsing command line options: " + ex.getMessage());
			System.exit(-1);
			return;
		}

		String cache = cmd.getOptionValue("cache");

		Store store = loadStore(cache);

		if (cmd.hasOption("items"))
		{
			String itemdir = cmd.getOptionValue("items");

			if (itemdir == null)
			{
				System.err.println("Item directory must be specified");
				return;
			}

			System.out.println("Dumping items to " + itemdir);
			dumpItems(store, new File(itemdir));
		}
		else if (cmd.hasOption("npcs"))
		{
			String npcdir = cmd.getOptionValue("npcs");

			if (npcdir == null)
			{
				System.err.println("NPC directory must be specified");
				return;
			}

			System.out.println("Dumping npcs to " + npcdir);
			dumpNpcs(store, new File(npcdir));
		}
		else if (cmd.hasOption("objects"))
		{
			String objectdir = cmd.getOptionValue("objects");

			if (objectdir == null)
			{
				System.err.println("Object directory must be specified");
				return;
			}

			System.out.println("Dumping objects to " + objectdir);
			dumpObjects(store, new File(objectdir));
		}
		else if (cmd.hasOption("sprites"))
		{
			String spritedir = cmd.getOptionValue("sprites");

			if (spritedir == null)
			{
				System.err.println("Sprite directory must be specified");
				return;
			}

			System.out.println("Dumping sprites to " + spritedir);
			dumpSprites(store, new File(spritedir));
		}
		else if (cmd.hasOption("interfaces"))
		{
			String interfacedir = cmd.getOptionValue("interfaces");

			if (interfacedir == null)
			{
				System.err.println("Interfaces directory must be specified");
				return;
			}

			System.out.println("Dumping interfaces to " + interfacedir);
			dumpInterfaces(store, new File(interfacedir));
		}
		else if (cmd.hasOption("enums")){
			String enumdir = cmd.getOptionValue("enums");

			if (enumdir == null)
			{
				System.err.println("Enum directory must be specified");
				return;
			}

			System.out.println("Dumping enums to " + enumdir);
			dumpEnums(store, new File(enumdir));
		}
		else if (cmd.hasOption("structs")){
			String structdir = cmd.getOptionValue("structs");

			if (structdir == null)
			{
				System.err.println("Struct directory must be specified");
				return;
			}

			System.out.println("Dumping structs to " + structdir);
			dumpStructs(store, new File(structdir));
		}
		else if (cmd.hasOption("params")){
			String paramdir = cmd.getOptionValue("params");

			if (paramdir == null)
			{
				System.err.println("Param directory must be specified");
				return;
			}

			System.out.println("Dumping params to " + paramdir);
			dumpParams(store, new File(paramdir));
		} else if (cmd.hasOption("regions")){
			String regiondir = cmd.getOptionValue("regions");

			if (regiondir == null)
			{
				System.err.println("Regions directory must be specified");
				return;
			}

			System.out.println("Dumping regions to " + regiondir);
			dumpRegions(store, new File(regiondir));
		} else if (cmd.hasOption("mapimages")){
			String imagesdir = cmd.getOptionValue("mapimages");

			if (imagesdir == null)
			{
				System.err.println("Map images directory must be specified");
				return;
			}

			System.out.println("Dumping map images to " + imagesdir);
			dumpMapImages(store, new File(imagesdir));
		}  else if (cmd.hasOption("models")){
			String imagesdir = cmd.getOptionValue("models");

			if (imagesdir == null)
			{
				System.err.println("Models directory must be specified");
				return;
			}

			System.out.println("Dumping models to " + imagesdir);
			dumpModels(store, new File(imagesdir));
		}
		else
		{
			System.err.println("Nothing to do");
		}


	}

	private static Store loadStore(String cache) throws IOException
	{
		Store store = new Store(new File(cache));
		store.load();
		return store;
	}

	private static void dumpItems(Store store, File itemdir) throws IOException
	{
		ItemManager dumper = new ItemManager(store);
		dumper.load();
		dumper.export(itemdir);
		dumper.java(itemdir);
	}

	private static void dumpNpcs(Store store, File npcdir) throws IOException
	{
		NpcManager dumper = new NpcManager(store);
		dumper.load();
		dumper.dump(npcdir);
		dumper.java(npcdir);
	}

	private static void dumpObjects(Store store, File objectdir) throws IOException
	{
		ObjectManager dumper = new ObjectManager(store);
		dumper.load();
		dumper.dump(objectdir);
		dumper.java(objectdir);
	}

	private static void dumpSprites(Store store, File spritedir) throws IOException
	{
		SpriteManager dumper = new SpriteManager(store);
		dumper.load();
		dumper.export(spritedir);
	}
	private static void dumpInterfaces(Store store, File interfacedir) throws IOException
	{
		InterfaceManager dumper = new InterfaceManager(store);
		dumper.load();
		dumper.export(interfacedir);
		dumper.java(interfacedir);
	}

	private static void dumpEnums(Store store, File enumdir) throws IOException
	{
		EnumManager dumper = new EnumManager(store);
		dumper.load();
		dumper.dump(enumdir);
	}

	private static void dumpStructs(Store store, File structdir) throws IOException
	{
		StructManager dumper = new StructManager(store);
		dumper.load();
		dumper.dump(structdir);
	}

	private static void dumpParams(Store store, File paramdir) throws IOException
	{
		ParamManager dumper = new ParamManager(store);
		dumper.load();
		dumper.dump(paramdir);
	}

	private static void dumpRegions(Store store, File regiondir) throws IOException
	{
		RegionManager dumper = new RegionManager(store);
		dumper.load();
		dumper.dump(regiondir);
	}
	private static void dumpMapImages(Store store, File imagedir) throws IOException
	{
		MapImageDumper dumper = new MapImageDumper(store);
		dumper.load();
		dumper.dump(imagedir);
	}
	private static void dumpModels(Store store, File modeldir) throws IOException
	{
		ModelDumper dumper = new ModelDumper(store);
		dumper.dump(modeldir, false);
	}
}
