package GUI;

import Settings.Pokemon;
import  javafx.fxml.FXML;
import  javafx.scene.control.Button;
import  javafx.scene.control.Label;

import javafx.event.ActionEvent;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

public class Controller {
    //変換に使う定数
    private final String miniKey = "mini";
    private final String dakuonKey = "daku";
    private final String handakuonKey = "hankaku";
    private final String normal = "normal";
    private final String specialList[] = {"ー","♂","♀","２","Ｚ"};

    private String firstChar = "";          //しりとりをする際の一文字目
    private String nowWritten = "";               //自分の入力をしている文字列
    private String bufferedChar = "";      //一つ前の入力

    private ArrayList<Button> resistButton = new ArrayList<Button>();   //入力の最中で使用されているボタン
    private HashMap<String, String> map = new HashMap<String, String>();          //文字列変換用map

    private ArrayList<Button> alreadyUsedButton = new ArrayList<Button>();  //使用済みボタン


    private InetSocketAddress socketAddress;
    private Socket socket;
    private InetAddress inetadrs;
    private BufferedReader socketreader;
    private PrintWriter writer;

    private Gson gson = new Gson();

    private Pokemon pokemon;

    private List<String> pokemonListAll = getPokemonListAll();

    @FXML
    private Label nowWrittenLabel;
    @FXML
    private Label previousNames;
    @FXML
    private Label WinORLose;
    @FXML
    private Button o;
    @FXML
    private Button su;
    @FXML
    private Button me;
    @FXML
    private Button tu;
    @FXML
    private Button ze;
    @FXML
    private Button to;

