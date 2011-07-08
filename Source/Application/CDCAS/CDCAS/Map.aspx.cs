using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using MapDataHandler;
using System.Configuration;
using System.Data;

namespace CDCAS
{
    public partial class Map : System.Web.UI.Page
    {
        private const string PgConnectionKey = "PgConnectionString";
        private const string GetYearsSql = "select distinct date_part('year', \"Date\") as year from \"MapData\" order by year desc";

        protected void Page_Load(object sender, EventArgs e)
        {

            string connectionString = ConfigurationManager.AppSettings[PgConnectionKey];
            PgConnection con = new PgConnection(connectionString);

            DataTable table = con.RunSql(GetYearsSql);

            DropDownList2.DataSource = table;
            DropDownList2.DataTextField = table.Columns[0].ToString();
            DropDownList2.DataValueField = table.Columns[0].ToString();
            DropDownList2.DataBind();
            
        }
    }
}