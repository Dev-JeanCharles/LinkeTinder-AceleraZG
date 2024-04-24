package org.jean.linketinder.Service

import org.jean.linketinder.DAO.CompanyDAO
import org.jean.linketinder.Entities.Company
import org.jean.linketinder.Interfaces.Implementation.CompanyImplementation
import org.jean.linketinder.View.PrintOperationsView

class CompanyService implements CompanyImplementation.CompanyOperationsInterface{

    private final PrintOperationsView printView
    private final CompanyDAO companyDAO
    private final Scanner scanner

    CompanyService(PrintOperationsView printView, CompanyDAO companyDAO, Scanner scanner) {
        this.printView = printView
        this.companyDAO = companyDAO
        this.scanner = scanner
    }

    @Override
    void createCompany() {
        Company newCompany = printView.createCompany(scanner)
        companyDAO.create(newCompany)
    }

    @Override
    void displayCompany() {
        List<Company> companies = companyDAO.getAll()

        if (companies) {
            println("Lista de empresas:")
            for (Company company : companies) {
                printView.displayCompanyInfo(company)
            }
        } else {
            println("Nenhuma empresa encontrada.")
        }
    }

    @Override
    void update() {
        Company company = printView.updateCompany(scanner)
        companyDAO.update(company.cnpj, company)
    }

    @Override
    void delete() {
        String cnpj = printView.deleteCompany(scanner)
        companyDAO.delete(cnpj)
    }
}
