package lang;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ResourceBundleProperty extends ResourceBundle {

	ResourceBundle resource;

	ConcurrentHashMap<String, StringProperty> weakList = new ConcurrentHashMap<String, StringProperty>();

	public ResourceBundleProperty(ResourceBundle resourceBundle) {
		this.resource = resourceBundle;
		
	}
	
	@Override
	public Locale getLocale() {
		return resource.getLocale();
	}

	public synchronized void setResource(ResourceBundle resource) {
		this.resource = resource;
		for (Entry<String, StringProperty> e : weakList.entrySet()) {
			StringProperty p = e.getValue();
			System.out.println(e.getKey() + " = " + p.get() + " > " + resource.getString(e.getKey()));
			if (p != null) {
				p.setValue(resource.getString(e.getKey()));
			}
		}
	}

	@Override
	protected synchronized String handleGetObject(String key) {
		return get(key).get();
	}

	@Override
	public Enumeration<String> getKeys() {
		return resource.getKeys();
	}

	public StringProperty get(String key) {
		StringProperty result = weakList.get(key);
		if (result == null) {
			result = new SimpleStringProperty(resource.getString(key));
			weakList.put(key, result);
		}
		return result;
	}

}
