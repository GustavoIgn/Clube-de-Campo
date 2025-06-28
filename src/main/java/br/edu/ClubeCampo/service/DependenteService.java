
package br.edu.ClubeCampo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.edu.ClubeCampo.model.associado.Dependente;
import br.edu.ClubeCampo.repository.DependenteRepository;

@Service
public class DependenteService {

    private final DependenteRepository repository;

    public DependenteService(DependenteRepository repository) {
        this.repository = repository;
    }

    public Dependente salvar(Dependente dependente) {
        return repository.save(dependente);
    }

    public Optional<Dependente> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public List<Dependente> listarTodos() {
        return repository.findAll();
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}