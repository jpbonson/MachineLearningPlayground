package user_interface.categorias;

import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import util.TipoDeSugestao;
import user_interface.Main;
import user_interface.Profile;

public class JPanelGeneralParaOBAA extends javax.swing.JPanel implements Categoria{

    Profile profile;
    
    public JPanelGeneralParaOBAA(Profile profile) {
        initComponents();
        this.profile = profile;
        jTableIdentifier.getColumnModel().getColumn(0).setMinWidth(270);
        jTableIdentifier.getColumnModel().getColumn(0).setMaxWidth(270);
        jTableIdentifier.setShowGrid(true);
        jTableIdentifier.setGridColor(new Color(0,0,153));
        jTableIdentifier.setTableHeader(null);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableIdentifier = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jButtonTableIdentifierAdd = new javax.swing.JButton();
        jButtonTableIdentifierDel = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jTextFieldLanguage = new javax.swing.JTextField();
        jComboBoxTitleLang = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextFieldTitle = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaDescription = new javax.swing.JTextArea();
        jComboBoxDescriptionLang = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextFieldKeywords = new javax.swing.JTextField();
        jComboBoxKeywordsLang = new javax.swing.JComboBox();
        jButtonSugestoesParaTitle = new javax.swing.JButton();
        jButtonSugestoesParaDescription = new javax.swing.JButton();
        jButtonSugestoesParaKeywords = new javax.swing.JButton();

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("1. Identifier:");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/help.png"))); // NOI18N
        jLabel8.setToolTipText("<html>A globally unique label that identifies this learning object.</html>");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("1.1. Catalog");

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/help.png"))); // NOI18N
        jLabel13.setToolTipText("<html>The name or designator of the identification or cataloging\nscheme for this entry. A namespace scheme.<br><br>\ne.g: \"ISBN\", \"ARIADNE\", \"URI\"</html>");

        jTableIdentifier.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));
        jTableIdentifier.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTableIdentifier.setModel(new javax.swing.table.DefaultTableModel(1,2));
        jTableIdentifier.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        jTableIdentifier.setRowHeight(24);
        jTableIdentifier.setSelectionBackground(new java.awt.Color(199, 230, 251));
        jTableIdentifier.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jTableIdentifier.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTableIdentifier);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("1.2. Entry");

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/help.png"))); // NOI18N
        jLabel14.setToolTipText("<html>\nThe value of the identifier within the identification<br>\nor cataloging scheme that designates or identifies<br>\nthis learning object. A namespace specific string.<br>\n<br>\ne.g: \"2-7342-0318\", \"LEAO875\",<br>\n\"http://www.ieee.org/documents/1234\"\n</html>");

        jButtonTableIdentifierAdd.setText("+");
        jButtonTableIdentifierAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTableIdentifierAddActionPerformed(evt);
            }
        });

        jButtonTableIdentifierDel.setText("-");
        jButtonTableIdentifierDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTableIdentifierDelActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("2. Title:");

        jComboBoxTitleLang.setEditable(true);
        jComboBoxTitleLang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "en", "pt" }));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/help.png"))); // NOI18N
        jLabel10.setToolTipText("<html>Name given to this learning object and the language that the title is writen.</html>");

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/help.png"))); // NOI18N
        jLabel15.setToolTipText("<html>The primary human language or languages used within this learning<br>\nobject to communicate to the intended user.<br><br>\n\nEx.: en; en-GB; fr-CA; pt-BR; pt-PT<br><br>\n\nCaso haja mais de uma informação separe os valores com ;</html>");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setText("3. Language:");

        jTextFieldTitle.setBackground(new java.awt.Color(204, 255, 204));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("4. Description:");

        jTextAreaDescription.setBackground(new java.awt.Color(204, 255, 204));
        jTextAreaDescription.setColumns(20);
        jTextAreaDescription.setLineWrap(true);
        jTextAreaDescription.setRows(5);
        jTextAreaDescription.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextAreaDescription);

        jComboBoxDescriptionLang.setEditable(true);
        jComboBoxDescriptionLang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "en", "pt" }));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/help.png"))); // NOI18N
        jLabel17.setToolTipText("<html>A textual description of the content of this learning object.<br><br>\n\nEx.: \"In this video clip, the life and works of Leonardo da Vinci are briefly presented. (...)</html>");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("5. Keywords:");

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/help.png"))); // NOI18N
        jLabel18.setToolTipText("<html>A keyword or phrase describing the topic of this learning object.<br><br>\n\nEx.: \"Mona Lisa\"<br><br>\n\nCaso haja mais de uma informação separe os valores com ;</html>");

        jTextFieldKeywords.setBackground(new java.awt.Color(204, 255, 204));

        jComboBoxKeywordsLang.setEditable(true);
        jComboBoxKeywordsLang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "en", "pt" }));

        jButtonSugestoesParaTitle.setBackground(new java.awt.Color(204, 255, 204));
        jButtonSugestoesParaTitle.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonSugestoesParaTitle.setForeground(new java.awt.Color(0, 102, 51));
        jButtonSugestoesParaTitle.setText("Suggest!");
        jButtonSugestoesParaTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSugestoesParaTitleActionPerformed(evt);
            }
        });

        jButtonSugestoesParaDescription.setBackground(new java.awt.Color(204, 255, 204));
        jButtonSugestoesParaDescription.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonSugestoesParaDescription.setForeground(new java.awt.Color(0, 102, 51));
        jButtonSugestoesParaDescription.setText("Suggest!");
        jButtonSugestoesParaDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSugestoesParaDescriptionActionPerformed(evt);
            }
        });

        jButtonSugestoesParaKeywords.setBackground(new java.awt.Color(204, 255, 204));
        jButtonSugestoesParaKeywords.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonSugestoesParaKeywords.setForeground(new java.awt.Color(0, 102, 51));
        jButtonSugestoesParaKeywords.setText("Suggest!");
        jButtonSugestoesParaKeywords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSugestoesParaKeywordsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator4)
                            .addComponent(jSeparator3)
                            .addComponent(jSeparator2)
                            .addComponent(jSeparator1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel8))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jScrollPane2)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel11)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel13)
                                                .addGap(168, 168, 168)
                                                .addComponent(jLabel12)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel14)
                                                .addGap(310, 492, Short.MAX_VALUE)))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButtonTableIdentifierAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonTableIdentifierDel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(214, 214, 214))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jTextFieldKeywords, javax.swing.GroupLayout.PREFERRED_SIZE, 674, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBoxKeywordsLang, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonSugestoesParaKeywords))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel15))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel17))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextFieldTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBoxTitleLang, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButtonSugestoesParaTitle))
                                    .addComponent(jTextFieldLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 678, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jComboBoxDescriptionLang, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButtonSugestoesParaDescription)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel7))
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel11)
                        .addComponent(jLabel13))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel12)
                        .addComponent(jLabel14)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonTableIdentifierAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonTableIdentifierDel)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxTitleLang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSugestoesParaTitle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBoxDescriptionLang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonSugestoesParaDescription))))
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxKeywordsLang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldKeywords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSugestoesParaKeywords))
                .addContainerGap(195, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public PacoteComCampos retornarCamposGeradoresDeSugestoes()
    {
        ArrayList<String> campos_valores = new ArrayList<String>();
        campos_valores.add(jTextFieldTitle.getText());
        campos_valores.add(jTextAreaDescription.getText());
        campos_valores.add(jTextFieldKeywords.getText());
        
        return new PacoteComCampos(util.TipoDeCategoria.GeneralParaOBAA, campos_valores);
    }
    
    private void jButtonTableIdentifierAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTableIdentifierAddActionPerformed
        if(jTableIdentifier.getRowCount() == 10)
        {
            JOptionPane.showMessageDialog(this, "Você não pode ter mais de 10 linhas na tabela!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel)jTableIdentifier.getModel();
        model.addRow(new String[2]);
    }//GEN-LAST:event_jButtonTableIdentifierAddActionPerformed

    private void jButtonTableIdentifierDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTableIdentifierDelActionPerformed
        if(jTableIdentifier.getRowCount() == 1)
        {
            JOptionPane.showMessageDialog(this, "Você não pode ter deletar a última linha de uma tabela!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(jTableIdentifier.getSelectedRow() == -1)
        {
            JOptionPane.showMessageDialog(this, "Você precisa selecionar a linha que deseja deletar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel)jTableIdentifier.getModel();
        model.removeRow(jTableIdentifier.getSelectedRow());
    }//GEN-LAST:event_jButtonTableIdentifierDelActionPerformed

    private void jButtonSugestoesParaTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSugestoesParaTitleActionPerformed
        profile.gerarSugestoes(TipoDeSugestao.SUGESTAO_TITLE);
    }//GEN-LAST:event_jButtonSugestoesParaTitleActionPerformed

    private void jButtonSugestoesParaDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSugestoesParaDescriptionActionPerformed
        profile.gerarSugestoes(TipoDeSugestao.SUGESTAO_DESCRIPTION);
    }//GEN-LAST:event_jButtonSugestoesParaDescriptionActionPerformed

    private void jButtonSugestoesParaKeywordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSugestoesParaKeywordsActionPerformed
        profile.gerarSugestoes(TipoDeSugestao.SUGESTAO_KEYWORDS);
    }//GEN-LAST:event_jButtonSugestoesParaKeywordsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonSugestoesParaDescription;
    private javax.swing.JButton jButtonSugestoesParaKeywords;
    private javax.swing.JButton jButtonSugestoesParaTitle;
    private javax.swing.JButton jButtonTableIdentifierAdd;
    private javax.swing.JButton jButtonTableIdentifierDel;
    private javax.swing.JComboBox jComboBoxDescriptionLang;
    private javax.swing.JComboBox jComboBoxKeywordsLang;
    private javax.swing.JComboBox jComboBoxTitleLang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTable jTableIdentifier;
    private javax.swing.JTextArea jTextAreaDescription;
    private javax.swing.JTextField jTextFieldKeywords;
    private javax.swing.JTextField jTextFieldLanguage;
    private javax.swing.JTextField jTextFieldTitle;
    // End of variables declaration//GEN-END:variables
}
