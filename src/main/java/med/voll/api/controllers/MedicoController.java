package med.voll.api.controllers;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.medico.DadosListagemMedicos;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    //método antes da refatoração sem resposta de status code
   // @PostMapping
    //metodos de escrita necessitam de uma transação aberta
    /*@Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados){
        this.repository.save(new Medico(dados));
    }*/

    /**
     * é uma boa pratica retornar status code 201 caso o post seja concluido com sucesso, além disso também é
     * necessário retornar uma uri e o objeto criado
     * UriComponentsBuilder cria a url de acordo com o ambiente que está sendo usado, esse metodo é nativo do spring
     * e encapsula toda logica para mudar o url dinamicamente*/
    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder){
        var medico = new Medico(dados);
        this.repository.save(medico);

        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

    /**
     * foi ultilizado record para ser feito o dto(no post tambem foi ultilizado o mesmo recurso)
     * para conversão dos dados foi ultilizado a stream e construtor no record DadosListagemMedico
     * */
    //sem paginação
    /*@GetMapping
    public List<DadosListagemMedicos> listar(){
        return repository.findAll().stream().map(DadosListagemMedicos::new).toList();
    }*/

    /**
     * o Pageable tem um metodo map nele, então foi removido o stream e como o retorno dele mudou para page
     * foi removido o toList()
     * todas configurações de paginação podem ser passadas pela url, caso ela não sejam informadas seram usadas essas
     * que configurei no @PageableDefault
     * obs.: todas essas configurações de paginação são opcionais*/
    //com paginação
    /*@GetMapping
    public Page<DadosListagemMedicos> listar(@PageableDefault(size = 10,sort = {"nome"}) Pageable paginacao){
        return repository.findAll(paginacao).map(DadosListagemMedicos::new);
    }*/
    //segunda refatoração para listar somente medicos com atributo ativo = true
    @GetMapping
    public ResponseEntity<Page<DadosListagemMedicos>> listar(@PageableDefault(size = 10,sort = {"nome"}) Pageable paginacao){
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedicos::new);
        return ResponseEntity.ok(page);
    }

    /**
     * Não foi necessário chamar o método save porque quando abrimos a transação o jpa identifica que houve alterações
     * e commita automaticamente */
    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    //exclusão real do banco de dados
    /*@DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Long id){
        repository.deleteById(id);
    }*/

    //exclusão lógica
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        var medico = repository.getReferenceById(id);
        medico.excluir();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        var medico = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }
}
