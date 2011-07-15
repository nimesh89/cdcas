using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CDCAS.Utilities
{
    public class Constants
    {
        public const string PgConnectionKey = "PgConnectionString";
        public const string SqlConnectionKey = "SqlConnectionString";
        public const string MapServiceUrlKey = "MapServiceUrl";
        public const string DieseaseCodeParamKey = "DESCODE";
        public const string DateParamKey = "DATE";
        public const string ParamSeperator = "&";
        public const string ParamSetValue = "=";
        public const string RangeIndicator = "-";
        public const string YearDieseaseSeperator = "  -   ";
        public const string DatePostFix = "-01-01";
        public const string SeariesOneKey = "Series1";
        public const string ChartAreaOneKey = "ChartArea1";
        public const string PieLabelStyleKey = "PieLabelStyle";
        public const string CommaSeperator = ",";
        public const string PresentageIndicator = "#PERCENT";
        public const string ValueOfXIndicator = "#VALX";
        public const string UseMapKey = "usemap";
        public const string UseMapDistricSecretaryValue = "#districSecretaryMap";
        
        
        public const string ExportToExcelLinkFormat = "~/ExportToExcel.aspx?diesease={0}&code={1}&year={2}";
        public const string GetYearsSql = "select distinct date_part('year', \"Date\") as year from \"MapData\" order by year desc";
        public const string GetMostSuitableDieseaseCodeSql = "select Distinct \"DiseaseCode\", pcount from fullcountview where date_part('year', \"Date\") = {0} and \"DiseaseCode\" is not null order by pcount desc limit 1";
        public const string GetDieseasesSql = "select * from tblDieseases";
        public const string GetMaxValueSql = "select pcount from {0} order by pcount desc limit 1  ";
        public const string GetMinValueSql = "select pcount from {0} order by pcount limit 1  ";
        public const string GetLegendTableSql = "select gid as \"Id\", divisec as \"Name\", pcount as \"Patients\" from {0} order by pcount desc";
        public const string GetYearVsPcountSql = "SELECT DATEPART(YEAR, [DATE]) AS [YEAR], COUNT([PID]) AS [PCOUNT] FROM [dbo].[tblPatients] GROUP BY DATEPART(YEAR, [DATE]) ORDER BY [Year]";
        public const string GetDieseaseVsPcountForCurrentYear = "SELECT	DE.[NAME],COUNT(PA.[PID]) AS [PCOUNT] FROM [dbo].[tblPatients] PA INNER JOIN [dbo].[tblDieseases] DE ON DE.DESECODE = PA.DIESEASECODE WHERE DATEPART(YEAR, PA.[DATE]) IN (SELECT DISTINCT TOP 5 DATEPART(YEAR, PA.[DATE]) AS [Year] FROM dbo.tblPatients PA ORDER BY [Year] DESC) GROUP BY DE.[NAME]";
        public const string GetPatientBreakDownAgeGenderAndYear = "EXEC dbo.spPatientsBreakDownAgeAndYear";
        public const string GetYearsStatSql = "SELECT DISTINCT DATEPART(YEAR, [DATE]) AS [YEAR] FROM tblPatients ORDER BY [YEAR] DESC";
    }
}