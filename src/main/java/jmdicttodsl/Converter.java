/*
 * Copyright (C) 2011-2012 Oleg Tolmatcev <oleg_tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import java.io.File;
import java.io.Writer;

/**
 *
 * @author Oleg Tolmatcev
 */
interface Converter {
    public void init(File file, Writer writer, String lang) throws Exception;
    public void doit(XmlEntry entry) throws Exception;
    public void finish() throws Exception;
    public void writeHeader() throws Exception;
}
