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

import br.edu.ClubeCampo.dto.reserva.DadosAtualizacaoAreaReservavel;
import br.edu.ClubeCampo.dto.reserva.DadosCadastroAreaReservavel;
import br.edu.ClubeCampo.dto.reserva.DadosDetalhamentoAreaReservavel;
import br.edu.ClubeCampo.dto.reserva.DadosReservaSimples;
import br.edu.ClubeCampo.model.reserva.AreaReservavel;
import br.edu.ClubeCampo.service.AreaReservavelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Área Reservável", description = "Gerenciamento de áreas reserváveis")
@RestController
@RequestMapping("/areas-reservaveis")
public class AreaReservavelController {

	@Autowired
	private AreaReservavelService service;

	@Operation(summary = "Cadastrar área reservável")
	@PostMapping
    public ResponseEntity<AreaReservavel> cadastrar(@RequestBody DadosCadastroAreaReservavel dados) {
        AreaReservavel area = new AreaReservavel();
        area.setNome(dados.nome());
        area.setCapacidade(dados.capacidade());
        area.setQuantidadeDisponivel(dados.quantidadeDisponivel());

        return ResponseEntity.ok(service.salvar(area));
    }
	
	@Operation(summary = "Listar área reservável")
	@GetMapping
	public ResponseEntity<List<DadosDetalhamentoAreaReservavel>> listarTodas() {
	    var lista = service.listarTodas().stream().map(area -> {
	        var reservasSimples = area.getReservas().stream()
	            .map(r -> new DadosReservaSimples(
	                r.getId(),
	                r.getDataReserva(),
	                r.getDataEvento(),
	                r.getAssociado().getId(),
	                r.getAssociado().getCpf(),
	                r.getAssociado().getNome()
	            )).toList();

	        return new DadosDetalhamentoAreaReservavel(
	            area.getId(),
	            area.getNome(),
	            area.getCapacidade(),
	            area.getQuantidadeDisponivel(),
	            reservasSimples
	        );
	    }).toList();

	    return ResponseEntity.ok(lista);
	}

	@Operation(summary = "Buscar área reservável")
	@GetMapping("/{id}")
	public ResponseEntity<AreaReservavel> buscarPorId(@PathVariable Long id) {
		return service.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@Operation(summary = "Atualizar área reservável")
    @PutMapping
    public ResponseEntity<AreaReservavel> atualizar(@RequestBody DadosAtualizacaoAreaReservavel dados) {
        return service.buscarPorId(dados.id())
            .map(area -> {
                area.setNome(dados.nome());
                area.setCapacidade(dados.capacidade());
                area.setQuantidadeDisponivel(dados.quantidadeDisponivel());
                return ResponseEntity.ok(service.salvar(area));
            })
            .orElse(ResponseEntity.notFound().build());
    }

	@Operation(summary = "Deletar área reservável")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		service.excluir(id);
		return ResponseEntity.noContent().build();
	}
	
	/*
	@Operation(summary = "Reservar área")
	@PostMapping("/reservar")
	public ResponseEntity<String> reservar(@RequestBody RequisicaoReservaDTO dto) {
		boolean sucesso = service.reservar(dto.idArea(), dto.idAssociado(), dto.dataReserva());

		if (sucesso) {
			return ResponseEntity.ok("Reserva realizada com sucesso.");
		} else {
			return ResponseEntity.badRequest()
					.body("Reserva não pode ser realizada (sem disponibilidade ou dados inválidos).");
		}
	}*/
}