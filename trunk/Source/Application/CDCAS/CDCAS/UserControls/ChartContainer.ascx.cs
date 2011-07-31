using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using DataHandler;
using System.Data;
using System.Configuration;
using CDCAS.Utilities;
using System.Web.UI.DataVisualization.Charting;
using System.Drawing;

namespace CDCAS.UserControls
{
    public partial class ChartContainer : System.Web.UI.UserControl
    {
        public Dictionary<string, object> ChartConfigurations
        {
            get;
            set;
        }

        protected void Page_Load(object sender, EventArgs e)
        {
            DataTable table = getData();

            if (Chart1 == null) 
            {
                initializeChart();
            }

            bool hasDynamicColumns = ChartConfigurations.ContainsKey(ChartConfigurationKeys.HASDYNAMICCOLUMNS.ToString()) ?
                Convert.ToBoolean(ChartConfigurations[ChartConfigurationKeys.HASDYNAMICCOLUMNS.ToString()]) : false;

            if (hasDynamicColumns)
            {
                List<string> columns = new List<string>();
                foreach (DataColumn col in table.Columns)
                {
                    columns.Add(col.ColumnName);
                }
                columns.Remove(Convert.ToString(ChartConfigurations[ChartConfigurationKeys.XVALARRAY.ToString()]));
                ChartConfigurations[ChartConfigurationKeys.NOOFCHARTSERIES.ToString()] = columns.Count;
                ChartConfigurations[ChartConfigurationKeys.YVALARRAY.ToString()] = columns;
                ChartConfigurations[ChartConfigurationKeys.SERIESNAMES.ToString()] = columns;
            }

            setChartStyles();

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
                ChartConfigurations[ChartConfigurationKeys.TYPE.ToString()].ToString());

            Docking legendDoking = ChartConfigurations.ContainsKey(ChartConfigurationKeys.LEGENDDOKING.ToString()) ? (Docking)Enum.Parse(typeof(Docking),
                ChartConfigurations[ChartConfigurationKeys.LEGENDDOKING.ToString()].ToString()) : Docking.Bottom;

            Chart1.Titles[0].Text = Convert.ToString(ChartConfigurations[ChartConfigurationKeys.TITLE.ToString()]);

            Chart1.ChartAreas[Constants.ChartAreaOneKey].AxisX.Title = Convert.ToString(ChartConfigurations[ChartConfigurationKeys.XTITLE.ToString()]);
            Chart1.ChartAreas[Constants.ChartAreaOneKey].AxisY.Title = Convert.ToString(ChartConfigurations[ChartConfigurationKeys.YTITLE.ToString()]);

            bool disableLabels = ChartConfigurations.ContainsKey(ChartConfigurationKeys.DISABLELABELS.ToString()) ?
                Convert.ToBoolean(ChartConfigurations[ChartConfigurationKeys.DISABLELABELS.ToString()]) : false;

            Chart1.ChartAreas[Constants.ChartAreaOneKey].AxisX.Interval = 1.0;

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
                    Chart1.Legends[Convert.ToString(ChartConfigurations[ChartConfigurationKeys.LEGEND.ToString()])].Docking = legendDoking;
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
                        if (!disableLabels)
                            Chart1.Series[series].Label = Constants.PresentageIndicator;
                        Chart1.Series[series].LegendText = Constants.ValueOfXIndicator;
                    }

                    if (enableLegend)
                    {
                        Chart1.Series[series].Legend = Convert.ToString(ChartConfigurations[ChartConfigurationKeys.LEGEND.ToString()]);
                        if (!disableLabels)
                            Chart1.Series[series].IsValueShownAsLabel = true;
                    }
                    index++;
                }
            }
        }

        private void initializeChart() 
        {
            Chart1 = new Chart();
            Chart1.Palette = ChartColorPalette.BrightPastel;
            Chart1.ImageType = ChartImageType.Png;
            Chart1.BorderlineDashStyle = ChartDashStyle.Solid;
            Chart1.BackSecondaryColor = Color.White;
            Chart1.BackGradientStyle = GradientStyle.TopBottom;
            Chart1.BorderlineWidth = 2;
            Chart1.BackColor = ColorTranslator.FromHtml("#D3DFF0");
            Chart1.BorderlineColor = Color.FromArgb(26, 59, 105);
            Title title = new Title();
            title.Name = "Title1";
            List<FontFamily> fontFamilies = new List<FontFamily>(FontFamily.Families);
            title.Font = new Font(fontFamilies.Find(family => family.Name.Equals("Times New Roman")), 12, FontStyle.Bold);
            Chart1.BorderSkin = new BorderSkin() { SkinStyle = BorderSkinStyle.Emboss };

            Chart1.Titles.Add(title);

            ChartArea area = new ChartArea();
            area.Name = "ChartArea1";
            area.BorderColor = Color.FromArgb(64, 64, 64, 64);
            area.BorderDashStyle = ChartDashStyle.Solid;
            area.BackSecondaryColor = Color.White;
            area.BackColor = Color.FromArgb(64, 165, 191, 228);
            area.ShadowColor = Color.Transparent;
            area.BackGradientStyle = GradientStyle.TopBottom;
            area.AxisY.LineColor = Color.FromArgb(64, 64, 64, 64);
            area.AxisY.LabelStyle.Font = new Font(fontFamilies.Find(family => family.Name.Equals("Trebuchet MS")), 8.25f, FontStyle.Bold);
            area.AxisY.MajorGrid.LineColor = Color.FromArgb(64, 64, 64, 64);
            area.AxisX.LineColor = Color.FromArgb(64, 64, 64, 64);
            area.AxisX.LabelStyle.Font = new Font(fontFamilies.Find(family => family.Name.Equals("Trebuchet MS")), 8.25f, FontStyle.Bold);
            area.AxisX.MajorGrid.LineColor = Color.FromArgb(64, 64, 64, 64);

            Chart1.ChartAreas.Add(area);
        }
    }
}