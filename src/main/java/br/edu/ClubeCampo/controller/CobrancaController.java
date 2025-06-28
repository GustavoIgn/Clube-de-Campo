package br.edu.ClubeCampo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ClubeCampo.dto.cobranca.DadosDetalhamentoCobranca;
import br.edu.ClubeCampo.model.financeiro.Cobranca;
import br.edu.ClubeCampo.service.CobrancaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/cobrancas")
@Tag(name = "Cobranças", description = "Gerenciamento de cobranças mensais")
public class CobrancaController {

	@Autowired
	private CobrancaService cobrancaService;

	@GetMapping
	@Operation(summary = "Listar todas as cobranças")
	public ResponseEntity<List<DadosDetalhamentoCobranca>> listar() {
		List<DadosDetalhamentoCobranca> lista = cobrancaService.listarTodas().stream().map(this::toDto)
				.collect(Collectors.toList());

		return ResponseEntity.ok(lista);
	}

	@PostMapping("/gerar")
	@Operation(summary = "Gerar cobranças mensais para todos os associados")
	public ResponseEntity<String> gerarCobrancasMensais() {
		cobrancaService.gerarCobrancasParaTodos();
		return ResponseEntity.status(HttpStatus.CREATED).body("Cobranças geradas com sucesso!");
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar cobrança por ID")
	public ResponseEntity<DadosDetalhamentoCobranca> buscarPorId(@PathVariable Long id) {
		return cobrancaService.buscarPorId(id).map(this::toDto).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}/status")
	@Operation(summary = "Atualizar manualmente o status da cobrança")
	public ResponseEntity<?> alterarStatusManual(@PathVariable Long id, @RequestParam String status) {
		try {
			cobrancaService.alterarStatusManual(id, status);
			return ResponseEntity.ok("Status atualizado com sucesso.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// Método auxiliar para montar o DTO
	private DadosDetalhamentoCobranca toDto(Cobranca c) {
		return new DadosDetalhamentoCobranca(c.getId(), c.getValor(), c.getDataVencimento(), c.isPaga(),
				c.getAssociado().getCpf(), c.getAssociado().getNome(), c.getStatus(), c.getValorOriginal());
	}
}