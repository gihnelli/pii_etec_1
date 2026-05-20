package database.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Partida;
import model.Resposta;
import model.tipos.NivelDificuldade;

public class PartidaDAO {

    /**
     * Inicia uma nova partida no banco de dados e retorna o ID gerado.
     */
    public int iniciarPartida(Partida partida) throws SQLException {
        String sql = "INSERT INTO partida (id_aluno, nivel_atual) VALUES (?, ?)";

        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, partida.getAluno().getId());
            ps.setString(2, partida.getNivelAtual().name());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    partida.setId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    /**
     * Salva as respostas de uma partida e chama a procedure para finalizar.
     */
    public void finalizarPartida(Partida partida) throws SQLException {
        String sqlResposta = "INSERT INTO resposta (id_partida, id_questao, id_alternativa, correta, tempo_resposta) VALUES (?, ?, ?, ?, ?)";
        
        Connection con = Conexao.getConnection();
        con.setAutoCommit(false);

        try {
            // 1. Salva as respostas
            try (PreparedStatement ps = con.prepareStatement(sqlResposta)) {
                for (Resposta resp : partida.getRespostas()) {
                    ps.setInt(1, partida.getId());
                    ps.setInt(2, resp.getQuestao().getId());
                    ps.setInt(3, resp.getAlternativaEscolhida().getId());
                    ps.setBoolean(4, resp.isCorreta());
                    ps.setInt(5, resp.getTempoResposta());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            // 2. Chama a procedure de finalização
            String sqlProc = "{CALL sp_finalizar_partida(?, ?)}";
            try (CallableStatement cs = con.prepareCall(sqlProc)) {
                cs.setInt(1, partida.getId());
                cs.setInt(2, partida.getPontuacao());
                cs.execute();
            }

            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
    }

    /**
     * Busca o histórico de partidas de um aluno.
     */
    public List<Partida> listarPorAluno(int idAluno) throws SQLException {
        String sql = "SELECT * FROM partida WHERE id_aluno = ? AND finalizada = TRUE ORDER BY data_hora_inicio DESC";
        List<Partida> lista = new ArrayList<>();

        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idAluno);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Partida p = new Partida();
                    p.setId(rs.getInt("id"));
                    p.setPontuacao(rs.getInt("pontuacao"));
                    p.setNivelAtual(NivelDificuldade.valueOf(rs.getString("nivel_atual")));
                    p.setFinalizada(true);
                    // Aqui poderíamos carregar as respostas também se necessário
                    lista.add(p);
                }
            }
        }
        return lista;
    }
}
