using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Configuration;
using CDCAS.Utilities;
using System.Net;
using System.IO;
using System.Text;

namespace CDCAS
{
    public partial class DrillDownMap : System.Web.UI.Page
    {
        public string ImageMapHtml { get; set; }
        private const string MapOptions = "&MAPTYPE=DRILDOWN&MAPLEVEL={1}&FILTERVALUES={2}&FILTERVALUES={3}";
        private const string ProvinceLevel = "PROVINCE";
        private const string DistricNameLabelFormat = "{0} Province ";
        private const string DSNameLabelFormat = "{0} Province {1} Distric ";

        protected void Page_Load(object sender, EventArgs e)
        {
            string mapLevel = Request.Params["MAPLEVEL"];

            Image1.Attributes.Add(Constants.UseMapKey, Constants.UseMapDistricSecretaryValue);
            if (!IsPostBack) 
            {
                if(string.IsNullOrEmpty(mapLevel))
                {
                    NameLabel.Text = "SriLanka ";

                    string mapServiceUrl = ConfigurationManager.AppSettings[Constants.MapServiceUrlKey];
                    string fullURL = string.Concat(mapServiceUrl, MapOptions);
                    fullURL = string.Format(fullURL, Constants.ProvinceMap, ProvinceLevel, 1, 1);
                    OverviewMapImage.ImageUrl = fullURL;

                    mapServiceUrl = ConfigurationManager.AppSettings[Constants.DrilldownImageKey];
                    fullURL = string.Format(fullURL, Constants.ProvinceMap, ProvinceLevel, 1);
                    Image1.ImageUrl = fullURL;

                    setAreaMap("bla", Constants.ProvinceMap, Constants.ProvinceType, 1);
                }
                else if(Constants.DistricType.Equals(mapLevel))
                {
                    string province = Request.Params["province_c"];
                    string provinceName = Request.Params["name"];

                    NameLabel.Text = string.Format(DistricNameLabelFormat, provinceName);

                    string mapServiceUrl = ConfigurationManager.AppSettings[Constants.MapServiceUrlKey];
                    string fullURL = string.Concat(mapServiceUrl, MapOptions);
                    fullURL = string.Format(fullURL, Constants.DistricMap, Constants.DistricType, Convert.ToInt32(province), 1);
                    OverviewMapImage.ImageUrl = fullURL;

                    mapServiceUrl = ConfigurationManager.AppSettings[Constants.DrilldownImageKey];
                    fullURL = string.Format(mapServiceUrl, Constants.DistricMap, Constants.DistricType, Convert.ToInt32(province));
                    Image1.ImageUrl = fullURL;

                    setAreaMap("bla", Constants.DistricMap, Constants.DistricType, Convert.ToInt32(province));
                }
                else if (Constants.DsType.Equals(mapLevel))
                {
                    string province = Request.Params["province_c"];
                    string dcode = Request.Params["dcode"];
                    string provinceName = Request.Params["province_n"];
                    string districName = Request.Params["name"];

                    NameLabel.Text = string.Format(DSNameLabelFormat, provinceName, districName);

                    string mapServiceUrl = ConfigurationManager.AppSettings[Constants.MapServiceUrlKey];
                    string fullURL = string.Concat(mapServiceUrl, MapOptions);
                    fullURL = string.Format(fullURL, Constants.DSMap, Constants.DsType, Convert.ToInt32(province), Convert.ToInt32(dcode));
                    OverviewMapImage.ImageUrl = fullURL;

                    mapServiceUrl = ConfigurationManager.AppSettings[Constants.DrilldownImageKey];
                    fullURL = string.Format(mapServiceUrl, Constants.DSMap, Constants.DsType, Convert.ToInt32(dcode));
                    Image1.ImageUrl = fullURL;

                    setAreaMap("bla", Constants.DSMap, Constants.DsType, Convert.ToInt32(dcode));
                }
            }
        }

        private void setAreaMap(string viewName, string baseMap, string type, int filterValue) 
        {
            string htmlImageMapServiceUrl = ConfigurationManager.AppSettings[Constants.HtmlImageMapServiceKey];
            string fullUrl = string.Format(htmlImageMapServiceUrl, viewName, baseMap, type, filterValue);
            HttpWebResponse response = (HttpWebResponse)((HttpWebRequest)HttpWebRequest.Create(fullUrl)).GetResponse();

            Stream resStream = response.GetResponseStream();

            StringBuilder build = new StringBuilder();

            byte[] buf = new byte[8192];

            string tempString = null;
            int count = 0;

            do
            {
                // fill the buffer with data
                count = resStream.Read(buf, 0, buf.Length);

                // make sure we read some data
                if (count != 0)
                {
                    // translate from bytes to ASCII text
                    tempString = Encoding.ASCII.GetString(buf, 0, count);

                    // continue building the string
                    build.Append(tempString);
                }
            }
            while (count > 0); // any more data to read?

            ImageMapHtml = build.ToString();
        }
    }
}