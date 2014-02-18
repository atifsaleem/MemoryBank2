/*
 * EpisodicTest.java
 *
 * Created on August 29, 2009, 12:21 AM
 */

package falcon;

/**
 *
 * @author  WA0003EN
 */


import java.util.*;
import java.io.*;
import java.lang.*;
import javax.swing.Timer; 
import java.awt.event.*;

public class PlayBackPanel2 extends javax.swing.JFrame {
    protected String seqFile="Player_3_seq_learner.ext";
    protected String eventFile="Player_3_event_learner.ext";
    //protected String filePath="D:\\work\\player3\\";
    protected String filePath="/Users/atifsaleem/NetBeansProjects/Falcon_14_05_10_atif/data/";
    //protected String filePath="C:\\Documents and Settings\\User\\Desktop\\My Dropbox\\wenwen\\em-modified\\";
    //protected String filePath=System.getProperty("user.dir")+"\\";
    protected String eventListFile=filePath+"event_Player_3_100.ext";
    protected String eventListFile_noise=filePath+"Player_3_noise_event1_new_10_percent.ext";
    protected Vector<Event> events;
    protected Vector<Event> pEvents;
    protected Vector<Event> curPSeq;
    protected Vector<Event> curSeq;
    protected EpisodicModelcc episodic;
    protected int eventIndex;
    
    protected ArrayList seqStartIndexes;
    protected ArrayList seqLenIndexes;
    protected ArrayList seqWinIndexes;
    
    protected int numFieldE = 4;
    protected int lenE [] = {6 ,60, 8, 2};
    protected double inputE [][] = new double[numFieldE][];
    
    protected double betaE [] = {(double)0.1, (double)0.1,(double)0.1, (double)0.1};
    protected double alphaE [] = {(double)0.1, (double)0.1,(double)0.1, (double)0.1};
    //protected double gammaE [] = {(double)0.4, (double)0.2,(double)0.2, (double)0.2};
    protected double gammaE [] = {(double)0.1, (double)0.3,(double)0.3, (double)0.3};
    protected double rhoE [] = {(double) 1, (double) 1,(double) 1, (double) 1};
    
    protected double betaS = 0.3;
    protected double alphaS = 0.1;
    protected double gammaS = 1.0;
    protected double rhoS = 1.0;
    
    protected boolean isStart=false;
    
    //*****Attribute added by WWW after 02/12/09****
    protected int partialCueLength=0;
    protected boolean partialCueBackwards=false;
    protected int seq_ptr=-1;
    protected boolean isNewStart=true;
    
     protected int testState = 0;
    static final int playback = 0;
    static final int recall = 1;
    static final int imperfectRecall = 2;
    protected int cueLen = 0;
    protected int cueLoc = 0;
    static final int startCue = 0;
    static final int endCue = 1;
    static final int anyCue = 2;
    protected boolean tstErrors = false;
    protected int errorType = 0; //0 for event; 1 for sequence;

    protected int errorRate = 0; //in percentage
    
    //add by www 19/03/10
     protected  FusionART CISM;
     protected FusionART WeaponSM;
     protected FusionART CRSM;
     int numFieldSM = 2;
     int lenSM [] = {6, 24};
     double inputSM [][] = new double[numFieldSM][];
        
     double betaSM [] = {(double)1, (double)1};
     double alphaSM [] = {(double)0.1, (double)0.1};
     double gammaSM [] = {(double)0.6, (double)0.2};
     double rhoSM [] = {(double) 0.85, (double)1};
     String fileSM="CISM.ext";
     
     int numFieldWeaponSM = 3;
     int lenWeaponSM [] = {20, 2, 2};
     double inputWeaponSM [][] = new double[numFieldWeaponSM][];
     
     double betaWeaponSM [] = {(double)1, (double)0.2, (double) 0.05};
     double alphaWeaponSM [] = {(double)0.1, (double)0.1, (double)0.1};
     double gammaWeaponSM [] = {(double)0.5, (double)0.5, (double) 0};
     double rhoWeaponSM [] = {(double) 1, (double)0.9, (double) 0};
     String fileWeaponSM="WeaponSM.ext";
     
     //int numFieldCRSM = 8;
     //int lenCRSM []={6,60,8,2,6,60,8,2};
     //double inputCRSM [][] = new double[numFieldCRSM][];
     
     //double betaCRSM [] = {(double)0.1, (double)0.1, (double)0.1, (double)0.1, (double)0.1, (double)0.1,(double)0.1, (double)0.1};
     //double alphaCRSM [] = {(double)0.1, (double)0.1,(double)0.1, (double)0.1, (double)0.1, (double)0.1,(double)0.1, (double)0.1};
     //double gammaCRSM [] = {(double)0.3, (double)0.4, (double) 0.2, (double)0.1, (double) 0, (double) 0, (double) 0, (double) 0};
     //double rhoCRSM [] = {(double) 0.5, (double) 0.5,(double) 0.5, (double)0.5,(double) 0, (double) 0, (double) 0, (double) 0};
     //String fileCRSM="CRSM.ext";
     
     int numFieldCRSM = 2;
     int lenCRSM []={76,76};
     double inputCRSM [][] = new double[numFieldCRSM][];
     
     double betaCRSM [] = {(double)0.05, (double)0.05};
     double alphaCRSM [] = {(double)0.1, (double)0.1};
     double gammaCRSM [] = {(double)0.5, (double) 0.5};
     double rhoCRSM [] = {(double) 0.5, (double) 0.5};
     String fileCRSM="CRSM.ext";
     int winCRSM=20;
     
     String allWeapons [] = new String [] {"FLAK_CANNON", "MINIGUN",
            "ROCKET_LAUNCHER", "LINK_GUN", "BIO_RIFLE", "LIGHTNING_GUN",
            "SHOCK_RIFLE", "SNIPER_RIFLE", "ASSAULT_RIFLE", "SHIELD_GUN"};
     
     
     //related to forgetting
     public boolean isForget=true;
     public double forgetRate1=0.00005;
     public double forgetRate2=0.0005;
     public double forgetRate3=0.001;
    
     Vector<Integer> numEvents=new Vector<Integer>(0);
     Vector<Integer> numEpis=new Vector<Integer>(0);

    
    protected Timer timer=new Timer(1,
              new ActionListener() {
                  public void actionPerformed(ActionEvent e) {
                      if(displayMode==DisplayByLine){
                           oneMoreStoryLine();}
                      else if(displayMode==DisplayByWord){
                           oneMoreStoryChar();}
                  }
              });
;
    protected Vector<Vector<String>> curStory=new Vector<Vector<String>>();
    protected Vector<Vector<String>> curPStory=new Vector<Vector<String>>();
    protected String storyDiscription="";
    protected String pStoryDiscription="";
    protected int storyCharIndex=0;
    protected int pStoryCharIndex=0;
    
    protected static final int DisplayByLine=1;
    protected static final int DisplayByWord=2;
    protected int displayMode=DisplayByWord;
    
    protected static final int SEQLENGTH = 30;
    
    
    /** Creates new form EpisodicTest */
    public PlayBackPanel2() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        jComboBox5 = new javax.swing.JComboBox();
        jComboBox6 = new javax.swing.JComboBox();
        jComboBox7 = new javax.swing.JComboBox();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 0, 51));
        setForeground(new java.awt.Color(153, 255, 153));

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setAutoscrolls(true);
        jPanel1.setFont(new java.awt.Font("Berlin Sans FB Demi", 1, 24));
        jPanel1.setMaximumSize(new java.awt.Dimension(10000, 10000));
        jPanel1.setPreferredSize(new java.awt.Dimension(500, 925));

        jLabel1.setBackground(new java.awt.Color(0, 0, 204));
        jLabel1.setFont(new java.awt.Font("Bauhaus 93", 3, 24));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("EPISODIC MEMORY SIMULATOR");

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Bodoni MT", 1, 16));
        jButton1.setText("CREATE EPISODIC MEMORY");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
                StartPressed(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(0, 0, 204));
        jLabel3.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Total Number of Events");

        jLabel4.setBackground(new java.awt.Color(0, 0, 204));
        jLabel4.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Total Number of Episodes");

        jLabel5.setBackground(new java.awt.Color(0, 0, 204));
        jLabel5.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("0");

        jLabel6.setBackground(new java.awt.Color(0, 0, 204));
        jLabel6.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("0");

        jLabel7.setBackground(new java.awt.Color(0, 0, 204));
        jLabel7.setFont(new java.awt.Font("Bodoni MT", 1, 16));
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("MORE ANALYSIS");

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Bodoni MT", 0, 12));
        jTextArea1.setForeground(new java.awt.Color(0, 102, 153));
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Bodoni MT", 0, 12)); // NOI18N
        jTextArea2.setForeground(new java.awt.Color(0, 102, 153));
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jLabel10.setBackground(new java.awt.Color(0, 0, 204));
        jLabel10.setFont(new java.awt.Font("Bodoni MT", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("RETRIEVE ACCURACY");

        jLabel11.setBackground(new java.awt.Color(0, 0, 204));
        jLabel11.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 14));
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("0");

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Bodoni MT", 1, 16)); // NOI18N
        jButton2.setText("PREVIOUS EPISODE");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Bodoni MT", 1, 16));
        jButton3.setLabel("PARAMETERS");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel12.setBackground(new java.awt.Color(0, 0, 204));
        jLabel12.setFont(new java.awt.Font("Bodoni MT", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("EPISODE NAVIGATION");

        jLabel9.setBackground(new java.awt.Color(0, 0, 204));
        jLabel9.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Retrieved Episode");

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setFont(new java.awt.Font("Bodoni MT", 1, 14));
        jButton4.setText("TOGGLE DISPLAY");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChangDispalyMode(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Bodoni MT", 1, 16)); // NOI18N
        jButton5.setText("NEXT EPISODE");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Bodoni MT", 0, 9));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECT AN EPISODE FOR PLAYBACK" }));
        jComboBox1.setEditor(null);
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jComboBox2.setFont(new java.awt.Font("Bodoni MT", 0, 9));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CUE LOCATION" }));
        jComboBox2.setEditor(null);
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel14.setBackground(new java.awt.Color(0, 0, 204));
        jLabel14.setFont(new java.awt.Font("Bodoni MT", 1, 15));
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Play Back Options: ");

        jLabel15.setBackground(new java.awt.Color(0, 0, 204));
        jLabel15.setFont(new java.awt.Font("Bodoni MT", 1, 15));
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Recall Options: ");

        jLabel16.setBackground(new java.awt.Color(0, 0, 204));
        jLabel16.setFont(new java.awt.Font("Bodoni MT", 1, 15));
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Imperfect Recall Options: ");

        jLabel13.setBackground(new java.awt.Color(0, 0, 204));
        jLabel13.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Retrieval Cue");

        jComboBox3.setFont(new java.awt.Font("Bodoni MT", 0, 9));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECT AN EPISODE FOR RECALL" }));
        jComboBox3.setEditor(null);
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jComboBox4.setFont(new java.awt.Font("Bodoni MT", 0, 9));
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECT AN EPISODE FOR RECALL" }));
        jComboBox4.setEditor(null);
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        jComboBox5.setFont(new java.awt.Font("Bodoni MT", 0, 9));
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CUE LENGTH" }));
        jComboBox5.setEditor(null);
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });

        jComboBox6.setFont(new java.awt.Font("Bodoni MT", 0, 9));
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ERROR RATE" }));
        jComboBox6.setEditor(null);
        jComboBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox6ActionPerformed(evt);
            }
        });

        jComboBox7.setFont(new java.awt.Font("Bodoni MT", 0, 9));
        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ERROR TYPE" }));
        jComboBox7.setEditor(null);
        jComboBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox7ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(255, 255, 255));
        jButton6.setFont(new java.awt.Font("Bodoni MT", 1, 10));
        jButton6.setText("START");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ChangDispalyMode(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(255, 255, 255));
        jButton7.setFont(new java.awt.Font("Bodoni MT", 1, 10));
        jButton7.setText("START");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ChangDispalyMode(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(255, 255, 255));
        jButton8.setFont(new java.awt.Font("Bodoni MT", 1, 10));
        jButton8.setText("START");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ChangDispalyMode(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(255, 255, 255));
        jButton9.setFont(new java.awt.Font("Bodoni MT", 1, 16));
        jButton9.setText("TRANSFER TO SM KNOWLEDGE");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
                jButton9StartPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 573, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(11, 11, 11)
                                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(23, 23, 23)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(26, 26, 26)
                                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(42, 42, 42)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton7)
                            .addComponent(jButton8)
                            .addComponent(jButton6)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addComponent(jButton9))
                        .addGap(96, 96, 96)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButton3)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton2))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(21, 21, 21)
                                                .addComponent(jButton5))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel12)
                                                    .addComponent(jLabel10)))))
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton4))
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jButton6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton7)
                            .addGap(8, 8, 8)
                            .addComponent(jButton8))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, 8)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 745, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 925, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jButton1ActionPerformed

