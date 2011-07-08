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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletConfig;
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
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.label.LabelCacheImpl;
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
import org.opengis.filter.Filter;

/**
 *
 * @author Gune
 */
@WebServlet(name = "MapService", urlPatterns = {"/MapService"})
public class MapService extends HttpServlet {

    private StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
    private FilterFactoryImpl filterFactory = new FilterFactoryImpl();
    private DataStore store;
    private String viewName;

    private enum Constants {

        DESCODE, DATE, DATEFORMAT, DATEPROPERTY, DESCODEPROPERTY, CREATEVIEW, CONSTRING, DRIVER, SEPERATOR, USER, PASSWD, DBTYPE, HOST, PORT, SCHEMA, DATABASE, VIEWNAMESEPERATOR {

            @Override
            public String toString() {
                return "_";
            }
        }, CHECKVIEW, FULLMAP, MINVALUE, MAXVALUE, PCOUNT {

            @Override
            public String toString() {
                return "pcount";
            }
        }, HEIGHT, WIDTH,}

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            MapContext map = generateMap(request.getParameterMap(), getInitParameter(Constants.DATEFORMAT.name()));
            GTRenderer renderer = new StreamingRenderer();
            renderer.setContext(map);

            HashMap<Object, Object> hints = new HashMap<Object, Object>();
            LabelCacheImpl lcsh = new LabelCacheImpl();
            lcsh.setLabelRenderingMode(LabelCacheImpl.LabelRenderingMode.ADAPTIVE);
            hints.put(StreamingRenderer.LABEL_CACHE_KEY, lcsh);

            renderer.setRendererHints(hints);
            Map<String, String[]> params = request.getParameterMap();
            
            String height = params.containsKey(Constants.HEIGHT.name())
                    ? (params.get(Constants.HEIGHT.name()).length > 0
                    ? params.get(Constants.HEIGHT.name())[0]
                    : null)
                    : null;
            
            String width = params.containsKey(Constants.WIDTH.name())
                    ? (params.get(Constants.WIDTH.name()).length > 0
                    ? params.get(Constants.WIDTH.name())[0]
                    : null)
                    : null;

