package br.edu.ClubeCampo.dto.reserva;

public record DadosAtualizacaoAreaReservavel(
    Long id,
    String nome,
    int capacidade,
    int quantidadeDisponivel
) {}