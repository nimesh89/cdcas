using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;

namespace CDCAS.Utilities
{
    public class ContainerPage: Page
    {
        override public void VerifyRenderingInServerForm(Control control) 
        {
            return;
        }
    }
}