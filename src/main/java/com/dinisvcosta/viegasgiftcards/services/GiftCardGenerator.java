package com.dinisvcosta.viegasgiftcards.services;

import com.dinisvcosta.viegasgiftcards.model.Code;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.io.FileOutputStream;
import java.io.IOException;

public class GiftCardGenerator
{
    public static String createDocument(Code[] codes)
    {
        try
        {
            Document document = new Document(PageSize.A4);

            //current local date to add to file name
            LocalDate currentLocalDate = new LocalDate();
            String currentLocalDateSting = currentLocalDate.toString();

            //DateTime to get hour, minute, seconds of creation
            DateTime currentDateTime = new DateTime();

            Integer hour = currentDateTime.getHourOfDay();
            String hourSting = hour.toString();

            Integer minute = currentDateTime.getMinuteOfHour();
            String minuteString = minute.toString();

            Integer second = currentDateTime.getSecondOfMinute();
            String secondString = second.toString();

            String currentDateString = "giftcard_documents/giftcards-" + currentLocalDateSting + "_" + hourSting + "-" + minuteString + "-" + secondString + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(currentDateString));

            document.open();

            //add image to gift card
            Image img = Image.getInstance("src/main/resources/images/coviran_viegas.png");
            img.scaleAbsolute(498f,60f);

            Font font = FontFactory.getFont(FontFactory.TIMES_BOLD, 35, BaseColor.WHITE);
            Chunk chunk = new Chunk("                Vale oferta 10€               ", font);
            chunk.setBackground(BaseColor.RED);

            for(int i = 0; i < codes.length; i++)
            {
                Chunk chunk1 = new Chunk("Código: " + codes[i].getFormattedCode() + " | Válido até: " + codes[i].getExpirationDate());

                document.add(img);
                document.add(new Paragraph("\n"));
                document.add(chunk);
                document.add(new Paragraph("\n"));
                document.add(chunk1);
                document.add(new Paragraph("\n"));
            }

            document.close();
            return currentDateString;
        }
        catch (DocumentException e)
        {
            e.printStackTrace();
            return "error";
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "error";
        }
    }
}
