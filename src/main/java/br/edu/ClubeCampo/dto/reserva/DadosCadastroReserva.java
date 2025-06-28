package br.edu.ClubeCampo.dto.reserva;

import java.time.LocalDate;

public record DadosCadastroReserva(
    LocalDate dataEvento,
    String nomeArea,
    String tipoArea
) {}
