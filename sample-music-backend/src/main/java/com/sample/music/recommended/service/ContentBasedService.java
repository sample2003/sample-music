package com.sample.music.recommended.service;

import com.sample.music.recommended.mapper.SongFeatureMapper;
import com.sample.music.recommended.SongSimilarity;
import com.sample.music.recommended.pojo.SongFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Service
@RequiredArgsConstructor
public class ContentBasedService {

    private final SongFeatureMapper songRepo;

    // 计算余弦相似度
    private double cosineSimilarity(double[] a, double[] b) {
        double dotProduct = 0.0, normA = 0.0, normB = 0.0;
        for (int i = 0; i < a.length; i++) {
            dotProduct += a[i] * b[i];
            normA += Math.pow(a[i], 2);
            normB += Math.pow(b[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public SongFeature ss(Long id) {
        return songRepo.findById(id);
    }

    // 获取相似歌曲推荐
    public List<String> recommend(Long targetSongId, int topN) {
        SongFeature target = songRepo.findById(targetSongId);
        List<SongFeature> allSongs = songRepo.findAll();

        PriorityQueue<SongSimilarity> heap = new PriorityQueue<>(Comparator.reverseOrder());
        for (SongFeature song : allSongs) {
            double sim = cosineSimilarity(target.getMfcc(), song.getMfcc());
            heap.add(new SongSimilarity(song.getId(), sim));
        }

        List<String> recommendations = new ArrayList<>();
        for (int i = 0; i < topN && !heap.isEmpty(); i++) {
            recommendations.add(heap.poll().getSongId());
        }
        return recommendations;
    }
}