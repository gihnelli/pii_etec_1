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

        BotaoArredondado botaoRelatorios = new BotaoArredondado("Relatórios");
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
                "Relatórios",
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
            for (int y = 20; y < getHeight(); y += 95) {
                for (int x = 15; x < getWidth(); x += 110) {
                    desenharMolecula(desenho, x, y);
                    desenharAtomo(desenho, x + 45, y + 48);
        }
            }
        }

        private void desenharMolecula(Graphics2D desenho, int x, int y) {
            desenho.setStroke(new BasicStroke(2));
            desenho.setColor(new Color(80, 100, 120));

            desenho.drawLine(x + 18, y + 18, x + 35, y + 8);
            desenho.drawLine(x + 18, y + 18, x + 35, y + 32);
            desenho.drawLine(x + 18, y + 18, x + 5, y + 8);

            desenho.setColor(new Color(160, 205, 245));
            desenho.fillOval(x + 8, y + 8, 20, 20);
            desenho.fillOval(x + 32, y + 2, 12, 12);
            desenho.fillOval(x + 32, y + 28, 12, 12);
            desenho.fillOval(x, y + 2, 12, 12);

            desenho.setColor(new Color(80, 100, 120));
            desenho.drawOval(x + 8, y + 8, 20, 20);
            desenho.drawOval(x + 32, y + 2, 12, 12);
            desenho.drawOval(x + 32, y + 28, 12, 12);
            desenho.drawOval(x, y + 2, 12, 12);
        }

        private void desenharAtomo(Graphics2D desenho, int x, int y) {
            desenho.setStroke(new BasicStroke(2));
            desenho.setColor(new Color(80, 100, 120));

            desenho.drawOval(x, y + 10, 48, 18);
            desenho.drawOval(x + 10, y, 28, 38);
            desenho.drawLine(x + 8, y + 34, x + 42, y + 4);

            desenho.setColor(new Color(160, 205, 245));
            desenho.fillOval(x + 20, y + 15, 10, 10);

            desenho.setColor(new Color(80, 100, 120));
            desenho.drawOval(x + 20, y + 15, 10, 10);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaMenuProfessor tela = new TelaMenuProfessor();
            tela.setVisible(true);
        });
    }
}