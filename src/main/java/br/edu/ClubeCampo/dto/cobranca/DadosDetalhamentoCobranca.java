package br.edu.ClubeCampo.dto.cobranca;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.ClubeCampo.model.financeiro.StatusCobranca;

public record DadosDetalhamentoCobranca(Long id, BigDecimal valor, LocalDate dataVencimento, boolean paga,
		String cpfAssociado, String nomeAssociado, StatusCobranca status, BigDecimal valorOriginal) {
}