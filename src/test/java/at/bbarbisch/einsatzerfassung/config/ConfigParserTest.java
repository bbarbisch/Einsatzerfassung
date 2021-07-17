package at.bbarbisch.einsatzerfassung.config;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ConfigParserTest {
	private ConfigParser parser;
	
	@TempDir
	Path tempDir;
	
	@BeforeEach
	private void setup() {
		parser = new ConfigParser();
	}
	
	@Test
	public void parseConfig() throws IOException, ConfigException {
		var path = createTestfile(tempDir, ""
				+ "<config>"
				+ " <templateDir>/test/path</templateDir>"
				+ " <targetPaths>"
				+ "  <targetPath path=\"asdf\">"
				+ "    <type>type1</type>"
				+ "    <type>type2</type>"
				+ "  </targetPath>"
				+ "  <targetPath path=\"jklö\">\"\n"
				+ "    <type>type3</type>"
				+ "  </targetPath>"
				+ " </targetPaths>"
				+ "</config>");
		
		var c = parser.parse(path);
		assertThat(c.getTemplateDir(), is(Paths.get("/test/path")));
		assertThat(c.getTypes(), hasSize(3));
		assertThat(c.getTypes(), containsInAnyOrder("type1", "type2", "type3"));
		assertThat(c.getPrefixPaths(), hasSize(2));
		assertThat(c.getPrefixPaths(), containsInAnyOrder(Paths.get("asdf"), Paths.get("jklö")));
	}
	
	@Test
	public void parseConfig_noTemplateDir() throws IOException {
		var path = createTestfile(tempDir, ""
				+ "<config>"
				+ " <targetPaths>"
				+ "  <targetPath path=\"asdf\">"
				+ "    <type>type1</type>"
				+ "  </targetPath>"
				+ " </targetPaths>"
				+ "</config>");
		
		assertThrows(ConfigException.class, () -> parser.parse(path));
	}
	
	@Test
	public void parseConfig_noTargetPath() throws IOException {
		var path = createTestfile(tempDir, ""
				+ "<config>"
				+ " <templateDir>/test/path</templateDir>"
				+ "</config>");
		
		assertThrows(ConfigException.class, () -> parser.parse(path));
	}
	
	@Test
	public void parseConfig_noTargetPathEntries() throws IOException {
		var path = createTestfile(tempDir, ""
				+ "<config>"
				+ " <templateDir>/test/path</templateDir>"
				+ " <targetPaths></targetPaths>"
				+ "</config>");
		
		assertThrows(ConfigException.class, () -> parser.parse(path));
	}
	
	@Test
	public void parseConfig_noTargetPathAttribute() throws IOException {
		var path = createTestfile(tempDir, ""
				+ "<config>"
				+ " <templateDir>/test/path</templateDir>"
				+ " <targetPaths>"
				+ "  <targetPath>"
				+ "    <type>type1</type>"
				+ "  </targetPath>"
				+ " </targetPaths>"
				+ "</config>");
		
		assertThrows(ConfigException.class, () -> parser.parse(path));
	}
	
	@Test
	public void parseConfig_noTypeEntries() throws IOException {
		var path = createTestfile(tempDir, ""
				+ "<config>"
				+ " <templateDir>/test/path</templateDir>"
				+ " <targetPaths>"
				+ "  <targetPath path=\"asdf\"></targetPath>"
				+ " </targetPaths>"
				+ "</config>");
		
		assertThrows(ConfigException.class, () -> parser.parse(path));
	}
	
	@Test
	public void parseConfig_duplicateTypeEntries_sameTargetPath() throws IOException {
		var path = createTestfile(tempDir, ""
				+ "<config>"
				+ " <templateDir>/test/path</templateDir>"
				+ " <targetPaths>"
				+ "  <targetPath path=\"asdf\">"
				+ "    <type>type1</type>"
				+ "    <type>type1</type>"
				+ "  </targetPath>"
				+ " </targetPaths>"
				+ "</config>");
		
		assertThrows(ConfigException.class, () -> parser.parse(path));
	}
	
	@Test
	public void parseConfig_duplicateTypeEntries_differentTargetPath() throws IOException {
		var path = createTestfile(tempDir, ""
				+ "<config>"
				+ " <templateDir>/test/path</templateDir>"
				+ " <targetPaths>"
				+ "  <targetPath path=\"asdf\">"
				+ "    <type>type1</type>"
				+ "    <type>type2</type>"
				+ "  </targetPath>"
				+ "  <targetPath path=\"jklö\">\"\n"
				+ "    <type>type1</type>"
				+ "  </targetPath>"
				+ " </targetPaths>"
				+ "</config>");
		
		assertThrows(ConfigException.class, () -> parser.parse(path));
	}
	
	@Test
	public void parseConfig_invalidXml() throws IOException {
		var path = createTestfile(tempDir, "asdf");
		
		assertThrows(ConfigException.class, () -> parser.parse(path));
	}
	
	private Path createTestfile(Path parent, String content) throws IOException {
		var path = parent.resolve("test.xml");
		Files.writeString(path, content);
		return path;
	}
}
