using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using CDCAS.Utilities;
using System.Web.UI.DataVisualization.Charting;

namespace CDCAS
{
    public partial class GeograpHicalStats : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            Dictionary<string, object> dic = new Dictionary<string, object>();

            dic[ChartConfigurationKeys.QUERY.ToString()] = string.Format("EXEC [dbo].[spDieseaseOverviewForDistricSecretary] {0}", Request.Params["gid"]);
            dic[ChartConfigurationKeys.HEIGHT.ToString()] = 300;
            dic[ChartConfigurationKeys.WIDTH.ToString()] = 400;
            dic[ChartConfigurationKeys.XVALARRAY.ToString()] = "NAME";
            dic[ChartConfigurationKeys.YVALARRAY.ToString()] = new List<string> { "PCOUNT" };
            dic[ChartConfigurationKeys.XTITLE.ToString()] = "Diesease";
            dic[ChartConfigurationKeys.YTITLE.ToString()] = "Patients";
            dic[ChartConfigurationKeys.TYPE.ToString()] = SeriesChartType.Pie;
            dic[ChartConfigurationKeys.LEGEND.ToString()] = "Diesease";
            dic[ChartConfigurationKeys.TITLE.ToString()] = string.Format("Dieseases Vs. Patients\n{0}", Request.Params["name"]);
            dic[ChartConfigurationKeys.VIEWLEGEND.ToString()] = true;
            dic[ChartConfigurationKeys.PIELABELSTYLE.ToString()] = "Outside";
            dic[ChartConfigurationKeys.NOOFCHARTSERIES.ToString()] = 1;
            dic[ChartConfigurationKeys.SERIESNAMES.ToString()] = new List<string> { "DieseasesVsPatients" };

            ChartContainer1.ChartConfigurations = dic;

            dic = new Dictionary<string, object>();

            dic = new Dictionary<string, object>();
            dic[ChartConfigurationKeys.QUERY.ToString()] = string.Format("EXEC [dbo].[spGeographicalPatintCountVsYear] {0}, {1}", "NULL", Request.Params["gid"]);
            dic[ChartConfigurationKeys.HEIGHT.ToString()] = 500;
            dic[ChartConfigurationKeys.WIDTH.ToString()] = 700;
            dic[ChartConfigurationKeys.XVALARRAY.ToString()] = "YEAR";
            dic[ChartConfigurationKeys.YVALARRAY.ToString()] = new List<string> { "MALECOUNT", "FEMALECOUNT", "CHILDCOUNT" };
            dic[ChartConfigurationKeys.XTITLE.ToString()] = "Year";
            dic[ChartConfigurationKeys.YTITLE.ToString()] = "Patients";
            dic[ChartConfigurationKeys.TYPE.ToString()] = SeriesChartType.Column;
            dic[ChartConfigurationKeys.LEGEND.ToString()] = "Gender";
            dic[ChartConfigurationKeys.TITLE.ToString()] = string.Format("Year Vs. Patients\n{0}", Request.Params["name"]);
            dic[ChartConfigurationKeys.VIEWLEGEND.ToString()] = true;
            dic[ChartConfigurationKeys.NOOFCHARTSERIES.ToString()] = 3;
            dic[ChartConfigurationKeys.SERIESNAMES.ToString()] = new List<string> { "Male", "Female", "Children" };

            ChartContainer2.ChartConfigurations = dic;

            dic = new Dictionary<string, object>();

            dic[ChartConfigurationKeys.QUERY.ToString()] = string.Format("EXEC dbo.spPatientCountForTopFiveDieseseForFiveYearsForGivenDistricSec -1", Request.Params["gid"]);
            dic[ChartConfigurationKeys.HEIGHT.ToString()] = 400;
            dic[ChartConfigurationKeys.WIDTH.ToString()] = 500;
            dic[ChartConfigurationKeys.XVALARRAY.ToString()] = "YEAR";
            dic[ChartConfigurationKeys.HASDYNAMICCOLUMNS.ToString()] = true;
            dic[ChartConfigurationKeys.XTITLE.ToString()] = "Diesease";
            dic[ChartConfigurationKeys.YTITLE.ToString()] = "Patients";
            dic[ChartConfigurationKeys.TYPE.ToString()] = SeriesChartType.Line;
            dic[ChartConfigurationKeys.LEGEND.ToString()] = "Diesease";
            dic[ChartConfigurationKeys.TITLE.ToString()] = string.Format("Diesease Trend\n{0}", Request.Params["name"]);
            dic[ChartConfigurationKeys.VIEWLEGEND.ToString()] = true;

            ChartContainer3.ChartConfigurations = dic;
        }
    }
}