using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace CDCAS
{
    public partial class NewMaster : System.Web.UI.MasterPage
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                int no = -1;
                int.TryParse(Request.QueryString["no"], out no);
                if (no == -1 || no > 10) return;
                Menu1.Items[no].Selected = true;
            }
        }
    }
}