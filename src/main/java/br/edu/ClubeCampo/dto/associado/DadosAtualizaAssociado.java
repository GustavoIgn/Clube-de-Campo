package br.edu.ClubeCampo.dto.associado;

import br.edu.ClubeCampo.model.associado.TipoAssociado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizaAssociado(
    @NotBlank String nome,
    @NotBlank String rg,
    @NotBlank String cpf,
    String endereco,
    String cep,
    String bairro,
    String cidade,
    String estado,
    String telefoneResidencial,
    String telefoneComercial,
    String celular,
    @NotNull TipoAssociado tipo,
    int mesesInadimplente, 
    boolean cartaoBloqueado
) {}