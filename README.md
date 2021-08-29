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

## eslintコマンド
```
$ npm run lint:fix
```