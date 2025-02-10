# RetailPulse

Team Member:
Galvin Leow,
Evalyn Low Wei Xuan,
Yap Chong Hwee Casper,
Pang Zhi Hao,
William Siling Tjhi

To Run:
1. Change directory to ./deploy/build_docker
2. Execute:
    docker rmi mysql-access:0.1.0
    docker build -t mysql-access:0.1.0 -f build_access_sql.dockerfile .
3. Change directory to ./deploy
4. Execute:
    docker compose -f 0_mysql_access.yaml -d
    docker compose -f 1_mysql_RP.yaml -d
5. Open a new terminal. Change directory to ./identity-access-management.
6. Execute:
  mvn clean install
  mvn spring-boot:run
7. Open a new terminal. Change directory to ./backend.
8. Execute:
  mvn clean install
  mvn spring-boot:run
9. Open a new terminal. Change directory to ./rp-web-app.
10. Execute:
  npm install
  npm start