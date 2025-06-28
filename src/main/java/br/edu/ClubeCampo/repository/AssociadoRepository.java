package br.edu.ClubeCampo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ClubeCampo.model.associado.Associado;

public interface AssociadoRepository extends JpaRepository<Associado, Long> {
	public Optional<Associado> findBycpf(String cpf); 
}