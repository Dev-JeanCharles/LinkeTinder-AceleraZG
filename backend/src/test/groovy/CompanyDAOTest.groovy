import groovy.sql.Sql
import org.jean.linketinder.DAO.CompanyDAO
import org.jean.linketinder.Entities.Company
import org.jean.linketinder.Exceptions.HandleException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import spock.lang.Specification
import static org.junit.jupiter.api.Assertions.*

class CompanyDAOTest extends Specification{

    Sql sql
    CompanyDAO companyDAO

    @BeforeEach
    void setup() {
        sql = Mock(Sql)
        companyDAO = new CompanyDAO(sql: sql)
        companyDAO.exception = new HandleException()
    }

    @Test
    void CreateCompanyDAOTest() {
        given:
        Company company = new Company(
                3,
                "Tech solutions",
                "tech@solutions.com.br",
                "12.345.678/0001-00",
                "Brasil",
                "SP",
                "01000-000",
                "Descrição da empresa"
        )

        when:
        companyDAO.create(company)

        then:
        assertEquals(3, company.id)
        assertEquals("Tech solutions", company.name)
        assertEquals("tech@solutions.com.br", company.email)
        assertEquals("12.345.678/0001-00", company.cnpj)
        assertEquals("Brasil", company.country)
        assertEquals("SP", company.state)
        assertEquals("01000-000", company.cep)
        assertEquals("Descrição da empresa", company.description)
    }

}