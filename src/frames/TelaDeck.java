/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import flashcards.Card;
import flashcards.Deck;
import flashcards.Estudo;
import java.awt.Color;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;


/**
 *
 * @author Victhor
 */
public class TelaDeck extends javax.swing.JFrame {  
    private static String nomeDeck = "";
    private final Deck deck;
    private boolean tfClicked = false;
    private boolean tfClicked2 = false;
    private final DefaultListModel mod = new DefaultListModel();

    private Estudo estudo;  // controla o comportamento do painelEstudo
    private boolean cardFrente = true;    // para alternar entre verso e frente do card
    private javax.swing.Timer timer;    // timer para causar movimento do card na animacao
    
    private int idxAtual = 0;
  
    /**
     * Creates new form TelaDeck
     * @param deck
     */
    public TelaDeck(Deck deck) {
        nomeDeck = deck.getNome();
        this.deck = deck;
        
        initComponents();
        
        txtFrente.setForeground(java.awt.Color.GRAY);
        txtFrente.setText("Adicionar frente do card (significante)");
        txtVerso.setForeground(java.awt.Color.GRAY);
        txtVerso.setText("Adicionar verso do card (significado)");
        
        // Desabilitar botões e labels
        btnEstudar.setEnabled(false);
        setEnabledBtnLbl(false);
        
        // habilitar ou desabilitar botões se um item estiver selecionado ou nao
        lstCards.addListSelectionListener((ListSelectionEvent e) -> {
            if (lstCards.getSelectedValue() != null) {
                setEnabledBtnLbl(true);
                int index = lstCards.getSelectionModel().getMaxSelectionIndex();
                setAcertoErroLabels(index);
            } else {
                setEnabledBtnLbl(false);
            }
        });
        
        // adiciona cards na lista caso deck ja contenha card(s) ao abrir a telaDeck
        lstCards.setModel(mod);
        lstCards.setFixedCellHeight(30);
        if (deck.getCards().size() > 0) {
            Card[] cards = deck.getCards().toArray(new Card[0]);
            for (Card c : cards) {
                mod.addElement(c.getFrente()); // Só a frente vai aparecer na lista.
                btnEstudar.setEnabled(true);
            }
        }
    }
    
    // retorna o deck para atualizá-lo na lista de decks da telaInicial
    public Deck getDeck() {
        return deck;
    }
    
    // Inicia as variaveis do painelEstudar
    private void initEstudo(Deck deck) {
        // Iniciar novo estudo
        this.estudo = new Estudo(deck);
        
        panelScore.setVisible(false);       // painel de pontuacao invisivel
        atualizaCard();
        mostraFrente();
        lblTitulo.setText("FRENTE");
        if (deck.getCards().size() == 1) {
            panelTras.setVisible(false);
        }
        
        java.awt.Point panelFrentePosition = panelFrente.getLocation();
        panelTras.setLocation(panelFrentePosition);
        panelTras.setPreferredSize(panelFrente.getSize());

        
        // Inicia o listener do timer
        timer = new javax.swing.Timer(1, (java.awt.event.ActionEvent e) -> {
            java.awt.Point p = panelFrente.getLocation();
            if (p.getX() > - panelFrente.getWidth()) {
                p.setLocation(p.getX()-10, p.getY());
                panelFrente.setLocation(p);
                
            } else {
                if (deck.getCards().size() - idxAtual == 1)
                    panelTras.setVisible(false);    // nao mostrar o próximo card (panelTras) caso ele nao exista
                else if (deck.getCards().size() - idxAtual == 0) {
                    // mostrar painel de pontuacao
                    panelEstudo.setVisible(false);
                    panelScore.setVisible(true);
                    float percentage = (float) estudo.getNumAcertos()/(float)(deck.getCards().size())*100;
                    lblScore.setText("" + String.format("%.01f", percentage) + "%");
                }
                // colocar o painel da frente no centro novamente
                panelFrente.setLocation(panelFrentePosition);
                timer.stop();   // finalizar animacao
            }
        });
    }
    
