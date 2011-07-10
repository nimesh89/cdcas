<%@ Page Title="" Language="C#" MasterPageFile="~/CDCAS.Master" AutoEventWireup="true" CodeBehind="Home.aspx.cs" Inherits="CDCAS.Home" %>
<%@ Register src="UserControls/ChartContainer.ascx" tagname="ChartContainer" tagprefix="uc1" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">

    <uc1:ChartContainer ID="ChartContainer1" runat="server" />
    <br />
    <uc1:ChartContainer ID="ChartContainer2" runat="server" />

</asp:Content>
