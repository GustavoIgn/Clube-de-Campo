package br.edu.ClubeCampo.model.financeiro;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.ClubeCampo.dto.pagamento.DadosAtualizaPagamento;
import br.edu.ClubeCampo.dto.pagamento.DadosCadastroPagamento;
import br.edu.ClubeCampo.model.associado.Associado;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pagamentos")

public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataPagamento;

    private Double valorPago;
    
    @Column(nullable = false)
    private BigDecimal valor; // valor total da cobrança

    private String formaPagamento; // Ex: PIX, Cartão, Boleto etc.

    @ManyToOne
    @JoinColumn(name = "associado_id")
    private Associado associado;

    @ManyToOne
    @JoinColumn(name = "cobranca_id")
    private Cobranca cobranca;
    
    public Pagamento() {
		super();
	}

	public Pagamento(DadosCadastroPagamento dados, Associado associado, Cobranca cobranca) {
        this.dataPagamento = LocalDate.now();
        this.valorPago = dados.valorPago();
        this.formaPagamento = dados.formaPagamento();
        this.associado = associado;
        this.cobranca = cobranca;
        this.valor = cobranca.getValor(); // <<<<<<<<<<
    }

    public void atualizar(DadosAtualizaPagamento dados) {
        this.valorPago = dados.valorPago();
        this.formaPagamento = dados.formaPagamento();
        this.dataPagamento = dados.dataPagamento();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Double getValorPago() {
		return valorPago;
	}

	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public Associado getAssociado() {
		return associado;
	}

	public void setAssociado(Associado associado) {
		this.associado = associado;
	}

	public Cobranca getCobranca() {
		return cobranca;
	}

	public void setCobranca(Cobranca cobranca) {
		this.cobranca = cobranca;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
        
	
	
}
