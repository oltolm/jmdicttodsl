/*
 * Copyright (C) 2011-2014 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 *
 * @author Oleg Tolmatcev
 */
@XStreamAlias("lsource")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"text"})
public class LSource {

    @XStreamAsAttribute
    public String lang;
    public String text;

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final LSource other = (LSource) obj;
        if ((this.lang == null) ? (other.lang != null) : !this.lang.equals(other.lang))
            return false;
        if ((this.text == null) ? (other.text != null) : !this.text.equals(other.text))
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
        return "LSource{" + "lang=" + lang + ", text=" + text + '}';
    }
}
