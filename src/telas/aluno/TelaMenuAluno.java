package telas.aluno;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

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

        JButton botaoHistorico = criarBotaoMenu("Histórico de perguntas");
        botaoHistorico.setBounds(148, 319, 240, 38);
        botaoHistorico.addActionListener((ActionEvent evento) -> abrirTelaHistoricoPerguntas());
        painelFundo.add(botaoHistorico);
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
        JOptionPane.showMessageDialog(
                this,
                "Aqui será aberta a tela do jogo.",
                "Jogar",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Quando a TelaJogo estiver pronta, use:
        // new TelaJogo().setVisible(true);
        // dispose();
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

    private void abrirTelaHistoricoPerguntas() {
        JOptionPane.showMessageDialog(
                this,
                "Aqui será aberto o histórico de perguntas.",
                "Histórico de perguntas",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Quando a TelaHistoricoPerguntas estiver pronta, use:
        // new TelaHistoricoPerguntas().setVisible(true);
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

        @Override
        protected void paintComponent(Graphics grafico) {
            super.paintComponent(grafico);

            Graphics2D desenho = (Graphics2D) grafico;
            desenho.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            desenho.setColor(new Color(223, 239, 252));
            desenho.fillRect(0, 0, getWidth(), getHeight());

            desenharPadraoLaboratorio(desenho);
        }

        private void desenharPadraoLaboratorio(Graphics2D desenho) {
            desenho.setStroke(new BasicStroke(2));
            desenho.setFont(new Font("Arial", Font.BOLD, 14));

            for (int y = 25; y < getHeight(); y += 85) {
                for (int x = 20; x < getWidth(); x += 105) {
                    desenharMolecula(desenho, x, y);
                }
            }
        }

        private void desenharMolecula(Graphics2D desenho, int x, int y) {
            desenho.setColor(new Color(85, 120, 150));

            desenho.drawLine(x, y, x + 18, y + 15);
            desenho.drawLine(x + 18, y + 15, x + 37, y);
            desenho.drawLine(x + 18, y + 15, x + 18, y + 38);

            desenho.setColor(new Color(160, 205, 245));
            desenho.fillOval(x + 10, y + 8, 18, 18);
            desenho.fillOval(x - 4, y - 4, 12, 12);
            desenho.fillOval(x + 32, y - 4, 12, 12);
            desenho.fillOval(x + 12, y + 34, 12, 12);

            desenho.setColor(new Color(85, 120, 150));
            desenho.drawOval(x + 10, y + 8, 18, 18);
            desenho.drawOval(x - 4, y - 4, 12, 12);
            desenho.drawOval(x + 32, y - 4, 12, 12);
            desenho.drawOval(x + 12, y + 34, 12, 12);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaMenuAluno tela = new TelaMenuAluno();
            tela.setVisible(true);
        });
    }
}
