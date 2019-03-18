package com.dinisvcosta.viegasgiftcards;

import com.dinisvcosta.viegasgiftcards.repository.DBConnection;
import com.dinisvcosta.viegasgiftcards.ui.Client;

public class ViegasGiftCards
{
    public static void main(String[] args)
    {
        DBConnection.getInstance();

        Client client = new Client();
        client.createAndShowGUI();
    }
}
