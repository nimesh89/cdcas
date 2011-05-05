<%@ Page Title="" Language="C#" MasterPageFile="~/CDCAS.Master" AutoEventWireup="true"
    CodeBehind="Register.aspx.cs" Inherits="CDCAS.Register" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <script src="Scripts/jquery-1.5.2.min.js" type="text/javascript"></script>
    <script src="Scripts/jquery-ui-1.8.12.custom.min.js" type="text/javascript"></script>
    <script type="text/javascript">

        function popupInit() {
            $("#register-div-popup-content").position({
                my: "center",
                at: "center",
                of: "#register-div-popup"
            });
            hidePopup();
        }

        function hidePopup() {
            $("#register-div-popup").hide();
            $("#register-div-popup-content").hide();
        }

        function showPopup() {
            $("#register-div-popup").show();
            $("#register-div-popup-content").show();
        }

        $(document).ready(
            function () {
                popupInit();
                $("#register-div-popup-content > div#popup-header").click(
                    function () {
                        hidePopup();
                        document.location = "Home.aspx?no=0";
                    }
                 );
            }
        );

        $(window).resize(
                function () {
                    $("#register-div-popup-content").position({
                        my: "center",
                        at: "center",
                        of: "#register-div-popup"
                    });
                }
        );
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="page-div-container">
        <div class="page-div-title">
            Register
        </div>
        <div id="register-control-container">
            <div class="register-div-groupbox">
                <fieldset class="register-fieldset">
                    <legend>Personal details</legend>
                    <table cellspacing="5" class="register-table-form">
                        <tr>
                            <td>
                                Full Name
                            </td>
                            <td>
                                <asp:TextBox ID="TextBox1" CssClass="register-textbox" runat="server"></asp:TextBox>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Occupation
                            </td>
                            <td>
                                <asp:TextBox ID="TextBox2" CssClass="register-textbox" runat="server"></asp:TextBox>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Address1
                            </td>
                            <td>
                                <asp:TextBox ID="TextBox3" CssClass="register-textbox" runat="server"></asp:TextBox>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Address2
                            </td>
                            <td>
                                <asp:TextBox ID="TextBox4" CssClass="register-textbox" runat="server"></asp:TextBox>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                City
                            </td>
                            <td>

                                <asp:TextBox ID="TextBox5" CssClass="register-textbox" runat="server"></asp:TextBox>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Phone
                            </td>
                            <td>
                                <asp:TextBox ID="TextBox6" CssClass="register-textbox" runat="server"></asp:TextBox>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Gender
                            </td>
                            <td>
                                <asp:RadioButtonList ID="RadioButtonList1" RepeatDirection="Horizontal" runat="server">
                                    <asp:ListItem Text="Male" Value="M"></asp:ListItem>
                                    <asp:ListItem Text="Female" Value="F"></asp:ListItem>
                                </asp:RadioButtonList>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Email
                            </td>
                            <td>
                                <asp:TextBox ID="TextBox8" CssClass="register-textbox" runat="server"></asp:TextBox>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                NIC or Passport
                            </td>
                            <td>
                                <asp:TextBox ID="TextBox9" CssClass="register-textbox" runat="server"></asp:TextBox>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <asp:CheckBox ID="CheckBox1" Text="If Passport tick here" runat="server" />
                            </td>
                        </tr>
                    </table>
                </fieldset>
            </div>
            <div class="register-div-groupbox">
                <fieldset class="register-fieldset">
                    <legend>Purpose</legend>
                    <table cellspacing="10" class="register-table-form">
                        <tr>
                            <td valign="top">
                                Purpose
                            </td>
                            <td>
                                <asp:TextBox ID="TextBox7" CssClass="register-textbox" TextMode="MultiLine" Height="60px"
                                    Width="200px" runat="server"></asp:TextBox>
                            </td>
                        </tr>
                    </table>
                </fieldset>
                <div id="register-signup-holder">
                    <asp:Button ID="Button1" runat="server" CssClass="login-button-subbmit" OnClientClick="showPopup();return false;"
                        Text="Request" />
                </div>
            </div>
            <div class="div-barricades">
            </div>
        </div>
    </div>
    <div id="register-div-popup">
    </div>
    <div id="register-div-popup-content">
        <div id="popup-header">
            X</div>
        <div id="popup-body">
            <div>
                Your request submited successfully.<br />
                We will contact you via email soon.</div>
        </div>
    </div>
</asp:Content>
