package br.edu.ClubeCampo.model.reserva;

import java.time.LocalDate;

import br.edu.ClubeCampo.dto.reserva.DadosAtualizaReserva;
import br.edu.ClubeCampo.dto.reserva.DadosCadastroReserva;
import br.edu.ClubeCampo.model.associado.Associado;
import br.edu.ClubeCampo.model.associado.Dependente;
import jakarta.persistence.*;

@Entity
@Table(name = "reserva")
public class Reserva {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDate dataReserva; // Quando foi feita a reserva

	@Column(nullable = false)
	private LocalDate dataEvento; // Quando o evento ocorrerá

	@Column(nullable = false)
	private String tipoArea;

	@Column(nullable = false)
	private String nomeArea;

	@ManyToOne
	@JoinColumn(name = "associado_id")
	private Associado associado;

	@ManyToOne
	@JoinColumn(name = "dependente_id")
	private Dependente dependente;

	@ManyToOne
	@JoinColumn(name = "area_reservavel_id")
	private AreaReservavel areaReservada;

	@ManyToOne
	@JoinColumn(name = "area_com_turmas_id")
	private AreaComTurmas areaTurma;

	public Reserva() {
	}

	public Reserva(DadosCadastroReserva dto, Associado associado) {
		this.associado = associado;
		this.dataReserva = LocalDate.now();
		this.dataEvento = dto.dataEvento();
		this.tipoArea = dto.tipoArea();
		this.nomeArea = dto.nomeArea();
	}

	public Reserva(DadosCadastroReserva dto, Dependente dependente) {
		this.dependente = dependente;
		this.dataReserva = LocalDate.now();
		this.dataEvento = dto.dataEvento();
		this.tipoArea = dto.tipoArea();
		this.nomeArea = dto.nomeArea();
	}

	public void atualizarDados(DadosAtualizaReserva dto) {
		if (dto.dataEvento() != null)
			this.dataEvento = dto.dataEvento();
		if (dto.tipoArea() != null)
			this.tipoArea = dto.tipoArea();
		if (dto.nomeArea() != null)
			this.nomeArea = dto.nomeArea();
	}

	// Getters e setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDataReserva() {
		return dataReserva;
	}

	public void setDataReserva(LocalDate dataReserva) {
		this.dataReserva = dataReserva;
	}

	public LocalDate getDataEvento() {
		return dataEvento;
	}

	public void setDataEvento(LocalDate dataEvento) {
		this.dataEvento = dataEvento;
	}

	public String getTipoArea() {
		return tipoArea;
	}

	public void setTipoArea(String tipoArea) {
		this.tipoArea = tipoArea;
	}

	public String getNomeArea() {
		return nomeArea;
	}

	public void setNomeArea(String nomeArea) {
		this.nomeArea = nomeArea;
	}

	public Associado getAssociado() {
		return associado;
	}

	public void setAssociado(Associado associado) {
		this.associado = associado;
	}

	public Dependente getDependente() {
		return dependente;
	}

	public void setDependente(Dependente dependente) {
		this.dependente = dependente;
	}

	public AreaReservavel getAreaReservada() {
		return areaReservada;
	}

	public void setAreaReservada(AreaReservavel areaReservada) {
		this.areaReservada = areaReservada;
	}

	public AreaComTurmas getAreaTurma() {
		return areaTurma;
	}

	public void setAreaTurma(AreaComTurmas areaTurma) {
		this.areaTurma = areaTurma;
	}
}