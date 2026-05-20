package telas.autenticacao;

import java.awt.*;
import javax.swing.*;
import telas.professor.TelaMenuProfessor;

public class TelaCadastro extends JFrame {

    private JTextField campoNome;
    private JTextField campoEmail;
    private JPasswordField campoSenha;

    public TelaCadastro() {
        configurarJanela();
        montarTela();
    }

    private void configurarJanela() {
        setTitle("LabQuest - Cadastrar Aluno");
        setSize(960, 680);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void montarTela() {
        PainelFundo painelFundo = new PainelFundo();
        painelFundo.setLayout(null);
        setContentPane(painelFundo);

        PainelArredondado painelCentral = new PainelArredondado(30);
        painelCentral.setLayout(null);
        painelCentral.setBounds(100, 150, 760, 380);
        painelCentral.setBackground(new Color(220, 220, 220, 230)); // Cinza com leve transparência
        painelFundo.add(painelCentral);

        campoNome = criarCampoArredondado("Nome:", 30, 30, 700, 55);
        painelCentral.add(campoNome);

        campoEmail = criarCampoArredondado("E-mail:", 30, 105, 700, 55);
        painelCentral.add(campoEmail);

        campoSenha = criarCampoSenhaArredondado("Senha (CPF):", 30, 180, 700, 55);
        painelCentral.add(campoSenha);

        JButton botaoCancelar = criarBotaoCustomizado("Cancelar", new Color(210, 170, 170), new Color(120, 60, 60));
        botaoCancelar.setBounds(200, 280, 180, 60);
        botaoCancelar.addActionListener(e -> voltarAoMenu());
        painelCentral.add(botaoCancelar);

        JButton botaoAdicionar = criarBotaoCustomizado("Adicionar", new Color(180, 210, 180), new Color(40, 90, 40));
        botaoAdicionar.setBounds(400, 280, 180, 60);
        botaoAdicionar.addActionListener(e -> cadastrarAluno());
        painelCentral.add(botaoAdicionar);
    }

    private JTextField criarCampoArredondado(String rotulo, int x, int y, int w, int h) {
        JTextField campo = new CampoTextoArredondado(20, rotulo);
        campo.setBounds(x, y, w, h);
        campo.setFont(new Font("Arial", Font.BOLD, 18));
        campo.setForeground(new Color(100, 100, 100));
        campo.setBackground(Color.WHITE);
        return campo;
    }

    private JPasswordField criarCampoSenhaArredondado(String rotulo, int x, int y, int w, int h) {
        JPasswordField campo = new CampoSenhaArredondada(20, rotulo);
        campo.setBounds(x, y, w, h);
        campo.setFont(new Font("Arial", Font.BOLD, 18));
        campo.setForeground(new Color(100, 100, 100));
        campo.setBackground(Color.WHITE);
        return campo;
    }

    private JButton criarBotaoCustomizado(String texto, Color corFundo, Color corTexto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setForeground(corTexto);
        btn.setBackground(corFundo);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 20, 20);
                super.paint(g2, c);
                g2.dispose();
            }
        });
        return btn;
    }

    private void voltarAoMenu() {
        new TelaMenuProfessor().setVisible(true);
        dispose();
    }

    private void cadastrarAluno() {
        String nome = campoNome.getText().trim();
        String email = campoEmail.getText().trim().toLowerCase();
        String senha = new String(campoSenha.getPassword());

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.matches("^[a-zA-Z0-9._%+-]+@aluno\\.cps\\.sp\\.gov\\.br$")) {
            JOptionPane.showMessageDialog(
                this, 
                "E-mail inválido!\nO aluno deve usar o formato: nome@aluno.cps.sp.gov.br", 
                "Erro de Formato", 
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (!senha.matches("^\\d{11}$")) {
            JOptionPane.showMessageDialog(
                this, 
                "Senha inválida!\nA senha (CPF) deve conter exatamente 11 números, sem pontos ou traços.", 
                "Erro de Senha", 
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(this, "Aluno " + nome + " cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        voltarAoMenu();
    }
    
    private static class PainelArredondado extends JPanel {
        private int raio;
        public PainelArredondado(int raio) { this.raio = raio; setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), raio, raio);
            g2.dispose();
        }
    }

    private static class CampoTextoArredondado extends JTextField {
        private int raio;
        private String placeholder;

        public CampoTextoArredondado(int raio, String placeholder) {
            this.raio = raio;
            this.placeholder = placeholder;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), raio, raio);
            super.paintComponent(g);
            if (getText().isEmpty()) {
                g2.setColor(new Color(150, 150, 150));
                g2.drawString(placeholder, 15, (getHeight() / 2) + 7);
            }
            g2.dispose();
        }
    }

    private static class CampoSenhaArredondada extends JPasswordField {
        private int raio;
        private String placeholder;

        public CampoSenhaArredondada(int raio, String placeholder) {
            this.raio = raio;
            this.placeholder = placeholder;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), raio, raio);
            super.paintComponent(g);
            if (getPassword().length == 0) {
                g2.setColor(new Color(150, 150, 150));
                g2.drawString(placeholder, 15, (getHeight() / 2) + 7);
            }
            g2.dispose();
        }
    }

    private static class PainelFundo extends JPanel {
        private Image img;
        public PainelFundo() { try { img = new ImageIcon("imagens/menu.png").getImage(); } catch(Exception e){} }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) {
                double esc = Math.max((double)getWidth()/img.getWidth(null), (double)getHeight()/img.getHeight(null));
                int nw = (int)(img.getWidth(null)*esc), nh = (int)(img.getHeight(null)*esc);
                g.drawImage(img, (getWidth()-nw)/2, (getHeight()-nh)/2, nw, nh, null);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaCadastro().setVisible(true));
    }
}
