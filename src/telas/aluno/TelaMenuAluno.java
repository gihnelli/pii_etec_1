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

    private model.Aluno aluno;

    public TelaMenuAluno(model.Aluno aluno) {
        this.aluno = aluno;
        configurarJanela();
        montarTela();
    }

    public TelaMenuAluno() {
        this(new model.Aluno(1, "Aluno Teste", "aluno@aluno.cps.sp.gov.br", "123", "1A", "12345"));
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

        JButton botaoPerfil = criarBotaoIconeReal("imagens/Perfil.png");
        botaoPerfil.setBounds(20, 20, 45, 45);
        botaoPerfil.addActionListener(e -> abrirPerfil());
        painelFundo.add(botaoPerfil);

        JButton botaoSair = criarBotaoIconeReal("imagens/Sair.png");
        botaoSair.setBounds(475, 20, 45, 45);
        botaoSair.addActionListener(e -> sairDaConta());
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

    private JButton criarBotaoIconeReal(String caminho) {
        JButton btn = new JButton();
        try {
            ImageIcon iconOriginal = new ImageIcon(caminho);
            Image img = iconOriginal.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            btn.setText("?");
        }
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void abrirPerfil() {
        if (this.aluno != null) {
            JOptionPane.showMessageDialog(
                this,
                "Nome: " + this.aluno.getNome() + "\nE-mail: " + this.aluno.getEmail(),
                "Perfil do Aluno",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(this, "Informações do aluno não encontradas.");
        }
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
        Partida partida = new Partida(this.aluno);
        List<Questao> lista = new ArrayList<>();

        Questao q1 = new Questao(1, "Qual instrumento é usado para medir volumes exatos?", TipoQuestao.MULTIPLA_ESCOLHA, model.tipos.NivelDificuldade.FACIL, "Química");
        q1.setImagemEnunciado("imagens/Béquer.jpg");
        q1.adicionarAlternativa(new Alternativa(1, "Béquer", false));
        q1.adicionarAlternativa(new Alternativa(2, "Pipeta Volumétrica", true));
        q1.adicionarAlternativa(new Alternativa(3, "Tubo de Ensaio", false));
        q1.adicionarAlternativa(new Alternativa(4, "Bastão de Vidro", false));
        lista.add(q1);

        Questao q2 = new Questao(2, "Combine os materiais aos sistemas correspondentes", TipoQuestao.ASSOCIACAO, model.tipos.NivelDificuldade.MEDIO, "Química");
        lista.add(q2);

        new TelaJogo(partida, lista).setVisible(true);
        dispose();
    }

    private void abrirTelaDesempenho() {
        new TelaDesempenhoAluno(this.aluno).setVisible(true);
        dispose();
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
        new telas.autenticacao.TelaLogin().setVisible(true);
        }
    }

    private static class PainelFundo extends JPanel {
        private Image imagemFundo;

        public PainelFundo() {
            try {
                imagemFundo = new ImageIcon("imagens/menu.png").getImage();
            } catch (Exception e) {
                System.err.println("Erro ao carregar imagem de fundo: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics grafico) {
            super.paintComponent(grafico);
            Graphics2D g2 = (Graphics2D) grafico.create();
            
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (imagemFundo != null) {
                int larguraPainel = getWidth();
                int alturaPainel = getHeight();
                int larguraImagem = imagemFundo.getWidth(this);
                int alturaImagem = imagemFundo.getHeight(this);

                if (larguraImagem > 0 && alturaImagem > 0) {
                    double escalaX = (double) larguraPainel / larguraImagem;
                    double escalaY = (double) alturaPainel / alturaImagem;
                    double escala = Math.max(escalaX, escalaY);

                    int novaLargura = (int) (larguraImagem * escala);
                    int novaAltura = (int) (alturaImagem * escala);
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
