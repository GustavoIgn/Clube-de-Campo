package br.edu.ClubeCampo.dto.pagamento;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizaPagamento(
    @NotNull Double valorPago,
    @NotNull String formaPagamento,
    @NotNull LocalDate dataPagamento
) {}