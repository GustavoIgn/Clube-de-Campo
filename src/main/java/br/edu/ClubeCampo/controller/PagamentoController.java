package br.edu.ClubeCampo.controller;

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

import br.edu.ClubeCampo.dto.pagamento.DadosAtualizaPagamento;
import br.edu.ClubeCampo.dto.pagamento.DadosCadastroPagamento;
import br.edu.ClubeCampo.dto.pagamento.DadosDetalhamentoPagamento;
import br.edu.ClubeCampo.model.financeiro.Pagamento;
import br.edu.ClubeCampo.service.CobrancaService;
import br.edu.ClubeCampo.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/pagamentos")
@Tag(name = "Pagamentos", description = "Gerenciamento de pagamentos")
public class PagamentoController {

	@Autowired
	private PagamentoService pagamentoService;
	
	@Autowired
	private CobrancaService cobrancaService;

	@GetMapping
	@Operation(summary = "Listar todos os pagamentos")
	public ResponseEntity<List<DadosDetalhamentoPagamento>> listar() {
		var lista = pagamentoService.listarTodos().stream()
				.map(p -> new DadosDetalhamentoPagamento(p.getId(), p.getValorPago(), p.getFormaPagamento(),
						p.getDataPagamento(), p.getAssociado().getId(), p.getCobranca().getId()))
				.collect(Collectors.toList());
		return ResponseEntity.ok(lista);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar pagamento por ID")
	public ResponseEntity<DadosDetalhamentoPagamento> buscarPorId(@PathVariable Long id) {
		return pagamentoService.buscarPorId(id)
				.map(p -> ResponseEntity
						.ok(new DadosDetalhamentoPagamento(p.getId(), p.getValorPago(), p.getFormaPagamento(),
								p.getDataPagamento(), p.getAssociado().getId(), p.getCobranca().getId())))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/cpf/{cpfAssociado}/cobranca/{idCobranca}")
	@Operation(summary = "Registrar um novo pagamento para uma cobrança de um associado")
	public ResponseEntity<DadosDetalhamentoPagamento> cadastrar(@PathVariable String cpfAssociado,
			@PathVariable Long idCobranca, @RequestBody DadosCadastroPagamento dados) {

		Pagamento pagamento = pagamentoService.cadastrar(cpfAssociado, idCobranca, dados);
		DadosDetalhamentoPagamento dto = new DadosDetalhamentoPagamento(pagamento.getId(), pagamento.getValorPago(),
				pagamento.getFormaPagamento(), pagamento.getDataPagamento(), pagamento.getAssociado().getId(),
				pagamento.getCobranca().getId());
		cobrancaService.atualizarStatusCobranca(idCobranca);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar pagamento")
	public ResponseEntity<DadosDetalhamentoPagamento> atualizar(@PathVariable Long id,
			@RequestBody DadosAtualizaPagamento dados) {

		return pagamentoService.atualizar(id, dados)
				.map(p -> ResponseEntity
						.ok(new DadosDetalhamentoPagamento(p.getId(), p.getValorPago(), p.getFormaPagamento(),
								p.getDataPagamento(), p.getAssociado().getId(), p.getCobranca().getId())))
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir pagamento")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		pagamentoService.deletar(id);
		return ResponseEntity.noContent().build();
	}
}