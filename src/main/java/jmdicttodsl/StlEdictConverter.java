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
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

/**
 *
 * @author Oleg Tolmatcev
 */
class StlEdictConverter implements Converter {

    private final Appendable appender;
    private final STGroup group;
    private final BlockingQueue<XmlEntry> queue;

    public StlEdictConverter(STGroup group, Appendable writer, String lang, BlockingQueue<XmlEntry> queue) {
        this.appender = writer;
        this.group = group;
        this.queue = queue;
    }

    private void doit(XmlEntry xmlEntry) throws IOException {
        for (Reading r_ele : xmlEntry.r_ele) {
            if (!xmlEntry.k_ele.isEmpty()) {
                for (Kanji k_ele : xmlEntry.k_ele) {
                    processEntry(xmlEntry, k_ele, r_ele);
                }
            }
            else {
                processEntry(xmlEntry, new Kanji(), r_ele);
            }
        }
    }

    @Override
    public void finish() throws IOException {
    }

    @Override
    public void writeHeader() throws IOException {
        appender.append("　？？？ /EDICT, EDICT_SUB(P), EDICT2 Japanese-English Electronic Dictionary Files/Copyright Electronic Dictionary Research & Development Group - 2011/Created: " + new Date().toString() + "\n");
    }

    private void processEntry(XmlEntry xmlEntry, Kanji k_ele, Reading r_ele) throws IOException {
        StringBuilder result = new StringBuilder();
        if (r_ele.re_restr.contains(k_ele.keb) || r_ele.re_restr.isEmpty()) {
            List<Sense> senses = new ArrayList<>();
            for (Sense s : xmlEntry.sense) {
                if ((s.stagr.isEmpty() && s.stagk.isEmpty())
                        || s.stagr.contains(r_ele.reb)
                        || s.stagk.contains(k_ele.keb)) {
                    senses.add(s);
                }
            }
            if (!senses.isEmpty()) {
                if (k_ele.keb != null) {
                    result.append(k_ele.keb).append(" [").append(r_ele.reb).append("] /");
                } else {
                    result.append(r_ele.reb).append(" /");
                }

                ST st;
                if (k_ele.keb != null) {
                    st = group.getInstanceOf("infos");
                    st.add("xs", k_ele.ke_inf);
                    result.append(st.render());
                }

                st = group.getInstanceOf("infos");
                st.add("xs", r_ele.re_inf);
                result.append(st.render());

                st = group.getInstanceOf("sense");
                st.add("xs", senses);
                st.add("count", senses.size() > 1);
                result.append(st.render());

                result.append("\n");
                appender.append(result.toString());
            }
        }
    }

    @Override
    public void run() {
        try {
            XmlEntry entry = queue.take();
            while (!entry.sense.isEmpty()) {
                doit(entry);
                entry = queue.take();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            throw Sneak.sneakyThrow(e);
        }
    }
}
