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

import net.runelite.cache.definitions.EnumDefinition;
import net.runelite.cache.definitions.exporters.EnumExporter;
import net.runelite.cache.definitions.loaders.EnumLoader;
import net.runelite.cache.definitions.providers.EnumProvider;
import net.runelite.cache.definitions.providers.StructProvider;
import net.runelite.cache.fs.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EnumManager implements EnumProvider
{
	private final Store store;
	private final Map<Integer, EnumDefinition> enums = new HashMap<>();

	public EnumManager(Store store)
	{
		this.store = store;
	}

	public void load() throws IOException
	{
		EnumLoader loader = new EnumLoader();

		Storage storage = store.getStorage();
		Index index = store.getIndex(IndexType.CONFIGS);
		Archive archive = index.getArchive(ConfigType.ENUM.getId());

		byte[] archiveData = storage.loadArchive(archive);
		ArchiveFiles files = archive.getFiles(archiveData);

		for (FSFile f : files.getFiles())
		{
			EnumDefinition def = loader.load(f.getFileId(), f.getContents());
			if(def != null) {
				enums.put(f.getFileId(), def);
			}
		}
	}

	public Map<Integer, EnumDefinition> getEnums()
	{
		return Collections.unmodifiableMap(enums);
	}

	public EnumDefinition getEnum(int structId)
	{
		return enums.get(structId);
	}

	@Override
	public EnumDefinition provide(int structId)
	{
		return getEnum(structId);
	}

	public void dump(File out) throws IOException
	{
		out.mkdirs();

		for (EnumDefinition def : enums.values())
		{
			EnumExporter exporter = new EnumExporter(def);

			File targ = new File(out, def.getId() + ".json");
			exporter.exportTo(targ);
		}
	}
}
