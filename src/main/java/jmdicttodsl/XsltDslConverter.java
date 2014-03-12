/*
 * Copyright (C) 2011-2013 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.TraxSource;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author Oleg Tolmatcev
 */
class XsltDslConverter implements Converter, Procedure<XmlEntry> {
    private final XStream xstream;
    private final Transformer transformer;
    private final Writer writer;
    private final DslProcessor processor = new DslProcessor();
    private final String lang;

    public XsltDslConverter(StreamSource template, Writer writer, String lang) throws TransformerConfigurationException {
        this.lang = lang;
        this.writer = writer;
        xstream = new XStream(new DomDriver());
        xstream.autodetectAnnotations(true);
        xstream.alias("entry", DslEntry.class);
        xstream.alias("word", Entry.class);
//        xstream.alias("sense", Sense.class);
//        xstream.alias("lsource", LSource.class);
//        xstream.aliasField("lsources", Sense.class, "lsource");
        xstream.omitField(Entry.class, "senses");
        xstream.omitField(Sense.class, "stagk");
        xstream.omitField(Sense.class, "stagr");

        TransformerFactory factory = TransformerFactory.newInstance();
        transformer = factory.newTransformer(template);
    }

    @Override
    public void doit(XmlEntry entry) throws TransformerException {
        List<DslEntry> entries = processor.process(entry);
        StreamResult result = new StreamResult(writer);
        for (DslEntry e : entries) {
            TraxSource in = new TraxSource(e, xstream);
            transformer.transform(in, result);
        }
    }

    @Override
    public void finish() throws IOException {
    }

    @Override
    public void writeHeader() throws IOException {
        String lng;
        switch (lang) {
            case "German":
                lng = "De";
                break;
            case "French":
                lng = "Fr";
                break;
            case "Russian":
                lng = "Ru";
                break;
            default:
                lng = "En";
                break;
        }
        writer.append("#NAME	\"JMdict (Ja-" + lng + ")\"\r\n");
        writer.append("#INDEX_LANGUAGE	\"Japanese\"\r\n");
        writer.append("#CONTENTS_LANGUAGE	\"" + lang + "\"\r\n\r\n");
    }

    @Override
    public void apply(XmlEntry arg) throws Exception {
        doit(arg);
    }
}
