package br.edu.ClubeCampo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ClubeCampo.dto.dependente.DadosCadastroDependente;
import br.edu.ClubeCampo.dto.dependente.DadosDetalhamentoDependente;
import br.edu.ClubeCampo.model.associado.Associado;
import br.edu.ClubeCampo.model.associado.Dependente;
import br.edu.ClubeCampo.repository.AssociadoRepository;
import br.edu.ClubeCampo.repository.DependenteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/associados/cpf/{cpfAssociado}/dependentes")
@Tag(name = "Dependentes", description = "Gerenciamento de dependentes")
public class DependenteController {

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private DependenteRepository dependenteRepository;

    // CREATE
    @PostMapping
    @Operation(summary = "Cadastrar dependente")
    public ResponseEntity<DadosDetalhamentoDependente> adicionarDependente(
            @PathVariable String cpfAssociado,
            @RequestBody DadosCadastroDependente dados) {

        Associado associado = associadoRepository.findBycpf(cpfAssociado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Associado não encontrado"));

        Dependente dependente = new Dependente(dados, associado);
        dependenteRepository.save(dependente);

        return ResponseEntity.status(HttpStatus.CREATED).body(new DadosDetalhamentoDependente(dependente));
    }

    // READ - Listar todos os dependentes de um associado
    @GetMapping
    @Operation(summary = "Listar dependentes")
    public ResponseEntity<List<DadosDetalhamentoDependente>> listar(@PathVariable String cpfAssociado) {
        return associadoRepository.findBycpf(cpfAssociado)
                .map(associado -> {
                    var dependentes = associado.getDependentes().stream()
                            .map(DadosDetalhamentoDependente::new)
                            .toList();
                    return ResponseEntity.ok(dependentes);
                }).orElse(ResponseEntity.notFound().build());
    }

    // READ - Buscar dependente por ID
    @GetMapping("/{idDependente}")
    @Operation(summary = "Buscar dependente")
    public ResponseEntity<DadosDetalhamentoDependente> buscarPorId(
            @PathVariable String cpfAssociado,
            @PathVariable Long idDependente) {

        return associadoRepository.findBycpf(cpfAssociado)
                .map(associado -> associado.getDependentes().stream()
                        .filter(d -> d.getId().equals(idDependente))
                        .findFirst()
                        .map(d -> ResponseEntity.ok(new DadosDetalhamentoDependente(d)))
                        .orElse(ResponseEntity.notFound().build()))
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{idDependente}")
    @Operation(summary = "Atualizar dependente")
    public ResponseEntity<DadosDetalhamentoDependente> atualizar(
            @PathVariable String cpfAssociado,
            @PathVariable Long idDependente,
            @RequestBody DadosCadastroDependente dadosAtualizados) {

        @SuppressWarnings("unchecked")
		ResponseEntity<DadosDetalhamentoDependente> orElse = (ResponseEntity<DadosDetalhamentoDependente>) associadoRepository.findBycpf(cpfAssociado)
				        .map(associado -> {
				            var dependenteOpt = associado.getDependentes().stream()
				                    .filter(d -> d.getId().equals(idDependente))
				                    .findFirst();

				            if (dependenteOpt.isPresent()) {
				                Dependente dependente = dependenteOpt.get();
				                dependente.setNomeCompleto(dadosAtualizados.nomeCompleto());
				                dependente.setRg(dadosAtualizados.rg());
				                dependenteRepository.save(dependente);
				                return ResponseEntity.ok(new DadosDetalhamentoDependente(dependente));
				            }

				            return ResponseEntity.notFound().build();
				        }).orElse(ResponseEntity.notFound().build());
		return orElse;
    }

    // DELETE
    @DeleteMapping("/{idDependente}")
    @Operation(summary = "Deletar dependente")
    public ResponseEntity<Object> deletar(
            @PathVariable String cpfAssociado,
            @PathVariable Long idDependente) {

        return associadoRepository.findBycpf(cpfAssociado)
                .map(associado -> {
                    var dependenteOpt = associado.getDependentes().stream()
                            .filter(d -> d.getId().equals(idDependente))
                            .findFirst();

                    if (dependenteOpt.isPresent()) {
                        Dependente dependente = dependenteOpt.get();
                        associado.getDependentes().remove(dependente);
                        dependenteRepository.delete(dependente);
                        return ResponseEntity.noContent().build();
                    }

                    return ResponseEntity.notFound().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}