//this method has minor changes by www after 02/12/09
private void StartPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartPressed
// TODO add your handling code here:
     EventListLoad();
            //partialLeEventListLoadngthTest(5,0);
            //this.episodic.eventLearner.rho = new double[]{(double) 0.5, (double) 0.5, (double) 0.5, (double) 0.5};
            this.episodic.seqLearner.rho = 0;
            this.episodic.eventLearner.rho =new double[]{0, 0, 0,0};

            jLabel5.setText("" + episodic.seqLearner.numF1);
            jLabel6.setText("" + (episodic.seqLearner.numCode));
            isStart=true;
            
           /* int len = this.cueLen;


    int numTrials = seqStartIndexes.size() - 1;
    int sumSuccess = 0;
    for (int t = 0; t < numTrials; t++) {
        int selidx = ((Integer) seqStartIndexes.get(t)).intValue();
        int epiLen = ((Integer) seqLenIndexes.get(t)).intValue();
        //int curLen = epiLen / this.cueLen;
        int curLen=epiLen/4;
        if (curLen <= 0) {
            curLen = epiLen;
        }
        //int sidx = 0;
        int sidx=(int)(epiLen*0.5);


        System.out.println("index: " + selidx + " fullLen: " + epiLen + " partial: " + curLen + " startPos: " + sidx);
        int winidx = seqRecognition(selidx, curLen, sidx, epiLen);
        if (winidx == ((Integer) seqWinIndexes.get(t)).intValue()) {
            sumSuccess++;
        }
    }
    this.jLabel11.setText((((1.0 * sumSuccess) / (1.0 * numTrials)) * 100) + "%");*/
            
            //this.jLabel11.setText(""+orderedFullLengthTest());
            //System.out.println("Accuracy: " + orderedPartialLengthTest(partialCueLength=4, this.partialCueBackwards=false));
/*
            System.out.println("Start testing");
            
            pEvents=new Vector<Event>(0);
            curPSeq=new Vector<Event>(0);
            curSeq=new Vector<Event>(0);
            
            eventIndex=0;
            //episodic.loadEpisodicModel(eventFile, seqFile);
            if(!isStart){
            initEpisodic();
            loadEvents();
            
            retrain();
            episodic.saveEpisodiceModel(this.eventFile, this.seqFile);
            isStart=true;
            }
            
            jLabel5.setText(""+episodic.seqLearner.numF1);
            jLabel6.setText(""+episodic.seqLearner.numCode);
            
            
            //jTextArea1.setText(getSeqString(events));
            
             */
            //jComboBox1.removeAllItems();
            //jComboBox1.addItem("SELECT AN EPISODE FOR PLAYBACK");

            //jComboBox2.removeAllItems();
            //jComboBox2.addItem("SELECT A RETRIEVE CUE");

            for (int i = 0; i < episodic.seqLearner.numCode; i++) {
                this.jComboBox1.addItem(new Integer(i));
                this.jComboBox4.addItem(new Integer(i));
                this.jComboBox3.addItem(new Integer(i));
            }

            for (int i = 1; i <= 5; i++) {
                if (i == 1) {
                    this.jComboBox5.addItem(new String("Full Length"));
                } else {
                    this.jComboBox5.addItem(new String("1/" + (i) + " Length"));
                }
            }

            this.jComboBox2.addItem(new String("Beginning"));
            this.jComboBox2.addItem(new String("End"));
            this.jComboBox2.addItem(new String("Arbitary"));

            this.jComboBox7.addItem(new String("Noisy Events"));
            this.jComboBox7.addItem(new String("Noisy Episodes"));

            this.jComboBox6.addItem(new String("2%"));
            this.jComboBox6.addItem(new String("5%"));
            this.jComboBox6.addItem(new String("10%"));
            this.jComboBox6.addItem(new String("15%"));
            this.jComboBox6.addItem(new String("20%"));
}//GEN-LAST:event_StartPressed

protected double getPrecision(){
    double err=0;
    for(int i=0;i<pEvents.size();i++){
        err+=pEvents.get(i).getDistance(events.get(i));
    }
    return err/=((double) pEvents.size());
}

protected double getPrecisionPerSeq(){
    double err=0;
    
    if(curPSeq.size()!=curSeq.size()){
        return 0;
    }
    
    for(int i=0;i<curPSeq.size();i++){
        err+=curPSeq.get(i).getDistance(curSeq.get(i));
    }
    return err/=((double) curPSeq.size());
}

protected void loadEvents(){
    
    try{
        events.removeAllElements();
        FileReader fr=new FileReader(eventListFile);
    	Scanner sc=new Scanner(fr);
        while(sc.hasNext()){
            Event e=new Event();
            e.x=sc.nextDouble();
            sc.nextDouble();
            e.z=sc.nextDouble();
            sc.nextDouble();
            e.y=sc.nextDouble();
            sc.nextDouble();
            e.curHealth=sc.nextDouble();
            sc.nextDouble();
            e.hasAmmo=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.emeDistance=sc.nextDouble();
            sc.nextDouble();
            e.reachableItem=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.getHealth=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.getWeapon=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.changeWeapon=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.isShooting=convertBool(sc.nextDouble());
            sc.nextDouble();
            
            e.pickupHealS=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupHealL=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon1=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon2=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon3=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon4=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon5=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon6=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon7=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon8=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon9=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon10=convertBool(sc.nextDouble());
            sc.nextDouble();
            
            e.curWeapon1=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.curWeapon2=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.curWeapon3=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.curWeapon4=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.curWeapon5=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.curWeapon6=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.curWeapon7=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.curWeapon8=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.curWeapon9=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.curWeapon10=convertBool(sc.nextDouble());
            sc.nextDouble();
            
            //e.shotNum=(int)sc.nextDouble();
            
            e.behave1=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.behave2=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.behave3=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.behave4=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.reward=sc.nextDouble();
            sc.nextDouble();
            events.add(e);
        }
        System.out.println("LOADING COMPLETED!");
        System.out.println("Number of events="+events.size());
    	
    }catch (Exception e){
         e.printStackTrace();
         System.out.println("Cannot load event lists!!!");
    }   
}

protected void loadNEvents(){
    
    try{
        events.removeAllElements();
        FileReader fr=new FileReader(this.eventListFile_noise);
    	Scanner sc=new Scanner(fr);
        while(sc.hasNext()){
            Event e=new Event();
            e.x=sc.nextDouble();
            sc.nextDouble();
            e.z=sc.nextDouble();
            sc.nextDouble();
            e.y=sc.nextDouble();
            sc.nextDouble();
            e.curHealth=sc.nextDouble();
            sc.nextDouble();
            e.hasAmmo=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.emeDistance=sc.nextDouble();
            sc.nextDouble();
            e.reachableItem=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.getHealth=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.getWeapon=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.changeWeapon=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.isShooting=convertBool(sc.nextDouble());
            sc.nextDouble();
            
            e.pickupHealS=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupHealL=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon1=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon2=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon3=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon4=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon5=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon6=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon7=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon8=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon9=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.pickupWeapon10=convertBool(sc.nextDouble());
            sc.nextDouble();
            
            e.curWeapon1=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.curWeapon2=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.curWeapon3=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.curWeapon4=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.curWeapon5=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.curWeapon6=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.curWeapon7=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.curWeapon8=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.curWeapon9=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.curWeapon10=convertBool(sc.nextDouble());
            sc.nextDouble();
            
            //e.shotNum=(int)sc.nextDouble();
            
            e.behave1=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.behave2=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.behave3=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.behave4=convertBool(sc.nextDouble());
            sc.nextDouble();
            e.reward=sc.nextDouble();
            sc.nextDouble();
            events.add(e);
        }
    	
    }catch (Exception e){
         e.printStackTrace();
         System.out.println("Cannot load event lists!!!");
    }   
}

public String eventDescription(Event e){
    //by Budhitama Subagdja
    String desc = "";
    desc = desc + "At (" + e.x + "," + e.z +") ";
    desc = desc + "Health: " + e.curHealth + " ";
    if(e.emeDistance<1){
        desc = desc + "Enemy Detected! Distance: " + e.emeDistance;
    }
    desc = desc + "\n";
    if(!e.hasAmmo) desc = desc + "|Empty Ammo| ";
    if(e.reachableItem) desc = desc + "|Item detected|";
    if(e.behave1) desc = desc + "|Doing A|";
    if(e.behave2) desc = desc + "|Doing B|";
    if(e.behave3) desc = desc + "|Doing C|";
    if(e.behave4) desc = desc + "|Doing D|";
    if(e.reward >=1){
        desc = desc + "\n";
        desc = desc + "FEELS GOOD!";
    }
    if(e.reward <=0){
        desc = desc + "\n";
        desc = desc + "HURT!";
    }
    return desc;
}

public void eventTextDescription(Event e){
    //by Budhitama Subagdja
    String desc = "";
    desc = desc + "At (" + e.x + "," + e.z +") ";
    desc = desc + "Health: " + e.curHealth + " ";
    if(e.emeDistance<1){
        desc = desc + "Enemy Detected! Distance: " + e.emeDistance;
    }
    //jTextArea3.append(desc);
    //jTextArea3.append("\n");
    
    desc = "";
    if(!e.hasAmmo) desc = desc + "|Empty Ammo| ";
    if(e.reachableItem) desc = desc + "|Item detected|";
    if(e.behave1) desc = desc + "|Doing A|";
    if(e.behave2) desc = desc + "|Doing B|";
    if(e.behave3) desc = desc + "|Doing C|";
    if(e.behave4) desc = desc + "|Doing D|";
    //jTextArea3.append(desc);
    desc="";
    //jTextArea3.append("\n");
    if(e.reward >=1){
        desc = "FEELS GOOD!";
    }
    if(e.reward <=0){
        desc = "HURT!";
    }
    //jTextArea3.append(desc);
    //jTextArea3.append("\n");
    
}


public boolean convertBool(double b){
    if(b>0.0){
        return true;
    }
    else{
        return false;
    }
}

protected Vector<Vector<String>> getStoryString(Vector<Event> eList){
     Vector<Vector<String>> story=new  Vector<Vector<String>>(0);
    
    for(int i=0;i<eList.size();i++){
        if(i==0){
            story.add(eList.get(i).printStoryBegin(this.isNewStart));
            continue;
        }
        
        else{
            Vector<String> oneEvent=EventDistance.incEDiscption(eList.get(i-1), eList.get(i));
            if(oneEvent.size()!=0){
                 story.add(oneEvent);
            }

        }
    }
     
    return story;
}

protected String getSeqString(Vector<Event> eList){
    String str="";
    
    for(int i=0;i<eList.size();i++){
        str+=eList.get(i).toString();
    }
    return str;
}

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
// TODO add your handling code here:
      if (this.seq_ptr < 0) {
        this.seq_ptr = 0;
    } else {

        this.seq_ptr--;
        if (this.seq_ptr < 0) {
            this.seq_ptr = this.episodic.seqLearner.numCode - 1;
        }
    }

    switch (this.testState) {
        case 0:
            this.oneTestPlayback();
            break;
        case 1:
            this.oneTestRecall();
            break;
        case 2:
            this.oneTestImperfect();
            break;
    }
}//GEN-LAST:event_jButton2ActionPerformed

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
// TODO add your handling code here:
    RetainPanel rp=new RetainPanel(this);
    
    //rp.main(null);
}//GEN-LAST:event_jButton3ActionPerformed

private void ChangDispalyMode(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChangDispalyMode
// TODO add your handling code here:
    if(this.displayMode==DisplayByWord){
        this.displayMode=DisplayByLine;
    }            
    else{
        this.displayMode=DisplayByWord;
    }
}//GEN-LAST:event_ChangDispalyMode

private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
// TODO add your handling code here:
    if (this.seq_ptr < 0) {
        this.seq_ptr = 0;
    } else {
        this.seq_ptr = (this.seq_ptr + 1) % this.episodic.seqLearner.numCode;
    }

    switch (this.testState) {
        case 0:
            this.oneTestPlayback();
            break;
        case 1:
            this.oneTestRecall();
            break;
        case 2:
            this.oneTestImperfect();
            break;
    }
}//GEN-LAST:event_jButton5ActionPerformed

private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
// TODO add your handling code here:
    if (this.jComboBox2.getSelectedIndex() == 0) {
        return;
    }

    if (this.testState != this.recall) {
        this.testState = this.recall;
    }

    this.cueLoc = this.jComboBox2.getSelectedIndex() - 1;
}//GEN-LAST:event_jComboBox2ActionPerformed

