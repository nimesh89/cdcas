<%@ Control Language="C#" AutoEventWireup="true" CodeBehind="UserRequest.ascx.cs"
    Inherits="CDCAS.UserControls.UserRequest" %>
<div class="admin-div-usermannage">
    <table>
        <tr>
            <td>
                <asp:Label ID="Label1" runat="server" Text=""></asp:Label>
            </td>
            <td class="td-usermanage-controls">
                <asp:ImageButton ID="ImageButton1" AlternateText=" " Width="25" Height="25" ImageUrl="~/images/accept-icon.png"
                    runat="server" />
                <asp:ImageButton ID="ImageButton2" AlternateText=" " Width="25" Height="25" ImageUrl="~/images/bmf-ignore-icon.gif"
                    runat="server" />
                <asp:LinkButton ID="LinkButton1" runat="server" OnClientClick="showPopup();return false;">Detailes</asp:LinkButton>
            </td>
        </tr>
    </table>
</div>
