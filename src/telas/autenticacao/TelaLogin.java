package telas.autenticacao;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

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
        JPanel painelFundo = new JPanel();
        painelFundo.setLayout(null);
        painelFundo.setBackground(new Color(223, 239, 252));
        setContentPane(painelFundo);

        JLabel textoEtec = new JLabel("Etec");
        textoEtec.setBounds(350, 35, 120, 40);
        textoEtec.setFont(new Font("Arial", Font.BOLD, 38));
        textoEtec.setForeground(new Color(47, 76, 94));
        painelFundo.add(textoEtec);

        JLabel textoUnidade = new JLabel("Júlio de Mesquita");
        textoUnidade.setBounds(353, 72, 150, 20);
        textoUnidade.setFont(new Font("Arial", Font.BOLD, 11));
        textoUnidade.setForeground(new Color(180, 0, 0));
        painelFundo.add(textoUnidade);

        JPanel painelLogin = new JPanel();
        painelLogin.setLayout(null);
        painelLogin.setBounds(265, 133, 435, 410);
        painelLogin.setBackground(new Color(247, 246, 252));
        painelFundo.add(painelLogin);

        JLabel titulo = new JLabel("LabQuest");
        titulo.setBounds(50, 25, 340, 80);
        titulo.setFont(new Font("Arial", Font.BOLD, 66));
        titulo.setForeground(new Color(30, 64, 127));
        painelLogin.add(titulo);

        JLabel textoEmail = new JLabel("E-mail institucional");
        textoEmail.setBounds(45, 117, 250, 25);
        textoEmail.setFont(new Font("Arial", Font.BOLD, 16));
        textoEmail.setForeground(new Color(30, 64, 127));
        painelLogin.add(textoEmail);

        campoEmail = new JTextField();
        campoEmail.setBounds(42, 142, 352, 38);
        campoEmail.setFont(new Font("Arial", Font.PLAIN, 15));
        campoEmail.setBorder(BorderFactory.createLineBorder(new Color(185, 190, 205)));
        painelLogin.add(campoEmail);

        JLabel textoSenha = new JLabel("Senha");
        textoSenha.setBounds(45, 192, 250, 25);
        textoSenha.setFont(new Font("Arial", Font.BOLD, 16));
        textoSenha.setForeground(new Color(30, 64, 127));
        painelLogin.add(textoSenha);

        campoSenha = new JPasswordField();
        campoSenha.setBounds(42, 217, 352, 38);
        campoSenha.setFont(new Font("Arial", Font.PLAIN, 15));
        campoSenha.setBorder(BorderFactory.createLineBorder(new Color(185, 190, 205)));
        painelLogin.add(campoSenha);

        caixaLembrarMe = new JCheckBox("Lembrar-me");
        caixaLembrarMe.setBounds(42, 270, 130, 25);
        caixaLembrarMe.setFont(new Font("Arial", Font.PLAIN, 15));
        caixaLembrarMe.setBackground(new Color(247, 246, 252));
        painelLogin.add(caixaLembrarMe);

        JLabel textoEsqueciSenha = new JLabel("<html><u>Esqueci minha senha</u></html>");
        textoEsqueciSenha.setBounds(248, 270, 160, 25);
        textoEsqueciSenha.setFont(new Font("Arial", Font.PLAIN, 15));
        textoEsqueciSenha.setForeground(new Color(150, 0, 0));
        textoEsqueciSenha.setCursor(new Cursor(Cursor.HAND_CURSOR));
        painelLogin.add(textoEsqueciSenha);

        JButton botaoEntrar = new JButton("Entrar");
        botaoEntrar.setBounds(42, 303, 352, 38);
        botaoEntrar.setFont(new Font("Arial", Font.BOLD, 16));
        botaoEntrar.setForeground(Color.WHITE);
        botaoEntrar.setBackground(new Color(45, 72, 190));
        botaoEntrar.setBorderPainted(false);
        botaoEntrar.setFocusPainted(false);
        botaoEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoEntrar.addActionListener((ActionEvent evento) -> validarLogin());
        painelLogin.add(botaoEntrar);

        JLabel textoCadastro = new JLabel("<html><u>Não possui uma conta? Cadastre-se</u></html>");
        textoCadastro.setBounds(92, 360, 280, 25);
        textoCadastro.setFont(new Font("Arial", Font.PLAIN, 15));
        textoCadastro.setForeground(new Color(30, 64, 127));
        textoCadastro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        painelLogin.add(textoCadastro);
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
        // Quando a tela do aluno estiver pronta, use:
        // new telas.aluno.TelaMenuAluno().setVisible(true);
        // dispose();

        JOptionPane.showMessageDialog(this, "Aqui será aberta a tela inicial do aluno.");
    }

    private void abrirTelaDoProfessor() {
        // Quando a tela do professor estiver pronta, use:
        // new telas.professor.TelaMenuProfessor().setVisible(true);
        // dispose();

        JOptionPane.showMessageDialog(this, "Aqui será aberta a tela inicial do professor.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaLogin telaLogin = new TelaLogin();
            telaLogin.setVisible(true);
        });
    }
}