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
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.jasper.tagplugins.jstl.core.ForEach;
import org.geotools.arcsde.session.Commands.FetchRowCommand;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.label.LabelCacheImpl;
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
            
            SimpleFeatureCollection featureSource = dataStore.getFeatureSource("divsec").getFeatures();
            
            FilterFactory2 factory = new FilterFactoryImpl();
            
            //process inputs
            
            //generate filter
            
            //generate diferent styles
            
            //Filter selectDengi = factory.not(factory.isNull(factory.property("pcount")));

            FeatureCollection dengiSet = dataStore.getFeatureSource("deng2011_07_07").getFeatures();

            MapContext map = new DefaultMapContext();
            map.addLayer(featureSource, createPolygonStyle(Color.BLACK, Color.white, null));
            map.addLayer(dengiSet, createPolygonStyle(Color.RED, Color.RED, null));
            //map.addLayer(dengiSet, createTextStyle());
            
            GTRenderer renderer = new StreamingRenderer();
            renderer.setContext(map);
            
            HashMap<Object, Object> hints = new HashMap<Object, Object>();
            LabelCacheImpl lcsh = new LabelCacheImpl();
            lcsh.setLabelRenderingMode(LabelCacheImpl.LabelRenderingMode.ADAPTIVE);
            hints.put(StreamingRenderer.LABEL_CACHE_KEY, lcsh);
            
            renderer.setRendererHints(hints);
            
            Rectangle imageSize = new Rectangle(1024, 1000);

            BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D gr = image.createGraphics();
            gr.setPaint(Color.BLUE);
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
    private Style createPolygonStyle(Color s, Color f, Filter filter) {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
        FilterFactory2 filterFactory = new FilterFactoryImpl();
        // create a partially opaque outline stroke
        Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(s),
                filterFactory.literal(1),
                filterFactory.literal(1.0));

        // create a partial opaque fill
        Fill fill = styleFactory.createFill(
                filterFactory.literal(f),
                filterFactory.literal(1.0));
//        
//        Fill fill1 = styleFactory.createFill(
//                filterFactory.literal(Color.RED),
//                filterFactory.literal(0.5));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);
//        PolygonSymbolizer sym1 = styleFactory.createPolygonSymbolizer(stroke, fill1, null);
        
        Rule rule = styleFactory.createRule();
//        Rule rule2 = styleFactory.createRule();
        //rule.
        rule.symbolizers().add(sym);
//        rule2.symbolizers().add(sym1);
//        rule2.setFilter(filter);
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
                "pcount");
        textSym.setHalo(build.createHalo(Color.WHITE, 1));
        textSym.setLabelPlacement(build.createPointPlacement(0.5, 0.5, 0.0));
        textSym.getOptions().put("spaceAround", "-1");
        //textSym.getOptions().put("goodnessOfFit", "0.7");
        Style style = build.createStyle(textSym);
        return style;
    }
    
    private Rule[] setColors(SimpleFeatureCollection collection)
    {
        ArrayList<Rule> rules = new ArrayList<Rule>();
        for(Object item: collection.toArray())
        {
            SimpleFeature feature = (SimpleFeature)item;
            int dcode = Integer.parseInt(feature.getProperty("dcode").getValue().toString());
            switch(dcode)
            {
                case 1:break;
                case 12:break;
                case 21:break;
                case 22:break;
                case 23:break;
                case 24:break;
                case 25:break;
                case 26:break;
                case 27:break;
                case 28:break;
                case 31:break;
                case 32:break;
                case 33:break;
                case 41:break;
                case 51:break;
                case 52:break;
                case 53:break;
                case 61:break;
                case 62:break;
                case 71:break;
                case 72:break;
                case 81:break;
                case 82:break;
                case 83:break;
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
