package telas.jogo;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import model.Alternativa;
import model.Partida;
import model.Questao;
import model.Resposta;

public class TelaJogo extends JFrame {

    private Partida partida;
    private List<Questao> questoes;
    private int indiceQuestaoAtual = 0;

    private JLabel labelPergunta;
    private JLabel labelImagemQuestao;
    private JPanel painelAlternativas;
    private List<BotaoAlternativa> botoesAlternativas;

    public TelaJogo(Partida partida, List<Questao> questoes) {
        this.partida = partida;
        this.questoes = questoes;
        this.botoesAlternativas = new ArrayList<>();

        configurarJanela();
        montarTela();
        carregarQuestaoAtual();
    }

    private void configurarJanela() {
        setTitle("LabQuest - Jogo");
        setSize(960, 680);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void montarTela() {
        PainelFundo painelFundo = new PainelFundo();
        painelFundo.setLayout(null);
        setContentPane(painelFundo);

        // Ícones do Topo Direito
        JPanel painelIcones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        painelIcones.setOpaque(false);
        painelIcones.setBounds(750, 10, 180, 80);

        JButton botaoAjuda = criarBotaoIcone("?!", new Color(47, 76, 113));
        JButton botaoSair = criarBotaoIcone("➡", new Color(47, 76, 113));
        botaoSair.addActionListener(e -> confirmarSaida());

        painelIcones.add(botaoAjuda);
        painelIcones.add(botaoSair);
        painelFundo.add(painelIcones);

        // Painel Central da Pergunta
        PainelArredondado painelPergunta = new PainelArredondado(30);
        painelPergunta.setLayout(null);
        painelPergunta.setBounds(100, 120, 760, 320);
        painelPergunta.setBackground(new Color(47, 76, 113));
        painelFundo.add(painelPergunta);

        labelPergunta = new JLabel("Pergunta aqui", SwingConstants.CENTER);
        labelPergunta.setBounds(20, 20, 720, 50);
        labelPergunta.setFont(new Font("Arial", Font.BOLD, 32));
        labelPergunta.setForeground(Color.WHITE);
        painelPergunta.add(labelPergunta);

        labelImagemQuestao = new JLabel("", SwingConstants.CENTER);
        labelImagemQuestao.setBounds(230, 80, 300, 220);
        painelPergunta.add(labelImagemQuestao);

        // Painel de Alternativas
        painelAlternativas = new JPanel();
        painelAlternativas.setLayout(null);
        painelAlternativas.setOpaque(false);
        painelAlternativas.setBounds(100, 460, 760, 180);
        painelFundo.add(painelAlternativas);
    }

    private JButton criarBotaoIcone(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(60, 60));
        btn.setFont(new Font("Arial", Font.BOLD, 30));
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
        labelPergunta.setText(questao.getEnunciado());
        
        // Simulação de imagem do béquer se não houver imagem real
        if (questao.getImagemEnunciado() != null && !questao.getImagemEnunciado().isEmpty()) {
            labelImagemQuestao.setIcon(new ImageIcon(questao.getImagemEnunciado()));
        } else {
            // Placeholder visual para o protótipo
            labelImagemQuestao.setText("<html><div style='text-align:center; color:white; font-size:100px;'>🧪</div></html>");
        }

        painelAlternativas.removeAll();
        botoesAlternativas.clear();

        List<Alternativa> alternativas = questao.getAlternativas();
        int y = 0;
        char letra = 'a';
        for (Alternativa alt : alternativas) {
            BotaoAlternativa btn = new BotaoAlternativa(letra + ") " + alt.getTexto());
            btn.setBounds(0, y, 760, 40);
            btn.addActionListener(e -> processarResposta(alt));
            painelAlternativas.add(btn);
            botoesAlternativas.add(btn);
            y += 45;
            letra++;
        }

        painelAlternativas.revalidate();
        painelAlternativas.repaint();
    }

    private void processarResposta(Alternativa escolhida) {
        Questao questaoAtual = questoes.get(indiceQuestaoAtual);
        Resposta resposta = new Resposta();
        // resposta.setQuestao(questaoAtual); // Depende da implementação do setQuestao
        // resposta.setAlternativaEscolhida(escolhida);
        partida.adicionarResposta(resposta);

        if (escolhida.isECorreta()) {
            JOptionPane.showMessageDialog(this, "Correto!", "Resultado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Incorreto!", "Resultado", JOptionPane.ERROR_MESSAGE);
        }

        indiceQuestaoAtual++;
        carregarQuestaoAtual();
    }

    private void confirmarSaida() {
        int opt = JOptionPane.showConfirmDialog(this, "Deseja realmente sair do jogo?", "Sair", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private void finalizarPartida() {
        partida.finalizar();
        JOptionPane.showMessageDialog(this, "Fim de jogo! Pontuação: " + partida.getPontuacao());
        dispose();
    }

    // Componentes Customizados
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
        }
    }

    private static class BotaoAlternativa extends JButton {
        public BotaoAlternativa(String texto) {
            super(texto);
            setHorizontalAlignment(SwingConstants.LEFT);
            setFont(new Font("Arial", Font.BOLD, 22));
            setForeground(Color.WHITE);
            setBackground(new Color(130, 150, 220));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    private static class PainelFundo extends JPanel {
        private Image imagemFundo;
        public PainelFundo() {
            try {
                imagemFundo = new ImageIcon("imagens/menu.png").getImage();
            } catch (Exception e) {}
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            if (imagemFundo != null) {
                double escala = Math.max((double)getWidth()/imagemFundo.getWidth(null), (double)getHeight()/imagemFundo.getHeight(null));
                int nw = (int)(imagemFundo.getWidth(null)*escala);
                int nh = (int)(imagemFundo.getHeight(null)*escala);
                g2.drawImage(imagemFundo, (getWidth()-nw)/2, (getHeight()-nh)/2, nw, nh, null);
            } else {
                g2.setColor(new Color(223, 239, 252));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Criando dados de teste
            model.Aluno alunoTeste = new model.Aluno(1, "Estudante", "aluno@aluno.cps.sp.gov.br", "123", "1A", "12345");
            model.Partida partidaTeste = new model.Partida(alunoTeste);
            
            List<model.Questao> listaTeste = new ArrayList<>();
            
            // Questão de Teste (Béquer)
            model.Questao q1 = new model.Questao(1, "Qual é a principal função do Béquer?", model.tipos.TipoQuestao.MULTIPLA_ESCOLHA, model.tipos.NivelDificuldade.FACIL, "Química");
            q1.setImagemEnunciado("imagens/Béquer.jpg"); // Adicionando o caminho da imagem
            q1.adicionarAlternativa(new model.Alternativa(1, "Misturar e aquecer líquidos", true));
            q1.adicionarAlternativa(new model.Alternativa(2, "Transferir líquidos", false));
            q1.adicionarAlternativa(new model.Alternativa(3, "Medir volume exato de líquido", false));
            q1.adicionarAlternativa(new model.Alternativa(4, "Liberar volume controlado", false));
            
            listaTeste.add(q1);

            // Abre a tela
            new TelaJogo(partidaTeste, listaTeste).setVisible(true);
        });
    }
}

