package com.example.backend.entity;

public class Result {
    private double score;
    private String root;
    private String keyword;

    public Result() {
    }

    public Result(double score, String root, String keyword) {
        this.score = score;
        this.root = root;
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "Result{" +
                "score=" + score +
                ", root='" + root + '\'' +
                ", keyword='" + keyword + '\'' +
                '}';
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
