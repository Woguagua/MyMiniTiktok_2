package com.ch.doudemo.recycler;

import java.util.ArrayList;
import java.util.List;

public class DataSet {
    public static List<Data> getData() {
        List<Data> result = new ArrayList();
        result.add(new Data("@惊奇队长", "更高，更远，更快","10.3w","1.5w"));
        result.add(new Data("@博纳电影集团", "#黄金律条 昆丁·塔伦蒂诺的第九部佳作","153w","10.3w"));
        result.add(new Data("@紧急救援", "#英雄 每一个英雄，背后都藏着恐惧","25.8w","2.5w"));
        result.add(new Data("@迪士尼电影", "可能是今年最好看的动画","4.1w","9025"));
        result.add(new Data("@东方影业", "#叶问 #十年经典，宗师回归 我是习武之人，看到不公平的事，一定要站出来","31.1w","3.2w"));
        result.add(new Data("@异格传媒", "#扫毒 黑暗世界，宿命对决","33.2w","2.9w"));
        result.add(new Data("@寰亚电影", "#无限动力 希望，能点燃声明；信念，能改变人生","2.4w","5033"));
        result.add(new Data("@拉近影业", "#花椒之味 看见平凡","15.5w","1.9w"));
        return result;
    }
}
