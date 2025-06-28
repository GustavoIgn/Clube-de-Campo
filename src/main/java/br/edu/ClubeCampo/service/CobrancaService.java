package br.edu.ClubeCampo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ClubeCampo.model.associado.Associado;
import br.edu.ClubeCampo.model.financeiro.Cobranca;
import br.edu.ClubeCampo.model.financeiro.Pagamento;
import br.edu.ClubeCampo.model.financeiro.StatusCobranca;
import br.edu.ClubeCampo.repository.AssociadoRepository;
import br.edu.ClubeCampo.repository.CobrancaRepository;
import br.edu.ClubeCampo.repository.PagamentoRepository;

@Service
public class CobrancaService {

    @Autowired
    private CobrancaRepository cobrancaRepo;

    @Autowired
    private PagamentoRepository pagamentoRepo;

    @Autowired
    private AssociadoRepository associadoRepo;

    private static final BigDecimal VALOR_MENSALIDADE = new BigDecimal("100.00"); // ajuste se necessário

    public List<Cobranca> listarTodas() {
        return cobrancaRepo.findAll();
    }

    public Optional<Cobranca> buscarPorId(Long id) {
        return cobrancaRepo.findById(id);
    }

    // Gera uma cobrança padrão para todos os associados ativos
    public void gerarCobrancasParaTodos() {
        List<Associado> associados = associadoRepo.findAll();

        LocalDate hoje = LocalDate.now();
        LocalDate vencimento = hoje.withDayOfMonth(10).plusMonths(1);

        for (Associado associado : associados) {
            boolean jaExisteCobranca = cobrancaRepo
                .existsByAssociadoAndDataVencimento(associado, vencimento);

            if (!jaExisteCobranca) {
                Cobranca nova = new Cobranca(VALOR_MENSALIDADE, vencimento, associado);
                cobrancaRepo.save(nova);
            }
        }
    }
    
    public void atualizarStatusCobranca(Long idCobranca) {
        Cobranca cobranca = cobrancaRepo.findById(idCobranca)
            .orElseThrow(() -> new RuntimeException("Cobrança não encontrada"));

        // Soma todos os pagamentos dessa cobrança
        Double somaPagamentos = pagamentoRepo.findByCobrancaId(idCobranca).stream()
            .mapToDouble(Pagamento::getValorPago)
            .sum();

        if (somaPagamentos >= cobranca.getValor().doubleValue()) {
            cobranca.setPaga(true);
        } else {
            cobranca.setPaga(false);
        }

        cobrancaRepo.save(cobranca);
    }
    
    public void alterarStatusManual(Long idCobranca, String novoStatus) {
        Cobranca cobranca = cobrancaRepo.findById(idCobranca)
            .orElseThrow(() -> new RuntimeException("Cobrança não encontrada"));

        try {
            StatusCobranca statusEnum = StatusCobranca.valueOf(novoStatus.toUpperCase());
            cobranca.setStatus(statusEnum);
            cobrancaRepo.save(cobranca);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido. Use: EM_ABERTO, PAGA ou CANCELADA.");
        }
    }
}