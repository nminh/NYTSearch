package com.nminh.nytsearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nminh on 6/24/17.
 */

public class SearchRequest implements Parcelable {

    private int page;
    private String query;
    private String beginDate;
    private String endDate;
    private String order = "newest";
    private boolean hasArts;
    private boolean hasFashionAndStyle;
    private boolean hasSports;


    public SearchRequest() {
    }

    protected SearchRequest (Parcel in) {
        page = in.readInt();
        query = in.readString();
        beginDate = in.readString();
        endDate = in.readString();
        order = in.readString();
        hasArts = in.readByte() != 0;
        hasFashionAndStyle = in.readByte() != 0;
        hasSports = in.readByte() != 0;
    }

    public int getPage() {
        return page;
    }

    public String getQuery() {
        return query;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getOrder() {
        return order;
    }

    public boolean isHasArts() {
        return hasArts;
    }

    public boolean isHasFashionAndStyle() {
        return hasFashionAndStyle;
    }

    public boolean isHasSports() {
        return hasSports;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setHasArts(boolean hasArts) {
        this.hasArts = hasArts;
    }

    public void setHasFashionAndStyle(boolean hasFashionAndStyle) {
        this.hasFashionAndStyle = hasFashionAndStyle;
    }

    public void setHasSports(boolean hasSports) {
        this.hasSports = hasSports;
    }

    public void resetPage() { page = 0; }


    public Map<String, String> toQueryMap() {
        Map<String, String> keyValueMap = new HashMap<>();
        if (query != null) {
            keyValueMap.put("q", query);
        }

        if (beginDate != null) { keyValueMap.put("begin_date", beginDate); }
        if (endDate != null) { keyValueMap.put("end_date", endDate); }
        if (order != null) { keyValueMap.put("order", order.toLowerCase()); }

        //for fq=news_desk:("Education" "Health")
        if (getNewsDesk() != null ) keyValueMap.put("fq", "news_desk=(" + getNewsDesk() + ")");
        keyValueMap.put("page", String.valueOf(page));

        return keyValueMap;
    }

    public static final Creator<SearchRequest> CREATOR = new Creator<SearchRequest>() {
        @Override
        public SearchRequest createFromParcel(Parcel in) {
            return new SearchRequest(in);
        }

        @Override
        public SearchRequest[] newArray(int size) {
            return new SearchRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(page);
        out.writeString(query);
        out.writeString(beginDate);
        out.writeString(endDate);
        out.writeString(order);
        out.writeByte((byte) (hasArts ? 1 : 0));
        out.writeByte((byte) (hasFashionAndStyle ? 1 : 0));
        out.writeByte((byte) (hasSports ? 1 : 0));
    }



    // Generate categories
    private String getNewsDesk() {

        if (!hasArts && !hasSports && !hasFashionAndStyle) return null;
        else {
            String value = "";
            if (hasArts) value += "\"Arts\" ";
            if (hasFashionAndStyle) value += "\"Fashion & Style\" ";
            if (hasSports) value += "\"Sports\" ";
            return value.trim(); // cuts spaces from sides of the string
        }
    }

    public void nextPage() {
        page += 1;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
