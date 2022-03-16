### **USER**
**spring boot + maven + docker + elastic**

#### 1. Run unit test
```bash
mvn clean test
```

#### 2. Build application jar
```bash
mvn clean install
```

#### 3. Build docker image locally
```bash
mvn clean package -Plocal-build
```

#### 4. Build docker image in registry
```bash
mvn clean package -Pregistry-build -Djib.to.auth.username= -Djib.to.auth.password=
```

#### 5. Build docker image and run application
```bash
mvn clean pre-integration-test -Plocal-build -Pdocker-compose
```

#### 5. Build docker image and run integration tests
```bash
mvn clean post-integration-test -Plocal-build -Pintegration-test
```