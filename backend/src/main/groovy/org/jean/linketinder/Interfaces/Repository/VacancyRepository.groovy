package org.jean.linketinder.Interfaces.Repository

import org.jean.linketinder.Model.Entity.Company
import org.jean.linketinder.Model.Entity.Vacancy

interface VacancyRepository {
    List<Vacancy> getAll()
    void create(Vacancy vacancy, Company company)
    void update(Integer vacancyId, Vacancy vacancy)
    void deleteByCompanyId(Integer companyId)
}