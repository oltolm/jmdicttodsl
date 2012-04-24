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
@XStreamAlias("DslEntry")
class DslEntry {
    @XStreamImplicit(itemFieldName="index")
    public List<String> index =  new ArrayList<String>();
    @XStreamImplicit
    public List<Entry> entry = new ArrayList<Entry>();
    @XStreamImplicit
    public List<Sense> sense = new ArrayList<Sense>();
}
