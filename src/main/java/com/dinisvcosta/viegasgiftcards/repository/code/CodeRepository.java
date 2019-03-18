package com.dinisvcosta.viegasgiftcards.repository.code;

import com.dinisvcosta.viegasgiftcards.model.Code;
import com.dinisvcosta.viegasgiftcards.enums.UseCodeResult;
import com.dinisvcosta.viegasgiftcards.repository.DBConnection;
import com.dinisvcosta.viegasgiftcards.services.GiftCardGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joda.time.LocalDate;

public class CodeRepository
{
    private DBConnection dbConnection = DBConnection.getInstance();

    private ResultSet runQuery(String query)
    {
        Connection connection = dbConnection.getConnection();

        ResultSet result = null;

        try
        {
            PreparedStatement statement = connection.prepareStatement(query);
            result = statement.executeQuery();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public String generateCodes(int n)
    {
        Code[] codes = new Code[n];
        for(int i = 0; i < n; i++)
        {
            boolean success = false;
            while(!success)
            {
                Code code = new Code();
                if(verifyAvailability(code))
                {
                    codes[i] = code;
                    insertCode(code);
                    success = true;
                }
            }
        }

        String fileName = GiftCardGenerator.createDocument(codes);

        System.out.println(n + " codes successfully generated and inserted to DB!");

        return fileName;
    }

    public UseCodeResult useCode(String input)
    {
        Pattern codePattern = Pattern.compile("([a-z]|[0-9]){9}");
        Matcher m = codePattern.matcher(input);

        boolean isCode = m.matches();

        if(isCode)
        {
            System.out.println("Input is a code: " + input);

            UseCodeResult codeVerification = verifyCode(input);

            if(codeVerification == UseCodeResult.VALID_CODE)
            {
                markAsUsed(input);
                return codeVerification;
            }
            else
            {
                return codeVerification;
            }
        }
        else
        {
            System.out.println("Input was not a code! (9 character long, only letters or numbers)");
            return UseCodeResult.INVALID_CODE;
        }
    }

    private boolean verifyAvailability(Code code)
    {
        String query = "SELECT * FROM codes WHERE code=\"" + code.getCode() + "\";";

        try
        {
            ResultSet rs = runQuery(query);

            /*ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();*/

            if(!rs.next())
            {
                System.out.println("Queried code was not found, stored as new.");
                return true;
            }
            else
            {
                /*do {
                    for(int i = 1; i <= columnsNumber; i++)
                    {
                        if(i == 1)
                        {
                            System.out.println("--------------Result--------------");
                        }
                        String columnValue = rs.getString(i);
                        System.out.println(rsmd.getColumnName(i) + " : " + columnValue);
                        if(i == columnsNumber)
                        {
                            System.out.println("-----------End of Result-----------");
                        }
                    }
                } while(rs.next());*/

                return false;
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private void insertCode(Code code)
    {
        String query = "INSERT INTO codes" + "(code, formattedCode, creationDate, expirationDate) VALUES (";
        query += ("\"" + code.getCode() + "\", \"" + code.getFormattedCode() + "\", \"" + code.getCreationDate() + "\", \"" + code.getExpirationDate() + "\");");

        runQuery(query);
    }

    private UseCodeResult verifyCode(String code)
    {
        LocalDate currentDate = new LocalDate();
        System.out.println("current date: " + currentDate);

        String findCodeQuery = "SELECT * FROM codes WHERE code=\"" + code + "\";";
        String codeUsedQuery = "SELECT * FROM codes WHERE code=\"" + code + "\" AND isUsed = \"0\";";
        String codeDateQuery = "SELECT * FROM codes WHERE code=\"" + code + "\" AND isUsed = \"0\" AND expirationDate >= \"" + currentDate + "\";";

        try
        {
            ResultSet rs = runQuery(findCodeQuery);
            if(!rs.next())
            {
                System.out.println("CODE NOT FOUND");
                return UseCodeResult.CODE_NOT_FOUND;
            }
            else
            {
                ResultSet rs2 = runQuery(codeUsedQuery);
                if(!rs2.next())
                {
                    System.out.println("CODE ALREADY USED");
                    return UseCodeResult.CODE_ALREADY_USED;
                }
                else
                {
                    ResultSet rs3 = runQuery(codeDateQuery);
                    if(!rs3.next())
                    {
                        System.out.println("CODE EXPIRED");
                        return UseCodeResult.CODE_EXPIRED;
                    }
                    else
                    {
                        System.out.println("VALID CODE");
                        return UseCodeResult.VALID_CODE;
                    }
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        //return UseCodeResult.ERROR_CONNECTING_TO_DATABASE;
        return UseCodeResult.CODE_NOT_FOUND;
    }

    private void markAsUsed(String input)
    {
        String query = "UPDATE codes SET isUsed = 1 where code=\"" + input + "\";";

        System.out.println(query);

        runQuery(query);
    }
}
