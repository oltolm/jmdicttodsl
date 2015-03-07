/*
 * Copyright (C) 2011-2014 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import java.io.StringWriter;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
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
        StlDslConverter converter = new StlDslConverter(group, writer, "eng");
        converter.accept(xmlEntry);
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
        StlEdictConverter converter = new StlEdictConverter(group, writer, "eng");
        converter.accept(xmlEntry);
        String edictString = "天気運 [てんきうん] /(n) weather conditions/\n";
        assertEquals(edictString, writer.getBuffer().toString());
    }

    private STGroup createGroup(String name) {
        URL resource = getClass().getResource("/" + name);
        return new STGroupFile(resource, "UTF-8", '<', '>');
    }
}
