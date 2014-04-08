/*
 * Copyright (C) 2011-2014 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Oleg Tolmatcev
 */
class DslEntry {
    public final Set<String> index =  new HashSet<>();
    public final List<Entry> entry = new ArrayList<>();
    public final List<Sense> sense = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.index);
        hash = 73 * hash + Objects.hashCode(this.entry);
        hash = 73 * hash + Objects.hashCode(this.sense);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final DslEntry other = (DslEntry) obj;
        if (!Objects.equals(this.index, other.index))
            return false;
        if (!Objects.equals(this.entry, other.entry))
            return false;
        if (!Objects.equals(this.sense, other.sense))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DslEntry{" + "index=" + index + ", entry=" + entry + ", sense=" + sense + '}';
    }
}
