package br.edu.ClubeCampo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ClubeCampo.dto.reserva.DadosAtualizaReserva;
import br.edu.ClubeCampo.dto.reserva.DadosReservaSimples;
import br.edu.ClubeCampo.dto.reserva.RequisicaoReservaDTO;
import br.edu.ClubeCampo.model.reserva.Reserva;
import br.edu.ClubeCampo.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Reservas e Inscrições", description = "Gerenciamento das reservas")
@RestController
@RequestMapping("/reservas")
public class ReservaController {

	private final ReservaService service;

	public ReservaController(ReservaService service) {
		this.service = service;
	}

	@Operation(summary = "Reservar área")
	@PostMapping("/reservar")
	public ResponseEntity<String> reservar(@RequestBody RequisicaoReservaDTO dto) {
		String resultado = service.reservarArea(dto.idArea(), dto.idAssociado(), dto.dataReserva());

		if (resultado.equals("Reserva realizada com sucesso.")) {
			return ResponseEntity.ok(resultado);
		} else {
			// Retorna erro com a mensagem específica
			return ResponseEntity.badRequest().body(resultado);
		}
	}

	@PostMapping("/{idArea}/inscrever-associado/{idAssociado}")
	@Operation(summary = "Inscrever associado em turma")
	public ResponseEntity<String> inscreverAssociado(@PathVariable Long idArea, @PathVariable Long idAssociado,
			@RequestParam LocalDate data) {
		String resultado = service.inscreverDependente(idArea, idAssociado, data);

		if (resultado.equals("Inscrição realizada com sucesso.")) {
			return ResponseEntity.ok(resultado);
		} else {
			// Retorna erro com a mensagem específica
			return ResponseEntity.badRequest().body(resultado);
		}
	}

	@PostMapping("/{idArea}/inscrever-dependente/{idDependente}")
	@Operation(summary = "Inscrever dependente em turma")
	public ResponseEntity<String> inscreverDependente(@PathVariable Long idArea, @PathVariable Long idDependente,
			@RequestParam LocalDate data) {
		String resultado = service.inscreverDependente(idArea, idDependente, data);

		if (resultado.equals("Inscrição realizada com sucesso.")) {
			return ResponseEntity.ok(resultado);
		} else {
			// Retorna erro com a mensagem específica
			return ResponseEntity.badRequest().body(resultado);
		}
	}

	@Operation(summary = "Listar reservas")
	@GetMapping
	public ResponseEntity<List<DadosReservaSimples>> listarTodas() {
		var lista = service.listarTodos().stream().map(reserva -> {
			Long idPessoa = null;
			String nomePessoa = null;
			String cpfPessoa = null;

			if (reserva.getAssociado() != null) {
				idPessoa = reserva.getAssociado().getId();
				nomePessoa = reserva.getAssociado().getNome();
				cpfPessoa = reserva.getAssociado().getCpf();
			} else if (reserva.getDependente() != null) {
				idPessoa = reserva.getDependente().getId();
				nomePessoa = reserva.getDependente().getNomeCompleto();
			}

			return new DadosReservaSimples(reserva.getId(), reserva.getDataReserva(), reserva.getDataEvento(), idPessoa,
					cpfPessoa, nomePessoa);
		}).toList();

		return ResponseEntity.ok(lista);
	}

	@Operation(summary = "Buscar reserva")
	@GetMapping("/{id}")
	public ResponseEntity<Reserva> buscarPorId(@PathVariable Long id) {
		return service.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@Operation(summary = "Atualizar reserva")
	@PutMapping
	public ResponseEntity<Reserva> atualizar(@RequestBody DadosAtualizaReserva dados) {
		return service.atualizar(dados).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@Operation(summary = "Deletar reserva ou inscrição")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}
}