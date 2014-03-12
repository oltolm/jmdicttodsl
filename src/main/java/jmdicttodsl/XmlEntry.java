/*
 * Copyright (C) 2011-2013 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Oleg Tolmatcev
 */
@XStreamAlias("entry")
public class XmlEntry {

    @XStreamImplicit
    public final List<Kanji> k_ele = new ArrayList<>();
    @XStreamImplicit
    public final List<Reading> r_ele = new ArrayList<>();
    @XStreamImplicit
    public final List<Sense> sense = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.k_ele);
        hash = 53 * hash + Objects.hashCode(this.r_ele);
        hash = 53 * hash + Objects.hashCode(this.sense);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final XmlEntry other = (XmlEntry) obj;
        if (!Objects.equals(this.k_ele, other.k_ele))
            return false;
        if (!Objects.equals(this.r_ele, other.r_ele))
            return false;
        if (!Objects.equals(this.sense, other.sense))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "XmlEntry{" + "k_ele=" + k_ele + ", r_ele=" + r_ele + ", sense=" + sense + '}';
    }
}
