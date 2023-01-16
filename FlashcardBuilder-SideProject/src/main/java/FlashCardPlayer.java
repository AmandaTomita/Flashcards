import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class FlashCardPlayer {

    private JTextArea display;
    private JTextArea answer;
    private ArrayList<FlashCard> cardList;
    private Iterator cardIterator;
    private FlashCard currectCard;
    private int currectCardIndex;
    private JButton showAnswer;
    private JFrame frame;
    private boolean isShowAnswer;


    public FlashCardPlayer() {
        //build UI
        frame = new JFrame("Flash Card Player");
        JPanel mainPanel = new JPanel();
        Font mFont = new Font("Helevetica Neue", Font.BOLD, 22);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        display = new JTextArea(10, 20);
        display.setLineWrap(true);
        display.setWrapStyleWord(true);
        display.setFont(mFont);

        JScrollPane qJScrollPane = new JScrollPane(display);
        qJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qJScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        showAnswer = new JButton("Show Answer");

        mainPanel.add(qJScrollPane);
        mainPanel.add(showAnswer);

        showAnswer.addActionListener(new NextCardListener());


        //add to frame

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(640, 500);
        frame.setVisible(true);

        //add menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenuItem = new JMenuItem("Load Card Set");
        loadMenuItem.addActionListener(new OpenMenuListener());

        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        answer = new JTextArea(6, 20);
        answer.setLineWrap(true);
        answer.setWrapStyleWord(true);
        answer.setFont(mFont);

//        JScrollPane aJScrollPane = new JScrollPane(display);
//        aJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//        aJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FlashCardPlayer();
            }
        });
    }

    class NextCardListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (isShowAnswer) {
                display.setText(currectCard.getAnswer());
                showAnswer.setText("Next Card");
                isShowAnswer = false;
            } else {
                //show next question
                if (cardIterator.hasNext()) {
                    showNextCard();
                } else {
                    //no more cards
                    display.setText("That was the last card.");
                    showAnswer.setEnabled(false);
                }


            }
        }
    }

    class OpenMenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(frame);
            loadFile(fileOpen.getSelectedFile());

        }
    }

    private void loadFile(File selectedFile) {

        cardList = new ArrayList<FlashCard>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
            String line = null;

            while ((line = reader.readLine()) != null) {
                makeCard(line);
            }

        } catch (Exception e) {
            System.out.println("Couldn't read file");
            e.printStackTrace();
        }

        //show the first card
        cardIterator = cardList.iterator();
        showNextCard();
    }

    private void showNextCard() {
        currectCard = (FlashCard) cardIterator.next();
        display.setText(currectCard.getQuestion());
        showAnswer.setText("Show Answer");
        isShowAnswer = true;
    }

    private void makeCard(String lineToParse) {
        //2 ways to parse the question from the answer
        // option 1
        String[] result = lineToParse.split("/"); //[question,answer]
        FlashCard card = new FlashCard(result[0], result[1]);
        cardList.add(card);
        System.out.println("Made a Card");

        // option 2
//        StringTokenizer result = new StringTokenizer(lineToParse, "/");
//        if(result.hasMoreTokens())  {
//            FlashCard card = new FlashCard(result.nextToken(), result.nextToken());
//            cardList.add(card);
//            System.out.println("Made a Card");

        }

    }
