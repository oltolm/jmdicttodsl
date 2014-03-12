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
@XStreamAlias("r_ele")
public class Reading {
    public String reb;
    @XStreamImplicit(itemFieldName="re_inf")
    public final List<String> re_inf = new ArrayList<>();
    @XStreamImplicit(itemFieldName="re_restr")
    public final List<String> re_restr = new ArrayList<>();
    @XStreamOmitField
    public final List<Sense> sense = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.reb);
        hash = 89 * hash + Objects.hashCode(this.re_inf);
        hash = 89 * hash + Objects.hashCode(this.re_restr);
        hash = 89 * hash + Objects.hashCode(this.sense);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Reading other = (Reading) obj;
        if (!Objects.equals(this.reb, other.reb))
            return false;
        if (!Objects.equals(this.re_inf, other.re_inf))
            return false;
        if (!Objects.equals(this.re_restr, other.re_restr))
            return false;
        if (!Objects.equals(this.sense, other.sense))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Reading{" + "reb=" + reb + ", re_inf=" + re_inf + ", re_restr=" + re_restr + ", sense=" + sense + '}';
    }
}
