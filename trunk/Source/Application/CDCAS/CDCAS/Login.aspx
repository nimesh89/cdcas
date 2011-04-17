<%@ Page Title="" Language="C#" MasterPageFile="~/CDCAS.Master" AutoEventWireup="true"
    CodeBehind="Login.aspx.cs" Inherits="CDCAS.Login" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="page-div-container">
        <div class="page-div-title">
            Login</div>
        <div id="login-div-control">
            <div id="login-div-control-container">
                <div class="login-form-text login-form-div">
                    Username</div>
                <div class="login-form-div">
                    <asp:TextBox ID="TextBox1" CssClass="login-form-textbox" runat="server"></asp:TextBox></div>
                <div class="div-barricades">
                </div>
                <div class="login-form-text login-form-div">
                    Password
                </div>
                <div class="login-form-div">
                    <asp:TextBox ID="TextBox2" CssClass="login-form-textbox" runat="server" TextMode="Password"></asp:TextBox></div>
                <div class="div-barricades">
                </div>
                <div id="login-button-subbmit-holder">
                    <asp:Button ID="Button1" CssClass="login-button-subbmit" runat="server" Text="Login" />
                </div>
                <div id="login-form-signup">
                    Don't have a account please click
                    <asp:LinkButton ID="LinkButton1" runat="server">here</asp:LinkButton></div>
            </div>
        </div>
        <div id="login-div-misc">
        </div>
        <div class="div-barricades">
        </div>
    </div>
</asp:Content>
