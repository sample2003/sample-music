package com.sample.music.recommended.mapper;

import com.sample.music.recommended.pojo.SongFeature;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SongFeatureMapper {
    // 查询全部歌曲
    @Select("select s from song s")
    List<SongFeature> findAll();
    // 根据id查询
    @Select("select * from song where id = #{songId}")
    SongFeature findById(@Param("songId") Long songId);
    // 根据风格查询
    @Select("select s from song s where s.style = #{style}")
    List<SongFeature> findByStyle(String style);

}