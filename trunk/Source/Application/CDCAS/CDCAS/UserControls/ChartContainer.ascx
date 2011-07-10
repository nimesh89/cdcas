<%@ Control Language="C#" AutoEventWireup="true" CodeBehind="ChartContainer.ascx.cs" Inherits="CDCAS.UserControls.ChartContainer" %>
<%@ Register Assembly="System.Web.DataVisualization, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35"
    Namespace="System.Web.UI.DataVisualization.Charting" TagPrefix="asp" %>
<div>
    <asp:Chart ID="Chart1" runat="server" Palette="BrightPastel"
            ImageType="Png" BorderlineDashStyle="Solid" BackSecondaryColor="White" BackGradientStyle="TopBottom"
            BorderWidth="2" BackColor="#D3DFF0" BorderColor="26, 59, 105">
            <Titles>
                <asp:Title Name="Title1" Font="Times New Roman, 12, style=Bold" ForeColor="Blue"></asp:Title>
            </Titles>
            <borderskin skinstyle="Emboss"></borderskin>
            <Series>
            </Series>
            <ChartAreas>
                <asp:ChartArea Name="ChartArea1" BorderColor="64, 64, 64, 64" BorderDashStyle="Solid"
                    BackSecondaryColor="White" BackColor="64, 165, 191, 228" ShadowColor="Transparent"
                    BackGradientStyle="TopBottom">
                    <AxisY LineColor="64, 64, 64, 64">
                        <LabelStyle Font="Trebuchet MS, 8.25pt, style=Bold" />
                        <MajorGrid LineColor="64, 64, 64, 64" />
                    </AxisY>
                    <AxisX LineColor="64, 64, 64, 64">
                        <LabelStyle Font="Trebuchet MS, 8.25pt, style=Bold" />
                        <MajorGrid LineColor="64, 64, 64, 64" />
                    </AxisX>
                </asp:ChartArea>
            </ChartAreas>
        </asp:Chart>
</div>