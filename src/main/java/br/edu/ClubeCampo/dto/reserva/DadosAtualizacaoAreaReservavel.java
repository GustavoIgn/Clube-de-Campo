package br.edu.ClubeCampo.dto.reserva;

public record DadosAtualizacaoAreaReservavel(
    String nome,
    int capacidade,
    int quantidadeDisponivel
) {}