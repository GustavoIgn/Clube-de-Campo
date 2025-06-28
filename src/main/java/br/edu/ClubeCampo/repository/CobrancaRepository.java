package br.edu.ClubeCampo.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ClubeCampo.model.associado.Associado;
import br.edu.ClubeCampo.model.financeiro.Cobranca;

public interface CobrancaRepository extends JpaRepository<Cobranca, Long> {
    boolean existsByAssociadoAndDataVencimento(Associado associado, LocalDate dataVencimento);
}