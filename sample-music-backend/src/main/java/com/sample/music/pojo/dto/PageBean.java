package com.sample.music.pojo.dto;

import lombok.Data;

import java.util.List;

//分页返回结果对象
@Data
public class PageBean<T>{
    private int total;//总条数
    private List<T> items;//当前页数据集合
}
