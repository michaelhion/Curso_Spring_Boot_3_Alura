package med.voll.api.domain.medico;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * anotation @DataJpaTest serve para testar a camada de persistencia do spring
 * Por padrão o spring procura um banco de dados em memoria (embebed), como vou testar no banco de dados que
 * realmente esta sendo utilizado na aplicação é necessário usar a  anotation @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
 * ela configura o spring para não substituir as configurações do database para testes
 * Outra observação importante é que é bom utilizar um database somente para teste então
 * criei um application-test.properties sobrescrevendo somente a linha que indica a url
 * do database e usei a anotation @ActiveProfiles("test") para indicar pro spring que
 * esse database deve ser utilizado nos testes
 * */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Test
    void escolherMedicoAleatorioLivreNaData() {
    }
}