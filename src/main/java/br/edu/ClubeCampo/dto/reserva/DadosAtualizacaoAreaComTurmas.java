package br.edu.ClubeCampo.dto.reserva;

public record DadosAtualizacaoAreaComTurmas(
    Long id,
    String nome,
    int turmasDisponiveis,
    int maxPessoasPorTurma
) {}