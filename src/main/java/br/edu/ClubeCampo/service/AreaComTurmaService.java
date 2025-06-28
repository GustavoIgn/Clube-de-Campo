package br.edu.ClubeCampo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.edu.ClubeCampo.dto.reserva.DadosCadastroReserva;
import br.edu.ClubeCampo.model.associado.Associado;
import br.edu.ClubeCampo.model.associado.Dependente;
import br.edu.ClubeCampo.model.reserva.AreaComTurmas;
import br.edu.ClubeCampo.model.reserva.IReservavel;
import br.edu.ClubeCampo.model.reserva.Reserva;
import br.edu.ClubeCampo.repository.AreaComTurmaRepository;
import br.edu.ClubeCampo.repository.AssociadoRepository;
import br.edu.ClubeCampo.repository.DependenteRepository;
import br.edu.ClubeCampo.repository.ReservaRepository;

@Service
public class AreaComTurmaService implements IReservavel{

    private final AreaComTurmaRepository areaRepo;
    private final ReservaRepository reservaRepo;
    private final AssociadoRepository associadoRepo;
    private final DependenteRepository dependenteRepo;

    public AreaComTurmaService(
        AreaComTurmaRepository areaRepo,
        ReservaRepository reservaRepo,
        AssociadoRepository associadoRepo,
        DependenteRepository dependenteRepo
    ) {
        this.areaRepo = areaRepo;
        this.reservaRepo = reservaRepo;
        this.associadoRepo = associadoRepo;
        this.dependenteRepo = dependenteRepo;
    }

    public List<AreaComTurmas> listarTodas() {
        return areaRepo.findAll();
    }

    public Optional<AreaComTurmas> buscarPorId(Long id) {
        return areaRepo.findById(id);
    }

    public AreaComTurmas salvar(AreaComTurmas areaComTurma) {
        return areaRepo.save(areaComTurma);
    }

    public void excluir(Long id) {
        areaRepo.deleteById(id);
    }

    public boolean reservar(Long idArea, Long idAssociado, LocalDate data) {
        Optional<AreaComTurmas> areaOpt = areaRepo.findById(idArea);
        Optional<Associado> associadoOpt = associadoRepo.findById(idAssociado);

        if (areaOpt.isEmpty() || associadoOpt.isEmpty()) {
            return false;
        }

        AreaComTurmas area = areaOpt.get();
        Associado associado = associadoOpt.get();

        long reservasNaData = area.getReservas().stream()
            .filter(r -> r.getDataEvento().equals(data))
            .count();

        long capacidadeTotal = (long) area.getTurmasDisponiveis() * area.getMaxPessoasPorTurma();

        if (reservasNaData < capacidadeTotal) {
            Reserva novaReserva = new Reserva(
                new DadosCadastroReserva(data, "Turmas", area.getNome()),
                associado
            );
            novaReserva.setAreaTurma(area);
            area.getReservas().add(novaReserva);
            reservaRepo.save(novaReserva);
            areaRepo.save(area);
            return true;
        } else {
            return false;
        }
    }

    public boolean inscreverDependente(Long idArea, Long idDependente, LocalDate data) {
        Optional<AreaComTurmas> areaOpt = areaRepo.findById(idArea);
        Optional<Dependente> dependenteOpt = dependenteRepo.findById(idDependente);

        if (areaOpt.isEmpty() || dependenteOpt.isEmpty()) {
            return false;
        }

        AreaComTurmas area = areaOpt.get();
        Dependente dependente = dependenteOpt.get();

        long reservasNaData = area.getReservas().stream()
            .filter(r -> r.getDataEvento().equals(data))
            .count();

        long capacidadeTotal = (long) area.getTurmasDisponiveis() * area.getMaxPessoasPorTurma();

        if (reservasNaData < capacidadeTotal) {
            Reserva novaReserva = new Reserva(
                new DadosCadastroReserva(data, area.getNome(), "AreaComTurmas"),
                dependente
            );
            novaReserva.setAreaTurma(area);
            area.getReservas().add(novaReserva);
            reservaRepo.save(novaReserva);
            areaRepo.save(area);
            return true;
        } else {
            return false;
        }
    }
}