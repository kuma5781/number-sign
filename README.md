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

## 起動
プロジェクトルートディレクトリで以下を実行
```
$ docker compose up
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

## DBの見方
- コンテナを起動時, Mysqlにログイン
  ```
  mysql -u root -p -h 127.0.0.1 -P 3306
  ```
  - パスワードは, `docker-compose.yml`の`MYSQL_ROOT_PASSWORD`変数に記入
  
