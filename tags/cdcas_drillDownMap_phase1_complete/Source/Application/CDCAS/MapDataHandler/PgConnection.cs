using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Npgsql;
using System.Data;

namespace MapDataHandler
{
    public class PgConnection
    {
        public NpgsqlConnection Connection { get; set; }
        public string ConnectionString { get; set; }

        //private const string sql = "SELECT * from public.deng2011";

        public PgConnection(string connectionString) 
        {
            ConnectionString = connectionString;
        }

        private void getConnection()
        {
            NpgsqlConnection con = new NpgsqlConnection(ConnectionString);

            con.Open();

            Connection = con;
        }

        public DataTable RunSql(string sql) 
        {
            DataTable table = new DataTable();

            try
            {
                getConnection();

                NpgsqlCommand command = new NpgsqlCommand(sql, Connection);

                NpgsqlDataReader reader = command.ExecuteReader();

                table.Load(reader);

                //while (reader.NextResult())
                //{
                //    for (int columnIndex = 0; columnIndex < reader.FieldCount; columnIndex++)
                //    {
                //        if (!table.ContainsKey(reader.GetName(columnIndex)))
                //        {
                //            table[reader.GetName(columnIndex)] = new List<object>();
                //        }
                //        table[reader.GetName(columnIndex)].Add(reader.GetValue(columnIndex));
                //    }
                //}
            }
            catch (NpgsqlException ex)
            {
                Console.WriteLine(ex.Errors);
            }
            finally 
            {
                Connection.Close();
            }

            return table;
        }

        //public void test()
        //{
        //    getConnection();

        //    NpgsqlCommand command = new NpgsqlCommand("select * from \"MapData\"", Connection);

        //    NpgsqlDataReader reader = command.ExecuteReader();

        //    Connection.Close();
        //}
    }
}
