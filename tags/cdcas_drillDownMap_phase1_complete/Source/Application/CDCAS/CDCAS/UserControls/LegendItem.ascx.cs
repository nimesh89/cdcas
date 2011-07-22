using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace CDCAS.UserControls
{
    public partial class LegendItem : System.Web.UI.UserControl
    {
        public string Color { get; set; }

        public string Description { set { Label1.Text = value; } }

        protected void Page_Load(object sender, EventArgs e)
        {
            colorbox.Style.Add(HtmlTextWriterStyle.BackgroundColor, Color);
        }
    }
}