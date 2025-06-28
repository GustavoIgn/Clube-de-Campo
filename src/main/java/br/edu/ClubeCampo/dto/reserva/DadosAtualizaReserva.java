package br.edu.ClubeCampo.dto.reserva;

import java.time.LocalDate;

public record DadosAtualizaReserva(Long id, LocalDate dataEvento, String nomeArea, String tipoArea) {
}
