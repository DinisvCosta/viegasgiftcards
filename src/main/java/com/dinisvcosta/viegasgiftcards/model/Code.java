package com.dinisvcosta.viegasgiftcards.model;

import java.util.Random;
import org.joda.time.LocalDate;

public class Code
{
    private String code;
    private String formattedCode;
    private LocalDate creationDate;
    private LocalDate expirationDate;
    private boolean isUsed;

    //public constructor that creates new code
    public Code()
    {
        //generate new 9 character random string composed of letters and numbers
        this.code = generateCode();

        //format code, inserting space every 3 characters
        this.formattedCode = formatCode(this.code);

        //creationDate
        this.creationDate = new LocalDate();

        //expirationDate calculated
        this.expirationDate = calculateExpirationDate(this.creationDate);

        this.isUsed = false;
    }

    private String generateCode()
    {
        //String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String SALTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 9) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    private String formatCode(String code)
    {
        StringBuilder form = new StringBuilder(code);
        form.insert(3, " ");
        form.insert(7, " ");
        return form.toString();
    }

    private LocalDate calculateExpirationDate(LocalDate date)
    {
        LocalDate expirationDate = date.plusMonths(6);
        return expirationDate;
    }

    //public getters
    public String getCode()
    {
        return this.code;
    }

    public String getFormattedCode()
    {
        return this.formattedCode;
    }

    public LocalDate getCreationDate()
    {
        return this.creationDate;
    }

    public LocalDate getExpirationDate()
    {
        return this.expirationDate;
    }
}
