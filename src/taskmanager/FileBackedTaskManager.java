package taskmanager;
import taskpackage.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

	public static void main(String[] args) throws IOException {
		List<String> fileStrings = Files.readAllLines(Paths.get("resources/taskStorage.csv"));
		System.out.println(fileStrings);
	}
}
