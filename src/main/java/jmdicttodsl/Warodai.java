package jmdicttodsl;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Oleg Tolmatcev
 */
public class Warodai {

    /**
     * @param args the command line arguments
     */
    public static void main (String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        String infile = null;
        String outfile = null;
        if (args.length == 2) {
            infile = args[0];
            outfile = args[1];
        }
        else {
            System.out.println("Usage: warodaiToDsl <warodai.txt> <warodai.dsl>");
            System.exit(1);
        }

        StringBuilder outputText = new StringBuilder();
        outputText.append("#NAME\t\"БЯРС (Jp-Ru)\"\n#INDEX_LANGUAGE\t\"Japanese\"\n#CONTENTS_LANGUAGE\t\"Russian\"\n\n");
        String inputText = readAll(infile);
        String abbrevsText = readAll("warodai_abrv.dsl");
        Pattern abbrevs = parseAbbrevs(abbrevsText);
        outputText.append(warodaiToDsl(inputText, abbrevs));
        writeAll(outfile, outputText.toString());
    }

    private static String warodaiToDsl (String text, Pattern abbrevs) {
        String[] entries = text.split("\n\n");
        StringBuilder retval = new StringBuilder();
        for (int i = 0; i < entries.length; ++i) {
            String entry = entries[i];
            String dsl = warodaiEntryToDsl(entry, abbrevs);
            retval.append(dsl);
            retval.append("\n\n");
        }
        return retval.toString();
    }

    private static Pattern parseAbbrevs (String text) {
        List<String> abbrevs = new ArrayList<>();
        String[] lines = text.split("\\n");

        for (int i = 0; i < lines.length; ++i) {
            String line = lines[i];
            Pattern p = Pattern.compile("^#|^\\t");
            Matcher m = p.matcher(line);
            if (m.lookingAt())
                continue; // ignore #, \t
            line = line.trim();
            if (line.isEmpty())
                continue;
            // an abbreviation reg. exp. should only match on a word boundary if
            // it ends in a letter
            if (line.endsWith("."))
                abbrevs.add(line);
            else abbrevs.add(line + "\\b");
        }

        Collections.sort(abbrevs, new Comparator<String>() {

            @Override
            public int compare (String s1, String s2) {
                return s2.length() - s1.length();
            }
        });
        String regex = "\\b(?:" + join(abbrevs, "|").replaceAll("\\.", "\\\\.") + ")";
        Pattern p = Pattern.compile(regex);
        return p;
    }

    private static String join (List<String> list, String sep) {
        StringBuilder retval = new StringBuilder();
        for (int i = 0; i < list.size(); ++i) {
            retval.append(list.get(i));
            if (i < list.size() - 1)
                retval.append(sep);
        }
        return retval.toString();
    }

    private static String warodaiEntryToDsl (String entry, Pattern abbrevs) {
        String[] lines = entry.split("\n");
        String header = firstLineToDsl(lines[0]);
        StringBuilder retval = new StringBuilder();
        StringBuilder trn = new StringBuilder();

        if (header.isEmpty())
            return "";

        for (int i = 1; i < lines.length; ++i) {
            String line = lines[i].trim();
            String dsl = lineToDsl(line, abbrevs);
            trn.append(dsl);
            if (i < lines.length - 1)
                trn.append("\n\t");
        }

        retval.append(header).append("\n\t").append("[trn]").append(trn).append("[/trn]");
        return retval.toString();
    }

    private static String firstLineToDsl (String line) {
        StringBuilder header = new StringBuilder();
        Pattern pattern = Pattern.compile("^(.+?)\\s*(?:\\u3010(.+?)\\u3011)?\\((.*?)\\)\\s*\\u3014(.+?)\\u3015");
        Matcher m = pattern.matcher(line);

        if (!m.lookingAt())
            return "";

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

        String l = "[m1][c maroon]" + kana.replaceFirst("I+", "[sup]$0[/sup]") + "[/c]";
        if (kanji != null)
            l += "[c navy]\u3010" + kanji.replaceFirst("I+", "[sup]$0[/sup]") + "\u3011[/c]";
        l += "[c red](" + cyr + ")[/c][/m]";
        header.append("\t");
        header.append(l);
        return header.toString();
    }

    private static String lineToDsl (String line, Pattern abbrevs) {
        StringBuilder retval = new StringBuilder();
        String line1 = line.replaceAll("<\\/?[ai].*?>", "");
        Pattern p = Pattern.compile("^\\u2022");
        // note
        if (p.matcher(line1).lookingAt())
            retval.append("[m2][c brown]").append(lineToDsl1(line, abbrevs)).append("[/c][/m]");
        // normal
        else {
            // '['? (number / cyrillic character)
            p = Pattern.compile("^\\[?[\\d\\u0400-\\u04FF]");
            Matcher m = p.matcher(line1);
            if (m.lookingAt())
                retval.append("[m2]").append(lineToDsl1(line, abbrevs)).append("[/m]");
            else
                retval.append("[m4][ex][*]").append(lineToDsl1(line, abbrevs)).append("[/*][/ex][/m]");
        }
        // example
        return retval.toString();
    }

    private static String lineToDsl1 (String line, Pattern p) {
        line = line.replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]");
        line = p.matcher(line).replaceAll("[p]$0[/p]");
        line = line.replaceAll("<i>", "[i]").replaceAll("<\\/i>", "[/i]").replaceAll("<a href=\".*?\">(.*?)<\\/a>", "[ref]$1[/ref]");
        return line;
    }

    private static String readAll (String fileName) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        BufferedReader br = null;
        try {
            StringBuilder retval = new StringBuilder();
            FileInputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-16");
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null)
                retval.append(line).append("\n");
            return retval.toString();
        } finally {
            if (br != null)
                br.close();
        }
    }

    private static void writeAll (String fileName, String string) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        BufferedWriter bw = null;
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
            bw = new BufferedWriter(osw);
            bw.append(string);
        } finally {
            if (bw != null)
                bw.close();
        }
    }
}
