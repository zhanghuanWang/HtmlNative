package com.mozz.htmlnative;

import android.util.Log;

import com.mozz.htmlnative.css.StyleEntry;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Yang Tao, 17/4/25.
 */
public final class InheritStyleStack implements Iterable<StyleEntry> {

    private int level = 0;
    private int index = 0;
    private int[] cssCount;
    private Object[] values;
    private String[] params;

    private static final int MAX_DEPTH = 20;

    public InheritStyleStack() {
        cssCount = new int[MAX_DEPTH];
        values = new Object[MAX_DEPTH * 4];
        params = new String[MAX_DEPTH * 4];
        reset();
    }

    public void push() {
        level++;
    }

    public void newStyle(String param, Object value) {
        values[index] = value;
        params[index] = param;
        index++;
        cssCount[level]++;
    }

    public void pop() {
        this.index -= cssCount[level];
        cssCount[level] = 0;
        level--;
    }

    public void reset() {
        level = -1;
        index = 0;
        Arrays.fill(values, null);
        Arrays.fill(params, null);
    }

    public int size() {
        return index;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < index; i++) {
            sb.append(params[i]).append("=").append(values[i]);
            sb.append(",");
        }

        return sb.toString();
    }

    @Override
    public Iterator<StyleEntry> iterator() {
        return new InheritStyleIterator();
    }

    private class InheritStyleIterator implements Iterator<StyleEntry> {
        private int index = 0;
        private int size = InheritStyleStack.this.index;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public StyleEntry next() {
            if (index > size) {
                return null;
            }

            StyleEntry entry = new StyleEntry(InheritStyleStack.this.params[index],
                    InheritStyleStack.this.values[index]);
            index++;
            return entry;
        }
    }
}