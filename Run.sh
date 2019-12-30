# Build front-end
cd ChatApp/src/main/resources/Static/
npm install
npm run build

# Build server
cd ../../../../
mvn clean package

# Run server
cd target/
java -jar NexChat-1.0.jar
