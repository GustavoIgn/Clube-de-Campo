package br.edu.ClubeCampo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ClubeCampo.model.reserva.AreaComTurmas;

@Repository
public interface AreaComTurmaRepository extends JpaRepository<AreaComTurmas, Long> {

}