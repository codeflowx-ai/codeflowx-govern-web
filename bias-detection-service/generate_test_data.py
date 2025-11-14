"""
Generate Test Data for Bias Detection Service
Creates CSV files with varying levels of bias for testing
"""

import pandas as pd
import numpy as np
from pathlib import Path

def generate_biased_data(
    n_samples: int = 1000,
    bias_level: str = "moderate",
    protected_attr_name: str = "gender",
    output_file: str = None
) -> pd.DataFrame:
    """
    Generate synthetic biased dataset
    
    Args:
        n_samples: Total number of samples
        bias_level: "none", "low", "moderate", "high", "critical"
        protected_attr_name: Name of protected attribute
        output_file: Output CSV filename
    
    Returns:
        DataFrame with y_true, y_pred, and protected attribute
    """
    np.random.seed(42)
    
    # Split samples between two groups
    n_group1 = n_samples // 2
    n_group2 = n_samples - n_group1
    
    # Define accuracy for each group based on bias level
    bias_config = {
        "none": (0.85, 0.85),      # No bias: same accuracy
        "low": (0.85, 0.80),        # 5% gap
        "moderate": (0.85, 0.70),   # 15% gap
        "high": (0.85, 0.60),       # 25% gap
        "critical": (0.85, 0.50)    # 35% gap
    }
    
    acc_group1, acc_group2 = bias_config.get(bias_level, (0.85, 0.70))
    
    print(f"Generating {bias_level.upper()} bias dataset:")
    print(f"  - Group 1 accuracy: {acc_group1:.0%}")
    print(f"  - Group 2 accuracy: {acc_group2:.0%}")
    print(f"  - Accuracy gap: {abs(acc_group1 - acc_group2):.0%}")
    
    # Generate Group 1 (privileged)
    group1_true = np.random.choice([0, 1], size=n_group1, p=[0.4, 0.6])
    group1_pred = np.where(
        group1_true == 1,
        np.random.choice([0, 1], size=n_group1, p=[1-acc_group1, acc_group1]),
        np.random.choice([0, 1], size=n_group1, p=[acc_group1, 1-acc_group1])
    )
    
    # Generate Group 2 (disadvantaged)
    group2_true = np.random.choice([0, 1], size=n_group2, p=[0.4, 0.6])
    group2_pred = np.where(
        group2_true == 1,
        np.random.choice([0, 1], size=n_group2, p=[1-acc_group2, acc_group2]),
        np.random.choice([0, 1], size=n_group2, p=[acc_group2, 1-acc_group2])
    )
    
    # Combine into DataFrame
    df = pd.DataFrame({
        'y_true': np.concatenate([group1_true, group2_true]),
        'y_pred': np.concatenate([group1_pred, group2_pred]),
        protected_attr_name: ['group_1'] * n_group1 + ['group_2'] * n_group2
    })
    
    # Shuffle
    df = df.sample(frac=1, random_state=42).reset_index(drop=True)
    
    # Save if output file specified
    if output_file:
        df.to_csv(output_file, index=False)
        print(f"✅ Saved to: {output_file}")
    
    return df


def generate_gender_bias_data(bias_level: str = "moderate") -> pd.DataFrame:
    """Generate dataset with gender bias"""
    return generate_biased_data(
        n_samples=1000,
        bias_level=bias_level,
        protected_attr_name="gender",
        output_file=f"test_bias_gender_{bias_level}.csv"
    ).replace({'group_1': 'male', 'group_2': 'female'})


