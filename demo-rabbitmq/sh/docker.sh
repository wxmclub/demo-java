
docker pull rabbitmq:3.8-management-alpine

docker run -d --name=myrabbitmq --hostname=myrabbitmq -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=123 rabbitmq:3.8-management-alpine
