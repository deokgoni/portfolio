package com.gon.webservice.controller;

import com.gon.webservice.domain.item.Book;
import com.gon.webservice.domain.item.Item;
import com.gon.webservice.dto.BookDto;
import com.gon.webservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createFormd(Model model){
        model.addAttribute("bookDto", new BookDto());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(@Valid BookDto form, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            log.info("error = {}",bindingResult);
            return "items/createItemForm";
        }
        Book book = new Book(form.getId(), form.getName(),form.getPrice(),form.getStockQuantity(),form.getAuthor(),form.getIsbn());
        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items",items);
        return "items/itemList";
    }

    @GetMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
        Book item = (Book) itemService.findOne(itemId);
        BookDto form = new BookDto(item);
        model.addAttribute("form",form);
        return "items/updateItemForm";
    }

    @PostMapping("items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookDto form){
        Book book = new Book(form.getId(), form.getName(),form.getPrice(),form.getStockQuantity(),form.getAuthor(),form.getIsbn());
        log.info("update bookForm : {}", book);
        itemService.saveItem(book);
        return "redirect:/items";
    }

}
