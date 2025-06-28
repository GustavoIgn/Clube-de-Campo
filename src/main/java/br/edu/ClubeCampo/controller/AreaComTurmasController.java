package br.edu.ClubeCampo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ClubeCampo.dto.reserva.DadosAtualizacaoAreaComTurmas;
import br.edu.ClubeCampo.dto.reserva.DadosCadastroAreaComTurmas;
import br.edu.ClubeCampo.dto.reserva.DadosDetalhamentoAreaComTurmas;
import br.edu.ClubeCampo.model.reserva.AreaComTurmas;
import br.edu.ClubeCampo.service.AreaComTurmaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/areas-com-turmas")
@Tag(name = "Área com Turmas", description = "Gerenciamento de áreas com turmas e inscrições")
public class AreaComTurmasController {

    @Autowired
	private AreaComTurmaService service;

    // CREATE
	@PostMapping
	@Operation(summary = "Cadastrar nova área com turmas")
	public ResponseEntity<AreaComTurmas> cadastrar(@RequestBody DadosCadastroAreaComTurmas dados) {
		AreaComTurmas area = new AreaComTurmas(dados.nome(), dados.turmasDisponiveis(), dados.maxPessoasPorTurma());
		return ResponseEntity.ok(service.salvar(area));
	}
	
    // READ - listar todos
	@GetMapping
	@Operation(summary = "Listar todas as áreas com turmas")
	public ResponseEntity<List<DadosDetalhamentoAreaComTurmas>> listar() {
	    List<DadosDetalhamentoAreaComTurmas> dtoList = service.listarTodas().stream().map(area -> {
	        var inscritos = area.getReservas().stream().map(reserva -> {
	            if (reserva.getAssociado() != null) {
	                return new DadosDetalhamentoAreaComTurmas.InscritoDTO(
	                    reserva.getAssociado().getId(),
	                    reserva.getAssociado().getNome(),
	                    "ASSOCIADO"
	                );
	            } else if (reserva.getDependente() != null) {
	                return new DadosDetalhamentoAreaComTurmas.InscritoDTO(
	                    reserva.getDependente().getId(),
	                    reserva.getDependente().getNomeCompleto(),
	                    "DEPENDENTE"
	                );
	            } else {
	                return null;
	            }
	        }).filter(i -> i != null).toList();

	        return new DadosDetalhamentoAreaComTurmas(
	            area.getId(),
	            area.getNome(),
	            area.getTurmasDisponiveis(),
	            area.getMaxPessoasPorTurma(),
	            inscritos
	        );
	    }).toList();

	    return ResponseEntity.ok(dtoList);
	}

    // READ - buscar por id
	@GetMapping("/{id}")
	@Operation(summary = "Buscar área com turmas por ID")
	public ResponseEntity<DadosDetalhamentoAreaComTurmas> buscarPorId(@PathVariable Long id) {
		return service.buscarPorId(id).map(area -> {
			var inscritos = area.getReservas().stream().map(reserva -> {
				if (reserva.getAssociado() != null) {
					return new DadosDetalhamentoAreaComTurmas.InscritoDTO(reserva.getAssociado().getId(),
							reserva.getAssociado().getNome(), "ASSOCIADO");
				} else if (reserva.getDependente() != null) {
					return new DadosDetalhamentoAreaComTurmas.InscritoDTO(reserva.getDependente().getId(),
							reserva.getDependente().getNomeCompleto(), "DEPENDENTE");
				} else {
					return null;
				}
			}).filter(i -> i != null).toList();

			var dto = new DadosDetalhamentoAreaComTurmas(area.getId(), area.getNome(), area.getTurmasDisponiveis(),
					area.getMaxPessoasPorTurma(), inscritos);

			return ResponseEntity.ok(dto);
		}).orElse(ResponseEntity.notFound().build());
	}


    // UPDATE
	@PutMapping("/{id}")
	@Operation(summary = "Atualizar dados da área com turmas")
	public ResponseEntity<AreaComTurmas> atualizar(@PathVariable Long id,
			@RequestBody DadosAtualizacaoAreaComTurmas dados) {
		return service.buscarPorId(id).map(area -> {
			area.setNome(dados.nome());
			area.setTurmasDisponiveis(dados.turmasDisponiveis());
			area.setMaxPessoasPorTurma(dados.maxPessoasPorTurma());
			return ResponseEntity.ok(service.salvar(area));
		}).orElse(ResponseEntity.notFound().build());
	}

    // DELETE
	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir área com turmas")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		service.excluir(id);
		return ResponseEntity.noContent().build();
	}

	/*
	@PostMapping("/{idArea}/inscrever-associado/{idAssociado}")
	@Operation(summary = "Inscrever associado em turma")
	public ResponseEntity<String> inscreverAssociado(@PathVariable Long idArea, @PathVariable Long idAssociado,
			@RequestParam LocalDate data) {
		boolean sucesso = service.reservar(idArea, idAssociado, data);
		return sucesso ? ResponseEntity.ok("Inscrição realizada com sucesso.")
				: ResponseEntity.badRequest().body("Não foi possível realizar a inscrição.");
	}

	@PostMapping("/{idArea}/inscrever-dependente/{idDependente}")
	@Operation(summary = "Inscrever dependente em turma")
	public ResponseEntity<String> inscreverDependente(@PathVariable Long idArea, @PathVariable Long idDependente,
			@RequestParam LocalDate data) {
		boolean sucesso = service.inscreverDependente(idArea, idDependente, data);
		return sucesso ? ResponseEntity.ok("Inscrição realizada com sucesso.")
				: ResponseEntity.badRequest().body("Não foi possível realizar a inscrição.");
	} */
}