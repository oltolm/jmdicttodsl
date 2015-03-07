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
    private static final Logger logger = Logger.getLogger(WarodaiTask.class.getName());
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
            logger.log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        return null;
    }

    @Override
    protected void done() {
        frame.finishConversion(outFile, start);
    }

}