private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
// TODO add your handling code here:
        if (this.jComboBox1.getSelectedIndex() == 0) {
        return;
    }

    this.seq_ptr = this.jComboBox1.getSelectedIndex() - 1;

    System.out.println("Seq_ptr=" + this.seq_ptr);

    if (this.testState != this.playback) {
        this.testState = this.playback;
    }

}//GEN-LAST:event_jComboBox1ActionPerformed

private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
// TODO add your handling code here:
    if (this.jComboBox3.getSelectedIndex() == 0) {
        return;
    }

    if (this.testState != this.recall) {
        this.testState = this.recall;
    }

    this.seq_ptr = this.jComboBox3.getSelectedIndex() - 1;

    System.out.println("Seq_ptr=" + this.seq_ptr);
}//GEN-LAST:event_jComboBox3ActionPerformed

private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
// TODO add your handling code here:
    if (this.jComboBox4.getSelectedIndex() == 0) {
        return;
    }

    if (this.testState != this.imperfectRecall) {
        this.testState = this.imperfectRecall;
    }

    this.seq_ptr = this.jComboBox4.getSelectedIndex() - 1;

    System.out.println("Seq_ptr=" + this.seq_ptr);
}//GEN-LAST:event_jComboBox4ActionPerformed

private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
// TODO add your handling code here:
     if (this.jComboBox5.getSelectedIndex() == 0) {
        return;
    }

    if (this.testState != this.recall) {
        this.testState = this.recall;
    }

    this.cueLen = this.jComboBox5.getSelectedIndex();
}//GEN-LAST:event_jComboBox5ActionPerformed

private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
// TODO add your handling code here:
    if (this.jComboBox6.getSelectedIndex() == 0) {
        return;
    }

    if (this.testState != this.imperfectRecall) {
        this.testState = this.imperfectRecall;
    }

    this.errorRate = this.jComboBox6.getSelectedIndex() - 1;
}//GEN-LAST:event_jComboBox6ActionPerformed

private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
// TODO add your handling code here:
     if (this.jComboBox7.getSelectedIndex() == 0) {
        return;
    }

    if (this.testState != this.imperfectRecall) {
        this.testState = this.imperfectRecall;
    }

    this.errorType = this.jComboBox7.getSelectedIndex() - 1;
}//GEN-LAST:event_jComboBox7ActionPerformed

private void jButton6ChangDispalyMode(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ChangDispalyMode
// TODO add your handling code here:
    if (this.testState != this.playback) {
        return;
    }

    if (this.tstErrors) {
        System.out.println("================");
        System.out.println("Event lengh before noise loading: " + events.size());
        this.EventListLoad();
        System.out.println("Event lengh after noise loading: " + events.size());
        this.tstErrors = false;
    }
    int numTrials = seqStartIndexes.size() - 1;
    int sumSuccess = 0;
    int len = this.partialCueLength;
    boolean fromLast = this.partialCueBackwards;

    curSeq.removeAllElements();
    curPSeq.removeAllElements();

    int selidx = ((Integer) seqStartIndexes.get(this.seq_ptr)).intValue();
    int epiLen = ((Integer) seqLenIndexes.get(this.seq_ptr)).intValue();
    if (len > epiLen) {
        len = epiLen;
    }
    int sidx = 0;

    //System.out.println("IDXs: " + selidx + " " + len + " " + sidx);

    if (selidx == 0) {
        this.isNewStart = true;
    } else if (events.get(selidx - 1).isForcedTer(events.get(selidx - 1))) {
        this.isNewStart = true;
    } else {
        this.isNewStart = false;
    }

    //get CurrentSeq list
    for (int i = 0; i < epiLen; i++) {
        this.curSeq.add(this.events.get(selidx + i));
    }

    int winidx = this.seq_ptr;

    if (winidx >= 0) {
       // System.out.println("play a sequence");
        curPSeq = episodic.getSeqEvents(episodic.seqLearner.getOneSeq(((Integer) seqWinIndexes.get(winidx)).intValue()));
    }

    this.jLabel10.setText("PREDICTION ACCURACY " + "(" + selidx + "~" + (selidx + epiLen - 1) + ")" + "/(" + 0 + "~" + (events.size() - 1) + ")");
    //curStory=getStoryString(curSeq);
    curPStory = getStoryString(curPSeq);
    this.jLabel9.setText("Retrieved Episode: (" + winidx + "/" + (this.episodic.seqLearner.numCode - 1) + ")");
    this.jLabel11.setText("");

    timer.start();
}//GEN-LAST:event_jButton6ChangDispalyMode

private void jButton7ChangDispalyMode(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ChangDispalyMode
// TODO add your handling code here:
    if (this.testState != this.recall) {
        return;
    }

    if (this.tstErrors) {
        System.out.println("================");
        System.out.println("Event lengh before noise loading: " + events.size());
        this.EventListLoad();
        System.out.println("Event lengh after noise loading: " + events.size());
        this.tstErrors = false;
    }

    //int len=this.jComboBox2.getSelectedIndex()+1;
    int len = this.cueLen;


    int numTrials = seqStartIndexes.size() - 1;
    int sumSuccess = 0;
    for (int t = 0; t < numTrials; t++) {
        int selidx = ((Integer) seqStartIndexes.get(t)).intValue();
        int epiLen = ((Integer) seqLenIndexes.get(t)).intValue();
        int curLen = epiLen / this.cueLen;
        //int curLen=epiLen/4;
        if (curLen <= 0) {
            curLen = epiLen;
        }
        int sidx = 0;

        if (this.cueLoc == this.endCue) {
            sidx = epiLen - curLen;
            if (sidx < 0) {
                sidx = 0;
            }
        }

        if (this.cueLoc == this.anyCue) {
            sidx = (int) ((epiLen - curLen) * Math.random());
            if (sidx < 0) {
                sidx = 0;
            }
        }

        System.out.println("index: " + selidx + " fullLen: " + epiLen + " partial: " + curLen + " startPos: " + sidx);
        int winidx = seqRecognition(selidx, curLen, sidx, epiLen);
        if (winidx == ((Integer) seqWinIndexes.get(t)).intValue()) {
            sumSuccess++;
        }
    }
    this.jLabel11.setText((((1.0 * sumSuccess) / (1.0 * numTrials)) * 100) + "%");

    //this.seq_ptr=(int)(Math.random()*(this.episodic.seqLearner.numCode));
    //if(seq_ptr==episodic.seqLearner.numCode){
    //%   seq_ptr--;
    //}

    System.out.println("Seq_ptr=" + this.seq_ptr);

    curSeq.removeAllElements();
    curPSeq.removeAllElements();

    int selidx = ((Integer) seqStartIndexes.get(this.seq_ptr)).intValue();
    int epiLen = ((Integer) seqLenIndexes.get(this.seq_ptr)).intValue();
    int curLen = epiLen / this.cueLen;
    //int curLen=epiLen/4;
    if (curLen <= 0) {
        curLen = epiLen;
    }
    int sidx = 0;

    if (this.cueLoc == this.endCue) {
        sidx = epiLen - curLen;
        if (sidx < 0) {
            sidx = 0;
        }
    }

    if (this.cueLoc == this.anyCue) {
        sidx = (int) ((epiLen - curLen) * Math.random());
        if (sidx < 0) {
            sidx = 0;
        }
    }

    System.out.println("index: " + selidx + " fullLen: " + epiLen + " partial: " + curLen + " startPos: " + sidx);
    int winidx = seqRecognition(selidx, curLen, sidx, epiLen);

    if (selidx + sidx == 0) {
        this.isNewStart = true;
    } else if (events.get(selidx + sidx - 1).isForcedTer(events.get(selidx+ sidx - 1))) {
        this.isNewStart = true;
    } else {
        this.isNewStart = false;
    }

    //get CurrentSeq list
    for (int i = selidx + sidx; i < selidx + sidx + curLen; i++) {
        this.curSeq.add(this.events.get(i));
    }



    if (winidx >= 0) {
        System.out.println("play a sequence");
        curPSeq = episodic.getSeqEvents(episodic.seqLearner.getOneSeq(((Integer) seqWinIndexes.get(winidx)).intValue()));
    }

    this.jLabel10.setText("RETRIEVE ACCURACY " + "(" + selidx + "~" + (selidx + epiLen - 1) + ")" + "/(" + 0 + "~" + (events.size() - 1) + ")");
    curStory = getStoryString(curSeq);
    curPStory = getStoryString(curPSeq);
    this.jLabel9.setText("Retrieved Episode: (" + winidx + "/" + (this.episodic.seqLearner.numCode - 1) + ")");

    timer.start();
}//GEN-LAST:event_jButton7ChangDispalyMode

private void jButton8ChangDispalyMode(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ChangDispalyMode
// TODO add your handling code here:
    if (this.testState != this.imperfectRecall) {
        return;
    }

    if (this.errorType == 0 && this.errorRate == 0) {
        this.eventListFile_noise = filePath + "Player_3_noise_event1_new_2_percent.ext";
    } else if (this.errorType == 0 && this.errorRate == 1) {
        this.eventListFile_noise = filePath + "Player_3_noise_event1_new_5_percent.ext";
    } else if (this.errorType == 0 && this.errorRate == 2) {
        this.eventListFile_noise = filePath + "Player_3_noise_event1_new_10_percent.ext";
    } else if (this.errorType == 0 && this.errorRate == 3) {
        this.eventListFile_noise = filePath + "Player_3_noise_event1_new_15_percent.ext";
    } else if (this.errorType == 1 && this.errorRate == 0) {
        this.eventListFile_noise = filePath + "Player_3_noise_event2_new_5_percent.ext";
    } else if (this.errorType == 1 && this.errorRate == 1) {
        this.eventListFile_noise = filePath + "Player_3_noise_event2_new_10_percent.ext";
    } else if (this.errorType == 1 && this.errorRate == 2) {
        this.eventListFile_noise = filePath + "Player_3_noise_event2_new_15_percent.ext";
    } else if (this.errorType == 1 && this.errorRate == 3) {
        this.eventListFile_noise = filePath + "Player_3_noise_event2_new_20_percent.ext";
    } 
     else {
        return;
    }

    System.out.println("================");
    System.out.println("Event lengh before noise loading: " + events.size());
    this.EventNoiseListLoad();
    System.out.println("Event lengh after noise loading: " + events.size());
    this.tstErrors = true;
    
    Event testE=events.get(80);
    double[][] testPattern=testE.toArray();
    for(int i=0;i<testPattern.length;i++){
        for(int j=0;j<testPattern[i].length;j++){
            System.out.print(""+testPattern[i][j]+"\t");
        }
        System.out.println("");
    }


    //int len=this.jComboBox2.getSelectedIndex()+1;
    int len = this.cueLen;


    int numTrials = seqStartIndexes.size() - 1;
    int sumSuccess = 0;
    for (int t = 0; t < numTrials; t++) {
        int selidx = ((Integer) seqStartIndexes.get(t)).intValue();
        int epiLen = ((Integer) seqLenIndexes.get(t)).intValue();
        int curLen = epiLen;
        //int curLen=epiLen/4;

        int sidx = 0;


        System.out.println("index: " + selidx + " fullLen: " + epiLen + " partial: " + curLen + " startPos: " + sidx);
        int winidx = seqRecognition(selidx, curLen, sidx, epiLen);
        if (winidx == ((Integer) seqWinIndexes.get(t)).intValue()) {
            sumSuccess++;
        }
    }
    this.jLabel11.setText((((1.0 * sumSuccess) / (1.0 * numTrials)) * 100) + "%");

    //this.seq_ptr=(int)(Math.random()*(this.episodic.seqLearner.numCode));
    //if(seq_ptr==episodic.seqLearner.numCode){
    //%   seq_ptr--;
    //}

   // System.out.println("Seq_ptr=" + this.seq_ptr);

    curSeq.removeAllElements();
    curPSeq.removeAllElements();

    int selidx = ((Integer) seqStartIndexes.get(this.seq_ptr)).intValue();
    int epiLen = ((Integer) seqLenIndexes.get(this.seq_ptr)).intValue();
    int curLen = epiLen;

    int sidx = 0;


    System.out.println("index: " + selidx + " fullLen: " + epiLen + " partial: " + curLen + " startPos: " + sidx);
    int winidx = seqRecognition(selidx, curLen, sidx, epiLen);

    if (selidx == 0) {
        this.isNewStart = true;
    } else if (events.get(selidx - 1).isForcedTer(events.get(selidx - 1))) {
        this.isNewStart = true;
    } else {
        this.isNewStart = false;
    }

    //get CurrentSeq list
    for (int i = 0; i < curLen; i++) {
        this.curSeq.add(this.events.get(selidx + i));
    }


    if (winidx >= 0) {
        System.out.println("play a sequence");
        curPSeq = episodic.getSeqEvents(episodic.seqLearner.getOneSeq(((Integer) seqWinIndexes.get(winidx)).intValue()));
    }

    this.jLabel10.setText("RETRIEVE ACCURACY " + "(" + selidx + "~" + (selidx + epiLen - 1) + ")" + "/(" + 0 + "~" + (events.size() - 1) + ")");
    curStory = getStoryString(curSeq);
    curPStory = getStoryString(curPSeq);
    this.jLabel9.setText("Retrieved Episode: (" + winidx + "/" + (this.episodic.seqLearner.numCode - 1) + ")");

    timer.start();
}//GEN-LAST:event_jButton8ChangDispalyMode

