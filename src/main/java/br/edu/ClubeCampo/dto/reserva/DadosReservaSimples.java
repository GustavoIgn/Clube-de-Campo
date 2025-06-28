package br.edu.ClubeCampo.dto.reserva;

import java.time.LocalDate;

public record DadosReservaSimples(
    Long idReserva,
    LocalDate dataReserva,
    LocalDate dataEvento,
    Long idAssociado,
    String cpfAssociado,
    String nomeAssociado
) {}
