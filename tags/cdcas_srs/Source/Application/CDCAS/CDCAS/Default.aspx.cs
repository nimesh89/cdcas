using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace CDCAS
{
    public partial class Default : System.Web.UI.Page
    {
        public const string HomePageURL = "~/Home.aspx";

        protected void Page_Load(object sender, EventArgs e)
        {
            Response.Redirect(HomePageURL);
        }
    }
}