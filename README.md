# Number Sign

## 概要
マークダウン記法を用いたメモアプリ

## 起動前準備
- javaはバージョン8推奨
- Dockerコンテナのビルド
  ```
  $ docker compose build
  ```
- モジュールのインストール
  ```
  $ cd ui
  $ npm install
  ```
- フロントエンド設定ファイル作成
  - ホームディレクトリ(number-sign)で以下のコマンド実行
    ```
    $ cp ui/src/resouces/config_sample.json ui/src/resouces/config.json
    ```
  - 設定情報をconfig.jsonに記入する

## 起動
プロジェクトルートディレクトリで以下を実行
```
$ docker compose up
```

## Scalaテスト
Mysqlコンテナを起動時以下のコマンドを実行
```
$ docker compose up play-test
```

## Scalaフォーマット
- `./app`内をフォーマット
  ```
  $ sbt scalafmt
  ```
- `*.sbt`および`project/*.scala`ファイルをフォーマット
  ```
  $ sbt scalafmtSbt
  ```
- テストコードをフォーマット
  ```
  $ sbt test:scalafmt
  ```
- 全てのScalaファイルをフォーマット
  ```
  $ sbt scalafmt & sbt scalafmtSbt & sbt test:scalafmt
  ```

## フロントエンドのコード整形コマンド
```
$ npm run lint:fix
```

## DBの見方
- Mysqlコンテナを起動時, ログイン
  ```
  mysql -u root -p -h 127.0.0.1 -P 3306
  ```
  - パスワードは, `docker-compose.yml`の`MYSQL_ROOT_PASSWORD`変数に記入
