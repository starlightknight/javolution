/*
 * Javolution - Java(TM) Solution for Real-Time and Embedded Systems
 * Copyright (C) 2012 - Javolution (http://javolution.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.javolution.util.internal.map;

import java.io.Serializable;
import java.util.Comparator;

import org.javolution.util.FastMap;
import org.javolution.util.FastTable;
import org.javolution.util.function.Equality;
import org.javolution.util.function.Order;

/**
 * A sorted map (based on comparators).
 */
public final class SortedMapImpl<K, V> extends FastMap<K, V> {

	/**
	 * The entry implementation.
	 */
	private static final class EntryImpl<K, V> extends Entry<K, V> implements Serializable {
		private static final long serialVersionUID = 0x700L; // Version.
		public K key;
		public V value;

		public EntryImpl(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public boolean equals(Object obj) { // As per Map.Entry contract.
			if (!(obj instanceof Entry))
				return false;
			@SuppressWarnings("unchecked")
			Entry<K, V> that = (Entry<K, V>) obj;
			return Order.ARBITRARY.areEqual(key, that.getKey())
					&& Order.ARBITRARY.areEqual(value, that.getValue());
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public int hashCode() { // As per Map.Entry contract.
			return Order.ARBITRARY.indexOf(key) ^ Order.ARBITRARY.indexOf(value);
		}

		V setValuePrivate(V newValue) {
			V oldValue = value;
			this.value = newValue;
			return oldValue;
		}

		@Override
		public String toString() {
			return "(" + key + '=' + value + ')'; // For debug.
		}
	}
	private static final long serialVersionUID = 0x700L; // Version.
	private final Comparator<Object> comparator;

	private FastTable<EntryImpl<K, V>> entries;

	public SortedMapImpl(final Order<? super K> order) {
		comparator = new Comparator<Object>() {
            // Supports entry or key comparisons.
			@SuppressWarnings("unchecked")
			@Override
			public int compare(Object left, Object right) {
				K leftKey = (left instanceof EntryImpl) ? 
						((EntryImpl<K,V>)left).key : (K) left;
				K rightKey = (right instanceof EntryImpl) ? 
								((EntryImpl<K,V>)right).key : (K) right;
				return order.compare(leftKey, rightKey);
			}};
			entries = FastTable.newTable();
	}

	@Override
	public EntryImpl<K, V> ceilingEntry(K key) {
        int i = entries.indexOfSorted(key, comparator);
        i = (i < 0) ? -(i+1) : i;
        return i < entries.size() ? entries.get(i) : null;
	}
	
	@Override
	public void clear() {
		entries.clear();
	}

	@Override
	public SortedMapImpl<K, V> clone() {
		// TODO
		return null;
	}

	@Override
	public Order<? super K> comparator() {
		return null;
	}
	
	@Override
	public EntryImpl<K, V> firstEntry() {
		return entries.peekFirst();
	}

	@Override
	public EntryImpl<K, V> floorEntry(K key) {
        int i = entries.indexOfSorted(key, comparator);
        i = (i < 0) ? -(i+1)-1 : i;
        return i >= 0 ? entries.get(i) : null;
	}

	@Override
	public EntryImpl<K, V> getEntry(K key) {
        int i = entries.indexOfSorted(key, comparator);
		return i >= 0 ? entries.get(i) : null;
	}

	@Override
	public EntryImpl<K, V> higherEntry(K key) {
        int i = entries.indexOfSorted(key, comparator);
        i = (i < 0) ? -(i+1) : i+1;
        return i < entries.size() ? entries.get(i) : null;
	}

	@Override
	public EntryImpl<K, V> lastEntry() {
		return entries.peekLast();
	}

	@Override
	public EntryImpl<K, V> lowerEntry(K key) {
        int i = entries.indexOfSorted(key, comparator);
        i = (i < 0) ? -(i+1)-1 : i-1;
        return i >= 0 ? entries.get(i) : null;
	}

	@Override
	public V put(K key, V value) {
        int i = entries.indexOfSorted(key, comparator);
        if (i >= 0) return entries.get(i).setValuePrivate(value);
		entries.add(new EntryImpl<K,V>(key, value));
		return null;
	}

	@Override
	public EntryImpl<K, V> removeEntry(K key) {
        int i = entries.indexOfSorted(key, comparator);
        if (i < 0) return null;
        return entries.remove(i);
	}

	@Override
	public int size() {
		return entries.size();
	}	
	
	@Override
	public Equality<? super V> valuesEquality() {
		return Equality.STANDARD;
	}

}
