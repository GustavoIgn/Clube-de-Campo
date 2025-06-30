package br.edu.ClubeCampo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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

	public ReservaService(AreaReservavelRepository areaReservavelRepo, ReservaRepository reservaRepo,
			AssociadoRepository associadoRepo, AreaComTurmaRepository areaRepo, DependenteRepository dependenteRepo) {
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

	public String reservarArea(Long idArea, Long idAssociado, LocalDate dataEvento) {
		Optional<AreaReservavel> areaOpt = areaReservavelRepo.findById(idArea);
		Optional<Associado> associadoOpt = associadoRepo.findById(idAssociado);

		if (areaOpt.isEmpty() || associadoOpt.isEmpty())
			return "Área ou associado não encontrado.";

		AreaReservavel area = areaOpt.get();
		Associado associado = associadoOpt.get();

		int mesesAtraso = calcularMesesInadimplentes(associado);

		// Aplica regras de restrição por inadimplência
		if (!podeReservar(area.getNome(), mesesAtraso)) {
			return "Reserva recusada: associado com cobranças vencidas há " + mesesAtraso + " meses.";
		}

		long reservasNaData = area.getReservas().stream().filter(r -> r.getDataEvento().equals(dataEvento)).count();

		if (reservasNaData < area.getQuantidadeDisponivel()) {
			Reserva novaReserva = new Reserva(new DadosCadastroReserva(dataEvento, area.getNome(), "Reservável"),
					associado);
			novaReserva.setAreaReservada(area);
			reservaRepo.save(novaReserva);
			return "Reserva realizada com sucesso.";
		}
		return "Data indisponível para reserva.";
	}

	public String inscreverAssociado(Long idArea, Long idAssociado, LocalDate data) {
		Optional<AreaComTurmas> areaOpt = areaRepo.findById(idArea);
		Optional<Associado> associadoOpt = associadoRepo.findById(idAssociado);

		if (areaOpt.isEmpty() || associadoOpt.isEmpty())
			return "Área ou associado não encontrado.";

		AreaComTurmas area = areaOpt.get();
		Associado associado = associadoOpt.get();

		int mesesAtraso = calcularMesesInadimplentes(associado);

		// Aplica regras de restrição por inadimplência
		if (!podeReservar(area.getNome(), mesesAtraso)) {
			return "Inscrição recusada: associado com cobranças vencidas há " + mesesAtraso + " meses.";
		}
		;

		long reservasNaData = area.getReservas().stream().filter(r -> r.getDataEvento().equals(data)).count();

		long capacidadeTotal = (long) area.getTurmasDisponiveis() * area.getMaxPessoasPorTurma();

		if (reservasNaData < capacidadeTotal) {
			Reserva novaReserva = new Reserva(new DadosCadastroReserva(data, area.getNome(), "AreaComTurmas"),
					associado);
			novaReserva.setAreaTurma(area);
			area.getReservas().add(novaReserva);
			reservaRepo.save(novaReserva);
			areaRepo.save(area);
			return "Inscrição realizada com sucesso.";
		}
		return "Data indisponível para inscrição.";
	}

	public String inscreverDependente(Long idArea, Long idDependente, LocalDate data) {
		Optional<AreaComTurmas> areaOpt = areaRepo.findById(idArea);
		Optional<Dependente> dependenteOpt = dependenteRepo.findById(idDependente);

		if (areaOpt.isEmpty() || dependenteOpt.isEmpty())
			return "Área ou dependente não encontrado.";

		AreaComTurmas area = areaOpt.get();
		Dependente dependente = dependenteOpt.get();

		Associado associado = dependente.getAssociado();
		int mesesAtraso = calcularMesesInadimplentes(associado);

		// Aplica regras de restrição por inadimplência
		if (!podeReservar(area.getNome(), mesesAtraso)) {
			return "Inscrição recusada: associado com cobranças vencidas há " + mesesAtraso + " meses.";
		}
		;

		long reservasNaData = area.getReservas().stream().filter(r -> r.getDataEvento().equals(data)).count();

		long capacidadeTotal = (long) area.getTurmasDisponiveis() * area.getMaxPessoasPorTurma();

		if (reservasNaData < capacidadeTotal) {
			Reserva novaReserva = new Reserva(new DadosCadastroReserva(data, area.getNome(), "AreaComTurmas"),
					dependente);
			novaReserva.setAreaTurma(area);
			area.getReservas().add(novaReserva);
			reservaRepo.save(novaReserva);
			areaRepo.save(area);
			return "Inscrição realizada com sucesso.";
		}
		return "Data indisponível para inscrição.";
	}

	private int calcularMesesInadimplentes(Associado associado) {
		LocalDate hoje = LocalDate.now();

		// Buscar a cobrança mais antiga não paga
		Optional<LocalDate> vencimentoMaisAntigoOpt = associado.getCobrancas().stream()
				.filter(c -> !c.isPaga() && c.getDataVencimento() != null).map(c -> c.getDataVencimento())
				.min(LocalDate::compareTo);

		if (vencimentoMaisAntigoOpt.isEmpty()) {
			// Nenhuma cobrança em atraso
			associado.setMesesInadimplente(0);
			associado.setCartaoBloqueado(false);
			associadoRepo.save(associado);
			return 0;
		}

		LocalDate vencimentoMaisAntigo = vencimentoMaisAntigoOpt.get();

		int diferencaMeses = (hoje.getYear() - vencimentoMaisAntigo.getYear()) * 12
				+ (hoje.getMonthValue() - vencimentoMaisAntigo.getMonthValue());

		// Se ainda não passou o mês do vencimento, considera 0
		if (hoje.getDayOfMonth() < vencimentoMaisAntigo.getDayOfMonth()) {
			diferencaMeses--;
		}

		int mesesAtraso = Math.max(diferencaMeses, 0);

		// Atualiza o associado
		associado.setMesesInadimplente(mesesAtraso);
		associado.setCartaoBloqueado(mesesAtraso >= 4);
		associadoRepo.save(associado);

		return mesesAtraso;
	}

	private boolean podeReservar(String nomeArea, int mesesAtraso) {
		String nome = nomeArea.toLowerCase();

		if (mesesAtraso >= 4)
			return false;
		if (mesesAtraso == 3 && !nome.contains("quadra"))
			return false;
		if (mesesAtraso == 2 && (nome.contains("haras") || nome.contains("golfe") || nome.contains("piscina"))) {
			return false;
		}
		return true;
	}
}