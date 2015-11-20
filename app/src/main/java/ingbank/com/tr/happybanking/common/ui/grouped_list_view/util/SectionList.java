package ingbank.com.tr.happybanking.common.ui.grouped_list_view.util;

import android.support.annotation.NonNull;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

import ingbank.com.tr.happybanking.common.ui.grouped_list_view.model.Section;
import ingbank.com.tr.happybanking.common.ui.grouped_list_view.model.SectionData;

/**
 * Created by Furkan Bayraktar
 * Created on 10/2/14.
 */
public class SectionList<E extends SectionData> implements GroupedList<E>, Serializable, Cloneable, RandomAccess {

    private ArrayList<Section<E>> sections;

    public SectionList() {
        this.sections = new ArrayList<Section<E>>();
    }

    @Override
    public void add(int location, Section<E> object) {
        sections.add(location, object);
    }

    @Override
    public boolean add(Section<E> object) {
        return sections.add(object);
    }

    @Override
    public boolean addAll(int location, Collection<? extends Section<E>> collection) {
        return sections.addAll(location, collection);
    }

    @Override
    public boolean addAll(Collection<? extends Section<E>> collection) {
        return sections.addAll(collection);
    }

    @Override
    public void clear() {
        sections.clear();
    }

    @Override
    public boolean contains(Object object) {
        return sections.contains(object);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return sections.containsAll(collection);
    }

    @Override
    public Section<E> get(int location) {
        return sections.get(location);
    }

    @Override
    public int indexOf(Object object) {
        return sections.indexOf(object);
    }

    @Override
    public boolean isEmpty() {
        return sections.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<Section<E>> iterator() {
        return sections.iterator();
    }

    @Override
    public int lastIndexOf(Object object) {
        return sections.lastIndexOf(object);
    }

    @NonNull
    @Override
    public ListIterator<Section<E>> listIterator() {
        return sections.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<Section<E>> listIterator(int location) {
        return sections.listIterator(location);
    }

    @Override
    public Section<E> remove(int location) {
        return sections.remove(location);
    }

    @Override
    public boolean remove(Object object) {
        return sections.remove(object);
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        return sections.retainAll(collection);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return sections.retainAll(collection);
    }

    @Override
    public Section<E> set(int location, Section<E> object) {
        return sections.set(location, object);
    }

    @Override
    public int size() {
        return sections.size();
    }

    @NonNull
    @Override
    public List<Section<E>> subList(int start, int end) {
        return sections.subList(start, end);
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return sections.toArray();
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] array) {
        return sections.toArray(array);
    }

    @Override
    public int lastSectionSize() {
        if (this.sections.size() > 0) {
            return this.sections.get(this.sections.size() - 1).size();
        } else {
            return 0;
        }
    }
}
