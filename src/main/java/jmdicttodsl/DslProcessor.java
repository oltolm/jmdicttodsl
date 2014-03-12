/*
 * Copyright (C) 2011-2014 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import java.util.*;

/**
 *
 * @author Oleg Tolmatcev
 */
class DslProcessor {
    public List<DslEntry> process(XmlEntry entry) {
        List<Entry> entries = new ArrayList<>();
        Map<String, Kanji> ktable = new HashMap<>();
        Map<String, Reading> rtable = new HashMap<>();
        List<Sense> senses = new ArrayList<>();

        for (Kanji k_ele : entry.k_ele)
            ktable.put(k_ele.keb, k_ele);

        for (Reading r_ele : entry.r_ele)
            rtable.put(r_ele.reb, r_ele);

        for (Sense sense : entry.sense)
            if (!sense.stagk.isEmpty())
                for (String k : sense.stagk)
                    ktable.get(k).sense.add(sense);
            else if (!sense.stagr.isEmpty())
                for (String r : sense.stagr)
                    rtable.get(r).sense.add(sense);
            else
                senses.add(sense);
        // cross product of kanji and readings
        for (Reading r : rtable.values())
            if (!r.re_restr.isEmpty())
                for (String k : r.re_restr)
                    addEntry(ktable, k, r, senses, entries);
            else if (!entry.k_ele.isEmpty())
                for (Kanji k_ele : entry.k_ele)
                    addEntry(ktable, k_ele.keb, r, senses, entries);
            else {
                Entry newEntry = new Entry();
                newEntry.kana = r.reb;
                newEntry.info.clear();
                newEntry.info.addAll(r.re_inf);
                newEntry.senses.addAll(r.sense);
                newEntry.senses.addAll(senses);
                entries.add(newEntry);
            }

        List<DslEntry> dslEntries = mergeEntries(entries);
        dslEntries = filterEntries(dslEntries);
        return dslEntries;
    }

    private List<DslEntry> filterEntries(List<DslEntry> entries) {
        Set<String> index;
        for (DslEntry dslEntry : entries) {
            index = new HashSet<>();
            for (Entry entry : dslEntry.entry) {
                index.add(entry.kana);
                index.add(entry.kanji);
            }
            dslEntry.index.addAll(index);
        }
        return entries;
    }

    private void addEntry(Map<String, Kanji> ktable, String k, Reading r, List<Sense> senses, List<Entry> entries) {
        Kanji k1 = ktable.get(k);
        Entry entry = new Entry();
        entry.info.addAll(r.re_inf);
        entry.info.addAll(k1.ke_inf);
        entry.kanji = k1.keb;
        entry.kana = r.reb;
        entry.senses.addAll(r.sense);
        entry.senses.addAll(k1.sense);
        entry.senses.addAll(senses);
        entries.add(entry);
    }

    private List<DslEntry> mergeEntries(List<Entry> entries) {
        List<DslEntry> retval = new ArrayList<>();
        for (int i = 0; i < entries.size(); ++i) {
            Entry entry = entries.get(i);
            if (entry == null)
                continue;
            entries.set(i, null);
            DslEntry dslEntry = new DslEntry();
            dslEntry.entry.add(entry);
            dslEntry.sense.addAll(entry.senses);
            entry.senses.clear();

            for (int j = i + 1; j < entries.size(); ++j) {
                Entry entry1 = entries.get(j);
                if (entry1 == null)
                    continue;
                if (dslEntry.sense.equals(entry1.senses)) {
                    entry1.senses.clear();
                    dslEntry.entry.add(entry1);
                    entries.set(j, null);
                }
            }
            retval.add(dslEntry);
        }
        return retval;
    }

}
