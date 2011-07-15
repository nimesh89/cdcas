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
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.styling.StyleBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;

/**
 *
 * @author Gune
 */
@WebServlet(name = "HtmlImageMapService", urlPatterns = {"/HtmlImageMapService"})
public class HtmlImageMapService extends HttpServlet {

    private final String viewNameKey = "VIEW";
    
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setHeader("Content-Type", "text/html");

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
            
            String view = request.getParameter(viewNameKey);
            

            dataStore = DataStoreFinder.getDataStore(params);

            FeatureSource featureSource = dataStore.getFeatureSource("vwTest");


            FilterFactory2 factory = new FilterFactoryImpl();


            map = new DefaultMapContext();


            StyleBuilder styleBuilder = new StyleBuilder();


            map.addLayer(featureSource.getFeatures(), styleBuilder.createStyle());

            Rectangle imageSize = new Rectangle(600, 650);



            AffineTransform worldToScreen = RendererUtilities.worldToScreenTransform(map.getAreaOfInterest(), imageSize);
            SimpleFeatureCollection col = dataStore.getFeatureSource(view).getFeatures();//factory.equals(factory.property("gid"), factory.literal(3))
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


                String val = "gid=" + feature.getProperty("gid").getValue().toString();
                response.getWriter().write(String.format("<area href=\"GeograpHicalStats.aspx?%s\" shape=\"poly\" title=\"%s\" coords=\"%s\" />\n", val, feature.getProperty("divisec").getValue().toString() + "," + feature.getProperty("gid").getValue().toString(), st));


            }


            itr.close();


        } catch (Exception e) {
            System.out.println("");
        } finally {
            if (dataStore != null) {
                dataStore.dispose();
            }
            if (map != null) {
                map.dispose();
            }
            //response.getWriter().write("</map>");
            response.getWriter().flush();
            response.getWriter().close();
        }
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
