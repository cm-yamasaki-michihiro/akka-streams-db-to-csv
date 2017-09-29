# akka-streams-db-to-csv

Akka Streamsを使用してRDBからデータを取得しCSVに書き出す処理のサンプルです。

## 環境
- Scala 2.12.1
- sbt 0.13.16

## 事前準備

プロジェクトのルートで以下のコマンドを入力します。
mysqlコマンドでパスワードを求められたら`password`と入力してください。

```shell
docker-compose up -d
mysql -u root -p -h 127.0.0.1 test < migration.sql 
```

## 実行
```shell
sbt run
```
