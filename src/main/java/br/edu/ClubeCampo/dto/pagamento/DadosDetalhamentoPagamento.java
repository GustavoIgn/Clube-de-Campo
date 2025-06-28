package br.edu.ClubeCampo.dto.pagamento;

import java.time.LocalDate;

public record DadosDetalhamentoPagamento(
    Long id,
    Double valorPago,
    String formaPagamento,
    LocalDate dataPagamento,
    Long idAssociado,
    Long idCobranca
) {}