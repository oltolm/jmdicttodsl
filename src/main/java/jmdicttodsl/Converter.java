/*
 * Copyright (C) 2011-2014 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import java.util.function.Consumer;

/**
 *
 * @author Oleg Tolmatcev
 */
interface Converter extends Consumer<XmlEntry> {
    public void finish() throws Exception;
    public void writeHeader() throws Exception;
}
