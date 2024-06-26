package org.jean.linketinder.Interfaces.DB

import groovy.sql.Sql

class DBOperations implements DatabaseOperations {

    private static DBOperations instance
    private Sql sql

    private DBOperations(Sql instance) {
        this.sql = instance
    }

    static synchronized DBOperations getInstance(Sql sqlInstance) {
        if (instance == null) {
            instance = new DBOperations(sqlInstance)
        }
        return instance
    }

    void createTable(String nameTable, List<String> fields) {
        try {

            if (!existTable(nameTable)) {

            String fieldsString = fields.join(", ")

            String query = "CREATE TABLE $nameTable ($fieldsString)"

            sql.execute(query)

            println("Tabela $nameTable criada com sucesso!")
            }

        } catch (Exception e) {
            println("Erro ao criar tabela $nameTable: ${e.message}")
        }
    }

     boolean existTable(String nameTable) {
         try {
            Boolean result = sql.firstRow("""
            SELECT EXISTS(
                SELECT 1
                FROM   information_schema.tables
                WHERE  table_name = ?
            )""", [nameTable])

            return result != null && result

        } catch (Exception e) {
            println("Erro ao verificar a existência da tabela $nameTable: ${e.message}")
            return false
        }
    }
}
