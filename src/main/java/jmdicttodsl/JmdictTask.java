/*
 * Copyright (C) 2014 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import static java.lang.String.format;
import java.net.URL;
import static java.nio.charset.StandardCharsets.UTF_16;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.Date;
import static java.util.logging.Level.SEVERE;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.swing.SwingWorker;
import javax.xml.transform.TransformerConfigurationException;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
class JmdictTask extends SwingWorker<Void, Void> {
    private final static Logger LOGGER = Logger.getLogger(JmdictTask.class.getName());
    private final File inFile;
    private final String language;
    private final String format;
    private File outFile;
    private final JmdictToDsl frame;
    private Date start;

    public JmdictTask(File file, String lang, String format, JmdictToDsl frame) {
        this.frame = frame;
        this.inFile = file;
        this.language = lang;
        this.format = format;
    }

    @Override
    protected Void doInBackground() throws Exception {
        start = new Date();
        String fileName = createFileName(inFile, format);
        outFile = new File(inFile.getParentFile(), fileName);
        if (outFile.exists())
            outFile.delete();
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),
                UTF_16));
             Reader reader = new BufferedReader(new InputStreamReader(createInputStream(inFile),
                     UTF_8))) {
            String lang = createLang(language);
            Converter converter = createConverter(inFile, writer, lang, format);
            //                    MyContentHandler handler = new MyContentHandler(lang, converter);
            //                    SAXParserFactory factory = SAXParserFactory.newInstance();
            //                    SAXParser saxParser = factory.newSAXParser();
            //                    saxParser.parse(inFile, handler);
            converter.writeHeader();
            StaxReader staxReader = new StaxReader(reader, lang, converter);
            staxReader.doit();
        } catch (Exception ex) {
            LOGGER.log(SEVERE, null, ex);
            System.exit(1);
        }
        return null;
    }

    @Override
    protected void done() {
        frame.finishConversion(outFile, start);
    }


    private String createFileName(File inFile, String format) {
        String fileName;
        if (inFile.getName().endsWith(".gz")) {
            fileName = inFile.getName().substring(0, inFile.getName().length() - 3);
        } else {
            fileName = inFile.getName();
        }
        return format("%s.%s", fileName, format.toLowerCase());
    }

    private InputStream createInputStream(File inFile) throws IOException {
        if (inFile.getName().endsWith(".gz")) {
            return new GZIPInputStream(new FileInputStream(inFile));
        } else {
            return new FileInputStream(inFile);
        }
    }

    private Converter createConverter(File file, Writer writer, String lang, String format)
            throws TransformerConfigurationException {
        switch (format) {
            case "DSL": {
                STGroupFile group = createSTGroup(file, "dsl.stg");
                return new StlDslConverter(group, writer, lang);
            }
            case "EDICT": {
                STGroupFile group = createSTGroup(file, "edict.stg");
                return new StlEdictConverter(group, writer, lang);
            }
        }
        throw new IllegalArgumentException(format("unknown format %s", format));
    }

    private String createLang(String language) {
        switch (language) {
            case "German":
                return "ger";
            case "French":
                return "fre";
            case "Russian":
                return "rus";
            case "English": {
                return "eng";
            }
            default:
                throw new IllegalArgumentException(format("unknown language %s", language));
        }
    }

    private STGroupFile createSTGroup(File file, String name) {
        String fileName = file.getParent() + File.separator + name;
        if (new File(fileName).exists()) {
            return new STGroupFile(fileName);
        } else  {
            URL resource = getClass().getResource("/" + name);
            return new STGroupFile(resource, "UTF-8", '<', '>');
        }
    }

}
