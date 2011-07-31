using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data.SqlClient;
using System.Data;

namespace DataHandler
{
    public class SqlServerConnection
    {
        public SqlConnection Connection { get; set; }
        public string ConnectionString { get; set; }

        public SqlServerConnection(string connectionString)
        {
            ConnectionString = connectionString;
        }

        private void getConnection()
        {
            SqlConnection con = new SqlConnection(ConnectionString);

            con.Open();

            Connection = con;
        }

        public DataTable RunSql(string sql)
        {
            DataTable table = new DataTable();

            try
            {
                getConnection();

                SqlCommand command = new SqlCommand(sql, Connection);

                SqlDataReader reader = command.ExecuteReader();

                table.Load(reader);


            }
            catch (SqlException ex)
            {
                Console.WriteLine(ex.Errors);
            }
            finally
            {
                if (Connection != null)
                    Connection.Close();
            }

            return table;
        }
    }
}
