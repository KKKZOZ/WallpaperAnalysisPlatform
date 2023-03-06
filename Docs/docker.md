docker run -d -p 15672:15672 -p 5672:5672 --restart=always --hostname my-rabbit --name some-rabbit -e RABBITMQ_DEFAULT_USER=root -e RABBITMQ_DEFAULT_PASS=kkkzoz rabbitmq:3.10-management


docker run -d -p 15672:15672 -p 5672:5672 --restart=always --hostname my-rabbit --name some-rabbit  rabbitmq:3.10-management



docker-compose -f example/standalone-mysql-8.yaml up -d