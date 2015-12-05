package br.com.coffeebeans.fachada;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import br.com.coffeebeans.atividade.Atividade;
import br.com.coffeebeans.atividade.AtividadeRealizada;
import br.com.coffeebeans.atividade.ControladorAtividade;
import br.com.coffeebeans.atividade.ControladorAtividadeRealizada;
import br.com.coffeebeans.dispositivo.ControladorDispositivo;
import br.com.coffeebeans.dispositivo.Dispositivo;
import br.com.coffeebeans.exception.*;
import br.com.coffeebeans.usuario.ControladorUsuario;
import br.com.coffeebeans.usuario.Usuario;

public class Fachada {
    private static Fachada instance = null;
    public static Context context;
    ControladorUsuario controladorUsuario;
    ControladorAtividade controladorAtividade;
    ControladorAtividadeRealizada controladorAtividadeRealizada;
    ControladorDispositivo controladorDispositivo;

    private Fachada(Context context) throws Exception {
        Fachada.context = context;
        this.controladorAtividade = new ControladorAtividade(context);
        this.controladorUsuario = new ControladorUsuario(context);
        this.controladorAtividadeRealizada = new ControladorAtividadeRealizada(context);
        this.controladorDispositivo = new ControladorDispositivo(context);
    }

    public static Fachada getInstance(Context context) throws Exception {
        if (Fachada.instance == null) {
            Fachada.instance = new Fachada(context);
        }
        return Fachada.instance;
    }

    public <E> void cadastrar(E element) throws SQLException, UsuarioJaExistenteException,
            ViolacaoChaveEstrangeiraException, AtividadeJaExistenteException,
            DAOException, PermissaoException, EmailJaExistenteException {

        if (element instanceof Usuario) {
            controladorUsuario.cadastrar((Usuario) element);
        } else if (element instanceof Atividade) {
            controladorAtividade.cadastrar((Atividade) element);
        } else if (element instanceof Dispositivo) {
            controladorDispositivo.cadastrar((Dispositivo) element);
        } else if (element instanceof AtividadeRealizada) {
            // controladorAtividadeRealizada.cadastrar((AtividadeRealizada)
            // element);
        }

    }

    public <E> void atualizar(E element)
            throws SQLException, UsuarioNaoEncontradoException, AtividadeNaoEncontradaException, DAOException, PermissaoException, AtividadeJaExistenteException, UsuarioJaExistenteException, EmailJaExistenteException, DispositivoNaoEncontradoException, DispositivoJaExistenteException {
        if (element instanceof Usuario) {
            controladorUsuario.atualizar((Usuario) element);
        } else if (element instanceof Atividade) {
            controladorAtividade.atualizar((Atividade) element);
        } else if (element instanceof Dispositivo) {
            controladorDispositivo.atualizar((Dispositivo) element);
        } else if (element instanceof AtividadeRealizada) {
            // controladorAtividadeRealizada.atualizar((AtividadeRealizada)
            // element);
        }

    }

    public void atividadeRealizadaRemover(int id)
            throws AtividadeNaoEncontradaException, SQLException, RepositorioException {
        // controladorAtividadeRealizada.excluir(id);
    }

    public void usuarioRemover(int id) throws SQLException, UsuarioNaoEncontradoException, DAOException, PermissaoException {
        controladorUsuario.remover(id);
    }

    public void atividadeRemover(int id) throws SQLException, AtividadeNaoEncontradaException, DAOException {
        controladorAtividade.remover(id);
    }

	/*
     * public List<AtividadeRealizada> atividadeRealizadaListar() throws
	 * SQLException, ListaVaziaException, RepositorioException { return
	 * controladorAtividadeRealizada.listar(); }
	 * 
	 * public List<AtividadeRealizada> atividadeRealizadaListar(int id) throws
	 * SQLException, ListaVaziaException, RepositorioException { return
	 * controladorAtividadeRealizada.listar(id); }
	 */

    public List<Atividade> atividadeListar() throws SQLException, DAOException {
        return controladorAtividade.listar();
    }

    public List<Usuario> getUsuarioLista() throws SQLException, DAOException {
        return controladorUsuario.getLista();

    }

	/*
     * public AtividadeRealizada atividadeRealizadaProcurar(int id) throws
	 * SQLException, AtividadeNaoEncontradaException, RepositorioException {
	 * return controladorAtividadeRealizada.procurar(id); }
	 */

    public Usuario usuarioProcurar(int id) throws SQLException, UsuarioNaoEncontradoException, DAOException {
        return controladorUsuario.procurar(id);
    }

    public Atividade atividadeProcurar(int id)
            throws SQLException, AtividadeNaoEncontradaException, DAOException {
        return controladorAtividade.procurar(id);
    }

    public boolean loginFacebook(String email) throws SQLException, DAOException {
        return controladorUsuario.loginFacebook(email);
    }

    public void alterarSenhaUsuario(int id, String senha)
            throws SQLException, UsuarioNaoEncontradoException, DAOException {
        controladorUsuario.alterarSenha(id, senha);
    }

    public boolean login(String usuario, String senha)
            throws UsuarioInativoException, SQLException, DAOException {
        return controladorUsuario.login(usuario, senha);
    }

    public Usuario getUsuarioLogado() throws SQLException, DAOException {

        return controladorUsuario.getUsuarioLogado();
    }

    public void logout() throws SQLException, DAOException {
        controladorUsuario.logout();
    }

    public Dispositivo dispositivoProcurar(int id) throws DispositivoNaoEncontradoException, SQLException, DAOException {
        return controladorDispositivo.procurar(id);
    }

    public Dispositivo dispositivoProcurarNome(String nome) throws DispositivoNaoEncontradoException, SQLException, DAOException {
        return controladorDispositivo.procurarNome(nome);
    }

    public List<Dispositivo> dispositivoListar() throws SQLException, DAOException {
        return controladorDispositivo.listar();
    }

    public void dispositivoRemover(int id) throws DispositivoNaoEncontradoException, SQLException, DAOException {
        controladorDispositivo.remover(id);
    }

    public void setDispositivoAtivo(Dispositivo dispositivoAtivo) throws DispositivoNaoEncontradoException, SQLException, DAOException {
        controladorDispositivo.setDispositivoAtivo(dispositivoAtivo);
    }

    public Dispositivo getDispositivoAtivo() throws SQLException, DAOException {
        return controladorDispositivo.getDispositivoAtivo();
    }
    /*
	 * public List<AtividadeRealizada> getUltimasAtividades() throws
	 * RepositorioException, SQLException { return
	 * controladorAtividadeRealizada.getUltimasAtividades(); }
	 */

}
