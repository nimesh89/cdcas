using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using MapDataHandler;
using System.Configuration;
using System.Data;
using System.IO;
using System.Text;

namespace CDCAS
{
    public partial class ExportToExcel : System.Web.UI.Page
    {
        private const string GetDataSql = "SELECT gid as \"Id\", dist as \"District\", divisec as \"DistricSecretary\" , \"MaleCount\", \"FemaleCount\", \"ChildCount\", \"Date\" FROM datafeatureview WHERE 	date_part('year', \"Date\") = {0} AND \"DiseaseCode\" = '{1}' ORDER BY \"Id\"";
        private const string YearParameKey = "year";
        private const string DieseaseParameKey = "diesease";
        private const string DieseaseCodeParameKey = "code";
        private const string PgConnectionKey = "PgConnectionString";
        private const string FileExtension = ".xls";

        protected void Page_Load(object sender, EventArgs e)
        {
            string year = Request.Params[YearParameKey];
            string diesease = Request.Params[DieseaseParameKey];

            string code = Request.Params[DieseaseCodeParameKey];
            string sql = null;

            if (!string.IsNullOrEmpty(year))
            {
                sql = string.Format(GetDataSql, year, code);
            }

            string pgConnectionString = ConfigurationManager.AppSettings[PgConnectionKey];
            PgConnection pgCon = new PgConnection(pgConnectionString);
            DataTable table = null;

            if (!string.IsNullOrEmpty(sql))
            {
                table = pgCon.RunSql(sql);
            }


            if (table != null)
            {
                DataGrid grid = new DataGrid();
                grid.HeaderStyle.Font.Bold = true;
                grid.DataSource = table;
                grid.DataMember = table.TableName;

                grid.DataBind();

                // render the DataGrid control to a file
                StringBuilder builder = new StringBuilder();

                using (StringWriter writer = new StringWriter(builder))
                {
                    using (HtmlTextWriter hw = new HtmlTextWriter(writer))
                    {
                        grid.RenderControl(hw);
                    }
                }

                Response.AddHeader("content-disposition", String.Format( "attachment;filename={0}", string.Concat(diesease,year,FileExtension)));
                Response.ContentEncoding = Encoding.UTF8;

                Response.Cache.SetCacheability(HttpCacheability.Private);
                Response.ContentType = "application/vnd.ms-excel";

                Response.Write(builder.ToString());
                Response.Flush();
                Response.Close();
            }
        }
    }
}