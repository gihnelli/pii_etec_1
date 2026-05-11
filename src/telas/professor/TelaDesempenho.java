package telas.professor;

import java.awt.*;
import java.awt.print.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.Alternativa;
import model.Aluno;
import model.Partida;
import model.Questao;
import model.Resposta;

/**
 * Tela de Desempenho para o Professor.
 * Agora gera gráficos dinâmicos baseados em dados reais e exporta PDFs autênticos.
 */
public class TelaDesempenho extends JFrame {

    private JPanel painelPrincipal;
    private CardLayout navegador;
    private List<Aluno> listaAlunos;
    
    // Cores do Projeto
    private final Color AZUL_ESCURO = new Color(47, 76, 113);
    private final Color AZUL_CLARO = new Color(160, 205, 245);
    private final Color AZUL_MEDIO = new Color(70, 130, 200);

    public TelaDesempenho(List<Aluno> alunos) {
        this.listaAlunos = alunos;
        configurarJanela();
        montarEstrutura();
    }

    // Construtor padrão para testes
    public TelaDesempenho() {
        this(gerarDadosExemplo());
    }

    private void configurarJanela() {
        setTitle("LabQuest - Gestão de Desempenho");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void montarEstrutura() {
        navegador = new CardLayout();
        painelPrincipal = new JPanel(navegador);
        setContentPane(painelPrincipal);

        painelPrincipal.add(criarVisaoGeral(), "GERAL");
        painelPrincipal.add(criarVisaoListaAlunos(), "LISTA");
    }

    // --- CÁLCULOS DE DADOS REAIS ---
    
    private double[] calcularEstatisticasTurma() {
        int totalAcertos = 0;
        int totalRespostas = 0;

        for (Aluno a : listaAlunos) {
            for (Partida p : a.getHistoricoPartidas()) {
                totalAcertos += p.getTotalAcertos();
                totalRespostas += p.getRespostas().size();
            }
        }

        if (totalRespostas == 0) return new double[]{0, 0};
        double percAcertos = (totalAcertos * 100.0) / totalRespostas;
        return new double[]{percAcertos, 100.0 - percAcertos};
    }

    private double[] calcularEstatisticasAluno(Aluno aluno) {
        int acertos = 0, total = 0;
        for (Partida p : aluno.getHistoricoPartidas()) {
            acertos += p.getTotalAcertos();
            total += p.getRespostas().size();
        }
        if (total == 0) return new double[]{0, 0};
        double perc = (acertos * 100.0) / total;
        return new double[]{perc, 100.0 - perc};
    }

    // --- VISÃO 1: GRÁFICO GERAL ---
    private JPanel criarVisaoGeral() {
        PainelFundo painel = new PainelFundo();
        painel.setLayout(null);
        adicionarIconesNavegacao(painel);

        double[] stats = calcularEstatisticasTurma();

        PainelArredondado painelGrafico = new PainelArredondado(30);
        painelGrafico.setBounds(30, 130, 480, 550);
        painelGrafico.setBackground(AZUL_ESCURO);
        painelGrafico.setLayout(new BorderLayout());
        
        JLabel lblTit = new JLabel("Desempenho Geral da Turma", SwingConstants.CENTER);
        lblTit.setFont(new Font("Arial", Font.BOLD, 24));
        lblTit.setForeground(Color.WHITE);
        lblTit.setBorder(new EmptyBorder(20, 0, 0, 0));
        painelGrafico.add(lblTit, BorderLayout.NORTH);

        GraficoPizza grafico = new GraficoPizza(stats[0], stats[1]);
        painelGrafico.add(grafico, BorderLayout.CENTER);
        painel.add(painelGrafico);

        BotaoMenu btnLista = new BotaoMenu("Ver Lista de Alunos");
        btnLista.setBounds(540, 380, 410, 75);
        btnLista.addActionListener(e -> navegador.show(painelPrincipal, "LISTA"));
        painel.add(btnLista);

        PainelArredondado pPdf = new PainelArredondado(20);
        pPdf.setBounds(540, 485, 410, 110);
        pPdf.setBackground(AZUL_ESCURO);
        pPdf.setLayout(null);
        
        JLabel lPdf = new JLabel("Exportar Relatório Geral", SwingConstants.CENTER);
        lPdf.setBounds(0, 10, 410, 30);
        lPdf.setFont(new Font("Arial", Font.BOLD, 18));
        lPdf.setForeground(Color.WHITE);
        pPdf.add(lPdf);

        JButton bPdf = new JButton("Gerar PDF da Turma");
        bPdf.setBounds(20, 50, 370, 45);
        bPdf.setFont(new Font("Arial", Font.BOLD, 18));
        bPdf.addActionListener(e -> exportarParaPDF("GERAL", "Relatorio_Geral_Turma"));
        pPdf.add(bPdf);
        painel.add(pPdf);

        return painel;
    }

    // --- VISÃO 2: LISTA DE ALUNOS ---
    private JPanel criarVisaoListaAlunos() {
        PainelFundo painel = new PainelFundo();
        painel.setLayout(null);
        adicionarIconesNavegacao(painel);

        PainelArredondado cab = new PainelArredondado(25);
        cab.setBounds(350, 35, 300, 50);
        cab.setBackground(AZUL_ESCURO);
        cab.setLayout(new BorderLayout());
        JLabel t = new JLabel("Alunos Cadastrados", SwingConstants.CENTER);
        t.setFont(new Font("Arial", Font.BOLD, 22));
        t.setForeground(Color.WHITE);
        cab.add(t);
        painel.add(cab);

        JPanel cont = new JPanel();
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
        cont.setOpaque(false);

        for (Aluno aluno : listaAlunos) {
            cont.add(new ItemListaAluno(aluno));
            cont.add(Box.createVerticalStrut(10));
        }

        JScrollPane scroll = new JScrollPane(cont);
        scroll.setBounds(30, 110, 930, 580);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        painel.add(scroll);

        return painel;
    }

    // --- GERAÇÃO REAL DE PDF (Via API de Impressão) ---
    private void exportarParaPDF(Object alvo, String nomeArquivo) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName(nomeArquivo);

        job.setPrintable(new RelatorioPrintable(alvo));

        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
                JOptionPane.showMessageDialog(this, "Relatório PDF gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao gerar PDF: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Classe responsável por desenhar o conteúdo do PDF de forma vetorial.
     */
    private class RelatorioPrintable implements Printable {
        private Object alvo;

        public RelatorioPrintable(Object alvo) {
            this.alvo = alvo;
        }

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex > 0) return NO_SUCH_PAGE;

            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Cabeçalho
            g2d.setColor(AZUL_ESCURO);
            g2d.fillRect(0, 0, (int) pageFormat.getImageableWidth(), 60);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 22));
            g2d.drawString("LabQuest - Relatório de Desempenho", 20, 40);

            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            String data = "Gerado em: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            g2d.drawString(data, (int) pageFormat.getImageableWidth() - 200, 80);

