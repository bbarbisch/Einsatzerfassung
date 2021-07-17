package at.bbarbisch.einsatzerfassung.data;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import at.bbarbisch.einsatzerfassung.config.Config;

public class DataHandlerTest {
	private static final String TYPE1 = "type1";
	private static final String DATE1 = "20120503";
	private static final String YEAR = "2012";
	private static final String DESC1 = "test description";
	private static final String GENERATED_DIR = String.format("%s-%s-%s", DATE1, TYPE1, DESC1);
	private Config config;
	private DataHandler dataHandler;
	private Path tempDirSource;
	private Path tempDirTarget;
	
	@TempDir
	Path tempDir;
	
	@BeforeEach
	private void setup() throws IOException {
		tempDirSource = tempDir.resolve("source");
		tempDirTarget = tempDir.resolve("target");
		Files.createDirectory(tempDirSource);
		Files.createDirectory(tempDirTarget);
		
		config = new Config();
		config.setTemplateDir(tempDirSource);
		config.addPrefixPath(tempDirTarget, TYPE1);
		dataHandler = new DataHandler(config);
	}
	
	@Test
	public void createStructure() throws IOException, DataException {
		Files.createFile(tempDirSource.resolve("test1.txt"));
		Files.createFile(tempDirSource.resolve("test2.txt"));
		Files.createFile(tempDirSource.resolve("test3.txt"));
		
		var result = dataHandler.createDataStructure(TYPE1, DATE1, DESC1);
		
		var base = tempDirTarget.resolve(YEAR).resolve(GENERATED_DIR);
		var generated1 = base.resolve("test1.txt");
		var generated2 = base.resolve("test2.txt");
		var generated3 = base.resolve("test3.txt");
		assertThat(result, is(base));
		assertThat(Files.exists(generated1), is(true));
		assertThat(Files.exists(generated2), is(true));
		assertThat(Files.exists(generated3), is(true));
	}
	
	@Test
	public void createStructure_subdir() throws IOException, DataException {
		Files.createDirectories(tempDirSource.resolve("sub1"));
		Files.createDirectories(tempDirSource.resolve("sub2"));
		Files.createFile(tempDirSource.resolve("sub1").resolve("test1.txt"));
		Files.createFile(tempDirSource.resolve("sub1").resolve("test2.txt"));
		Files.createFile(tempDirSource.resolve("sub2").resolve("test3.txt"));
		
		var result = dataHandler.createDataStructure(TYPE1, DATE1, DESC1);
		
		var base = tempDirTarget.resolve(YEAR).resolve(GENERATED_DIR);
		var generated1 = base.resolve("sub1").resolve("test1.txt");
		var generated2 = base.resolve("sub1").resolve("test2.txt");
		var generated3 = base.resolve("sub2").resolve("test3.txt");
		assertThat(result, is(base));
		assertThat(Files.exists(generated1), is(true));
		assertThat(Files.exists(generated2), is(true));
		assertThat(Files.exists(generated3), is(true));
	}
	
	@Test
	public void createStructure_emptySubdir() throws IOException, DataException {
		Files.createDirectories(tempDirSource.resolve("sub1"));
		Files.createDirectories(tempDirSource.resolve("sub2"));
		Files.createFile(tempDirSource.resolve("sub1").resolve("test1.txt"));
		Files.createFile(tempDirSource.resolve("sub1").resolve("test2.txt"));
		
		var result = dataHandler.createDataStructure(TYPE1, DATE1, DESC1);
		
		var base = tempDirTarget.resolve(YEAR).resolve(GENERATED_DIR);
		var generated1 = base.resolve("sub1").resolve("test1.txt");
		var generated2 = base.resolve("sub1").resolve("test2.txt");
		var generated3 = base.resolve("sub2");
		assertThat(result, is(base));
		assertThat(Files.exists(generated1), is(true));
		assertThat(Files.exists(generated2), is(true));
		assertThat(Files.exists(generated3), is(true));
	}
	
	@Test
	public void createStructure_empty() throws DataException, IOException {		
		var result = dataHandler.createDataStructure(TYPE1, DATE1, DESC1);
		
		var generated1 = tempDirTarget.resolve(YEAR).resolve(GENERATED_DIR);
		assertThat(result, is(generated1));
		assertThat(Files.exists(generated1), is(true));
		assertThat(Files.list(generated1).count(), is(0L));
		
	}
	
	@Test
	public void createStructure_targetAlreadyExists() throws IOException {
		Files.createDirectories(tempDirTarget.resolve(YEAR).resolve(GENERATED_DIR));
		
		assertThrows(DataException.class, () -> dataHandler.createDataStructure(TYPE1, DATE1, DESC1));
	}
	
	@Test
	public void createStructure_sourceDirEqualsTargetDir() {
		config.setTemplateDir(tempDirTarget);
		assertThrows(DataException.class, () -> dataHandler.createDataStructure(TYPE1, DATE1, DESC1));
	}
	
	@Test
	public void debug() {
		String dateString = "01.01.2012";
		assertThat(dateString.matches("\\d{2}\\.\\d{2}\\.\\d{4}"), is(true));
	}
}
