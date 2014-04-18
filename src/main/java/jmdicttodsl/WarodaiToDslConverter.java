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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.nio.charset.StandardCharsets.UTF_16;
import static java.nio.file.Files.readAllLines;
import java.nio.file.Paths;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import static java.util.Comparator.comparingInt;
import java.util.List;
import static java.util.logging.Level.FINEST;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Oleg Tolmatcev
 */
class WarodaiToDslConverter {
    private static final Logger LOGGER = Logger.getLogger(WarodaiToDslConverter.class.getName());
    private final File inFile;
    private final File outFile;

    WarodaiToDslConverter(File inFile, File outFile) {
        this.inFile = inFile;
        this.outFile = outFile;
    }

    void convert() throws IOException {
        StringBuilder outputText = new StringBuilder();
        outputText.append("#NAME\t\"БЯРС (Jp-Ru)\"\n#INDEX_LANGUAGE\t\"Japanese\"\n#CONTENTS_LANGUAGE\t\"Russian\"\n\n");
        String inputText = readAll(inFile.getPath());
        String abbrevsFileName = inFile.getParent() + File.separator + "warodai_abrv.dsl";
        List<String> abbrevsText = readAllLines(Paths.get(abbrevsFileName), UTF_16);
        Pattern abbrevs = parseAbbrevs(abbrevsText);
        outputText.append(convert(inputText, abbrevs));
        writeAll(outFile.getPath(), outputText.toString());
    }

    String convert(String text, Pattern abbrevs) throws IOException {
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

    private String convertEntry(String entry, Pattern abbrevs) throws IOException {
        String[] lines = entry.split("\n");
        String header = convertFirstLine(lines[0]);
        StringBuilder retval = new StringBuilder();
        StringBuilder trn = new StringBuilder();

        if (header.isEmpty()) {
            return "";
        }
        for (int i = 1; i < lines.length; ++i) {
            String line = lines[i].trim();
            String dsl = convertLine(line, abbrevs, i);
            trn.append(dsl);
            if (i < lines.length - 1) {
                trn.append("\n\t");
            }
        }
        retval.append(header).append("\n\t").append(format("[trn]%s[/trn]", trn));
        return retval.toString();
    }

    private String convertFirstLine(String line) throws IOException {
        StringBuilder header = new StringBuilder();
        String kanaPat = "(.+?)";
        String kajiPat = "(?:\u3010(.+?)\u3011)?";
        String cyrPat = "\\((.*?)\\)(?: \\[.*?])?";
        String numberPat = "\u3014(.+?)\u3015";
        String pat = (format("^%s ?%s%s%s", kanaPat, kajiPat, cyrPat, numberPat));

        Pattern pattern = Pattern.compile(pat);
        Matcher m = pattern.matcher(line);
        if (!m.lookingAt()) {
            LOGGER.severe(line);
            return "";
        }
        String kana = m.group(1);
        String kanji = m.group(2);
        String cyr = m.group(3);
        LOGGER.log(FINEST, cyr);

        header.append(join("\n", getKana(kana))).append("\n");
        if (kanji != null) {
            header.append(join("\n", getKanji(kanji))).append("\n\t");
        }
        header.append(format("[m1][c maroon]%s[/c]", kana.replaceFirst("I+", "[sup]$0[/sup]")));
        if (kanji != null) {
            header.append(format("[c navy]\u3010%s\u3011[/c]", kanji.replaceFirst("I+", "[sup]$0[/sup]")));
        }
        header.append(format("[c red](%s)[/c][/m]", cyr));
        return header.toString();
    }

    private List<String> getKanji(String kanji) {
        return asList(kanji.split(", ")).stream()
                .flatMap(field -> asList(field.split("\\uFF65")).stream())
                .map(field -> field.replaceFirst("I+", ""))
                .collect(toList());
    }

    private List<String> getKana(String kana) {
        return asList(kana.split(", ")).stream()
                .map(field -> field.replaceFirst("I+", ""))
                .collect(toList());
    }

    private String convertLine(String line, Pattern abbrevs, int i) throws IOException {
        String jPattern = "(?:\\p{IsHiragana}|\\p{IsKatakana}|\\p{IsHan})";
        String cPattern = "\\p{IsCyrillic}";
        String line2 = convertLine2(line, abbrevs);
        Matcher m = Pattern.compile("^\u2022").matcher(line);
        // note
        if (m.lookingAt()) {
            return format("[m2][c brown]%s[/c][/m]", line2);
        } else {
            String pat = format("(?:%s)", join("|", asList("\\d+\\)",
                    "\\(?<i>",
                    "\\d\\.",
                    "[абвгдеж]\\)",
                    "<i>с[мр]\\.",
                    "<i>связ\\.",
                    ".*?\uFF5E.*?",
                    "\\[?\\p{IsCyrillic}",
                    ": \\p{IsCyrillic}")));
            // normal
            m = Pattern.compile("^" + pat).matcher(line);
            if (i == 1 || m.lookingAt()) {
                return format("[m2]%s[/m]", line2);
            } else {
                String prefix = format("(?:%s)?", join("|", asList("8/",
                        "<i>прост\\.</i> ",
                        ": ",
                        "SOS",
                        "ABC/",
                        "5%/",
                        "69",
                        "<a href=\"#1-008-1-55\">",
                        "<a href=\"#1-031-1-25\">",
                        "[\u2026\u300E\u300CABKMNPSX\\[3568]")));
                m = Pattern.compile("^\u25C7?" + prefix + jPattern)
                        .matcher(line);
                if (!m.lookingAt()) {
                    throw new IOException(line);
                }
                return format("[m4][ex][*]%s[/*][/ex][/m]", line2);
            }
        }
    }

    private String convertLine2(String line, Pattern abbrevs) {
        line = line.replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]");
        line = abbrevs.matcher(line).replaceAll("[p]$0[/p]");
        line = line.replaceAll("<i>", "[i]")
                .replaceAll("<\\/i>", "[/i]")
                .replaceAll("<a href=\".*?\">(.*?)<\\/a>", "[ref]$1[/ref]");
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

}
