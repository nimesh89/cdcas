<%@ Control Language="C#" AutoEventWireup="true" CodeBehind="ManageUser.ascx.cs"
    Inherits="CDCAS.UserControls.ManageUser" %>
<div class="admin-div-usermannage">
    <table>
        <tr>
            <td>
                <asp:Label ID="Label1" runat="server" Text=""></asp:Label>
            </td>
            <td class="td-usermanage-controls">
                <asp:ImageButton ID="ImageButton1" AlternateText=" " Width="25" Height="25" ImageUrl="~/images/block.png"
                    runat="server" />
                <asp:LinkButton ID="LinkButton1" runat="server" OnClientClick="showPopup();return false;" >Detailes</asp:LinkButton>
            </td>
        </tr>
    </table>
</div>
