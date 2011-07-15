<%@ Page Title="" Language="C#" MasterPageFile="~/CDCAS.Master" AutoEventWireup="true"
    CodeBehind="Map.aspx.cs" Inherits="CDCAS.Map" %>

<%@ Register Src="UserControls/LegendItem.ascx" TagName="LegendItem" TagPrefix="uc1" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <script src="Scripts/jquery-1.5.2.min.js" type="text/javascript"></script>
    <script src="Scripts/jquery-ui-1.8.12.custom.min.js" type="text/javascript"></script>
    <link href="styles/jquery-ui-1.8.12.custom.css" rel="stylesheet" type="text/css" />
    <script src="Scripts/jquery.maphilight.min.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(document).ready
        (
            function () {
                $(".map-date").datepicker();
                $('.map-div-imageholder-image').maphilight();
            }
        )
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="page-div-container">
        <div class="page-div-title">
            Map&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<asp:Label ID="NameLabel" runat="server" Text=""></asp:Label>
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
            <fieldset class="map-fieldset-controls">
                <legend>Map Controls </legend>
                <table>
                    <tr>
                        <td>
                            Disease
                        </td>
                        <td>
                            <asp:DropDownList ID="DropDownList1" runat="server">
                            </asp:DropDownList>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Year
                        </td>
                        <td>
                            <asp:DropDownList ID="DropDownList2" runat="server">
                            </asp:DropDownList>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="map-td-button">
                            <asp:Button ID="Button1" runat="server" Text="Generate" OnClick="Button1_Click" />
                        </td>
                    </tr>
                </table>
            </fieldset>
        </div>
        <div id="map-div-legend">
            <fieldset class="map-fieldset-controls">
                <legend>Legend</legend>
                <uc1:LegendItem ID="CriticalLegendItem" Color="Red" runat="server" />
                <uc1:LegendItem ID="HighLegendItem" Color="Orange" runat="server" />
                <uc1:LegendItem ID="ModerateLegendItem" Color="Yellow" runat="server" />
                <br />
                <asp:GridView ID="LegendGridView" runat="server">
                </asp:GridView>
                <br />
                <asp:HyperLink ID="HyperLink1" Target="_blank" runat="server">Export To Excel</asp:HyperLink>
            </fieldset>
        </div>
        <div class="div-barricades">
        </div>
    </div>
</asp:Content>
