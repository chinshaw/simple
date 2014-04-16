package com.simple.domain.model.ui.dashboard;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.simple.api.orchestrator.IMetricPlot;

public class PlotIOUtils {

	
	@Inject
	@Named("com.simple.plots.dir")
	private static String plotsDir;
	
    /**
     * This calls the {@link #writeMetricPlot(IMetricPlot, String, String)} with
     * the output from the {@link
     * ServerProperties.getInteractivePlotsDirectory()} as the default plots
     * directory. This plot should not be expected to persist for long and will
     * probably be deleted within the day. You should not be concerned about
     * calling delete on this plot as it will be cleaned up automatically.
     * 
     * @param metric
     * @param imageType
     * @return
     * @throws IOException
     */
    public static final String writeInteractiveMetricPlot(IMetricPlot metric, String imageType) throws IOException {
        String fileName = writeMetricPlot(metric, imageType, plotsDir);
        return fileName;
    }
    

    /**
     * This calls the {@link #writeMetricPlot(IMetricPlot, String, String)} with
     * the output from the ServerProperties.getPlotsDirectory() as the default
     * plots directory. The plot will be saved indefinitely. The metric plot can
     * be either deleted by calling deletePlot but should not be manually
     * deleted.
     * 
     * @param metric
     * @param imageType
     * @return
     * @throws IOException
     */
    public static final String writeMetricPlot(IMetricPlot metric, String imageType) throws PlotIOException {
        return writeMetricPlot(metric, imageType, plotsDir);
    }

    /**
     * Writes a metrics image data out to the disk in a place that the web
     * server can get to. This is much more efficient that saving the files to
     * the db because the web server can handle all the io and it supports
     * caching of the static image files. The files will be located in a public
     * accessible directory that the web server has access to. You can change
     * this directory my modifying the
     * {@link ServerProperties.getPlotsDirectory}
     * 
     * 
     * @param metric
     *            The MetricPlotEntity that contains the image data to save.
     * @return Returns the name of the generated file. This file is based on a
     *         uuid.
     * @throws IOException
     *             If there is a problem writing the file.
     */
    public static final String writeMetricPlot(IMetricPlot metric, String imageType, String directory) throws PlotIOException {

        String fileName = UUID.randomUUID().toString() + "." + imageType;
        String file = directory + "/" + fileName;

        writeImage(new File(file), imageType, metric.getImageData());

        return fileName;
    }

    /**
     * Used to write an acutally byte[] array of image data out to disk.
     * 
     * @param imageOutFile
     * @param imageType
     * @param imageData
     * @throws IOException
     */
    public static final void writeImage(File imageOutFile, String imageType, byte[] imageData) throws PlotIOException {
        if (imageOutFile == null)
            throw new PlotIOException("File cannot be null when calling writeImage");
        if (imageType == null)
            throw new PlotIOException("Image type is required to write a metric plot");
        if (imageData.length <= 0)
            throw new PlotIOException("Image should not be null when trying to save image to filesystem");

        try {
            InputStream in = new ByteArrayInputStream(imageData);
            BufferedImage image = null;

            image = ImageIO.read(in);
            in.close();

            ImageIO.write(image, imageType, imageOutFile);
        } catch (IOException ioe) {
            throw new PlotIOException("An error occurred while try got write plot image out to file " + imageOutFile.getAbsolutePath(), ioe);
        }

    }

    /**
     * Use this to delete a plot by the file name.
     * 
     * @param plotName
     */
    public static boolean deletePlot(String plotName) {
        String file = plotsDir + "/" + plotName;
        return new File(file).delete();
    }
}
