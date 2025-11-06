#!/bin/bash

# Setup script for Payment Enrichment Service

echo "🚀 Setting up Payment Enrichment Service..."

# Check prerequisites
echo "Checking prerequisites..."

if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17+"
    exit 1
fi

if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven 3.8+"
    exit 1
fi

if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker"
    exit 1
fi

echo "✅ All prerequisites met"

# Build the project
echo "Building the project..."
mvn clean install -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed"
    exit 1
fi

echo "✅ Build successful"

# Start dependencies
echo "Starting dependencies (Redis, LocalStack)..."
docker-compose up -d redis localstack

echo "⏳ Waiting for services to be ready..."
sleep 10

# Verify services are running
echo "Verifying services..."
docker-compose ps

echo ""
echo "✅ Setup complete!"
echo ""
echo "📚 Next steps:"
echo "  1. Run the application:"
echo "     cd enrichment-api && mvn spring-boot:run -Dspring-boot.run.profiles=local"
echo ""
echo "  2. Or use Docker Compose:"
echo "     docker-compose up --build"
echo ""
echo "  3. Access the application:"
echo "     - API: http://localhost:8080"
echo "     - Swagger UI: http://localhost:8080/swagger-ui.html"
echo "     - Health: http://localhost:8080/actuator/health"
echo ""
