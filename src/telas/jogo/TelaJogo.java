package telas.jogo;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import model.Alternativa;
import model.Partida;
import model.Questao;
import model.tipos.TipoQuestao;

/**
 * Tela principal do jogo LabQuest.
 * Gerencia a exibição de questões de múltipla escolha e associação,
 * além do sistema de ajuda e progressão da partida.
 */
public class TelaJogo extends JFrame {

    private Partida partida;
    private List<Questao> questoes;
    private int indiceQuestaoAtual = 0;

    private JLabel labelPergunta;
    private JLabel labelImagemQuestao;
    private JPanel painelConteudo;
    private List<BotaoAlternativa> botoesAlternativas;
    
    // Controle do Sistema de Ajuda
    private boolean ajuda5050Usada = false;
    private boolean chanceExtraUsada = false;
    private boolean pularUsada = false;
    private boolean chanceExtraAtiva = false;

    // Controle do Modo Associação
    private BotaoAssociacao selecionadoEsquerda = null;
    private BotaoAssociacao selecionadoDireita = null;
    private int paresResolvidos = 0;

    public TelaJogo(Partida partida, List<Questao> questoes) {
        this.partida = partida;
        this.questoes = questoes;
        this.botoesAlternativas = new ArrayList<>();

        configurarJanela();
        montarEstruturaBase();
        carregarQuestaoAtual();
    }

