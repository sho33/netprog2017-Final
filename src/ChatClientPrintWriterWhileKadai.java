import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ChatClientPrintWriterWhileKadai {
    public static void main(String[] args)
    {
        try {
            //
            //InetSocketAddress socketAddress =
            //new InetSocketAddress(args[0], Integer.parseInt(args[1]));

            InetSocketAddress socketAddress = new InetSocketAddress("localhost", 8888);
            //隣の人にチャットができるか確認してみよう。
            //InetSocketAddress socketAddress = new InetSocketAddress("25.52.41.207", 8888);


            //※2

            Socket socket = new Socket();
            socket.connect(socketAddress, 10000);

            InetAddress inetadrs;
            if ((inetadrs = socket.getInetAddress()) != null) {
                System.out.println("address:" + inetadrs);
            }
            else {
                System.out.println("Connection fail");
                return;
            }

            //※1
            //please reset your name
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            //String message = "Chat Test from Your name";
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            BufferedReader socketreader = new BufferedReader
                    (new InputStreamReader(socket.getInputStream()));

            int i = 100;
            while (i-->0){
                System.out.println("待機中");
                String line = socketreader.readLine();
                Gson gson = new Gson();
                Pokemon pokemon = gson.fromJson(line,Pokemon.class);
                System.out.println("クライアントからのメッセージ:" + pokemon);
                System.out.println("名前を取り出す:" + pokemon.getName());
                System.out.println("リストを取り出す:" + pokemon.getPokemonList());

                System.out.println("名前を入力してください。");
                String name = reader.readLine();
                pokemon.setName(name);
                pokemon.setPokemonList(name);

                String json = gson.toJson(pokemon);

                writer.println(json);
                writer.flush();//フラッシュすることでバッファ内の内容を送信を行います。
            }
            //※1
            writer.close();
            socket.close();
            //※2

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
