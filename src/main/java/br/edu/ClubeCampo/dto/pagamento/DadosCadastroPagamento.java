package br.edu.ClubeCampo.dto.pagamento;

import jakarta.validation.constraints.NotNull;

public record DadosCadastroPagamento(
    @NotNull Double valorPago,
    @NotNull String formaPagamento
) {}