/*
 * Copyright (C) 2011-2012 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Oleg Tolmatcev
 */
@XStreamAlias("k_ele")
public class Kanji {
    public String keb;
    @XStreamImplicit(itemFieldName="ke_inf")
    public final List<String> ke_inf = new ArrayList<>();
    @XStreamOmitField
    public final List<Sense> sense = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.keb);
        hash = 17 * hash + Objects.hashCode(this.ke_inf);
        hash = 17 * hash + Objects.hashCode(this.sense);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Kanji other = (Kanji) obj;
        if (!Objects.equals(this.keb, other.keb))
            return false;
        if (!Objects.equals(this.ke_inf, other.ke_inf))
            return false;
        if (!Objects.equals(this.sense, other.sense))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Kanji{" + "keb=" + keb + ", ke_inf=" + ke_inf + ", sense=" + sense + '}';
    }
}
