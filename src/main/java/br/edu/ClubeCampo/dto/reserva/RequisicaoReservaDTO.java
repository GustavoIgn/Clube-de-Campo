package br.edu.ClubeCampo.dto.reserva;

import java.time.LocalDate;

public record RequisicaoReservaDTO (Long idArea, Long idAssociado, LocalDate dataReserva)
{}