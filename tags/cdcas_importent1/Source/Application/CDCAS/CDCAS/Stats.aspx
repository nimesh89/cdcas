<%@ Page Title="" Language="C#" MasterPageFile="~/CDCAS.Master" AutoEventWireup="true"
    CodeBehind="Stats.aspx.cs" Inherits="CDCAS.Stats" %>

<%@ Register Assembly="System.Web.DataVisualization, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35"
    Namespace="System.Web.UI.DataVisualization.Charting" TagPrefix="asp" %>
<%@ Register src="UserControls/ChartContainer.ascx" tagname="ChartContainer" tagprefix="uc1" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="page-div-container">
        <div class="page-div-title">
            Stats
        </div>
        <div class="stat-div-chart">
            Diesease:&nbsp<asp:DropDownList ID="DropDownList1" runat="server">
            </asp:DropDownList>
            <br />
            Year:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<asp:DropDownList ID="DropDownList2" runat="server">
            </asp:DropDownList>
            <br />
            <asp:Button ID="Button1" runat="server" Text="Generate" 
                onclick="Button1_Click" />
        </div>
        <div class="div-barricades"></div>
        
        <uc1:ChartContainer ID="ChartContainer1" runat="server" />
    </div>
</asp:Content>
