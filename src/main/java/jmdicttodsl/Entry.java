/*
 * Copyright (C) 2011-2014 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
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
@XStreamAlias("entry")
class Entry {

    public String kana;
    public String kanji;
    @XStreamImplicit(itemFieldName="info")
    public final List<String> info = new ArrayList<>();
    @XStreamImplicit
    public final List<Sense> senses = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Entry other = (Entry) obj;
        if (this.info != other.info && (this.info == null || !this.info.equals(other.info)))
            return false;
        if ((this.kanji == null) ? (other.kanji != null) : !this.kanji.equals(other.kanji))
            return false;
        if ((this.kana == null) ? (other.kana != null) : !this.kana.equals(other.kana))
            return false;
        if (this.senses != other.senses && (this.senses == null || !this.senses.equals(other.senses)))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public String toString() {
        return "Entry{" + "kana=" + kana + ", kanji=" + kanji + ", info=" + info + ", senses=" + senses + '}';
    }
}
