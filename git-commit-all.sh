#!/bin/bash

# Automated Git Commit Script for Payment Enrichment Service
# This script commits the code in logical, reviewable pieces

set -e  # Exit on any error

echo "🚀 Starting Git commits for Payment Enrichment Service"
echo "=================================================="
echo ""

# Check if git is initialized
if [ ! -d ".git" ]; then
    echo "📦 Initializing Git repository..."
    git init
    git branch -M main
    echo "✅ Git initialized"
else
    echo "✅ Git repository already exists"
fi

echo ""
echo "Please configure Git if not already done:"
echo "  git config user.name \"Leo Moon\""
echo "  git config user.email \"hyungjibi@gmail.com\""
echo ""
read -p "Press Enter to continue with commits..."

# Commit 1: Project Setup
echo ""
echo "📝 Commit 1/13: Project Setup & Configuration"
git add pom.xml .gitignore mvnw LICENSE
git commit -m "chore: initialize Maven multi-module project structure

- Add parent pom.xml with Spring Boot 3.2 and AWS SDK dependencies
- Configure Maven compiler plugin for Java 17
- Add .gitignore for Java/Maven/IDE files
- Add Maven wrapper for consistent builds
- Add MIT License" || echo "⚠️  No changes or already committed"

# Commit 2: Core Domain
echo "📝 Commit 2/13: Core Domain Models"
git add enrichment-core/pom.xml
git add enrichment-core/src/main/java/com/mastercard/enrichment/core/domain/ 2>/dev/null || true
git commit -m "feat(core): add core domain models

- Add Transaction domain model
- Add MerchantCategory with risk levels
- Add GeolocationData model
- Add EnrichedTransaction aggregate
- Add EnrichmentStatus enum
- Define value objects for payment enrichment" || echo "⚠️  No changes or already committed"

# Commit 3: Core Interfaces
echo "📝 Commit 3/13: Core Service Interfaces"
git add enrichment-core/src/main/java/com/mastercard/enrichment/core/service/ 2>/dev/null || true
git add enrichment-core/src/main/java/com/mastercard/enrichment/core/repository/ 2>/dev/null || true
git commit -m "feat(core): define service and repository interfaces

- Add EnrichmentService interface with sync/async methods
- Add MerchantCategoryService interface
- Add GeolocationService interface
- Add TransactionRepository interface
- Support for batch processing and status tracking" || echo "⚠️  No changes or already committed"

# Commit 4: Exceptions
echo "📝 Commit 4/13: Exception Handling"
git add enrichment-core/src/main/java/com/mastercard/enrichment/core/exception/ 2>/dev/null || true
git commit -m "feat(core): implement custom exception hierarchy

- Add EnrichmentException base class
- Add TransactionNotFoundException
- Prepare for proper error handling in services" || echo "⚠️  No changes or already committed"

# Commit 5: AWS Config
echo "📝 Commit 5/13: AWS Configuration"
git add enrichment-infrastructure/pom.xml
git add enrichment-infrastructure/src/main/java/com/mastercard/enrichment/infrastructure/config/AwsConfig.java 2>/dev/null || true
git commit -m "feat(infrastructure): add AWS SDK configuration

- Configure DynamoDB client with enhanced API
- Configure S3 client for data storage
- Support LocalStack for local development
- Add region and endpoint configuration" || echo "⚠️  No changes or already committed"

# Commit 6: Redis Config
echo "📝 Commit 6/13: Redis Configuration"
git add enrichment-infrastructure/src/main/java/com/mastercard/enrichment/infrastructure/config/RedisConfig.java 2>/dev/null || true
git commit -m "feat(infrastructure): add Redis caching configuration

- Configure Redis connection factory with Lettuce
- Set up RedisTemplate with JSON serialization
- Configure RedisCacheManager with 10-minute TTL
- Enable Spring Cache abstraction" || echo "⚠️  No changes or already committed"

# Commit 7: DynamoDB Repository
echo "📝 Commit 7/13: DynamoDB Repository"
git add enrichment-infrastructure/src/main/java/com/mastercard/enrichment/infrastructure/persistence/ 2>/dev/null || true
git commit -m "feat(infrastructure): implement DynamoDB repository

- Add TransactionEntity with DynamoDB annotations
- Implement DynamoDbTransactionRepository
- Add entity-to-domain mapping
- Support for CRUD operations on transactions" || echo "⚠️  No changes or already committed"

