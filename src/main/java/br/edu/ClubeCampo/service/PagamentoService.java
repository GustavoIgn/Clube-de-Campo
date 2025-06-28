package br.edu.ClubeCampo.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ClubeCampo.dto.pagamento.DadosAtualizaPagamento;
import br.edu.ClubeCampo.dto.pagamento.DadosCadastroPagamento;
import br.edu.ClubeCampo.model.associado.Associado;
import br.edu.ClubeCampo.model.financeiro.Cobranca;
import br.edu.ClubeCampo.model.financeiro.Pagamento;
import br.edu.ClubeCampo.model.financeiro.StatusCobranca;
import br.edu.ClubeCampo.repository.AssociadoRepository;
import br.edu.ClubeCampo.repository.CobrancaRepository;
import br.edu.ClubeCampo.repository.PagamentoRepository;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepo;

    @Autowired
    private AssociadoRepository associadoRepo;

    @Autowired
    private CobrancaRepository cobrancaRepo;

    public List<Pagamento> listarTodos() {
        return pagamentoRepo.findAll();
    }

    public Optional<Pagamento> buscarPorId(Long id) {
        return pagamentoRepo.findById(id);
    }

    public Pagamento cadastrar(String cpfAssociado, Long idCobranca, DadosCadastroPagamento dados) {
        Associado associado = associadoRepo.findBycpf(cpfAssociado)
            .orElseThrow(() -> new RuntimeException("Associado não encontrado"));

        Cobranca cobranca = cobrancaRepo.findById(idCobranca)
            .orElseThrow(() -> new RuntimeException("Cobrança não encontrada"));

        if (cobranca.getStatus() != StatusCobranca.EM_ABERTO) {
            throw new RuntimeException("Pagamento só permitido para cobranças com status EM_ABERTO.");
        }

        BigDecimal valorPago = BigDecimal.valueOf(dados.valorPago());
        BigDecimal saldoAtual = cobranca.getValor();

        if (valorPago.compareTo(saldoAtual) > 0) {
            throw new RuntimeException("Pagamento excede o valor restante da cobrança. Valor atual: R$ " + saldoAtual);
        }

        // Subtrair o valor do pagamento
        BigDecimal novoSaldo = saldoAtual.subtract(valorPago);
        cobranca.setValor(novoSaldo);

        // Atualiza status se zerado
        if (novoSaldo.compareTo(BigDecimal.ZERO) == 0) {
            cobranca.setPaga(true);
            cobranca.setStatus(StatusCobranca.PAGA);
        }

        cobrancaRepo.save(cobranca);

        Pagamento pagamento = new Pagamento(dados, associado, cobranca);
        return pagamentoRepo.save(pagamento);
    }
    
    public Optional<Pagamento> atualizar(Long id, DadosAtualizaPagamento dados) {
        return pagamentoRepo.findById(id).map(pagamento -> {
            pagamento.atualizar(dados);
            return pagamentoRepo.save(pagamento);
        });
    }

    public void deletar(Long id) {
        pagamentoRepo.deleteById(id);
    }
}