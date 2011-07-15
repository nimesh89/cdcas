using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace CDCAS.UserControls
{
    public partial class UserRequest : System.Web.UI.UserControl
    {
        public string UserName { get; set; }

        protected void Page_Load(object sender, EventArgs e)
        {
            Label1.Text = UserName;
        }
    }
}