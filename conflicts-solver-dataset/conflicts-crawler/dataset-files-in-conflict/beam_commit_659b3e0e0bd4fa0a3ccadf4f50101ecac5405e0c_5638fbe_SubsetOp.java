package org.esa.beam.framework.gpf.operators.common;

import com.bc.ceres.core.ProgressMonitor;
import org.esa.beam.framework.dataio.ProductReader;
import org.esa.beam.framework.dataio.ProductSubsetBuilder;
import org.esa.beam.framework.dataio.ProductSubsetDef;
import org.esa.beam.framework.datamodel.Band;
import org.esa.beam.framework.datamodel.Product;
import org.esa.beam.framework.datamodel.ProductData;
import org.esa.beam.framework.gpf.Operator;
import org.esa.beam.framework.gpf.OperatorException;
import org.esa.beam.framework.gpf.OperatorSpi;
import org.esa.beam.framework.gpf.Tile;
import org.esa.beam.framework.gpf.annotations.OperatorMetadata;
import org.esa.beam.framework.gpf.annotations.Parameter;
import org.esa.beam.framework.gpf.annotations.SourceProduct;
import org.esa.beam.framework.gpf.annotations.TargetProduct;

import java.awt.Rectangle;
import java.io.IOException;

@OperatorMetadata(alias = "Subset",
                  description = "Create a spatial and/or spectral subset of the source product.")
public class SubsetOp extends Operator {

    @SourceProduct
    private Product sourceProduct;
    @TargetProduct
    private Product targetProduct;
    @Parameter
    private Rectangle region;
    @Parameter(defaultValue = "1")
    private int subSamplingX;
    @Parameter(defaultValue = "1")
    private int subSamplingY;
    @Parameter
    private String[] tiePointGridNames;
    @Parameter
    private String[] bandNames;
    @Parameter(defaultValue = "false")
    private boolean copyMetadata;

    private ProductReader subsetReader;
    private ProductSubsetDef subsetDef;

    public SubsetOp() {
        super();
        subSamplingX = 1;
        subSamplingY = 1;
    }

    public String[] getTiePointGridNames() {
        return tiePointGridNames != null ? tiePointGridNames.clone() : null;
    }

    public void setTiePointGridNames(String[] tiePointGridNames) {
        this.tiePointGridNames = tiePointGridNames != null ? tiePointGridNames.clone() : null;
    }

    public String[] getBandNames() {
        return bandNames != null ? bandNames.clone() : null;
    }

    public void setBandNames(String[] bandNames) {
        this.bandNames = bandNames != null ? bandNames.clone() : null;
    }

    public void setCopyMetadata(boolean copyMetadata) {
        this.copyMetadata = copyMetadata;
    }

    public Rectangle getRegion() {
        return region != null ? new Rectangle(region) : null;
    }

    public void setRegion(Rectangle region) {
        this.region = region != null ? new Rectangle(region) : null;
    }

    public void setSubSamplingX(int subSamplingX) {
        this.subSamplingX = subSamplingX;
    }

    public void setSubSamplingY(int subSamplingY) {
        this.subSamplingY = subSamplingY;
    }

    @Override
    public void initialize() throws OperatorException {
        subsetReader = new ProductSubsetBuilder();
        subsetDef = new ProductSubsetDef();
        if (tiePointGridNames != null) {
            subsetDef.addNodeNames(tiePointGridNames);
        } else {
            subsetDef.addNodeNames(sourceProduct.getTiePointGridNames());
        }
        if (bandNames != null) {
            subsetDef.addNodeNames(bandNames);
        } else {
            subsetDef.addNodeNames(sourceProduct.getBandNames());
        }
        if (region != null) {
            subsetDef.setRegion(region);
        }
        subsetDef.setSubSampling(subSamplingX, subSamplingY);
        subsetDef.setIgnoreMetadata(!copyMetadata);

        try {
            targetProduct = subsetReader.readProductNodes(sourceProduct, subsetDef);
        } catch (Throwable t) {
            throw new OperatorException(t);
        }
    }

    @Override
    public void computeTile(Band band, Tile targetTile, ProgressMonitor pm) throws OperatorException {
        ProductData destBuffer = targetTile.getRawSamples();
        Rectangle rectangle = targetTile.getRectangle();
        try {
            subsetReader.readBandRasterData(band,
                                            rectangle.x,
                                            rectangle.y,
                                            rectangle.width,
                                            rectangle.height,
                                            destBuffer, pm);
            targetTile.setRawSamples(destBuffer);
        } catch (IOException e) {
            throw new OperatorException(e);
        }
    }


    public static class Spi extends OperatorSpi {
        public Spi() {
            super(SubsetOp.class);
        }
    }
}
