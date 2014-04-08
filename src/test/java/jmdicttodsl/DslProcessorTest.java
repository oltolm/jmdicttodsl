/*
 * Copyright (C) 2011-2014 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import static java.util.Arrays.asList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
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
        dslEntry.index.addAll(asList("天気運", "てんきうん"));
        Entry entry = new Entry();
        entry.kana = "てんきうん";
        entry.kanji = "天気運";
        dslEntry.entry.add(entry);
        dslEntry.sense.add(xmlEntry.sense.get(0));

        DslProcessor instance = new DslProcessor();
        List<DslEntry> expResult = asList(dslEntry);
        List<DslEntry> result = instance.process(xmlEntry);
        assertEquals(expResult, result);
    }

    @Test
    public void testMerge() {
        XmlEntry xmlEntry = new XmlEntry();
        Kanji k = new Kanji();
        k.keb = "伯林";
        xmlEntry.k_ele.add(k);
        Reading r = new Reading();
        r.reb = "べるりん";
        xmlEntry.r_ele.add(r);
        Reading r1 = new Reading();
        r1.reb = "ベルリン";
        xmlEntry.r_ele.add(r1);
        Sense s = new Sense();
        s.gloss.add("Berlin");
        s.pos.add("n");
        xmlEntry.sense.add(s);

        DslProcessor instance = new DslProcessor();
        DslEntry dslEntry = new DslEntry();
        dslEntry.index.addAll(asList("伯林", "ベルリン", "べるりん"));
        Entry e1 = new Entry();
        e1.kana = "ベルリン";
        e1.kanji = "伯林";
        Entry e2 = new Entry();
        e2.kana = "べるりん";
        e2.kanji = "伯林";
        dslEntry.entry.addAll(asList(e1, e2));
        Sense s2 = new Sense();
        s2.pos.add("n");
        s2.gloss.add("Berlin");
        dslEntry.sense.add(s2);
        List<DslEntry> expResult = asList(dslEntry);
        List<DslEntry> result = instance.process(xmlEntry);
        assertEquals(expResult, result);
    }
}
