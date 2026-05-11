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

        JLabel botaoPerfil = new JLabel("●", SwingConstants.CENTER);
        botaoPerfil.setBounds(17, 18, 40, 40);
        botaoPerfil.setFont(new Font("Arial", Font.BOLD, 30));
        botaoPerfil.setForeground(Color.WHITE);
        botaoPerfil.setOpaque(true);
        botaoPerfil.setBackground(new Color(36, 73, 130));
        painelFundo.add(botaoPerfil);

        JLabel botaoSair = new JLabel("↪", SwingConstants.CENTER);
        botaoSair.setBounds(581, 18, 40, 40);
        botaoSair.setFont(new Font("Arial", Font.BOLD, 30));
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
        titulo.setBounds(70, 105, 500, 95);
        titulo.setFont(new Font("Arial", Font.BOLD, 76));
        titulo.setForeground(new Color(31, 65, 126));
        painelFundo.add(titulo);

        BotaoArredondado botaoGerenciarPerguntas = new BotaoArredondado("Gerenciar perguntas");
        botaoGerenciarPerguntas.setBounds(170, 263, 300, 45);
        botaoGerenciarPerguntas.addActionListener((ActionEvent evento) -> abrirGerenciarPerguntas());
        painelFundo.add(botaoGerenciarPerguntas);

        BotaoArredondado botaoRelatorios = new BotaoArredondado("Desempenho dos alunos");
        botaoRelatorios.setBounds(170, 315, 300, 45);
        botaoRelatorios.addActionListener((ActionEvent evento) -> abrirRelatorios());
        painelFundo.add(botaoRelatorios);
    }

    private void abrirGerenciarPerguntas() {
        JOptionPane.showMessageDialog(
                this,
                "Aqui será aberta a tela de gerenciamento de perguntas.",
                "Gerenciar perguntas",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Quando a tela estiver pronta, use:
        // new TelaGerenciarPerguntas().setVisible(true);
        // dispose();
    }

    private void abrirRelatorios() {
        JOptionPane.showMessageDialog(
                this,
                "Aqui será aberta a tela de relatórios.",
                "Desempenho dos alunos",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Quando a tela estiver pronta, use:
        // new TelaRelatorios().setVisible(true);
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
            TelaMenuProfessor tela = new TelaMenuProfessor();
            tela.setVisible(true);
        });
    }
}