# RetailPulse

Team Member:
Galvin Leow,
Evalyn Low Wei Xuan,
Yap Chong Hwee Casper,
Pang Zhi Hao,
William Siling Tjhi

To Run:
1. Change directory to ./infrastructure/build_docker
2. Execute:
    docker rmi mysql-access:0.1.0
    docker build -t mysql-access:0.1.0 -f build_access_sql.dockerfile .
3. Change directory to ./infrastructure
4. Execute:
    docker compose -f 0_mysql_access.yaml -d
    docker compose -f 1_mysql_RP.yaml -d
5. Run identity-access-management service using your IDE.
6. Run backend service using your IDE.
7. 