    private void setEnabledBtnLbl(boolean b) {
        btnModificar.setEnabled(b);
        btnExcluir.setEnabled(b);
        lblData.setEnabled(b);
        lblMostraData.setEnabled(b);
        lblAcertos.setEnabled(b);
        lblNumAcertos.setEnabled(b);
        lblNumErros.setEnabled(b);
        lblErros.setEnabled(b);
        lblAproveitamento.setEnabled(b);
        lblPorcentagem.setEnabled(b);
    }
    
    // atualiza o numero de acertos e erros do card no historico
    public void setAcertoErroLabels(int index) {
        if (index >= 0) {
            Card card = deck.getCards().get(index);
            lblNumAcertos.setText("" + card.getNumAcertos());
            lblNumErros.setText("" + card.getNumErros());
            String porcentagem = String.format("%.01f", card.aproveitamento()*100);
            lblPorcentagem.setText("" + porcentagem + "%");
        }
    }
    
    // formatar texto do card. Caso ele seja muito grande, ir para nova linha
    // usando <html>, <body> e <br>. Tem que ser com a fonte específica selecionada
    private String formatarTexto(String s, int length) {
        String htmlInit = "<html><body>";
        String htmlFinal = "</html></body>";
        
        String txt = s;
        int limit = 35; // quantidade de caracteres por linha
        if (length > limit) {
            StringBuilder txtHTML = new StringBuilder(htmlInit + s + htmlFinal);
            for (int i = limit + htmlInit.length(); i >= 0; i--) {
                if (txtHTML.charAt(i) == ' ') {
                    txt = txtHTML.insert(i+1, "<br>").toString();
                    txtHTML.delete(0, i+5).toString();
                    break;
                } else if (i == 0) {
                    txt = txtHTML.insert(htmlInit.length()+limit, "<br>").toString();
                    txtHTML.delete(0, htmlInit.length()+limit+4).toString();
                    break;
                }
            }

            txtHTML.delete(txtHTML.length() - htmlFinal.length(), txtHTML.length()).toString();
            formatarTexto(txtHTML.toString(), txtHTML.length());    // recursividade
        }
        return txt;
    }
    
    // pega o próximo card da fila
    private Card atualizaCard() {
        Card proximo = estudo.proximoCard();
        return proximo;
    }
    
    // vira o card
    private void mostraVerso() {
        Card c = estudo.getPrimeiroCard();
        if (c != null) {
            String verso = c.getVerso();
            verso = formatarTexto(verso, verso.length());
            lblCard2.setText(verso);
        }
    }
    
    // exibe a informacao frontal do card no panelFrente
    private void mostraFrente() {
        Card c = estudo.getPrimeiroCard();
        if (c != null) {
            String frente = c.getFrente();
            frente = formatarTexto(frente, frente.length());
            lblCard2.setText(frente);
        }
    }
    
