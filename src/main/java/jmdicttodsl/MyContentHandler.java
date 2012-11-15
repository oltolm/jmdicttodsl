/*
 * Copyright (C) 2011-2012 Oleg Tolmatcev <oleg_tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import java.io.File;
import java.io.Writer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Oleg Tolmatcev
 */
class MyContentHandler extends DefaultHandler {

    private StringBuilder builder;
    private String lang;
    private String glossLang;
    private File file;
    private Writer writer;
    private Converter converter;

    private XmlEntry entry;
    private Kanji kanji;
    private Reading reading;
    private LSource lsource;
    private Sense sense;
    private String format;

    @Override
    public void error(SAXParseException e) throws SAXException {
        throw e;
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        throw e;
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        throw e;
    }

    public MyContentHandler(File file, Writer writer, String lang, String template, String format) {
        this.format = format;
        if (template.equals("XSLT")) {
            if (format.equals("DSL"))
                this.converter = new XsltDslConverter();
            else if ("EDICT".equals(format))
                this.converter = new XsltEdictConverter();
        }
        else if (template.equals("StringTemplate"))
            this.converter = new StlDslConverter();
        this.writer = writer;
        this.entry = null;
        this.builder = null;
        this.glossLang = "";
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

    @Override
    public void startDocument() throws SAXException {
        try {
            converter.init(file, writer, lang);
            converter.writeHeader();
        } catch (Exception ex) {
            throw new SAXException(ex);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            converter.finish();
        } catch (Exception ex) {
            throw new SAXException(ex);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        builder.append(ch, start, length);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        builder = new StringBuilder();
        switch (qName) {
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
                glossLang = attributes.getValue("xml:lang");
                break;
            case "lsource":
                lsource = new LSource();
                lsource.lang = attributes.getValue("xml:lang");
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("entry"))
            try {
            converter.doit(entry);
        } catch (Exception ex) {
            throw new SAXException(ex);
        }
        else if (qName.equals("keb"))
            kanji.keb = builder.toString();
        else if (qName.equals("ke_inf"))
            kanji.ke_inf.add(builder.toString());
        else if (qName.equals("reb"))
            reading.reb = builder.toString();
        else if (qName.equals("re_inf"))
            reading.re_inf.add(builder.toString());
        else if (qName.equals("re_restr"))
            reading.re_restr.add(builder.toString());
        else if (qName.equals("pos"))
            sense.pos.add(builder.toString());
        else if (qName.equals("field"))
            sense.field.add(builder.toString());
        else if (qName.equals("misc"))
            sense.misc.add(builder.toString());
        else if (qName.equals("xref"))
            sense.xref.add(builder.toString());
        else if (qName.equals("gloss"))
        {
            if (glossLang.equals(lang))
                  sense.gloss.add(builder.toString());
        }
        else if (qName.equals("stagk"))
            sense.stagk.add(builder.toString());
        else if (qName.equals("stagr"))
            sense.stagr.add(builder.toString());
        else if (qName.equals("k_ele"))
            entry.k_ele.add(kanji);
        else if (qName.equals("r_ele"))
            entry.r_ele.add(reading);
        else if (qName.equals("lsource"))
        {
            lsource.text = builder.toString();
            sense.lsource.add(lsource);
        }
        else if (qName.equals("sense"))
            entry.sense.add(sense);
    }

}
