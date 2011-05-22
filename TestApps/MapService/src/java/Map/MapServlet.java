/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;

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
import org.geotools.data.postgis.PostgisDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.BasicPolygonStyle;
import org.opengis.feature.Property;

/**
 *
 * @author Gune
 */
@WebServlet(name = "MapServlet", urlPatterns = {"/MapServlet"})
public class MapServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
            //input.close();
        } catch (IOException e) {
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
