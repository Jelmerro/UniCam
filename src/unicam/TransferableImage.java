package unicam;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Class to convert BufferedImages to TransferableImages, so they can be copied
 *
 * @author Jelmerro
 */
public class TransferableImage implements Transferable {

    private final BufferedImage image;

    /**
     * Creates a TransferableImage
     *
     * @param image BufferedImage
     */
    public TransferableImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * Returns the DataFlavor
     *
     * @return dataFlavor DataFlavor
     */
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.imageFlavor};
    }

    /**
     * Checks if the specified flavor is supported
     *
     * @param flavor DataFlavor
     * @return isSupported boolean
     */
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor == DataFlavor.imageFlavor;
    }

    /**
     * Getter for the data of the TransferableImage
     *
     * @param flavor DataFlavor
     * @return image BufferedImage
     * @throws UnsupportedFlavorException
     * @throws IOException
     */
    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(flavor)) {
            return image;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}
