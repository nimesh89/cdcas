/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package map;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.SLD;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.postgis.PostgisDataStoreFactory;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.BasicPolygonStyle;
import org.geotools.styling.StyleBuilder;
import org.opengis.feature.Property;


/**
 * GeoTools Quickstart demo application. Prompts the user for a shapefile
 * and displays its contents on the screen in a map frame
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/demo/example/src/main/java/org/geotools/demo/Quickstart.java $
 */
class Quickstart {

    /**
     * GeoTools Quickstart demo application. Prompts the user for a shapefile
     * and displays its contents on the screen in a map frame
     */
    public static void main(String[] args) throws Exception {
        // display a data store file chooser dialog for shapefiles
//        File file = JFileDataStoreChooser.showOpenFile("shp", null);
//        if (file == null) {
//            return;
//        }
//
//        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        HashMap params = new HashMap();
        params.put(PostgisDataStoreFactory.DBTYPE.key, "postgis");
        params.put(PostgisDataStoreFactory.HOST.key, "localhost");
        params.put(PostgisDataStoreFactory.PORT.key, 5432);
        params.put(PostgisDataStoreFactory.SCHEMA.key, "public");
        params.put(PostgisDataStoreFactory.DATABASE.key, "postgis");
        params.put(PostgisDataStoreFactory.USER.key, "postgres");
        params.put(PostgisDataStoreFactory.PASSWD.key, "ASDqwe123$");

        DataStore dataStore = DataStoreFinder.getDataStore(params);

//        for(String s: dataStore.getTypeNames())
//        {
//            System.out.println(s);
//        }

        FeatureSource featureSource = dataStore.getFeatureSource("divsec");
        FeatureCollection col = featureSource.getFeatures();
        Property p = col.features().next().getProperty("");

//
//        // Create a map context and add our shapefile to it
        MapContext map = new DefaultMapContext();
        map.setTitle("Quickstart");
        BasicPolygonStyle st = new BasicPolygonStyle();
        map.addLayer(col, st);
saveImage(map, "C:\\Users\\Public\\test\\junk\\asd.jpg");
//        // Now display the map
        //JMapFrame.showMap(map);
    }

    public static void saveImage(MapContext map, String file) {

        GTRenderer renderer = new StreamingRenderer();
        renderer.setContext(map);

        Rectangle imageSize = new Rectangle(600, 600);

        BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gr = image.createGraphics();
        gr.setPaint(Color.WHITE);
        gr.fill(imageSize);  //otherwise black background - which throws of transparency of color
        try {
            StringBuffer arr = new StringBuffer();
            ByteArrayOutputStream bt = new ByteArrayOutputStream();
            ImageOutputStream st = ImageIO.createImageOutputStream(bt);
            renderer.paint(gr, imageSize, map.getAreaOfInterest());
            File fileToSave = new File(file);
            ImageIO.write(image, "jpeg", st);
            System.out.println(bt.size());
        } catch (IOException e) {
        }

    }

//    public static void main(String[] args) throws Exception {
//        File file = JFileDataStoreChooser.showOpenFile("shp", null);
//        if (file == null) {
//            return;
//        }
//
//        Quickstart me = new Quickstart();
//        me.displayShapefile(file);
//    }

    /**
     * Connect to the shapefile and prompt the user to choose a feature
     * attribute to base line and fill colours on. A colour will be
     * generated for each unique value of the chosen attribute.
     */
    private void displayShapefile(File file) throws Exception {
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        FeatureSource featureSource = store.getFeatureSource();

        // Create a map context and add our shapefile to it
        MapContext map = new DefaultMapContext();
        map.setTitle("Style Function Lab");

        /*
         * Prompt the user for the feature attribute used to
         * determine the line and fill colour for each feature
         */
        FeatureType type = featureSource.getSchema();
        StyleBuilder builder = new StyleBuilder();
        
//        PropertyDescriptor geomDesc = type.getGeometryDescriptor();
//        List<String> attributeNames = new ArrayList<String>();
//        for (PropertyDescriptor desc : type.getDescriptors()) {
//            if (!desc.equals(geomDesc)) {
//                attributeNames.add(desc.getName().getLocalPart());
//            }
//        }
//
//        Object selection = JOptionPane.showInputDialog(null,
//                "Unique values of the attribute will be used to create a colour lookup table",
//                "Select feature attribute",
//                JOptionPane.PLAIN_MESSAGE, null,
//                attributeNames.toArray(), null);
//
//        /**
//         * Create the Style and display the shapefile
//         */
//        if (selection != null) {
//            Style style = createStyle(featureSource, (String) selection);
            map.addLayer(featureSource.getFeatures(), null);

            // Now display the map
            JMapFrame.showMap(map);
//        }

    }

