# Система управления банковскими картами
## Инструкция по запуску приложения
1) Клонирование репозитория  
`git clone https://github.com/kokosDeveloper/bank-rest.git`
2) Запуск базы данных через Docker Compose  
`docker compose up -d`
3) Проверка, что база данных запущена   
`docker ps`
4) Запуск приложения  
`java -jar target/bank-rest-0.0.1-SNAPSHOT.jar`
5) Приложение доступно по адресу  
`http://localhost:8080/api/v1`
6) Документация API доступна в формате OpenAPI в файле `docs/openapi.yaml`.
Чтобы с ней ознакомиться, необходимо перейти на https://editor.swagger.io и импортировать данный файл.