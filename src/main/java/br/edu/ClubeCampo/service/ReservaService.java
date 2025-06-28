package br.edu.ClubeCampo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.edu.ClubeCampo.dto.reserva.DadosAtualizaReserva;
import br.edu.ClubeCampo.dto.reserva.DadosCadastroReserva;
import br.edu.ClubeCampo.model.associado.Associado;
import br.edu.ClubeCampo.model.associado.Dependente;
import br.edu.ClubeCampo.model.reserva.AreaComTurmas;
import br.edu.ClubeCampo.model.reserva.AreaReservavel;
import br.edu.ClubeCampo.model.reserva.Reserva;
import br.edu.ClubeCampo.repository.AreaComTurmaRepository;
import br.edu.ClubeCampo.repository.AreaReservavelRepository;
import br.edu.ClubeCampo.repository.AssociadoRepository;
import br.edu.ClubeCampo.repository.DependenteRepository;
import br.edu.ClubeCampo.repository.ReservaRepository;

@Service
public class ReservaService {

    private final AreaReservavelRepository areaReservavelRepo;
    private final ReservaRepository reservaRepo;
    private final AssociadoRepository associadoRepo;
    private final AreaComTurmaRepository areaRepo;
    private final DependenteRepository dependenteRepo;

    public ReservaService(
            AreaReservavelRepository areaReservavelRepo,
            ReservaRepository reservaRepo,
            AssociadoRepository associadoRepo,
            AreaComTurmaRepository areaRepo,
            DependenteRepository dependenteRepo
    ) {
        this.areaReservavelRepo = areaReservavelRepo;
        this.reservaRepo = reservaRepo;
        this.associadoRepo = associadoRepo;
        this.areaRepo = areaRepo;
        this.dependenteRepo = dependenteRepo;
    }

    public Optional<Reserva> buscarPorId(Long id) {
        return reservaRepo.findById(id);
    }

    public List<Reserva> listarTodos() {
        return reservaRepo.findAll();
    }

    public void deletar(Long id) {
        reservaRepo.deleteById(id);
    }

    public Optional<Reserva> atualizar(DadosAtualizaReserva dados) {
        return reservaRepo.findById(dados.id()).map(reserva -> {
            reserva.atualizarDados(dados);
            return reservaRepo.save(reserva);
        });
    }

    public boolean reservarArea(Long idArea, Long idAssociado, LocalDate dataEvento) {
        Optional<AreaReservavel> areaOpt = areaReservavelRepo.findById(idArea);
        Optional<Associado> associadoOpt = associadoRepo.findById(idAssociado);

        if (areaOpt.isEmpty() || associadoOpt.isEmpty()) return false;

        AreaReservavel area = areaOpt.get();
        Associado associado = associadoOpt.get();

        int mesesAtraso = calcularMesesInadimplentes(associado);
        
        // Aplica regras de restrição por inadimplência
        if (!podeReservar(area.getNome(), mesesAtraso)) return false;

        long reservasNaData = area.getReservas().stream()
                .filter(r -> r.getDataEvento().equals(dataEvento))
                .count();

        if (reservasNaData < area.getQuantidadeDisponivel()) {
            Reserva novaReserva = new Reserva(
                new DadosCadastroReserva(dataEvento, "Reservável", area.getNome()),
                associado
            );
            novaReserva.setAreaReservada(area);
            reservaRepo.save(novaReserva);
            return true;
        }
        return false;
    }

    public boolean inscreverAssociado(Long idArea, Long idAssociado, LocalDate data) {
        Optional<AreaComTurmas> areaOpt = areaRepo.findById(idArea);
        Optional<Associado> associadoOpt = associadoRepo.findById(idAssociado);

        if (areaOpt.isEmpty() || associadoOpt.isEmpty()) return false;

        AreaComTurmas area = areaOpt.get();
        Associado associado = associadoOpt.get();

        int mesesAtraso = calcularMesesInadimplentes(associado);

        // Aplica regras de restrição por inadimplência nas inscrições também
        if (!podeReservar(area.getNome(), mesesAtraso)) return false;

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
        }
        return false;
    }

    public boolean inscreverDependente(Long idArea, Long idDependente, LocalDate data) {
        Optional<AreaComTurmas> areaOpt = areaRepo.findById(idArea);
        Optional<Dependente> dependenteOpt = dependenteRepo.findById(idDependente);

        if (areaOpt.isEmpty() || dependenteOpt.isEmpty()) return false;

        AreaComTurmas area = areaOpt.get();
        Dependente dependente = dependenteOpt.get();

        Associado associado = dependente.getAssociado();
        int mesesAtraso = calcularMesesInadimplentes(associado);
        
        // Aplica regras de restrição por inadimplência nas inscrições também
        if (!podeReservar(area.getNome(), mesesAtraso)) return false;

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
        }
        return false;
    }

    private int calcularMesesInadimplentes(Associado associado) {
        LocalDate hoje = LocalDate.now();
        int meses = 0;

        for (int i = 1; i <= 4; i++) {
            LocalDate mesVerificado = hoje.minusMonths(i).withDayOfMonth(1);

            boolean pagou = associado.getPagamentos().stream()
                .anyMatch(p -> p.getDataPagamento() != null &&
                        p.getDataPagamento().getMonth() == mesVerificado.getMonth() &&
                        p.getDataPagamento().getYear() == mesVerificado.getYear());

            if (!pagou) meses++;
        }

        return meses;
    }

    private boolean podeReservar(String nomeArea, int mesesAtraso) {
        String nome = nomeArea.toLowerCase();

        if (mesesAtraso >= 4) return false;
        if (mesesAtraso == 3 && !nome.contains("quadra")) return false;
        if (mesesAtraso == 2 && (nome.contains("haras") || nome.contains("golfe") || nome.contains("piscina"))) {
            return false;
        }
        return true;
    }
}