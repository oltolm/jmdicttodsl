/*
 * Copyright (C) 2011-2014 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jmdicttodsl;

import java.io.Reader;
import java.util.Iterator;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author Oleg Tolmatcev
 */
class StaxReader implements Iterator<XmlEntry>, AutoCloseable {

    /** The value of the xml:lang attribute. */
    private final String lang;
    private XMLStreamReader xmlReader;

    StaxReader(Reader reader, String lang) {
        this.lang = lang;
        try {
            xmlReader = XMLInputFactory.newInstance().createXMLStreamReader(reader);
        } catch (XMLStreamException | FactoryConfigurationError e) {
            throw Sneak.sneakyThrow(e);
        }
    }
    
    @Override
    public boolean hasNext() {
        try {
            return xmlReader.hasNext();
        } catch (XMLStreamException e) {
            throw Sneak.sneakyThrow(e);
        }
    }

    @Override
    public XmlEntry next() {
        XmlEntry entry = new XmlEntry();
        Kanji kanji = new Kanji();
        Reading reading = new Reading();
        Sense sense = new Sense();
        try {
            while (xmlReader.hasNext()) {
                switch (xmlReader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    switch (xmlReader.getLocalName()) {
                    case "entry":
//                            entry = new XmlEntry();
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
                    case "entry":
//                      if (!entry.sense.isEmpty())
                        return entry;
                    }
                    break;

                default:
                    break;
                }
            }
            return entry; // unreachable
        } catch (XMLStreamException e) {
            throw Sneak.sneakyThrow(e);
        }
    }

    @Override
    public void close() throws Exception {
        xmlReader.close();
    }
}
