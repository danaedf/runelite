package net.runelite.cache;

import net.runelite.cache.definitions.ModelDefinition;
import net.runelite.cache.definitions.loaders.ModelLoader;
import net.runelite.cache.fs.Archive;
import net.runelite.cache.fs.Index;
import net.runelite.cache.fs.Storage;
import net.runelite.cache.fs.Store;
import net.runelite.cache.models.ObjExporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class ModelDumper {
    private final Store store;

    public ModelDumper(Store store) {
        this.store = store;
    }

    public void dump(File modeldir, boolean convertModels) throws IOException
    {
        store.load();

        int count = 0;

        modeldir.mkdirs();

        Storage storage = store.getStorage();
        Index index = store.getIndex(IndexType.MODELS);
        ModelLoader loader = new ModelLoader();

        for (Archive archive : index.getArchives())
        {
            byte[] contents = archive.decompress(storage.loadArchive(archive));

            loader.load(archive.getArchiveId(), contents);

            int indexNumber = archive.getArchiveId();
            String outFileName = modeldir + File.separator + indexNumber + ".model";
            Path path = Paths.get(outFileName);
            Files.write(path, contents);

            if (convertModels)
            {
                convert(modeldir, indexNumber);
            }
            count++;
        }
        System.out.println(">>> Dumped models:" + count);
    }

    public void convert(File modelDir, int indexNumber) throws IOException
    {
        TextureManager tm = new TextureManager(store);
        tm.load();
        ModelLoader loader = new ModelLoader();

        String modelDirPath = modelDir.getPath();
        String modelFileAbsolutePath = modelDirPath + File.separator + indexNumber + ".model";

        ModelDefinition model = loader.load(indexNumber, Files.readAllBytes(new File(modelFileAbsolutePath).toPath()));
        ObjExporter exporter = new ObjExporter(tm, model);
        String objFileOut = modelDirPath + File.separator + indexNumber + ".obj";
        String mtlFileOut = modelDirPath + File.separator + indexNumber + ".mtl";
        try (PrintWriter objWriter = new PrintWriter(new FileWriter(objFileOut));
             PrintWriter mtlWriter = new PrintWriter(new FileWriter(mtlFileOut)))
        {
            exporter.export(objWriter, mtlWriter);
        }
    }
}
