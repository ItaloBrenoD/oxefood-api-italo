package br.com.ifpe.oxefood.model.cliente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.oxefood.model.acesso.Usuario;
import br.com.ifpe.oxefood.model.acesso.UsuarioService;
import br.com.ifpe.oxefood.model.mensagens.EmailService;
import br.com.ifpe.oxefood.util.exception.ClienteException;
import jakarta.transaction.Transactional;

@Service
public class ClienteService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EnderecoClienteRepository enderecoClienteRepository;

    @Autowired
    private ClienteRepository repository;
    @Autowired
    private UsuarioService usuarioService;

    @Transactional
    public Cliente save(Cliente cliente, Usuario usuarioLogado) {

        usuarioService.save(cliente.getUsuario());

        if (!cliente.getFoneCelular().startsWith("(81")) {
            throw new ClienteException(ClienteException.MSG_START_81);
        }

        cliente.setHabilitado(Boolean.TRUE);
        cliente.setVersao(1L);
        cliente.setDataCriacao(LocalDate.now());
        cliente.setCriadoPor(usuarioLogado);
        Cliente clienteAlterado = repository.save(cliente);

        emailService.enviarEmailConfirmacaoCadastroCliente(clienteAlterado);
        return clienteAlterado;
    }

    public List<Cliente> listarTodos() {

        return repository.findAll();
    }

    public Cliente obterPorID(Long id) {

        return repository.findById(id).get();
    }

    @Transactional
    public void update(Long id, Cliente clienteAlterado, Usuario usuarioLogado) {

        Cliente cliente = repository.findById(id).get();
        cliente.setNome(clienteAlterado.getNome());
        cliente.setDataNascimento(clienteAlterado.getDataNascimento());
        cliente.setCpf(clienteAlterado.getCpf());
        cliente.setFoneCelular(clienteAlterado.getFoneCelular());
        cliente.setFoneFixo(clienteAlterado.getFoneFixo());

        cliente.setVersao(cliente.getVersao() + 1);
        cliente.setDataUltimaModificacao(LocalDate.now());
        cliente.setUltimaModificacaoPor(usuarioLogado);

        repository.save(cliente);
    }

    @Transactional
    public void delete(Long id) {

        Cliente cliente = repository.findById(id).get();
        cliente.setHabilitado(Boolean.FALSE);
        cliente.setVersao(cliente.getVersao() + 1);

        repository.save(cliente);
    }

    @Transactional
    public EnderecoCliente adicionarEnderecoCliente(Long clienteId, EnderecoCliente endereco) {

        Cliente cliente = this.obterPorID(clienteId);

        // Primeiro salva o EnderecoCliente:

        endereco.setCliente(cliente);
        endereco.setHabilitado(Boolean.TRUE);
        enderecoClienteRepository.save(endereco);

        // Depois acrescenta o endereço criado ao cliente e atualiza o cliente:

        List<EnderecoCliente> listaEnderecoCliente = cliente.getEnderecos();

        if (listaEnderecoCliente == null) {
            listaEnderecoCliente = new ArrayList<EnderecoCliente>();
        }

        listaEnderecoCliente.add(endereco);
        cliente.setEnderecos(listaEnderecoCliente);
        cliente.setVersao(cliente.getVersao() + 1);
        repository.save(cliente);

        return endereco;
    }

    @Transactional
    public EnderecoCliente atualizarEnderecoCliente(Long id, EnderecoCliente enderecoAlterado) {

        EnderecoCliente endereco = enderecoClienteRepository.findById(id).get();
        endereco.setRua(enderecoAlterado.getRua());
        endereco.setNumero(enderecoAlterado.getNumero());
        endereco.setBairro(enderecoAlterado.getBairro());
        endereco.setCep(enderecoAlterado.getCep());
        endereco.setCidade(enderecoAlterado.getCidade());
        endereco.setEstado(enderecoAlterado.getEstado());
        endereco.setComplemento(enderecoAlterado.getComplemento());

        return enderecoClienteRepository.save(endereco);
    }

    @Transactional
    public void removerEnderecoCliente(Long id) {

        EnderecoCliente endereco = enderecoClienteRepository.findById(id).get();
        endereco.setHabilitado(Boolean.FALSE);
        enderecoClienteRepository.save(endereco);

        Cliente cliente = this.obterPorID(endereco.getCliente().getId());
        cliente.getEnderecos().remove(endereco);
        cliente.setVersao(cliente.getVersao() + 1);
        repository.save(cliente);
    }

    public List<Cliente> filtrar(String cpf, String nome) {

        List<Cliente> listaClientes = repository.findAll();

        if ((cpf != null && !"".equals(cpf)) && (nome == null || "".equals(nome))) {
            listaClientes = repository.buscarPorCpf(cpf);
        } else if ((nome != null && !"".equals(nome)) && (cpf == null || "".equals(cpf))) {
            listaClientes = repository.buscarPorNome(nome);
        } else if ((nome != null && !"".equals(nome)) && (cpf != null && !"".equals(cpf))) {
            listaClientes = repository.consultarPorCpfENome(cpf, nome);
        }

        return listaClientes;
    }
}
