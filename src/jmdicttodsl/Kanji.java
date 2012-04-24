/*
 * Copyright (C) 2011-2012 Oleg Tolmatcev <oleg_tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Oleg Tolmatcev
 */
@XStreamAlias("k_ele")
class Kanji {
    public String keb;
    @XStreamImplicit(itemFieldName="ke_inf")
    public List<String> ke_inf = new ArrayList<String>();
    @XStreamOmitField
    public List<Sense> sense = new ArrayList<Sense>();
}