    // Mostra frente no card de trás (card placeholder)
    private void mostraFrente2() {
        Card c = estudo.getPrimeiroCard();
        if (c != null) {
            String frente = c.getFrente();
            frente = formatarTexto(frente, frente.length());
            lblCard1.setText(frente);
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelDeck = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnEstudar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstCards = new javax.swing.JList<>();
        jPanel4 = new javax.swing.JPanel();
        lblData = new javax.swing.JLabel();
        lblAcertos = new javax.swing.JLabel();
        lblErros = new javax.swing.JLabel();
        lblMostraData = new javax.swing.JLabel();
        lblNumAcertos = new javax.swing.JLabel();
        lblNumErros = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblAproveitamento = new javax.swing.JLabel();
        lblPorcentagem = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtFrente = new javax.swing.JTextField();
        txtVerso = new javax.swing.JTextField();
        btnAdicionar = new javax.swing.JButton();
        panelEstudo = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        panelFrente = new javax.swing.JPanel();
        lblCard2 = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        lblVerso = new javax.swing.JLabel();
        panelTras = new javax.swing.JPanel();
        lblCard1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnAcertei = new javax.swing.JButton();
        btnErrei = new javax.swing.JButton();
        panelScore = new javax.swing.JPanel();
        lblScore = new javax.swing.JLabel();
        lblScoreTitulo = new javax.swing.JLabel();
        btnReiniciar = new javax.swing.JButton();
        btnRetornar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tela deck");
        setBackground(new java.awt.Color(255, 255, 255));
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/images/deck.png")).getImage());
        setResizable(false);
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(245, 245, 245));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel1MousePressed(evt);
            }
        });

        btnEstudar.setText("Estudar");
        btnEstudar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstudarActionPerformed(evt);
            }
        });

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(245, 245, 245));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Cards do Deck '" + nomeDeck + "'"));

        lstCards.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstCardsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(lstCards);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel4.setBackground(new java.awt.Color(245, 245, 245));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Histórico"));
        jPanel4.setPreferredSize(new java.awt.Dimension(158, 90));

        lblData.setText("Data de criação:");

        lblAcertos.setText("Número de acertos:");

        lblErros.setText("Número de erros:");

        lblMostraData.setText("DD/MM/AAAA");

        lblNumAcertos.setText("0");

        lblNumErros.setText("0");

        lblAproveitamento.setText("Aproveitamento:");

        lblPorcentagem.setText("0.0%");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lblData)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMostraData, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(lblAproveitamento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPorcentagem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(lblErros)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblNumErros, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(lblAcertos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblNumAcertos, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(12, 12, 12))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblData)
                    .addComponent(lblMostraData))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAcertos, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNumAcertos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblErros, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNumErros))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAproveitamento, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPorcentagem))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(btnEstudar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(105, 105, 105))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEstudar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });

        txtFrente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtFrenteMousePressed(evt);
            }
        });
        txtFrente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFrenteActionPerformed(evt);
            }
        });

        txtVerso.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtVersoFocusGained(evt);
            }
        });
        txtVerso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtVersoMousePressed(evt);
            }
        });
        txtVerso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVersoActionPerformed(evt);
            }
        });

        btnAdicionar.setText("Adicionar Card");
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 52, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtFrente, javax.swing.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
                    .addComponent(txtVerso))
                .addGap(79, 79, 79))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(239, 239, 239)
                .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(txtFrente, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtVerso, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelDeckLayout = new javax.swing.GroupLayout(panelDeck);
        panelDeck.setLayout(panelDeckLayout);
        panelDeckLayout.setHorizontalGroup(
            panelDeckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelDeckLayout.setVerticalGroup(
            panelDeckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDeckLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(panelDeck, "card2");

        jLayeredPane1.setBackground(new java.awt.Color(245, 245, 245));

        panelFrente.setBackground(new java.awt.Color(255, 255, 255));
        panelFrente.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 255), 2, true));

        lblCard2.setBackground(new java.awt.Color(255, 255, 255));
        lblCard2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCard2.setFont(new java.awt.Font("Monospaced", 1, 15)); // NOI18N
        lblCard2.setAlignmentX(0.5F);
        lblCard2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCard2lblCard2MouseClicked(evt);
            }
        });

        lblTitulo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setForeground(new java.awt.Color(156, 154, 154));
        lblTitulo.setAlignmentX(0.5F);

        lblVerso.setBackground(new java.awt.Color(255, 255, 255));
        lblVerso.setOpaque(true);

        javax.swing.GroupLayout panelFrenteLayout = new javax.swing.GroupLayout(panelFrente);
        panelFrente.setLayout(panelFrenteLayout);
        panelFrenteLayout.setHorizontalGroup(
            panelFrenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFrenteLayout.createSequentialGroup()
                .addGroup(panelFrenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFrenteLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblCard2, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE))
                    .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addComponent(lblVerso, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelFrenteLayout.setVerticalGroup(
            panelFrenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFrenteLayout.createSequentialGroup()
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblCard2, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(lblVerso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelTras.setBackground(new java.awt.Color(255, 255, 255));
        panelTras.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 255), 2, true));

        lblCard1.setBackground(new java.awt.Color(255, 255, 255));
        lblCard1.setFont(new java.awt.Font("Monospaced", 1, 15)); // NOI18N
        lblCard1.setAlignmentX(0.5F);
        lblCard1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCard1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelTrasLayout = new javax.swing.GroupLayout(panelTras);
        panelTras.setLayout(panelTrasLayout);
        panelTrasLayout.setHorizontalGroup(
            panelTrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTrasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCard1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        panelTrasLayout.setVerticalGroup(
            panelTrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTrasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCard1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLayeredPane1.setLayer(panelFrente, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(panelTras, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGap(162, 162, 162)
                .addComponent(panelFrente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(125, Short.MAX_VALUE))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap(159, Short.MAX_VALUE)
                    .addComponent(panelTras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(155, Short.MAX_VALUE)))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(panelFrente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(106, Short.MAX_VALUE))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap(58, Short.MAX_VALUE)
                    .addComponent(panelTras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(109, Short.MAX_VALUE)))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        btnAcertei.setText("Acertei");
        btnAcertei.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcerteiActionPerformed(evt);
            }
        });

        btnErrei.setText("Errei");
        btnErrei.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnErreiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(164, 164, 164)
                .addComponent(btnAcertei, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(92, 92, 92)
                .addComponent(btnErrei, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(186, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAcertei, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnErrei, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelEstudoLayout = new javax.swing.GroupLayout(panelEstudo);
        panelEstudo.setLayout(panelEstudoLayout);
        panelEstudoLayout.setHorizontalGroup(
            panelEstudoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelEstudoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelEstudoLayout.createSequentialGroup()
                    .addComponent(jLayeredPane1)
                    .addContainerGap()))
        );
        panelEstudoLayout.setVerticalGroup(
            panelEstudoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEstudoLayout.createSequentialGroup()
                .addGap(0, 460, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panelEstudoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelEstudoLayout.createSequentialGroup()
                    .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 106, Short.MAX_VALUE)))
        );

        getContentPane().add(panelEstudo, "card2");

        panelScore.setBackground(new java.awt.Color(255, 255, 255));

        lblScore.setFont(new java.awt.Font("Calibri Light", 1, 55)); // NOI18N
        lblScore.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblScore.setText("100.0%");
        lblScore.setAlignmentX(0.5F);

        lblScoreTitulo.setFont(new java.awt.Font("Calibri Light", 1, 36)); // NOI18N
        lblScoreTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblScoreTitulo.setText("PONTUAÇÃO");
        lblScoreTitulo.setAlignmentX(0.5F);

        btnReiniciar.setBackground(new java.awt.Color(210, 210, 210));
        btnReiniciar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnReiniciar.setText("REINICIAR");
        btnReiniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReiniciarActionPerformed(evt);
            }
        });

        btnRetornar.setBackground(new java.awt.Color(210, 210, 210));
        btnRetornar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnRetornar.setText("RETORNAR");
        btnRetornar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRetornarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelScoreLayout = new javax.swing.GroupLayout(panelScore);
        panelScore.setLayout(panelScoreLayout);
        panelScoreLayout.setHorizontalGroup(
            panelScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblScore, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblScoreTitulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelScoreLayout.createSequentialGroup()
                .addContainerGap(200, Short.MAX_VALUE)
                .addGroup(panelScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnRetornar, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                    .addComponent(btnReiniciar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(228, 228, 228))
        );
        panelScoreLayout.setVerticalGroup(
            panelScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelScoreLayout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(lblScoreTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(lblScore, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79)
                .addComponent(btnReiniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRetornar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );

        getContentPane().add(panelScore, "card2");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnEstudarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEstudarActionPerformed
        panelDeck.setVisible(false);
        panelEstudo.setVisible(true);
        initEstudo(deck);
    }//GEN-LAST:event_btnEstudarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // indice do ultimo item selecionado
        int index = lstCards.getSelectionModel().getMaxSelectionIndex();
        
        javax.swing.JTextField tfFrente = new javax.swing.JTextField(deck.getCards().get(index).getFrente());
        javax.swing.JTextField tfVerso = new javax.swing.JTextField(deck.getCards().get(index).getVerso());
        Object[] message = {
            "Frente:", tfFrente,
            "Verso:", tfVerso,
        };
        
        // Modificar frente e verso do card
        int option = JOptionPane.showConfirmDialog(null, message, "Modificar Card", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        tfFrente.requestFocus();
        if (option == JOptionPane.OK_OPTION) {
            String newFrente = tfFrente.getText();
            String newVerso = tfVerso.getText();

            if (!newFrente.equals("") && !newVerso.equals("")) {
                mod.remove(index);
                mod.add(index, newFrente);
                lstCards.getSelectionModel().setLeadSelectionIndex(index);
                // atualiza valor no Card do Deck
                deck.getCards().set(index, new Card(newFrente, newVerso));
            } else {
                JOptionPane.showMessageDialog(null, "Um ou mais campos não preenchidos.\nCard não modificado.",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }
        
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir?", "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION){
            int index = lstCards.getSelectionModel().getMaxSelectionIndex();
            mod.remove(index);
            deck.getCards().remove(index);
            if (mod.getSize() == 0) {
                btnEstudar.setEnabled(false);
            }
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFrenteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFrenteMousePressed
        if(!tfClicked){
            tfClicked=true;
            txtFrente.setText("");
            txtFrente.setForeground(java.awt.Color.BLACK);
        }
    }//GEN-LAST:event_txtFrenteMousePressed

    private void txtFrenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFrenteActionPerformed
        // ao clicar no card, virá-lo
        txtFrente.requestFocus();
        tfClicked=true;
        txtFrente.setText("");
        txtFrente.setForeground(java.awt.Color.BLACK);
    }//GEN-LAST:event_txtFrenteActionPerformed

    private void txtVersoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVersoActionPerformed
        btnAdicionar.doClick();
        btnAdicionar.requestFocus();
    }//GEN-LAST:event_txtVersoActionPerformed

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MousePressed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        jPanelMouseClicked(jPanel1);
    }//GEN-LAST:event_jPanel1MouseClicked

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        jPanelMouseClicked(jPanel2);
    }//GEN-LAST:event_jPanel2MouseClicked

    private void txtVersoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtVersoMousePressed

    }//GEN-LAST:event_txtVersoMousePressed

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        // adicionar deque na lista se o usuário digitou algo
        if (!txtFrente.getText().equals("") && txtFrente.getForeground() != Color.GRAY
            && !txtVerso.getText().equals("") && txtVerso.getForeground() != Color.GRAY) {
            
            String frente = txtFrente.getText().trim();
            String verso = txtVerso.getText().trim();
            mod.addElement(frente); // Só a frente vai aparecer na lista.
            
            deck.addCard(new Card(frente, verso));
            btnEstudar.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(null, "Um ou mais campos não preenchidos",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // retornar com texto placeholder
        txtFrente.setForeground(java.awt.Color.GRAY);
        txtFrente.setText("Adicionar frente do card (significante)");
        txtVerso.setForeground(java.awt.Color.GRAY);
        txtVerso.setText("Adicionar verso do card (significado)");
        
        tfClicked = false;
        tfClicked2 = false;
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void lstCardsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstCardsMouseClicked
        int index = lstCards.getSelectionModel().getMaxSelectionIndex();
        setAcertoErroLabels(index);
    }//GEN-LAST:event_lstCardsMouseClicked

    private void txtVersoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtVersoFocusGained
        if(!tfClicked2){
            tfClicked2=true;
            txtVerso.setText("");
            txtVerso.setForeground(java.awt.Color.BLACK);
        }
    }//GEN-LAST:event_txtVersoFocusGained

    private void lblCard2lblCard2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCard2lblCard2MouseClicked
        if (cardFrente) {
            cardFrente = false;
            mostraVerso();
            lblTitulo.setText("VERSO");
            // detalhe no canto do card quando ele estiver mostrando o verso
            lblVerso.setBackground(new java.awt.Color(121,179,255));
        }
        else {
            mostraFrente();
            cardFrente = true;
            lblTitulo.setText("FRENTE");
            lblVerso.setBackground(new java.awt.Color(255,255,255));
        }
    }//GEN-LAST:event_lblCard2lblCard2MouseClicked

    private void btnAcerteiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcerteiActionPerformed
        estudo.acertei(); // atualiza numero de acertos
        apertaBotao();
    }//GEN-LAST:event_btnAcerteiActionPerformed

    // apertou o botao acertei ou o botao errei
    private void apertaBotao() {
        if(!timer.isRunning()) timer.start();   // comecar a animacao
        
        if (atualizaCard() != null) {
            mostraFrente();
            mostraFrente2();
        }
        idxAtual += 1;
    }
    
    private void btnErreiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnErreiActionPerformed
        estudo.errei(); // atualiza numero de erros
        apertaBotao();
    }//GEN-LAST:event_btnErreiActionPerformed

    private void lblCard1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCard1MouseClicked
        if (cardFrente) {
            cardFrente = false;
            mostraVerso();
            lblTitulo.setText("VERSO");
            // detalhe no canto do card quando ele estiver mostrando o verso
            lblVerso.setBackground(new java.awt.Color(121,179,255));
        }
        else {
            mostraFrente();
            cardFrente = true;
            lblTitulo.setText("FRENTE");
            lblVerso.setBackground(new java.awt.Color(255,255,255));
        }
    }//GEN-LAST:event_lblCard1MouseClicked

    private void btnReiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReiniciarActionPerformed
        panelScore.setVisible(false);
        panelEstudo.setVisible(true);
        estudo = estudo.novoEstudo();
        idxAtual = 0;   // reinicia o indice o card atual
        initEstudo(deck);
    }//GEN-LAST:event_btnReiniciarActionPerformed

    private void btnRetornarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRetornarActionPerformed
        panelScore.setVisible(false);
        panelDeck.setVisible(true);
        idxAtual = 0;   // reinicia o indice o card atual
        // atualizar estatisticas
        int index = lstCards.getSelectionModel().getMaxSelectionIndex();
        setAcertoErroLabels(index);
    }//GEN-LAST:event_btnRetornarActionPerformed
    
    private void jPanelMouseClicked(javax.swing.JPanel jPanel) {
        jPanel.requestFocus();
        
        if (txtFrente.getText().isEmpty()) {
            txtFrente.setForeground(java.awt.Color.GRAY);
            txtFrente.setText("Adicionar frente do card (significante)");
            tfClicked = false;
        }
        
        if (txtVerso.getText().isEmpty()) {
            txtVerso.setForeground(java.awt.Color.GRAY);
            txtVerso.setText("Adicionar verso do card (significado)");
            tfClicked2 = false;
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaDeck.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new TelaDeck(null).setVisible(true);
        });
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAcertei;
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnErrei;
    private javax.swing.JButton btnEstudar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnReiniciar;
    private javax.swing.JButton btnRetornar;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblAcertos;
    private javax.swing.JLabel lblAproveitamento;
    private javax.swing.JLabel lblCard1;
    private javax.swing.JLabel lblCard2;
    private javax.swing.JLabel lblData;
    private javax.swing.JLabel lblErros;
    private javax.swing.JLabel lblMostraData;
    private javax.swing.JLabel lblNumAcertos;
    private javax.swing.JLabel lblNumErros;
    private javax.swing.JLabel lblPorcentagem;
    private javax.swing.JLabel lblScore;
    private javax.swing.JLabel lblScoreTitulo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblVerso;
    private javax.swing.JList<String> lstCards;
    private javax.swing.JPanel panelDeck;
    private javax.swing.JPanel panelEstudo;
    private javax.swing.JPanel panelFrente;
    private javax.swing.JPanel panelScore;
    private javax.swing.JPanel panelTras;
    private javax.swing.JTextField txtFrente;
    private javax.swing.JTextField txtVerso;
    // End of variables declaration//GEN-END:variables
}
