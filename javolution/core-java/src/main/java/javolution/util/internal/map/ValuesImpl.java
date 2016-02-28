/*
 * Javolution - Java(TM) Solution for Real-Time and Embedded Systems
 * Copyright (C) 2012 - Javolution (http://javolution.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javolution.util.internal.map;

import java.util.Iterator;

import javolution.util.FastCollection;
import javolution.util.FastMap;
import javolution.util.function.Equality;

/**
 * A collection view over a map values.
 */
public final class ValuesImpl<K, V> extends FastCollection<V> {

	private static final long serialVersionUID = 0x700L; // Version.
	private final FastMap<K, V> map;

	public ValuesImpl(FastMap<K, V> map) {
		this.map = map;
	}

	@Override
	public boolean add(V element) {
		throw new UnsupportedOperationException(
				"Values cannot be added directly to maps");
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public FastCollection<V> clone() {
		return new ValuesImpl<K, V>(map.clone());
	}

	@Override
	public boolean contains(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Equality<? super V> equality() {
		return map.valueEquality();
	}

	@Override
	public Iterator<V> iterator() {
		return new IteratorImpl<K, V>(new MapEntryIteratorImpl<K,V>(map));
	}

	@Override
	public int size() {
		return map.size();
	}

	/** Then generic iterator over the map values */
	private static class IteratorImpl<K, V> implements Iterator<V> {
		final MapEntryIteratorImpl<K,V> mapItr;

		public IteratorImpl(MapEntryIteratorImpl<K,V> mapItr) {
			this.mapItr = mapItr;
		}

		@Override
		public boolean hasNext() {
			return mapItr.hasNext();
		}

		@Override
		public V next() {
			return mapItr.next().getValue();
		}

		@Override
		public void remove() {
			mapItr.remove();
		}
	}

}
