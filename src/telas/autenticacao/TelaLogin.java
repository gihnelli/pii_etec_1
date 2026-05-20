package telas.autenticacao;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import telas.aluno.TelaMenuAluno;
import telas.professor.TelaMenuProfessor;

public class TelaLogin extends JFrame {

    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JCheckBox caixaLembrarMe;

    public TelaLogin() {
        configurarJanela();
        montarTela();
    }

    private void configurarJanela() {
        setTitle("LabQuest - Login");
        setSize(960, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void montarTela() {
        PainelFundo painelFundo = new PainelFundo();
        painelFundo.setLayout(null);
        setContentPane(painelFundo);

        PainelArredondado painelLogin = new PainelArredondado(30);
        painelLogin.setLayout(null);
        painelLogin.setBounds(275, 135, 410, 480);
        painelLogin.setBackground(Color.WHITE);
        painelFundo.add(painelLogin);

        JLabel titulo = new JLabel("LabQuest", SwingConstants.CENTER);
        titulo.setBounds(35, 30, 340, 80);
        titulo.setFont(new Font("Verdana", Font.BOLD, 62));
        titulo.setForeground(new Color(47, 76, 113));
        painelLogin.add(titulo);

        JLabel textoEmail = new JLabel("E-mail institucional");
        textoEmail.setBounds(45, 125, 250, 25);
        textoEmail.setFont(new Font("Verdana", Font.BOLD, 15));
        textoEmail.setForeground(new Color(47, 76, 113));
        painelLogin.add(textoEmail);

        campoEmail = new CampoArredondado(15);
        campoEmail.setBounds(42, 150, 326, 45);
        campoEmail.setFont(new Font("Verdana", Font.PLAIN, 15));
        campoEmail.setBackground(new Color(245, 247, 251));
        painelLogin.add(campoEmail);

        JLabel textoSenha = new JLabel("Senha");
        textoSenha.setBounds(45, 205, 250, 25);
        textoSenha.setFont(new Font("Verdana", Font.BOLD, 15));
        textoSenha.setForeground(new Color(47, 76, 113));
        painelLogin.add(textoSenha);

        campoSenha = new CampoSenhaArredondado(15);
        campoSenha.setBounds(42, 230, 326, 45);
        campoSenha.setFont(new Font("Verdana", Font.PLAIN, 15));
        campoSenha.setBackground(new Color(245, 247, 251));
        painelLogin.add(campoSenha);

        caixaLembrarMe = new JCheckBox("Lembrar-me");
        caixaLembrarMe.setBounds(42, 290, 130, 25);
        caixaLembrarMe.setFont(new Font("Verdana", Font.PLAIN, 15));
        caixaLembrarMe.setBackground(Color.WHITE);
        caixaLembrarMe.setForeground(new Color(47, 76, 113));
        painelLogin.add(caixaLembrarMe);

        JLabel textoEsqueciSenha = new JLabel("<html><u>Esqueci minha senha</u></html>");
        textoEsqueciSenha.setBounds(230, 290, 150, 25);
        textoEsqueciSenha.setFont(new Font("Verdana", Font.PLAIN, 15));
        textoEsqueciSenha.setForeground(new Color(150, 40, 27));
        textoEsqueciSenha.setCursor(new Cursor(Cursor.HAND_CURSOR));
        painelLogin.add(textoEsqueciSenha);

        BotaoArredondado botaoEntrar = new BotaoArredondado("Entrar", 15);
        botaoEntrar.setBounds(42, 335, 326, 45);
        botaoEntrar.addActionListener((ActionEvent evento) -> validarLogin());
        painelLogin.add(botaoEntrar);

        JLabel textoCadastro = new JLabel("<html><u>Não possui um conta? Cadastre-se</u></html>", SwingConstants.CENTER);
        textoCadastro.setBounds(35, 410, 340, 25);
        textoCadastro.setFont(new Font("Verdana", Font.PLAIN, 15));
        textoCadastro.setForeground(new Color(47, 76, 113));
        textoCadastro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        painelLogin.add(textoCadastro);
    }

    private static class PainelArredondado extends JPanel {
        private int raio;

        public PainelArredondado(int raio) {
            this.raio = raio;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), raio, raio);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class CampoArredondado extends JTextField {
        private int raio;

        public CampoArredondado(int raio) {
            this.raio = raio;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), raio, raio);
            g2.setColor(new Color(200, 200, 200));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, raio, raio);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class CampoSenhaArredondado extends JPasswordField {
        private int raio;

        public CampoSenhaArredondado(int raio) {
            this.raio = raio;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), raio, raio);
            g2.setColor(new Color(200, 200, 200));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, raio, raio);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class BotaoArredondado extends JButton {
        private int raio;

        public BotaoArredondado(String texto, int raio) {
            super(texto);
            this.raio = raio;
            setFont(new Font("Arial", Font.BOLD, 18));
            setForeground(Color.WHITE);
            setBackground(new Color(36, 73, 130));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), raio, raio);
            super.paintComponent(g);
            g2.dispose();
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
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            
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


    private void validarLogin() {
        String emailDigitado = campoEmail.getText().trim().toLowerCase();
        String senhaDigitada = new String(campoSenha.getPassword());

        if (emailDigitado.isEmpty() || senhaDigitada.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Preencha o e-mail institucional e a senha.",
                    "Campos obrigatórios",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (ehEmailDeAluno(emailDigitado)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Login de aluno identificado com sucesso.",
                    "Aluno",
                    JOptionPane.INFORMATION_MESSAGE
            );

            abrirTelaDoAluno();

        } else if (ehEmailDeProfessor(emailDigitado)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Login de professor identificado com sucesso.",
                    "Professor",
                    JOptionPane.INFORMATION_MESSAGE
            );

            abrirTelaDoProfessor();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "E-mail institucional inválido.\n\nUse um dos formatos:\n\nAluno: nome@aluno.cps.sp.gov.br\nProfessor: nome@cps.sp.gov.br",
                    "E-mail inválido",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private boolean ehEmailDeAluno(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@aluno\\.cps\\.sp\\.gov\\.br$");
    }

    private boolean ehEmailDeProfessor(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@cps\\.sp\\.gov\\.br$");
    }

    private void abrirTelaDoAluno() {
        new TelaMenuAluno().setVisible(true);
        dispose();
    }

    private void abrirTelaDoProfessor() {
        new TelaMenuProfessor().setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaLogin telaLogin = new TelaLogin();
            telaLogin.setVisible(true);
        });
    }
}