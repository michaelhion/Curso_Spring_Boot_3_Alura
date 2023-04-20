package med.voll.api.domain.consulta;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.ValidadorAgendamentoDeConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultas {

    @Autowired
    private ConsultaRepository repository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    /**
     *esta lista recebe no generics uma interface que tem varias implementações
     *o spring vai procurar todas as implementações dessa interface e injetar
     * automaticamente evitando ter que fazer todas elas uma por uma
     * além disso é chamado a listar e feito um foreach nela com um lambda que ira
     * executar todas as implementações que ela possui
     * essa ação se assemelha ao design pattern strategy onde tem uma interface e varias classes
     * implementando estrategias diferentes, só que o strategy originalmente vc utiliza uma delas e estou
     * ultilizado todas as classes que implementão a interface ao mesmo tempo
     *
     * Estou aplicando os seguintes princípios do SOLID:
     *
     * Single Responsibility Principle (Princípio da responsabilidade única):
     * porque cada classe de validação tem apenas uma responsabilidade.
     * Open-Closed Principle (Princípio aberto-fechado):
     * na classe service, AgendadeConsultas, porque ela está fechada para modificação,
     * não precisamos mexer nela. Mas ela está aberta para extensão, conseguimos adicionar novos
     * validadores apenas criando as classes implementando a interface.
     * Dependency Inversion Principle (Princípio da inversão de dependência):
     * porque nossa classe service depende de uma abstração, que é a interface,
     * não depende dos validadores, das implementações especificamente.
     * O módulo de alto nível, a service, não depende dos módulos de baixo nível, que são os validadores.
    */
    @Autowired
    private List<ValidadorAgendamentoDeConsulta> validadores;

    public void agendar(DadosAgendamentoConsulta dados){
        if (!pacienteRepository.existsById(dados.idPaciente())){
            throw new ValidacaoException("Id do paciente informado não existe!");
        }

        if (dados.idMedico() !=null && !medicoRepository.existsById(dados.idMedico())){
            throw new ValidacaoException("Id do médico informado não existe!");
        }

        validadores.forEach(v->v.validar(dados));

        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
        var medico = escolherMedico(dados);
        var consulta = new Consulta(null, medico, paciente ,dados.data(),null);
        repository.save(consulta);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if (dados.idMedico()!=null){
            return medicoRepository.getReferenceById(dados.idMedico());
        }
        if (dados.especialidade() == null){
            throw new ValidacaoException("Especialidade é obrigatória quando médico não for informado!");
        }
        return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(),dados.data());
    }

    public void cancelar(DadosCancelamentoConsulta dados) {
        if (!repository.existsById(dados.idConsulta())) {
            throw new ValidacaoException("Id da consulta informado não existe!");
        }
        var consulta = repository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivosCancelamento());
    }
}
