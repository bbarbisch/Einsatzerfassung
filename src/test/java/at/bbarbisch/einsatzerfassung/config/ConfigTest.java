package at.bbarbisch.einsatzerfassung.config;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConfigTest {
	private Config config;
	
	@BeforeEach
	private void setup() {
		config = new Config();
	}
	
	@Test
	public void addPrefixType() {
		config.addPrefixPath(Paths.get("1"), "test1");
		config.addPrefixPath(Paths.get("2"), "test2");
		var types = config.getTypes();
		assertThat(types, containsInAnyOrder("test1", "test2"));
	}
	
	@Test
	public void addPrefixType_existsAlready() {
		config.addPrefixPath(Paths.get("1"), "test1");
		config.addPrefixPath(Paths.get("1"), "test1");
		var types = config.getTypes();
		assertThat(types, hasSize(1));
		assertThat(types, containsInAnyOrder("test1"));
	}
	
	@Test
	public void getTypes_empty() {
		var types = config.getTypes();
		assertThat(types, is(not(nullValue())));
		assertThat(types, is(empty()));
	}
	
	@Test
	public void setTemplateFile() {
		var path = Paths.get("test1");
		config.setTemplateDir(path);
		var templateDir = config.getTemplateDir();
		assertThat(templateDir, is(path));
	}
	
	@Test
	public void getTemplateFiles_empty() {
		var templateDir = config.getTemplateDir();
		assertThat(templateDir, is(nullValue()));
	}
	
	@Test
	public void addPrefix() {
		var path1 = Paths.get("test1");
		var path2 = Paths.get("test2");
		config.addPrefixPath(path1, "type1");
		config.addPrefixPath(path2, "type2");
		var prefixPaths = config.getPrefixPaths();
		assertThat(prefixPaths, containsInAnyOrder(path1, path2));
	}
	
	@Test
	public void addPrefix_existsAlready() {
		var path1 = Paths.get("test1");
		var path2 = Paths.get("test1");
		config.addPrefixPath(path1, "type1");
		config.addPrefixPath(path2, "type2");
		var prefixPaths = config.getPrefixPaths();
		assertThat(prefixPaths, hasSize(1));
		assertThat(prefixPaths, containsInAnyOrder(path1));
	}
	
	@Test
	public void getPrefixPath_empty() {
		var prefixPaths = config.getPrefixPaths();
		assertThat(prefixPaths, is(not(nullValue())));
		assertThat(prefixPaths, is(empty()));
	}
}