private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
// TODO add your handling code here:
     //this.initCISM();
     //this.transferToSM();
     //ICSMDisplay cismDisplay=new ICSMDisplay();
     //cismDisplay.setICSM(CISM);
     
     this.initWeaponSM();
     this.transferToWeaponSM();
     WeaponSMDisplay weaponSMDisplay=new WeaponSMDisplay();
     weaponSMDisplay.setWeaponSM(this.WeaponSM);
     
     //this.initCRSM();
     //this.transferToCRSM();
     //CRSMDisplay CRSMDisplay=new CRSMDisplay();
     //CRSMDisplay.setCRSM(this.CRSM, this.episodic);
}//GEN-LAST:event_jButton9ActionPerformed

private void jButton9StartPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9StartPressed
// TODO add your handling code here:
   
}//GEN-LAST:event_jButton9StartPressed




public void testingPartialSeqScenario1(){
    System.out.println("=======================");
    System.out.println("ONE SEQUENCE LEARNT");
    System.out.println("=======================");
    
    for(;eventIndex<events.size();eventIndex++){
        Event e=events.get(eventIndex);
        curSeq.add(e);
        int[] learntSeq=null;
        int curSeqLength=curSeq.size();
        int startIndex=curSeqLength/3;
        if(e.isForcedTer(e)){  
            System.out.println("Sequence Termimated by Battle Consequences");
            System.out.println("Sequence Length="+(eventIndex-curSeq.size()+1)+"~"+eventIndex);
            System.out.println("Contain Identicial Events?"+this.containsIdentical(curSeq));
            
            this.jLabel10.setText("PREDICTION ACCURACY "+"("+(eventIndex-curSeq.size()+1)+"~"+eventIndex+")"+"/("+0+"~"+events.size()+")");
            
            if(curSeqLength<3){
                startIndex=1;
            }
            System.out.println("Testing Partial Sequence Length="+startIndex);
            boolean succeed=true;
            for(int i=0;i<startIndex;i++){
                if(!episodic.newTrainEvent(curSeq.get(i).toArray())){
                    succeed=false;
                }
             }
            
            if(!succeed){
                learntSeq=null;
            }
            else{
                learntSeq=episodic.seqLearner.oneTrainIn();
                
                int index=startIndex;
                while(learntSeq!=null&&learntSeq[0]==-1&&index<curSeqLength){
                    if(!episodic.newTrainEvent(curSeq.get(index).toArray())){
                        learntSeq=null;
                        break;
                    }    
                    learntSeq=episodic.seqLearner.oneTrainIn();
                    index++;
                }
                
                if(learntSeq!=null&&learntSeq[0]==-1){
                    learntSeq=null;
                }
            }
            
            episodic.seqLearner.emptyAccumulated();
             
            if(learntSeq==null){
                System.out.println("Result: Sequence cant be retrieved");
                for(int j=0;j<curSeq.size();j++){
                    pEvents.add(new Event());
                    pEvents.lastElement().setDefault();
                }
                System.out.println("pEvents.length="+pEvents.size());
                
                //this.jTextArea1.setText(getSeqString(curSeq));
                //this.jTextArea2.setText(getSeqString(curPSeq));
                //curStory=getStoryString(curSeq);
                curPStory=getStoryString(curPSeq);
                timer.start();
                System.out.println("Learnt Sequence Length="+curPSeq.size());
                System.out.println("Lerant Sequence Length="+(pEvents.size()-curSeq.size())+"~"+(pEvents.size()-1));
                //this.jLabel11.setText(""+getPrecisionPerSeq());
                this.curPSeq.removeAllElements();
                this.curSeq.removeAllElements();                
                eventIndex++;
                
                break;
            }
            
             if(curSeq.size()==learntSeq.length){
                System.out.println("Result: Sequence Learnt with Identicial Length");
                curPSeq=episodic.getSeqEvents(learntSeq);
                for(int j=0;j<curPSeq.size();j++){
                    pEvents.add(curPSeq.get(j));
                }
                System.out.println("pEvents.length="+pEvents.size());
                System.out.println("Lerant Sequence Length="+(pEvents.size()-curSeq.size())+"~"+(pEvents.size()-1));
            }
            else if(curSeq.size()>learntSeq.length){
                System.out.println("Result: Sequence Learnt with Shorter Length");
                curPSeq=episodic.getSeqEvents(learntSeq);
                for(int j=0;j<curPSeq.size();j++){
                    pEvents.add(curPSeq.get(j));
                }
                System.out.println("Learnt Sequence Length="+curPSeq.size());
                for(int j=0;j<curSeq.size()-learntSeq.length;j++){
                    pEvents.add(new Event());
                    pEvents.lastElement().setDefault();
                }
                System.out.println("pEvents.length="+pEvents.size());
                System.out.println("Lerant Sequence Length="+(pEvents.size()-curSeq.size())+"~"+(pEvents.size()-1));
            }
            else{
                System.out.println("Result: Sequence Learnt with Longer Length");
                System.out.println("Learnt Sequence Length="+curPSeq.size());
                curPSeq=episodic.getSeqEvents(learntSeq);
                for(int j=0;j<curSeq.size();j++){
                    pEvents.add(curPSeq.get(j));
                }
                System.out.println("pEvents.length="+pEvents.size());
                System.out.println("Lerant Sequence Length="+(pEvents.size()-curSeq.size())+"~"+(pEvents.size()-1));
            }
            
            //this.jTextArea1.setText(getSeqString(curSeq));
            //this.jTextArea2.setText(getSeqString(curPSeq));
            //curStory=getStoryString(curSeq);
            curPStory=getStoryString(curPSeq);
            timer.start();
            this.jLabel11.setText(""+getPrecisionPerSeq());
            this.curPSeq.removeAllElements();
            this.curSeq.removeAllElements();
            eventIndex++;
            break;
        }
    }
    
    if(eventIndex>=events.size()){
        this.jLabel10.setText("PREDICTION ACCURACY (0~"+events.size()+")");
        this.jLabel11.setText(""+this.getPrecision());
    }   
    
}

public void testingPartialSeqScenario2(){
    System.out.println("=======================");
    System.out.println("ONE SEQUENCE LEARNT");
    System.out.println("=======================");
    
    for(;eventIndex<events.size();eventIndex++){
        Event e=events.get(eventIndex);
        curSeq.add(e);
        int[] learntSeq=null;
        int curSeqLength=curSeq.size();
        int startIndex=curSeqLength/3;
        System.out.println("eventIndex: " + eventIndex);
        System.out.println("startIndex: " + startIndex);
        
        if(e.isForcedTer(e)){  
            System.out.println("Sequence Termimated by Battle Consequences");
            System.out.println("Sequence Length="+(eventIndex-curSeq.size()+1)+"~"+eventIndex);
            System.out.println("Contain Identicial Events?"+this.containsIdentical(curSeq));
            
            this.jLabel10.setText("PREDICTION ACCURACY "+"("+(eventIndex-curSeq.size()+1)+"~"+eventIndex+")"+"/("+0+"~"+events.size()+")");
            
            if(curSeqLength<3){
                startIndex=1;
            }
            System.out.println("Testing Partial Sequence Length="+startIndex);
            boolean succeed=true;
            for(int i=0;i<startIndex;i++){
                if(!episodic.newTrainEvent(curSeq.get(i).toArray())){
                    succeed=false;
                }
             }
            /*
            if(!succeed){
                learntSeq=null;
            }
            else{
                learntSeq=episodic.seqLearner.oneTrainIn();
                
                int index=startIndex;
                while(learntSeq!=null&&learntSeq[0]==-1&&index<curSeqLength){
                    if(!episodic.newTrainEvent(curSeq.get(index).toArray())){
                        learntSeq=null;
                        break;
                    }    
                    learntSeq=episodic.seqLearner.oneTrainIn();
                    index++;
                }
                
                if(learntSeq!=null&&learntSeq[0]==-1){
                    learntSeq=null;
                }
            }
            
            episodic.seqLearner.emptyAccumulated();
             
            if(learntSeq==null){
                System.out.println("Result: Sequence cant be retrieved");
                for(int j=0;j<curSeq.size();j++){
                    pEvents.add(new Event());
                    pEvents.lastElement().setDefault();
                }
                System.out.println("pEvents.length="+pEvents.size());
                
                //this.jTextArea1.setText(getSeqString(curSeq));
                //this.jTextArea2.setText(getSeqString(curPSeq));
                curStory=getStoryString(curSeq);
                curPStory=getStoryString(curPSeq);
                timer.start();
                System.out.println("Learnt Sequence Length="+curPSeq.size());
                System.out.println("Lerant Sequence Length="+(pEvents.size()-curSeq.size())+"~"+(pEvents.size()-1));
                this.jLabel11.setText(""+getPrecisionPerSeq());
                this.curPSeq.removeAllElements();
                this.curSeq.removeAllElements();                
                eventIndex++;
                
                break;
            }
            
             if(curSeq.size()==learntSeq.length){
                System.out.println("Result: Sequence Learnt with Identicial Length");
                curPSeq=episodic.getSeqEvents(learntSeq);
                for(int j=0;j<curPSeq.size();j++){
                    pEvents.add(curPSeq.get(j));
                }
                System.out.println("pEvents.length="+pEvents.size());
                System.out.println("Lerant Sequence Length="+(pEvents.size()-curSeq.size())+"~"+(pEvents.size()-1));
            }
            else if(curSeq.size()>learntSeq.length){
                System.out.println("Result: Sequence Learnt with Shorter Length");
                curPSeq=episodic.getSeqEvents(learntSeq);
                for(int j=0;j<curPSeq.size();j++){
                    pEvents.add(curPSeq.get(j));
                }
                System.out.println("Learnt Sequence Length="+curPSeq.size());
                for(int j=0;j<curSeq.size()-learntSeq.length;j++){
                    pEvents.add(new Event());
                    pEvents.lastElement().setDefault();
                }
                System.out.println("pEvents.length="+pEvents.size());
                System.out.println("Lerant Sequence Length="+(pEvents.size()-curSeq.size())+"~"+(pEvents.size()-1));
            }
            else{
                System.out.println("Result: Sequence Learnt with Longer Length");
                System.out.println("Learnt Sequence Length="+curPSeq.size());
                curPSeq=episodic.getSeqEvents(learntSeq);
                for(int j=0;j<curSeq.size();j++){
                    pEvents.add(curPSeq.get(j));
                }
                System.out.println("pEvents.length="+pEvents.size());
                System.out.println("Lerant Sequence Length="+(pEvents.size()-curSeq.size())+"~"+(pEvents.size()-1));
            }
            
            //this.jTextArea1.setText(getSeqString(curSeq));
            //this.jTextArea2.setText(getSeqString(curPSeq));
            curStory=getStoryString(curSeq);
            curPStory=getStoryString(curPSeq);
            //timer.start();
            //this.jLabel11.setText(""+getPrecisionPerSeq());
            this.curPSeq.removeAllElements();
            this.curSeq.removeAllElements();
            eventIndex++;
            break;
             */
        }
    
     }
    
}

public void EventListLoad(){
//by Budhitama Subagdja    
    System.out.println("Start loading");
    
    events=new Vector<Event>(0);
    pEvents=new Vector<Event>(0);
    curPSeq=new Vector<Event>(0);
    curSeq=new Vector<Event>(0);

    eventIndex=0;
    
    loadEvents();
    
    //episodic.loadEpisodicModel(eventFile, seqFile);
    if(!isStart){
        initEpisodic();
        //loadEvents();

        //for(int i=0;i<events.size();i++){
        //    Event e=events.get(i);
        //    System.out.println(e);
        //}
        initTraining();
        
        /*retrain();
        episodic.saveEpisodiceModel(this.eventFile, this.seqFile);*/
        isStart=true;
    }
    
    
    
    //jTextArea1.setText(getSeqString(events));
    
    
}

