import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServerBufferedReaderWhileKadai {
    public static void main(String[] args)
    {
        try {
            // サーバーソケット作成
            //起動時パラメータからポートを読み取り、
            //そのポートで接続要求を待つ
            //ServerSocketクラスはクライアントからの接続を待ち、
            //srvSock.accept();によって接続したSocketオブジェクト
            //を返す。
            //その後の通信には、このSocketオブジェクトを使用する。
            //int port = Integer.parseInt(args[0]);
            int port=8888;
            //※2
            ServerSocket srvSock = new ServerSocket(port);

            // 接続待機。接続完了後、次行命令に移る。
            System.out.println("クライアント1からの接続を待ちます。");
            Socket socket = srvSock.accept();
            //接続先アドレスを表示
            System.out.println("address1:" + socket.getInetAddress());

            //クライアント2つ目の接続待機
            int port2=8889;
            ServerSocket srvSock2 = new ServerSocket(port2);

            // 接続待機。接続完了後、次行命令に移る。
            System.out.println("クライアント2からの接続を待ちます。");
            Socket socket2 = srvSock2.accept();
            //接続先アドレスを表示
            System.out.println("address2:" + socket2.getInetAddress());

            //　通信処理
            //ソケットの入力ストリームから文字列を1行読み取る。
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(socket.getInputStream()));

            BufferedReader reader2 = new BufferedReader
                    (new InputStreamReader(socket2.getInputStream()));

            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            PrintWriter writer2 = new PrintWriter(socket2.getOutputStream());

            Pokemon pokemon = new Pokemon();
            Gson gson = new Gson();
            String json = gson.toJson(pokemon);

            writer.println(json);
            writer.flush();
            //※1
            int i = 100;
            while (i-->0){
                System.out.println("待機中");
                String line = reader.readLine();//読み取った文字列を表示
                pokemon = gson.fromJson(line,Pokemon.class);
                System.out.println("クライアント１からのメッセージ:" + pokemon);
                System.out.println("ひとつ前の名前:" + pokemon.getPrevious());
                System.out.println("名前を取り出す:" + pokemon.getName());
                pokemon.setPrevious(pokemon.getName());
                System.out.println("リストを取り出す:" + pokemon.getPokemonList());

                json = gson.toJson(pokemon);
                writer2.println(json);
                writer2.flush();

                System.out.println("待機中");
                String line2 = reader2.readLine();//読み取った文字列を表示
                pokemon = gson.fromJson(line2,Pokemon.class);
                System.out.println("クライアント２からのメッセージ:" + pokemon);
                System.out.println("ひとつ前の名前:" + pokemon.getPrevious());
                System.out.println("名前を取り出す:" + pokemon.getName());
                pokemon.setPrevious(pokemon.getName());
                System.out.println("リストを取り出す:" + pokemon.getPokemonList());

                json = gson.toJson(pokemon);
                writer.println(json);
                writer.flush();
            }

            //終了処理　このプログラムは1行読み取ったら終了する。
            //通信を続けるのであれば、reader.readLine();を
            //ループするが、終了コマンドをチェックする等の処理を
            //記述する。
            //※1
            reader.close();
            socket.close();
            srvSock.close();
            //※2
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
