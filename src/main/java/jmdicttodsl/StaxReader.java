/*
 * Copyright (C) 2011-2014 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import java.io.Reader;
import java.util.function.Consumer;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author Oleg Tolmatcev
 */
class StaxReader {

    private final Consumer<XmlEntry> consumer;
    private final Reader reader;
    /** The value of the xml:lang attribute. */
    private final String lang;

    StaxReader(Reader reader, String lang, Consumer<XmlEntry> consumer) {
        this.consumer = consumer;
        this.reader = reader;
        this.lang = lang;
    }

    void doit() throws Exception {
        XmlEntry entry = null;
        Kanji kanji = null;
        Reading reading = null;
        Sense sense = null;

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader xmlReader = factory.createXMLStreamReader(reader);
        while (xmlReader.hasNext()) {
            switch (xmlReader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    switch (xmlReader.getLocalName()) {
                        case "entry":
                            entry = new XmlEntry();
                            break;
                        case "k_ele":
                            kanji = new Kanji();
                            break;
                        case "r_ele":
                            reading = new Reading();
                            break;
                        case "sense":
                            sense = new Sense();
                            break;
                        case "gloss":
                            String glossLang = xmlReader.getAttributeValue(null, "lang");
                            if (glossLang == null)
                                glossLang = "eng";
                            if (glossLang.equals(lang))
                                sense.gloss.add(xmlReader.getElementText());
                            break;
                        case "lsource":
                            LSource lsource = new LSource();
                            lsource.lang = xmlReader.getAttributeValue(null, "lang");
                            if (lsource.lang == null)
                                lsource.lang = "eng";
                            lsource.text = xmlReader.getElementText();
                            sense.lsource.add(lsource);
                            break;
                        case "keb":
                            kanji.keb = xmlReader.getElementText();
                            break;
                        case "ke_inf":
                            kanji.ke_inf.add(xmlReader.getElementText());
                            break;
                        case "reb":
                            reading.reb = xmlReader.getElementText();
                            break;
                        case "re_inf":
                            reading.re_inf.add(xmlReader.getElementText());
                            break;
                        case "re_restr":
                            reading.re_restr.add(xmlReader.getElementText());
                            break;
                        case "pos":
                            sense.pos.add(xmlReader.getElementText());
                            break;
                        case "field":
                            sense.field.add(xmlReader.getElementText());
                            break;
                        case "misc":
                            sense.misc.add(xmlReader.getElementText());
                            break;
                        case "xref":
                            sense.xref.add(xmlReader.getElementText());
                            break;
                        case "stagk":
                            sense.stagk.add(xmlReader.getElementText());
                            break;
                        case "stagr":
                            sense.stagr.add(xmlReader.getElementText());
                            break;
                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    switch (xmlReader.getLocalName()) {
                        case "entry":
                            if (!entry.sense.isEmpty())
                                consumer.accept(entry);
                            break;
                        case "k_ele":
                            entry.k_ele.add(kanji);
                            break;
                        case "r_ele":
                            entry.r_ele.add(reading);
                            break;
                        case "sense":
                            if (!sense.gloss.isEmpty())
                                entry.sense.add(sense);
                            break;
                    }
                    break;

                default:
                    break;
            }
        }
        xmlReader.close();
    }
}
