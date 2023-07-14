# HOW TO deploy to server

- rebuild maven(bootJar)
- go to main-api folder
- run 'docker build --tag=main-api-service:latest .'
- run 'docker tag main-api-service registry.digitalocean.com/catbox-registry/main-api-service'
- run 'docker push registry.digitalocean.com/catbox-registry/main-api-service'
- kubectl replace -f ...