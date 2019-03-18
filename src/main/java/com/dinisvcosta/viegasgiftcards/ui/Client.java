package com.dinisvcosta.viegasgiftcards.ui;

import java.awt.Desktop;
import java.io.File;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import com.dinisvcosta.viegasgiftcards.repository.code.CodeRepository;
import com.dinisvcosta.viegasgiftcards.enums.UseCodeResult;

public class Client
{
    //Creates the window to draw on
    private JFrame frame = new JFrame("ViegasGiftCards");
    private CodeRepository codeRepository = new CodeRepository();

    //sets the window size, the icon image and calls the main window drawing function
    public void createAndShowGUI()
    {
        //load icon image
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(new FileInputStream("src/main/resources/images/gift-flat.png"));
        }
        catch(IOException exception)
        {
            exception.printStackTrace();
        }

        //set up the window dimensions and turns resizable off
        frame.setPreferredSize(new Dimension(500, 200));
        frame.setResizable(false);

        //add icon image to window
        frame.setIconImage(image);

        //sets default close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //Load main panel
        showMainWindow();
    }

    private void showMainWindow()
    {
        final int FRAME_WIDTH = frame.getWidth();

        //create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(true);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(null);

        //"Viegas Gift Cards" title label
        JLabel titleLabel = new JLabel("Viegas Gift Cards", JLabel.CENTER);
        titleLabel.setFont(new Font("arial", Font.BOLD, 18));
        titleLabel.setSize(new Dimension(200, 60)) ;
        titleLabel.setLocation((FRAME_WIDTH/2) - (titleLabel.getWidth() / 2), 0);

        //Create "Generate Codes" button
        JButton generateCodesButton = new JButton("Generate Codes");
        generateCodesButton.setBounds(25,75,200,50);
        //add functionality to the button
        generateCodesButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                showGenerateCodesWindow();
            }
        });

        //Create "Use Codes" button
        JButton useCodes = new JButton("Use Codes");
        useCodes.setBounds(275,75,200,50);
        useCodes.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                showUseCodesWindow();
            }
        });

        //Add main title and buttons
        mainPanel.add(titleLabel);
        mainPanel.add(generateCodesButton);
        mainPanel.add(useCodes);

        frame.setContentPane(mainPanel);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private void showGenerateCodesWindow()
    {
        //Create generate codes panel
        JPanel generateCodesPanel = new JPanel();
        generateCodesPanel.setOpaque(true);
        generateCodesPanel.setBackground(Color.WHITE);
        generateCodesPanel.setLayout(null);

        //Create "Back" button
        JButton backButton = new JButton("<   Back");
        backButton.setBounds(5, 5, 90, 30);
        backButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                showMainWindow();
            }
        });

        //Create label
        JLabel numberOfCodesLabel = new JLabel("Number of 10€ codes to be created:");
        numberOfCodesLabel.setFont(new Font("arial", Font.PLAIN, 18));
        numberOfCodesLabel.setBounds(55, 50, 300, 30);

        //Create Spinner input for number of codes
        int numberOfCodes = 1;
        SpinnerNumberModel codesModel = new SpinnerNumberModel(numberOfCodes, numberOfCodes, numberOfCodes + 9, 1);
        final JSpinner numberOfCodesSpinner = new JSpinner(codesModel);
        numberOfCodesSpinner.setBounds(350, 50, 50, 30);

        //make JSpinner not editable with keyboard input
        JFormattedTextField tf = ((JSpinner.DefaultEditor) numberOfCodesSpinner.getEditor()).getTextField();
        tf.setEditable(false);
        tf.setBackground(Color.WHITE);

        //Create "Generate" button
        JButton generateButton = new JButton("Generate");
        generateButton.setBounds(145, 110, 200, 50);
        generateButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int spinnerValue = (Integer) numberOfCodesSpinner.getValue();
                JOptionPane confirmPane = new JOptionPane(
                        "Are you sure you want to create " + spinnerValue + " codes for a total of " + spinnerValue * 10 + "€?",
                        JOptionPane.QUESTION_MESSAGE,
                        JOptionPane.YES_NO_OPTION);
                JDialog dialog = confirmPane.createDialog(frame, "Confirmation");
                dialog.setVisible(true);
                Object selectedValue = confirmPane.getValue();

                //deal with selected option
                if(selectedValue == null || (Integer) selectedValue == 1)
                {
                    System.out.println("User closed or clicked no");
                }
                else if((Integer) selectedValue == 0)
                {
                    final String fileName = codeRepository.generateCodes(spinnerValue);

                    JButton openFileButton = new JButton("Open file");
                    openFileButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if(Desktop.isDesktopSupported())
                            {
                                try
                                {
                                    File codesFile = new File(fileName);
                                    Desktop.getDesktop().open(codesFile);
                                }
                                catch (IOException ex)
                                {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    });

                    Object[] options = {openFileButton};

                    JOptionPane infoPane = new JOptionPane(spinnerValue + " codes and respective file were successfully created.", JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, options);
                    JDialog successDialog = infoPane.createDialog(frame, "Success");
                    successDialog.setVisible(true);

                    System.out.println("User confirmed the creation of " + spinnerValue + " codes!");
                }
            }
        });

        //Add label, spinner and button to window
        generateCodesPanel.add(backButton);
        generateCodesPanel.add(numberOfCodesLabel);
        generateCodesPanel.add(numberOfCodesSpinner);
        generateCodesPanel.add(generateButton);

        frame.setContentPane(generateCodesPanel);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        //sets focus to spinner so user can use up and down arrows to change the value
        numberOfCodesSpinner.requestFocusInWindow();
    }

    private void showUseCodesWindow()
    {
        //Create use codes panel
        JPanel useCodesPanel = new JPanel();
        useCodesPanel.setOpaque(true);
        useCodesPanel.setBackground(Color.WHITE);
        useCodesPanel.setLayout(null);

        //Create "Back" button
        JButton backButton = new JButton("<   Back");
        backButton.setBounds(5, 5, 90, 30);
        backButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                showMainWindow();
            }
        });

        //Create "Insert Code" label
        JLabel insertCodesLabel = new JLabel("Insert Code:");
        insertCodesLabel.setFont(new Font("arial", Font.PLAIN, 18));
        insertCodesLabel.setSize(new Dimension(200, 50)) ;
        insertCodesLabel.setLocation((200) - (insertCodesLabel.getWidth() / 2), 50);

        //Create text field that the user uses to input the code
        final JTextField codeTextField = new JTextField();
        codeTextField.setSize(new Dimension(110, 30));
        codeTextField.setFont(new Font("arial", Font.PLAIN, 18));
        codeTextField.setLocation(215, 62);
        //limit number of characters to 9
        codeTextField.addKeyListener(new KeyAdapter()
        {
            public void keyTyped(KeyEvent e)
            {
                if(codeTextField.getText().length() >= 9)
                {
                    e.consume();
                }
            }
        });

        //Create "Use Codes" button
        JButton useCodesButton = new JButton("Use Code");
        useCodesButton.setBounds(145, 110, 200, 50);
        useCodesButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String textFieldInput = codeTextField.getText();
                System.out.println(textFieldInput);

                UseCodeResult result = codeRepository.useCode(textFieldInput);

                if(result == UseCodeResult.INVALID_CODE)
                {
                    JOptionPane infoPane = new JOptionPane("Error in code format. Codes are 9 characters long, letters and numbers only.", JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
                    JDialog dialog = infoPane.createDialog(frame, "Error");
                    dialog.setVisible(true);
                }
                else if(result == UseCodeResult.CODE_NOT_FOUND)
                {
                    JOptionPane infoPane = new JOptionPane("Code not found.", JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION);
                    JDialog dialog = infoPane.createDialog(frame, "Warning");
                    dialog.setVisible(true);
                }
                else if(result == UseCodeResult.CODE_ALREADY_USED)
                {
                    JOptionPane infoPane = new JOptionPane("Code already used.", JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION);
                    JDialog dialog = infoPane.createDialog(frame, "Warning");
                    dialog.setVisible(true);
                }
                else if(result == UseCodeResult.CODE_EXPIRED)
                {
                    JOptionPane infoPane = new JOptionPane("Code has expired.", JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION);
                    JDialog dialog = infoPane.createDialog(frame, "Warning");
                    dialog.setVisible(true);
                }
                else if(result == UseCodeResult.VALID_CODE)
                {
                    JOptionPane infoPane = new JOptionPane("Code successfully used!", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
                    JDialog dialog = infoPane.createDialog(frame, "Success");
                    dialog.setVisible(true);
                }


            }
        });

        useCodesPanel.add(backButton);
        useCodesPanel.add(insertCodesLabel);
        useCodesPanel.add(useCodesButton);
        useCodesPanel.add(codeTextField);

        frame.setContentPane(useCodesPanel);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        //sets focus to text field so user doesn't need to click it to type
        codeTextField.requestFocusInWindow();
    }
}
