package user_interface;

import java.awt.Color;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JOptionPane;

public class MainFrame extends javax.swing.JFrame {

    final static String ADEQUACAO_PROFILE_PANEL = "Card with Perfil Adequação";
    final static String MINIMUM_OBAA_PROFILE_PANEL = "Card with OBAA Mínimo Profile";
    final static String IDEAL_OBAA_PROFILE_PANEL = "Card with OBAA Ideal Profile";
    
    ProfileAdequacao profileAdequacao = new ProfileAdequacao();
    ProfileOBAAMinimo profileOBAAMin = new ProfileOBAAMinimo();
    ProfileOBAAIdeal profileOBAAIdeal = new ProfileOBAAIdeal();
    
    public MainFrame() {
        initComponents();
        buttonGroupProfiles.add(jRadioButtonMenuItemProfileAdequacao);
        buttonGroupProfiles.add(jRadioButtonMenuItemProfileOBAAMinimo);
        buttonGroupProfiles.add(jRadioButtonMenuItemProfileOBAAIdeal);
        jRadioButtonMenuItemProfileOBAAMinimo.setEnabled(true);
        jPanelCards.setLayout(new CardLayout());
        jPanelCards.add(profileAdequacao, ADEQUACAO_PROFILE_PANEL);
        jPanelCards.add(profileOBAAMin, MINIMUM_OBAA_PROFILE_PANEL);
        jPanelCards.add(profileOBAAIdeal, IDEAL_OBAA_PROFILE_PANEL);
        jLabelProfileName.setText("Perfil Compatível Mínimo com OBAA (PM-OBAA-CORE)");
        CardLayout cl = (CardLayout)(jPanelCards.getLayout());
        cl.show(jPanelCards, MINIMUM_OBAA_PROFILE_PANEL);
    }
 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupProfiles = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jPanelCards = new javax.swing.JPanel();
        jLabelProfileName = new javax.swing.JLabel();
        jButtonCarregarObjAprendizagem = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemNewFile = new javax.swing.JMenuItem();
        jMenuItemOpenFile = new javax.swing.JMenuItem();
        jMenuItemSaveFile = new javax.swing.JMenuItem();
        jMenuItemSaveFileAs = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemGenerateXMLFile = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jRadioButtonMenuItemProfileAdequacao = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemProfileOBAAMinimo = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemProfileOBAAIdeal = new javax.swing.JRadioButtonMenuItem();
        jMenu3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ferramenta de Autoria de Metadados de Objetos de Aprendizagem");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Current Metadata Profile:");

        javax.swing.GroupLayout jPanelCardsLayout = new javax.swing.GroupLayout(jPanelCards);
        jPanelCards.setLayout(jPanelCardsLayout);
        jPanelCardsLayout.setHorizontalGroup(
            jPanelCardsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelCardsLayout.setVerticalGroup(
            jPanelCardsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 653, Short.MAX_VALUE)
        );

        jLabelProfileName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelProfileName.setText("profile_name");

        jButtonCarregarObjAprendizagem.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonCarregarObjAprendizagem.setText("Carregar Obj. de Aprendizagem");
        jButtonCarregarObjAprendizagem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCarregarObjAprendizagemActionPerformed(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/help.png"))); // NOI18N
        jLabel3.setToolTipText("<html>O programa processará o objeto de aprendizagem.<br>\nPara gerar sugestões para os campos de metadados, clique no<br>\nbotão Go! ao lado do campo ou clique em Go All! para carregar<br>\ntodas as sugestões possíveis.<br>\nO objeto deve estar nos formatos X, Y ou Z.<br>\n</html>");

        jMenu1.setText("File");

        jMenuItemNewFile.setText("New File");
        jMenu1.add(jMenuItemNewFile);

        jMenuItemOpenFile.setText("Open FIle");
        jMenu1.add(jMenuItemOpenFile);

        jMenuItemSaveFile.setText("Save File");
        jMenu1.add(jMenuItemSaveFile);

        jMenuItemSaveFileAs.setText("Save File As");
        jMenu1.add(jMenuItemSaveFileAs);
        jMenu1.add(jSeparator1);

        jMenuItemGenerateXMLFile.setText("Generate XML File");
        jMenu1.add(jMenuItemGenerateXMLFile);
        jMenu1.add(jSeparator2);

        jMenuItemExit.setText("Exit");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemExit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Profiles");

        jRadioButtonMenuItemProfileAdequacao.setText("Perfil Adequação");
        jRadioButtonMenuItemProfileAdequacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemProfileAdequacaoActionPerformed(evt);
            }
        });
        jMenu2.add(jRadioButtonMenuItemProfileAdequacao);

        jRadioButtonMenuItemProfileOBAAMinimo.setSelected(true);
        jRadioButtonMenuItemProfileOBAAMinimo.setText("OBAA Mínimo");
        jRadioButtonMenuItemProfileOBAAMinimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemProfileOBAAMinimoActionPerformed(evt);
            }
        });
        jMenu2.add(jRadioButtonMenuItemProfileOBAAMinimo);

        jRadioButtonMenuItemProfileOBAAIdeal.setText("OBAA Ideal");
        jRadioButtonMenuItemProfileOBAAIdeal.setToolTipText("");
        jRadioButtonMenuItemProfileOBAAIdeal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemProfileOBAAIdealActionPerformed(evt);
            }
        });
        jMenu2.add(jRadioButtonMenuItemProfileOBAAIdeal);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("About");
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelCards, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonCarregarObjAprendizagem, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addGap(51, 51, 51)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelProfileName)
                        .addGap(0, 451, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonCarregarObjAprendizagem, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(jLabelProfileName)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-1167)/2, (screenSize.height-815)/2, 1167, 815);
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButtonMenuItemProfileAdequacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemProfileAdequacaoActionPerformed
        CardLayout cl = (CardLayout)(jPanelCards.getLayout());
        cl.show(jPanelCards, ADEQUACAO_PROFILE_PANEL);
        jLabelProfileName.setText("Perfil Adequação (PM-ADEQ-BASE)");
    }//GEN-LAST:event_jRadioButtonMenuItemProfileAdequacaoActionPerformed

    private void jRadioButtonMenuItemProfileOBAAMinimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemProfileOBAAMinimoActionPerformed
        CardLayout cl = (CardLayout)(jPanelCards.getLayout());
        cl.show(jPanelCards, MINIMUM_OBAA_PROFILE_PANEL);
        jLabelProfileName.setText("Perfil Compatível Mínimo com OBAA (PM-OBAA-CORE)");
    }//GEN-LAST:event_jRadioButtonMenuItemProfileOBAAMinimoActionPerformed

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    private void jRadioButtonMenuItemProfileOBAAIdealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemProfileOBAAIdealActionPerformed
        CardLayout cl = (CardLayout)(jPanelCards.getLayout());
        cl.show(jPanelCards, IDEAL_OBAA_PROFILE_PANEL);
        jLabelProfileName.setText("Perfil Compatível Ideal com OBAA (PM-OBAA-FULL)");
    }//GEN-LAST:event_jRadioButtonMenuItemProfileOBAAIdealActionPerformed

    private void jButtonCarregarObjAprendizagemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCarregarObjAprendizagemActionPerformed
        JOptionPane.showMessageDialog(this, "Essa função ainda não está implementada.");
    }//GEN-LAST:event_jButtonCarregarObjAprendizagemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupProfiles;
    private javax.swing.JButton jButtonCarregarObjAprendizagem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelProfileName;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemGenerateXMLFile;
    private javax.swing.JMenuItem jMenuItemNewFile;
    private javax.swing.JMenuItem jMenuItemOpenFile;
    private javax.swing.JMenuItem jMenuItemSaveFile;
    private javax.swing.JMenuItem jMenuItemSaveFileAs;
    private javax.swing.JPanel jPanelCards;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemProfileAdequacao;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemProfileOBAAIdeal;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemProfileOBAAMinimo;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    // End of variables declaration//GEN-END:variables
}
