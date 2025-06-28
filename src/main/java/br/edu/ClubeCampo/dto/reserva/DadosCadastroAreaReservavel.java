package br.edu.ClubeCampo.dto.reserva;

public record DadosCadastroAreaReservavel(
    String nome,
    int capacidade,
    int quantidadeDisponivel
) {}