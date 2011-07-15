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
using CDCAS.Utilities;

namespace CDCAS
{
    public partial class Map : System.Web.UI.Page
    {
        private  string PgConnectionKey                      =   Constants.PgConnectionKey;                
        private  string SqlConnectionKey                     =   Constants.SqlConnectionKey;               
        private  string MapServiceUrlKey                     =   Constants.MapServiceUrlKey;               
        private  string DieseaseCodeParamKey                 =   Constants.DieseaseCodeParamKey;           
        private  string DateParamKey                         =   Constants.DateParamKey;                   
        private  string ParamSeperator                       =   Constants.ParamSeperator;                 
        private  string ParamSetValue                        =   Constants.ParamSetValue;                  
        private  string RangeIndicator                       =   Constants.RangeIndicator;                 
        private  string YearDieseaseSeperator                =   Constants.YearDieseaseSeperator;          
        private  string DatePostFix                          =   Constants.DatePostFix;                    
        private  string ExportToExcelLinkFormat              =   Constants.ExportToExcelLinkFormat;        
        private  string GetYearsSql                          =   Constants.GetYearsSql;                    
        private  string GetMostSuitableDieseaseCodeSql       =   Constants.GetMostSuitableDieseaseCodeSql; 
        private  string GetDieseasesSql                      =   Constants.GetDieseasesSql;                
        private  string GetMaxValueSql                       =   Constants.GetMaxValueSql;                 
        private  string GetMinValueSql                       =   Constants.GetMinValueSql;
        private string GetLegendTableSql                     =   Constants.GetLegendTableSql;              
                                                                
        protected void Page_Load(object sender, EventArgs e)
        {
            Image1.Attributes.Add(Constants.UseMapKey, Constants.UseMapDistricSecretaryValue);

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

            int min = 0, max = 0;

            if (table.Rows.Count > 0) 
            {
                min = Convert.ToInt32(table.Rows[0][0].ToString());
            }

            table = pgCon.RunSql(string.Format(GetMaxValueSql, viewName));


            if (table.Rows.Count > 0)
            {
                max = Convert.ToInt32(table.Rows[0][0].ToString());
            }
            

            int q1 = (max - min) / 3 + min;
            int q2 = max - (max - min) / 3;

            string criticalRange = string.Concat(max, RangeIndicator, q2);
            string highRange = string.Concat(q2>0?q2-1:q2, RangeIndicator, q1);
            string moderateRange = string.Concat(q1>0?q1-1:q1, RangeIndicator, min);

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