    private void configurarJanela() {
        setTitle("LabQuest - Desafio de Laboratório");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void montarEstruturaBase() {
        PainelFundo painelFundo = new PainelFundo();
        painelFundo.setLayout(null);
        setContentPane(painelFundo);

        // Barra Superior: Ícones de Perfil e Sair (Usando imagens reais)
        JPanel painelIcones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        painelIcones.setOpaque(false);
        painelIcones.setBounds(800, 10, 180, 80);

        JButton botaoPerfil = criarBotaoIconeReal("imagens/Perfil.png");
        botaoPerfil.addActionListener(e -> abrirPerfil());
        
        JButton botaoSair = criarBotaoIconeReal("imagens/Sair.png");
        botaoSair.addActionListener(e -> confirmarSaida());

        painelIcones.add(botaoPerfil);
        painelIcones.add(botaoSair);
        painelFundo.add(painelIcones);

        // Botão de Ajuda (Fica separado ou junto)
        JButton botaoAjuda = new JButton("?!");
        botaoAjuda.setBounds(20, 20, 50, 50);
        botaoAjuda.setFont(new Font("Arial", Font.BOLD, 24));
        botaoAjuda.setForeground(Color.WHITE);
        botaoAjuda.setBackground(new Color(47, 76, 113));
        botaoAjuda.setFocusPainted(false);
        botaoAjuda.setBorderPainted(false);
        botaoAjuda.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoAjuda.addActionListener(e -> mostrarPopUpAjuda());
        painelFundo.add(botaoAjuda);

        // Área Central Dinâmica
        painelConteudo = new JPanel();
        painelConteudo.setLayout(null);
        painelConteudo.setOpaque(false);
        painelConteudo.setBounds(50, 100, 900, 580);
        painelFundo.add(painelConteudo);
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
        JOptionPane.showMessageDialog(this, "Perfil do jogador...");
    }

    private JButton criarBotaoIcone(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(65, 65));
        btn.setFont(new Font("Arial", Font.BOLD, 32));
        btn.setForeground(Color.WHITE);
        btn.setBackground(cor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void carregarQuestaoAtual() {
        if (indiceQuestaoAtual >= questoes.size()) {
            finalizarPartida();
            return;
        }

        Questao questao = questoes.get(indiceQuestaoAtual);
        painelConteudo.removeAll();
        chanceExtraAtiva = false;
        paresResolvidos = 0;
        selecionadoEsquerda = null;
        selecionadoDireita = null;

        if (questao.getTipo() == TipoQuestao.ASSOCIACAO) {
            montarInterfaceAssociacao(questao);
        } else {
            montarInterfaceAlternativa(questao);
        }

        painelConteudo.revalidate();
        painelConteudo.repaint();
    }

    private void montarInterfaceAlternativa(Questao questao) {
        // Painel da Pergunta (Fundo Azul Escuro Arredondado)
        PainelArredondado painelPergunta = new PainelArredondado(30);
        painelPergunta.setLayout(null);
        painelPergunta.setBounds(50, 10, 800, 320);
        painelPergunta.setBackground(new Color(47, 76, 113));
        painelConteudo.add(painelPergunta);

        labelPergunta = new JLabel("<html><center>" + questao.getEnunciado() + "</center></html>", SwingConstants.CENTER);
        labelPergunta.setBounds(40, 20, 720, 60);
        labelPergunta.setFont(new Font("Arial", Font.BOLD, 28));
        labelPergunta.setForeground(Color.WHITE);
        painelPergunta.add(labelPergunta);

        labelImagemQuestao = new JLabel("", SwingConstants.CENTER);
        labelImagemQuestao.setBounds(250, 90, 300, 210);
        if (questao.getImagemEnunciado() != null && !questao.getImagemEnunciado().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(questao.getImagemEnunciado());
                Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                labelImagemQuestao.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                labelImagemQuestao.setText("🧪");
                labelImagemQuestao.setFont(new Font("Arial", Font.PLAIN, 100));
                labelImagemQuestao.setForeground(Color.WHITE);
            }
        }
        painelPergunta.add(labelImagemQuestao);

        // Botões de Alternativas (Azul Claro Arredondado)
        botoesAlternativas.clear();
        List<Alternativa> alternativas = questao.getAlternativas();
        int y = 350;
        char letra = 'a';
        for (Alternativa alt : alternativas) {
            BotaoAlternativa btn = new BotaoAlternativa(letra + ") " + alt.getTexto());
            btn.setBounds(50, y, 800, 48);
            btn.addActionListener(e -> processarRespostaAlternativa(alt, btn));
            painelConteudo.add(btn);
            botoesAlternativas.add(btn);
            y += 58;
            letra++;
        }
    }

    private void montarInterfaceAssociacao(Questao questao) {
        // Cabeçalho de Instrução
        PainelArredondado painelInstrucao = new PainelArredondado(20);
        painelInstrucao.setLayout(new BorderLayout());
        painelInstrucao.setBounds(100, 5, 700, 70);
        painelInstrucao.setBackground(new Color(47, 76, 113));
        
        JLabel lblMsg = new JLabel("Conecte o material ao sistema experimental correspondente", SwingConstants.CENTER);
        lblMsg.setFont(new Font("Arial", Font.BOLD, 18));
        lblMsg.setForeground(Color.WHITE);
        painelInstrucao.add(lblMsg);
        painelConteudo.add(painelInstrucao);

        // Dados Fixos para Demonstração (Podem vir do banco de dados futuramente)
        String[][] pares = {
            {"Bureta", "Titulação", "imagens/Bureta.jpg"},
            {"Condensador", "Destilação", "imagens/BicoDeBunsen.jpg"},
            {"Funil de Decantação", "Extração", "imagens/FunilDeHasteLonga.jpg"},
            {"Balão Volumétrico", "Diluição", "imagens/BalãoVolumétrico.jpg"}
        };

        List<Integer> ordemEsq = new ArrayList<>();
        List<Integer> ordemDir = new ArrayList<>();
        for(int i=0; i<4; i++) { ordemEsq.add(i); ordemDir.add(i); }
        Collections.shuffle(ordemEsq);
        Collections.shuffle(ordemDir);

        int y = 90;
        for (int i = 0; i < 4; i++) {
            final int idxEsq = ordemEsq.get(i);
            final int idxDir = ordemDir.get(i);
            
            // Lado Esquerdo: Material (Com Imagem)
            BotaoAssociacao btnEsq = new BotaoAssociacao(pares[idxEsq][0], pares[idxEsq][2], true);
            btnEsq.setBounds(50, y, 230, 110);
            btnEsq.addActionListener(e -> lidarCliqueAssociacao(btnEsq, true, pares[idxEsq][1]));
            painelConteudo.add(btnEsq);

            // Lado Direito: Sistema (Apenas Texto)
            BotaoAssociacao btnDir = new BotaoAssociacao(pares[idxDir][1], null, false);
            btnDir.setBounds(620, y, 230, 110);
            btnDir.addActionListener(e -> lidarCliqueAssociacao(btnDir, false, pares[idxDir][1]));
            painelConteudo.add(btnDir);

            y += 120;
        }
    }

    private void lidarCliqueAssociacao(BotaoAssociacao btn, boolean esq, String idCorreto) {
        if (btn.isResolvido()) return;

        if (esq) {
            if (selecionadoEsquerda != null) selecionadoEsquerda.setSelecionado(false);
            selecionadoEsquerda = btn;
            selecionadoEsquerda.setSelecionado(true);
            selecionadoEsquerda.setIdPar(idCorreto);
        } else {
            if (selecionadoDireita != null) selecionadoDireita.setSelecionado(false);
            selecionadoDireita = btn;
            selecionadoDireita.setSelecionado(true);
            selecionadoDireita.setIdPar(idCorreto);
        }

        // Se ambos os lados forem selecionados, valida o par
        if (selecionadoEsquerda != null && selecionadoDireita != null) {
            if (selecionadoEsquerda.getIdPar().equals(selecionadoDireita.getIdPar())) {
                // Acerto
                selecionadoEsquerda.setResolvido(true);
                selecionadoDireita.setResolvido(true);
                selecionadoEsquerda.setBackground(new Color(46, 204, 113));
                selecionadoDireita.setBackground(new Color(46, 204, 113));
                paresResolvidos++;
                
                if (paresResolvidos == 4) {
                    Timer t = new Timer(800, e -> { indiceQuestaoAtual++; carregarQuestaoAtual(); });
                    t.setRepeats(false);
                    t.start();
                }
            } else {
                // Erro
                selecionadoEsquerda.setBackground(new Color(231, 76, 60));
                selecionadoDireita.setBackground(new Color(231, 76, 60));
                Timer t = new Timer(500, e -> {
                    if (selecionadoEsquerda != null) {
                        selecionadoEsquerda.setSelecionado(false);
                        selecionadoEsquerda.setBackground(new Color(130, 150, 220));
                    }
                    if (selecionadoDireita != null) {
                        selecionadoDireita.setSelecionado(false);
                        selecionadoDireita.setBackground(new Color(130, 150, 220));
                    }
                    selecionadoEsquerda = null;
                    selecionadoDireita = null;
                });
                t.setRepeats(false);
                t.start();
                return;
            }
            selecionadoEsquerda = null;
            selecionadoDireita = null;
        }
    }

    private void mostrarPopUpAjuda() {
        JDialog dialog = new JDialog(this, "Suporte LabQuest", true);
        dialog.setSize(600, 480);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);
        dialog.getContentPane().setBackground(new Color(47, 76, 113));

        JLabel titulo = new JLabel("Central de Ajuda", SwingConstants.CENTER);
        titulo.setBounds(0, 20, 600, 40);
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setForeground(Color.WHITE);
        dialog.add(titulo);

        // Botões de Ajuda
        JButton btn5050 = criarBotaoAjuda("50/50", 50, 85, !ajuda5050Usada);
        btn5050.addActionListener(e -> { usar5050(); dialog.dispose(); });
        
        JButton btnChance = criarBotaoAjuda("Chance Extra", 310, 85, !chanceExtraUsada);
        btnChance.addActionListener(e -> { usarChanceExtra(); dialog.dispose(); });

        JButton btnPular = criarBotaoAjuda("Pular Questão", 180, 245, !pularUsada);
        btnPular.addActionListener(e -> { usarPular(); dialog.dispose(); });

        dialog.add(btn5050);
        dialog.add(btnChance);
        dialog.add(btnPular);

        JLabel lblInfo = new JLabel("Você pode usar cada ajuda apenas uma vez por partida.", SwingConstants.CENTER);
        lblInfo.setBounds(0, 400, 600, 20);
        lblInfo.setForeground(new Color(200, 200, 200));
        dialog.add(lblInfo);

        dialog.setVisible(true);
    }

