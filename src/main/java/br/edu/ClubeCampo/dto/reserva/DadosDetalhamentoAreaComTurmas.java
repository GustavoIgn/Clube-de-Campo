package br.edu.ClubeCampo.dto.reserva;

import java.util.List;

public record DadosDetalhamentoAreaComTurmas(
    Long id,
    String nome,
    int turmasDisponiveis,
    int maxPessoasPorTurma,
    List<InscritoDTO> inscritos
) {

    public record InscritoDTO(Long id, String nome, String tipo) {}
}