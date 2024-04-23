package org.jean.linketinder.DAO

import groovy.sql.Sql
import org.jean.linketinder.Entities.Candidate
import org.jean.linketinder.Entities.Skill
import org.jean.linketinder.Exceptions.HandleException

class CandidateDAO {
    private static final INSERT_CANDIDATE_QUERY = "INSERT INTO candidates (name, email, cpf, age, state, cep, description) VALUES (?, ?, ?, ?, ?, ?, ?)"
    private static final INSERT_SKILL_QUERY = "INSERT INTO skills (name) VALUES (?)"
    private static final INSERT_CANDIDATE_SKILL_QUERY = "INSERT INTO candidate_skills (candidate_id, skill_id) VALUES (?, ?)"
    private static final SELECT_ALL_CANDIDATES_QUERY = "SELECT * FROM candidates"
    private static final SELECT_SKILLS_FOR_CANDIDATE_QUERY = "SELECT name FROM skills " + "INNER JOIN candidate_skills ON skills.skill_id = candidate_skills.skill_id " + "WHERE candidate_skills.candidate_id = ?"
    private static final DELETE_CANDIDATE_SKILLS_QUERY = "DELETE FROM candidate_skills WHERE candidate_id = ?"
    private static final DELETE_CANDIDATE_QUERY = "DELETE FROM candidates WHERE cpf = ?"

    HandleException exception = new HandleException()

    Sql sql = Sql.newInstance(DBConection.conect())

    void create(Candidate candidate) {
        try {
            insertCandidate(candidate)
            insertCandidateSkills(candidate)
            println("Candidato adicionado com sucesso!")
        } catch (Exception e) {
            exception.handleException("Erro ao adicionar candidato", e)
        }
    }

    private void insertCandidate(Candidate candidate) {
        sql.execute(INSERT_CANDIDATE_QUERY, [
                candidate.name,
                candidate.email,
                candidate.cpf,
                candidate.age,
                candidate.state,
                candidate.cep,
                candidate.description
        ])
    }

    private void insertCandidateSkills(Candidate candidate) {
        candidate.skills.each { skill ->
            Integer skillId = getOrCreateSkillId(skill)
            sql.execute(INSERT_CANDIDATE_SKILL_QUERY, [getCandidateId(candidate.cpf), skillId])
        }
    }

    private Integer getCandidateId(String cpf) {
        Map<String, Object> idRow = sql.firstRow("SELECT id FROM candidates WHERE cpf = ?", [cpf])
        return idRow?.id as Integer
    }

    private Integer getOrCreateSkillId(skill) {
        String skillName = (skill instanceof String) ? skill : skill.name
        Integer skillId = sql.firstRow("SELECT skill_id FROM skills WHERE name = ?", [skillName])?.skill_id as Integer
        if (skillId == null) {
            sql.execute(INSERT_SKILL_QUERY, [skillName])
            skillId = sql.firstRow("SELECT skill_id FROM skills WHERE name = ?", [skillName])?.skill_id as Integer
        }
        return skillId
    }

    List<Candidate> getAll() {
        try {
            List<Candidate> candidates = []
            List<Map<String, Object>> rows = sql.rows(SELECT_ALL_CANDIDATES_QUERY)

            rows.each { row ->
                List<String> skills = getSkillsForCandidate(row.id as Integer)

                Candidate candidate = new Candidate(
                        row.name as String,
                        row.email as String,
                        row.state as String,
                        row.cep as String,
                        row.description as String,
                        skills as List<Skill>,
                        row.id as String,
                        row.cpf as String,
                        row.age as Integer,
                        []
                )
                candidates.add(candidate)
            }
            return candidates

        } catch (Exception e) {
            exception.handleException("Erro ao recuperar candidatos", e)
            return []
        }
    }

    private List<String> getSkillsForCandidate(Integer candidateId) {
        List<Map<String, Object>> skillRows = sql.rows(SELECT_SKILLS_FOR_CANDIDATE_QUERY, [candidateId])
        return skillRows.collect(({ it.name } as Closure<String>))
    }

    void update(String cpf, Candidate candidate) {
        try {
            updateCandidate(cpf, candidate)
            println("Candidato atualizado com sucesso!")
        } catch (Exception e) {
            exception.handleException("Erro ao atualizar candidato", e)
        }
    }

    private void updateCandidate(String cpf, Candidate candidate) {
        sql.execute("UPDATE candidates SET name = ?, email = ?, cpf = ?, age = ?, state = ?, cep = ?, description = ? WHERE cpf = ?", [
                candidate.name,
                candidate.email,
                candidate.cpf,
                candidate.age,
                candidate.state,
                candidate.cep,
                candidate.description,
                cpf
        ])
        Integer id = getCandidateId(cpf)
        if (id != null) {
            sql.execute(DELETE_CANDIDATE_SKILLS_QUERY, [id])
            insertCandidateSkills(candidate)
        } else {
            println "Candidato com CPF $cpf não encontrado."
        }
    }

    void delete(String cpf) {
        try {
            Integer id = getCandidateId(cpf)
            if (id != null) {
                sql.execute(DELETE_CANDIDATE_SKILLS_QUERY, [id])
                sql.execute(DELETE_CANDIDATE_QUERY, [cpf])
                println("Candidato removido com sucesso!")
            } else {
                println "Candidato com CPF $cpf não encontrado."
            }
        } catch (Exception e) {
            exception.handleException("Erro ao remover candidato", e)
        }
    }
}
