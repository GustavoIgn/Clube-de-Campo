package br.edu.ClubeCampo.model.reserva;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "area_com_turmas")
public class AreaComTurmas {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	private int turmasDisponiveis;

	private int maxPessoasPorTurma;

	@OneToMany(mappedBy = "areaTurma", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Reserva> reservas = new ArrayList<>();

	// Construtores
	public AreaComTurmas() {
	}

	public AreaComTurmas(String nome, int turmasDisponiveis, int maxPessoasPorTurma) {
		this.nome = nome;
		this.turmasDisponiveis = turmasDisponiveis;
		this.maxPessoasPorTurma = maxPessoasPorTurma;
	}

	// Getters e setters
	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getTurmasDisponiveis() {
		return turmasDisponiveis;
	}

	public void setTurmasDisponiveis(int turmasDisponiveis) {
		this.turmasDisponiveis = turmasDisponiveis;
	}

	public int getMaxPessoasPorTurma() {
		return maxPessoasPorTurma;
	}

	public void setMaxPessoasPorTurma(int maxPessoasPorTurma) {
		this.maxPessoasPorTurma = maxPessoasPorTurma;
	}

	public List<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/*
	@Override
	public boolean reservar(Associado associado, LocalDate data) {
	    long quantidadeReservas = reservas.stream()
	        .filter(r -> r.getDataReserva().equals(data))
	        .count();

	    // Cada turma comporta até maxPessoasPorTurma
	    long totalPossivel = turmasDisponiveis * maxPessoasPorTurma;

	    if (quantidadeReservas < totalPossivel) {
	        Reserva novaReserva = new Reserva(
	            new DadosCadastroReserva(data, this.getNome(), "AreaComTurmas"),
	            associado
	        );
	        reservas.add(novaReserva);
	        return true;
	    } else {
	        return false; // Todas as turmas estão cheias
	    }
	}

	public boolean reservarSendoDependente(Dependente dependente, LocalDate data) {
	    long quantidadeReservas = reservas.stream()
	        .filter(r -> r.getDataReserva().equals(data))
	        .count();

	    // Cada turma comporta até maxPessoasPorTurma
	    long totalPossivel = turmasDisponiveis * maxPessoasPorTurma;

	    if (quantidadeReservas < totalPossivel) {
	        Reserva novaReserva = new Reserva(
	            new DadosCadastroReserva(data, this.getNome(), "AreaComTurmas"),
	            dependente
	        );
	        reservas.add(novaReserva);
	        return true;
	    } else {
	        return false; // Todas as turmas estão cheias
	    }
	}*/
}
