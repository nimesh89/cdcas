/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cdcas.mapservice.servlets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
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
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.BasicPolygonStyle;
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
import org.geotools.text.Text;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.style.Font;

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

//        for(String s: dataStore.getTypeNames())
//        {
//            System.out.println(s);
//        }

            FeatureSource featureSource = dataStore.getFeatureSource("fullcountview");
            FilterFactory2 factory = new FilterFactoryImpl();
            
//            Filter selectMaharagama = factory.between(factory.property("code"), factory.literal(1114.0), factory.literal(2107.0));
//
//            FeatureCollection maharagama = dataStore.getFeatureSource("divsec").getFeatures(selectMaharagama);

            StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
//
//        // Create a map context and add our shapefile to it
            MapContext map = new DefaultMapContext();
            map.addLayer(featureSource, createPolygonStyle(Color.BLUE, Color.CYAN));
//            map.addLayer(maharagama, createTextStyle());
            map.setTitle("Quickstart");
            BasicPolygonStyle st = new BasicPolygonStyle();

            GTRenderer renderer = new StreamingRenderer();
            renderer.setContext(map);

            Rectangle imageSize = new Rectangle(600, 600);

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
                output.flush();
                output.close();

            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
        finally
        {
            if(dataStore!=null)
                dataStore.dispose();
        }
    }

    /**
     * Create a Style to draw polygon features with a thin blue outline and
     * a cyan fill
     */
    private Style createPolygonStyle(Color s, Color f) {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
        FilterFactory2 filterFactory = new FilterFactoryImpl();
        // create a partially opaque outline stroke
        Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(s),
                filterFactory.literal(1),
                filterFactory.literal(0.5));

        // create a partial opaque fill
        Fill fill = styleFactory.createFill(
                filterFactory.literal(f),
                filterFactory.literal(0.5));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
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
                "divisec");
        textSym.setHalo(build.createHalo(Color.WHITE, 1));
        textSym.setLabelPlacement(build.createPointPlacement(0.5, 0.5, 0, 0, 0));
        textSym.getOptions().put("spaceAround", "-1");
        Style style = build.createStyle(textSym);
        return style;
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
