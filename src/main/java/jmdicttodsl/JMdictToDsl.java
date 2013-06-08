/*
 * Copyright (C) 2011-2013 Oleg Tolmatcev <oleg_tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamSource;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Oleg Tolmatcev
 */
public class JMdictToDsl extends javax.swing.JFrame {

    private Date start;
    private boolean isBusy = false;

    boolean isBusy () {
        return isBusy;
    }

    class Task extends SwingWorker<Void, Void> {

        private File file;
        private String language;
        private String templateLanguage;
        private String format;

        public Task(File file, String lang, String template, String format) {
            this.file = file;
            this.language = lang;
            this.templateLanguage = template;
            this.format = format;
        }

        @Override
        protected Void doInBackground() throws Exception {
            Writer writer = null;

            textArea.append(String.format("Starting conversion to %1$s using %2$s.\n",
                    language, templateLanguage));
            start = new Date();

            try {
                String fileName;
                if (file.getPath().endsWith(".xml"))
                    fileName = file.getPath().substring(0, file.getPath().length() - 4)
                            + "." + format.toLowerCase();
                else
                    fileName = file.getPath() + "." + format.toLowerCase();
                File outFile = new File(fileName);
                if (outFile.exists())
                    outFile.delete();
                writer = new OutputStreamWriter(new FileOutputStream(outFile), "UTF-16");
//                writer = new FileWriter(outFile, true);

                String lang = createLang(language);
                Converter converter = createConverter(file, writer, lang);
                if (false) {
                    MyContentHandler handler = new MyContentHandler(lang, converter);
                    SAXParserFactory factory = SAXParserFactory.newInstance();
                    SAXParser saxParser = factory.newSAXParser();
                    saxParser.parse(file, handler);
                } else {
                    InputStream inputStream = new FileInputStream(file);
                    converter.writeHeader();
                    StaxReader staxReader = new StaxReader(inputStream, lang, (Procedure<XmlEntry>) converter);
                    staxReader.doit();
                    converter.finish();
                }
            } catch (Exception ex) {
                Logger.getLogger(JMdictToDsl.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (writer != null)
                    try {
                        writer.close();
                    } catch (IOException ex) {
                        Logger.getLogger(JMdictToDsl.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            return null;
        }

        @Override
        protected void done() {
            isBusy = false;
            progressBar.setIndeterminate(false);
            Date end = new Date();
            textArea.append(String.format("Done in %1$sms.\n", end.getTime() - start.getTime()));
            JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
            scrollBar.setValue(scrollBar.getMaximum());
        }

        private Converter createConverter(File file, Writer writer, String lang) throws TransformerConfigurationException {
            switch (templateLanguage) {
                case "XSLT":
                    switch (format) {
                        case "DSL": {
                            StreamSource template = getXsltTemplate(file, "dsl.xsl");
                            return new XsltDslConverter(template, writer, lang);
                        }
                        case "EDICT": {
                            StreamSource template = getXsltTemplate(file, "edict.xsl");
                            return new XsltEdictConverter(template, writer, lang);
                        }
                    }
                case "StringTemplate":
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
                default: return null;
            }
        }

        private String createLang(String language) {
            switch (language) {
                case "German":
                    return "ger";
                case "French":
                    return "fre";
                case "Russian":
                    return "rus";
                default: {
                    assert language.equals("English");
                    return "eng";
                }
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

        private StreamSource getXsltTemplate(File file, String name) {
            String fileName = file.getParent() + File.separator + name;
            if (new File(fileName).exists())
                return new StreamSource(new File(fileName));
            else
                return new StreamSource(getClass().getResource("/" + name).toString());
        }
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
        templateLabel = new javax.swing.JLabel();
        templateComboBox = new javax.swing.JComboBox();
        scrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        outputComboBox = new javax.swing.JComboBox();
        outputLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JMDict to DSL");

        label.setText("Drop the file \"JMDict\" here.");

        langComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "English", "French", "German", "Russian" }));

        languageLabel.setText("Language:");

        templateLabel.setText("Template:");

        templateComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "StringTemplate", "XSLT" }));

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
                            .addComponent(templateLabel)
                            .addComponent(outputLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(templateComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(langComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(templateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(templateLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(outputComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(outputLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
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
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    new JMdictToDsl();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                        UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(JMdictToDsl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    void doit(List<File> files) {
        isBusy = true;
        progressBar.setIndeterminate(true);
        String template = (String) templateComboBox.getSelectedItem();
        String lang = (String) langComboBox.getSelectedItem();
        String format = (String) outputComboBox.getSelectedItem();
        Task task = new Task(files.get(0), lang, template, format);
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
    private javax.swing.JComboBox templateComboBox;
    private javax.swing.JLabel templateLabel;
    private javax.swing.JTextArea textArea;
    // End of variables declaration//GEN-END:variables
}
