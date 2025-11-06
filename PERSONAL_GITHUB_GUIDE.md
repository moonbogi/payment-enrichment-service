# Managing Personal GitHub Repository

## ✅ Repository URLs

**Personal GitHub (Public):** https://github.com/moonbogi/payment-enrichment-service  
**Company GitHub (Private):** https://ghe.intra.nudatasecurity.com/Leo-Moon/payment-enrichment-service

---

## 📝 Quick Commands Reference

### View Current Remote
```bash
git remote -v
```

### Push to Personal GitHub
```bash
# Make sure you're using the right account
GH_HOST=github.com gh auth status

# Push changes
git push origin main
```

### Switch Between GitHub Accounts

**Check current account:**
```bash
gh auth status
```

**Login to personal GitHub.com:**
```bash
gh auth login --hostname github.com
# Follow prompts, choose HTTPS, authenticate via web browser
```

**Switch back to company GitHub:**
```bash
gh auth switch --hostname ghe.intra.nudatasecurity.com
```

---

## 🔄 Making Changes and Pushing

### Standard Git Workflow

```bash
# 1. Make your changes to files

# 2. Check what changed
git status
git diff

# 3. Stage changes
git add <files>
# or add all changes
git add .

# 4. Commit with meaningful message
git commit -m "feat: add new feature"

# 5. Push to personal GitHub
git push origin main
```

### Commit Message Format

```
<type>(<scope>): <subject>

Examples:
feat(api): add new endpoint for batch processing
fix(cache): resolve Redis connection timeout
docs(readme): update installation instructions
test(service): add unit tests for enrichment
```

Types: `feat`, `fix`, `docs`, `test`, `chore`, `refactor`, `style`, `perf`

---

## 🌐 Repository Management

### Add Topics on GitHub

Go to: https://github.com/moonbogi/payment-enrichment-service

Click "⚙️" next to "About" section and add:
- java
- spring-boot
- aws
- microservices
- redis
- docker
- payment-processing
- fintech
- dynamodb
- rest-api
- ci-cd

### Update Repository Description

Set description on GitHub:
```
Production-ready payment data enrichment microservice built with 
Java 17, Spring Boot 3.2, AWS (DynamoDB, S3), Redis caching, 
comprehensive testing, and CI/CD
```

---

## 📊 Enable GitHub Actions

1. Go to: https://github.com/moonbogi/payment-enrichment-service/actions
2. Click "I understand my workflows, go ahead and enable them"
3. Workflows will run automatically on push/PR

---

## 🔐 Important Notes

### Personal GitHub (github.com)
- ✅ Public repository - safe for portfolio
- ✅ Can share on resume/LinkedIn
- ✅ Good for job applications
- ⚠️ Don't commit sensitive data (API keys, passwords)

### Company GitHub (ghe.intra.nudatasecurity.com)
- 🔒 Private/internal to company
- ⚠️ Check company policy before sharing externally
- May contain proprietary information

---

## 📱 Share Your Project

### LinkedIn Post Template

```
🚀 Excited to share my latest project: Payment Data Enrichment Microservice!

Built a production-ready cloud-native service demonstrating:
• Java 17 & Spring Boot 3.2
• AWS integration (DynamoDB, S3)
• Redis caching for <20ms latency
• 80%+ test coverage
• Full CI/CD with GitHub Actions
• Prometheus & Grafana monitoring

Perfect showcase of enterprise microservices architecture for 
fintech and payment processing.

🔗 https://github.com/moonbogi/payment-enrichment-service

#Java #SpringBoot #AWS #Microservices #CloudNative #Fintech
```

### On Your Resume

```
Payment Data Enrichment Service | Java, Spring Boot, AWS
- Architected cloud-native microservice with DynamoDB and Redis
- Achieved <100ms API latency with multi-level caching strategy  
- Implemented CI/CD pipeline with 80%+ test coverage
- Tech: Java 17, Spring Boot 3.2, AWS SDK, Docker, Prometheus
- GitHub: github.com/moonbogi/payment-enrichment-service
```

---

## 🎯 Future Updates

When you make improvements:

```bash
# 1. Make changes
# 2. Test locally
./mvnw clean test

# 3. Commit
git add .
git commit -m "feat: your improvement"

# 4. Push to personal GitHub
git push origin main
```

---

## ✅ Checklist for Portfolio Readiness

- [x] Code pushed to personal GitHub
- [x] README updated with your info
- [x] LICENSE has your name
- [ ] Add repository topics on GitHub
- [ ] Enable GitHub Actions
- [ ] Add LinkedIn URL to README
- [ ] Pin repository on GitHub profile
- [ ] Share on LinkedIn
- [ ] Add to resume

---

## 🆘 Troubleshooting

### Wrong GitHub Account
```bash
# Check which account is active
gh auth status

# Switch to personal GitHub
GH_HOST=github.com gh auth login
```

### Push Fails
```bash
# Verify remote is correct
git remote -v

# Should show: origin  https://github.com/moonbogi/payment-enrichment-service.git

# If wrong, fix it:
git remote set-url origin https://github.com/moonbogi/payment-enrichment-service.git
```

### Conflicts
```bash
# Pull latest changes first
git pull origin main

# Resolve conflicts
# Then commit and push
git push origin main
```

---

**Your repository is live at:** https://github.com/moonbogi/payment-enrichment-service 🎉
