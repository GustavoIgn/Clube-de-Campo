package br.edu.ClubeCampo.model.reserva;

import java.time.LocalDate;

public interface IReservavel {
	boolean reservar(Long idArea, Long idAssociado, LocalDate data);
}
