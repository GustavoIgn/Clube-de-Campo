package br.edu.ClubeCampo.dto.reserva;

public record DadosCadastroAreaComTurmas(
    String nome,
    int turmasDisponiveis,
    int maxPessoasPorTurma
) {}