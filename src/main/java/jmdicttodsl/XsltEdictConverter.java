/*
 * Copyright (C) 2011-2013 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.TraxSource;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
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
class XsltEdictConverter implements Converter, Procedure<XmlEntry> {

    private Writer writer;
    private XStream xstream;
    private Transformer transformer;

    public XsltEdictConverter(StreamSource template, Writer writer, String lang) throws TransformerConfigurationException {
        this.writer = writer;
//        XmlFriendlyNameCoder coder = new XmlFriendlyNameCoder("dollar", "_");
//        xstream = new XStream(new DomDriver("UTF-8", coder));
        xstream = new XStream(new DomDriver());
        xstream.autodetectAnnotations(true);

        TransformerFactory factory = TransformerFactory.newInstance();
        transformer = factory.newTransformer(template);
    }

    @Override
    public void doit(XmlEntry entry) throws TransformerException {
        StreamResult result = new StreamResult(writer);
        TraxSource in = new TraxSource(entry, xstream);
        transformer.transform(in, result);
    }

    @Override
    public void finish() throws IOException {
    }

    @Override
    public void writeHeader() throws IOException {
        writer.append("　？？？ /EDICT, EDICT_SUB(P), EDICT2 Japanese-English Electronic Dictionary Files/Copyright Electronic Dictionary Research & Development Group - 2011/Created: " + new Date().toString() + "\n");
    }

    @Override
    public void apply(XmlEntry arg) throws Exception {
        doit(arg);
    }
}
