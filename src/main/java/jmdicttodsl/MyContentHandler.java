/*
 * Copyright (C) 2011-2013 Oleg Tolmatcev <oleg_tolmatcev@yahoo.de>
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
    /** Value of the xml:lang attribute. */
    private String lang;
    private String glossLang;
    private Converter converter;

    private XmlEntry entry;
    private Kanji kanji;
    private Reading reading;
    private LSource lsource;
    private Sense sense;

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

    public MyContentHandler(String lang, Converter converter) {
        this.converter = converter;
        this.entry = null;
        this.builder = null;
        this.glossLang = "";
        this.lang = lang;
    }

    @Override
    public void startDocument() throws SAXException {
        try {
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
        switch (qName) {
            case "entry":
                try {
                    converter.doit(entry);
                } catch (Exception ex) {
                    throw new SAXException(ex);
                }
                break;
            case "keb":
                kanji.keb = builder.toString();
                break;
            case "ke_inf":
                kanji.ke_inf.add(builder.toString());
                break;
            case "reb":
                reading.reb = builder.toString();
                break;
            case "re_inf":
                reading.re_inf.add(builder.toString());
                break;
            case "re_restr":
                reading.re_restr.add(builder.toString());
                break;
            case "pos":
                sense.pos.add(builder.toString());
                break;
            case "field":
                sense.field.add(builder.toString());
                break;
            case "misc":
                sense.misc.add(builder.toString());
                break;
            case "xref":
                sense.xref.add(builder.toString());
                break;
            case "gloss":
                if (glossLang.equals(lang))
                      sense.gloss.add(builder.toString());
                break;
            case "stagk":
                sense.stagk.add(builder.toString());
                break;
            case "stagr":
                sense.stagr.add(builder.toString());
                break;
            case "k_ele":
                entry.k_ele.add(kanji);
                break;
            case "r_ele":
                entry.r_ele.add(reading);
                break;
            case "lsource":
                lsource.text = builder.toString();
                sense.lsource.add(lsource);
                break;
            case "sense":
                entry.sense.add(sense);
                break;
        }
    }

}
