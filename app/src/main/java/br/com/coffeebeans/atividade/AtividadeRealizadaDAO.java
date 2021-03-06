package br.com.coffeebeans.atividade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.coffeebeans.exception.AtividadeJaExistenteException;
import br.com.coffeebeans.exception.AtividadeNaoEncontradaException;
import br.com.coffeebeans.exception.BDException;
import br.com.coffeebeans.exception.DAOException;
import br.com.coffeebeans.exception.ListaVaziaException;
import br.com.coffeebeans.exception.RepositorioException;
import br.com.coffeebeans.exception.ViolacaoChaveEstrangeiraException;

public class AtividadeRealizadaDAO implements IAtividadeRealizadaDAO {
	private String sistema = "mysql";
	private Connection conexao;

	public AtividadeRealizadaDAO() throws Exception {
		//this.conexao = Conexao.conectar(sistema);
	}

	@Override
	public void cadastrar(AtividadeRealizada atividadeRealizada)
			throws SQLException, AtividadeJaExistenteException,
			ViolacaoChaveEstrangeiraException, RepositorioException {
		PreparedStatement stmt = null;
		try {
			String sql = "INSERT INTO ATIVIDADE_REALIZADA(ID_ATIVIDADE, ID_USUARIO, GASTO, DATA_HORA_INICIO, DATA_HORA_FIM) "
					+ "VALUES(?, ?, ?, ?, ?)";
			stmt = this.conexao.prepareStatement(sql);
			stmt.setInt(1, atividadeRealizada.getAtividade().getId());
			stmt.setInt(2, atividadeRealizada.getUsuario().getId());
			stmt.setDouble(3, atividadeRealizada.getGasto());
			stmt.setTimestamp(4, new Timestamp(atividadeRealizada
					.getDataHoraInicio().getTime()));
			stmt.setTimestamp(5, new Timestamp(atividadeRealizada
					.getDataHoraFim().getTime()));
			stmt.execute();
		} catch (Exception e) {
			throw new RepositorioException(e);
		} finally {
			stmt.close();
		}

	}