    /*
    初期化処理
     */
    @FXML
    private void initialize(){
        setHenkanMap(map);
        nowWritten = getFirstChar();
        nowWrittenLabel.setText(nowWritten);

        try {
            pokemonListAll = getPokemonListAll();

            socketAddress = new InetSocketAddress("localhost", 8888);
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

            //読み込み
            String line = socketreader.readLine();
            pokemon = gson.fromJson(line,Pokemon.class);

            //コンソールに情報を出力
            printDetailOnConsole();

            //履歴の表示
            printLog();

            //最初の一文字の表示
            firstCharSettings();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /*
    csvからポケモンの名前の配列を作成する
     */
    private List<String> getPokemonListAll(){
        List<String> pokemonListAll = new ArrayList<>();
        try {
            // csvを読み込み、全ポケモンの名前の入った配列を作成
            BufferedReader br
                    = new BufferedReader(new InputStreamReader(new FileInputStream("pokemon_list.csv"),"SJIS"));
            String s;
            // ファイルを行単位で読む
            while( (s = br.readLine()) != null ) {
                // カンマで分割したString配列を得る
                String array[] = s.split( "," );
                // データ数をチェックしたあと代入、プリントする
                if( array.length != 2 ) throw new NumberFormatException();
                pokemonListAll.add(array[1]);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return pokemonListAll;
    }

    //一文字目を設定する関数
    private void setFirstChar(String firstChar){
        this.firstChar = firstChar;
    }
    private String getFirstChar(){return firstChar;}

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


        //変換済みの語を戻す
        map.put(normal + "ガ", "カ");
        map.put(normal + "ギ", "キ");
        map.put(normal + "グ", "ク");
        map.put(normal + "ゲ", "ケ");
        map.put(normal + "ゴ", "コ");

        map.put(normal + "ザ", "サ");
        map.put(normal + "ジ", "シ");
        map.put(normal + "ズ", "ス");
        map.put(normal + "ゼ", "セ");
        map.put(normal + "ゾ", "ソ");

        map.put(normal + "ダ", "タ");
        map.put(normal + "ヂ", "チ");
        map.put(normal + "ヅ", "ツ");
        map.put(normal + "デ", "テ");
        map.put(normal + "ド", "ト");

        map.put(normal + "バ", "ハ");
        map.put(normal + "ビ", "ヒ");
        map.put(normal + "ブ", "フ");
        map.put(normal + "ベ", "ヘ");
        map.put(normal + "ボ", "ホ");

        map.put(normal + "パ", "ハ");
        map.put(normal + "ピ", "ヒ");
        map.put(normal + "プ", "フ");
        map.put(normal + "ペ", "ヘ");
        map.put(normal + "ポ", "ホ");

        map.put(normal + "ァ", "ア");
        map.put(normal + "ィ", "イ");
        map.put(normal + "ゥ", "ウ");
        map.put(normal + "ェ", "エ");
        map.put(normal + "ォ", "オ");

        map.put(normal + "ッ", "ツ");

        map.put(normal + "ャ", "ヤ");
        map.put(normal + "ュ", "ユ");
        map.put(normal + "ョ", "ヨ");

        //特殊文字用の変換
        map.put("♂", "ス");
        map.put("♀", "ス");
        map.put("２", "ツ");
        map.put("Ｚ", "ト");
    }

    /*
    文字をクリックした際の処理
    クリックしたらその文字をwordに足してlabelに出力
    クリックされたbuttonを非活性化
    後のためにbuttonのオブジェクトは配列に保存
     */
    public void clickedCharacter(ActionEvent event){
        Button b = (Button)event.getSource();
        buttonAction(b);

        b.setDisable(true);
        resistButton.add(b);
    }

    //ー、・の際の処理。非活性化はしない
    public void clickedSymbol(ActionEvent event){
        Button b = (Button)event.getSource();
        buttonAction(b);
    }


    /*
    濁音、半濁音、小さくするbuttonの動作
    末尾一文字を切り出して変換
     */
    //　゛
    public void clickedDakuon(ActionEvent event){
        chenge(dakuonKey);
    }
    //　゜
    public void clickedHandakuon(ActionEvent event){
        chenge(handakuonKey);
    }
    //小さく
    public void clickedMini(ActionEvent event){
        chenge(miniKey);
    }

    //交換及び表示処理
    public void chenge(String key){
        if (nowWritten.length() > 0) {
            if (map.containsKey(key + bufferedChar)) {
                nowWritten = nowWritten.substring(0, nowWritten.length() - 1) + map.get(key + bufferedChar);
                nowWrittenLabel.setText(nowWritten);
            }
        }
    }


    /*
    特殊文字のクリック
    片仮名に直した際に使う文字が使用済みなら使用不可
    使用後は外套の片仮名を非活性化
     */
    //♂
    public void clickedOsu(ActionEvent event){
        if (!o.isDisable() && !su.isDisable()) {
            Button b = (Button) event.getSource();
            buttonAction(b);

            o.setDisable(true);
            su.setDisable(true);
        }
    }
    //♀
    public void clickedMesu(ActionEvent event){
        if (!me.isDisable() && !su.isDisable()) {
            Button b = (Button) event.getSource();
            buttonAction(b);

            me.setDisable(true);
            su.setDisable(true);
        }
    }
    //２
    public void clicked2(ActionEvent event){
        if (!tu.isDisable()) {
            Button b = (Button) event.getSource();
            buttonAction(b);

            tu.setDisable(true);
        }
    }
    //Ｚ
    public void clickedZetto(ActionEvent event){
        if (!ze.isDisable() && !tu.isDisable() && !to.isDisable()) {
            Button b = (Button) event.getSource();
            buttonAction(b);

            ze.setDisable(true);
            tu.setDisable(true);
            to.setDisable(true);
        }
    }

    //buttonが押された際の基本的な動作
    public void buttonAction(Button b){
        bufferedChar = b.getText();
        nowWritten += b.getText();
        nowWrittenLabel.setText(nowWritten);
    }


    /*
    OKがクリックされた際の処理
     */
    public void clickedOK(ActionEvent event){
        /*
        データをサーバーに送る
         */
        //ポケモンの名前か判定
        if(pokemonListAll.contains(nowWritten)){
            send();

            //リセット処理
            resistButton = new ArrayList<Button>();
            nowWritten = "";

            //相手からのデータを受け取った後の処理
            //readLine()で待機しているので
            try {
                String line = socketreader.readLine();
                if(line != null) {
                    pokemon = gson.fromJson(line, Pokemon.class);
                    printDetailOnConsole();
                    recieve();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }else {
            clickedClear(event);
        }
    }

    /*
    Clearをクリックした際の処理
    labelとwordの文字を消去する
    クリックされて非活性化していたbuttonを復活
     */
    public void clickedClear(ActionEvent event){
        nowWritten = firstChar;
        nowWrittenLabel.setText(nowWritten);

        for (Button button : resistButton){
            button.setDisable(false);
        }

        resistButton = new ArrayList<Button>();
    }

    public void send(){
        //使い終わったbuttonを保存
        for (Button used : resistButton){
            alreadyUsedButton.add(used);
        }
        //勝利判定
        if (alreadyUsedButton.size() == 46){
            //勝ちの処理をここに
            pokemon.setWinFlag(-1);//相手の負け
            WinORLose.setText("あなたの勝ち");
        }
        //敗北判定
        if ("ン".equals(nowWritten.substring(nowWritten.length() - 1, nowWritten.length()))){
            pokemon.setWinFlag(1);//相手の勝ち
            WinORLose.setText("あなたの負け");
        }
        pokemon.setName(nowWritten);
        pokemon.setPokemonList(nowWritten);
        String json = gson.toJson(pokemon);
        writer.println(json);
        writer.flush();
    }

    /*
    受け取った際の処理
    勝ち負けの判定も行う
     */
    public void recieve(){
        //しりとりの履歴を表示
        printLog();

        //勝ち負けのフラグを見る
        if (pokemon.getWinFlag() == 1) {
            WinORLose.setText("あなたの勝ち");
            try {
                socketreader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (pokemon.getWinFlag() == -1) {
            WinORLose.setText("あなたの負け");
            try {
                socketreader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            //末尾の一文字を取り出し、それをfirstCharに設定
            firstCharSettings();
        }
    }

    //コンソールに情報を出力
    public void printDetailOnConsole(){
        System.out.println("待機中");
        System.out.println("クライアントからのメッセージ:" + pokemon);
        System.out.println("名前を取り出す:" + pokemon.getName());
        System.out.println("リストを取り出す:" + pokemon.getPokemonList());
    }

    //履歴出力
    public void printLog(){
        String previous = "";
        for (String names : pokemon.getPokemonList()) {
            previous += names + "＞";
        }
        previousNames.setText(previous);
    }

    //一文字目をラベルなどに設定
    public void firstCharSettings(){
        String onePrevious = pokemon.getName();
        String preChar = onePrevious.substring(onePrevious.length() - 1, onePrevious.length());

        //末尾が特殊な場合には変更をする
        //"ー"なら一文字前を取る
        //他はmapから交換
        while (true) {
            if (preChar.equals("ー")) {
                preChar = onePrevious.substring(onePrevious.length() - 2, onePrevious.length() - 1);
                continue;
            } else if (map.containsKey(preChar)) {
                preChar = map.get(preChar);
                continue;
            } else if (map.containsKey("normal" + preChar)) {
                preChar = map.get("normal" + preChar);
                continue;
            }
            break;
        }
        setFirstChar(preChar);
        bufferedChar = preChar;
        nowWritten += getFirstChar();
        nowWrittenLabel.setText(nowWritten);
    }
}
