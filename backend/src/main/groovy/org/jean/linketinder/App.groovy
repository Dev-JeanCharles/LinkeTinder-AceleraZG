package org.jean.linketinder

import groovy.sql.Sql
import org.jean.linketinder.DAO.DBConection
import org.jean.linketinder.DAO.DBOperations
import org.jean.linketinder.Menu.Menu

class App {
    static void main(String[] args) {
        createDatabaseTables()
        startMenu()
    }

    static void createDatabaseTables() {
        DBConection conectDTO = new DBConection()
        Sql instance = new Sql(conectDTO.conect())
        DBOperations operationsDAO = new DBOperations(instance)

        createTableCandidates(operationsDAO)
        createTableSkills(operationsDAO)
        createTableCandidateSkills(operationsDAO)
        createTableCompanies(operationsDAO)
        createTableVacancies(operationsDAO)
        createTableVacancyCompanies(operationsDAO)
        createTableVacancySkills(operationsDAO)
    }

    static void createTableCandidates(DBOperations operationsDAO) {
        List<String> fieldCandidates = [
                "id SERIAL PRIMARY KEY",
                "name VARCHAR(100)",
                "email VARCHAR(255)",
                "cpf VARCHAR(15) UNIQUE",
                "age INTEGER",
                "state VARCHAR(100)",
                "cep VARCHAR(15)",
                "description TEXT"
        ]
        operationsDAO.createTable("candidates", fieldCandidates)
    }

    static void createTableSkills(DBOperations operationsDAO) {
        List<String> fieldSkills = [
                "skill_id SERIAL PRIMARY KEY",
                "name VARCHAR(100)"
        ]
        operationsDAO.createTable("skills", fieldSkills)
    }

    static void createTableCandidateSkills(DBOperations operationsDAO) {
        List<String> candidateSkills = [
                "candidate_id INTEGER REFERENCES candidates(id)",
                "skill_id INTEGER REFERENCES skills(skill_id)"
        ]
        operationsDAO.createTable("candidate_skills", candidateSkills)
    }

    static void createTableCompanies(DBOperations operationsDAO) {
        List<String> fieldCompanies = [
                "id SERIAL PRIMARY KEY",
                "name VARCHAR(100)",
                "cnpj VARCHAR(50) UNIQUE",
                "email VARCHAR(255)",
                "state VARCHAR(100)",
                "country VARCHAR(100)",
                "cep VARCHAR(15)",
                "description TEXT"
        ]
        operationsDAO.createTable("companies", fieldCompanies)
    }

    static void createTableVacancies(DBOperations operationsDAO) {
        List<String> fieldVacancies = [
                "vacancy_id SERIAL PRIMARY KEY",
                "name VARCHAR(100)",
                "locality VARCHAR(100)",
                "description TEXT"
        ]
        operationsDAO.createTable("vacancies", fieldVacancies)
    }

    static void createTableVacancyCompanies(DBOperations operationsDAO) {
        List<String> companyVacancies = [
                "company_id INTEGER REFERENCES companies(id)",
                "vacancy_id INTEGER REFERENCES vacancies(vacancy_id)"
        ]
        operationsDAO.createTable("vacancy_companies", companyVacancies)
    }

    static void createTableVacancySkills(DBOperations operationsDAO) {
        List<String> vacancySkills = [
                "vacancy_id INTEGER REFERENCES vacancies(vacancy_id)",
                "skill_id INTEGER REFERENCES skills(skill_id)"
        ]
        operationsDAO.createTable("vacancy_skills", vacancySkills)
    }

    static void startMenu() {
        new Menu().menuHome()
    }
}
