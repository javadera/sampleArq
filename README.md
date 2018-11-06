# sampleArq
Trying Arquillian with WildFly and Jacoco

WildFly と Arquillian、Jacoco を用いてJava EEアプリケーションのテストとカバレッジを出力してみました。  

1. arquillian.xml と pom.xml の jbossHome にローカルにダウンロードした WildFly のパスを設定します
1. コマンドラインで以下を実行します  
    mvn test -Pjacoco jacoco:report
1. target フォルダ内の site -> jacoco に結果が出力されます
    