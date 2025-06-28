package br.edu.ClubeCampo.model.associado;

import br.edu.ClubeCampo.dto.dependente.DadosCadastroDependente;
import jakarta.persistence.*;

@Entity
@Table(name = "dependente")
public class Dependente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCompleto;
    private String rg;

    @ManyToOne
    @JoinColumn(name = "associado_id")
    private Associado associado;

    public Dependente() {
    }

    public Dependente(DadosCadastroDependente dados, Associado associado) {
        this.nomeCompleto = dados.nomeCompleto();
        this.rg = dados.rg();
        this.associado = associado;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public Associado getAssociado() {
        return associado;
    }

    public void setAssociado(Associado associado) {
        this.associado = associado;
    }
}