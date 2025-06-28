package br.edu.ClubeCampo.dto.associado;

import java.time.LocalDate;
import java.util.List;

import br.edu.ClubeCampo.dto.dependente.DadosDetalhamentoDependente;
import br.edu.ClubeCampo.model.associado.Associado;
import br.edu.ClubeCampo.model.associado.TipoAssociado;

public record DadosDetalhamentoAssociado(
	    Long id,
	    String nome,
	    String rg,
	    String cpf,
	    String endereco,
	    String cep,
	    String bairro,
	    String cidade,
	    String estado,
	    String telefoneResidencial,
	    String telefoneComercial,
	    String celular,
	    LocalDate dataCadastro,
	    TipoAssociado tipo,
	    int mesesInadimplente,
	    boolean cartaoBloqueado,
	    List<DadosDetalhamentoDependente> dependentes
	) {
	    public DadosDetalhamentoAssociado(Associado associado) {
	        this(
	            associado.getId(),
	            associado.getNome(),
	            associado.getRg(),
	            associado.getCpf(),
	            associado.getEndereco(),
	            associado.getCep(),
	            associado.getBairro(),
	            associado.getCidade(),
	            associado.getEstado(),
	            associado.getTelefoneResidencial(),
	            associado.getTelefoneComercial(),
	            associado.getCelular(),
	            associado.getDataCadastro(),
	            associado.getTipo(),
	            associado.getMesesInadimplente(),
	            associado.isCartaoBloqueado(),
	            associado.getDependentes().stream()
	                .map(DadosDetalhamentoDependente::new)
	                .toList()
	        );
	    }
	}
