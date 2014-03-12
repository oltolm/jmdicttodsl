/*
 * Copyright (C) 2011-2014 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
package jmdicttodsl;

/**
 *
 * @author Oleg Tolmatcev
 */
interface Converter {
    public void doit(XmlEntry entry) throws Exception;
    public void finish() throws Exception;
    public void writeHeader() throws Exception;
}
