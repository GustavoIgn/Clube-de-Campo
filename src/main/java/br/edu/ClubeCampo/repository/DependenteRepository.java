package br.edu.ClubeCampo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ClubeCampo.model.associado.Dependente;

public interface DependenteRepository extends JpaRepository<Dependente, Long> {
}