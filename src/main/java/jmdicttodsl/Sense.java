/*
 * Copyright (C) 2011-2013 Oleg Tolmatcev <oleg_tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Oleg Tolmatcev
 */
@XStreamAlias("sense")
class Sense {
    @XStreamImplicit(itemFieldName="pos")
    public List<String> pos = new ArrayList<>();
    @XStreamImplicit(itemFieldName="field")
    public List<String> field = new ArrayList<>();
    @XStreamImplicit(itemFieldName="misc")
    public List<String> misc = new ArrayList<>();
    @XStreamImplicit(itemFieldName="dial")
    public List<String> dial = new ArrayList<>();
    @XStreamImplicit
    public List<LSource> lsource = new ArrayList<>();
    @XStreamImplicit(itemFieldName="gloss")
    public List<String> gloss = new ArrayList<>();
    @XStreamImplicit(itemFieldName="xref")
    public List<String> xref = new ArrayList<>();
    @XStreamImplicit(itemFieldName="stagk")
    public List<String> stagk = new ArrayList<>();
    @XStreamImplicit(itemFieldName="stagr")
    public List<String> stagr = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Sense other = (Sense) obj;
        if (this.stagk != other.stagk && (this.stagk == null || !this.stagk.equals(other.stagk)))
            return false;
        if (this.stagr != other.stagr && (this.stagr == null || !this.stagr.equals(other.stagr)))
            return false;
        if (this.pos != other.pos && (this.pos == null || !this.pos.equals(other.pos)))
            return false;
        if (this.xref != other.xref && (this.xref == null || !this.xref.equals(other.xref)))
            return false;
        if (this.field != other.field && (this.field == null || !this.field.equals(other.field)))
            return false;
        if (this.misc != other.misc && (this.misc == null || !this.misc.equals(other.misc)))
            return false;
        if (this.lsource != other.lsource && (this.lsource == null || !this.lsource.equals(other.lsource)))
            return false;
        if (this.dial != other.dial && (this.dial == null || !this.dial.equals(other.dial)))
            return false;
        if (this.gloss != other.gloss && (this.gloss == null || !this.gloss.equals(other.gloss)))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public String toString() {
        return "Sense{" + "pos=" + pos + ", field=" + field + ", misc=" + misc + ", dial=" + dial + ", lsource=" + lsource + ", gloss=" + gloss + ", xref=" + xref + ", stagk=" + stagk + ", stagr=" + stagr + '}';
    }
}
