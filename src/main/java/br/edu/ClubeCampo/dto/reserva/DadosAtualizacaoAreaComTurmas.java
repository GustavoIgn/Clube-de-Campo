package br.edu.ClubeCampo.dto.reserva;

public record DadosAtualizacaoAreaComTurmas(
    String nome,
    int turmasDisponiveis,
    int maxPessoasPorTurma
) {}