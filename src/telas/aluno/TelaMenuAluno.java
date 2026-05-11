package telas.aluno;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import model.Alternativa;
import model.Partida;
import model.Questao;
import model.tipos.TipoQuestao;
import telas.jogo.TelaJogo;

public class TelaMenuAluno extends JFrame {

    public TelaMenuAluno() {
        configurarJanela();
        montarTela();
    }

    private void configurarJanela() {
        setTitle("Tela - Início");
        setSize(540, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void montarTela() {
        PainelFundo painelFundo = new PainelFundo();
        painelFundo.setLayout(null);
        setContentPane(painelFundo);

        JLabel botaoPerfil = new JLabel("👤", SwingConstants.CENTER);
        botaoPerfil.setBounds(10, 12, 35, 35);
        botaoPerfil.setFont(new Font("Arial", Font.PLAIN, 25));
        botaoPerfil.setForeground(Color.WHITE);
        botaoPerfil.setOpaque(true);
        botaoPerfil.setBackground(new Color(36, 73, 130));
        painelFundo.add(botaoPerfil);

        JLabel botaoSair = new JLabel("↪", SwingConstants.CENTER);
        botaoSair.setBounds(493, 12, 35, 35);
        botaoSair.setFont(new Font("Arial", Font.BOLD, 28));
        botaoSair.setForeground(Color.WHITE);
        botaoSair.setOpaque(true);
        botaoSair.setBackground(new Color(36, 73, 130));
        botaoSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoSair.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evento) {
                sairDaConta();
            }
        });
        painelFundo.add(botaoSair);

        JLabel titulo = new JLabel("LabQuest", SwingConstants.CENTER);
        titulo.setBounds(70, 85, 400, 90);
        titulo.setFont(new Font("Arial", Font.BOLD, 72));
        titulo.setForeground(new Color(31, 65, 126));
        painelFundo.add(titulo);

        JButton botaoJogar = criarBotaoMenu("Jogar");
        botaoJogar.setBounds(148, 225, 240, 38);
        botaoJogar.addActionListener((ActionEvent evento) -> abrirTelaJogo());
        painelFundo.add(botaoJogar);

        JButton botaoDesempenho = criarBotaoMenu("Desempenho");
        botaoDesempenho.setBounds(148, 272, 240, 38);
        botaoDesempenho.addActionListener((ActionEvent evento) -> abrirTelaDesempenho());
        painelFundo.add(botaoDesempenho);
    }

    private JButton criarBotaoMenu(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, 18));
        botao.setForeground(Color.WHITE);
        botao.setBackground(new Color(36, 73, 130));
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return botao;
    }

    private void abrirTelaJogo() {
        // Criando uma partida e questões de exemplo para o teste
        model.Aluno alunoExemplo = new model.Aluno(1, "Aluno Teste", "aluno@aluno.cps.sp.gov.br", "123", "1A", "12345");
        Partida partida = new Partida(alunoExemplo);
        List<Questao> lista = new ArrayList<>();
        
        // Questão 1: Alternativa
        Questao q1 = new Questao(1, "Qual instrumento é usado para medir volumes exatos?", TipoQuestao.MULTIPLA_ESCOLHA, model.tipos.NivelDificuldade.FACIL, "Química");
        q1.setImagemEnunciado("imagens/Béquer.jpg");
        q1.adicionarAlternativa(new Alternativa(1, "Béquer", false));
        q1.adicionarAlternativa(new Alternativa(2, "Pipeta Volumétrica", true));
        q1.adicionarAlternativa(new Alternativa(3, "Tubo de Ensaio", false));
        q1.adicionarAlternativa(new Alternativa(4, "Bastão de Vidro", false));
        lista.add(q1);

        // Questão 2: Associação
        Questao q2 = new Questao(2, "Combine os materiais aos sistemas correspondentes", TipoQuestao.ASSOCIACAO, model.tipos.NivelDificuldade.MEDIO, "Química");
        lista.add(q2);

        new TelaJogo(partida, lista).setVisible(true);
        dispose();
    }

    private void abrirTelaDesempenho() {
        JOptionPane.showMessageDialog(
                this,
                "Aqui será aberta a tela de desempenho do aluno.",
                "Desempenho",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Quando a TelaDesempenhoAluno estiver pronta, use:
        // new TelaDesempenhoAluno().setVisible(true);
        // dispose();
    }

    private void sairDaConta() {
        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja sair da conta?",
                "Sair",
                JOptionPane.YES_NO_OPTION
        );

        if (resposta == JOptionPane.YES_OPTION) {
            dispose();

            // Quando quiser voltar para o login, use:
            // new telas.autenticacao.TelaLogin().setVisible(true);
        }
    }

    private static class PainelFundo extends JPanel {
        private Image imagemFundo;

        public PainelFundo() {
            try {
                // Carrega a imagem de fundo menu.png
                imagemFundo = new ImageIcon("imagens/menu.png").getImage();
            } catch (Exception e) {
                System.err.println("Erro ao carregar imagem de fundo: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics grafico) {
            super.paintComponent(grafico);
            Graphics2D g2 = (Graphics2D) grafico.create();
            
            // Habilita interpolação de alta qualidade
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (imagemFundo != null) {
                int larguraPainel = getWidth();
                int alturaPainel = getHeight();
                int larguraImagem = imagemFundo.getWidth(this);
                int alturaImagem = imagemFundo.getHeight(this);

                if (larguraImagem > 0 && alturaImagem > 0) {
                    // Calcula a escala para cobrir todo o painel mantendo a proporção (tipo "cover")
                    double escalaX = (double) larguraPainel / larguraImagem;
                    double escalaY = (double) alturaPainel / alturaImagem;
                    double escala = Math.max(escalaX, escalaY);

                    int novaLargura = (int) (larguraImagem * escala);
                    int novaAltura = (int) (alturaImagem * escala);

                    // Centraliza a imagem no painel
                    int x = (larguraPainel - novaLargura) / 2;
                    int y = (alturaPainel - novaAltura) / 2;

                    g2.drawImage(imagemFundo, x, y, novaLargura, novaAltura, this);
                } else {
                    g2.drawImage(imagemFundo, 0, 0, larguraPainel, alturaPainel, this);
                }
            } else {
                g2.setColor(new Color(223, 239, 252));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
            
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaMenuAluno tela = new TelaMenuAluno();
            tela.setVisible(true);
        });
    }
}
