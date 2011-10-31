<%@ Page Title="" Language="C#" MasterPageFile="~/NewMaster.Master" AutoEventWireup="true"
    CodeBehind="Home.aspx.cs" Inherits="CDCAS.Home" %>

<%@ Register Src="UserControls/ChartContainer.ascx" TagName="ChartContainer" TagPrefix="uc1" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <style type="text/css">
        .widget-container
        {
            position: relative;
            float: left;
        }
    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="widget-container">
        <!-- Feedzilla Widget BEGIN -->
        <div class="feedzilla-news-widget feedzilla-4320409030187875" style="width: 250px;
            padding: 0; text-align: center; font-size: 11px; border: 0;">
            <script type="text/javascript" src="http://widgets.feedzilla.com/news/iframe/js/widget.js"></script>
            <script type="text/javascript">
                new FEEDZILLA.Widget({
                    style: 'slide-top-to-bottom',
                    culture_code: 'en_all',
                    c: '1085',
                    sc: '27462',
                    headerBackgroundColor: '#329efc',
                    footerBackgroundColor: '#329efc',
                    title: 'Health',
                    caption: 'Infectious Diseases',
                    order: 'relevance',
                    count: '20',
                    w: '250',
                    h: '810',
                    timestamp: 'true',
                    scrollbar: 'false',
                    theme: 'ui-lightness',
                    className: 'feedzilla-4320409030187875'
                });
            </script>
        </div>
        <!-- Feedzilla Widget END -->
    </div>
    <div class="widget-container" style="width: 450px; text-align: center; font-size: 22px;
        font-weight: bold; background-color: #329efc; color: rgb(5,46,114); margin-bottom: 10px;
        margin-left: 5px;">
        Welcome</div>
    <div class="widget-container" style="width: 450px; margin-left: 5px;background-color:White">
        <p style="text-align:justify">
            Epidemiology is the study of patterns of health and illness and associated factors
            at the population level. It is the method of public health research, and helps inform
            evidence-based medicine for identifying risk factors for disease and determining
            optimal treatment approaches to clinical practice and for preventative medicine.</p>
        <p style="text-align:justify">
            In the study of communicable and non-communicable diseases, epidemiologists are
            involved in outbreak investigation to study design, data collection, statistical
            analysis, documentation of results and submission for publication.</p>
        <p style="text-align:justify">
            Department of Epidemic Diseases is the institute in Sri Lanka which responsible
            for gathering data, generating statistic, Educating public and planning controlling
            methods about communicable diseases. Currently there is no centralized automated
            system for these tasks. Main solution for this task is to make data available in
            simplified and graphical forma to be aided in decision making processes.</p>
    </div>
    <div class="widget-container" style="margin-left: 30px;">
        <!-- Yahoo! Weather Badge -->
        <iframe allowtransparency="true" marginwidth="0" marginheight="0" hspace="0" vspace="0"
            frameborder="0" scrolling="no" src="http://weather.yahoo.com/badge/?id=2189783&u=c&t=default&l=vertical"
            height="255px" width="186px"></iframe>
        <noscript>
            <a href="http://weather.yahoo.com/sri-lanka/western/colombo-2189783/">Colombo Weather</a>
            from <a href="http://weather.yahoo.com">Yahoo! Weather</a>
        </noscript>
        <!-- Yahoo! Weather Badge -->
    </div>
    <div class="widget-container" style="margin-left:5px;margin-top:5px">
        <!-- Shout Box -->
        <!-- Begin ShoutMix - http://www.shoutmix.com -->
        <iframe title="podigune" src="http://www5.shoutmix.com/?podigune" width="445" height="400"
            frameborder="0" scrolling="auto"><a href="http://www5.shoutmix.com/?podigune">View shoutbox</a>
        </iframe>
        <br />
        <!-- End ShoutMix -->
        <!-- Shout Box -->
    </div>
    <div class="widget-container" style="margin-left:5px;">
        <!-- Twittter -->
        <script type="text/javascript" src="http://widgets.twimg.com/j/2/widget.js"></script>
        <script type="text/javascript">
            new TWTR.Widget({
                version: 2,
                type: 'search',
                search: 'epidemic Diseases',
                interval: 30000,
                title: 'Epidemic Disease Threats',
                subject: 'What We Can Do?',
                width: 250,
                height: 320,
                theme: {
                    shell: {
                        background: '#8ec1da',
                        color: '#ffffff'
                    },
                    tweets: {
                        background: '#ffffff',
                        color: '#444444',
                        links: '#1985b5'
                    }
                },
                features: {
                    scrollbar: true,
                    loop: true,
                    live: true,
                    behavior: 'default'
                }
            }).render().start();
        </script>
        <!-- Twitter -->
    </div>
</asp:Content>
