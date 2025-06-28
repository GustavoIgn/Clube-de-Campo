package br.edu.ClubeCampo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ClubeCampo.model.reserva.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}