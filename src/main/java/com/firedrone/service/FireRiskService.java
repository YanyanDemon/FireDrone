package com.firedrone.service;

public class FireRiskService {

    public String calculateRiskLevel(double temperature, double smoke, double co, int flame) {
        int score = 0;

        if (temperature > 45) {
            score += 2;
        }

        if (temperature > 60) {
            score += 3;
        }

        if (smoke > 300) {
            score += 2;
        }

        if (smoke > 600) {
            score += 3;
        }

        if (co > 80) {
            score += 2;
        }

        if (co > 150) {
            score += 3;
        }

        if (flame == 1) {
            score += 5;
        }

        if (score < 3) {
            return "正常";
        } else if (score < 6) {
            return "预警";
        } else if (score < 9) {
            return "危险";
        } else {
            return "紧�?;
        }
    }
}