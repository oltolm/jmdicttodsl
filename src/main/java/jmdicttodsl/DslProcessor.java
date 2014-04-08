/*
 * Copyright (C) 2011-2014 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author Oleg Tolmatcev
 */
class DslProcessor {
    public List<DslEntry> process(XmlEntry entry) {
        List<Sense> senses = new ArrayList<>();
        Map<String, Kanji> ktable = entry.k_ele.stream()
                .collect(toMap(k_ele -> k_ele.keb, identity()));
        Map<String, Reading> rtable = entry.r_ele.stream()
                .collect(toMap(r_ele -> r_ele.reb, identity()));

        fillTables(entry, ktable, rtable, senses);

        Map<Entry, List<Sense>> map = crossProduct(entry, ktable, rtable, senses);

        List<DslEntry> dslEntries = mergeEntries(map);
        createIndex(dslEntries);
        return dslEntries;
    }

    private void fillTables(XmlEntry entry, Map<String, Kanji> ktable, Map<String, Reading> rtable, List<Sense> senses) {
        for (Sense sense : entry.sense) {
            if (!sense.stagk.isEmpty()) {
                for (String k : sense.stagk) {
                    ktable.get(k).sense.add(sense);
                }
            } else if (!sense.stagr.isEmpty()) {
                for (String r : sense.stagr) {
                    rtable.get(r).sense.add(sense);
                }
            } else {
                senses.add(sense);
            }
        }
    }

    private Map<Entry, List<Sense>> crossProduct(XmlEntry entry,
            Map<String, Kanji> ktable,
            Map<String, Reading> rtable,
            List<Sense> senses) {
        Map<Entry, List<Sense>> map = new HashMap<>();
        // cross product of kanji and readings
        for (Reading r : rtable.values()) {
            if (!r.re_restr.isEmpty()) {
                for (String k : r.re_restr) {
                    putEntry(ktable.get(k), r, senses, map);
                }
            } else if (!entry.k_ele.isEmpty()) {
                for (Kanji k_ele : entry.k_ele) {
                    putEntry(ktable.get(k_ele.keb), r, senses, map);
                }
            } else {
                Entry newEntry = new Entry();
                newEntry.kana = r.reb;
                newEntry.info.addAll(r.re_inf);
                List<Sense> allSenses = new ArrayList<>();
                allSenses.addAll(r.sense);
                allSenses.addAll(senses);
                map.put(newEntry, allSenses);
            }
        }
        return map;
    }

    private void createIndex(List<DslEntry> entries) {
        for (DslEntry dslEntry : entries) {
            for (Entry entry : dslEntry.entry) {
                dslEntry.index.add(entry.kana);
                dslEntry.index.add(entry.kanji);
            }
        }
    }

    private void putEntry(Kanji k1, Reading r, List<Sense> senses,
            Map<Entry, List<Sense>> map) {
        Entry entry = new Entry();
        entry.info.addAll(r.re_inf);
        entry.info.addAll(k1.ke_inf);
        entry.kanji = k1.keb;
        entry.kana = r.reb;
        List<Sense> allSenses = new ArrayList<>();
        allSenses.addAll(r.sense);
        allSenses.addAll(k1.sense);
        allSenses.addAll(senses);
        map.put(entry, allSenses);
    }

    private List<DslEntry> mergeEntries(Map<Entry, List<Sense>> map) {
        Map<List<Sense>, List<Entry>> map1 = map.keySet().stream()
                .collect(groupingBy(entry -> map.get(entry)));
        List<DslEntry> result = new ArrayList<>();
        for (List<Entry> es : map1.values()) {
            DslEntry dslEntry = new DslEntry();
            dslEntry.entry.addAll(es);
            dslEntry.sense.addAll(map.get(es.get(0)));
            result.add(dslEntry);
        }
        return result;
    }

}
