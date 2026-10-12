package com.sleepywang.xiangyin.dialect.util;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sleepywang.xiangyin.dialect.vo.DialectSortVO;

import java.util.ArrayList;
import java.util.List;

public class CommUtils {
    public static<E> IPage<E> List2IPage(Page page,List<E> list)
    {
        long current=Math.max(1L,page.getCurrent());
        long size= Math.min(page.getSize()>0? page.getSize() : 10L, 100L);
        int total=list==null?0:list.size();
        int from = (int) Math.min((current - 1) * size, total);
        int to = (int) Math.min(from + size, total);
        Page<E> pageE=new Page<>(current,size,total);
        pageE.setRecords(new ArrayList<>(list==null?new ArrayList<>():list.subList(from,to)));
        return pageE;
    }

    /**
     * 用于列表filter的like方法
     * @param value
     * @param kw
     * @return
     */
    public static boolean like(String value, String kw) {
        if (StrUtil.isBlank(kw)) return true;                       // 空条件 = 不筛
        if (value == null) return false;
        String v = value.trim().toLowerCase();
        String k = kw.trim().toLowerCase();
        return v.contains(k);                                       // %kw%
        // 前缀匹配用 v.startsWith(k)；后缀用 v.endsWith(k)
    }
//    public static <E>boolean in(E data,List<E>)
}
