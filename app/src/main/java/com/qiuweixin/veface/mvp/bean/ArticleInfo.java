package com.qiuweixin.veface.mvp.bean;


import com.alibaba.fastjson.annotation.JSONField;

public class ArticleInfo {
    // 文章Id
    @JSONField(name = "id")
    private String id;

    // 广告Id
    @JSONField(name = "ad_id")
    private Integer ad_id;

    // 文章Id（用于删除文章）
    @JSONField(name = "article_id")
    private String article_id;

    // 分享Id（在文章代表一条分享记录有值）
    @JSONField(name = "article_share_link_id")
    private String shareId;
    // 收藏Id（在文章代表一条收藏记录有值）
    @JSONField(name = "collect_id")
    private String collectionId;
    @JSONField(name = "promotion")
    private boolean promotion;
    @JSONField(name = "article_type")
    private String articleType;
    @JSONField(name = "img_src")
    private String image;
    @JSONField(name = "title")
    private String title;
    @JSONField(name = "pubtime")
    private long date; //文章发布时间
    @JSONField(name = "add_time")
    private long add_time;// 用于分享 收藏 原创列表的文章发布时间
    @JSONField(name = "view_num")
    private long readCount;
    @JSONField(name = "url")
    private String url;
    //文章ack_code码
    @JSONField(name = "ack_code")
    private String ack_code;
    //来源公众号
    @JSONField(name = "src_name")
    private String src_name;

    private long likeCount;

    private long presentTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAd_id() {
        return ad_id;
    }

    public void setAd_id(Integer ad_id) {
        this.ad_id = ad_id;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public long getAdd_time() {
        return add_time;
    }

    public void setAdd_time(long add_time) {
        this.add_time = add_time;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public boolean isPromotion() {
        return promotion;
    }

    public void setPromotion(boolean promotion) {
        this.promotion = promotion;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getReadCount() {
        return readCount;
    }

    public void setReadCount(long readCount) {
        this.readCount = readCount;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public String getUrl() {
        return url;
    }

    public String getAck_code() {
        return ack_code;
    }

    public void setAck_code(String ack_code) {
        this.ack_code = ack_code;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSrc_name() {
        return src_name;
    }

    public void setSrc_name(String src_name) {
        this.src_name = src_name;
    }


    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public long getPresentTime() {
        return presentTime;
    }

    public void setPresentTime(long presentTime) {
        this.presentTime = presentTime;
    }
}