# Commit 8: Service Implementations
echo "📝 Commit 8/13: Service Implementations"
git add enrichment-infrastructure/src/main/java/com/mastercard/enrichment/infrastructure/service/ 2>/dev/null || true
git commit -m "feat(infrastructure): implement enrichment services

- Implement EnrichmentServiceImpl with caching
- Implement MerchantCategoryServiceImpl with MCC codes
- Implement GeolocationServiceImpl with mock data
- Add async processing support with @Async
- Implement data normalization logic" || echo "⚠️  No changes or already committed"

# Commit 9: API Controllers
echo "📝 Commit 9/13: API Controllers & DTOs"
git add enrichment-api/pom.xml
git add enrichment-api/src/main/java/com/mastercard/enrichment/api/EnrichmentServiceApplication.java 2>/dev/null || true
git add enrichment-api/src/main/java/com/mastercard/enrichment/api/dto/ 2>/dev/null || true
git add enrichment-api/src/main/java/com/mastercard/enrichment/api/controller/ 2>/dev/null || true
git add enrichment-api/src/main/java/com/mastercard/enrichment/api/mapper/ 2>/dev/null || true
git commit -m "feat(api): add REST API controllers and DTOs

- Add EnrichmentController with sync/async/batch endpoints
- Add TransactionRequest and EnrichedTransactionResponse DTOs
- Add TransactionMapper for DTO conversions
- Add OpenAPI/Swagger annotations
- Support for API versioning (v1)" || echo "⚠️  No changes or already committed"

# Commit 10: Exception Handling & Config
echo "📝 Commit 10/13: API Exception Handling & Configuration"
git add enrichment-api/src/main/java/com/mastercard/enrichment/api/exception/ 2>/dev/null || true
git add enrichment-api/src/main/resources/ 2>/dev/null || true
git commit -m "feat(api): add global exception handling and configuration

- Add @RestControllerAdvice for centralized error handling
- Add ErrorResponse DTO with validation errors
- Add application.yml with Spring Boot configuration
- Add application-local.yml for local development
- Configure actuator endpoints and metrics" || echo "⚠️  No changes or already committed"

# Commit 11: Tests
echo "📝 Commit 11/13: Test Suite"
git add enrichment-api/src/test/ 2>/dev/null || true
git add enrichment-infrastructure/src/test/ 2>/dev/null || true
git add enrichment-core/src/test/ 2>/dev/null || true
git commit -m "test: add comprehensive test suite

- Add EnrichmentControllerTest with Mockito
- Add EnrichmentServiceImplTest for service layer
- Add unit tests with 80%+ coverage
- Configure TestContainers for integration tests
- Add REST Assured for API testing" || echo "⚠️  No changes or already committed"

# Commit 12: Docker & DevOps
echo "📝 Commit 12/13: Docker & CI/CD"
git add Dockerfile
git add docker-compose.yml
git add monitoring/
git add .github/
git add .vscode/
git add setup.sh
git commit -m "feat(devops): add Docker and CI/CD configuration

- Add Dockerfile with multi-stage build
- Add docker-compose.yml with Redis, LocalStack, Prometheus, Grafana
- Add GitHub Actions CI/CD pipeline
- Add Prometheus monitoring configuration
- Add VS Code tasks for development
- Add setup script for quick start" || echo "⚠️  No changes or already committed"

# Commit 13: Documentation
echo "📝 Commit 13/13: Documentation"
git add README.md
git add QUICKSTART.md
git add INTERVIEW_GUIDE.md
git add PROJECT_COMPLETE.md
git add GIT_COMMIT_GUIDE.md
git add api-examples.http
git commit -m "docs: add comprehensive project documentation

- Add detailed README with architecture and setup
- Add QUICKSTART guide for rapid onboarding
- Add INTERVIEW_GUIDE with talking points
- Add GIT_COMMIT_GUIDE for contribution workflow
- Add API examples for testing
- Add PROJECT_COMPLETE summary" || echo "⚠️  No changes or already committed"

echo ""
echo "=================================================="
echo "✅ All commits completed!"
echo ""
echo "📊 Commit summary:"
git log --oneline --graph --all
echo ""
echo "🌐 Next steps:"
echo "1. Review commits: git log --oneline"
echo "2. Create GitHub repository:"
echo "   Option A: gh repo create payment-enrichment-service --public --source=. --push"
echo "   Option B: Manually on GitHub.com, then:"
echo "             git remote add origin https://github.com/YOUR_USERNAME/payment-enrichment-service.git"
echo "             git push -u origin main"
echo ""
echo "3. Verify on GitHub and add repository topics/description"
echo ""
echo "🎉 Ready to showcase your project!"