public void EventNoiseListLoad(){
//by Budhitama Subagdja    
    System.out.println("Start loading");
    
    events=new Vector<Event>(0);
    pEvents=new Vector<Event>(0);
    curPSeq=new Vector<Event>(0);
    curSeq=new Vector<Event>(0);

    eventIndex=0;
    //episodic.loadEpisodicModel(eventFile, seqFile);
       this.loadNEvents();
    }
    



public void initEpisodic(){
    episodic=new EpisodicModelcc(); 
     for (int i=0;i<numFieldE;i++) {
          inputE[i] = new double [lenE[i]];
     }
     episodic.initEventPara(numFieldE, lenE, betaE, alphaE, gammaE, rhoE);
     episodic.initSeqPara(betaS, alphaS, gammaS, rhoS);
     
}

public void initTraining(){
    //by Budhitama Subagdja
    int previdx = 0; 
    int diffidx = 0;
    int maxseqlimit = SEQLENGTH;
    if(events.isEmpty()){
        System.out.println("ERROR: EMPTY EVENT SET!!");
        return;
    }
    int seqcount=0;
    int curSeqLen=0;
    seqStartIndexes = new ArrayList();
    seqLenIndexes = new ArrayList();
    seqWinIndexes = new ArrayList();
    seqStartIndexes.add(0);
    for(int i=0;i<events.size();i++){
    //for(int i=0;i<12;i++){
        //System.out.println("");
        Event e=events.get(i);
        //System.out.println(e);
        //System.out.println(eventDescription(e)+"\n");
        //eventTextDescription(e);       
        episodic.newEvent(e.toArray());
        curSeqLen++;
        //System.out.println("Tho activation: " + episodic.seqLearner.tho);
        if((e.isForcedTer(e))||(episodic.seqLearner.tho<=0.0)||i==events.size()-1){
        //if(curSeqLen>maxseqlimit){
             //if(episodic.seqLearner.tho<=0.0)
                //System.out.println("REACHING SEQUENCE LIMIT");
             //else
                 //System.out.println("TERMINATION SIGNAL DETECTED");
             curSeqLen=0;
             seqcount++;
             seqStartIndexes.add(i+1);
             diffidx = (i+1)-previdx;
             seqLenIndexes.add(diffidx);
            // System.out.println("Episode length: " + diffidx);
             previdx = i+1;
             //System.out.println("Last index: " + (i));
             //System.out.println("End sequence: " + seqcount);
             int win=-1;
             System.out.println("F2: " + (win=episodic.seqLearner.sequenceEndWinner()));
             seqWinIndexes.add(win);
             episodic.seqLearner.resetCurSeq();
             episodic.seqLearner.resetActivityF1();
             episodic.seqLearner.resetBaseLineTho();
            // jTextArea3.append("------------------------------\n\n");
             
             if(this.isForget){
                 this.episodic.eventLearner.prune();
                 int[] purged=this.episodic.eventLearner.purge();
                  this.episodic.seqLearner.purgeEvent(purged);
             }

        }
        
        //this.episodic.eventLearner.decay();
        //decaying of events according to their rewards
        for(int k=0;k<this.episodic.eventLearner.numCode-1;k++){
            if(this.episodic.eventLearner.weight[k][3][0]==0.5){
                this.episodic.eventLearner.fasterDecay(k,this.forgetRate3);
            }
            else if(this.episodic.eventLearner.weight[k][3][0]==0||this.episodic.eventLearner.weight[k][3][0]==1){
                this.episodic.eventLearner.slowerDecay(k,this.forgetRate1);
            }
            else{
                this.episodic.eventLearner.slowerDecay(k,this.forgetRate2);
            }
        }
        
        if(i%100==0){
            this.numEvents.add(new Integer(this.episodic.eventLearner.numCode-1));
            this.numEpis.add(new Integer(this.episodic.seqLearner.numCode-1));
        }
        
        if(i%1000==0){
            System.out.println("STATISIC PRINT @ TIME_STAMP="+i);
            System.out.println("Number of events: ");
            for(int t=0;t<numEvents.size();t++){
                System.out.print(numEvents.get(t).intValue()+"\t");
            }
            System.out.println("");
            System.out.println("Number of episodes: ");
            for(int t=0;t<numEpis.size();t++){
                System.out.print(numEpis.get(t).intValue()+"\t");
            }
            System.out.println("");
        }
    }
    if(curSeqLen>0){
        seqcount++;
        //System.out.println("End sequence: " + seqcount);
        System.out.println("F2: " + episodic.seqLearner.sequenceEndWinner());
        episodic.seqLearner.resetCurSeq();
        episodic.seqLearner.resetActivityF1();
        episodic.seqLearner.resetBaseLineTho();
    }
    
    //episodic.saveEpisodiceModel(this.eventFile, this.seqFile);
    
    System.out.println("Number of episodes="+seqcount);
    //for(int m=0;m<seqWinIndexes.size();m++){
      //  System.out.println(""+((Integer)seqWinIndexes.get(m)).intValue());
    //}
    
    System.out.println("================");
}

public double orderedFullLengthTest(){
    //by Budhitama Subagdja
    int numTrials = seqStartIndexes.size()-1;
    int sumSuccess = 0;
    for(int t=0;t<numTrials;t++){
        int selidx = ((Integer)seqStartIndexes.get(t)).intValue();
        int epiLen = ((Integer)seqLenIndexes.get(t)).intValue();
        
        int winidx = seqRecognition(selidx,epiLen,0,epiLen);
        
        System.out.println("t="+t+", winner="+winidx);
        if(winidx==((Integer)seqWinIndexes.get(t)).intValue()){
            sumSuccess++;
            System.out.println("Matched accurately: " + sumSuccess);
        }    
    }
    return (((1.0*sumSuccess)/(1.0*numTrials))*100);
}

public double orderedPartialLengthTest(int len, boolean fromLast){
    //by Budhitama Subagdja
    int numTrials = seqStartIndexes.size()-1;
    int sumSuccess = 0;
    for(int t=0;t<numTrials;t++){
        int selidx = ((Integer)seqStartIndexes.get(t)).intValue();
        int epiLen = ((Integer)seqLenIndexes.get(t)).intValue();
        if(len>epiLen)
            len = epiLen;
        int sidx = 0;
        if(fromLast){
            sidx = epiLen-len;
            if(sidx<0)
                sidx = 0;
            
        }
        System.out.println("IDXs: " + selidx + " " + len + " " + sidx);
        int winidx = seqRecognition(selidx,len,sidx,epiLen);
        if(winidx==((Integer)seqWinIndexes.get(t)).intValue()){
            sumSuccess++;
            System.out.println("Matched accurately: " + sumSuccess);
        }    
    }
    return (((1.0*sumSuccess)/(1.0*numTrials))*100);

}

public double orderedPartialLengthTest(int portionIdx, int numPortions){
    //by Budhitama Subagdja
    
    System.out.println("test portion "+portionIdx+"/"+numPortions);
    int numTrials = seqStartIndexes.size()-1;
    int sumSuccess = 0;
    for(int t=0;t<numTrials;t++){
        int selidx = ((Integer)seqStartIndexes.get(t)).intValue();
        int epiLen = ((Integer)seqLenIndexes.get(t)).intValue();
        int len=epiLen/numPortions;
        int sidx=0;
        
        if(len<=0){
            len=epiLen;
            
        }
        
        else{
        
            sidx = len*portionIdx;
        
            if(portionIdx==numPortions-1){
                len=epiLen-(epiLen/numPortions)*portionIdx;
            }
        }

        System.out.println("start: " + selidx + " cueLength: " + len + " startCue: " + sidx+ " fullLen: "+epiLen);
        int winidx = seqRecognition(selidx,len,sidx,epiLen);
        if(winidx==((Integer)seqWinIndexes.get(t)).intValue()){
            sumSuccess++;
            System.out.println("Matched accurately: " + sumSuccess);
        }    
    }
    return (((1.0*sumSuccess)/(1.0*numTrials))*100);

}

public double fullLengthTest(){
    //by Budhitama Subagdja
    
    int testTrial = 100;
    //Object seqsidx[] = seqStartIndexes.toArray();
    int seqLen = seqStartIndexes.size()-1;
    if(seqLen<0){
        seqLen = 0;
    }
    System.out.println("seqLen: " + seqLen);
    int sumSuccess = 0;
    for(int i=0;i<testTrial;i++){
        int selidx = (int)(Math.random()*seqLen);
        int epiLen = ((Integer)seqLenIndexes.get(selidx)).intValue();
        System.out.println("Selected index: " + selidx);
        //int winidx = selidx;
        //int winidx = seqRecognition(((Integer)seqStartIndexes.get(selidx)).intValue(),SEQLENGTH);
        int winidx = seqRecognition(((Integer)seqStartIndexes.get(selidx)).intValue(),epiLen);
        System.out.println("Winning index: " + winidx);
        if(winidx==selidx){
            sumSuccess++;
            System.out.println("Matched accurately: " + sumSuccess);
        }
    }
    System.out.println("Accuracy: " + (((1.0*sumSuccess)/(1.0*testTrial))*100) + "%");
    return (((1.0*sumSuccess)/(1.0*testTrial))*100);
}

public double partialLengthTest(int len, int startpos){
    //by Budhitama Subagdja
    int testTrial = 100;
    //Object seqsidx[] = seqStartIndexes.toArray();
    int seqLen = seqStartIndexes.size()-1;
    if(seqLen<0){
        seqLen = 0;
    }
    System.out.println("seqLen: " + seqLen);
    int sumSuccess = 0;
    for(int i=0;i<testTrial;i++){
        int selidx = (int)(Math.random()*seqLen);
        System.out.println("Selected index: " + selidx);
        //int winidx = selidx;
        int winidx = seqRecognition(((Integer)seqStartIndexes.get(selidx)).intValue(),len,startpos);
        System.out.println("Winning index: " + winidx);
        if(winidx==selidx){
            sumSuccess++;
            System.out.println("Matched accurately: " + sumSuccess);
        }
    }
    System.out.println("Accuracy: " + (((1.0*sumSuccess)/(1.0*testTrial))*100) + "%");
    return (((1.0*sumSuccess)/(1.0*testTrial))*100);

}

public int seqRecognition(int startidx, int len){
//by Budhitama Subagdja
    int widx = -1;
    int relidx = 0;
    int absidx = relidx + startidx;
    episodic.seqLearner.resetCurSeq();
    episodic.seqLearner.resetActivityF1();
    episodic.seqLearner.resetBaseLineTho();
    while((relidx<len)&&(absidx<events.size())){
        Event e=events.get(absidx);
        episodic.newEvent(e.toArray());
        relidx++;
        absidx++;
        widx = episodic.seqLearner.sequenceRecWinner();
        if(widx>=0){
            System.out.print("matching case: "+widx);
        }
    }
    System.out.println("\n"+"final matching result: "+widx);
    //widx = episodic.seqLearner.sequenceRecWinner();
    return widx;
}

public int seqRecognition(int startidx, int len, int locidx){
    //by Budhitama Subagdja
    int widx = -1;
    int relidx = 0;
    int absidx = relidx + startidx;
    int lidx = absidx + locidx;
    episodic.seqLearner.resetCurSeq();
    episodic.seqLearner.resetActivityF1();
    episodic.seqLearner.resetBaseLineTho();
    //while((relidx<=len)&&(absidx<events.size())){
    while((lidx<absidx+len+locidx)&&(lidx<events.size())){
        Event e=events.get(lidx);
        episodic.newEvent(e.toArray());
        lidx++;
    }
    
    widx = episodic.seqLearner.sequenceRecWinner();
    System.out.println("Sequence WINNNN: " + widx);
    return widx;    
}

public int seqRecognition(int startidx, int len, int locidx, int fullLen){
    //by Budhitama Subagdja    
   // System.out.println("index: " + startidx +" fullLen: "+fullLen+ " partial: " + len + " startPos: " + locidx);
    int widx = -1;
    int relidx = 0;
    int absidx = relidx + startidx;
    int lidx = absidx + locidx;
   // System.out.println("start_lidx="+lidx);
    episodic.seqLearner.resetCurSeq();
    episodic.seqLearner.resetActivityF1();
    episodic.seqLearner.resetBaseLineTho();
    //while((relidx<=len)&&(absidx<events.size())){
    while((lidx<absidx+len+locidx)&&(lidx<events.size())){
        Event e=events.get(lidx);
        episodic.newEvent(e.toArray());
        //System.out.println("load event: lidx="+lidx+" endCueIndex="+(absidx+len-1));
        
        widx = episodic.seqLearner.sequenceRecWinner();
        if(widx>=0){
           // System.out.println("matching case: "+widx);
        }
        
        lidx++;
    }
    
    while((lidx<absidx+fullLen)&&(lidx<events.size())){
       //System.out.println("ERROR for last order: lidx="+lidx+" endIndex="+(absidx+fullLen-1));
       /* for(int i=0;i<episodic.seqLearner.curSeqWeight.size();i++){
                double decayedValue=episodic.seqLearner.curSeqWeight.get(i).doubleValue()*(1-episodic.seqLearner.v);
                episodic.seqLearner.curSeqWeight.remove(i);
                episodic.seqLearner.curSeqWeight.add(i, new Double(decayedValue));
        }*/
        
        decay();
        
        widx = episodic.seqLearner.sequenceRecWinner();
        if(widx>=0){
          //  System.out.print("matching case: "+widx);
        }
        
        lidx++;
    }
    
    widx = episodic.seqLearner.sequenceRecWinner();
    System.out.println("Sequence WINNNN: " + widx);
    return widx;    
}

