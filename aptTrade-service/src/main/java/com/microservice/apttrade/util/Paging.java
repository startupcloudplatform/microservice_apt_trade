package com.microservice.apttrade.util;


public class Paging {
    public int wSize = 5; // 글 개수
    public int pSize = 5;  // 페이지 개수
    public int totalCount = 0;
    public int currentPage = 0;

    public Paging(int wSize, int pSize, int totalCount, int currentPage) {
        this.wSize = wSize;
        this.pSize = pSize;
        this.totalCount = totalCount;
        this.currentPage = currentPage;
    }

    public Paging (int wSize, int totalCount) {
        this.wSize = wSize;
        this.totalCount = totalCount;
    }


    public int getPageCount() {
        return ((totalCount - 1) / wSize) + 1;
    }

    public int getPageStart() {

        return ((currentPage - 1) / pSize) * pSize + 1;
    }

    public int getPageEnd() {
        return Math.min(getPageStart() + pSize - 1, getPageCount());
    }

    public boolean isPre() {
        return getPageStart() != 1;
    }

    public boolean isNext() {
        return getPageEnd() < getPageCount();
    }

    public int getWritingStart() {
        return getWritingEnd() - wSize + 1;
    }

    public int getWritingEnd() {
        return currentPage * wSize;
    }
}

