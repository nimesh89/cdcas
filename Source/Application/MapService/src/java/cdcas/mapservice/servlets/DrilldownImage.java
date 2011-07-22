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
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.EmptyFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 *
 * @author Gune
 */
@WebServlet(name = "DrilldownImage", urlPatterns = {"/DrilldownImage"})
public class DrilldownImage extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
    private FilterFactoryImpl filterFactory = new FilterFactoryImpl();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.request = request;
        this.response = response;

        String height = extractParams(DrillDownConstants.HEIGHT)[0];
        String width = extractParams(DrillDownConstants.WIDTH)[0];
        String baseMap = extractParams(DrillDownConstants.BASEMAP)[0];
        String mapColor = extractParams(DrillDownConstants.MAPCOLOR)[0];
        String filterValue = extractParams(DrillDownConstants.FILTERVALUE)[0];
        String[] background = extractParams(DrillDownConstants.BACKGROUND);
        String mapLevel = extractParams(DrillDownConstants.MAPLEVEL)[0];
        DrillDownConstants level = DrillDownConstants.valueOf(mapLevel);

        DataStore store = getDataStore();
        Filter filter = null;

        if (level == DrillDownConstants.DISTRIC) {
            filter = filterFactory.equals(filterFactory.property("province_c"), filterFactory.literal(filterValue));
        } else if (level == DrillDownConstants.DS) {
            filter = filterFactory.equals(filterFactory.property("dcode"), filterFactory.literal(filterValue));
        }

        if (store != null) {
            MapContext map = new DefaultMapContext();
            try {
                //Query query = filter == null?new Query(baseMap):new Query(baseMap, filter);
                Style layerStyle = createPolygonStyle(Color.BLACK, Color.decode(mapColor));
                SimpleFeatureCollection features = store.getFeatureSource(baseMap).getFeatures(filter);
                //SimpleFeatureCollection inMemmory = getInMemmoryFeatureCollection(features.features(), features.getSchema(), filter);
                //Layer layer = new FeatureLayer(inMemmory, layerStyle);
                map.addLayer(features, layerStyle);

                Rectangle imageSize = new Rectangle(Integer.parseInt(width), Integer.parseInt(height));

                BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D gr = image.createGraphics();
                gr.setPaint(background == null ? Color.BLUE : Color.decode(background[0]));
                gr.fill(imageSize);  //otherwise black background - which throws of transparency of color
                try {
                    GTRenderer renderer = new StreamingRenderer();
                    renderer.setContext(map);


                    ByteArrayOutputStream bt = new ByteArrayOutputStream();
                    ImageOutputStream str = ImageIO.createImageOutputStream(bt);

                    renderer.paint(gr, imageSize, map.getAreaOfInterest());

                    ImageIO.write(image, "jpeg", str);

                    response.setHeader("Content-Type", "imsge/jpeg");
                    response.setHeader("Content-Length", "" + bt.toByteArray().length);
                    response.setHeader("Content-Disposition", "inline; filename=\"" + baseMap.concat(filterValue) + "\"");

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
                    System.out.println("");
                } finally {
                    if (store != null) {
                        store.dispose();
                    }
                }
            } catch (Exception e) {
                System.out.println("");
            } finally {
                if (map != null) {
                    map.dispose();
                }
            }
        }
    }

    private SimpleFeatureCollection getInMemmoryFeatureCollection(SimpleFeatureIterator reader, SimpleFeatureType schema, Filter filter) {
        SimpleFeatureCollection inMemmory = new EmptyFeatureCollection(schema);
        while (reader.hasNext()) {
            SimpleFeature feature = reader.next();
            boolean satisfy = filter.evaluate(feature);
            if (satisfy) {
                inMemmory.add(feature);
            }
        }
        reader.close();
        return inMemmory;
    }

    private DataStore getDataStore() throws IOException {
        HashMap params = new HashMap();
        params.put(PostgisNGDataStoreFactory.DBTYPE.key, "postgis");
        params.put(PostgisNGDataStoreFactory.HOST.key, "localhost");
        params.put(PostgisNGDataStoreFactory.PORT.key, 5432);
        params.put(PostgisNGDataStoreFactory.SCHEMA.key, "public");
        params.put(PostgisNGDataStoreFactory.DATABASE.key, "postgis");
        params.put(PostgisNGDataStoreFactory.USER.key, "postgres");
        params.put(PostgisNGDataStoreFactory.PASSWD.key, "123");

        return DataStoreFinder.getDataStore(params);
    }

    private String[] extractParams(DrillDownConstants key) {
        Map<String, String[]> params = this.request.getParameterMap();

        String[] val = params.containsKey(key.name())
                ? (params.get(key.name()).length > 0
                ? params.get(key.name())
                : null)
                : null;

        return val;
    }

    private Style createPolygonStyle(Color s, Color f) {
        // create a partially opaque outline stroke
        Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(s),
                filterFactory.literal(1),
                filterFactory.literal(1.0));

        // create a partial opaque fill
        Fill fill = styleFactory.createFill(
                filterFactory.literal(f),
                filterFactory.literal(1.0));

        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        Rule rules[] = new Rule[1];

        rules[0] = styleFactory.createRule();

        rules[0].symbolizers().add(sym);



        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(rules);
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

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

enum DrillDownConstants {

    BASEMAP,
    HEIGHT,
    WIDTH,
    BACKGROUND,
    FILTERKEY,
    FILTERVALUE,
    MAPCOLOR,
    MAPLEVEL,
    PROVINCE,
    DISTRIC,
    DS
}