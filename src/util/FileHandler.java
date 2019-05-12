package util;

import borrowedcode.RuntimeTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.GameObjects.Block;
import game.GameObjects.EndBlock;
import game.GameObjects.MovingObjects.Enemy;
import game.GameObjects.KeyBlock;
import game.GameObjects.MovingObjects.Player;
import gui.LevelChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static borrowedcode.InterfaceTypeAdapterFactory.getInterfaceTypeAdapterFactory;

/**
 * This class handles reading and saving levels to files.
 */
public final class FileHandler
{
    private final static RuntimeTypeAdapterFactory<Block> TYPE_FACTORY;
    private final static String RESOURCE_DIRECTORY;

    static {
	RESOURCE_DIRECTORY = "levels/";
	TYPE_FACTORY =	RuntimeTypeAdapterFactory.of(Block.class, "type").registerSubtype(KeyBlock.class, KeyBlock.class.getName())
				.registerSubtype(Player.class, Player.class.getName()).registerSubtype(Block.class, Block.class.getName())
				.registerSubtype(EndBlock.class, EndBlock.class.getName()).registerSubtype(Enemy.class, Enemy.class.getName());
    }

    private FileHandler() {
    }

    /**
     * Read a level from a file or resource folder. If the file name exists in the resource folder and the file system,
     * the level in the resource folder will be prioritized.
     *
     * @param fileName 	The name of the file to read.
     *
     * @return 		An 2D array of the blocks read from the file.
     * @throws IOException
     */
    public static Block[][] readLevel(final String fileName) throws IOException {
        return (isResourceLevel(fileName)) ? readLevelFromResources(fileName) : readLevelFromFile(fileName);
    }

    /**
     * Read a level from a file on the local file system of the computer.
     *
     * @param fileName 	The name of the file to read.
     *
     * @return 		An 2D array of the blocks read from the file.
     * @throws IOException
     */
    private static Block[][] readLevelFromFile(final String fileName) throws IOException {
	return interpretJsonData(Files.readString(Paths.get(fileName)));
    }

    /**
     * Check if a file exists in the resource folder.
     *
     * @param fileName 	The name of the file to check.
     *
     * @return 		True if the file is found in the resource folder.
     */
    private static boolean isResourceLevel(final String fileName) {
	return ClassLoader.getSystemClassLoader().getResource(RESOURCE_DIRECTORY + LevelChooser.addFileExtension(fileName)) != null;
    }

    /**
     * Read a level from a resource file.
     *
     * @param fileName 	The name of the file to read from.
     *
     * @return 		An 2D array of the blocks read from the file.
     */
    private static Block[][] readLevelFromResources(String fileName) {
        fileName = RESOURCE_DIRECTORY + LevelChooser.addFileExtension(fileName);

        // borrowcode
	final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
	final InputStream inputStream = classLoader.getResourceAsStream(fileName);
	if (inputStream != null) {
	    final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	    return interpretJsonData(reader.lines().collect(Collectors.joining(System.lineSeparator())));
	}
	return null;
    }

    /**
     * Convert JSON data to a 2D array of blocks.
     *
     * @param jsonData 	The JSON data to interpret.
     *
     * @return 		An 2D array of the blocks.
     */
    private static Block[][] interpretJsonData(final String jsonData) {
	final Gson gson = new GsonBuilder().registerTypeAdapterFactory(
		TYPE_FACTORY).registerTypeAdapterFactory(getInterfaceTypeAdapterFactory()).setPrettyPrinting().create();
	return gson.fromJson(jsonData, Block[][].class);
    }

    /**
     * Save a 2D array of blocks to a file in JSON format.
     *
     * @param fileName 	The name of the file to save to.
     * @param blocks 	The 2D array of blocks to save.
     *
     * @throws FileNotFoundException
     */
    public static void saveLevel(final String fileName, final Block[][] blocks) throws FileNotFoundException {
	final Gson gson = new GsonBuilder().registerTypeAdapterFactory(
		TYPE_FACTORY).registerTypeAdapterFactory(getInterfaceTypeAdapterFactory()).create();
	final String levelAsJson = gson.toJson(blocks, blocks.getClass());
	try (PrintWriter out = new PrintWriter(fileName)) {
	    out.println(levelAsJson);
	}
    }

    /**
     * Get a list of all the names of the levels in the resource folder.
     *
     * @return A list of level names.
     * @throws IOException
     * @throws URISyntaxException
     */
    public static List<String> getLevelsFromResources() throws IOException, URISyntaxException {
        return borrowcode_getLevelsFromResources();
    }

    /**
     * Get a list of all the names of the levels in the resource folder.
     *
     * @return A list of level names.
     * @throws IOException
     * @throws URISyntaxException
     */
    private static List<String> borrowcode_getLevelsFromResources() throws IOException, URISyntaxException {
	final List<String> levelsFound = new ArrayList<>();

	final File jarFile = new File(FileHandler.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	if (jarFile.isFile()) {  // Run with JAR file
	    final JarFile jar = new JarFile(jarFile);
	    final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
	    while (entries.hasMoreElements()) {
		final String name = entries.nextElement().getRealName();
		if (name.contains(RESOURCE_DIRECTORY) && !name.equals(RESOURCE_DIRECTORY)) { //filter according to the path
		    levelsFound.add(LevelChooser.removeFileExtension(name.replace(RESOURCE_DIRECTORY, "")));
		}
	    }
	    jar.close();
	} else { // Run with IDE
	    final URL url = FileHandler.class.getResource("/" + RESOURCE_DIRECTORY);
	    if (url != null) {
		final File levels = new File(url.toURI());
		for (File level : levels.listFiles()) {
		    levelsFound.add(LevelChooser.removeFileExtension(level.getName()));
		}
	    }
	}

	levelsFound.sort(String::compareToIgnoreCase);
	return levelsFound;
    }
}
