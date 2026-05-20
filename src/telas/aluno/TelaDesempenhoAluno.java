package telas.aluno;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

import model.Alternativa;
import model.Aluno;
import model.Partida;
import model.Questao;
import model.Resposta;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

public class TelaDesempenhoAluno extends JFrame {

    private Aluno aluno;

    private final Color AZUL_ESCURO = new Color(34, 62, 107);
    private final Color AZUL_CLARO = new Color(160, 205, 245);
    private final Color AZUL_MEDIO = new Color(70, 130, 230);
    private final Color BRANCO_GELO = new Color(238, 242, 248);

    private static final PDFont FONTE_NORMAL =
            new PDType1Font(Standard14Fonts.FontName.HELVETICA);

    private static final PDFont FONTE_NEGRITO =
            new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

    public TelaDesempenhoAluno(Aluno aluno) {
        this.aluno = aluno;
        configurarJanela();
        montarTela();
    }

    public TelaDesempenhoAluno() {
        this(gerarAlunoExemplo());
    }

    private void configurarJanela() {
        setTitle("Tela - Desempenho do Aluno");
        setSize(720, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void montarTela() {
        PainelFundo painel = new PainelFundo();
        painel.setLayout(null);
        setContentPane(painel);

        adicionarIcones(painel);

        Estatisticas estatisticas = calcularEstatisticasAluno(aluno);

        PainelArredondado painelGrafico = new PainelArredondado(18);
        painelGrafico.setBounds(14, 85, 355, 385);
        painelGrafico.setBackground(AZUL_ESCURO);
        painelGrafico.setLayout(null);
        painel.add(painelGrafico);

        JLabel titulo = new JLabel("Desempenho do mês", SwingConstants.CENTER);
        titulo.setBounds(0, 14, 355, 35);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        painelGrafico.add(titulo);

        GraficoPizza grafico = new GraficoPizza(
                estatisticas.percentualAcertos,
                estatisticas.percentualErros
        );
        grafico.setBounds(20, 55, 315, 265);
        painelGrafico.add(grafico);

        JLabel resumo = new JLabel(
                estatisticas.totalAcertos + " acertos | "
                        + estatisticas.totalErros + " erros | "
                        + estatisticas.pontuacaoTotal + " pontos",
                SwingConstants.CENTER
        );
        resumo.setBounds(0, 325, 355, 28);
        resumo.setFont(new Font("Arial", Font.BOLD, 13));
        resumo.setForeground(Color.WHITE);
        painelGrafico.add(resumo);

        PainelArredondado painelRelatorio = new PainelArredondado(18);
        painelRelatorio.setBounds(382, 250, 315, 105);
        painelRelatorio.setBackground(AZUL_ESCURO);
        painelRelatorio.setLayout(null);
        painel.add(painelRelatorio);

        JLabel textoRelatorio = new JLabel("Gerar relatório", SwingConstants.CENTER);
        textoRelatorio.setBounds(0, 8, 315, 34);
        textoRelatorio.setFont(new Font("Arial", Font.BOLD, 19));
        textoRelatorio.setForeground(Color.WHITE);
        painelRelatorio.add(textoRelatorio);

        JButton botaoPdf = new JButton("Baixar PDF");
        botaoPdf.setBounds(14, 50, 287, 40);
        botaoPdf.setFont(new Font("Arial", Font.BOLD, 17));
        botaoPdf.setForeground(AZUL_ESCURO);
        botaoPdf.setBackground(BRANCO_GELO);
        botaoPdf.setFocusPainted(false);
        botaoPdf.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoPdf.addActionListener(evento -> gerarPdfDoAluno());
        painelRelatorio.add(botaoPdf);
    }

    private void adicionarIcones(JPanel painel) {
        JLabel iconePerfil = new JLabel("●", SwingConstants.CENTER);
        iconePerfil.setBounds(16, 14, 48, 48);
        iconePerfil.setFont(new Font("Arial", Font.BOLD, 28));
        iconePerfil.setForeground(Color.WHITE);
        iconePerfil.setOpaque(true);
        iconePerfil.setBackground(AZUL_ESCURO);
        painel.add(iconePerfil);

        JLabel iconeSair = new JLabel("↪", SwingConstants.CENTER);
        iconeSair.setBounds(654, 14, 48, 48);
        iconeSair.setFont(new Font("Arial", Font.BOLD, 30));
        iconeSair.setForeground(Color.WHITE);
        iconeSair.setOpaque(true);
        iconeSair.setBackground(AZUL_ESCURO);
        iconeSair.setCursor(new Cursor(Cursor.HAND_CURSOR));

        iconeSair.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evento) {
                dispose();
            new TelaMenuAluno().setVisible(true);
            }
        });

