package org.jean.linketinder.DAO

import groovy.sql.Sql
import org.jean.linketinder.Entities.Company
import org.jean.linketinder.Entities.Skill
import org.jean.linketinder.Entities.Vacancy
import org.jean.linketinder.Exceptions.HandleException

import java.sql.PreparedStatement

class VacancyDAO {
    private static final String INSERT_VACANCY_QUERY = "INSERT INTO vacancies (name, locality, description) VALUES (?, ?, ?)"
    private static final String INSERT_VACANCY_SKILL_QUERY = "INSERT INTO vacancy_skills (vacancy_id, skill_id) VALUES (?, ?)"
    private static final String INSERT_SKILL_QUERY = "INSERT INTO skills (name) VALUES (?)"
    private static final String INSERT_VACANCY_COMPANY_QUERY = "INSERT INTO vacancy_companies (company_id, vacancy_id) VALUES (?, ?)"
    private static final String SELECT_ALL_VACANCIES_QUERY = """
    SELECT v.vacancy_id, v.name, v.locality, v.description, 
           s.name as skill_name, vc.company_id, c.name as company_name
    FROM vacancies v
    LEFT JOIN vacancy_skills vs ON v.vacancy_id = vs.vacancy_id
    LEFT JOIN skills s ON vs.skill_id = s.skill_id
    LEFT JOIN vacancy_companies vc ON v.vacancy_id = vc.vacancy_id
    LEFT JOIN companies c ON vc.company_id = c.id
"""

    private HandleException exception = new HandleException()
    private Sql sql = Sql.newInstance(DBConection.conect())

    List<Vacancy> getAll() {
        try {
            List<Vacancy> vacancies = []
            List<Map<String, Object>> rows = sql.rows(SELECT_ALL_VACANCIES_QUERY)
            rows.each { Map<String, Object> row ->
                Vacancy vacancy = createVacancyFromRow(row, vacancies)
                addSkillToVacancy(row, vacancy)
                addCompanyToVacancy(row, vacancy)
            }
            return vacancies
        } catch (Exception e) {
            exception.handleException("Erro ao obter todas as vagas", e)
            return []
        }
    }

    private static Vacancy createVacancyFromRow(Map<String, Object> row, List<Vacancy> vacancies) {
        Integer vacancyId = row['vacancy_id'] as Integer
        Vacancy vacancy = vacancies.find { it.id == vacancyId }
        if (!vacancy) {
            vacancy = new Vacancy(
                    vacancyId as Integer,
                    row['name'] as String,
                    row['locality'] as String,
                    row['description'] as String,
                    []
            )
            vacancies.add(vacancy)
        }
        return vacancy
    }

    private static void addSkillToVacancy(Map<String, Object> row, Vacancy vacancy) {
        if (row['skill_name']) {
            Skill skill = new Skill(row['skill_name'] as String)
            vacancy.skills.add(skill)
        }
    }

    private static void addCompanyToVacancy(Map<String, Object> row, Vacancy vacancy) {
        if (row['company_id']) {
            Company company = createCompanyFromRow(row)
            vacancy.setCompany(company)
        }
    }

    private static Company createCompanyFromRow(Map<String, Object> row) {
        return new Company(
                null,
                row['company_name'] as String,
                null,
                null,
                null,
                null,
                null,
                null
        )
    }

    void create(Vacancy vacancy, Company company) {
        try {
            println "Criando nova vaga: ${vacancy.name}"

            Integer vacancyId = insertVacancy(vacancy)
            if (vacancyId != null) {
                insertVacancySkills(vacancyId, vacancy.skills)
                associateVacancyWithCompany(company.id, vacancyId)
            } else {
                println "Erro ao criar a vaga. O ID retornado é nulo."
            }
        } catch (Exception e) {
            exception.handleException("Erro ao adicionar a vaga", e)
        }
    }

    private Integer insertVacancy(Vacancy vacancy) {
        try {
            List<Object> parameters = [vacancy.name, vacancy.locality, vacancy.description]
            sql.executeInsert(INSERT_VACANCY_QUERY, parameters)

            return (sql.firstRow("SELECT lastval() as id")?.id as Integer)
        } catch (Exception e) {
            exception.handleException("Erro ao inserir a vaga", e)
            return null
        }
    }

    private void insertVacancySkills(Integer vacancyId, List<Skill> skills) {
        try {
            skills.each { skill ->
                String skillName = skill instanceof Skill ? skill.name : skill
                Integer skillId = getOrCreateSkillId(skillName as String)
                if (skillId) {
                    sql.execute(INSERT_VACANCY_SKILL_QUERY, [vacancyId, skillId])
                }
            }
        } catch (Exception e) {
            exception.handleException("Erro ao adicionar habilidades à vaga", e)
        }
    }

    private void associateVacancyWithCompany(Integer companyId, Integer vacancyId) {
        try {
            if (companyId != null && vacancyId != null) {
                PreparedStatement statement = sql.getConnection().prepareStatement(INSERT_VACANCY_COMPANY_QUERY) as PreparedStatement
                statement.setInt(1, companyId)
                statement.setInt(2, vacancyId)
                statement.execute()
                println "Vaga associada à empresa com sucesso."
            } else {
                println "IDs de empresa ou vaga nulos. A vaga não pode ser associada à empresa."
            }
        } catch (Exception e) {
            exception.handleException("Erro ao associar vaga à empresa", e)
        }
    }

    private Integer getOrCreateSkillId(String skillName) {
        try {
            Integer skillId = sql.firstRow("SELECT skill_id FROM skills WHERE name = ?", [skillName])?.skill_id as Integer
            if (skillId == null) {
                sql.execute(INSERT_SKILL_QUERY, [skillName])
                skillId = sql.firstRow("SELECT lastval() as id")?.id as Integer
            }
            return skillId
        } catch (Exception e) {
            exception.handleException("Erro ao obter ou criar habilidade", e)
            return null
        }
    }
}
