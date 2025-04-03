package com.sample.music.recommended;

public class SongSimilarity implements Comparable<SongSimilarity> {
    private final Long songId;     // 歌曲ID
    private final double similarity; // 相似度得分（0~1）

    public SongSimilarity(String songId, double sim) {
        this.songId = Long.valueOf(songId);
        this.similarity = sim;
    }

    @Override
    public int compareTo(SongSimilarity other) {
        // 按相似度降序排序
        return Double.compare(this.similarity, other.similarity);
    }

    public String getSongId() {
        return Long.toString(songId);
    }
}