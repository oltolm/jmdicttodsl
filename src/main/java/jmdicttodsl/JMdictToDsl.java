/*
 * Copyright (C) 2011-2014 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import static java.lang.String.format;
import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.swing.*;
import javax.xml.transform.TransformerConfigurationException;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Oleg Tolmatcev
 */
public class JMdictToDsl extends javax.swing.JFrame {
    private static final Logger LOGGER = Logger.getLogger(JMdictToDsl.class.getName());

    private Date start;
    private volatile boolean isBusy = false;

    boolean isBusy () {
        return isBusy;
    }

    class Task extends SwingWorker<Void, Void> {

        private final File inFile;
        private final String language;
        private final String format;
        private File outFile;

        public Task(File file, String lang, String format) {
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
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-16LE"));
                Reader reader = new BufferedReader(new InputStreamReader(createInputStream(inFile), "UTF-8"))) {
                writer.write('\uFEFF');
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
                LOGGER.log(Level.SEVERE, null, ex);
            }
            return null;
        }

        @Override
        protected void done() {
            isBusy = false;
            progressBar.setIndeterminate(false);
            Date end = new Date();
            textArea.append(format("Created %s in %s ms.%n", outFile.getPath(), end.getTime() - start.getTime()));
            JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
            scrollBar.setValue(scrollBar.getMaximum());
        }

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

    private InputStream createInputStream(File inFile) throws IOException, UnsupportedEncodingException {
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
        STGroupFile group;
        String fileName = file.getParent() + File.separator + name;
        if (new File(fileName).exists()) {
            group = new STGroupFile(fileName);
        } else  {
            URL resource = getClass().getResource("/" + name);
            group = new STGroupFile(resource, "UTF-8", '<', '>');
        }
        return group;
    }

    /**
     * Creates new form JMdictToDsl
     */
    public JMdictToDsl() {
        initComponents();
        setVisible(true);
        panel.setTransferHandler(new MyTransferhandler(this));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        label = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        langComboBox = new javax.swing.JComboBox();
        languageLabel = new javax.swing.JLabel();
        scrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        outputComboBox = new javax.swing.JComboBox();
        outputLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JMDict to DSL");

        label.setText("Drop the file \"JMDict\" or \"JMDict.gz\" here.");

        langComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "English", "French", "German", "Russian" }));

        languageLabel.setText("Language:");

        textArea.setColumns(20);
        textArea.setEditable(false);
        textArea.setRows(5);
        scrollPane.setViewportView(textArea);

        outputComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DSL", "EDICT" }));

        outputLabel.setText("Output:");

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                    .addComponent(label, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelLayout.createSequentialGroup()
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(languageLabel)
                            .addComponent(outputLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(langComboBox, 0, 97, Short.MAX_VALUE)
                            .addComponent(outputComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(languageLabel)
                    .addComponent(langComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(outputComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(outputLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.setProperty("entityExpansionLimit", "1000000");
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new JMdictToDsl();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                    UnsupportedLookAndFeelException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    void doit(List<File> files) {
        isBusy = true;
        progressBar.setIndeterminate(true);
        String lang = (String) langComboBox.getSelectedItem();
        String format = (String) outputComboBox.getSelectedItem();
        textArea.append(format("Starting conversion to %s.\n", lang));
        Task task = new Task(files.get(0), lang, format);
        task.execute();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel label;
    private javax.swing.JComboBox langComboBox;
    private javax.swing.JLabel languageLabel;
    private javax.swing.JComboBox outputComboBox;
    private javax.swing.JLabel outputLabel;
    private javax.swing.JPanel panel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextArea textArea;
    // End of variables declaration//GEN-END:variables
}
