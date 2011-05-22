<%@ Page Title="" Language="C#" MasterPageFile="~/CDCAS.Master" AutoEventWireup="true"
    CodeBehind="Stats.aspx.cs" Inherits="CDCAS.Stats" %>

<%@ Register Assembly="System.Web.DataVisualization, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35"
    Namespace="System.Web.UI.DataVisualization.Charting" TagPrefix="asp" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="page-div-container">
        <div class="page-div-title">
            Stats
        </div>
        <div class="stat-div-chart">
            <asp:Chart ID="Chart1" runat="server">
                <Series>
                    <asp:Series Name="Patients" YValueType="Int32" ChartType="Bar" ChartArea="ChartArea1">
                        <Points>
                            <asp:DataPoint AxisLabel="Matara" YValues="23" />
                            <asp:DataPoint AxisLabel="Colombo" YValues="57" />
                            <asp:DataPoint AxisLabel="Badulla" YValues="10" />
                        </Points>
                    </asp:Series>
                </Series>
                <ChartAreas>
                    <asp:ChartArea Name="ChartArea1">
                    </asp:ChartArea>
                </ChartAreas>
            </asp:Chart>
        </div>
        <div class="div-barricades"></div>
    </div>
</asp:Content>
