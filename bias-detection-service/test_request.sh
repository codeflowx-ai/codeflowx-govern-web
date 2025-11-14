#!/bin/bash
# AI Governance Platform - Bias Detection Service
# Test Request Script

echo "🧪 Testing Bias Detection Service"
echo "================================="
echo ""

# Configuration
SERVICE_URL="http://localhost:8001"
TEST_DATA_FILE="test_bias_data.csv"

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test 1: Health Check
echo "📊 Test 1: Health Check"
echo "------------------------"
health_response=$(curl -s "${SERVICE_URL}/health")
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Service is running${NC}"
    echo "$health_response" | python3 -m json.tool
else
    echo -e "${RED}❌ Service is not responding${NC}"
    exit 1
fi
echo ""

# Test 2: Generate Test Data
echo "📊 Test 2: Generate Test Data"
echo "-------------------------------"
if [ ! -f "$TEST_DATA_FILE" ]; then
    echo "Generating biased test dataset..."
    python3 - << 'EOF'
import pandas as pd
import numpy as np

np.random.seed(42)

# Male group: higher accuracy (80%)
male_true = np.random.choice([0, 1], size=500, p=[0.4, 0.6])
male_pred = np.where(male_true == 1, 
                     np.random.choice([0, 1], size=500, p=[0.15, 0.85]),
                     np.random.choice([0, 1], size=500, p=[0.85, 0.15]))

# Female group: lower accuracy (70%) - BIASED
female_true = np.random.choice([0, 1], size=500, p=[0.4, 0.6])
female_pred = np.where(female_true == 1,
                       np.random.choice([0, 1], size=500, p=[0.30, 0.70]),
                       np.random.choice([0, 1], size=500, p=[0.70, 0.30]))

df = pd.DataFrame({
    'y_true': np.concatenate([male_true, female_true]),
    'y_pred': np.concatenate([male_pred, female_pred]),
    'gender': ['male'] * 500 + ['female'] * 500
})

df.to_csv('test_bias_data.csv', index=False)
print(f"✅ Created test dataset: {len(df)} rows")
print(f"   - Male samples: {len(df[df['gender']=='male'])}")
print(f"   - Female samples: {len(df[df['gender']=='female'])}")
EOF
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Test data generated successfully${NC}"
    else
        echo -e "${RED}❌ Failed to generate test data${NC}"
        exit 1
    fi
else
    echo -e "${YELLOW}⚠️  Test data already exists${NC}"
fi
echo ""

# Test 3: Analyze Bias
echo "📊 Test 3: Bias Analysis"
echo "-------------------------"
echo "Analyzing bias in test dataset..."
echo ""

response=$(curl -s -X POST "${SERVICE_URL}/api/bias-analysis/analyze" \
  -F "file=@${TEST_DATA_FILE}" \
  -F "model_id=test_model_001" \
  -F "protected_attribute=gender" \
  -F "favorable_outcome=1" \
  -F "threshold=0.8")

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Analysis completed successfully${NC}"
    echo ""
    echo "Results:"
    echo "--------"
    echo "$response" | python3 -m json.tool
    echo ""
    
    # Extract key metrics
    classification=$(echo "$response" | python3 -c "import sys, json; print(json.load(sys.stdin).get('classification', 'N/A'))")
    echo -e "Bias Classification: ${YELLOW}${classification}${NC}"
    
else
    echo -e "${RED}❌ Analysis failed${NC}"
    exit 1
fi
echo ""

# Test 4: Test with Invalid Data
echo "📊 Test 4: Error Handling (Invalid CSV)"
echo "-----------------------------------------"
echo "Testing error handling with invalid data..."

# Create invalid CSV (missing column)
echo "y_true,y_pred" > invalid_test.csv
echo "1,0" >> invalid_test.csv
echo "0,1" >> invalid_test.csv

error_response=$(curl -s -X POST "${SERVICE_URL}/api/bias-analysis/analyze" \
  -F "file=@invalid_test.csv" \
  -F "model_id=test_invalid" \
  -F "protected_attribute=gender")

if echo "$error_response" | grep -q "Missing required columns"; then
    echo -e "${GREEN}✅ Error handling works correctly${NC}"
    echo "Error message:"
    echo "$error_response" | python3 -c "import sys, json; print(json.load(sys.stdin).get('detail', 'N/A'))"
else
    echo -e "${RED}❌ Error handling not working as expected${NC}"
fi

# Cleanup
rm -f invalid_test.csv
echo ""

# Summary
echo "================================="
echo "🎉 All tests completed!"
echo "================================="
echo ""
echo "Next steps:"
echo "1. Review the bias classification above"
echo "2. Check recommendations in the analysis response"
echo "3. Integrate with Java backend using RestTemplate"
echo ""
echo "Service URL: ${SERVICE_URL}"
echo "Interactive docs: ${SERVICE_URL}/docs"

