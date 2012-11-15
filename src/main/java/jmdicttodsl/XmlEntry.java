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
@XStreamAlias("entry")
class XmlEntry {

    @XStreamImplicit
    public List<Kanji> k_ele = new ArrayList<>();
    @XStreamImplicit
    public List<Reading> r_ele = new ArrayList<>();
    @XStreamImplicit
    public List<Sense> sense = new ArrayList<>();
}
