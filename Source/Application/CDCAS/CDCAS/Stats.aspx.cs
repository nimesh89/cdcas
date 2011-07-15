using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using CDCAS.Utilities;
using DataHandler;
using System.Configuration;
using System.Data;
using System.Web.UI.DataVisualization.Charting;

namespace CDCAS
{
    public partial class Stats : System.Web.UI.Page
    {
        private string SqlConnectionKey = Constants.SqlConnectionKey;

        private const string ChartDataSql = "EXEC spPatientBreakDownMonthForYear {0}, N'{1}'";

        private const string TitleFormat = "Month Vs. Patients\n{0} - {1}";

        private Dictionary<string, object> dic;

        protected void Page_Load(object sender, EventArgs e)
        {
            dic = new Dictionary<string, object>();

            dic = new Dictionary<string, object>();
            dic[ChartConfigurationKeys.HEIGHT.ToString()] = 500;
            dic[ChartConfigurationKeys.WIDTH.ToString()] = 800;
            dic[ChartConfigurationKeys.XVALARRAY.ToString()] = "MONTH";
            dic[ChartConfigurationKeys.YVALARRAY.ToString()] = new List<string> { "MALECOUNT", "FEMALECOUNT", "CHILDCOUNT" };
            dic[ChartConfigurationKeys.XTITLE.ToString()] = "Month";
            dic[ChartConfigurationKeys.YTITLE.ToString()] = "Patients";
            dic[ChartConfigurationKeys.TYPE.ToString()] = SeriesChartType.Column;
            dic[ChartConfigurationKeys.LEGEND.ToString()] = "Gender";
            dic[ChartConfigurationKeys.VIEWLEGEND.ToString()] = true;
            dic[ChartConfigurationKeys.NOOFCHARTSERIES.ToString()] = 3;
            dic[ChartConfigurationKeys.SERIESNAMES.ToString()] = new List<string> { "Male", "Female", "Children" };

            if (!IsPostBack) 
            {
                string sqlConnectionString = ConfigurationManager.AppSettings[SqlConnectionKey];
                SqlServerConnection sqlCon = new SqlServerConnection(sqlConnectionString);

                DataTable table = sqlCon.RunSql(Constants.GetDieseasesSql);

                DropDownList1.DataSource = table;
                DropDownList1.DataTextField = table.Columns[1].ToString();
                DropDownList1.DataValueField = table.Columns[0].ToString();
                DropDownList1.DataBind();

                DropDownList1.Items.Insert(0, new ListItem(){ Value="ALL", Text="All" });

                table = sqlCon.RunSql(Constants.GetYearsStatSql);

                DropDownList2.DataSource = table;
                DropDownList2.DataTextField = table.Columns[0].ToString();
                DropDownList2.DataValueField = table.Columns[0].ToString();
                DropDownList2.DataBind();

                dic[ChartConfigurationKeys.QUERY.ToString()] = string.Format(ChartDataSql, DropDownList2.Items[0].Value, "ALL");
                dic[ChartConfigurationKeys.TITLE.ToString()] = string.Format(TitleFormat, DropDownList1.Items[0].Text, DropDownList2.Items[0].Value);

                ChartContainer1.ChartConfigurations = dic;
            }

            if (IsPostBack) 
            {
                dic[ChartConfigurationKeys.QUERY.ToString()] = string.Format(ChartDataSql, DropDownList2.SelectedValue, DropDownList1.SelectedValue);
                dic[ChartConfigurationKeys.TITLE.ToString()] = string.Format(TitleFormat, DropDownList1.SelectedItem.Text, DropDownList2.SelectedValue);

                ChartContainer1.ChartConfigurations = dic;
            }
        }

        protected void Button1_Click(object sender, EventArgs e)
        {
            //dic[ChartConfigurationKeys.QUERY.ToString()] = string.Format(ChartDataSql, DropDownList2.SelectedValue, DropDownList1.SelectedValue);

            //ChartContainer1.ChartConfigurations = dic;
        }
    }
}