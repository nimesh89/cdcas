<%@ Page Title="" Language="C#" MasterPageFile="~/CDCAS.Master" AutoEventWireup="true" CodeBehind="DrillDownMap.aspx.cs" Inherits="CDCAS.DrillDownMap" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <script src="Scripts/jquery-1.5.2.min.js" type="text/javascript"></script>
    <script src="Scripts/jquery-ui-1.8.12.custom.min.js" type="text/javascript"></script>
    <link href="styles/jquery-ui-1.8.12.custom.css" rel="stylesheet" type="text/css" />
    <script src="Scripts/jquery.maphilight.min.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(document).ready
        (
            function () {
                $('.map-div-imageholder-image').maphilight();
            }
        )
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="page-div-container">
        <div class="page-div-title">
            <asp:Label ID="NameLabel" runat="server" Text=""></asp:Label>Map
        </div>
        <div class="div-barricades">
        </div>
        <div id="map-div-imageholder">
            <asp:Image ID="Image1" AlternateText=" " Width="600px" Height="650px" CssClass="map-div-imageholder-image"
                runat="server" />
            <map id="districSecretaryMap" name="districSecretaryMap">
                <%=ImageMapHtml %>
            </map>
        </div>
        <div id="map-div-controls">
            <asp:Image ID="OverviewMapImage" AlternateText=" " Width="250px" Height="280px"
                runat="server" />
        </div>
        <div class="div-barricades">
        </div>
    </div>
</asp:Content>
