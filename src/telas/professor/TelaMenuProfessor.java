package telas.professor;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class TelaMenuProfessor extends JFrame {

    public TelaMenuProfessor() {
        configurarJanela();
        montarTela();
    }

    private void configurarJanela() {
        setTitle("Tela - Início");
        setSize(640, 480);
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
        botaoSair.setBounds(575, 20, 45, 45);
        botaoSair.addActionListener(e -> sairDaConta());
        painelFundo.add(botaoSair);

        JLabel titulo = new JLabel("LabQuest", SwingConstants.CENTER);
        titulo.setBounds(70, 105, 500, 95);
        titulo.setFont(new Font("Arial", Font.BOLD, 76));
        titulo.setForeground(new Color(31, 65, 126));
        painelFundo.add(titulo);

        BotaoArredondado botaoGerenciarPerguntas = new BotaoArredondado("Gerenciar perguntas");
        botaoGerenciarPerguntas.setBounds(170, 240, 300, 45);
        botaoGerenciarPerguntas.addActionListener((ActionEvent evento) -> abrirGerenciarPerguntas());
        painelFundo.add(botaoGerenciarPerguntas);

        BotaoArredondado botaoRelatorios = new BotaoArredondado("Relatórios");
        botaoRelatorios.setBounds(170, 295, 300, 45);
        botaoRelatorios.addActionListener((ActionEvent evento) -> abrirRelatorios());
        painelFundo.add(botaoRelatorios);

        BotaoArredondado botaoCadastrarAluno = new BotaoArredondado("Cadastrar Aluno");
        botaoCadastrarAluno.setBounds(170, 350, 300, 45);
        botaoCadastrarAluno.addActionListener((ActionEvent evento) -> abrirCadastrarAluno());
        painelFundo.add(botaoCadastrarAluno);
    }

    private JButton criarBotaoIconeReal(String caminho) {
        JButton btn = new JButton();
        try {
            ImageIcon iconOriginal = new ImageIcon(caminho);
            // Garante que a imagem seja redimensionada proporcionalmente para caber no botão de 45x45
            Image img = iconOriginal.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            btn.setText("?");
        }
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setMargin(new Insets(0, 0, 0, 0)); // Remove margens que podem causar cortes
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void abrirPerfil() {
        JOptionPane.showMessageDialog(
            this,
            "Nome: Professor Teste\nE-mail: professor@cps.sp.gov.br",
            "Perfil do Professor",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void abrirCadastrarAluno() {
        new telas.autenticacao.TelaCadastro().setVisible(true);
        dispose();
    }

    private void abrirGerenciarPerguntas() {
        new TelaGerenciarPerguntas().setVisible(true);
        dispose();
    }

    private void abrirRelatorios() {
        new TelaDesempenho().setVisible(true);
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

    private static class BotaoArredondado extends JButton {

        public BotaoArredondado(String texto) {
            super(texto);
            setFont(new Font("Arial", Font.BOLD, 21));
            setForeground(Color.WHITE);
            setBackground(new Color(36, 73, 130));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics grafico) {
            Graphics2D desenho = (Graphics2D) grafico.create();
            desenho.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            desenho.setColor(getBackground());
            desenho.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

            super.paintComponent(grafico);
            desenho.dispose();
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
            TelaMenuProfessor tela = new TelaMenuProfessor();
            tela.setVisible(true);
        });
    }
}