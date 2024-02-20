package com.controller;

import com.pojo.Books;
import com.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {

    /*
     * Controller 调 Service 层
     * */
    @Autowired
    @Qualifier("BookServiceImpl")
    private BookService bookService;

    //  查新全部书籍，并且返回到一个书籍展示页面
    @RequestMapping("/allBook")
    public String list(Model model) {
        List<Books> list = bookService.queryAllBook();
        model.addAttribute("list", list);
        return "allBook";
    }


    //  跳转到增加书籍页面
    @RequestMapping("/toAddBook")
    public String toAddPage() {
        return "addBook";
    }

    //  添加书籍的请求
    @RequestMapping("/addBook")
    public String addBook(Books books) {
        bookService.addBook(books);
        //  重定向
        return "redirect:/book/allBook";
    }


    //  跳转到修改页面
    @RequestMapping("/tuUpdateBook")
    public String toUpdatePage(int id, Model model) {
        Books books = bookService.queryBookById(id);
        model.addAttribute("Books", books);
        return "updateBook";
    }

    //  数据修改
    @RequestMapping("/updateBook")
    public String updateBoo(Books books) {
        bookService.updateBook(books);
        return "redirect:/book/allBook";
    }

    @RequestMapping("/deleteBook")
    public String deleteBook( int id) {
        bookService.deleteBookById(id);
        return "redirect:/book/allBook";
    }


    //  查询书籍
    @RequestMapping("/queryBook")
    public String queryBook(String queryBookName, Model model) {
        Books books = bookService.queryBookByName(queryBookName);
        List<Books> list = new ArrayList<>();
        list.add(books);


        if (books == null) {
            list = bookService.queryAllBook();
            model.addAttribute("error", "未查到");
        }
        model.addAttribute("list", list);
        return "allBook";
    }

}
