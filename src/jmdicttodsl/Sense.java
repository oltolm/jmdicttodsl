/*
 * Copyright (C) 2011-2012 Oleg Tolmatcev <oleg_tolmatcev@yahoo.de>
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
    public List<String> pos = new ArrayList<String>();
    @XStreamImplicit(itemFieldName="field")
    public List<String> field = new ArrayList<String>();
    @XStreamImplicit(itemFieldName="misc")
    public List<String> misc = new ArrayList<String>();
    @XStreamImplicit(itemFieldName="dial")
    public List<String> dial = new ArrayList<String>();
    @XStreamImplicit
    public List<LSource> lsource = new ArrayList<LSource>();
    @XStreamImplicit(itemFieldName="gloss")
    public List<String> gloss = new ArrayList<String>();
    @XStreamImplicit(itemFieldName="xref")
    public List<String> xref = new ArrayList<String>();
    @XStreamImplicit(itemFieldName="stagk")
    public List<String> stagk = new ArrayList<String>();
    @XStreamImplicit(itemFieldName="stagr")
    public List<String> stagr = new ArrayList<String>();

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
}
