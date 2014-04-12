package jmdicttodsl;

import java.io.File;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
class WarodaiTask extends SwingWorker<Void, Void>{
    private static final Logger LOGGER = Logger.getLogger(WarodaiTask.class.getName());
    private final JmdictToDsl frame;
    private final File file;
    private File outFile;
    private Date start;

    WarodaiTask(JmdictToDsl frame, File file) {
        this.frame = frame;
        this.file = file;
    }

    @Override
    protected Void doInBackground() throws Exception {
        start = new Date();
        outFile = new File(file.getPath() + ".dsl");
        WarodaiToDslConverter converter = new WarodaiToDslConverter(file, outFile);

        try {
            converter.convert();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        return null;
    }

    @Override
    protected void done() {
        frame.finishConversion(outFile, start);
    }

}
