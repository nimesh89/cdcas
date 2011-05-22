<%@ Page Title="" Language="C#" MasterPageFile="~/CDCAS.Master" AutoEventWireup="true"
    CodeBehind="Map.aspx.cs" Inherits="CDCAS.Map" %>

<%@ Register Src="UserControls/LegendItem.ascx" TagName="LegendItem" TagPrefix="uc1" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <script src="Scripts/jquery-1.5.2.min.js" type="text/javascript"></script>
    <script src="Scripts/jquery-ui-1.8.12.custom.min.js" type="text/javascript"></script>
    <link href="styles/jquery-ui-1.8.12.custom.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript">
        $(document).ready
        (
            function () {
                $(".map-date").datepicker(); 
            }
        )
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="page-div-container">
        <div class="page-div-title">
            Map
        </div>
        <div class="div-barricades">
        </div>
        <div id="map-div-imageholder">
            <asp:Image ID="Image1" AlternateText=" " ImageUrl="~/images/asd.jpg" runat="server" />
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
                                <asp:ListItem Value="" Text="Dengi"></asp:ListItem>
                                <asp:ListItem Value="" Text="Cholara"></asp:ListItem>
                                <asp:ListItem Value="" Text="ChickenPox"></asp:ListItem>
                            </asp:DropDownList>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            From
                        </td>
                        <td>
                            <asp:TextBox ID="TextBox1" CssClass="map-date" runat="server"></asp:TextBox>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            To
                        </td>
                        <td>
                            <asp:TextBox ID="TextBox2" CssClass="map-date" runat="server"></asp:TextBox>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="map-td-button">
                            <asp:Button ID="Button1" runat="server" Text="Generate" />
                        </td>
                    </tr>
                </table>
            </fieldset>
        </div>
        <div id="map-div-legend">
            <fieldset class="map-fieldset-controls">
                <legend>Legend</legend>
                <uc1:LegendItem ID="LegendItem1" Color="Yellow" Description="Affected Areas" runat="server" />
            </fieldset>
        </div>
        <div class="div-barricades">
        </div>
    </div>
</asp:Content>
