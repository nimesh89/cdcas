using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.UI.DataVisualization.Charting;
using System.Drawing;
using DataHandler;
using System.Configuration;
using System.Data;
using CDCAS.Utilities;

namespace CDCAS
{
    public partial class test : System.Web.UI.Page
    {
        public Dictionary<string, object> ChartConfigurations
        {
            get;
            set;
        }

        protected void Page_Load(object sender, EventArgs e)
        {
            Dictionary<string, object> dic = new Dictionary<string, object>();
            dic[ChartConfigurationKeys.QUERY.ToString()] = string.Format(Constants.GetDieseaseVsPcountForCurrentYear, DateTime.Now.Year);
            dic[ChartConfigurationKeys.HEIGHT.ToString()] = 500;
            dic[ChartConfigurationKeys.WIDTH.ToString()] = 700;
            dic[ChartConfigurationKeys.XVALARRAY.ToString()] = "NAME";
            dic[ChartConfigurationKeys.YVALARRAY.ToString()] = new List<string> { "PCOUNT" };
            dic[ChartConfigurationKeys.XTITLE.ToString()] = "Diesease";
            dic[ChartConfigurationKeys.YTITLE.ToString()] = "Patients";
            dic[ChartConfigurationKeys.TYPE.ToString()] = SeriesChartType.Pie;
            dic[ChartConfigurationKeys.LEGEND.ToString()] = "Diesease";
            dic[ChartConfigurationKeys.TITLE.ToString()] = "Dieseases Vs. Patients\n2011";
            dic[ChartConfigurationKeys.VIEWLEGEND.ToString()] = true;
            dic[ChartConfigurationKeys.PIELABELSTYLE.ToString()] = "Outside";
            dic[ChartConfigurationKeys.NOOFCHARTSERIES.ToString()] = 1;
            dic[ChartConfigurationKeys.SERIESNAMES.ToString()] = new List<string> { "DieseasesVsPatients" };

            //Dictionary<string, object> dic = new Dictionary<string, object>();
            //dic[ChartConfigurationKeys.QUERY.ToString()] = "EXEC dbo.spPatientsBreakDownAgeAndYear";
            //dic[ChartConfigurationKeys.HEIGHT.ToString()] = 356;
            //dic[ChartConfigurationKeys.WIDTH.ToString()] = 600;
            //dic[ChartConfigurationKeys.XVALARRAY.ToString()] = "YEAR";
            //dic[ChartConfigurationKeys.YVALARRAY.ToString()] = new List<string> { "MALECOUNT", "FEMALECOUNT", "CHILDCOUNT" };
            //dic[ChartConfigurationKeys.XTITLE.ToString()] = "Year";
            //dic[ChartConfigurationKeys.YTITLE.ToString()] = "Patients";
            //dic[ChartConfigurationKeys.TYPE.ToString()] = SeriesChartType.Column;
            //dic[ChartConfigurationKeys.LEGEND.ToString()] = "Gender";
            //dic[ChartConfigurationKeys.TITLE.ToString()] = "Year Vs. Patients\n2011";
            //dic[ChartConfigurationKeys.VIEWLEGEND.ToString()] = true;
            //dic[ChartConfigurationKeys.PIELABELSTYLE.ToString()] = "Inside";
            //dic[ChartConfigurationKeys.NOOFCHARTSERIES.ToString()] = 3;
            //dic[ChartConfigurationKeys.SERIESNAMES.ToString()] = new List<string> { "Male", "Female", "Children" };

            ChartConfigurations = dic;
            setChartStyles();

            DataTable table = getData();

            Chart1.DataSource = table;
            Chart1.DataBind();
        }

        private DataTable getData()
        {
            string sqlConnectionString = ConfigurationManager.AppSettings[Constants.SqlConnectionKey];
            SqlServerConnection sqlCon = new SqlServerConnection(sqlConnectionString);

            DataTable table = sqlCon.RunSql(Convert.ToString(ChartConfigurations[ChartConfigurationKeys.QUERY.ToString()]));

            return table;
        }

        private void setChartStyles()
        {
            Chart1.Height = Convert.ToInt32(ChartConfigurations[ChartConfigurationKeys.HEIGHT.ToString()]);
            Chart1.Width = Convert.ToInt32(ChartConfigurations[ChartConfigurationKeys.WIDTH.ToString()]);
            SeriesChartType type = (SeriesChartType)Enum.Parse(typeof(SeriesChartType),
                Convert.ToInt32(ChartConfigurations[ChartConfigurationKeys.TYPE.ToString()]).ToString());

            Chart1.Titles[0].Text = Convert.ToString(ChartConfigurations[ChartConfigurationKeys.TITLE.ToString()]);

            Chart1.ChartAreas[Constants.ChartAreaOneKey].AxisX.Title = Convert.ToString(ChartConfigurations[ChartConfigurationKeys.XTITLE.ToString()]);
            Chart1.ChartAreas[Constants.ChartAreaOneKey].AxisY.Title = Convert.ToString(ChartConfigurations[ChartConfigurationKeys.YTITLE.ToString()]);


            object noOfChartSeries = ChartConfigurations.ContainsKey(ChartConfigurationKeys.NOOFCHARTSERIES.ToString()) ? ChartConfigurations[ChartConfigurationKeys.NOOFCHARTSERIES.ToString()] : null;
            int no = noOfChartSeries != null ? Convert.ToInt32(noOfChartSeries) : 0;

            if (no > 0)
            {
                List<string> seriesNameList = (List<string>)ChartConfigurations[ChartConfigurationKeys.SERIESNAMES.ToString()];
                List<string> yvalues = (List<string>)ChartConfigurations[ChartConfigurationKeys.YVALARRAY.ToString()];

                bool enableLegend = Convert.ToBoolean(ChartConfigurations[ChartConfigurationKeys.VIEWLEGEND.ToString()]);
                if (enableLegend)
                {
                    Chart1.Legends.Add(new Legend(Convert.ToString(ChartConfigurations[ChartConfigurationKeys.LEGEND.ToString()])));
                    Chart1.Legends[Convert.ToString(ChartConfigurations[ChartConfigurationKeys.LEGEND.ToString()])].Title = Convert.ToString(ChartConfigurations[ChartConfigurationKeys.LEGEND.ToString()]);
                }

                int index = 0;
                foreach (string series in seriesNameList)
                {
                    Chart1.Series.Add(new Series(series));

                    Chart1.Series[series].YValueMembers = yvalues[index];

                    Chart1.Series[series].XValueMember = Convert.ToString(ChartConfigurations[ChartConfigurationKeys.XVALARRAY.ToString()]);

                    Chart1.Series[series].ChartType = type;


                    if (Chart1.Series[series].ChartType.Equals(SeriesChartType.Pie))
                    {
                        Chart1.Series[series][Constants.PieLabelStyleKey] = Convert.ToString(ChartConfigurations[ChartConfigurationKeys.PIELABELSTYLE.ToString()]);
                        Chart1.ChartAreas[Constants.ChartAreaOneKey].Area3DStyle.Enable3D = true;
                        Chart1.Series[series].Label = Constants.PresentageIndicator;
                        Chart1.Series[series].LegendText = Constants.ValueOfXIndicator;
                    }

                    if (enableLegend)
                    {
                        Chart1.Series[series].Legend = Convert.ToString(ChartConfigurations[ChartConfigurationKeys.LEGEND.ToString()]);
                        Chart1.Series[series].IsValueShownAsLabel = true;
                    }
                    index++;
                }
            }
        }
    }
}