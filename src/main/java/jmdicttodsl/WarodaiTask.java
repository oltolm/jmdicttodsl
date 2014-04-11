package jmdicttodsl;

import java.io.File;
import javax.swing.SwingWorker;

/**
 *
 * @author Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
class WarodaiTask extends SwingWorker<Void, Void>{
    private final JmdictToDsl frame;
    private final File file;

    WarodaiTask(JmdictToDsl frame, File file) {
        this.frame = frame;
        this.file = file;
    }

    @Override
    protected Void doInBackground() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void done() {
        frame.setIsBusy(false);
    }

}
