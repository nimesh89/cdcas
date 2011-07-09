using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using MapDataHandler;
using System.Configuration;
using System.Data;
using DataHandler;
using System.Net;

namespace CDCAS
{
    public partial class Map : System.Web.UI.Page
    {
        private const string PgConnectionKey = "PgConnectionString";
        private const string SqlConnectionKey = "SqlConnectionString";
        private const string MapServiceUrlKey = "MapServiceUrl";
        private const string DieseaseCodeParamKey = "DESCODE";
        private const string DateParamKey = "DATE";
        private const string ParamSeperator = "&";
        private const string ParamSetValue = "=";
        private const string RangeIndicator = "-";
        private const string YearDieseaseSeperator = "  -   ";
        private const string DatePostFix = "-01-01";
        private const string ExportToExcelLinkFormat = "~/ExportToExcel.aspx?diesease={0}&code={1}&year={2}";  
        private const string GetYearsSql = "select distinct date_part('year', \"Date\") as year from \"MapData\" order by year desc";
        private const string GetMostSuitableDieseaseCodeSql = "select Distinct \"DiseaseCode\", pcount from fullcountview where date_part('year', \"Date\") = {0} and \"DiseaseCode\" is not null order by pcount desc limit 1";
        private const string GetDieseasesSql = "select * from tblDieseases";
        private const string GetMaxValueSql = "select pcount from {0} order by pcount desc limit 1  ";
        private const string GetMinValueSql = "select pcount from {0} order by pcount limit 1  ";
        private const string GetLegendTableSql = "select gid as \"Id\", divisec as \"Name\", pcount as \"Patients\" from {0} order by pcount desc";

        protected void Page_Load(object sender, EventArgs e)
        {

            if (!IsPostBack) 
            {
                string pgConnectionString = ConfigurationManager.AppSettings[PgConnectionKey];
                PgConnection pgCon = new PgConnection(pgConnectionString);

                DataTable table = pgCon.RunSql(GetYearsSql);

                DropDownList2.DataSource = table;
                DropDownList2.DataTextField = table.Columns[0].ToString();
                DropDownList2.DataValueField = table.Columns[0].ToString();
                DropDownList2.DataBind();

                string sqlConnectionString = ConfigurationManager.AppSettings[SqlConnectionKey];
                SqlServerConnection sqlCon = new SqlServerConnection(sqlConnectionString);

                table = sqlCon.RunSql(GetDieseasesSql);

                DropDownList1.DataSource = table;
                DropDownList1.DataTextField = table.Columns[1].ToString();
                DropDownList1.DataValueField = table.Columns[0].ToString();
                DropDownList1.DataBind();

                selectMostSuitableDiesease();
            }
            
        }

        private void selectMostSuitableDiesease() 
        {
            string pgConnectionString = ConfigurationManager.AppSettings[PgConnectionKey];
            PgConnection pgCon = new PgConnection(pgConnectionString);

            string year = DateTime.Now.Year.ToString();

            DataTable table = pgCon.RunSql(string.Format(GetMostSuitableDieseaseCodeSql, year));

            string dieseaseCode = table.Rows[0][0].ToString();

            DropDownList1.ClearSelection();
            DropDownList1.Items.FindByValue(dieseaseCode).Selected = true;

            NameLabel.Text = string.Concat(year, YearDieseaseSeperator, DropDownList1.SelectedItem.Text);

            DropDownList2.ClearSelection();
            DropDownList2.Items.FindByValue(year).Selected = true;

            HyperLink1.NavigateUrl = string.Format(ExportToExcelLinkFormat, DropDownList1.SelectedItem.Text.Trim(), DropDownList1.SelectedValue, year);

            string viewName = string.Concat(dieseaseCode.ToLower(), year);

            setMapOptions(dieseaseCode, year, false);
            setLeggendColorDescriptions(viewName);
            getDetailedLegend(viewName);
        }

        private void setMapOptions(string dieseaseCode, string year, bool isNew) 
        {
            string mapServiceUrl = ConfigurationManager.AppSettings[MapServiceUrlKey];
            string fullURL = string.Concat(mapServiceUrl,ParamSeperator, DieseaseCodeParamKey, ParamSetValue, dieseaseCode,ParamSeperator, DateParamKey, ParamSetValue, year,DatePostFix);
            if(isNew)
                WebRequest.Create(fullURL).GetResponse();
            Image1.ImageUrl = fullURL;
        }


        private void setLeggendColorDescriptions(string viewName) 
        {
            string pgConnectionString = ConfigurationManager.AppSettings[PgConnectionKey];
            PgConnection pgCon = new PgConnection(pgConnectionString);

            DataTable table = pgCon.RunSql(string.Format(GetMinValueSql, viewName));

            int min = Convert.ToInt32(table.Rows[0][0].ToString());

            table = pgCon.RunSql(string.Format(GetMaxValueSql, viewName));

            int max = Convert.ToInt32(table.Rows[0][0].ToString());

            int q1 = (max - min) / 3 + min;
            int q2 = max - (max - min) / 3;

            string criticalRange = string.Concat(max, RangeIndicator, q2);
            string highRange = string.Concat(q2-1, RangeIndicator, q1);
            string moderateRange = string.Concat(q1-1, RangeIndicator, min);

            CriticalLegendItem.Description = criticalRange;
            HighLegendItem.Description = highRange;
            ModerateLegendItem.Description = moderateRange;
        }

        private void getDetailedLegend(string viewName) 
        {
            string pgConnectionString = ConfigurationManager.AppSettings[PgConnectionKey];
            PgConnection pgCon = new PgConnection(pgConnectionString);

            DataTable table = pgCon.RunSql(string.Format(GetLegendTableSql, viewName));
            LegendGridView.DataSource = table;
            LegendGridView.DataBind();
        }

        protected void Button1_Click(object sender, EventArgs e)
        {
            string dieseaseCode = DropDownList1.SelectedValue;
            string year = DropDownList2.SelectedValue;

            NameLabel.Text = string.Concat(year, YearDieseaseSeperator, DropDownList1.SelectedItem.Text);

            string viewName = string.Concat(dieseaseCode.ToLower(), year);

            HyperLink1.NavigateUrl = string.Format(ExportToExcelLinkFormat, DropDownList1.SelectedItem.Text.Trim(), DropDownList1.SelectedValue, year);

            setMapOptions(dieseaseCode, year, true);
            setLeggendColorDescriptions(viewName);
            getDetailedLegend(viewName);
        }
    }
}