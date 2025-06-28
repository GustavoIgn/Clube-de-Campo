package br.edu.ClubeCampo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ClubeCampo.model.financeiro.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
	List<Pagamento> findByCobrancaId(Long idCobranca);
}
