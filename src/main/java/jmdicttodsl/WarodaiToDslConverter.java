/*
 * Copyright (C) 2011-2014 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import java.io.*;
import static java.nio.charset.StandardCharsets.UTF_16;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import static java.util.Comparator.comparingInt;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.lang.String.format;
import static java.lang.String.join;

/**
 *
 * @author Oleg Tolmatcev
 */
class WarodaiToDslConverter {
    private final String inFile;
    private final String outFile;

    WarodaiToDslConverter(String infile, String outfile) {
        this.inFile = infile;
        this.outFile = outfile;
    }

    void convert() throws IOException {
        StringBuilder outputText = new StringBuilder();
        outputText.append("#NAME\t\"БЯРС (Jp-Ru)\"\n#INDEX_LANGUAGE\t\"Japanese\"\n#CONTENTS_LANGUAGE\t\"Russian\"\n\n");
        String inputText = readAll(inFile);
        List<String> abbrevsText = readAllLines("warodai_abrv.dsl");
        Pattern abbrevs = parseAbbrevs(abbrevsText);
        outputText.append(convert(inputText, abbrevs));
        writeAll(outFile, outputText.toString());
    }

    String convert(String text, Pattern abbrevs) {
        String[] entries = text.split("\n\n");
        StringBuilder retval = new StringBuilder();
        for (String entry : entries) {
            String dsl = convertEntry(entry, abbrevs);
            retval.append(dsl);
            retval.append("\n\n");
        }
        return retval.toString();
    }

    Pattern parseAbbrevs(List<String> lines) {
        List<String> abbrevs = new ArrayList<>();
        for (String line : lines) {
            Pattern p = Pattern.compile("^#|^\\t");
            Matcher m = p.matcher(line);
            if (m.lookingAt()) {
                continue; // ignore #, \t
            }
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            // an abbreviation reg. exp. should only match on a word boundary if
            // it ends in a letter
            if (line.endsWith(".")) {
                abbrevs.add(line);
            } else {
                abbrevs.add(line + "\\b");
            }
        }
        abbrevs.sort(comparingInt(String::length));
        String regex = format("\\b(?:%s)", join("|", abbrevs).replaceAll("\\.", "\\\\."));
        return Pattern.compile(regex);
    }

    private String convertEntry(String entry, Pattern abbrevs) {
        String[] lines = entry.split("\n");
        String header = convertFirstLine(lines[0]);
        StringBuilder retval = new StringBuilder();
        StringBuilder trn = new StringBuilder();

        if (header.isEmpty()) {
            return "";
        }
        for (int i = 1; i < lines.length; ++i) {
            String line = lines[i].trim();
            String dsl = convertLine(line, abbrevs);
            trn.append(dsl);
            if (i < lines.length - 1) {
                trn.append("\n\t");
            }
        }
        retval.append(header).append("\n\t").append("[trn]").append(trn).append("[/trn]");
        return retval.toString();
    }

    private String convertFirstLine(String line) {
        StringBuilder header = new StringBuilder();
        Pattern pattern = Pattern.compile("^(.+?)\\s*(?:\\u3010(.+?)\\u3011)?\\((.*?)\\)\\s*\\u3014(.+?)\\u3015");
        Matcher m = pattern.matcher(line);

        if (!m.lookingAt()) {
            return "";
        }

        String kana = m.group(1);
        String kanji = m.group(2);
        String cyr = m.group(3);
        String[] kana1 = kana.split(", ");

        for (int i = 0; i < kana1.length; ++i) {
            String kana2 = kana1[i];
            header.append(kana2.replaceFirst("I+", ""));
            header.append("\n");
        }
        if (kanji != null) {
            String[] kanji1 = kanji.split(", ");
            for (int i = 0; i < kanji1.length; ++i) {
                String kanji2 = kanji1[i];
                String[] kanji3 = kanji2.split("\\uFF65");
                for (int j = 0; j < kanji3.length; ++j) {
                    String kanji4 = kanji3[j];
                    header.append(kanji4.replaceFirst("I+", ""));
                    header.append("\n");
                }
            }
        }

        StringBuilder l = new StringBuilder();
        l.append("[m1][c maroon]").append(kana.replaceFirst("I+", "[sup]$0[/sup]"))
                .append("[/c]");
        if (kanji != null) {
            l.append("[c navy]\u3010").append(kanji.replaceFirst("I+", "[sup]$0[/sup]"))
                    .append("\u3011[/c]");
        }
        l.append("[c red](").append(cyr).append(")[/c][/m]");
        header.append("\t");
        header.append(l);
        return header.toString();
    }

    private String convertLine(String line, Pattern abbrevs) {
        StringBuilder retval = new StringBuilder();
        String line1 = line.replaceAll("<\\/?[ai].*?>", "");
        Pattern p = Pattern.compile("^\\u2022");
        // note
        if (p.matcher(line1).lookingAt()) {
            retval.append("[m2][c brown]").append(convertLine2(line, abbrevs)).append("[/c][/m]");
            // normal
        } else {
            // '['? (number / cyrillic character)
            p = Pattern.compile("^\\[?[\\d\\u0400-\\u04FF]");
            Matcher m = p.matcher(line1);
            if (m.lookingAt()) {
                retval.append("[m2]").append(convertLine2(line, abbrevs)).append("[/m]");
            } else {
                retval.append("[m4][ex][*]").append(convertLine2(line, abbrevs)).append("[/*][/ex][/m]");
            }
        }
        // example
        return retval.toString();
    }

    private String convertLine2(String line, Pattern p) {
        line = line.replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]");
        line = p.matcher(line).replaceAll("[p]$0[/p]");
        line = line.replaceAll("<i>", "[i]").replaceAll("<\\/i>", "[/i]").replaceAll("<a href=\".*?\">(.*?)<\\/a>", "[ref]$1[/ref]");
        return line;
    }

    private String readAll(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),
                UTF_16))) {
            StringBuilder retval = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                retval.append(line).append("\n");
            }
            return retval.toString();
        }
    }

    private void writeAll(String fileName, String string) throws IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName),
                UTF_16))) {
            writer.append(string);
        }
    }

    private List<String> readAllLines(String fileName) throws IOException {
        return Files.readAllLines(Paths.get(fileName), UTF_16);
    }
}
