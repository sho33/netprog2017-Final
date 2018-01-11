package GUI;

import Settings.Pokemon;
import  javafx.fxml.FXML;
import  javafx.scene.control.Button;
import  javafx.scene.control.Label;

import javafx.event.ActionEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

public class Controller2 {
    private final String miniKey = "mini";
    private final String dakuonKey = "daku";
    private final String handakuonKey = "hankaku";

    private String firstChar = "";
    private String word = "";
    private String bufferedChar = "";
    private String previous = "";
    private String onePrevious = "";

    private ArrayList<Button> resistButton = new ArrayList<Button>();
    private HashMap<String, String> map = new HashMap<String, String>();

    //この辺ザキさんの
    private InetSocketAddress socketAddress;
    private Socket socket;
    private InetAddress inetadrs;
    private BufferedReader socketreader;
    private PrintWriter writer;
    private Gson gson = new Gson();
    private Pokemon pokemon;

    @FXML
    private Label nowWord;
    @FXML
    private Label previousNames;

    /*
    初期化処理
     */
    @FXML
    private void initialize(){
        setHenkanMap(map);
        word = String.valueOf(firstChar);
        nowWord.setText(word);


        //この辺ザキさんの
        try {
            socketAddress = new InetSocketAddress("localhost", 8889);
            socket = new Socket();
            socket.connect(socketAddress, 10000);

            if ((inetadrs = socket.getInetAddress()) != null) {
                System.out.println("address:" + inetadrs);
            }
            else {
                System.out.println("Connection fail");
                return;
            }

            socketreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());

            System.out.println("待機中");
            String line = socketreader.readLine();
            pokemon = gson.fromJson(line,Pokemon.class);
            System.out.println("クライアントからのメッセージ:" + pokemon);
            System.out.println("名前を取り出す:" + pokemon.getName());
            System.out.println("リストを取り出す:" + pokemon.getPokemonList());

            for (String names : pokemon.getPokemonList()){
                previous += names + "＞";
            }
            previousNames.setText(previous);

            onePrevious = pokemon.getName();
            setFirstChar(onePrevious.substring(onePrevious.length() - 1, onePrevious.length()));
            word += firstChar;
            nowWord.setText(firstChar);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void setFirstChar(String firstChar){
        this.firstChar = firstChar;
    }

    /*
    変換用のhashMapの設定
     */
    private void setHenkanMap(HashMap<String, String> map){
        map.put(dakuonKey + "カ", "ガ");
        map.put(dakuonKey + "キ", "ギ");
        map.put(dakuonKey + "ク", "グ");
        map.put(dakuonKey + "ケ", "ゲ");
        map.put(dakuonKey + "コ", "ゴ");

        map.put(dakuonKey + "サ", "ザ");
        map.put(dakuonKey + "シ", "ジ");
        map.put(dakuonKey + "ス", "ズ");
        map.put(dakuonKey + "セ", "ゼ");
        map.put(dakuonKey + "ソ", "ゾ");

        map.put(dakuonKey + "タ", "ダ");
        map.put(dakuonKey + "チ", "ヂ");
        map.put(dakuonKey + "ツ", "ヅ");
        map.put(dakuonKey + "テ", "デ");
        map.put(dakuonKey + "ト", "ド");

        map.put(dakuonKey + "ハ", "バ");
        map.put(dakuonKey + "ヒ", "ビ");
        map.put(dakuonKey + "フ", "ブ");
        map.put(dakuonKey + "ヘ", "ベ");
        map.put(dakuonKey + "ホ", "ボ");

        map.put(handakuonKey + "ハ", "パ");
        map.put(handakuonKey + "ヒ", "ピ");
        map.put(handakuonKey + "フ", "プ");
        map.put(handakuonKey + "ヘ", "ペ");
        map.put(handakuonKey + "ホ", "ポ");

        map.put(miniKey + "ア", "ァ");
        map.put(miniKey + "イ", "ィ");
        map.put(miniKey + "ウ", "ゥ");
        map.put(miniKey + "エ", "ェ");
        map.put(miniKey + "オ", "ォ");

        map.put(miniKey + "ツ", "ッ");

        map.put(miniKey + "ヤ", "ャ");
        map.put(miniKey + "ユ", "ュ");
        map.put(miniKey + "ヨ", "ョ");
    }

    /*
    濁音、半濁音、小さくするbuttonの動作
     */
    public void clickedDakuon(ActionEvent event){
        if (word.length() > 0) {
            if (map.containsKey(dakuonKey + bufferedChar)) {
                String chengedChar = map.get(dakuonKey + bufferedChar);
                word = word.substring(0, word.length() - 1) + chengedChar;
                nowWord.setText(word);
            }
        }
    }
    public void clickedHandakuon(ActionEvent event){
        if (word.length() > 0) {
            if (map.containsKey(handakuonKey + bufferedChar)) {
                String chengedChar = map.get(handakuonKey + bufferedChar);
                word = word.substring(0, word.length() - 1) + chengedChar;
                nowWord.setText(word);
            }
        }
    }
    public void clickedMini(ActionEvent event){
        if (word.length() > 0) {
            if (map.containsKey(miniKey + bufferedChar)) {
                String chengedChar = map.get(miniKey + bufferedChar);
                word = word.substring(0, word.length() - 1) + chengedChar;
                nowWord.setText(word);
            }
        }
    }

    /*
    文字をクリックした際の処理
    クリックしたらその文字をwordに足してlabelに出力
    クリックされたbuttonを非活性化
    後のためにbuttonのオブジェクトは配列に保存
     */
    public void clickedCharacter(ActionEvent event){
        Button b = (Button)event.getSource();
        bufferedChar = b.getText();

        word += b.getText();
        nowWord.setText(word);

        b.setDisable(true);
        resistButton.add(b);
    }
    public void clickedSymbol(ActionEvent event){
        Button b = (Button)event.getSource();
        bufferedChar = b.getText();

        word += b.getText();
        nowWord.setText(word);
    }


    /*
    OKがクリックされた際の処理
     */
    public void clickedOK(ActionEvent event){
        pokemon.setName(word);
        pokemon.setPokemonList(word);
        String json = gson.toJson(pokemon);
        writer.println(json);
        writer.flush();

        resistButton = new ArrayList<Button>();
        word = "";
        previous = "";

        try {
            System.out.println("待機中");
            String line = socketreader.readLine();
            pokemon = gson.fromJson(line, Pokemon.class);
            System.out.println("クライアントからのメッセージ:" + pokemon);
            System.out.println("名前を取り出す:" + pokemon.getName());
            System.out.println("リストを取り出す:" + pokemon.getPokemonList());

            for (String names : pokemon.getPokemonList()){
                previous += names + "＞";
            }

            previousNames.setText(previous);

            onePrevious = pokemon.getName();
            setFirstChar(onePrevious.substring(onePrevious.length() - 1, onePrevious.length()));
            word += firstChar;
            nowWord.setText(firstChar);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /*
    Clearをクリックした際の処理
    labelとwordの文字を消去する
    クリックされて非活性化していたbuttonを復活
     */
    public void clickedClear(ActionEvent event){
        word = "" + firstChar;
        nowWord.setText(word);

        for (Button button : resistButton){
            button.setDisable(false);
        }

        resistButton = new ArrayList<Button>();
    }
}