        painel.add(iconeSair);
    }

    private Estatisticas calcularEstatisticasAluno(Aluno aluno) {
        Estatisticas estatisticas = new Estatisticas();

        if (aluno == null || aluno.getHistoricoPartidas() == null) {
            return estatisticas;
        }

        for (Partida partida : aluno.getHistoricoPartidas()) {
            if (partida == null) {
                continue;
            }

            int acertos = partida.getTotalAcertos();
            int erros = partida.getTotalErros();
            int totalRespostas = acertos + erros;

            if (totalRespostas == 0 && partida.getRespostas() != null) {
                totalRespostas = partida.getRespostas().size();
                erros = Math.max(0, totalRespostas - acertos);
            }

            estatisticas.totalAcertos += acertos;
            estatisticas.totalErros += erros;
            estatisticas.totalRespostas += totalRespostas;
            estatisticas.pontuacaoTotal += partida.getPontuacao();
            estatisticas.totalPartidas++;
        }

        estatisticas.calcularPercentuais();
        return estatisticas;
    }

    private void gerarPdfDoAluno() {
        File arquivo = null;

        try {
            String nomeAluno = aluno != null ? aluno.getNome() : "Aluno";
            String nomeArquivo = "Relatorio_" + limparNomeArquivo(nomeAluno) + ".pdf";

            arquivo = new File(nomeArquivo);

            try (PDDocument documento = new PDDocument()) {
                PDPage pagina = new PDPage(PDRectangle.A4);
                documento.addPage(pagina);

                try (PDPageContentStream conteudo = new PDPageContentStream(documento, pagina)) {
                    desenharRelatorioAluno(conteudo);
                }

                documento.save(arquivo);
            }

            JOptionPane.showMessageDialog(
                    this,
                    "PDF gerado com sucesso!\n\nArquivo salvo em:\n" + arquivo.getAbsolutePath(),
                    "PDF gerado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(arquivo);
                }
            } catch (IOException erroAbertura) {
                System.out.println("PDF gerado, mas não foi possível abrir automaticamente.");
            }

        } catch (Exception erro) {
            erro.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao gerar PDF:\n" + erro.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void desenharRelatorioAluno(PDPageContentStream conteudo) throws IOException {
        Estatisticas estatisticas = calcularEstatisticasAluno(aluno);

        desenharCabecalhoPdf(conteudo, "LabQuest - Relatorio individual do aluno");

        escreverTexto(conteudo, "Dados do aluno", FONTE_NEGRITO, 18, 50, 730, AZUL_ESCURO);
        escreverTexto(conteudo, "Nome: " + aluno.getNome(), FONTE_NORMAL, 12, 50, 705, Color.BLACK);
        escreverTexto(conteudo, "Turma: " + aluno.getTurma(), FONTE_NORMAL, 12, 50, 687, Color.BLACK);
        escreverTexto(conteudo, "E-mail: " + aluno.getEmail(), FONTE_NORMAL, 12, 50, 669, Color.BLACK);

        escreverTexto(conteudo, "Resumo de desempenho", FONTE_NEGRITO, 15, 50, 630, AZUL_ESCURO);
        escreverTexto(conteudo, "Partidas: " + estatisticas.totalPartidas, FONTE_NORMAL, 12, 50, 608, Color.BLACK);
        escreverTexto(conteudo, "Pontuacao total: " + estatisticas.pontuacaoTotal, FONTE_NORMAL, 12, 50, 590, Color.BLACK);
        escreverTexto(conteudo, "Respostas: " + estatisticas.totalRespostas, FONTE_NORMAL, 12, 50, 572, Color.BLACK);
        escreverTexto(conteudo, "Acertos: " + estatisticas.totalAcertos + " (" + formatarPercentual(estatisticas.percentualAcertos) + ")", FONTE_NORMAL, 12, 50, 554, Color.BLACK);
        escreverTexto(conteudo, "Erros: " + estatisticas.totalErros + " (" + formatarPercentual(estatisticas.percentualErros) + ")", FONTE_NORMAL, 12, 50, 536, Color.BLACK);

        desenharBarraDesempenhoPdf(
                conteudo,
                50,
                492,
                480,
                28,
                estatisticas.percentualAcertos,
                estatisticas.percentualErros
        );

        escreverTexto(conteudo, "Historico de partidas", FONTE_NEGRITO, 15, 50, 445, AZUL_ESCURO);

        float y = 420;

        desenharLinhaTabela(conteudo, y, true);
        escreverTexto(conteudo, "Data/Hora", FONTE_NEGRITO, 10, 55, y + 8, Color.WHITE);
        escreverTexto(conteudo, "Pontuacao", FONTE_NEGRITO, 10, 200, y + 8, Color.WHITE);
        escreverTexto(conteudo, "Acertos", FONTE_NEGRITO, 10, 300, y + 8, Color.WHITE);
        escreverTexto(conteudo, "Erros", FONTE_NEGRITO, 10, 390, y + 8, Color.WHITE);
        escreverTexto(conteudo, "Total", FONTE_NEGRITO, 10, 470, y + 8, Color.WHITE);

        y -= 24;

        if (aluno.getHistoricoPartidas() != null) {
            DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (Partida partida : aluno.getHistoricoPartidas()) {
                if (y < 70) {
                    break;
                }

                int acertos = partida.getTotalAcertos();
                int erros = partida.getTotalErros();
                int total = acertos + erros;

                if (total == 0 && partida.getRespostas() != null) {
                    total = partida.getRespostas().size();
                    erros = Math.max(0, total - acertos);
                }

                String data = "-";

                if (partida.getDataHoraInicio() != null) {
                    data = partida.getDataHoraInicio().format(formatoData);
                }

                desenharLinhaTabela(conteudo, y, false);
                escreverTexto(conteudo, data, FONTE_NORMAL, 9, 55, y + 8, Color.BLACK);
                escreverTexto(conteudo, String.valueOf(partida.getPontuacao()), FONTE_NORMAL, 9, 200, y + 8, Color.BLACK);
                escreverTexto(conteudo, String.valueOf(acertos), FONTE_NORMAL, 9, 300, y + 8, Color.BLACK);
                escreverTexto(conteudo, String.valueOf(erros), FONTE_NORMAL, 9, 390, y + 8, Color.BLACK);
                escreverTexto(conteudo, String.valueOf(total), FONTE_NORMAL, 9, 470, y + 8, Color.BLACK);

                y -= 24;
            }
        }
    }

    private void definirCorPreenchimento(PDPageContentStream conteudo, Color cor) throws IOException {
        conteudo.setNonStrokingColor(
                cor.getRed() / 255f,
                cor.getGreen() / 255f,
                cor.getBlue() / 255f
        );
    }

    private void definirCorContorno(PDPageContentStream conteudo, Color cor) throws IOException {
        conteudo.setStrokingColor(
                cor.getRed() / 255f,
                cor.getGreen() / 255f,
                cor.getBlue() / 255f
        );
    }

    private void desenharCabecalhoPdf(PDPageContentStream conteudo, String titulo) throws IOException {
        definirCorPreenchimento(conteudo, AZUL_ESCURO);

        conteudo.addRect(0, 790, 595, 52);
        conteudo.fill();

        escreverTexto(conteudo, titulo, FONTE_NEGRITO, 18, 40, 810, Color.WHITE);

        escreverTexto(
                conteudo,
                "Gerado automaticamente pelo sistema LabQuest",
                FONTE_NORMAL,
                10,
                40,
                795,
                Color.WHITE
        );
    }

    private void desenharBarraDesempenhoPdf(
            PDPageContentStream conteudo,
            float x,
            float y,
            float largura,
            float altura,
            double percentualAcertos,
            double percentualErros
    ) throws IOException {
        float larguraAcertos = (float) (largura * percentualAcertos / 100.0);
        float larguraErros = largura - larguraAcertos;

        definirCorPreenchimento(conteudo, AZUL_CLARO);
        conteudo.addRect(x, y, larguraAcertos, altura);
        conteudo.fill();

        definirCorPreenchimento(conteudo, AZUL_MEDIO);
        conteudo.addRect(x + larguraAcertos, y, larguraErros, altura);
        conteudo.fill();

        definirCorContorno(conteudo, AZUL_ESCURO);
        conteudo.addRect(x, y, largura, altura);
        conteudo.stroke();

        escreverTexto(
                conteudo,
                "Acertos: " + formatarPercentual(percentualAcertos),
                FONTE_NEGRITO,
                10,
                x,
                y - 18,
                Color.BLACK
        );

        escreverTexto(
                conteudo,
                "Erros: " + formatarPercentual(percentualErros),
                FONTE_NEGRITO,
                10,
                x + 180,
                y - 18,
                Color.BLACK
        );
    }

    private void desenharLinhaTabela(PDPageContentStream conteudo, float y, boolean cabecalho) throws IOException {
        if (cabecalho) {
            definirCorPreenchimento(conteudo, AZUL_ESCURO);
        } else {
            definirCorPreenchimento(conteudo, new Color(235, 235, 235));
        }

        conteudo.addRect(50, y, 500, 22);
        conteudo.fill();
    }

    private void escreverTexto(
            PDPageContentStream conteudo,
            String texto,
            PDFont fonte,
            float tamanho,
            float x,
            float y,
            Color cor
    ) throws IOException {
        conteudo.beginText();
        conteudo.setFont(fonte, tamanho);

        definirCorPreenchimento(conteudo, cor);

        conteudo.newLineAtOffset(x, y);
        conteudo.showText(prepararTextoPdf(texto));
        conteudo.endText();
    }

    private String prepararTextoPdf(String texto) {
        if (texto == null) {
            return "";
        }

        String textoTratado = texto
                .replace("\n", " ")
                .replace("\r", " ")
                .replace("“", "\"")
                .replace("”", "\"")
                .replace("–", "-")
                .replace("—", "-");

        String semAcentos = Normalizer.normalize(textoTratado, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        return semAcentos.replaceAll("[^\\x20-\\x7E]", "");
    }

    private String formatarPercentual(double valor) {
        return String.format("%.1f%%", valor);
    }

    private String limparNomeArquivo(String texto) {
        if (texto == null || texto.isBlank()) {
            return "aluno";
        }

        String semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        return semAcento.replaceAll("[^a-zA-Z0-9_-]", "_");
    }

    private class GraficoPizza extends JPanel {

        private final double percentualAcertos;
        private final double percentualErros;

        public GraficoPizza(double percentualAcertos, double percentualErros) {
            this.percentualAcertos = percentualAcertos;
            this.percentualErros = percentualErros;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics grafico) {
            super.paintComponent(grafico);

            Graphics2D desenho = (Graphics2D) grafico.create();
            desenho.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int tamanho = Math.min(getWidth(), getHeight()) - 80;

            if (tamanho < 120) {
                tamanho = 120;
            }

            int x = (getWidth() - tamanho) / 2;
            int y = (getHeight() - tamanho) / 2;

            int anguloAcertos = (int) Math.round(percentualAcertos * 3.6);
            int anguloErros = 360 - anguloAcertos;

            desenho.setColor(AZUL_CLARO);
            desenho.fillArc(x, y, tamanho, tamanho, 90, -anguloAcertos);

            desenho.setColor(AZUL_MEDIO);
            desenho.fillArc(x, y, tamanho, tamanho, 90 - anguloAcertos, -anguloErros);

            desenho.setColor(Color.WHITE);
            desenho.setFont(new Font("Arial", Font.BOLD, 11));

            desenharTextoDoGrafico(
                    desenho,
                    x,
                    y,
                    tamanho,
                    90 - (anguloAcertos / 2),
                    "Acertou",
                    formatarPercentual(percentualAcertos)
            );

            desenharTextoDoGrafico(
                    desenho,
                    x,
                    y,
                    tamanho,
                    90 - anguloAcertos - (anguloErros / 2),
                    "Errou",
                    formatarPercentual(percentualErros)
            );

            desenho.dispose();
        }

        private void desenharTextoDoGrafico(
                Graphics2D desenho,
                int x,
                int y,
                int tamanho,
                int angulo,
                String linha1,
                String linha2
        ) {
            double radiano = Math.toRadians(angulo);
            int raio = tamanho / 2 + 38;

            int posicaoX = (int) (x + tamanho / 2 + raio * Math.cos(radiano));
            int posicaoY = (int) (y + tamanho / 2 - raio * Math.sin(radiano));

            FontMetrics medidas = desenho.getFontMetrics();

            int largura1 = medidas.stringWidth(linha1);
            int largura2 = medidas.stringWidth(linha2);

            desenho.drawString(linha1, posicaoX - largura1 / 2, posicaoY);
            desenho.drawString(linha2, posicaoX - largura2 / 2, posicaoY + medidas.getHeight());
        }
    }

    private static class Estatisticas {
        int totalAcertos;
        int totalErros;
        int totalRespostas;
        int pontuacaoTotal;
        int totalPartidas;
        double percentualAcertos;
        double percentualErros;

        void calcularPercentuais() {
            if (totalRespostas <= 0) {
                percentualAcertos = 0;
                percentualErros = 0;
                return;
            }

            percentualAcertos = (totalAcertos * 100.0) / totalRespostas;
            percentualErros = 100.0 - percentualAcertos;
        }
    }

    private static class PainelArredondado extends JPanel {

        private final int raio;

        public PainelArredondado(int raio) {
            this.raio = raio;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics grafico) {
            Graphics2D desenho = (Graphics2D) grafico.create();
            desenho.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            desenho.setColor(getBackground());
            desenho.fillRoundRect(0, 0, getWidth(), getHeight(), raio, raio);

            desenho.dispose();
        }
    }

    private static class PainelFundo extends JPanel {

        private Image imagemFundo;

        public PainelFundo() {
            imagemFundo = new ImageIcon("imagens/menu.png").getImage();
        }

        @Override
        protected void paintComponent(Graphics grafico) {
            super.paintComponent(grafico);

            if (imagemFundo != null && imagemFundo.getWidth(null) > 0) {
                double escala = Math.max(
                        (double) getWidth() / imagemFundo.getWidth(null),
                        (double) getHeight() / imagemFundo.getHeight(null)
                );

                int novaLargura = (int) (imagemFundo.getWidth(null) * escala);
                int novaAltura = (int) (imagemFundo.getHeight(null) * escala);

                grafico.drawImage(
                        imagemFundo,
                        (getWidth() - novaLargura) / 2,
                        (getHeight() - novaAltura) / 2,
                        novaLargura,
                        novaAltura,
                        null
                );
            } else {
                grafico.setColor(new Color(223, 239, 252));
                grafico.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    private static Aluno gerarAlunoExemplo() {
        Aluno aluno = new Aluno(
                1,
                "Aluno 1",
                "aluno1@aluno.cps.sp.gov.br",
                "123",
                "0001"
        );

        Questao questaoTeste = new Questao();
        Partida partida = new Partida(aluno);

        for (int i = 0; i < 10; i++) {
            Resposta resposta = new Resposta();
            resposta.setQuestao(questaoTeste);

            Alternativa alternativa = new Alternativa();
            alternativa.setECorreta(i < 7);

            resposta.setAlternativaEscolhida(alternativa);
            partida.adicionarResposta(resposta);
        }

        aluno.adicionarPartida(partida);

        return aluno;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaDesempenhoAluno tela = new TelaDesempenhoAluno();
            tela.setVisible(true);
        });
    }
}