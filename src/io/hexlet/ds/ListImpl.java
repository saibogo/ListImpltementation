package io.hexlet.ds;

import java.util.*;

public class ListImpl<E> implements List<E> {

    private E[] storage =  (E[]) new Object[1];
    private int useInStorage = 0;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append('[');
        for (int i = 0; i < this.size(); i++) {
            result.append(" " + this.storage[i]);
        }
        result.append(']');
        return result.toString();
    }

    @Override
    public int size() {
        return this.useInStorage;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return this.indexOf(o) >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        return this.listIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[this.size()];
        for (int i = 0; i < this.size(); i++) {
            result[i] = this.storage[i];
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        if (ts.length < this.size()) return (T[]) Arrays.copyOf(storage, this.size(), ts.getClass());

        System.arraycopy(storage, 0, ts, 0, this.size());

        if (ts.length > this.size()) ts[this.size()] = null;

        return ts;
    }

    @Override
    public boolean add(E e) {
       add(this.size(), e);
        return true;
    }


    @Override
    public boolean remove(Object o) {
        int i = this.indexOf(o);
        if (i < 0) return false;

        final Object current = this.remove(i);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object item: collection) {
            if (!this.contains(item)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        for (E item: collection) {
            this.add(item);
        }
        return true;
    }

    @Override
    public boolean addAll(int i, Collection<? extends E> collection) throws IndexOutOfBoundsException{
        if (i < 0 || i > this.size()) throw new IndexOutOfBoundsException();
        int j = i;
        for (E item: collection) {
            this.add(j, item);
            j++;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        int startSize = this.size();
        for (Object item: collection) {
            this.remove(item);
        }
        return startSize != this.size();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        int startSize = this.size();
        List<E> newList = new ListImpl<>();
        for (int i = 0; i < this.size(); i++) {
            if (collection.contains(this.storage[i])) {
                newList.add(this.get(i));
            }
        }
        this.storage = (E[])newList.toArray();
        this.useInStorage = this.storage.length;
        if (this.size() == 0) {
            this.storage = (E[]) new Object[1];
        }
        return startSize != this.size();
    }

    @Override
    public void clear() {
        this.useInStorage = 0;
    }

    @Override
    public E get(int i) throws IndexOutOfBoundsException{
        if (i < 0 || i >= this.size()) throw new IndexOutOfBoundsException();
        return this.storage[i];
    }

    @Override
    public E set(int i, E e) throws IndexOutOfBoundsException{
        if (i < 0 || i >= this.size()) throw new IndexOutOfBoundsException();
        final E current = this.get(i);
        this.storage[i] = e;
        return current;
    }

    @Override
    public void add(int i, E e) throws IndexOutOfBoundsException {
        if (i < 0 || i > this.size()) throw new IndexOutOfBoundsException();
        if (this.storage.length == this.size()) {
            E[] newStorage = (E[]) new Object[this.size() * 2];
            System.arraycopy(this.storage, 0, newStorage, 0, this.size());
            this.storage = newStorage;
        }
        for (int j = this.size(); j > i; j--) {
            this.storage[j] = this.storage[j - 1];
        }
        this.storage[i] = e;
        this.useInStorage++;

    }

    @Override
    public E remove(int i) throws IndexOutOfBoundsException {
        if (i < 0 || i >= this.size()) throw new IndexOutOfBoundsException();
        final E current = this.get(i);
        for (int j = i + 1; j < this.size(); j++) {
            this.storage[j - 1] = this.storage[j];
        }
        this.useInStorage--;
        return current;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < this.size(); i++) {
            final E current = this.get(i);
            if (current == null && o == null) return i;
            if (current != null && o != null && current.equals(o)) return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = this.size() - 1; i >= 0; i--) {
            final E current = this.get(i);
            if (current == null && o == null) return i;
            if (current != null && o != null && current.equals(o)) return i;
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return this.listIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int i) {
        return new ListImplIterator(i);
    }

    @Override
    public List<E> subList(int start, int stop) throws IndexOutOfBoundsException {
        if (start < 0 || stop >= this.size() || start > stop) throw new IndexOutOfBoundsException();
        List<E> result = new ListImpl<>();
        for (int i = start; i < stop; i++) {
            result.add(this.get(i));
        }
        return result;
    }

    private class ListImplIterator implements ListIterator<E> {

        private static final int LAST_IS_NOT_SET = -1;
        private int index;
        private int lastIndex = LAST_IS_NOT_SET;

        public ListImplIterator(final int index) {
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return ListImpl.this.size() > this.index;
        }

        @Override
        public E next() throws NoSuchElementException{
            if (!hasNext()) throw new NoSuchElementException();
            this.lastIndex = this.index++; // or lastIndex = nextIndex(); index++;
            return ListImpl.this.storage[lastIndex];
        }

        @Override
        public boolean hasPrevious() {
            return this.index > 0;
        }

        @Override
        public E previous() throws NoSuchElementException {
            if (!this.hasPrevious()) throw new NoSuchElementException();
            this.lastIndex = --this.index;
            return ListImpl.this.storage[this.lastIndex];
        }

        @Override
        public int nextIndex() {
            return this.index;
        }

        @Override
        public int previousIndex() {
            return this.index - 1;
        }

        @Override
        public void remove() throws IllegalStateException {
            if (this.lastIndex == LAST_IS_NOT_SET) throw new IllegalStateException();
            ListImpl.this.remove(this.lastIndex);
            this.index--;
            this.lastIndex = LAST_IS_NOT_SET;
        }

        @Override
        public void set(E e) throws IllegalStateException{
            if (this.lastIndex == LAST_IS_NOT_SET) throw new IllegalStateException();
            ListImpl.this.set(this.lastIndex, e);
        }

        @Override
        public void add(E e) {
            ListImpl.this.add(this.index++, e);
            this.lastIndex = LAST_IS_NOT_SET;
        }
    }
}
