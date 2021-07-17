package at.bbarbisch.einsatzerfassung.data;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import at.bbarbisch.einsatzerfassung.config.Config;

public class DataHandler {
	private Config config;
	
	public DataHandler(Config config) {
		this.config = config;
	}
	
	public Path createDataStructure(String type, String date, String description) throws DataException {
		if(config.getTemplateDir().equals(config.getPrefixPathForType(type))) {
			throw new DataException("The source directory is equal to the target directory");
		}
		
		Path source = config.getTemplateDir();
		String dirName = String.format("%s-%s-%s", date, type, description);
		String year = date.substring(0, 4);
		Path target = config.getPrefixPathForType(type).resolve(year).resolve(dirName);
		if(Files.exists(target)) {
			throw new DataException("Target Directory already exists");
		}
		
		try {
			Files.walkFileTree(config.getTemplateDir(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					Files.createDirectories(target.resolve(source.relativize(dir)));
			        return FileVisitResult.CONTINUE;
				}
				
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.copy(file, target.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
			        return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			throw new DataException("Error occured during copying files to target: " + e.getMessage(), e);
		}
		
		return target;
	}
}
