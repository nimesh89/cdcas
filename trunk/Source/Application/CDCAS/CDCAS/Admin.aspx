<%@ Page Title="" Language="C#" MasterPageFile="~/CDCAS.Master" AutoEventWireup="true"
    CodeBehind="Admin.aspx.cs" Inherits="CDCAS.Admin" %>

<%@ Register src="UserControls/ManageUser.ascx" tagname="ManageUser" tagprefix="uc1" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <script src="Scripts/jquery-1.5.2.min.js" type="text/javascript"></script>
    <style type="text/css">
        #div-navigation ul
        {
            visibility: hidden;
        }
    </style>
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
            }
        );
    </script>
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
                    sdfsdfsdf
                </div>
            </fieldset>
            <fieldset class="admin-user-manage-fieldset">
                <legend>Result</legend>
                <uc1:ManageUser ID="ManageUser1" UserName="Nimesh Gunasekara" runat="server" />
                <uc1:ManageUser ID="ManageUser2" UserName="Yasodya Panditha" runat="server" />
            </fieldset>
        </div>
        <div id="admin-div-request-process">
        </div>
        <div class="div-barricades">
        </div>
    </div>
</asp:Content>
