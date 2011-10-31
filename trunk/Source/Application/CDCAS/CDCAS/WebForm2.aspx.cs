using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.UI.DataVisualization.Charting;
using CDCAS.Utilities;

namespace CDCAS
{
    public partial class WebForm2 : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            Dictionary<string, object> dic = new Dictionary<string, object>();
            dic[ChartConfigurationKeys.HEIGHT.ToString()] = 500;
            dic["WIDTH"] = 300;
            dic["QUERY"] = "EXEC dbo.spPatientsBreakDownAgeAndYear";
            dic["TYPE"] = SeriesChartType.Column;
            dic["TITLE"] = "Year Vs. Patients";
            dic["XTITLE"] = "Year";
            dic["YTITLE"] = "Patients";
            dic["SERIESNAMES"] = new List<string>() { "Male", "Female", "Child" };
            dic["XVALARRAY"] = "YEAR";
            dic["YVALARRAY"] = new List<string> { "MALECOUNT", "FEMALECOUNT", "CHILDCOUNT" };
            dic["NOOFCHARTSERIES"] = 3;
            dic["VIEWLEGEND"] = true;
            dic["LEGEND"] = "Gender";

            ChartContainer1.ChartConfigurations = dic;
        }
    }
}