<%@ Page Title="" Language="C#" MasterPageFile="~/NewMaster.Master" AutoEventWireup="true" CodeBehind="WebForm2.aspx.cs" Inherits="CDCAS.WebForm2" %>
<%@ Register src="UserControls/ChartContainer.ascx" tagname="ChartContainer" tagprefix="uc1" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">

    <uc1:ChartContainer ID="ChartContainer1" runat="server" />

</asp:Content>
