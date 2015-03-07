/*
 * Copyright (C) 2011-2014 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import java.io.StringReader;
import java.util.function.Consumer;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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

        Consumer<XmlEntry> consumer = (XmlEntry arg) -> {
            assertEquals(xmlEntry, arg);
        };
        StringReader reader = new StringReader(entry);
        StaxReader instance = new StaxReader(reader, lang, consumer);
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

        Consumer<XmlEntry> consumer = (XmlEntry arg) -> {
            assertEquals(xmlEntry, arg);
        };
        StringReader reader = new StringReader(entry);
        StaxReader instance = new StaxReader(reader, lang, consumer);
        instance.doit();
    }

}
