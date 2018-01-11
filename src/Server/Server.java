package Server;

import Settings.Pokemon;
import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server {
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
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(socket.getInputStream()));

            BufferedReader reader2 = new BufferedReader
                    (new InputStreamReader(socket2.getInputStream()));

            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            PrintWriter writer2 = new PrintWriter(socket2.getOutputStream());

            //　クライアント１にjsonを送りしりとり開始
            Pokemon pokemon = new Pokemon();
            pokemon.setPrevious("ナエトル");
            pokemon.setName("ナエトル");
            Gson gson = new Gson();
            String json = gson.toJson(pokemon);

            writer.println(json);
            writer.flush();
            //※1
            while (true){
                System.out.println("待機中");
                String line = reader.readLine();//テキストを読み込む
                pokemon = gson.fromJson(line, Pokemon.class);
                System.out.println("クライアント１からのメッセージ:" + pokemon);
                System.out.println("ひとつ前の名前:" + pokemon.getPrevious());
                //char last = pokemon.getPrevious().charAt(pokemon.getPrevious().length()-1);
                //System.out.println(String.valueOf(last));
                System.out.println("名前を取り出す:" + pokemon.getName());
                //char first = pokemon.getName().charAt(0);
                //System.out.println(String.valueOf(first));
                pokemon.setPrevious(pokemon.getName());
                /*
                if(last == first){
                    System.out.println("正しいしりとり");
                }
                if(pokemonListAll.contains(pokemon.getName())){
                    System.out.println(pokemon.getName()+"を含んでいる");
                }
                System.out.println("リストを取り出す:" + pokemon.getPokemonList());
                */
                if(pokemon.getWinFlag() == -1 || pokemon.getWinFlag() == 1){
                    break;
                }
                json = gson.toJson(pokemon);
                writer2.println(json);
                writer2.flush();

                System.out.println("待機中");
                String line2 = reader2.readLine();//テキストを読み込む
                pokemon = gson.fromJson(line2, Pokemon.class);
                System.out.println("クライアント２からのメッセージ:" + pokemon);
                System.out.println("ひとつ前の名前:" + pokemon.getPrevious());
                //char last2 = pokemon.getPrevious().charAt(pokemon.getPrevious().length()-1);
                //System.out.println(String.valueOf(last2));
                System.out.println("名前を取り出す:" + pokemon.getName());
                //char first2 = pokemon.getName().charAt(0);
                //System.out.println(String.valueOf(first2));
                pokemon.setPrevious(pokemon.getName());
                /*
                if(last2 == first2){
                    System.out.println("正しいしりとり");
                }
                if(pokemonListAll.contains(pokemon.getName())){
                    System.out.println(pokemon.getName()+"を含んでいる");
                }
                System.out.println("リストを取り出す:" + pokemon.getPokemonList());
                */
                if(pokemon.getWinFlag() == -1 || pokemon.getWinFlag() == 1){
                    break;
                }
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
            writer.close();
            socket.close();
            srvSock.close();
            //※2
            reader2.close();
            writer2.close();
            socket2.close();
            srvSock2.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
