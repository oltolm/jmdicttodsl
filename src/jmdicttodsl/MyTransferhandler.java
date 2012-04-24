/*
 * Copyright (C) 2011-2012 Oleg Tolmatcev <oleg_tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.TransferHandler;

/**
 *
 * @author Oleg Tolmatcev
 */
class MyTransferhandler extends TransferHandler {
    private JMdictToDsl frame;

    MyTransferhandler(JMdictToDsl frame) {
        this.frame = frame;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    @Override
    public boolean importData(TransferSupport support) {
        try {
            Transferable transferable = support.getTransferable();
            Object transferData = transferable.getTransferData(DataFlavor.javaFileListFlavor);
            List<File> files = (List<File>) transferData;
            frame.doit(files);
            return true;
        } catch (UnsupportedFlavorException ex) {
            Logger.getLogger(MyTransferhandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MyTransferhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
