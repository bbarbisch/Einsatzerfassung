package at.bbarbisch.einsatzerfassung.config;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Config {
	private Path templateDir;
	private List<PrefixType> prefixPaths;
	
	public Config() {
		templateDir = null;
		prefixPaths = new ArrayList<>();
	}
	
	public List<String> getTypes() {
		return prefixPaths.stream().map(x -> x.type).collect(Collectors.toList());
	}
	
	public boolean containsType(String type) {
		return prefixPaths.stream().anyMatch(x -> x.type.equals(type));
	}
	
	public void setTemplateDir(Path templateDir) {
		this.templateDir = templateDir;
	}
	
	public Path getTemplateDir() {
		return this.templateDir;
	}
	
	public void addPrefixPath(Path prefix, String type) {
		var entry = new PrefixType(prefix, type);
		if(!prefixPaths.contains(entry)) {
			prefixPaths.add(entry);
		}
	}
	
	public List<Path> getPrefixPaths() {
		return prefixPaths.stream()
				.map(x -> x.prefixPath)
				.distinct()
				.collect(Collectors.toList());
	}
	
	public Path getPrefixPathForType(String type) {
		return prefixPaths.stream()
				.filter(x -> x.type.equals(type))
				.map(x -> x.prefixPath)
				.findFirst()
				.get();
	}
	
	private static class PrefixType {
		Path prefixPath;
		String type;
		
		public PrefixType(Path prefixPath, String type) {
			this.prefixPath = prefixPath;
			this.type = type;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof PrefixType)) {
				return false;
			}
			
			var other = (PrefixType)obj;
			return this.prefixPath.equals(other.prefixPath) && this.type.equals(other.type);
		}
	}
}
