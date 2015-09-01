/*
 * Copyright (C) 2011-2014 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Oleg Tolmatcev
 */
public class ConverterTest extends JmdictTest {

    public ConverterTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testStlDslConverter() throws Exception {
        StringWriter writer = new StringWriter();
        XmlEntry xmlEntry = createXmlEntryForWeatherConditions();
        STGroup group = createGroup("dsl.stg");
        BlockingQueue<XmlEntry> queue = new ArrayBlockingQueue<>(1);
        StlDslConverter converter = new StlDslConverter(group, writer, "eng", queue);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> result = executor.submit(converter);
        queue.put(xmlEntry);
        queue.put(new XmlEntry());
        result.get();
        String dslString = "天気運\r\n" +
"てんきうん\r\n" +
"	[m1][c maroon]てんきうん[/c][c navy]【天気運】[/c][/m]\r\n" +
"	[trn][m2][i]([p]n[/p])[/i] weather conditions.[/m][/trn]\r\n\r\n";
        assertEquals(dslString, writer.getBuffer().toString());
    }

    @Test
    public void testStlEdictConverter() throws Exception {
        StringWriter writer = new StringWriter();
        XmlEntry xmlEntry = createXmlEntryForWeatherConditions();
        STGroup group = createGroup("edict.stg");
        BlockingQueue<XmlEntry> queue = new ArrayBlockingQueue<>(1);
        StlEdictConverter converter = new StlEdictConverter(group, writer, "eng", queue);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> result = executor.submit(converter);
        queue.put(xmlEntry);
        queue.put(new XmlEntry());
        result.get();
        String edictString = "天気運 [てんきうん] /(n) weather conditions/\n";
        assertEquals(edictString, writer.getBuffer().toString());
    }

    private STGroup createGroup(String name) {
        URL resource = getClass().getResource("/" + name);
        return new STGroupFile(resource, "UTF-8", '<', '>');
    }
}
