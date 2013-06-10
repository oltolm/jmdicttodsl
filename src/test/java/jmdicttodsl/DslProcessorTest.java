/*
 * Copyright (C) 2011-2013 Oleg Tolmatcev <oleg_tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Oleg
 */
public class DslProcessorTest extends JmdictTest {

    public DslProcessorTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of process method, of class DslProcessor.
     */
    @Test
    public void testProcess() {
        final XmlEntry xmlEntry = createXmlEntryForWeatherConditions();

        DslEntry dslEntry = new DslEntry();
        dslEntry.index = Arrays.asList("天気運", "てんきうん");
        Entry entry = new Entry();
        entry.kana = "てんきうん";
        entry.kanji = "天気運";
        dslEntry.entry.add(entry);
        dslEntry.sense.add(xmlEntry.sense.get(0));

        DslProcessor instance = new DslProcessor();
        List<DslEntry> expResult = Arrays.asList(dslEntry);
        List<DslEntry> result = instance.process(xmlEntry);
        assertEquals(expResult, result);
    }
}