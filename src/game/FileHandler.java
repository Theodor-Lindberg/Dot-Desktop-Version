package game;

import borrowedcode.RuntimeTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import static borrowedcode.InterfaceTypeAdapterFactory.getInterfaceTypeAdapterFactory;

/**
 * This class handles reading and saving levels to files.
 */
public final class FileHandler
{
    private final static RuntimeTypeAdapterFactory<Block> TYPE_FACTORY;

    static {
	TYPE_FACTORY =	RuntimeTypeAdapterFactory.of(Block.class, "type").registerSubtype(KeyBlock.class, KeyBlock.class.getName())
				.registerSubtype(Player.class, Player.class.getName()).registerSubtype(Block.class, Block.class.getName())
				.registerSubtype(EndBlock.class, EndBlock.class.getName()).registerSubtype(Enemy.class, Enemy.class.getName());
    }

    private FileHandler() {
    }

    public static Block[][] readLevel(final String fileName) throws IOException {
	final String content = Files.readString(Paths.get(fileName));
	final Gson gson = new GsonBuilder().registerTypeAdapterFactory(
		TYPE_FACTORY).registerTypeAdapterFactory(getInterfaceTypeAdapterFactory()).setPrettyPrinting().create();
	return gson.fromJson(content, Block[][].class);
    }

    public static void saveLevel(final String fileName, final Block[][] blocks) throws FileNotFoundException {
	final Gson gson = new GsonBuilder().registerTypeAdapterFactory(
		TYPE_FACTORY).registerTypeAdapterFactory(getInterfaceTypeAdapterFactory()).setPrettyPrinting().create();
	final String levelAsJson = gson.toJson(blocks, blocks.getClass());
	try (PrintWriter out = new PrintWriter(fileName)) {
	    out.println(levelAsJson);
	}
    }
}