	@Override
	public AtividadeRealizada procurar(int id) throws SQLException,
			AtividadeNaoEncontradaException, RepositorioException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		AtividadeRealizada atividadeRealizada = null;
		try {
			String sql = "SELECT * FROM ATIVIDADE_REALIZADA WHERE ID=?";
			stmt = this.conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				atividadeRealizada = new AtividadeRealizada(
						rs.getTimestamp("DATA_HORA_INICIO"),
						rs.getTimestamp("DATA_HORA_FIM"),
						rs.getInt("ID_ATIVIDADE"), rs.getInt("ID_USUARIO"),
						rs.getDouble("GASTO"));
				atividadeRealizada.setId(rs.getInt("ID"));
			}
		} catch (Exception e) {
			throw new RepositorioException(e);
		} finally {
			stmt.close();
			rs.close();
		}
		return atividadeRealizada;
	}

	@Override
	public List<AtividadeRealizada> procurar(String descricao) throws SQLException,
			AtividadeNaoEncontradaException, RepositorioException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		AtividadeRealizada atividadeRealizada = null;
		List<AtividadeRealizada> list = new ArrayList<AtividadeRealizada>();

		try {
			String sql = "SELECT ar.DATA_HORA_INICIO,ar.DATA_HORA_FIM,ar.ID_ATIVIDADE,ar.ID_USUARIO,ar.GASTO,ar.ID FROM ATIVIDADE_REALIZADA as ar,atividade as a where a.ID=ar.id_atividade and a.descricao=?";
			stmt = this.conexao.prepareStatement(sql);
			stmt.setString(1, descricao);
			rs = stmt.executeQuery();
			while (rs.next()) {
				atividadeRealizada = new AtividadeRealizada(
						rs.getTimestamp("DATA_HORA_INICIO"),
						rs.getTimestamp("DATA_HORA_FIM"),
						rs.getInt("ID_ATIVIDADE"), rs.getInt("ID_USUARIO"),
						rs.getDouble("GASTO"));
				atividadeRealizada.setId(rs.getInt("ID"));
				list.add(atividadeRealizada);
			}
		} catch (Exception e) {
			throw new RepositorioException(e);
		} finally {
			stmt.close();
			rs.close();
		}
		return list;
	}

	@Override
	public void atualizar(AtividadeRealizada atividadeRealizada)
			throws AtividadeNaoEncontradaException, SQLException,
			RepositorioException {
		PreparedStatement stmt = null;
		try {
			String sql = "UPDATE ATIVIDADE_REALIZADA SET DATA_HORA_INICIO = ?, DATA_HORA_FIM = ?, ID_ATIVIDADE = ?, ID_USUARIO = ?, GASTO = ? WHERE ID = ?";
			stmt = this.conexao.prepareStatement(sql);
			stmt.setTimestamp(1, new Timestamp(atividadeRealizada
					.getDataHoraInicio().getTime()));
			stmt.setTimestamp(2, new Timestamp(atividadeRealizada
					.getDataHoraFim().getTime()));
			stmt.setInt(5, atividadeRealizada.getAtividade().getId());
			stmt.setInt(6, atividadeRealizada.getUsuario().getId());
			stmt.setDouble(7, atividadeRealizada.getGasto());
			stmt.setInt(8, atividadeRealizada.getId());
			Integer resultado = stmt.executeUpdate();
			if (resultado == 0) {
				throw new AtividadeNaoEncontradaException();
			}
		} catch (Exception e) {
			throw new RepositorioException(e);
		} finally {
			stmt.close();
		}
	}

	@Override
	public void excluir(int id) throws AtividadeNaoEncontradaException,
			SQLException, RepositorioException {
		PreparedStatement stmt = null;
		try {
			String sql = "DELETE FROM ATIVIDADE_REALIZADA WHERE ID=?";
			stmt = this.conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.execute();
		} catch (Exception e) {
			throw new RepositorioException(e);
		} finally {
			stmt.close();
		}

	}

	@Override
	public List<AtividadeRealizada> listar() throws SQLException,
			ListaVaziaException, RepositorioException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		AtividadeRealizada atividadeRealizada = null;
		List<AtividadeRealizada> atividades = new ArrayList<AtividadeRealizada>();
		try {
			String sql = "SELECT * FROM ATIVIDADE_REALIZADA ORDER BY DATA_HORA_FIM DESC";
			stmt = this.conexao.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				atividadeRealizada = new AtividadeRealizada(
						rs.getTimestamp("DATA_HORA_INICIO"),
						rs.getTimestamp("DATA_HORA_FIM"),
						rs.getInt("ID_USUARIO"), rs.getInt("ID_ATIVIDADE"),
						rs.getDouble("GASTO"));
				atividadeRealizada.setId(rs.getInt("ID"));
				atividades.add(atividadeRealizada);
			}

		} catch (Exception e) {
			throw new RepositorioException(e);
		} finally {
			stmt.close();
			rs.close();
		}
		return atividades;
	}

	@Override
	public List<AtividadeRealizada> listar(int id) throws SQLException,
			ListaVaziaException, RepositorioException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		AtividadeRealizada atividadeRealizada = null;
		List<AtividadeRealizada> atividades = new ArrayList<AtividadeRealizada>();
		try {
			String sql = "SELECT * FROM ATIVIDADE_REALIZADA WHERE ID_USUARIO = ? ORDER BY DATA_HORA_FIM DESC";
			stmt = this.conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				atividadeRealizada = new AtividadeRealizada(
						rs.getTimestamp("DATA_HORA_INICIO"),
						rs.getTimestamp("DATA_HORA_FIM"),
						rs.getInt("ID_USUARIO"), rs.getInt("ID_ATIVIDADE"),
						rs.getDouble("GASTO"));
				atividadeRealizada.setId(rs.getInt("ID"));
				atividades.add(atividadeRealizada);
			}

		} catch (Exception e) {
			throw new RepositorioException(e);
		} finally {
			stmt.close();
			rs.close();
		}
		return atividades;
	}

	@Override
	public boolean existe(int id_usuario, int id_atividade, Date dataHoraInicio, Date dataHoraFim) throws SQLException, DAOException {
		boolean existe = false;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			// this.connection = Conexao.conectar(sistema);
			if (conexao != null) {
				String sql = "SELECT * FROM ATIVIDADE_REALIZADA WHERE ID_USUARIO = ? AND ID_ATIVIDADE=? AND DATA_HORA_INICIO=? AND DATA_HORA_FIM=? ";
				stmt = this.conexao.prepareStatement(sql);
				stmt.setInt(1, id_usuario);
				stmt.setInt(2, id_atividade);
				stmt.setTimestamp(3, new Timestamp(dataHoraInicio.getTime()));
				stmt.setTimestamp(4, new Timestamp(dataHoraFim.getTime()));

				rs = stmt.executeQuery();
				if (rs.next()) {
					existe = true;
				}
			} else
				throw new BDException();
		} catch (SQLException e) {
			throw new SQLException(e);
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			if (stmt != null)
				stmt.close();
			if (rs != null)
				rs.close();
			/*
			 * if (conexao != null) if (!conexao.isClosed())
			 * this.conexao.close();
			 */

		}

		return existe;

	}

	public List<AtividadeRealizada> getUltimasAtividades()
			throws RepositorioException, SQLException {
		List<AtividadeRealizada> atividades = new ArrayList<AtividadeRealizada>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		AtividadeRealizada atividadeRealizada = null;
		try {
			String sql = "SELECT * FROM ATIVIDADE_REALIZADA ORDER BY DATA_HORA_FIM DESC LIMIT 3";
			stmt = this.conexao.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				atividadeRealizada = new AtividadeRealizada(
						rs.getTimestamp("DATA_HORA_INICIO"),
						rs.getTimestamp("DATA_HORA_FIM"),
						rs.getInt("ID_USUARIO"), rs.getInt("ID_ATIVIDADE"),
						rs.getDouble("GASTO"));
				atividadeRealizada.setId(rs.getInt("ID"));
				atividades.add(atividadeRealizada);
			}
		} catch (SQLException e) {
			throw new RepositorioException(e);
		} catch (Exception e) {
			throw new RepositorioException(e);
		} finally {
			stmt.close();
			rs.close();
		}
		return atividades;
	}
}
