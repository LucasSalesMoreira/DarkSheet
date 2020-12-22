package com.sales.darksheet.fileManager;

import android.content.Context;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Manager {

    private Context context;
    private static final String FILE_NAME = "token.txt";

    public Manager(Context context) {
        this.context = context;
    }

    public void saveToken(String token) {
        File file = new File(context.getFilesDir(), FILE_NAME);

        if (writeToken(file, token))
            Toast.makeText(context, "Token salvo!", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, "Falha no salvamento do token!", Toast.LENGTH_LONG).show();
    }

    public String readToken() {
        File file = new File(context.getFilesDir(), FILE_NAME);
        try {
            if (file.exists() && file.isFile()) {
                FileReader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String token = bufferedReader.readLine();
                System.out.println("TOKEN = " + token);
                return token;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteToken() {
        return writeToken(new File(context.getFilesDir(), FILE_NAME),"XXX-XXX");
    }

    private boolean writeToken(File file, String token) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(token);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
