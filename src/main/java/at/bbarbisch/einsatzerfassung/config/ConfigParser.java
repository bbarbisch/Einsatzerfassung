package at.bbarbisch.einsatzerfassung.config;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ConfigParser {

	public Config parse(Path configPath) throws ConfigException {
		try {
			var factory = DocumentBuilderFactory.newInstance();
			var builder = factory.newDocumentBuilder();
			var document = builder.parse(configPath.toFile());
			var rootElement = document.getDocumentElement();
			var config = new Config();
			
			parseTemplateDir(rootElement, config);
			parseTargetPaths(rootElement, config);
			
			return config;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new ConfigException(String.format("Could not parse the given configuration file '%s': %s", configPath.toString(), e.getMessage()), e);
		}
	}
	
	private void parseTemplateDir(Element parent, Config config) throws ConfigException {
		var node = getChildNode(parent, "templateDir");
		if(node != null) {
			var tmpPath = node.getTextContent();
			config.setTemplateDir(Paths.get(tmpPath));
		} else {
			throw new ConfigException("Invalid Configuration - the templateDir element must be present");
		}
	}
	
	private void parseTargetPaths(Element parent, Config config) throws ConfigException {
		var targetPaths = getChildNode(parent, "targetPaths");
		if(targetPaths != null) {
			var targetPathNodes = getChildNodes(targetPaths, "targetPath");
			if(targetPathNodes.size() == 0) {
				throw new ConfigException("Invalid Configuration - at least one targetPath element must be present");
			}
			for(var targetPath : targetPathNodes) {
				if(!targetPath.hasAttribute("path")) {
					throw new ConfigException("Invalid Configuration - missing path attribute for targetPath element");
				}
				var path = targetPath.getAttribute("path");
				parseTypes(targetPath, Paths.get(path), config);
			}
		} else {
			throw new ConfigException("Invalid Configuration - the targetPaths element must be present");
		}
	}
	
	private void parseTypes(Element parent, Path prefix, Config config) throws ConfigException {
		var types = getChildNodes(parent, "type");
		if(types.size() == 0) {
			throw new ConfigException("Invalid Configruation - no types defined for targetPath: " + parent.getAttribute("path"));
		}
		for(var type : types) {
			var typeValue = type.getTextContent();
			if(config.containsType(typeValue)) {
				throw new ConfigException("Invalid configuration - multiple occurrence for type: " + typeValue);
			}
			config.addPrefixPath(prefix, typeValue);
		}
	}
	
	private Element getChildNode(Node node, String childName) {
		var childs = node.getChildNodes();
		for(int i = 0; i < childs.getLength(); i++) {
			var child = childs.item(i);
			if(child.getNodeName().equals(childName)) {
				return (Element)child;
			}
		}
		return null;
	}
	
	private List<Element> getChildNodes(Node node, String childName) {
		var nodes = new ArrayList<Element>();
		var childs = node.getChildNodes();
		for(int i = 0; i < childs.getLength(); i++) {
			var child = childs.item(i);
			if(child.getNodeName().equals(childName)) {
				nodes.add((Element)child);
			}
		}
		return nodes;
	}
}
