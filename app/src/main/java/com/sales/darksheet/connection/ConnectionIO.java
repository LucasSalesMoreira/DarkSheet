package com.sales.darksheet.connection;

import android.util.Log;
import com.sales.darksheet.conf.Conf;
import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;

public class ConnectionIO {

    private Socket socket = null;

    public Socket connect() {
        try {
            socket = IO.socket(Conf.LOCAL_SERVER_HOST);
            return socket.connect();
        } catch (URISyntaxException e) {
            Log.d("ERROR_CONNECTION", e.getMessage());
            return null;
        }
    }
}
