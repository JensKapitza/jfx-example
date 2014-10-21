package jfx;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import lang.ResourceBundleProperty;

public class Loader extends FXMLLoader {

	public Loader(URL resource, ResourceBundleProperty resources) {
		super(resource, resources);
	}

	@Override
	public <T> T load() throws IOException {
		T data = super.load();
		try {
			bind((Node) data);
		} catch (Exception e) {
			throw new IOException(e);
		}
		return data;
	}

	@Override
	public <T> T load(InputStream inputStream) throws IOException {
		T data = super.load(inputStream);
		try {
			bind((Node) data);
		} catch (Exception e) {
			throw new IOException(e);
		}
		return data;
	}

	private void bind(Node load) throws Exception {

		ResourceBundleProperty res = (ResourceBundleProperty) getResources();
		if (load instanceof Parent) {
			for (Node n : ((Parent) load).getChildrenUnmodifiable()) {
				bind(n);
			}
		}

		// suche alle Properties und nach einem §

		BeanInfo info = Introspector.getBeanInfo(load.getClass());
		for (MethodDescriptor desc : info.getMethodDescriptors()) {

			if (StringUtils.endsWith(desc.getName(), "Property")) {
				Method m = desc.getMethod();
				Class<?> retType = m.getReturnType();
				if (StringProperty.class.isAssignableFrom(retType)) {
					StringProperty prop = (StringProperty) m.invoke(load);
					String keyFull = prop.get();
					if (StringUtils.startsWith(keyFull, "§")) {
						String key = keyFull.substring(1);
						prop.bind(res.get(key));
					}

				}
			}
		}

	}
}