//method modified by www after 02/12/09
public void retrain(){
    int numFieldE_temp=numFieldE;
    int[] lenE_temp=new int[lenE.length];
    System.arraycopy(lenE, 0, lenE_temp, 0, lenE.length);
    double[] betaE_temp=new double[betaE.length];
    System.arraycopy(betaE, 0, betaE_temp, 0, betaE.length);
    double[] alphaE_temp=new double[alphaE.length];
    System.arraycopy(alphaE, 0, alphaE_temp, 0, alphaE.length);
    double[] gammaE_temp=new double[gammaE.length];
    System.arraycopy(gammaE, 0, gammaE_temp, 0, gammaE.length);
    double[] rhoE_temp=new double[rhoE.length];
    System.arraycopy(rhoE, 0, rhoE_temp, 0, rhoE.length);
    double betaS_temp=betaS;
    double alphaS_temp=alphaS;
    double gammaS_temp=gammaS;
    double rhoS_temp=rhoS;
 
    episodic=new EpisodicModelcc(); 
    this.seq_ptr=0;
     for (int i=0;i<numFieldE_temp;i++) {
          inputE[i] = new double [lenE[i]];
     }
     episodic.initEventPara(numFieldE_temp, lenE_temp, betaE_temp, alphaE_temp, gammaE_temp, rhoE_temp);
     episodic.initSeqPara(betaS_temp, alphaS_temp, gammaS_temp, rhoS_temp);
     
     if(events.isEmpty()){
         System.out.println("ERROR: EMPTY EVENT SET!!");
         return;
     }
     
    /* int curSeqLen=0;
     for(int i=0;i<events.size();i++){
         Event e=events.get(i);
         curSeqLen++;
         if(e.isForcedTer(e)){
             episodic.newTermination(e.toArray());
             System.out.println("Sequence length identified: " + curSeqLen);
             curSeqLen=0;
         }
         else{
             episodic.newEvent(e.toArray());
         }
     }*/
     
     this.initTraining();
     
     int len1=this.jComboBox1.getItemCount()-1;
     
     for(int i=1;i<=len1;i++){
         this.jComboBox1.removeItemAt(jComboBox1.getItemCount()-1);
     }


    
    for(int i=0;i<episodic.seqLearner.numCode;i++){
            this.jComboBox1.addItem(new Integer(i));
    }
     
     episodic.saveEpisodiceModel(this.eventFile, this.seqFile);
     
}
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PlayBackPanel2().setVisible(true);
            }
        });
    }
    
    public boolean containsIdentical(Vector<Event> eList){
        boolean contains=false;
        
        for(int i=0;i<eList.size();i++){
            for(int j=i+1;j<eList.size();j++){
                if(eList.get(i).isEqual(eList.get(j))){
                    System.out.println("Identitical Item="+i+", "+j);
                    return true;
                }    
            }
        }
        
        return contains;
    }
    
    //*****code modified or added by WWW after 02/12/09*****
    
    public void testingPartialSeqScenario3(){
    System.out.println("=======================");
    System.out.println("ONE SEQUENCE LEARNT");
    System.out.println("=======================");
    
    System.out.println("Seq_ptr="+this.seq_ptr);
    
     int numTrials = seqStartIndexes.size()-1;
    int sumSuccess = 0;
    int len=this.partialCueLength;
    boolean fromLast=this.partialCueBackwards;
    
        curSeq.removeAllElements();
        curPSeq.removeAllElements();
        
        int selidx = ((Integer)seqStartIndexes.get(this.seq_ptr)).intValue();
        int epiLen = ((Integer)seqLenIndexes.get(this.seq_ptr)).intValue();
        if(len>epiLen)
            len = epiLen;
        int sidx = 0;
        if(fromLast){
            sidx = epiLen-len;
            if(sidx<0)
                sidx = 0;
            
        }
        System.out.println("IDXs: " + selidx + " " + len + " " + sidx);
        
        if(selidx==0){
            this.isNewStart=true;
        }
        else if(events.get(selidx-1).isForcedTer(events.get(selidx-1))){
            this.isNewStart=true;
        }
        else{
            this.isNewStart=false;
        }
        
        //get CurrentSeq list
        for(int i=0;i<epiLen;i++){
            this.curSeq.add(this.events.get(selidx+i));
        }
        
        int winidx = this.seq_ptr;
        if(winidx==this.seq_ptr){
            sumSuccess++;
            
        } 
        
        if(winidx>=0){
            System.out.println("play a sequence");
            curPSeq=episodic.getSeqEvents(episodic.seqLearner.getOneSeq(winidx));
        }
        
        this.jLabel10.setText("PREDICTION ACCURACY "+"("+selidx+"~"+(selidx+epiLen-1)+")"+"/("+0+"~"+(events.size()-1)+")");
        //curStory=getStoryString(curSeq);
        curPStory=getStoryString(curPSeq);
        timer.start();
        this.jLabel9.setText("Retrieved Episode: ("+winidx+"/"+(this.episodic.seqLearner.numCode-1)+")");
        
    
    /*
    for(;eventIndex<events.size();eventIndex++){
        Event e=events.get(eventIndex);
        curSeq.add(e);
        int[] learntSeq=null;
        int curSeqLength=curSeq.size();
        int startIndex=curSeqLength/3;
        if(e.isForcedTer(e)){  
            System.out.println("Sequence Termimated by Battle Consequences");
            System.out.println("Sequence Length="+(eventIndex-curSeq.size()+1)+"~"+eventIndex);
            System.out.println("Contain Identicial Events?"+this.containsIdentical(curSeq));
            
            this.jLabel10.setText("PREDICTION ACCURACY "+"("+(eventIndex-curSeq.size()+1)+"~"+eventIndex+")"+"/("+0+"~"+events.size()+")");
            
            if(curSeqLength<3){
                startIndex=1;
            }
            System.out.println("Testing Partial Sequence Length="+startIndex);
            boolean succeed=true;
            for(int i=0;i<startIndex;i++){
                if(!episodic.newTrainEvent(curSeq.get(i).toArray())){
                    succeed=false;
                }
             }
            
            if(!succeed){
                learntSeq=null;
            }
            else{
                learntSeq=episodic.seqLearner.oneTrainIn();
                
                int index=startIndex;
                while(learntSeq!=null&&learntSeq[0]==-1&&index<curSeqLength){
                    if(!episodic.newTrainEvent(curSeq.get(index).toArray())){
                        learntSeq=null;
                        break;
                    }    
                    learntSeq=episodic.seqLearner.oneTrainIn();
                    index++;
                }
                
                if(learntSeq!=null&&learntSeq[0]==-1){
                    learntSeq=null;
                }
            }
            
            episodic.seqLearner.emptyAccumulated();
             
            if(learntSeq==null){
                System.out.println("Result: Sequence cant be retrieved");
                for(int j=0;j<curSeq.size();j++){
                    pEvents.add(new Event());
                    pEvents.lastElement().setDefault();
                }
                System.out.println("pEvents.length="+pEvents.size());
                
                //this.jTextArea1.setText(getSeqString(curSeq));
                //this.jTextArea2.setText(getSeqString(curPSeq));
                curStory=getStoryString(curSeq);
                curPStory=getStoryString(curPSeq);
                timer.start();
                System.out.println("Learnt Sequence Length="+curPSeq.size());
                System.out.println("Lerant Sequence Length="+(pEvents.size()-curSeq.size())+"~"+(pEvents.size()-1));
                this.jLabel11.setText(""+getPrecisionPerSeq());
                this.curPSeq.removeAllElements();
                this.curSeq.removeAllElements();                
                eventIndex++;
                
                break;
            }
            
             if(curSeq.size()==learntSeq.length){
                System.out.println("Result: Sequence Learnt with Identicial Length");
                curPSeq=episodic.getSeqEvents(learntSeq);
                for(int j=0;j<curPSeq.size();j++){
                    pEvents.add(curPSeq.get(j));
                }
                System.out.println("pEvents.length="+pEvents.size());
                System.out.println("Lerant Sequence Length="+(pEvents.size()-curSeq.size())+"~"+(pEvents.size()-1));
            }
            else if(curSeq.size()>learntSeq.length){
                System.out.println("Result: Sequence Learnt with Shorter Length");
                curPSeq=episodic.getSeqEvents(learntSeq);
                for(int j=0;j<curPSeq.size();j++){
                    pEvents.add(curPSeq.get(j));
                }
                System.out.println("Learnt Sequence Length="+curPSeq.size());
                for(int j=0;j<curSeq.size()-learntSeq.length;j++){
                    pEvents.add(new Event());
                    pEvents.lastElement().setDefault();
                }
                System.out.println("pEvents.length="+pEvents.size());
                System.out.println("Lerant Sequence Length="+(pEvents.size()-curSeq.size())+"~"+(pEvents.size()-1));
            }
            else{
                System.out.println("Result: Sequence Learnt with Longer Length");
                System.out.println("Learnt Sequence Length="+curPSeq.size());
                curPSeq=episodic.getSeqEvents(learntSeq);
                for(int j=0;j<curSeq.size();j++){
                    pEvents.add(curPSeq.get(j));
                }
                System.out.println("pEvents.length="+pEvents.size());
                System.out.println("Lerant Sequence Length="+(pEvents.size()-curSeq.size())+"~"+(pEvents.size()-1));
            }
            
            //this.jTextArea1.setText(getSeqString(curSeq));
            //this.jTextArea2.setText(getSeqString(curPSeq));
            curStory=getStoryString(curSeq);
            curPStory=getStoryString(curPSeq);
            timer.start();
            this.jLabel11.setText(""+getPrecisionPerSeq());
            this.curPSeq.removeAllElements();
            this.curSeq.removeAllElements();
            eventIndex++;
            break;
        }
    }
    
    if(eventIndex>=events.size()){
        this.jLabel10.setText("PREDICTION ACCURACY (0~"+events.size()+")");
        this.jLabel11.setText(""+this.getPrecision());
    }   
    */
}
    
    
    protected void oneMoreStoryChar(){
        if(curStory.isEmpty()&&curPStory.isEmpty()){
            timer.stop();
            storyDiscription="";
            pStoryDiscription="";
            storyCharIndex=0;
            pStoryCharIndex=0;
            return;
        }
        
        if(!curStory.isEmpty()){
            if(storyCharIndex<curStory.firstElement().firstElement().length()){
                storyDiscription+=curStory.firstElement().firstElement().charAt(storyCharIndex);
                storyCharIndex++;
            }
            else{
                curStory.firstElement().remove(0);
                storyCharIndex=0;
            }    
            if(curStory.firstElement().isEmpty()){
                curStory.remove(0);
                storyCharIndex=0;
            }
        }
        
        if(!curPStory.isEmpty()){
            if(pStoryCharIndex<curPStory.firstElement().firstElement().length()){
                pStoryDiscription+=curPStory.firstElement().firstElement().charAt(pStoryCharIndex);
                pStoryCharIndex++;
            }
            else{
                curPStory.firstElement().remove(0);
                pStoryCharIndex=0;
            }
            if(curPStory.firstElement().isEmpty()){
                curPStory.remove(0);
                pStoryCharIndex=0;
            }
        }
        
        this.jTextArea1.setText(storyDiscription+"_");
        this.jTextArea2.setText(pStoryDiscription+"_");
    }
    
    protected void oneMoreStoryLine(){
        if(curStory.isEmpty()&&curPStory.isEmpty()){
            timer.stop();
            storyDiscription="";
            pStoryDiscription="";
            storyCharIndex=0;
            pStoryCharIndex=0;
            return;
        }
        
        if(!curStory.isEmpty()){
            if(!curStory.firstElement().isEmpty()){
                storyDiscription+=curStory.firstElement().firstElement();
                curStory.firstElement().remove(0);
            }
               
            if(curStory.firstElement().isEmpty()){
                curStory.remove(0);
            }
        }
        
        if(!curPStory.isEmpty()){
            if(!curPStory.firstElement().isEmpty()){
                pStoryDiscription+=curPStory.firstElement().firstElement();
                curPStory.firstElement().remove(0);
            }
                
            if(curPStory.firstElement().isEmpty()){
                curPStory.remove(0);
            }
        }
        
        this.jTextArea1.setText(storyDiscription+"_");
        this.jTextArea2.setText(pStoryDiscription+"_");
    }
    
    
    protected void decay(){
        for(int i=0;i<episodic.seqLearner.curSeqWeight.size();i++){
                double decayedValue=episodic.seqLearner.curSeqWeight.get(i).doubleValue()*(1-episodic.seqLearner.v);
                episodic.seqLearner.curSeqWeight.remove(i);
                episodic.seqLearner.curSeqWeight.add(i, new Double(decayedValue));
        }
    }
    
     protected void oneTestPlayback() {

        curSeq.removeAllElements();
        curPSeq.removeAllElements();

        int selidx = ((Integer) seqStartIndexes.get(this.seq_ptr)).intValue();
        int epiLen = ((Integer) seqLenIndexes.get(this.seq_ptr)).intValue();
        int sidx = 0;

        System.out.println("IDXs: " + selidx + " " + epiLen + " " + sidx);

        if (selidx == 0) {
            this.isNewStart = true;
        } else if (events.get(selidx - 1).isForcedTer(events.get(selidx - 1))) {
            this.isNewStart = true;
        } else {
            this.isNewStart = false;
        }

        //get CurrentSeq list
        for (int i = 0; i < epiLen; i++) {
            this.curSeq.add(this.events.get(selidx + i));
        }

        int winidx = this.seq_ptr;

        if (winidx >= 0) {
            System.out.println("play a sequence");
            curPSeq = episodic.getSeqEvents(episodic.seqLearner.getOneSeq(((Integer) seqWinIndexes.get(winidx)).intValue()));
        }

        this.jLabel10.setText("PREDICTION ACCURACY " + "(" + selidx + "~" + (selidx + epiLen - 1) + ")" + "/(" + 0 + "~" + (events.size() - 1) + ")");
        //curStory=getStoryString(curSeq);
        curPStory = getStoryString(curPSeq);
        this.jLabel9.setText("Retrieved Episode: (" + winidx + "/" + (this.episodic.seqLearner.numCode - 1) + ")");
        this.jLabel11.setText("");

        timer.start();
    }

    protected void oneTestRecall() {
        System.out.println("Seq_ptr=" + this.seq_ptr);

        curSeq.removeAllElements();
        curPSeq.removeAllElements();

        int selidx = ((Integer) seqStartIndexes.get(this.seq_ptr)).intValue();
        int epiLen = ((Integer) seqLenIndexes.get(this.seq_ptr)).intValue();
        int curLen = epiLen / cueLen;
        //int curLen=epiLen/4;
        if (curLen <= 0) {
            curLen = epiLen;
        }
        int sidx = 0;

        if (this.cueLoc == this.endCue) {
            sidx = epiLen - curLen;
            if (sidx < 0) {
                sidx = 0;
            }
        }

        if (this.cueLoc == this.anyCue) {
            sidx = (int) ((epiLen - curLen) * Math.random());
            if (sidx < 0) {
                sidx = 0;
            }
        }

        System.out.println("index: " + selidx + " fullLen: " + epiLen + " partial: " + curLen + " startPos: " + sidx);
        int winidx = seqRecognition(selidx, curLen, sidx, epiLen);

            if (selidx + sidx == 0) {
        this.isNewStart = true;
    } else if (events.get(selidx + sidx - 1).isForcedTer(events.get(selidx+ sidx - 1))) {
        this.isNewStart = true;
    } else {
        this.isNewStart = false;
    }

        //get CurrentSeq list
        for (int i = selidx + sidx; i < selidx + sidx + curLen; i++) {
            this.curSeq.add(this.events.get(i));
        }


        if (winidx >= 0) {
            System.out.println("play a sequence");
            curPSeq = episodic.getSeqEvents(episodic.seqLearner.getOneSeq(((Integer) seqWinIndexes.get(winidx)).intValue()));
        }

        this.jLabel10.setText("RETRIEVE ACCURACY " + "(" + selidx + "~" + (selidx + epiLen - 1) + ")" + "/(" + 0 + "~" + (events.size() - 1) + ")");
        curStory = getStoryString(curSeq);
        curPStory = getStoryString(curPSeq);
        this.jLabel9.setText("Retrieved Episode: (" + winidx + "/" + (this.episodic.seqLearner.numCode - 1) + ")");

        timer.start();
    }

    protected void oneTestImperfect() {
        System.out.println("Seq_ptr=" + this.seq_ptr);

        curSeq.removeAllElements();
        curPSeq.removeAllElements();

        int selidx = ((Integer) seqStartIndexes.get(this.seq_ptr)).intValue();
        int epiLen = ((Integer) seqLenIndexes.get(this.seq_ptr)).intValue();
        int curLen = epiLen;

        int sidx = 0;


        System.out.println("index: " + selidx + " fullLen: " + epiLen + " partial: " + curLen + " startPos: " + sidx);
        int winidx = seqRecognition(selidx, curLen, sidx, epiLen);

        if (selidx == 0) {
            this.isNewStart = true;
        } else if (events.get(selidx - 1).isForcedTer(events.get(selidx - 1))) {
            this.isNewStart = true;
        } else {
            this.isNewStart = false;
        }

        //get CurrentSeq list
        for (int i = 0; i < curLen; i++) {
            this.curSeq.add(this.events.get(selidx + i));
        }


        if (winidx >= 0) {
            System.out.println("play a sequence");
            curPSeq = episodic.getSeqEvents(episodic.seqLearner.getOneSeq(((Integer) seqWinIndexes.get(winidx)).intValue()));
        }

        this.jLabel10.setText("RETRIEVE ACCURACY " + "(" + selidx + "~" + (selidx + epiLen - 1) + ")" + "/(" + 0 + "~" + (events.size() - 1) + ")");
        curStory = getStoryString(curSeq);
        curPStory = getStoryString(curPSeq);
        this.jLabel9.setText("Retrieved Episode: (" + winidx + "/" + (this.episodic.seqLearner.numCode - 1) + ")");

        timer.start();
    }
    
    protected void initCISM() {
            CISM = new FusionART();
            for (int i=0;i<numFieldSM;i++) {
                inputSM[i] = new double [lenSM[i]];
            }
            CISM.initParameters(numFieldSM, lenSM, betaSM, alphaSM, gammaSM, rhoSM);
    }
    
    protected void initWeaponSM() {
            WeaponSM = new FusionART();
            for (int i=0;i<numFieldWeaponSM;i++) {
                inputWeaponSM[i] = new double [lenWeaponSM[i]];
            }
            WeaponSM.initParameters(numFieldWeaponSM, lenWeaponSM, betaWeaponSM, alphaWeaponSM, gammaWeaponSM, rhoWeaponSM);
    }
    
    protected void initCRSM() {
            CRSM = new FusionART();            
          
            for (int i=0;i<numFieldCRSM;i++) {
                inputCRSM[i] = new double [lenCRSM[i]];
            }
            CRSM.initParameters(numFieldCRSM, lenCRSM, betaCRSM, alphaCRSM, gammaCRSM, rhoCRSM);
    }
    
    protected void transferToSM(){
        int num=0;
        for(int i=0;i<this.episodic.seqLearner.numCode;i++){
            System.out.println("EP "+i+" is writing...");
            Vector<EventPattern> eList = episodic.getSeqEventPatterns(episodic.seqLearner.getOneSeq(i));
            for(int j=0;j<eList.size();j++){
                EventPattern e=eList.get(j);
                
                double[][] inPattern=new double[numFieldSM][];
                for (int k=0;k<numFieldSM;k++) {
                    inPattern[k] = new double [lenSM[k]];
                }
                
                
                //int locIndex=getLocIndex(e);
                System.arraycopy(resetStatus(inPattern[1]), 0, inPattern[1], 0, inPattern[1].length);
                //System.arraycopy(setLoc(inPattern[0],locIndex), 0, inPattern[0], 0, inPattern[0].length);
                System.arraycopy((e.toArray())[0], 0, inPattern[0], 0, inPattern[0].length);
                
                if(e.pickupHealS==1.0){
                    num++;
                    System.out.print("1 Event: "+e.x+","+e.x_compl+", "+e.y+", "+e.y_compl+", "+e.z+e.z_compl+", "+"\t");
                    //System.out.print("Area code index: "+locIndex+"\t");
                    System.out.println("Get medical kit small");
                    
                    inPattern[1][0]=1;
                    inPattern[1][1]=0;
                    
                    int winner=CISM.findWinner(inPattern, CISM.FUZZYART);
                    CISM.doLearn(winner, CISM.FUZZYART, 0.5);
                    
                    System.arraycopy(resetStatus(inPattern[1]), 0, inPattern[1], 0, inPattern[1].length);
                }
                
                if(e.pickupHealL==1.0){
                    num++;
                    System.out.print("1 Event: "+e.x+","+e.x_compl+", "+e.y+", "+e.y_compl+", "+e.z+e.z_compl+", "+"\t");
                    //System.out.print("Area code index: "+locIndex+"\t");
                    System.out.println("Get medical kit large");
                    
                    inPattern[1][2]=1;
                    inPattern[1][3]=0;
                    
                    int winner=CISM.findWinner(inPattern, CISM.FUZZYART);
                    CISM.doLearn(winner, CISM.FUZZYART,0.5);
                    
                    System.arraycopy(resetStatus(inPattern[1]), 0, inPattern[1], 0, inPattern[1].length);
                }
                
                if(e.pickupWeapon1==1.0){
                    num++;
                    System.out.print("1 Event: "+e.x+","+e.x_compl+", "+e.y+", "+e.y_compl+", "+e.z+e.z_compl+", "+"\t");
                    //System.out.print("Area code index: "+locIndex+"\t");
                    System.out.println("Get medical "+this.allWeapons[0]);
                    
                    inPattern[1][4]=1;
                    inPattern[1][5]=0;
                    
                    int winner=CISM.findWinner(inPattern, CISM.FUZZYART);
                    CISM.doLearn(winner, CISM.FUZZYART, 0.5);
                    
                    System.arraycopy(resetStatus(inPattern[1]), 0, inPattern[1], 0, inPattern[1].length);
                }
                
                if(e.pickupWeapon2==1.0){
                    num++;
                    System.out.print("1 Event: "+e.x+","+e.x_compl+", "+e.y+", "+e.y_compl+", "+e.z+e.z_compl+", "+"\t");
                    //System.out.print("Area code index: "+locIndex+"\t");
                    System.out.println("Get medical "+this.allWeapons[1]);
                    
                    inPattern[1][6]=1;
                    inPattern[1][7]=0;
                    
                    int winner=CISM.findWinner(inPattern, CISM.FUZZYART);
                    CISM.doLearn(winner, CISM.FUZZYART,0.5);
                    
                    System.arraycopy(resetStatus(inPattern[1]), 0, inPattern[1], 0, inPattern[1].length);
                }
                
                if(e.pickupWeapon3==1.0){
                    num++;
                    System.out.print("1 Event: "+e.x+","+e.x_compl+", "+e.y+", "+e.y_compl+", "+e.z+e.z_compl+", "+"\t");
                    //System.out.print("Area code index: "+locIndex+"\t");
                    System.out.println("Get medical "+this.allWeapons[2]);
                    
                    inPattern[1][8]=1;
                    inPattern[1][9]=0;
                    
                    int winner=CISM.findWinner(inPattern, CISM.FUZZYART);
                    CISM.doLearn(winner, CISM.FUZZYART, 0.5);
                    
                    System.arraycopy(resetStatus(inPattern[1]), 0, inPattern[1], 0, inPattern[1].length);
                }
                
                if(e.pickupWeapon4==1.0){
                    num++;
                    System.out.print("1 Event: "+e.x+","+e.x_compl+", "+e.y+", "+e.y_compl+", "+e.z+e.z_compl+", "+"\t");
                    //System.out.print("Area code index: "+locIndex+"\t");
                    System.out.println("Get medical "+this.allWeapons[3]);
                    
                    inPattern[1][10]=1;
                    inPattern[1][11]=0;
                    
                    int winner=CISM.findWinner(inPattern, CISM.FUZZYART);
                    CISM.doLearn(winner, CISM.FUZZYART,0.5);
                    
                    System.arraycopy(resetStatus(inPattern[1]), 0, inPattern[1], 0, inPattern[1].length);
                }
                
                if(e.pickupWeapon5==1.0){
                    num++;
                    System.out.print("1 Event: "+e.x+","+e.x_compl+", "+e.y+", "+e.y_compl+", "+e.z+e.z_compl+", "+"\t");
                    //System.out.print("Area code index: "+locIndex+"\t");
                    System.out.println("Get medical "+this.allWeapons[4]);
                    
                    inPattern[1][12]=1;
                    inPattern[1][13]=0;
                    
                    int winner=CISM.findWinner(inPattern, CISM.FUZZYART);
                    CISM.doLearn(winner, CISM.FUZZYART,0.5);
                    
                    System.arraycopy(resetStatus(inPattern[1]), 0, inPattern[1], 0, inPattern[1].length);
                }
                
                if(e.pickupWeapon6==1.0){
                    num++;
                    System.out.print("1 Event: "+e.x+","+e.x_compl+", "+e.y+", "+e.y_compl+", "+e.z+e.z_compl+", "+"\t");
                    //System.out.print("Area code index: "+locIndex+"\t");
                    System.out.println("Get medical "+this.allWeapons[5]);
                    
                    inPattern[1][14]=1;
                    inPattern[1][15]=0;
                    
                    int winner=CISM.findWinner(inPattern, CISM.FUZZYART);
                    CISM.doLearn(winner, CISM.FUZZYART,0.5);
                    
                    System.arraycopy(resetStatus(inPattern[1]), 0, inPattern[1], 0, inPattern[1].length);
                }
                
                if(e.pickupWeapon7==1.0){
                    num++;
                    System.out.print("1 Event: "+e.x+","+e.x_compl+", "+e.y+", "+e.y_compl+", "+e.z+e.z_compl+", "+"\t");
                    //System.out.print("Area code index: "+locIndex+"\t");
                    System.out.println("Get medical "+this.allWeapons[6]);
                    
                    inPattern[1][16]=1;
                    inPattern[1][17]=0;
                    
                    int winner=CISM.findWinner(inPattern, CISM.FUZZYART);
                    CISM.doLearn(winner, CISM.FUZZYART,0.5);
                    
                    System.arraycopy(resetStatus(inPattern[1]), 0, inPattern[1], 0, inPattern[1].length);
                }
                
                if(e.pickupWeapon8==1.0){
                    num++;
                    System.out.print("1 Event: "+e.x+","+e.x_compl+", "+e.y+", "+e.y_compl+", "+e.z+e.z_compl+", "+"\t");
                    //System.out.print("Area code index: "+locIndex+"\t");
                    System.out.println("Get medical "+this.allWeapons[7]);
                    
                    inPattern[1][18]=1;
                    inPattern[1][19]=0;
                    
                    int winner=CISM.findWinner(inPattern, CISM.FUZZYART);
                    CISM.doLearn(winner, CISM.FUZZYART,0.5);
                    
                    System.arraycopy(resetStatus(inPattern[1]), 0, inPattern[1], 0, inPattern[1].length);
                }
                
                if(e.pickupWeapon9==1.0){
                    num++;
                    System.out.print("1 Event: "+e.x+","+e.x_compl+", "+e.y+", "+e.y_compl+", "+e.z+e.z_compl+", "+"\t");
                   // System.out.print("Area code index: "+locIndex+"\t");
                    System.out.println("Get medical "+this.allWeapons[8]);
                    
                    inPattern[1][20]=1;
                    inPattern[1][21]=0;
                    
                    int winner=CISM.findWinner(inPattern, CISM.FUZZYART);
                    CISM.doLearn(winner, CISM.FUZZYART,0.5);
                    
                    System.arraycopy(resetStatus(inPattern[1]), 0, inPattern[1], 0, inPattern[1].length);
                }
                
                if(e.pickupWeapon10==1.0){
                    num++;
                    System.out.print("1 Event: "+e.x+","+e.x_compl+", "+e.y+", "+e.y_compl+", "+e.z+e.z_compl+", "+"\t");
                    //System.out.print("Area code index: "+locIndex+"\t");
                    System.out.println("Get medical "+this.allWeapons[9]);
                    
                    inPattern[1][22]=1;
                    inPattern[1][23]=0;
                    
                    int winner=CISM.findWinner(inPattern, CISM.FUZZYART);
                    CISM.doLearn(winner, CISM.FUZZYART,0.5);
                    
                    System.arraycopy(resetStatus(inPattern[1]), 0, inPattern[1], 0, inPattern[1].length);
                }
            }
        }
        
        try{
            System.out.println("!!!!!!!!!!"+num);
            CISM.saveCode(new FileWriter(filePath+fileSM));
        }
        catch(Exception ex){
            System.out.println("ERR@ SAVE CISM@!!!!!!!!");
        }
    }
    
    protected void transferToWeaponSM(){
        int eNum=0;
        
        for(int i=0;i<this.episodic.seqLearner.numCode;i++){
            Vector<EventPattern> eList = episodic.getSeqEventPatterns(episodic.seqLearner.getOneSeq(i));
            //int shotNum=0;
            //int curWeapon=-1;
            //int prevWeapon=-1;
            
           // for(int j=eList.size()-1;j>=0;j--){          
              for(int j=0;j<eList.size();j++){
                    EventPattern e=eList.get(j);
                    
                    if(e.reward>0.5&&e.reward_compl<0.5){
                        eNum++;
                        System.out.println("Shooting event #"+eNum);
                    double[][] inPattern=new double[numFieldWeaponSM][];
                    for (int k=0;k<numFieldWeaponSM;k++) {
                         inPattern[k] = new double [lenWeaponSM[k]];
                         System.arraycopy(resetStatus(inPattern[k]), 0, inPattern[k], 0, inPattern[k].length);
                    }
                
                    if(e.curWeapon1==1.0){                    
                        inPattern[0][0]=1;
                        inPattern[0][1]=0;
                        System.out.println("1");
                    }
                    
                    if(e.curWeapon2==1.0){                    
                        inPattern[0][2]=1;
                        inPattern[0][3]=0;
                        System.out.println("2");
                    }
                    
                    if(e.curWeapon3==1.0){                    
                        inPattern[0][4]=1;
                        inPattern[0][5]=0;
                        System.out.println("3");
                    }
                    
                    if(e.curWeapon4==1.0){                    
                        inPattern[0][6]=1;
                        inPattern[0][7]=0;
                        System.out.println("4");
                    }
                    
                    if(e.curWeapon5==1.0){                    
                        inPattern[0][8]=1;
                        inPattern[0][9]=0;
                        System.out.println("5");
                    }
                    
                    if(e.curWeapon6==1.0){                    
                        inPattern[0][10]=1;
                        inPattern[0][11]=0;
                        System.out.println("6");
                    }
                    
                    if(e.curWeapon7==1.0){                    
                        inPattern[0][12]=1;
                        inPattern[0][13]=0;
                        System.out.println("7");
                    }
                    
                    if(e.curWeapon8==1.0){                    
                        inPattern[0][14]=1;
                        inPattern[0][15]=0;
                        System.out.println("8");
                    }
                    
                    if(e.curWeapon9==1.0){                    
                        inPattern[0][16]=1;
                        inPattern[0][17]=0;
                        System.out.println("9");
                    }
                    
                    if(e.curWeapon10==1.0){                    
                        inPattern[0][18]=1;
                        inPattern[0][19]=0;
                        System.out.println("10");
                    }
                    
                    inPattern[1][0]=e.emeDistance;
                    inPattern[1][1]=e.emeDistance_compl;
                    
                    inPattern[2][0]=(e.reward-0.5)*2;
                    inPattern[2][1]=e.reward_compl;
                    
                    int winner=WeaponSM.findWinner(inPattern, WeaponSM.FUZZYART);
                    WeaponSM.doLearn(winner, WeaponSM.FUZZYART,0.5);

                }
            }
        }
        
        System.out.println("!!!!!"+eNum);
        
        try{
            WeaponSM.saveCode(new FileWriter(filePath+fileWeaponSM));
        }
        catch(Exception ex){
            System.out.println("ERR@ SAVE WeaponSM@!!!!!!!!");
        }
    }
    
    private void sortSequence(Vector<Event> seq){
        for(int i=0;i<seq.size();i++){
            for(int j=i;j<seq.size();j++){
                if(seq.get(i).reward<seq.get(j).reward){
                    Event temp=seq.get(i);
                    seq.set(i, seq.get(j));
                    seq.set(j, temp);      
                    i=j;
                }
            }    
        }
    }
    
    protected void transferToCRSM(){
        for(int i=0;i<this.episodic.seqLearner.numCode;i++){
            Vector<EventPattern> eList = episodic.getSeqEventPatterns(episodic.seqLearner.getOneSeq(i));
            System.out.println("transfer sequence "+i);
            
            for(int j=0;j<eList.size()-1-winCRSM;j++){
                EventPattern pPattern=eList.get(j);
                EventPattern nPattern=eList.get(j+winCRSM);
                
                double[][] inPattern=new double[this.numFieldCRSM][];
                for(int k=0;k<this.numFieldCRSM;k++){
                    inPattern[k]=new double[this.lenCRSM[k]];
                    System.arraycopy(resetStatus(inPattern[k]), 0, inPattern[k], 0, inPattern[k].length);
                }
                
                double[][] nArr=nPattern.toArray();
                double[][] pArr=pPattern.toArray();
                //System.arraycopy(nArr[0], 0, inPattern[0], 0, inPattern[0].length);
                //System.arraycopy(nArr[1], 0, inPattern[1], 0, inPattern[1].length);
               // System.arraycopy(nArr[2], 0, inPattern[2], 0, inPattern[2].length);
                //System.arraycopy(nArr[3], 0, inPattern[3], 0, inPattern[3].length);
                //System.arraycopy(pArr[0], 0, inPattern[4], 0, inPattern[4].length);
                //System.arraycopy(pArr[1], 0, inPattern[5], 0, inPattern[5].length);
                //System.arraycopy(pArr[2], 0, inPattern[6], 0, inPattern[6].length);
                //System.arraycopy(pArr[3], 0, inPattern[7], 0, inPattern[7].length);
                System.arraycopy(nArr[0], 0, inPattern[0], 0, nArr[0].length);
                System.arraycopy(nArr[1], 0, inPattern[0], nArr[0].length, nArr[1].length);
                System.arraycopy(nArr[2], 0, inPattern[0], nArr[0].length+nArr[1].length, nArr[2].length);
                System.arraycopy(nArr[3], 0, inPattern[0], nArr[0].length+nArr[1].length+nArr[2].length, nArr[3].length);
                System.arraycopy(pArr[0], 0, inPattern[1], 0, pArr[0].length);
                System.arraycopy(pArr[1], 0, inPattern[1], pArr[0].length, pArr[1].length);
                System.arraycopy(pArr[2], 0, inPattern[1], pArr[0].length+pArr[1].length, pArr[2].length);
                System.arraycopy(pArr[3], 0, inPattern[1], pArr[0].length+pArr[1].length+pArr[2].length, pArr[3].length);
                    
                 int winner=CRSM.findWinner(inPattern, CRSM.FUZZYART);                   
                 CRSM.doLearn(winner, CRSM.FUZZYART,0.5);
            }
                
        }
        
        try{
            CRSM.saveCode(new FileWriter(filePath+fileCRSM));
        }
        catch(Exception ex){
            System.out.println("ERR@ SAVE CRSM@!!!!!!!!");
        }
  }
    
    private double[] resetStatus(double[] arr){
        for(int j=0;j<arr.length;j++){
            if(j%2==0){
                arr[j]=0;
            }
            else{
                arr[j]=1;
            }
        }
        
        return arr;
    }
    
    private double[] setLoc(double[] arr, int locIndex){
        for(int j=0;j<arr.length;j++){
            if(j%2==0){
                arr[j]=0;
            }
            else{
                arr[j]=1;
            }
        }
        
        arr[locIndex*2]=1;
        arr[locIndex*2+1]=0;
        
        return arr;
    }
    
    private int getLocIndex(Event e){
        double xGrid=1.0/((double)(Event.divX));
        int xIndex=(int)(e.x/xGrid+0.01);
        double yGrid=1.0/((double)(Event.divY));
        int yIndex=(int)(e.y/yGrid+0.01);
        double zGrid=1.0/((double)(Event.divZ));
        int zIndex=(int)(e.z/zGrid+0.01);
        
        //System.out.println("xIndex="+xIndex+"yIndex="+yIndex+"zIndex="+zIndex);
        //System.out.println("xGrid="+xGrid+"yGrid="+yGrid+"zGrid="+zGrid);
        
        return zIndex+yIndex*Event.divZ+xIndex*Event.divZ*Event.divY;
    }
    
    
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    // End of variables declaration//GEN-END:variables

}
