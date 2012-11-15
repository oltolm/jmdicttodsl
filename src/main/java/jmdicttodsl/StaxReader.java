/*
 * Copyright (C) 2011-2012 Oleg Tolmatcev <oleg_tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import java.io.File;
import java.io.FileInputStream;
import java.io.Writer;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author Oleg Tolmatcev
 */
class StaxReader {

    private Converter converter;
    private Writer writer;
    private String lang;
    private File file;
    private String format;

    public StaxReader(File file, Writer writer, String lang, String template, String format) {
        this.format = format;
        if (template.equals("XSLT")) {
            if (format.equals("DSL"))
                this.converter = new XsltDslConverter();
            else if (format.equals("EDICT"))
                this.converter = new XsltEdictConverter();
        } else if (template.equals("StringTemplate")) {
            if (format.equals("DSL"))
                this.converter = new StlDslConverter();
            else if (format.equals("EDICT"))
                this.converter = new StlEdictConverter();
        }
        this.writer = writer;
        this.file = file;
        this.lang = "eng";
        switch (lang) {
            case "German":
                this.lang = "ger";
                break;
            case "French":
                this.lang = "fre";
                break;
            case "Russian":
                this.lang = "rus";
                break;
        }
    }

    void doit() throws Exception {
        String qName;
        XmlEntry entry = null;
        Kanji kanji = null;
        Reading reading = null;
        Sense sense = null;

        converter.init(file, writer, lang);
        converter.writeHeader();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader parser = factory.createXMLStreamReader(new FileInputStream(file));
        while (parser.hasNext()) {
            switch (parser.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    qName = parser.getLocalName();
                    if (qName.equals("entry"))
                        entry = new XmlEntry();
                    else if (qName.equals("k_ele"))
                        kanji = new Kanji();
                    else if (qName.equals("r_ele"))
                        reading = new Reading();
                    else if (qName.equals("sense"))
                        sense = new Sense();
                    else if (qName.equals("gloss")) {
                        String glossLang = parser.getAttributeValue(null, "lang");
                        if (glossLang == null)
                            glossLang = "eng";
                        if (glossLang.equals(lang))
                            sense.gloss.add(parser.getElementText());
                    } else if (qName.equals("lsource")) {
                        LSource lsource = new LSource();
                        lsource.lang = parser.getAttributeValue(null, "lang");
                        if (lsource.lang == null)
                            lsource.lang = "eng";
                        lsource.text = parser.getElementText();
                        sense.lsource.add(lsource);
                    } else if (qName.equals("keb"))
                        kanji.keb = parser.getElementText();
                    else if (qName.equals("ke_inf"))
                        kanji.ke_inf.add(parser.getElementText());
                    else if (qName.equals("reb"))
                        reading.reb = parser.getElementText();
                    else if (qName.equals("re_inf"))
                        reading.re_inf.add(parser.getElementText());
                    else if (qName.equals("re_restr"))
                        reading.re_restr.add(parser.getElementText());
                    else if (qName.equals("pos"))
                        sense.pos.add(parser.getElementText());
                    else if (qName.equals("field"))
                        sense.field.add(parser.getElementText());
                    else if (qName.equals("misc"))
                        sense.misc.add(parser.getElementText());
                    else if (qName.equals("xref"))
                        sense.xref.add(parser.getElementText());
                    else if (qName.equals("stagk"))
                        sense.stagk.add(parser.getElementText());
                    else if (qName.equals("stagr"))
                        sense.stagr.add(parser.getElementText());
                    break;

                case XMLStreamConstants.CHARACTERS:
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    qName = parser.getLocalName();
                    if (qName.equals("entry")) {
                        if (!entry.sense.isEmpty())
                            converter.doit(entry);
                    } else if (qName.equals("k_ele"))
                        entry.k_ele.add(kanji);
                    else if (qName.equals("r_ele"))
                        entry.r_ele.add(reading);
                    else if (qName.equals("sense")) {
                        if (!sense.gloss.isEmpty())
                            entry.sense.add(sense);
                    }
                    break;

                default:
                    break;
            }
        }
        parser.close();
        converter.finish();
    }
}
