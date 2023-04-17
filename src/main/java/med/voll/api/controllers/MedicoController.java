package med.voll.api.controllers;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    //metodos de escrita necessitam de uma transação aberta
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados){
        this.repository.save(new Medico(dados));
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
    public Page<DadosListagemMedicos> listar(@PageableDefault(size = 10,sort = {"nome"}) Pageable paginacao){
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedicos::new);
    }

    /**
     * Não foi necessário chamar o método save porque quando abrimos a transação o jpa identifica que houve alterações
     * e commita automaticamente */
    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
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
    public void excluir(@PathVariable Long id){
        var medico = repository.getReferenceById(id);
        medico.excluir();
    }
}
