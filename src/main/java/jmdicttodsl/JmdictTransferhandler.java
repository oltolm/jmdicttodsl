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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.TransferHandler;

/**
 *
 * @author Oleg Tolmatcev
 */
@SuppressWarnings("serial")
public class JmdictTransferhandler extends TransferHandler {
    private static final Logger LOGGER = Logger.getLogger(JmdictTransferhandler.class.getName());
    private final Predicate<Void> pred;
    private final Consumer<List<File>> command;

    JmdictTransferhandler(Predicate<Void> pred, Consumer<List<File>> command) {
        this.pred = pred;
        this.command = command;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        return pred.test(null) && support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean importData(TransferSupport support) {
        try {
            Transferable transferable = support.getTransferable();
            Object transferData = transferable.getTransferData(DataFlavor.javaFileListFlavor);
            List<File> files = (List<File>) transferData;
            command.accept(files);
            return true;
        } catch (UnsupportedFlavorException | IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
