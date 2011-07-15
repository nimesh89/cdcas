<%@ Page Title="" Language="C#" MasterPageFile="~/CDCAS.Master" AutoEventWireup="true"
    CodeBehind="Admin.aspx.cs" Inherits="CDCAS.Admin" %>

<%@ Register Src="UserControls/ManageUser.ascx" TagName="ManageUser" TagPrefix="uc1" %>
<%@ Register Src="UserControls/UserRequest.ascx" TagName="UserRequest" TagPrefix="uc2" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <script src="Scripts/jquery-1.5.2.min.js" type="text/javascript"></script>
    <script src="Scripts/jquery-ui-1.8.12.custom.min.js" type="text/javascript"></script>
    <%--<style type="text/css">
        #div-navigation ul
        {
            visibility: hidden;
        }
    </style>--%>
    <script type="text/javascript">
        function toggleSearch() {
            $("#admin-div-search-criterias").slideToggle(300);
            var src = $("#admin-image-searchcriteria").attr("src");
            if (src == "images/collapse_icon.gif") {
                $("#admin-image-searchcriteria").attr("src", "images/expand_icon.gif");
            }
            else {
                $("#admin-image-searchcriteria").attr("src", "images/collapse_icon.gif");
            }
        }

        $(document).ready
        (
            function () {
                $("#admin-div-search-criterias").hide();
                $("#admin-image-searchcriteria").click
                (
                    function () {
                        toggleSearch();
                    }
                );
                popupInit();
                $("#register-div-popup-content > div#popup-header").click(
                    function () {
                        hidePopup();
                    }
                 );
            }
        );
    </script>
    <script src="Scripts/popup.js" type="text/javascript"></script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="page-div-container">
        <div class="page-div-title">
            Admin Controls
        </div>
        <div class="div-barricades">
        </div>
        <div id="admin-div-user-manage">
            <fieldset class="admin-user-manage-fieldset">
                <legend>Search</legend>
                <table>
                    <tr>
                        <td>
                            <asp:TextBox ID="TextBox1" runat="server"></asp:TextBox>
                        </td>
                        <td>
                            <asp:ImageButton ID="ImageButton1" AlternateText=" " ImageUrl="~/images/search.jpg"
                                Width="25" Height="25" runat="server" />
                        </td>
                        <td>
                            <img alt=" " id="admin-image-searchcriteria" height="9" width="9" src="images/expand_icon.gif" />
                        </td>
                        <td>
                            search criterias
                        </td>
                    </tr>
                </table>
                <div id="admin-div-search-criterias">
                    <table>
                        <tr>
                            <td>
                                Search By
                            </td>
                            <td>
                                <asp:DropDownList ID="DropDownList1" runat="server">
                                    <asp:ListItem Text="ID" Value="0"></asp:ListItem>
                                    <asp:ListItem Text="First Name" Value="1"></asp:ListItem>
                                    <asp:ListItem Text="Last Name" Value="2"></asp:ListItem>
                                    <asp:ListItem Text="City" Value="3"></asp:ListItem>
                                    <asp:ListItem Text="Email" Value="4"></asp:ListItem>
                                </asp:DropDownList>
                            </td>
                        </tr>
                    </table>
                </div>
            </fieldset>
            <fieldset class="admin-user-manage-fieldset">
                <legend>Result</legend>
                <uc1:ManageUser ID="ManageUser1" UserName="Nimesh Gunasekara" runat="server" />
                <uc1:ManageUser ID="ManageUser2" UserName="Yasodya Panditha" runat="server" />
            </fieldset>
        </div>
        <div id="admin-div-request-process">
            <fieldset class="admin-user-manage-fieldset">
                <legend>Requests</legend>
                <uc2:UserRequest ID="UserRequest1" UserName="Pinith Weerasekara" runat="server" />
                <uc2:UserRequest ID="UserRequest3" UserName="Gothami Mendis" runat="server" />
                <uc2:UserRequest ID="UserRequest2" UserName="Nidusha Lakmali" runat="server" />
            </fieldset>
        </div>
        <div class="div-barricades">
        </div>
    </div>
    <div id="register-div-popup">
    </div>
    <div id="register-div-popup-content">
        <div id="popup-header">
            X</div>
        <div id="popup-body">
            <div>
                Details</div>
        </div>
    </div>
</asp:Content>
