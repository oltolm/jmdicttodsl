/*
 * Copyright (C) 2011-2013 Oleg Tolmatcev <oleg_tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import java.io.StringReader;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Oleg Tolmatcev
 */
public class StaxReaderTest extends JmdictTest {

    public StaxReaderTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of doit method, of class StaxReader.
     */
    @Test
    public void testDoit() throws Exception {
        String lang = "eng";
        String entry = "<entry>\n" +
"<ent_seq>1438700</ent_seq>\n" +
"<k_ele>\n" +
"<keb>天気運</keb>\n" +
"</k_ele>\n" +
"<r_ele>\n" +
"<reb>てんきうん</reb>\n" +
"</r_ele>\n" +
"<sense>\n" +
"<pos>noun (common) (futsuumeishi)</pos>\n" +
"<gloss>weather conditions</gloss>\n" +
"<gloss xml:lang=\"ger\">(n) Wetterkarte</gloss>\n" +
"</sense>\n" +
"</entry>";

        final XmlEntry xmlEntry = createXmlEntryForWeatherConditions();

        Procedure<XmlEntry> proc = new Procedure<XmlEntry>() {

            @Override
            public void apply(XmlEntry arg) throws Exception {
                assertEquals(xmlEntry, arg);
            }
        };
        StringReader reader = new StringReader(entry);
        StaxReader instance = new StaxReader(reader, lang, proc);
        instance.doit();
    }

    @Test
    public void testDoitGerman() throws Exception {
        String lang = "ger";
        String entry = "<entry>\n" +
"<ent_seq>1438700</ent_seq>\n" +
"<k_ele>\n" +
"<keb>天気運</keb>\n" +
"</k_ele>\n" +
"<r_ele>\n" +
"<reb>てんきうん</reb>\n" +
"</r_ele>\n" +
"<sense>\n" +
"<pos>noun (common) (futsuumeishi)</pos>\n" +
"<gloss>weather conditions</gloss>\n" +
"<gloss xml:lang=\"ger\">(n) Wetterkarte</gloss>\n" +
"</sense>\n" +
"</entry>";

        final XmlEntry xmlEntry = createXmlEntryForWeatherConditionsGerman();

        Procedure<XmlEntry> proc = new Procedure<XmlEntry>() {

            @Override
            public void apply(XmlEntry arg) throws Exception {
                assertEquals(xmlEntry, arg);
            }
        };
        StringReader reader = new StringReader(entry);
        StaxReader instance = new StaxReader(reader, lang, proc);
        instance.doit();
    }

}