/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cdcas.mapservice.servlets;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.TextSymbolizer;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/**
 *
 * @author Gune
 */
@WebServlet(name = "MapImageService", urlPatterns = {"/MapImageService"})
public class MapImageService extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();
        DataStore dataStore = null;
        MapContext map = null;
        try {
            HashMap params = new HashMap();
            params.put(PostgisNGDataStoreFactory.DBTYPE.key, "postgis");
            params.put(PostgisNGDataStoreFactory.HOST.key, "localhost");
            params.put(PostgisNGDataStoreFactory.PORT.key, 5432);
            params.put(PostgisNGDataStoreFactory.SCHEMA.key, "public");
            params.put(PostgisNGDataStoreFactory.DATABASE.key, "postgis");
            params.put(PostgisNGDataStoreFactory.USER.key, "postgres");
            params.put(PostgisNGDataStoreFactory.PASSWD.key, "123");

            dataStore = DataStoreFinder.getDataStore(params);

            FeatureSource featureSource = dataStore.getFeatureSource("provincemap");

            FilterFactory2 factory = new FilterFactoryImpl();

            //process inputs

            //generate filter

            //generate diferent styles

            //Filter selectDengi = factory.not(factory.isNull(factory.property("pcount")));

            // FeatureCollection dengiSet = dataStore.getFeatureSource("deng2011_07_07").getFeatures();

            StreamingRenderer renderer = new StreamingRenderer();
            map = new DefaultMapContext();


            int i = 120;
            Filter filters[] = new Filter[]{
                //factory.equals(factory.property("gid"), factory.literal(110)), 
                //                            factory.equals(factory.property("gid"), factory.literal(i++)),
            //                            factory.equals(factory.property("gid"), factory.literal(i++)),
            //                            factory.equals(factory.property("gid"), factory.literal(i++)),
            //                            factory.equals(factory.property("gid"), factory.literal(i++)),
            //                            factory.equals(factory.property("gid"), factory.literal(i++)),
            //                            factory.equals(factory.property("gid"), factory.literal(i++)),
            //                            factory.equals(factory.property("gid"), factory.literal(i++)),
            //                            factory.equals(factory.property("gid"), factory.literal(i++)),
            //                            factory.equals(factory.property("gid"), factory.literal(i++)),
            //                            factory.equals(factory.property("gid"), factory.literal(i++)),
            //                            factory.equals(factory.property("gid"), factory.literal(i++)),
            //                            factory.equals(factory.property("gid"), factory.literal(i++)),
            //                            factory.equals(factory.property("gid"), factory.literal(i++)),
            //                            factory.equals(factory.property("gid"), factory.literal(i++)),
            //                            factory.equals(factory.property("gid"), factory.literal(i++))
            };

            Color fills[] = new Color[]{
               // Color.decode("0x1BA5E0")
//                Color.decode("0x4D2EFF"),
//                Color.decode("0xB62EFF"),
//                Color.decode("0xFF2EE0"),
//                Color.decode("0xFF2E77"),
//                Color.decode("0x2E77FF"),
//                Color.decode("0x816BFF"),
//                Color.decode("0xB5A8FF"),
//                Color.decode("0xFF4D2E"),
//                Color.decode("0x2EE0FF"),
//                Color.decode("0xF2FFA8"),
//                Color.decode("0xE9FF6B"),
//                Color.decode("0xFFB62E"),
//                Color.decode("0x2EFFB6"),
//                Color.decode("0x2EFF4D"),
//                Color.decode("0x77FF2E"),
//                Color.decode("0xE0FF2E")
            };


            map.addLayer(featureSource.getFeatures(), createPolygonStyle(Color.BLACK, filters, fills));

            Rectangle imageSize = new Rectangle(600, 650);

//            AffineTransform worldToScreen = RendererUtilities.worldToScreenTransform(map.getAreaOfInterest(), imageSize);
//            SimpleFeatureCollection col = dataStore.getFeatureSource("vwTest").getFeatures(factory.equals(factory.property("gid"), factory.literal(51)));
////            GeometryFactory fact = new GeometryFactory();
////            List<LineString> al = new LinkedList<LineString>();
//            while (col.features().hasNext()) {
//                SimpleFeature feature = col.features().next();
//                MultiPolygon g = (MultiPolygon)feature.getDefaultGeometry();
//                double[] worldCordinates = new double[g.getCoordinates().length*2];
//                int x=0;
//                for(Coordinate cord:g.getCoordinates()){
//                    worldCordinates[x++] = cord.x;
//                    worldCordinates[x++] = cord.y;
//                }
//                
//                double[] screenCordinates = new double[worldCordinates.length];
//                
//                worldToScreen.transform(worldCordinates, 0, screenCordinates, 0, screenCordinates.length);
//                
//                System.out.println("");
//                MultiPolygon mp = (MultiPolygon) feature.getDefaultGeometryProperty().getValue();
//                int n = mp.getNumGeometries();
//                for (int l = 0; l < n; l++) {
//                    Geometry g = mp.getGeometryN(l);
//                    Coordinate[] coords = g.getCoordinates();
//                    LineString ls = fact.createLineString(coords);
//                    al.add(ls);
//                }
//            }

//////

//            FilterFactory ff = FilterFactoryFinder.createFilterFactory();
//GeometryFactory fact = JTSFactoryFinder.getGeometryFactory(null);
//GeometryFilter gf = ff.createGeometryFilter(FilterType.GEOMETRY_WITHIN);
//gf.addLeftGeometry(ff.createLiteralExpression(fact.createPoint(new Coordinate(316, 420))));

//FeatureCollection result = featureSource.getFeatures(gf);
//
//            JMapFrame frame  = new JMapFrame(map);
//            JMapPane pane = new JMapPane(renderer, map);
//            frame.createImage(600, 650); 
//            Image img = pane.createImage(600, 650);
//
//            AffineTransform screenToWorld = frame.getMapPane().getScreenToWorldTransform();
//
//            Rectangle2D worldRect = screenToWorld.createTransformedShape(new Rectangle(316, 420, 1, 1)).getBounds2D();
//            ReferencedEnvelope bbox = new ReferencedEnvelope(
//                    worldRect,
//                    map.getCoordinateReferenceSystem());
//
//            Filter selected = factory.intersects(factory.property("the_geom"), factory.literal(bbox));
//            FeatureCollection selectedSet = dataStore.getFeatureSource("divsec").getFeatures(selected);
//
//            new Rectangle(316, 420, 1, 1);
//            map.addLayer(result, createPolygonStyle(Color.RED, Color.YELLOW, null));
//            map.addLayer(dengiSet, createTextStyle());
//
            renderer.setContext(map);


            HashMap<Object, Object> hints = new HashMap<Object, Object>();
            LabelCacheImpl lcsh = new LabelCacheImpl();
            lcsh.setLabelRenderingMode(LabelCacheImpl.LabelRenderingMode.ADAPTIVE);
            hints.put(StreamingRenderer.LABEL_CACHE_KEY, lcsh);
            hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            renderer.setRendererHints(hints);



            BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D gr = image.createGraphics();
            gr.setPaint(Color.WHITE);
            gr.fill(imageSize);  //otherwise black background - which throws of transparency of color
            try {
                ByteArrayOutputStream bt = new ByteArrayOutputStream();
                ImageOutputStream str = ImageIO.createImageOutputStream(bt);

                renderer.paint(gr, imageSize, map.getAreaOfInterest());

                ImageIO.write(image, "jpeg", str);

                response.setHeader("Content-Type", "imsge/jpeg");
                response.setHeader("Content-Length", "" + bt.toByteArray().length);
                response.setHeader("Content-Disposition", "inline; filename=\"" + "test.jpeg" + "\"");

                BufferedInputStream input = new BufferedInputStream(new ByteArrayInputStream(bt.toByteArray()));
                BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream());
                byte[] buffer = new byte[8192];
                for (int length = 0; (length = input.read(buffer)) > 0;) {
                    output.write(buffer, 0, length);
                }
                str.flush();
                str.close();
                input.close();
                output.flush();
                output.close();

            } catch (Exception e) {
            }

        } catch (Exception e) {
            System.out.println("");
        } finally {
            if (dataStore != null) {
                dataStore.dispose();
            }
            if (map != null) {
                map.dispose();
            }
        }
    }

    /**
     * Create a Style to draw polygon features with a thin blue outline and
     * a cyan fill
     */
    private Style createPolygonStyle(Color s, Filter filter[], Color fills[]) {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
        FilterFactory2 filterFactory = new FilterFactoryImpl();
        // create a partially opaque outline stroke
        Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(s),
                filterFactory.literal(1),
                filterFactory.literal(1.0));

        Fill defaultFill = styleFactory.createFill(
                filterFactory.literal(Color.WHITE),
                filterFactory.literal(0.0));

        ArrayList<Rule> rules = new ArrayList<Rule>();

        for (int i = 0; i < filter.length; i++) {
            // create a partial opaque fill
            Fill fill = styleFactory.createFill(
                    filterFactory.literal(fills[i]),
                    filterFactory.literal(1.0));


            PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);


            Rule rule = styleFactory.createRule();

            rule.setFilter(filter[i]);

            rule.symbolizers().add(sym);

            rules.add(rule);
        }

        StyleBuilder build = new StyleBuilder();
        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, defaultFill, null);
        TextSymbolizer textSym = build.createTextSymbolizer(Color.BLACK, build.createFont("Arial", false, false, 9),
                "gid");
        textSym.setHalo(build.createHalo(Color.WHITE, 2));
        textSym.setLabelPlacement(build.createPointPlacement(0.5, 0.5, 0.0));
        textSym.getOptions().put("spaceAround", "-1");

        Rule rule = styleFactory.createRule();

        //rule.symbolizers().add(textSym);
        rule.symbolizers().add(sym);

        rules.add(rule);

        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(rules.toArray(new Rule[rules.size()]));
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    /**
     * Create a Style to draw point features as circles with blue outlines
     * and cyan fill
     */
    private Style createPointStyle() {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
        FilterFactory2 filterFactory = new FilterFactoryImpl();
        Graphic gr = styleFactory.createDefaultGraphic();

        Mark mark = styleFactory.getCircleMark();

        mark.setStroke(styleFactory.createStroke(
                filterFactory.literal(Color.BLUE), filterFactory.literal(1)));

        mark.setFill(styleFactory.createFill(filterFactory.literal(Color.RED)));

        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add(mark);
        gr.setSize(filterFactory.literal(5));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        PointSymbolizer sym = styleFactory.createPointSymbolizer(gr, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    private Style createTextStyle() {
        StyleBuilder build = new StyleBuilder();
        TextSymbolizer textSym = build.createTextSymbolizer(Color.BLACK, build.createFont("Arial", false, false, 9),
                "gid");
        textSym.setHalo(build.createHalo(Color.WHITE, 2));
        textSym.setLabelPlacement(build.createPointPlacement(0.5, 0.5, 0.0));
        textSym.getOptions().put("spaceAround", "-1");
        //textSym.getOptions().put("goodnessOfFit", "0.7");
        Style style = build.createStyle(textSym);
        return style;
    }

    private Rule[] setColors(SimpleFeatureCollection collection) {
        ArrayList<Rule> rules = new ArrayList<Rule>();
        for (Object item : collection.toArray()) {
            SimpleFeature feature = (SimpleFeature) item;
            int dcode = Integer.parseInt(feature.getProperty("dcode").getValue().toString());
            switch (dcode) {
                case 1:
                    break;
                case 12:
                    break;
                case 21:
                    break;
                case 22:
                    break;
                case 23:
                    break;
                case 24:
                    break;
                case 25:
                    break;
                case 26:
                    break;
                case 27:
                    break;
                case 28:
                    break;
                case 31:
                    break;
                case 32:
                    break;
                case 33:
                    break;
                case 41:
                    break;
                case 51:
                    break;
                case 52:
                    break;
                case 53:
                    break;
                case 61:
                    break;
                case 62:
                    break;
                case 71:
                    break;
                case 72:
                    break;
                case 81:
                    break;
                case 82:
                    break;
                case 83:
                    break;
            }
        }

        return rules.toArray(new Rule[rules.size()]);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
