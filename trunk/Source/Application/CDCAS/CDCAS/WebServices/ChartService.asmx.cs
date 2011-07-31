using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Web.UI;
using CDCAS.UserControls;
using CDCAS.Utilities;
using System.Web.UI.DataVisualization.Charting;
using System.IO;
using System.Text;

namespace CDCAS.WebServices
{
    /// <summary>
    /// Summary description for ChartService
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class ChartService : System.Web.Services.WebService
    {

        [WebMethod(EnableSession = true)]
        public void LoadOverviewChart(string filter, string title)
        {
            HttpResponse response = HttpContext.Current.Response;
            try
            {
                Page page = new ContainerPage();
                ChartContainer chart = (ChartContainer)page.LoadControl("~/UserControls/ChartContainer.ascx");

                Dictionary<string, object> dic = new Dictionary<string, object>();

                dic[ChartConfigurationKeys.QUERY.ToString()] = string.Format(Constants.GenerateDrilldownChart, filter);
                dic[ChartConfigurationKeys.HEIGHT.ToString()] = 350;
                dic[ChartConfigurationKeys.WIDTH.ToString()] = 250;
                dic[ChartConfigurationKeys.XVALARRAY.ToString()] = "NAME";
                dic[ChartConfigurationKeys.YVALARRAY.ToString()] = new List<string> { "PCOUNT" };
                dic[ChartConfigurationKeys.XTITLE.ToString()] = "Diesease";
                dic[ChartConfigurationKeys.YTITLE.ToString()] = "Patients";
                dic[ChartConfigurationKeys.TYPE.ToString()] = SeriesChartType.Pie;
                dic[ChartConfigurationKeys.LEGEND.ToString()] = "Diesease";
                dic[ChartConfigurationKeys.TITLE.ToString()] = title;
                dic[ChartConfigurationKeys.VIEWLEGEND.ToString()] = true;
                dic[ChartConfigurationKeys.DISABLELABELS.ToString()] = false;
                dic[ChartConfigurationKeys.PIELABELSTYLE.ToString()] = "Outside";
                dic[ChartConfigurationKeys.NOOFCHARTSERIES.ToString()] = 1;
                dic[ChartConfigurationKeys.SERIESNAMES.ToString()] = new List<string> { "DieseasesVsPatients" };

                chart.ChartConfigurations = dic;

                page.Controls.Add(chart);

                StringBuilder text = new StringBuilder();
                StringWriter writer = new StringWriter(text);

                HtmlTextWriter htmlWriter = new HtmlTextWriter(writer);

                //page.RenderControl(htmlWriter);
                HttpContext.Current.Server.Execute(page, writer, false);
                response.Output.Write(text.ToString());

            }
            catch (Exception e)
            {
                response.Output.Write(e.Message);
            }
            finally
            {
                response.Output.Flush();
                response.Output.Close();
            }
        }
    }
}
