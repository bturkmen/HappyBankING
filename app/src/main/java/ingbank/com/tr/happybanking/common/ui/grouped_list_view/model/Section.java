package ingbank.com.tr.happybanking.common.ui.grouped_list_view.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.UUID;

/**
 * Created by Furkan Bayraktar
 * Created on 9/23/14.
 */
public abstract class Section<E extends SectionData> implements
        Serializable,
        Cloneable,
        RandomAccess,
        List<E> {

    private String id;

    private String header;
    private ArrayList<E> data;

    public Section(String header) {
        this.id = UUID.randomUUID().toString();
        this.header = header;
        this.data = new ArrayList<E>();
    }

    @Override
    public void add(int location, E object) {
        data.add(location, object);
    }

    @Override
    public boolean add(E object) {
        return data.add(object);
    }

    @Override
    public boolean addAll(int location, Collection<? extends E> collection) {
        try {
            for (E aCollection : collection) {
                data.add(location, aCollection);
                location++;
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        boolean result = true;
        for (E aCollection : collection) {
            result = data.add(aCollection);
        }
        return result;
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public boolean contains(Object object) {
        return data.contains(object);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return data.containsAll(collection);
    }

    @Override
    public E get(int location) {
        return data.get(location);
    }

    @Override
    public int indexOf(Object object) {
        return data.indexOf(object);
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<E> iterator() {
        return data.iterator();
    }

    @Override
    public int lastIndexOf(Object object) {
        return data.lastIndexOf(object);
    }

    @NonNull
    @Override
    public ListIterator<E> listIterator() {
        return data.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<E> listIterator(int location) {
        return data.listIterator(location);
    }

    @Override
    public E remove(int location) {
        return data.remove(location);
    }

    @Override
    public boolean remove(Object object) {
        return data.remove(object);
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        return data.removeAll(collection);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return data.retainAll(collection);
    }

    @Override
    public E set(int location, E object) {
        return data.set(location, object);
    }

    @Override
    public int size() {
        return data.size();
    }

    @NonNull
    @Override
    public List<E> subList(int start, int end) {
        return data.subList(start, end);
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return data.toArray();
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] array) {
        return data.toArray(array);
    }

    public String getHeader() {
        return header;
    }

    public String getId() {
        return id;
    }

}
