package br.edu.ClubeCampo.model.associado;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.edu.ClubeCampo.dto.associado.DadosCadastroAssociado;
import br.edu.ClubeCampo.model.financeiro.Cobranca;
import br.edu.ClubeCampo.model.financeiro.Pagamento;
import br.edu.ClubeCampo.model.reserva.Reserva;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "associado")
public class Associado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String rg;
    @Column(unique = true, nullable = false)
    private String cpf;
    private String endereco;
    private String cep;
    private String bairro;
    private String cidade;
    private String estado;

    private String telefoneResidencial;
    private String telefoneComercial;
    private String celular;

    private LocalDate dataCadastro;

    @Enumerated(EnumType.STRING)
    private TipoAssociado tipo;

    private int mesesInadimplente;
    private boolean cartaoBloqueado;

    @OneToMany(mappedBy = "associado", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Dependente> dependentes = new ArrayList<>();

    @OneToMany(mappedBy = "associado", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Pagamento> pagamentos = new ArrayList<>();

    @OneToMany(mappedBy = "associado", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    @OneToMany(mappedBy = "associado", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Cobranca> cobrancas = new ArrayList<>();

    public Associado() {
    }

    public Associado(DadosCadastroAssociado dados) {
        this.nome = dados.nome();
        this.rg = dados.rg();
        this.cpf = dados.cpf();
        this.endereco = dados.endereco();
        this.cep = dados.cep();
        this.bairro = dados.bairro();
        this.cidade = dados.cidade();
        this.estado = dados.estado();
        this.telefoneResidencial = dados.telefoneResidencial();
        this.telefoneComercial = dados.telefoneComercial();
        this.celular = dados.celular();
        this.dataCadastro = dados.dataCadastro();
        this.tipo = dados.tipo();
        this.mesesInadimplente = dados.mesesInadimplente();
        this.cartaoBloqueado = dados.cartaoBloqueado();
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTelefoneResidencial() {
        return telefoneResidencial;
    }

    public void setTelefoneResidencial(String telefoneResidencial) {
        this.telefoneResidencial = telefoneResidencial;
    }

    public String getTelefoneComercial() {
        return telefoneComercial;
    }

    public void setTelefoneComercial(String telefoneComercial) {
        this.telefoneComercial = telefoneComercial;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public TipoAssociado getTipo() {
        return tipo;
    }

    public void setTipo(TipoAssociado tipo) {
        this.tipo = tipo;
    }

    public int getMesesInadimplente() {
        return mesesInadimplente;
    }

    public void setMesesInadimplente(int mesesInadimplente) {
        this.mesesInadimplente = mesesInadimplente;
    }

    public boolean isCartaoBloqueado() {
        return cartaoBloqueado;
    }

    public void setCartaoBloqueado(boolean cartaoBloqueado) {
        this.cartaoBloqueado = cartaoBloqueado;
    }

	public List<Dependente> getDependentes() {
		return dependentes;
	}

	public void setDependentes(List<Dependente> dependentes) {
		this.dependentes = dependentes;
	}

	public List<Pagamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}

	public List<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}

	public List<Cobranca> getCobrancas() {
		return cobrancas;
	}

	public void setCobrancas(List<Cobranca> cobrancas) {
		this.cobrancas = cobrancas;
	}
    
    
}
