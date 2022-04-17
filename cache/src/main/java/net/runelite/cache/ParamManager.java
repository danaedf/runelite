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
import net.runelite.cache.definitions.StructDefinition;
import net.runelite.cache.definitions.exporters.ParamExporter;
import net.runelite.cache.definitions.exporters.StructExporter;
import net.runelite.cache.definitions.loaders.ParamLoader;
import net.runelite.cache.definitions.providers.ParamProvider;
import net.runelite.cache.fs.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ParamManager implements ParamProvider
{
	private final Store store;
	private final Map<Integer, ParamDefinition> params = new HashMap<>();

	public ParamManager(Store store)
	{
		this.store = store;
	}

	public void load() throws IOException
	{
		ParamLoader loader = new ParamLoader();

		Storage storage = store.getStorage();
		Index index = store.getIndex(IndexType.CONFIGS);
		Archive archive = index.getArchive(ConfigType.PARAMS.getId());

		byte[] archiveData = storage.loadArchive(archive);
		ArchiveFiles files = archive.getFiles(archiveData);

		for (FSFile f : files.getFiles())
		{
			ParamDefinition def = loader.load(f.getContents());
			params.put(f.getFileId(), def);
		}
	}

	public void dump(File out) throws IOException
	{
		out.mkdirs();

		for (Map.Entry<Integer, ParamDefinition> entry : params.entrySet())
		{
			int id = entry.getKey();
			ParamDefinition def = entry.getValue();

			ParamExporter exporter = new ParamExporter(def);

			File targ = new File(out, id + ".json");
			exporter.exportTo(targ);
		}
	}

	public Map<Integer, ParamDefinition> getParams()
	{
		return Collections.unmodifiableMap(params);
	}

	public ParamDefinition getParam(int paramId)
	{
		return params.get(paramId);
	}

	@Override
	public ParamDefinition provide(int paramId)
	{
		return getParam(paramId);
	}


}
