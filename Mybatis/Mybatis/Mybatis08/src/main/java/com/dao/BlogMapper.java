package com.dao;

import com.pojo.Blog;

import java.util.List;
import java.util.Map;

public interface BlogMapper {
    //  插入数据
    int addBook(Blog blog);

    List<Blog> queryBlogIF(Map map);


    List<Blog> queryBlogChoose(Map map);

    //    更新博客
    int updateBlog(Map map);


    // 查询第1-2-3号记录的信息
    List<Blog> queryBolgForeach(Map map);


}
