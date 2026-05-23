package telas.professor;

import java.awt.BasicStroke;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class TelaGerenciarPerguntas extends JFrame {

    private final List<PerguntaCadastro> bancoPerguntas = new ArrayList<>();

    private final CardLayout layoutConteudo = new CardLayout();
    private JPanel painelConteudo;

    private String telaAtual = "VISUALIZAR";
    private int indicePerguntaSelecionada = 0;

    private String caminhoImagemAdicionar = "";
    private String caminhoImagemEditar = "";

    public TelaGerenciarPerguntas() {
        carregarPerguntasExemplo();
        configurarJanela();
        montarTela();
        mostrarTela("VISUALIZAR");
    }

    private void configurarJanela() {
        setTitle("LabQuest - Gerenciar Perguntas");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void montarTela() {
        PainelFundoImagem painelBase = new PainelFundoImagem();
        painelBase.setLayout(new GridBagLayout());
        setContentPane(painelBase);

        JPanel painelPrincipal = new JPanel(null);
        painelPrincipal.setPreferredSize(new Dimension(980, 720));
        painelPrincipal.setOpaque(false);
        painelBase.add(painelPrincipal);

        JButton botaoPerfil = criarBotaoIconeReal("imagens/Perfil.png");
        botaoPerfil.setBounds(18, 18, 45, 45);
        botaoPerfil.addActionListener(e -> abrirPerfil());
        painelPrincipal.add(botaoPerfil);

        JButton botaoAdicionar = criarBotaoTopo("Adicionar<br>pergunta");
        botaoAdicionar.setBounds(248, 18, 148, 60);
        botaoAdicionar.addActionListener(evento -> mostrarTela("ADICIONAR"));
        painelPrincipal.add(botaoAdicionar);

        JButton botaoEditar = criarBotaoTopo("Editar<br>pergunta");
        botaoEditar.setBounds(410, 18, 148, 60);
        botaoEditar.addActionListener(evento -> mostrarTela("EDITAR"));
        painelPrincipal.add(botaoEditar);

        JButton botaoRemover = criarBotaoTopo("Remover<br>pergunta");
        botaoRemover.setBounds(572, 18, 160, 60);
        botaoRemover.addActionListener(evento -> mostrarTela("REMOVER"));
        painelPrincipal.add(botaoRemover);

        JButton botaoSair = criarBotaoIconeReal("imagens/Sair.png");
        botaoSair.setBounds(888, 18, 45, 45);
        botaoSair.addActionListener(e -> fecharTela());
        painelPrincipal.add(botaoSair);

        painelConteudo = new JPanel(layoutConteudo);
        painelConteudo.setOpaque(false);
        painelConteudo.setBounds(18, 98, 940, 590);
        painelPrincipal.add(painelConteudo);
    }

    private void abrirPerfil() {
        JOptionPane.showMessageDialog(
            this,
            "Nome: Professor Teste\nE-mail: professor@cps.sp.gov.br",
            "Perfil do Professor",
            JOptionPane.INFORMATION_MESSAGE
        );
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

    private JButton criarBotaoTopo(String texto) {
        BotaoArredondado botao = new BotaoArredondado("<html><center>" + texto + "</center></html>");
        botao.setFont(new Font("Verdana", Font.BOLD, 17));
        botao.setForeground(Color.WHITE);
        botao.setBackground(new Color(35, 74, 131));
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setContentAreaFilled(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return botao;
    }

    private void mostrarTela(String nomeTela) {
        this.telaAtual = nomeTela;
        reconstruirPaineis();
        layoutConteudo.show(painelConteudo, nomeTela);
    }

    private void reconstruirPaineis() {
        painelConteudo.removeAll();
        painelConteudo.add(criarPainelVisualizacao(), "VISUALIZAR");
        painelConteudo.add(criarPainelAdicionar(), "ADICIONAR");
        painelConteudo.add(criarPainelEditar(), "EDITAR");
        painelConteudo.add(criarPainelRemover(), "REMOVER");
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }

    private JPanel criarPainelVisualizacao() {
        JPanel painelBase = new JPanel(null);
        painelBase.setOpaque(false);

        PainelArredondado painelListaExterna = new PainelArredondado(new Color(245, 245, 250), new Color(42, 82, 145), 1);
        painelListaExterna.setLayout(null);
        painelListaExterna.setBounds(12, 8, 900, 562);
        painelBase.add(painelListaExterna);

        JPanel painelLinhas = new JPanel(null);
        painelLinhas.setOpaque(false);
        painelLinhas.setPreferredSize(new Dimension(860, Math.max(500, bancoPerguntas.size() * 42 + 16)));

        int y = 8;
        for (int i = 0; i < bancoPerguntas.size(); i++) {
            boolean selecionada = i == indicePerguntaSelecionada;
            JPanel linha = criarLinhaVisualizacao(bancoPerguntas.get(i), i, selecionada);
            linha.setBounds(12, y, 848, 34);
            painelLinhas.add(linha);
            y += 41;
        }

        JScrollPane barraRolagem = new JScrollPane(painelLinhas);
        barraRolagem.setBounds(10, 10, 878, 538);
        barraRolagem.setBorder(null);
        barraRolagem.setOpaque(false);
        barraRolagem.getViewport().setOpaque(false);
        barraRolagem.getVerticalScrollBar().setUnitIncrement(16);
        painelListaExterna.add(barraRolagem);

        return painelBase;
    }

    private JPanel criarLinhaVisualizacao(PerguntaCadastro pergunta, int indice, boolean selecionada) {
        JPanel linha = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics grafico) {
                super.paintComponent(grafico);
                Graphics2D desenho = (Graphics2D) grafico.create();
                desenho.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                desenho.setColor(new Color(213, 213, 213));
                desenho.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

                if (selecionada) {
                    desenho.setColor(new Color(45, 125, 220));
                    desenho.setStroke(new BasicStroke(2));
                    desenho.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 18, 18);
                }

                desenho.setColor(new Color(78, 100, 138));
                desenho.drawLine(360, 5, 360, getHeight() - 5);

                desenho.dispose();
            }
        };

        linha.setOpaque(false);
        linha.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel textoPergunta = new JLabel("“" + pergunta.getEnunciado() + "”");
        textoPergunta.setBounds(18, 3, 338, 28);
        textoPergunta.setFont(new Font("Verdana", Font.BOLD, 14));
        textoPergunta.setForeground(new Color(35, 74, 131));
        linha.add(textoPergunta);

        JLabel textoResposta = new JLabel(pergunta.getResumoRespostaCorreta());
        textoResposta.setBounds(380, 3, 450, 28);
        textoResposta.setFont(new Font("Verdana", Font.BOLD, 14));
        textoResposta.setForeground(new Color(35, 74, 131));
        linha.add(textoResposta);

        linha.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evento) {
                indicePerguntaSelecionada = indice;
                mostrarTela("VISUALIZAR");
            }
        });

        return linha;
    }

    private JPanel criarPainelAdicionar() {
        JPanel painelBase = new JPanel(null);
        painelBase.setOpaque(false);

        PainelArredondado painelFormulario = new PainelArredondado(new Color(212, 212, 212), new Color(42, 82, 145), 1);
        painelFormulario.setLayout(null);
        painelFormulario.setBounds(4, 84, 904, 438);
        painelBase.add(painelFormulario);

        JTextField campoEnunciado = new JTextField();
        configurarCampoTexto(campoEnunciado);
        campoEnunciado.setBounds(28, 18, 840, 44);
        campoEnunciado.setText("");
        campoEnunciado.setToolTipText("Digite o enunciado");
        painelFormulario.add(campoEnunciado);

        JLabel textoEnunciado = new JLabel("Enunciado:");
        textoEnunciado.setBounds(38, 26, 120, 26);
        textoEnunciado.setFont(new Font("Verdana", Font.BOLD, 16));
        textoEnunciado.setForeground(new Color(170, 170, 185));
        painelFormulario.add(textoEnunciado);

        JTextField[] camposAlternativas = new JTextField[4];
        JRadioButton[] opcoesCorretas = new JRadioButton[4];
        ButtonGroup grupoCorreta = new ButtonGroup();

        String[] letras = {"a)", "b)", "c)", "d)"};
        int y = 92;

        for (int i = 0; i < 4; i++) {
            JRadioButton radio = new JRadioButton();
            radio.setBounds(36, y + 14, 22, 22);
            radio.setOpaque(false);
            radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
            grupoCorreta.add(radio);
            opcoesCorretas[i] = radio;
            painelFormulario.add(radio);

            JTextField campoAlternativa = new JTextField();
            configurarCampoTexto(campoAlternativa);
            campoAlternativa.setBounds(66, y, 650, 46);
            campoAlternativa.setText("");
            camposAlternativas[i] = campoAlternativa;
            painelFormulario.add(campoAlternativa);

            JLabel textoLetra = new JLabel(letras[i]);
            textoLetra.setBounds(76, y + 8, 35, 28);
            textoLetra.setFont(new Font("Verdana", Font.BOLD, 16));
            textoLetra.setForeground(new Color(170, 170, 185));
            painelFormulario.add(textoLetra);

            y += 59;
        }

        opcoesCorretas[0].setSelected(true);

        PainelArredondado painelImagem = new PainelArredondado(new Color(232, 232, 236), new Color(232, 232, 236), 0);
        painelImagem.setLayout(null);
        painelImagem.setBounds(730, 125, 142, 140);
        painelFormulario.add(painelImagem);

        JButton botaoImagem = new JButton("<html><center>⬆<br>Selecionar<br>imagem</center></html>");
        botaoImagem.setBounds(20, 18, 100, 80);
        botaoImagem.setFont(new Font("Verdana", Font.BOLD, 18));
        botaoImagem.setForeground(Color.BLACK);
        botaoImagem.setBorderPainted(false);
        botaoImagem.setFocusPainted(false);
        botaoImagem.setContentAreaFilled(false);
        botaoImagem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        painelImagem.add(botaoImagem);

        JLabel textoFormato = new JLabel("*.png, *.jpeg", SwingConstants.CENTER);
        textoFormato.setBounds(10, 104, 122, 24);
        textoFormato.setFont(new Font("Verdana", Font.BOLD, 14));
        textoFormato.setForeground(new Color(96, 96, 120));
        painelImagem.add(textoFormato);

        JLabel textoArquivoEscolhido = new JLabel("Nenhum arquivo", SwingConstants.CENTER);
        textoArquivoEscolhido.setBounds(10, 88, 122, 18);
        textoArquivoEscolhido.setFont(new Font("Verdana", Font.PLAIN, 11));
        textoArquivoEscolhido.setForeground(new Color(100, 100, 100));
        painelImagem.add(textoArquivoEscolhido);

        botaoImagem.addActionListener(evento -> {
            JFileChooser seletorArquivo = new JFileChooser();
            int resposta = seletorArquivo.showOpenDialog(this);

            if (resposta == JFileChooser.APPROVE_OPTION) {
                File arquivo = seletorArquivo.getSelectedFile();
                caminhoImagemAdicionar = arquivo.getAbsolutePath();
                textoArquivoEscolhido.setText(arquivo.getName());
            }
        });

        PainelArredondado painelNivel = new PainelArredondado(new Color(235, 235, 239), new Color(235, 235, 239), 0);
        painelNivel.setLayout(null);
        painelNivel.setBounds(28, 328, 840, 46);
        painelFormulario.add(painelNivel);

        JLabel textoNivel = new JLabel("Nível de dificuldade:");
        textoNivel.setBounds(10, 8, 190, 28);
        textoNivel.setFont(new Font("Verdana", Font.BOLD, 15));
        textoNivel.setForeground(new Color(170, 170, 185));
        painelNivel.add(textoNivel);

        JRadioButton radioFacil = criarRadioNivel("Fácil", 200, true);
        JRadioButton radioMedio = criarRadioNivel("Médio", 300, false);
        JRadioButton radioDificil = criarRadioNivel("Difícil", 400, false);

        ButtonGroup grupoNivel = new ButtonGroup();
        grupoNivel.add(radioFacil);
        grupoNivel.add(radioMedio);
        grupoNivel.add(radioDificil);

        painelNivel.add(radioFacil);
        painelNivel.add(radioMedio);
        painelNivel.add(radioDificil);

        JButton botaoCancelar = criarBotaoAcao("Cancelar", new Color(221, 188, 188), new Color(104, 44, 44));
        botaoCancelar.setBounds(228, 385, 206, 44);
        botaoCancelar.addActionListener(evento -> mostrarTela("VISUALIZAR"));
        painelFormulario.add(botaoCancelar);

        JButton botaoAdicionar = criarBotaoAcao("Adicionar", new Color(176, 215, 172), new Color(44, 103, 48));
        botaoAdicionar.setBounds(468, 385, 206, 44);
        botaoAdicionar.addActionListener(evento -> {
            String enunciado = campoEnunciado.getText().trim();

            List<String> alternativas = new ArrayList<>();
            for (JTextField campo : camposAlternativas) {
                alternativas.add(campo.getText().trim());
            }

            if (enunciado.isEmpty()) {
                mostrarMensagem("Digite o enunciado da pergunta.");
                return;
            }

            for (String alternativa : alternativas) {
                if (alternativa.isEmpty()) {
                    mostrarMensagem("Preencha todas as alternativas.");
                    return;
                }
            }

            int indiceCorreta = 0;
            for (int i = 0; i < opcoesCorretas.length; i++) {
                if (opcoesCorretas[i].isSelected()) {
                    indiceCorreta = i;
                    break;
                }
            }

            String nivel = "Fácil";
            if (radioMedio.isSelected()) {
                nivel = "Médio";
            } else if (radioDificil.isSelected()) {
                nivel = "Difícil";
            }

            PerguntaCadastro novaPergunta = new PerguntaCadastro(
                    enunciado,
                    alternativas,
                    indiceCorreta,
                    nivel,
                    caminhoImagemAdicionar
            );

            bancoPerguntas.add(novaPergunta);
            indicePerguntaSelecionada = bancoPerguntas.size() - 1;
            caminhoImagemAdicionar = "";
            mostrarTela("VISUALIZAR");
        });
        painelFormulario.add(botaoAdicionar);

        return painelBase;
    }

    private JPanel criarPainelEditar() {
        JPanel painelBase = new JPanel(null);
        painelBase.setOpaque(false);

        if (bancoPerguntas.isEmpty()) {
            return painelBase;
        }

        PerguntaCadastro perguntaSelecionada = bancoPerguntas.get(indicePerguntaSelecionada);

        PainelArredondado painelFormulario = new PainelArredondado(new Color(212, 212, 212), new Color(42, 82, 145), 1);
        painelFormulario.setLayout(null);
        painelFormulario.setBounds(4, 84, 904, 380);
        painelBase.add(painelFormulario);

        PainelArredondado campoEnunciado = new PainelArredondado(new Color(235, 235, 239), new Color(235, 235, 239), 0);
        campoEnunciado.setLayout(null);
        campoEnunciado.setBounds(30, 18, 842, 46);
        painelFormulario.add(campoEnunciado);

        JLabel textoEnunciado = new JLabel("Enunciado: " + perguntaSelecionada.getEnunciado());
        textoEnunciado.setBounds(12, 8, 800, 28);
        textoEnunciado.setFont(new Font("Verdana", Font.BOLD, 16));
        textoEnunciado.setForeground(new Color(120, 120, 145));
        campoEnunciado.add(textoEnunciado);

        JRadioButton[] radiosCorretos = new JRadioButton[4];
        JTextField[] camposAlternativas = new JTextField[4];
        ButtonGroup grupoCorreta = new ButtonGroup();

        int y = 78;
        String[] letras = {"a)", "b)", "c)", "d)"};

        for (int i = 0; i < 4; i++) {
            JRadioButton radio = new JRadioButton();
            radio.setBounds(38, y + 14, 22, 22);
            radio.setOpaque(false);
            radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
            radio.setSelected(i == perguntaSelecionada.getIndiceCorreta());
            grupoCorreta.add(radio);
            radiosCorretos[i] = radio;
            painelFormulario.add(radio);

            JTextField campoAlternativa = new JTextField(perguntaSelecionada.getAlternativas().get(i));
            configurarCampoTexto(campoAlternativa);
            campoAlternativa.setBounds(68, y, 650, 46);
            camposAlternativas[i] = campoAlternativa;
            painelFormulario.add(campoAlternativa);

            JLabel textoLetra = new JLabel(letras[i]);
            textoLetra.setBounds(102, y + 8, 40, 28);
            textoLetra.setFont(new Font("Verdana", Font.BOLD, 16));
            textoLetra.setForeground(new Color(120, 120, 145));
            painelFormulario.add(textoLetra);

            y += 58;
        }

        PainelArredondado painelImagem = new PainelArredondado(new Color(235, 235, 239), new Color(235, 235, 239), 0);
        painelImagem.setLayout(null);
        painelImagem.setBounds(730, 125, 142, 140);
        painelFormulario.add(painelImagem);

        JLabel imagemLabel = new JLabel("Imagem", SwingConstants.CENTER);
        imagemLabel.setBounds(10, 10, 122, 110);
        imagemLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        imagemLabel.setForeground(new Color(80, 100, 130));
        painelImagem.add(imagemLabel);

        JButton botaoTrocarImagem = new JButton("Trocar");
        botaoTrocarImagem.setBounds(30, 106, 82, 24);
        botaoTrocarImagem.setFont(new Font("Verdana", Font.BOLD, 12));
        botaoTrocarImagem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        painelImagem.add(botaoTrocarImagem);

        if (perguntaSelecionada.getCaminhoImagem() != null && !perguntaSelecionada.getCaminhoImagem().isBlank()) {
            configurarPreviaImagem(imagemLabel, perguntaSelecionada.getCaminhoImagem());
        }

        botaoTrocarImagem.addActionListener(evento -> {
            JFileChooser seletorArquivo = new JFileChooser();
            int resposta = seletorArquivo.showOpenDialog(this);

            if (resposta == JFileChooser.APPROVE_OPTION) {
                File arquivo = seletorArquivo.getSelectedFile();
                caminhoImagemEditar = arquivo.getAbsolutePath();
                configurarPreviaImagem(imagemLabel, caminhoImagemEditar);
            }
        });

        JButton botaoCancelar = criarBotaoAcao("Cancelar", new Color(221, 188, 188), new Color(104, 44, 44));
        botaoCancelar.setBounds(232, 316, 206, 50);
        botaoCancelar.addActionListener(evento -> mostrarTela("VISUALIZAR"));
        painelFormulario.add(botaoCancelar);

        JButton botaoSalvar = criarBotaoAcao("Salvar alterações", new Color(176, 215, 172), new Color(44, 103, 48));
        botaoSalvar.setBounds(472, 316, 206, 50);
        botaoSalvar.addActionListener(evento -> {
            List<String> novasAlternativas = new ArrayList<>();
            for (JTextField campo : camposAlternativas) {
                novasAlternativas.add(campo.getText().trim());
            }

            for (String alternativa : novasAlternativas) {
                if (alternativa.isEmpty()) {
                    mostrarMensagem("Preencha todas as alternativas.");
                    return;
                }
            }

            int novaCorreta = 0;
            for (int i = 0; i < radiosCorretos.length; i++) {
                if (radiosCorretos[i].isSelected()) {
                    novaCorreta = i;
                    break;
                }
            }

            perguntaSelecionada.setAlternativas(novasAlternativas);
            perguntaSelecionada.setIndiceCorreta(novaCorreta);

            if (!caminhoImagemEditar.isBlank()) {
                perguntaSelecionada.setCaminhoImagem(caminhoImagemEditar);
            }

            caminhoImagemEditar = "";
            mostrarTela("VISUALIZAR");
        });
        painelFormulario.add(botaoSalvar);

        return painelBase;
    }

    private JPanel criarPainelRemover() {
        JPanel painelBase = new JPanel(null);
        painelBase.setOpaque(false);

        PainelArredondado painelListaExterna = new PainelArredondado(new Color(245, 245, 250), new Color(42, 82, 145), 1);
        painelListaExterna.setLayout(null);
        painelListaExterna.setBounds(0, 0, 902, 320);
        painelBase.add(painelListaExterna);

        ButtonGroup grupoSelecao = new ButtonGroup();
        JPanel painelLinhas = new JPanel(null);
        painelLinhas.setOpaque(false);
        painelLinhas.setPreferredSize(new Dimension(860, Math.max(250, bancoPerguntas.size() * 42 + 16)));

        int y = 8;

        for (int i = 0; i < bancoPerguntas.size(); i++) {
            PerguntaCadastro pergunta = bancoPerguntas.get(i);

            JPanel linha = new JPanel(null) {
                @Override
                protected void paintComponent(Graphics grafico) {
                    super.paintComponent(grafico);
                    Graphics2D desenho = (Graphics2D) grafico.create();
                    desenho.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    desenho.setColor(new Color(213, 213, 213));
                    desenho.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

                    desenho.setColor(new Color(78, 100, 138));
                    desenho.drawLine(346, 5, 346, getHeight() - 5);

                    desenho.dispose();
                }
            };

            linha.setOpaque(false);
            linha.setBounds(10, y, 842, 34);

            JRadioButton radio = new JRadioButton();
            radio.setBounds(8, 6, 24, 24);
            radio.setOpaque(false);
            radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
            radio.setSelected(i == indicePerguntaSelecionada);
            grupoSelecao.add(radio);
            linha.add(radio);

            int indice = i;
            radio.addActionListener(evento -> indicePerguntaSelecionada = indice);

            JLabel textoPergunta = new JLabel("“" + pergunta.getEnunciado() + "”");
            textoPergunta.setBounds(42, 3, 294, 28);
            textoPergunta.setFont(new Font("Verdana", Font.BOLD, 14));
            textoPergunta.setForeground(new Color(35, 74, 131));
            linha.add(textoPergunta);

            JLabel textoResposta = new JLabel(pergunta.getResumoRespostaCorreta());
            textoResposta.setBounds(360, 3, 460, 28);
            textoResposta.setFont(new Font("Verdana", Font.BOLD, 14));
            textoResposta.setForeground(new Color(35, 74, 131));
            linha.add(textoResposta);

            painelLinhas.add(linha);
            y += 41;
        }

        JScrollPane barraRolagem = new JScrollPane(painelLinhas);
        barraRolagem.setBounds(10, 8, 878, 232);
        barraRolagem.setBorder(null);
        barraRolagem.setOpaque(false);
        barraRolagem.getViewport().setOpaque(false);
        barraRolagem.getVerticalScrollBar().setUnitIncrement(16);
        painelListaExterna.add(barraRolagem);

        JButton botaoCancelar = criarBotaoAcao("Cancelar", new Color(221, 188, 188), new Color(104, 44, 44));
        botaoCancelar.setBounds(228, 255, 206, 50);
        botaoCancelar.addActionListener(evento -> mostrarTela("VISUALIZAR"));
        painelListaExterna.add(botaoCancelar);

        JButton botaoRemover = criarBotaoAcao("Remover", new Color(176, 215, 172), new Color(44, 103, 48));
        botaoRemover.setBounds(468, 255, 206, 50);
        botaoRemover.addActionListener(evento -> {
            if (bancoPerguntas.isEmpty()) {
                mostrarMensagem("Não há perguntas para remover.");
                return;
            }

            int resposta = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente remover a pergunta selecionada?",
                    "Confirmar remoção",
                    JOptionPane.YES_NO_OPTION
            );

            if (resposta == JOptionPane.YES_OPTION) {
                bancoPerguntas.remove(indicePerguntaSelecionada);

                if (bancoPerguntas.isEmpty()) {
                    indicePerguntaSelecionada = 0;
                } else if (indicePerguntaSelecionada >= bancoPerguntas.size()) {
                    indicePerguntaSelecionada = bancoPerguntas.size() - 1;
                }

                mostrarTela("VISUALIZAR");
            }
        });
        painelListaExterna.add(botaoRemover);

        return painelBase;
    }

    private void configurarCampoTexto(JTextField campo) {
        campo.setFont(new Font("Verdana", Font.BOLD, 15));
        campo.setForeground(new Color(120, 120, 145));
        campo.setBackground(new Color(235, 235, 239));
        campo.setBorder(new EmptyBorder(0, 12, 0, 12));
    }

    private JRadioButton criarRadioNivel(String texto, int x, boolean selecionado) {
        JRadioButton radio = new JRadioButton(texto);
        radio.setBounds(x, 8, 120, 28);
        radio.setOpaque(false);
        radio.setSelected(selecionado);
        radio.setFont(new Font("Verdana", Font.BOLD, 15));
        radio.setForeground(new Color(35, 74, 131));
        radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return radio;
    }

    private JButton criarBotaoAcao(String texto, Color corFundo, Color corTexto) {
        BotaoArredondado botao = new BotaoArredondado(texto);
        botao.setFont(new Font("Verdana", Font.BOLD, 16));
        botao.setForeground(corTexto);
        botao.setBackground(corFundo);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setContentAreaFilled(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return botao;
    }

    private void mostrarMensagem(String texto) {
        JOptionPane.showMessageDialog(this, texto);
    }

    private void fecharTela() {
        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja sair da tela de gerenciamento de perguntas?",
                "Sair",
                JOptionPane.YES_NO_OPTION
        );

        if (resposta == JOptionPane.YES_OPTION) {
            dispose();
            new TelaMenuProfessor().setVisible(true);
        }
    }

    private void configurarPreviaImagem(JLabel imagemLabel, String caminhoImagem) {
        try {
            Image imagem = ImageIO.read(new File(caminhoImagem));
            Image imagemRedimensionada = imagem.getScaledInstance(110, 100, Image.SCALE_SMOOTH);
            imagemLabel.setText("");
            imagemLabel.setIcon(new ImageIcon(imagemRedimensionada));
        } catch (IOException erro) {
            imagemLabel.setText("Imagem");
            imagemLabel.setIcon(null);
        }
    }

    private void carregarPerguntasExemplo() {
        bancoPerguntas.add(new PerguntaCadastro(
                "Qual a função do Béquer?",
                Arrays.asList(
                        "Misturar e aquecer líquidos",
                        "Transferir líquidos",
                        "Medir volume exato de líquido",
                        "Liberar volume controlado"
                ),
                0,
                "Fácil",
                ""
        ));

        bancoPerguntas.add(new PerguntaCadastro(
                "Qual a função do funil?",
                Arrays.asList(
                        "Misturar líquidos",
                        "Aquecimento de soluções",
                        "Medir soluções",
                        "Transferir líquidos e auxiliar na filtração"
                ),
                3,
                "Fácil",
                ""
        ));

        bancoPerguntas.add(new PerguntaCadastro(
                "Qual material é usado em um sistema de titulação?",
                Arrays.asList(
                        "Proveta",
                        "Bureta",
                        "Béquer",
                        "Bastão de vidro"
                ),
                1,
                "Médio",
                ""
        ));

        bancoPerguntas.add(new PerguntaCadastro(
                "Qual material é usado em um sistema de mistura?",
                Arrays.asList(
                        "Pipeta",
                        "Bureta",
                        "Béquer",
                        "Funil"
                ),
                2,
                "Fácil",
                ""
        ));

        bancoPerguntas.add(new PerguntaCadastro(
                "Qual a função do bastão de vidro?",
                Arrays.asList(
                        "Aquecimento",
                        "Titulação",
                        "Filtração",
                        "Misturar soluções"
                ),
                3,
                "Fácil",
                ""
        ));

        bancoPerguntas.add(new PerguntaCadastro(
                "Qual material é usado em um sistema de filtração?",
                Arrays.asList(
                        "Funil",
                        "Pipeta",
                        "Bastão de vidro",
                        "Proveta"
                ),
                0,
                "Fácil",
                ""
        ));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaGerenciarPerguntas tela = new TelaGerenciarPerguntas();
            tela.setVisible(true);
        });
    }

    private static class PerguntaCadastro {
        private String enunciado;
        private List<String> alternativas;
        private int indiceCorreta;
        private String nivel;
        private String caminhoImagem;

        public PerguntaCadastro(String enunciado, List<String> alternativas, int indiceCorreta, String nivel, String caminhoImagem) {
            this.enunciado = enunciado;
            this.alternativas = new ArrayList<>(alternativas);
            this.indiceCorreta = indiceCorreta;
            this.nivel = nivel;
            this.caminhoImagem = caminhoImagem;
        }

        public String getEnunciado() {
            return enunciado;
        }

        public List<String> getAlternativas() {
            return alternativas;
        }

        public void setAlternativas(List<String> alternativas) {
            this.alternativas = new ArrayList<>(alternativas);
        }

        public int getIndiceCorreta() {
            return indiceCorreta;
        }

        public void setIndiceCorreta(int indiceCorreta) {
            this.indiceCorreta = indiceCorreta;
        }

        public String getNivel() {
            return nivel;
        }

        public String getCaminhoImagem() {
            return caminhoImagem;
        }

        public void setCaminhoImagem(String caminhoImagem) {
            this.caminhoImagem = caminhoImagem;
        }

        public String getResumoRespostaCorreta() {
            String[] letras = {"a", "b", "c", "d"};
            return letras[indiceCorreta] + ") " + alternativas.get(indiceCorreta);
        }
    }

    private static class BotaoArredondado extends JButton {

        public BotaoArredondado(String texto) {
            super(texto);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics grafico) {
            Graphics2D desenho = (Graphics2D) grafico.create();
            desenho.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            desenho.setColor(getBackground());
            desenho.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

            super.paintComponent(grafico);
            desenho.dispose();
        }
    }

    private static class PainelArredondado extends JPanel {
        private final Color corFundo;
        private final Color corBorda;
        private final int espessuraBorda;

        public PainelArredondado(Color corFundo, Color corBorda, int espessuraBorda) {
            this.corFundo = corFundo;
            this.corBorda = corBorda;
            this.espessuraBorda = espessuraBorda;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics grafico) {
            super.paintComponent(grafico);

            Graphics2D desenho = (Graphics2D) grafico.create();
            desenho.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            desenho.setColor(corFundo);
            desenho.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);

            if (espessuraBorda > 0) {
                desenho.setColor(corBorda);
                desenho.setStroke(new BasicStroke(espessuraBorda));
                desenho.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
            }

            desenho.dispose();
        }
    }

private static class PainelFundoImagem extends JPanel {

    private Image imagemFundo;

    public PainelFundoImagem() {
        carregarImagem();
    }

    private void carregarImagem() {
        try {
            File arquivo = new File("imagens/Menu.png");
            imagemFundo = ImageIO.read(arquivo);
        } catch (IOException erro) {
            System.out.println("Erro ao carregar imagem de fundo: " + erro.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics grafico) {
        super.paintComponent(grafico);

        if (imagemFundo != null) {
            grafico.drawImage(imagemFundo, 0, 0, getWidth(), getHeight(), this);
        } else {
            grafico.setColor(new Color(223, 239, 252));
            grafico.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}

}
