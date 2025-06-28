package br.edu.ClubeCampo.dto.reserva;

import java.util.List;

public record DadosDetalhamentoAreaReservavel(
    Long id,
    String nome,
    int capacidade,
    int quantidadeDisponivel,
    List<DadosReservaSimples> reservas
) {}