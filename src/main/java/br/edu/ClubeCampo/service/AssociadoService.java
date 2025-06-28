package br.edu.ClubeCampo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ClubeCampo.model.associado.Associado;
import br.edu.ClubeCampo.repository.AssociadoRepository;

@Service
public class AssociadoService {

	@Autowired
	private AssociadoRepository repository;

	public Associado salvar(Associado associado) {
		return repository.save(associado);
	}

	public Optional<Associado> buscarPorId(Long id) {
		return repository.findById(id);
	}

	public List<Associado> listarTodos() {
		return repository.findAll();
	}

	public void deletar(Long id) {
		repository.deleteById(id);
	}
	
	public Optional<Associado> buscarPorCPF(String CPF){
		return repository.findBycpf(CPF);
	}
}