    private JButton criarBotaoAjuda(String texto, int x, int y, boolean ativo) {
        JButton btn = new JButton(texto);
        btn.setBounds(x, y, 240, 145);
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        btn.setBackground(ativo ? Color.WHITE : new Color(100, 100, 100));
        btn.setForeground(new Color(47, 76, 113));
        btn.setFocusPainted(false);
        btn.setEnabled(ativo);
        btn.setCursor(ativo ? new Cursor(Cursor.HAND_CURSOR) : null);
        return btn;
    }

    private void usar5050() {
        ajuda5050Usada = true;
        Questao q = questoes.get(indiceQuestaoAtual);
        if (q.getTipo() == TipoQuestao.ASSOCIACAO) return;

        int removidas = 0;
        List<BotaoAlternativa> copia = new ArrayList<>(botoesAlternativas);
        Collections.shuffle(copia);

        for (BotaoAlternativa btn : copia) {
            String txt = btn.getText().substring(3);
            Alternativa alt = q.getAlternativas().stream()
                .filter(a -> a.getTexto().equals(txt)).findFirst().orElse(null);

            if (alt != null && !alt.isECorreta() && removidas < 2) {
                btn.setEnabled(false);
                btn.setAlpha(0.2f);
                removidas++;
            }
        }
    }

    private void usarChanceExtra() {
        chanceExtraUsada = true;
        chanceExtraAtiva = true;
        JOptionPane.showMessageDialog(this, "Ajuda Ativada: Se errar agora, você terá uma segunda chance!");
    }

    private void usarPular() {
        pularUsada = true;
        indiceQuestaoAtual++;
        carregarQuestaoAtual();
    }