            Rectangle imageSize = new Rectangle(Integer.parseInt(width), Integer.parseInt(height));

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
                response.setHeader("Content-Disposition", "inline; filename=\"" + viewName.concat(".jpeg") + "\"");

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
            } finally {
                if (store != null) {
                    store.dispose();
                }
            }

        } catch (Exception e) {
        }
    }

    /*
     * 
     */
    private DataStore getDatastore() {
        DataStore datastore = null;
        try {
            ServletConfig config = getServletConfig();

            HashMap params = new HashMap();
            params.put(PostgisNGDataStoreFactory.DBTYPE.key, config.getInitParameter(Constants.DBTYPE.name()));
            params.put(PostgisNGDataStoreFactory.HOST.key, config.getInitParameter(Constants.HOST.name()));
            params.put(PostgisNGDataStoreFactory.PORT.key, config.getInitParameter(Constants.PORT.name()));
            params.put(PostgisNGDataStoreFactory.SCHEMA.key, config.getInitParameter(Constants.SCHEMA.name()));
            params.put(PostgisNGDataStoreFactory.DATABASE.key, config.getInitParameter(Constants.DATABASE.name()));
            params.put(PostgisNGDataStoreFactory.USER.key, config.getInitParameter(Constants.USER.name()));
            params.put(PostgisNGDataStoreFactory.PASSWD.key, config.getInitParameter(Constants.PASSWD.name()));

            datastore = DataStoreFinder.getDataStore(params);
        } catch (Exception e) {
            if (datastore != null) {
                datastore.dispose();
            }
            return null;
        }
        return datastore;
    }

    private MapContext generateMap(Map<String, String[]> params, String format) {
        String descode = params.containsKey(Constants.DESCODE.name())
                ? (params.get(Constants.DESCODE.name()).length > 0
                ? params.get(Constants.DESCODE.name())[0]
                : null)
                : null;
        String dateString = params.containsKey(Constants.DATE.name())
                ? (params.get(Constants.DATE.name()).length > 0
                ? params.get(Constants.DATE.name())[0]
                : null)
                : null;
        SimpleDateFormat dateFormat = new SimpleDateFormat();

        Date date = new Date();

        try {
            date = dateFormat.parse(dateString);
        } catch (Exception e) {
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);

        viewName = generateViewt(year, descode);

        store = getDatastore();

        MapContext map = new DefaultMapContext();

        try {
            FeatureSource fullMap = store.getFeatureSource(getInitParameter(Constants.FULLMAP.name()));
            FeatureSource selectedAreas = store.getFeatureSource(viewName);
            map.addLayer(fullMap, createPolygonStyle(Color.BLACK, Color.decode("0xA7C951"), false));
            map.addLayer(selectedAreas, createPolygonStyle(Color.BLACK, Color.RED, true));
        } catch (Exception e) {
        }

        return map;

    }

    private Connection getPostgreConnection() {
        Connection connection = null;
        try {
            Class.forName(getInitParameter(Constants.DRIVER.name()));
            connection = DriverManager.getConnection(getInitParameter(Constants.CONSTRING.name()), getInitParameter(Constants.USER.name()), getInitParameter(Constants.PASSWD.name()));
        } catch (Exception e) {
            System.out.println("cdcas.mapservice.servlets.MapService.getPostgreConnection:connection error");
        }
        return connection;
    }

    private String generateViewt(int year, String diseaseCode) {
        Connection con = getPostgreConnection();
        Statement statement = null;
        String view = diseaseCode.toLowerCase() + year;
        String checkViewSql = String.format(getInitParameter(Constants.CHECKVIEW.name()), view);
        String sql = String.format(getInitParameter(Constants.CREATEVIEW.name()), view, year, diseaseCode);
        try {
            if (con != null) {
                statement = con.createStatement();
                ResultSet result = statement.executeQuery(checkViewSql);
                if (!result.next()) {
                    statement.execute(sql);
                }
                if (result != null) {
                    result.close();
                }
            } else {
                throw new Exception("cdcas.mapservice.servlets.MapService.generateView:NULL Connection");
            }
        } catch (Exception e) {
            System.out.println("cdcas.mapservice.servlets.MapService.generateView:SQL error");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
        return view;
    }

    private Style createPolygonStyle(Color s, Color f, boolean enableText) {
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

        Rule rules[] = new Rule[2];

        rules[0] = styleFactory.createRule();
        rules[1] = styleFactory.createRule();

        rules[0].symbolizers().add(sym);

        if (enableText) {

            Fill criticalFill = styleFactory.createFill(
                    filterFactory.literal(Color.RED),
                    filterFactory.literal(1.0));

            Fill highlFill = styleFactory.createFill(
                    filterFactory.literal(Color.ORANGE),
                    filterFactory.literal(1.0));

            Fill moderateFill = styleFactory.createFill(
                    filterFactory.literal(Color.YELLOW),
                    filterFactory.literal(1.0));

            PolygonSymbolizer criticalSym = styleFactory.createPolygonSymbolizer(stroke, criticalFill, null);
            PolygonSymbolizer highSym = styleFactory.createPolygonSymbolizer(stroke, highlFill, null);
            PolygonSymbolizer moderateSym = styleFactory.createPolygonSymbolizer(stroke, moderateFill, null);

            Filter[] filters = generateFilter();

            rules = new Rule[]{styleFactory.createRule(), styleFactory.createRule(), styleFactory.createRule(), styleFactory.createRule()};

            rules[0].symbolizers().add(criticalSym);
            rules[0].setFilter(filters[0]);

            rules[1].symbolizers().add(highSym);
            rules[1].setFilter(filters[1]);

            rules[2].symbolizers().add(moderateSym);
            rules[2].setFilter(filters[2]);

            rules[3].symbolizers().add(createTextStyle());
        }

        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(rules);
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    /**
     * Create a Style to draw point features as circles with blue outlines
     * and cyan fill
     */
    private Style createPointStyle() {
        Graphic gr = styleFactory.createDefaultGraphic();

        Mark mark = styleFactory.getCircleMark();

        mark.setStroke(styleFactory.createStroke(
                filterFactory.literal(Color.BLUE), filterFactory.literal(1)));

        mark.setFill(styleFactory.createFill(filterFactory.literal(Color.RED)));

        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add(mark);
        gr.setSize(filterFactory.literal(5));

        PointSymbolizer sym = styleFactory.createPointSymbolizer(gr, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    private TextSymbolizer createTextStyle() {
        StyleBuilder build = new StyleBuilder();
        TextSymbolizer textSym = build.createTextSymbolizer(Color.BLACK, build.createFont("Arial", false, false, 9),
                "gid");
        textSym.setHalo(build.createHalo(Color.WHITE, 2));
        textSym.setLabelPlacement(build.createPointPlacement(0.5, 0.5, 0.0));
        textSym.getOptions().put("spaceAround", "-1");
        return textSym;
    }

    private Filter[] generateFilter() {
        Connection con = getPostgreConnection();
        Statement statement = null;
        Filter[] filters = null;
        try {
            if (con != null) {
                statement = con.createStatement();
                String minValueSql = String.format(getInitParameter(Constants.MINVALUE.name()), viewName);
                String maxValueSql = String.format(getInitParameter(Constants.MAXVALUE.name()), viewName);

                ResultSet minResult = statement.executeQuery(minValueSql);
                statement = con.createStatement();
                ResultSet maxResultSet = statement.executeQuery(maxValueSql);

                int max = 0, min = 0;

                while (minResult.next() & maxResultSet.next()) {
                    max = maxResultSet.getInt(1);
                    min = minResult.getInt(1);
                }

                minResult.close();
                maxResultSet.close();

                int segment = (max - min) / 3;
                int q1 = min + segment;
                int q2 = max - segment;

                Filter critical = filterFactory.between(filterFactory.property(Constants.PCOUNT.toString()), filterFactory.literal(q2), filterFactory.literal(max));
                Filter high = filterFactory.between(filterFactory.property(Constants.PCOUNT.toString()), filterFactory.literal(q1), filterFactory.literal(q2));
                Filter moderate = filterFactory.between(filterFactory.property(Constants.PCOUNT.toString()), filterFactory.literal(min), filterFactory.literal(q1));

                filters = new Filter[]{critical, high, moderate};
            } else {
                throw new Exception("cdcas.mapservice.servlets.MapService.generateView:NULL Connection");
            }
        } catch (Exception e) {
            System.out.println("cdcas.mapservice.servlets.MapService.generateView:SQL error");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }

        return filters;
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
