/*
 * Copyright (C) 2011-2013 Oleg Tolmatcev <oleg_tolmatcev@yahoo.de>
 */
package jmdicttodsl;

/**
 *
 * @author Oleg Tolmatcev
 */
public class JmdictTest {

    public JmdictTest() {
    }

    protected XmlEntry createXmlEntryForWeatherConditions() {
        XmlEntry xmlEntry = new XmlEntry();
        Kanji kanji = new Kanji();
        kanji.keb = "天気運";
        xmlEntry.k_ele.add(kanji);
        Reading reading = new Reading();
        reading.reb = "てんきうん";
        xmlEntry.r_ele.add(reading);

        Sense sense = new Sense();
        sense.pos.add("noun (common) (futsuumeishi)");
        sense.gloss.add("weather conditions");
        xmlEntry.sense.add(sense);
        return xmlEntry;
    }

    protected XmlEntry createXmlEntryForWeatherConditionsGerman() {
        XmlEntry xmlEntry = new XmlEntry();
        Kanji kanji = new Kanji();
        kanji.keb = "天気運";
        xmlEntry.k_ele.add(kanji);
        Reading reading = new Reading();
        reading.reb = "てんきうん";
        xmlEntry.r_ele.add(reading);

        Sense sense = new Sense();
        sense.pos.add("noun (common) (futsuumeishi)");
        sense.gloss.add("(n) Wetterkarte");
        xmlEntry.sense.add(sense);
        return xmlEntry;
    }

}
