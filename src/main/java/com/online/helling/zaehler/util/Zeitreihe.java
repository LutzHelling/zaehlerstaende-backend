package com.online.helling.zaehler.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class Zeitreihe<E> implements Set<Pair<Date, E>>,Serializable {

	private static final long serialVersionUID = -8547056562705166452L;
	TreeSet<Pair<Date, E>> speicher = new TreeSet<>();

	public Zeitreihe() {
		super();
	}

	public Zeitreihe(int size, Date start, int intervall, int amount, boolean backwards) {
		super();
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		if (backwards) {
			for (int i = size - 1; i >= 0; i--) {
				add(new ImmutablePair<Date, E>(cal.getTime(), null));
				cal.add(intervall, amount * -1);
			}
		} else {
			for (int i = 0; i < size; i++) {
				add(new ImmutablePair<Date, E>(cal.getTime(), null));
				cal.add(intervall, amount);
			}
		}
	}

	@Override
	public String toString() {
		return "Zeitreihe [speicher=" + speicher + "]";
	}

	public boolean add(Date key, E newElem) {
		this.speicher.add(new ImmutablePair<Date, E>(key, newElem));
		return true;
	}

	@Override
	public void clear() {
		this.speicher.clear();
	}

	@Override
	public boolean contains(Object o) {
		return this.speicher.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.speicher.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return this.speicher.isEmpty();
	}

	@Override
	public boolean remove(Object o) {
		return this.speicher.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.speicher.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.speicher.retainAll(c);
	}

	@Override
	public int size() {
		return this.speicher.size();
	}

	@Override
	public Object[] toArray() {
		return this.speicher.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.speicher.toArray(a);
	}

	@Override
	public boolean add(Pair<Date, E> e) {
		return this.speicher.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends Pair<Date, E>> c) {
		return this.speicher.addAll(c);
	}

	@Override
	public Iterator<Pair<Date, E>> iterator() {
		return this.speicher.iterator();
	}

	public E get(Date d) {
		for (Pair<Date, E> pair : speicher) {
			if (pair.getLeft().equals(d))
				return pair.getRight();
		}
		return null;
	}

	public E getPrevious(Date d) {
		Date maxi = new Date(0);
		E ret = null;
		for (Pair<Date, E> pair : speicher) {
			if (pair.getLeft().before(d) && pair.getLeft().after(maxi)) {
				maxi = pair.getLeft();
				ret = pair.getRight();
			}
		}
		return ret;
	}

	public E first() {
		if (speicher.size() > 0)
			return speicher.first().getRight();
		else
			return null;
	}

	public E last() {
		if (speicher.size() > 0)
			return speicher.last().getRight();
		else
			return null;
	}
}
