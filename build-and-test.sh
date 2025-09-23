#!/bin/bash

# Student Management System - Build and Test Script
# This script builds both backend and frontend components

set -e  # Exit on any error

echo "🎓 Student Management System - Build & Test"
echo "============================================="

# Check prerequisites
echo "📋 Checking prerequisites..."

# Check Java
if ! command -v java &> /dev/null; then
    echo "❌ Java not found. Please install Java 21."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n1 | cut -d'"' -f2)
echo "✅ Java version: $JAVA_VERSION"

# Check Node.js
if ! command -v node &> /dev/null; then
    echo "❌ Node.js not found. Please install Node.js 18+."
    exit 1
fi

NODE_VERSION=$(node --version)
echo "✅ Node.js version: $NODE_VERSION"

# Check npm
if ! command -v npm &> /dev/null; then
    echo "❌ npm not found. Please install npm."
    exit 1
fi

NPM_VERSION=$(npm --version)
echo "✅ npm version: $NPM_VERSION"

echo ""

# Build Backend
echo "🏗️  Building Java Backend..."
echo "=============================="

export JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64

# Run tests first
echo "🧪 Running backend tests..."
./gradlew test --no-daemon

# Build JAR
echo "📦 Building backend JAR..."
./gradlew build --no-daemon

echo "✅ Backend build completed!"
echo ""

# Build Frontend
echo "🎨 Building React Frontend..."
echo "=============================="

cd frontend

# Install dependencies if node_modules doesn't exist
if [ ! -d "node_modules" ]; then
    echo "📦 Installing frontend dependencies..."
    npm install
fi

# Build frontend
echo "🏗️  Building React application..."
npm run build

echo "✅ Frontend build completed!"
echo ""

# Integration Test
echo "🔗 Testing Integration..."
echo "========================="

cd ..

# Start backend in background
echo "🚀 Starting backend server..."
export JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64
./gradlew bootRun --no-daemon &
BACKEND_PID=$!

# Wait for backend to start
echo "⏳ Waiting for backend to start..."
sleep 10

# Test API endpoints
echo "🔍 Testing API endpoints..."

# Test health check
if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
    echo "✅ Backend health check passed"
else
    echo "❌ Backend health check failed"
    kill $BACKEND_PID
    exit 1
fi

# Test students endpoint
if curl -f http://localhost:8080/api/students > /dev/null 2>&1; then
    echo "✅ Students API endpoint accessible"
else
    echo "❌ Students API endpoint failed"
    kill $BACKEND_PID
    exit 1
fi

# Test statistics endpoint
if curl -f http://localhost:8080/api/statistics/summary > /dev/null 2>&1; then
    echo "✅ Statistics API endpoint accessible"
else
    echo "❌ Statistics API endpoint failed"
    kill $BACKEND_PID
    exit 1
fi

# Clean up
echo "🧹 Cleaning up..."
kill $BACKEND_PID
sleep 2

echo ""
echo "🎉 Build and Test Completed Successfully!"
echo "========================================"
echo ""
echo "📚 Next Steps:"
echo "  1. Start the application:"
echo "     cd frontend && npm start"
echo ""
echo "  2. Or run components separately:"
echo "     Backend:  ./gradlew bootRun"
echo "     Frontend: cd frontend && npm run dev"
echo ""
echo "  3. Create distribution packages:"
echo "     cd frontend && npm run dist"
echo ""
echo "📖 See README-ARCHITECTURE.md for detailed documentation"