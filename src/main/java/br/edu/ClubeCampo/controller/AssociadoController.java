package br.edu.ClubeCampo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ClubeCampo.dto.associado.DadosAtualizaAssociado;
import br.edu.ClubeCampo.dto.associado.DadosCadastroAssociado;
import br.edu.ClubeCampo.dto.associado.DadosDetalhamentoAssociado;
import br.edu.ClubeCampo.model.associado.Associado;
import br.edu.ClubeCampo.service.AssociadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/associados")
@Tag(name = "Associados", description = "Gerenciamento de associados")
public class AssociadoController {

    @Autowired
    private AssociadoService associadoService;

    // CREATE
    @PostMapping
    @Operation(summary = "Cadastrar associado")
    public ResponseEntity<DadosDetalhamentoAssociado> criar(@RequestBody @Valid DadosCadastroAssociado dados) {
        Associado associado = new Associado(dados);
        associado.setDataCadastro(LocalDate.now());
        associado = associadoService.salvar(associado);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DadosDetalhamentoAssociado(associado));
    }
    
    // READ - listar todos
    @GetMapping
    @Operation(summary = "Listar associado")
    public List<DadosDetalhamentoAssociado> listar() {
        return associadoService.listarTodos()
                .stream()
                .map(DadosDetalhamentoAssociado::new)
                .collect(Collectors.toList());
    }

    // READ - buscar por id
    @GetMapping("/{cpf}")
    @Operation(summary = "Buscar Associado")
    public ResponseEntity<DadosDetalhamentoAssociado> buscarPorId(@PathVariable String cpf) {
        return associadoService.buscarPorCPF(cpf)
                .map(associado -> ResponseEntity.ok(new DadosDetalhamentoAssociado(associado)))
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{cpf}")
    @Operation(summary = "Atualizar associado")
    public ResponseEntity<DadosDetalhamentoAssociado> atualizar(@PathVariable String cpf, @RequestBody @Valid DadosAtualizaAssociado dados) {
        return associadoService.buscarPorCPF(cpf).map(associado -> {
            associado.setNome(dados.nome());
            associado.setRg(dados.rg());
            associado.setEndereco(dados.endereco());
            associado.setCep(dados.cep());
            associado.setBairro(dados.bairro());
            associado.setCidade(dados.cidade());
            associado.setEstado(dados.estado());
            associado.setTelefoneResidencial(dados.telefoneResidencial());
            associado.setTelefoneComercial(dados.telefoneComercial());
            associado.setCelular(dados.celular());
            associado.setTipo(dados.tipo());
            associado.setMesesInadimplente(dados.mesesInadimplente());
            associado.setCartaoBloqueado(dados.cartaoBloqueado());
            associado = associadoService.salvar(associado);
            return ResponseEntity.ok(new DadosDetalhamentoAssociado(associado));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{cpf}")
    @Operation(summary = "Deletar associado")
    public ResponseEntity<Object> deletar(@PathVariable String cpf) {
        return associadoService.buscarPorCPF(cpf)
            .map(associado -> {
                associadoService.deletar(associado.getId());
                return ResponseEntity.noContent().build();
            }).orElse(ResponseEntity.notFound().build());
    }
}