package br.edu.ClubeCampo.model.financeiro;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.ClubeCampo.model.associado.Associado;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Cobranca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal valor;
    
    private BigDecimal valorOriginal;

    private LocalDate dataVencimento;

    private boolean paga;

    @Enumerated(EnumType.STRING)
    private StatusCobranca status = StatusCobranca.EM_ABERTO;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "associado_id")
    private Associado associado;

    // Construtores
    public Cobranca() {}

    public Cobranca(BigDecimal valor, LocalDate dataVencimento, Associado associado) {
    	this.valorOriginal = valor;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.associado = associado;
        this.status = StatusCobranca.EM_ABERTO;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public boolean isPaga() {
        return paga;
    }

    public void setPaga(boolean paga) {
        this.paga = paga;
    }

    public Associado getAssociado() {
        return associado;
    }

    public void setAssociado(Associado associado) {
        this.associado = associado;
    }

	public StatusCobranca getStatus() {
		return status;
	}

	public void setStatus(StatusCobranca status) {
		this.status = status;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValorOriginal() {
		return valorOriginal;
	}

	public void setValorOriginal(BigDecimal valorOriginal) {
		this.valorOriginal = valorOriginal;
	}
    
    
}