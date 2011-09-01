/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cdcas.mapservice.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.styling.StyleBuilder;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;

/**
 *
 * @author Gune
 */
@WebServlet(name = "HtmlImageMapService", urlPatterns = {"/HtmlImageMapService"})
public class HtmlImageMapService extends HttpServlet {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterFactoryImpl filterFactory = new FilterFactoryImpl();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.request = request;
        this.response = response;

        String height = extractParams(DrillDownConstants.HEIGHT)[0];
        String width = extractParams(DrillDownConstants.WIDTH)[0];
        String baseMap = extractParams(DrillDownConstants.BASEMAP)[0];
        String filterValue = extractParams(DrillDownConstants.FILTERVALUE)[0];
        String view = extractParams(DrillDownConstants.VIEW)[0];
        String mapLevel = extractParams(DrillDownConstants.MAPLEVEL)[0];
        DrillDownConstants level = DrillDownConstants.valueOf(mapLevel);

        DataStore store = getDataStore();
        MapContext map = new DefaultMapContext();
        StyleBuilder build = new StyleBuilder();
        Filter filter = null;
        SimpleFeatureCollection col = null;
        SimpleFeatureCollection colForArea = null;

        if (level == DrillDownConstants.PROVINCE) {
            col = store.getFeatureSource(baseMap).getFeatures();
            colForArea = store.getFeatureSource(baseMap).getFeatures();
        } else if (level == DrillDownConstants.DISTRIC) {
            filter = filterFactory.equals(filterFactory.property("province_c"), filterFactory.literal(filterValue));
            col = store.getFeatureSource(baseMap).getFeatures(filter);
            colForArea = store.getFeatureSource(baseMap).getFeatures(filter);
        } else if (level == DrillDownConstants.DS) {
            filter = filterFactory.equals(filterFactory.property("dcode"), filterFactory.literal(filterValue));
            col = store.getFeatureSource(baseMap).getFeatures(filter);
            colForArea = store.getFeatureSource(baseMap).getFeatures(filter);
        } else if (level == DrillDownConstants.OVERVIEW) {
            col = store.getFeatureSource(baseMap).getFeatures();
            colForArea = store.getFeatureSource(view).getFeatures();
        }

        map.addLayer(col, build.createStyle());
        Rectangle imageSize = new Rectangle(Integer.parseInt(width), Integer.parseInt(height));

        generateMapAreas(map, imageSize, store, colForArea, response, level);

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

    private void generateMapAreas(MapContext map, Rectangle imageSize, DataStore dataStore, SimpleFeatureCollection col, HttpServletResponse response, DrillDownConstants level) {
        try {
            AffineTransform worldToScreen = RendererUtilities.worldToScreenTransform(map.getAreaOfInterest(), imageSize);
            SimpleFeatureCollection reader = DataUtilities.collection(col.features());

            //response.getWriter().write("<map id=\"districSecretaryMap\" name=\"districSecretaryMap\">");

            int y = 1;
            SimpleFeatureIterator itr = reader.features();
            while (itr.hasNext()) {
                SimpleFeature feature = itr.next();
                MultiPolygon g = (MultiPolygon) feature.getDefaultGeometry();
                int num = g.getNumPoints();
                System.out.println(num);

//                double area = Double.parseDouble(feature.getProperty("area").getValue().toString());


                com.vividsolutions.jts.geom.Polygon smallest = (com.vividsolutions.jts.geom.Polygon) g.convexHull();


                Geometry target = g;
                if (level == DrillDownConstants.PROVINCE) {
                    target = g.convexHull();
                }


                double[] worldCordinates = new double[target.getCoordinates().length * 2];
                int x = 0;

                for (Coordinate c : target.getCoordinates()) {
                    worldCordinates[x++] = c.x;
                    worldCordinates[x++] = c.y;
                }


                double[] screenCordinates = new double[worldCordinates.length];

                worldToScreen.transform(worldCordinates, 0, screenCordinates, 0, screenCordinates.length / 2);

                StringBuilder build = new StringBuilder();
                for (int i = 0; i < screenCordinates.length; i++) {
                    if (i == 0) {
                        int val = (int) screenCordinates[i];
                        build.append(val);
                    } else {
                        int val = (int) screenCordinates[i];
                        build.append(',');
                        build.append(val);
                    }
                }



                String st = build.toString();


                if (level == DrillDownConstants.OVERVIEW) {
                    String val = "gid=" + feature.getProperty("gid").getValue().toString() + "&name=" + feature.getProperty("divisec").getValue().toString();
                    String title = feature.getProperty("gid").getValue().toString() + ","
                            + feature.getProperty("divisec").getValue().toString() + ","
                            + feature.getProperty("pcount").getValue().toString();
                    response.getWriter().write(String.format("<area href=\"GeograpHicalStats.aspx?%s\" shape=\"poly\" title=\"%s\" coords=\"%s\" />\n", val, title, st));
                } else if (level == DrillDownConstants.PROVINCE) {
                    Object provinceId = feature.getProperty("province_c").getValue();
                    Object provinceName = feature.getProperty("province_n").getValue();

                    String val = "province_c=" + provinceId.toString() + "&name=" + provinceName.toString() + "&MAPLEVEL=DISTRIC";
                    String title = provinceId.toString() + ","
                            + provinceName.toString();
                    response.getWriter().write(String.format("<area href=\"DrillDownMap.aspx?%s\" shape=\"poly\" onmouseover=\"getChart('province_c = %s', '%s')\" onmouseout=\"$('#map-div-chart').toggle();\" title=\"%s\" coords=\"%s\" />\n", val, provinceId.toString(), provinceName.toString(),title, st));
                }
                if (level == DrillDownConstants.DISTRIC) {
                    Object provinceId = feature.getProperty("province_c").getValue();
                    Object provinceName = feature.getProperty("province_n").getValue();
                    Object distId = feature.getProperty("dcode").getValue();
                    Object distName = feature.getProperty("dist").getValue();

                    String val = "dcode=" + distId.toString() + "&province_c=" + provinceId.toString() + "&province_n=" + provinceName.toString() + "&name=" + distName.toString() + "&MAPLEVEL=DS";
                    String title = distId.toString() + ","
                            + distName.toString();
                    response.getWriter().write(String.format("<area href=\"DrillDownMap.aspx?%s\" shape=\"poly\" onmouseover=\"getChart('dcode = %s', '%s')\" onmouseout=\"$('#map-div-chart').toggle();\" title=\"%s\" coords=\"%s\" />\n", val, distId.toString(), distName.toString()+" District",title, st));
                }
                if (level == DrillDownConstants.DS) {
                    Object gid = feature.getProperty("gid").getValue();
                    Object name = feature.getProperty("divisec").getValue();
                    String val = "gid=" + gid.toString() + "&name=" + name.toString();
                    String title = gid.toString() + ","
                            + name.toString();
                    response.getWriter().write(String.format("<area href=\"GeograpHicalStats.aspx?%s\" onmouseover=\"getChart('GID = %s', '%s')\" shape=\"poly\" title=\"%s\" coords=\"%s\" />\n", val, gid.toString(), name.toString(),title, st));
                }

            }


            itr.close();


        } catch (Exception e) {
            System.out.println("");
        }
    }

    enum DrillDownConstants {

        BASEMAP,
        HEIGHT,
        WIDTH,
        FILTERKEY,
        FILTERVALUE,
        MAPLEVEL,
        PROVINCE,
        DISTRIC,
        DS,
        OVERVIEW,
        VIEW
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
