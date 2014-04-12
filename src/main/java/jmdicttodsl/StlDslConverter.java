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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

/**
 *
 * @author Oleg Tolmatcev
 */
class StlDslConverter implements Converter, Consumer<XmlEntry> {

    private final Appendable appender;
    private final STGroup group;
    private final DslProcessor processor = new DslProcessor();
    private final String lang;

    public StlDslConverter(STGroup group, Appendable appender, String lang) {
        this.appender = appender;
        this.lang = lang;
        this.group = group;
    }

    private void doit(XmlEntry xmlEntry) throws IOException {
        List<DslEntry> dslEntries = processor.process(xmlEntry);
        for (DslEntry dslEntry : dslEntries) {
            List<String> senses = processSenses(dslEntry);
            ST st = group.getInstanceOf("senses");
            st.add("index", dslEntry.index);
            st.add("entries", dslEntry.entry);
            st.add("senses", senses);
            st.add("count", senses.size() > 1);
            appender.append(st.render());
            appender.append("\r\n\r\n");
        }
    }

    private List<String> processSenses(DslEntry dslEntry) {
        List<String> names = Arrays.asList("pos", "field", "misc",
                "dial", "lsource", "gloss", "xref");
        List<String> senses = new ArrayList<>();
        for (Sense sense : dslEntry.sense) {
            // STL evaluates an attribute to false only if it is "null" or
            // "false".
            for (LSource lsource : sense.lsource) {
                if (lsource.text != null && lsource.text.isEmpty())
                    lsource.text = null;
            }
            List<String> attributes = new ArrayList<>();
            for (String name : names) {
                ST st = group.getInstanceOf(name);
                st.add("sense", sense);
                String result = st.render();
                if (!result.isEmpty())
                    attributes.add(result);
            }

            if (!attributes.isEmpty()) {
                ST st = group.getInstanceOf("join");
                st.add("list", attributes);
                senses.add(st.render());
            }
        }
        return senses;
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
        appender.append("#NAME	\"JMdict (Ja-" + lng + ")\"\r\n");
        appender.append("#INDEX_LANGUAGE	\"Japanese\"\r\n");
        appender.append("#CONTENTS_LANGUAGE	\"" + lang + "\"\r\n\r\n");
    }

    @Override
    public void accept(XmlEntry arg) {
        try {
            doit(arg);
        } catch (IOException ex) {
            throw Sneak.sneakyThrow(ex);
        }
    }
}
