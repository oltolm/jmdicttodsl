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
@XStreamAlias("r_ele")
class Reading {
    public String reb;
    @XStreamImplicit(itemFieldName="re_inf")
    public List<String> re_inf = new ArrayList<>();
    @XStreamImplicit(itemFieldName="re_restr")
    public List<String> re_restr = new ArrayList<>();
    @XStreamOmitField
    public List<Sense> sense = new ArrayList<>();
}