    private void processarRespostaAlternativa(Alternativa escolhida, BotaoAlternativa btn) {
        if (escolhida.isECorreta()) {
            btn.setBackground(new Color(46, 204, 113));
            JOptionPane.showMessageDialog(this, "Resposta Correta! +10 pontos.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            indiceQuestaoAtual++;
            carregarQuestaoAtual();
        } else {
            if (chanceExtraAtiva) {
                btn.setEnabled(false);
                btn.setBackground(new Color(231, 76, 60));
                chanceExtraAtiva = false;
                JOptionPane.showMessageDialog(this, "Ops! Resposta errada, mas você tem sua Chance Extra. Tente de novo!", "Segunda Chance", JOptionPane.WARNING_MESSAGE);
            } else {
                btn.setBackground(new Color(231, 76, 60));
                JOptionPane.showMessageDialog(this, "Incorreto! A resposta certa seria destacada.", "Fim de Turno", JOptionPane.ERROR_MESSAGE);
                indiceQuestaoAtual++;
                carregarQuestaoAtual();
            }
        }
    }

    private void confirmarSaida() {
        int opt = JOptionPane.showConfirmDialog(this, "Sair agora interromperá sua partida. Confirmar?", "Sair do Jogo", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) dispose();
    }

    private void finalizarPartida() {
        partida.finalizar();
        JOptionPane.showMessageDialog(this, "Desafio Concluído!\nSua Pontuação Final: " + partida.getPontuacao(), "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    // --- Componentes Visuais Customizados ---

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

    private static class BotaoAlternativa extends JButton {
        private float alpha = 1.0f;
        public BotaoAlternativa(String texto) {
            super(texto);
            setHorizontalAlignment(SwingConstants.LEFT);
            setFont(new Font("Arial", Font.BOLD, 20));
            setForeground(Color.WHITE);
            setBackground(new Color(130, 150, 220));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        }
        public void setAlpha(float a) { this.alpha = a; repaint(); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    private static class BotaoAssociacao extends JButton {
        private boolean resolvido = false;
        private boolean selecionado = false;
        private String idPar;

        public BotaoAssociacao(String texto, String caminhoImg, boolean comImagem) {
            setLayout(new BorderLayout());
            setBackground(new Color(130, 150, 220));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            if (comImagem && caminhoImg != null) {
                try {
                    ImageIcon icon = new ImageIcon(caminhoImg);
                    Image img = icon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
                    add(new JLabel(new ImageIcon(img)), BorderLayout.CENTER);
                } catch (Exception e) {}
            }

            JLabel lbl = new JLabel("<html><center>" + texto + "</center></html>", SwingConstants.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, 14));
            lbl.setForeground(Color.WHITE);
            add(lbl, comImagem ? BorderLayout.SOUTH : BorderLayout.CENTER);
        }

        public void setResolvido(boolean v) { this.resolvido = v; setEnabled(!v); }
        public boolean isResolvido() { return resolvido; }
        public void setSelecionado(boolean v) { this.selecionado = v; repaint(); }
        public void setIdPar(String id) { this.idPar = id; }
        public String getIdPar() { return idPar; }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            if (selecionado) g2.setColor(getBackground().darker());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            if (selecionado) {
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(2, 2, getWidth()-5, getHeight()-5, 18, 18);
            }
            super.paintComponent(g);
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

    // --- Ponto de Entrada para Testes ---

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            model.Aluno a = new model.Aluno(1, "Demo", "a@a.com", "1", "1A", "1");
            Partida p = new Partida(a);
            List<Questao> lista = new ArrayList<>();
            
            // Exemplo 1: Múltipla Escolha
            Questao q1 = new Questao(1, "Qual instrumento é usado para medir volumes exatos?", TipoQuestao.MULTIPLA_ESCOLHA, model.tipos.NivelDificuldade.FACIL, "Q");
            q1.setImagemEnunciado("imagens/Béquer.jpg");
            q1.adicionarAlternativa(new Alternativa(1, "Béquer", false));
            q1.adicionarAlternativa(new Alternativa(2, "Pipeta Volumétrica", true));
            q1.adicionarAlternativa(new Alternativa(3, "Tubo de Ensaio", false));
            q1.adicionarAlternativa(new Alternativa(4, "Bastão de Vidro", false));
            lista.add(q1);

            // Exemplo 2: Associação
            Questao q2 = new Questao(2, "Combine os materiais aos sistemas", TipoQuestao.ASSOCIACAO, model.tipos.NivelDificuldade.MEDIO, "Q");
            lista.add(q2);

            new TelaJogo(p, lista).setVisible(true);
        });
    }
}