def generate_race_bias_data(bias_level: str = "moderate") -> pd.DataFrame:
    """Generate dataset with race bias"""
    np.random.seed(42)
    n_samples = 1000
    
    # Multiple racial groups with varying performance
    races = ['white', 'black', 'asian', 'hispanic']
    
    bias_config = {
        "none": [0.85, 0.85, 0.85, 0.85],
        "low": [0.85, 0.82, 0.84, 0.83],
        "moderate": [0.85, 0.70, 0.78, 0.75],
        "high": [0.85, 0.60, 0.68, 0.65],
        "critical": [0.85, 0.50, 0.58, 0.55]
    }
    
    accuracies = bias_config.get(bias_level, [0.85, 0.70, 0.78, 0.75])
    samples_per_race = n_samples // len(races)
    
    all_data = []
    
    for race, accuracy in zip(races, accuracies):
        y_true = np.random.choice([0, 1], size=samples_per_race, p=[0.4, 0.6])
        y_pred = np.where(
            y_true == 1,
            np.random.choice([0, 1], size=samples_per_race, p=[1-accuracy, accuracy]),
            np.random.choice([0, 1], size=samples_per_race, p=[accuracy, 1-accuracy])
        )
        
        race_data = pd.DataFrame({
            'y_true': y_true,
            'y_pred': y_pred,
            'race': race
        })
        all_data.append(race_data)
    
    df = pd.concat(all_data, ignore_index=True)
    df = df.sample(frac=1, random_state=42).reset_index(drop=True)
    
    output_file = f"test_bias_race_{bias_level}.csv"
    df.to_csv(output_file, index=False)
    print(f"✅ Generated race bias dataset: {output_file}")
    
    return df


def generate_age_bias_data(bias_level: str = "moderate") -> pd.DataFrame:
    """Generate dataset with age bias"""
    np.random.seed(42)
    n_samples = 1000
    
    # Age groups
    age_groups = ['18-30', '31-45', '46-60', '60+']
    
    bias_config = {
        "none": [0.85, 0.85, 0.85, 0.85],
        "low": [0.85, 0.83, 0.82, 0.80],
        "moderate": [0.85, 0.78, 0.72, 0.68],
        "high": [0.85, 0.70, 0.62, 0.58],
        "critical": [0.85, 0.60, 0.52, 0.48]
    }
    
    accuracies = bias_config.get(bias_level, [0.85, 0.78, 0.72, 0.68])
    samples_per_group = n_samples // len(age_groups)
    
    all_data = []
    
    for age_group, accuracy in zip(age_groups, accuracies):
        y_true = np.random.choice([0, 1], size=samples_per_group, p=[0.4, 0.6])
        y_pred = np.where(
            y_true == 1,
            np.random.choice([0, 1], size=samples_per_group, p=[1-accuracy, accuracy]),
            np.random.choice([0, 1], size=samples_per_group, p=[accuracy, 1-accuracy])
        )
        
        age_data = pd.DataFrame({
            'y_true': y_true,
            'y_pred': y_pred,
            'age_group': age_group
        })
        all_data.append(age_data)
    
    df = pd.concat(all_data, ignore_index=True)
    df = df.sample(frac=1, random_state=42).reset_index(drop=True)
    
    output_file = f"test_bias_age_{bias_level}.csv"
    df.to_csv(output_file, index=False)
    print(f"✅ Generated age bias dataset: {output_file}")
    
    return df


def generate_all_test_datasets():
    """Generate complete test dataset suite"""
    print("=" * 60)
    print("GENERATING TEST DATASETS FOR BIAS DETECTION SERVICE")
    print("=" * 60)
    print()
    
    # Gender bias datasets
    print("📊 GENDER BIAS DATASETS")
    print("-" * 60)
    for level in ["none", "low", "moderate", "high", "critical"]:
        generate_gender_bias_data(level)
        print()
    
    # Race bias datasets
    print("📊 RACE BIAS DATASETS")
    print("-" * 60)
    for level in ["none", "moderate", "high"]:
        generate_race_bias_data(level)
        print()
    
    # Age bias datasets
    print("📊 AGE BIAS DATASETS")
    print("-" * 60)
    for level in ["none", "moderate", "high"]:
        generate_age_bias_data(level)
        print()
    
    print("=" * 60)
    print("✅ ALL TEST DATASETS GENERATED SUCCESSFULLY")
    print("=" * 60)
    print()
    print("Generated files:")
    for file in sorted(Path(".").glob("test_bias_*.csv")):
        print(f"  - {file.name}")


if __name__ == "__main__":
    import sys
    
    if len(sys.argv) > 1:
        bias_level = sys.argv[1]
        if bias_level not in ["none", "low", "moderate", "high", "critical"]:
            print(f"Invalid bias level: {bias_level}")
            print("Valid options: none, low, moderate, high, critical")
            sys.exit(1)
        
        print(f"Generating single dataset with {bias_level} bias...")
        generate_gender_bias_data(bias_level)
    else:
        # Generate all test datasets
        generate_all_test_datasets()

