package org.goko.core.common.io.xml;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.strategy.TreeStrategy;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.OutputNode;

public class DerivedTreeStrategy extends TreeStrategy implements Strategy {

	@Override
	public boolean write(Type type, Object value, NodeMap node, Map map) {
		Class actual = value.getClass();
		Class expect = type.getType();
		Class real = actual;

		if (actual != expect) {
			((OutputNode) node.getNode()).setName(getAnnotation(real, Root.class).name());
		}
		return false;
	}

	private <T extends Annotation> T getAnnotation(Class<?> type, Class<T> label) {
		return type.getAnnotation(label);
	}

	@Override
	public Value read(Type type, NodeMap node, Map map) throws Exception {
		Class actual = readValue(type, node);
		Class expect = type.getType();

		if (expect != actual) {
			return new ObjectValue(actual);
		}

		return super.read(type, node, map);
	}

	private Class readValue(Type type, NodeMap node) throws Exception {
//		Reflections reflections = new Reflections("org.goko");
//		Predicate d = null;
//		Set<Class<?>> subtype = reflections.getSubTypesOf(type.getType());
//		InputNode inputNode = (InputNode) node.getNode();
//
//		Iterator<Class<?>> iterator = subtype.iterator();
//
//		while(iterator.hasNext()){
//			Class<?> subClass = iterator.next();
//			Root root = subClass.getAnnotation(Root.class);
//			if(StringUtils.equals(root.name(), inputNode.getName())){
//				return subClass;
//			}
//		}

		return type.getType();
	}
}