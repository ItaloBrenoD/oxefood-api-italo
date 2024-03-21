package br.com.ifpe.oxefood.model.entregador;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.oxefood.model.produto.Produto;
import jakarta.transaction.Transactional;

@Service
public class EntregadorService {
    
    @Autowired
    private EntregadorRepository repository;

    @Transactional
    public Entregador save(Entregador entregador) {
        
        entregador.setHabilitado(true);
        entregador.setVersao(1L);
        entregador.setDataCriacao(LocalDate.now());

        return repository.save(entregador);
    }

    public List<Entregador> listarTodos() {
  
        return repository.findAll();
    }

    public Entregador obterPorID(Long id) {

        return repository.findById(id).get();
    }
}