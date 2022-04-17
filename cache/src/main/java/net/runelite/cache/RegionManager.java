/*
 * Copyright (c) 2018, Joshua Filby <joshua@filby.me>
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

import net.runelite.cache.definitions.ParamDefinition;
import net.runelite.cache.definitions.exporters.ParamExporter;
import net.runelite.cache.definitions.exporters.RegionExporter;
import net.runelite.cache.definitions.loaders.ParamLoader;
import net.runelite.cache.definitions.providers.RegionProvider;
import net.runelite.cache.fs.*;
import net.runelite.cache.region.Region;
import net.runelite.cache.region.RegionLoader;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RegionManager implements RegionProvider
{
	private final Store store;
	private final RegionLoader regionLoader;

	public RegionManager(Store store)
	{
		this.store = store;
		this.regionLoader = new RegionLoader(store);
	}

	public void load() throws IOException
	{
		regionLoader.loadRegions();
	}

	public void dump(File out) throws IOException
	{
		out.mkdirs();

		for (Region region : regionLoader.getRegions())
		{
			RegionExporter exporter = new RegionExporter(region);

			File targ = new File(out, region.getRegionID() + ".json");
			exporter.exportTo(targ);
		}
	}


	public Region getRegion(int regionId)
	{
		return regionLoader.getRegion(regionId);
	}

	@Override
	public Region provide(int regionId)
	{
		return getRegion(regionId);
	}


}
