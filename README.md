# Instruções
Está documentação tem como objetivo instruir o testador a como subir e executar o projeto:

# Getting Started

* ### Criar um novo .jar do serviço backend
```
mvn clean package
```

* ### Criar containers da aplicação
```
docker-compose up -d
```
Os containers criados nessa fase são:
- MongoDB
- Mongo Express
- Localstack (serviços AWS)
- Redis
- Backend task-manager

* ### Serviços AWS
Para ver recursos executados, criados e logs(CloudWatch Logs) acessar:
[Localstack Dashboar](https://app.localstack.cloud/inst/default/status)




