<%@ Page Title="" Language="C#" MasterPageFile="~/NewMaster.Master" AutoEventWireup="true"
    CodeBehind="Stats.aspx.cs" Inherits="CDCAS.Stats" %>

<%@ Register Assembly="System.Web.DataVisualization, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35"
    Namespace="System.Web.UI.DataVisualization.Charting" TagPrefix="asp" %>
<%@ Register Src="UserControls/ChartContainer.ascx" TagName="ChartContainer" TagPrefix="uc1" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <style type="text/css">
        .subheder1
        {
            font-size: 22px;
            font-weight: bold;
            width: 960px;
            text-align: center;
            margin: 10px auto 5px auto;
            background-color: #329efc;
            color: rgb(5,46,114);
        }
    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="page-div-container">
        <div class="page-div-title">
            Charts
        </div>
        <div class="subheder1">
            Semi Dynamic Charts
        </div>
        <div class="stat-div-chart">
            Diesease:&nbsp<asp:DropDownList ID="DropDownList1" runat="server">
            </asp:DropDownList>
            <br />
            Year:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<asp:DropDownList ID="DropDownList2"
                runat="server">
            </asp:DropDownList>
            <br />
            <asp:Button ID="Button1" runat="server" Text="Generate" OnClick="Button1_Click" />
        </div>
        <div class="div-barricades">
        </div>
        <div style="margin-left: 18px; margin-top: 5px;">
            <uc1:ChartContainer ID="ChartContainer1" runat="server" />
        </div>
    </div>
    <div class="subheder1">
        Static Charts
    </div>
    <uc1:ChartContainer ID="ChartContainer2" runat="server" />
    <br />
    <uc1:ChartContainer ID="ChartContainer3" runat="server" />
    <br />
</asp:Content>