            if (alvo instanceof Aluno aluno) {
                desenharRelatorioAluno(g2d, aluno, pageFormat);
            } else {
                desenharRelatorioGeral(g2d, pageFormat);
            }

            return PAGE_EXISTS;
        }

        private void desenharRelatorioAluno(Graphics2D g2d, Aluno aluno, PageFormat pf) {
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.drawString("Aluno: " + aluno.getNome(), 20, 110);
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.drawString("Turma: " + aluno.getTurma(), 20, 130);
            g2d.drawString("Email: " + aluno.getEmail(), 20, 150);

            double[] stats = calcularEstatisticasAluno(aluno);
            
            // Desenhar Gráfico no PDF
            int chartSize = 200;
            int cx = 350;
            int cy = 100;
            desenharGraficoVetor(g2d, cx, cy, chartSize, stats[0], stats[1]);

            // Tabela de Partidas
            int y = 280;
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("Histórico de Partidas", 20, y);
            y += 25;
            
            g2d.drawLine(20, y, (int) pf.getImageableWidth() - 20, y);
            y += 20;
            g2d.drawString("Data/Hora", 25, y);
            g2d.drawString("Pontuação", 200, y);
            g2d.drawString("Acertos", 300, y);
            g2d.drawString("Erros", 400, y);
            y += 10;
            g2d.drawLine(20, y, (int) pf.getImageableWidth() - 20, y);
            y += 20;

            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            for (Partida p : aluno.getHistoricoPartidas()) {
                if (y > pf.getImageableHeight() - 50) break;
                String dt = p.getDataHoraInicio().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                g2d.drawString(dt, 25, y);
                g2d.drawString(String.valueOf(p.getPontuacao()), 200, y);
                g2d.drawString(String.valueOf(p.getTotalAcertos()), 300, y);
                g2d.drawString(String.valueOf(p.getTotalErros()), 400, y);
                y += 20;
            }
        }

        private void desenharRelatorioGeral(Graphics2D g2d, PageFormat pf) {
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.drawString("Resumo Geral da Turma", 20, 110);

            double[] stats = calcularEstatisticasTurma();
            desenharGraficoVetor(g2d, 350, 100, 200, stats[0], stats[1]);

            int y = 300;
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("Ranking de Alunos", 20, y);
            y += 25;
            
            g2d.drawLine(20, y, (int) pf.getImageableWidth() - 20, y);
            y += 20;
            g2d.drawString("Nome", 25, y);
            g2d.drawString("Turma", 250, y);
            g2d.drawString("Média Acertos", 400, y);
            y += 10;
            g2d.drawLine(20, y, (int) pf.getImageableWidth() - 20, y);
            y += 20;

            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            for (Aluno a : listaAlunos) {
                if (y > pf.getImageableHeight() - 50) break;
                double[] s = calcularEstatisticasAluno(a);
                g2d.drawString(a.getNome(), 25, y);
                g2d.drawString(a.getTurma(), 250, y);
                g2d.drawString(String.format("%.1f%%", s[0]), 400, y);
                y += 20;
            }
        }

        private void desenharGraficoVetor(Graphics2D g2d, int x, int y, int size, double acertos, double erros) {
            int angA = (int) Math.round(acertos * 3.6);
            int angE = 360 - angA;

            // Sombra
            g2d.setColor(new Color(0, 0, 0, 50));
            g2d.fillOval(x + 5, y + 5, size, size);

            g2d.setColor(AZUL_CLARO);
            g2d.fillArc(x, y, size, size, 90, -angA);
            g2d.setColor(AZUL_MEDIO);
            g2d.fillArc(x, y, size, size, 90 - angA, -angE);

            // Legenda
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            g2d.setColor(Color.BLACK);
            g2d.fillRect(x + size + 20, y + 40, 15, 15);
            g2d.drawString(String.format("Acertos (%.1f%%)", acertos), x + size + 40, y + 52);
            
            g2d.setColor(AZUL_MEDIO);
            g2d.fillRect(x + size + 20, y + 65, 15, 15);
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.format("Erros (%.1f%%)", erros), x + size + 40, y + 77);
        }
    }

    private void adicionarIconesNavegacao(JPanel painel) {
        JButton btnPerfil = criarBotaoIconeReal("imagens/Perfil.png");
        btnPerfil.setBounds(20, 20, 45, 45);
        painel.add(btnPerfil);

        JButton btnSair = criarBotaoIconeReal("imagens/Sair.png");
        btnSair.setBounds(920, 20, 45, 45);
        btnSair.addActionListener(e -> {
            if (painel.getLayout() instanceof CardLayout) {
                 dispose();
            } else {
                navegador.show(painelPrincipal, "GERAL");
            }
        });
        painel.add(btnSair);
    }

    private JButton criarBotaoIconeReal(String caminho) {
        JButton btn = new JButton();
        try {
            ImageIcon icon = new ImageIcon(caminho);
            Image img = icon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        } catch (Exception e) {}
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- COMPONENTES ---

    private class ItemListaAluno extends JPanel {
        private boolean exp = false;
        private JPanel pInfo;

        public ItemListaAluno(Aluno aluno) {
            setLayout(new BorderLayout());
            setOpaque(false);
            setMaximumSize(new Dimension(900, 400));

            PainelArredondado h = new PainelArredondado(20);
            h.setPreferredSize(new Dimension(900, 55));
            h.setBackground(new Color(235, 235, 235));
            h.setLayout(new BorderLayout());
            h.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JLabel n = new JLabel(aluno.getNome() + " (" + aluno.getTurma() + ")");
            n.setFont(new Font("Arial", Font.BOLD, 18));
            n.setBorder(new EmptyBorder(0, 20, 0, 0));
            h.add(n, BorderLayout.WEST);

            JLabel s = new JLabel("<  ");
            s.setFont(new Font("Arial", Font.BOLD, 20));
            h.add(s, BorderLayout.EAST);
            add(h, BorderLayout.NORTH);

            pInfo = new PainelArredondado(20);
            pInfo.setPreferredSize(new Dimension(900, 320));
            pInfo.setBackground(new Color(210, 210, 210));
            pInfo.setLayout(null);
            pInfo.setVisible(false);

            double[] stats = calcularEstatisticasAluno(aluno);
            
            PainelArredondado pG = new PainelArredondado(20);
            pG.setBounds(20, 20, 450, 280);
            pG.setBackground(AZUL_ESCURO);
            pG.setLayout(new BorderLayout());
            pG.add(new JLabel("Desempenho Individual", SwingConstants.CENTER) {{ 
                setForeground(Color.WHITE); setFont(new Font("Arial", Font.BOLD, 18)); 
            }}, BorderLayout.NORTH);
            pG.add(new GraficoPizza(stats[0], stats[1]), BorderLayout.CENTER);
            pInfo.add(pG);

            JButton bPdf = new JButton("Gerar PDF do Aluno");
            bPdf.setBounds(500, 130, 250, 50);
            bPdf.addActionListener(e -> exportarParaPDF(aluno, "Relatorio_" + aluno.getNome()));
            pInfo.add(bPdf);

            add(pInfo, BorderLayout.CENTER);

            h.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    exp = !exp;
                    pInfo.setVisible(exp);
                    s.setText(exp ? "v  " : "<  ");
                    revalidate();
                }
            });
        }
    }

    private class GraficoPizza extends JPanel {
        private double acertos, erros;
        public GraficoPizza(double a, double e) { 
            this.acertos = a; 
            this.erros = e; 
            setOpaque(false); 
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight()) - 150;
            int x = (getWidth() - size) / 2 - 50; // Deslocado para dar espaço à legenda
            int y = (getHeight() - size) / 2;

            int angA = (int) Math.round(acertos * 3.6);
            int angE = 360 - angA;

            // Gradiente para o gráfico
            RadialGradientPaint gAcerto = new RadialGradientPaint(
                new Point(x + size/2, y + size/2), size,
                new float[]{0.0f, 1.0f},
                new Color[]{AZUL_CLARO.brighter(), AZUL_CLARO}
            );
            
            // Desenhar fatias
            g2.setPaint(gAcerto);
            g2.fillArc(x, y, size, size, 90, -angA);
            
            g2.setColor(AZUL_MEDIO);
            g2.fillArc(x, y, size, size, 90 - angA, -angE);

            // Borda branca entre fatias
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x, y, size, size);
            
            // Linha divisória
            double rad = Math.toRadians(90 - angA);
            g2.drawLine(x + size/2, y + size/2, 
                        (int)(x + size/2 + (size/2) * Math.cos(rad)), 
                        (int)(y + size/2 - (size/2) * Math.sin(rad)));

            // Legenda Lateral
            int lx = x + size + 40;
            int ly = y + size/2 - 30;

            desenharItemLegenda(g2, lx, ly, AZUL_CLARO, String.format("Acertos: %.1f%%", acertos));
            desenharItemLegenda(g2, lx, ly + 40, AZUL_MEDIO, String.format("Erros: %.1f%%", erros));

            g2.dispose();
        }

        private void desenharItemLegenda(Graphics2D g2, int x, int y, Color cor, String texto) {
            g2.setColor(cor);
            g2.fillRoundRect(x, y, 20, 20, 5, 5);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString(texto, x + 30, y + 15);
        }
    }

    private static List<Aluno> gerarDadosExemplo() {
        List<Aluno> lista = new ArrayList<>();
        Questao qFake = new Questao(); // Questão dummy para validar respostas
        
        for (int i = 1; i <= 5; i++) {
            Aluno a = new Aluno(i, "Aluno " + i, "aluno"+i+"@aluno.cps.sp.gov.br", "123", "3º A", "123456789");
            Partida p = new Partida(a);
            for(int j=0; j<10; j++) {
                Resposta r = new Resposta();
                r.setQuestao(qFake);
                
                Alternativa alt = new Alternativa();
                alt.setECorreta(Math.random() > 0.3);
                r.setAlternativaEscolhida(alt);
                
                p.adicionarResposta(r);
            }
            a.adicionarPartida(p);
            lista.add(a);
        }
        return lista;
    }

    private static class BotaoMenu extends JButton {
        public BotaoMenu(String texto) {
            super(texto);
            setFont(new Font("Arial", Font.BOLD, 26));
            setForeground(Color.WHITE);
            setBackground(new Color(47, 76, 113));
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
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            super.paintComponent(g);
            g2.dispose();
        }
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
        SwingUtilities.invokeLater(() -> new TelaDesempenho().setVisible(true));
    }
}