    /**
     * Create a rendering style to display features from the given feature source
     * by matching unique values of the specified feature attribute to colours
     *
     * @param featureSource the feature source
     * @return a new Style instance
     */
    private Style createStyle(FeatureSource featureSource, String attributeName)
            throws Exception {

        FilterFactory2 filterFactory = CommonFactoryFinder.getFilterFactory2(null);
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);

        ColorLookupFunction colourFn = new ColorLookupFunction(
                featureSource.getFeatures(), filterFactory.property(attributeName));

        Stroke stroke = styleFactory.createStroke(
                colourFn, // function to choose feature colour
                filterFactory.literal(1.0f), // line width
                filterFactory.literal(1.0f));  // opacity

        Fill fill = styleFactory.createFill(
                colourFn, // function to choose feature colour
                filterFactory.literal(1.0f));  // opacity

        Class<?> geomClass = featureSource.getSchema().getGeometryDescriptor().getType().getBinding();
        Symbolizer sym = null;
        if (Polygon.class.isAssignableFrom(geomClass)
                || MultiPolygon.class.isAssignableFrom(geomClass)) {

            sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        } else if (LineString.class.isAssignableFrom(geomClass)
                || MultiLineString.class.isAssignableFrom(geomClass)) {
            sym = styleFactory.createLineSymbolizer(stroke, null);

        } else {
            Graphic gr = styleFactory.createDefaultGraphic();
            gr.graphicalSymbols().clear();
            Mark mark = styleFactory.getCircleMark();
            mark.setFill(fill);
            mark.setStroke(stroke);
            gr.graphicalSymbols().add(mark);
            gr.setSize(filterFactory.literal(10.0f));
            sym = styleFactory.createPointSymbolizer(gr, null);
        }

        Style style = SLD.wrapSymbolizers(sym);
        return style;
    }

    /**
 * A function to dynamically allocate colours to features. It works with a lookup table
 * where the key is a user-specified feature attribute. Colours are generated using
 * a simple colour ramp algorithm.
 */
static class ColorLookupFunction extends FunctionExpressionImpl {

    private static final float INITIAL_HUE = 0.1f;
    private final FeatureCollection collection;
    Map<Object, Color> lookup;
    private int numColours;
    private float hue;
    private float hueIncr;
    private float saturation = 0.7f;
    private float brightness = 0.7f;

    /**
     * Creates an instance of the function for the given feature collection. Features will
     * be assigned fill colours by matching the value of the specified feature attribute
     * in a lookup table of unique attribute values with associated colours.
     *
     * @param collection the feature collection
     *
     * @param colourAttribute a literal expression that specifies the feature attribute
     *        to use for colour lookup
     */
    public ColorLookupFunction(FeatureCollection collection, Expression colourAttribute) {
        super("UniqueColour");
        this.collection = collection;

        this.params.add(colourAttribute);
        this.fallback = CommonFactoryFinder.getFilterFactory2(null).literal(Color.WHITE);
    }

    @Override
    public int getArgCount() {
        return 1;
    }

    /**
     * Evalute this function for a given feature and return a
     * Color.
     *
     * @param object the feature for which a colour is being requested
     *
     * @return the colour for this feature
     */
    @Override
    public Object evaluate(Object feature) {
        if (lookup == null) {
            createLookup();
        }

        Object key = ((Expression) params.get(0)).evaluate(feature);
        Color color = lookup.get(key);
        if (color == null) {
            color = addColor(key);
        }

        return color;
    }

    /**
     * Creates the lookup table and initializes variables used in
     * colour generation
     */
    private void createLookup() {
        lookup = new HashMap<Object, Color>();
        try {
            UniqueVisitor visitor = new UniqueVisitor((Expression) params.get(0));
            collection.accepts(visitor, null);
            numColours = visitor.getUnique().size();
            hue = INITIAL_HUE;
            hueIncr = (1.0f - hue) / numColours;

        } catch (Exception ex) {
            throw new IllegalStateException("Problem creating colour lookup", ex);
        }
    }

    /*
     * Generates a new colour for the colour ramp and adds it to
     * the lookup table
     */
    private Color addColor(Object key) {
        Color c = new Color(Color.HSBtoRGB(hue, saturation, brightness));
        hue += hueIncr;
        lookup.put(key, c);
        return c;
    }
}
}
