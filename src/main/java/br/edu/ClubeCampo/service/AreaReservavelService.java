package br.edu.ClubeCampo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.edu.ClubeCampo.dto.reserva.DadosCadastroReserva;
import br.edu.ClubeCampo.model.associado.Associado;
import br.edu.ClubeCampo.model.reserva.AreaReservavel;
import br.edu.ClubeCampo.model.reserva.IReservavel;
import br.edu.ClubeCampo.model.reserva.Reserva;
import br.edu.ClubeCampo.repository.AreaReservavelRepository;
import br.edu.ClubeCampo.repository.AssociadoRepository;
import br.edu.ClubeCampo.repository.ReservaRepository;

@Service
public class AreaReservavelService implements IReservavel {

    private final AreaReservavelRepository repository;
    private final ReservaRepository reservaRepo;
    private final AssociadoRepository associadoRepo;

    public AreaReservavelService(
        AreaReservavelRepository areaRepo,
        ReservaRepository reservaRepo,
        AssociadoRepository associadoRepo
    ) {
        this.repository = areaRepo;
        this.reservaRepo = reservaRepo;
        this.associadoRepo = associadoRepo;
    }

    public List<AreaReservavel> listarTodas() {
        return repository.findAll();
    }

    public Optional<AreaReservavel> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public AreaReservavel salvar(AreaReservavel areaReservavel) {
        return repository.save(areaReservavel);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
    
    @Override
    public boolean reservar(Long idArea, Long idAssociado, LocalDate data) {
        Optional<AreaReservavel> areaOpt = repository.findById(idArea);
        Optional<Associado> associadoOpt = associadoRepo.findById(idAssociado);

        if (areaOpt.isEmpty() || associadoOpt.isEmpty()) {
            return false; // Não encontrado
        }

        AreaReservavel area = areaOpt.get();
        Associado associado = associadoOpt.get();

        long reservasNaData = area.getReservas().stream()
            .filter(r -> r.getDataEvento().equals(data))
            .count();

        if (reservasNaData < area.getQuantidadeDisponivel()) {
            Reserva novaReserva = new Reserva(
                new DadosCadastroReserva(data, "Reservável", area.getNome()),
                associado
            );
            novaReserva.setAreaReservada(area);

            reservaRepo.save(novaReserva); // Persistir
            return true;
        } else {
            return false; // Sem disponibilidade
        }
    }
}