package br.edu.ClubeCampo.dto.dependente;

import br.edu.ClubeCampo.model.associado.Dependente;

public record DadosDetalhamentoDependente(
    Long id,
    String nomeCompleto,
    String rg
) {
    public DadosDetalhamentoDependente(Dependente dependente) {
        this(dependente.getId(), dependente.getNomeCompleto(), dependente.getRg());
    }
}